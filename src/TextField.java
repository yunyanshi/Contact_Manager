import javax.swing.*;
import java.awt.*;

public class TextField extends JTextField {
    public TextField() {
        super();
        this.setFont(new Font("Courier", Font.PLAIN, 16));
        this.setBorder(null);
        this.setForeground(Color.BLACK);
        this.setEditable(false);
    }

}
