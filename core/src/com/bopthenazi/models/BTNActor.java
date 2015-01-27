package com.bopthenazi.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class BTNActor extends Actor {

	private Texture texture;
	
	public BTNActor(Texture texture, float x, float y, float width, float height){
		
		// TODO: No super constructor called?
		this.texture = texture;
		
		this.setWidth(width);
		this.setHeight(height);
		
		this.setX(x - this.getWidth() / 2.0f);
		this.setY(y - this.getHeight() / 2.0f);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		super.draw(batch, parentAlpha);
		
		batch.draw(texture, getX(), getY(), getWidth(), getHeight());
	}
}
