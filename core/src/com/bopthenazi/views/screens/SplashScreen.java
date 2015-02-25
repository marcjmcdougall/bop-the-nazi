package com.bopthenazi.views.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.bopthenazi.game.BTNGame;

public class SplashScreen implements Screen {

	private BTNGame game;
	
	private Stage splashStage;
	private BTNMenuScreen menuScreen;
	
	private float showDuration;
	
	public SplashScreen(BTNGame game){
		
		this.game = game;
		this.menuScreen = new BTNMenuScreen(game);
		
		this.showDuration = 1.5f;
	}
	
	@Override
	public void show() {
		
		FitViewport viewport = new FitViewport(1080.0f, 1920.0f);
		splashStage = new Stage(viewport);
		
		Image logo = new Image(new Texture("textures/logo.png"));
		logo.setWidth(300.0f);
		logo.setHeight(345.15f);
		
		logo.setPosition((1080.0f / 2.0f) - logo.getWidth() / 2.0f, (1920.0f / 2.0f) - logo.getHeight() / 2.0f);
		
		splashStage.addActor(logo);
	}

	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		splashStage.act(delta);
		splashStage.draw();
		
		if(menuScreen.getAssetManager().update() && showDuration <= 0.0f){
			
			game.setScreen(menuScreen);
		}
		
		showDuration-= delta;
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
