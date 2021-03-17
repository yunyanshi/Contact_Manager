// COEN 275 Final Project
// Contact Manager
// Author: Ce Pang & Yunyan Shi

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
    protected static final Color darkGray = new Color(64,64,64);
    private final String[] tabs = {"Contacts", "Favorites", "Family", "Friends"};
    private final DBConnection connection;

    private JFrame window, newContactWindow, editContactWindow;
    private Panel topPanel, leftPanel, rightPanel, tabPanel, contactListPanel;
    private JScrollPane contactListScrollPane;
    private TextField  emailTextField, addressTextField, phoneNumberTextField, notesTextField, birthdayTextField;
    private JCheckBox isFavorite, isFamily, isFriend;
    private TabButton allContactsTab, favoritesTab, familyTab, friendsTab;
    private JTextField nameTextField;
    private String selectedTab = tabs[0];
    private int selectedUserID;

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
        window.setTitle(tabs[0]);
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

        LinkedHashMap<ContactEntryButton, Integer> buttonMap = new LinkedHashMap<>();
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
        rightPanel.setLayout(new GridLayout(4, 1));
        rightPanel.setBorder(BorderFactory.createEtchedBorder());

        Panel nameAndCheckBoxPanel = new Panel();
        nameAndCheckBoxPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        nameAndCheckBoxPanel.setLayout(new GridLayout(2, 1));
        nameTextField = new JTextField();
        nameTextField.setFont(new Font("Courier", Font.BOLD, 22));
        nameTextField.setBorder(null);
        nameTextField.setHorizontalAlignment(JTextField.CENTER);
        nameAndCheckBoxPanel.add(nameTextField, BorderLayout.CENTER);

        Panel checkBoxPanel = new Panel();
        isFavorite = new JCheckBox(tabs[1]);
        isFavorite.setFont(new Font("Courier", Font.PLAIN, 16));
        isFavorite.setForeground(Color.DARK_GRAY);
        isFavorite.setSelected(false);
        isFavorite.setEnabled(false);
        checkBoxPanel.add(isFavorite);

        isFamily = new JCheckBox(tabs[2]);
        isFamily.setFont(new Font("Courier", Font.PLAIN, 16));
        isFamily.setForeground(Color.DARK_GRAY);
        isFamily.setSelected(false);
        isFamily.setEnabled(false);
        checkBoxPanel.add(isFamily);

        isFriend = new JCheckBox(tabs[3]);
        isFriend.setFont(new Font("Courier", Font.PLAIN, 16));
        isFriend.setForeground(Color.DARK_GRAY);
        isFriend.setSelected(false);
        isFriend.setEnabled(false);
        checkBoxPanel.add(isFriend);
        nameAndCheckBoxPanel.add(checkBoxPanel, BorderLayout.SOUTH);
        rightPanel.add(nameAndCheckBoxPanel);

        Panel phoneAndEmailAndBirthdayPanel = new Panel();
        phoneAndEmailAndBirthdayPanel.setBorder(new EmptyBorder(0, 15, 15, 15));
        phoneAndEmailAndBirthdayPanel.setLayout(new GridLayout(3, 1));
        Panel phoneNumberPanel = new Panel();
        phoneNumberPanel.setLayout(new BorderLayout());
        Label phoneLabel = new Label("Phone Number:");
        ImageIcon phoneIcon = new ImageIcon("src/images/phone-icon.png");
        phoneLabel.setIcon(phoneIcon);
        phoneNumberPanel.add(phoneLabel, BorderLayout.WEST);
        phoneNumberTextField = new TextField();
        phoneNumberPanel.add(phoneNumberTextField, BorderLayout.CENTER);
        phoneAndEmailAndBirthdayPanel.add(phoneNumberPanel);

        Panel emailPanel = new Panel();
        emailPanel.setLayout(new BorderLayout());
        ImageIcon emailIcon = new ImageIcon("src/images/email-icon.png");
        Label emailLabel = new Label("Email: ");
        emailLabel.setIcon(emailIcon);
        emailPanel.add(emailLabel, BorderLayout.WEST);
        emailTextField = new TextField();
        emailPanel.add(emailTextField, BorderLayout.CENTER);
        phoneAndEmailAndBirthdayPanel.add(emailPanel);

        Panel birthdayPanel = new Panel();
        birthdayPanel.setLayout(new BorderLayout());
        Label dobLabel = new Label("Birthday:");
        ImageIcon dobIcon = new ImageIcon("src/images/dob-icon.png");
        dobLabel.setIcon(dobIcon);
        birthdayPanel.add(dobLabel, BorderLayout.WEST);
        birthdayTextField = new TextField();
        birthdayPanel.add(birthdayTextField, BorderLayout.CENTER);
        phoneAndEmailAndBirthdayPanel.add(birthdayPanel);
        rightPanel.add(phoneAndEmailAndBirthdayPanel);

        Panel addressAndNotesPanel = new Panel();
        addressAndNotesPanel.setLayout(new GridLayout(2, 1));
        Panel addressPanel = new Panel();
        addressPanel.setBorder(new EmptyBorder(0, 15, 0, 15));
        addressPanel.setLayout(new BorderLayout());
        Label addressLabel = new Label("Address:");
        ImageIcon addressIcon = new ImageIcon("src/images/home-icon.png");
        addressLabel.setIcon(addressIcon);
        addressPanel.add(addressLabel, BorderLayout.NORTH);
        addressTextField = new TextField();
        addressPanel.add(addressTextField, BorderLayout.CENTER);
        addressAndNotesPanel.add(addressPanel);

        Panel notesPanel = new Panel();
        notesPanel.setBorder(new EmptyBorder(0, 15, 0, 15));
        notesPanel.setLayout(new BorderLayout());
        Label notesLabel = new Label("Notes: ");
        notesLabel.setIcon(new ImageIcon("src/images/notes-icon.png"));
        notesPanel.add(notesLabel,BorderLayout.NORTH);
        notesTextField = new TextField();
        notesPanel.add(notesTextField, BorderLayout.CENTER);
        addressAndNotesPanel.add(notesPanel);
        rightPanel.add(addressAndNotesPanel);

        Panel editAndDeletePanel = new Panel();
        editAndDeletePanel.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));
        JButton editButton = new JButton("Edit");
        editButton.setFont(new Font("Courier", Font.PLAIN, 16));
        editButton.addActionListener(this::editContactActionPerformed);
        editAndDeletePanel.add(editButton);

        JButton deleteButton = new JButton("Delete");
        deleteButton.setFont(new Font("Courier", Font.PLAIN, 16));
        deleteButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(window,
                    "Are you sure you want to delete this contact?",
                    "Contact Deletion Confirmation", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                connection.deleteContact(selectedUserID);
                selectedTab = tabs[0];
                reloadContactListPanel();
                reloadRightPanel();
            }
        });
        editAndDeletePanel.add(deleteButton);

        rightPanel.add(editAndDeletePanel);
    }
    
    public void addContactActionPerformed(ActionEvent e) {
        newContactWindow = new JFrame();
        Panel newContactTopPanel = new Panel();
        newContactTopPanel.setLayout(new GridLayout(0, 1));
        newContactTopPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        Panel namePanel = new Panel();
        namePanel.setBorder(BorderFactory.createEmptyBorder(5,0,5,0));
        namePanel.setLayout(new BorderLayout());
        Label nameLabel = new Label("Name: ");
        namePanel.add(nameLabel, BorderLayout.WEST);
        JTextField nameTextField = new JTextField();
        namePanel.add(nameTextField, BorderLayout.CENTER);
        newContactTopPanel.add(namePanel);

        Panel phonePanel = new Panel();
        phonePanel.setBorder(BorderFactory.createEmptyBorder(5,0,5,0));
        phonePanel.setLayout(new BorderLayout());
        Label phoneLabel = new Label("Phone Number: ");
        phonePanel.add(phoneLabel, BorderLayout.WEST);
        JTextField phoneTextField = new JTextField();
        phonePanel.add(phoneTextField, BorderLayout.CENTER);
        newContactTopPanel.add(phonePanel);

        Panel emailPanel = new Panel();
        emailPanel.setBorder(BorderFactory.createEmptyBorder(5,0,5,0));
        emailPanel.setLayout(new BorderLayout());
        Label emailLabel = new Label("Email: ");
        emailPanel.add(emailLabel, BorderLayout.WEST);
        JTextField emailTextField = new JTextField();
        emailPanel.add(emailTextField, BorderLayout.CENTER);
        newContactTopPanel.add(emailPanel);

        Panel dobPanel = new Panel();
        dobPanel.setBorder(BorderFactory.createEmptyBorder(5,0,5,0));
        dobPanel.setLayout(new BorderLayout());
        Label birthdayLabel = new Label("Birthday: ");
        dobPanel.add(birthdayLabel, BorderLayout.WEST);
        DatePicker birthdayPicker = new DatePicker();
        birthdayPicker.setBackground(Color.white);
        JButton date_button = birthdayPicker.getComponentToggleCalendarButton();
        date_button.setText(null);
        date_button.setIcon(new ImageIcon("src/images/datepicker-icon.png"));
        dobPanel.add(birthdayPicker, BorderLayout.CENTER);
        newContactTopPanel.add(dobPanel);

        Panel addressPanel = new Panel();
        addressPanel.setBorder(BorderFactory.createEmptyBorder(5,0,5,0));
        addressPanel.setLayout(new BorderLayout());
        Label addressLabel = new Label("Address: ");
        addressPanel.add(addressLabel, BorderLayout.WEST);
        JTextField addressTextField = new JTextField();
        addressPanel.add(addressTextField, BorderLayout.CENTER);
        newContactTopPanel.add(addressPanel);

        Panel notesPanel = new Panel();
        notesPanel.setBorder(BorderFactory.createEmptyBorder(5,0,5,0));
        notesPanel.setLayout(new BorderLayout());
        Label notesLabel = new Label("Notes: ");
        notesPanel.add(notesLabel, BorderLayout.WEST);
        JTextField notesTextField = new JTextField();
        notesPanel.add(notesTextField, BorderLayout.CENTER);
        newContactTopPanel.add(notesPanel);

        Panel checkBoxPanel = new Panel();
        checkBoxPanel.setLayout(new GridLayout(1, 3));
        JCheckBox isFavorite = new JCheckBox(tabs[1]);
        isFavorite.setFont(new Font("Courier", Font.PLAIN, 16));
        isFavorite.setForeground(Color.DARK_GRAY);
        isFavorite.setSelected(false);
        checkBoxPanel.add(isFavorite);
        JCheckBox isFamily = new JCheckBox(tabs[2]);
        isFamily.setFont(new Font("Courier", Font.PLAIN, 16));
        isFamily.setForeground(Color.DARK_GRAY);
        isFamily.setSelected(false);
        checkBoxPanel.add(isFamily);
        JCheckBox isFriend = new JCheckBox(tabs[3]);
        isFriend.setFont(new Font("Courier", Font.PLAIN, 16));
        isFriend.setForeground(Color.DARK_GRAY);
        isFriend.setSelected(false);
        checkBoxPanel.add(isFriend);
        newContactTopPanel.add(checkBoxPanel);

        Panel cancelOrConfirmPanel = new Panel();
        cancelOrConfirmPanel.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Courier", Font.PLAIN, 16));
        cancelOrConfirmPanel.add(cancelButton);
        cancelButton.addActionListener(e1 -> newContactWindow.dispose());
        JButton confirmButton = new JButton("Confirm");
        confirmButton.setFont(new Font("Courier", Font.PLAIN, 16));
        cancelOrConfirmPanel.add(confirmButton);
        confirmButton.addActionListener(e2 -> {
            if (nameTextField.getText().length() == 0) {
                JOptionPane.showMessageDialog(newContactWindow, "Please enter the contact name!",
                        "Invalid New Contact Information", JOptionPane.WARNING_MESSAGE);
            } else {
                int newId = connection.createContact(nameTextField.getText(), phoneTextField.getText(),
                        birthdayPicker.getDate(), emailTextField.getText(),addressTextField.getText(),
                        notesTextField.getText());
                if (isFavorite.isSelected()) {
                    connection.addToTab(tabs[1], newId);
                }
                if (isFamily.isSelected()) {
                    connection.addToTab(tabs[2], newId);
                }
                if (isFriend.isSelected()) {
                    connection.addToTab(tabs[3], newId);
                }
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
    
    public void editContactActionPerformed(ActionEvent e) {
    	editContactWindow = new JFrame();
        Panel editContactTopPanel = new Panel();
        editContactTopPanel.setLayout(new GridLayout(0, 1));

        ResultSet contactInfoResultSet = connection.getContactInfoResultSet(selectedUserID);

        Panel namePanel = new Panel();
        namePanel.setLayout(new BorderLayout());
        Label nameLabel = new Label("Name: ");
        namePanel.add(nameLabel, BorderLayout.WEST);
        JTextField nameTextField = new JTextField();
        namePanel.add(nameTextField, BorderLayout.CENTER);
        editContactTopPanel.add(namePanel);

        Panel phonePanel = new Panel();
        phonePanel.setLayout(new BorderLayout());
        Label phoneLabel = new Label("Phone Number: ");
        phonePanel.add(phoneLabel, BorderLayout.WEST);
        JTextField phoneTextField = new JTextField();
        phonePanel.add(phoneTextField, BorderLayout.CENTER);
        editContactTopPanel.add(phonePanel);

        Panel emailPanel = new Panel();
        emailPanel.setLayout(new BorderLayout());
        Label emailLabel = new Label("Email: ");
        emailPanel.add(emailLabel, BorderLayout.WEST);
        JTextField emailTextField = new JTextField();
        emailPanel.add(emailTextField, BorderLayout.CENTER);
        editContactTopPanel.add(emailPanel);

        Panel dobPanel = new Panel();
        DatePicker birthdayPicker = new DatePicker();
        ImageIcon dateExampleIcon = new ImageIcon("src/images/datepicker-icon.png");
        JButton date_button = birthdayPicker.getComponentToggleCalendarButton();
        date_button.setText("");
        date_button.setIcon(dateExampleIcon);
        dobPanel.add(birthdayPicker);
        editContactTopPanel.add(dobPanel);

        Panel addressPanel = new Panel();
        addressPanel.setLayout(new BorderLayout());
        Label addressLabel = new Label("Address: ");
        addressPanel.add(addressLabel, BorderLayout.WEST);
        JTextField addressTextField = new JTextField();
        addressPanel.add(addressTextField, BorderLayout.CENTER);
        editContactTopPanel.add(addressPanel);

        Panel notesPanel = new Panel();
        notesPanel.setLayout(new BorderLayout());
        Label notesLabel = new Label("Notes: ");
        notesPanel.add(notesLabel, BorderLayout.WEST);
        JTextField notesTextField = new JTextField();
        notesPanel.add(notesTextField, BorderLayout.CENTER);
        editContactTopPanel.add(notesPanel);

        Panel checkBoxPanel = new Panel();
        JCheckBox isFavorite = new JCheckBox(tabs[1]);
        isFavorite.setFont(new Font("Courier", Font.PLAIN, 16));
        isFavorite.setForeground(Color.DARK_GRAY);

        checkBoxPanel.add(isFavorite);
        JCheckBox isFamily = new JCheckBox(tabs[2]);
        isFamily.setFont(new Font("Courier", Font.PLAIN, 16));
        isFamily.setForeground(Color.DARK_GRAY);

        checkBoxPanel.add(isFamily);
        JCheckBox isFriend = new JCheckBox(tabs[3]);
        isFriend.setFont(new Font("Courier", Font.PLAIN, 16));
        isFriend.setForeground(Color.DARK_GRAY);

        checkBoxPanel.add(isFriend);


        try {
            while (contactInfoResultSet.next()) {
                nameTextField.setText(contactInfoResultSet.getString("name"));
                phoneTextField.setText(contactInfoResultSet.getString("phone_number"));
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



        editContactTopPanel.add(checkBoxPanel);

        Panel cancelOrConfirmPanel = new Panel();
        JButton cancelButton = new JButton("Cancel");
        cancelOrConfirmPanel.add(cancelButton);
        cancelButton.addActionListener(e1 -> editContactWindow.dispose());
        JButton confirmButton = new JButton("Confirm");
        cancelOrConfirmPanel.add(confirmButton);
        confirmButton.addActionListener(e2 -> {

            String message = connection.updateContact(selectedUserID,
                    nameTextField.getText(), phoneTextField.getText(),emailTextField.getText(),
                    birthdayPicker.getDate(), addressTextField.getText(),
                    notesTextField.getText());
            JOptionPane.showMessageDialog(newContactWindow, message, "Successful Updates",JOptionPane.PLAIN_MESSAGE);
            selectedTab = tabs[0];
            allContactsTab.setSelected(true);
            reloadRightPanel();
            editContactWindow.dispose();

        });
        editContactTopPanel.add(cancelOrConfirmPanel);
        editContactWindow.setContentPane(editContactTopPanel);
        editContactWindow.setSize(500, 500);
        editContactWindow.setTitle("New Contact");
        editContactWindow.setVisible(true);
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
                    birthdayTextField.setText(birthday.toString());
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
