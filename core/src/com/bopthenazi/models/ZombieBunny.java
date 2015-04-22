package com.bopthenazi.models;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.bopthenazi.utils.Collidable;
import com.bopthenazi.utils.SoundManager;
import com.bopthenazi.views.screens.BTNGameScreen;

public class ZombieBunny extends BTNContainedActor {

	private TextureRegion onHitTexture;
	
	public ZombieBunny(float x, float y, BTNGameScreen gameScreen, Container container) {
		
		super(gameScreen.getTexture("zombie-bunny"), x, y, gameScreen, container);
		
		this.onHitTexture = gameScreen.getTexture("zombie-bunny-hit-frame");
	}
	
	@Override
	public void onCollide(Collidable partner) {
		
		super.onCollide(partner);
		
		gameScreen.getSoundManager().playSound(SoundManager.SOUND_ID_PUNCH);
		gameScreen.getSoundManager().playSound(SoundManager.SOUND_ID_ZOMBIE_BUNNY_DEATH);
//		gameScreen.incrementScore();
		
		this.setTextures(new Array<TextureRegion>(new TextureRegion[]{onHitTexture}));
	}
}