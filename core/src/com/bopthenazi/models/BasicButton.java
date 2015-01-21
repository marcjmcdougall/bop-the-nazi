package com.bopthenazi.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.bopthenazi.game.BTNGame;

public class BasicButton extends Actor {

	private Texture unclicked;
	private Texture clicked;
	
	private boolean isClicked;
	
	public BasicButton(Texture unclicked, Texture clicked, float x, float y){
		
		this.setWidth(400.0f);
		this.setHeight(206.0f);
		
		this.setX(x - this.getWidth() / 2.0f);
		this.setY(y - this.getHeight() / 2.0f);
		
		this.isClicked = false;
		
		this.unclicked = unclicked;
		this.clicked = clicked;
		
		this.addListener(new InputListener(){

			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				
				super.touchDown(event, x, y, pointer, button);
				
				BasicButton.this.setClicked(true);
				
				Gdx.app.log(BTNGame.TAG, "Touch down received on " + BasicButton.class);
				
				return true;
			}
			
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				
				super.touchUp(event, x, y, pointer, button);
				
				BasicButton.this.setClicked(false);
				
				Gdx.app.log(BTNGame.TAG, "Touch up received on " + BasicButton.class);
			}
		});
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		super.draw(batch, parentAlpha);
		
		if(isClicked){
		
			batch.draw(clicked, getX(), getY(), getWidth(), getHeight());
		}
		else{
			
			batch.draw(unclicked, getX(), getY(), getWidth(), getHeight());
		}
	}

	public boolean isClicked() {
		
		return isClicked;
	}

	public void setClicked(boolean isClicked) {
		
		this.isClicked = isClicked;
	}
}