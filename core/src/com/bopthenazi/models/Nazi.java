package com.bopthenazi.models;

import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.bopthenazi.utils.BTNCollideableActor;
import com.bopthenazi.views.screens.BTNGameScreen;

public class Nazi extends BTNCollideableActor {

	private static final float OSCILLATION_DELTA = 300.0f;
	
	public static final float NAZI_WIDTH = 200.0f;
	public static final float NAZI_HEIGHT = 388.8f;
	
	private boolean isHiding;
	private boolean isActivated;
	
	private float startingY;
	
	private BTNGameScreen gameScreen;
	private SequenceAction oscillateSequence;
	
	public Nazi(float x, float y, BTNGameScreen screen){
		
		super(new Random().nextInt(2) == 1 ? new Texture("zombie.png") : new Texture("zombie-2.png"), x, y, NAZI_WIDTH, NAZI_HEIGHT);
		
		this.startingY = getY();
		this.gameScreen = screen;
		
		setHiding(true);
		setActivated(false);
	}

	public void onCollide() {
		
		setHiding(true);
		
		this.clearActions();
		
		MoveToAction moveDown = new MoveToAction();
		
		moveDown.setPosition(getX(), getY() - getHeight());
		moveDown.setDuration(0.25f);
		moveDown.setInterpolation(Interpolation.linear);
		
		this.addAction(moveDown);
		
		performNaziDeactivate(true);
	}

	public void setHiding(boolean newHidingState){
		
		this.isHiding = newHidingState;
	}
	
	public boolean isHiding() {
		
		return isHiding;
	}
	
	/**
	 * @return the isActivated
	 */
	public boolean isActivated() {
		
		return isActivated;
	}

	/**
	 * @param isActivated the isActivated to set
	 */
	public void setActivated(boolean isActivated) {
		
		this.isActivated = isActivated;
	}

	public void prepareAnimation() {
		
		DelayAction initialDelay = new DelayAction((float) (Math.random() * 2.0f));
		
		RunnableAction notifyUp = new RunnableAction();
		notifyUp.setRunnable(new Runnable() {

			@Override
			public void run() {
				
				Nazi.this.setHiding(false);
			}
		});
		
		MoveToAction moveUp = new MoveToAction();
		moveUp.setPosition(this.getX(), startingY + OSCILLATION_DELTA);
		moveUp.setDuration(0.5f);
		moveUp.setInterpolation(Interpolation.linear);

		DelayAction delay = new DelayAction(1.0f);

		MoveToAction moveDown = new MoveToAction();
		moveDown.setPosition(this.getX(), startingY);
		moveDown.setDuration(0.5f);
		moveDown.setInterpolation(Interpolation.linear);
		
		RunnableAction notifyDown = new RunnableAction();
		notifyDown.setRunnable(new Runnable() {

			@Override
			public void run() {
				
				Nazi.this.setHiding(true);
				performNaziDeactivate(false);
			}
		});

		DelayAction delay2 = new DelayAction(2.0f);
		
		oscillateSequence = new SequenceAction();
		oscillateSequence.addAction(initialDelay);
		oscillateSequence.addAction(notifyUp);
		oscillateSequence.addAction(moveUp);
		oscillateSequence.addAction(delay);
		oscillateSequence.addAction(moveDown);
		oscillateSequence.addAction(notifyDown);
		oscillateSequence.addAction(delay2);
	}
	
	private void performNaziDeactivate(boolean hit) {
		
		this.setActivated(false);
		gameScreen.notifyNaziDeactivate(hit);
	}
	
	public void performNaziActivate(){
		
		this.prepareAnimation();
		this.addAction(oscillateSequence);
		this.setActivated(true);
	}
}
