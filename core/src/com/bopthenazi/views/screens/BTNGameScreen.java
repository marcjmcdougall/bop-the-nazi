package com.bopthenazi.views.screens;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AddAction;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.bopthenazi.game.BTNGame;
import com.bopthenazi.models.BTNActor;
import com.bopthenazi.models.BTNContainedActor;
import com.bopthenazi.models.BTNStage;
import com.bopthenazi.models.BasicButton;
import com.bopthenazi.models.Bunny;
import com.bopthenazi.models.Container;
import com.bopthenazi.models.Dynamite;
import com.bopthenazi.models.Explosion;
import com.bopthenazi.models.Glove;
import com.bopthenazi.models.Heart;
import com.bopthenazi.models.LivesModule;
import com.bopthenazi.models.Score;
import com.bopthenazi.models.Zombie;
import com.bopthenazi.models.ZombieBunny;
import com.bopthenazi.utils.FontFactory;
import com.bopthenazi.utils.SaveManager;
import com.sun.org.apache.bcel.internal.generic.NEWARRAY;

public class BTNGameScreen implements Screen{

	private static final boolean QUIET_MODE = false;
	
	private static final float DEFAULT_VOLUME = 1.0f;
	
	public static final int SOUND_ID_SPLAT = 0;
	public static final int SOUND_ID_PUNCH = 1;
	public static final int SOUND_ID_EXPLOSION = 2;
	public static final int SOUND_ID_GAME_OVER = 3;
	public static final int SOUND_ID_LETS_GO = 4;
	public static final int SOUND_ID_ZOMBIE_DEATH = 5;
	public static final int SOUND_ID_BUNNY_DEATH = 6;
	public static final int SOUND_ID_NEW_RECORD = 7;
	public static final int SOUND_ID_POWERUP = 8;
	public static final int SOUND_ID_ZOMBIE_BUNNY_DEATH = 9;
	
	private static final int MAX_ZOMBIE_COUNT = 5;
	private static final int MAX_CONCURRENT_ZOMBIES = 3;
	
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
	
	private float baseFrequencyContainerActivate = 0.25f;
	
	private static final int MODE_STANDARD = 0;
	public static final int MODE_APOCALYPSE = 1;
	
	private int mode = MODE_STANDARD;
	
	private AssetManager assetManager;
	
	private BTNGame game;

	private BTNStage gameStage;
	private Stage hudStage;
	
	private SaveManager saveManager;
	
	private Group gameOverScreen;
	private Label gameOverScoreLabel;
	
	private static final int LAYOUT_NORMAL = 0;
	private static final int LAYOUT_U = 1;
	private static final int CONTAINER_LAYOUT = LAYOUT_NORMAL;
	
	private static final float[] NORMAL_CONTAINER_COORDINATES = {137.625f, 540.0f, 942.375f, 341.3125f, 752.6875f};

	private Array<Container> containers;

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
	
	private boolean paused;

	private Music backgroundMusic;

	private boolean showingGameOverScreen;

	private static final String TEXTURE_PREPEND = "textures/";
	private static final String SFX_PREPEND = "sfx/";
	private static final String FONTS_PREPEND = "fonts/";
	private static final String MUSIC_PREPEND = "music/";

	private static final float NUMBER_WIDTH = 50.0f;
	
	public BTNGameScreen(BTNGame game){
		
		this.game = game;
		
		this.assetManager = new AssetManager();
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
		loadSFX("bunny-die.wav");
		loadSFX("explosion.wav");
		loadSFX("game-over.wav");
		loadSFX("lets-go.wav");
		loadSFX("new-record.wav");
		loadSFX("punch.wav");
		loadSFX("splat.wav");
		loadSFX("zombie-die-2.wav");
		loadSFX("powerup.wav");
		loadSFX("zombie-bunny-die.wav");
		
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
	
	private void loadSFX(String fileNamePostPrepend){
		
		this.assetManager.load(SFX_PREPEND + fileNamePostPrepend, Sound.class);
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
		
		for(int i = 0; i < MAX_ZOMBIE_COUNT; i++){
			
			initializeContainer(i);
		}
		
		for(int i = 0; i < MAX_ZOMBIE_COUNT; i++){
			
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

	private void reset(){
		
//		this.game.setScreen(new BTNGameScreen(this.game));
		
		// Reset the game to the startState.
//		this.gameOverScreen.setVisible(false);
//		this.setPaused(false);
//		this.score.updateScore(0);
//		this.score.setLives(Score.DEFAULT_NUMBER_LIVES);
//		this.livesModule.reset();
		
		// Remove all the actors.
		gameStage.getActors().removeRange(0, gameStage.getActors().size - 1);
		
		// Simply call "show" again to restart the whole game.
		this.show();
		
//		for(int i = 0; i < MAX_ZOMBIE_COUNT; i++){
//			
////			containers.get(i).getContents().remove();
//			initializeContainer(i);
//		}
	}
	
	@Override
	public void show() {
		
		this.setPaused(true);
		this.containers = new Array<Container>(MAX_ZOMBIE_COUNT);
		this.score = new Score(GAME_WIDTH / 2.0f - 220.0f, (GAME_HEIGHT - Score.SCORE_HEIGHT) - AD_TOP_OFFSET);
		
		this.score.getColor().a = 0.0f;
		
		this.setMode(MODE_STANDARD);
		
		timeElapsedSinceLastZombie = 0f;
		
		FitViewport viewport = new FitViewport(GAME_WIDTH, GAME_HEIGHT);
		
		gameStage = new BTNStage(viewport, game, this);
		hudStage = new Stage(viewport);
		
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
		
		int randomIndex = r.nextInt(MAX_ZOMBIE_COUNT);
		
		this.showingGameOverScreen = false;
		
		activateContainerContents(containers.get(randomIndex));
		
		if(saveManager.isFirstShot()){
			
			showTutorialScreen();
		}
		else{
			
			begin();
		}
	}

	private void begin(){
		
		score.addAction(Actions.fadeIn(3.0f));
		topBar.addAction(Actions.moveTo(topBar.getX(), TOP_BAR_TOP, 3.0f, Interpolation.pow4));
		bottomBar.addAction(Actions.moveTo(bottomBar.getX(), BOTTOM_BAR_BOTTOM, 3.0f, Interpolation.pow4));
		
		three.addAction(Actions.sequence(Actions.alpha(1.0f), Actions.fadeOut(1.0f)));
		three.addAction(Actions.sequence(Actions.scaleBy(4.0f, 4.0f, 1.0f), Actions.run(new Runnable() {
			
			@Override
			public void run() {
				
				two.addAction(Actions.sequence(Actions.alpha(1.0f), Actions.fadeOut(1.0f)));
				two.addAction(Actions.sequence(Actions.scaleBy(4.0f, 4.0f, 1.0f), Actions.run(new Runnable() {
					
					@Override
					public void run() {
						
						one.addAction(Actions.sequence(Actions.alpha(1.0f), Actions.fadeOut(1.0f)));
						one.addAction(Actions.sequence(Actions.scaleBy(4.0f, 4.0f, 1.0f), Actions.run(new Runnable() {
							
							@Override
							public void run() {
								
								playSound(SOUND_ID_LETS_GO);
								bop.addAction(Actions.sequence(Actions.alpha(1.0f), Actions.fadeOut(1.0f)));
								bop.addAction(Actions.sequence(Actions.scaleBy(4.0f, 4.0f, 1.0f), Actions.run(new Runnable() {
									
									@Override
									public void run() {
										
										BTNGameScreen.this.setPaused(false);
									}
									
								})));
							}
							
						})));
					}
					
				})));
			}
			
		})));
	}
	
	private void showTutorialScreen() {
		
		this.setPaused(true);
		
		final Group tutorialScreen = new Group();
		
		final BTNActor alpha = new BTNActor(getTexture("screen-game-over/alpha-25.png").getTexture(), BTNGameScreen.GAME_WIDTH / 2.0f, BTNGameScreen.GAME_HEIGHT / 2.0f);
		final BTNActor bg = new BTNActor(getTexture("screen-tutorial/instructions-screen.png").getTexture(), BTNGameScreen.GAME_WIDTH / 2.0f, GAME_HEIGHT / 2.0f - 200.0f, BTNGameScreen.GAME_WIDTH * 0.75f, BTNGameScreen.GAME_HEIGHT * 0.55f);
		
		final BasicButton ok = new BasicButton(getTexture("screen-tutorial/ok-button-up-state.png").getTexture(), getTexture("screen-tutorial/ok-button-down-state.png").getTexture(), GAME_WIDTH * 0.72f, GAME_HEIGHT * 0.2f, 200.0f, 150.0f);
		
		ok.addListener(new InputListener(){
			
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				
				return true;
			}
			
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				
				super.touchUp(event, x, y, pointer, button);
				
				bg.addAction(Actions.moveBy(0.0f, 2000.0f, 1.0f, Interpolation.pow4));
				ok.addAction(Actions.moveBy(0.0f, 2000.0f, 1.0f, Interpolation.pow4));
				
				begin();
				
				tutorialScreen.addAction(Actions.sequence(Actions.alpha(0.0f, 1.0f), Actions.run(new Runnable() {
					
					@Override
					public void run() {
						
						tutorialScreen.setVisible(false);
					}
				})));
			}
		});
		
		bg.addAction(Actions.moveBy(0.0f, 2000.0f));
		bg.addAction(Actions.moveBy(0.0f, -2000.0f, 1.0f, Interpolation.pow4));
		
		ok.addAction(Actions.moveBy(0.0f, 2000.0f));
		ok.addAction(Actions.moveBy(0.0f, -2000.0f, 1.0f, Interpolation.pow4));
		
		tutorialScreen.addActor(alpha);
		tutorialScreen.addActor(bg);
		tutorialScreen.addActor(ok);
		
		tutorialScreen.getColor().a = 0.0f;
		
		AlphaAction fadeIn = Actions.alpha(1.0f, 1.0f);
		
		tutorialScreen.addAction(fadeIn);
		
		hudStage.addActor(tutorialScreen);
	}

	public TextureRegion getTexture(String textureNamePostPrepend) {
		
		return new TextureRegion(assetManager.get(TEXTURE_PREPEND + textureNamePostPrepend, Texture.class));
	}
	
	public Sound getSound(String soundNamePostPrepend){
		
		return assetManager.get(SFX_PREPEND + soundNamePostPrepend, Sound.class);
	}

	public Music getMusic(String musicNamePostPrepend){
		
		return assetManager.get(MUSIC_PREPEND + musicNamePostPrepend, Music.class);
	}
	
	private void setPaused(boolean paused) {
		
		this.paused = paused;
	}

	@Override
	public void render(float delta) {
	
		Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        if(!this.isPaused()){
        	
	        timeElapsedSinceLastZombie += delta;
	        
	        if(timeElapsedSinceLastZombie >= baseFrequencyContainerActivate){
	        	
	        	// Activate a random Nazi that has *not yet been activated*
	        	doActivateUniqueContainer();
	        	
	        	timeElapsedSinceLastZombie = 0.0f;
	        }
	        
	        gameStage.act(delta);
        }
	        
        hudStage.act(delta);
        
        gameStage.draw();
        hudStage.draw();
	}

	public boolean isPaused() {
		
		return paused;
	}

	private void doActivateUniqueContainer(){
		
		// If no Nazis are already activated, then choose one at random.
		int numContainersActivated = 0;
		
		print("Activating new container now.");
		
		for(Container container : containers){
		
			if(container.getContents().isActivated()){
			
				numContainersActivated++;
			}
		}
		
		if(numContainersActivated < MAX_CONCURRENT_ZOMBIES){
			
			for(int i = 0; i < MAX_CONCURRENT_ZOMBIES; i++){
				
				// Select a random number.
				int index = new Random().nextInt(MAX_ZOMBIE_COUNT);
				
				if(containers.get(index).getContents() != null){
					
					if(!containers.get(index).getContents().isActivated()){
						
						containers.get(index).getContents().remove();
						
						BTNContainedActor newActor = null;
						
						if(getMode() == MODE_STANDARD){
							
							int cursor = new Random().nextInt(10);
							
							
							// 60% chance...
							if(cursor < 6){
								
								cursor = new Random().nextInt(2);
								
								// 30% chance...
								if(cursor == 0){
									
									newActor = new Zombie(0.0f, 0.0f, this);
								}
								// 30% chance...
								else{
									
									newActor = new ZombieBunny(0.0f, 0.0f, this);						
								}
							}
							// 30% chance...
							else if(cursor < 9){
								
								newActor = new Bunny(0.0f, 0.0f, this);
							}
							// 10% chance...
							else{
								
								cursor = new Random().nextInt(2);
								
								// 5% chance...
								if(cursor == 0 && this.score.getLives() < Score.DEFAULT_NUMBER_LIVES){
									
									newActor = new Heart(0.0f, 0.0f, this);
								}
								// 5% chance...
								else{
									
									newActor  = new Dynamite(0.0f, 0.0f, this);						
								}
							}
							
						}
						else if(getMode() == MODE_APOCALYPSE){
							
							newActor = new Random().nextInt(2) == 1 ? new Zombie(0.0f, 0.0f, this) : new ZombieBunny(0.0f, 0.0f, this);
						}
	
						containers.get(index).setContents(newActor);
						
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

	public void playSound(int soundID){
		
		if(!QUIET_MODE){
			
			switch(soundID){
			
				case SOUND_ID_PUNCH :{
					
//					punchSound.play(DEFAULT_VOLUME * 0.75f);
					getSound("punch.wav").play(DEFAULT_VOLUME * 0.75f);
					
					break;
				}
				case SOUND_ID_SPLAT :{
					
//					splatSound.play(DEFAULT_VOLUME * 0.25f);
					getSound("splat.wav").play(DEFAULT_VOLUME * 0.25f);
					
					break;
				}
				case SOUND_ID_EXPLOSION :{
					
//					explosionSound.play(DEFAULT_VOLUME * 0.15f);
					getSound("explosion.wav").play(DEFAULT_VOLUME * 0.15f);
					
					break;
				}
				case SOUND_ID_GAME_OVER :{
					
//					gameOverSound.play(DEFAULT_VOLUME);
					getSound("game-over.wav").play(DEFAULT_VOLUME);
					
					break;
				}
				case SOUND_ID_LETS_GO : {
					
//					letsGoSound.play(DEFAULT_VOLUME);
					getSound("lets-go.wav").play(DEFAULT_VOLUME);
					
					break;
				}
				case SOUND_ID_BUNNY_DEATH : {
					
//					bunnyDeathSound.play(DEFAULT_VOLUME * 0.75f);
					getSound("bunny-die.wav").play(DEFAULT_VOLUME * 0.75f);
					
					break;
				}
				case SOUND_ID_ZOMBIE_DEATH : {
					
//					zombieDeathSound.play(DEFAULT_VOLUME * 0.75f);
					getSound("zombie-die-2.wav").play(DEFAULT_VOLUME * 0.75f);
					
					break;
				}
				case SOUND_ID_NEW_RECORD : {
					
//					newRecordSound.play(DEFAULT_VOLUME * 0.75f);
					getSound("new-record.wav").play(DEFAULT_VOLUME * 0.75f);
					
					break;
				}
				case SOUND_ID_POWERUP :{
					
					getSound("powerup.wav").play();
					
					break;
				}
				case SOUND_ID_ZOMBIE_BUNNY_DEATH :{
					
//					zombieBunnyDieSound.play(DEFAULT_VOLUME * 0.75f);
					getSound("zombie-bunny-die.wav").play(DEFAULT_VOLUME * 0.75f);
					
					break;
				}
				default :{
					
					// Do nothing.
					break;
				}
			}
		}
	}
	
	public void doEndGame() {
		
		Gdx.app.log(BTNGame.TAG, "Game Over!");
		
		for(Container container : getContainers()){
			
			container.getContents().clearActions();
		}
		
		// Pause the game.
		this.setPaused(true);
		
//		game.setScreen(new BTNGameOverScreen(game, score.getScore()));
		
		if(score.getScore() > Integer.parseInt(saveManager.retrieveScore())){
			
			saveManager.saveScore(score.getScore());
			showGameOverScreen(score.getScore(), true);
		}
		else{
			
			showGameOverScreen(score.getScore(), false);
		}
	}

	private void showGameOverScreen(int score, boolean isHighScore) {
		
		if(!showingGameOverScreen){
			
			final MoveByAction moveOut = Actions.moveBy(0.0f, 2000.0f, 1.0f, Interpolation.pow4);
			MoveByAction moveOutInstant = Actions.moveBy(0.0f, 2000.0f);
			
			MoveByAction moveIn = Actions.moveBy(0.0f, -2000.0f, 1.0f, Interpolation.pow4);
			
			final AlphaAction fadeOut = Actions.fadeOut(1.0f);
			AlphaAction fadeIn = Actions.fadeIn(1.0f);
			RunnableAction playGameOver = Actions.run(new Runnable() {
				
				@Override
				public void run() {
					
					playSound(SOUND_ID_GAME_OVER);
				}
			});
			
			RunnableAction playNewRecord = Actions.run(new Runnable() {
				
				@Override
				public void run() {
					
					playSound(SOUND_ID_NEW_RECORD);
				}
			});
			
		
			SequenceAction sequence = null;
			
			if(isHighScore){
				
				sequence = Actions.sequence(playGameOver, Actions.delay(1.2f), playNewRecord);
			}
			else{
				
				sequence = Actions.sequence(playGameOver);
			}
			
			// Clear the glove cache so that Actions cached due to rapid clicking do not get pushed to the currentAction slot.
			this.glove.clearCache();
			this.gameOverScreen = new Group();
			
			final BTNActor gameOverAlpha = new BTNActor(new Texture("textures/screen-game-over/alpha-25.png"), GAME_WIDTH / 2.0f, GAME_HEIGHT / 2.0f, GAME_WIDTH, GAME_HEIGHT);
			BTNActor gameOverBackground = new BTNActor(new Texture("textures/screen-game-over/game-over-box.png"), GAME_WIDTH / 2.0f, GAME_HEIGHT / 2.0f - 200.0f, GAME_WIDTH * 0.75f, GAME_HEIGHT * 0.6f);
			BTNActor gameOverText = new BTNActor(new Texture("textures/screen-game-over/game-over-text.png"), GAME_WIDTH / 2.0f, GAME_HEIGHT / 2.0f + 25.0f, GAME_WIDTH * 0.40f, GAME_HEIGHT * 0.15f);
			
			gameOverScoreLabel = new Label("Score: " + score, new LabelStyle(FontFactory.buildFont(80), new Color(0.0f, 0.0f, 0.0f, 1.0f)));
			gameOverScoreLabel.setHeight(100.0f);
			gameOverScoreLabel.setX(BTNGameScreen.GAME_WIDTH / 2.0f - (gameOverScoreLabel.getWidth() / 2.0f));
			gameOverScoreLabel.setY((670.0f + (this.score.getHeight() / 2.0f)));
			
			int highScore = Integer.parseInt(saveManager.retrieveScore());
			
			Label highScoreLabel = new Label("High Score: " + highScore, new LabelStyle(FontFactory.buildFont(80), new Color(0.0f, 0.0f, 0.0f, 1.0f)));
			highScoreLabel.setHeight(100.0f);
			highScoreLabel.setX(BTNGameScreen.GAME_WIDTH / 2.0f - (highScoreLabel.getWidth() / 2.0f));
			highScoreLabel.setY((570.0f + (this.score.getHeight() / 2.0f))/* - AD_TOP_OFFSET*/);
			
			BasicButton restart = new BasicButton(new Texture("textures/screen-game-over/restart-button.png"), new Texture("textures/screen-game-over/restart-button-down-state.png"), GAME_WIDTH / 2.0f, GAME_HEIGHT / 2.0f - 550.0f);
		
			restart.setHeight(GAME_HEIGHT * 0.1f); 
			restart.setWidth(GAME_WIDTH * 0.45f);
			restart.setX((GAME_WIDTH / 2.0f) - (restart.getWidth() / 2.0f));
			restart.setY(restart.getY() - restart.getHeight() / 2.0f);
			
			restart.addListener(new com.badlogic.gdx.scenes.scene2d.InputListener(){
				
				@Override
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
					
					return true;
				}
				
				@Override
				public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
					
					super.touchUp(event, x, y, pointer, button);
					
					gameOverAlpha.addAction(Actions.fadeOut(1.0f));
					gameOverScreen.addAction(fadeOut);
					
					BTNGameScreen.this.score.addAction(Actions.fadeOut(1.0f));
					topBar.addAction(Actions.moveTo(GAME_WIDTH / 2.0f, TOP_BAR_TOGETHER, 1.0f, Interpolation.bounceOut));
					bottomBar.addAction(Actions.sequence(Actions.moveTo(GAME_WIDTH / 2.0f, BOTTOM_BAR_TOGETHER, 1.0f, Interpolation.bounceOut), Actions.delay(1.0f), Actions.run(new Runnable() {
						
						@Override
						public void run() {
							
							BTNGameScreen.this.reset();
						}
					})));
				}
			});
			
			gameOverScreen.getColor().a = 0.0f;
			gameOverAlpha.getColor().a = 0.0f;
			
			gameOverScreen.setVisible(true);
			
			this.gameOverScreen.addActor(gameOverBackground);
			this.gameOverScreen.addActor(gameOverText);
			this.gameOverScreen.addActor(gameOverScoreLabel);
			this.gameOverScreen.addActor(highScoreLabel);
			this.gameOverScreen.addActor(restart);
			
			gameOverScreen.addAction(moveOutInstant);
			gameOverScreen.addAction(moveIn);
			gameOverScreen.addAction(fadeIn);
			gameOverAlpha.addAction(Actions.fadeIn(1.0f));
			gameOverScreen.addAction(sequence);
			
			hudStage.addActor(gameOverAlpha);
			hudStage.addActor(gameOverScreen);
			
			showingGameOverScreen = true;
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
		
		print("==========================");
	}
	
	private void print(String output){
		
		Gdx.app.log(BTNGame.TAG, output);
	}

	public void notifyDeactivate(BTNContainedActor btnContainedActor) {
		
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
		
		print("Handling Bunny deactivate now");
		
		// Do nothing.
	}

	private void handleDynamiteDeactivate(Dynamite btnContainedActor) {
		
		print("Handling Dynamite deactivate now");
		
		// Do nothing.
	}

	private void handleZombieDeactivate(BTNContainedActor zombie) {
		
		print("Handling Zombie deactivate now");
		
		doActivateUniqueContainer();
		
		if(!(zombie.getActorState() == BTNContainedActor.STATE_HIT)){
			
			subtractLife();
		}
	}

	public void notifyCollision(BTNContainedActor containerContents) {
		
		generateExplosion(containerContents.getX(), containerContents.getY() + containerContents.getHeight() / 2.0f, (containerContents instanceof Dynamite) ? true : false);
	
		glove.onCollide(containerContents);
		containerContents.onCollide(glove);
	}

	public void doExplosionSplash() {
		
		explosionSplash.setVisible(true);
		
		AlphaAction fadein = new AlphaAction();
		fadein.setAlpha(1.0f);
		fadein.setDuration(3.0f);
		
		AlphaAction fadeout = new AlphaAction();
		fadein.setAlpha(0.0f);
		fadein.setDuration(2.0f);
		
		RunnableAction endGame = new RunnableAction();
		endGame.setRunnable(new Runnable() {
			
			@Override
			public void run() {
				
				explosionSplash.setVisible(false);
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
		sequence.addAction(fadein);
		sequence.addAction(fadeout);
		sequence.addAction(endGame);
		sequence.addAction(fadeBackIn);
		
		explosionSplash.addAction(sequence);
	}

	public void subtractLife() {
		
		score.setLives(score.getLives() - 1);
		livesModule.popHeart();
		
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
		
		containers.get(new Random().nextInt(MAX_ZOMBIE_COUNT)).setContents(new Dynamite(0.0f, 0.0f, this));
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
}
