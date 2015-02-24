package com.bopthenazi.views.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateByAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateToAction;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.bopthenazi.game.BTNGame;
import com.bopthenazi.models.BTNActor;
import com.bopthenazi.models.BTNProgressBar;
import com.bopthenazi.models.BTNStage;
import com.bopthenazi.models.BasicButton;

public class BTNMenuScreen implements Screen {

	private static final float MENU_TOP_BAR_TOP = BTNGameScreen.TOP_BAR_TOP + 275.0f;
	
	private static final float MENU_WIDTH = 1080.0f;
	private static final float MENU_HEIGHT = 1920.0f;
	
	private BTNGame game;
	private BTNGameScreen gameScreen;
	
	private Stage menuStage;
	
	private BTNActor bg;
	private BTNActor stripes;
	private BTNActor zombie;
	private BasicButton startGame;
	private BTNActor topBar;
	private BTNActor title;
	private BTNActor bottomBar;
	
	private BTNProgressBar prog;
	
	private volatile boolean animationComplete;
	
	public BTNMenuScreen(BTNGame game){
		
		this.game = game;
		this.gameScreen = new BTNGameScreen(game);
	}
	
	@Override
	public void show() {
		
		FitViewport viewport = new FitViewport(MENU_WIDTH, MENU_HEIGHT);
		menuStage = new Stage(viewport);
		
		this.setAnimationComplete(false);
		this.bg = new BTNActor(new Texture(Gdx.files.internal("textures/screen-menu/orange-background.png")), MENU_WIDTH / 2.0f, MENU_HEIGHT / 2.0f, MENU_WIDTH, MENU_HEIGHT);
		this.title = new BTNActor(new Texture("textures/screen-menu/zombie-bop-menu-title.png"), MENU_WIDTH / 2.0f, MENU_HEIGHT * 0.75f, 850.0f, 600.0f);
		this.stripes = new BTNActor(new Texture("textures/screen-menu/yellow-stripes.png"), MENU_WIDTH / 2.0f, (MENU_HEIGHT / 2.0f));
		this.zombie = new BTNActor(new Texture("textures/screen-menu/happy-zombie.png"), MENU_WIDTH / 2.0f, MENU_HEIGHT * 0.32f, MENU_WIDTH, MENU_HEIGHT * 0.6f);
		this.topBar = new BTNActor(new Texture("textures/screen-game/top-bar.png"), MENU_WIDTH / 2.0f, MENU_TOP_BAR_TOP, MENU_WIDTH, BTNGameScreen.BAR_HEIGHT);
		this.bottomBar = new BTNActor(new Texture("textures/screen-game/bottom-bar.png"), MENU_WIDTH / 2.0f, BTNGameScreen.BOTTOM_BAR_BOTTOM + 625.0f, MENU_WIDTH, BTNGameScreen.BAR_HEIGHT);
		this.startGame = new BasicButton(new Texture("textures/screen-menu/start-button-up.png"), new Texture("textures/screen-menu/start-button-down.png"), MENU_WIDTH / 2.0f, 125.0f, MENU_WIDTH * 0.70f, 200.0f);
		this.stripes.setOriginX(stripes.getWidth() / 2.0f);
		this.stripes.setOriginY(stripes.getHeight() / 2.0f);
		this.prog = new BTNProgressBar(new Texture("textures/screen-menu/progress-bar/pb-back.png"), new Texture("textures/screen-menu/progress-bar/pb-front.png"), MENU_WIDTH / 2.0f, MENU_HEIGHT / 2.0f - 450.0f, 1000.0f, 200.0f);
		
		this.prog.getColor().a = 0.0f;
		
		RotateByAction rotate = new RotateByAction();
		rotate.setAmount(360.0f);
		rotate.setDuration(12.0f);
		
		startGame.addListener(new com.badlogic.gdx.scenes.scene2d.InputListener(){
			
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				
				return true;
			}
			
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				
				super.touchUp(event, x, y, pointer, button);
				
				Gdx.app.log(BTNGame.TAG, "TOUCHUP Received");
				
//				BTNMenuScreen.this.game.setScreen(new BTNGameScreen(BTNMenuScreen.this.game));
//				BTNMenuScreen.this.game.setScreen(new BTNLoadingScreen(game));
				
				bottomBar.addAction(Actions.moveTo(bottomBar.getX(), BTNGameScreen.BOTTOM_BAR_TOGETHER, 1.0f, Interpolation.bounceOut));
				topBar.addAction(Actions.moveTo(topBar.getX(), BTNGameScreen.TOP_BAR_TOGETHER, 1.0f, Interpolation.bounceOut));
				
				prog.addAction(Actions.sequence(Actions.delay(1.0f), Actions.fadeIn(1.0f), Actions.run(new Runnable() {
					
					@Override
					public void run() {
						
						setAnimationComplete(true);
					}
				})));
				
				startGame.addAction(Actions.fadeOut(1.0f));
			}
		});
		
		RepeatAction repeat = new RepeatAction();
		repeat.setAction(rotate);
		repeat.setCount(RepeatAction.FOREVER);
		
		stripes.addAction(repeat);
		
		menuStage.addActor(bg);
		menuStage.addActor(stripes);
		menuStage.addActor(title);
		menuStage.addActor(zombie);
		menuStage.addActor(topBar);
		menuStage.addActor(bottomBar);
		menuStage.addActor(startGame);
		menuStage.addActor(prog);
		
		Gdx.input.setInputProcessor(menuStage);
	}

	@Override
	public void render(float delta) {
		
		Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(gameScreen.getAssetManager().update() && isAnimationComplete()){
        	
        	game.setScreen(gameScreen);
        }
        	
        prog.setPercentDraw(gameScreen.getAssetManager().getProgress());
        
        menuStage.act(delta);
        menuStage.draw();
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void dispose() {
		
		menuStage.dispose();
	}

	public boolean isAnimationComplete() {
		
		return animationComplete;
	}

	public void setAnimationComplete(boolean animationComplete) {
		
		this.animationComplete = animationComplete;
	}
}
