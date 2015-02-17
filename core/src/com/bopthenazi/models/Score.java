package com.bopthenazi.models;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.bopthenazi.utils.FontFactory;

public class Score extends Actor {

	public static final float SCORE_WIDTH = 200.0f;
	public static final float SCORE_HEIGHT = 100.0f;
	
	public static final int DEFAULT_NUMBER_LIVES = 3;
	
	private int score;
	private int lives;
	
	private Label label;
	
	public Score(float x, float y){
		
		this.setX(x);
		this.setY(y);
		
		this.score = 0;
		this.lives = DEFAULT_NUMBER_LIVES;
		
		LabelStyle style = new LabelStyle(FontFactory.buildFont(22), new Color(1.0f, 1.0f, 1.0f, 1.0f));
		
		label = new Label("Score: " + score, style);
		label.setX(getX());
		label.setY(getY());
		label.setWidth(getWidth());
		label.setHeight(getHeight());
		label.setFontScale(5.0f);
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


	public void reset() {
		
		setLives(DEFAULT_NUMBER_LIVES);
		this.score = 0;
	}
}
