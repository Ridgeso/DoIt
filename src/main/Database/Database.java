package main.Database;
import java.sql.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import main.Application;
import main.Database.Models.Offer;
import main.Database.Models.User;

import javax.sound.midi.Soundbank;
import javax.swing.text.Style;
import javax.xml.crypto.Data;

public class Database implements AutoCloseable {
    public Connection conn = null;
    private boolean use_test = false;

    public Database(){
        connect();
    }
    public  Database(Connection con){
        conn = con;
        use_test = true;

    }

    public void connect() {

        Properties prop = new Properties();
        try (FileInputStream input = new FileInputStream("src/main/Database/dbCredentials.env"))
        {
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

    @Override
    public void close(){
        try {
            if (conn != null) {
                conn.close();
                conn = null;
                System.out.println("Connection to Postgres has been closed.");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void addNewUser(String firstName, String lastName, String email, String login, String password, String phoneNumber){
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
    }

    public int checkIfUserExist(String userFName, String userLName) throws SQLException {
        Statement stmt = conn.createStatement();
        String userSelect = MessageFormat.format(
            "SELECT id FROM users WHERE first_name = ''{0}'' and last_name = ''{1}'';",
            userFName,
            userLName);
        ResultSet checkExistID = stmt.executeQuery(userSelect);
        int user_id = checkExistID.next() ? checkExistID.getInt(1) : Application.INVALID_USER_ID;
        return user_id;
    }

    public int checkUserLogin(String login, String password) throws SQLException {
        Statement stmt = conn.createStatement();
        String userSelect = MessageFormat.format(
            "SELECT id FROM users WHERE login = ''{0}'' and password = ''{1}'';",
            login,
            password);
        ResultSet checkExistID = stmt.executeQuery(userSelect);
        int user_id = checkExistID.next() ? checkExistID.getInt(1) : Application.INVALID_USER_ID;
        return user_id;
    }

    public ArrayList<String> getUserData(int id)
    {
        ArrayList<String> data = new ArrayList<>();
        ResultSet myRs = null;
        String userSelect = "SELECT first_name,last_name,email,phone_number FROM users WHERE users.id =?;";

        try (PreparedStatement Ps = conn.prepareStatement(userSelect, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)){
            if(Ps != null){
                Ps.setInt(1, id);
                myRs = Ps.executeQuery();
                if(myRs!=null) {
                    while (myRs.next()) {
                        data.add(myRs.getString(1));
                        data.add(myRs.getString(2));
                        data.add(myRs.getString(3));
                        data.add(myRs.getString(4));
                        System.out.println("Pobrano dane uzytkownika: " + myRs.getString(1) + " " + myRs.getString(2) + " " + myRs.getString(3) + " " + myRs.getString(4));
                    }
                }
            }

        }
        catch (SQLException e){
            System.out.println("getUserData: " + e.getMessage());
        }
        catch (NullPointerException e){
            System.out.println("Null pointer caught");
        }

        return data;
    }

    public Vector<Offer> getUserOffers(int id)
    {
        Vector<Offer> data = new Vector<>();
        ResultSet myRs = null;
        String updateString = "SELECT offers.id as id,type,city,price,description from offers join users on users.id=offers.id_user where users.id = ?";
        Offer offer = null;

        try (PreparedStatement Ps = conn.prepareStatement(updateString, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)) {

            if (Ps != null) {
                Ps.setInt(1, id);
                myRs = Ps.executeQuery();
                while (myRs.next()) {

                    String OfferID = myRs.getString("id");
                    String type = myRs.getString("type");
                    String city = myRs.getString("city");
                    String price = myRs.getString("price");
                    String description = myRs.getString("description");
                    offer = new Offer(Integer.parseInt(OfferID), "", Double.parseDouble(price), type, description, city, price);
                    data.add(offer);
                }
            }
        }
        catch (SQLException e){
            System.out.println("getUserOffers: " + e.getMessage());
        }
        return data;
    }

    public Vector<Vector<String>> getUserApplications(int id){
        Vector<Vector<String>> data = new Vector<>();
        String updateString = "SELECT offer_id,type,city,price from (applicants join users on users.id=applicants.user_id) join offers on applicants.offer_id = offers.id where users.id = ?";
        ResultSet myRs = null;

        try(PreparedStatement Ps = conn.prepareStatement(updateString, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE) ){
            if(Ps != null) {
                Ps.setInt(1, id);
                myRs = Ps.executeQuery();
                while (myRs.next()) {

                    Vector<String> tmp = new Vector<>();
                    tmp.add((String) myRs.getString("offer_id"));
                    tmp.add((String) myRs.getString("type"));
                    tmp.add((String) (myRs.getString("city")));
                    tmp.add((String) (myRs.getString("price")));
                    data.add(tmp);

                }
            }
        }
        catch (SQLException e){
            System.out.println("getUserApplications: " + e.getMessage());
        }
        return data;

    }
    public Vector<User> getApplicantsData(int id){
        Vector<User> data = new Vector<>();
        String updateString = "SELECT first_name,last_name,email,phone_number from (applicants join users on users.id=applicants.user_id)" +
                " join offers on applicants.offer_id = offers.id where offers.id = ?";
        ResultSet myRs = null;
        User user = null;
        try(PreparedStatement Ps = conn.prepareStatement(updateString, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE) ){
            if(Ps != null) {
                Ps.setInt(1, id);
                myRs = Ps.executeQuery();
                while (myRs.next()) {

                    Vector<String> tmp = new Vector<>();
                    String fname = myRs.getString("first_name");
                    String lname = myRs.getString("last_name");
                    String email = myRs.getString("email");
                    String phoneNumber = myRs.getString("phone_number");
                    user = new User(fname,lname,email,"","",phoneNumber);
                    data.add(user);
                }
            }
        }
        catch (SQLException e){
            System.out.println("getUserApplications: " + e.getMessage());
        }
        return data;

    }
    public User getAnnoucerData(int id){
        Vector<User> data = new Vector<>();
        String updateString = " SELECT first_name,last_name,email,phone_number from" +
                " Users join offers on Users.id = offers.id_user where offers.id = ?";
        ResultSet myRs = null;
        User user = null;
        try(PreparedStatement Ps = conn.prepareStatement(updateString, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE) ){
            if(Ps != null) {
                Ps.setInt(1, id);
                myRs = Ps.executeQuery();
                while (myRs.next()) {   

                    Vector<String> tmp = new Vector<>();
                    String fname = myRs.getString("first_name");
                    String lname = myRs.getString("last_name");
                    String email = myRs.getString("email");
                    String phoneNumber = myRs.getString("phone_number");
                    user = new User(fname,lname,email,"","",phoneNumber);
                }
            }
        }
        catch (SQLException e){
            System.out.println("getUserApplications: " + e.getMessage());
        }
        return user;

    }
    public void addNewOfferWhenUserExists(Integer user_id, String city, String type, float price, String description)throws SQLException
    {
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

    public Offer getOfferById(String id) {
        Offer offer = null;
        String updateString = "SELECT * FROM Offers JOIN Users ON Users.id = Offers.id_user WHERE Offers.id = ?";
        try (PreparedStatement Ps = conn.prepareStatement(updateString)) {
            Ps.setInt(1, Integer.parseInt(id));
            try (ResultSet myRs = Ps.executeQuery()) {

                if (myRs.next()) {
                    String type = myRs.getString("type");
                    String city = myRs.getString("city");
                    String price = myRs.getString("price");
                    String description = myRs.getString("description");
                    String phoneNumber = myRs.getString("phone_number");
                    offer = new Offer(Integer.parseInt(id), phoneNumber, Double.parseDouble(price), type, description, city, price);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return offer;
    }
    
    
    public List<Offer> getAllOffers() {
        List<Offer> offers = new ArrayList<>();
        String queryString = "SELECT * FROM Offers JOIN Users ON Users.id = Offers.id_user";
        try (PreparedStatement ps = conn.prepareStatement(queryString);
             ResultSet resultSet = ps.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String type = resultSet.getString("type");
                String city = resultSet.getString("city");
                String price = resultSet.getString("price");
                String description = resultSet.getString("description");
                String phoneNumber = resultSet.getString("phone_number");
                double rate = resultSet.getDouble("price");
                Offer offer = new Offer(id, phoneNumber, rate, type, description, city, price);
                offers.add(offer);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return offers;
    }
    public void assignUserToOffer(int offerId, int userId) {
        try{
            if(!use_test)
                connect();
            Statement stmt = conn.createStatement();
            if (userId== -1){
                System.out.println("User doesn't exist");
                return;
            }
            String queryString = "insert into applicants(user_id, offer_id) values (" + userId + ",\'" + offerId+"\');";


            stmt.executeUpdate(queryString);
            System.out.println("Pomyślnie zaaplikowano");
        }catch (Exception e){}
    }
    public int countApplications(int offer_id){
        String queryString = "SELECT COUNT(*) FROM applicants WHERE offer_id=" + offer_id;
        try (PreparedStatement ps = conn.prepareStatement(queryString)) {
            try (ResultSet myRs = ps.executeQuery()) {
                if (myRs.next()) {
                    return myRs.getInt(1);
                }
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return 0;

    }
}
