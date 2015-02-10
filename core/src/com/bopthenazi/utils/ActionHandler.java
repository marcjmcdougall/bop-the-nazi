package com.bopthenazi.utils;

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
		
		System.out.println("Consuming Action Now! (" + actionQueue.size + ")");
		
		if(actionQueue.size > 1){
			
			System.out.println("Debug Start.");
		}
		
		actionQueue.peekSync().setConsumed(true);
	}
	
	@Override
	public void run() {
		
		super.run();
		
		while(running){
			
			// TODO: Implementation.
		}
	}
	
	public void addAction(Action a){
		
		System.out.println("Adding action now.");
		
		actionQueue.addSync(a);
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
}
