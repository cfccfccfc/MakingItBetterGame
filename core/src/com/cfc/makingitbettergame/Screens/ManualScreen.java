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
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.cfc.makingitbettergame.GameClasses.Miscellaneous.GameSettings;
import com.cfc.makingitbettergame.MakingItBetterGame;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static com.cfc.makingitbettergame.MakingItBetterGame.*;
import static com.cfc.makingitbettergame.MakingItBetterGame.V_HEIGHT;

public class ManualScreen implements Screen {
    private final MakingItBetterGame game;
    private Stage stage;

    private Image backgroundImg;
    private Image page1, page2;

    private Button backButton;

    private Sound buttonClickSound, flipPageSound;

    private Button bookArrowLeftButton, bookArrowRightButton;

    private float arrowButtonsSize = .2f;
    private float arrowButtonsWidth = 268 * arrowButtonsSize, arrowButtonsHeight = 188 * arrowButtonsSize;

    public ManualScreen(MakingItBetterGame game) {
        this.game = game;
        this.stage = new Stage(new FitViewport(MakingItBetterGame.V_WIDTH, MakingItBetterGame.V_HEIGHT, game.gameCam));
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        stage.clear();

        Texture backgroundTexture = game.assetManager.get(GameSettings.getInstance().getGameBackgroundFilePath(), Texture.class);
        backgroundImg = new Image(backgroundTexture);
        backgroundImg.setPosition(0, 0);
        backgroundImg.setSize(MakingItBetterGame.V_WIDTH, MakingItBetterGame.V_HEIGHT);

        buttonClickSound = game.assetManager.get(GameSettings.getInstance().getButtonSoundFilePath());
        flipPageSound = game.assetManager.get(GameSettings.getInstance().getFlipPageFilePath());

        stage.addActor(backgroundImg);

        initTable();
        initButtons();
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

    public void initButtons() {
        Texture backButtonTexture = game.assetManager.get(GameSettings.getInstance().getBackButtonFilePath(), Texture.class);
        Texture backButtonPressedTexture = game.assetManager.get(GameSettings.getInstance().getBackButtonPressedFilePath(), Texture.class);
        Button.ButtonStyle backButtonStyle = new Button.ButtonStyle();
        backButtonStyle.up = new NinePatchDrawable(new NinePatch(backButtonTexture));
        backButtonStyle.down = new NinePatchDrawable(new NinePatch(backButtonPressedTexture));

        backButton = new Button(backButtonStyle);
        backButton.setPosition(game.menuButtonHorizontalOffset, MakingItBetterGame.V_HEIGHT - (float) textButtonDefaultHeight / 2 - game.menuButtonVerticalTopOffset);
        backButton.setSize(textButtonDefaultWidth, textButtonDefaultHeight);
        backButton.addAction(sequence(alpha(0), parallel(fadeIn(game.buttonAnimationTime), moveBy(0, -20, game.buttonAnimationTime, Interpolation.pow5Out))));
        backButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                buttonClickSound.setVolume(buttonClickSound.play(), game.defaultButtonClickSoundVolume);
                game.toggleManual();
            }
        });

        Texture bookArrowLeftTexture = game.assetManager.get(GameSettings.getInstance().getLeftArrowBookFilePath(), Texture.class);
        Texture bookArrowLeftPressedTexture = game.assetManager.get(GameSettings.getInstance().getLeftArrowBookPressedFilePath(), Texture.class);
        Button.ButtonStyle bookArrowLeftStyle = new Button.ButtonStyle();
        bookArrowLeftStyle.up = new NinePatchDrawable(new NinePatch(bookArrowLeftTexture));
        bookArrowLeftStyle.down = new NinePatchDrawable(new NinePatch(bookArrowLeftPressedTexture));

        bookArrowLeftButton = new Button(bookArrowLeftStyle);
        bookArrowLeftButton.setPosition(130 , (float) V_HEIGHT / 2);
        bookArrowLeftButton.setSize(arrowButtonsWidth, arrowButtonsHeight);
        bookArrowLeftButton.setColor(0, 0, 0, 0);
        bookArrowLeftButton.setTouchable(Touchable.disabled);
        bookArrowLeftButton.addListener(new ClickListener(){
           public void clicked(InputEvent event, float x, float y) {
               flipPageSound.play();

               page1.setColor(1, 1, 1, 1);
               page2.setColor(0, 0, 0, 0);

               bookArrowLeftButton.setColor(0, 0, 0, 0);
               bookArrowLeftButton.setTouchable(Touchable.disabled);

               bookArrowRightButton.setColor(1, 1, 1, 1);
               bookArrowRightButton.setTouchable(Touchable.enabled);
           }
        });

        Texture bookArrowRightTexture = game.assetManager.get(GameSettings.getInstance().getRightArrowBookFilePath(), Texture.class);
        Texture bookArrowRightPressedTexture = game.assetManager.get(GameSettings.getInstance().getRightArrowBookPressedFilePath(), Texture.class);
        Button.ButtonStyle bookArrowRightStyle = new Button.ButtonStyle();
        bookArrowRightStyle.up = new NinePatchDrawable(new NinePatch(bookArrowRightTexture));
        bookArrowRightStyle.down = new NinePatchDrawable(new NinePatch(bookArrowRightPressedTexture));

        bookArrowRightButton = new Button(bookArrowRightStyle);
        bookArrowRightButton.setPosition(V_WIDTH - 180 , (float) V_HEIGHT / 2);
        bookArrowRightButton.setSize(arrowButtonsWidth, arrowButtonsHeight);
        bookArrowRightButton.addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y) {
                flipPageSound.play();

                page1.setColor(0, 0, 0, 0);
                page2.setColor(1, 1, 1, 1);

                bookArrowRightButton.setColor(0, 0, 0, 0);
                bookArrowRightButton.setTouchable(Touchable.disabled);

                bookArrowLeftButton.setColor(1, 1, 1, 1);
                bookArrowLeftButton.setTouchable(Touchable.enabled);
            }
        });

        stage.addActor(backButton);
        stage.addActor(bookArrowRightButton);
        stage.addActor(bookArrowLeftButton);
    }

    public void initTable() {
        Texture page1Texture = game.assetManager.get(GameSettings.getInstance().getManualScreenFilePath(), Texture.class);

        page1 = new Image(page1Texture);

        page1.setSize(980,628);
        page1.setPosition((float) V_WIDTH / 2 - page1.getWidth() / 2, (float) V_HEIGHT / 2 - page1.getHeight() / 2);

        Texture page2Texture = game.assetManager.get(GameSettings.getInstance().getManualScreen2FilePath(), Texture.class);

        page2 = new Image(page2Texture);

        page2.setSize(980,628);
        page2.setPosition((float) V_WIDTH / 2 - page1.getWidth() / 2, (float) V_HEIGHT / 2 - page1.getHeight() / 2);
        page2.setColor(0, 0, 0, 0);

        stage.addActor(page1);
        stage.addActor(page2);
    }
}
