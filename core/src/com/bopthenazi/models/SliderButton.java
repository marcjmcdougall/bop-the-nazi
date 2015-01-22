package com.bopthenazi.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.bopthenazi.views.screens.BTNGameScreen;

public class SliderButton extends BTNActor {

	public static final float SLIDER_BUTTON_WIDTH = 270.0f;
	public static final float SLIDER_BUTTON_HEIGHT = 270.0f;
	
	private boolean lock = false;
	
	private final BTNGameScreen gameScreen;
	
	public SliderButton(float x, float y, float width, float height, BTNGameScreen gameScreen) {
		
		super(new Texture("bop-button.png"), x, y, width, height);
		
		this.gameScreen = gameScreen;
		
		this.addListener(new InputListener(){
			
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				
				return true;
			}
			
			@Override
			public void touchDragged(InputEvent event, float x, float y, int pointer) {
				
				if(event.getStageX() > SLIDER_BUTTON_WIDTH / 2.0f && event.getStageX() < BTNGameScreen.GAME_WIDTH - SLIDER_BUTTON_WIDTH / 2.0f && !lock){
					
					setX(event.getStageX() - SLIDER_BUTTON_WIDTH / 2.0f);
					SliderButton.this.gameScreen.notifyNewX(event.getStageX());
				}
				
				super.touchDragged(event, x, y, pointer);
			}
			
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				
				super.touchUp(event, x, y, pointer, button);
				
				if(!lock){
					lock = true;
				
					SliderButton.this.gameScreen.notifyTouchUp();
				}
			}
		});
	}
	
	public void unlock(){
		
		this.lock = false;
	}
}
