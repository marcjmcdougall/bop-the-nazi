package com.bopthenazi.views.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.bopthenazi.game.BTNGame;
import com.bopthenazi.models.BTNActor;

public class BTNGameOverScreen implements Screen{

	private static final int GO_SCREEN_WIDTH = 540;
	private static final int GO_SCREEN_HEIGHT = 960;
	
	private BTNGame game;
	private Stage goStage;
	
	public BTNGameOverScreen(BTNGame game){
		
		this.game = game;
		
		FitViewport viewport = new FitViewport(GO_SCREEN_WIDTH, GO_SCREEN_HEIGHT);
		goStage = new Stage(viewport);
		
//		goStage.addActor(new BTNActor(new Texture("alpha-25.png"), 0, 0, 1080.0f, 1920.0f));
		goStage.addActor(new BTNActor(new Texture("bop-button.png"), GO_SCREEN_WIDTH / 2.0f, GO_SCREEN_HEIGHT / 2.0f, 200.0f, 200.0f));
	}
	
	@Override
	public void show() {
		
		// TODO: Implementation.
	}

	@Override
	public void render(float delta) {
		
        goStage.act(delta);
        goStage.draw();
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
