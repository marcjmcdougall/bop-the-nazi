package com.bopthenazi.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class Slider extends BTNActor {

	public static final float SLIDER_HEIGHT = 284.0f;
	public static final float SLIDER_WIDTH = 1080.0f;
	
	public Slider(float x, float y, float width, float height) {
		
		super(new Texture("bop-slider.png"), x, y, width, height);
		
		this.addListener(new InputListener(){
			
			
		});
	}
}
