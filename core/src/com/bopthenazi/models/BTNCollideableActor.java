package com.bopthenazi.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public abstract class BTNCollideableActor extends BTNActor {

	private Rectangle rect;
	
	public BTNCollideableActor(Texture texture, float x, float y, float width, float height){
		
		super(texture, x, y, width, height);
		
		this.setRect(new Rectangle(x, y, width, height));
	}
	
	public BTNCollideableActor(Texture texture, float x, float y, float width, float height, float xHitBox, float yHitBox, float widthHitBox, float heightHitBox) {
		
		super(texture, x, y, width, height);
		
		this.setRect(new Rectangle(xHitBox, yHitBox, widthHitBox, heightHitBox));
	}

	/**
	 * @return the rect
	 */
	public Rectangle getRect() {
		
		return rect;
	}

	/**
	 * @param rect the rect to set
	 */
	public void setRect(Rectangle rect) {
		
		this.rect = rect;
	}
	
	@Override
	public void setX(float x) {
		
		super.setX(x);
		
		if(rect != null){
			
			rect.setX(x);
		}
	}
	
	@Override
	public void setY(float y) {
		
		super.setY(y);
		
		if(rect != null){
			
			rect.setY(y);
		}
	}
	
	@Override
	public void setPosition(float x, float y) {
		
		super.setPosition(x, y);
		
		if(rect != null){
			
			rect.setX(x);
			rect.setY(y);
		}
	}
	
	@Override
	public void setPosition(float x, float y, int alignment) {
		
		super.setPosition(x, y, alignment);
		
		if(rect != null){
			
			rect.setX(x);
			rect.setY(y);
		}
	}
}
