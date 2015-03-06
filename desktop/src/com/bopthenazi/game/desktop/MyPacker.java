package com.bopthenazi.game.desktop;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class MyPacker {
    
	public static void main (String[] args) throws Exception {
        
		// Process the menu assets.
		TexturePacker.process("F:/Google Drive/Kilobyte Games/Zombie Bop!/Art Assets/Extract/screen-menu", "F:/Developer/GitHub Repository/zombie-bop/android/assets/textures/textures-packed", "menu");
		
		// Process the game assets.
		TexturePacker.process("F:/Google Drive/Kilobyte Games/Zombie Bop!/Art Assets/Extract/screen-game", "F:/Developer/GitHub Repository/zombie-bop/android/assets/textures/textures-packed", "game");
		
		// Process the general assets.
		TexturePacker.process("F:/Google Drive/Kilobyte Games/Zombie Bop!/Art Assets/Extract/general", "F:/Developer/GitHub Repository/zombie-bop/android/assets/textures/textures-packed", "general");
    }
}