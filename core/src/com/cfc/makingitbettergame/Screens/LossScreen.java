package com.cfc.makingitbettergame.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.cfc.makingitbettergame.GameClasses.Miscellaneous.GameSettings;
import com.cfc.makingitbettergame.MakingItBetterGame;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static com.cfc.makingitbettergame.MakingItBetterGame.textButtonDefaultHeight;
import static com.cfc.makingitbettergame.MakingItBetterGame.textButtonDefaultWidth;

public class LossScreen implements Screen {
    private final MakingItBetterGame game;
    private Stage stage;

    private Button mainMenuButton;

    private Image lossBackgroundImg;
    private Image youLoseImg;

    private Sound youLoseSound;
    private Sound buttonClickSound;

    private int youLoseImgWidth = 276,  youLoseImgHeight = 745;
    private int youLoseImgVerticalCenterOffset = 350;
    private int youLoseImgDragDistance = 120;

    public LossScreen(MakingItBetterGame game) {
        this.game = game;
        this.stage = new Stage(new FitViewport(MakingItBetterGame.V_WIDTH, MakingItBetterGame.V_HEIGHT, game.gameCam));
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        stage.clear();

        Texture lossBackgroundTexture = game.assetManager.get(GameSettings.getInstance().getLossBackgroundFilePath(), Texture.class);
        lossBackgroundImg = new Image(lossBackgroundTexture);
        lossBackgroundImg.setPosition(0, 0);
        lossBackgroundImg.setSize(MakingItBetterGame.V_WIDTH, MakingItBetterGame.V_HEIGHT);

        Texture youLoseTexture = game.assetManager.get(GameSettings.getInstance().getYouLoseFilePath(), Texture.class);
        youLoseImg = new Image(youLoseTexture);
        youLoseImg.setPosition((float) MakingItBetterGame.V_WIDTH / 2 - (float) youLoseImgWidth / 2,
                (float) MakingItBetterGame.V_HEIGHT / 2 - (float) youLoseImgHeight / 2);
        youLoseImg.setSize(youLoseImgWidth, youLoseImgHeight);

        youLoseImg.setOrigin((float) youLoseImgWidth / 2, (float) youLoseImgHeight / 2 + youLoseImgVerticalCenterOffset + youLoseImgDragDistance);
        youLoseImg.setPosition(stage.getWidth() / 2 - (float) youLoseImgWidth / 2,
                stage.getHeight() / 2 - (float) youLoseImgHeight / 2 + youLoseImgVerticalCenterOffset + youLoseImgDragDistance);
        youLoseImg.addAction(sequence(scaleTo(.98f, .98f),
                parallel(scaleTo(1f, 1f, 2.5f, Interpolation.pow5),
                        moveTo(stage.getWidth() / 2 - (float) youLoseImgWidth / 2,
                                stage.getHeight() / 2 - (float) youLoseImgHeight / 2 + youLoseImgVerticalCenterOffset, 1.5f))));

        youLoseSound = game.assetManager.get(GameSettings.getInstance().getLossSoundFilePath());
        youLoseSound.play();

        buttonClickSound = game.assetManager.get(GameSettings.getInstance().getButtonSoundFilePath());

        stage.addActor(lossBackgroundImg);
        stage.addActor(youLoseImg);

        initButtons();
    }

    public void render(float v) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(v);

        stage.draw();
    }

    private void update(float v) {
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
            }
        });

        stage.addActor(mainMenuButton);
    }
}
