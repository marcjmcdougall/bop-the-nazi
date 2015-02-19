package com.bopthenazi.utils;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;

public class SaveManager {

	private FileHandle scoreFile;
	private FileHandle oneShotFile;
	
	
	public SaveManager(){
		
		scoreFile = Gdx.files.local("bin/save.txt");
		oneShotFile = Gdx.files.local("bin/oneshot.txt");
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
	
	public boolean isOneShot(){
		
		if(oneShotFile.exists()){
			
			if(oneShotFile.readString() == ""){
				
				oneShotFile.writeString("You have accessed the application once before.", false);
				
				return true;
			}
		}
		
		return false;
	}
}
