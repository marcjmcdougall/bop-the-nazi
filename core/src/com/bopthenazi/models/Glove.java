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
	
	private boolean readyToDrop;
	
	private BTNGameScreen gameScreen;
	
	private static final float GLOVE_UNLOCK_BARRIER = 800.0f;
	
	private static final float GLOVE_STATIC_VELOCITY_X = 0.0f;
	private static final float GLOVE_STATIC_VELOCITY_Y = 0.0f;
	
	private static final float GLOVE_VELOCITY_Y_DOWN = -2500.0f;
	private static final float GLOVE_VELOCITY_Y_UP = 500.0f;
	
	public Glove(float x, float y, float width, float height, BTNGameScreen game) {
		
		super(new Texture("glove.png"), x, y, width, height);
		
		// Velocity is measured in game-pixels / second.
		this.velocityX = GLOVE_STATIC_VELOCITY_X;
		this.velocityY = GLOVE_STATIC_VELOCITY_Y;
		
		this.readyToDrop = true;
		
		this.gameScreen = game;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		super.draw(batch, parentAlpha);
	}
	
	@Override
	public void act(float delta) {
		
		super.act(delta);
		
		this.setX(getX() + delta * velocityX);
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
	
	public void release(){
		
		this.setVelocityY(GLOVE_VELOCITY_Y_DOWN);
		this.setReadyToDrop(false);
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
}
