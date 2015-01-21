package com.bopthenazi.models;

import com.badlogic.gdx.graphics.Texture;

public class Nazi extends BTNActor {

	public static final float NAZI_WIDTH = 200.0f;
	public static final float NAZI_HEIGHT = 388.8f;
	
	public Nazi(float x, float y){
		
		super(new Texture("nazi.png"), x, y, NAZI_WIDTH, NAZI_HEIGHT);
	}
}
