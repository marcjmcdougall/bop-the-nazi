package com.bopthenazi.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.bopthenazi.views.screens.BTNGameScreen;

public class Dynamite extends BTNContainedActor {

	public Dynamite(float x, float y, BTNGameScreen gameScreen){
		
		super(new Array<Texture>(new Texture[]{new Texture("dynamite-01.png"), new Texture("dynamite-02.png"), new Texture("dynamite-03.png"), 
				new Texture("dynamite-04.png"), new Texture("dynamite-05.png")}), x, y, gameScreen);
	}
}
