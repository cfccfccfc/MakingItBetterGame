package com.cfc.makingitbettergame.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.cfc.makingitbettergame.GameClasses.Miscellaneous.GameSettings;
import com.cfc.makingitbettergame.MakingItBetterGame;

public class LoadingScreen implements Screen {
    private final MakingItBetterGame game;
    private ShapeRenderer shapeRenderer;
    private Texture backgroundTexture;

    private float progress;
    private int progressBarHorizontalOffset = 200;
    private int progressBarVerticalOffset = -170;
    private int progressBarHeight = 18;
    public LoadingScreen(MakingItBetterGame game) {
        this.game = game;
        this.shapeRenderer = new ShapeRenderer();
        backgroundTexture = new Texture(GameSettings.getInstance().getGameBackgroundFilePath());
    }
    private void queueAssets() {
        game.assetManager.load(GameSettings.getInstance().getGameLogoFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getUiSkinAtlasFilePath(), TextureAtlas.class);
        game.assetManager.load(GameSettings.getInstance().getGameBackgroundFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getVictoryBackgroundFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getLossBackgroundFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getYouWinFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getYouLoseFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getLeftArrowButtonFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getRightArrowButtonFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getChemicalEngineerFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getCivilEngineerFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getCommunityLeaderFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getLuckyPersonFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getManagerFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getSalespersonFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getBoardFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getPlayerTabFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getResourcesTabFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getLogTabFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getPositiveEventSquareFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getNegativeEventSquareFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getTaskSquareFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getDie6FilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getDie5FilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getDie4FilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getDie3FilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getDie2FilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getDie1FilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getBackButtonFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getManualButtonFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getMenuButtonFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getNoButtonFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getQuitButtonFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getRemovePlayerButtonFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getStartButtonFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getTransferTaskButtonFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getYesButtonFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getAddPlayerButtonFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getAddPlayerButtonOffFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getNoButtonOffFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getTransferTaskButtonOffFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getYesButtonOffFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getChemicalEngineerGlowingFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getEnvironmentalScientistFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getCommunityLeaderGlowingFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getEnvironmentalScientistGlowingFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getLuckyPersonGlowingFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getManagerGlowingFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getSalespersonGlowingFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getLogBackgroundFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getObjectiveTabFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getPlayerExpertiseFrameFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getPlayersTableFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getWoodenFrameTopFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getWoodenFrameMiddleFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getManualScreenFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getCivilEngineerGlowingFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getButtonSoundFilePath(), Sound.class);
        game.assetManager.load(GameSettings.getInstance().getLossSoundFilePath(), Sound.class);
        game.assetManager.load(GameSettings.getInstance().getWinSoundFilePath(), Sound.class);
        game.assetManager.load(GameSettings.getInstance().getDieRollSoundFilePath(), Sound.class);
        game.assetManager.load(GameSettings.getInstance().getCoinTossSoundFilePath(), Sound.class);
        game.assetManager.load(GameSettings.getInstance().getBackButtonPressedFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getManualButtonPressedFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getMenuButtonPressedFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getNoButtonPressedFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getQuitButtonPressedFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getRemovePlayerButtonPressedFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getStartButtonPressedFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getTransferTaskButtonPressedFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getYesButtonPressedFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getAddPlayerButtonPressedFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getLeftArrowButtonPressedFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getRightArrowButtonPressedFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getDescriptionFrameFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getCoinFlipTrueFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getCoinFlipFalseFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getCoinFlipIcon1FilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getCoinFlipIcon2FilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getCoinFlipIcon3FilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getCoinFlipIcon4FilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getCoinFlipIcon5FilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getCoinFlipIcon6FilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getCoinFlipIcon7FilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getCoinFlipIcon8FilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getCoinFlipIcon9FilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getCoinFlipIcon10FilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getCoinFlipIcon11FilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getCoinFlipIcon12FilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getCoinFlipIcon13FilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getCoinFlipIcon14FilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getCoinFlipIcon15FilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getCoinFlipIcon16FilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getCoinFlipIcon17FilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getCoinFlipIcon18FilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getCoinFlipIcon19FilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getCoinFlipIcon20FilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getLeftArrowBookFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getRightArrowBookFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getLeftArrowBookPressedFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getRightArrowBookPressedFilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getManualScreen2FilePath(), Texture.class);
        game.assetManager.load(GameSettings.getInstance().getFlipPageFilePath(), Sound.class);
    }
    @Override
    public void show() {
        shapeRenderer.setProjectionMatrix(game.gameCam.combined);

        this.progress = 0f;

        queueAssets();
    }
    private void update(float v) {
        progress = MathUtils.lerp(progress, game.assetManager.getProgress(), .75f);
        if (game.assetManager.update())
        {
            game.initGame();
            game.initSkin();
            game.setScreen(game.mainMenuScreen);
            //game.setScreen(game.victoryScreen);
            //game.setScreen(game.lossScreen);
        }
    }
    @Override
    public void render(float v) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.end();

        update(v);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(progressBarHorizontalOffset, game.gameCam.viewportHeight / 2 - (float) progressBarHeight / 2 + progressBarVerticalOffset,
                game.gameCam.viewportWidth - progressBarHorizontalOffset * 2, progressBarHeight);

        shapeRenderer.setColor(Color.SKY);
        shapeRenderer.rect(progressBarHorizontalOffset, game.gameCam.viewportHeight / 2 - (float) progressBarHeight / 2 + progressBarVerticalOffset,
                 progress * (game.gameCam.viewportWidth - progressBarHorizontalOffset * 2), progressBarHeight);
        shapeRenderer.end();
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
        shapeRenderer.dispose();
    }
}
