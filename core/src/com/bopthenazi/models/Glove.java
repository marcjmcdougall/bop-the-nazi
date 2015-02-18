package com.bopthenazi.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.bopthenazi.utils.Action;
import com.bopthenazi.utils.Collidable;
import com.bopthenazi.views.screens.BTNGameScreen;

public class Glove extends BTNActor {

	public static final float GLOVE_WIDTH = 144.0f;
	public static final float GLOVE_HEIGHT = 1775.25f;
	
	private static final float GLOVE_HEIGHT_OFFSET = 775.0f;
	
	private static final int STATE_STATIC = 1;
	private static final int STATE_MOVING_DOWN = 2;
	private static final int STATE_MOVING_UP = 3;
	
	
	private float velocityX;
	private float velocityY;
	
	private float toX;
	private float toXOffset;
	
	private SequenceAction moveDown;
	private SequenceAction moveUp;
	private MoveToAction translateX;
	
	private RunnableAction stateMutator;
	
	private boolean readyToDrop;
	private boolean dropRequested;

	private BTNGameScreen gameScreen;
	private BTNActor gloveCase;
	
	private volatile float cachedX;
	
	private volatile SequenceAction currentAction;
	private volatile SequenceAction cachedAction;
	
	public static final float GLOVE_UNLOCK_BARRIER = BTNGameScreen.GAME_HEIGHT + BTNGameScreen.GAME_HEIGHT * 0.10f;
	
	private static final float GLOVE_STATIC_VELOCITY_X = 0.0f;
	private static final float GLOVE_STATIC_VELOCITY_Y = 0.0f;
	
//	private static final float GLOVE_VELOCTY_X = 4000.0f;
	
	private static final float GLOVE_VELOCTY_X = 8000.0f;
	
//	private static final float GLOVE_VELOCITY_Y_DOWN = 5000.0f;
//	private static final float GLOVE_VELOCITY_Y_UP = 8000.0f;
	
	private static final float GLOVE_VELOCITY_Y_DOWN = 8000.0f;
	private static final float GLOVE_VELOCITY_Y_UP = 8000.0f;
	
	public Glove(float x, float y, float width, float height, BTNGameScreen game, BTNActor gloveCase) {
		
		super(game.getTexture("screen-game/glove.png"), x, y, width, height);
		
		// Velocity is measured in game-pixels / second.
		this.velocityX = GLOVE_VELOCTY_X;
		this.velocityY = GLOVE_STATIC_VELOCITY_Y;
		
		this.setActorState(STATE_STATIC);
		
		this.toX = getX();
		this.toXOffset = 2500.0f / 100.0f + 20.0f;
		
		this.readyToDrop = true;
		this.dropRequested = false;
		this.setCachedX(-1.0f);
		
		this.gameScreen = game;
		this.gloveCase = gloveCase;
		
		this.setCurrentAction(null);
		this.setCachedAction(null);
		this.setCollide(true);
		
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
		
		if(currentAction == null){
			
			this.setCurrentAction(buildAction(x));
			this.addAction(getCurrentAction());
		}
		else{
			
			this.setCachedAction(buildAction(x));
		}
	}
	
	private synchronized void pushCachedAction(){
		
		this.setCurrentAction(null);
		
		if(this.getCachedAction() != null){
			
			this.setCurrentAction(this.getCachedAction());
			this.addAction(getCurrentAction());
			
			this.setCachedAction(null);
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
	private MoveToAction buildMoveUpAction(float x, float y) {
		
		MoveToAction moveUpTranslate = new MoveToAction();
		
		moveUpTranslate.setX(x);
		moveUpTranslate.setY(GLOVE_UNLOCK_BARRIER);
		
		moveUpTranslate.setDuration(Math.abs((GLOVE_UNLOCK_BARRIER) - y) / GLOVE_VELOCITY_Y_UP);
		
		return moveUpTranslate;
	}

	private MoveToAction buildMoveDownAction(float x) {
		
		MoveToAction moveDownTranslate = new MoveToAction();
		
		moveDownTranslate.setX(x);
		moveDownTranslate.setY(0.0f + BTNGameScreen.GAME_HEIGHT * 0.55f);
		moveDownTranslate.setInterpolation(Interpolation.pow3);
		
		// We're always moving to the bottom of the screen.
		moveDownTranslate.setDuration((GLOVE_UNLOCK_BARRIER) / GLOVE_VELOCITY_Y_DOWN);
		
		return moveDownTranslate;
	}

	private void setCachedX(float x) {
		
		this.cachedX = x;
	}

	private MoveToAction buildTranslateXAction(float x) {
		
		float effectiveX = x;
		
		MoveToAction translateX = new MoveToAction();
		
		translateX.setX(effectiveX);
		translateX.setY(GLOVE_UNLOCK_BARRIER);
		translateX.setDuration(Math.abs((effectiveX - this.getX()) / GLOVE_VELOCTY_X));
		translateX.setInterpolation(Interpolation.pow2);
		
		return translateX;
	}

	private SequenceAction buildAction(float newX){
		
		SequenceAction output = new SequenceAction();
		
		MoveToAction translateX = buildTranslateXAction(newX);
		MoveToAction moveDown = buildMoveDownAction(newX);
		MoveToAction moveUp = buildMoveUpAction(newX, 0.0f);
		
		RunnableAction finish = new RunnableAction();
		finish.setRunnable(new Runnable() {
			
			@Override
			public void run() {
				
				Glove.this.pushCachedAction();
			}
		});
		
		output.addAction(translateX);
		output.addAction(moveDown);
		output.addAction(moveUp);
		output.addAction(finish);
		
		return output;
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

	@Override
	public void onCollide(Collidable partner) {
		
		super.onCollide(partner);
		
		this.clearActions();
		this.setY(this.getY());
		
		this.setCollide(false);
		
		RunnableAction finished = new RunnableAction();
		finished.setRunnable(new Runnable() {
			
			@Override
			public void run() {
				
				Glove.this.setCollide(true);
				Glove.this.pushCachedAction();
			}
		});

		SequenceAction sequence = new SequenceAction();
		
		sequence.addAction(buildMoveUpAction(this.getX(), this.getY()));
		sequence.addAction(finished);
		
		this.addAction(sequence);
	}
	
	public float getCachedX() {
		
		return cachedX;
	}

	public void requestDrop(){
		
		this.dropRequested = true;
	}

	@Override
	public void setX(float x) {
		
		super.setX(x);
		
		if(gloveCase != null){
			
			gloveCase.setX(x);
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

	public SequenceAction getCachedAction() {
		return cachedAction;
	}

	public void setCachedAction(SequenceAction cachedAction) {
		this.cachedAction = cachedAction;
	}

	public void setCurrentAction(SequenceAction currentAction) {
		
		this.currentAction = currentAction;
	}
	
	public SequenceAction getCurrentAction(){
		
		return currentAction;
	}

	public void clearCache() {
		
		this.setCachedAction(null);
	}
}
