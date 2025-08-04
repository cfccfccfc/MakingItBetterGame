package com.cfc.makingitbettergame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.cfc.makingitbettergame.GameClasses.BoardClasses.*;
import com.cfc.makingitbettergame.GameClasses.BoardClasses.SquareIconData;
import com.cfc.makingitbettergame.GameClasses.Miscellaneous.GameController;
import com.cfc.makingitbettergame.GameClasses.Miscellaneous.GameSettings;
import com.cfc.makingitbettergame.GameClasses.Miscellaneous.Resources;
import com.cfc.makingitbettergame.GameClasses.ObjectiveRelatedClasses.Event;
import com.cfc.makingitbettergame.GameClasses.ObjectiveRelatedClasses.Objective;
import com.cfc.makingitbettergame.GameClasses.ObjectiveRelatedClasses.Task;
import com.cfc.makingitbettergame.GameClasses.PlayerClasses.Player;
import com.cfc.makingitbettergame.Screens.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import java.util.*;

public class MakingItBetterGame extends Game {
	public static int V_WIDTH = 1280;
	public static int V_HEIGHT = 720;
	public OrthographicCamera gameCam;
	public SpriteBatch batch;
	public BitmapFont fontMainMenu;
	public AssetManager assetManager;
	public Skin skin;

	public MainMenuScreen mainMenuScreen;
	public PlayScreen playScreen;
	public LoadingScreen loadingScreen;
	public VictoryScreen victoryScreen;
	public LossScreen lossScreen;
	public ManualScreen manualScreen;
	public Screen activeScreen;

	private int fontMainMenu21 = 21;
	public static int textButtonDefaultWidth = 150, textButtonDefaultHeight = 66;

	public int boardSize = 24;

	public int squareWidth = 86;
	public int squareHeight = 85;
	public int squareOffset = 11;

	public int menuButtonsSpacing = 6;
	public int menuButtonOutOfBoundsEdge = 13;
	public int menuButtonVerticalTopOffset = 4 + menuButtonOutOfBoundsEdge;
	public int menuButtonHorizontalOffset = 4;

	public int yesNoButtonsWidth = 100, yesNoButtonsHeight = 44;

	public float buttonAnimationTime = .5f;
	public float defaultButtonClickSoundVolume = .2f;

	public GameController gameController;

	@Override
	public void create () {
		assetManager = new AssetManager();
		gameCam = new OrthographicCamera();
		gameCam.setToOrtho(false, V_WIDTH, V_HEIGHT);

		batch = new SpriteBatch();

		initFonts();

		mainMenuScreen = new MainMenuScreen(this);
		playScreen = new PlayScreen(this);
		loadingScreen = new LoadingScreen(this);
		victoryScreen = new VictoryScreen(this);
		lossScreen = new LossScreen(this);
		manualScreen = new ManualScreen(this);

		setScreen(loadingScreen);
	}

	@Override
	public void render () {
		super.render();
	}

	public void dispose()
	{
		batch.dispose();
		fontMainMenu.dispose();
		assetManager.dispose();
		mainMenuScreen.dispose();
		playScreen.dispose();
		loadingScreen.dispose();
		victoryScreen.dispose();
		lossScreen.dispose();
		manualScreen.dispose();
	}

	public void initFonts()
	{
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/gameFontDefault.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter fontParameters = new FreeTypeFontGenerator.FreeTypeFontParameter();

		fontParameters.size = fontMainMenu21;
		fontParameters.color = Color.BLACK;

		fontMainMenu = generator.generateFont(fontParameters);
	}

	public void initSkin() {
		this.skin = new Skin();
		this.skin.addRegions(assetManager.get(GameSettings.getInstance().getUiSkinAtlasFilePath(), TextureAtlas.class));
		this.skin.add("default-font", fontMainMenu);
		this.skin.load(Gdx.files.internal("ui/uiskin.json"));
	}

	public void toggleManual() {
		if (getScreen() != manualScreen) {
			activeScreen = getScreen();
			setScreen(manualScreen);
		}
		else {
			setScreen(activeScreen);
		}
	}

	public void initGame() {
		SquareLinkedList board = new SquareLinkedList();

		List<String> eventSquareDescriptions = new ArrayList<>();
		List<Event.EventType> eventTypes = new ArrayList<>();
		List<List<Resources>> eventSquareResources = new ArrayList<>();

		List<Event> eventsList = new ArrayList<>();

		List<SquareIconData> squareSquareIconDataList = new ArrayList<>();

		Resources resourcePool = new Resources(GameSettings.getInstance().getDefaultMoneyQuantity(), GameSettings.getInstance().getDefaultLabourQuantity(),
				GameSettings.getInstance().getDefaultCommunitySupportQuantity(), GameSettings.getInstance().getDefaultReputationQuantity());

		List<Objective> objectivesList = new ArrayList<>();

		Random randomSeed = new Random();

		squareSquareIconDataList.addAll(populateSquareCoordinatesList());
		objectivesList.addAll(populateObjectivesList());

		eventSquareDescriptions.addAll(populateEventSquareDescriptions());
		eventTypes.addAll(populateEventTypes());
		eventSquareResources.addAll(populateEventSquareResources());

		eventsList.addAll(populateEventsList(eventTypes, eventSquareResources));

		Texture positiveEventSquareTexture = assetManager.get(GameSettings.getInstance().getPositiveEventSquareFilePath(), Texture.class);
		Texture negativeEventSquareTexture = assetManager.get(GameSettings.getInstance().getNegativeEventSquareFilePath(), Texture.class);
		Texture taskSquareTexture = assetManager.get(GameSettings.getInstance().getTaskSquareFilePath(), Texture.class);

		board.addSquare(new StartSquare(squareSquareIconDataList.getFirst()));

		int taskSquareCounter = 0, positiveEventSquareCounter = 0,  negativeEventSquareCounter = 0;

		for (int i = 1; i < boardSize; i++) {
			switch (randomSeed.nextInt(3)) {
				case (0):
					if (positiveEventSquareCounter < 9) {
						board.addSquare(new EventSquare(Square.SquareType.PositiveEventSquare, squareSquareIconDataList.get(i), new Image(positiveEventSquareTexture)));
						positiveEventSquareCounter++;
					}
					else if (negativeEventSquareCounter < 9) {
						board.addSquare(new EventSquare(Square.SquareType.NegativeEventSquare, squareSquareIconDataList.get(i), new Image(negativeEventSquareTexture)));
						negativeEventSquareCounter++;
					}
					else if (taskSquareCounter < 5) {
						board.addSquare(new TaskSquare(squareSquareIconDataList.get(i), new Image(taskSquareTexture)));
						taskSquareCounter++;
					}
					break;
				case(1):
					if (negativeEventSquareCounter < 9) {
						board.addSquare(new EventSquare(Square.SquareType.NegativeEventSquare, squareSquareIconDataList.get(i), new Image(negativeEventSquareTexture)));
						negativeEventSquareCounter++;
					}
					else if (taskSquareCounter < 5) {
						board.addSquare(new TaskSquare(squareSquareIconDataList.get(i), new Image(taskSquareTexture)));
						taskSquareCounter++;
					}
					else if (positiveEventSquareCounter < 9) {
						board.addSquare(new EventSquare(Square.SquareType.PositiveEventSquare, squareSquareIconDataList.get(i), new Image(positiveEventSquareTexture)));
						positiveEventSquareCounter++;
					}
					break;
				case(2):
					if (taskSquareCounter < 5) {
						board.addSquare(new TaskSquare(squareSquareIconDataList.get(i), new Image(taskSquareTexture)));
						taskSquareCounter++;
					}
					else if (positiveEventSquareCounter < 9) {
						board.addSquare(new EventSquare(Square.SquareType.PositiveEventSquare, squareSquareIconDataList.get(i), new Image(positiveEventSquareTexture)));
						positiveEventSquareCounter++;
					}
					else if (negativeEventSquareCounter < 9) {
						board.addSquare(new EventSquare(Square.SquareType.NegativeEventSquare, squareSquareIconDataList.get(i), new Image(negativeEventSquareTexture)));
						negativeEventSquareCounter++;
					}
					break;
			}
		}

		board.linkLastAndFirst();

		SquareNode currentSquare = board.getStartSquare().getNextSquare();

		while (!allEventsAndTasksAssigned(board)) {
			int roll;

			switch (currentSquare.getSquare().getSquareType()) {
				case TaskSquare:
					roll = randomSeed.nextInt(3);

					if (!objectivesList.getFirst().getTaskList().get(roll).getAssignedToSquare() && currentSquare.getTaskSquare().getTask() == null) {
						currentSquare.getTaskSquare().setTask(objectivesList.getFirst().getTaskList().get(roll));
						objectivesList.getFirst().getTaskList().get(roll).isAssignedToSquare();
						currentSquare.getTaskSquare().setDescription(objectivesList.getFirst().getTaskList().get(roll).getDescription());
					}

					if (objectivesList.getFirst().getTaskList().get(0).getAssignedToSquare() && objectivesList.getFirst().getTaskList().get(1).getAssignedToSquare()
					 		&& objectivesList.getFirst().getTaskList().get(2).getAssignedToSquare() && currentSquare.getTaskSquare().getTask() == null) {
						currentSquare.getTaskSquare().setTask(objectivesList.getFirst().getTaskList().get(roll));
						currentSquare.getTaskSquare().setDescription(objectivesList.getFirst().getTaskList().get(roll).getDescription());
					}

					break;
				case PositiveEventSquare:
					roll = randomSeed.nextInt(9);

					if (!eventsList.get(roll).getAssignedToSquare() && currentSquare.getEventSquare().getEvent() == null) {
						currentSquare.getEventSquare().setEvent(eventsList.get(roll));
						eventsList.get(roll).isAssignedToSquare();
						currentSquare.getEventSquare().setDescription(eventSquareDescriptions.get(roll));
					}
					break;
				case NegativeEventSquare:
					roll = randomSeed.nextInt(9) + 9;

					if (!eventsList.get(roll).getAssignedToSquare() && currentSquare.getEventSquare().getEvent() == null) {
						currentSquare.getEventSquare().setEvent(eventsList.get(roll));
						eventsList.get(roll).isAssignedToSquare();
						currentSquare.getEventSquare().setDescription(eventSquareDescriptions.get(roll));
					}
					break;
			}

			currentSquare = currentSquare.getNextSquare();
		}

		gameController = new GameController(objectivesList, resourcePool, board);
	}

	public Boolean allEventsAndTasksAssigned(SquareLinkedList board) {
		int eventsAndTasksAssignedCount = 0;
		SquareNode currentSquare = board.getStartSquare().getNextSquare();

		for (int i = 0; i < boardSize; i++) {
			switch (currentSquare.getSquare().getSquareType()) {
				case TaskSquare:
					if (currentSquare.getTaskSquare().getTask() != null)
						eventsAndTasksAssignedCount++;
					break;
				case PositiveEventSquare:
                case NegativeEventSquare:
                    if (currentSquare.getEventSquare().getEvent() != null)
						eventsAndTasksAssignedCount++;
					break;
            }

			currentSquare = currentSquare.getNextSquare();
		}

        return eventsAndTasksAssignedCount == boardSize - 1;
    }

	public List<SquareIconData> populateSquareCoordinatesList() {
		List<SquareIconData> squareSquareIconDataList = new ArrayList<>();

		squareSquareIconDataList.add(new SquareIconData(squareOffset, 6 * squareHeight + squareOffset * 4 + 2, 86, 86));
		squareSquareIconDataList.add(new SquareIconData(squareWidth + squareOffset * 2 - 7, 6 * squareHeight + squareOffset * 4 + 1, 86, 87));
		squareSquareIconDataList.add(new SquareIconData(2 * squareWidth + squareOffset * 4 - 24, 6 * squareHeight + squareOffset * 4 + 1, 87, 87));
		squareSquareIconDataList.add(new SquareIconData(3 * squareWidth + squareOffset * 5 - 29, 6 * squareHeight + squareOffset * 4 + 1, 85, 87));
		squareSquareIconDataList.add(new SquareIconData(4 * squareWidth + squareOffset * 6 - 36, 6 * squareHeight + squareOffset * 4 + 1, 85, 87));
		squareSquareIconDataList.add(new SquareIconData(5 * squareWidth + squareOffset * 7 - 42, 6 * squareHeight + squareOffset * 4 + 1, 85, 87));
		squareSquareIconDataList.add(new SquareIconData(6 * squareWidth + squareOffset * 8 - 47, 6 * squareHeight + squareOffset * 4 + 1, 84, 87));

		squareSquareIconDataList.add(new SquareIconData(6 * squareWidth + squareOffset * 8 - 47, 5 * squareHeight + squareOffset * 4 - 4, 86, 87));
		squareSquareIconDataList.add(new SquareIconData(6 * squareWidth + squareOffset * 8 - 48, 4 * squareHeight + squareOffset * 3, 86, 88));
		squareSquareIconDataList.add(new SquareIconData(6 * squareWidth + squareOffset * 8 - 47, 3 * squareHeight + squareOffset * 2 + 5, 85, 88));
		squareSquareIconDataList.add(new SquareIconData(6 * squareWidth + squareOffset * 8 - 47, 2 * squareHeight + squareOffset * 2 - 2, 86, 88));
		squareSquareIconDataList.add(new SquareIconData(6 * squareWidth + squareOffset * 8 - 47, squareHeight + squareOffset * 2 - 7, 86, 87));
		squareSquareIconDataList.add(new SquareIconData(6 * squareWidth + squareOffset * 8 - 47, squareOffset, 86, 85));

		squareSquareIconDataList.add(new SquareIconData(5 * squareWidth + squareOffset * 3 + 2, squareOffset, 86, 85));
		squareSquareIconDataList.add(new SquareIconData(4 * squareWidth + squareOffset * 3 - 3, squareOffset, 86, 86));
		squareSquareIconDataList.add(new SquareIconData(3 * squareWidth + squareOffset * 2 + 4, squareOffset, 86, 85));
		squareSquareIconDataList.add(new SquareIconData(2 * squareWidth + squareOffset * 2 - 2, squareOffset, 86, 85));
		squareSquareIconDataList.add(new SquareIconData(squareWidth + squareOffset + 4, squareOffset, 85, 85));
		squareSquareIconDataList.add(new SquareIconData(squareOffset, squareOffset, 86, 85));

		squareSquareIconDataList.add(new SquareIconData(squareOffset, squareHeight + squareOffset * 2 - 6, 86, 87));
		squareSquareIconDataList.add(new SquareIconData(squareOffset - 1, 2 * squareHeight + squareOffset * 2, 88, 87));
		squareSquareIconDataList.add(new SquareIconData(squareOffset, 3 * squareHeight + squareOffset * 3 - 6, 86, 88));
		squareSquareIconDataList.add(new SquareIconData(squareOffset, 4 * squareHeight + squareOffset * 3 - 1, 86, 89));
		squareSquareIconDataList.add(new SquareIconData(squareOffset, 5 * squareHeight + squareOffset * 4 - 5, 86, 88));

		return squareSquareIconDataList;
	}

	public List<Objective> populateObjectivesList() {
		List<Objective> objectivesList = new ArrayList<>();

		List<Task> taskListObjective1 = new ArrayList<>();
		List<Task> taskListObjective2 = new ArrayList<>();
		List<Task> taskListObjective3 = new ArrayList<>();

		HashMap<Player.PlayerExpertise, Integer> playerExpertiseBenefactorsList1 = new HashMap<>();
		HashMap<Player.PlayerExpertise, Integer> playerExpertiseBenefactorsList2 = new HashMap<>();
		HashMap<Player.PlayerExpertise, Integer> playerExpertiseBenefactorsList3 = new HashMap<>();
		HashMap<Player.PlayerExpertise, Integer> playerExpertiseBenefactorsList4 = new HashMap<>();
		HashMap<Player.PlayerExpertise, Integer> playerExpertiseBenefactorsList5 = new HashMap<>();
		HashMap<Player.PlayerExpertise, Integer> playerExpertiseBenefactorsList6 = new HashMap<>();
		HashMap<Player.PlayerExpertise, Integer> playerExpertiseBenefactorsList7 = new HashMap<>();
		HashMap<Player.PlayerExpertise, Integer> playerExpertiseBenefactorsList8 = new HashMap<>();
		HashMap<Player.PlayerExpertise, Integer> playerExpertiseBenefactorsList9 = new HashMap<>();

		playerExpertiseBenefactorsList1.put(Player.PlayerExpertise.Manager, 3);
		playerExpertiseBenefactorsList1.put(Player.PlayerExpertise.EnvironmentalScientist, 15);
		playerExpertiseBenefactorsList1.put(Player.PlayerExpertise.CommunityLeader, 10);

		playerExpertiseBenefactorsList2.put(Player.PlayerExpertise.Manager, 3);
		playerExpertiseBenefactorsList2.put(Player.PlayerExpertise.ChemicalEngineer, 15);
		playerExpertiseBenefactorsList2.put(Player.PlayerExpertise.Salesperson, 30);

		playerExpertiseBenefactorsList3.put(Player.PlayerExpertise.Manager, 3);
		playerExpertiseBenefactorsList3.put(Player.PlayerExpertise.CivilEngineer, 15);
		playerExpertiseBenefactorsList3.put(Player.PlayerExpertise.CommunityLeader, 10);

		playerExpertiseBenefactorsList4.put(Player.PlayerExpertise.Manager, 3);
		playerExpertiseBenefactorsList4.put(Player.PlayerExpertise.CommunityLeader, 15);
		playerExpertiseBenefactorsList4.put(Player.PlayerExpertise.EnvironmentalScientist, 10);

		playerExpertiseBenefactorsList5.put(Player.PlayerExpertise.Manager, 3);
		playerExpertiseBenefactorsList5.put(Player.PlayerExpertise.EnvironmentalScientist, 5);
		playerExpertiseBenefactorsList5.put(Player.PlayerExpertise.CommunityLeader, 5);

		playerExpertiseBenefactorsList6.put(Player.PlayerExpertise.Manager, 3);
		playerExpertiseBenefactorsList6.put(Player.PlayerExpertise.CommunityLeader, 15);
		playerExpertiseBenefactorsList6.put(Player.PlayerExpertise.EnvironmentalScientist, 10);

		playerExpertiseBenefactorsList7.put(Player.PlayerExpertise.Manager, 3);
		playerExpertiseBenefactorsList7.put(Player.PlayerExpertise.Salesperson, 1);
		playerExpertiseBenefactorsList7.put(Player.PlayerExpertise.CivilEngineer, 10);
		playerExpertiseBenefactorsList7.put(Player.PlayerExpertise.ChemicalEngineer, 15);

		playerExpertiseBenefactorsList8.put(Player.PlayerExpertise.Manager, 3);
		playerExpertiseBenefactorsList8.put(Player.PlayerExpertise.CommunityLeader, 5);

		playerExpertiseBenefactorsList9.put(Player.PlayerExpertise.Manager, 3);
		playerExpertiseBenefactorsList6.put(Player.PlayerExpertise.EnvironmentalScientist, 15);
		playerExpertiseBenefactorsList9.put(Player.PlayerExpertise.CommunityLeader, 10);

		taskListObjective1.add(new Task(1, "Assess Environmental \nImpact", "Assess Environmental Impact: Before construction, perform " +
				"an environmental " +
				"assessment to ensure the chosen location " +
				"and construction methods minimize harm to the local ecosystem. The decision for the water filter implementation, need to choose strategic locations for " +
				"the water filters considering the village layout and water source proximity. " +
				"(2,400,000) (3% Manager, 15% Environmentalist, 10% Community leader)",
				GameSettings.getInstance().getDefaultMoraleCost(), playerExpertiseBenefactorsList1,
				new Resources(2400000, 0, 0, 0)));

		taskListObjective1.add(new Task(2, "Design of the water \nfilter system","Design of the water filter system: Secure funding for tanks, " +
				"hoses, and sand, along with a suitable" +
				" water pump, possibly exploring biogas options for environmental sustainability/petrol. Installation of Water Filters: Set up 4 gravity-fed water systems, " +
				"each with four 500-liter tanks, this steps including calculating the budget and how this installation is possible. " +
				"(13,000,000) (3% Manager, 15% Chemical, 30% Salesman)",
				GameSettings.getInstance().getDefaultMoraleCost(), playerExpertiseBenefactorsList2,
				new Resources(13000000, 0, 0, 0)));

		taskListObjective1.add(new Task(3, "Construction\n","Construction: Mobilise community labour for construction and implementation, " +
				"emphasising the simplicity and " +
				"cost-effectiveness of the setup. This includes: " +
				"Gather and preparation for the labour " +
				"Secure Construction Permits: Navigate the bureaucratic process to obtain necessary permits, adding a layer of realism related to regulatory compliance." +
				"(13,000,000) (3% Manager, 15% Civil, 10% Community leader)",
				GameSettings.getInstance().getDefaultMoraleCost(), playerExpertiseBenefactorsList3,
				new Resources(13000000, 0, 0, 0)));

		taskListObjective2.add(new Task(1, "Develop Educational \nfor The Awareness","Develop Educational for The Awareness: " +
				"Develop Educational Materials: Create brochures, posters to distribute in the community." +
				"Conduct Workshops: Organise educational workshops for children in schools focusing on the importance of clean water and the gravity-fed water " +
				"filter. " +
				"Community Talks: Engage with villagers to underscore the benefits of using clean water from the newly installed system." +
				"(20,000,000) (3% Manager, 3% Civil, 15% Community leader, 10% Environmental)",
				GameSettings.getInstance().getDefaultMoraleCost(), playerExpertiseBenefactorsList4,
				new Resources(20000000, 0, 0, 0)));

		taskListObjective2.add(new Task(2, "Organise Community \nClean-Up","Organise Community Clean-Up: To complement the talks, " +
				"organise a clean-up of " +
				"local water sources to physically " +
				"demonstrate the importance of clean water. Demonstrate on how to change the filter also crucial, show practical demonstrations on changing the sand " +
				"filter and maintaining the system for long-term use." +
				"(900,000) (3% manager, 5% environmental, 5% community leader)",
				GameSettings.getInstance().getDefaultMoraleCost(), playerExpertiseBenefactorsList5,
				new Resources(900000, 0, 0, 0)));

		taskListObjective2.add(new Task(3, "Launch a Social \nMedia Campaign","Launch a Social Media Campaign: Use social media platforms " +
				"to reach a wider " +
				"audience, sharing information about the " +
				"project and how the community can contribute." +
				"(5,000,000) (3% Manager, 15% Community leader, 10% Environmental)",
				GameSettings.getInstance().getDefaultMoraleCost(), playerExpertiseBenefactorsList6,
				new Resources(5000000, 0, 0, 0)));

		taskListObjective3.add(new Task(1, "Basic reparation and \nclean check","Basic reparation and clean check: " +
				"Change the Sand Filter: Plan and execute the annual replacement of sand in the filters as part of maintenance." +
				"Clean the Tank: Schedule tank cleaning to prevent algae growth and sediment buildup." +
				"(200,000) (3% manager, 1% Salesman, 10% Civil, 15% Chemical)",
				GameSettings.getInstance().getDefaultMoraleCost(), playerExpertiseBenefactorsList7,
				new Resources(200000, 0, 0, 0)));

		taskListObjective3.add(new Task(2, "Monitor System \nPerformance","Monitor System Performance: Implement a task where players " +
				"must regularly check the " +
				"system's performance and make " +
				"necessary adjustments. Quality Control: Regularly check the water quality to ensure it meets health standards." +
				"(200,000) (3% manager, 5% community leader)",
				GameSettings.getInstance().getDefaultMoraleCost(), playerExpertiseBenefactorsList8,
				new Resources(200000, 0, 0, 0)));

		taskListObjective3.add(new Task(3, "Sustainability Planning\n","Sustainability Planning: Plan for long-term sustainability by " +
				"setting aside resources " +
				"for future repairs and upgrades." +
				"(118,00) (3% manager, 15% environmental, 10% community leader)",
				GameSettings.getInstance().getDefaultMoraleCost(), playerExpertiseBenefactorsList9,
				new Resources(118000, 0, 0, 0)));

		objectivesList.add(new Objective("Build water filter", taskListObjective1));
		objectivesList.add(new Objective("Raise awareness", taskListObjective2));
		objectivesList.add(new Objective("Maintain water filter", taskListObjective3));

		return objectivesList;
	}

	public List<Event.EventType> populateEventTypes() {
		List<Event.EventType> eventSquareTypes = new ArrayList<>();

        eventSquareTypes.addAll(Arrays.asList(Event.EventType.values()));

		return eventSquareTypes;
	}

	public List<List<Resources>> populateEventSquareResources() {
		List<List<Resources>> eventSquareResources = new ArrayList<>();

		List<Resources> eventSquareResource1 = new ArrayList<>();
		eventSquareResource1.add(new Resources(0,0,0,0));

		List<Resources> eventSquareResource2 = new ArrayList<>();
		eventSquareResource2.add(new Resources(50000,0,0,0));

		List<Resources> eventSquareResource3 = new ArrayList<>();
		eventSquareResource3.add(new Resources(0,0,0,0));

		List<Resources> eventSquareResource4 = new ArrayList<>();
		eventSquareResource4.add(new Resources(5000,2,0,0));

		List<Resources> eventSquareResource5 = new ArrayList<>();
		eventSquareResource5.add(new Resources(0,0,0,0));
		eventSquareResource5.add(new Resources(1000000,0,2,0));

		List<Resources> eventSquareResource6 = new ArrayList<>();
		eventSquareResource6.add(new Resources(0,0,0,3));

		List<Resources> eventSquareResource7 = new ArrayList<>();
		eventSquareResource7.add(new Resources(0,0,0,0));
		eventSquareResource7.add(new Resources(5000000,0,1,2));

		List<Resources> eventSquareResource8 = new ArrayList<>();
		eventSquareResource8.add(new Resources(0,0,0,2));

		List<Resources> eventSquareResource9 = new ArrayList<>();
		eventSquareResource9.add(new Resources(0,0,0,0));
		eventSquareResource9.add(new Resources(2000000,0,1,0));

		List<Resources> eventSquareResource10 = new ArrayList<>();
		eventSquareResource10.add(new Resources(0,3,0,0));

		List<Resources> eventSquareResource11 = new ArrayList<>();
		eventSquareResource11.add(new Resources(0,1,0,0));

		List<Resources> eventSquareResource12 = new ArrayList<>();
		eventSquareResource12.add(new Resources(0,5,0,0));
		eventSquareResource12.add(new Resources(500000,0,0,0));

		List<Resources> eventSquareResource13 = new ArrayList<>();
		eventSquareResource13.add(new Resources(500000,0,0,0));

		List<Resources> eventSquareResource14 = new ArrayList<>();
		eventSquareResource14.add(new Resources(0,0,0,2));
		eventSquareResource14.add(new Resources(300000,0,0,0));

		List<Resources> eventSquareResource15 = new ArrayList<>();
		eventSquareResource15.add(new Resources(0,0,1,0));

		List<Resources> eventSquareResource16 = new ArrayList<>();
		eventSquareResource16.add(new Resources(0,0,0,0));

		List<Resources> eventSquareResource17 = new ArrayList<>();
		eventSquareResource17.add(new Resources(0,0,0,2));

		List<Resources> eventSquareResource18 = new ArrayList<>();
		eventSquareResource18.add(new Resources(1000000,0,0,0));

		eventSquareResources.add(eventSquareResource1);
		eventSquareResources.add(eventSquareResource2);
		eventSquareResources.add(eventSquareResource3);
		eventSquareResources.add(eventSquareResource4);
		eventSquareResources.add(eventSquareResource5);
		eventSquareResources.add(eventSquareResource6);
		eventSquareResources.add(eventSquareResource7);
		eventSquareResources.add(eventSquareResource8);
		eventSquareResources.add(eventSquareResource9);
		eventSquareResources.add(eventSquareResource10);
		eventSquareResources.add(eventSquareResource11);
		eventSquareResources.add(eventSquareResource12);
		eventSquareResources.add(eventSquareResource13);
		eventSquareResources.add(eventSquareResource14);
		eventSquareResources.add(eventSquareResource15);
		eventSquareResources.add(eventSquareResource16);
		eventSquareResources.add(eventSquareResource17);
		eventSquareResources.add(eventSquareResource18);

		return eventSquareResources;
	}

	public List<String> populateEventSquareDescriptions() {
		List<String> eventSquareDescriptions = new ArrayList<>();

		eventSquareDescriptions.add("Community Volunteer Day: \nOn Community Volunteer Day, the village rallies together, waving aside labour costs for the " +
				"next round. " +
				"It's a day of collective effort, where neighbors join forces to tackle tasks big and small, strengthening bonds and accelerating progress.");

		eventSquareDescriptions.add("Government Grant: \nA Government Grant arrives as a beacon of support, injecting a significant sum - 5,000 times the " +
				"village's reputation " +
				"score - into the project's coffers. With this boost, villagers can pursue ambitious goals and realize their aspirations for the community's betterment.");

		eventSquareDescriptions.add("Donation of Materials: \nGenerosity abounds as the village receives a Donation of " +
				"Materials, offering " +
				"a 15% discount on Task 2 of Objective 1 (Gathering resources). It's a tangible display of solidarity from within, allowing resources to be " +
				"stretched further towards shared objectives.");

		eventSquareDescriptions.add("Local Business Sponsorships: \nLocal businesses step up to the plate, offering financial support equivalent to 500 times the " +
				"village's reputation score, along with two additional labourers. This partnership between commerce and community fuels progress and fosters mutual benefit.");

		eventSquareDescriptions.add("Village Fair: \nPress yes to spend 1,000,000 KHR to hold a village fair? If so, enthusiasm will be met by the village as they " +
				"gear up for a day of celebration. Despite the initial cost, the fair will boost community support by 2 points, serving as a joyous occasion " +
				"that strengthens bonds and uplifts spirits.");

		eventSquareDescriptions.add("Media Coverage: \nThe village gains visibility as Media Coverage shines a spotlight on its endeavors, boosting reputation by 3 " +
				"points. It's more than just publicity; it's validation of the village's efforts and an invitation for others to take notice.");

		eventSquareDescriptions.add("Technical Training Workshop: \nAn opportunity to hold a big workshop has arisen! Pressing yes to spend 5,000,000 KHR for it yields " +
				"long-term benefits, with a modest increase of 1 point to community support and a reputation boost of 2 points. Equipped with newfound skills, " +
				"villagers can tackle challenges more efficiently, driving progress and innovation.");

		eventSquareDescriptions.add("Recognition by Health Organizations: \nHealth Organizations lend their endorsement to the village's efforts, boosting reputation by " +
				"2 points. It's a testament to the project's impact on well-being and sustainability, inspiring confidence and reinforcing the village's " +
				"commitment to health.");

		eventSquareDescriptions.add("Innovative Water Saving Techniques Workshop: \nPress yes to hold a workshop! Choosing to spent 2,000,000 KHR " +
				"to host this workshop demonstrates a commitment to responsible water management. Despite the initial cost, the village will gain 1 point " +
				"of community support, while acquiring knowledge that promotes sustainability and resilience for the future.");

		eventSquareDescriptions.add("Flood: \nA flood strikes, and the village faces a setback as labourers decrease by 3. It's a daunting challenge, requiring " +
				"swift action and resilience to overcome the aftermath and restore normalcy to the community.");

		eventSquareDescriptions.add("Drought: \nA drought strikes, decreasing labourers by 1, and objectives cannot be completed on the next player's turn. " +
				"It's a sobering reminder of nature's unpredictability, calling for careful resource management and adaptation to ensure the village's survival.");

		eventSquareDescriptions.add("Worker Injury: \nA worker has gotten injured! Press yes to spend 500,000 KHR to negate the impact or accept a reduction of 5 labour units. " +
				"It's a stark reminder of the risks inherent in community endeavors, highlighting the importance of safety measures and support for injured workers.");

		eventSquareDescriptions.add("Supply Shortage: \nWith a shortage in supplies, the village faces a financial blow as money decreases by 500,000 KHR. It's a test " +
				"of resourcefulness, requiring villagers to prioritize needs and find creative solutions to stretch limited resources.");

		eventSquareDescriptions.add("Equipment Broken: \nEquipment has broken down, villagers face a tough decision: Press yes to 300,000 KHR to negate the damage or accept a " +
				"reputation decrease of 2 points. It's a lesson in the importance of maintenance and preparedness, highlighting the cost of neglecting essential " +
				"infrastructure.");

		eventSquareDescriptions.add("Supply Delay: \nA supply delay dampens community spirits as community support decreases by 1. It's a frustrating setback, testing " +
				"patience and resilience as villagers await the arrival of essential resources.");

		eventSquareDescriptions.add("Workers Holiday: \nWorkers' Holiday brings an unexpected financial burden as money instantly decreases by the labourer's cost for the " +
				"round. It's a reminder of the importance of workforce stability and contingency planning to mitigate disruptions to essential projects.");

		eventSquareDescriptions.add("Low Awareness Opposition: \nThere is a lack of project awareness within the village! Reputation " +
				"decreases by 2. It's a wake-up call to prioritize communication and engagement efforts to build understanding and support for the village's initiatives.");

		eventSquareDescriptions.add("Permit Issues: \nPermit issues present a costly obstacle as money decreases by 1,000,000 KHR in order to bribe corrupt officials. It's " +
				"a test of integrity and resourcefulness, requiring villagers to navigate bureaucratic hurdles while upholding ethical standards and commitments to " +
				"transparency.");

		return eventSquareDescriptions;
	}

	public List<Event> populateEventsList(List<Event.EventType> eventTypes, List<List<Resources>> eventSquareResources) {
		List<Event> eventsList = new ArrayList<>();

		for (int i = 0; i < eventTypes.size(); i++)
			eventsList.add(new Event(eventTypes.get(i), eventSquareResources.get(i)));

		return eventsList;
	}
}