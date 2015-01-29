package com.bopthenazi.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.bopthenazi.utils.BTNCollideableActor;
import com.bopthenazi.views.screens.BTNGameScreen;

public class Glove extends BTNCollideableActor {

	public static final float GLOVE_WIDTH = 192.0f;
	public static final float GLOVE_HEIGHT = 2367.0f;
	
	private float downwardVelocity;
	private float upwardVelocity;
	
	private boolean moving;
	private boolean movingDown;
	private boolean readyToDrop;
	
	private BTNGameScreen gameScreen;
	
	public Glove(float x, float y, float width, float height, BTNGameScreen game) {
		
		super(new Texture("glove.png"), x, y, width, height);
		
		// Velocity is measured in game-pixels / second.
		this.downwardVelocity = 2500.0f;
		this.upwardVelocity = 500.0f;
		
		this.moving = false;
		this.movingDown = true;
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
		
		if(moving){
			
			if(movingDown){
				
				if(!(getY() <= 0.0f)){
				
					setY(getY() - downwardVelocity * delta);
				}
				else{
					
					setMovingDown(false);
				}
			}
			else{
				
				if(!(getY() >= 1163.1667f)){
					
					setY(getY() + upwardVelocity * delta);
				}
				else{
					
					readyToDrop = true;
				}
			}
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
		
		this.movingDown = false;
	}

	/**
	 * @return the moving
	 */
	public boolean isMoving() {
		
		return moving;
	}

	/**
	 * @param moving the moving to set
	 */
	public void setMoving(boolean moving) {
		
		this.moving = moving;
	}
	
	/**
	 * @return the movingDown
	 */
	public boolean isMovingDown() {
		
		return movingDown;
	}

	/**
	 * @param movingDown the movingDown to set
	 */
	public void setMovingDown(boolean movingDown) {
		
		this.movingDown = movingDown;
	}
}
