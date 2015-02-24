package com.bopthenazi.views.screens;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.bopthenazi.game.BTNGame;
import com.bopthenazi.models.BTNActor;
import com.bopthenazi.models.BTNContainedActor;
import com.bopthenazi.models.BTNStage;
import com.bopthenazi.models.Bunny;
import com.bopthenazi.models.Container;
import com.bopthenazi.models.Dynamite;
import com.bopthenazi.models.Explosion;
import com.bopthenazi.models.GameOverModule;
import com.bopthenazi.models.Glove;
import com.bopthenazi.models.Heart;
import com.bopthenazi.models.LivesModule;
import com.bopthenazi.models.Score;
import com.bopthenazi.models.TutorialScreenModule;
import com.bopthenazi.models.Zombie;
import com.bopthenazi.models.ZombieBunny;
import com.bopthenazi.utils.DifficultyManager;
import com.bopthenazi.utils.SaveManager;
import com.bopthenazi.utils.SoundManager;

public class BTNGameScreen implements Screen{

	private static final float NUMBER_SCALE = 6.0f;
	
	public static final float GAME_WIDTH = 1080.0f;
	public static final float GAME_HEIGHT = 1920.0f;
	
	public static final float AD_TOP_OFFSET = 150.0f;
	
	public static final float BAR_HEIGHT = 3250.0f;
	public static final float ZOMBIE_OFFSET_HORIZONTAL_MARGIN = 25.0f;
	public static final float BAR_OFFSET_LOWER = 136.3f;
	public static final float BAR_OFFSET_TOP = 256.0f;
	
	public static final float TOP_BAR_TOP = GAME_HEIGHT * 1.62f;
	public static final float BOTTOM_BAR_BOTTOM = GAME_HEIGHT * -1.0f;
	
	public static final float TOP_BAR_TOGETHER = GAME_HEIGHT * 1.2925f;
	public static final float BOTTOM_BAR_TOGETHER = GAME_HEIGHT * -0.40f;
	
	private static final int MODE_STANDARD = 0;
	public static final int MODE_APOCALYPSE = 1;
	
	private int mode = MODE_STANDARD;
	
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
	
	private static final float[] NORMAL_CONTAINER_COORDINATES = {137.625f, 540.0f, 942.375f, 341.3125f, 752.6875f};

	private Array<Container> containers;

	private GameOverModule gameOverScreen;
	private TutorialScreenModule tutorialScreen;
	private Glove glove;
	private BTNActor bg;
	private BTNActor explosionSplash;
	private Score score;
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

	private Music backgroundMusic;

	private boolean showingGameOverScreen;

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
		
		// Load textures...
		loadTexture("screen-game/bunny/bunny-hit-frame.png");
		loadTexture("screen-game/bunny/bunny.png");
		
		loadTexture("screen-game/bunny-2/bunny-2.png");
		loadTexture("screen-game/bunny-2/bunny2-hit-frame.png");
		
		loadTexture("screen-game/container/tunnel-back.png");
		loadTexture("screen-game/container/tunnel-front.png");
		loadTexture("screen-game/container/heart-minus.png");
		
		loadTexture("screen-game/dynamite/dynamite-01.png");
		loadTexture("screen-game/dynamite/dynamite-02.png");
		loadTexture("screen-game/dynamite/dynamite-03.png");
		loadTexture("screen-game/dynamite/dynamite-04.png");
		loadTexture("screen-game/dynamite/dynamite-05.png");
		
		loadTexture("screen-game/explosion/explosion-01.png");
		loadTexture("screen-game/explosion/explosion-02.png");
		loadTexture("screen-game/explosion/explosion-03.png");
		loadTexture("screen-game/explosion/explosion-04.png");
		
		loadTexture("screen-game/heart/heart-empty-v2.png");
		loadTexture("screen-game/heart/heart.png");
		
		loadTexture("screen-game/heart-contained/heart-contained.png");
		loadTexture("screen-game/heart-contained/heart-contained-01.png");
		loadTexture("screen-game/heart-contained/heart-contained-02.png");
		loadTexture("screen-game/heart-contained/heart-contained-03.png");
		loadTexture("screen-game/heart-contained/heart-contained-04.png");
		loadTexture("screen-game/heart-contained/heart-contained-05.png");
		
		loadTexture("screen-game/zombie/zombie.png");
		loadTexture("screen-game/zombie/zombie1-hit-frame.png");
		
		loadTexture("screen-game/zombie-2/zombie-2.png");
		loadTexture("screen-game/zombie-2/zombie2-hit-frame.png");

		loadTexture("screen-game/zombie-bunny/zombie-bunny.png");
		loadTexture("screen-game/zombie-bunny/zombie-bunny-hit-frame.png");
		
		loadTexture("screen-game/background.png");
		loadTexture("screen-game/explosion-splash.png");
		loadTexture("screen-game/glove.png");
		loadTexture("screen-game/mover.png");
		loadTexture("screen-game/top-bar.png");
		loadTexture("screen-game/bottom-bar.png");
		loadTexture("screen-game/ad-placeholder.png");
		
		loadTexture("screen-game-over/alpha-25.png");
		loadTexture("screen-game-over/game-over-box.png");
		loadTexture("screen-game-over/game-over-text.png");
		loadTexture("screen-game-over/restart-button.png");
		loadTexture("screen-game-over/restart-button-down-state.png");
		
		loadTexture("screen-tutorial/instructions-screen.png");
		loadTexture("screen-tutorial/ok-button-down-state.png");
		loadTexture("screen-tutorial/ok-button-up-state.png");
		
		loadTexture("screen-game/numbers/1.png");
		loadTexture("screen-game/numbers/2.png");
		loadTexture("screen-game/numbers/3.png");
		loadTexture("screen-game/numbers/bop.png");
		
		// Load sounds...
		soundManager.beginLoadSFX();
		
		// Load music...
		loadMusic("candyland.mp3");
		
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
			
			containers.add(new Container(NORMAL_CONTAINER_COORDINATES[i], (BAR_OFFSET_LOWER + ZOMBIE_OFFSET_HORIZONTAL_MARGIN) - AD_TOP_OFFSET, this));
		}
		else if(i >= 3){
			
			containers.add(new Container(NORMAL_CONTAINER_COORDINATES[i], ((BAR_OFFSET_LOWER + ZOMBIE_OFFSET_HORIZONTAL_MARGIN) * 2) - AD_TOP_OFFSET, this));
		}
	}
	
	private void initializeLivesModule() {
		
		this.livesModule = new LivesModule(this);
		
		for(BTNActor heartOutline : livesModule.getHeartOutlines()){
			
			gameStage.addActor(heartOutline);
		}
		
		for(BTNActor heart : livesModule.getHearts()){
			
			gameStage.addActor(heart);
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
		this.score.updateScore(0);
		this.score.setLives(Score.DEFAULT_NUMBER_LIVES);
		this.livesModule.reset();
		this.glove.setX(GAME_WIDTH / 2.0f);
		this.difficultyManager.reset();
		
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
		
		initialize();
		
		tempTrue = true;
		
		if(/*saveManager.isFirstShot()*/ tempTrue){
			
			tempTrue = false;
			showTutorialScreen();
		}
		else{
			
			begin(0.5f);
		}
	}

	private void initialize() {
		
		tutorialScreen = new TutorialScreenModule(this);
		difficultyManager = new DifficultyManager();
		
		this.setGamePaused(true);
		
		this.containers = new Array<Container>(DifficultyManager.MAX_CONTAINERS);
		this.score = new Score(GAME_WIDTH / 2.0f, (GAME_HEIGHT - Score.SCORE_HEIGHT) - AD_TOP_OFFSET);
		
		this.score.getColor().a = 0.0f;
		
		this.setMode(MODE_STANDARD);
		
		timeElapsedSinceLastZombie = 0f;
		
		FitViewport viewport = new FitViewport(GAME_WIDTH, GAME_HEIGHT);
		
		gameStage = new BTNStage(viewport, game, this);
		hudStage = new Stage(viewport);
		
		gameOverScreen = new GameOverModule(this);
		bg = new BTNActor(getTexture("screen-game/background.png").getTexture(), GAME_WIDTH / 2.0f, (GAME_HEIGHT / 2.0f) - AD_TOP_OFFSET, GAME_WIDTH, GAME_HEIGHT);
		gloveCase = new BTNActor(getTexture("screen-game/mover.png").getTexture(), GAME_WIDTH / 2.0f, (GAME_HEIGHT - 350.0f) - AD_TOP_OFFSET, 162.0f, 138.6f);
		glove = new Glove(GAME_WIDTH / 2.0f, Glove.GLOVE_UNLOCK_BARRIER, Glove.GLOVE_WIDTH, Glove.GLOVE_HEIGHT, this, gloveCase);
		topBar = new BTNActor(getTexture("screen-game/top-bar.png").getTexture(), GAME_WIDTH / 2.0f, /*(GAME_HEIGHT - TOP_BAR_HEIGHT / 2.0f) - AD_TOP_OFFSET*/TOP_BAR_TOGETHER, GAME_WIDTH, BAR_HEIGHT);
		bottomBar = new BTNActor(getTexture("screen-game/bottom-bar.png").getTexture(), GAME_WIDTH / 2.0f, BOTTOM_BAR_TOGETHER, GAME_WIDTH, BAR_HEIGHT);
		one = new BTNActor(getTexture("screen-game/numbers/1.png").getTexture(), GAME_WIDTH / 2.0f - NUMBER_WIDTH / 2.0f, GAME_HEIGHT / 2.0f - NUMBER_WIDTH / 2.0f);
		two = new BTNActor(getTexture("screen-game/numbers/2.png").getTexture(), GAME_WIDTH / 2.0f - NUMBER_WIDTH / 2.0f, GAME_HEIGHT / 2.0f - NUMBER_WIDTH / 2.0f);
		three = new BTNActor(getTexture("screen-game/numbers/3.png").getTexture(), GAME_WIDTH / 2.0f - NUMBER_WIDTH / 2.0f, GAME_HEIGHT / 2.0f - NUMBER_WIDTH / 2.0f);
		bop = new BTNActor(getTexture("screen-game/numbers/bop.png").getTexture(), GAME_WIDTH / 2.0f - NUMBER_WIDTH / 2.0f, GAME_HEIGHT / 2.0f - NUMBER_WIDTH / 2.0f);
		
		one.setOrigin(one.getWidth() / 2.0f, one.getHeight() / 2.0f);
		two.setOrigin(two.getWidth() / 2.0f, two.getHeight() / 2.0f);
		three.setOrigin(three.getWidth() / 2.0f, three.getHeight() / 2.0f);
		bop.setOrigin(bop.getWidth() / 2.0f, bop.getHeight() / 2.0f);
		
		one.getColor().a = 0.0f;
		two.getColor().a = 0.0f;
		three.getColor().a = 0.0f;
		bop.getColor().a = 0.0f;
		
		saveManager = new SaveManager();
		
		explosionSplash = new BTNActor(getTexture("screen-game/explosion-splash.png").getTexture(), GAME_WIDTH / 2.0f, GAME_HEIGHT / 2.0f, GAME_WIDTH, GAME_HEIGHT);
		
		gameStage.addActor(bg);
		
		this.backgroundMusic = getMusic("candyland.mp3");
		
		this.backgroundMusic.setVolume(0.25f);
		this.backgroundMusic.setLooping(true);
//		this.backgroundMusic.play();
		
		initializeContainers();
		initializeLivesModule();
		
		explosionSplash.setVisible(false);

		gameStage.addActor(glove);
		gameStage.addActor(gloveCase);
		gameStage.addActor(explosionSplash);
		
		hudStage.addActor(topBar);
		hudStage.addActor(bottomBar);
		hudStage.addActor(this.score);
		
		hudStage.addActor(three);
		hudStage.addActor(two);
		hudStage.addActor(one);
		hudStage.addActor(bop);
		
		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(hudStage);
		multiplexer.addProcessor(gameStage);
		
		Gdx.input.setInputProcessor(multiplexer);
		
		Random r = new Random();
		
		int randomIndex = r.nextInt(DifficultyManager.MAX_CONTAINERS);
		
		this.showingGameOverScreen = false;
		
		hudStage.addActor(gameOverScreen.getGameOverAlpha());
		hudStage.addActor(gameOverScreen);
		
		hudStage.addActor(tutorialScreen);
		
		activateContainerContents(containers.get(randomIndex));
	}

	public void begin(float initialDelay){
		
		// We delay everything by one second to allow the slow Android OS to catch up.
		score.addAction(Actions.sequence(Actions.delay(initialDelay), Actions.run(new Runnable() {
			
			@Override
			public void run() {
				
				score.addAction(Actions.fadeIn(1.0f));
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

	public TextureRegion getTexture(String textureNamePostPrepend) {
		
		return new TextureRegion(assetManager.get(TEXTURE_PREPEND + textureNamePostPrepend, Texture.class));
	}
	

	public Music getMusic(String musicNamePostPrepend){
		
		return assetManager.get(MUSIC_PREPEND + musicNamePostPrepend, Music.class);
	}
	
	private void setGamePaused(boolean paused) {
		
		this.gamePaused = paused;
	}

	@Override
	public void render(float delta) {
	
		Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        if(!this.isPaused()){
        	
	        timeElapsedSinceLastZombie += delta;
	        
	        if(timeElapsedSinceLastZombie >= difficultyManager.getNewContainerSpawnRate()){
	        	
	        	// Activate a random Nazi that has *not yet been activated*
	        	doActivateUniqueContainer(null);
	        	
	        	timeElapsedSinceLastZombie = 0.0f;
	        }
	        
	        difficultyManager.updateDifficulty(delta);
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
		
		if(numContainersActivated < difficultyManager.getMaxConcurrentContainers()){
			
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
		
		// TODO Auto-generated method stub
	}

	@Override
	public void pause() {
		
		// TODO Auto-generated method stub
	}

	@Override
	public void resume() {
		
		// TODO Auto-generated method stub
	}

	@Override
	public void hide() {

		// TODO Auto-generated method stub
		dispose();
	}

	@Override
	public void dispose() {
		
		// TODO Auto-generated method stub
		gameStage.dispose();
		
		assetManager.dispose();
	}

	public void doEndGame() {
	
		// TODO: Removed for now!
		Gdx.app.log(BTNGame.TAG, "Game Over!");
		
		for(Container container : getContainers()){
			
			container.getContents().clearActions();
		}
		
		// Pause the game.
		this.setGamePaused(true);
		
		soundManager.playSound(SoundManager.SOUND_ID_GAME_OVER);
		
		if(score.getScore() > Integer.parseInt(saveManager.retrieveScore())){
			
			saveManager.saveScore(score.getScore());
			showGameOverScreen(score.getScore(), true);
			
			topBar.addAction(Actions.sequence(Actions.delay(1.25f), Actions.run(new Runnable() {
				
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
			this.score.addAction(Actions.fadeOut(1.0f));
			
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
			
			if(!(contents instanceof Dynamite) && contents.canCollide()){
				
				contents.onCollide(null);
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
		difficultyManager.onHeartLoss();
		
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
}
