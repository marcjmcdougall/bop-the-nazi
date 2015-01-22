package com.bopthenazi.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.bopthenazi.game.BTNGame;
import com.bopthenazi.views.screens.BTNMenuScreen;

public class BTNStage extends Stage {

	private BTNGame game;
	
	public BTNStage(BTNGame game){

		this.game = game;
	}
	
	public BTNStage(FitViewport viewport, BTNGame game) {
	
		super(viewport);
		
		this.game = game;
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
						
						if(((Nazi) nazi).getRect().overlaps(((Glove) a).getRect())){
							
							Gdx.app.log(BTNGame.TAG, "Collision detected!");
							
							if(!((Nazi) nazi).isHiding()){
								
								((Glove) a).notifyCollide();
								((Nazi) nazi).notifyCollide();
							}
						}
					}
				}
				
				break;
			}
		}
	}
	
	@Override
	public boolean keyDown(int keyCode) {
		
		if(keyCode == Keys.BACK){
			
			game.setScreen(new BTNMenuScreen(game));
		}
		
		return super.keyDown(keyCode);
	}
}
