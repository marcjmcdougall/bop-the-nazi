package com.bopthenazi.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class BTNActor extends Actor {

	private Texture texture;
	private Rectangle rect;
	
	public BTNActor(Texture texture, float x, float y, float width, float height){
		
		this(texture, x, y, width, height, x, y, width, height);
	}
	
	public BTNActor(Texture texture, float x, float y, float width, float height, float xHitBox, float yHitBox, float widthHitBox, float heightHitBox) {
		
		super();
		
		this.texture = texture;
		this.setWidth(width);
		this.setHeight(height);
		
		this.setX(x);
		this.setY(y);
		
		this.setRect(new Rectangle(xHitBox, yHitBox, widthHitBox, heightHitBox));
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		super.draw(batch, parentAlpha);
		
		batch.draw(texture, this.getX() - (this.getWidth() / 2.0f), this.getY() - (this.getHeight() / 2.0f), this.getWidth(), this.getHeight());
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
		
		calculateHitBox();
	}
	
	private void calculateHitBox() {

		if(rect != null){
			
			rect.set(getX() - (getWidth() / 2.0f), getY() - (getHeight() / 2.0f), getWidth(), getHeight());
		}
	}

	@Override
	public void setX(float x) {
		
		super.setX(x);
		
		calculateHitBox();
	}
	
	@Override
	public void setY(float y) {
		
		super.setY(y);
		
		calculateHitBox();
	}
	
	@Override
	public void setPosition(float x, float y) {
		
		super.setPosition(x, y);
		
		calculateHitBox();
	}
	
	@Override
	public void setPosition(float x, float y, int alignment) {
		
		super.setPosition(x, y, alignment);
		
		calculateHitBox();
	}
}
