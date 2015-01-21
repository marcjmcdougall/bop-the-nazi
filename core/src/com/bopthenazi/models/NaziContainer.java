package com.bopthenazi.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Array;

public class NaziContainer {

	private static final float OSCILLATION_DELTA = 50.0f;
	
	private Nazi nazi;
	
	private BTNActor holeFront;
	private BTNActor holeBack;
	
	public NaziContainer(float x, float y){
		
		this.nazi = new Nazi(x, y /*- Nazi.NAZI_HEIGHT / 2.0f*/);
		this.holeFront = new BTNActor(new Texture("hole-front.png"), x, y, Nazi.NAZI_WIDTH, Nazi.NAZI_HEIGHT);
		this.holeBack = new BTNActor(new Texture("hole-back.png"), x, y + 275.0f, Nazi.NAZI_WIDTH, Nazi.NAZI_HEIGHT);
		
		float originalY = nazi.getY();
		
		MoveToAction moveUp = new MoveToAction();
		moveUp.setPosition(nazi.getX(), originalY + OSCILLATION_DELTA);
		moveUp.setDuration(1.0f);
		moveUp.setInterpolation(Interpolation.swing);
		
		DelayAction delay = new DelayAction(2.0f);
		
		MoveToAction moveDown = new MoveToAction();
		moveDown.setPosition(nazi.getX(), originalY);
		moveDown.setDuration(1.0f);
		moveDown.setInterpolation(Interpolation.exp10);
		
		SequenceAction oscillateSequence = new SequenceAction();
		oscillateSequence.addAction(moveUp);
		oscillateSequence.addAction(delay);
		oscillateSequence.addAction(moveDown);
		
		RepeatAction repeatOscillate = new RepeatAction();
		repeatOscillate.setAction(oscillateSequence);
		
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
