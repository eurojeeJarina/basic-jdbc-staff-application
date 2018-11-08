package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.ArrayList;


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


    // ALL BUTTONS DECLARED HERE
    @FXML
    Button previousBtn;
    @FXML
    Button nextBtn;
    @FXML
    Button browseAllBtn;


    @FXML
    public void initialize() {
        staffQueries = new StaffQueries();
        statusLabel.setText(staffQueries.statusConnection());
        displayStaff();
    }

    @FXML
    public void onButtonClicked(ActionEvent e) {
        if (e.getSource().equals(previousBtn)) {
            System.out.println("Previous Button here!");

        } else if (e.getSource().equals(browseAllBtn)) { // If browse all btn is clicked
            displayStaff();
        }else if(e.getSource().equals(nextBtn)) // If next btn is clicked
        {
            nextStaff();
        }
    }
    private void nextStaff()
    {

        this.currentEntryIndex++;
        // the current index is already at index[0]
        // add 1 to the currentEntryIndex when the button is pressed


        if(this.currentEntryIndex >= this.numberOfEntries)
        {
            // if the currentEntryIndex have reached the max number of entries
            // subtract 1 from it to avoid the out of bounds exception
            // and then disable the next button;
            this.currentEntryIndex--;
            nextBtn.setDisable(true);
        }

        indexField.setText((this.currentEntryIndex+1) + "");
        displayCurrentStaff(this.currentEntryIndex);

    }
    private void previousStaff()
    {

    }

    private void displayStaff() {
        try {
            staffArrayList = staffQueries.getAllStaff();
            numberOfEntries = staffArrayList.size();

            if (numberOfEntries != 0) {
                currentEntryIndex = 0;
                currentStaff = staffArrayList.get(currentEntryIndex);

                staffIdField.setText(currentStaff.getStaffId()+"");
                firstNameField.setText(currentStaff.getFirstName());
                lastNameField.setText(currentStaff.getLastName());
                dateOfBirthField.setText(currentStaff.getDateOfBirth());
                departmentField.setText(currentStaff.getDepartment());
                salaryField.setText(currentStaff.getSalary()+"");
                startDateField.setText(currentStaff.getStartDate());
                fullTimeField.setText(currentStaff.isFullTime()+"");

                maxIndexField.setText(numberOfEntries + "");
                indexField.setText((currentEntryIndex + 1) + "");
            }

        } catch (Exception ex) {
            System.err.println("Problem with database connection.");
        }
    }

    private void displayCurrentStaff(int index)
    {
        currentStaff = this.staffArrayList.get(index);

        staffIdField.setText(currentStaff.getStaffId()+"");
        firstNameField.setText(currentStaff.getFirstName());
        lastNameField.setText(currentStaff.getLastName());
        dateOfBirthField.setText(currentStaff.getDateOfBirth());
        departmentField.setText(currentStaff.getDepartment());
        salaryField.setText(currentStaff.getSalary()+"");
        startDateField.setText(currentStaff.getStartDate());
        fullTimeField.setText(currentStaff.isFullTime()+"");
    }
}
