package com.bopthenazi.models;

import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.bopthenazi.utils.Collidable;
import com.bopthenazi.views.screens.BTNGameScreen;

public class Zombie extends BTNContainedActor {

	private static final float OSCILLATION_DELTA = 300.0f;
	
	public static final float NAZI_WIDTH = 200.0f;
	public static final float NAZI_HEIGHT = 388.8f;
	
	private float startingY;
	
	private BTNGameScreen gameScreen;
	private SequenceAction oscillateSequence;
	
	public Zombie(float x, float y, BTNGameScreen screen){
		
		super(new Random().nextInt(2) == 1 ? new Texture("zombie.png") : new Texture("zombie-2.png"), x, y);
		
		this.startingY = getY();
		this.gameScreen = screen;
		
		this.initialize();
	}

	@Override
	public void onCollide(Collidable partner) {
		
		setActorState(STATE_HIT);
		
		this.clearActions();
		
		MoveToAction moveDown = new MoveToAction();
		
		moveDown.setPosition(getX(), getY() - getHeight());
		moveDown.setDuration(0.25f);
		moveDown.setInterpolation(Interpolation.linear);
		
		this.addAction(moveDown);
		
		deactivate();
	}

	@Override
	public void activate() {
		
		super.activate();
		
		this.prepare();
		this.addAction(oscillateSequence);
		this.setActivated(true);
	}

	@Override
	public void deactivate() {
		
		super.deactivate();
		
		gameScreen.notifyZombieDeactivate(this);
	}
	
	@Override
	public void prepare() {
		
		DelayAction initialDelay = new DelayAction((float) (Math.random() * 2.0f));
		
		RunnableAction notifyUp = new RunnableAction();
		notifyUp.setRunnable(new Runnable() {

			@Override
			public void run() {
				
				Zombie.this.setActorState(STATE_VISIBLE);
			}
		});
		
		MoveToAction moveUp = new MoveToAction();
		moveUp.setPosition(this.getX(), startingY + OSCILLATION_DELTA);
		moveUp.setDuration(0.5f);
		moveUp.setInterpolation(Interpolation.exp5);

		DelayAction delay = new DelayAction(1.0f);

		MoveToAction moveDown = new MoveToAction();
		moveDown.setPosition(this.getX(), startingY);
		moveDown.setDuration(0.5f);
		moveDown.setInterpolation(Interpolation.linear);
		
		RunnableAction notifyDown = new RunnableAction();
		notifyDown.setRunnable(new Runnable() {

			@Override
			public void run() {
				
				Zombie.this.setActorState(STATE_HIDING);
				deactivate();
			}
		});

		DelayAction delay2 = new DelayAction(2.0f);
		
		oscillateSequence = new SequenceAction();
		oscillateSequence.addAction(initialDelay);
		oscillateSequence.addAction(moveUp);
		oscillateSequence.addAction(notifyUp);
		oscillateSequence.addAction(delay);
		oscillateSequence.addAction(moveDown);
		oscillateSequence.addAction(notifyDown);
		oscillateSequence.addAction(delay2);
	}
}
