package com.bopthenazi.models;

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
	
//	private static final float CONTENT_WIDTH = 200.0f;
//	private static final float CONTENT_HEIGHT = 388.8f;

	private static final float CONTENT_WIDTH = 180.0f;
	private static final float CONTENT_HEIGHT = 349.0f;
	
	
	protected BTNGameScreen gameScreen;
	private SequenceAction oscillateSequence;
	
	private Container container;
	
	private volatile boolean activated;
	private float anchorY;
	
	public BTNContainedActor() {
		
		// This constructor should never be used for obvious reasons.
		super();
		
		initialize(null, null);
	}
	
	public BTNContainedActor(Array<TextureRegion> textures, float x, float y, BTNGameScreen gameScreen, Container c){
		
		super(textures, x, y, CONTENT_WIDTH, CONTENT_HEIGHT, x, y, CONTENT_WIDTH, CONTENT_HEIGHT);
		
		initialize(gameScreen, c);
	}
	
	public BTNContainedActor(TextureRegion texture, float x, float y, BTNGameScreen gameScreen, Container c){
		
		super(texture, x, y, CONTENT_WIDTH, CONTENT_HEIGHT);
		
		initialize(gameScreen, c);
	}
	
	@Override
	public void activate() {
		
		this.setActivated(true);
		
		this.prepare();
		this.addAction(oscillateSequence);
	}
	
	private void prepare() {
		
		RunnableAction notifyVisible = new RunnableAction();
		
		notifyVisible.setRunnable(new Runnable() {
			
			@Override
			public void run() {
				
				BTNContainedActor.this.setActorState(STATE_VISIBLE);
			}
		});
		
		MoveToAction moveUp = new MoveToAction();
		moveUp.setPosition(this.getX(), this.getAnchorY() + OSCILLATION_DELTA);
		moveUp.setDuration(gameScreen.getDifficultyManager().getLaunchSpeed());
		moveUp.setInterpolation(Interpolation.exp5);

		RunnableAction notifyUp = new RunnableAction();
	
		notifyUp.setRunnable(new Runnable() {
			
			@Override
			public void run() {
				
				BTNContainedActor.this.setCollide(true);
			}
		});
		
		DelayAction delay = new DelayAction(1.0f);

		MoveToAction moveDown = new MoveToAction();
		moveDown.setPosition(this.getX(), this.getAnchorY());
		moveDown.setDuration(gameScreen.getDifficultyManager().getLaunchSpeed());
		moveDown.setInterpolation(Interpolation.linear);
		
		RunnableAction notifyDown = new RunnableAction();
		
		notifyDown.setRunnable(new Runnable() {
			
			@Override
			public void run() {
				
				doContentsDownNormal();
			}
		});
		
		oscillateSequence = new SequenceAction();
		
//		oscillateSequence.addAction(initialDelay);
		oscillateSequence.addAction(notifyVisible);
		oscillateSequence.addAction(moveUp);
		oscillateSequence.addAction(notifyUp);
		oscillateSequence.addAction(delay);
		oscillateSequence.addAction(moveDown);
		oscillateSequence.addAction(notifyDown);
	}
	
	@Override
	public void deactivate() {
		
		this.setCollide(false);
		this.setActivated(false);
		this.gameScreen.notifyDeactivate(this.getContainer());
	}
	
	public Container getContainer() {
		
		return this.container;
	}

	protected void initialize(BTNGameScreen gameScreen, Container c) {
		
		this.setActivated(false);
		this.setActorState(STATE_HIDING);
		
		this.container = c;
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
		moveDown.setDuration(gameScreen.getDifficultyManager().getLaunchSpeed() / 2.0f);
		moveDown.setInterpolation(Interpolation.linear);
		
		this.addAction(moveDown);
		
		deactivate();
	}

	private void doContentsDownNormal() {
		
		BTNContainedActor.this.setActorState(STATE_HIDING);
		
		deactivate();
	}
}
