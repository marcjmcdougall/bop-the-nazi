package com.bopthenazi.models;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.bopthenazi.views.screens.BTNGameScreen;

public class PauseScreenModule extends Group{

	private static final int NUM_PAUSE_ANIMATION_FRAMES = 20;
	
	private BTNGameScreen gameScreen;
	
	private Image bgAlpha;
	private BTNActor pause;
	
	public PauseScreenModule(BTNGameScreen gameScreen) {
	
		this.gameScreen = gameScreen;
		
		initialize();
	}
	
	private void initialize(){
		
		bgAlpha = new Image(gameScreen.getTexture("alpha-25"));
		bgAlpha.setSize(BTNGameScreen.GAME_WIDTH, BTNGameScreen.GAME_HEIGHT);
		bgAlpha.setX(0.0f);
		bgAlpha.setY(0.0f);
		
		Array<TextureRegion> textures = new Array<TextureRegion>();
		
		for(int i = 0; i < NUM_PAUSE_ANIMATION_FRAMES; i++){
			
			if(i < 10){
				
				textures.add(gameScreen.getTexture("paused-text0" + i));
			}
			else{
				
				textures.add(gameScreen.getTexture("paused-text" + i));
			}
		}
		
		pause = new BTNActor(textures, BTNGameScreen.GAME_WIDTH / 2.0f, BTNGameScreen.GAME_HEIGHT / 2.0f, 600.0f, 300.0f);
		pause.setX(BTNGameScreen.GAME_WIDTH / 2.0f);
		pause.setY(BTNGameScreen.GAME_HEIGHT / 2.0f - (pause.getHeight() / 2.0f));
		
		this.addListener(new InputListener(){
			
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				
				super.touchDown(event, x, y, pointer, button);
				
				return true;
			}
			
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				
				super.touchUp(event, x, y, pointer, button);
				
				gameScreen.hidePauseScreen();
			}
		});
		
		this.addActor(bgAlpha);
		this.addActor(pause);
		
		this.setVisible(false);
	}
}
