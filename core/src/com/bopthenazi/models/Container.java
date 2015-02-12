package com.bopthenazi.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.bopthenazi.views.screens.BTNGameScreen;

public class Container {

	private Nazi nazi;

	private BTNActor holeFront;
	private BTNActor holeBack;
	
	public Container(float x, float y, BTNGameScreen gameScreen){

		this.nazi = new Nazi(x, y, gameScreen);
		this.holeFront = new BTNActor(new Texture("hole-front.png"), x, y, 229.5f, 575.0f);
		this.holeBack = new BTNActor(new Texture("hole-back.png"), x, y + 256.5f, 229.5f, 135.2f);

		nazi.prepareAnimation();
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
