package com.bopthenazi.game.desktop;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class MyPacker {
    
	public static void main (String[] args) throws Exception {
        
		TexturePacker.process("F:/Developer/GitHub Repository/bop-the-nazi/android/assets/textures/screen-menu", "F:/Developer/GitHub Repository/bop-the-nazi/android/assets/textures/textures-packed", "menu");
		TexturePacker.process("F:/Developer/GitHub Repository/bop-the-nazi/android/assets/textures/screen-game", "F:/Developer/GitHub Repository/bop-the-nazi/android/assets/textures/textures-packed", "game");
		TexturePacker.process("F:/Developer/GitHub Repository/bop-the-nazi/android/assets/textures/general", "F:/Developer/GitHub Repository/bop-the-nazi/android/assets/textures/textures-packed", "general");
    }
}