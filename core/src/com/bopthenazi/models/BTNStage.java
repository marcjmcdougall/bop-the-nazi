package com.bopthenazi.models;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bopthenazi.game.BTNGame;
import com.bopthenazi.views.screens.BTNGameScreen;
import com.bopthenazi.views.screens.BTNMenuScreen;

public class BTNStage extends Stage {

	private static final boolean DEBUG_NO_COLLIDE = false;
	
	private BTNGame game;
	private BTNGameScreen screen;
	
	public BTNStage(BTNGame game){

		this.game = game;
	}
	
	public BTNStage(Viewport viewport, BTNGame game, BTNGameScreen screen) {
	
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
		
		Array<Container> containers = screen.getContainers();
		
		for(int i = 0; i < containers.size; i++){
			
			BTNContainedActor contents = containers.get(i).getContents();
			Glove glove = screen.getGlove();
			
			if(glove.getRect().overlaps(contents.getRect()) && contents.canCollide() && glove.canCollide() && !DEBUG_NO_COLLIDE){
				
				screen.notifyCollision(contents);
			}
		}
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		
		Vector2 coords = new Vector2(screenX, screenY);
		
		Vector2 screenCoords = screenToStageCoordinates(coords);
		
		screen.notifyNewX(screenCoords.x);
		
		return super.touchDown(screenX, screenY, pointer, button);
	}
	
	@Override
	public boolean keyDown(int keyCode) {
		
		if(keyCode == Keys.BACKSPACE || keyCode == Keys.BACK){
			
			game.setScreen(new BTNMenuScreen(game));
		}
		if(keyCode == Keys.P || keyCode == Keys.ESCAPE){
			
			screen.showPauseScreen();
		}
		if(keyCode == Keys.G){
			
			screen.doScreenShake();
		}
		if(keyCode == Keys.E){
			
			screen.doEndGame();
		}
		if(keyCode == Keys.R){
			
			screen.resetScore();
		}
		if(keyCode == Keys.A){
			
			screen.addLife();
		}
		if(keyCode == Keys.X){
			
			screen.setMode(BTNGameScreen.MODE_APOCALYPSE);
		}
		
		return super.keyDown(keyCode);
	}
}
