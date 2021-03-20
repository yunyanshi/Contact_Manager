import javax.swing.*;
import java.awt.*;

public class Label extends JLabel {

    public Label(String text) {
        super(text);
        this.setForeground(Color.darkGray);
        this.setFont(ContactsGUI.courier16Font);

    }
}
