package com.bopthenazi.utils;


public interface Collidable {

	public abstract void onCollide(Collidable partner);
	
	public abstract boolean canCollide();
	public abstract void setCollide(boolean collide);
}
