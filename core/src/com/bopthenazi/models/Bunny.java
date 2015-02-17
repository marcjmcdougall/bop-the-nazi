package com.bopthenazi.models;

import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.bopthenazi.utils.Collidable;
import com.bopthenazi.views.screens.BTNGameScreen;

public class Bunny extends BTNContainedActor {

	private static final int BUNNY_TYPE_BROWN = 0;
	private static final int BUNNY_TYPE_BLUE = 1;
	
	private Texture onHitTexture;
	
	public Bunny(float x, float y, BTNGameScreen gameScreen) {
	
		super(gameScreen.getTexture("screen-game/bunny/bunny.png"), x, y, gameScreen);
		
		initializeBunnyTextures();
	}
	
	@Override
	public void onCollide(Collidable partner) {
		
		super.onCollide(partner);
		
		gameScreen.playSound(BTNGameScreen.SOUND_ID_PUNCH);
		gameScreen.playSound(BTNGameScreen.SOUND_ID_BUNNY_DEATH);
		gameScreen.subtractLife();
		
		this.setTextures(new Array<Texture>(new Texture[]{onHitTexture}));
	}
	
	private void initializeBunnyTextures(){
		
		int bunnyType = new Random().nextInt(2);
		
		switch(bunnyType){
		
			case BUNNY_TYPE_BLUE :{
				
				this.setTextures(new Array<Texture>(new Texture[]{gameScreen.getTexture("screen-game/bunny/bunny.png")/*, new Texture("zombie-2.png"), new Texture("zombie-2.png"), new Texture("zombie2-hit-frame.png")*/}));
				this.onHitTexture = gameScreen.getTexture("screen-game/bunny/bunny-hit-frame.png");
				
				break;
			}
			case BUNNY_TYPE_BROWN :{
				
				this.setTextures(new Array<Texture>(new Texture[]{gameScreen.getTexture("screen-game/bunny-2/bunny-2.png")/*, new Texture("zombie.png"), new Texture("zombie.png"), new Texture("zombie1-hit-frame.png")*/}));
				this.onHitTexture = gameScreen.getTexture("screen-game/bunny-2/bunny2-hit-frame.png");
				
				break;
			}
			default :{
				
				// Do nothing.
				break;
			}
		}
	}
}
