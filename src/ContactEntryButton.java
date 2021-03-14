import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ContactEntryButton extends JButton {

    public ContactEntryButton(String text) {
        super(text);
        super.setContentAreaFilled(false);
        this.setFont(new Font("Courier", Font.PLAIN, 18));
        this.setHorizontalAlignment(SwingConstants.LEFT);
        this.setBackground(Color.WHITE);
        this.setForeground(ContactsGUI.darkGray);
        this.setBorder(new EmptyBorder(5, 0, 5, 0));
    }
}
