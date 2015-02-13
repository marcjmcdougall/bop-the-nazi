package com.bopthenazi.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.bopthenazi.utils.Activatable;

public abstract class BTNContainedActor extends BTNActor implements Activatable{

	private static final float OSCILLATION_DELTA = 300.0f;
	
	public static final int STATE_HIDING = 1;
	public static final int STATE_VISIBLE = 2;
	public static final int STATE_HIT = 3;
	
	private static final float CONTENT_WIDTH = 200.0f;
	private static final float CONTENT_HEIGHT = 388.8f;
	
	private volatile boolean activated;
	
	private SequenceAction oscillateSequence;
	
	public BTNContainedActor() {
		
		super();
		
		initialize();
	}
	
	public BTNContainedActor(Texture texture, float x, float y){
		
		super(texture, x, y, CONTENT_WIDTH, CONTENT_HEIGHT);
		
		initialize();
	}
	
	@Override
	public void activate() {
		
		this.prepare();
		this.addAction(oscillateSequence);
		
		this.setActivated(true);
	}
	
	private void prepare() {
		
		DelayAction initialDelay = new DelayAction((float) (Math.random() * 2.0f));
		
		MoveToAction moveUp = new MoveToAction();
		moveUp.setPosition(this.getX(), this.getY() + OSCILLATION_DELTA);
		moveUp.setDuration(0.5f);
		moveUp.setInterpolation(Interpolation.exp5);

		RunnableAction notifyUp = new RunnableAction();
	
		notifyUp.setRunnable(new Runnable() {
			
			@Override
			public void run() {
				
				BTNContainedActor.this.setActorState(STATE_VISIBLE);
			}
		});
		
		DelayAction delay = new DelayAction(1.0f);

		MoveToAction moveDown = new MoveToAction();
		moveDown.setPosition(this.getX(), getY());
		moveDown.setDuration(0.5f);
		moveDown.setInterpolation(Interpolation.linear);
		
		RunnableAction notifyDown = new RunnableAction();
		
		notifyDown.setRunnable(new Runnable() {
			
			@Override
			public void run() {
				
				BTNContainedActor.this.setActorState(STATE_HIDING);
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
	
	@Override
	public void deactivate() {
		
		this.setActivated(false);
	}
	
	protected void initialize() {
		
		this.setActivated(false);
		this.setActorState(STATE_HIDING);
	}
	
	public boolean isActivated(){
		
		return activated;
	}
	
	public void setActivated(boolean activated){
		
		this.activated = activated;
	}
}
