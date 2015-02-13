package com.bopthenazi.views.screens;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
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
import com.bopthenazi.models.Glove;
import com.bopthenazi.models.LivesModule;
import com.bopthenazi.models.Score;
import com.bopthenazi.models.Zombie;

public class BTNGameScreen implements Screen{

	private static final boolean USE_HANDLER = true;
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
	
	private Sound punchSound;
	private Sound splatSound;
	private Sound explosionSound;
	
	private static final float[] CONTAINER_COORDINATES = {212.625f, 540.0f, 867.375f, 376.3125f, 703.6875f};

	private Array<Container> containers;

	private Glove glove;
	private BTNActor bg;
	private Score score;
	private BTNActor topBar;
	private BTNActor gloveCase;
	private LivesModule livesModule;
	
	private float timeElapsedSinceLastZombie;
	
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
	
	private void replaceContainerContents(Container container, BTNContainedActor contents){
		
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
		
		timeElapsedSinceLastZombie = 0f;
		
		FitViewport viewport = new FitViewport(GAME_WIDTH, GAME_HEIGHT);
		gameStage = new BTNStage(viewport, game, this);
//		slider = new Slider(GAME_WIDTH / 2.0f, BAR_OFFSET_LOWER, Slider.SLIDER_WIDTH, Slider.SLIDER_HEIGHT, this);
//		sliderButton = new SliderButton(BTNGameScreen.GAME_WIDTH / 2.0f, BTNGameScreen.BAR_OFFSET_LOWER, SliderButton.SLIDER_BUTTON_WIDTH, SliderButton.SLIDER_BUTTON_HEIGHT, this);
		bg = new BTNActor(new Texture("background.png"), GAME_WIDTH / 2.0f, GAME_HEIGHT / 2.0f, GAME_WIDTH, GAME_HEIGHT);
		gloveCase = new BTNActor(new Texture("mover.png"), GAME_WIDTH / 2.0f, GAME_HEIGHT - 350.0f, 162.0f, 138.6f);
		glove = new Glove(GAME_WIDTH / 2.0f, GAME_HEIGHT + GAME_HEIGHT / 4.5f, Glove.GLOVE_WIDTH, Glove.GLOVE_HEIGHT, this, gloveCase);
		topBar = new BTNActor(new Texture("top-bar.png"), GAME_WIDTH / 2.0f, GAME_HEIGHT - TOP_BAR_HEIGHT / 2.0f, GAME_WIDTH, TOP_BAR_HEIGHT);
		
		gameStage.addActor(bg);
		
		this.punchSound = Gdx.audio.newSound(Gdx.files.internal("sfx/punch.wav"));
		this.splatSound = Gdx.audio.newSound(Gdx.files.internal("sfx/splat.wav"));
		this.explosionSound = Gdx.audio.newSound(Gdx.files.internal("sfx/explosion.wav"));
		
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
        
        timeElapsedSinceLastZombie += delta;
        
        if(timeElapsedSinceLastZombie >= BASE_FREQUENCY_NAZI_REVEAL){
        	
        	// Activate a random Nazi that has *not yet been activated*
        	doActivateUniqueContainer();
        }
        
        gameStage.act(delta);
        gameStage.draw();
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
					
					int cursor = new Random().nextInt(3);
					
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
		
		game.setScreen(new BTNGameOverScreen(game, score.getScore()));
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
		print("> Glove.getActorState: " + glove.getActorState());
		print("> Glove.getCachedX: " + glove.getCachedX());
		
		print("==========================");
	}
	
	private void print(String output){
		
		Gdx.app.log(BTNGame.TAG, output);
	}

	public void notifyDeactivate(BTNContainedActor btnContainedActor) {
		
		String className = btnContainedActor.getClass().getName();
		
		if(className.equals(Zombie.class.getName())){
			
			handleZombieDeactivate((Zombie) btnContainedActor);
		}
		else if(className.equals(Dynamite.class.getName())){
			
			handleDynamiteDeactivate((Dynamite) btnContainedActor);
		}
		else{
			
			// Log that something strange happened.
			print("A BTNContainedActor was deactivated but not handled.");
		}
	}

	private void handleDynamiteDeactivate(Dynamite btnContainedActor) {
		
		print("Handling Dynamite deactivate now");
	}

	private void handleZombieDeactivate(Zombie zombie) {
		
		print("Handling Zombie deactivate now");
		
		doActivateUniqueContainer();
		
		if(!(zombie.getActorState() == BTNContainedActor.STATE_HIT)){
			
			score.setLives(score.getLives() - 1);
			livesModule.popHeart();
			
			playSound(SOUND_ID_SPLAT);
			
			if(score.getLives() <= 0){
				
				doEndGame();
			}
		}
	}

	public void notifyCollision(BTNContainedActor containerContents) {
		
		glove.notifyCollide();
		
		String name = containerContents.getClass().getName();
		containerContents.onCollide(glove);
		
		generateExplosion(containerContents.getX(), containerContents.getY() + containerContents.getHeight() / 2.0f, (containerContents instanceof Dynamite) ? true : false);
		
		if(name.equals(Zombie.class.getName()) || name.equals(Bunny.class.getName())){
			
			playSound(SOUND_ID_PUNCH);
			score.updateScore(score.getScore() + 1);
		}
		else if(name.equals(Dynamite.class.getName())){
			
			playSound(SOUND_ID_EXPLOSION);
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
