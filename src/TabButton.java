import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class TabButton extends JRadioButton {

    public TabButton(String text) {
        super(text);
        this.setFont(new Font("Courier",Font.PLAIN,18));
        this.setForeground(ContactsGUI.darkGray);
    }
//
//    protected void paintComponent(Graphics g) {
//        Graphics2D 	g2 = (Graphics2D) g;
//        g2.setColor(new Color(238, 238, 238));
//        g2.fillRect(getX(), getY(), getWidth(), getHeight());
//        super.paintComponent(g2);
//    }
}
