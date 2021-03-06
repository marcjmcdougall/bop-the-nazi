package com.bopthenazi.models;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.bopthenazi.utils.Collidable;
import com.bopthenazi.utils.SoundManager;
import com.bopthenazi.views.screens.BTNGameScreen;

public class Heart extends BTNContainedActor {
	
	public Heart(float x, float y, BTNGameScreen gameScreen, Container container){
		
		super(new Array<TextureRegion>(new TextureRegion[]{gameScreen.getTexture("heart-contained")}), x, y, gameScreen, container);
	}
	
	@Override
	public void onCollide(Collidable partner) {
		
		super.onCollide(partner);
		
		gameScreen.getSoundManager().playSound(SoundManager.SOUND_ID_PUNCH);
		gameScreen.getSoundManager().playSound(SoundManager.SOUND_ID_POWERUP);
		gameScreen.addLife();
	}
}
