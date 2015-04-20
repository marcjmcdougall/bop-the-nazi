package com.bopthenazi.models;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.bopthenazi.views.screens.BTNGameScreen;

public class Timer extends Actor {

	private Label time;
	private float timeValue;
	
	private BTNGameScreen gameScreen;
	
	public Timer(float x, float y, BTNGameScreen gameScreen){
		
		this.setX(x);
		this.setY(y);
		
		this.gameScreen = gameScreen;
		
		LabelStyle style = new LabelStyle(gameScreen.getAssetManager().get("masaaki-regular-120.otf", BitmapFont.class), new Color(1.0f, 1.0f, 1.0f, 1.0f));
		
		timeValue = 30.5f;
		
		time = new Label(getTimerText(timeValue), style);
		
		time.setX(getX() - time.getWidth() / 2.0f);
		time.setY(getY());
		
		time.setWidth(getWidth());
		time.setHeight(getHeight());

		time.setDebug(true);
	}	
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
 		time.draw(batch, this.getColor().a * parentAlpha);
	}
	
	public void updateTimer(float timeElapsed){
		
		timeValue -= timeElapsed;

		if(timeValue < 0.0f){
			
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

	private void updateText() {
		
		time.setText(getTimerText(timeValue));
		time.setX(time.getX() - time.getWidth() / 2.0f);
	}
	
	public void reset(){
		
		this.timeValue = 30.0f;
		this.time.setText(getTimerText(timeValue));
	}
	
	private String getTimerText(float time){
		
		String output = "" + time;
		
		output = output.substring(0, output.indexOf(".") + 2);
		
		return output;
	}
	
	private void onComplete(){
		
		gameScreen.doEndGame();
	}
}
