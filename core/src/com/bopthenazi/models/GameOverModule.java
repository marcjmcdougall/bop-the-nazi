package com.bopthenazi.models;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.bopthenazi.utils.FontFactory;
import com.bopthenazi.views.screens.BTNGameScreen;

public class GameOverModule extends Group {

	private static final String SCORE_PREPEND = "Score: ";
	private static final String HIGH_SCORE_PREPEND = "High Score: ";
	
	private BTNGameScreen gameScreen;
	
	private BTNActor gameOverAlpha;
	private BTNActor background;
	private BTNActor gameOverImage;
	
	private Label scoreLabel;
	private Label highScoreLabel;
	
	private BasicButton restart;
	
	private float hiddenY;
	
	private int score;
	private int highScore;
	
	private boolean showing;
	
	public GameOverModule(BTNGameScreen gameScreen){
		
		super();
		
		this.gameScreen = gameScreen;
		
		initialize();
	}

	private void initialize() {
		
		this.setShowing(false);
		this.setVisible(false);
		
		gameOverAlpha = new BTNActor(gameScreen.getTexture("screen-game-over/alpha-25.png").getTexture(), BTNGameScreen.GAME_WIDTH / 2.0f, BTNGameScreen.GAME_HEIGHT / 2.0f, BTNGameScreen.GAME_WIDTH, BTNGameScreen.GAME_HEIGHT);
		background = new BTNActor(gameScreen.getTexture("screen-game-over/game-over-box.png").getTexture(), BTNGameScreen.GAME_WIDTH / 2.0f, BTNGameScreen.GAME_HEIGHT / 2.0f - 200.0f, BTNGameScreen.GAME_WIDTH * 0.75f, BTNGameScreen.GAME_HEIGHT * 0.6f);
		gameOverImage = new BTNActor(gameScreen.getTexture("screen-game-over/game-over-text.png").getTexture(), BTNGameScreen.GAME_WIDTH / 2.0f, BTNGameScreen.GAME_HEIGHT / 2.0f + 25.0f, BTNGameScreen.GAME_WIDTH * 0.6f, BTNGameScreen.GAME_HEIGHT * 0.225f);
		
		scoreLabel = new Label("Score: " + score, new LabelStyle(FontFactory.buildFont(80), new Color(0.0f, 0.0f, 0.0f, 1.0f)));
		scoreLabel.setHeight(100.0f);
		scoreLabel.setX(BTNGameScreen.GAME_WIDTH / 2.0f - (scoreLabel.getWidth() / 2.0f));
		scoreLabel.setY((590.0f + (scoreLabel.getHeight() / 2.0f)));
		
		highScoreLabel = new Label("High Score: " + highScore, new LabelStyle(FontFactory.buildFont(80), new Color(0.0f, 0.0f, 0.0f, 1.0f)));
		highScoreLabel.setHeight(100.0f);
		highScoreLabel.setX(BTNGameScreen.GAME_WIDTH / 2.0f - (highScoreLabel.getWidth() / 2.0f));
		highScoreLabel.setY((490.0f + (highScoreLabel.getHeight() / 2.0f)));
		
		restart = new BasicButton(new Texture("textures/screen-game-over/restart-button.png"), new Texture("textures/screen-game-over/restart-button-down-state.png"), BTNGameScreen.GAME_WIDTH / 2.0f, BTNGameScreen.GAME_HEIGHT / 2.0f - 550.0f);
	
		restart.setHeight(BTNGameScreen.GAME_HEIGHT * 0.1f); 
		restart.setWidth(BTNGameScreen.GAME_WIDTH * 0.45f);
		restart.setX((BTNGameScreen.GAME_WIDTH / 2.0f) - (restart.getWidth() / 2.0f));
		restart.setY(restart.getY() - restart.getHeight() / 2.0f);
		
		restart.addListener(new com.badlogic.gdx.scenes.scene2d.InputListener(){
			
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				
				return true;
			}
			
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				
				super.touchUp(event, x, y, pointer, button);
				
				gameScreen.reset();
				
				gameOverAlpha.addAction(Actions.fadeOut(1.0f));
				GameOverModule.this.addAction(Actions.sequence(Actions.moveBy(0.0f, 2000.0f, 1.0f, Interpolation.pow4), Actions.run(new Runnable() {
					
					@Override
					public void run() {
						
						GameOverModule.this.setVisible(false);
					}
				})));
				
//				TODO: Move to BTNGameScreen#reset()
//				BTNGameScreen.this. score.addAction(Actions.fadeOut(1.0f));
			}
		});
		
		this.getColor().a = 0.0f;
		gameOverAlpha.getColor().a = 0.0f;
		
		this.addActor(background);
		this.addActor(gameOverImage);
		this.addActor(scoreLabel);
		this.addActor(highScoreLabel);
		this.addActor(restart);
		
		this.hiddenY = getY() + 2000.0f;
	}
	
	public void doAnimate(){
		
		setShowing(true);
		setVisible(true);
		
		MoveToAction moveOutInstant = Actions.moveTo(getX(), hiddenY);
		MoveByAction moveIn = Actions.moveBy(0.0f, -2000.0f, 1.0f, Interpolation.pow4);
		
		this.addAction(moveOutInstant);
		this.addAction(moveIn);
		this.addAction(Actions.fadeIn(1.0f));
		gameOverAlpha.addAction(Actions.fadeIn(1.0f));
	}
	
	public void setScores(int score, int highScore){
		
		this.score = score;
		this.highScore = highScore;
		
		updateLabels();
	}
	
	private void updateLabels(){
		
		scoreLabel.setText(SCORE_PREPEND + score);
		scoreLabel.setX(BTNGameScreen.GAME_WIDTH / 2.0f - (scoreLabel.getWidth() / 2.0f));
		
		highScoreLabel.setText(HIGH_SCORE_PREPEND + highScore);
		highScoreLabel.setX(BTNGameScreen.GAME_WIDTH / 2.0f - (highScoreLabel.getWidth() / 2.0f));
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
}
