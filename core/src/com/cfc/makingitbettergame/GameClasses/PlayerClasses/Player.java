package com.cfc.makingitbettergame.GameClasses.PlayerClasses;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.cfc.makingitbettergame.GameClasses.BoardClasses.SquareNode;
import com.cfc.makingitbettergame.GameClasses.Miscellaneous.GameSettings;
import com.cfc.makingitbettergame.GameClasses.ObjectiveRelatedClasses.Task;

import java.util.ArrayList;
import java.util.List;

public class Player {
    public enum PlayerExpertise {
        Manager,
        CivilEngineer,
        ChemicalEngineer,
        CommunityLeader,
        Salesperson,
        LuckyPerson,
        EnvironmentalScientist
    }

    private String name;
    private PlayerExpertise playerExpertise;
    private SquareNode playerBoardPosition;
    private int morale = GameSettings.getInstance().getDefaultMoraleQuantity();
    private Image playerIcon;
    private List<Task> collectedTasks;
    private Boolean canTransferOrReceiveTask = true;

    public Player(String name, PlayerExpertise playerExpertise, Image playerIcon) {
        this.name = name;
        this.playerExpertise = playerExpertise;
        this.playerIcon = playerIcon;
        collectedTasks = new ArrayList<>();
    }

    public String getPlayerName() {
        return name;
    }
    public void resetCanTransferOrReceiveTask() { canTransferOrReceiveTask = true; }
    public PlayerExpertise getPlayerExpertise() {
        return playerExpertise;
    }
    public int getMorale() {
        return morale;
    }
    public void resetMorale(int morale) {
        this.morale = morale;
    }
    public SquareNode getPlayerBoardPosition() {
        return playerBoardPosition;
    }
    public Image getPlayerIcon() {
        return playerIcon;
    }
    public void setPlayerBoardPosition(SquareNode playerBoardPosition) {
        this.playerBoardPosition = playerBoardPosition;
    }
    public void collectTask(Task task) {
        if (tryCollectTask(task)) {
            collectedTasks.add(task);
            morale -= task.getMoraleCost();
            task.isCollected();
        }
    }
    private void forceCollectTask(Task task) {
        collectedTasks.add(task);
    }
    public List<Task> getCollectedTasks() {
        return collectedTasks;
    }
    public Boolean transferTask(Player player) {
        Task taskToTransfer = null;

        for (int i = 0; i < collectedTasks.size(); i++)
            if (collectedTasks.get(i).getCanBeTransferred())
                taskToTransfer = collectedTasks.get(i);

        if (taskToTransfer == null)
            return false;

        if (collectedTasks.contains(taskToTransfer) && !player.getCollectedTasks().contains(taskToTransfer) && player.getMorale() >= taskToTransfer.getMoraleCost()
                && canTransferOrReceiveTask && player.canTransferOrReceiveTask) {
            collectedTasks.remove(taskToTransfer);
            player.forceCollectTask(taskToTransfer);
            player.resetMorale(player.getMorale() - taskToTransfer.getMoraleCost());
            canTransferOrReceiveTask = false;
            player.canTransferOrReceiveTask = false;
            taskToTransfer.hasBeenTransferred();

            return true;
        }

        return false;
    }
    public Boolean tryCollectTask(Task task) {
        return !task.getCollected() && morale >= task.getMoraleCost();
    }
    public Boolean canTransferOrReceiveTask() {
        for (Task task : collectedTasks)
            if (task.getCanBeTransferred())
                return canTransferOrReceiveTask;

        return false;
    }
}
