package com.bopthenazi.views.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.bopthenazi.game.BTNGame;
import com.bopthenazi.models.BTNActor;
import com.bopthenazi.models.BasicButton;
import com.bopthenazi.utils.FontFactory;

public class BTNGameOverScreen implements Screen{

	private static final int GO_SCREEN_WIDTH = 810;
	private static final int GO_SCREEN_HEIGHT = 1152;
	
	private BTNGame game;
	private Stage goStage;
	
	private BTNActor gameOverText;
	private BasicButton restart;
	private BTNActor background;
	
	private Label score;
	
	public BTNGameOverScreen(BTNGame game, int score){
		
		this.game = game;
		
		FitViewport viewport = new FitViewport(BTNGameScreen.GAME_WIDTH, BTNGameScreen.GAME_HEIGHT);
		goStage = new Stage(viewport);
		
		background = new BTNActor(new Texture("screen-game-over/game-over-box.png"), BTNGameScreen.GAME_WIDTH / 2.0f, BTNGameScreen.GAME_HEIGHT / 2.0f - 150.0f, GO_SCREEN_WIDTH, GO_SCREEN_HEIGHT);
		gameOverText = new BTNActor(new Texture("screen-game-over/game-over-text.png"), BTNGameScreen.GAME_WIDTH / 2.0f, BTNGameScreen.GAME_HEIGHT / 2.0f + 100.0f, GO_SCREEN_WIDTH * 0.5f, GO_SCREEN_HEIGHT * 0.25f);
		restart = new BasicButton(new Texture("screen-game-over/restart-button.png"), new Texture("screen-game-over/restart-button-down-state.png"), BTNGameScreen.GAME_WIDTH / 2.0f, 450.0f);
		
		this.score = new Label("Score: " + score, new LabelStyle(FontFactory.buildFont(), new Color(0.0f, 0.0f, 0.0f, 1.0f)));
		
		this.score.setFontScale(4.0f);
		this.score.setHeight(100.0f);
		this.score.setX(BTNGameScreen.GAME_WIDTH / 2.0f - 175.0f);
		this.score.setY(650.0f + (this.score.getHeight() / 2.0f));
		
		restart.setWidth(BTNGameScreen.GAME_WIDTH * 0.5f);
		restart.setX((BTNGameScreen.GAME_WIDTH / 2.0f) - restart.getWidth() / 2.0f);
		
		restart.addListener(new com.badlogic.gdx.scenes.scene2d.InputListener(){
			
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				
				return true;
			}
			
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				
				super.touchUp(event, x, y, pointer, button);
				
				Gdx.app.log(BTNGame.TAG, "TOUCHUP Received");
				
				BTNGameOverScreen.this.game.setScreen(new BTNGameScreen(BTNGameOverScreen.this.game));
			}
		});
		
		goStage.addActor(background);
		goStage.addActor(gameOverText);
		goStage.addActor(this.score);
		goStage.addActor(restart);
		
		Gdx.input.setInputProcessor(goStage);
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
