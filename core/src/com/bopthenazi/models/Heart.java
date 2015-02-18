package com.bopthenazi.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.bopthenazi.utils.Collidable;
import com.bopthenazi.views.screens.BTNGameScreen;

public class Heart extends BTNContainedActor {
	
	public Heart(float x, float y, BTNGameScreen gameScreen){
		
		super(new Array<Texture>(new Texture[]{gameScreen.getTexture("screen-game/heart-contained/heart-contained.png")}), x, y, gameScreen);
	}
	
	@Override
	public void onCollide(Collidable partner) {
		
		super.onCollide(partner);
		
		gameScreen.playSound(BTNGameScreen.SOUND_ID_PUNCH);
		gameScreen.playSound(BTNGameScreen.SOUND_ID_POWERUP);
		gameScreen.addLife();
	}
}
