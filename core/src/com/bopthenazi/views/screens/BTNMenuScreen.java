package com.bopthenazi.views.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.RotateToAction;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.bopthenazi.game.BTNGame;
import com.bopthenazi.models.BTNActor;
import com.bopthenazi.models.BasicButton;

public class BTNMenuScreen implements Screen {

	private static final float MENU_WIDTH = 1080.0f;
	private static final float MENU_HEIGHT = 1920.0f;
	
	private BTNGame game;
	
	private Stage menuStage;
	
	private BTNActor bg;
	private BTNActor stripes;
	private BTNActor zombie;
	private BasicButton startGame;
	private BTNActor topBar;
	private BTNActor title;
	
	public BTNMenuScreen(BTNGame game){
		
		this.game = game;
	}
	
	@Override
	public void show() {
		
		FitViewport viewport = new FitViewport(MENU_WIDTH, MENU_HEIGHT);
		menuStage = new Stage(viewport);
		
		this.bg = new BTNActor(new Texture("screen-menu/orange-background.png"), MENU_WIDTH / 2.0f, MENU_HEIGHT / 2.0f, MENU_WIDTH, MENU_HEIGHT);
		this.title = new BTNActor(new Texture("screen-menu/zombie-bop-menu-title.png"), MENU_WIDTH / 2.0f, MENU_HEIGHT * 0.75f, 850.0f, 600.0f);
		this.stripes = new BTNActor(new Texture("screen-menu/yellow-stripes.png"), MENU_WIDTH / 2.0f, (MENU_HEIGHT / 2.0f), MENU_WIDTH * 1.5f, MENU_HEIGHT * 1.25f);
		this.zombie = new BTNActor(new Texture("screen-menu/happy-zombie.png"), MENU_WIDTH / 2.0f, MENU_HEIGHT * 0.32f, MENU_WIDTH, MENU_HEIGHT * 0.6f);
		this.topBar = new BTNActor(new Texture("screen-menu/top-bar.png"), MENU_WIDTH / 2.0f, MENU_HEIGHT - 76.0f, MENU_WIDTH, 150.0f);
		this.startGame = new BasicButton(new Texture("screen-menu/start-bar-up-state.png"), new Texture("screen-menu/start-bar-down-state.png"), MENU_WIDTH / 2.0f, 0.0f);
		this.startGame.setWidth(MENU_WIDTH);
		this.startGame.setHeight(MENU_HEIGHT * 0.125f);
		
		this.startGame.setX(MENU_WIDTH / 2.0f - (startGame.getWidth() / 2.0f));
		this.startGame.setY(0.0f);
		
		RotateToAction rotate = new RotateToAction();
		rotate.setRotation(180.0f);
		rotate.setDuration(5.0f);
		
		startGame.addListener(new com.badlogic.gdx.scenes.scene2d.InputListener(){
			
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
		
		stripes.addAction(rotate);
		
		menuStage.addActor(bg);
		menuStage.addActor(stripes);
		menuStage.addActor(title);
		menuStage.addActor(zombie);
		menuStage.addActor(topBar);
		menuStage.addActor(startGame);
		menuStage.addActor(startGame);
		
		Gdx.input.setInputProcessor(menuStage);
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
