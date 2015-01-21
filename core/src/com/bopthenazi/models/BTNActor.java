package com.bopthenazi.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class BTNActor extends Actor {

	private Texture texture;
	
	public BTNActor(Texture texture, float x, float y, float width, float height){
		
		this.texture = texture;
		
		this.setX(x);
		this.setY(y);
		
		this.setWidth(width);
		this.setHeight(height);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		super.draw(batch, parentAlpha);
		
		batch.draw(texture, getX() - (getWidth() / 2.0f), getY() - (getHeight() / 2.0f), getWidth(), getHeight());
	}
}
