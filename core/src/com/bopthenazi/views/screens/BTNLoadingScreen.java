package com.bopthenazi.views.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.bopthenazi.game.BTNGame;
import com.bopthenazi.models.BTNActor;
import com.bopthenazi.models.BTNStage;

public class BTNLoadingScreen implements Screen {

	private Stage loadingScreenStage;
	
	private BTNGame game;
	private BTNGameScreen gameScreen;
	
	private BTNActor background;
	
	public BTNLoadingScreen(BTNGame game) {
	
		this.game = game;
		this.gameScreen = new BTNGameScreen(game);
		
		FitViewport viewport = new FitViewport(BTNGameScreen.GAME_WIDTH, BTNGameScreen.GAME_HEIGHT);
		this.loadingScreenStage = new Stage(viewport);
	}
	
	@Override
	public void show() {
	
		this.background = new BTNActor(new Texture(Gdx.files.internal("textures/screen-loading/bg.png")), BTNGameScreen.GAME_WIDTH / 2.0f, BTNGameScreen.GAME_HEIGHT / 2.0f, BTNGameScreen.GAME_WIDTH, BTNGameScreen.GAME_HEIGHT);
		
		loadingScreenStage.addActor(background);
	}

	@Override
	public void render(float delta) {
		
		Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
		loadingScreenStage.act(delta);
		loadingScreenStage.draw();
		
		if(gameScreen.getAssetManager().update()){
			
			game.setScreen(gameScreen);
		}
		
		System.out.println("Progress: " + gameScreen.getAssetManager().getProgress());
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
}
