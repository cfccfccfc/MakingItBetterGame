package com.cfc.makingitbettergame.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.cfc.makingitbettergame.GameClasses.Miscellaneous.GameSettings;
import com.cfc.makingitbettergame.GameClasses.PlayerClasses.Player;
import com.cfc.makingitbettergame.MakingItBetterGame;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.GL20;

import java.util.*;
import java.util.List;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static com.cfc.makingitbettergame.MakingItBetterGame.*;

public class MainMenuScreen implements Screen {
    private final MakingItBetterGame game;
    private Stage stage;
    private Skin skin;
    private ShapeRenderer shapeRenderer;

    private int currentPlayerExpertiseSelectionIndex;
    private List<Player.PlayerExpertise> playerExpertiseList;
    private List<Image> playerExpertiseIcons;
    private List<Texture> playerExpertiseIconTexturesList;
    private int iconSize = 75;

    private Button startButton, quitButton, addPlayerButton, removePlayerButton, manualButton, leftArrowButton, rightArrowButton;
    private int centralButtonsVerticalBottomOffset = 265;
    private int buttonsSpacing = 5;
    private int sideButtonsVerticalTopOffset = 320;
    private int sideButtonsHorizontalRightSideOffset = 10;

    private int arrowButtonWidth = 50, arrowButtonHeight = 50;

    private List<TextButton> playerLabelsList;
    private int labelVerticalTopOffset = sideButtonsVerticalTopOffset - 233;
    private int labelsSpacing = 1;


    private TextField textField;

    private Image logoImg;
    private int logoImgVerticalCenterOffset = 100;

    private Image backgroundImg;

    private Image playersTableImg;
    private int namesTableVerticalTopOffset = sideButtonsVerticalTopOffset - 237;

    private Image playerExpertiseTableImg;

    private int nameCharacterLimit = GameSettings.getInstance().getNameCharacterLimit();

    private Sound buttonClickSound;

    public MainMenuScreen(MakingItBetterGame game) {
        this.game = game;
        this.shapeRenderer = new ShapeRenderer();
        this.stage = new Stage(new FitViewport(MakingItBetterGame.V_WIDTH, MakingItBetterGame.V_HEIGHT, game.gameCam));
    }
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        stage.clear();

        this.skin = game.skin;
        currentPlayerExpertiseSelectionIndex = 0;

        buttonClickSound = game.assetManager.get(GameSettings.getInstance().getButtonSoundFilePath());

        Texture addPlayerButtonOnTexture = game.assetManager.get(GameSettings.getInstance().getAddPlayerButtonFilePath(), Texture.class);
        skin.add("addPlayerButtonOnTexture", addPlayerButtonOnTexture);
        Texture addPlayerButtonOffTexture = game.assetManager.get(GameSettings.getInstance().getAddPlayerButtonOffFilePath(), Texture.class);
        skin.add("addPlayerButtonOffTexture", addPlayerButtonOffTexture);

        Texture logoTexture = game.assetManager.get(GameSettings.getInstance().getGameLogoFilePath(), Texture.class);
        logoImg = new Image(logoTexture);
        logoImg.setSize(680, 384);
        logoImg.setOrigin(logoImg.getWidth() / 2, logoImg.getHeight() / 2);
        logoImg.setPosition(stage.getWidth() / 2 - logoImg.getWidth() / 2, stage.getHeight() / 2 + logoImg.getHeight() / 2);
        logoImg.addAction(sequence(alpha(0), scaleTo(.01f, .01f),
                parallel(fadeIn(.7f, Interpolation.pow2),
                        scaleTo(1f, 1f, 1.1f, Interpolation.pow5),
                        moveTo(stage.getWidth() / 2 - logoImg.getWidth() / 2,
                                stage.getHeight() / 2 - logoImg.getHeight() / 2 + logoImgVerticalCenterOffset, .7f, Interpolation.swing))));

        Texture backgroundTexture = game.assetManager.get(GameSettings.getInstance().getGameBackgroundFilePath(), Texture.class);
        backgroundImg = new Image(backgroundTexture);
        backgroundImg.setPosition(0, 0);
        backgroundImg.setSize(MakingItBetterGame.V_WIDTH, MakingItBetterGame.V_HEIGHT);

        stage.addActor(backgroundImg);
        stage.addActor(logoImg);

        playerLabelsList = new ArrayList<>();

        playerExpertiseList = new ArrayList<>();
        Collections.addAll(playerExpertiseList, Player.PlayerExpertise.values());

        playerExpertiseIcons = new ArrayList<>();

        playerExpertiseIconTexturesList = new ArrayList<>();

        initTextField();

        initTables();

        initButtons();

        initPlayerIcons();

        for (Player player : game.gameController.getPlayersList())
            playerLabelsList.add(new TextButton(player.getPlayerName(), skin, "default"));

        drawLabels();
    }

    @Override
    public void render(float v) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(v);

        stage.draw();
    }

    public void update(float v) {
        stage.act(v);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, false);
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
        stage.dispose();
        shapeRenderer.dispose();
    }

    private void initTextField() {
        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();

        textFieldStyle.font = game.fontMainMenu;

        textFieldStyle.fontColor = com.badlogic.gdx.graphics.Color.WHITE;

        textFieldStyle.cursor = new NinePatchDrawable(skin.getPatch("cursor"));
        textFieldStyle.background = skin.getDrawable("textfield");

        textField = new TextField("", textFieldStyle);
        textField.setSize(textButtonDefaultWidth * 2f, (float) textButtonDefaultHeight / 2);
        textField.setPosition(V_WIDTH - textField.getWidth() - sideButtonsHorizontalRightSideOffset,
                V_HEIGHT - sideButtonsVerticalTopOffset);
        textField.setAlignment(1);

        if (game.gameController.getPlayersList().size() >= 4) {
            textField.setMessageText("Maximum players added");
            textField.setDisabled(true);
        }
        else
            textField.setMessageText("Input player name");

        stage.addActor(textField);
    }

    private void initButtons() {
        Texture leftArrowButtonTexture = game.assetManager.get(GameSettings.getInstance().getLeftArrowButtonFilePath(), Texture.class);
        Texture rightArrowButtonTexture = game.assetManager.get(GameSettings.getInstance().getRightArrowButtonFilePath(), Texture.class);
        Texture leftArrowButtonPressedTexture = game.assetManager.get(GameSettings.getInstance().getLeftArrowButtonPressedFilePath(), Texture.class);
        Texture rightArrowButtonPressedTexture = game.assetManager.get(GameSettings.getInstance().getRightArrowButtonPressedFilePath(), Texture.class);

        Button.ButtonStyle buttonStyleLeftArrow = new Button.ButtonStyle();
        Button.ButtonStyle buttonStyleRightArrow = new Button.ButtonStyle();

        Drawable drawableUpLeftArrow = new NinePatchDrawable(new NinePatch(leftArrowButtonTexture));
        Drawable drawableUpRightArrow = new NinePatchDrawable(new NinePatch(rightArrowButtonTexture));
        Drawable drawableUpLeftArrowPressed = new NinePatchDrawable(new NinePatch(leftArrowButtonPressedTexture));
        Drawable drawableUpRightArrowPressed = new NinePatchDrawable(new NinePatch(rightArrowButtonPressedTexture));

        buttonStyleLeftArrow.up = drawableUpLeftArrow;
        buttonStyleRightArrow.up = drawableUpRightArrow;
        buttonStyleLeftArrow.down = drawableUpLeftArrowPressed;
        buttonStyleRightArrow.down = drawableUpRightArrowPressed;

        leftArrowButton = new Button(buttonStyleLeftArrow);
        rightArrowButton = new Button(buttonStyleRightArrow);

        leftArrowButton.setSize(arrowButtonWidth, arrowButtonHeight);
        leftArrowButton.setPosition(textField.getX() + 27, textField.getY() + arrowButtonHeight - 10);

        rightArrowButton.setSize(arrowButtonWidth, arrowButtonHeight);
        rightArrowButton.setPosition(textField.getX() + textField.getWidth() - arrowButtonWidth - 27, textField.getY() + arrowButtonHeight - 10);

        leftArrowButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                buttonClickSound.setVolume(buttonClickSound.play(), game.defaultButtonClickSoundVolume);
                int temp = currentPlayerExpertiseSelectionIndex;

                if (currentPlayerExpertiseSelectionIndex > 0)
                    currentPlayerExpertiseSelectionIndex = (currentPlayerExpertiseSelectionIndex - 1) % Player.PlayerExpertise.values().length;
                else
                    currentPlayerExpertiseSelectionIndex = Player.PlayerExpertise.values().length - 1;

                playerExpertiseIcons.get(temp).addAction(parallel(scaleTo(.9f,.9f, .3f, Interpolation.pow5),
                        moveBy(35, iconSize * .0275f, .3f), fadeOut(.3f, Interpolation.pow2)));

                playerExpertiseIcons.get(currentPlayerExpertiseSelectionIndex).setPosition(leftArrowButton.getX() + leftArrowButton.getWidth(),
                        rightArrowButton.getY() - 13);
                playerExpertiseIcons.get(currentPlayerExpertiseSelectionIndex).setColor(1, 1, 1, 1);
                playerExpertiseIcons.get(currentPlayerExpertiseSelectionIndex).setScale(.9f, .9f);
                playerExpertiseIcons.get(currentPlayerExpertiseSelectionIndex).addAction(parallel(scaleTo(1f,1f, .3f, Interpolation.pow5),
                        moveBy(35, iconSize * .0275f, .3f), fadeIn(.3f, Interpolation.pow2)));
            }
        });

        rightArrowButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                buttonClickSound.setVolume(buttonClickSound.play(), game.defaultButtonClickSoundVolume);
                int temp = currentPlayerExpertiseSelectionIndex;

                currentPlayerExpertiseSelectionIndex = (currentPlayerExpertiseSelectionIndex + 1) % Player.PlayerExpertise.values().length;

                playerExpertiseIcons.get(temp).addAction(parallel(scaleTo(.9f,.9f, .3f, Interpolation.pow5),
                        moveBy(-35, iconSize * .0275f, .3f), fadeOut(.3f, Interpolation.pow2)));

                playerExpertiseIcons.get(currentPlayerExpertiseSelectionIndex).setPosition(rightArrowButton.getX() - iconSize,
                        rightArrowButton.getY() - 13);
                playerExpertiseIcons.get(currentPlayerExpertiseSelectionIndex).setColor(1, 1, 1, 1);
                playerExpertiseIcons.get(currentPlayerExpertiseSelectionIndex).setScale(.9f, .9f);
                playerExpertiseIcons.get(currentPlayerExpertiseSelectionIndex).addAction(parallel(scaleTo(1f,1f, .3f, Interpolation.pow5),
                        moveBy(-35, iconSize * .0275f, .3f), fadeIn(.3f, Interpolation.pow2)));
            }
        });

        Texture startButtonTexture = game.assetManager.get(GameSettings.getInstance().getStartButtonFilePath(), Texture.class);
        Texture startButtonPressedTexture = game.assetManager.get(GameSettings.getInstance().getStartButtonPressedFilePath(), Texture.class);
        Button.ButtonStyle startButtonStyle = new Button.ButtonStyle();
        startButtonStyle.up = new NinePatchDrawable(new NinePatch(startButtonTexture));
        startButtonStyle.down = new NinePatchDrawable(new NinePatch(startButtonPressedTexture));

        startButton = new Button(startButtonStyle);
        startButton.setPosition((float) V_WIDTH / 2 - (float) textButtonDefaultWidth / 2, centralButtonsVerticalBottomOffset);
        startButton.setSize(textButtonDefaultWidth, textButtonDefaultHeight);
        startButton.addAction(sequence(alpha(0), parallel(fadeIn(game.buttonAnimationTime), moveBy(0, -20, game.buttonAnimationTime, Interpolation.pow5Out))));
        startButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                buttonClickSound.setVolume(buttonClickSound.play(), game.defaultButtonClickSoundVolume);
                if (game.gameController.getPlayersList().size() >= 2) {
                    game.setScreen(game.playScreen);
                } else {
                    textField.setMessageText("At least 2 players");
                    textField.setText("");
                    stage.unfocus(textField);
                }
            }
        });

        Texture quitButtonTexture = game.assetManager.get(GameSettings.getInstance().getQuitButtonFilePath(), Texture.class);
        Texture quitButtonPressedTexture = game.assetManager.get(GameSettings.getInstance().getQuitButtonPressedFilePath(), Texture.class);
        Button.ButtonStyle quitButtonStyle = new Button.ButtonStyle();
        quitButtonStyle.up = new NinePatchDrawable(new NinePatch(quitButtonTexture));
        quitButtonStyle.down = new NinePatchDrawable(new NinePatch(quitButtonPressedTexture));

        quitButton = new Button(quitButtonStyle);
        quitButton.setPosition((float) V_WIDTH / 2 - (float) textButtonDefaultWidth / 2,
                centralButtonsVerticalBottomOffset - textButtonDefaultHeight - buttonsSpacing);
        quitButton.setSize(textButtonDefaultWidth, textButtonDefaultHeight);
        quitButton.addAction(sequence(alpha(0), parallel(fadeIn(game.buttonAnimationTime), moveBy(0, -20, game.buttonAnimationTime,
                Interpolation.pow5Out))));
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                buttonClickSound.setVolume(buttonClickSound.play(), game.defaultButtonClickSoundVolume);
                Gdx.app.exit();
            }
        });

        Texture addPlayerButtonPressedTexture = game.assetManager.get(GameSettings.getInstance().getAddPlayerButtonPressedFilePath(), Texture.class);
        Button.ButtonStyle addPlayerButtonStyle = new Button.ButtonStyle();
        addPlayerButtonStyle.down = new NinePatchDrawable(new NinePatch(addPlayerButtonPressedTexture));

        if (game.gameController.getPlayersList().size() >= 4)
            addPlayerButtonStyle.up = skin.getDrawable("addPlayerButtonOffTexture");
        else
            addPlayerButtonStyle.up = skin.getDrawable("addPlayerButtonOnTexture");

        addPlayerButton = new Button(addPlayerButtonStyle);
        if (game.gameController.getPlayersList().size() >= 4)
            addPlayerButton.setTouchable(Touchable.disabled);
        addPlayerButton.setPosition(V_WIDTH - (float) textButtonDefaultWidth / 2 - textField.getWidth() / 2,
                textField.getY() - (float) textButtonDefaultHeight / 2 - textField.getHeight() / 2 - buttonsSpacing + 4);
        addPlayerButton.setSize(textButtonDefaultWidth, textButtonDefaultHeight);
        addPlayerButton.addAction(sequence(alpha(0), parallel(fadeIn(game.buttonAnimationTime), moveBy(0, -20, game.buttonAnimationTime, Interpolation.pow5Out))));
        addPlayerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                buttonClickSound.setVolume(buttonClickSound.play(), game.defaultButtonClickSoundVolume);
                if (game.gameController.getPlayersList().size() < 4 && !game.gameController.containsPlayerName(textField.getText()) &&
                        textField.getText().matches("[a-zA-Z]+") &&
                        !game.gameController.containsPlayerExpertise(playerExpertiseList.get(currentPlayerExpertiseSelectionIndex)) &&
                        textField.getText().length() <= nameCharacterLimit) {

                    game.gameController.addPlayer(new Player(textField.getText(), playerExpertiseList.get(currentPlayerExpertiseSelectionIndex),
                            new Image(playerExpertiseIconTexturesList.get(currentPlayerExpertiseSelectionIndex))));

                    playerLabelsList.add(new TextButton(textField.getText(), skin, "default"));
                    drawLabels();

                    textField.setText("");

                    if (game.gameController.getPlayersList().size() == 4) {
                        textField.setText("");
                        textField.setDisabled(true);
                        textField.setMessageText("Maximum players added");
                        addPlayerButton.getStyle().up = skin.getDrawable("addPlayerButtonOffTexture");
                        addPlayerButton.setTouchable(Touchable.disabled);
                    }
                }
                else if (game.gameController.getPlayersList().size() >= 4) {
                    textField.setText("");
                    textField.setDisabled(true);
                    textField.setMessageText("Maximum players added");
                    addPlayerButton.getStyle().up = skin.getDrawable("addPlayerButtonOffTexture");
                    addPlayerButton.setTouchable(Touchable.disabled);
                }
                else if (!textField.getText().matches("[a-zA-Z]+")) {
                    textField.setText("");
                    textField.setMessageText("Letters only");
                    stage.unfocus(textField);
                }
                else if (game.gameController.containsPlayerName(textField.getText())) {
                    textField.setText("");
                    textField.setMessageText("Name already taken");
                    stage.unfocus(textField);
                }
                else if (game.gameController.containsPlayerExpertise(playerExpertiseList.get(currentPlayerExpertiseSelectionIndex))) {
                    textField.setText("");
                    textField.setMessageText("Job already taken");
                    stage.unfocus(textField);
                }
                else if (textField.getText().length() > nameCharacterLimit) {
                    textField.setText("");
                    textField.setMessageText("Name too long");
                    stage.unfocus(textField);
                }
            }
        });

        Texture removePlayerButtonTexture = game.assetManager.get(GameSettings.getInstance().getRemovePlayerButtonFilePath(), Texture.class);
        Texture removePlayerButtonPressedTexture = game.assetManager.get(GameSettings.getInstance().getRemovePlayerButtonPressedFilePath(), Texture.class);
        Button.ButtonStyle removePlayerButtonStyle = new Button.ButtonStyle();
        removePlayerButtonStyle.up = new NinePatchDrawable(new NinePatch(removePlayerButtonTexture));
        removePlayerButtonStyle.down = new NinePatchDrawable(new NinePatch(removePlayerButtonPressedTexture));

        removePlayerButton = new Button(removePlayerButtonStyle);
        removePlayerButton.setPosition(V_WIDTH - (float) textButtonDefaultWidth / 2 - textField.getWidth() / 2,
                addPlayerButton.getY() - textButtonDefaultHeight - buttonsSpacing);
        removePlayerButton.setSize(textButtonDefaultWidth, textButtonDefaultHeight);
        removePlayerButton.addAction(sequence(alpha(0), parallel(fadeIn(game.buttonAnimationTime), moveBy(0, -20,
                game.buttonAnimationTime, Interpolation.pow5Out))));
        removePlayerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                buttonClickSound.setVolume(buttonClickSound.play(), game.defaultButtonClickSoundVolume);
                if (!game.gameController.getPlayersList().isEmpty()) {
                    playerLabelsList.getLast().remove();
                    playerLabelsList.removeLast();
                    game.gameController.removeLastPlayer();
                    textField.setDisabled(false);
                    textField.setMessageText("Input player name");
                    addPlayerButton.getStyle().up = skin.getDrawable("addPlayerButtonOnTexture");
                    addPlayerButton.setTouchable(Touchable.enabled);
                }
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

        stage.addActor(manualButton);
        stage.addActor(leftArrowButton);
        stage.addActor(rightArrowButton);
        stage.addActor(startButton);
        stage.addActor(quitButton);
        stage.addActor(addPlayerButton);
        stage.addActor(removePlayerButton);
    }

    public void drawLabels() {
        for (int i = 0; i < playerLabelsList.size(); i++) {
            playerLabelsList.get(i).setSize(textField.getWidth() - 20, textField.getHeight());
            playerLabelsList.get(i).setPosition(V_WIDTH - playerLabelsList.get(i).getWidth() / 2 - textField.getWidth() / 2 - sideButtonsHorizontalRightSideOffset,
                    V_HEIGHT - playerLabelsList.get(i).getHeight() / 2 - labelVerticalTopOffset - i * (labelsSpacing + playerLabelsList.get(i).getHeight())
                        - labelsSpacing - 8);
            playerLabelsList.get(i).setTouchable(Touchable.disabled);

            stage.addActor(playerLabelsList.get(i));
        }
    }

    public void initTables() {
        Texture playersTableTexture = game.assetManager.get(GameSettings.getInstance().getPlayersTableFilePath(), Texture.class);
        Texture playerExpertiseFrameTexture = game.assetManager.get(GameSettings.getInstance().getPlayerExpertiseFrameFilePath(), Texture.class);

        playersTableImg = new Image(playersTableTexture);
        playerExpertiseTableImg = new Image(playerExpertiseFrameTexture);

        playersTableImg.setSize(textField.getWidth() - 7, textField.getHeight() * 4 + 14);
        playersTableImg.setPosition(V_WIDTH - playersTableImg.getWidth() / 2 - textField.getWidth() / 2 - sideButtonsHorizontalRightSideOffset,
                V_HEIGHT - playersTableImg.getHeight() - namesTableVerticalTopOffset + 9);

        playerExpertiseTableImg.setSize(iconSize + 58, iconSize - 12);
        playerExpertiseTableImg.setPosition(textField.getX() + textField.getWidth() / 2 - playerExpertiseTableImg.getWidth() / 2, textField.getY() + 35);

        stage.addActor(playersTableImg);
        stage.addActor(playerExpertiseTableImg);
    }

    public void initPlayerIcons() {
        Texture managerTexture = game.assetManager.get(GameSettings.getInstance().getManagerFilePath(), Texture.class);
        Texture civilEngineerTexture = game.assetManager.get(GameSettings.getInstance().getCivilEngineerFilePath(), Texture.class);
        Texture chemicalEngineerTexture = game.assetManager.get(GameSettings.getInstance().getChemicalEngineerFilePath(), Texture.class);
        Texture communityLeaderTexture = game.assetManager.get(GameSettings.getInstance().getCommunityLeaderFilePath(), Texture.class);
        Texture salespersonTexture = game.assetManager.get(GameSettings.getInstance().getSalespersonFilePath(), Texture.class);
        Texture luckyPersonTexture = game.assetManager.get(GameSettings.getInstance().getLuckyPersonFilePath(), Texture.class);
        Texture environmentalScientistTexture = game.assetManager.get(GameSettings.getInstance().getEnvironmentalScientistFilePath(), Texture.class);

        playerExpertiseIconTexturesList.add(managerTexture);
        playerExpertiseIconTexturesList.add(civilEngineerTexture);
        playerExpertiseIconTexturesList.add(chemicalEngineerTexture);
        playerExpertiseIconTexturesList.add(communityLeaderTexture);
        playerExpertiseIconTexturesList.add(salespersonTexture);
        playerExpertiseIconTexturesList.add(luckyPersonTexture);
        playerExpertiseIconTexturesList.add(environmentalScientistTexture);

        playerExpertiseIcons.add(new Image(managerTexture));
        playerExpertiseIcons.add(new Image(civilEngineerTexture));
        playerExpertiseIcons.add(new Image(chemicalEngineerTexture));
        playerExpertiseIcons.add(new Image(communityLeaderTexture));
        playerExpertiseIcons.add(new Image(salespersonTexture));
        playerExpertiseIcons.add(new Image(luckyPersonTexture));
        playerExpertiseIcons.add(new Image(environmentalScientistTexture));

        playerExpertiseIcons.get(0).setPosition(textField.getX() + textField.getWidth() / 2 - (float) iconSize / 2, textField.getY() + 29);
        playerExpertiseIcons.get(0).setSize(iconSize, iconSize);

        stage.addActor(playerExpertiseIcons.get(0));

        for (int i = 1; i < playerExpertiseIcons.size(); i++) {
            playerExpertiseIcons.get(i).setColor(1, 1, 1, 0);
            playerExpertiseIcons.get(i).setSize(iconSize, iconSize);
            stage.addActor(playerExpertiseIcons.get(i));
        }
    }
}
