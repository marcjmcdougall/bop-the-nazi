package com.bopthenazi.models;

import com.badlogic.gdx.graphics.Texture;
import com.bopthenazi.utils.Collidable;
import com.bopthenazi.views.screens.BTNGameScreen;

public class Bunny extends BTNContainedActor {

	public Bunny(float x, float y, BTNGameScreen gameScreen) {
	
		super(new Texture("bunny.png"), x, y, gameScreen);
	}
	
	@Override
	public void onCollide(Collidable partner) {
		
		super.onCollide(partner);
		
		gameScreen.playSound(BTNGameScreen.SOUND_ID_PUNCH);
		gameScreen.playSound(BTNGameScreen.SOUND_ID_BUNNY_DEATH);
		gameScreen.subtractLife();
	}
}
