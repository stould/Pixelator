package com.pixelator.io;

import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileGenerator {
	private Image image;
	private Graphics gaphics;

	public FileGenerator(final Image image) {
		this.image = image;
		this.gaphics = image.getImage().createGraphics();
		if (!image.getDrawStack().isEmpty()) {
			gaphics.drawImage(image.getDrawStack().peek(), 0, 0, null);
		}
	}

	public void export(String extension, File file) {
		try {			
			ImageIO.write(this.image.getImage(), extension, new File(file.getPath()));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "There was a Problem Exporting the Image, Please report the bug to the Developers");
		}
	}

	public void save() {
		try {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setDialogTitle("Save");
			fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("JPG", ".jpg"));
			fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("BMP", ".bmp"));
			fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("PNG", ".png"));
			fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("GIF", ".gif"));
		
			fileChooser.setApproveButtonText("Save");
			fileChooser.setAcceptAllFileFilterUsed(true);
			
			if (fileChooser.showOpenDialog(fileChooser) == JFileChooser.CANCEL_OPTION)
				return;			
			fileChooser.setVisible(true);			
			String type = fileChooser.getFileFilter().getDescription();
			
			while(type.equals("All Files")) {
				if (fileChooser.showOpenDialog(fileChooser) == JFileChooser.CANCEL_OPTION) {					
					return;
				}
				type = fileChooser.getFileFilter().getDescription();
			}
			export(type, fileChooser.getSelectedFile());
		} catch (NullPointerException e) {
			JOptionPane.showMessageDialog(null, "There was a Problem Saving the Image, Please report the bug to the Developers");
		}
	}
}
