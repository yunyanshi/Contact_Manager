import javax.swing.*;
import java.awt.*;

public class TabButton extends JRadioButton {

    public TabButton(String text) {
        super(text);
        this.setFont(new Font("Courier",Font.PLAIN,18));
        this.setForeground(ContactsGUI.darkGray);
    }
}
