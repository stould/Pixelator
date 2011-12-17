package com.pixelator.draws;

import java.awt.Color;
import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

import com.pixelator.utils.AbstractForm;

public class Polyline extends AbstractForm {
	private List<Point> points;
	private int[] x, y;
	private int n;

	public Polyline(Color c) {
		super(c);
		this.points = new LinkedList<Point>();
		n = 0;
	}

	public int[] getX() {
		return x;
	}

	public int[] getY() {
		return y;
	}

	public int getN() {
		return n;
	}

	public void fill() {
		int n = points.size();
		x = new int[n];
		y = new int[n];
		for (int i = 0; i < n; ++i) {
			Point aux = points.get(i);
			x[i] = aux.x;
			y[i] = aux.y;
		}
	}

	public void add(int x, int y) {
		points.add(new Point(x, y));
		++n;
	}

	public void removeLast() {
		if (n - 1 >= 0)
			points.remove(--n);
	}

	public void clear() {
		if (!points.isEmpty())
			points.clear();
		n = 0;
	}
}
