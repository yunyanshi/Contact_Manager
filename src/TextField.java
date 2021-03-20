import javax.swing.*;
import java.awt.*;

public class TextField extends JTextField {

    public TextField() {
        super();
        this.setFont(ContactsGUI.courier16Font);
        this.setBorder(null);
        this.setForeground(Color.BLACK);
        this.setEditable(false);
    }

}
