package com.bopthenazi.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.bopthenazi.game.BTNGame;
import com.bopthenazi.utils.Action;
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
	public void act(float delta) {
		
		super.act(delta);
		
		for(Actor a : getActors()){
			
			a.setX(a.getX());
			a.setY(a.getY());
			
			if(a instanceof Glove){
				
//				Gdx.app.log(BTNGame.TAG, "Glove Coords: (" + (((Glove) a).getRect().getX()) + ", " + ((BTNCollideableActor) a).getRect().getY() + ")");
				
				for(Actor nazi : getActors()){
					
					if(nazi instanceof Nazi){
						
						if(((Nazi) nazi).getRect().overlaps(((Glove) a).getRect()) && !((Nazi) nazi).isHiding() && ((Glove) a).getVelocityY() < 0){
							
							Gdx.app.log(BTNGame.TAG, "Collision detected!");
							
							if(!((Nazi) nazi).isHiding()){
								
								screen.onGloveCollision((Nazi) nazi);
							}
						}
					}
				}
				
				break;
			}
		}
	}
	
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		
		screen.notifyTouchUp();
		
		return super.touchUp(screenX, screenY, pointer, button);
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		
		Vector2 coords = new Vector2(screenX, screenY);
		
		Vector2 screenCoords = screenToStageCoordinates(coords);
		
		screen.notifyNewX(screenCoords.x);
		
		Action a = new Action(screenCoords.x, screenCoords.y, ActionType.TOUCH_DOWN);
		
		screen.addActionToHandlerQueue(a);
		
		return super.touchDown(screenX, screenY, pointer, button);
	}
	
	@Override
	public boolean keyDown(int keyCode) {
		
		if(keyCode == Keys.BACKSPACE){
			
			game.setScreen(new BTNMenuScreen(game));
		}
		
		return super.keyDown(keyCode);
	}
}
