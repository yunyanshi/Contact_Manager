// This is another test.
// This is the third test.
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class ContactsGUI {
    private JFrame window, newContactWindow;
    private JPanel topPanel, leftPanel, rightPanel, tabPanel, contactListPanel, newContactTopPanel;
    private DBConnection connection;
    private HashMap<JButton, Integer> buttonMap;
    JScrollPane contactListScrollPane;
    private JTextField nameTextField, emailTextField, addressTextField, phoneNumberTextField, birthdayMonthTextField,
            birthdayDayTextField, birthdayYearTextField, notesTextField;
    private JCheckBox isFavorite, isFamily, isFriend;


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
    //Main Panel (Left Panel + Right Panel)
    private void createPanels() {
        topPanel = new JPanel(new GridLayout(1, 2));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        createLeftPanel();
        topPanel.add(leftPanel);
        createRightPanel();
        topPanel.add(rightPanel);
    }
    //Leftmost Panel + Contacts List Panel
    private void createLeftPanel() {
        leftPanel = new JPanel(new GridLayout(1, 2));
        createTabPanel();
        leftPanel.add(tabPanel);
        createContactListPanel();
        leftPanel.add(contactListScrollPane);
    }
    
    //Leftmost Panel;
    private void createTabPanel() {
        tabPanel = new JPanel();
        tabPanel.setBorder(BorderFactory.createEtchedBorder());
        tabPanel.setLayout(new BoxLayout(tabPanel, BoxLayout.Y_AXIS));

        JPanel tabLabelPanel = new JPanel();
        tabLabelPanel.setLayout(new GridLayout(4, 1));

        JButton allContactsTab = new JButton("All Contacts");
        tabLabelPanel.add(allContactsTab);
        allContactsTab.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				reload("contact");
			}
        });

        JButton favoritesTab = new JButton("Favorites");
        favoritesTab.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				reload("favorites");
			}
        });
        
        tabLabelPanel.add(favoritesTab);

        JButton familyTab = new JButton("Family");
        tabLabelPanel.add(familyTab);
        familyTab.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				reload("family");
			}
        });

        JButton friendsTab = new JButton("Friends");
        friendsTab.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				reload("friends");
			}
        });
        tabLabelPanel.add(friendsTab);

        tabPanel.add(tabLabelPanel);
        tabPanel.add(Box.createVerticalGlue());

        JButton addContactButton = new JButton("+");
        addContactButton.setToolTipText("New Contact");
        addContactButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addContactButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addContactActionPerformed(e);
            }
        });
        tabPanel.add(addContactButton);
    }
    
    //Contacts List Panel
    private void createContactListPanel() {
        contactListPanel = new JPanel();
        contactListPanel.setBorder(BorderFactory.createEtchedBorder());
        contactListPanel.setLayout(new BoxLayout(contactListPanel, BoxLayout.Y_AXIS));
        ResultSet contactListResultSet = connection.getContactListResultSet();
        buttonMap = new HashMap<>();
        try {
            while (contactListResultSet != null && contactListResultSet.next()) {
                int user_id = contactListResultSet.getInt(1);
                String name = contactListResultSet.getString(2);
                JButton button = new JButton(name);

                button.setBorderPainted(false);
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
                                addressTextField.setText(contactInfoResultSet.getString("address"));
                                notesTextField.setText(contactInfoResultSet.getString("notes"));
                            }
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }

                    }
                });
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        
        contactListScrollPane = new JScrollPane(contactListPanel);
        contactListScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
    }
    
    public void reload(String tab)  {
    	contactListPanel.removeAll();
    	
    	
    	
    	ResultSet contactListResultSet = connection.getListResultSet(tab);
    	contactListPanel = new JPanel();
        contactListPanel.setBorder(BorderFactory.createEtchedBorder());
        contactListPanel.setLayout(new BoxLayout(contactListPanel, BoxLayout.Y_AXIS));
       // ResultSet contactListResultSet = connection.getContactListResultSet();
       // buttonMap.clear();
        buttonMap = new HashMap<>();
        try {
            while (contactListResultSet != null && contactListResultSet.next()) {
                int user_id = contactListResultSet.getInt(1);
                String name = contactListResultSet.getString(2);
                System.out.println(user_id + ", " + name);
                JButton button = new JButton(name);
                button.setBorderPainted(false);
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
                                addressTextField.setText(contactInfoResultSet.getString("address"));
                                notesTextField.setText(contactInfoResultSet.getString("notes"));
                            }
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    }
                });
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        
        contactListScrollPane = new JScrollPane(contactListPanel);
        contactListScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        contactListPanel.revalidate();
        contactListPanel.repaint();
    }
    
    //Right Panel
    private void createRightPanel() {
        rightPanel = new JPanel();
        rightPanel.setBorder(BorderFactory.createEtchedBorder());
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        JPanel namePanel = new JPanel();
        namePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        nameTextField = new JTextField(20);
        nameTextField.setEditable(false);
        nameTextField.setBorder(null);
        nameTextField.setBackground(new Color(238, 238, 238));
        nameTextField.setFont(new Font("Serif", Font.BOLD,25));
        namePanel.add(nameTextField);
        rightPanel.add(namePanel);

        JPanel phoneNumberPanel = new JPanel();
        phoneNumberPanel.add(new JLabel("Phone Number:"));
        phoneNumberTextField = new JTextField(20);
        phoneNumberTextField.setEditable(false);
        phoneNumberTextField.setBorder(null);
        phoneNumberTextField.setBackground(new Color(238, 238, 238));
        phoneNumberPanel.add(phoneNumberTextField);
        rightPanel.add(phoneNumberPanel);

        JPanel emailPanel = new JPanel();
        emailPanel.add(new JLabel("Email: "));
        emailTextField = new JTextField(20);
        emailTextField.setAlignmentX(Component.LEFT_ALIGNMENT);
        emailTextField.setBackground(new Color(238, 238, 238));
        emailTextField.setEditable(false);
        emailTextField.setBorder(null);
        emailPanel.add(emailTextField);
        rightPanel.add(emailPanel);

        JPanel birthdayPanel = new JPanel();
        birthdayPanel.add(new JLabel("Birthday"));
        birthdayMonthTextField = new JTextField(2);
        birthdayMonthTextField.setBackground(new Color(238, 238, 238));
        birthdayMonthTextField.setEditable(false);
        birthdayMonthTextField.setBorder(null);
        birthdayPanel.add(birthdayMonthTextField);
        birthdayPanel.add(new JLabel("-"));
        birthdayDayTextField = new JTextField(2);
        birthdayDayTextField.setBackground(new Color(238, 238, 238));
        birthdayDayTextField.setEditable(false);
        birthdayDayTextField.setBorder(null);
        birthdayPanel.add(birthdayDayTextField);
        birthdayPanel.add(new JLabel("-"));
        birthdayYearTextField = new JTextField(4);
        birthdayYearTextField.setBackground(new Color(238, 238, 238));
        birthdayYearTextField.setEditable(false);
        birthdayYearTextField.setBorder(null);
        birthdayPanel.add(birthdayYearTextField);
        rightPanel.add(birthdayPanel);

        JPanel addressPanel = new JPanel();
        addressPanel.add(new JLabel("Address:"));
        addressTextField = new JTextField(20);
        addressTextField.setBackground(new Color(238, 238, 238));
        addressTextField.setEditable(false);
        addressTextField.setBorder(null);
        addressPanel.add(addressTextField);
        rightPanel.add(addressPanel);

        JPanel notesPanel = new JPanel();
        notesPanel.add(new JLabel("Notes:"));
        notesTextField = new JTextField(20);
        notesTextField.setBackground(new Color(238, 238, 238));
        notesTextField.setEditable(false);
        notesTextField.setBorder(null);
        notesPanel.add(notesTextField);
        rightPanel.add(notesPanel);

        JPanel checkBoxPanel = new JPanel();
        isFavorite = new JCheckBox();
        isFavorite.setSelected(false);
        checkBoxPanel.add(isFavorite);
        isFamily = new JCheckBox();
        isFamily.setSelected(false);
        checkBoxPanel.add(isFamily);
        isFriend = new JCheckBox();
        isFriend.setSelected(false);
        checkBoxPanel.add(isFriend);
        rightPanel.add(checkBoxPanel);
    }
    
    //Add Button Action Listener
    public void addContactActionPerformed(ActionEvent e) {
        newContactWindow = new JFrame();
        createNewContactTopPanel();
    }
    
    //Add new contact
    public void createNewContactTopPanel() {
        newContactTopPanel = new JPanel();
        newContactWindow.setContentPane(newContactTopPanel);
        newContactWindow.setSize(500, 500);
        newContactWindow.setTitle("New Contact");
        newContactWindow.setVisible(true);
    }

    public static void main(String[] args) {
        ContactsGUI gui = new ContactsGUI();
    }
}
