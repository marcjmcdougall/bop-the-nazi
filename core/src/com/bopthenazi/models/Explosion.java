package com.bopthenazi.models;

import com.badlogic.gdx.graphics.Texture;

public class Explosion extends BTNActor {

	private static final float EXPLOSION_WIDTH = 367.0f;
	private static final float EXPLOSION_HEIGHT = 250.0f;
	
	private float aliveDuration;
	
	public Explosion(float x, float y, float aliveDuration) {
		
		super(new Texture("explosion.png"), x, y, EXPLOSION_WIDTH, EXPLOSION_HEIGHT);
	
		this.aliveDuration = aliveDuration;
	}
	
	@Override
	public void act(float delta) {
		
		if(aliveDuration > 0.0){
			
			aliveDuration -= delta;
		}
		else{
			
			this.remove();
		}
		
		super.act(delta);
	}
}
