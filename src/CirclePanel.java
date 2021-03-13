import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;

import javax.swing.*;

public class CirclePanel extends JPanel {
	
	private static Color m_tGreen = new Color(0, 255, 0, 150);
	private static Font sanSerifFont = new Font("SanSerif", Font.PLAIN, 24);
	private String text;
	public CirclePanel(String text) {
		this.text = text;
	}
	
	public CirclePanel() {
		
	}
	
	protected void paintComponent(Graphics g) {
		// Adding super.paintComponent....
		super.paintComponent(g);
		//Graphics2D myGraphics = (Graphics2D)g;
		//Point frameCenter = new Point(50, 50);
		g.setColor(Color.white);
	    //g.fillRect(0, 0, 500, 50);
		g.setColor(m_tGreen);
		g.fillOval(25,25, 50,50);
		g.setColor(Color.black);
		FontMetrics fm = g.getFontMetrics();
		g.setFont(sanSerifFont);
	
		fm = g.getFontMetrics();
		g.drawString(text, 40, 52);	
	}
	
	public void setText(String text) {
		this.text = text;
		repaint();
	}
}
