package com.bopthenazi.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.bopthenazi.views.screens.BTNGameScreen;

public class Container extends Group{

	private static final float HOLE_WIDTH = 229.5f;
	private static final float HOLE_FRONT_HEIGHT = 575.0f;
	private static final float HOLE_BACK_HEIGHT = 135.2f;
	
	private BTNContainedActor contents;

	private BTNActor holeFront;
	private BTNActor holeBack;
	
	public Container(float x, float y, BTNGameScreen screen){

		this.holeFront = new BTNActor(new Texture("hole-front.png"), x, y, HOLE_WIDTH, HOLE_FRONT_HEIGHT);
		this.holeBack = new BTNActor(new Texture("hole-back.png"), x, y + 256.5f, HOLE_WIDTH, HOLE_BACK_HEIGHT);
		
		// By default, we will place a Nazi in each container.
		this.contents = new Zombie(x, y, screen);
		
		initializeContents();
		
		this.addActor(holeBack);
		this.addActor(contents);
		this.addActor(holeFront);
	}
	
	public void setContents(BTNContainedActor contents) {
		
		this.contents = contents;
		
		this.removeActor(this.contents);
		this.addActorAt(1, contents);
		
		initializeContents();
	}

	public BTNContainedActor getContents(){

		return contents;
	}
	
	private void initializeContents(){
		
		contents.setX(holeFront.getX());
		contents.setY(holeFront.getY());
		
		contents.setAnchorY(holeFront.getY());
	}
}
