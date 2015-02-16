package com.bopthenazi.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class FontFactory {

	public static BitmapFont buildFont(int fontSize){
		
		BitmapFont output = new BitmapFont();
		
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Masaaki-Regular.otf"));
		
		FreeTypeFontParameter params = new FreeTypeFontParameter();
		params.size = fontSize;
		output = generator.generateFont(params);
		
		generator.dispose();
		
		return output;
	}
}
