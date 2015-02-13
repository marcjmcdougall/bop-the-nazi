package com.bopthenazi.models;

import com.badlogic.gdx.graphics.Texture;
import com.bopthenazi.views.screens.BTNGameScreen;

public class Dynamite extends BTNContainedActor {

	public Dynamite(float x, float y, BTNGameScreen screen){
		
		super(new Texture("dynamite.png"), x, y, screen);
		
		this.initialize(screen);
	}
}
