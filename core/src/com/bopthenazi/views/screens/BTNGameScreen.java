package com.bopthenazi.views.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.bopthenazi.game.BTNGame;
import com.bopthenazi.models.BTNActor;
import com.bopthenazi.models.Glove;
import com.bopthenazi.models.Nazi;
import com.bopthenazi.models.NaziContainer;
import com.bopthenazi.models.Slider;
import com.bopthenazi.models.SliderButton;

public class BTNGameScreen implements Screen{

	public static final float GAME_WIDTH = 1080.0f;
	public static final float GAME_HEIGHT = 1920.0f;
	
	public static final float BAR_OFFSET_LOWER = 136.3f;
	public static final float BAR_OFFSET_TOP = 256.0f;
	
	private BTNGame game;
	private Stage gameStage;
	
	private NaziContainer naziContainer;
	
//	private NaziContainer[] naziContainers;
	private Slider slider;
	private SliderButton sliderButton;
	private Glove glove;
	private BTNActor bg;
	
	public BTNGameScreen(BTNGame game){
		
		this.game = game;
//		this.naziContainers = new NaziContainer[7];
		this.naziContainer = new NaziContainer(540, 960);
		
		FitViewport viewport = new FitViewport(GAME_WIDTH, GAME_HEIGHT);
		gameStage = new Stage(viewport);
		
		slider = new Slider(GAME_WIDTH / 2.0f, BAR_OFFSET_LOWER, Slider.SLIDER_WIDTH, Slider.SLIDER_HEIGHT);
		sliderButton = new SliderButton(BTNGameScreen.GAME_WIDTH / 2.0f, BTNGameScreen.BAR_OFFSET_LOWER, SliderButton.SLIDER_BUTTON_WIDTH, SliderButton.SLIDER_BUTTON_HEIGHT, this);
		bg = new BTNActor(new Texture("background.png"), GAME_WIDTH / 2.0f, GAME_HEIGHT / 2.0f, GAME_WIDTH, GAME_HEIGHT);
		glove = new Glove(GAME_WIDTH / 2.0f, GAME_HEIGHT - (BAR_OFFSET_TOP + Glove.GLOVE_HEIGHT / 2.0f), Glove.GLOVE_WIDTH, Glove.GLOVE_HEIGHT);
		
		gameStage.addActor(bg);
		
		for(Actor actor : naziContainer.getActors()){
			
			gameStage.addActor(actor);
		}
		
//		initializeNazis();
		gameStage.addActor(slider);
		gameStage.addActor(sliderButton);
		gameStage.addActor(glove);
		
		Gdx.input.setInputProcessor(gameStage);
	}
	
	public void notifyNewX(float x) {
		
		glove.setX(x);
	}
	
	public void notifyTouchUp() {
		
		float originalY = glove.getY();
		
		MoveToAction moveTo = new MoveToAction();
		
		moveTo.setPosition(glove.getX(), BAR_OFFSET_LOWER);
		moveTo.setDuration(1.0f);
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
	
//	private void initializeNazis() {
//		
//		// TODO: Incomplete.
//		int count = 0;
//		
//		for(NaziContainer naziContainer : naziContainers){
//			
//			naziContainer = new NaziContainer(, y);
//			gameStage.addActor(nazi);
//			
//			count++;
//		}
//	}

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
}
