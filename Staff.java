package sample;

public class Staff {

    private int staffId;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String department;
    private double salary;
    private String startDate;
    private boolean fullTime;

    public Staff()
    {

    }
    public Staff(int staffId, String firstName, String lastName, String dateOfBirth, String department, double salary, String startDate, boolean fullTime) {
        this.staffId = staffId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.department = department;
        this.salary = salary;
        this.startDate = startDate;
        this.fullTime = fullTime;
    }

    public int getStaffId() {
        return staffId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getDepartment() {
        return department;
    }

    public double getSalary() {
        return salary;
    }

    public String getStartDate() {
        return startDate;
    }

    public boolean isFullTime() {
        return fullTime;
    }
}
