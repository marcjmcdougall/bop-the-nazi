package com.bopthenazi.models;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.utils.Array;
import com.bopthenazi.views.screens.BTNGameScreen;

public class LivesModule{

	private static final float HEART_Y = 1520.0f; 
	
	private static final float HEART_X_OFFSET = 40.0f; 
	
	private static final float HEART_WIDTH = 100.0f;
	private static final float HEART_HEIGHT = 75.0f;
	
	private Array<BTNActor> heartOutlines;
	private Array<BTNActor> hearts;
	
	private BTNGameScreen gameScreen;
	
	private int heartIndex;
	
	public LivesModule(BTNGameScreen gameScreen){
		
		this.gameScreen = gameScreen;
		
		initialize();
	}
	
	private void initialize(){
		
		heartIndex = Score.DEFAULT_NUMBER_LIVES - 1;
		intializeHeartOutlines();
		initializeHearts();
	}

	private void intializeHeartOutlines() {
		
		this.heartOutlines = new Array<BTNActor>();
		
		for(int i = 0; i < Score.DEFAULT_NUMBER_LIVES; i++){
			
			heartOutlines.add(new BTNActor(gameScreen.getTexture("screen-game/heart/heart-empty-v2.png"), BTNGameScreen.GAME_WIDTH - ((HEART_X_OFFSET + HEART_WIDTH / 2.0f) * (i + 1)) - (i * HEART_X_OFFSET), HEART_Y, HEART_WIDTH, HEART_HEIGHT));
		}
	}
	
	private void initializeHearts() {
		
		this.hearts = new Array<BTNActor>();
		
		for(int i = 0; i < Score.DEFAULT_NUMBER_LIVES; i++){
			
			hearts.add(new BTNActor(gameScreen.getTexture("screen-game/heart/heart.png"), BTNGameScreen.GAME_WIDTH - ((HEART_X_OFFSET + HEART_WIDTH / 2.0f) * (i + 1)) - (i * HEART_X_OFFSET), HEART_Y, HEART_WIDTH, HEART_HEIGHT));
			hearts.get(i).setOrigin(hearts.get(i).getWidth() / 2.0f, hearts.get(i).getHeight() / 2.0f);
		}
	}

	public void popHeart(){
		
		if(heartIndex >= 0){
			
			AlphaAction fadeOut = Actions.fadeOut(0.5f);
			
			ScaleToAction scaleTo = Actions.scaleTo(3.0f, 3.0f, 0.5f);
			
			hearts.get(heartIndex).addAction(fadeOut);
			hearts.get(heartIndex).addAction(scaleTo);
			
			heartIndex--;
		}
	}
	
	public void pushHeart() {

		// If we have at least a single heart deficit.
		if(heartIndex < Score.DEFAULT_NUMBER_LIVES - 1){
			
			AlphaAction fadeIn = Actions.fadeIn(0.5f);
			ScaleToAction scaleTo = Actions.scaleTo(1.0f, 1.0f, 0.5f);
			
			heartIndex++;
			
			hearts.get(heartIndex).addAction(fadeIn);
			hearts.get(heartIndex).addAction(scaleTo);
		}
	}
	
	/**
	 * @return the totalLives
	 */
	public static int getTotalLives() {
		
		return Score.DEFAULT_NUMBER_LIVES;
	}

	/**
	 * @return the heartOutlines
	 */
	public Array<BTNActor> getHeartOutlines() {
		
		return heartOutlines;
	}

	/**
	 * @param heartOutlines the heartOutlines to set
	 */
	public void setHeartOutlines(Array<BTNActor> heartOutlines) {
		
		this.heartOutlines = heartOutlines;
	}

	/**
	 * @return the hearts
	 */
	public Array<BTNActor> getHearts() {
		
		return hearts;
	}

	/**
	 * @param hearts the hearts to set
	 */
	public void setHearts(Array<BTNActor> hearts) {
		
		this.hearts = hearts;
	}

	public void reset() {
		
		initializeHearts();
	}
}
