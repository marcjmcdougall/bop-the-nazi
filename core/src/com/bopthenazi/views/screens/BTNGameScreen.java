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
import com.bopthenazi.models.BTNMoveableActor;
import com.bopthenazi.models.BTNStage;
import com.bopthenazi.models.Explosion;
import com.bopthenazi.models.Glove;
import com.bopthenazi.models.LivesModule;
import com.bopthenazi.models.Nazi;
import com.bopthenazi.models.NaziContainer;
import com.bopthenazi.models.Score;
import com.bopthenazi.utils.Action;
import com.bopthenazi.utils.ActionHandler;

public class BTNGameScreen implements Screen{

	private static final boolean USE_HANDLER = true;
	private static final boolean QUIET_MODE = true;
	
	private static final float DEFAULT_VOLUME = 1.0f;
	
	private static final int SOUND_ID_SPLAT = 0;
	private static final int SOUND_ID_PUNCH = 1;
	
	private static final int MAX_NAZI_COUNT = 5;
	private static final int MAX_CONCURRENT_NAZIS = 2;
	
	private static final float BASE_FREQUENCY_NAZI_REVEAL = 1.0f;
	
	public static final float GAME_WIDTH = 1080.0f;
	public static final float GAME_HEIGHT = 1920.0f;
	
	public static final float TOP_BAR_HEIGHT = 284.0f;
	public static final float NAZI_OFFSET_HORIZONTAL_MARGIN = 25.0f;
	public static final float BAR_OFFSET_LOWER = 136.3f;
	public static final float BAR_OFFSET_TOP = 256.0f;
	
	private BTNGame game;
	private BTNStage gameStage;
	
	private Sound punchSound;
	private Sound splatSound;
	
	private ActionHandler handler;
	
	private static final float[] NAZI_CONTAINER_COORDINATES = {212.625f, 540.0f, 867.375f, 376.3125f, 703.6875f};

	private Array<NaziContainer> naziContainers;

//	private Slider slider;
//	private SliderButton sliderButton;
	private Glove glove;
	private BTNActor bg;
	private Score score;
	private BTNActor topBar;
	private BTNMoveableActor gloveCase;
	private LivesModule livesModule;
	
	private float timeElapsedSinceLastNazi;
	
	public BTNGameScreen(BTNGame game){
		
		this.game = game;
	}
	
	public void notifyNewX(float x) {
		
		System.out.println("x: " + x);
		
		glove.notifyTouch(x);
//		sliderButton.setX(x - sliderButton.getWidth() / 2.0f);
//		gloveCase.setX(x - gloveCase.getWidth());
	}
	
	public void onGloveCollision(Nazi naziCollided){
		
		glove.notifyCollide();
		generateExplosion(naziCollided.getX() + naziCollided.getWidth() / 2.0f - 25.0f, naziCollided.getY() + naziCollided.getHeight());
		naziCollided.onCollide();
	}
	
	private void generateExplosion(float x, float y) {
		
		Explosion explosion = new Explosion(x, y, 0.10f);
		gameStage.addActor(explosion);
	}

	private void activateNewNazi(int index){
		
//		Gdx.audio.newSound(Gdx.files.internal("sfx/zombie-appear.wav")).play();
		naziContainers.get(index).getNazi().performNaziActivate();
	}
	
	private void initializeNaziContainers() {
		
		int count = 0;
		
		Array<Array<Actor>> actors = new Array<Array<Actor>>();
		
		for(int i = 0; i < MAX_NAZI_COUNT; i++){
			
			if(i < 3){
				
				naziContainers.add(new NaziContainer(NAZI_CONTAINER_COORDINATES[count], BAR_OFFSET_LOWER + NAZI_OFFSET_HORIZONTAL_MARGIN, this));
			}
			else if(i >= 3){
				
				naziContainers.add(new NaziContainer(NAZI_CONTAINER_COORDINATES[count], (BAR_OFFSET_LOWER + NAZI_OFFSET_HORIZONTAL_MARGIN) * 2, this));
			}
			
			Array<Actor> containerActors = new Array<Actor>();
			
			for (Actor actor : naziContainers.get(i).getActors()){
				
				containerActors.add(actor);
			}
			
			actors.add(containerActors);
			
			count++;
		}
		
		actors.reverse();
		
		for(Array<Actor> actorContainer : actors){
			
			for(Actor actor : actorContainer){
				
				gameStage.addActor(actor);
			}
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

	@Override
	public void show() {
		
		this.naziContainers = new Array<NaziContainer>(MAX_NAZI_COUNT);
		this.score = new Score(GAME_WIDTH / 2.0f - 220.0f, GAME_HEIGHT - Score.SCORE_HEIGHT);
		
		timeElapsedSinceLastNazi = 0f;
		
		FitViewport viewport = new FitViewport(GAME_WIDTH, GAME_HEIGHT);
		gameStage = new BTNStage(viewport, game, this);
//		slider = new Slider(GAME_WIDTH / 2.0f, BAR_OFFSET_LOWER, Slider.SLIDER_WIDTH, Slider.SLIDER_HEIGHT, this);
//		sliderButton = new SliderButton(BTNGameScreen.GAME_WIDTH / 2.0f, BTNGameScreen.BAR_OFFSET_LOWER, SliderButton.SLIDER_BUTTON_WIDTH, SliderButton.SLIDER_BUTTON_HEIGHT, this);
		bg = new BTNActor(new Texture("background.png"), GAME_WIDTH / 2.0f, GAME_HEIGHT / 2.0f, GAME_WIDTH, GAME_HEIGHT);
		gloveCase = new BTNMoveableActor(new Texture("mover.png"), GAME_WIDTH / 2.0f, GAME_HEIGHT - 350.0f, 162.0f, 138.6f);
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
		
		initializeNaziContainers();
		initializeLivesModule();
		
		gameStage.addActor(glove);
		gameStage.addActor(gloveCase);
//		gameStage.addActor(slider);
//		gameStage.addActor(sliderButton);
		gameStage.addActor(topBar);
		gameStage.addActor(score);
		
		Gdx.input.setInputProcessor(gameStage);
		
		Random r = new Random();
		
		int randomIndex = r.nextInt(MAX_NAZI_COUNT);
		
		activateNewNazi(randomIndex);
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
		int numNazisActivated = 0;
		
		for(NaziContainer naziContainer : naziContainers){
		
			if(naziContainer.getNazi().isActivated()){
			
				numNazisActivated++;
			}
		}
		
		if(numNazisActivated < MAX_CONCURRENT_NAZIS){
			
			for(int i = 0; i < MAX_CONCURRENT_NAZIS; i++){
				
				// Select a random number.
				int index = new Random().nextInt(MAX_NAZI_COUNT);
				
				if(!naziContainers.get(index).getNazi().isActivated()){
					
					activateNewNazi(index);
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

	public void notifyNaziDeactivate(boolean hit) {
		
		doActivateUniqueNazi();
		
		if(hit){
			
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
					
					splatSound.play(DEFAULT_VOLUME / 2);
					
					break;
				}
				case SOUND_ID_SPLAT :{
					
					punchSound.play(DEFAULT_VOLUME);
					
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
		
		print("==========================");
	}
	
	private void print(String output){
		
		Gdx.app.log(BTNGame.TAG, output);
	}
}
