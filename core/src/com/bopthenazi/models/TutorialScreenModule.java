package com.bopthenazi.models;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.bopthenazi.views.screens.BTNGameScreen;

public class TutorialScreenModule extends Group {
	
	private BTNGameScreen gameScreen;
	
	private BTNActor alpha;
	private BTNActor bg;
	private BasicButton ok;

	public TutorialScreenModule(BTNGameScreen gameScreen){
		
		super();
		
		this.gameScreen = gameScreen;
		
		initialize();
	}

	private void initialize() {
		
		this.alpha = new BTNActor(gameScreen.getTexture("alpha-25"), BTNGameScreen.GAME_WIDTH / 2.0f, BTNGameScreen.GAME_HEIGHT / 2.0f, BTNGameScreen.GAME_WIDTH, BTNGameScreen.GAME_HEIGHT);
		this.bg = new BTNActor(gameScreen.getTexture("instructions-screen"), BTNGameScreen.GAME_WIDTH / 2.0f, BTNGameScreen.GAME_HEIGHT / 2.0f - 200.0f, BTNGameScreen.GAME_WIDTH * 0.75f, BTNGameScreen.GAME_HEIGHT * 0.55f);
		this.ok = new BasicButton(gameScreen.getTexture("ok-button-up-state"), gameScreen.getTexture("ok-button-down-state"), BTNGameScreen.GAME_WIDTH * 0.72f, BTNGameScreen.GAME_HEIGHT * 0.2f, 200.0f, 150.0f);
	
		ok.addListener(new InputListener(){
			
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				
				gameScreen.onButtonClickDown();
				
				return true;
			}
			
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				
				super.touchUp(event, x, y, pointer, button);
				
				gameScreen.onButtonClickUp();
				
				bg.addAction(Actions.moveBy(0.0f, 2000.0f, 1.0f, Interpolation.pow4));
				ok.addAction(Actions.moveBy(0.0f, 2000.0f, 1.0f, Interpolation.pow4));
				
				gameScreen.begin(0.5f);
				
				TutorialScreenModule.this.addAction(Actions.sequence(Actions.alpha(0.0f, 1.0f), Actions.run(new Runnable() {
					
					@Override
					public void run() {
						
						TutorialScreenModule.this.setVisible(false);
					}
				})));
			}
		});
		
		this.addActor(alpha);
		this.addActor(bg);
		this.addActor(ok);
		
		bg.setY(bg.getY() + 2000.0f);
		ok.setY(ok.getY() + 2000.0f);
		
		this.getColor().a = 0.0f;
		this.setVisible(false);
	}
	
	public void doAnimate(){
		
		this.setVisible(true);
		
		alpha.addAction(Actions.sequence(Actions.fadeIn(1.0f)));
		bg.addAction(Actions.sequence(Actions.moveBy(0.0f, -2000.0f, 1.0f, Interpolation.pow4)));
		ok.addAction(Actions.sequence(Actions.moveBy(0.0f, -2000.0f, 1.0f, Interpolation.pow4)));
		
		this.addAction(Actions.fadeIn(1.0f));
	}
}
