// This is another test.
// This is the third test.
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import com.github.lgooddatepicker.components.DatePicker;

public class ContactsGUI {
    protected static Color darkGray = new Color(64,64,64);
    private JFrame window, newContactWindow;
    private Panel topPanel, leftPanel, rightPanel, tabPanel, contactListPanel, newContactTopPanel;
    private DBConnection connection;
    private HashMap<ContactEntryButton, Integer> buttonMap;
    JScrollPane contactListScrollPane;
    private JTextField nameTextField, emailTextField, addressTextField, phoneNumberTextField, notesTextField;
    private JCheckBox isFavorite, isFamily, isFriend;
    private DatePicker birthdayPicker;
    private String tabSelected = "Contacts";


    public ContactsGUI() {
        connection = new DBConnection();
        initComponents();
    }

    private void initComponents() {
        window = new JFrame();
        createPanels();
        window.setContentPane(topPanel);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(1000, 500);
        window.setTitle("Contacts");
        window.setVisible(true);
    }

    private void createPanels() {
        topPanel = new Panel();
        topPanel.setLayout(new GridLayout(1, 2));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        createLeftPanel();
        topPanel.add(leftPanel);
        createRightPanel();
        topPanel.add(rightPanel);
    }

    private void createLeftPanel() {
        leftPanel = new Panel();
        leftPanel.setLayout(new BorderLayout());
        createTabPanel();
        leftPanel.add(tabPanel, BorderLayout.WEST);
        createContactListPanel();
        leftPanel.add(contactListScrollPane, BorderLayout.CENTER);
    }
    
    private void createTabPanel() {
        tabPanel = new Panel();
        tabPanel.setBorder(BorderFactory.createEtchedBorder());
        tabPanel.setLayout(new BoxLayout(tabPanel, BoxLayout.Y_AXIS));

        ButtonGroup tabsGroup = new ButtonGroup();

        Panel tabLabelPanel = new Panel();
        tabLabelPanel.setLayout(new GridLayout(4, 1));

        TabButton allContactsTab = new TabButton("All Contacts");
        allContactsTab.setSelected(true);
        tabsGroup.add(allContactsTab);
        tabLabelPanel.add(allContactsTab);
        allContactsTab.addActionListener(e -> { tabSelected = "Contacts"; reloadContactListPanel(); });

        TabButton favoritesTab = new TabButton("Favorites");
        tabsGroup.add(favoritesTab);
        tabLabelPanel.add(favoritesTab);
        favoritesTab.addActionListener(e -> { tabSelected = "Favorites"; reloadContactListPanel(); });

        TabButton familyTab = new TabButton("Family");
        tabsGroup.add(familyTab);
        tabLabelPanel.add(familyTab);
        familyTab.addActionListener(e -> { tabSelected = "Family"; reloadContactListPanel(); });

        TabButton friendsTab = new TabButton("Friends");
        tabsGroup.add(friendsTab);
        tabLabelPanel.add(friendsTab);
        friendsTab.addActionListener(e -> { tabSelected = "Friends"; reloadContactListPanel(); });

        tabPanel.add(tabLabelPanel);
        tabPanel.add(Box.createVerticalStrut(300));

        JButton addContactButton = new JButton("+");
        addContactButton.setToolTipText("New Contact");
        addContactButton.setFont(new Font("Courier",Font.PLAIN,18));
        addContactButton.setForeground(darkGray);
        addContactButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        addContactButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addContactActionPerformed(e);
            }
        });
        tabPanel.add(addContactButton);
    }
    
    private void createContactListPanel() {
        contactListPanel = new Panel();
        contactListPanel.setBorder(new EmptyBorder(0, 10, 0, 10));
        contactListPanel.setLayout(new GridLayout(0, 1));

        contactListScrollPane = new JScrollPane(contactListPanel);
        contactListScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        contactListScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        reloadContactListPanel();
    }
    
    public void reloadContactListPanel()  {
    	contactListPanel.removeAll();
    	contactListPanel.repaint();
    	contactListPanel.revalidate();

        buttonMap = new HashMap<>();
        ResultSet contactListResultSet = connection.getContactListResultSet(tabSelected);
        if (contactListResultSet != null) {
            try {
                while (contactListResultSet.next()) {
                    int user_id = contactListResultSet.getInt(1);
                    String name = contactListResultSet.getString(2);
                    ContactEntryButton button = new ContactEntryButton(name);
                    buttonMap.put(button, user_id);

                    contactListPanel.add(button);
                    button.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {


                            ResultSet contactInfoResultSet = connection.getContactInfoResultSet(user_id);
                            nameTextField.setText(name);
                            try {
                                while (contactInfoResultSet.next()) {
                                    phoneNumberTextField.setText(contactInfoResultSet.getString("phone_number"));
                                    emailTextField.setText((contactInfoResultSet.getString("email")));
                                    Date birthday = contactInfoResultSet.getDate("birthday");
                                    birthdayPicker.setDate(birthday.toLocalDate());
                                    addressTextField.setText(contactInfoResultSet.getString("address"));
                                    notesTextField.setText(contactInfoResultSet.getString("notes"));
                                }
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }
                            if (connection.ifBelongs(user_id, "Favorites")) {
                                isFavorite.setSelected(true);
                            } else {
                                isFavorite.setSelected(false);
                            }
                            if (connection.ifBelongs(user_id, "Family")) {
                                isFamily.setSelected(true);
                            } else {
                                isFamily.setSelected(false);
                            }
                            if (connection.ifBelongs(user_id, "Friends")) {
                                isFriend.setSelected(true);
                            } else {
                                isFriend.setSelected(false);
                          }
                        }
                    });
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
    
    private void createRightPanel() {
        rightPanel = new Panel();
        rightPanel.setBorder(BorderFactory.createEtchedBorder());
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        Panel namePanel = new Panel();
        namePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        nameTextField = new JTextField(20);
        nameTextField.setEditable(false);
        nameTextField.setBorder(null);
        nameTextField.setBackground(new Color(238, 238, 238));
        nameTextField.setFont(new Font("Serif", Font.BOLD,25));
        namePanel.add(nameTextField);
        rightPanel.add(namePanel);

        Panel phoneNumberPanel = new Panel();
        phoneNumberPanel.add(new JLabel("Phone Number:"));
        phoneNumberTextField = new JTextField(20);
        phoneNumberTextField.setEditable(false);
        phoneNumberTextField.setBorder(null);
        phoneNumberTextField.setBackground(new Color(238, 238, 238));
        phoneNumberPanel.add(phoneNumberTextField);
        rightPanel.add(phoneNumberPanel);

        Panel emailPanel = new Panel();
        emailPanel.add(new JLabel("Email: "));
        emailTextField = new JTextField(20);
        emailTextField.setAlignmentX(Component.LEFT_ALIGNMENT);
        emailTextField.setBackground(new Color(238, 238, 238));
        emailTextField.setEditable(false);
        emailTextField.setBorder(null);
        emailPanel.add(emailTextField);
        rightPanel.add(emailPanel);

        Panel birthdayPanel = new Panel();
        birthdayPanel.add(new JLabel("Birthday"));
        birthdayPicker = new DatePicker();
        birthdayPanel.add(birthdayPicker);
        rightPanel.add(birthdayPanel);

        Panel addressPanel = new Panel();
        addressPanel.add(new JLabel("Address:"));
        addressTextField = new JTextField(20);
        addressTextField.setBackground(new Color(238, 238, 238));
        addressTextField.setEditable(false);
        addressTextField.setBorder(null);
        addressPanel.add(addressTextField);
        rightPanel.add(addressPanel);

        Panel notesPanel = new Panel();
        notesPanel.add(new JLabel("Notes:"));
        notesTextField = new JTextField(20);
        notesTextField.setBackground(new Color(238, 238, 238));
        notesTextField.setEditable(false);
        notesTextField.setBorder(null);
        notesPanel.add(notesTextField);
        rightPanel.add(notesPanel);

        Panel checkBoxPanel = new Panel();
        checkBoxPanel.add(new JLabel("Favorite"));
        isFavorite = new JCheckBox();
        isFavorite.setSelected(false);
        isFavorite.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (!isFavorite.isSelected()) {

                }
            }
        });
        checkBoxPanel.add(isFavorite);
        checkBoxPanel.add(new JLabel("Family"));
        isFamily = new JCheckBox();
        isFamily.setSelected(false);
        checkBoxPanel.add(isFamily);
        checkBoxPanel.add(new JLabel("Friend"));
        isFriend = new JCheckBox();
        isFriend.setSelected(false);
        checkBoxPanel.add(isFriend);
        rightPanel.add(checkBoxPanel);
    }
    
    public void addContactActionPerformed(ActionEvent e) {
        newContactWindow = new JFrame();
        createNewContactTopPanel();
    }
    
    public void createNewContactTopPanel() {
        newContactTopPanel = new Panel();
        newContactWindow.setContentPane(newContactTopPanel);
        newContactWindow.setSize(500, 500);
        newContactWindow.setTitle("New Contact");
        newContactWindow.setVisible(true);
    }

    public static void main(String[] args) {
        ContactsGUI gui = new ContactsGUI();
    }
}
