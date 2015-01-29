package com.bopthenazi.views.screens;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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
import com.bopthenazi.models.Nazi;
import com.bopthenazi.models.NaziContainer;
import com.bopthenazi.models.Score;
import com.bopthenazi.models.Slider;
import com.bopthenazi.models.SliderButton;

public class BTNGameScreen implements Screen{

	private static final int MAX_NAZI_COUNT = 7;
	private static final int MAX_CONCURRENT_NAZIS = 1;
	
	private static final float BASE_FREQUENCY_NAZI_REVEAL = 3f;
	
	public static final float GAME_WIDTH = 1080.0f;
	public static final float GAME_HEIGHT = 1920.0f;
	
	public static final float TOP_BAR_HEIGHT = 284.0f;
	public static final float NAZI_OFFSET_HORIZONTAL_MARGIN = 25.0f;
	public static final float BAR_OFFSET_LOWER = 136.3f;
	public static final float BAR_OFFSET_TOP = 256.0f;
	
	private BTNGame game;
	private BTNStage gameStage;
	
	private static final float[] NAZI_CONTAINER_COORDINATES = {157.5f, 412.5f, 667.5f, 922.5f, 285.0f, 540.0f, 795.0f};

	private Array<NaziContainer> naziContainers;

	private Slider slider;
	private SliderButton sliderButton;
	private Glove glove;
	private BTNActor bg;
	private Score score;
	private BTNActor topBar;
	private BTNMoveableActor gloveCase;
	
	private float timeElapsedSinceLastNazi;
	
	public BTNGameScreen(BTNGame game){
		
		this.game = game;
		this.naziContainers = new Array<NaziContainer>(MAX_NAZI_COUNT);
		this.score = new Score(GAME_WIDTH / 2.0f - 220.0f, GAME_HEIGHT - Score.SCORE_HEIGHT);
		
		timeElapsedSinceLastNazi = 0f;
		
		FitViewport viewport = new FitViewport(GAME_WIDTH, GAME_HEIGHT);
		gameStage = new BTNStage(viewport, game, this);
		slider = new Slider(GAME_WIDTH / 2.0f, BAR_OFFSET_LOWER, Slider.SLIDER_WIDTH, Slider.SLIDER_HEIGHT);
		sliderButton = new SliderButton(BTNGameScreen.GAME_WIDTH / 2.0f, BTNGameScreen.BAR_OFFSET_LOWER, SliderButton.SLIDER_BUTTON_WIDTH, SliderButton.SLIDER_BUTTON_HEIGHT, this);
		bg = new BTNActor(new Texture("background.png"), GAME_WIDTH / 2.0f, GAME_HEIGHT / 2.0f, GAME_WIDTH, GAME_HEIGHT);
		glove = new Glove(GAME_WIDTH / 2.0f, GAME_HEIGHT + GAME_HEIGHT / 4.5f, Glove.GLOVE_WIDTH, Glove.GLOVE_HEIGHT, this);
		gloveCase = new BTNMoveableActor(new Texture("mover.png"), GAME_WIDTH / 2.0f, GAME_HEIGHT - 350.0f, 162.0f, 138.6f);
		topBar = new BTNActor(new Texture("top-bar.png"), GAME_WIDTH / 2.0f, GAME_HEIGHT - TOP_BAR_HEIGHT / 2.0f, GAME_WIDTH, TOP_BAR_HEIGHT);
		
		gameStage.addActor(bg);
		
		initializeNaziContainers();
		gameStage.addActor(glove);
		gameStage.addActor(gloveCase);
		gameStage.addActor(slider);
		gameStage.addActor(sliderButton);
		gameStage.addActor(topBar);
		gameStage.addActor(score);
		
		Gdx.input.setInputProcessor(gameStage);
	}
	
	public void notifyNewX(float x) {
		
		glove.setX(x - Glove.GLOVE_WIDTH / 2.0f);
		gloveCase.setX(x - 162.0f / 2.0f);
	}
	
	public void onGloveCollision(Nazi naziCollided){
		
		glove.notifyCollide();
		generateExplosion(naziCollided.getX() + naziCollided.getWidth() / 2.0f, naziCollided.getY() + naziCollided.getHeight());
		naziCollided.onCollide();
	}
	
	private void generateExplosion(float x, float y) {
		
		Explosion explosion = new Explosion(x, y, 0.10f);
		
		gameStage.addActor(explosion);
	}

	public void notifyTouchUp() {
		
		if(glove.isReadyToDrop()){
			
			glove.setMoving(true);
			glove.setMovingDown(true);
			glove.setReadyToDrop(false);
		}
	}
	
	private void activateNewNazi(int index){
		
		naziContainers.get(index).getNazi().performNaziActivate();
	}
	
	private void initializeNaziContainers() {
		
		int count = 0;
		
		Array<Array<Actor>> actors = new Array<Array<Actor>>();
		
		for(int i = 0; i < MAX_NAZI_COUNT; i++){
			
			if(i < 4){
				
				naziContainers.add(new NaziContainer(NAZI_CONTAINER_COORDINATES[count], BAR_OFFSET_LOWER + NAZI_OFFSET_HORIZONTAL_MARGIN, this));
			}
			else if(i >= 4){
				
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

	@Override
	public void show() {
		
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
			
			// Select a random number.
			int index = new Random().nextInt(MAX_NAZI_COUNT);
				
			activateNewNazi(index);
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
	}

	@Override
	public void dispose() {
		
		// TODO Auto-generated method stub
		gameStage.dispose();
	}

	public SliderButton getSliderButton() {
		
		return sliderButton;
	}

	public void notifyNaziDeactivate(boolean hit) {
		
		doActivateUniqueNazi();
		
		if(hit){
			
			score.updateScore(score.getScore() + 1);
		}
		else{
			
			score.setLives(score.getLives() - 1);
			
			if(score.getLives() <= 0){
				
				doEndGame();
			}
		}
	}

	private void doEndGame() {
		
		Gdx.app.log(BTNGame.TAG, "Game Over!");
		
		game.setScreen(new BTNMenuScreen(game));
	}
}
