package com.bopthenazi.utils;

public class Action {

	public static enum ActionType{
		
		TOUCH_DOWN, TOUCH_UP, TOUCH_DRAGGED
	}
	
	private ActionType actionType;
	
	private float eventX;
	private float eventY;
	
	private volatile boolean consumed;

	private boolean acting;
	
	public Action(){
		
		// TODO: Implementation.
	}
	
	public Action(float eventX, float eventY, ActionType actionType){
	
		this.setEventX(eventX);
		this.setEventY(eventY);
		
		this.setActionType(actionType);
		this.setConsumed(false);
	}

	/**
	 * @return the eventX
	 */
	public float getEventX() {
		
		return eventX;
	}

	/**
	 * @param eventX the eventX to set
	 */
	public void setEventX(float eventX) {
		
		this.eventX = eventX;
	}

	/**
	 * @return the eventY
	 */
	public float getEventY() {
		
		return eventY;
	}

	/**
	 * @param eventY the eventY to set
	 */
	public void setEventY(float eventY) {
		
		this.eventY = eventY;
	}

	/**
	 * @return the actionType
	 */
	public ActionType getActionType() {
		
		return actionType;
	}

	/**
	 * @param actionType the actionType to set
	 */
	public void setActionType(ActionType actionType) {
		
		this.actionType = actionType;
	}

	/**
	 * @return the consumed
	 */
	public synchronized boolean isConsumed() {
		
		return consumed;
	}

	/**
	 * @param consumed the consumed to set
	 */
	public synchronized void setConsumed(boolean consumed) {
		
		this.consumed = consumed;
	}

	public void setActing(boolean b) {
		
		this.acting = b;
	}
	
	public boolean getActing(){
		
		return acting;
	}
}
