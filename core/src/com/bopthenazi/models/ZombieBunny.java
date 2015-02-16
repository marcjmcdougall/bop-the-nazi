package com.bopthenazi.models;

import com.badlogic.gdx.graphics.Texture;
import com.bopthenazi.views.screens.BTNGameScreen;

public class ZombieBunny extends BTNContainedActor {

	public ZombieBunny(float x, float y, BTNGameScreen gameScreen) {
		
		super(new Texture("zombie-bunny.png"), x, y, gameScreen);
	}
}
