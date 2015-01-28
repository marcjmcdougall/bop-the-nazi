package com.bopthenazi.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader.FreeTypeFontGeneratorParameters;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

public class Score extends Actor {

	private static final float SCORE_WIDTH = 200.0f;
	private static final float SCORE_HEIGHT = 100.0f;
	
	private BitmapFont font;
	
	private int score;
	private Label label;
	
	public Score(float x, float y){
		
		this.setX(x);
		this.setY(y);
		
		this.setWidth(SCORE_WIDTH);
		this.setHeight(SCORE_HEIGHT);
		
		this.score = 0;
		
		initializeFont();
		
		LabelStyle style = new LabelStyle(new BitmapFont(), new Color(1.0f, 1.0f, 1.0f, 1.0f));
		
		label = new Label("Score: " + score, style);
		label.setX(getX());
		label.setY(getY());
		label.setWidth(getWidth());
		label.setHeight(getHeight());
		label.setFontScale(5.0f);
	}
	
	private void initializeFont() {
		
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/myfont.ttf"));
		
		FreeTypeFontParameter params = new FreeTypeFontParameter();
		params.size = 24;
		font = generator.generateFont(params);
		
		generator.dispose();
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		label.draw(batch, parentAlpha);
	}
}
