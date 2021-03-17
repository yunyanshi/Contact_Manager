// This is another test.
// This is the third test.
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.github.lgooddatepicker.components.DatePicker;


public class ContactsGUI {
    protected static Color darkGray = new Color(64,64,64);
    private JFrame window, newContactWindow;
    private Panel topPanel, leftPanel, rightPanel, tabPanel, contactListPanel, newContactTopPanel;
    private DBConnection connection;
    private LinkedHashMap<ContactEntryButton, Integer> buttonMap;
    JScrollPane contactListScrollPane;
    private TextField  emailTextField, addressTextField, phoneNumberTextField, notesTextField;
    private JCheckBox isFavorite, isFamily, isFriend;
    private DatePicker birthdayPicker;
    private final String[] tabs = {"Contacts", "Favorites", "Family", "Friends"};
    private String selectedTab = tabs[0];
    private int selectedUserID;
    private TabButton allContactsTab, favoritesTab, familyTab, friendsTab;
    JTextField nameTextField;

    public ContactsGUI() {
        connection = new DBConnection();
        initComponents();
    }

    private void initComponents() {
        window = new JFrame();
        createPanels();
        window.setContentPane(topPanel);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(1100, 700);
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
        reloadContactListPanel();
        reloadRightPanel();
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

        allContactsTab = new TabButton("All Contacts");
        allContactsTab.setSelected(true);
        tabsGroup.add(allContactsTab);
        tabLabelPanel.add(allContactsTab);
        allContactsTab.addActionListener(e -> {
            selectedTab = "Contacts";
            reloadContactListPanel();
            reloadRightPanel();
        });

        favoritesTab = new TabButton("Favorites");
        tabsGroup.add(favoritesTab);
        tabLabelPanel.add(favoritesTab);
        favoritesTab.addActionListener(e -> {
            selectedTab = "Favorites";
            reloadContactListPanel();
            reloadRightPanel();
        });

        familyTab = new TabButton("Family");
        tabsGroup.add(familyTab);
        tabLabelPanel.add(familyTab);
        familyTab.addActionListener(e -> {
            selectedTab = "Family";
            reloadContactListPanel();
            reloadRightPanel();
        });

        friendsTab = new TabButton("Friends");
        tabsGroup.add(friendsTab);
        tabLabelPanel.add(friendsTab);
        friendsTab.addActionListener(e -> {
            selectedTab = "Friends";
            reloadContactListPanel();
            reloadRightPanel();
        });

        tabPanel.add(tabLabelPanel);
        tabPanel.add(Box.createVerticalStrut(400));

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
        contactListPanel.setLayout(new BoxLayout(contactListPanel, BoxLayout.Y_AXIS));

        contactListScrollPane = new JScrollPane(contactListPanel);
        contactListScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        contactListScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }
    
    public void reloadContactListPanel() {
    	contactListPanel.removeAll();
    	contactListPanel.repaint();
    	contactListPanel.revalidate();

        buttonMap = new LinkedHashMap<>();
        ResultSet contactListResultSet = connection.getContactListResultSet(selectedTab);
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
                            selectedUserID = user_id;
                            reloadRightPanel();
                        }
                    });
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if (buttonMap.size() > 0) {
            Iterator<Map.Entry<ContactEntryButton, Integer>> iterator = buttonMap.entrySet().iterator();
            Map.Entry<ContactEntryButton, Integer> entry = iterator.next();
            selectedUserID = entry.getValue();
        }
    }
    
    private void createRightPanel() {
        rightPanel = new Panel();
        rightPanel.setLayout(new GridLayout(0, 1));
        rightPanel.setBorder(BorderFactory.createEtchedBorder());

        Panel namePanel = new Panel();
        namePanel.setBorder(new EmptyBorder(5, 15, 15, 15));
        namePanel.setLayout(new BorderLayout());
        nameTextField = new JTextField();
        nameTextField.setFont(new Font("Courier", Font.BOLD, 22));
        nameTextField.setBorder(null);
        nameTextField.setHorizontalAlignment(JTextField.CENTER);
        namePanel.add(nameTextField, BorderLayout.CENTER);
        rightPanel.add(namePanel);

        Panel phoneAndEmailPanel = new Panel();
        phoneAndEmailPanel.setBorder(new EmptyBorder(0, 15, 15, 15));
        phoneAndEmailPanel.setLayout(new GridLayout(2, 1));
        Panel phoneNumberPanel = new Panel();
        phoneNumberPanel.setLayout(new BorderLayout());
        phoneNumberPanel.add(new Label("Phone Number:"), BorderLayout.WEST);
        phoneNumberTextField = new TextField();

        phoneNumberPanel.add(phoneNumberTextField, BorderLayout.CENTER);
        phoneAndEmailPanel.add(phoneNumberPanel);

        Panel emailPanel = new Panel();
        emailPanel.setLayout(new BorderLayout());
        emailPanel.add(new Label("Email: "), BorderLayout.WEST);
        emailTextField = new TextField();
        emailPanel.add(emailTextField, BorderLayout.CENTER);
        phoneAndEmailPanel.add(emailPanel);
        rightPanel.add(phoneAndEmailPanel);

        Panel birthdayPanel = new Panel();
        birthdayPanel.setBorder(new EmptyBorder(5, 15, 45, 15));
        birthdayPanel.setLayout(new BorderLayout());
        birthdayPanel.add(new Label("Birthday: "), BorderLayout.NORTH);
        ImageIcon dateExampleIcon = new ImageIcon("src/datepickerbutton1.png");
        birthdayPicker = new DatePicker();
        JButton date_button = birthdayPicker.getComponentToggleCalendarButton();
        date_button.setText("");
        date_button.setIcon(dateExampleIcon);
        birthdayPicker.setBackground(Color.white);
        birthdayPanel.add(birthdayPicker, BorderLayout.CENTER);
        rightPanel.add(birthdayPanel);

        Panel addressPanel = new Panel();
        addressPanel.setBorder(new EmptyBorder(0, 15, 10, 15));
        addressPanel.setLayout(new BorderLayout());
        addressPanel.add(new Label("Address:"), BorderLayout.NORTH);
        addressTextField = new TextField();
        addressPanel.add(addressTextField, BorderLayout.CENTER);
        rightPanel.add(addressPanel);

        Panel notesPanel = new Panel();
        notesPanel.setBorder(new EmptyBorder(0, 15, 10, 15));
        notesPanel.setLayout(new BorderLayout());
        notesPanel.add(new Label("Notes:"),BorderLayout.NORTH);
        notesTextField = new TextField();
        notesPanel.add(notesTextField, BorderLayout.CENTER);
        rightPanel.add(notesPanel);

        Panel bottomPanel = new Panel();
        bottomPanel.setLayout(new GridLayout(2, 1));
        Panel checkBoxPanel = new Panel();
        isFavorite = new JCheckBox("Favorite");
        isFavorite.setFont(new Font("Courier", Font.PLAIN, 16));
        isFavorite.setForeground(Color.DARK_GRAY);
        isFavorite.setSelected(false);
        checkBoxPanel.add(isFavorite);

        isFamily = new JCheckBox("Family");
        isFamily.setFont(new Font("Courier", Font.PLAIN, 16));
        isFamily.setForeground(Color.DARK_GRAY);
        isFamily.setSelected(false);
        checkBoxPanel.add(isFamily);

        isFriend = new JCheckBox("Friend");
        isFriend.setFont(new Font("Courier", Font.PLAIN, 16));
        isFriend.setForeground(Color.DARK_GRAY);
        isFriend.setSelected(false);
        checkBoxPanel.add(isFriend);
        bottomPanel.add(checkBoxPanel);

        Panel editAndDeletePanel = new Panel();
        JButton editButton = new JButton("Edit");
        editButton.setForeground(Color.DARK_GRAY);
        editButton.setFont(new Font("Courier", Font.PLAIN, 16));
        editAndDeletePanel.add(editButton);

        JButton deleteButton = new JButton("Delete");
        deleteButton.setForeground(Color.DARK_GRAY);
        deleteButton.setFont(new Font("Courier", Font.PLAIN, 16));
        editAndDeletePanel.add(deleteButton);
        bottomPanel.add(editAndDeletePanel);
        rightPanel.add(bottomPanel);
    }
    
    public void addContactActionPerformed(ActionEvent e) {
        newContactWindow = new JFrame();
        createNewContactTopPanel();
    }
    
    public void createNewContactTopPanel() {
        newContactTopPanel = new Panel();
        newContactTopPanel.setLayout(new GridLayout(0, 1));

        Panel namePanel = new Panel();
        namePanel.setLayout(new BorderLayout());
        Label nameLabel = new Label("Name: ");
        namePanel.add(nameLabel, BorderLayout.WEST);
        JTextField nameTextField = new JTextField();
        namePanel.add(nameTextField, BorderLayout.CENTER);
        newContactTopPanel.add(namePanel);

        Panel phonePanel = new Panel();
        phonePanel.setLayout(new BorderLayout());
        Label phoneLabel = new Label("Phone Number: ");
        phonePanel.add(phoneLabel, BorderLayout.WEST);
        JTextField phoneTextField = new JTextField();
        phonePanel.add(phoneTextField, BorderLayout.CENTER);
        newContactTopPanel.add(phonePanel);

        Panel emailPanel = new Panel();
        emailPanel.setLayout(new BorderLayout());
        Label emailLabel = new Label("Email: ");
        emailPanel.add(emailLabel, BorderLayout.WEST);
        JTextField emailTextField = new JTextField();
        emailPanel.add(emailTextField, BorderLayout.CENTER);
        newContactTopPanel.add(emailPanel);

        Panel dobPanel = new Panel();
        DatePicker birthdayPicker = new DatePicker();
        dobPanel.add(birthdayPicker);
        newContactTopPanel.add(dobPanel);

        Panel addressPanel = new Panel();
        addressPanel.setLayout(new BorderLayout());
        Label addressLabel = new Label("Address: ");
        addressPanel.add(addressLabel, BorderLayout.WEST);
        JTextField addressTextField = new JTextField();
        addressPanel.add(addressTextField, BorderLayout.CENTER);
        newContactTopPanel.add(addressPanel);

        Panel notesPanel = new Panel();
        notesPanel.setLayout(new BorderLayout());
        Label notesLabel = new Label("Notes: ");
        notesPanel.add(notesLabel, BorderLayout.WEST);
        JTextField notesTextField = new JTextField();
        notesPanel.add(notesTextField, BorderLayout.CENTER);
        newContactTopPanel.add(notesPanel);

        Panel checkBoxPanel = new Panel();
        JCheckBox isFavorite = new JCheckBox("Favorite");
        isFavorite.setFont(new Font("Courier", Font.PLAIN, 16));
        isFavorite.setForeground(Color.DARK_GRAY);
        isFavorite.setSelected(false);
        checkBoxPanel.add(isFavorite);
        JCheckBox isFamily = new JCheckBox("Family");
        isFamily.setFont(new Font("Courier", Font.PLAIN, 16));
        isFamily.setForeground(Color.DARK_GRAY);
        isFamily.setSelected(false);
        checkBoxPanel.add(isFamily);
        JCheckBox isFriend = new JCheckBox("Friend");
        isFriend.setFont(new Font("Courier", Font.PLAIN, 16));
        isFriend.setForeground(Color.DARK_GRAY);
        isFriend.setSelected(false);
        checkBoxPanel.add(isFriend);
        newContactTopPanel.add(checkBoxPanel);

        Panel cancelOrConfirmPanel = new Panel();
        JButton cancelButton = new JButton("Cancel");
        cancelOrConfirmPanel.add(cancelButton);
        cancelButton.addActionListener(e -> newContactWindow.dispose());
        JButton confirmButton = new JButton("Confirm");
        cancelOrConfirmPanel.add(confirmButton);
        confirmButton.addActionListener(e -> {
            if (nameTextField.getText().length() == 0) {
                JOptionPane.showMessageDialog(newContactWindow, "Please enter the contact name!",
                        "Invalid New Contact Information", JOptionPane.WARNING_MESSAGE);
            } else {
                int newId = connection.createNewContact(nameTextField.getText(), phoneTextField.getText(),
                        birthdayPicker.getDate(), emailTextField.getText(),addressTextField.getText(),
                        notesTextField.getText());
                selectedTab = tabs[0];
                allContactsTab.setSelected(true);
                reloadContactListPanel();
                selectedUserID = newId;
                reloadRightPanel();
                newContactWindow.dispose();
            }
        });
        newContactTopPanel.add(cancelOrConfirmPanel);

        newContactWindow.setContentPane(newContactTopPanel);
        newContactWindow.setSize(500, 500);
        newContactWindow.setTitle("New Contact");
        newContactWindow.setVisible(true);
    }

    public void reloadRightPanel() {
        ResultSet contactInfoResultSet = connection.getContactInfoResultSet(selectedUserID);

        try {
            while (contactInfoResultSet.next()) {
                nameTextField.setText(contactInfoResultSet.getString("name"));
                phoneNumberTextField.setText(contactInfoResultSet.getString("phone_number"));
                emailTextField.setText((contactInfoResultSet.getString("email")));
                Date birthday = contactInfoResultSet.getDate("birthday");
                if (birthday != null) {
                    birthdayPicker.setDate(birthday.toLocalDate());
                } else {
                    birthdayPicker.clear();
                }
                addressTextField.setText(contactInfoResultSet.getString("address"));
                notesTextField.setText(contactInfoResultSet.getString("notes"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        isFavorite.setSelected(connection.ifBelongs(selectedUserID, "Favorites"));
        isFamily.setSelected(connection.ifBelongs(selectedUserID, "Family"));
        isFriend.setSelected(connection.ifBelongs(selectedUserID, "Friends"));
    }

    public static void main(String[] args) {
        ContactsGUI gui = new ContactsGUI();
    }
}
