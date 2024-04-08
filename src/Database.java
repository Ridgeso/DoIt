import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private Connection conn = null;
    private void connect() {

        try {
            Class.forName("org.postgresql.Driver");
        }
        catch (java.lang.ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        try {
            // db parameters - specify the path to your database file
            String url = "jdbc:postgresql://isilo.db.elephantsql.com/hznwjjkw";
            String user = "hznwjjkw";
            String pass = "Z4kfkk7BkYQeCZsecNF6N8gPKn-QDLSs";
            // create a connection to the database
            conn = DriverManager.getConnection(url, user, pass);

            System.out.println("Connection to Postgres has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void closeConnection(){
        try {
            if (conn != null) {
                conn.close();
                conn = null;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void addNewUser(String firstName, String lastName, String email, String login, String password, String phoneNumber){
        connect();
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            String insert = "INSERT INTO Users (login, password, email, first_name, last_name, phone_number) VALUES (" +
                    "\'" +login + "\',\'" + password + "\',\'" + email + "\',\'" + firstName + "\',\'" + lastName + "\',\'" + phoneNumber + "\');";
            stmt.executeUpdate(insert);
            System.out.println("INSERTED USER");
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        closeConnection();
    }

    public void addNewOffer(String userFName, String userLName, String city, String type, float price, String description){
        connect();
//        try {
            Statement stmt = null;
            String userSelect = "SELECT id FROM users WHERE first_name = \'" + userFName + "\' and \' last_name = \'" + userLName + "\');";
//            stmt = conn.createStatement();
//            String output = stmt.exe
//        }
//        catch (SQLException e){
//            System.out.println(e.getMessage());
//        }
        closeConnection();
    }
}
