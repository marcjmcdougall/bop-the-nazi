package com.bopthenazi.models;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.bopthenazi.utils.Collidable;
import com.bopthenazi.utils.SoundManager;
import com.bopthenazi.views.screens.BTNGameScreen;

public class Bunny extends BTNContainedActor {

	private static final int BUNNY_TYPE_BROWN = 0;
	private static final int BUNNY_TYPE_BLUE = 1;
	
	private TextureRegion onHitTexture;
	
	public Bunny(float x, float y, BTNGameScreen gameScreen, Container container) {
	
		super(gameScreen.getTexture("bunny"), x, y, gameScreen, container);
		
		initializeBunnyTextures();
	}
	
	@Override
	public void onCollide(Collidable partner) {
		
		super.onCollide(partner);
		
		gameScreen.getSoundManager().playSound(SoundManager.SOUND_ID_PUNCH);
		gameScreen.getSoundManager().playSound(SoundManager.SOUND_ID_BUNNY_DEATH);
		gameScreen.doScreenShake();
		gameScreen.subtractLife();
		
		this.getContainer().animateHeart();
		
		this.setTextures(new Array<TextureRegion>(new TextureRegion[]{onHitTexture}));
	}
	
	private void initializeBunnyTextures(){
		
		int bunnyType = new Random().nextInt(2);
		
		switch(bunnyType){
		
			case BUNNY_TYPE_BLUE :{
				
				// TODO: This is inefficient, fix it!
				this.setTextures(new Array<TextureRegion>(new TextureRegion[]{gameScreen.getTexture("bunny")/*, new Texture("zombie-2.png"), new Texture("zombie-2.png"), new Texture("zombie2-hit-frame.png")*/}));
				this.onHitTexture = gameScreen.getTexture("bunny-hit-frame");
				
				break;
			}
			case BUNNY_TYPE_BROWN :{
				
				this.setTextures(new Array<TextureRegion>(new TextureRegion[]{gameScreen.getTexture("bunny-2")/*, new Texture("zombie.png"), new Texture("zombie.png"), new Texture("zombie1-hit-frame.png")*/}));
				this.onHitTexture = gameScreen.getTexture("bunny2-hit-frame");
				
				break;
			}
			default :{
				
				// Do nothing.
				break;
			}
		}
	}
}
