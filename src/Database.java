import java.sql.*;

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

    private Integer checkIfUserExist(String userFName, String userLName) throws SQLException{
        Integer user_id = -1;
        Statement stmt = conn.createStatement();
        String userSelect = "SELECT id FROM users WHERE first_name = \'" + userFName + "\' and last_name = \'" + userLName + "\';";
        ResultSet checkExistID = stmt.executeQuery(userSelect);
        boolean exist = false;
        while(checkExistID.next()){
            exist = true;
            user_id = checkExistID.getInt(1);
        }
        return user_id;
    }

    public void addNewOffer(String userFName, String userLName, String city, String type, float price, String description){
        connect();
        try {
            Integer user_id = checkIfUserExist(userFName, userLName);
            Statement stmt = conn.createStatement();
            if (user_id == -1){
                System.out.println("User doesn't exist");
                return;
            }
            String insert = "INSERT INTO offers(id_user, type, city, price, description) VALUES (" + user_id.toString() + ",\'" +
                    type + "\', \'" + city + "\', " + price + ", \'" + description + "\');";
            stmt.executeUpdate(insert);
            System.out.println("Urzytkownik dodany");
        }
        catch (SQLException e){
            System.out.println("ERROR:" + e.getMessage());
        }
        closeConnection();
    }
}
