package com.bopthenazi.views.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.bopthenazi.game.BTNGame;

public class BTNGameScreen implements Screen{

	private static final int GAME_WIDTH = 1080;
	private static final int GAME_HEIGHT = 1920;
	
	private BTNGame game;
	private Stage gameStage;
	
	private Actor slider;
	private Actor sliderButton;
	
	private Actor[] nazis;
	private Actor[] holes;
	
	private Actor glove;
	
	public BTNGameScreen(BTNGame game){
		
		this.game = game;
		
		FitViewport viewport = new FitViewport(GAME_WIDTH, GAME_HEIGHT);
		gameStage = new Stage(viewport);
		
		initializeActors();
	}
	
	@Override
	public void show() {
	}

	@Override
	public void render(float delta) {
	
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
		
	}
	
	private void initializeActors() {
	
		slider = new Actor();
	}
}
