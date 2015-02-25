package com.bopthenazi.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bopthenazi.views.screens.BTNMenuScreen;
import com.bopthenazi.views.screens.SplashScreen;

public class BTNGame extends Game {
	
	public static final String TAG = "BTNGame";
	
	private SpriteBatch batch;
	
	@Override
	public void create() {
		
		this.setScreen(new SplashScreen(this));
	}

	@Override
	public void render() {
		
		super.render();
	}
	
	/*
	 * ====================
	 * Getters and Setters
	 * ====================
	 */
	
	/**
	 * @return the batch
	 */
	public SpriteBatch getBatch() {
		
		return batch;
	}

	/**
	 * @param batch the batch to set
	 */
	public void setBatch(SpriteBatch batch) {
		
		this.batch = batch;
	}
}
