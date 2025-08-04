package com.cfc.makingitbettergame.GameClasses.Miscellaneous;

public class GameSettings {
    private static GameSettings gameSettings;
    private int dieFacesNumber = 6;
    private int moneyChargePerLabourUnit = 50000;
    private int donationOfMaterialsDiscount = 15;
    private int minimalCommunitySupportForDonationOfMaterials = 3;
    private int defaultMoneyQuantity = 75000000;
    private int defaultLabourQuantity = 20;
    private int defaultCommunitySupportQuantity = 2;
    private int defaultReputationQuantity = 10;
    private int defaultMoraleQuantity = 100;
    private int defaultMoraleCost = 50;
    private int nameCharacterLimit = 8;

    private String gameLogoFilePath = "img/icons/gameLogo.png";
    private String uiSkinAtlasFilePath = "ui/uiskin.atlas";
    private String gameBackgroundFilePath = "img/backgrounds/gameBackground.png";
    private String victoryBackgroundFilePath = "img/backgrounds/victoryBackground.jpg";
    private String lossBackgroundFilePath = "img/backgrounds/lossBackground.jpg";
    private String youWinFilePath = "img/backgrounds/youWin.png";
    private String youLoseFilePath = "img/backgrounds/youLose.png";
    private String leftArrowButtonFilePath = "img/buttons/buttonsOn/leftArrowButton.png";
    private String rightArrowButtonFilePath = "img/buttons/buttonsOn/rightArrowButton.png";
    private String chemicalEngineerFilePath = "img/icons/playerIcons/chemicalEngineer.png";
    private String civilEngineerFilePath = "img/icons/playerIcons/civilEngineer.png";
    private String communityLeaderFilePath = "img/icons/playerIcons/communityLeader.png";
    private String luckyPersonFilePath = "img/icons/playerIcons/luckyPerson.png";
    private String managerFilePath = "img/icons/playerIcons/manager.png";
    private String salespersonFilePath = "img/icons/playerIcons/salesperson.png";
    private String environmentalScientistFilePath = "img/icons/playerIcons/environmentalScientist.png";
    private String boardFilePath = "img/backgrounds/board.png";
    private String playerTabFilePath = "img/backgrounds/playerTab.png";
    private String resourcesTabFilePath = "img/backgrounds/resourcesTab.png";
    private String logTabFilePath = "img/backgrounds/logTab.png";
    private String positiveEventSquareFilePath = "img/icons/positiveEventSquare.png";
    private String negativeEventSquareFilePath = "img/icons/negativeEventSquare.png";
    private String taskSquareFilePath = "img/icons/taskSquare.png";
    private String die6FilePath = "img/dieValues/die6.png";
    private String die5FilePath = "img/dieValues/die5.png";
    private String die4FilePath = "img/dieValues/die4.png";
    private String die3FilePath = "img/dieValues/die3.png";
    private String die2FilePath = "img/dieValues/die2.png";
    private String die1FilePath = "img/dieValues/die1.png";
    private String backButtonFilePath = "img/buttons/buttonsOn/backButton.png";
    private String manualButtonFilePath = "img/buttons/buttonsOn/manualButton.png";
    private String menuButtonFilePath = "img/buttons/buttonsOn/menuButton.png";
    private String noButtonFilePath = "img/buttons/buttonsOn/noButton.png";
    private String quitButtonFilePath = "img/buttons/buttonsOn/quitButton.png";
    private String removePlayerButtonFilePath = "img/buttons/buttonsOn/removePlayerButton.png";
    private String startButtonFilePath = "img/buttons/buttonsOn/startButton.png";
    private String transferTaskButtonFilePath = "img/buttons/buttonsOn/transferTaskButton.png";
    private String yesButtonFilePath = "img/buttons/buttonsOn/yesButton.png";
    private String addPlayerButtonFilePath = "img/buttons/buttonsOn/addPlayerButton.png";
    private String addPlayerButtonOffFilePath = "img/buttons/buttonsOff/addPlayerButtonOff.png";
    private String noButtonOffFilePath = "img/buttons/buttonsOff/noButtonOff.png";
    private String transferTaskButtonOffFilePath = "img/buttons/buttonsOff/transferTaskButtonOff.png";
    private String yesButtonOffFilePath = "img/buttons/buttonsOff/yesButtonOff.png";
    private String chemicalEngineerGlowingFilePath = "img/icons/playerIconsGlowing/chemicalEngineerGlowing.png";
    private String civilEngineerGlowingFilePath = "img/icons/playerIconsGlowing/civilEngineerGlowing.png";
    private String communityLeaderGlowingFilePath = "img/icons/playerIconsGlowing/communityLeaderGlowing.png";
    private String luckyPersonGlowingFilePath = "img/icons/playerIconsGlowing/luckyPersonGlowing.png";
    private String managerGlowingFilePath = "img/icons/playerIconsGlowing/managerGlowing.png";
    private String salespersonGlowingFilePath = "img/icons/playerIconsGlowing/salespersonGlowing.png";
    private String environmentalScientistGlowingFilePath = "img/icons/playerIconsGlowing/environmentalScientistGlowing.png";
    private String logBackgroundFilePath = "img/backgrounds/logBackground.png";
    private String playerExpertiseFrameFilePath = "img/backgrounds/playerExpertiseFrame.png";
    private String objectiveTabFilePath = "img/backgrounds/objectiveTab.png";
    private String playersTableFilePath = "img/backgrounds/playersTable.png";
    private String woodenFrameMiddleFilePath = "img/backgrounds/woodenFrameMiddle.png";
    private String woodenFrameTopFilePath = "img/backgrounds/woodenFrameTop.png";
    private String manualScreenFilePath = "img/backgrounds/manualScreen.png";
    private String buttonClickSoundFilePath = "sounds/buttonClickSound.mp3";
    private String coinTossSoundFilePath = "sounds/coinTossSound.mp3";
    private String dieRollSoundFilePath = "sounds/dieRollSound.mp3";
    private String lossSoundFilePath = "sounds/lossSound.mp3";
    private String winSoundFilePath = "sounds/winSound.mp3";
    private String backButtonPressedFilePath = "img/buttons/buttonsPressed/backButtonPressed.png";
    private String manualButtonPressedFilePath = "img/buttons/buttonsPressed/manualButtonPressed.png";
    private String menuButtonPressedFilePath = "img/buttons/buttonsPressed/menuButtonPressed.png";
    private String noButtonPressedFilePath = "img/buttons/buttonsPressed/noButtonPressed.png";
    private String quitButtonPressedFilePath = "img/buttons/buttonsPressed/quitButtonPressed.png";
    private String removePlayerButtonPressedFilePath = "img/buttons/buttonsPressed/removePlayerButtonPressed.png";
    private String startButtonPressedFilePath = "img/buttons/buttonsPressed/startButtonPressed.png";
    private String transferTaskButtonPressedFilePath = "img/buttons/buttonsPressed/transferTaskButtonPressed.png";
    private String yesButtonPressedFilePath = "img/buttons/buttonsPressed/yesButtonPressed.png";
    private String addPlayerButtonPressedFilePath = "img/buttons/buttonsPressed/addPlayerButtonPressed.png";
    private String leftArrowButtonPressedFilePath = "img/buttons/buttonsPressed/leftArrowButtonPressed.png";
    private String rightArrowButtonPressedFilePath = "img/buttons/buttonsPressed/rightArrowButtonPressed.png";
    private String leftArrowBookFilePath = "img/buttons/buttonsOn/leftArrowBook.png";
    private String rightArrowBookFilePath = "img/buttons/buttonsOn/rightArrowBook.png";
    private String leftArrowBookPressedFilePath = "img/buttons/buttonsPressed/leftArrowBookPressed.png";
    private String rightArrowBookPressedFilePath = "img/buttons/buttonsPressed/rightArrowBookPressed.png";
    private String descriptionFrameFilePath = "img/backgrounds/descriptionFrame.png";
    private String manualScreen2FilePath = "img/backgrounds/manualScreen2.png";
    private String coinFlipTrueFilePath = "img/icons/coinFlipTrue.jpg";
    private String coinFlipFalseFilePath = "img/icons/coinFlipFalse.jpg";
    private String flipPageFilePath = "sounds/flipPage.mp3";
    private String coinFlipIcon1FilePath = "img/icons/coinFlipIcons/coinFlipIcon1.jpg";
    private String coinFlipIcon2FilePath = "img/icons/coinFlipIcons/coinFlipIcon2.jpg";
    private String coinFlipIcon3FilePath = "img/icons/coinFlipIcons/coinFlipIcon3.jpg";
    private String coinFlipIcon4FilePath = "img/icons/coinFlipIcons/coinFlipIcon4.jpg";
    private String coinFlipIcon5FilePath = "img/icons/coinFlipIcons/coinFlipIcon5.jpg";
    private String coinFlipIcon6FilePath = "img/icons/coinFlipIcons/coinFlipIcon6.jpg";
    private String coinFlipIcon7FilePath = "img/icons/coinFlipIcons/coinFlipIcon7.jpg";
    private String coinFlipIcon8FilePath = "img/icons/coinFlipIcons/coinFlipIcon8.jpg";
    private String coinFlipIcon9FilePath = "img/icons/coinFlipIcons/coinFlipIcon9.jpg";
    private String coinFlipIcon10FilePath = "img/icons/coinFlipIcons/coinFlipIcon10.jpg";
    private String coinFlipIcon11FilePath = "img/icons/coinFlipIcons/coinFlipIcon11.jpg";
    private String coinFlipIcon12FilePath = "img/icons/coinFlipIcons/coinFlipIcon12.jpg";
    private String coinFlipIcon13FilePath = "img/icons/coinFlipIcons/coinFlipIcon13.jpg";
    private String coinFlipIcon14FilePath = "img/icons/coinFlipIcons/coinFlipIcon14.jpg";
    private String coinFlipIcon15FilePath = "img/icons/coinFlipIcons/coinFlipIcon15.jpg";
    private String coinFlipIcon16FilePath = "img/icons/coinFlipIcons/coinFlipIcon16.jpg";
    private String coinFlipIcon17FilePath = "img/icons/coinFlipIcons/coinFlipIcon17.jpg";
    private String coinFlipIcon18FilePath = "img/icons/coinFlipIcons/coinFlipIcon18.jpg";
    private String coinFlipIcon19FilePath = "img/icons/coinFlipIcons/coinFlipIcon19.jpg";
    private String coinFlipIcon20FilePath = "img/icons/coinFlipIcons/coinFlipIcon20.jpg";

    private GameSettings() { }

    public static GameSettings getInstance() {
        if (gameSettings == null)
            gameSettings = new GameSettings();

        return gameSettings;
    }

    public int getDieFacesNumber() {
        return dieFacesNumber;
    }
    public int getMoneyChargePerLabourUnit() { return moneyChargePerLabourUnit; }
    public int getMinimalCommunitySupportForDonationOfMaterials() { return minimalCommunitySupportForDonationOfMaterials; }
    public int getDonationOfMaterialsDiscount() { return donationOfMaterialsDiscount; }
    public int getDefaultMoraleQuantity() { return defaultMoraleQuantity; }
    public int getDefaultMoraleCost() { return defaultMoraleCost; }
    public int getNameCharacterLimit() { return nameCharacterLimit; }

    public int getDefaultMoneyQuantity() {
        return defaultMoneyQuantity;
    }
    public int getDefaultLabourQuantity() {
        return defaultLabourQuantity;
    }
    public int getDefaultCommunitySupportQuantity() {
        return defaultCommunitySupportQuantity;
    }
    public int getDefaultReputationQuantity() {
        return defaultReputationQuantity;
    }

    public String getGameLogoFilePath() {
        return gameLogoFilePath;
    }
    public String getUiSkinAtlasFilePath() {
        return uiSkinAtlasFilePath;
    }
    public String getGameBackgroundFilePath() {
        return gameBackgroundFilePath;
    }
    public String getVictoryBackgroundFilePath() {
        return victoryBackgroundFilePath;
    }
    public String getLossBackgroundFilePath() {
        return lossBackgroundFilePath;
    }
    public String getYouWinFilePath() {
        return youWinFilePath;
    }
    public String getYouLoseFilePath() { return youLoseFilePath; }
    public String getManualScreen2FilePath() { return manualScreen2FilePath; }
    public String getLeftArrowButtonFilePath() {
        return leftArrowButtonFilePath;
    }
    public String getRightArrowButtonFilePath() {
        return rightArrowButtonFilePath;
    }
    public String getChemicalEngineerFilePath() {
        return chemicalEngineerFilePath;
    }
    public String getCivilEngineerFilePath() {
        return civilEngineerFilePath;
    }
    public String getCommunityLeaderFilePath() {
        return communityLeaderFilePath;
    }
    public String getLuckyPersonFilePath() {
        return luckyPersonFilePath;
    }
    public String getManagerFilePath() {
        return managerFilePath;
    }
    public String getSalespersonFilePath() {
        return salespersonFilePath;
    }
    public String getBoardFilePath() {
        return boardFilePath;
    }
    public String getPlayerTabFilePath() {
        return playerTabFilePath;
    }
    public String getResourcesTabFilePath() {
        return resourcesTabFilePath;
    }
    public String getLogTabFilePath() {
        return logTabFilePath;
    }
    public String getPositiveEventSquareFilePath() {
        return positiveEventSquareFilePath;
    }
    public String getNegativeEventSquareFilePath() {
        return negativeEventSquareFilePath;
    }
    public String getTaskSquareFilePath() {
        return taskSquareFilePath;
    }
    public String getDie6FilePath() {
        return die6FilePath;
    }
    public String getDie5FilePath() {
        return die5FilePath;
    }
    public String getDie4FilePath() {
        return die4FilePath;
    }
    public String getDie3FilePath() {
        return die3FilePath;
    }
    public String getDie2FilePath() {
        return die2FilePath;
    }
    public String getDie1FilePath() {
        return die1FilePath;
    }
    public String getBackButtonFilePath() {
        return backButtonFilePath;
    }
    public String getManualButtonFilePath() {
        return manualButtonFilePath;
    }
    public String getMenuButtonFilePath() {
        return menuButtonFilePath;
    }
    public String getNoButtonFilePath() {
        return noButtonFilePath;
    }
    public String getQuitButtonFilePath() { return quitButtonFilePath; }
    public String getRemovePlayerButtonFilePath() {
        return removePlayerButtonFilePath;
    }
    public String getStartButtonFilePath() {
        return startButtonFilePath;
    }
    public String getTransferTaskButtonFilePath() {
        return transferTaskButtonFilePath;
    }
    public String getYesButtonFilePath() {
        return yesButtonFilePath;
    }
    public String getAddPlayerButtonFilePath() {
        return addPlayerButtonFilePath;
    }
    public String getAddPlayerButtonOffFilePath() { return addPlayerButtonOffFilePath; }
    public String getNoButtonOffFilePath() { return noButtonOffFilePath; }
    public String getTransferTaskButtonOffFilePath() { return transferTaskButtonOffFilePath; }
    public String getYesButtonOffFilePath() { return yesButtonOffFilePath; }
    public String getChemicalEngineerGlowingFilePath() { return chemicalEngineerGlowingFilePath; }
    public String getCivilEngineerGlowingFilePath() { return civilEngineerGlowingFilePath; }
    public String getEnvironmentalScientistFilePath() { return environmentalScientistFilePath; }
    public String getCommunityLeaderGlowingFilePath() { return communityLeaderGlowingFilePath; }
    public String getEnvironmentalScientistGlowingFilePath() { return environmentalScientistGlowingFilePath; }
    public String getLuckyPersonGlowingFilePath() { return luckyPersonGlowingFilePath; }
    public String getManagerGlowingFilePath() { return managerGlowingFilePath; }
    public String getSalespersonGlowingFilePath() { return salespersonGlowingFilePath; }
    public String getLogBackgroundFilePath() { return logBackgroundFilePath; }
    public String getObjectiveTabFilePath() { return objectiveTabFilePath; }
    public String getPlayerExpertiseFrameFilePath() { return playerExpertiseFrameFilePath; }
    public String getPlayersTableFilePath() { return playersTableFilePath; }
    public String getWoodenFrameMiddleFilePath() { return woodenFrameMiddleFilePath; }
    public String getWoodenFrameTopFilePath() { return woodenFrameTopFilePath; }
    public String getManualScreenFilePath() { return manualScreenFilePath; }
    public String getButtonSoundFilePath() { return buttonClickSoundFilePath; }
    public String getCoinTossSoundFilePath() { return coinTossSoundFilePath; }
    public String getDieRollSoundFilePath() { return dieRollSoundFilePath; }
    public String getLossSoundFilePath() { return lossSoundFilePath; }
    public String getWinSoundFilePath() { return winSoundFilePath; }
    public String getButtonClickSoundFilePath() { return buttonClickSoundFilePath; }
    public String getFlipPageFilePath() { return flipPageFilePath; }
    public String getBackButtonPressedFilePath() { return backButtonPressedFilePath; }
    public String getManualButtonPressedFilePath() { return manualButtonPressedFilePath; }
    public String getAddPlayerButtonPressedFilePath() { return addPlayerButtonPressedFilePath; }
    public String getMenuButtonPressedFilePath() { return menuButtonPressedFilePath; }
    public String getNoButtonPressedFilePath() { return noButtonPressedFilePath; }
    public String getQuitButtonPressedFilePath() { return quitButtonPressedFilePath; }
    public String getRemovePlayerButtonPressedFilePath() { return removePlayerButtonPressedFilePath; }
    public String getStartButtonPressedFilePath() { return startButtonPressedFilePath; }
    public String getTransferTaskButtonPressedFilePath() { return transferTaskButtonPressedFilePath; }
    public String getYesButtonPressedFilePath() { return yesButtonPressedFilePath; }
    public String getLeftArrowButtonPressedFilePath() { return leftArrowButtonPressedFilePath; }
    public String getRightArrowButtonPressedFilePath() { return rightArrowButtonPressedFilePath; }
    public String getDescriptionFrameFilePath() { return descriptionFrameFilePath; }
    public String getLeftArrowBookFilePath() { return leftArrowBookFilePath; }
    public String getLeftArrowBookPressedFilePath() { return leftArrowBookPressedFilePath; }
    public String getRightArrowBookFilePath() { return rightArrowBookFilePath; }
    public String getRightArrowBookPressedFilePath() { return rightArrowBookPressedFilePath; }
    public String getCoinFlipTrueFilePath() { return coinFlipTrueFilePath; }
    public String getCoinFlipFalseFilePath() { return coinFlipFalseFilePath; }
    public String getCoinFlipIcon1FilePath() { return coinFlipIcon1FilePath; }
    public String getCoinFlipIcon2FilePath() { return coinFlipIcon2FilePath; }
    public String getCoinFlipIcon3FilePath() { return coinFlipIcon3FilePath; }
    public String getCoinFlipIcon4FilePath() { return coinFlipIcon4FilePath; }
    public String getCoinFlipIcon5FilePath() { return coinFlipIcon5FilePath; }
    public String getCoinFlipIcon6FilePath() { return coinFlipIcon6FilePath; }
    public String getCoinFlipIcon7FilePath() { return coinFlipIcon7FilePath; }
    public String getCoinFlipIcon8FilePath() { return coinFlipIcon8FilePath; }
    public String getCoinFlipIcon9FilePath() { return coinFlipIcon9FilePath; }
    public String getCoinFlipIcon10FilePath() { return coinFlipIcon10FilePath; }
    public String getCoinFlipIcon11FilePath() { return coinFlipIcon11FilePath; }
    public String getCoinFlipIcon12FilePath() { return coinFlipIcon12FilePath; }
    public String getCoinFlipIcon13FilePath() { return coinFlipIcon13FilePath; }
    public String getCoinFlipIcon14FilePath() { return coinFlipIcon14FilePath; }
    public String getCoinFlipIcon15FilePath() { return coinFlipIcon15FilePath; }
    public String getCoinFlipIcon16FilePath() { return coinFlipIcon16FilePath; }
    public String getCoinFlipIcon17FilePath() { return coinFlipIcon17FilePath; }
    public String getCoinFlipIcon18FilePath() { return coinFlipIcon18FilePath; }
    public String getCoinFlipIcon19FilePath() { return coinFlipIcon19FilePath; }
    public String getCoinFlipIcon20FilePath() { return coinFlipIcon20FilePath; }
}
