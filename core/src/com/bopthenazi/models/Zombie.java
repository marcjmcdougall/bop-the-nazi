package com.bopthenazi.models;

import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.bopthenazi.utils.Collidable;
import com.bopthenazi.utils.SoundManager;
import com.bopthenazi.views.screens.BTNGameScreen;

public class Zombie extends BTNContainedActor {

	public static final float NAZI_WIDTH = 200.0f;
	public static final float NAZI_HEIGHT = 388.8f;
	
	private static final int ZOMBIE_TYPE_OFFICE = 0;
	private static final int ZOMBIE_TYPE_SHIRT = 1;
	
	private Random random;
	
	private TextureRegion onHitTexture;
	
	public Zombie(float x, float y, BTNGameScreen screen, Container container){
		
		super(new Random().nextInt(2) == 1 ? screen.getTexture("zombie") : screen.getTexture("zombie-2"), x, y, screen, container);
		
		random = new Random();
		
		initializeZombieTextures();
	}

	@Override
	public void onCollide(Collidable partner) {
		
		super.onCollide(partner);
		
		this.setTextures(new Array<TextureRegion>(new TextureRegion[]{onHitTexture}));
		
		gameScreen.getSoundManager().playSound(SoundManager.SOUND_ID_PUNCH);
		gameScreen.getSoundManager().playSound(SoundManager.SOUND_ID_ZOMBIE_DEATH);
		gameScreen.incrementScore();
	}
	
	private void initializeZombieTextures(){
		
		int zombieType = random.nextInt(2);
		
		switch(zombieType){
		
			case ZOMBIE_TYPE_OFFICE :{
				
				this.setTextures(new Array<TextureRegion>(new TextureRegion[]{getGameScreen().getTexture("zombie-2")/*, new Texture("zombie-2.png"), new Texture("zombie-2.png"), new Texture("zombie2-hit-frame.png")*/}));
				this.onHitTexture = getGameScreen().getTexture("zombie2-hit-frame");
				
				break;
			}
			case ZOMBIE_TYPE_SHIRT :{
				
				this.setTextures(new Array<TextureRegion>(new TextureRegion[]{getGameScreen().getTexture("zombie")/*, new Texture("zombie.png"), new Texture("zombie.png"), new Texture("zombie1-hit-frame.png")*/}));
				this.onHitTexture = getGameScreen().getTexture("zombie1-hit-frame");
				
				break;
			}
			default :{
				
				// Do nothing.
				break;
			}
		}
	}
	
	@Override
	public void act(float delta) {
		
		super.act(delta);
	}
}