package com.bopthenazi.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class FontFactory {

	public static BitmapFont buildFont(){
		
		BitmapFont output = new BitmapFont();
		
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/chalkdust.ttf"));
		
		FreeTypeFontParameter params = new FreeTypeFontParameter();
		params.size = 20;
		output = generator.generateFont(params);
		
		generator.dispose();
		
		return output;
	}
}
