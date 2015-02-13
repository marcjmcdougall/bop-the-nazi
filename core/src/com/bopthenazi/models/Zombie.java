package com.bopthenazi.models;

import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.bopthenazi.utils.Collidable;
import com.bopthenazi.views.screens.BTNGameScreen;

public class Zombie extends BTNContainedActor {

	public static final float NAZI_WIDTH = 200.0f;
	public static final float NAZI_HEIGHT = 388.8f;
	
	private BTNGameScreen gameScreen;
	
	public Zombie(float x, float y, BTNGameScreen screen){
		
		super(new Random().nextInt(2) == 1 ? new Texture("zombie.png") : new Texture("zombie-2.png"), x, y);
		
		this.gameScreen = screen;
		
		this.initialize();
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

	@Override
	public void activate() {
		
		super.activate();
		
		// TODO: Implementation.
	}

	@Override
	public void deactivate() {
		
		super.deactivate();
		
		gameScreen.notifyZombieDeactivate(this);
	}
}
