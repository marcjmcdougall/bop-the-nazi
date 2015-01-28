package com.bopthenazi.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.bopthenazi.utils.BTNCollideableActor;
import com.bopthenazi.views.screens.BTNGameScreen;

public class Nazi extends BTNCollideableActor {

	public static final float NAZI_WIDTH = 200.0f;
	public static final float NAZI_HEIGHT = 388.8f;
	
	private boolean isHiding;
	
	public Nazi(float x, float y){
		
		super(new Texture("nazi.png"), x, y, NAZI_WIDTH, NAZI_HEIGHT);
		
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
	}

	public void setHiding(boolean newHidingState){
		
		this.isHiding = newHidingState;
	}
	
	public boolean isHiding() {
		
		return isHiding;
	}
}
