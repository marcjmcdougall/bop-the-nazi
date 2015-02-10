package com.bopthenazi.utils;

import com.badlogic.gdx.utils.Array;

public class SynchronizedArray<T> extends Array<T> {

	public synchronized void addSync(T value){
		
		this.add(value);
	}
	
	public synchronized T peekSync(){
		
		return this.peek();
	}
	
	public synchronized T popSync(){
		
		return this.removeIndex(0);
	}
}
