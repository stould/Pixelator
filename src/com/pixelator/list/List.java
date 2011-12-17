package com.pixelator.list;

import java.awt.image.BufferedImage;

public class List {
	private Node head;
	private Node tail;
	public static int length;

	public List() {
		this.head = null;
		this.tail = null;
		length = 0;
	}

	public boolean isEmpty() {
		return this.head == null;
	}

	public void addFirst(BufferedImage value) {
		Node element = new Node(value);
		if (isEmpty()) {
			this.head = element;
			this.tail = element;
		} else {
			element.setNext(head);
			head.setPrev(element);
			this.head = element;
		}
		length++;
	}

	public void addEnd(BufferedImage value) {
		Node element = new Node(value);
		if (isEmpty()) {
			this.head = element;
			this.tail = element;
		} else {
			element.setPrev(tail);
			tail.setNext(element);
			tail = element;
		}
		length++;
	}

	public boolean removeFirst() {
		boolean ret = false;
		if (!isEmpty()) {
			if (length == 1) {
				this.head = null;
				this.tail = null;
				ret = true;
			} else {
				Node next = head.getNext();
				next.setPrev(null);
				head = next;
				ret = true;
			}
			length--;
		}
		return ret;
	}

	public boolean removeLast() {
		boolean ret = false;
		if (!isEmpty()) {
			if (length == 1) {
				this.head = null;
				this.tail = null;
				ret = true;
			} else {
				Node prev = tail.getPrev();
				prev.setNext(null);
				tail = prev;
				ret = true;
			}
			length--;
		}
		return ret;
	}

	public void clear() {
		this.head = null;
		this.tail = null;
		length = 0;
	}

	public BufferedImage getFirst() {
		return this.head.getValue();
	}
}
