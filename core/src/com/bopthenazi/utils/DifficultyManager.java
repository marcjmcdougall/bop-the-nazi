package com.bopthenazi.utils;

import com.badlogic.gdx.Gdx;
import com.bopthenazi.game.BTNGame;


public class DifficultyManager {
	
	// The absolute max number of containers allowed to be active at once.
	public static final int MAX_CONTAINERS = 5;
	
	// The default for the actual max number of containers that can be concurrently-active.
	private static final int DEFAULT_MAX_CONCURRENT_CONTAINERS = 4;
	
	// The default for the spawn rate of a new container.
	private static final float DEFAULT_NEW_CONTAINER_SPAWN_RATE = 1.0f;
	
	// The default for the rate at which the difficulty is updated.  This should be constant for a linear difficulty progression.
	private static final float DEFAULT_DIFFICULTY_UPDATE_RATE = 0.5f;
	
	// The default for the amount which we increase the difficulty by at each iteration.  Additionally, this will be the amount we 
	// will decrease the difficulty when a heart is lost.
	private static final float DEFAULT_DIFFICULTY_INCREASE_DELTA = 0.05f;
	
	// The default for the amount which we decrease the difficulty.
	private static final float DEFAULT_DIFFICULTY_DECREASE_DELTA = 0.25f;
	
	// The default for the rate at which the difficulty update rate accelerates.  Note that this will be 1.0 for now as the rate is contant.
	private static final float DEFAULT_DIFFICULTY_UPDATE_RATE_ACCELERATION = 1.0f;
	
	// The default amount of time that the game will pause for when a heart is lost.
	private static final float DEFAULT_PAUSE_DURATION = 5.0f;
	
	// The ACTUAL spawn rate of new containers.  This will vary from the default over time.
	private float newContainerSpawnRate;
	
	// The ACTUAL difficulty update rate.  This may vary over time.
	private float difficultyUpdateRate;
	
	// The ACTUAL change in difficulty experienced at each iteration of the update() loop.
	private float difficultyIncreaseDelta;
	
	// The ACTUAL change in difficulty experienced at each iteration of the update() loop (for decreases in difficulty).
	private float difficultyDecreaseDelta;
	
	// The ACTUAL acceleration of the update rate. Unused for now.
	private float difficultyDeltaAcceleration;
	
	// The ACTUAL maximum number of concurrent containers.
	private int maxConcurrentContainers;
	
	// A class variable that is used to define whether or not the difficulty update loop should run.
	private boolean paused;
	
	// The amount of time the difficulty update algorithm has been paused.
	private float timePaused;
	
	// Represents the last time an update was performed.  Used to define when to make a change to the effective difficulty of the game.
	private float timeSinceLastDifficultyUpdate; 
	
	// The total time played (in seconds) in the game.  Used to modulate the magnitude of dynamic difficulty updates.  They will become 
	// less powerful later in the game to prevent abuse.
	private float totalTimePlayed;

	// Represents the time that the difficulty algorithm will pause when requested (usually a heart-loss). 
	private float pauseDuration;
	
	public DifficultyManager(){
		
		initialize();
	}

	public void updateDifficulty(float delta){
		
		if(paused){
			
			this.timePaused += delta;
			
			if(timePaused >= pauseDuration){
				
				this.paused = false;
			}
		}
		else{
			
			if(timeSinceLastDifficultyUpdate >= difficultyUpdateRate){
				
				if(totalTimePlayed >= 30.0f){
					
					difficultyDecreaseDelta /= totalTimePlayed;
//					difficultyIncreaseDelta /= totalTimePlayed;
				}
				
				increaseDifficulty();
				this.timeSinceLastDifficultyUpdate = 0.0f;
			}
			else{
				
				this.timeSinceLastDifficultyUpdate += delta;
			}
		}
		
		totalTimePlayed += delta;
	}
	
	public void increaseDifficulty(){
		
		if(newContainerSpawnRate - difficultyIncreaseDelta >= 0){
			
			this.newContainerSpawnRate -= this.difficultyIncreaseDelta;
			
			Gdx.app.log(BTNGame.TAG, "Increasing spawn rate to: " + newContainerSpawnRate);
		}
	}
	
	private void decreaseDifficulty(boolean multiply){
	
		this.newContainerSpawnRate += this.difficultyDecreaseDelta;
		
		if(multiply){
		
			this.newContainerSpawnRate += this.difficultyDecreaseDelta;
		}
		
		Gdx.app.log(BTNGame.TAG, "Decreasing spawn rate to: " + newContainerSpawnRate);
	}
	
	public void onHeartLoss(){
		
		if(paused){
		
			// Decrease the difficulty.
			decreaseDifficulty(true);
			pause(DEFAULT_PAUSE_DURATION * 2.0f);
		}
		else{
			
			decreaseDifficulty(false);
			// Pause the difficulty increase
			pause(DEFAULT_PAUSE_DURATION);
		}
	}
	
	public void reset(){
		
		initialize();
	}
	
	private void pause(float duration){
		
		this.timePaused = 0.0f;
		this.pauseDuration = duration;
		this.paused = true;
	}
	
	private void initialize() {
		
		this.newContainerSpawnRate = DEFAULT_NEW_CONTAINER_SPAWN_RATE;
		this.difficultyUpdateRate = DEFAULT_DIFFICULTY_UPDATE_RATE;
		this.difficultyIncreaseDelta = DEFAULT_DIFFICULTY_INCREASE_DELTA;
		this.difficultyDecreaseDelta = DEFAULT_DIFFICULTY_DECREASE_DELTA;
		this.difficultyDeltaAcceleration = DEFAULT_DIFFICULTY_UPDATE_RATE_ACCELERATION;
		this.maxConcurrentContainers = DEFAULT_MAX_CONCURRENT_CONTAINERS;
		this.pauseDuration = DEFAULT_PAUSE_DURATION;
		
		this.timeSinceLastDifficultyUpdate = 0.0f;
		this.totalTimePlayed = 0.0f;
		this.paused = false;
	}
	
	public float getNewContainerSpawnRate() {
		
		return newContainerSpawnRate;
	}

	public void setNewContainerSpawnRate(float newContainerSpawnRate) {
		
		this.newContainerSpawnRate = newContainerSpawnRate;
	}

	public int getMaxConcurrentContainers() {
		
		return maxConcurrentContainers;
	}

	public void setMaxConcurrentContainers(int maxConcurrentContainers) {
		
		this.maxConcurrentContainers = maxConcurrentContainers;
	}
}
