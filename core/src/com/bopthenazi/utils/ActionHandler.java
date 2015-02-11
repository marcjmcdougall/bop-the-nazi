package com.bopthenazi.utils;

import com.badlogic.gdx.Gdx;
import com.bopthenazi.game.BTNGame;
import com.bopthenazi.views.screens.BTNGameScreen;

public class ActionHandler extends Thread {

	private volatile SynchronizedArray<Action> actionQueue;
	
	private BTNGameScreen screen;
	
	private boolean running;
	
	public ActionHandler(BTNGameScreen screen){
		
		this.screen = screen;
		this.actionQueue = new SynchronizedArray<Action>();
	}
	
	public void consume() {
		
		actionQueue.peekSync().setConsumed(true);
	}
	
	@Override
	public void run() {
		
		super.run();
		
		while(running){
			
			if(screen.getGlove().getCurrentAction() == null){
				
				if(actionQueue.size > 0){
					
					screen.getGlove().injectNewAction(actionQueue.popSync());
				}
			}
		}
		
		Gdx.app.log(BTNGame.TAG, "ActionHandler thread exiting now.");
	}
	
	public void addAction(Action a){
		
		if(actionQueue.size < 1){
			
			actionQueue.addSync(a);
			
			System.out.println("Adding action now: " + actionQueue);
		}
	}

	/**
	 * @return the running
	 */
	public boolean isRunning() {
		
		return running;
	}

	/**
	 * @param running the running to set
	 */
	public void setRunning(boolean running) {
		
		this.running = running;
	}

	public SynchronizedArray<Action> getActionQueue() {
		return actionQueue;
	}
}
