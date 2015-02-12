package com.bopthenazi.views.screens;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.bopthenazi.game.BTNGame;
import com.bopthenazi.models.BTNActor;
import com.bopthenazi.models.BTNContainedActor;
import com.bopthenazi.models.BTNStage;
import com.bopthenazi.models.Container;
import com.bopthenazi.models.Explosion;
import com.bopthenazi.models.Glove;
import com.bopthenazi.models.LivesModule;
import com.bopthenazi.models.Zombie;
import com.bopthenazi.models.Score;
import com.bopthenazi.utils.Action;
import com.bopthenazi.utils.ActionHandler;

public class BTNGameScreen implements Screen{

	private static final boolean USE_HANDLER = true;
	private static final boolean QUIET_MODE = false;
	
	private static final float DEFAULT_VOLUME = 1.0f;
	
	private static final int SOUND_ID_SPLAT = 0;
	private static final int SOUND_ID_PUNCH = 1;
	
	private static final int MAX_ZOMBIE_COUNT = 5;
	private static final int MAX_CONCURRENT_ZOMBIES = 3;
	
	private static final float BASE_FREQUENCY_NAZI_REVEAL = 1.0f;
	
	public static final float GAME_WIDTH = 1080.0f;
	public static final float GAME_HEIGHT = 1920.0f;
	
	public static final float TOP_BAR_HEIGHT = 284.0f;
	public static final float ZOMBIE_OFFSET_HORIZONTAL_MARGIN = 25.0f;
	public static final float BAR_OFFSET_LOWER = 136.3f;
	public static final float BAR_OFFSET_TOP = 256.0f;
	
	private BTNGame game;
	private BTNStage gameStage;
	
	private Sound punchSound;
	private Sound splatSound;
	
	private ActionHandler handler;
	
	private static final float[] CONTAINER_COORDINATES = {212.625f, 540.0f, 867.375f, 376.3125f, 703.6875f};

	private Array<Container> containers;

//	private Slider slider;
//	private SliderButton sliderButton;
	private Glove glove;
	private BTNActor bg;
	private Score score;
	private BTNActor topBar;
	private BTNActor gloveCase;
	private LivesModule livesModule;
	
	private float timeElapsedSinceLastNazi;
	
	public BTNGameScreen(BTNGame game){
		
		this.game = game;
	}
	
	public void notifyNewX(float x) {
		
		glove.notifyTouch(x);
//		sliderButton.setX(x - sliderButton.getWidth() / 2.0f);
//		gloveCase.setX(x - gloveCase.getWidth());
	}
	
	public void onGloveCollision(Zombie naziCollided){
		
		glove.notifyCollide();
		generateExplosion(naziCollided.getX(), naziCollided.getY() + naziCollided.getHeight() / 2.0f);
		naziCollided.onCollide(glove);
	}
	
	private void generateExplosion(float x, float y) {
		
		Explosion explosion = new Explosion(x, y, 0.10f);
		gameStage.addActor(explosion);
	}

	private void activateContainerContents(Container container){
		
		BTNContainedActor contents = container.getContents(); 
		
		if(contents != null){
			
			contents.activate();
		}
	}
	
	private void initializeContainers() {
		
		Array<Array<Actor>> actors = new Array<Array<Actor>>();
		
		for(int i = 0; i < MAX_ZOMBIE_COUNT; i++){
			
			actors.add(initializeContainer(i));
		}
		
		actors.reverse();
		
		for(Array<Actor> actorContainer : actors){
			
			for(Actor actor : actorContainer){
				
				if(actor != null){
					
					gameStage.addActor(actor);
				}
			}
		}
	}

	private Array<Actor> initializeContainer(int i) {
		
		if(i < 3){
			
			containers.add(new Container(CONTAINER_COORDINATES[i], BAR_OFFSET_LOWER + ZOMBIE_OFFSET_HORIZONTAL_MARGIN, this));
		}
		else if(i >= 3){
			
			containers.add(new Container(CONTAINER_COORDINATES[i], (BAR_OFFSET_LOWER + ZOMBIE_OFFSET_HORIZONTAL_MARGIN) * 2, this));
		}
		
		Array<Actor> containerActors = new Array<Actor>();
		
		for (Actor actor : containers.get(i).getActors()){
			
			containerActors.add(actor);
		}
		
		return containerActors;
	}
	
	private void replaceContainerContents(Container container, BTNContainedActor contents){
		
		// TODO: Implementation.
		container.setContents(contents);
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

	@Override
	public void show() {
		
		this.containers = new Array<Container>(MAX_ZOMBIE_COUNT);
		this.score = new Score(GAME_WIDTH / 2.0f - 220.0f, GAME_HEIGHT - Score.SCORE_HEIGHT);
		
		timeElapsedSinceLastNazi = 0f;
		
		FitViewport viewport = new FitViewport(GAME_WIDTH, GAME_HEIGHT);
		gameStage = new BTNStage(viewport, game, this);
//		slider = new Slider(GAME_WIDTH / 2.0f, BAR_OFFSET_LOWER, Slider.SLIDER_WIDTH, Slider.SLIDER_HEIGHT, this);
//		sliderButton = new SliderButton(BTNGameScreen.GAME_WIDTH / 2.0f, BTNGameScreen.BAR_OFFSET_LOWER, SliderButton.SLIDER_BUTTON_WIDTH, SliderButton.SLIDER_BUTTON_HEIGHT, this);
		bg = new BTNActor(new Texture("background.png"), GAME_WIDTH / 2.0f, GAME_HEIGHT / 2.0f, GAME_WIDTH, GAME_HEIGHT);
		gloveCase = new BTNActor(new Texture("mover.png"), GAME_WIDTH / 2.0f, GAME_HEIGHT - 350.0f, 162.0f, 138.6f);
		glove = new Glove(GAME_WIDTH / 2.0f, GAME_HEIGHT + GAME_HEIGHT / 4.5f, Glove.GLOVE_WIDTH, Glove.GLOVE_HEIGHT, this, gloveCase);
		topBar = new BTNActor(new Texture("top-bar.png"), GAME_WIDTH / 2.0f, GAME_HEIGHT - TOP_BAR_HEIGHT / 2.0f, GAME_WIDTH, TOP_BAR_HEIGHT);
		
		gameStage.addActor(bg);
		
		if(USE_HANDLER){
		
			handler = new ActionHandler(this);
			handler.setRunning(true);
			handler.start();
		}
		
		this.punchSound = Gdx.audio.newSound(Gdx.files.internal("sfx/punch.wav"));
		this.splatSound = Gdx.audio.newSound(Gdx.files.internal("sfx/splat.wav"));
		
		initializeContainers();
		initializeLivesModule();
		
		gameStage.addActor(glove);
		gameStage.addActor(gloveCase);
//		gameStage.addActor(slider);
//		gameStage.addActor(sliderButton);
		gameStage.addActor(topBar);
		gameStage.addActor(score);
		
		Gdx.input.setInputProcessor(gameStage);
		
		Random r = new Random();
		
		int randomIndex = r.nextInt(MAX_ZOMBIE_COUNT);
		
		activateContainerContents(containers.get(randomIndex));
	}

	@Override
	public void render(float delta) {
	
		Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        timeElapsedSinceLastNazi += delta;
        
        if(timeElapsedSinceLastNazi >= BASE_FREQUENCY_NAZI_REVEAL){
        	
        	// Activate a random Nazi that has *not yet been activated*
        	doActivateUniqueNazi();
        }
        
        gameStage.act(delta);
        gameStage.draw();
	}

	private void doActivateUniqueNazi(){
		
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
					
					activateContainerContents(containers.get(index));
					break;
				}
			}
		}
		
	    timeElapsedSinceLastNazi = 0f;
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
		
		if(USE_HANDLER){
			
			while(handler.isAlive()){
				
				try{
					
					handler.setRunning(false);	
				}
				catch(Exception e){
					
					// Just try again.
				}
				finally{
					
					handler.setRunning(false);
				}
			}
		}
	}

	public void notifyZombieDeactivate(Zombie hitTarget) {
		
		doActivateUniqueNazi();
		
		if(hitTarget.getActorState() == Zombie.STATE_HIT){
			
			playSound(SOUND_ID_PUNCH);
			
			score.updateScore(score.getScore() + 1);
		}
		else{
			
			score.setLives(score.getLives() - 1);
			livesModule.popHeart();
			
			playSound(SOUND_ID_SPLAT);
			
			if(score.getLives() <= 0){
				
				doEndGame();
			}
		}
	}
	
	public void addActionToHandlerQueue(Action a){
		
		if(USE_HANDLER){
			
			this.handler.addAction(a);
		}
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
				default :{
					
					// Do nothing.
					break;
				}
			}
		}
	}
	
	private void doEndGame() {
		
//		Gdx.app.log(BTNGame.TAG, "Game Over!");
		
//		game.setScreen(new BTNMenuScreen(game));
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
		print("> ActionHandler.isRunning: " + handler.isRunning());
		print("> Glove.getCurrentAction: " + glove.getCurrentAction());
		print("> ActionHandler.getActionQueue: " + handler.getActionQueue());
		print("> Glove.getActorState: " + glove.getActorState());
		print("> Glove.getCachedX: " + glove.getCachedX());
		print("> Glove.willCollide: " + glove.willCollide());
		
		print("==========================");
	}
	
	private void print(String output){
		
		Gdx.app.log(BTNGame.TAG, output);
	}
}
