package com.bopthenazi.models;

import com.badlogic.gdx.graphics.Texture;
import com.bopthenazi.utils.Activatable;

public abstract class BTNContainedActor extends BTNActor implements Activatable{

	public static final int STATE_HIDING = 1;
	public static final int STATE_VISIBLE = 2;
	public static final int STATE_HIT = 3;
	
	private static final float CONTENT_WIDTH = 200.0f;
	private static final float CONTENT_HEIGHT = 388.8f;
	
	private volatile boolean activated;
	
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
		
		this.setActivated(true);
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
