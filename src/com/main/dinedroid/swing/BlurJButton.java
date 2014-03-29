package com.main.dinedroid.swing;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextPane;



public class BlurJButton extends JButton
{
	public BlurJButton() {
		super();
	}

	public void paintComponent(Graphics g) {
		if(isEnabled()) {
		super.paintComponent(g);
		return;

	}
	BufferedImage buf = new BufferedImage(getWidth(),getHeight(),
			BufferedImage.TYPE_INT_ARGB);        
	super.paintComponent(buf.getGraphics());
	float[] my_kernel = {
			0.10f, 0.10f, 0.10f,
			0.10f, 0.04f, 0.10f,
			0.10f, 0.10f, 0.10f };
		ConvolveOp op = new ConvolveOp(new Kernel(3,3, my_kernel));
		BufferedImage img = op.filter(buf,null);
		g.drawImage(img,0,0,null);
	} 
}

