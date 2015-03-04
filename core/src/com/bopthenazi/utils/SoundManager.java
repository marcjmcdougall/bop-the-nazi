package com.bopthenazi.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {

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
	public static final int SOUND_ID_CLICK_UP = 10;
	public static final int SOUND_ID_CLICK_DOWN = 11;
	
	private AssetManager assets;
	
	private boolean muted;
	
	public SoundManager(AssetManager assetManager){
		
		assets = assetManager;
		this.setMuted(false);
	}
	
	public void beginLoadSFX(){
		
		// Load sounds...
		loadSFX("bunny-die.ogg");
		loadSFX("explosion.ogg");
		loadSFX("game-over-cackle.ogg");
		loadSFX("lets-go.ogg");
		loadSFX("new-record.ogg");
		loadSFX("punch.ogg");
		loadSFX("zombie-hit.ogg");
		loadSFX("powerup.ogg");
		loadSFX("zombie-bunny-die.ogg");
		loadSFX("lose-heart.ogg");
		loadSFX("click-down.ogg");
		loadSFX("click-up.ogg");
	}
	
	private void loadSFX(String fileNamePostPrepend){
		
		this.assets.load(SFX_PREPEND + fileNamePostPrepend, Sound.class);
	}
	
	public void playSound(int soundID){
		
		if(!isMuted()){
			
			switch(soundID){
			
				case SOUND_ID_PUNCH :{
					
//					punchSound.play(DEFAULT_VOLUME * 0.75f);
					getSound("punch.ogg").play(DEFAULT_VOLUME * 0.75f);
					
					break;
				}
				case SOUND_ID_SPLAT :{
					
//					splatSound.play(DEFAULT_VOLUME * 0.25f);
//					getSound("splat.wav").play(DEFAULT_VOLUME * 0.25f);
					getSound("lose-heart.ogg").play(DEFAULT_VOLUME * 0.5f);
					
					break;
				}
				case SOUND_ID_EXPLOSION :{
					
//					explosionSound.play(DEFAULT_VOLUME * 0.15f);
					getSound("explosion.ogg").play(DEFAULT_VOLUME * 0.15f);
					
					break;
				}
				case SOUND_ID_GAME_OVER :{
					
//					gameOverSound.play(DEFAULT_VOLUME);
//					getSound("game-over.wav").play(DEFAULT_VOLUME);
					getSound("game-over-cackle.ogg").play(DEFAULT_VOLUME * 0.5f);
					
					break;
				}
				case SOUND_ID_LETS_GO : {
					
//					letsGoSound.play(DEFAULT_VOLUME);
					getSound("lets-go.ogg").play(DEFAULT_VOLUME);
					
					break;
				}
				case SOUND_ID_BUNNY_DEATH : {
					
//					bunnyDeathSound.play(DEFAULT_VOLUME * 0.75f);
					getSound("bunny-die.ogg").play(DEFAULT_VOLUME * 0.75f);
					
					break;
				}
				case SOUND_ID_ZOMBIE_DEATH : {
					
//					zombieDeathSound.play(DEFAULT_VOLUME * 0.75f);
					getSound("zombie-hit.ogg").play(DEFAULT_VOLUME * 0.15f);
					
					break;
				}
				case SOUND_ID_NEW_RECORD : {
					
//					newRecordSound.play(DEFAULT_VOLUME * 0.75f);
					getSound("new-record.ogg").play(DEFAULT_VOLUME * 1.0f);
					
					break;
				}
				case SOUND_ID_POWERUP :{
					
					getSound("powerup.ogg").play();
					
					break;
				}
				case SOUND_ID_ZOMBIE_BUNNY_DEATH :{
					
//					zombieBunnyDieSound.play(DEFAULT_VOLUME * 0.75f);
					getSound("zombie-bunny-die.ogg").play(DEFAULT_VOLUME * 0.75f);
					
					break;
				}
				case SOUND_ID_CLICK_DOWN :{
					
					getSound("click-down.ogg").play(DEFAULT_VOLUME * 0.5f);
					
					break;
				}
				case SOUND_ID_CLICK_UP :{
					
					getSound("click-up.ogg").play(DEFAULT_VOLUME * 0.5f);
					
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

	public boolean isMuted() {
		
		return muted;
	}

	private void setMuted(boolean muted) {
		
		this.muted = muted;
	}
	
	public void toggleSound(){
		
		if(isMuted()){
			
			playSound(SOUND_ID_CLICK_UP);
			setMuted(false);
		}
		else{
			
			setMuted(true);
		}
	}
}
