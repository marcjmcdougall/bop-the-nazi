package com.bopthenazi.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.bopthenazi.game.BTNGame;
import com.bopthenazi.utils.Action;
import com.bopthenazi.utils.Collidable;
import com.bopthenazi.utils.Action.ActionType;
import com.bopthenazi.views.screens.BTNGameScreen;
import com.bopthenazi.views.screens.BTNMenuScreen;

public class BTNStage extends Stage {

	private BTNGame game;
	private BTNGameScreen screen;
	
	public BTNStage(BTNGame game){

		this.game = game;
	}
	
	public BTNStage(FitViewport viewport, BTNGame game, BTNGameScreen screen) {
	
		super(viewport);
		
		this.game = game;
		this.screen = screen;
	}

	@Override
	public void draw() {
		
		super.draw();
	}
	
	@Override
	public void act(float delta) {
		
		super.act(delta);
		
		for(Actor a : getActors()){
			
			a.setX(a.getX());
			a.setY(a.getY());
		}
		
		for(Container container : screen.getContainers()){
			
			BTNContainedActor contents = container.getContents();
			Glove glove = screen.getGlove();
			
			if(glove.getRect().overlaps(contents.getRect()) && contents.canCollide() && glove.canCollide()){
				
				screen.notifyCollision(contents);
			}
		}
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		
		Vector2 coords = new Vector2(screenX, screenY);
		
		Vector2 screenCoords = screenToStageCoordinates(coords);
		
		Action a = new Action(screenCoords.x, screenCoords.y, ActionType.TOUCH_DOWN);
		
//		screen.addActionToHandlerQueue(a);
		
		screen.notifyNewX(screenCoords.x);
		
		return super.touchDown(screenX, screenY, pointer, button);
	}
	
	@Override
	public boolean keyDown(int keyCode) {
		
		if(keyCode == Keys.BACKSPACE){
			
			game.setScreen(new BTNMenuScreen(game));
		}
		if(keyCode == Keys.P){
			
			screen.printDebug();
		}
		if(keyCode == Keys.G){
			
			screen.generate();
		}
		if(keyCode == Keys.E){
			
			screen.doEndGame();
		}
		
		return super.keyDown(keyCode);
	}
}
