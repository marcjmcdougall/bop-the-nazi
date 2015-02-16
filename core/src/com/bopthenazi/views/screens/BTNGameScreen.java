package com.bopthenazi.views.screens;

import java.util.Random;

import javax.swing.GroupLayout.Alignment;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
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
import com.bopthenazi.models.LivesModule;
import com.bopthenazi.models.Score;
import com.bopthenazi.models.Zombie;
import com.bopthenazi.models.ZombieBunny;
import com.bopthenazi.utils.FontFactory;

public class BTNGameScreen implements Screen{

	private static final boolean QUIET_MODE = false;
	
	private static final float DEFAULT_VOLUME = 1.0f;
	
	private static final int SOUND_ID_SPLAT = 0;
	private static final int SOUND_ID_PUNCH = 1;
	private static final int SOUND_ID_EXPLOSION = 2;
	
	private static final int MAX_ZOMBIE_COUNT = 5;
	private static final int MAX_CONCURRENT_ZOMBIES = 2;
	
	private static final float BASE_FREQUENCY_NAZI_REVEAL = 1.0f;
	
	public static final float GAME_WIDTH = 1080.0f;
	public static final float GAME_HEIGHT = 1920.0f;
	
	public static final float TOP_BAR_HEIGHT = 284.0f;
	public static final float ZOMBIE_OFFSET_HORIZONTAL_MARGIN = 25.0f;
	public static final float BAR_OFFSET_LOWER = 136.3f;
	public static final float BAR_OFFSET_TOP = 256.0f;
	
	private BTNGame game;
	private BTNStage gameStage;
	
	private Group gameOverScreen;
	
	private Sound punchSound;
	private Sound splatSound;
	private Sound explosionSound;
	
	private static final float[] CONTAINER_COORDINATES = {212.625f, 540.0f, 867.375f, 376.3125f, 703.6875f};

	private Array<Container> containers;

	private Glove glove;
	private BTNActor bg;
	private BTNActor explosionSplash;
	private Score score;
	private BTNActor topBar;
	private BTNActor gloveCase;
	private LivesModule livesModule;
	
	private float timeElapsedSinceLastZombie;
	
	private boolean paused;
	
	public BTNGameScreen(BTNGame game){
		
		this.game = game;
	}
	
	public void notifyNewX(float x) {
		
		glove.notifyTouch(x);
	}
	
	private void generateExplosion(float x, float y, boolean expand) {
		
		Explosion explosion = new Explosion(x, y, expand);
		gameStage.addActor(explosion);
	}

	private void activateContainerContents(Container container){
		
		BTNContainedActor contents = container.getContents(); 
		
		if(contents != null){
			
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
			
			containers.add(new Container(CONTAINER_COORDINATES[i], BAR_OFFSET_LOWER + ZOMBIE_OFFSET_HORIZONTAL_MARGIN, this));
		}
		else if(i >= 3){
			
			containers.add(new Container(CONTAINER_COORDINATES[i], (BAR_OFFSET_LOWER + ZOMBIE_OFFSET_HORIZONTAL_MARGIN) * 2, this));
		}
	}
	
	private void initializeLivesModule() {
		
		this.livesModule = new LivesModule();
		
		for(BTNActor heartOutline : livesModule.getHeartOutlines()){
			
			gameStage.addActor(heartOutline);
		}
		
		for(BTNActor heart : livesModule.getHearts()){
			
			gameStage.addActor(heart);
		}
	}

	private void reset(){
		
//		score.reset();
//		livesModule.reset();
//		
//		this.gameOverScreen.setVisible(false);
//		
//		this.setPaused(false);
		
		this.game.setScreen(new BTNGameScreen(this.game));
	}
	
	@Override
	public void show() {
		
		this.setPaused(false);
		this.containers = new Array<Container>(MAX_ZOMBIE_COUNT);
		this.score = new Score(GAME_WIDTH / 2.0f - 220.0f, GAME_HEIGHT - Score.SCORE_HEIGHT);
		
		timeElapsedSinceLastZombie = 0f;
		
		FitViewport viewport = new FitViewport(GAME_WIDTH, GAME_HEIGHT);
		gameStage = new BTNStage(viewport, game, this);
//		slider = new Slider(GAME_WIDTH / 2.0f, BAR_OFFSET_LOWER, Slider.SLIDER_WIDTH, Slider.SLIDER_HEIGHT, this);
//		sliderButton = new SliderButton(BTNGameScreen.GAME_WIDTH / 2.0f, BTNGameScreen.BAR_OFFSET_LOWER, SliderButton.SLIDER_BUTTON_WIDTH, SliderButton.SLIDER_BUTTON_HEIGHT, this);
		bg = new BTNActor(new Texture("background.png"), GAME_WIDTH / 2.0f, GAME_HEIGHT / 2.0f, GAME_WIDTH, GAME_HEIGHT);
		gloveCase = new BTNActor(new Texture("mover.png"), GAME_WIDTH / 2.0f, GAME_HEIGHT - 350.0f, 162.0f, 138.6f);
		glove = new Glove(GAME_WIDTH / 2.0f, GAME_HEIGHT + GAME_HEIGHT / 4.5f, Glove.GLOVE_WIDTH, Glove.GLOVE_HEIGHT, this, gloveCase);
		topBar = new BTNActor(new Texture("top-bar.png"), GAME_WIDTH / 2.0f, GAME_HEIGHT - TOP_BAR_HEIGHT / 2.0f, GAME_WIDTH, TOP_BAR_HEIGHT);
		
		explosionSplash = new BTNActor(new Texture("explosion-splash.png"), GAME_WIDTH / 2.0f, GAME_HEIGHT / 2.0f, GAME_WIDTH, GAME_HEIGHT);
		
		glove.setDebug(true);
		
		gameStage.addActor(bg);
		
		this.punchSound = Gdx.audio.newSound(Gdx.files.internal("sfx/punch.wav"));
		this.splatSound = Gdx.audio.newSound(Gdx.files.internal("sfx/splat.wav"));
		this.explosionSound = Gdx.audio.newSound(Gdx.files.internal("sfx/explosion.wav"));
		
		initializeContainers();
		initializeLivesModule();
		
		explosionSplash.setVisible(false);
		
		gameStage.addActor(glove);
		gameStage.addActor(gloveCase);
//		gameStage.addActor(slider);
//		gameStage.addActor(sliderButton);
		gameStage.addActor(explosionSplash);
		gameStage.addActor(topBar);
		gameStage.addActor(this.score);
		
		Gdx.input.setInputProcessor(gameStage);
		
		Random r = new Random();
		
		int randomIndex = r.nextInt(MAX_ZOMBIE_COUNT);
		
		activateContainerContents(containers.get(randomIndex));
	}

	private void setPaused(boolean paused) {
		
		this.paused = paused;
	}

	@Override
	public void render(float delta) {
	
		Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        if(!isPaused()){
        	
	        timeElapsedSinceLastZombie += delta;
	        
	        if(timeElapsedSinceLastZombie >= BASE_FREQUENCY_NAZI_REVEAL){
	        	
	        	// Activate a random Nazi that has *not yet been activated*
	        	doActivateUniqueContainer();
	        }
	        
	        gameStage.act(delta);
        }
        
        gameStage.draw();
	}

	private boolean isPaused() {
		
		return paused;
	}

	private void doActivateUniqueContainer(){
		
		// If no Nazis are already activated, then choose one at random.
		int numContainersActivated = 0;
		
		for(Container container : containers){
		
			if(container.getContents().isActivated()){
			
				numContainersActivated++;
			}
		}
		
		if(numContainersActivated < MAX_CONCURRENT_ZOMBIES){
			
			for(int i = 0; i < MAX_CONCURRENT_ZOMBIES; i++){
				
				// Select a random number.
				int index = new Random().nextInt(MAX_ZOMBIE_COUNT);
				
				if(!containers.get(index).getContents().isActivated()){
					
					int cursor = new Random().nextInt(4);
					
					BTNContainedActor newActor = null;
					
					switch(cursor){
					
						case 0:{
							
							newActor  = new Dynamite(0.0f, 0.0f, this);
							
							break;
						}
						case 1 :{
							
							newActor = new Zombie(0.0f, 0.0f, this);
							
							break;
						}
						case 2 :{
							
							newActor = new Bunny(0.0f, 0.0f, this);
									
							break;
						}
						case 3 :{
							
							newActor = new ZombieBunny(0.0f, 0.0f, this);
							
							break;
						}
						default: {
							
							// Do nothing.
							break;
						}
					}
					
					containers.get(index).setContents(newActor);
					
					activateContainerContents(containers.get(index));
					break;
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
		punchSound.dispose();
		splatSound.dispose();
	}

	private void playSound(int soundID){
		
		if(!QUIET_MODE){
			
			switch(soundID){
			
				case SOUND_ID_PUNCH :{
					
					punchSound.play(DEFAULT_VOLUME * 0.75f);
					
					break;
				}
				case SOUND_ID_SPLAT :{
					
					splatSound.play(DEFAULT_VOLUME * 0.25f);
					
					break;
				}
				case SOUND_ID_EXPLOSION :{
					
					explosionSound.play(DEFAULT_VOLUME * 0.75f);
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
		
//		game.setScreen(new BTNGameOverScreen(game, score.getScore()));
		
		showGameOverScreen(score.getScore());
	}

	private void showGameOverScreen(int score) {
		
		this.gameOverScreen = new Group();
		
		BTNActor gameOverAlpha = new BTNActor(new Texture("screen-game-over/alpha-25.png"), GAME_WIDTH / 2.0f, GAME_HEIGHT / 2.0f, GAME_WIDTH, GAME_HEIGHT);
		BTNActor gameOverBackground = new BTNActor(new Texture("screen-game-over/game-over-box.png"), GAME_WIDTH / 2.0f, GAME_HEIGHT / 2.0f - 200.0f, GAME_WIDTH * 0.75f, GAME_HEIGHT * 0.6f);
		BTNActor gameOverText = new BTNActor(new Texture("screen-game-over/game-over-text.png"), GAME_WIDTH / 2.0f, GAME_HEIGHT / 2.0f + 25.0f, GAME_WIDTH * 0.40f, GAME_HEIGHT * 0.15f);
		
		Label scoreLabel = new Label("Score: " + score, new LabelStyle(FontFactory.buildFont(80), new Color(0.0f, 0.0f, 0.0f, 1.0f)));
		scoreLabel.setHeight(100.0f);
		scoreLabel.setX(BTNGameScreen.GAME_WIDTH / 2.0f - (scoreLabel.getWidth() / 2.0f));
		scoreLabel.setY(650.0f + (this.score.getHeight() / 2.0f));
		
		BasicButton restart = new BasicButton(new Texture("screen-game-over/restart-button.png"), new Texture("screen-game-over/restart-button-down-state.png"), GAME_WIDTH / 2.0f, GAME_HEIGHT / 2.0f - 550.0f);
	
		restart.setWidth(GAME_WIDTH * 0.45f);
		restart.setX((GAME_WIDTH / 2.0f) - (restart.getWidth() / 2.0f));
		
		restart.addListener(new com.badlogic.gdx.scenes.scene2d.InputListener(){
			
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				
				return true;
			}
			
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				
				super.touchUp(event, x, y, pointer, button);
				
				Gdx.app.log(BTNGame.TAG, "TOUCHUP Received");
				
				BTNGameScreen.this.reset();
			}
		});
		
		this.gameOverScreen.addActor(gameOverAlpha);
		this.gameOverScreen.addActor(gameOverBackground);
		this.gameOverScreen.addActor(gameOverText);
		this.gameOverScreen.addActor(scoreLabel);
		this.gameOverScreen.addActor(restart);
		
		gameOverScreen.setVisible(false);
		
		gameStage.addActor(gameOverScreen);
		
		this.setPaused(true);
		gameOverScreen.setVisible(true);
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
		
		glove.notifyCollide();
		
		String name = containerContents.getClass().getName();
		containerContents.onCollide(glove);
		
		generateExplosion(containerContents.getX(), containerContents.getY() + containerContents.getHeight() / 2.0f, (containerContents instanceof Dynamite) ? true : false);
		
		if(name.equals(Zombie.class.getName()) || name.equals(ZombieBunny.class.getName())){
			
			playSound(SOUND_ID_PUNCH);
			score.updateScore(score.getScore() + 1);
		}
		else if(name.equals(Bunny.class.getName())){
			
			playSound(SOUND_ID_PUNCH);
			subtractLife();
		}
		else if(name.equals(Dynamite.class.getName())){
			
			playSound(SOUND_ID_EXPLOSION);
			
			doExplosionSplash();
			
//			doEndGame();
		}
	}

	private void doExplosionSplash() {
		
		Color originalColor = explosionSplash.getColor();
		
//		explosionSplash.setColor(originalColor.r, originalColor.g, originalColor.b, 0.0f);
		
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
			
			getContainers().get(i).getContents().onCollide(null);
		}
		
		SequenceAction sequence = new SequenceAction();
		sequence.addAction(fadein);
		sequence.addAction(fadeout);
		sequence.addAction(endGame);
		sequence.addAction(fadeBackIn);
		
		explosionSplash.addAction(sequence);
	}

	private void subtractLife() {
		
		score.setLives(score.getLives() - 1);
		livesModule.popHeart();
		
		playSound(SOUND_ID_SPLAT);
		
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
}
