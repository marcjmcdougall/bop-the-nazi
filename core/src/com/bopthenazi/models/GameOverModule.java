package com.bopthenazi.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.bopthenazi.views.screens.BTNGameScreen;

public class GameOverModule extends Group {

	private static final float HEIGHT_OFFSET = 200.0f;
	
	private static final String SCORE_PREPEND = "Last Time: ";
	private static final String HIGH_SCORE_PREPEND = "Record Time: ";
	
	private BTNGameScreen gameScreen;
	
	private BTNActor gameOverAlpha;
	private BTNActor background;
	private BTNActor gameOverImage;
	
	private Label scoreLabel;
	private Label highScoreLabel;
	private Label copyrightLabel;
	private Label reviewLabel;
	private Image pencil;
	
	private BasicButton restart;
	
	private float hiddenY;
	
	private float score;
	private float highScore;
	
	private boolean showing;
	
	public GameOverModule(BTNGameScreen gameScreen){
		
		super();
		
		this.gameScreen = gameScreen;
		
		initialize();
	}

	private void initialize() {
		
		this.setShowing(false);
		this.setVisible(false);
		
		gameOverAlpha = new BTNActor(gameScreen.getTexture("alpha-25"), BTNGameScreen.GAME_WIDTH / 2.0f, BTNGameScreen.GAME_HEIGHT / 2.0f, BTNGameScreen.GAME_WIDTH, BTNGameScreen.GAME_HEIGHT);
		background = new BTNActor(gameScreen.getTexture("game-over-box"), BTNGameScreen.GAME_WIDTH / 2.0f, BTNGameScreen.GAME_HEIGHT / 2.0f, BTNGameScreen.GAME_WIDTH * 0.75f, BTNGameScreen.GAME_HEIGHT * 0.6f);
		gameOverImage = new BTNActor(gameScreen.getTexture("game-over-text"), BTNGameScreen.GAME_WIDTH / 2.0f, BTNGameScreen.GAME_HEIGHT / 2.0f + HEIGHT_OFFSET, BTNGameScreen.GAME_WIDTH * 0.6f, BTNGameScreen.GAME_HEIGHT * 0.225f);
		
		copyrightLabel = new Label("Copyright 2015 Kilobyte Games", new LabelStyle(gameScreen.getAssetManager().get("masaaki-regular-40.otf", BitmapFont.class), new Color(0.0f, 0.0f, 0.0f, 1.0f)));
		copyrightLabel.setHeight(100.0f);
		copyrightLabel.setX(BTNGameScreen.GAME_WIDTH / 2.0f - (copyrightLabel.getWidth() / 2.0f));
		copyrightLabel.setY(0.0f);
		
		reviewLabel = new Label("Got feedback? Click here!", new LabelStyle(gameScreen.getAssetManager().get("masaaki-regular-70.otf", BitmapFont.class), new Color(1.0f, 1.0f, 1.0f, 1.0f)));
		reviewLabel.setHeight(100.0f);
		reviewLabel.setX(BTNGameScreen.GAME_WIDTH / 2.0f - (reviewLabel.getWidth() / 2.0f) - 50.0f);
		reviewLabel.setY(150.0f);
		
		reviewLabel.setY(reviewLabel.getY() - 1000.0f);
		
		reviewLabel.addListener(new InputListener(){
			
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				
				super.touchDown(event, x, y, pointer, button);
				
				return true;
			}
			
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				
				super.touchUp(event, x, y, pointer, button);
				
				Gdx.net.openURI("https://play.google.com/store/apps/details?id=com.kilobytegames.zombiebop.android");
			}
		});
		
		pencil = new Image(gameScreen.getTexture("pencil"));
		pencil.setSize(100.0f, 90.0f);
		
		pencil.setPosition(BTNGameScreen.GAME_WIDTH / 2.0f - (pencil.getWidth() / 2.0f) + 440.0f, 170.0f);
		
		scoreLabel = new Label("Last Time: " + score, new LabelStyle(gameScreen.getAssetManager().get("masaaki-regular-80.otf", BitmapFont.class), new Color(0.0f, 0.0f, 0.0f, 1.0f)));
		scoreLabel.setHeight(100.0f);
		scoreLabel.setX(BTNGameScreen.GAME_WIDTH / 2.0f - (scoreLabel.getWidth() / 2.0f));
		scoreLabel.setY((590.0f + (scoreLabel.getHeight() / 2.0f)) + HEIGHT_OFFSET);
		
		highScoreLabel = new Label("Best Time: " + highScore, new LabelStyle(gameScreen.getAssetManager().get("masaaki-regular-80.otf", BitmapFont.class), new Color(0.0f, 0.0f, 0.0f, 1.0f)));
		highScoreLabel.setHeight(100.0f);
		highScoreLabel.setX(BTNGameScreen.GAME_WIDTH / 2.0f - (highScoreLabel.getWidth() / 2.0f));
		highScoreLabel.setY((490.0f + (highScoreLabel.getHeight() / 2.0f)) + HEIGHT_OFFSET);
		
		restart = new BasicButton(gameScreen.getTexture("restart-button"), gameScreen.getTexture("restart-button-down-state"), BTNGameScreen.GAME_WIDTH / 2.0f, BTNGameScreen.GAME_HEIGHT / 2.0f - 550.0f);
	
		restart.setHeight(BTNGameScreen.GAME_HEIGHT * 0.1f); 
		restart.setWidth(BTNGameScreen.GAME_WIDTH * 0.45f);
		restart.setX((BTNGameScreen.GAME_WIDTH / 2.0f) - (restart.getWidth() / 2.0f));
		restart.setY(restart.getY() - restart.getHeight() / 2.0f + HEIGHT_OFFSET);
		
		
		restart.addListener(new com.badlogic.gdx.scenes.scene2d.InputListener(){
			
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				
				gameScreen.onButtonClickDown();
				
				return true;
			}
			
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				
				super.touchUp(event, x, y, pointer, button);
				
				gameScreen.onButtonClickUp();
				
				gameOverAlpha.clearActions();
				copyrightLabel.clearActions();
				pencil.clearActions();
				reviewLabel.clearActions();
				
				copyrightLabel.addAction(Actions.fadeOut(0.5f));
				gameOverAlpha.addAction(Actions.fadeOut(1.0f));
				pencil.addAction(Actions.fadeOut(0.5f));
				reviewLabel.addAction(Actions.fadeOut(0.5f));
				
				GameOverModule.this.addAction(Actions.sequence(Actions.moveBy(0.0f, 2000.0f, 1.0f, Interpolation.pow4), Actions.run(new Runnable() {
					
					@Override
					public void run() {
						
						GameOverModule.this.setVisible(false);
						
						reviewLabel.setVisible(false);
						reviewLabel.setY(reviewLabel.getY() - 1000.0f);
						
						copyrightLabel.getColor().a = 0.0f;
						gameOverAlpha.getColor().a = 0.0f;
						pencil.getColor().a = 0.0f;
						reviewLabel.getColor().a = 0.0f;
					}
				})));
				
				gameScreen.reset();
			}
		});
		
		this.getColor().a = 0.0f;
		gameOverAlpha.getColor().a = 0.0f;
		copyrightLabel.getColor().a = 0.0f;
		pencil.getColor().a = 0.0f;
		reviewLabel.getColor().a = 0.0f;
		
		this.addActor(background);
		this.addActor(gameOverImage);
		this.addActor(scoreLabel);
		this.addActor(highScoreLabel);
		this.addActor(restart);
		
		this.addActor(copyrightLabel);
		
		this.hiddenY = getY() + 2000.0f;
	}
	
	public void doAnimate(){
		
		setShowing(true);
		setVisible(true);
		reviewLabel.setVisible(true);
		
		reviewLabel.setY(reviewLabel.getY() + 1000.0f);
		
		MoveToAction moveOutInstant = Actions.moveTo(getX(), hiddenY);
		MoveByAction moveIn = Actions.moveBy(0.0f, -2000.0f, 1.0f, Interpolation.pow4);
		
		this.addAction(moveOutInstant);
		this.addAction(moveIn);
		this.addAction(Actions.fadeIn(1.0f));
		gameOverAlpha.addAction(Actions.sequence(Actions.fadeIn(1.0f), Actions.run(new Runnable() {
			
			@Override
			public void run() {
				
				copyrightLabel.addAction(Actions.fadeIn(1.0f));
				reviewLabel.addAction(Actions.fadeIn(1.0f));
				pencil.addAction(Actions.fadeIn(1.0f));
			}
		})));
	}
	
	public void setScores(float score, float highScore){
		
		this.score = score;
		this.highScore = highScore;
		
		updateLabels();
	}
	
	private void updateLabels(){
		
		String scoreText = "" + score;
		String highScoreText = "" + highScore;
		
		scoreText = scoreText.substring(0, scoreText.indexOf(".") + 2);
		
		scoreLabel.setText(SCORE_PREPEND + scoreText);
		scoreLabel.setX(BTNGameScreen.GAME_WIDTH / 2.0f - (scoreLabel.getTextBounds().width / 2.0f));
		
		highScoreText = highScoreText.substring(0, highScoreText.indexOf(".") + 2);
		
		highScoreLabel.setText(HIGH_SCORE_PREPEND + highScoreText);
		highScoreLabel.setX(BTNGameScreen.GAME_WIDTH / 2.0f - (highScoreLabel.getTextBounds().width / 2.0f));
	}

	public boolean isShowing() {
		
		return showing;
	}

	public void setShowing(boolean showing) {
		
		this.showing = showing;
	}

	public BTNActor getGameOverAlpha() {
		
		return gameOverAlpha;
	}
	
	public Label getCopyrightLabel() {
		
		return copyrightLabel;
	}

	public Image getPencil() {
		
		return pencil;
	}

	public Label getReviewLabel(){
		
		return reviewLabel;
	}
}
