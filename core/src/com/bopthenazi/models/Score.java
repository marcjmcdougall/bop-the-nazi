package com.bopthenazi.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

public class Score extends Actor {

	public static final float SCORE_WIDTH = 200.0f;
	public static final float SCORE_HEIGHT = 100.0f;
	
	private static final int DEFAULT_NUMBER_LIVES = 4;
	
	private BitmapFont font;
	
	private int score;
	private int lives;
	
	private Label label;
	
	public Score(float x, float y){
		
		this.setX(x);
		this.setY(y);
		
//		this.setWidth(SCORE_WIDTH);
//		this.setHeight(SCORE_HEIGHT);
		
		this.score = 0;
		this.lives = DEFAULT_NUMBER_LIVES;
		
		initializeFont();
		
		LabelStyle style = new LabelStyle(font, new Color(1.0f, 1.0f, 1.0f, 1.0f));
		
		label = new Label("Score: " + score, style);
		label.setX(getX());
		label.setY(getY());
		label.setWidth(getWidth());
		label.setHeight(getHeight());
		label.setFontScale(5.0f);
	}
	
	private void initializeFont() {
		
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/chalkdust.ttf"));
		
		FreeTypeFontParameter params = new FreeTypeFontParameter();
		params.size = 20;
		font = generator.generateFont(params);
		
		generator.dispose();
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		label.draw(batch, parentAlpha);
	}
	
	public void updateScore(int newScore){
		
		this.score = newScore;
		
		updateScoreText();
	}

	private void updateScoreText() {
		
		label.setText("Score: " + score);
	}

	public int getScore() {
		
		return score;
	}

	/**
	 * @return the lives
	 */
	public int getLives() {
		
		return lives;
	}

	/**
	 * @param lives the lives to set
	 */
	public void setLives(int lives) {
		
		this.lives = lives;
	}
}
