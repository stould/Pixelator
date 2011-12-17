package com.pixelator.io;

import java.awt.image.BufferedImage;

import com.pixelator.utils.DrawStack;

public class Image {
	private BufferedImage image;
	private DrawStack drawStack;
	private int height;
	private int width;
	
	public Image(BufferedImage image, DrawStack drawStack, int height, int width) {	
		this.image = new BufferedImage(height, width, BufferedImage.TYPE_INT_RGB);;
		this.drawStack = drawStack;
		this.height = height;
		this.width = width;
	}

	public BufferedImage getImage() {
		return image;
	}

	public DrawStack getDrawStack() {
		return drawStack;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}	
}
