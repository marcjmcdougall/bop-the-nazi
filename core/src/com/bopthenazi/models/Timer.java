package com.bopthenazi.models;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.bopthenazi.views.screens.BTNGameScreen;

public class Timer extends Group {

	private static final float TIMER_STARTING_VALUE = 30.00f;
	
	private Label time;
	private float timeValue;
	
	private BTNGameScreen gameScreen;
	
	public Timer(float x, float y, BTNGameScreen gameScreen){
		
		this.setX(0.0f);
		this.setY(0.0f);
		
		this.gameScreen = gameScreen;
		
		LabelStyle style = new LabelStyle(gameScreen.getAssetManager().get("masaaki-regular-120.otf", BitmapFont.class), new Color(1.0f, 1.0f, 1.0f, 1.0f));
		
		timeValue = TIMER_STARTING_VALUE;
		
		time = new Label(getTimerText(timeValue), style);
		
//		time.setX(getX()/* - time.getWidth() / 2.0f*/);
//		time.setY(getY()/* - time.getHeight() / 2.0f*/);
		
		time.setAlignment(Align.center);
		time.setX(x - time.getTextBounds().width / 2.0f);
		time.setY(y - 75.0f/* - time.getHeight() / 2.0f*/);
		
		this.addActor(time);
	}	
	
//	@Override
//	public void draw(Batch batch, float parentAlpha) {
//		
// 		time.draw(batch, this.getColor().a * parentAlpha);
// 		super.draw(batch, this.getColor().a * parentAlpha);
//	}
	
	public void updateTimer(float timeElapsed){
		
		timeValue -= timeElapsed;

		if(timeValue < 0.0f){
			
			timeValue = 0.0f;
			updateText();
			onComplete();
		}
		else if(timeValue == 0.0f){
			
			updateText();
			onComplete();
		}
		else{
			
			updateText();
		}
	}

	public void doPulse() {
		
//		time.addAction(Actions.sequence(Actions.scaleBy(1.5f, 1.5f, 0.25f), Actions.scaleBy(-1.5f, -1.5f, 0.25f)));
		time.addAction(Actions.sequence(Actions.fadeOut(0.25f), Actions.fadeIn(0.25f)));
	}

	private void updateText() {
		
		time.setText(getTimerText(timeValue));
		time.setAlignment(Align.center);
	}
	
	public void reset(){
		
		this.timeValue = TIMER_STARTING_VALUE;
		this.time.setText(getTimerText(timeValue));
	}
	
	private String getTimerText(float time){
		
		String output = "" + time;
		
		output = output.substring(0, output.indexOf(".") + 2);
		
		return output;
	}
	
	public float getTimerValue(){
		
		return timeValue;
	}
	
	private void onComplete(){
		
		gameScreen.doEndGame();
	}
}
