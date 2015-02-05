package com.bopthenazi.models;

import com.badlogic.gdx.graphics.Texture;
import com.bopthenazi.utils.BTNCollideableActor;
import com.bopthenazi.views.screens.BTNGameScreen;

public class LineBreak extends BTNCollideableActor {

	private static final float LINE_BREAK_HEIGHT = 50.0f;
	
	public LineBreak(float y) {
		
		super(new Texture("dashed-line.png"), BTNGameScreen.GAME_WIDTH / 2.0f, y, BTNGameScreen.GAME_WIDTH, LINE_BREAK_HEIGHT);
	}
}
