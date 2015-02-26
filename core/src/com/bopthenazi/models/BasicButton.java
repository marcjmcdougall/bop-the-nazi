package com.bopthenazi.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.bopthenazi.game.BTNGame;

public class BasicButton extends Actor {

	private TextureRegion unclicked;
	private TextureRegion clicked;
	
	private boolean isClicked;
	
	private Sound downClick;
	private Sound upClick;
	
	public BasicButton(TextureRegion unclicked, TextureRegion clicked, float x, float y){
		
		this(unclicked, clicked, x, y, unclicked.getRegionWidth(), unclicked.getRegionHeight());
	}
	
	public BasicButton(TextureRegion unclicked, TextureRegion clicked, float x, float y, float width, float height){
		
		this.setWidth(width);
		this.setHeight(height);
		
		this.setX(x - this.getWidth() / 2.0f);
		this.setY(y - this.getHeight() / 2.0f);
		
		this.isClicked = false;
		
		this.unclicked = unclicked;
		this.clicked = clicked;
		
		this.addListener(new InputListener(){

			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				
				super.touchDown(event, x, y, pointer, button);
				
				BasicButton.this.setClicked(true);
				
				return true;
			}
			
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				
				super.touchUp(event, x, y, pointer, button);
				
				BasicButton.this.setClicked(false);
			}
		});
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		super.draw(batch, parentAlpha);
		
		batch.setColor(this.getColor().a, this.getColor().g, this.getColor().b, this.getColor().a * parentAlpha);
				
		if(isClicked){
		
			batch.draw(clicked, this.getX()/* - (this.getWidth() / 2.0f)*/, (this.getY()/* - 133.5f*/)/* - (this.getHeight() / 2.0f)*/, this.getOriginX(), this.getOriginY(), this.getWidth(), this.getHeight(), this.getScaleX(), this.getScaleY(), this.getRotation());
		}
		else{
			
			batch.draw(unclicked, this.getX()/* - (this.getWidth() / 2.0f)*/, (this.getY()/* - 133.5f*/)/* - (this.getHeight() / 2.0f)*/, this.getOriginX(), this.getOriginY(), this.getWidth(), this.getHeight(), this.getScaleX(), this.getScaleY(), this.getRotation());
		}
	}

	public boolean isClicked() {
		
		return isClicked;
	}

	public void setClicked(boolean isClicked) {
		
		this.isClicked = isClicked;
	}
}