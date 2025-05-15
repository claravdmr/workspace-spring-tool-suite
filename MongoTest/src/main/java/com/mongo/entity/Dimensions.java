package com.mongo.entity;


public class Dimensions {

	private int height;
	private int width;
	private int depth;
	
	public int getHeight() {
		return height;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public int getWidth() {
		return width;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public int getDepth() {
		return depth;
	}
	
	public void setDepth(int depth) {
		this.depth = depth;
	}

	@Override
	public String toString() {
		return "Dimensions [height=" + height + ", width=" + width + ", depth=" + depth + "]";
	}
	
	
	
}
