package com.bopthenazi.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Array;
import com.bopthenazi.utils.Activatable;
import com.bopthenazi.utils.Collidable;
import com.bopthenazi.views.screens.BTNGameScreen;

public abstract class BTNContainedActor extends BTNActor implements Activatable{

	private static final float OSCILLATION_DELTA = 300.0f;
	
	public static final int STATE_HIDING = 1;
	public static final int STATE_VISIBLE = 2;
	public static final int STATE_HIT = 3;
	
	private static final float CONTENT_WIDTH = 200.0f;
	private static final float CONTENT_HEIGHT = 388.8f;
	
	protected BTNGameScreen gameScreen;
	private SequenceAction oscillateSequence;
	
	private volatile boolean activated;
	private float anchorY;
	
	public BTNContainedActor() {
		
		// This constructor should never be used for obvious reasons.
		super();
		
		initialize(null);
	}
	
	public BTNContainedActor(Array<TextureRegion> textures, float x, float y, BTNGameScreen gameScreen){
		
		super(textures, x, y, CONTENT_WIDTH, CONTENT_HEIGHT, x, y, CONTENT_WIDTH, CONTENT_HEIGHT);
		
		initialize(gameScreen);
	}
	
	public BTNContainedActor(TextureRegion texture, float x, float y, BTNGameScreen gameScreen){
		
		super(texture.getTexture(), x, y, CONTENT_WIDTH, CONTENT_HEIGHT);
		
		initialize(gameScreen);
	}
	
	@Override
	public void activate() {
		
		System.out.println("BANANA" + gameScreen.isPaused());
		
		this.setActivated(true);
		
		this.prepare();
		this.addAction(oscillateSequence);
	}
	
	private void prepare() {
		
		MoveToAction moveUp = new MoveToAction();
		moveUp.setPosition(this.getX(), this.getAnchorY() + OSCILLATION_DELTA);
		moveUp.setDuration(0.5f);
		moveUp.setInterpolation(Interpolation.exp5);

		RunnableAction notifyUp = new RunnableAction();
	
		notifyUp.setRunnable(new Runnable() {
			
			@Override
			public void run() {
				
				BTNContainedActor.this.setActorState(STATE_VISIBLE);
				BTNContainedActor.this.setCollide(true);
			}
		});
		
		DelayAction delay = new DelayAction(1.0f);

		MoveToAction moveDown = new MoveToAction();
		moveDown.setPosition(this.getX(), this.getAnchorY());
		moveDown.setDuration(0.5f);
		moveDown.setInterpolation(Interpolation.linear);
		
		RunnableAction notifyDown = new RunnableAction();
		
		notifyDown.setRunnable(new Runnable() {
			
			@Override
			public void run() {
				
				doContentsDownNormal();
			}
		});
		
		DelayAction delay2 = new DelayAction(2.0f);
		
		oscillateSequence = new SequenceAction();
		
//		oscillateSequence.addAction(initialDelay);
		oscillateSequence.addAction(moveUp);
		oscillateSequence.addAction(notifyUp);
		oscillateSequence.addAction(delay);
		oscillateSequence.addAction(moveDown);
		oscillateSequence.addAction(notifyDown);
		oscillateSequence.addAction(delay2);
	}
	
	@Override
	public void deactivate() {
		
		this.setCollide(false);
		this.setActivated(false);
		this.gameScreen.notifyDeactivate(this);
	}
	
	protected void initialize(BTNGameScreen gameScreen) {
		
		this.setActivated(false);
		this.setActorState(STATE_HIDING);
		
		this.gameScreen = gameScreen;
	}
	
	public boolean isActivated(){
		
		return activated;
	}
	
	public void setActivated(boolean activated){
		
		this.activated = activated;
	}

	public float getAnchorY() {
		
		return anchorY;
	}

	public void setAnchorY(float anchorY) {
		
		this.anchorY = anchorY;
	}

	public BTNGameScreen getGameScreen() {
		
		return gameScreen;
	}
	
	@Override
	public void onCollide(Collidable partner) {
		
		super.onCollide(partner);
		
		setActorState(STATE_HIT);
		
		this.clearActions();
		
		MoveToAction moveDown = new MoveToAction();
		
		moveDown.setPosition(getX(), getY() - getHeight());
		moveDown.setDuration(0.25f);
		moveDown.setInterpolation(Interpolation.linear);
		
		this.addAction(moveDown);
		
		deactivate();
	}

	private void doContentsDownNormal() {
		
		BTNContainedActor.this.setActorState(STATE_HIDING);
		
		deactivate();
	}
}
