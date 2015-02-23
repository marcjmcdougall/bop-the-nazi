package com.bopthenazi.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {

	private static final boolean QUIET_MODE = true;
	
	private static final float DEFAULT_VOLUME = 1.0f;
	
	private static final String SFX_PREPEND = "sfx/";
	
	public static final int SOUND_ID_SPLAT = 0;
	public static final int SOUND_ID_PUNCH = 1;
	public static final int SOUND_ID_EXPLOSION = 2;
	public static final int SOUND_ID_GAME_OVER = 3;
	public static final int SOUND_ID_LETS_GO = 4;
	public static final int SOUND_ID_ZOMBIE_DEATH = 5;
	public static final int SOUND_ID_BUNNY_DEATH = 6;
	public static final int SOUND_ID_NEW_RECORD = 7;
	public static final int SOUND_ID_POWERUP = 8;
	public static final int SOUND_ID_ZOMBIE_BUNNY_DEATH = 9;
	
	private AssetManager assets;
	
	public SoundManager(AssetManager assetManager){
		
		assets = assetManager;
	}
	
	public void beginLoadSFX(){
		
		// Load sounds...
		loadSFX("bunny-die.wav");
		loadSFX("explosion.wav");
		loadSFX("game-over.wav");
		loadSFX("lets-go.wav");
		loadSFX("new-record.wav");
		loadSFX("punch.wav");
		loadSFX("splat.wav");
		loadSFX("zombie-die-2.wav");
		loadSFX("powerup.wav");
		loadSFX("zombie-bunny-die.wav");
	}
	
	private void loadSFX(String fileNamePostPrepend){
		
		this.assets.load(SFX_PREPEND + fileNamePostPrepend, Sound.class);
	}
	
	public void playSound(int soundID){
		
		if(!QUIET_MODE){
			
			switch(soundID){
			
				case SOUND_ID_PUNCH :{
					
//					punchSound.play(DEFAULT_VOLUME * 0.75f);
					getSound("punch.wav").play(DEFAULT_VOLUME * 0.75f);
					
					break;
				}
				case SOUND_ID_SPLAT :{
					
//					splatSound.play(DEFAULT_VOLUME * 0.25f);
					getSound("splat.wav").play(DEFAULT_VOLUME * 0.25f);
					
					break;
				}
				case SOUND_ID_EXPLOSION :{
					
//					explosionSound.play(DEFAULT_VOLUME * 0.15f);
					getSound("explosion.wav").play(DEFAULT_VOLUME * 0.15f);
					
					break;
				}
				case SOUND_ID_GAME_OVER :{
					
//					gameOverSound.play(DEFAULT_VOLUME);
					getSound("game-over.wav").play(DEFAULT_VOLUME);
					
					break;
				}
				case SOUND_ID_LETS_GO : {
					
//					letsGoSound.play(DEFAULT_VOLUME);
					getSound("lets-go.wav").play(DEFAULT_VOLUME);
					
					break;
				}
				case SOUND_ID_BUNNY_DEATH : {
					
//					bunnyDeathSound.play(DEFAULT_VOLUME * 0.75f);
					getSound("bunny-die.wav").play(DEFAULT_VOLUME * 0.75f);
					
					break;
				}
				case SOUND_ID_ZOMBIE_DEATH : {
					
//					zombieDeathSound.play(DEFAULT_VOLUME * 0.75f);
					getSound("zombie-die-2.wav").play(DEFAULT_VOLUME * 0.75f);
					
					break;
				}
				case SOUND_ID_NEW_RECORD : {
					
//					newRecordSound.play(DEFAULT_VOLUME * 0.75f);
					getSound("new-record.wav").play(DEFAULT_VOLUME * 0.75f);
					
					break;
				}
				case SOUND_ID_POWERUP :{
					
					getSound("powerup.wav").play();
					
					break;
				}
				case SOUND_ID_ZOMBIE_BUNNY_DEATH :{
					
//					zombieBunnyDieSound.play(DEFAULT_VOLUME * 0.75f);
					getSound("zombie-bunny-die.wav").play(DEFAULT_VOLUME * 0.75f);
					
					break;
				}
				default :{
					
					// Do nothing.
					break;
				}
			}
		}
	}
	
	public Sound getSound(String soundNamePostPrepend){
		
		return assets.get(SFX_PREPEND + soundNamePostPrepend, Sound.class);
	}
}
