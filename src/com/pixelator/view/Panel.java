package com.pixelator.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Stack;

import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import com.pixelator.draws.CanPaint;
import com.pixelator.draws.Circle;
import com.pixelator.draws.Eraser;
import com.pixelator.draws.Line;
import com.pixelator.draws.Pen;
import com.pixelator.draws.Polyline;
import com.pixelator.draws.Square;

public class Panel extends JPanel {
	private static final long serialVersionUID = 1L;
	private Dimension dimension;
	private BufferedImage imageBuffer;
	private final BufferedImage defaultImage;
	public static boolean dragging = false;
	private SelectedForm objectDragging;
	private Object objectDragged;
	private boolean antiAliasing;

	public Panel(int sizeX, int sizeY) {
		this.dimension = new Dimension(sizeX, sizeY);
		setSize(dimension);
		setVisible(true);
		setBorder((Border) new LineBorder(new Color(0, 0, 0)));
		this.imageBuffer = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = imageBuffer.createGraphics();
		g2d.setColor(Color.WHITE);
		g2d.fillRect(1, 1, 800 - 2, 600 - 2);
		g2d.setColor(Color.BLACK);
		g2d.dispose();
		setBackground(Color.WHITE);
		setFocusable(true);
		this.defaultImage = deepCopy(imageBuffer);
	}

	public void tempDraw(SelectedForm type, Object form) {
		if (dragging) {
			objectDragging = type;
			objectDragged = form;
			repaint();
		}
	}

	public void draw(SelectedForm type, Object form) {
		Graphics2D copy = imageBuffer.createGraphics();
		if (antiAliasing && type != SelectedForm.POLYLINE)
			copy.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		if (type == SelectedForm.LINE) {
			Line l = (Line) form;
			copy.setColor(l.getColor());
			copy.drawLine(l.getP1().x, l.getP1().y, l.getP2().x, l.getP2().y);
		} else if (type == SelectedForm.SQUARE) {
			Square sq = (Square) form;
			copy.setColor(sq.getColor());
			copy.drawRect(sq.adjustRendering().x, sq.adjustRendering().y, sq.getWidth(), sq.getHeight());
		} else if (type == SelectedForm.CIRCLE) {
			Circle c = (Circle) form;
			copy.setColor(c.getColor());
			copy.drawOval(c.adjustRendering().x, c.adjustRendering().y, c.getWidth(), c.getHeight());
		} else if (type == SelectedForm.ERASER) {
			Eraser e = (Eraser) form;
			e.adjustPreferedSize(e.getP1());
			copy.fillOval(e.getP1().x, e.getP1().y, e.getSize(), e.getSize());
		} else if (type == SelectedForm.CANPAINT) {
			CanPaint cp = (CanPaint) form;
			floodFill(cp.getP().x, cp.getP().y, cp.getActualColor(), cp.getReplacementColor());
		} else if (type == SelectedForm.POLYLINE) {
			Polyline poly = (Polyline) form;
			copy.setColor(poly.getColor());
			poly.fill();
			int[] XIS = poly.getX();
			int[] YSP = poly.getY();
			copy.drawPolyline(XIS, YSP, poly.getN());
		} else if (type == SelectedForm.PEN) {
			Pen pen = (Pen) form;
			copy.setColor(pen.getColor());
			copy.drawLine(pen.getP1().x, pen.getP1().y, pen.getP2().x, pen.getP2().y);
		}
		copy.dispose();
		repaint();
	}

	public Color getPixelColor(int x, int y) {
		return new Color(imageBuffer.getRGB(x, y));
	}

	public BufferedImage getDefault() {
		return this.defaultImage;
	}

	public BufferedImage deepCopy() {
		return new BufferedImage(imageBuffer.getColorModel(), imageBuffer.copyData(null), imageBuffer.isAlphaPremultiplied(), null);
	}

	public BufferedImage deepCopy(BufferedImage bi) {
		return new BufferedImage(bi.getColorModel(), bi.copyData(null), bi.isAlphaPremultiplied(), null);
	}

	public void setAntiAliasing(boolean antiAliasing) {
		this.antiAliasing = antiAliasing;
	}

	public void clear() {
		this.imageBuffer = deepCopy(getDefault());
		repaint();
	}

	public void setBuffer(BufferedImage bi) {
		this.imageBuffer = deepCopy(bi);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D copy = (Graphics2D) g.create();
		if (dragging) {
			copy.drawImage(imageBuffer, 0, 0, null);
			if (objectDragging == SelectedForm.LINE) {
				Line l = (Line) objectDragged;
				copy.setColor(l.getColor());
				copy.drawLine(l.getP1().x, l.getP1().y, l.getP2().x, l.getP2().y);
			} else if (objectDragging == SelectedForm.SQUARE) {
				Square sq = (Square) objectDragged;
				copy.setColor(sq.getColor());
				copy.drawRect(sq.adjustRendering().x, sq.adjustRendering().y, sq.getWidth(), sq.getHeight());
			} else if (objectDragging == SelectedForm.CIRCLE) {
				Circle c = (Circle) objectDragged;
				copy.setColor(c.getColor());
				copy.drawOval(c.adjustRendering().x, c.adjustRendering().y, c.getWidth(), c.getHeight());
			} else if (objectDragging == SelectedForm.POLYLINE) {
				Polyline poly = (Polyline) objectDragged;
				copy.setColor(poly.getColor());
				poly.fill();
				int[] XIS = poly.getX();
				int[] YSP = poly.getY();
				copy.drawPolyline(XIS, YSP, poly.getN());
				copy.drawLine(XIS[poly.getN() - 1], YSP[poly.getN() - 1], poly.getActualX(), poly.getActualY());
			}
		} else {
			copy.drawImage(imageBuffer, 0, 0, null);
		}
		copy.dispose();
	}

	private int[] dx = { 0, -1, 0, 1 };
	private int[] dy = { -1, 0, 1, 0 };

	public void floodFillRecursive(int x, int y, Color target_color, Color replacement_color) {
		if (imageBuffer.getRGB(x, y) != target_color.getRGB()) {
			return;
		}
		imageBuffer.setRGB(x, y, replacement_color.getRGB());
		floodFillRecursive(x + dx[0], y + dy[0], target_color, replacement_color);
		floodFillRecursive(x + dx[1], y + dy[1], target_color, replacement_color);
		floodFillRecursive(x + dx[2], y + dy[2], target_color, replacement_color);
		floodFillRecursive(x + dx[3], y + dy[3], target_color, replacement_color);
	}

	public void floodFill(int x, int y, Color cur, Color rep) {
		int act = cur.getRGB();
		int repl = rep.getRGB();
		if (act == repl)
			return;
		Stack<Integer[]> s = new Stack<Integer[]>();
		s.add(new Integer[] { x, y });
		imageBuffer.setRGB(x, y, repl);
		while (!s.isEmpty()) {
			Integer[] pek = s.pop();
			imageBuffer.setRGB(pek[0], pek[1], repl);
			for (int i = 0; i < 4; ++i) {
				int ax = pek[0] + dx[i];
				int ay = pek[1] + dy[i];
				if (ax >= 0 && ay >= 0 && ax < 800 && ay < 600 && imageBuffer.getRGB(ax, ay) == act) {
					s.push(new Integer[] { ax, ay });
				}
			}
		}
	}

	public void reverseColor() {
		for (int x = 0; x < 800; x++) {
			for (int y = 0; y < 600; y++) {
				int aux = getPixelColor(x, y).getRGB();
				int red = 255 - ((aux >> 16) & 0xff);
				int green = 255 - ((aux >> 8) & 0xff);
				int blue = 255 - ((aux) & 0xff);
				imageBuffer.setRGB(x, y, new Color(red, green, blue).getRGB());
			}
		}
		repaint();
	}
}