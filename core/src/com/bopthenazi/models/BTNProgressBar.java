package com.bopthenazi.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class BTNProgressBar extends Actor {

	private Texture backgroundTexture;
	private TextureRegion progressTexture;
	
	private float percentDraw;
	private float effectiveWidth;
	
	public BTNProgressBar(Texture bgTexture, Texture progressTexture, float x, float y, float width, float height){
		
		this.setBackGroundTexture(bgTexture);
		this.setProgressTexture(new TextureRegion(progressTexture));
		
		this.setX(x);
		this.setY(y);
		
		this.setWidth(width);
		this.setHeight(height);
		
		this.setPercentDraw(0.0f);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		batch.setColor(this.getColor().a, this.getColor().g, this.getColor().b, this.getColor().a * parentAlpha);
		
		batch.draw(backgroundTexture, this.getX() - this.getWidth() / 2.0f, this.getY() - this.getHeight() / 2.0f, this.getWidth(), this.getHeight());
//		batch.draw(progressTexture, this.getX() - this.getWidth() / 2.0f, this.getY() - this.getHeight() / 2.0f, this.getEffectiveWidth(), this.getHeight(), Math.round(this.getX()), Math.round(this.getY()), Math.round(this.getWidth()), Math.round(this.getHeight()), false, false);
		batch.draw(progressTexture, this.getX() - this.getEffectiveWidth() / 2.0f, this.getY() - this.getHeight() / 2.0f, this.getEffectiveWidth(), this.getHeight());
	}	
	
	public Texture getBackgroundTexture() {
		
		return backgroundTexture;
	}

	public void setBackGroundTexture(Texture backGroundTexture) {
		
		this.backgroundTexture = backGroundTexture;
	}

	public float getPercentDraw() {
		
		return percentDraw;
	}

	public void setPercentDraw(float percentDraw) {
		
		this.percentDraw = percentDraw;
		this.setEffectiveWidth(this.getWidth() * percentDraw);
	}
	
	public TextureRegion getProgressTexture(){
		
		return progressTexture;
	}
	
	public void setProgressTexture(TextureRegion progressTexture) {
		
		this.progressTexture = progressTexture;
	}

	public float getEffectiveWidth() {
		
		return effectiveWidth;
	}

	public void setEffectiveWidth(float effectiveWidth) {
		
		this.effectiveWidth = effectiveWidth;
	}
}
