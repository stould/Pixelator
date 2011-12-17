package com.pixelator.draws;

import java.awt.Color;
import java.awt.Point;

public class CanPaint {
	private Point p;
	private Color ActualColor;
	private Color ReplacementColor;

	public CanPaint(Point p, Color actualColor, Color replacementColor) {
		this.p = p;
		ActualColor = actualColor;
		ReplacementColor = replacementColor;
	}

	public Point getP() {
		return p;
	}

	public void setP(int x, int y) {
		p.move(x, y);
	}

	public void setActualColor(Color actualColor) {
		ActualColor = actualColor;
	}

	public void setReplacementColor(Color replacementColor) {
		ReplacementColor = replacementColor;
	}

	public Color getActualColor() {
		return ActualColor;
	}

	public Color getReplacementColor() {
		return ReplacementColor;
	}

}
