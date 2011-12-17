package com.pixelator.list;

import java.awt.image.BufferedImage;

public class Node {
	private Node next;
	private Node prev;
	private BufferedImage value;

	public Node(BufferedImage value) {
		next = null;
		prev = null;
		this.value = value;
	}

	public Node getNext() {
		return next;
	}

	public Node getPrev() {
		return prev;
	}

	public BufferedImage getValue() {
		return value;
	}

	public void setNext(Node next) {
		this.next = next;
	}

	public void setPrev(Node prev) {
		this.prev = prev;
	}

	public void setValue(BufferedImage value) {
		this.value = value;
	}
}
