package com.bopthenazi.utils;

public class Action {

	private float eventX;
	private float eventY;

	public Action(){
		
		// TODO: Implementation.
	}
	
	public Action(float eventX, float eventY){
	
		this.setEventX(eventX);
		this.setEventY(eventY);
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
}
