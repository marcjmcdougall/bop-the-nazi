package com.bopthenazi.models;

import com.badlogic.gdx.graphics.Texture;
import com.bopthenazi.utils.Collidable;
import com.bopthenazi.views.screens.BTNGameScreen;

public class ZombieBunny extends BTNContainedActor {

	public ZombieBunny(float x, float y, BTNGameScreen gameScreen) {
		
		super(new Texture("zombie-bunny.png"), x, y, gameScreen);
	}
	
	@Override
	public void onCollide(Collidable partner) {
		
		super.onCollide(partner);
		
		gameScreen.playSound(BTNGameScreen.SOUND_ID_PUNCH);
		gameScreen.playSound(BTNGameScreen.SOUND_ID_ZOMBIE_DEATH);
		gameScreen.incrementScore();
	}
}
