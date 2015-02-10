package com.bopthenazi.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.bopthenazi.views.screens.BTNGameScreen;

public class Slider extends BTNActor {

	public static final float SLIDER_HEIGHT = 284.0f;
	public static final float SLIDER_WIDTH = 1080.0f;
	
	private BTNGameScreen gameScreen;
	
	public Slider(float x, float y, float width, float height, BTNGameScreen gameScreen) {
		
		super(new Texture("bop-slider.png"), x, y, width, height);
		
		this.gameScreen = gameScreen;
		
		this.addListener(new InputListener(){
			
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				
				super.touchUp(event, x, y, pointer, button);
			}
			
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				
				Slider.this.gameScreen.notifyNewX(event.getStageX());
				
				return true;
			}
			
			@Override
			public void touchDragged(InputEvent event, float x, float y, int pointer) {
				
//				Slider.this.gameScreen.notifyNewX(event.getStageX());
				
				super.touchDragged(event, x, y, pointer);
			}
		});
	}
}
