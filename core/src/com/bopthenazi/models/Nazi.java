package com.bopthenazi.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.bopthenazi.utils.BTNCollideableActor;
import com.bopthenazi.views.screens.BTNGameScreen;

public class Nazi extends BTNCollideableActor {

	public static final float NAZI_WIDTH = 200.0f;
	public static final float NAZI_HEIGHT = 388.8f;
	
	public Nazi(float x, float y){
		
		super(new Texture("nazi.png"), x, y, NAZI_WIDTH, NAZI_HEIGHT);
	}

	public void notifyCollide() {
		
		// TODO: Unfinished.
		this.clearActions();
		
		MoveToAction moveDown = new MoveToAction();
		
		moveDown.setPosition(getX(), getY() - getHeight());
		moveDown.setDuration(0.25f);
		moveDown.setInterpolation(Interpolation.linear);
		
		
		// TODO: This should be in the GAMESCREEN method.
//		BTNActor explosion = new BTNActor(new Texture("explosion.png"), getX(), getY() + NAZI_WIDTH / 2.0, 367.0f, 194.0f);
		
		this.addAction(moveDown);
	}

	public boolean isHiding() {
		// TODO Auto-generated method stub
		return false;
	}
}
