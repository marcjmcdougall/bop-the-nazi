package com.bopthenazi.views.screens;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.bopthenazi.game.BTNGame;
import com.bopthenazi.models.BTNActor;
import com.bopthenazi.models.BTNContainedActor;
import com.bopthenazi.models.BTNStage;
import com.bopthenazi.models.BasicButton;
import com.bopthenazi.models.Bunny;
import com.bopthenazi.models.Container;
import com.bopthenazi.models.Dynamite;
import com.bopthenazi.models.Explosion;
import com.bopthenazi.models.GameOverModule;
import com.bopthenazi.models.Glove;
import com.bopthenazi.models.Heart;
import com.bopthenazi.models.LivesModule;
import com.bopthenazi.models.PauseScreenModule;
import com.bopthenazi.models.Score;
import com.bopthenazi.models.Timer;
import com.bopthenazi.models.TutorialScreenModule;
import com.bopthenazi.models.Zombie;
import com.bopthenazi.models.ZombieBunny;
import com.bopthenazi.utils.DifficultyManager;
import com.bopthenazi.utils.SaveManager;
import com.bopthenazi.utils.SoundManager;

public class BTNGameScreen implements Screen{

	public static final float GAME_WIDTH = 1080.0f;
	public static final float GAME_HEIGHT = 1920.0f;

	public static final float AD_TOP_OFFSET = 150.0f;

	public static final float BAR_HEIGHT = 3250.0f;
	public static final float ZOMBIE_OFFSET_HORIZONTAL_MARGIN = 25.0f;
	public static final float BAR_OFFSET_LOWER = 136.3f;
	public static final float BAR_OFFSET_TOP = 256.0f;

	public static final float TOP_BAR_TOP = GAME_HEIGHT * 1.62f;
	public static final float BOTTOM_BAR_BOTTOM = GAME_HEIGHT * -0.72f;

	public static final float TOP_BAR_TOGETHER = GAME_HEIGHT * 1.2925f;
	public static final float BOTTOM_BAR_TOGETHER = GAME_HEIGHT * -0.40f;

	private static final int MODE_STANDARD = 0;
	public static final int MODE_APOCALYPSE = 1;

	private int mode;
	
	private DifficultyManager difficultyManager;

	private AssetManager assetManager;

	private BTNGame game;

	private BTNStage gameStage;
	private Stage hudStage;

	private SaveManager saveManager;
	private SoundManager soundManager;

	private static final int LAYOUT_NORMAL = 0;
	private static final int LAYOUT_U = 1;
	private static final int CONTAINER_LAYOUT = LAYOUT_NORMAL;

	//	private static final float[] NORMAL_CONTAINER_COORDINATES = {137.625f, 540.0f, 942.375f, 341.3125f, 752.6875f};
	private static final float[] NORMAL_CONTAINER_COORDINATES = {137.625f, 540.0f, 942.375f, 331.3125f, 742.6875f};

	private Array<Container> containers;

	private GameOverModule gameOverScreen;
	private TutorialScreenModule tutorialScreen;
	private PauseScreenModule pauseScreen;
	private Image soundControl;
	private Image pauseControl;
	private Glove glove;
	private BTNActor bg;
	private BTNActor explosionSplash;
	private Score score;
	private Timer timer;
	private BTNActor topBar;
	private BTNActor bottomBar;
	private BTNActor gloveCase;
	private LivesModule livesModule;
	
	private BTNActor one;
	private BTNActor two;
	private BTNActor three;
	private BTNActor bop;

	private float timeElapsedSinceLastZombie;

	private boolean gamePaused;

	private boolean tempTrue;

	private static final String TEXTURE_PREPEND = "textures/";
	private static final String FONTS_PREPEND = "fonts/";
	private static final String MUSIC_PREPEND = "music/";

	private static final float NUMBER_WIDTH = 50.0f;

	public BTNGameScreen(BTNGame game){

		this.game = game;

		this.assetManager = new AssetManager();
		this.soundManager = new SoundManager(assetManager);

		beginAssetLoad();
	}

	public void beginAssetLoad(){
		
		// Load the fonts...
		FileHandleResolver resolver = new InternalFileHandleResolver();
		getAssetManager().setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
		getAssetManager().setLoader(BitmapFont.class, ".otf", new FreetypeFontLoader(resolver));

		FreeTypeFontLoaderParameter size40Params = new FreeTypeFontLoaderParameter();
		size40Params.fontFileName = "fonts/masaaki-regular.otf";
		size40Params.fontParameters.size = 40;
		getAssetManager().load("masaaki-regular-40.otf", BitmapFont.class, size40Params);

		FreeTypeFontLoaderParameter size70Params = new FreeTypeFontLoaderParameter();
		size70Params.fontFileName = "fonts/masaaki-regular.otf";
		size70Params.fontParameters.size = 70;
		getAssetManager().load("masaaki-regular-70.otf", BitmapFont.class, size70Params);

		FreeTypeFontLoaderParameter size80Params = new FreeTypeFontLoaderParameter();
		size80Params.fontFileName = "fonts/masaaki-regular.otf";
		size80Params.fontParameters.size = 80;
		getAssetManager().load("masaaki-regular-80.otf", BitmapFont.class, size80Params);
		
		FreeTypeFontLoaderParameter size120Params = new FreeTypeFontLoaderParameter();
		size120Params.fontFileName = "fonts/masaaki-regular.otf";
		size120Params.fontParameters.size = 120;
		getAssetManager().load("masaaki-regular-120.otf", BitmapFont.class, size120Params);	
		
		getAssetManager().load("textures/textures-packed/game.atlas", TextureAtlas.class);

		// Load sounds...
		soundManager.beginLoadSFX();

		// Load music...
//		loadMusic("8-bit-dungeon-boss.mp3");

		// Load fonts...
		// TODO: Implementation.
	}

	private void loadFont(String fileNamePostPrepend){

		this.assetManager.load(FONTS_PREPEND + fileNamePostPrepend, BitmapFont.class);
	}

	private void loadTexture(String fileNamePostPrepend){

		this.assetManager.load(TEXTURE_PREPEND + fileNamePostPrepend, Texture.class);
	}

	private void loadMusic(String fileNamePostPrepend){

		this.assetManager.load(MUSIC_PREPEND + fileNamePostPrepend, Music.class);
	}

	public void notifyNewX(float x) {

		if(!isPaused()){

			glove.notifyTouch(x);
		}
	}

	private void generateExplosion(float x, float y, boolean expand) {

		Explosion explosion = new Explosion(x, y, expand, this);
		gameStage.addActor(explosion);
	}

	private void activateContainerContents(Container container){

		BTNContainedActor contents = container.getContents(); 

		if(contents != null && !isPaused()){

			contents.activate();
		}
	}

	private void initializeContainers() {

		Group background = new Group();
		Group foreground = new Group();

		for(int i = 0; i < DifficultyManager.MAX_CONTAINERS; i++){

			initializeContainer(i);
		}

		for(int i = 0; i < DifficultyManager.MAX_CONTAINERS; i++){

			if(i < 3){

				foreground.addActor(containers.get(i));
			}
			else if(i >=3){

				background.addActor(containers.get(i));
			}
		}

		gameStage.addActor(background);
		gameStage.addActor(foreground);
	}

	private void initializeContainer(int i) {

		if(i < 3){

			containers.add(new Container(NORMAL_CONTAINER_COORDINATES[i], (BAR_OFFSET_LOWER + ZOMBIE_OFFSET_HORIZONTAL_MARGIN)/* - AD_TOP_OFFSET*/, this));
		}
		else if(i >= 3){

			containers.add(new Container(NORMAL_CONTAINER_COORDINATES[i], ((BAR_OFFSET_LOWER + ZOMBIE_OFFSET_HORIZONTAL_MARGIN) * 2)/* - AD_TOP_OFFSET*/, this));
		}
	}

	private void initializeLivesModule() {

		this.livesModule = new LivesModule(this);

		for(BTNActor heartOutline : livesModule.getHeartOutlines()){

			hudStage.addActor(heartOutline);
		}

		for(BTNActor heart : livesModule.getHearts()){

			hudStage.addActor(heart);
		}
	}

	public void reset(){

		//		this.gameOverScreen.getGameOverAlpha().addAction(Actions.fadeOut(1.0f));
		gameOverScreen.addAction(Actions.sequence(Actions.fadeOut(1.0f), Actions.run(new Runnable() {

			@Override
			public void run() {

				// Remove all the actors.
				//				gameStage.getActors().removeRange(0, gameStage.getActors().size - 1);

				// TODO: Alongside issue #33, this needs to be changed to simply rework the state variables of the game.
				// Simply call "show" again to restart the whole game.
				//				BTNGameScreen.this.show();
			}
		})));

		BTNGameScreen.this.softReset();
	}

	private void softReset(){

		this.setMode(MODE_STANDARD);
		this.soundControl.setY(soundControl.getY() - 1000.0f);
		this.pauseControl.setY(pauseControl.getY() - 1000.0f);
		this.score.updateScore(0);
		this.score.setLives(Score.DEFAULT_NUMBER_LIVES);
		this.livesModule.reset();
		this.timer.reset();
		this.glove.setX(GAME_WIDTH / 2.0f);
		this.getDifficultyManager().reset();

		for(Container container : containers){

			if(container.getContents() != null){

				container.getContents().remove();

				container.setContents(generateRandomContainedActor(false, container));
			}
		}

		this.begin(0.0f);
	}

	private BTNContainedActor generateRandomContainedActor(boolean generateDynamite, Container c) {

		BTNContainedActor newActor = null;

		if(getMode() == MODE_STANDARD){

			float cursor = new Random().nextInt(10);

			// 70% chance...
			if(cursor < 7){

				cursor = new Random().nextInt(2);

				// 30% chance...
				if(cursor == 0){

					newActor = new Zombie(0.0f, 0.0f, this, c);
				}
				// 30% chance...
				else{

					newActor = new ZombieBunny(0.0f, 0.0f, this, c);						
				}
			}
			// 20% chance...
			else if(cursor < 9){

				newActor = new Bunny(0.0f, 0.0f, this, c);
			}
			// 10% chance...
			else{

				cursor = new Random().nextInt(2);

				// 2.5% chance...
				if(cursor == 0 && this.score.getLives() < Score.DEFAULT_NUMBER_LIVES){

					newActor = new Heart(0.0f, 0.0f, this, c);
				}
				// 2.5% chance...
				else{

					if(generateDynamite){

						newActor  = new Dynamite(0.0f, 0.0f, this, c);	
					}
					else{

						newActor = new Zombie(0.0f, 0.0f, this, c);
					}
				}
			}

		}
		else if(getMode() == MODE_APOCALYPSE){

			newActor = new Random().nextInt(2) == 1 ? new Zombie(0.0f, 0.0f, this, c) : new ZombieBunny(0.0f, 0.0f, this, c);
		}

		return newActor;
	}

	@Override
	public void show() {

		assetManager.finishLoading();

		initialize();
		
		tempTrue = true;

		if(saveManager.isFirstShot()){

			tempTrue = false;
			showTutorialScreen();
		}
		else{

			begin(0.5f);
		}
	}

	private void initialize() {

		// TODO: This method takes 1 second to complete.  This is causing a ton of issues.
		tutorialScreen = new TutorialScreenModule(this);
		
		pauseScreen = new PauseScreenModule(this);

		// The game over screen is providing the MOST amount of latency here.
		gameOverScreen = new GameOverModule(this);
		this.difficultyManager = new DifficultyManager();

		this.setGamePaused(true);

		// This seems to cause quite a bit of lag...V
		this.containers = new Array<Container>(DifficultyManager.MAX_CONTAINERS);
		this.score = new Score(GAME_WIDTH / 2.0f, (GAME_HEIGHT - Score.SCORE_HEIGHT) - AD_TOP_OFFSET, this);
		
		this.timer = new Timer(GAME_WIDTH / 2.0f, (GAME_HEIGHT - Score.SCORE_HEIGHT) - AD_TOP_OFFSET, this);
		//..........................................A

		this.score.getColor().a = 0.0f;
		this.timer.getColor().a = 0.0f;

		this.setMode(MODE_STANDARD);

		timeElapsedSinceLastZombie = 0f;

		//		FitViewport viewport = new FitViewport(GAME_WIDTH, GAME_HEIGHT);
		//		FillViewport viewport = new FillViewport(GAME_WIDTH, GAME_HEIGHT);
		StretchViewport viewport = new StretchViewport(GAME_WIDTH, GAME_HEIGHT);

		gameStage = new BTNStage(viewport, game, this);
		//		hudStage = new Stage(viewport);

		// Reuse the batch for efficiency-reasons.
		hudStage = new Stage(viewport, gameStage.getBatch());

		pauseControl = new Image(getTexture("pause"));
		pauseControl.setSize(65.0f, 70.0f);
		pauseControl.setX(150.0f);
		pauseControl.setY(LivesModule.HEART_Y - pauseControl.getHeight() / 2.0f - 5.0f + 18.0f);
		
		pauseControl.addListener(new InputListener(){
			
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				
				super.touchDown(event, x, y, pointer, button);
				
				soundManager.playSound(SoundManager.SOUND_ID_CLICK_DOWN);
				
				showPauseScreen();
				
				return true;
			}
			
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				
				soundManager.playSound(SoundManager.SOUND_ID_CLICK_UP);
				
				super.touchUp(event, x, y, pointer, button);
			}
		});
		
		soundControl = new Image(getTexture("mute-off"));
		soundControl.setSize(100.0f, 81.0f);
		soundControl.setX(25.0f);
		soundControl.setY(LivesModule.HEART_Y - soundControl.getHeight() / 2.0f + 15.0f);
		
		soundControl.addListener(new InputListener(){
			
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				
				super.touchDown(event, x, y, pointer, button);
				
				soundManager.toggleSound();
				
				if(soundManager.isMuted()){
					
					soundControl.setDrawable(new TextureRegionDrawable(getTexture("mute-on")));
				}
				else{
					
					soundControl.setDrawable(new TextureRegionDrawable(getTexture("mute-off")));
				}
				
				return true;
			}
			
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				
				super.touchUp(event, x, y, pointer, button);
				
			}
		});
		
		bg = new BTNActor(getTexture("background"), GAME_WIDTH / 2.0f, (GAME_HEIGHT / 2.0f)/* - AD_TOP_OFFSET*/, GAME_WIDTH, GAME_HEIGHT);
		gloveCase = new BTNActor(getTexture("mover"), GAME_WIDTH / 2.0f, (GAME_HEIGHT - 350.0f) - AD_TOP_OFFSET, 162.0f, 138.6f);
		glove = new Glove(GAME_WIDTH / 2.0f, Glove.GLOVE_UNLOCK_BARRIER, Glove.GLOVE_WIDTH, Glove.GLOVE_HEIGHT, this, gloveCase);
		topBar = new BTNActor(getTexture("top-bar"), GAME_WIDTH / 2.0f, /*(GAME_HEIGHT - TOP_BAR_HEIGHT / 2.0f) - AD_TOP_OFFSET*/TOP_BAR_TOGETHER, GAME_WIDTH, BAR_HEIGHT);
		bottomBar = new BTNActor(getTexture("bottom-bar"), GAME_WIDTH / 2.0f, BOTTOM_BAR_TOGETHER, GAME_WIDTH, BAR_HEIGHT);

		saveManager = new SaveManager();

		explosionSplash = new BTNActor(getTexture("explosion-splash"), GAME_WIDTH / 2.0f, GAME_HEIGHT / 2.0f, GAME_WIDTH, GAME_HEIGHT);

		gameStage.addActor(bg);

		initializeContainers();

		explosionSplash.setVisible(false);

		gameStage.addActor(glove);
		gameStage.addActor(gloveCase);
		gameStage.addActor(explosionSplash);

		hudStage.addActor(soundControl);
		hudStage.addActor(pauseControl);
		initializeLivesModule();
		hudStage.addActor(topBar);
		hudStage.addActor(bottomBar);
		hudStage.addActor(this.timer);

		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(hudStage);
		multiplexer.addProcessor(gameStage);

		Gdx.input.setInputProcessor(multiplexer);

		Random r = new Random();

		int randomIndex = r.nextInt(DifficultyManager.MAX_CONTAINERS);

		hudStage.addActor(gameOverScreen.getGameOverAlpha());
		hudStage.addActor(gameOverScreen.getCopyrightLabel());
		hudStage.addActor(gameOverScreen.getPencil());
		hudStage.addActor(gameOverScreen.getReviewLabel());
		hudStage.addActor(gameOverScreen);
		
		hudStage.addActor(pauseScreen);

		hudStage.addActor(tutorialScreen);

		activateContainerContents(containers.get(randomIndex));
	}

	public void begin(float initialDelay){

		// We delay everything by one second to allow the slow Android OS to catch up.
		timer.addAction(Actions.sequence(Actions.delay(initialDelay), Actions.run(new Runnable() {

			@Override
			public void run() {

				timer.addAction(Actions.fadeIn(1.0f));
				topBar.addAction(Actions.moveTo(topBar.getX(), TOP_BAR_TOP, 1.0f, Interpolation.pow4));
				bottomBar.addAction(Actions.moveTo(bottomBar.getX(), BOTTOM_BAR_BOTTOM, 1.0f, Interpolation.pow4));

				soundManager.playSound(SoundManager.SOUND_ID_LETS_GO);
				BTNGameScreen.this.setGamePaused(false);
			}
		})));

		//		three.addAction(Actions.sequence(Actions.alpha(1.0f), Actions.fadeOut(1.0f)));
		//		three.addAction(Actions.sequence(Actions.scaleBy(NUMBER_SCALE, NUMBER_SCALE, 1.0f), Actions.run(new Runnable() {
		//			
		//			@Override
		//			public void run() {
		//				
		//				two.addAction(Actions.sequence(Actions.alpha(1.0f), Actions.fadeOut(1.0f)));
		//				two.addAction(Actions.sequence(Actions.scaleBy(NUMBER_SCALE, NUMBER_SCALE, 1.0f), Actions.run(new Runnable() {
		//					
		//					@Override
		//					public void run() {
		//						
		//						one.addAction(Actions.sequence(Actions.alpha(1.0f), Actions.fadeOut(1.0f)));
		//						one.addAction(Actions.sequence(Actions.scaleBy(NUMBER_SCALE, NUMBER_SCALE, 1.0f), Actions.run(new Runnable() {
		//							
		//							@Override
		//							public void run() {
		//								
		//								playSound(SOUND_ID_LETS_GO);
		//								bop.addAction(Actions.sequence(Actions.alpha(1.0f), Actions.fadeOut(1.0f)));
		//								bop.addAction(Actions.sequence(Actions.scaleBy(NUMBER_SCALE, NUMBER_SCALE, 1.0f), Actions.run(new Runnable() {
		//									
		//									@Override
		//									public void run() {
		//										
		//										playSound(SOUND_ID_LETS_GO);
		//										BTNGameScreen.this.setGamePaused(false);
		//									}
		//									
		//								})));
		//							}
		//							
		//						})));
		//					}
		//					
		//				})));
		//			}
		//			
		//		})));
	}

	private void showTutorialScreen() {

		this.setGamePaused(true);

		tutorialScreen.doAnimate();

	}
	
	public void showPauseScreen(){

		this.setGamePaused(true);
		pauseScreen.setVisible(true);
	}
	
	public void hidePauseScreen(){
		
		soundManager.playSound(SoundManager.SOUND_ID_LETS_GO);
		pauseScreen.setVisible(false);
		this.setGamePaused(false);
	}

	public TextureRegion getTexture(String textureNamePostPrepend) {

		return assetManager.get("textures/textures-packed/game.atlas", TextureAtlas.class).findRegion(textureNamePostPrepend);
	}


	public Music getMusic(String musicNamePostPrepend){

		return assetManager.get(MUSIC_PREPEND + musicNamePostPrepend, Music.class);
	}

	private void setGamePaused(boolean paused) {

		this.gamePaused = paused;
	}

	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if(!this.isPaused()){

			timeElapsedSinceLastZombie += delta;

			if(timeElapsedSinceLastZombie >= getDifficultyManager().getNewContainerSpawnRate()){

				// Activate a random Nazi that has *not yet been activated*
				doActivateUniqueContainer(null);

				timeElapsedSinceLastZombie = 0.0f;
			}

			timer.updateTimer(delta);
			
			getDifficultyManager().updateDifficulty(delta);
			gameStage.act(delta);
		}
		
		hudStage.act(delta);

		gameStage.draw();
		hudStage.draw();
	}

	public boolean isPaused() {

		return gamePaused;
	}

	private void doActivateUniqueContainer(Container containerBopped){

		// If no Nazis are already activated, then choose one at random.
		int numContainersActivated = 0;

		boolean nonDynamiteContentActive = false;

		for(Container container : containers){

			if(container.getContents().isActivated()){

				numContainersActivated++;

				if(!container.getContents().getClass().getName().equals(Dynamite.class) && !nonDynamiteContentActive){

					nonDynamiteContentActive = true;
				}
			}
		}

		if(numContainersActivated < getDifficultyManager().getMaxConcurrentContainers()){

			for(int i = 0; i < DifficultyManager.MAX_CONTAINERS; i++){

				// Select a random number.
				int index = new Random().nextInt(DifficultyManager.MAX_CONTAINERS);

				if(containers.get(index).getContents() != null){

					if(containerBopped != null){

						if(containers.get(index).equals(containerBopped)){

							// If that container is the same one that was just bopped, skip this iteration of the loop.
							continue;
						}
					}

					if(!containers.get(index).getContents().isActivated()){

						containers.get(index).getContents().remove();

						containers.get(index).setContents(generateRandomContainedActor(nonDynamiteContentActive, containers.get(index)));

						activateContainerContents(containers.get(index));
						break;
					}
				}
			}
		}

		timeElapsedSinceLastZombie = 0f;
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

		if(!(gameOverScreen.isVisible() || tutorialScreen.isVisible())){
			
			showPauseScreen();
		}
	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {

		// TODO Auto-generated method stub
		gameStage.dispose();
		assetManager.dispose();
	}

	public void doEndGame() {

		// TODO: Removed for now!
//		Gdx.app.log(BTNGame.TAG, "Game Over!");

		for(Container container : getContainers()){

			container.getContents().clearActions();
		}

		// Pause the game.
		this.setGamePaused(true);

		soundManager.playSound(SoundManager.SOUND_ID_GAME_OVER);

		if(score.getScore() > Integer.parseInt(saveManager.retrieveScore())){

			saveManager.saveScore(score.getScore());
			showGameOverScreen(score.getScore(), true);

			topBar.addAction(Actions.sequence(Actions.delay(1.5f), Actions.run(new Runnable() {

				@Override
				public void run() {

					soundManager.playSound(SoundManager.SOUND_ID_NEW_RECORD);
				}

			})));
		}
		else{

			showGameOverScreen(score.getScore(), false);
		}
	}

	private void showGameOverScreen(int score, boolean isHighScore) {

		if(!gameOverScreen.isVisible()){

			// Clear the glove cache so that Actions cached due to rapid clicking do not get pushed to the currentAction slot.
			getGlove().clearCache();

			topBar.addAction(Actions.moveTo(GAME_WIDTH / 2.0f, TOP_BAR_TOGETHER, 1.0f, Interpolation.bounceOut));
			bottomBar.addAction(Actions.moveTo(GAME_WIDTH / 2.0f, BOTTOM_BAR_TOGETHER, 1.0f, Interpolation.bounceOut));
			this.timer.addAction(Actions.fadeOut(1.0f));
			this.soundControl.addAction(Actions.moveBy(0.0f, 1000.0f));
			this.pauseControl.addAction(Actions.moveBy(0.0f, 1000.0f));
			
			if(isHighScore){

				gameOverScreen.setScores(score, score);
			}
			else{

				gameOverScreen.setScores(score, Integer.parseInt(saveManager.retrieveScore()));
			}

			gameOverScreen.doAnimate();
		}
	}

	/**
	 * @return the glove
	 */
	public Glove getGlove() {

		return glove;
	}

	public void printDebug() {

		print("==========================");
		print("*      DEBUG OUTPUT      *");
		print("==========================");
		print("> Glove.getCurrentAction: " + glove.getCurrentAction());
		print("> Glove.getCachedAction: " + glove.getCachedAction());
		print("> GameOverScreen.isVisible: " + gameOverScreen.isVisible());
		print("==========================");
	}

	private void print(String output){

		Gdx.app.log(BTNGame.TAG, output);
	}

	public void notifyDeactivate(Container container) {

		BTNContainedActor btnContainedActor = container.getContents();
		String className = btnContainedActor.getClass().getName();

		if(className.equals(Zombie.class.getName()) || className.equals(ZombieBunny.class.getName())){

			handleZombieDeactivate(btnContainedActor);
		}
		else if(className.equals(Dynamite.class.getName())){

			handleDynamiteDeactivate((Dynamite) btnContainedActor);
		}
		else if(className.equals(Bunny.class.getName())){

			handleBunnyDeactivate((Bunny) btnContainedActor);
		}
		else{

			// Log that something strange happened.
			print("A BTNContainedActor was deactivated but not handled.");
		}
	}

	private void handleBunnyDeactivate(Bunny btnContainedActor) {

		// Do nothing.
	}

	private void handleDynamiteDeactivate(Dynamite btnContainedActor) {

		// Do nothing.
	}

	private void handleZombieDeactivate(BTNContainedActor zombie) {

		//		doActivateUniqueContainer(zombie.getContainer());

		if(!(zombie.getActorState() == BTNContainedActor.STATE_HIT)){

			subtractLife();
			zombie.getContainer().animateHeart();
		}
	}

	public void notifyCollision(BTNContainedActor containerContents) {

		generateExplosion(containerContents.getX(), containerContents.getY() + containerContents.getHeight() / 2.0f, (containerContents instanceof Dynamite) ? true : false);

		glove.onCollide(containerContents);
		containerContents.onCollide(glove);
	}

	public void doExplosionSplash() {

		explosionSplash.setVisible(true);
		explosionSplash.getColor().a = 1.0f;
		explosionSplash.clearActions();

		AlphaAction fadein = new AlphaAction();
		fadein.setAlpha(1.0f);
		fadein.setDuration(3.0f);

		AlphaAction fadeout = Actions.fadeOut(1.0f);

		RunnableAction endGame = new RunnableAction();
		endGame.setRunnable(new Runnable() {

			@Override
			public void run() {

				explosionSplash.getColor().a = 0.0f;
			}
		});

		AlphaAction fadeBackIn = new AlphaAction();
		fadeBackIn.setAlpha(1.0f);

		for(int i = 0; i < getContainers().size; i++){

			BTNContainedActor contents = getContainers().get(i).getContents();

			if(!(contents instanceof Dynamite) && contents.getActorState() == BTNContainedActor.STATE_VISIBLE){
				
				if(contents instanceof Bunny && contents.canCollide()){
					
					contents.onCollide(null);
				}
				else{
					
					contents.onCollide(null);
				}
			}
		}

		SequenceAction sequence = new SequenceAction();
		//		sequence.addAction(fadein);
		sequence.addAction(fadeout);
		//		sequence.addAction(endGame);
		//		sequence.addAction(fadeBackIn);

		explosionSplash.addAction(sequence);
	}

	public void subtractLife() {

		score.setLives(score.getLives() - 1);
		livesModule.popHeart();
		getDifficultyManager().onHeartLoss();

		if(score.getLives() <= 0){

			doEndGame();
		}
	}

	/**
	 * @return the containers
	 */
	public Array<Container> getContainers() {

		return containers;
	}

	public void generate() {

		//		containers.get(new Random().nextInt(MAX_ZOMBIE_COUNT)).setContents(new Dynamite(0.0f, 0.0f, this));
	}

	public void incrementScore() {

		score.updateScore(score.getScore() + 1);
	}

	public void resetScore() {

		saveManager.saveScore(0);
	}

	public void addLife() {

		int currentLives = this.score.getLives(); 

		if(currentLives < Score.DEFAULT_NUMBER_LIVES){

			this.score.setLives(currentLives + 1);
			this.livesModule.pushHeart();
		}
	}

	public BTNGame getGame() {

		return game;
	}

	public AssetManager getAssetManager() {

		return assetManager;
	}

	public int getMode() {

		return mode;
	}

	public void setMode(int mode) {

		this.mode = mode;
	}

	public SoundManager getSoundManager() {

		return soundManager;
	}

	public void onButtonClickUp() {

		soundManager.playSound(SoundManager.SOUND_ID_CLICK_UP);
	}

	public void onButtonClickDown() {

		soundManager.playSound(SoundManager.SOUND_ID_CLICK_DOWN);
	}

	public DifficultyManager getDifficultyManager() {
		
		return difficultyManager;
	}
}
