package com.bopthenazi.views.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.bopthenazi.game.BTNGame;
import com.bopthenazi.models.BasicButton;

public class BTNMenuScreen implements Screen {

	private static final int MENU_WIDTH = 1080;
	private static final int MENU_HEIGHT = 1920;
	
	private BTNGame game;
	
	private Stage menuStage;
	private BasicButton newGame;
	
	public BTNMenuScreen(BTNGame game){
		
		this.game = game;
		
		FitViewport viewport = new FitViewport(MENU_WIDTH, MENU_HEIGHT);
		menuStage = new Stage(viewport);
		
		newGame = new BasicButton(new Texture("start-button-up.png"), new Texture("start-button-down.png"), 540, 960);
		
		newGame.addListener(new com.badlogic.gdx.scenes.scene2d.InputListener(){
			
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				
				return true;
			}
			
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				
				super.touchUp(event, x, y, pointer, button);
				
				Gdx.app.log(BTNGame.TAG, "TOUCHUP Received");
				
				BTNMenuScreen.this.game.setScreen(new BTNGameScreen(BTNMenuScreen.this.game));
			}
		});
		
		menuStage.addActor(newGame);
		
		Gdx.input.setInputProcessor(menuStage);
	}
	
	@Override
	public void show() {
		
	}

	@Override
	public void render(float delta) {
		
		Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        menuStage.act(delta);
        menuStage.draw();
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void dispose() {
		
		menuStage.dispose();
	}
}
