package com.bopthenazi.views.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateByAction;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.bopthenazi.game.BTNGame;
import com.bopthenazi.models.BTNActor;
import com.bopthenazi.models.BTNProgressBar;
import com.bopthenazi.models.BasicButton;

public class BTNMenuScreen implements Screen {

//	private static final float MENU_TOP_BAR_TOP = BTNGameScreen.TOP_BAR_TOP/ + 275.0f;
	private static final float MENU_TOP_BAR_TOP = BTNGameScreen.TOP_BAR_TOP + 150.0f;
	
	private static final float MENU_WIDTH = 1080.0f;
	private static final float MENU_HEIGHT = 1920.0f;
	
	private AssetManager assetManager;
	
	private BTNGame game;
	private BTNGameScreen gameScreen;
	
	private Stage menuStage;
	
	private BTNActor bg;
	private BTNActor beta;
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
		
		this.assetManager = new AssetManager();
		
		beginAssetLoad();
	}
	
	private void beginAssetLoad(){
		
		getAssetManager().load("textures/screen-menu/orange-background.png", Texture.class);
		getAssetManager().load("textures/screen-menu/zombie-bop-menu-title.png", Texture.class);
		getAssetManager().load("textures/screen-menu/yellow-stripes.png", Texture.class);
//		getAssetManager().load("textures/screen-menu/happy-zombie.png", Texture.class);
		getAssetManager().load("textures/screen-menu/happy-zombie-with-scars-hd.png", Texture.class);
		getAssetManager().load("textures/screen-menu/start-button-up.png", Texture.class);
		getAssetManager().load("textures/screen-menu/start-button-down.png", Texture.class);
		getAssetManager().load("textures/screen-menu/beta.png", Texture.class);
		
		getAssetManager().load("textures/screen-menu/progress-bar/pb-back.png", Texture.class);
		getAssetManager().load("textures/screen-menu/progress-bar/pb-front.png", Texture.class);
		
		getAssetManager().load("textures/screen-game/top-bar.png", Texture.class);
		getAssetManager().load("textures/screen-game/bottom-bar.png", Texture.class);
	}
	
	@Override
	public void show() {
		
		getAssetManager().finishLoading();
		
		FitViewport viewport = new FitViewport(MENU_WIDTH, MENU_HEIGHT);
		menuStage = new Stage(viewport);
		
		this.setAnimationComplete(false);
		this.bg = new BTNActor(getAssetManager().get("textures/screen-menu/orange-background.png", Texture.class), MENU_WIDTH / 2.0f, MENU_HEIGHT / 2.0f, MENU_WIDTH, MENU_HEIGHT);
		this.beta = new BTNActor(getAssetManager().get("textures/screen-menu/beta.png", Texture.class), MENU_WIDTH * 0.75f, 0.85f);
		this.title = new BTNActor(getAssetManager().get("textures/screen-menu/zombie-bop-menu-title.png", Texture.class), MENU_WIDTH / 2.0f, MENU_HEIGHT * 0.75f, 850.0f, 600.0f);
		this.stripes = new BTNActor(getAssetManager().get("textures/screen-menu/yellow-stripes.png", Texture.class), MENU_WIDTH / 2.0f, (MENU_HEIGHT / 2.0f));
//		this.zombie = new BTNActor(getAssetManager().get("textures/screen-menu/happy-zombie.png", Texture.class), MENU_WIDTH / 2.0f, MENU_HEIGHT * 0.32f, MENU_WIDTH, MENU_HEIGHT * 0.6f);
		this.zombie = new BTNActor(getAssetManager().get("textures/screen-menu/happy-zombie-with-scars-hd.png", Texture.class), MENU_WIDTH / 2.0f, MENU_HEIGHT * 0.32f, MENU_WIDTH, MENU_HEIGHT * 0.6f);
		this.topBar = new BTNActor(getAssetManager().get("textures/screen-game/top-bar.png", Texture.class), MENU_WIDTH / 2.0f, MENU_TOP_BAR_TOP, MENU_WIDTH, BTNGameScreen.BAR_HEIGHT);
		this.bottomBar = new BTNActor(getAssetManager().get("textures/screen-game/bottom-bar.png", Texture.class), MENU_WIDTH / 2.0f, BTNGameScreen.BOTTOM_BAR_BOTTOM + 90.0f /* + 625.0f*/, MENU_WIDTH, BTNGameScreen.BAR_HEIGHT);
		this.startGame = new BasicButton(getAssetManager().get("textures/screen-menu/start-button-up.png", Texture.class), getAssetManager().get("textures/screen-menu/start-button-down.png", Texture.class), MENU_WIDTH / 2.0f, 125.0f, MENU_WIDTH * 0.70f, 200.0f);
		this.stripes.setOriginX(stripes.getWidth() / 2.0f);
		this.stripes.setOriginY(stripes.getHeight() / 2.0f);
		this.prog = new BTNProgressBar(getAssetManager().get("textures/screen-menu/progress-bar/pb-back.png", Texture.class), getAssetManager().get("textures/screen-menu/progress-bar/pb-front.png", Texture.class), MENU_WIDTH / 2.0f, MENU_HEIGHT / 2.0f - 450.0f, MENU_WIDTH, 200.0f);
		
//		this.beta.setRotation(10.0f);
		this.beta.setWidth(400.0f);
		this.beta.setHeight(264.0f);
		this.beta.setScale(0.8f);
		
		this.beta.setX(MENU_WIDTH * .96f - (beta.getWidth() / 2.0f));
		this.beta.setY(MENU_HEIGHT * 0.735f - (beta.getHeight() / 2.0f));
		
		this.prog.setX(MENU_WIDTH / 2.0f - (prog.getWidth() / 2.0f));
		this.prog.setY(BTNGameScreen.BOTTOM_BAR_BOTTOM + 1100.0f);
		
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
				
				bottomBar.addAction(Actions.moveTo(bottomBar.getX(), BTNGameScreen.BOTTOM_BAR_TOGETHER, 1.0f, Interpolation.pow4));
				topBar.addAction(Actions.moveTo(topBar.getX(), BTNGameScreen.TOP_BAR_TOGETHER, 1.0f, Interpolation.pow4));
				prog.addAction(Actions.moveTo(prog.getX(), BTNGameScreen.BOTTOM_BAR_TOGETHER + 1100.0f, 1.0f, Interpolation.pow4));
				
				beta.addAction(Actions.fadeOut(0.5f));
				
				prog.addAction(Actions.sequence(Actions.delay(1.0f), Actions.fadeIn(1.0f), Actions.run(new Runnable() {
					
					@Override
					public void run() {
						
						setAnimationComplete(true);
					}
				})));
				
				startGame.addAction(Actions.moveBy(0.0f, -500.0f, 1.0f, Interpolation.pow4));
			}
		});
		
		RepeatAction repeat = new RepeatAction();
		repeat.setAction(rotate);
		repeat.setCount(RepeatAction.FOREVER);
		
		stripes.addAction(repeat);
		
		menuStage.addActor(bg);
		menuStage.addActor(stripes);
		menuStage.addActor(zombie);
		menuStage.addActor(topBar);
		menuStage.addActor(bottomBar);
		menuStage.addActor(title);
		menuStage.addActor(beta);
		menuStage.addActor(prog);
		menuStage.addActor(startGame);
		
		Gdx.input.setInputProcessor(menuStage);
	}

	@Override
	public void render(float delta) {
		
		Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(gameScreen.getAssetManager().update() && isAnimationComplete()){
        	
        	gameScreen.getAssetManager().finishLoading();
        	
        	title.addAction(Actions.moveBy(0.0f, 500.0f, 1.0f, Interpolation.pow4));
        	prog.addAction(Actions.sequence(Actions.moveBy(0.0f, -500.0f, 1.0f, Interpolation.pow4), Actions.run(new Runnable() {
				
				@Override
				public void run() {
					
					game.setScreen(gameScreen);
				}
			})));
        }
        	
        prog.setPercentDraw(gameScreen.getAssetManager().getProgress());
        
//        prog.setValue(0.5f);
        
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

	public AssetManager getAssetManager() {
		
		return assetManager;
	}
}
