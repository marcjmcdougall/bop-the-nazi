package com.bopthenazi.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.bopthenazi.utils.Collidable;
import com.bopthenazi.views.screens.BTNGameScreen;

public class ZombieBunny extends BTNContainedActor {

	private Texture onHitTexture;
	
	public ZombieBunny(float x, float y, BTNGameScreen gameScreen) {
		
		super(gameScreen.getTexture("screen-game/zombie-bunny/zombie-bunny.png"), x, y, gameScreen);
		
		this.onHitTexture = gameScreen.getTexture("screen-game/zombie-bunny/zombie-bunny-hit-frame.png");
	}
	
	@Override
	public void onCollide(Collidable partner) {
		
		super.onCollide(partner);
		
		gameScreen.playSound(BTNGameScreen.SOUND_ID_PUNCH);
		gameScreen.playSound(BTNGameScreen.SOUND_ID_ZOMBIE_BUNNY_DEATH);
		gameScreen.incrementScore();
		
		this.setTextures(new Array<Texture>(new Texture[]{onHitTexture}));
	}
}