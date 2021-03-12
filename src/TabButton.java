import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class TabButton extends JButton {

    public TabButton(String text) {
        super(text);
        this.setFont(new Font("Segoe UI",Font.PLAIN,15));
        this.setFocusPainted(true);
//        this.setBorder(BorderFactory.createEtchedBorder());

    }

    protected void paintComponent(Graphics g) {
        Graphics2D 	g2 = (Graphics2D) g;
        g2.setColor(new Color(238, 238, 238));
        g2.fillRect(getX(), getY(), getWidth(), getHeight());
        super.paintComponent(g2);
    }
}
