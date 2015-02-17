package com.bopthenazi.utils;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;

public class SaveManager {

	private FileHandle file;
	
	public SaveManager(){
		
		file = Gdx.files.local("bin/save.txt");
	}
	
	public void saveScore(int newScore){
		
		file.writeString(Base64Coder.encodeString("" + newScore), false);
	}
	
	public String retrieveScore(){
		
		String output = "0";
		
		if(file.exists()){
			
			output = Base64Coder.decodeString(file.readString());
			
			if(output == ""){
				
				output = "0";
			}
		}
		
		return output;
	}
}
