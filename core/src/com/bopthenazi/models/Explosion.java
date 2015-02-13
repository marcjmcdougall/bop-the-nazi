package com.bopthenazi.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

public class Explosion extends BTNActor {

	private static final float EXPLOSION_WIDTH = 400.0f;
	private static final float EXPLOSION_HEIGHT = 400.0f;
	
	private static final float EXPLOSION_FPS = 20.0f;
	private static final int EXPLOSION_FRAME_COUNT = 3;
	
	private float aliveDuration;
	
	public Explosion(float x, float y) {
		
		this(x, y, false);
	}
	
	public Explosion(float x, float y, boolean expand){
		
		super(new Array<Texture>(new Texture[]{new Texture("explosion-03.png"), new Texture("explosion-02.png"), 
				new Texture("explosion-01.png"), /*new Texture("explosion-04.png")*/}), 
				x, y, EXPLOSION_WIDTH, EXPLOSION_HEIGHT, x, y, EXPLOSION_WIDTH, EXPLOSION_HEIGHT);
	
		this.aliveDuration = (1 / EXPLOSION_FPS) * EXPLOSION_FRAME_COUNT - .05f;
		this.setFps(EXPLOSION_FPS);
		
		if(expand){
			
			this.setWidth(this.getWidth() * 5.0f);
			this.setHeight(this.getHeight() * 5.0f);
		}
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
