package com.pixelator.utils;

import java.awt.image.BufferedImage;

import com.pixelator.list.List;

public class DrawStack {
	private List allForms;

	public int length() {
		return List.length;
	}

	public DrawStack() {
		this.allForms = new List();
	}

	public void addForm(BufferedImage bf) {
		if (length() > 129) {
			this.allForms.removeLast();
		}
		this.allForms.addFirst(bf);
	}

	public void clearForms() {
		if (!isEmpty())
			allForms.clear();
	}

	public BufferedImage peek() {
		return isEmpty() ? null : allForms.getFirst();
	}

	public void pop() {
		if (!isEmpty()) {
			allForms.removeFirst();
		}
	}

	public boolean isEmpty() {
		return allForms.isEmpty();
	}
}
