package com.bopthenazi.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.bopthenazi.game.BTNGame;
import com.bopthenazi.utils.BTNCollideableActor;

public class BTNStage extends Stage {

	public BTNStage(){
	
		// Do nothing.
	}
	
	public BTNStage(FitViewport viewport) {
	
		super(viewport);
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
}
