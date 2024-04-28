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


    public ArrayList<String> getUserData(int id)
    {
        connect();
        ArrayList<String> data = new ArrayList<>();
        PreparedStatement Ps = null;
        ResultSet myRs = null;
        try {

            Ps = conn.prepareStatement("SELECT  first_name, last_name,email, phone_number from Users where Users.id = ?",
                    ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            Ps.setInt(1, id);
            myRs = Ps.executeQuery();
            while (myRs.next()) {
                data.add(myRs.getObject(0).toString());
                data.add(myRs.getObject(1).toString());
                data.add(myRs.getObject(2).toString());
                data.add(myRs.getObject(3).toString());
            }

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
        PreparedStatement Ps = null;
        ResultSet myRs = null;

        try {
            Ps = conn.prepareStatement("SELECT type,city,price,description from Offers join Users on Users.id=Offers.id where Users.id = ?",
                    ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            Ps.setInt(1, id);
            myRs = Ps.executeQuery();
            while (myRs.next()) {

                Vector<String> tmp = new Vector<>();
                tmp.add( (String)myRs.getObject(0));
                tmp.add((String)(myRs.getObject(1)));
                tmp.add((String)(myRs.getObject(2)));
                tmp.add((String)(myRs.getObject(3)));
                data.add(tmp);
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }

        closeConnection();
        return data;
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
