package sample;


import java.sql.*;
import java.util.ArrayList;

public class StaffQueries {


    private static final String _URL = "jdbc:mysql://localhost:3306/iactproject";
    private static final String _USER = "root";
    private static final String _PASSWORD = "";

    private static Connection conn;
    private static PreparedStatement browseAll;
    private static PreparedStatement searchStatement;
    private static PreparedStatement searchByDepartmentStatement;
    private static PreparedStatement insertStatement;
    private static PreparedStatement findStatement;
    private static PreparedStatement updateStatement;

    private String status = "Loading...";

    public StaffQueries() {
        connection();
    }

    private void connection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            this.status = "Driver successfully loaded";

            final String _SELECTALL = "SELECT * FROM staff1"; // select entire table

            final String _SEARCHQUERY = "SELECT * FROM staff1 WHERE " +
                    "UPPER(FirstName) LIKE UPPER(?) OR " +
                    "UPPER(LastName) LIKE UPPER(?)";

            final String _SEARCHDEPTQUERY = "SELECT * FROM staff1 " +
                    "WHERE UPPER(Department) LIKE UPPER(?)";     // search if FirstName = '' OR LastName='' OR Department=''

            final String _INSERTQUERY = "INSERT INTO staff1" +
                    "(FirstName, LastName, DateOfBirth,Department,Salary,StartDate,Fulltime)" +
                    "VALUES (?,?,?,?,?,?,?)";

//            final String _DELETEQUERY = "DELETE FROM staff1 " +
//                    "WHERE StaffId=(?)";

            final String _FINDQUERY = "SELECT * FROM staff1 WHERE " +
                    "UPPER(FirstName) LIKE UPPER(?) AND " +
                    "UPPER(LastName) LIKE UPPER(?)";

            final String _UPDATEQUERY = "UPDATE staff1 " +
                    "SET FirstName= (?), LastName = (?), DateOfBirth = (?), Department = (?), Salary = (?), StartDate = (?), Fulltime = (?)" +
                    "WHERE StaffId = (?)";

            //establish a connection to the database
            conn = DriverManager.getConnection(_URL, _USER, _PASSWORD);
            //System.out.println("Connection to database successful!");
            this.status = "Connected to the database";

            browseAll = conn.prepareStatement(_SELECTALL);
            searchStatement = conn.prepareStatement(_SEARCHQUERY);
            searchByDepartmentStatement = conn.prepareStatement(_SEARCHDEPTQUERY);
            insertStatement = conn.prepareStatement(_INSERTQUERY);
            findStatement = conn.prepareStatement(_FINDQUERY);
            updateStatement = conn.prepareStatement(_UPDATEQUERY);

        } catch (SQLException sqlex) {
            System.err.println("Unable to connect to the Database");
            sqlex.printStackTrace(); // debugging;
            System.exit(1);
        } catch (ClassNotFoundException classException) {
            System.err.println("Unable to load MySQL Driver!");
            classException.printStackTrace();
            System.exit(2);
        }
    }

    private void close() {
        try {
            conn.close();
            this.status = "Disconnected";
        } catch (SQLException sqlEx) {
            System.err.println("Couldn't close connection");
            sqlEx.printStackTrace();
        }
    }
    /*******************GET ALL METHOD***********************
     *
     *
     * *this method will get all the staff to database*/
    public ArrayList<Staff> getAllStaff() {
        ArrayList<Staff> staffArrayList = null;
        ResultSet resultSet = null;

        try {

            resultSet = browseAll.executeQuery();
            staffArrayList = new ArrayList<Staff>();

            // store data from the database into a local variable


            while (resultSet.next()) {

                staffArrayList.add(new Staff(resultSet.getInt("StaffId"),
                        resultSet.getString("FirstName"),
                        resultSet.getString("LastName"),
                        resultSet.getString("DateOfBirth"),
                        resultSet.getString("Department"),
                        resultSet.getDouble("Salary"),
                        resultSet.getString("StartDate"),
                        resultSet.getBoolean("Fulltime")));
            }

        } catch (SQLException sqlEx1) {
            System.err.println("Lost connection with the database, try later...");
            sqlEx1.printStackTrace();
        } finally { // no matter what happen, run this finally code block
            try {
                resultSet.close();
            } catch (SQLException sqlEx2) {
                System.err.println("Something went wrong, closing connection....");
                close(); // close the database connection
            }
        }

        return staffArrayList;
    }

    /*******************INSERT METHOD***********************
     *
     *
     * *this method will add staff to database*/
    public boolean addStaff(String firstName, String lastName, String dob, String dept,
                            double salary, String startDate, boolean fullTime) {
        boolean results = ifStaffExist(firstName,lastName);

        try {
            if (results) {
                // the staff already exist then return false;
                return false;
            } else {
                insertStatement.setString(1, firstName);
                insertStatement.setString(2, lastName);
                insertStatement.setString(3, dob);
                insertStatement.setString(4, dept);
                insertStatement.setDouble(5, salary);
                insertStatement.setString(6, startDate);
                insertStatement.setBoolean(7, fullTime);

                insertStatement.executeUpdate();

                // SUCCESS adding new staff return true;
                return true;
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        return false;
    }

    /*******************UPDATE METHOD***********************
     *
     *
     * *this method will update staff to database*/

    public boolean updateStaff(int staffId, String firstName, String lastName, String dateOfBirth, String department, double salary,
                                String startDate, boolean fullTime)
    {
        try{
            updateStatement.setString(1, firstName);
            updateStatement.setString(2, lastName);
            updateStatement.setString(3, dateOfBirth);
            updateStatement.setString(4, department);
            updateStatement.setDouble(5, salary);
            updateStatement.setString(6, startDate);
            updateStatement.setBoolean(7, fullTime);
            updateStatement.setInt(8, staffId);

            updateStatement.executeUpdate();

            return true;

        }catch (SQLException sqlEx)
        {
            sqlEx.printStackTrace();
        }

        return false;
    }

    /*******************Find METHOD************************
     let us try find if the data is in the database return true or false; */

    public ArrayList<Staff> searchStaff(String query) {
        ArrayList<Staff> staffArrayList = null;
        ResultSet resultSet;


        if (!findStaff(query)) {
            System.out.println("sorry not found");
        } else {
            try {
                searchStatement.setString(1, "%" + query + "%");
                searchStatement.setString(2, "%" + query + "%");

                resultSet = searchStatement.executeQuery();
                staffArrayList = new ArrayList<>();


                while (resultSet.next()) {
                    // add a new Staff object to the arrayList
                    staffArrayList.add(new Staff(resultSet.getInt("StaffId"),
                            resultSet.getString("FirstName"),
                            resultSet.getString("LastName"),
                            resultSet.getString("DateOfBirth"),
                            resultSet.getString("Department"),
                            resultSet.getDouble("Salary"),
                            resultSet.getString("StartDate"),
                            resultSet.getBoolean("Fulltime")));
                }

            } catch (SQLException sqlEx) {
                sqlEx.printStackTrace();
            }
        }

        return staffArrayList;
    }

    public ArrayList<Staff> searchStaffByDepartment(String query) {
        ArrayList<Staff> staffArrayList = null;
        ResultSet resultSet;

            try {
                searchByDepartmentStatement.setString(1, "%" + query + "%");

                resultSet = searchByDepartmentStatement.executeQuery();
                staffArrayList = new ArrayList<>();


                while (resultSet.next()) {
                    // add a new Staff object to the arrayList
                    staffArrayList.add(new Staff(resultSet.getInt("StaffId"),
                            resultSet.getString("FirstName"),
                            resultSet.getString("LastName"),
                            resultSet.getString("DateOfBirth"),
                            resultSet.getString("Department"),
                            resultSet.getDouble("Salary"),
                            resultSet.getString("StartDate"),
                            resultSet.getBoolean("Fulltime")));
                }

            } catch (SQLException sqlEx) {
                sqlEx.printStackTrace();
            }


        return staffArrayList;
    }

    private boolean ifStaffExist(String firstName, String lastName)
    {
        boolean result = false;
        ResultSet resultSet;

        try {
            findStatement.setString(1, "%" + firstName + "%");
            findStatement.setString(2, "%" + lastName + "%");
            // searchStatement.setString(3, "%" + query + "%");

            resultSet = findStatement.executeQuery();
            result = resultSet.next(); // check if there are rows in the database

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }

        return result;
    }

    private boolean findStaff(String query) {

        boolean result = false;
        ResultSet resultSet;


        try {
            searchStatement.setString(1, "%" + query + "%");
            searchStatement.setString(2, "%" + query + "%");
            // searchStatement.setString(3, "%" + query + "%");

            resultSet = searchStatement.executeQuery();
            result = resultSet.next(); // check if there are rows in the database

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }

        return result;
    }

    public String statusConnection() {
        return this.status;
    }

    private void statusConnection(String message) {
        this.status = this.status + "\n" + message;
    }
}
