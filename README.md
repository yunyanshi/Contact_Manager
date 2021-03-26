# Contact Manager
## Overview:
* Contact Manager is a contact management application for individuals. 
* It is based on the local MySQL database, which stores all the data related to the contacts. 
* Contact Manager has a GUI that allows users to interact with contact information by manipulating visual 
  widgets of the GUI.
  * The user can browse contacts under different tabs.
  * The user can select any contact from the contact list loaded with the selected tab. 
  * Once a contact selected, the detailed information of this contact will be displayed by the rightmost panel.
  * The user can click the edit button which toggles the edit mode for contact information, or the delete button which 
    deletes all the information
    corresponding to the designated contact.
  * The user is also allowed to add a new contact at any time.
      
* The GUI is connected to the database by JDBC connections.
* Contact Manager consists of seven classes and is entirely written in Java.
* Two JAR files — MySQL driver and LGoodDatePicker — need to be added to the classpath variable of this application.
aljdlfj