package com.bopthenazi.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.bopthenazi.utils.Collidable;
import com.bopthenazi.views.screens.BTNGameScreen;

public class BTNActor extends Actor implements Collidable{

	private static final int STATE_DEFAULT = 0;
	
	private static final boolean DEFAULT_COLLIDE_STATE = true;
	
	private static final float DEFAULT_X = BTNGameScreen.GAME_WIDTH / 2.0f;
	private static final float DEFAULT_Y = BTNGameScreen.GAME_HEIGHT / 2.0f;
	private static final float DEFAULT_WIDTH = 50.0f;
	private static final float DEFAULT_HEIGHT = 50.0f;
	
	private static final String DEFAULT_TEXTURE = "alpha-25.png";
	
	private boolean collidable;
	private Texture texture;
	private Rectangle rect;
	
	private volatile int actorState;
	
	public BTNActor(){
		
		this(new Texture(DEFAULT_TEXTURE), DEFAULT_X, DEFAULT_Y, DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}
	
	public BTNActor(Texture texture, float x, float y, float width, float height){
		
		this(texture, x, y, width, height, x, y, width, height);
	}
	
	public BTNActor(Texture texture, float x, float y, float width, float height, float xHitBox, float yHitBox, float widthHitBox, float heightHitBox) {
		
		super();
		
		this.texture = texture;
		this.setWidth(width);
		this.setHeight(height);
		
		this.setCollide(DEFAULT_COLLIDE_STATE);
		this.setActorState(STATE_DEFAULT);
		
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

	@Override
	public void onCollide(Collidable partner) {
		
		// Do nothing right now.  Expect override.
	}

	@Override
	public boolean canCollide() {
		
		return collidable;
	}

	@Override
	public void setCollide(boolean collide) {
		
		this.collidable = collide;
	}

	public synchronized int getActorState() {
		
		return actorState;
	}

	public synchronized void setActorState(int actorState) {
		
		this.actorState = actorState;
	}
}
