package com.bopthenazi.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Array;
import com.bopthenazi.game.BTNGame;

public class NaziContainer {

	private static final float OSCILLATION_DELTA = 300.0f;

	private Nazi nazi;

	private BTNActor holeFront;
	private BTNActor holeBack;

	public NaziContainer(float x, float y){

		this.nazi = new Nazi(x, y);
		this.holeFront = new BTNActor(new Texture("hole-front.png"), x, y, Nazi.NAZI_WIDTH + 70.0f, Nazi.NAZI_HEIGHT);
		this.holeBack = new BTNActor(new Texture("hole-back.png"), x, y + 275.0f, Nazi.NAZI_WIDTH + 70.0f, Nazi.NAZI_HEIGHT);

		float originalY = nazi.getY();

		DelayAction initialDelay = new DelayAction((float) (Math.random() * 2.0f));
		
		RunnableAction notifyUp = new RunnableAction();
		notifyUp.setRunnable(new Runnable() {

			@Override
			public void run() {
				
				nazi.setHiding(false);
			}
		});
		
		MoveToAction moveUp = new MoveToAction();
		moveUp.setPosition(nazi.getX(), originalY + OSCILLATION_DELTA);
		moveUp.setDuration(1.0f);
		moveUp.setInterpolation(Interpolation.linear);

		DelayAction delay = new DelayAction(2.0f);

		MoveToAction moveDown = new MoveToAction();
		moveDown.setPosition(nazi.getX(), originalY);
		moveDown.setDuration(1.0f);
		moveDown.setInterpolation(Interpolation.linear);
		
		RunnableAction notifyDown = new RunnableAction();
		notifyDown.setRunnable(new Runnable() {

			@Override
			public void run() {
				
				nazi.setHiding(true);
			}
		});

		DelayAction delay2 = new DelayAction(2.0f);
		
		SequenceAction oscillateSequence = new SequenceAction();
		oscillateSequence.addAction(initialDelay);
		oscillateSequence.addAction(notifyUp);
		oscillateSequence.addAction(moveUp);
		oscillateSequence.addAction(delay);
		oscillateSequence.addAction(moveDown);
		oscillateSequence.addAction(notifyDown);
		oscillateSequence.addAction(delay2);

		RepeatAction repeatOscillate = new RepeatAction();
		repeatOscillate.setAction(oscillateSequence);
		repeatOscillate.setCount(RepeatAction.FOREVER);
		
		this.nazi.addAction(repeatOscillate);
	}
	
	public Array<Actor> getActors(){

		Array<Actor> output = new Array<Actor>();

		output.add(holeBack);
		output.add(nazi);
		output.add(holeFront);

		return output;
	}

	public Nazi getNazi(){

		return nazi;
	}
}
