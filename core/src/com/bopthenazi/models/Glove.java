package com.bopthenazi.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class Glove extends BTNActor {

	public static final float GLOVE_WIDTH = 356.4f;
	public static final float GLOVE_HEIGHT = 345.6f;
	
	public Glove(float x, float y, float width, float height) {
		
		super(new Texture("glove.png"), x, y, width, height);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		super.draw(batch, parentAlpha);
	}
}
