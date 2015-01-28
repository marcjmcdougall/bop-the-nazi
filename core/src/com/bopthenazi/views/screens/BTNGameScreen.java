package com.bopthenazi.views.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
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

	public static final float GAME_WIDTH = 1080.0f;
	public static final float GAME_HEIGHT = 1920.0f;
	
	public static final float TOP_BAR_HEIGHT = 284.0f;
	public static final float NAZI_OFFSET_HORIZONTAL_MARGIN = 25.0f;
	public static final float BAR_OFFSET_LOWER = 136.3f;
	public static final float BAR_OFFSET_TOP = 256.0f;
	
	private BTNGame game;
	private BTNStage gameStage;
	
	private static final float[] NAZI_CONTAINER_COORDINATES = {157.5f, 412.5f, 667.5f, 922.5f, 285.0f, 540.0f, 795.0f};
	
	private NaziContainer[] naziContainers;
	private Slider slider;
	private SliderButton sliderButton;
	private Glove glove;
	private BTNActor bg;
	private Score score;
	private BTNActor topBar;
	private BTNActor sideBars;
	private BTNMoveableActor gloveCase;
	
	public BTNGameScreen(BTNGame game){
		
		this.game = game;
		this.naziContainers = new NaziContainer[7];
		this.score = new Score(GAME_WIDTH / 2.0f + 50.0f, GAME_HEIGHT - Score.SCORE_HEIGHT);
		
		FitViewport viewport = new FitViewport(GAME_WIDTH, GAME_HEIGHT);
		gameStage = new BTNStage(viewport, game, this);
		slider = new Slider(GAME_WIDTH / 2.0f, BAR_OFFSET_LOWER, Slider.SLIDER_WIDTH, Slider.SLIDER_HEIGHT);
		sliderButton = new SliderButton(BTNGameScreen.GAME_WIDTH / 2.0f, BTNGameScreen.BAR_OFFSET_LOWER, SliderButton.SLIDER_BUTTON_WIDTH, SliderButton.SLIDER_BUTTON_HEIGHT, this);
		bg = new BTNActor(new Texture("background.png"), GAME_WIDTH / 2.0f, GAME_HEIGHT / 2.0f, GAME_WIDTH, GAME_HEIGHT);
		sideBars = new BTNActor(new Texture("bars.png"), GAME_WIDTH / 2.0f, GAME_HEIGHT / 2.0f, GAME_WIDTH, GAME_HEIGHT);
		glove = new Glove(GAME_WIDTH / 2.0f, GAME_HEIGHT + GAME_HEIGHT / 4.5f, Glove.GLOVE_WIDTH, Glove.GLOVE_HEIGHT, this);
		gloveCase = new BTNMoveableActor(new Texture("mover.png"), GAME_WIDTH / 2.0f, GAME_HEIGHT - 350.0f, 162.0f, 138.6f);
		topBar = new BTNActor(new Texture("top-bar.png"), GAME_WIDTH / 2.0f, GAME_HEIGHT - TOP_BAR_HEIGHT / 2.0f, GAME_WIDTH, TOP_BAR_HEIGHT);
		
		gameStage.addActor(bg);
		
		initializeNaziContainers();
		gameStage.addActor(glove);
		gameStage.addActor(gloveCase);
		gameStage.addActor(sideBars);
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
		
		score.updateScore(score.getScore() + 1);
		glove.notifyCollide();
		generateExplosion(naziCollided.getX() + naziCollided.getWidth() / 2.0f, naziCollided.getY() + naziCollided.getHeight());
		naziCollided.onCollide();
	}
	
	private void generateExplosion(float x, float y) {
		
		Explosion explosion = new Explosion(x, y, 0.10f);
		
		gameStage.addActor(explosion);
	}

	public void notifyTouchUp() {
		
		float originalY = glove.getY();
		
		System.out.println(originalY);
		
		MoveToAction moveTo = new MoveToAction();
		
		moveTo.setPosition(glove.getX(), BAR_OFFSET_LOWER);
		moveTo.setDuration(0.25f);
		moveTo.setInterpolation(Interpolation.pow2);
		
		MoveToAction moveFrom = new MoveToAction();
		
		moveFrom.setPosition(glove.getX(), originalY);
		moveFrom.setDuration(1.0f);
		moveFrom.setInterpolation(Interpolation.linear);
		
		RunnableAction unlock = new RunnableAction();
		unlock.setRunnable(new Runnable() {
			
			@Override
			public void run() {
				
				sliderButton.unlock();
			}
		});
		
		SequenceAction sequence = new SequenceAction(moveTo, moveFrom, unlock);
		glove.addAction(sequence);
	}
	
	private void initializeNaziContainers() {
		
		int count = 0;
		
		Array<Array<Actor>> actors = new Array<Array<Actor>>();
		
		
		for(NaziContainer naziContainer : naziContainers){
			
			if(count < 4){
				
				naziContainer = new NaziContainer(NAZI_CONTAINER_COORDINATES[count], BAR_OFFSET_LOWER + NAZI_OFFSET_HORIZONTAL_MARGIN);
			}
			else if( count >= 4){
				
				naziContainer = new NaziContainer(NAZI_CONTAINER_COORDINATES[count], (BAR_OFFSET_LOWER + NAZI_OFFSET_HORIZONTAL_MARGIN) * 2);
			}
			
			Array<Actor> containerActors = new Array<Actor>();
			
			for (Actor actor : naziContainer.getActors()){
				
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
		
	}

	@Override
	public void render(float delta) {
	
		Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        gameStage.act(delta);
        gameStage.draw();
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
}
