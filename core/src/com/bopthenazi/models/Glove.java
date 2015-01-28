package com.bopthenazi.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.bopthenazi.utils.BTNCollideableActor;
import com.bopthenazi.views.screens.BTNGameScreen;

public class Glove extends BTNCollideableActor {

	public static final float GLOVE_WIDTH = 192.0f;
	public static final float GLOVE_HEIGHT = 2367.0f;
	
	private BTNGameScreen gameScreen;
	
	public Glove(float x, float y, float width, float height, BTNGameScreen game) {
		
		super(new Texture("glove.png"), x, y, width, height);
		
		this.gameScreen = game;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		super.draw(batch, parentAlpha);
	}

	public void notifyCollide() {
		
		this.clearActions();
		
		MoveToAction moveToAction = new MoveToAction();
		moveToAction.setPosition(getX(), 1163.1667f);
		moveToAction.setDuration(1.0f);
		moveToAction.setInterpolation(Interpolation.linear);
		
		RunnableAction unlock = new RunnableAction();
		unlock.setRunnable(new Runnable() {
			
			@Override
			public void run() {
				
				gameScreen.getSliderButton().unlock();
			}
		});
		
		SequenceAction sequence = new SequenceAction(moveToAction, unlock);
		
		this.addAction(sequence);
	}
}
