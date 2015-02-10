package com.bopthenazi.models;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.utils.Array;
import com.bopthenazi.views.screens.BTNGameScreen;

public class LivesModule{

	private static final int TOTAL_LIVES = 3;
	
	private static final float HEART_Y = 1520.0f; 
	
	private static final float HEART_X_OFFSET = 50.0f; 
	private static final float HEART_SPACING = 20.0f;
	
	private static final float HEART_WIDTH = 50.0f;
	private static final float HEART_HEIGHT = 50.0f;
	
	private Array<BTNActor> heartOutlines;
	private Array<BTNActor> hearts;
	
	private int heartIndex;
	
	public LivesModule(){

		initialize();
	}
	
	private void initialize(){
		
		heartIndex = TOTAL_LIVES - 1;
		
		intializeHeartOutlines();
		initializeHearts();
	}

	private void intializeHeartOutlines() {
		
		this.heartOutlines = new Array<BTNActor>();
		
		for(int i = 0; i < TOTAL_LIVES; i++){
			
			heartOutlines.add(new BTNActor(new Texture("heart-outline.png"), ((BTNGameScreen.GAME_WIDTH - HEART_X_OFFSET) - HEART_WIDTH * i) - HEART_WIDTH / 2.0f + HEART_SPACING, HEART_Y - HEART_HEIGHT / 2.0f, HEART_WIDTH, HEART_HEIGHT));
		}
	}
	
	private void initializeHearts() {
		
		this.hearts = new Array<BTNActor>();
		
		for(int i = 0; i < TOTAL_LIVES; i++){
			
			hearts.add(new BTNActor(new Texture("heart.png"), ((BTNGameScreen.GAME_WIDTH - HEART_X_OFFSET) - HEART_WIDTH * i) - HEART_WIDTH / 2.0f + HEART_SPACING, HEART_Y - HEART_HEIGHT / 2.0f, HEART_WIDTH, HEART_HEIGHT));
		}
	}

	public void popHeart(){
		
		if(heartIndex >= 0){
			
			System.out.println("Fading heart now!");
			
			AlphaAction fadeOut = Actions.fadeOut(0.5f);
			
			hearts.get(heartIndex).remove();
			
			heartIndex--;
		}
	}
	
	/**
	 * @return the totalLives
	 */
	public static int getTotalLives() {
		
		return TOTAL_LIVES;
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
}
