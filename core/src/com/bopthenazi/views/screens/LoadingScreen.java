package com.bopthenazi.views.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.bopthenazi.game.BTNGame;
import com.bopthenazi.models.BTNStage;

public class LoadingScreen implements Screen {

	private Stage loadingScreenStage;
	
	private BTNGame game;
	private BTNGameScreen gameScreen;
	
	public LoadingScreen(BTNGame game) {
	
		this.game = game;
		this.gameScreen = new BTNGameScreen(game);
		
		FitViewport viewport = new FitViewport(BTNGameScreen.GAME_WIDTH, BTNGameScreen.GAME_HEIGHT);
		this.loadingScreenStage = new Stage(viewport);
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta) {
		
		loadingScreenStage.act(delta);
		loadingScreenStage.draw();
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
