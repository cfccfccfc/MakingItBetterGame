package com.cfc.makingitbettergame.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.StringBuilder;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.cfc.makingitbettergame.GameClasses.BoardClasses.*;
import com.cfc.makingitbettergame.GameClasses.Miscellaneous.GameController;
import com.cfc.makingitbettergame.GameClasses.Miscellaneous.GameSettings;
import com.cfc.makingitbettergame.GameClasses.Miscellaneous.Resources;
import com.cfc.makingitbettergame.GameClasses.ObjectiveRelatedClasses.Event;
import com.cfc.makingitbettergame.GameClasses.ObjectiveRelatedClasses.Task;
import com.cfc.makingitbettergame.GameClasses.PlayerClasses.Player;
import com.cfc.makingitbettergame.MakingItBetterGame;

import java.util.*;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static com.cfc.makingitbettergame.MakingItBetterGame.*;

public class PlayScreen implements Screen {
    private final MakingItBetterGame game;
    private Stage stage;
    private Skin skin16;
    private Skin skin15;

    private Image backgroundImg;
    private Image boardImg;
    private Image resourcesTabImg;
    private Image logTabImg;
    private Image playerTabImg;
    private Image descriptionFrameImg;
    private Image logBackgroundImg;
    private Image frameTopImg, frameMiddleImg;
    private Sprite frameLeftSprite, frameRightSprite, frameBottomSprite;
    private Image objectiveTabImg;

    private SpriteBatch spriteBatch;

    private Button mainMenuButton, manualButton, yesButton, noButton, dieButton, coinFlipButton, transferTaskButton;
    private int descriptionAndPromptOfsset = 100;

    private float topHudBoundHeight = textButtonDefaultHeight + 8;
    private float middleHudBoundSize = 7;

    private float resourcesTabOverlap = 50;

    private int dieButtonSize = 73;

    private int playerGamePieceSize = 52;
    private int playerGamePieceSpacing;

    private float playerButtonSize = 90;
    private float firstPlayerButtonX = 660, firstPlayerButtonY = 156;
    private float playerButtonHorizontalSpacing = 120 + playerButtonSize;
    private float playerButtonVerticalSpacing = 17 + playerButtonSize;

    private Sound buttonClickSound, dieRollSound, coinTossSound;

    private HashMap<Player, Button> playerButtons;

    private Label moneyLabel, labourLabel, communitySupportLabel, descriptionLabel, activeObjectiveLabel, reputationLabel, communitySupportLabelValue;
    private float firstResourceLabelX = 700, firstResourceLabelY = 600;
    private float resourceLabelsSpacing = 162;
    private float descriptionLabelXOffset = 180, descriptionLabelYOffset = 130;
    private float resourceValuesOffset = 20;
    private float playerTasksLabelsOffset = 10;

    private HashMap<Resources.ResourceTypes, Label> resourceTypesLabels;
    private HashMap<Player, Label> playerTasksLabels;

    private List<String> dieTextureStrings;
    private List<String> coinFlipTextureStrings;

    private float gamePieceMovementDeltaTime = 0, dieRollAnimationDeltaTime = 1, coinFlipAnimationDeltaTime = 1;
    private float gamePieceMovementDelay = 1, dieRollAnimationTime = 1, coinFlipAnimationTime = .05f;
    private int dieFaceValue = 0, coinFlipFrameNumber = Integer.MAX_VALUE;

    private String currentCoinFlipAnimationFrame = "coinFlip1Texture";

    private String descriptionText = "";

    private int playerMoveCounter = 0;

    private int fontSizePlayScreen = 16;

    private Touchable yesButtonState = Touchable.disabled, noButtonState = Touchable.disabled, transferTaskButtonState = Touchable.disabled,
            dieButtonState =  Touchable.enabled, coinFlipButtonState = Touchable.disabled, playerButtonsState = Touchable.disabled;

    private Label tasksLabel, currentObjectiveLabel;
    private List<Label> tasksLabelsList;
    private List<String> communitySupportLabelsList;

    private HashMap<Player, Label> playerNamesLabels, playerMoraleLabels;

    private List<String> logMessagesList = new ArrayList<>();
    private Label logLabel;
    private int maxLogMessages = 13, maxLogCharacters = 15;

    private Label promptLabel;
    private String promptText = "Welcome! Roll die to start";

    private Boolean luckyPersonSecondFlip = false, skipNextNextTurn = false;

    private Resources resourcePoolTemp = new Resources();
    private HashMap<Resources.ResourceTypes, String> resourceTypesToStrings;

    private Boolean dieRolled = false;
    private Boolean objectiveCompletedThisTurn = false;

    public PlayScreen(MakingItBetterGame game) {
        this.game = game;
        this.stage = new Stage(new FitViewport(MakingItBetterGame.V_WIDTH, MakingItBetterGame.V_HEIGHT, game.gameCam));

        this.playerGamePieceSpacing = (game.squareWidth - playerGamePieceSize * 2) / 3;
    }

    public void initSkins() {
        resourceTypesToStrings = new HashMap<>();

        resourceTypesToStrings.put(Resources.ResourceTypes.Money, "KHR");
        resourceTypesToStrings.put(Resources.ResourceTypes.Reputation, "Reputation");
        resourceTypesToStrings.put(Resources.ResourceTypes.Labour, "Labour");
        resourceTypesToStrings.put(Resources.ResourceTypes.CommunitySupport, "Community support");

        FreeTypeFontGenerator generator16 = new FreeTypeFontGenerator(Gdx.files.internal("fonts/gameFontDefault.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameters16 = new FreeTypeFontGenerator.FreeTypeFontParameter();

        fontParameters16.size = fontSizePlayScreen;
        fontParameters16.color = Color.DARK_GRAY;

        BitmapFont fontMainMenu16 = generator16.generateFont(fontParameters16);

        this.skin16 = new Skin();
        this.skin16.addRegions(game.assetManager.get(GameSettings.getInstance().getUiSkinAtlasFilePath(), TextureAtlas.class));
        this.skin16.add("default-font", fontMainMenu16);
        this.skin16.load(Gdx.files.internal("ui/uiskin.json"));


        FreeTypeFontGenerator generator15 = new FreeTypeFontGenerator(Gdx.files.internal("fonts/gameFontDefault.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameters15 = new FreeTypeFontGenerator.FreeTypeFontParameter();

        fontParameters15.size = fontSizePlayScreen - 1;
        fontParameters15.color = Color.BLACK;

        BitmapFont fontMainMenu15 = generator15.generateFont(fontParameters15);

        this.skin15 = new Skin();
        this.skin15.addRegions(game.assetManager.get(GameSettings.getInstance().getUiSkinAtlasFilePath(), TextureAtlas.class));
        this.skin15.add("default-font", fontMainMenu15);
        this.skin15.load(Gdx.files.internal("ui/uiskin.json"));
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        stage.clear();

        spriteBatch = new SpriteBatch();

        initSkins();

        playerButtons = new HashMap<>();
        resourceTypesLabels = new HashMap<>();
        playerTasksLabels = new HashMap<>();
        dieTextureStrings = new ArrayList<>();
        coinFlipTextureStrings = new ArrayList<>();
        tasksLabelsList = new ArrayList<>();
        communitySupportLabelsList = new ArrayList<>();
        playerNamesLabels = new HashMap<>();
        playerMoraleLabels = new HashMap<>();

        communitySupportLabelsList.add("Very Low (0)");
        communitySupportLabelsList.add("Low (1)");
        communitySupportLabelsList.add("Moderate (2)");
        communitySupportLabelsList.add("High (3)");
        communitySupportLabelsList.add("Very High (4)");

        initSounds();
        populateSkinWithTextures();
        initBackground();
        initButtons();
        initSquares();
        initPlayerButtons();
        initLabels();
        initPlayerGamePieces();
        updatePlayerDetailsLabels();
        updateButtonStates();
        updateLog();
    }

    @Override
    public void render(float v) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(v);

        stage.draw();

        spriteBatch.begin();
        frameBottomSprite.draw(spriteBatch);
        frameLeftSprite.draw(spriteBatch);
        frameRightSprite.draw(spriteBatch);
        spriteBatch.end();

        updateButtonStates();
        updateActivePlayerButton();
        updateLabels();
        updateActiveObjectiveLabel();
        updatePlayerDetailsLabels();

        if (game.gameController.getGameState() == GameController.GameState.Won)
            game.setScreen(game.victoryScreen);
        if (game.gameController.getGameState() == GameController.GameState.Lost)
            game.setScreen(game.lossScreen);

        gamePieceMovementDeltaTime += v;
        dieRollAnimationDeltaTime += v;
        coinFlipAnimationDeltaTime += v;

        if (dieRollAnimationDeltaTime < dieRollAnimationTime) {
            dieButton.getStyle().up = skin16.getDrawable(dieTextureStrings.get(dieFaceValue % GameSettings.getInstance().getDieFacesNumber()));
            dieFaceValue++;
        }
        else
            dieButton.getStyle().up = skin16.getDrawable(dieTextureStrings.get(game.gameController.getDieRollValue() - 1));

        if (coinFlipAnimationDeltaTime > coinFlipAnimationTime && coinFlipFrameNumber < coinFlipTextureStrings.size()) {
            coinFlipButton.getStyle().up = skin16.getDrawable(coinFlipTextureStrings.get(coinFlipFrameNumber));
            coinFlipAnimationDeltaTime = 0;
            coinFlipFrameNumber++;
        }
        else if (coinFlipFrameNumber == coinFlipTextureStrings.size()) {
            if (game.gameController.getCoinFlipValue()) {
                coinFlipButton.getStyle().up = skin16.getDrawable("coinFlipTrueTexture");
                currentCoinFlipAnimationFrame = "coinFlipTrueTexture";
            }
            else {
                coinFlipButton.getStyle().up = skin16.getDrawable("coinFlipFalseTexture");
                currentCoinFlipAnimationFrame = "coinFlipFalseTexture";
            }
            coinFlipFrameNumber++;
        }

        if (gamePieceMovementDeltaTime > gamePieceMovementDelay && playerMoveCounter > 0) {
            game.gameController.animatePlayerMovement(true);
            game.gameController.getActivePlayer().setPlayerBoardPosition(game.gameController.getBoard().moveForward(game.gameController.getActivePlayer()));

            gamePieceMovementDeltaTime = 0;
            playerMoveCounter--;

            if (playerMoveCounter == 0) {
                switch (game.gameController.getActivePlayer().getPlayerBoardPosition().getSquare().getSquareType()) {
                    case StartSquare:
                        promptText = "Roll die to continue";
                        nextTurn();
                        break;
                    case TaskSquare:
                        if (game.gameController.getActivePlayer().tryCollectTask(game.gameController.getActivePlayer().getPlayerBoardPosition().getTaskSquare().getTask())) {
                            promptText = "Accept task " + game.gameController.getActivePlayer().getPlayerBoardPosition().getTaskSquare().getTask().getTaskNumber() + " ?";
                            yesButtonState = Touchable.enabled;
                            noButtonState = Touchable.enabled;
                            descriptionText = game.gameController.getActivePlayer().getPlayerBoardPosition().getSquare().getSquareDescription();
                        }
                        else {
                            promptText = "Cannot accept task "  + game.gameController.getActivePlayer().getPlayerBoardPosition().getTaskSquare().getTask().getTaskNumber();
                            nextTurn();
                        }
                        break;
                    default:
                        promptText = "Flip the coin";
                        coinFlipButtonState = Touchable.enabled;
                        descriptionText = game.gameController.getActivePlayer().getPlayerBoardPosition().getSquare().getSquareDescription();
                }
            }
        }
    }

    private void update(float v)
    {
        stage.act(v);
    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        stage.dispose();
    }

    private void initButtons() {
        Texture mainMenuButtonTexture = game.assetManager.get(GameSettings.getInstance().getMenuButtonFilePath(), Texture.class);
        Texture mainMenuButtonPressedTexture = game.assetManager.get(GameSettings.getInstance().getMenuButtonPressedFilePath(), Texture.class);
        Button.ButtonStyle mainMenuButtonStyle = new Button.ButtonStyle();
        mainMenuButtonStyle.up = new NinePatchDrawable(new NinePatch(mainMenuButtonTexture));
        mainMenuButtonStyle.down = new NinePatchDrawable(new NinePatch(mainMenuButtonPressedTexture));

        mainMenuButton = new Button(mainMenuButtonStyle);
        mainMenuButton.setPosition(game.menuButtonHorizontalOffset, MakingItBetterGame.V_HEIGHT - (float) textButtonDefaultHeight / 2 - game.menuButtonVerticalTopOffset);
        mainMenuButton.setSize(textButtonDefaultWidth, textButtonDefaultHeight);
        mainMenuButton.addAction(sequence(alpha(0), parallel(fadeIn(game.buttonAnimationTime), moveBy(0, -20,
                game.buttonAnimationTime, Interpolation.pow5Out))));
        mainMenuButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                buttonClickSound.setVolume(buttonClickSound.play(), game.defaultButtonClickSoundVolume);
                game.initGame();
                game.setScreen(game.mainMenuScreen);

                resetButtonStates();
                resetAnimatedElements();
            }
        });

        Texture manualButtonTexture = game.assetManager.get(GameSettings.getInstance().getManualButtonFilePath(), Texture.class);
        Texture manualButtonPressedTexture = game.assetManager.get(GameSettings.getInstance().getManualButtonPressedFilePath(), Texture.class);
        Button.ButtonStyle manualButtonStyle = new Button.ButtonStyle();
        manualButtonStyle.up = new NinePatchDrawable(new NinePatch(manualButtonTexture));
        manualButtonStyle.down = new NinePatchDrawable(new NinePatch(manualButtonPressedTexture));

        manualButton = new Button(manualButtonStyle);
        manualButton.setPosition(game.menuButtonsSpacing + textButtonDefaultWidth + game.menuButtonHorizontalOffset,
                MakingItBetterGame.V_HEIGHT - (float) textButtonDefaultHeight / 2 - game.menuButtonVerticalTopOffset);
        manualButton.setSize(textButtonDefaultWidth, textButtonDefaultHeight);
        manualButton.addAction(sequence(alpha(0), parallel(fadeIn(game.buttonAnimationTime), moveBy(0, -20,
                game.buttonAnimationTime, Interpolation.pow5Out))));
        manualButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                buttonClickSound.setVolume(buttonClickSound.play(), game.defaultButtonClickSoundVolume);
                game.toggleManual();
            }
        });

        Texture dieButtonTexture = game.assetManager.get(GameSettings.getInstance().getDie6FilePath(), Texture.class);
        Button.ButtonStyle dieButtonStyle = new Button.ButtonStyle();
        dieButtonStyle.up = new NinePatchDrawable(new NinePatch(dieButtonTexture));

        dieButton = new Button(dieButtonStyle);
        dieButton.setSize(dieButtonSize, dieButtonSize);
        dieButton.setPosition(V_WIDTH - dieButtonSize - 22, 165);

        dieButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.gameController.rollDie();
                dieRollSound.play();
                animateDie();

                dieRolled = true;

                coinFlipFrameNumber = Integer.MAX_VALUE;
                coinFlipButton.getStyle().up = skin16.getDrawable(coinFlipTextureStrings.getFirst());

                transferTaskButtonState = Touchable.disabled;

                promptText = "You rolled " + game.gameController.getDieRollValue();
                dieButtonState = Touchable.disabled;
                playerMoveCounter = game.gameController.getDieRollValue();

                addMessageToLog(game.gameController.getActivePlayer().getPlayerName() + " rolled " + game.gameController.getDieRollValue());
            }
        });

        Button.ButtonStyle coinFlipButtonStyle = new Button.ButtonStyle();
        coinFlipButtonStyle.up = skin16.getDrawable(currentCoinFlipAnimationFrame);

        coinFlipButton = new Button(coinFlipButtonStyle);

        coinFlipButton.setSize(dieButtonSize, dieButtonSize);
        coinFlipButton.setPosition(V_WIDTH - dieButtonSize * 2 - 26, dieButton.getY());

        coinFlipButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                coinTossSound.play();
                game.gameController.flipCoin();
                animateCoin();
                coinFlipButtonState = Touchable.disabled;

                if (game.gameController.getActivePlayer().getPlayerBoardPosition().getSquare().getSquareType() == Square.SquareType.PositiveEventSquare) {
                    if (game.gameController.getCoinFlipValue()) {
                        if (game.gameController.getActivePlayer().getPlayerBoardPosition().getEventSquare().getEvent().getResourcesChangeValues().size() == 1) {
                            switch (game.gameController.getActivePlayer().getPlayerBoardPosition().getEventSquare().getEvent().getEventType()) {
                                case CommunityVolunteerDay:
                                    promptText = "No labour costs for next round";
                                    addMessageToLog(game.gameController.getActivePlayer().getPlayerName() + " negated labour costs");
                                    break;
                                case DonationOfMaterials:
                                    if (game.gameController.getResourcePool().getAllResourceTypesQuantity().get(Resources.ResourceTypes.CommunitySupport) >=
                                            GameSettings.getInstance().getMinimalCommunitySupportForDonationOfMaterials()
                                            && !game.gameController.getDonationOfMaterialsDiscountApplied()) {
                                        promptText = "Discount applied to objective 1 task 2";
                                        addMessageToLog(game.gameController.getActivePlayer().getPlayerName() + " got discount");
                                    }
                                    else
                                        promptText = "Nothing happened";
                                    break;
                                case TechnicalTrainingWorkshop:
                                    promptText = "Next negative event will be negated";
                                    addMessageToLog(game.gameController.getActivePlayer().getPlayerName() + " got negative event pass");
                                default:
                                    promptText = "You gained resources";
                            }

                            resourcePoolTemp = new Resources();
                            for (Resources.ResourceTypes resourceType : Resources.ResourceTypes.values())
                                resourcePoolTemp.modifyResource(resourceType,
                                        game.gameController.getResourcePool().getAllResourceTypesQuantity().get(resourceType), true);

                            game.gameController.handleEventSquare();

                            for (Resources.ResourceTypes resourceType : Resources.ResourceTypes.values()) {
                                if (!Objects.equals(game.gameController.getResourcePool().getAllResourceTypesQuantity().get(resourceType),
                                        resourcePoolTemp.getAllResourceTypesQuantity().get(resourceType))) {
                                    if(resourcePoolTemp.getAllResourceTypesQuantity().get(resourceType) -
                                            game.gameController.getResourcePool().getAllResourceTypesQuantity().get(resourceType) > 0)
                                        addMessageToLog(game.gameController.getActivePlayer().getPlayerName() + " lost " +
                                                (resourcePoolTemp.getAllResourceTypesQuantity().get(resourceType) -
                                                        game.gameController.getResourcePool().getAllResourceTypesQuantity().get(resourceType)) + " " +
                                                resourceTypesToStrings.get(resourceType));
                                    else
                                        addMessageToLog(game.gameController.getActivePlayer().getPlayerName() + " gained " +
                                                (game.gameController.getResourcePool().getAllResourceTypesQuantity().get(resourceType) -
                                                        resourcePoolTemp.getAllResourceTypesQuantity().get(resourceType)) + " " +
                                                resourceTypesToStrings.get(resourceType));
                                }
                            }
                            resourcePoolTemp = new Resources();

                            nextTurn();
                            luckyPersonSecondFlip = false;
                        }

                        else {
                            promptText = "Decision: ";
                            luckyPersonSecondFlip = false;
                            yesButtonState = Touchable.enabled;
                            noButtonState = Touchable.enabled;
                        }
                    }
                    else {
                        if (game.gameController.getActivePlayer().getPlayerExpertise() == Player.PlayerExpertise.LuckyPerson && !luckyPersonSecondFlip) {
                            promptText = "Lucky person can flip twice";
                            luckyPersonSecondFlip = true;
                            coinFlipButtonState = Touchable.enabled;
                        }
                        else {
                            promptText = "Nothing happened";
                            nextTurn();
                            luckyPersonSecondFlip = false;
                        }
                    }
                }
                else {
                    if (game.gameController.getCoinFlipValue()) {
                        promptText = "Nothing happened";
                        nextTurn();
                        luckyPersonSecondFlip = false;
                    }
                    else {
                        if (game.gameController.getActivePlayer().getPlayerExpertise() == Player.PlayerExpertise.LuckyPerson && !luckyPersonSecondFlip) {
                            promptText = "Lucky person can flip twice";
                            luckyPersonSecondFlip = true;
                            coinFlipButtonState = Touchable.enabled;
                        }
                        else {
                            if (!game.gameController.getNegateNegativeSquareEvent()) {
                                if (game.gameController.getActivePlayer().getPlayerBoardPosition().getEventSquare().getEvent().getResourcesChangeValues().size() == 1) {
                                    resourcePoolTemp = new Resources();
                                    for (Resources.ResourceTypes resourceType : Resources.ResourceTypes.values())
                                        resourcePoolTemp.modifyResource(resourceType,
                                                game.gameController.getResourcePool().getAllResourceTypesQuantity().get(resourceType), true);

                                    game.gameController.handleEventSquare();

                                    for (Resources.ResourceTypes resourceType : Resources.ResourceTypes.values()) {
                                        if (!Objects.equals(game.gameController.getResourcePool().getAllResourceTypesQuantity().get(resourceType),
                                                resourcePoolTemp.getAllResourceTypesQuantity().get(resourceType))) {
                                            if(resourcePoolTemp.getAllResourceTypesQuantity().get(resourceType) -
                                                    game.gameController.getResourcePool().getAllResourceTypesQuantity().get(resourceType) > 0)
                                                addMessageToLog(game.gameController.getActivePlayer().getPlayerName() + " lost " +
                                                        (resourcePoolTemp.getAllResourceTypesQuantity().get(resourceType) -
                                                                game.gameController.getResourcePool().getAllResourceTypesQuantity().get(resourceType)) + " " +
                                                        resourceTypesToStrings.get(resourceType));
                                            else
                                                addMessageToLog(game.gameController.getActivePlayer().getPlayerName() + " gained " +
                                                        (game.gameController.getResourcePool().getAllResourceTypesQuantity().get(resourceType) -
                                                                resourcePoolTemp.getAllResourceTypesQuantity().get(resourceType)) + " " +
                                                        resourceTypesToStrings.get(resourceType));
                                        }
                                    }
                                    resourcePoolTemp = new Resources();

                                    switch (game.gameController.getActivePlayer().getPlayerBoardPosition().getEventSquare().getEvent().getEventType()) {
                                        case Drought:
                                            promptText = "Objective completion ban for 1 turn";
                                            addMessageToLog(game.gameController.getActivePlayer().getPlayerName() + " got objective completion ban");
                                            break;
                                        case LowAwarenessOpposition:
                                            if (game.gameController.getResourcePool().getAllResourceTypesQuantity().get(Resources.ResourceTypes.CommunitySupport) == 0)
                                                promptText = "You lost resources";
                                            else
                                                promptText = "Nothing happened";
                                            break;
                                        default:
                                            promptText = "You lost resources";
                                    }
                                    nextTurn();
                                    luckyPersonSecondFlip = false;
                                }
                                else {
                                    promptText = "Decision: ";
                                    luckyPersonSecondFlip = false;
                                    yesButtonState = Touchable.enabled;
                                    noButtonState = Touchable.enabled;
                                }
                            }
                            else {
                                promptText = "Negative event negated";
                                nextTurn();
                                luckyPersonSecondFlip = false;
                                game.gameController.negativeSquareEventNegated();
                            }
                        }
                    }
                }
            }
        });

        Texture yesButtonTexture = game.assetManager.get(GameSettings.getInstance().getYesButtonFilePath(), Texture.class);
        Texture yesButtonPressedTexture = game.assetManager.get(GameSettings.getInstance().getYesButtonPressedFilePath(), Texture.class);
        Button.ButtonStyle yesButtonStyle = new Button.ButtonStyle();
        yesButtonStyle.up = new NinePatchDrawable(new NinePatch(yesButtonTexture));
        yesButtonStyle.down = new NinePatchDrawable(new NinePatch(yesButtonPressedTexture));

        yesButton = new Button(yesButtonStyle);
        yesButton.setPosition(182, 155);
        yesButton.setSize(game.yesNoButtonsWidth, game.yesNoButtonsHeight);
        yesButton.addAction(sequence(alpha(0), parallel(fadeIn(game.buttonAnimationTime), moveBy(0, -20,
                game.buttonAnimationTime, Interpolation.pow5Out))));

        yesButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                buttonClickSound.setVolume(buttonClickSound.play(), game.defaultButtonClickSoundVolume);
                yesButtonState = Touchable.disabled;
                noButtonState = Touchable.disabled;

                if (skipNextNextTurn) {
                    resourcePoolTemp = new Resources();
                    for (Resources.ResourceTypes resourceType : Resources.ResourceTypes.values())
                        resourcePoolTemp.modifyResource(resourceType,
                                game.gameController.getResourcePool().getAllResourceTypesQuantity().get(resourceType), true);

                    if (game.gameController.completeActiveObjective()) {
                        descriptionText = "";
                        promptText = "Objective complete";

                        for (Resources.ResourceTypes resourceType : Resources.ResourceTypes.values()) {
                            if (!Objects.equals(game.gameController.getResourcePool().getAllResourceTypesQuantity().get(resourceType),
                                    resourcePoolTemp.getAllResourceTypesQuantity().get(resourceType))) {
                                if(resourcePoolTemp.getAllResourceTypesQuantity().get(resourceType) -
                                        game.gameController.getResourcePool().getAllResourceTypesQuantity().get(resourceType) > 0)
                                    addMessageToLog(game.gameController.getActivePlayer().getPlayerName() + " spent " +
                                            (resourcePoolTemp.getAllResourceTypesQuantity().get(resourceType) -
                                                    game.gameController.getResourcePool().getAllResourceTypesQuantity().get(resourceType)) + " " +
                                            resourceTypesToStrings.get(resourceType));
                                else
                                    addMessageToLog(game.gameController.getActivePlayer().getPlayerName() + " spent " +
                                            (game.gameController.getResourcePool().getAllResourceTypesQuantity().get(resourceType) -
                                                    resourcePoolTemp.getAllResourceTypesQuantity().get(resourceType)) + " " +
                                            resourceTypesToStrings.get(resourceType));
                            }
                        }

                        addMessageToLog(game.gameController.getActivePlayer().getPlayerName() + " completed objective");
                    }
                    skipNextNextTurn = false;
                    dieButtonState = Touchable.enabled;
                }
                else {
                    if (game.gameController.getActivePlayer().getPlayerBoardPosition().getSquare().getSquareType() == Square.SquareType.PositiveEventSquare ||
                            game.gameController.getActivePlayer().getPlayerBoardPosition().getSquare().getSquareType() == Square.SquareType.NegativeEventSquare) {
                        resourcePoolTemp = new Resources();
                        for (Resources.ResourceTypes resourceType : Resources.ResourceTypes.values())
                            resourcePoolTemp.modifyResource(resourceType,
                                    game.gameController.getResourcePool().getAllResourceTypesQuantity().get(resourceType), true);

                        game.gameController.handleEventSquareWithChoice(true);

                        for (Resources.ResourceTypes resourceType : Resources.ResourceTypes.values()) {
                            if (!Objects.equals(game.gameController.getResourcePool().getAllResourceTypesQuantity().get(resourceType),
                                    resourcePoolTemp.getAllResourceTypesQuantity().get(resourceType))) {
                                if(resourcePoolTemp.getAllResourceTypesQuantity().get(resourceType) -
                                        game.gameController.getResourcePool().getAllResourceTypesQuantity().get(resourceType) > 0)
                                    addMessageToLog(game.gameController.getActivePlayer().getPlayerName() + " lost " +
                                            (resourcePoolTemp.getAllResourceTypesQuantity().get(resourceType) -
                                                    game.gameController.getResourcePool().getAllResourceTypesQuantity().get(resourceType)) + " " +
                                            resourceTypesToStrings.get(resourceType));
                                else
                                    addMessageToLog(game.gameController.getActivePlayer().getPlayerName() + " gained " +
                                            (game.gameController.getResourcePool().getAllResourceTypesQuantity().get(resourceType) -
                                                    resourcePoolTemp.getAllResourceTypesQuantity().get(resourceType)) + " " +
                                            resourceTypesToStrings.get(resourceType));
                            }
                        }
                        resourcePoolTemp = new Resources();

                        promptText = "Roll die to continue";
                        nextTurn();
                    }
                    if (game.gameController.getActivePlayer().getPlayerBoardPosition().getSquare().getSquareType() == Square.SquareType.TaskSquare) {
                        if (!game.gameController.getNegateObjectiveCompletion()) {
                            resourcePoolTemp = new Resources();
                            for (Resources.ResourceTypes resourceType : Resources.ResourceTypes.values())
                                resourcePoolTemp.modifyResource(resourceType,
                                        game.gameController.getResourcePool().getAllResourceTypesQuantity().get(resourceType), true);

                            if (game.gameController.completeActiveObjective()) {
                                descriptionText = "";
                                promptText = "Objective complete";

                                for (Resources.ResourceTypes resourceType : Resources.ResourceTypes.values()) {
                                    if (!Objects.equals(game.gameController.getResourcePool().getAllResourceTypesQuantity().get(resourceType),
                                            resourcePoolTemp.getAllResourceTypesQuantity().get(resourceType))) {
                                        if(resourcePoolTemp.getAllResourceTypesQuantity().get(resourceType) -
                                                game.gameController.getResourcePool().getAllResourceTypesQuantity().get(resourceType) > 0)
                                            addMessageToLog(game.gameController.getActivePlayer().getPlayerName() + " spent " +
                                                    (resourcePoolTemp.getAllResourceTypesQuantity().get(resourceType) -
                                                            game.gameController.getResourcePool().getAllResourceTypesQuantity().get(resourceType)) + " " +
                                                    resourceTypesToStrings.get(resourceType));
                                        else
                                            addMessageToLog(game.gameController.getActivePlayer().getPlayerName() + " spent " +
                                                    (game.gameController.getResourcePool().getAllResourceTypesQuantity().get(resourceType) -
                                                            resourcePoolTemp.getAllResourceTypesQuantity().get(resourceType)) + " " +
                                                    resourceTypesToStrings.get(resourceType));
                                        objectiveCompletedThisTurn = true;
                                    }
                                }
                                resourcePoolTemp = new Resources();

                                addMessageToLog(game.gameController.getActivePlayer().getPlayerName() + " completed objective");
                            }
                        }
                        if (game.gameController.getActivePlayer().getPlayerBoardPosition().getSquare().getSquareType() == Square.SquareType.TaskSquare) {
                            if (!objectiveCompletedThisTurn) {
                                game.gameController.getActivePlayer().collectTask(game.gameController.getActivePlayer().getPlayerBoardPosition().getTaskSquare().getTask());
                                promptText = "Task " + game.gameController.getActivePlayer().getPlayerBoardPosition().getTaskSquare().getTask().getTaskNumber() + " accepted";

                                addMessageToLog(game.gameController.getActivePlayer().getPlayerName() + " accepted task " +
                                        game.gameController.getActivePlayer().getPlayerBoardPosition().getTaskSquare().getTask().getTaskNumber());
                            }
                        }

                        if (game.gameController.tryCompleteActiveObjective() && !game.gameController.getNegateObjectiveCompletion()) {
                            promptText = "Complete current objective?";
                            descriptionText = game.gameController.getActiveObjective().getDescription() + "Total cost: " +
                                    game.gameController.getActiveObjectiveResourceCostWithDiscount().getAllResourceTypesQuantity().get(Resources.ResourceTypes.Money) + " KHR";
                            yesButtonState = Touchable.enabled;
                            noButtonState = Touchable.enabled;
                        }
                        else {
                            nextTurn();
                        }
                    }
                }
            }
        });

        Texture noButtonTexture = game.assetManager.get(GameSettings.getInstance().getNoButtonFilePath(), Texture.class);
        Texture noButtonPressedTexture = game.assetManager.get(GameSettings.getInstance().getNoButtonPressedFilePath(), Texture.class);
        Button.ButtonStyle noButtonStyle = new Button.ButtonStyle();
        noButtonStyle.up = new NinePatchDrawable(new NinePatch(noButtonTexture));
        noButtonStyle.down = new NinePatchDrawable(new NinePatch(noButtonPressedTexture));

        noButton = new Button(noButtonStyle);
        noButton.setPosition(350, 155);
        noButton.setSize(game.yesNoButtonsWidth, game.yesNoButtonsHeight);
        noButton.addAction(sequence(alpha(0), parallel(fadeIn(game.buttonAnimationTime), moveBy(0, -20,
                game.buttonAnimationTime, Interpolation.pow5Out))));
        noButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                buttonClickSound.setVolume(buttonClickSound.play(), game.defaultButtonClickSoundVolume);
                yesButtonState = Touchable.disabled;
                noButtonState = Touchable.disabled;

                if (game.gameController.getActivePlayer().getPlayerBoardPosition().getSquare().getSquareType() == Square.SquareType.PositiveEventSquare ||
                        game.gameController.getActivePlayer().getPlayerBoardPosition().getSquare().getSquareType() == Square.SquareType.NegativeEventSquare) {
                    resourcePoolTemp = new Resources();
                    for (Resources.ResourceTypes resourceType : Resources.ResourceTypes.values())
                        resourcePoolTemp.modifyResource(resourceType,
                                game.gameController.getResourcePool().getAllResourceTypesQuantity().get(resourceType), true);

                    game.gameController.handleEventSquareWithChoice(false);

                    for (Resources.ResourceTypes resourceType : Resources.ResourceTypes.values()) {
                        if (!Objects.equals(game.gameController.getResourcePool().getAllResourceTypesQuantity().get(resourceType),
                                resourcePoolTemp.getAllResourceTypesQuantity().get(resourceType))) {
                            if(resourcePoolTemp.getAllResourceTypesQuantity().get(resourceType) -
                                    game.gameController.getResourcePool().getAllResourceTypesQuantity().get(resourceType) > 0)
                                addMessageToLog(game.gameController.getActivePlayer().getPlayerName() + " lost " +
                                        (resourcePoolTemp.getAllResourceTypesQuantity().get(resourceType) -
                                                game.gameController.getResourcePool().getAllResourceTypesQuantity().get(resourceType)) + " " +
                                        resourceTypesToStrings.get(resourceType));
                            else
                                addMessageToLog(game.gameController.getActivePlayer().getPlayerName() + " gained " +
                                        (game.gameController.getResourcePool().getAllResourceTypesQuantity().get(resourceType) -
                                                resourcePoolTemp.getAllResourceTypesQuantity().get(resourceType)) + " " +
                                        resourceTypesToStrings.get(resourceType));
                        }
                    }
                    resourcePoolTemp = new Resources();
                }

                descriptionText = "";
                promptText = "Roll die to continue";
                nextTurn();
            }
        });

        Texture transferTaskButtonTexture = game.assetManager.get(GameSettings.getInstance().getTransferTaskButtonFilePath(), Texture.class);
        Texture transferTaskButtonPressedTexture = game.assetManager.get(GameSettings.getInstance().getTransferTaskButtonPressedFilePath(), Texture.class);
        Button.ButtonStyle transferTaskButtonStyle = new Button.ButtonStyle();
        transferTaskButtonStyle.up = new NinePatchDrawable(new NinePatch(transferTaskButtonTexture));
        transferTaskButtonStyle.down = new NinePatchDrawable(new NinePatch(transferTaskButtonPressedTexture));

        transferTaskButton = new Button(transferTaskButtonStyle);
        transferTaskButton.setSize(textButtonDefaultWidth, textButtonDefaultHeight);
        transferTaskButton.setPosition(dieButton.getX() - textButtonDefaultWidth + dieButtonSize,
                coinFlipButton.getY() - textButtonDefaultHeight + 16);
        transferTaskButton.addAction(sequence(alpha(0), parallel(fadeIn(game.buttonAnimationTime), moveBy(0, -20,
                game.buttonAnimationTime, Interpolation.pow5Out))));
        transferTaskButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                buttonClickSound.setVolume(buttonClickSound.play(), game.defaultButtonClickSoundVolume);
                playerButtonsState = Touchable.enabled;
                transferTaskButtonState = Touchable.disabled;
                dieButtonState = Touchable.disabled;
            }
        });

        stage.addActor(mainMenuButton);
        stage.addActor(manualButton);
        stage.addActor(dieButton);
        stage.addActor(coinFlipButton);
        stage.addActor(yesButton);
        stage.addActor(noButton);
        stage.addActor(transferTaskButton);
    }

    public void initBackground() {
        Texture backgroundTexture = game.assetManager.get(GameSettings.getInstance().getGameBackgroundFilePath(), Texture.class);
        backgroundImg = new Image(backgroundTexture);
        backgroundImg.setPosition(0, 0);
        backgroundImg.setSize(V_WIDTH, V_HEIGHT);

        Texture boardTexture = game.assetManager.get(GameSettings.getInstance().getBoardFilePath(), Texture.class);
        boardImg = new Image(boardTexture);
        boardImg.setSize(V_HEIGHT - topHudBoundHeight - middleHudBoundSize, V_HEIGHT - topHudBoundHeight - middleHudBoundSize);
        boardImg.setPosition(middleHudBoundSize, middleHudBoundSize);

        Texture resourcesTabTexture = game.assetManager.get(GameSettings.getInstance().getResourcesTabFilePath(), Texture.class);
        resourcesTabImg = new Image(resourcesTabTexture);
        resourcesTabImg.setSize(V_WIDTH - boardImg.getWidth() - middleHudBoundSize * 3, (V_HEIGHT - topHudBoundHeight) * .2f);
        resourcesTabImg.setPosition(boardImg.getWidth() + middleHudBoundSize * 2, V_HEIGHT - topHudBoundHeight - resourcesTabImg.getHeight());

        Texture logBackgroundTexture = game.assetManager.get(GameSettings.getInstance().getLogBackgroundFilePath(), Texture.class);
        logBackgroundImg = new Image(logBackgroundTexture);
        logBackgroundImg.setSize(V_WIDTH - boardImg.getWidth() - middleHudBoundSize * 3, (V_HEIGHT - topHudBoundHeight) * .5f);
        logBackgroundImg.setPosition(boardImg.getWidth() + middleHudBoundSize * 2, V_HEIGHT - topHudBoundHeight - (resourcesTabImg.getHeight() -
                resourcesTabOverlap) - logBackgroundImg.getHeight());

        Texture playerTabTexture = game.assetManager.get(GameSettings.getInstance().getPlayerTabFilePath(), Texture.class);
        playerTabImg = new Image(playerTabTexture);
        playerTabImg.setSize(V_WIDTH - boardImg.getWidth() - middleHudBoundSize * 3, (V_HEIGHT - topHudBoundHeight) * .377f);
        playerTabImg.setPosition(boardImg.getWidth() + middleHudBoundSize * 2, middleHudBoundSize);

        Texture descriptionFrameTexture = game.assetManager.get(GameSettings.getInstance().getDescriptionFrameFilePath(), Texture.class);
        descriptionFrameImg = new Image(descriptionFrameTexture);
        descriptionFrameImg.setSize(boardImg.getWidth() - game.squareWidth * 2 - descriptionAndPromptOfsset,
                (boardImg.getHeight() - game.squareHeight * 2) * .75f - (float) descriptionAndPromptOfsset / 2);
        descriptionFrameImg.setPosition(game.squareWidth + (float) descriptionAndPromptOfsset / 2,
                boardImg.getHeight() - game.squareHeight - (float) descriptionAndPromptOfsset / 2 - descriptionFrameImg.getHeight());

        Texture logTabTexture = game.assetManager.get(GameSettings.getInstance().getLogTabFilePath(), Texture.class);
        logTabImg = new Image(logTabTexture);
        logTabImg.setSize(logBackgroundImg.getWidth() / 2 + 30, logBackgroundImg.getHeight() - 28);
        logTabImg.setPosition(logBackgroundImg.getX() + logTabImg.getWidth() - 50, logBackgroundImg.getY() + 3);

        Texture objectiveTabTexture = game.assetManager.get(GameSettings.getInstance().getObjectiveTabFilePath(), Texture.class);
        objectiveTabImg = new Image(objectiveTabTexture);
        objectiveTabImg.setSize(logBackgroundImg.getWidth() / 2, logBackgroundImg.getHeight() - 25);
        objectiveTabImg.setPosition(logBackgroundImg.getX() - 4, logBackgroundImg.getY() + 3);

        Texture frameTopTexture = game.assetManager.get(GameSettings.getInstance().getWoodenFrameTopFilePath(), Texture.class);
        frameTopImg = new Image(frameTopTexture);
        frameTopImg.setSize(V_WIDTH, V_HEIGHT - boardImg.getHeight() - middleHudBoundSize);
        frameTopImg.setPosition(0, boardImg.getHeight() + middleHudBoundSize);

        Texture frameMiddleTexture = game.assetManager.get(GameSettings.getInstance().getWoodenFrameMiddleFilePath(), Texture.class);
        frameMiddleImg = new Image(frameMiddleTexture);
        frameMiddleImg.setSize(20, V_HEIGHT - frameTopImg.getHeight() - middleHudBoundSize);
        frameMiddleImg.setPosition(boardImg.getWidth() + 1, middleHudBoundSize);

        frameBottomSprite = new Sprite(frameTopTexture);
        frameBottomSprite.setSize(V_WIDTH, V_HEIGHT - boardImg.getHeight() - middleHudBoundSize);
        frameBottomSprite.setPosition(0, -frameBottomSprite.getHeight() + middleHudBoundSize);
        frameBottomSprite.flip(false, true);

        frameLeftSprite = new Sprite(frameTopTexture);
        frameLeftSprite.setSize(V_WIDTH - frameTopImg.getHeight(), V_HEIGHT - boardImg.getHeight() - middleHudBoundSize);
        frameLeftSprite.setPosition(-V_WIDTH + 146, -boardImg.getHeight() + 15);
        frameLeftSprite.flip(false, true);
        frameLeftSprite.setRotation(270);

        frameRightSprite = new Sprite(frameTopTexture);
        frameRightSprite.setSize(V_WIDTH - frameTopImg.getHeight(), V_HEIGHT - boardImg.getHeight() - middleHudBoundSize);
        frameRightSprite.setPosition(77, boardImg.getHeight() - topHudBoundHeight - 58);
        frameRightSprite.flip(false, true);
        frameRightSprite.setRotation(90);

        stage.addActor(backgroundImg);
        stage.addActor(frameTopImg);
        stage.addActor(frameMiddleImg);
        stage.addActor(boardImg);
        stage.addActor(logBackgroundImg);
        stage.addActor(logTabImg);
        stage.addActor(objectiveTabImg);
        stage.addActor(playerTabImg);
        stage.addActor(resourcesTabImg);
        stage.addActor(descriptionFrameImg);
    }

    public void initSquares() {
        SquareNode currentSquare = game.gameController.getBoard().getStartSquare().getNextSquare();

        for (int i = 1; i < game.boardSize; i++) {
            if (currentSquare.getSquare().getSquareType() == Square.SquareType.TaskSquare) {
                currentSquare.getTaskSquare().getSquareImg().setSize(currentSquare.getSquare().getSquareCoordinates().getWidth(),
                        currentSquare.getSquare().getSquareCoordinates().getHeight());
                currentSquare.getTaskSquare().getSquareImg().setPosition(currentSquare.getTaskSquare().getSquareCoordinates().getX(),
                        currentSquare.getTaskSquare().getSquareCoordinates().getY());
                stage.addActor(currentSquare.getTaskSquare().getSquareImg());
            }
            else {
                currentSquare.getEventSquare().getSquareImg().setSize(currentSquare.getSquare().getSquareCoordinates().getWidth(),
                        currentSquare.getSquare().getSquareCoordinates().getHeight());
                currentSquare.getEventSquare().getSquareImg().setPosition(currentSquare.getEventSquare().getSquareCoordinates().getX(),
                        currentSquare.getEventSquare().getSquareCoordinates().getY());
                stage.addActor(currentSquare.getEventSquare().getSquareImg());
            }

            currentSquare = currentSquare.getNextSquare();
        }
    }

    public void initPlayerGamePieces() {
        for (int i = 0; i < game.gameController.getPlayersList().size(); i++) {
            Player player = game.gameController.getPlayersList().get(i);

            switch (i) {
                case 0:
                    player.getPlayerIcon().setSize(playerGamePieceSize, playerGamePieceSize);
                    player.getPlayerIcon().setPosition(player.getPlayerBoardPosition().getSquare().getSquareCoordinates().getX() + playerGamePieceSpacing,
                            player.getPlayerBoardPosition().getSquare().getSquareCoordinates().getY() + playerGamePieceSpacing);
                    break;
                case 1:
                    player.getPlayerIcon().setSize(playerGamePieceSize, playerGamePieceSize);
                    player.getPlayerIcon().setPosition(player.getPlayerBoardPosition().getSquare().getSquareCoordinates().getX() + playerGamePieceSpacing * 2 + playerGamePieceSize,
                            player.getPlayerBoardPosition().getSquare().getSquareCoordinates().getY() + playerGamePieceSpacing);
                    break;
                case 2:
                    player.getPlayerIcon().setSize(playerGamePieceSize, playerGamePieceSize);
                    player.getPlayerIcon().setPosition(player.getPlayerBoardPosition().getSquare().getSquareCoordinates().getX() + playerGamePieceSpacing,
                            player.getPlayerBoardPosition().getSquare().getSquareCoordinates().getY() + playerGamePieceSpacing * 2 + playerGamePieceSize);
                    break;
                case 3:
                    player.getPlayerIcon().setSize(playerGamePieceSize, playerGamePieceSize);
                    player.getPlayerIcon().setPosition(player.getPlayerBoardPosition().getSquare().getSquareCoordinates().getX() + playerGamePieceSpacing * 2 + playerGamePieceSize,
                            player.getPlayerBoardPosition().getSquare().getSquareCoordinates().getY() + playerGamePieceSpacing * 2 + playerGamePieceSize);
                    break;
            }

            stage.addActor(player.getPlayerIcon());
        }
    }

    public void initLabels() {
        moneyLabel = new Label("Money (Riel)", skin16);
        moneyLabel.setPosition(firstResourceLabelX - 6, firstResourceLabelY);

        labourLabel = new Label("Labour", skin16);
        labourLabel.setPosition(firstResourceLabelX + resourceLabelsSpacing - 20, firstResourceLabelY);

        communitySupportLabel = new Label("Community Support", skin16);
        communitySupportLabel.setPosition(firstResourceLabelX + resourceLabelsSpacing * 2 - 80, firstResourceLabelY);

        reputationLabel = new Label("Reputation", skin16);
        reputationLabel.setPosition(firstResourceLabelX + resourceLabelsSpacing * 3 - 38, firstResourceLabelY);

        descriptionLabel = new Label(descriptionText, skin15);
        descriptionLabel.setPosition(boardImg.getX() + boardImg.getWidth() / 2 - descriptionLabelXOffset,
                boardImg.getY() + boardImg.getHeight() / 2 + descriptionLabelYOffset - 120);
        descriptionLabel.setWidth(343);
        descriptionLabel.setWrap(true);

        activeObjectiveLabel = new Label(game.gameController.getActiveObjective().getDescription(), skin16);
        activeObjectiveLabel.setPosition(680,462);

        tasksLabel = new Label("Tasks:", skin16);
        tasksLabel.setPosition(680,410);

        currentObjectiveLabel = new Label("Current Objective:", skin16);
        currentObjectiveLabel.setPosition(680,482);

        for (int i = 0; i < game.gameController.getActiveObjective().getTaskList().size(); i++) {
            tasksLabelsList.add(new Label(game.gameController.getActiveObjective().getTaskList().get(i).getName(), skin16));
            tasksLabelsList.get(i).setPosition(680, 370 - 40 * i);

            stage.addActor(tasksLabelsList.get(i));
        }

        communitySupportLabelValue = new Label(communitySupportLabelsList.get(game.gameController.getResourcePool().getAllResourceTypesQuantity().
                get(Resources.ResourceTypes.CommunitySupport)), skin16);
        communitySupportLabelValue.setPosition(970, 580);

        logLabel = new Label("", skin16);
        logLabel.setPosition(975,265);

        promptLabel = new Label(promptText, skin16);
        promptLabel.setPosition(150, 465);
        promptLabel.setWidth(200);

        stage.addActor(moneyLabel);
        stage.addActor(labourLabel);
        stage.addActor(communitySupportLabel);
        stage.addActor(reputationLabel);
        stage.addActor(descriptionLabel);
        stage.addActor(activeObjectiveLabel);
        stage.addActor(currentObjectiveLabel);
        stage.addActor(tasksLabel);
        stage.addActor(communitySupportLabelValue);
        stage.addActor(logLabel);
        stage.addActor(promptLabel);

        for (int i = 0; i < Resources.ResourceTypes.values().length; i++) {
            resourceTypesLabels.put(Resources.ResourceTypes.values()[i],
                    new Label(game.gameController.getResourcePool().getAllResourceTypesQuantity().get(Resources.ResourceTypes.values()[i]).toString(), skin16));
            resourceTypesLabels.get(Resources.ResourceTypes.values()[i]).setPosition(firstResourceLabelX + resourceLabelsSpacing * i,
                    firstResourceLabelY - resourceValuesOffset);

            if (Resources.ResourceTypes.values()[i] != Resources.ResourceTypes.CommunitySupport)
                stage.addActor(resourceTypesLabels.get(Resources.ResourceTypes.values()[i]));
        }

        resourceTypesLabels.get(Resources.ResourceTypes.Money).setPosition(resourceTypesLabels.get(Resources.ResourceTypes.Money).getX() - 20,
                resourceTypesLabels.get(Resources.ResourceTypes.Money).getY());

        for (int i = 0; i < game.gameController.getPlayersList().size(); i++) {
            playerTasksLabels.put(game.gameController.getPlayersList().get(i), new Label("", skin16));
            playerNamesLabels.put(game.gameController.getPlayersList().get(i), new Label(game.gameController.getPlayersList().get(i).getPlayerName(), skin16));
            playerMoraleLabels.put(game.gameController.getPlayersList().get(i), new Label("", skin16));

            switch (i) {
                case 0:
                    playerTasksLabels.get(game.gameController.getPlayersList().get(i)).setPosition(firstPlayerButtonX + 12,
                            firstPlayerButtonY - playerTasksLabelsOffset + 2);
                    playerNamesLabels.get(game.gameController.getPlayersList().get(i)).setPosition(firstPlayerButtonX + playerButtonSize - 8,
                            firstPlayerButtonY - playerTasksLabelsOffset + playerButtonSize - 25);
                    playerMoraleLabels.get(game.gameController.getPlayersList().get(i)).setPosition(firstPlayerButtonX + playerButtonSize - 5,
                            firstPlayerButtonY - playerTasksLabelsOffset + playerButtonSize - 50);
                    break;
                case 1:
                    playerTasksLabels.get(game.gameController.getPlayersList().get(i)).setPosition(firstPlayerButtonX + playerButtonHorizontalSpacing + 12,
                            firstPlayerButtonY - playerTasksLabelsOffset + 2);
                    playerNamesLabels.get(game.gameController.getPlayersList().get(i)).setPosition(firstPlayerButtonX + playerButtonHorizontalSpacing + playerButtonSize - 8,
                            firstPlayerButtonY - playerTasksLabelsOffset + playerButtonSize - 25);
                    playerMoraleLabels.get(game.gameController.getPlayersList().get(i)).setPosition(firstPlayerButtonX + playerButtonHorizontalSpacing + playerButtonSize - 5,
                            firstPlayerButtonY - playerTasksLabelsOffset + playerButtonSize - 50);
                    break;
                case 2:
                    playerTasksLabels.get(game.gameController.getPlayersList().get(i)).setPosition(firstPlayerButtonX + 12,
                            firstPlayerButtonY - playerTasksLabelsOffset - playerButtonVerticalSpacing + 2);
                    playerNamesLabels.get(game.gameController.getPlayersList().get(i)).setPosition(firstPlayerButtonX + playerButtonSize - 8,
                            firstPlayerButtonY - playerTasksLabelsOffset - playerButtonVerticalSpacing + playerButtonSize - 25);
                    playerMoraleLabels.get(game.gameController.getPlayersList().get(i)).setPosition(firstPlayerButtonX + playerButtonSize - 5,
                            firstPlayerButtonY - playerTasksLabelsOffset - playerButtonVerticalSpacing + playerButtonSize - 50);
                    break;
                case 3:
                    playerTasksLabels.get(game.gameController.getPlayersList().get(i)).setPosition(firstPlayerButtonX + playerButtonHorizontalSpacing + 12,
                            firstPlayerButtonY - playerTasksLabelsOffset - playerButtonVerticalSpacing + 2);
                    playerNamesLabels.get(game.gameController.getPlayersList().get(i)).setPosition(firstPlayerButtonX + playerButtonHorizontalSpacing + playerButtonSize - 8,
                            firstPlayerButtonY - playerTasksLabelsOffset - playerButtonVerticalSpacing + playerButtonSize - 25);
                    playerMoraleLabels.get(game.gameController.getPlayersList().get(i)).setPosition(firstPlayerButtonX + playerButtonHorizontalSpacing + playerButtonSize - 5,
                            firstPlayerButtonY - playerTasksLabelsOffset - playerButtonVerticalSpacing + playerButtonSize - 50);
                    break;
            }

            stage.addActor(playerTasksLabels.get(game.gameController.getPlayersList().get(i)));
            stage.addActor(playerNamesLabels.get(game.gameController.getPlayersList().get(i)));
            stage.addActor(playerMoraleLabels.get(game.gameController.getPlayersList().get(i)));
        }
    }

    public void updateLabels() {
        for (Resources.ResourceTypes resourceType : Resources.ResourceTypes.values()) {
            if (resourceType != Resources.ResourceTypes.CommunitySupport)
                resourceTypesLabels.get(resourceType).setText(game.gameController.getResourcePool().getAllResourceTypesQuantity().get(resourceType));
            else {
                if (game.gameController.getResourcePool().getAllResourceTypesQuantity().get(resourceType) > 4)
                    game.gameController.getResourcePool().modifyResource(resourceType,
                            game.gameController.getResourcePool().getAllResourceTypesQuantity().get(resourceType) - 4, false);
                communitySupportLabelValue.setText(communitySupportLabelsList.get(game.gameController.getResourcePool().getAllResourceTypesQuantity().get(resourceType)));
            }
        }

        resourceTypesLabels.get(Resources.ResourceTypes.Money).setText(resourceTypesLabels.get(Resources.ResourceTypes.Money).getText() + " KHR");

        descriptionLabel.setText("");
        descriptionLabel.setText(descriptionText);

        promptLabel.setText("");
        promptLabel.setText(promptText);

        for (int i = 0; i < game.gameController.getActiveObjective().getTaskList().size(); i++) {
            tasksLabelsList.get(i).setText("");
            tasksLabelsList.get(i).setText(game.gameController.getActiveObjective().getTaskList().get(i).getTaskNumber() + ". "
                    + game.gameController.getActiveObjective().getTaskList().get(i).getName());
        }
    }

    public void updatePlayerDetailsLabels() {
        for (Player player : game.gameController.getPlayersList()) {
            playerTasksLabels.get(player).setText("");

            if (!player.getCollectedTasks().isEmpty())
                playerTasksLabels.get(player).setText("Owned Tasks:\n");

            for (Task task : player.getCollectedTasks())
                playerTasksLabels.get(player).setText(playerTasksLabels.get(player).getText() + "Task " + task.getTaskNumber() + " ");


            playerMoraleLabels.get(player).setText("Morale: " + player.getMorale());
        }
    }

    public void updateActiveObjectiveLabel() {
        activeObjectiveLabel.setText(game.gameController.getActiveObjective().getDescription());
    }

    public void updateButtonStates() {
        for (Player player : game.gameController.getPlayersList())
            playerButtons.get(player).setTouchable(playerButtonsState);

        yesButton.setTouchable(yesButtonState);
        if (yesButtonState == Touchable.enabled)
            yesButton.getStyle().up = skin16.getDrawable("yesButtonTexture");
        else
            yesButton.getStyle().up = skin16.getDrawable("yesButtonOffTexture");

        noButton.setTouchable(noButtonState);
        if (noButtonState == Touchable.enabled)
            noButton.getStyle().up = skin16.getDrawable("noButtonTexture");
        else
            noButton.getStyle().up = skin16.getDrawable("noButtonOffTexture");

        transferTaskButton.setTouchable(transferTaskButtonState);
        if (transferTaskButtonState == Touchable.enabled)
            transferTaskButton.getStyle().up = skin16.getDrawable("transferTaskButtonTexture");
        else
            transferTaskButton.getStyle().up = skin16.getDrawable("transferTaskButtonOffTexture");

        dieButton.setTouchable(dieButtonState);
        coinFlipButton.setTouchable(coinFlipButtonState);
    }

    public void resetButtonStates() {
        yesButtonState = Touchable.disabled;
        noButtonState = Touchable.disabled;
        transferTaskButtonState = Touchable.disabled;
        dieButtonState =  Touchable.enabled;
        coinFlipButtonState = Touchable.disabled;
        playerButtonsState = Touchable.disabled;
    }

    public void updateActivePlayerButton() {
        for (Player player : game.gameController.getPlayersList()) {
            if (player != game.gameController.getActivePlayer()) {
                switch (player.getPlayerExpertise()) {
                    case Manager:
                        playerButtons.get(player).getStyle().up = skin16.getDrawable("managerTexture");
                        break;
                    case CivilEngineer:
                        playerButtons.get(player).getStyle().up = skin16.getDrawable("civilEngineerTexture");
                        break;
                    case ChemicalEngineer:
                        playerButtons.get(player).getStyle().up = skin16.getDrawable("chemicalEngineerTexture");
                        break;
                    case CommunityLeader:
                        playerButtons.get(player).getStyle().up = skin16.getDrawable("communityLeaderTexture");
                        break;
                    case Salesperson:
                        playerButtons.get(player).getStyle().up = skin16.getDrawable("salespersonTexture");
                        break;
                    case LuckyPerson:
                        playerButtons.get(player).getStyle().up = skin16.getDrawable("luckyPersonTexture");
                        break;
                    case EnvironmentalScientist:
                        playerButtons.get(player).getStyle().up = skin16.getDrawable("environmentalScientistTexture");
                        break;
                }
            }
            else {
                switch (player.getPlayerExpertise()) {
                    case Manager:
                        playerButtons.get(player).getStyle().up = skin16.getDrawable("managerGlowingTexture");
                        break;
                    case CivilEngineer:
                        playerButtons.get(player).getStyle().up = skin16.getDrawable("civilEngineerGlowingTexture");
                        break;
                    case ChemicalEngineer:
                        playerButtons.get(player).getStyle().up = skin16.getDrawable("chemicalEngineerGlowingTexture");
                        break;
                    case CommunityLeader:
                        playerButtons.get(player).getStyle().up = skin16.getDrawable("communityLeaderGlowingTexture");
                        break;
                    case Salesperson:
                        playerButtons.get(player).getStyle().up = skin16.getDrawable("salespersonGlowingTexture");
                        break;
                    case LuckyPerson:
                        playerButtons.get(player).getStyle().up = skin16.getDrawable("luckyPersonGlowingTexture");
                        break;
                    case EnvironmentalScientist:
                        playerButtons.get(player).getStyle().up = skin16.getDrawable("environmentalScientistGlowingTexture");
                        break;
                }
            }
        }
    }

    public void populateSkinWithPlayerButtons() {
        Texture managerTexture = game.assetManager.get(GameSettings.getInstance().getManagerFilePath(), Texture.class);
        skin16.add("managerTexture", managerTexture);

        Texture civilEngineerTexture = game.assetManager.get(GameSettings.getInstance().getCivilEngineerFilePath(), Texture.class);
        skin16.add("civilEngineerTexture", civilEngineerTexture);

        Texture chemicalEngineerTexture = game.assetManager.get(GameSettings.getInstance().getChemicalEngineerFilePath(), Texture.class);
        skin16.add("chemicalEngineerTexture", chemicalEngineerTexture);

        Texture communityLeaderTexture = game.assetManager.get(GameSettings.getInstance().getCommunityLeaderFilePath(), Texture.class);
        skin16.add("communityLeaderTexture", communityLeaderTexture);

        Texture salespersonTexture = game.assetManager.get(GameSettings.getInstance().getSalespersonFilePath(), Texture.class);
        skin16.add("salespersonTexture", salespersonTexture);

        Texture luckyPersonTexture = game.assetManager.get(GameSettings.getInstance().getLuckyPersonFilePath(), Texture.class);
        skin16.add("luckyPersonTexture", luckyPersonTexture);

        Texture environmentalScientistTexture = game.assetManager.get(GameSettings.getInstance().getEnvironmentalScientistFilePath(), Texture.class);
        skin16.add("environmentalScientistTexture", environmentalScientistTexture);

        Texture managerGlowingTexture = game.assetManager.get(GameSettings.getInstance().getManagerGlowingFilePath(), Texture.class);
        skin16.add("managerGlowingTexture", managerGlowingTexture);

        Texture civilEngineerGlowingTexture = game.assetManager.get(GameSettings.getInstance().getCivilEngineerGlowingFilePath(), Texture.class);
        skin16.add("civilEngineerGlowingTexture", civilEngineerGlowingTexture);

        Texture chemicalEngineerGlowingTexture = game.assetManager.get(GameSettings.getInstance().getChemicalEngineerGlowingFilePath(), Texture.class);
        skin16.add("chemicalEngineerGlowingTexture", chemicalEngineerGlowingTexture);

        Texture communityLeaderGlowingTexture = game.assetManager.get(GameSettings.getInstance().getCommunityLeaderGlowingFilePath(), Texture.class);
        skin16.add("communityLeaderGlowingTexture", communityLeaderGlowingTexture);

        Texture salespersonGlowingTexture = game.assetManager.get(GameSettings.getInstance().getSalespersonGlowingFilePath(), Texture.class);
        skin16.add("salespersonGlowingTexture", salespersonGlowingTexture);

        Texture luckyPersonGlowingTexture = game.assetManager.get(GameSettings.getInstance().getLuckyPersonGlowingFilePath(), Texture.class);
        skin16.add("luckyPersonGlowingTexture", luckyPersonGlowingTexture);

        Texture environmentalScientistGlowingTexture = game.assetManager.get(GameSettings.getInstance().getEnvironmentalScientistGlowingFilePath(), Texture.class);
        skin16.add("environmentalScientistGlowingTexture", environmentalScientistGlowingTexture);
    }

    public void populateSkinWithUiButtons() {
        Texture yesButtonTexture = game.assetManager.get(GameSettings.getInstance().getYesButtonFilePath(), Texture.class);
        skin16.add("yesButtonTexture", yesButtonTexture);

        Texture noButtonTexture = game.assetManager.get(GameSettings.getInstance().getNoButtonFilePath(), Texture.class);
        skin16.add("noButtonTexture", noButtonTexture);

        Texture transferTaskButtonTexture = game.assetManager.get(GameSettings.getInstance().getTransferTaskButtonFilePath(), Texture.class);
        skin16.add("transferTaskButtonTexture", transferTaskButtonTexture);

        Texture yesButtonOffTexture = game.assetManager.get(GameSettings.getInstance().getYesButtonOffFilePath(), Texture.class);
        skin16.add("yesButtonOffTexture", yesButtonOffTexture);

        Texture noButtonOffTexture = game.assetManager.get(GameSettings.getInstance().getNoButtonOffFilePath(), Texture.class);
        skin16.add("noButtonOffTexture", noButtonOffTexture);

        Texture transferTaskButtonOffTexture = game.assetManager.get(GameSettings.getInstance().getTransferTaskButtonOffFilePath(), Texture.class);
        skin16.add("transferTaskButtonOffTexture", transferTaskButtonOffTexture);
    }

    public void populateSkinWithDieButtons() {
        dieTextureStrings.add("die1Texture");
        dieTextureStrings.add("die2Texture");
        dieTextureStrings.add("die3Texture");
        dieTextureStrings.add("die4Texture");
        dieTextureStrings.add("die5Texture");
        dieTextureStrings.add("die6Texture");

        Texture die1Texture = game.assetManager.get(GameSettings.getInstance().getDie1FilePath(), Texture.class);
        skin16.add(dieTextureStrings.get(0), die1Texture);

        Texture die2Texture = game.assetManager.get(GameSettings.getInstance().getDie2FilePath(), Texture.class);
        skin16.add(dieTextureStrings.get(1), die2Texture);

        Texture die3Texture = game.assetManager.get(GameSettings.getInstance().getDie3FilePath(), Texture.class);
        skin16.add(dieTextureStrings.get(2), die3Texture);

        Texture die4Texture = game.assetManager.get(GameSettings.getInstance().getDie4FilePath(), Texture.class);
        skin16.add(dieTextureStrings.get(3), die4Texture);

        Texture die5Texture = game.assetManager.get(GameSettings.getInstance().getDie5FilePath(), Texture.class);
        skin16.add(dieTextureStrings.get(4), die5Texture);

        Texture die6Texture = game.assetManager.get(GameSettings.getInstance().getDie6FilePath(), Texture.class);
        skin16.add(dieTextureStrings.get(5), die6Texture);
    }

    public void populateSkinWithCoinFlipTextures() {
        coinFlipTextureStrings.add("coinFlip1Texture");
        coinFlipTextureStrings.add("coinFlip2Texture");
        coinFlipTextureStrings.add("coinFlip3Texture");
        coinFlipTextureStrings.add("coinFlip4Texture");
        coinFlipTextureStrings.add("coinFlip5Texture");
        coinFlipTextureStrings.add("coinFlip6Texture");
        coinFlipTextureStrings.add("coinFlip7Texture");
        coinFlipTextureStrings.add("coinFlip8Texture");
        coinFlipTextureStrings.add("coinFlip9Texture");
        coinFlipTextureStrings.add("coinFlip10Texture");
        coinFlipTextureStrings.add("coinFlip11Texture");
        coinFlipTextureStrings.add("coinFlip12Texture");
        coinFlipTextureStrings.add("coinFlip13Texture");
        coinFlipTextureStrings.add("coinFlip14Texture");
        coinFlipTextureStrings.add("coinFlip15Texture");
        coinFlipTextureStrings.add("coinFlip16Texture");
        coinFlipTextureStrings.add("coinFlip17Texture");
        coinFlipTextureStrings.add("coinFlip18Texture");
        coinFlipTextureStrings.add("coinFlip19Texture");
        coinFlipTextureStrings.add("coinFlip20Texture");

        Texture coinFlip1Texture = game.assetManager.get(GameSettings.getInstance().getCoinFlipIcon1FilePath(), Texture.class);
        skin16.add(coinFlipTextureStrings.get(0), coinFlip1Texture);

        Texture coinFlip2Texture = game.assetManager.get(GameSettings.getInstance().getCoinFlipIcon2FilePath(), Texture.class);
        skin16.add(coinFlipTextureStrings.get(1), coinFlip2Texture);

        Texture coinFlip3Texture = game.assetManager.get(GameSettings.getInstance().getCoinFlipIcon3FilePath(), Texture.class);
        skin16.add(coinFlipTextureStrings.get(2), coinFlip3Texture);

        Texture coinFlip4Texture = game.assetManager.get(GameSettings.getInstance().getCoinFlipIcon4FilePath(), Texture.class);
        skin16.add(coinFlipTextureStrings.get(3), coinFlip4Texture);

        Texture coinFlip5Texture = game.assetManager.get(GameSettings.getInstance().getCoinFlipIcon5FilePath(), Texture.class);
        skin16.add(coinFlipTextureStrings.get(4), coinFlip5Texture);

        Texture coinFlip6Texture = game.assetManager.get(GameSettings.getInstance().getCoinFlipIcon6FilePath(), Texture.class);
        skin16.add(coinFlipTextureStrings.get(5), coinFlip6Texture);

        Texture coinFlip7Texture = game.assetManager.get(GameSettings.getInstance().getCoinFlipIcon7FilePath(), Texture.class);
        skin16.add(coinFlipTextureStrings.get(6), coinFlip7Texture);

        Texture coinFlip8Texture = game.assetManager.get(GameSettings.getInstance().getCoinFlipIcon8FilePath(), Texture.class);
        skin16.add(coinFlipTextureStrings.get(7), coinFlip8Texture);

        Texture coinFlip9Texture = game.assetManager.get(GameSettings.getInstance().getCoinFlipIcon9FilePath(), Texture.class);
        skin16.add(coinFlipTextureStrings.get(8), coinFlip9Texture);

        Texture coinFlip10Texture = game.assetManager.get(GameSettings.getInstance().getCoinFlipIcon10FilePath(), Texture.class);
        skin16.add(coinFlipTextureStrings.get(9), coinFlip10Texture);

        Texture coinFlip11Texture = game.assetManager.get(GameSettings.getInstance().getCoinFlipIcon11FilePath(), Texture.class);
        skin16.add(coinFlipTextureStrings.get(10), coinFlip11Texture);

        Texture coinFlip12Texture = game.assetManager.get(GameSettings.getInstance().getCoinFlipIcon12FilePath(), Texture.class);
        skin16.add(coinFlipTextureStrings.get(11), coinFlip12Texture);

        Texture coinFlip13Texture = game.assetManager.get(GameSettings.getInstance().getCoinFlipIcon13FilePath(), Texture.class);
        skin16.add(coinFlipTextureStrings.get(12), coinFlip13Texture);

        Texture coinFlip14Texture = game.assetManager.get(GameSettings.getInstance().getCoinFlipIcon14FilePath(), Texture.class);
        skin16.add(coinFlipTextureStrings.get(13), coinFlip14Texture);

        Texture coinFlip15Texture = game.assetManager.get(GameSettings.getInstance().getCoinFlipIcon15FilePath(), Texture.class);
        skin16.add(coinFlipTextureStrings.get(14), coinFlip15Texture);

        Texture coinFlip16Texture = game.assetManager.get(GameSettings.getInstance().getCoinFlipIcon16FilePath(), Texture.class);
        skin16.add(coinFlipTextureStrings.get(15), coinFlip16Texture);

        Texture coinFlip17Texture = game.assetManager.get(GameSettings.getInstance().getCoinFlipIcon17FilePath(), Texture.class);
        skin16.add(coinFlipTextureStrings.get(16), coinFlip17Texture);

        Texture coinFlip18Texture = game.assetManager.get(GameSettings.getInstance().getCoinFlipIcon18FilePath(), Texture.class);
        skin16.add(coinFlipTextureStrings.get(17), coinFlip18Texture);

        Texture coinFlip19Texture = game.assetManager.get(GameSettings.getInstance().getCoinFlipIcon19FilePath(), Texture.class);
        skin16.add(coinFlipTextureStrings.get(18), coinFlip19Texture);

        Texture coinFlip20Texture = game.assetManager.get(GameSettings.getInstance().getCoinFlipIcon20FilePath(), Texture.class);
        skin16.add(coinFlipTextureStrings.get(19), coinFlip20Texture);


        Texture coinFlipTrueTexture = game.assetManager.get(GameSettings.getInstance().getCoinFlipTrueFilePath(), Texture.class);
        skin16.add("coinFlipTrueTexture", coinFlipTrueTexture);

        Texture coinFlipFalseTexture = game.assetManager.get(GameSettings.getInstance().getCoinFlipFalseFilePath(), Texture.class);
        skin16.add("coinFlipFalseTexture", coinFlipFalseTexture);
    }

    public void initPlayerButtons() {
        Button.ButtonStyle managerButtonStyle = new Button.ButtonStyle();
        managerButtonStyle.up = skin16.getDrawable("managerTexture");

        Button.ButtonStyle civilEngineerButtonStyle = new Button.ButtonStyle();
        civilEngineerButtonStyle.up = skin16.getDrawable("civilEngineerTexture");

        Button.ButtonStyle chemicalEngineerButtonStyle = new Button.ButtonStyle();
        chemicalEngineerButtonStyle.up = skin16.getDrawable("chemicalEngineerTexture");

        Button.ButtonStyle communityLeaderButtonStyle = new Button.ButtonStyle();
        communityLeaderButtonStyle.up = skin16.getDrawable("communityLeaderTexture");

        Button.ButtonStyle salespersonButtonStyle = new Button.ButtonStyle();
        salespersonButtonStyle.up = skin16.getDrawable("salespersonTexture");

        Button.ButtonStyle luckyPersonButtonStyle = new Button.ButtonStyle();
        luckyPersonButtonStyle.up = skin16.getDrawable("luckyPersonTexture");

        Button.ButtonStyle environmentalScientistButtonStyle = new Button.ButtonStyle();
        environmentalScientistButtonStyle.up = skin16.getDrawable("environmentalScientistTexture");

        for (Player player : game.gameController.getPlayersList()) {
            switch (player.getPlayerExpertise()) {
                case Manager:
                    playerButtons.put(player, new Button(managerButtonStyle));
                    break;
                case CivilEngineer:
                    playerButtons.put(player, new Button(civilEngineerButtonStyle));
                    break;
                case ChemicalEngineer:
                    playerButtons.put(player, new Button(chemicalEngineerButtonStyle));
                    break;
                case CommunityLeader:
                    playerButtons.put(player, new Button(communityLeaderButtonStyle));
                    break;
                case Salesperson:
                    playerButtons.put(player, new Button(salespersonButtonStyle));
                    break;
                case LuckyPerson:
                    playerButtons.put(player, new Button(luckyPersonButtonStyle));
                    break;
                case EnvironmentalScientist:
                    playerButtons.put(player, new Button(environmentalScientistButtonStyle));
                    break;
            }

            playerButtons.get(player).setSize(playerButtonSize, playerButtonSize);
            playerButtons.get(player).setTouchable(Touchable.disabled);
            playerButtons.get(player).addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (game.gameController.getActivePlayer() != player) {
                        buttonClickSound.setVolume(buttonClickSound.play(), game.defaultButtonClickSoundVolume);
                        playerButtonsState = Touchable.disabled;
                        dieButtonState = Touchable.enabled;
                        if (game.gameController.getActivePlayer().transferTask(player)) {
                            promptText = "Task transferred";

                            if (!player.getCollectedTasks().getFirst().getCanBeTransferred())
                                addMessageToLog(game.gameController.getActivePlayer().getPlayerName() + " transferred task " +
                                    player.getCollectedTasks().getFirst().getTaskNumber() + " to " + player.getPlayerName());
                            else
                                addMessageToLog(game.gameController.getActivePlayer().getPlayerName() + " transferred task " +
                                        player.getCollectedTasks().getLast().getTaskNumber() + " to " + player.getPlayerName());
                        }
                        else {
                            promptText = "Task not transferred";
                            transferTaskButtonState = Touchable.enabled;
                        }
                    }
                }
            });

            stage.addActor(playerButtons.get(player));
        }

        playerButtons.get(game.gameController.getPlayersList().get(0)).setPosition(firstPlayerButtonX, firstPlayerButtonY);
        playerButtons.get(game.gameController.getPlayersList().get(1)).setPosition(firstPlayerButtonX + playerButtonHorizontalSpacing, firstPlayerButtonY);

        if (playerButtons.size() >= 3)
            playerButtons.get(game.gameController.getPlayersList().get(2)).setPosition(firstPlayerButtonX, firstPlayerButtonY - playerButtonVerticalSpacing);

        if (playerButtons.size() == 4)
            playerButtons.get(game.gameController.getPlayersList().get(3)).setPosition(firstPlayerButtonX + playerButtonHorizontalSpacing,
                firstPlayerButtonY - playerButtonVerticalSpacing);
    }

    public void initSounds() {
        buttonClickSound = game.assetManager.get(GameSettings.getInstance().getButtonSoundFilePath());
        dieRollSound = game.assetManager.get(GameSettings.getInstance().getDieRollSoundFilePath());
        coinTossSound = game.assetManager.get(GameSettings.getInstance().getCoinTossSoundFilePath());
    }

    public void populateSkinWithTextures() {
        populateSkinWithPlayerButtons();
        populateSkinWithUiButtons();
        populateSkinWithDieButtons();
        populateSkinWithCoinFlipTextures();
    }

    public void animateDie() {
        dieRollAnimationDeltaTime = 0;
    }

    public void animateCoin() {
        coinFlipFrameNumber = 0;
    }

    public void resetAnimatedElements() {
        dieButton.getStyle().up = skin16.getDrawable("die6Texture");

        coinFlipButton.getStyle().up = skin16.getDrawable("coinFlip1Texture");
        currentCoinFlipAnimationFrame = "coinFlip1Texture";

        coinFlipFrameNumber = Integer.MAX_VALUE;
        dieRollAnimationDeltaTime = 1;
        coinFlipAnimationDeltaTime = 1;
        playerMoveCounter = 0;

        descriptionText = "";
        promptText = "Welcome! Roll die to start";
        logMessagesList.clear();
    }

    public void addMessageToLog(String newMessage) {
        logMessagesList.add(newMessage);

        updateLog();
    }

    public int findLastNewLineIndex(String message) {
        for (int i = message.length() - 1; i >= 0; i--)
            if (message.charAt(i) == '\n')
                return i;
        return -1;
    }

    public void updateLog() {
        logLabel.setText("");

        if (!logMessagesList.isEmpty()) {
            while (needsWrapping(logMessagesList.getLast())) {
                int newLineIndexGuess = findLastNewLineIndex(logMessagesList.getLast()) + maxLogCharacters;
                for (int i = 0; i < maxLogCharacters + 1; i++) {
                    if (newLineIndexGuess - i >= 0 && newLineIndexGuess - i < logMessagesList.getLast().length()) {
                        if (logMessagesList.getLast().charAt(newLineIndexGuess - i) == ' ') {
                            StringBuilder stringBuilder = new StringBuilder(logMessagesList.getLast());
                            stringBuilder.deleteCharAt(newLineIndexGuess - i);
                            stringBuilder.insert(newLineIndexGuess - i, '\n');
                            logMessagesList.removeLast();
                            logMessagesList.add(stringBuilder.toString());
                            break;
                        }
                    }
                }
            }
        }

        while (countNewLineCharactersInLog() + logMessagesList.size() > maxLogMessages)
            logMessagesList.removeFirst();

        for (String message : logMessagesList) {
            logLabel.setText(logLabel.getText() + message + "\n");
        }

        for (int i = 0; i < logMessagesList.size() + countNewLineCharactersInLog(); i++)
            logLabel.setText(logLabel.getText() + "\n");
    }

    public int countNewLineCharactersInLog() {
        int newLineCounter = 0;

        for (String message : logMessagesList)
            for (int i = 0; i < message.length(); i++)
                if (message.charAt(i) == '\n')
                    newLineCounter++;

        return newLineCounter;
    }

    public Boolean needsWrapping(String message) {
        int charactersInLineCounter = 0;
        int charactersInLineCounterMax = 0;
        int charactersWithoutSpaceOrNewLineCounter = 0;

        for (int i = 0; i < message.length(); i++) {
            if (message.charAt(i) == '\n') {
                charactersInLineCounter = 0;
                charactersWithoutSpaceOrNewLineCounter = 0;
            }
            else {
                if (message.charAt(i) == ' ')
                    charactersWithoutSpaceOrNewLineCounter = 0;

                charactersWithoutSpaceOrNewLineCounter++;
                charactersInLineCounter++;
                if (charactersInLineCounterMax < charactersInLineCounter)
                    charactersInLineCounterMax = charactersInLineCounter;
            }
            if (charactersWithoutSpaceOrNewLineCounter > maxLogCharacters)
                return false;
        }

        return charactersInLineCounterMax > maxLogCharacters;
    }

    public void nextTurn() {
        dieButtonState = Touchable.enabled;
        yesButtonState = Touchable.disabled;
        noButtonState = Touchable.disabled;

        if (dieRolled) {
            game.gameController.nextTurn();
            descriptionText = "";

            dieRolled = false;
            objectiveCompletedThisTurn = false;

            if (game.gameController.tryCompleteActiveObjective() && !game.gameController.getNegateObjectiveCompletion()) {
                skipNextNextTurn = true;

                promptText = "Complete current objective?";
                descriptionText = game.gameController.getActiveObjective().getDescription() + "\nTotal cost: " +
                        game.gameController.getActiveObjectiveResourceCostWithDiscount().getAllResourceTypesQuantity().get(Resources.ResourceTypes.Money) + " KHR";
                yesButtonState = Touchable.enabled;
                noButtonState = Touchable.enabled;
                dieButtonState = Touchable.disabled;
            }

            if (game.gameController.getActivePlayer().canTransferOrReceiveTask())
                transferTaskButtonState = Touchable.enabled;
        }
    }
}
