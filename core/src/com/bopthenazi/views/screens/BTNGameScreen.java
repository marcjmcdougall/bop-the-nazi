package com.bopthenazi.views.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.bopthenazi.game.BTNGame;
import com.bopthenazi.models.Nazi;

public class BTNGameScreen implements Screen{

	private static final int GAME_WIDTH = 1080;
	private static final int GAME_HEIGHT = 1920;
	
	private BTNGame game;
	private Stage gameStage;
	
	private Nazi[] nazis;
	
	public BTNGameScreen(BTNGame game){
		
		this.game = game;
		this.nazis = new Nazi[6];
		
		FitViewport viewport = new FitViewport(GAME_WIDTH, GAME_HEIGHT);
		gameStage = new Stage(viewport);
		
		initializeNazis();
		
		Gdx.input.setInputProcessor(gameStage);
	}
	
	private void initializeNazis() {
		
		// TODO: Incomplete.
		int count = 0;
		
		for(Nazi nazi : nazis){
			
			nazi = new Nazi((Nazi.NAZI_WIDTH / 2.0f) + count * Nazi.NAZI_WIDTH, 0.0f);
			gameStage.addActor(nazi);
			
			count++;
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
}
