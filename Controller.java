package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.converter.LocalDateTimeStringConverter;

import javax.swing.text.DateFormatter;
import javax.xml.soap.Text;
import java.sql.SQLException;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
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
    DatePicker datePickerField, startDatePickerField;
    @FXML
    ComboBox departmentDropDown, fullTimeDropDown;

    @FXML
    public void initialize() {
        staffQueries = new StaffQueries(); // initialize the
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
            //System.out.println(dateFormatter(this.startDatePickerField.getValue()));

        } else if (e.getSource().equals(cancelButton)) {
            resetControls();
            displayStaff();
        }
    }

    @FXML
    public void onKeyPressed(ActionEvent event) {
        try {

            if (event.getSource().equals(indexField)) {
                int inputIndex = Integer.parseInt(indexField.getText());

                if (inputIndex < 1 || inputIndex > this.numberOfEntries) {
                    System.out.println("Please enter number within range.");
                } else {
                    displayCurrentStaff(inputIndex - 1);
                }
            } else if (event.getSource().equals(salaryField)) {
                double salaryValue = Double.parseDouble(salaryField.getText());
                if (salaryValue < 10000f || salaryValue > 99000f) {
                    statusLabel.setTextFill(Color.RED);
                    statusLabel.setText("Salary (10000-99000)");
                } else {
                    statusLabel.setTextFill(Color.BLACK);
                    statusLabel.setText("Add New Staff?");
                }
            }

        } catch (NumberFormatException ex) {
            System.out.println("Please enter a valid number");
        }
    }

    private void insertButtonHandler() {
        unResetControls();

        // DISABLE DATE OF BIRTH FIELDS
        datePickerField.setVisible(true);
        datePickerField.setDisable(false);

        dateOfBirthField.setDisable(true);
        dateOfBirthField.setVisible(false);

        // DISABLE START DATE FIELDS
        startDatePickerField.setVisible(true);
        startDatePickerField.setDisable(false);

        startDateField.setVisible(false);
        startDateField.setDisable(true);

        // DISABLED DEPARTMENT FIELD HERE
        departmentField.setDisable(true);

        departmentDropDown.setDisable(false);
        departmentDropDown.setVisible(true);
        departmentDropDown.setEditable(false);
        departmentDropDown.getSelectionModel().clearSelection();

        // DISABLED FULLTIME FIELDS
        fullTimeField.setDisable(true);
        fullTimeField.setVisible(false);

        fullTimeDropDown.setVisible(true);
        fullTimeDropDown.setDisable(false);
        fullTimeDropDown.getSelectionModel().selectFirst();



        statusLabel.setText("Add New Staff?");
    }

    private boolean areFieldsValid() {


        if (firstNameField.getText().isEmpty() ||
                lastNameField.getText().isEmpty() ||
                dateFormatter(this.datePickerField.getValue()) == null ||
                departmentDropDown.getValue() == null ||
                dateFormatter(this.startDatePickerField.getValue()) == null ||
                salaryField.getText().isEmpty() ||
                fullTimeDropDown.getValue() == null) {

                statusLabel.setTextFill(Color.RED);
                statusLabel.setText("You have missing fields.");
            return false;

        } else {
            try {
                double salaryValue = Double.parseDouble(salaryField.getText());
                if (salaryValue < 10000f || salaryValue > 99000f)
                {
                    statusLabel.setTextFill(Color.RED);
                    statusLabel.setText("Salary not in Range 10000-99000");
                    return false;
                }
                else{
                        return true;
                }
            }
            catch (NumberFormatException e)
            {
                statusLabel.setTextFill(Color.RED);
                statusLabel.setText("Salary must be numbers.");
                return false;
            }
        }
    }

    private void addNewStaff(boolean isValid) {

        if (!isValid) {
            //
        } else {

            addNewStaff(firstNameField.getText(),
                    lastNameField.getText(),
                    dateFormatter(this.datePickerField.getValue()),
                    departmentDropDown.getValue().toString(),
                    salaryField.getText(),
                    dateFormatter(this.startDatePickerField.getValue()),
                    fullTimeDropDown.getValue().toString());
        }
    }

    /* @Overloaded method this method checks if the name already exist in the database*/
    private void addNewStaff(String firstName,
                             String lastName,
                             String dob,
                             String dept,
                             String sal,
                             String startDate,
                             String fullTime) {

        double salary = Double.parseDouble(sal);
        boolean isFull = Boolean.parseBoolean(fullTime);

        boolean isInserted = staffQueries.addStaff(firstName, lastName, dob, dept, salary, startDate, isFull);
        if (!isInserted) {
            statusLabel.setTextFill(Color.RED);
            statusLabel.setText("Staff already exists.");
        } else {
            statusLabel.setTextFill(Color.GREEN);
            statusLabel.setText("Successfully added a new staff.");
            displayStaff();
            resetControls();
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
            statusLabel.setText("Connected to the database");

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
            statusLabel.setTextFill(Color.RED);
            statusLabel.setText("Problem connecting to the database.");
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

    private void resetControls() {
        //ReEnable THE DISABLED PRIMARY BUTTONS
        // index field editable = true;
        // search fields editable = true;

        indexField.setDisable(false);
        maxIndexField.setDisable(false);

        previousBtn.setDisable(false); // previous button is ENABLED
        nextBtn.setDisable(false);  // next button is ENABLED
        searchByNameBtn.setDisable(false);
        searchByDepartmentBtn.setDisable(false);
        updateButton.setDisable(false);


        browseAllBtn.setDisable(false);
        insertButton.setDisable(false);

//        indexField.setEditable(true);
//        searchByDepartmentField.setEditable(true);
//        searchByFirstNameField.setEditable(true);
        searchByDepartmentField.setDisable(false);
        searchByFirstNameField.setDisable(false);

        saveButton.setDisable(true);
        cancelButton.setDisable(true);

        // Date of Birth picker field
        datePickerField.setVisible(false);
        datePickerField.setDisable(true);

        dateOfBirthField.setDisable(false);
        dateOfBirthField.setVisible(true);

        // START DATE FIELDS
        startDatePickerField.setVisible(false);
        startDatePickerField.setDisable(true);

        startDateField.setVisible(true);
        startDateField.setDisable(false);

        // Department fields
        departmentDropDown.setEditable(false);
        departmentDropDown.setVisible(false);


        departmentField.setDisable(false);

        // DISABLED FULLTIME FIELDS
        fullTimeField.setDisable(false);
        fullTimeField.setVisible(true);

        fullTimeDropDown.setVisible(false);
        fullTimeDropDown.setDisable(true);

        // Status fields
        statusLabel.setTextFill(Color.BLACK);
    }

    private void unResetControls() {
        // Clear the fields of top panel
        indexField.clear();
        maxIndexField.clear();
        indexField.setDisable(true);
        maxIndexField.setDisable(true);

        // Clear the fields of info panel
        staffIdField.clear();
        firstNameField.clear();
        lastNameField.clear();
        dateOfBirthField.clear();
        datePickerField.setValue(null);
        departmentField.clear();
        salaryField.clear();
        startDatePickerField.setValue(null);
        fullTimeField.clear();

        // Clear the search fields
        searchByFirstNameField.clear();
        searchByDepartmentField.clear();

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

        searchByDepartmentField.setDisable(true);
        searchByFirstNameField.setDisable(true);

        saveButton.setDisable(false);
        cancelButton.setDisable(false);
    }

    private String dateFormatter(LocalDate date) {
        LocalDate dateValue = date;
        String datePickerValue = "";

        if (dateValue == null) {
            datePickerValue = null;
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
            datePickerValue = formatter.format(dateValue);
        }

        return datePickerValue;
    }
}
