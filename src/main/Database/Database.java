package main.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.Vector;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private Connection conn = null;

    public void connect() {

        Properties prop = new Properties();
        try (FileInputStream input = new FileInputStream("src/main/Database/dbCredentials.env")) {
            prop.load(input);
            Class.forName("org.postgresql.Driver");

            String url = prop.getProperty("db.url");
            String user = prop.getProperty("db.user");
            String pass = prop.getProperty("db.pass");

            conn = DriverManager.getConnection(url, user, pass);

            System.out.println("Connection to Postgres has been established.");
        } catch (ClassNotFoundException | SQLException | IOException e) {
            e.printStackTrace();
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

    public Integer checkUserLogin(String login, String password) throws SQLException{
        Integer user_id = -1;
        Statement stmt = conn.createStatement();
        String userSelect = "SELECT id FROM users WHERE first_name = \'" + login + "\' and last_name = \'" + password + "\';";
        ResultSet checkExistID = stmt.executeQuery(userSelect);
        boolean exist = false;
        while(checkExistID.next()){
            exist = true;
            user_id = checkExistID.getInt(1);
        }
        return user_id;
    }

    public ArrayList<String> getUserData(int id)
    {
        connect();
        ArrayList<String> data = new ArrayList<>();
        String updateString = "SELECT first_name,last_name,email,phone_number FROM Users WHERE Users.id = ?";
        ResultSet myRs = null;
        try (PreparedStatement Ps = conn.prepareStatement(updateString, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)){

            if(Ps != null) {

                Ps.setInt(1, id);
                myRs = Ps.executeQuery();
                while (myRs.next()) {
                    data.add(myRs.getObject(0).toString());
                    data.add(myRs.getObject(1).toString());
                    data.add(myRs.getObject(2).toString());
                    data.add(myRs.getObject(3).toString());
                }
            }
            System.out.println("Pobrano dane uzytkownika");

    }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }

        closeConnection();
        return data;
    }

    public Vector<Vector<String>> getUserOffers(int id)
    {
        connect();
        Vector<Vector<String>> data = new Vector<>();
        ResultSet myRs = null;
        String updateString = "SELECT type,city,price,description from Offers join Users on Users.id=Offers.id where Users.id = ?";

        try (PreparedStatement Ps = conn.prepareStatement(updateString, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)) {

            if (Ps != null) {
                Ps.setInt(1, id);
                myRs = Ps.executeQuery();
                while (myRs.next()) {

                    Vector<String> tmp = new Vector<>();
                    tmp.add((String) myRs.getObject(0));
                    tmp.add((String) (myRs.getObject(1)));
                    tmp.add((String) (myRs.getObject(2)));
                    tmp.add((String) (myRs.getObject(3)));
                    data.add(tmp);
                }
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }

        closeConnection();
        return data;
    }
    public Vector<Vector<String>> getUserApplications(int id){
        connect();
        Vector<Vector<String>> data = new Vector<>();
        String updateString = "SELECT type,city,price from (Applications join Users on Users.id=Application.id_user) join Offers on Application.id_offer = Offers.id where Users.id = ?";
        ResultSet myRs = null;

        try(PreparedStatement Ps = conn.prepareStatement(updateString, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE) ){
            if(Ps != null) {
                Ps.setInt(1, id);
                myRs = Ps.executeQuery();
                while (myRs.next()) {

                    Vector<String> tmp = new Vector<>();
                    tmp.add((String) myRs.getObject(0));
                    tmp.add((String) (myRs.getObject(1)));
                    tmp.add((String) (myRs.getObject(2)));
                    data.add(tmp);

                }
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        closeConnection();
        return data;

    }
    public void addNewOfferWhenUserExists(Integer user_id, String city, String type, float price, String description)throws SQLException
    {

        connect();

        Statement stmt = conn.createStatement();
        if (user_id == -1){
            System.out.println("User doesn't exist");
            return;
        }
        String insert = "INSERT INTO offers(id_user, type, city, price, description) VALUES (" + user_id.toString() + ",\'" +
                type + "\', \'" + city + "\', " + price + ", \'" + description + "\');";
        stmt.executeUpdate(insert);
        System.out.println("Urzytkownik dodany");

        closeConnection();
    }
}
