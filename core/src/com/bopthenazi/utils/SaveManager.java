package com.bopthenazi.utils;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;

public class SaveManager {

	private FileHandle scoreFile;
	
	private Preferences prefs;
	
	private static final String DEFAULT_PREFERENCES = "default";
	private static final String PREF_ONE_SHOT = "one_shot";
	
	public SaveManager(){
		
		scoreFile = Gdx.files.local("bin/save.txt");
		
		prefs = Gdx.app.getPreferences(DEFAULT_PREFERENCES);
	}
	
	public void saveScore(int newScore){
		
		scoreFile.writeString(Base64Coder.encodeString("" + newScore), false);
	}
	
	public String retrieveScore(){
		
		String output = "0";
		
		if(scoreFile.exists()){
			
			output = Base64Coder.decodeString(scoreFile.readString());
			
			if(output == ""){
				
				output = "0";
			}
		}
		
		return output;
	}
	
	public boolean isFirstShot(){
		
		// If the preference returns false (it has not been accessed before)
		if(!prefs.getBoolean(PREF_ONE_SHOT, false)){
			
			// Change it to true
			prefs.putBoolean(PREF_ONE_SHOT, true);
			
			prefs.flush();
			
			// Return true to tell the system that this is the first (and only time)
			return true;
		}
		
		// Otherwise, simply return false.
		return false;
	}
}
