package com.bopthenazi.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.bopthenazi.utils.BTNCollideableActor;
import com.bopthenazi.views.screens.BTNGameScreen;

public class Glove extends BTNCollideableActor {

	public static final float GLOVE_WIDTH = 192.0f;
	public static final float GLOVE_HEIGHT = 2367.0f;
	
	private static final float GLOVE_HEIGHT_OFFSET = 775.0f;
	
	private float velocityX;
	private float velocityY;
	
	private float toX;
	private float toXOffset;
	
	private boolean readyToDrop;
	private boolean dropRequested;

	private BTNGameScreen gameScreen;
	private BTNMoveableActor gloveCase;
	
	private static final float GLOVE_UNLOCK_BARRIER = BTNGameScreen.GAME_HEIGHT - GLOVE_HEIGHT_OFFSET;
	
	private static final float GLOVE_STATIC_VELOCITY_X = 0.0f;
	private static final float GLOVE_STATIC_VELOCITY_Y = 0.0f;
	
	private static final float GLOVE_VELOCTY_X = 3000.0f;
	
	private static final float GLOVE_VELOCITY_Y_DOWN = -4000.0f;
	private static final float GLOVE_VELOCITY_Y_UP = 4000.0f;
	
	public Glove(float x, float y, float width, float height, BTNGameScreen game, BTNMoveableActor gloveCase) {
		
		super(new Texture("glove.png"), x, y, width, height);
		
		// Velocity is measured in game-pixels / second.
		this.velocityX = GLOVE_VELOCTY_X;
		this.velocityY = GLOVE_STATIC_VELOCITY_Y;
		
		this.toX = getX();
		this.toXOffset = 2500.0f / 100.0f + 20.0f;
		
		this.readyToDrop = true;
		this.dropRequested = false;
		
		this.gameScreen = game;
		this.gloveCase = gloveCase;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		super.draw(batch, parentAlpha);
	}
	
	@Override
	public void act(float delta) {
		
		super.act(delta);
		
		if(this.getX() < (getToX() - toXOffset)){
			
			this.setX(getX() + delta * velocityX);
		}
		else if (this.getX() > (getToX() + toXOffset)){
			
			this.setX(getX() - delta * velocityX);
		}
		else{
			
			release();
		}
		
		this.setY(getY() + delta * velocityY);
		
		if(this.getY() >= GLOVE_UNLOCK_BARRIER){
			
			// If the glove is moving upwards...
			if(this.getVelocityY() > 0){
				
				// Allow another drop.
				this.setReadyToDrop(true);
			}
		}
		
		// If the glove hits the bottom of the screen...
		if(this.getY() <= 0){
			
			this.setVelocityY(GLOVE_VELOCITY_Y_UP);
			this.setReadyToDrop(false);
		}
		
		// If the glove hits the top part of the screen...
		if(this.getY() >= BTNGameScreen.GAME_HEIGHT - GLOVE_HEIGHT_OFFSET){
			
			this.setVelocityY(GLOVE_STATIC_VELOCITY_Y);
			this.setY((BTNGameScreen.GAME_HEIGHT - GLOVE_HEIGHT_OFFSET) - 1);
		}
	}

	/**
	 * @return the readyToDrop
	 */
	public boolean isReadyToDrop() {
		
		return readyToDrop;
	}

	/**
	 * @param readyToDrop the readyToDrop to set
	 */
	public void setReadyToDrop(boolean readyToDrop) {
		
		this.readyToDrop = readyToDrop;
	}

	public void notifyCollide() {
		
		this.setVelocityY(GLOVE_VELOCITY_Y_UP);
	}
	
	private void release(){
		
		if(readyToDrop && dropRequested){
			
			this.setVelocityY(GLOVE_VELOCITY_Y_DOWN);
			this.setReadyToDrop(false);
			this.dropRequested = false;
		}
	}
	
	public void requestDrop(){
		
		this.dropRequested = true;
	}

	@Override
	public void setX(float x) {
		
		super.setX(x);
		
		if(gloveCase != null){
			
			gloveCase.setX(x + 15.0f);
		}
	}
	
	/**
	 * @return the velocityX
	 */
	public float getVelocityX() {
		
		return velocityX;
	}

	/**
	 * @param velocityX the velocityX to set
	 */
	public void setVelocityX(float velocityX) {
		
		this.velocityX = velocityX;
	}

	/**
	 * @return the velocityY
	 */
	public float getVelocityY() {
		
		return velocityY;
	}

	/**
	 * @param velocityY the velocityY to set
	 */
	public void setVelocityY(float velocityY) {
		
		this.velocityY = velocityY;
	}
	
	/**
	 * @return the toX
	 */
	public float getToX() {
		
		return toX;
	}

	/**
	 * @param toX the toX to set
	 */
	public void setToX(float toX) {
		
		this.toX = toX;
	}
}
