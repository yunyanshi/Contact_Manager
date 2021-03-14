import javax.swing.*;
import java.awt.*;

public class ContactEntryButton extends JButton {
    public ContactEntryButton() {
        super();
        this.setFont(new Font("Courier",Font.PLAIN,16));
        this.setFocusPainted(true);
        this.setForeground(ContactsGUI.darkGray);
    }
}
