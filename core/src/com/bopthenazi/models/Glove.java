package com.bopthenazi.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.bopthenazi.utils.Action;
import com.bopthenazi.views.screens.BTNGameScreen;

public class Glove extends BTNCollideableActor {

	public static final float GLOVE_WIDTH = 192.0f;
	public static final float GLOVE_HEIGHT = 2367.0f;
	
	private static final float GLOVE_HEIGHT_OFFSET = 775.0f;
	
	public static final boolean COLLIDE = true;
	
	private static final int STATE_STATIC = 0;
	private static final int STATE_MOVING_DOWN = 1;
	private static final int STATE_MOVING_UP = 2;
	
	private volatile int actorState;
	
	private volatile boolean willCollide;
	
	private float velocityX;
	private float velocityY;
	
	private float toX;
	private float toXOffset;
	
	private SequenceAction moveDown;
	private SequenceAction moveUp;
	private MoveToAction translateX;
	
	private RunnableAction stateMutator;
	
	private volatile Action currentAction;
	
	private boolean readyToDrop;
	private boolean dropRequested;

	private BTNGameScreen gameScreen;
	private BTNMoveableActor gloveCase;
	
	private volatile float cachedX;
	
	private static final float GLOVE_UNLOCK_BARRIER = BTNGameScreen.GAME_HEIGHT - GLOVE_HEIGHT_OFFSET;
	
	private static final float GLOVE_STATIC_VELOCITY_X = 0.0f;
	private static final float GLOVE_STATIC_VELOCITY_Y = 0.0f;
	
	private static final float GLOVE_VELOCTY_X = 4000.0f;
	
	private static final float GLOVE_VELOCITY_Y_DOWN = 4000.0f;
	private static final float GLOVE_VELOCITY_Y_UP = 4000.0f;
	
	public Glove(float x, float y, float width, float height, BTNGameScreen game, BTNMoveableActor gloveCase) {
		
		super(new Texture("glove.png"), x, y, width, height);
		
		// Velocity is measured in game-pixels / second.
		this.velocityX = GLOVE_VELOCTY_X;
		this.velocityY = GLOVE_STATIC_VELOCITY_Y;
		
		this.actorState = STATE_STATIC;
		this.willCollide = true;
		
		this.toX = getX();
		this.toXOffset = 2500.0f / 100.0f + 20.0f;
		
		this.readyToDrop = true;
		this.dropRequested = false;
		this.setCachedX(-1.0f);
		
		this.gameScreen = game;
		this.gloveCase = gloveCase;
		
		this.moveDown = new SequenceAction();
		this.moveUp = new SequenceAction();
		this.translateX = new MoveToAction();
		this.stateMutator = new RunnableAction();
		
		this.currentAction = null;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		super.draw(batch, parentAlpha);
	}
	
	@Override
	public void act(float delta) {
		
		super.act(delta);
		
//		System.out.println(getX() + ", " + getY());
		
//		if(this.getX() < (getToX() - toXOffset)){
//			
//			this.setX(getX() + delta * velocityX);
//		}
//		else if (this.getX() > (getToX() + toXOffset)){
//			
//			this.setX(getX() - delta * velocityX);
//		}
//		else{
//			
//			release();
//		}
//		
//		this.setY(getY() + delta * velocityY);
//		
//		if(this.getY() >= GLOVE_UNLOCK_BARRIER){
//			
//			// If the glove is moving upwards...
//			if(this.getVelocityY() > 0){
//				
//				// Allow another drop.
//				this.setReadyToDrop(true);
//			}
//		}
//		
//		// If the glove hits the bottom of the screen...
//		if(this.getY() <= 0){
//			
//			this.setVelocityY(GLOVE_VELOCITY_Y_UP);
//			this.setReadyToDrop(false);
//		}
//		
//		// If the glove hits the top part of the screen...
//		if(this.getY() >= BTNGameScreen.GAME_HEIGHT - GLOVE_HEIGHT_OFFSET){
//			
//			this.setVelocityY(GLOVE_STATIC_VELOCITY_Y);
//			this.setY((BTNGameScreen.GAME_HEIGHT - GLOVE_HEIGHT_OFFSET) - 1);
//		}
	}
	
	public void notifyTouch(float x){
		
		switch(actorState){
			
			case STATE_STATIC :{
				
				SequenceAction sequence = new SequenceAction();
				sequence.addAction(buildTranslateXAction(x));
				sequence.addAction(buildMoveDownAction(x, BTNGameScreen.GAME_HEIGHT - GLOVE_HEIGHT_OFFSET));
				sequence.addAction(buildMoveUpAction(x, 0.0f));
				
				this.addAction(sequence);
				
				break;
			}
			// If the glove is already moving down...
			case STATE_MOVING_DOWN :{
				
				System.out.println("Moving Down!");
				
				// Clear all actions on the Glove.
				this.clearActions();
				
				this.setCachedX(x);
				
				SequenceAction sequence = new SequenceAction();
				
				// First, add an action to simply finish the original "MoveDown" MoveToAction.
				sequence.addAction(buildMoveDownAction(this.getX() + (getWidth() / 2.0f), this.getY()));
				
				// Then, move up to the top of the screen.
				sequence.addAction(buildMoveUpAction(this.getX() + (getWidth() / 2.0f), 0.0f));
				
				// Then, translate across the top of the screen.
				sequence.addAction(buildTranslateXAction(x));
				
				// Then, perform another "bop".
				sequence.addAction(buildMoveDownAction(x, BTNGameScreen.GAME_HEIGHT - GLOVE_HEIGHT_OFFSET));
				sequence.addAction(buildMoveUpAction(x, 0.0f));
				
				this.addAction(sequence);
				
				break;
			}
			// If the glove is already moving up...
			case STATE_MOVING_UP :{
				
				System.out.println("Moving Up!");
				
				// Clear all actions on the Glove.
				this.clearActions();
				
				SequenceAction sequence = new SequenceAction();
				
				// Then, move up to the top of the screen.
				sequence.addAction(buildMoveUpAction(this.getX() + (getWidth() / 2.0f), this.getY()));
				
				// Then, translate across the top of the screen.
				sequence.addAction(buildTranslateXAction(x));
				
				// Then, perform another "bop".  
				// Note: We know that the glove will be at the top of the screen here, so we put that value as y.
				sequence.addAction(buildMoveDownAction(x, BTNGameScreen.GAME_HEIGHT - GLOVE_HEIGHT_OFFSET));
				sequence.addAction(buildMoveUpAction(x, 0.0f));
				
				this.addAction(sequence);
				
				break;
			}
			default :{
				
				// Do nothing.
				
				break;
			}
		}
	}
	
	/**
	 * Generates the "Move Up" MoveToAction for the Glove.  Note that we require the y-parameter here because 
	 * we will not know where the Glove will be at the time of the execution of the Action, and we need that
	 * information at execution time.
	 * 
	 * For example, we build a MoveToAction from y = 0.0 to y = 10.0, but since the Glove will be moving on the 
	 * y-axis as the animation executes, we can't use getY().
	 * 
	 * Therefore, we just specify the location of the Glove at the execution time of this method.
	 * 
	 * @param x The x-coordinate of the MoveToAction.
	 * @param y The y-coordinate of the Glove.
	 * @return A MoveToAction built for moving the glove back into it's holster.
	 */
	private com.badlogic.gdx.scenes.scene2d.Action buildMoveUpAction(float x, float y) {
		
		RunnableAction stateMutatorUp = new RunnableAction();
		
		stateMutatorUp.setRunnable(new Runnable() {
			
			@Override
			public void run() {
				
				Glove.this.setWillCollide(true);
				Glove.this.setCachedX(-1.0f);
				Glove.this.setActorState(STATE_MOVING_UP);
			}
		});
		
		MoveToAction moveUpTranslate = new MoveToAction();
		
		moveUpTranslate.setX(x - (getWidth() / 2.0f));
		moveUpTranslate.setY(BTNGameScreen.GAME_HEIGHT - GLOVE_HEIGHT_OFFSET);
		moveUpTranslate.setDuration(Math.abs((BTNGameScreen.GAME_HEIGHT - GLOVE_HEIGHT_OFFSET) - y) / GLOVE_VELOCITY_Y_UP);
		
		RunnableAction stateMutatorStatic = new RunnableAction();
		
		stateMutatorStatic.setRunnable(new Runnable() {
			
			public void run() {
				
				Glove.this.setActorState(STATE_STATIC);
			}
		});
		
		SequenceAction output = new SequenceAction();
		
		output.addAction(stateMutatorUp);
		output.addAction(moveUpTranslate);
		output.addAction(stateMutatorStatic);
		
		return output;
	}

	private synchronized void setActorState(int stateStatic) {
		
		this.actorState = stateStatic;
	}

	public int getActorState() {
		
		return actorState;
	}

	private com.badlogic.gdx.scenes.scene2d.Action buildMoveDownAction(final float x, float y) {
		
		RunnableAction stateMutatorDown = new RunnableAction();
		
		stateMutatorDown.setRunnable(new Runnable() {
			
			@Override
			public void run() {
				
				Glove.this.setActorState(STATE_MOVING_DOWN);
			}
		});
		
		MoveToAction moveDownTranslate = new MoveToAction();
		
		moveDownTranslate.setX(x - (getWidth() / 2.0f));
		moveDownTranslate.setY(0.0f);
		moveDownTranslate.setInterpolation(Interpolation.pow3);
		
		// We're always moving to the bottom of the screen.
		moveDownTranslate.setDuration(Math.abs(y - 0.0f) / GLOVE_VELOCITY_Y_DOWN);
		
		SequenceAction output = new SequenceAction();
		
		output.addAction(stateMutatorDown);
		output.addAction(moveDownTranslate);
		
		return output;
	}

	private void setCachedX(float x) {
		
		this.cachedX = x;
	}

	private com.badlogic.gdx.scenes.scene2d.Action buildTranslateXAction(float x) {
		
		float effectiveX = x - (getWidth() / 2.0f);
		
		MoveToAction translateX = new MoveToAction();
		
		translateX.setX(effectiveX);
		translateX.setY(BTNGameScreen.GAME_HEIGHT - GLOVE_HEIGHT_OFFSET);
		translateX.setDuration(Math.abs((effectiveX - this.getX()) / GLOVE_VELOCTY_X));
		
		return translateX;
	}

	/**
	 * @return the currentAction
	 */
	public Action getCurrentAction() {
		
		return currentAction;
	}
	
	public void injectNewAction(Action a){
		
//		this.currentAction = a;
//		
//		MoveToAction translateX = new MoveToAction();
//		translateX.setX(a.getEventX() - getWidth() / 2.0f);
//		translateX.setY(getY());
//		translateX.setDuration(Math.abs((a.getEventX() - 2.0f) - getX()) / GLOVE_VELOCTY_X);
//		
//		MoveToAction moveDown = new MoveToAction();
//		moveDown.setX(a.getEventX() - getWidth() / 2.0f);
//		moveDown.setY(0.0f);
//		moveDown.setDuration(Math.abs((getY() - 0.0f)) / GLOVE_VELOCITY_Y_DOWN);
//		
//		MoveToAction moveUp = new MoveToAction();
//		moveUp.setX(a.getEventX() - getWidth() / 2.0f);
//		moveUp.setY(getY());
//		moveUp.setDuration(Math.abs((getY() - 0.0f)) / GLOVE_VELOCITY_Y_UP);
//		
//		RunnableAction notifyConsume = new RunnableAction();
//		notifyConsume.setRunnable(new Runnable() {
//			
//			@Override
//			public void run() {
//				
//				Glove.this.currentAction = null;
//			}
//		});
//		
//		SequenceAction sequence = new SequenceAction();
//		sequence.addAction(translateX);
//		sequence.addAction(moveDown);
//		sequence.addAction(moveUp);
//		sequence.addAction(notifyConsume);
//		
//		this.addAction(sequence);
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
		
		this.clearActions();
		
		this.setWillCollide(false);
		
		SequenceAction sequence = new SequenceAction();
		
		sequence.addAction(buildMoveUpAction(this.getX() + (this.getWidth() / 2.0f), this.getY()));
		
		if(this.getCachedX() >= 0.0f){
			
			sequence.addAction(buildTranslateXAction(getCachedX()));
			sequence.addAction(buildMoveDownAction(getCachedX(), BTNGameScreen.GAME_HEIGHT - GLOVE_HEIGHT_OFFSET));
			sequence.addAction(buildMoveUpAction(getCachedX(), 0.0f));
		}
		
		// We need to add half the width because the method already accommodates for width, and so does getX().
		// TODO: This functionality needs to be fixed.
		this.addAction(sequence);
	}
	
	public float getCachedX() {
		
		return cachedX;
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

	public boolean willCollide() {
		return willCollide;
	}

	public void setWillCollide(boolean willCollide) {
		this.willCollide = willCollide;
	}
}
