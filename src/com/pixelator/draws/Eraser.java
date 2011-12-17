package com.pixelator.draws;

import java.awt.Color;
import java.awt.Point;

import com.pixelator.utils.AbstractForm;

public class Eraser extends AbstractForm {
	public static final int DEFAULT_SIZE = 15;
	private int size;

	public Eraser(Point start, int size, Color color) {
		super(start, color);
		this.size = size;
		adjustPreferedSize(p1);
	}
	
	public Eraser(Point start, Color color) {
		super(start, color);
		this.size = DEFAULT_SIZE;
		adjustPreferedSize(p1);
	}

	public void setSize(int size) {
		this.size = size;
	}

	public void setP1(int x, int y) {
		p1.move(x, y);
	}

	public int getSize() {
		return size;
	}

	public void adjustPreferedSize(Point p) {
		p.move((int) (p.getX() - Math.ceil(((double) size / 2.))), (int) (p.getY() - Math.ceil(((double) size / 2.))));
	}
}