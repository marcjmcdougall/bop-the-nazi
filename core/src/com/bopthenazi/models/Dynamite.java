package com.bopthenazi.models;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.bopthenazi.utils.Collidable;
import com.bopthenazi.utils.SoundManager;
import com.bopthenazi.views.screens.BTNGameScreen;

public class Dynamite extends BTNContainedActor {

	public Dynamite(float x, float y, BTNGameScreen gameScreen, Container container){
		
		super(new Array<TextureRegion>(new TextureRegion[]{gameScreen.getTexture("screen-game/dynamite/dynamite-01.png"), gameScreen.getTexture("screen-game/dynamite/dynamite-02.png"), gameScreen.getTexture("screen-game/dynamite/dynamite-03.png"), 
				gameScreen.getTexture("screen-game/dynamite/dynamite-04.png"), gameScreen.getTexture("screen-game/dynamite/dynamite-05.png")}), x, y, gameScreen, container);
	}
	
	@Override
	public void onCollide(Collidable partner) {
		
		super.onCollide(partner);
		
		gameScreen.getSoundManager().playSound(SoundManager.SOUND_ID_EXPLOSION);
		
		gameScreen.doExplosionSplash();
	}
}
