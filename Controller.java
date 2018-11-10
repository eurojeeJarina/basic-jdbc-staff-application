package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.xml.soap.Text;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;


public class Controller {

    private StaffQueries staffQueries;
    private Staff currentStaff; // staff currently displayed
    private ArrayList<Staff> staffArrayList; // all the staff we want to  display
    private int numberOfEntries; // number of entries in the arraylist
    private int currentEntryIndex; // store the index of the current staff

    /*******LABELS**********/
    @FXML
    Label statusLabel;


    // TEXT FIELD DECLARED HERE

    @FXML
    TextField indexField, maxIndexField;

    @FXML
    TextField staffIdField, firstNameField, lastNameField, dateOfBirthField,
            departmentField, salaryField, startDateField, fullTimeField;

    @FXML
    TextField searchByFirstNameField, searchByDepartmentField;

    // ALL BUTTONS DECLARED HERE
    @FXML
    Button previousBtn;
    @FXML
    Button nextBtn;
    @FXML
    Button browseAllBtn;
    @FXML
    Button searchByNameBtn;
    @FXML
    Button searchByDepartmentBtn;
    @FXML
    Button insertButton;
    @FXML
    Button updateButton;
    @FXML
    Button saveButton, cancelButton;


    @FXML
    DatePicker datePickerField;


    @FXML
    public void initialize() {
        staffQueries = new StaffQueries();
        statusLabel.setText(staffQueries.statusConnection());
        displayStaff();
    }

    @FXML
    public void onButtonClicked(ActionEvent e) {


        if (e.getSource().equals(browseAllBtn)) { // If browse all btn is clicked
            displayStaff();
        } else if (e.getSource().equals(nextBtn)) // If next btn is clicked
        {
            nextStaff();
        } else if (e.getSource().equals(previousBtn)) {
            previousStaff();
        } else if (e.getSource().equals(searchByNameBtn)) {
            if (searchByFirstNameField.getText().isEmpty()) {
                System.out.println("Empty Field");
            } else {
                searchByNames(searchByFirstNameField.getText());
            }
        } else if (e.getSource().equals(searchByDepartmentBtn)) {
            if (searchByDepartmentField.getText().isEmpty()) {
                System.out.println("Empty Field");
            } else {
                searchByDepartment(searchByDepartmentField.getText());
            }
        } else if (e.getSource().equals(insertButton)) {
            insertButtonHandler();
        } else if (e.getSource().equals(updateButton)) {

        } else if (e.getSource().equals(saveButton)) {
            addNewStaff(areFieldsValid());
        } else if (e.getSource().equals(cancelButton)) {

        }
    }

    @FXML
    public void onKeyPressed(ActionEvent event) {
        try {
            int inputIndex = Integer.parseInt(indexField.getText());

            if (inputIndex < 1 || inputIndex > this.numberOfEntries) {
                System.out.println("Please enter number within range.");
            } else {
                displayCurrentStaff(inputIndex - 1);
            }

        } catch (NumberFormatException ex) {
            System.out.println("Please enter a valid number");
        }
    }

    private void insertButtonHandler() {
        // Clear the fields of top panel
        indexField.clear();
        maxIndexField.clear();

        // Clear the fields of info panel
        staffIdField.clear();
        firstNameField.clear();
        lastNameField.clear();
        dateOfBirthField.clear();
        departmentField.clear();
        salaryField.clear();
        startDateField.clear();
        fullTimeField.clear();

        //DISABLE ALL BUTTONS EXCEPT THE 'SAVE BUTTON'
        // index field editable = false;
        // search fields editable = false;
        // save button enable

        previousBtn.setDisable(true);
        nextBtn.setDisable(true);
        searchByNameBtn.setDisable(true);
        searchByDepartmentBtn.setDisable(true);
        updateButton.setDisable(true);

        // only buttons that are currently enable
        browseAllBtn.setDisable(true);
        insertButton.setDisable(true);

        indexField.setEditable(false);
        searchByDepartmentField.setEditable(false);
        searchByFirstNameField.setEditable(false);

        statusLabel.setText("Add New Staff?");
        saveButton.setDisable(false);
        cancelButton.setDisable(false);
    }

    private boolean areFieldsValid() {
        if (firstNameField.getText().isEmpty() ||
                lastNameField.getText().isEmpty() ||
                dateOfBirthField.getText().isEmpty() ||
                departmentField.getText().isEmpty() ||
                salaryField.getText().isEmpty() ||
                startDateField.getText().isEmpty() ||
                fullTimeField.getText().isEmpty()) {
                return false;
        }
        else{
                return true;
        }
    }

    private void addNewStaff(boolean isValid) {

        if(!isValid)
        {
            System.out.println("Some fields are missing!");
        }else
        {
            System.out.println("Ok we can proceed");
        }
    }

    private void searchByNames(String query) {
        try {

            staffArrayList = staffQueries.searchStaff(query); // pass the parameter to the searchStaff() from staffQuerries Class
            numberOfEntries = staffArrayList.size();

            //System.out.println(numberOfEntries);

            if (numberOfEntries != 0) {
                currentEntryIndex = 0;
                currentStaff = staffArrayList.get(0);

                displayCurrentStaff(currentEntryIndex);


                maxIndexField.setText(numberOfEntries + "");
                indexField.setText((currentEntryIndex + 1) + "");
            } else {
                System.out.println("Not found!");
                displayStaff();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void searchByDepartment(String query) {
        try {

            staffArrayList = staffQueries.searchStaffByDepartment(query);
            numberOfEntries = staffArrayList.size();

            if (numberOfEntries != 0) {
                currentEntryIndex = 0;
                currentStaff = staffArrayList.get(0);

                displayCurrentStaff(currentEntryIndex);


                maxIndexField.setText(numberOfEntries + "");
                indexField.setText((currentEntryIndex + 1) + "");
            } else {
                System.out.println("Not found!");
                displayStaff();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void nextStaff() {

        this.currentEntryIndex++;
        // the current index is already at index[0]
        // add 1 to the currentEntryIndex when the button is pressed


        if (this.currentEntryIndex >= this.numberOfEntries) {
            // if the currentEntryIndex have reached the max number of entries
            // subtract 1 from it to avoid the out of bounds exception
            // and then disable the next button;
            this.currentEntryIndex = 0;

        }

        indexField.setText((this.currentEntryIndex + 1) + "");
        displayCurrentStaff(this.currentEntryIndex);

    }

    private void previousStaff() {
        this.currentEntryIndex--;

        if (this.currentEntryIndex < 0) {
            this.currentEntryIndex = this.numberOfEntries - 1;
        }
        indexField.setText((this.currentEntryIndex + 1) + "");
        displayCurrentStaff(this.currentEntryIndex);
    }


    private void displayStaff() {
        try {
            staffArrayList = staffQueries.getAllStaff();
            numberOfEntries = staffArrayList.size();

            if (numberOfEntries != 0) {
                currentEntryIndex = 0;
                currentStaff = staffArrayList.get(currentEntryIndex);

                staffIdField.setText(currentStaff.getStaffId() + "");
                firstNameField.setText(currentStaff.getFirstName());
                lastNameField.setText(currentStaff.getLastName());
                dateOfBirthField.setText(currentStaff.getDateOfBirth());
                departmentField.setText(currentStaff.getDepartment());
                salaryField.setText(currentStaff.getSalary() + "");
                startDateField.setText(currentStaff.getStartDate());
                fullTimeField.setText(currentStaff.isFullTime() + "");

                maxIndexField.setText(numberOfEntries + "");
                indexField.setText((currentEntryIndex + 1) + "");

            }
        } catch (Exception ex) {
            System.err.println("Problem with database connection.");
        }
    }

    private void displayCurrentStaff(int index) {

        currentStaff = this.staffArrayList.get(index);

        staffIdField.setText(currentStaff.getStaffId() + "");
        firstNameField.setText(currentStaff.getFirstName());
        lastNameField.setText(currentStaff.getLastName());
        dateOfBirthField.setText(currentStaff.getDateOfBirth());
        departmentField.setText(currentStaff.getDepartment());
        salaryField.setText(currentStaff.getSalary() + "");
        startDateField.setText(currentStaff.getStartDate());
        fullTimeField.setText(currentStaff.isFullTime() + "");
    }
}
