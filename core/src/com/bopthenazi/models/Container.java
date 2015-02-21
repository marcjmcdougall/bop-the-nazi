package com.bopthenazi.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.bopthenazi.views.screens.BTNGameScreen;

public class Container extends Group{

	private static final float HOLE_WIDTH = 229.5f;
	private static final float HOLE_FRONT_HEIGHT = 575.0f;
	private static final float HOLE_BACK_HEIGHT = 135.2f;
	
	private BTNContainedActor contents;

	private BTNActor holeFront;
	private BTNActor holeBack;
	
	private BTNActor minusHeart;
	
	public Container(float x, float y, BTNGameScreen screen){

		this.holeFront = new BTNActor(screen.getTexture("screen-game/container/tunnel-front.png").getTexture(), x, y + 55.0f, HOLE_WIDTH, HOLE_FRONT_HEIGHT);
		this.holeBack = new BTNActor(screen.getTexture("screen-game/container/tunnel-back.png").getTexture(), x, y + 256.5f, HOLE_WIDTH, HOLE_BACK_HEIGHT);
		
		// By default, we will place a Nazi in each container.
		this.contents = new Zombie(x, y, screen, this);
		
		this.minusHeart = new BTNActor(screen.getTexture("screen-game/container/heart-minus.png").getTexture(), x, y + 200.0f, HOLE_WIDTH, HOLE_WIDTH * 2.0f);
		
		initializeContents();
		
		this.addActor(holeBack);
		this.addActor(contents);
		this.addActor(minusHeart);
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
		contents.setY(holeFront.getY() - 55.0f);
		
		contents.setAnchorY(holeFront.getY() - 55.0f);
	}
	
	public void animateHeart(){
		
		this.minusHeart.addAction(Actions.fadeOut(1.0f));
		this.minusHeart.addAction(Actions.sequence(Actions.moveBy(0.0f, 400.0f, 1.0f, Interpolation.linear), Actions.moveBy(0.0f, -400.0f), Actions.fadeIn(0.0f)));
	}
	
	public BTNActor getMinusHeart(){
		
		return minusHeart;
	}
}
