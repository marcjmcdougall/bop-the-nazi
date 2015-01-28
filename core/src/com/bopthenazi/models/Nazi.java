package com.bopthenazi.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.bopthenazi.utils.BTNCollideableActor;
import com.bopthenazi.views.screens.BTNGameScreen;

public class Nazi extends BTNCollideableActor {

	private static final float OSCILLATION_DELTA = 300.0f;
	
	public static final float NAZI_WIDTH = 200.0f;
	public static final float NAZI_HEIGHT = 388.8f;
	
	private boolean isHiding;
	private float startingY;
	
	public Nazi(float x, float y){
		
		super(new Texture("zombie.png"), x, y, NAZI_WIDTH, NAZI_HEIGHT);
		
		this.startingY = getY();
		
		setHiding(true);
	}

	public void onCollide() {
		
		setHiding(true);
		
		this.clearActions();
		
		MoveToAction moveDown = new MoveToAction();
		
		moveDown.setPosition(getX(), getY() - getHeight());
		moveDown.setDuration(0.25f);
		moveDown.setInterpolation(Interpolation.linear);
		
		this.addAction(moveDown);
		
		initializeAnimation();
	}

	public void initializeAnimation() {
		
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
		moveUp.setDuration(1.0f);
		moveUp.setInterpolation(Interpolation.linear);

		DelayAction delay = new DelayAction(2.0f);

		MoveToAction moveDown = new MoveToAction();
		moveDown.setPosition(this.getX(), startingY);
		moveDown.setDuration(1.0f);
		moveDown.setInterpolation(Interpolation.linear);
		
		RunnableAction notifyDown = new RunnableAction();
		notifyDown.setRunnable(new Runnable() {

			@Override
			public void run() {
				
				Nazi.this.setHiding(true);
			}
		});

		DelayAction delay2 = new DelayAction(2.0f);
		
		SequenceAction oscillateSequence = new SequenceAction();
		oscillateSequence.addAction(initialDelay);
		oscillateSequence.addAction(notifyUp);
		oscillateSequence.addAction(moveUp);
		oscillateSequence.addAction(delay);
		oscillateSequence.addAction(moveDown);
		oscillateSequence.addAction(notifyDown);
		oscillateSequence.addAction(delay2);

		RepeatAction repeatOscillate = new RepeatAction();
		repeatOscillate.setAction(oscillateSequence);
		repeatOscillate.setCount(RepeatAction.FOREVER);
		
		this.addAction(repeatOscillate);
	}

	public void setHiding(boolean newHidingState){
		
		this.isHiding = newHidingState;
	}
	
	public boolean isHiding() {
		
		return isHiding;
	}
}
