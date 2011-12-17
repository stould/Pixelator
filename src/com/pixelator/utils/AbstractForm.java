package com.pixelator.utils;

import java.awt.Color;
import java.awt.Point;

public abstract class AbstractForm {
	protected Point p1;
	protected Point p2;
	protected Color color;
	private int actualX, actualY;

	public AbstractForm(Point p1, Point p2, Color color) {
		this.p1 = p1;
		this.p2 = p2;
		this.color = color;
	}

	public AbstractForm(Point p, Color color) {
		this.p1 = p;
		this.color = color;
	}

	public AbstractForm(Color color) {
		this.color = color;
	}

	public void setP1(Point p1) {
		this.p1 = p1;
	}

	public Point getP1() {
		return p1;
	}

	public Point getP2() {
		return p2;
	}

	public void setP2(Point p2) {
		this.p2 = p2;
	}

	public void setColor(Color c) {
		this.color = c;
	}

	public void setP1(int x, int y) {
		p1.move(x, y);
	}

	public void setP2(int x, int y) {
		p2.move(x, y);
	}

	public Color getColor() {
		return color;
	}

	public int getActualX() {
		return actualX;
	}

	public void setActualX(int actualX) {
		this.actualX = actualX;
	}

	public int getActualY() {
		return actualY;
	}

	public void setActualY(int actualY) {
		this.actualY = actualY;
	}

	public int getHeight() {
		return Math.abs(p2.y - p1.y);
	}

	public int getWidth() {
		return Math.abs(p2.x - p1.x);
	}

	public Point adjustRendering() {
		if (p1.x > p2.x && p1.y >= p2.y) {
			return new Point(p2.x, p2.y);
		} else if (p1.x > p2.x && p1.y < p2.y) {
			return new Point(p2.x, p1.y);
		} else if (p1.x <= p2.x && p1.y > p2.y) {
			return new Point(p1.x, p2.y);
		}
		return new Point(p1.x, p1.y);
	}
}