package com.cfc.makingitbettergame.GameClasses.Miscellaneous;

import com.cfc.makingitbettergame.GameClasses.BoardClasses.EventSquare;
import com.cfc.makingitbettergame.GameClasses.BoardClasses.Square;
import com.cfc.makingitbettergame.GameClasses.BoardClasses.SquareLinkedList;
import com.cfc.makingitbettergame.GameClasses.BoardClasses.SquareNode;
import com.cfc.makingitbettergame.GameClasses.ObjectiveRelatedClasses.Objective;
import com.cfc.makingitbettergame.GameClasses.ObjectiveRelatedClasses.Task;
import com.cfc.makingitbettergame.GameClasses.PlayerClasses.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class GameController {
    public enum GameState {
        Won,
        Lost,
        Ongoing
    }

    private List<Player> playersList;
    private List<Objective> objectivesList;
    private Resources resourcePool;
    private SquareLinkedList board;
    private Objective activeObjective;
    private Player activePlayer;
    private int turnCounter = 1;
    private GameState gameState = GameState.Ongoing;
    private Boolean negateLaborCost = false;
    private Boolean negateNegativeSquareEvent = false;
    private Boolean negateObjectiveCompletion = false;
    private Boolean donationOfMaterialsDiscountApplied = false;
    private int negateObjectiveCompletionTurnCount;
    private int dieRollValue = GameSettings.getInstance().getDieFacesNumber();
    private Boolean coinFlipValue;
    private float playerAnimationTime = .5f;
    private Random randomSeed = new Random();

    public GameController(List<Objective> objectivesList, Resources resourcePool, SquareLinkedList board) {
        this.playersList = new ArrayList<>();
        this.objectivesList = new ArrayList<>();

        if (!objectivesList.isEmpty()) {
            this.objectivesList.addAll(objectivesList);
            this.activeObjective = objectivesList.getFirst();
        }

        this.resourcePool = resourcePool;
        this.board = board;
    }

    public List<Player> getPlayersList() {
        return playersList;
    }
    public List<Objective> getObjectivesList() {
        return objectivesList;
    }
    public Player getActivePlayer() {
        return activePlayer;
    }
    public Objective getActiveObjective() {
        return activeObjective;
    }
    public Resources getResourcePool() {
        return resourcePool;
    }
    public SquareLinkedList getBoard() {
        return board;
    }
    public int getTurnCounter() {
        return turnCounter;
    }
    public Boolean getNegateObjectiveCompletion() {
        return negateObjectiveCompletion;
    }
    public Boolean getNegateNegativeSquareEvent() {
        return negateNegativeSquareEvent;
    }
    public Boolean getDonationOfMaterialsDiscountApplied() { return donationOfMaterialsDiscountApplied; }
    public void negativeSquareEventNegated() {
        negateNegativeSquareEvent = false;
    }

    public void nextTurn() {
        turnCounter++;
        activePlayer = playersList.get((turnCounter - 1) % playersList.size());

        if (negateObjectiveCompletion && turnCounter > negateObjectiveCompletionTurnCount + 1)
            negateObjectiveCompletion = false;

        if (turnCounter % playersList.size() == 1) {
            if (!negateLaborCost)
                resourcePool.modifyResource(Resources.ResourceTypes.Money, resourcePool.getAllResourceTypesQuantity().get(Resources.ResourceTypes.Labour)
                        * GameSettings.getInstance().getMoneyChargePerLabourUnit(), false);

            negateLaborCost = false;
        }
    }
    public GameState getGameState() {
        int completedObjectivesCounter = 0;

        for (Objective objective : objectivesList)
            if (objective.getIsCompleted())
                completedObjectivesCounter++;

        if (completedObjectivesCounter == objectivesList.size())
            gameState = GameState.Won;

        for (Resources.ResourceTypes resourceType : Resources.ResourceTypes.values())
            if (resourcePool.getAllResourceTypesQuantity().get(resourceType) < 0)
                gameState = GameState.Lost;

        return gameState;
    }
    public Resources getActiveObjectiveResourceCostWithDiscount() {
        Resources resourcesCostWithDiscount = new Resources();

            for (Player player : playersList) {
                for (Task task : player.getCollectedTasks()) {
                    if (task.getPlayerExpertiseBenefactorsHashMap().containsKey(player.getPlayerExpertise()))
                        resourcesCostWithDiscount.modifyAllResources(task.getResourcesCost().applyDiscountToAll(task.getPlayerExpertiseBenefactorsHashMap().get(player.getPlayerExpertise())), true);
                    else
                        resourcesCostWithDiscount.modifyAllResources(task.getResourcesCost(), true);
                }
            }

        return resourcesCostWithDiscount;
    }
    public Boolean tryCompleteActiveObjective() {
        return activeObjective.getAllTasksCollected() && !activeObjective.getIsCompleted() && resourcePool.has(getActiveObjectiveResourceCostWithDiscount());
    }
    public Boolean completeActiveObjective() {
        if (tryCompleteActiveObjective()) {
            resourcePool.modifyAllResources(getActiveObjectiveResourceCostWithDiscount(), false);
            activeObjective.isCompleted();
            nextObjective();

            for (Player player : playersList) {
                player.resetMorale(GameSettings.getInstance().getDefaultMoraleQuantity());
                player.resetCanTransferOrReceiveTask();
            }

            return true;
        }

        return false;
    }
    private void nextObjective() {
        if (activeObjective != objectivesList.getLast()) {
            activeObjective = objectivesList.get(objectivesList.indexOf(activeObjective) + 1);
            updateTaskSquares();

            for (Player player : playersList)
                player.getCollectedTasks().clear();
        }
    }
    private void updateTaskSquares() {
        SquareNode currentSquare = board.getStartSquare().getNextSquare();

        for (int i = 0; i < board.getSquareCounter(); i++) {
            if (currentSquare.getSquare().getSquareType() == Square.SquareType.TaskSquare)
                currentSquare.getTaskSquare().setTask(null);

            currentSquare = currentSquare.getNextSquare();
        }

        currentSquare = board.getStartSquare().getNextSquare();

        while (!allTaskSquaresUpdated()) {
            if (currentSquare.getSquare().getSquareType() == Square.SquareType.TaskSquare) {
                int roll = randomSeed.nextInt(3);

                if (!activeObjective.getTaskList().get(roll).getAssignedToSquare() && currentSquare.getTaskSquare().getTask() == null) {
                    currentSquare.getTaskSquare().setTask(activeObjective.getTaskList().get(roll));
                    activeObjective.getTaskList().get(roll).isAssignedToSquare();
                    currentSquare.getTaskSquare().setDescription(activeObjective.getTaskList().get(roll).getDescription());
                }

                if (activeObjective.getTaskList().get(0).getAssignedToSquare() && activeObjective.getTaskList().get(1).getAssignedToSquare()
                        && activeObjective.getTaskList().get(2).getAssignedToSquare() && currentSquare.getTaskSquare().getTask() == null) {
                    currentSquare.getTaskSquare().setTask(activeObjective.getTaskList().get(roll));
                    currentSquare.getTaskSquare().setDescription(activeObjective.getTaskList().get(roll).getDescription());
                }
            }

            currentSquare = currentSquare.getNextSquare();
        }
    }
    private Boolean allTaskSquaresUpdated() {
        SquareNode currentSquare = board.getStartSquare().getNextSquare();

        for (int i = 0; i < board.getSquareCounter(); i++) {
            if (currentSquare.getSquare().getSquareType() == Square.SquareType.TaskSquare)
                if (currentSquare.getTaskSquare().getTask() == null)
                    return false;

            currentSquare = currentSquare.getNextSquare();
        }

        return true;
    }
    public void rollDie() {
        dieRollValue = randomSeed.nextInt(GameSettings.getInstance().getDieFacesNumber()) + 1;
    }
    public int getDieRollValue() {
        return dieRollValue;
    }
    public void flipCoin() { coinFlipValue = randomSeed.nextBoolean(); }
    public Boolean getCoinFlipValue() { return coinFlipValue; }
    public void addPlayer(Player player) {
        player.setPlayerBoardPosition(board.getStartSquare());
        if (playersList.isEmpty())
            activePlayer = player;

        playersList.add(player);
    }

    public void animatePlayerMovement(Boolean forwards){
        float playerRelativeToSquareX = activePlayer.getPlayerIcon().getX() - activePlayer.getPlayerBoardPosition().getSquare().getSquareCoordinates().getX();
        float playerRelativeToSquareY = activePlayer.getPlayerIcon().getY() - activePlayer.getPlayerBoardPosition().getSquare().getSquareCoordinates().getY();

        SquareNode iconSquarePosition = activePlayer.getPlayerBoardPosition();

        if (forwards) {
            activePlayer.getPlayerIcon().addAction(moveTo(
                    iconSquarePosition.getNextSquare().getSquare().getSquareCoordinates().getX() +
                            playerRelativeToSquareX, iconSquarePosition.getNextSquare().getSquare().getSquareCoordinates().getY() +
                            playerRelativeToSquareY, playerAnimationTime));

            iconSquarePosition = iconSquarePosition.getNextSquare();
        }
        else {
            activePlayer.getPlayerIcon().addAction(moveTo(
                    iconSquarePosition.getNextSquare().getSquare().getSquareCoordinates().getX() +
                            playerRelativeToSquareX, iconSquarePosition.getPreviousSquare().getSquare().getSquareCoordinates().getY() +
                            playerRelativeToSquareY, playerAnimationTime));

            iconSquarePosition = iconSquarePosition.getPreviousSquare();
        }
    }
    public Boolean containsPlayerName(String playerName) {
        for (Player player : playersList) {
            if (player.getPlayerName().matches(playerName))
                return true;
        }
        return false;
    }
    public Boolean containsPlayerExpertise(Player.PlayerExpertise playerExpertise) {
        for (Player player : playersList) {
            if (player.getPlayerExpertise() == playerExpertise)
                return true;
        }
        return false;
    }
    public void removeLastPlayer() {
        if (!playersList.isEmpty())
            playersList.removeLast();
    }
    public void handleEventSquare() {
        if (activePlayer.getPlayerBoardPosition().getSquare().getSquareType() == Square.SquareType.PositiveEventSquare) {
            EventSquare eventSquare = activePlayer.getPlayerBoardPosition().getEventSquare();

            switch (eventSquare.getEvent().getEventType()) {
                case CommunityVolunteerDay:
                    negateLaborCost = true;
                    break;
                case GovernmentGrant:
                case LocalBusinessSponsorships:
                    Resources resourcesTemp = new Resources();

                    for (Resources.ResourceTypes resourceType : Resources.ResourceTypes.values())
                        resourcesTemp.modifyResource(resourceType, eventSquare.getEvent().getResourcesChangeValues().getFirst().getAllResourceTypesQuantity().get(resourceType)
                                , true);

                    resourcesTemp.modifyResource(Resources.ResourceTypes.Money, resourcesTemp.getAllResourceTypesQuantity().get(Resources.ResourceTypes.Money)
                            * (resourcePool.getAllResourceTypesQuantity().get(Resources.ResourceTypes.Reputation) - 1), true);
                    resourcePool.modifyAllResources(resourcesTemp, true);
                    break;
                case DonationOfMaterials:
                    if (resourcePool.getAllResourceTypesQuantity().get(Resources.ResourceTypes.CommunitySupport) >=
                            GameSettings.getInstance().getMinimalCommunitySupportForDonationOfMaterials() && !donationOfMaterialsDiscountApplied) {
                        objectivesList.get(0).getTaskList().get(1).getResourcesCost().applyDiscountToAll(GameSettings.getInstance().getDonationOfMaterialsDiscount());
                        donationOfMaterialsDiscountApplied = true;
                    }
                    break;
                default:
                    resourcePool.modifyAllResources(eventSquare.getEvent().getResourcesChangeValues().getFirst(), true);
            }
        }

        if (activePlayer.getPlayerBoardPosition().getSquare().getSquareType() == Square.SquareType.NegativeEventSquare) {
            EventSquare eventSquare = activePlayer.getPlayerBoardPosition().getEventSquare();

                switch (eventSquare.getEvent().getEventType()) {
                    case Drought:
                        negateObjectiveCompletion = true;
                        negateObjectiveCompletionTurnCount = turnCounter;

                        resourcePool.modifyAllResources(eventSquare.getEvent().getResourcesChangeValues().getFirst(), false);
                        break;
                    case WorkersHoliday:
                        resourcePool.modifyResource(Resources.ResourceTypes.Money, resourcePool.getAllResourceTypesQuantity().get(Resources.ResourceTypes.Labour)
                                * GameSettings.getInstance().getMoneyChargePerLabourUnit(), false);
                        break;
                    case LowAwarenessOpposition:
                        if (resourcePool.getAllResourceTypesQuantity().get(Resources.ResourceTypes.CommunitySupport) == 0)
                            resourcePool.modifyAllResources(eventSquare.getEvent().getResourcesChangeValues().getFirst(), false);
                        break;
                    default:
                        resourcePool.modifyAllResources(eventSquare.getEvent().getResourcesChangeValues().getFirst(), false);
                }
        }

        if (resourcePool.getAllResourceTypesQuantity().get(Resources.ResourceTypes.CommunitySupport) > 4)
            resourcePool.modifyResource(Resources.ResourceTypes.CommunitySupport,
                    resourcePool.getAllResourceTypesQuantity().get(Resources.ResourceTypes.CommunitySupport) - 4, false);
    }
    public void handleEventSquareWithChoice(Boolean yes) {
        if (activePlayer.getPlayerBoardPosition().getSquare().getSquareType() == Square.SquareType.PositiveEventSquare) {
            EventSquare eventSquare = activePlayer.getPlayerBoardPosition().getEventSquare();

            switch (eventSquare.getEvent().getEventType()) {
                case TechnicalTrainingWorkshop:
                    negateNegativeSquareEvent = true;

                case InnovativeWaterSavingTechniquesWorkshop:
                case VillageFair:
                    if (yes) {
                        resourcePool.modifyResource(Resources.ResourceTypes.Money,
                                eventSquare.getEvent().getResourcesChangeValues().getLast().getAllResourceTypesQuantity().get(Resources.ResourceTypes.Money), false);

                        for (Resources.ResourceTypes resourceType : Resources.ResourceTypes.values()) {
                            if (resourceType != Resources.ResourceTypes.Money)
                                resourcePool.modifyResource(resourceType,
                                        eventSquare.getEvent().getResourcesChangeValues().getLast().getAllResourceTypesQuantity().get(resourceType), true);
                        }
                    }
                    break;
            }
        }

        if (activePlayer.getPlayerBoardPosition().getSquare().getSquareType() == Square.SquareType.NegativeEventSquare) {
            EventSquare eventSquare = activePlayer.getPlayerBoardPosition().getEventSquare();
                switch (eventSquare.getEvent().getEventType()) {
                    case EquipmentBroken:
                    case WorkerInjury:
                        if (yes)
                            resourcePool.modifyAllResources(eventSquare.getEvent().getResourcesChangeValues().getLast(), false);
                        else
                            resourcePool.modifyAllResources(eventSquare.getEvent().getResourcesChangeValues().getFirst(), false);
                        break;
                }
        }
        if (resourcePool.getAllResourceTypesQuantity().get(Resources.ResourceTypes.CommunitySupport) > 4)
            resourcePool.modifyResource(Resources.ResourceTypes.CommunitySupport,
                    resourcePool.getAllResourceTypesQuantity().get(Resources.ResourceTypes.CommunitySupport) - 4, false);
    }
}
