package com.bopthenazi.models;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.bopthenazi.utils.Collidable;
import com.bopthenazi.utils.SoundManager;
import com.bopthenazi.views.screens.BTNGameScreen;

public class Dynamite extends BTNContainedActor {

	public Dynamite(float x, float y, BTNGameScreen gameScreen, Container container){
		
		super(new Array<TextureRegion>(new TextureRegion[]{gameScreen.getTexture("dynamite-01"), gameScreen.getTexture("dynamite-02"), gameScreen.getTexture("dynamite-03"), 
				gameScreen.getTexture("dynamite-04"), gameScreen.getTexture("dynamite-05")}), x, y, gameScreen, container);
	}
	
	@Override
	public void onCollide(Collidable partner) {
		
		super.onCollide(partner);
		
		gameScreen.getSoundManager().playSound(SoundManager.SOUND_ID_EXPLOSION);
		
		gameScreen.doExplosionSplash();
		gameScreen.doScreenShake();
	}
}
