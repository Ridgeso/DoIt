package Tests;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import main.Database.Models.Offer;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.sql.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import main.Database.Database;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class TestDatabase {
    private Database sut;
    private Connection mockConnection;
    private Statement mockStatement;

    static MockedStatic<DriverManager> mockDriverManager;

    void expectGetConnection()
            throws SQLException {
        mockDriverManager = mockStatic(DriverManager.class);
        mockConnection = mock(Connection.class);
        mockStatement = mock(Statement.class);
        when(mockConnection.createStatement()).thenReturn(mockStatement);
    }

    @Test
    void verifyGetConnection() {
        mockDriverManager.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                .thenReturn(mockConnection);

    }

    @BeforeEach
    void setUp() throws SQLException {
        try (var __ = mockConstruction(Database.class)) {
            MockitoAnnotations.openMocks(this);
        }
        expectGetConnection();
        sut = new Database(mockConnection);
    }


    @AfterEach
    void finish() {
        //verifyGetConnection();
        mockDriverManager.close();
    }

    @Test
    void testConnect() {
        sut.connect();
    }

    @Test
    void checkUserLogin() throws SQLException{
        String login = "validUser";
        String password = "validPassword";
        int expectedUserId = 1;

        String sqlStatement = MessageFormat.format(
                "SELECT id FROM users WHERE login = ''{0}'' and password = ''{1}'';",
                login,
                password);

        try (var __ = mock(PreparedStatement.class)) {

            var mockRez = mock(ResultSet.class);

//            when(mockConnection.prepareStatement(sqlStatement)).thenReturn(mockStatement);
            when(mockStatement.executeQuery(sqlStatement)).thenReturn(mockRez);
            when(mockRez.next()).thenReturn(true);
            when(mockRez.getInt(1)).thenReturn(expectedUserId);

            int actualUserId = sut.checkUserLogin(login, password);
//            assertEquals(expectedUserId, actualUserId);

            verify(mockConnection, times(1)).createStatement();

        }
    }


    @Test
    void addNewUser()
        throws SQLException
    {
        var firstName = "firstName";
        var lastName = "lastName";
        var email = "email";
        var login = "login";
        var password = "password";
        var phoneNumber = "phoneNumber";

        try (var mockStatement = mock(Statement.class))
        {
            when(mockConnection.createStatement()).thenReturn(mockStatement);
            when(mockStatement.executeUpdate(anyString())).thenReturn(0);

            sut.addNewUser(firstName, lastName, email, login, password, phoneNumber);

            verify(mockConnection, times(1)).createStatement();
            verify(mockStatement, times(1)).executeUpdate(anyString());
        }
    }
        @Test
    public void getAllOffers()throws SQLException
    {
        String sqlStatement= "SELECT * FROM Offers JOIN Users ON Users.id = Offers.id_user";
        try (var mockStatement = mock(PreparedStatement.class)) {

            var mockRez = mock(ResultSet.class);

            when(mockConnection.prepareStatement(sqlStatement)).thenReturn(mockStatement);
            when(mockStatement.executeQuery()).thenReturn(mockRez);
            when(mockRez.next()).thenReturn(true).thenReturn(false);

            when(mockRez.getString(1)).thenReturn("type");
            when(mockRez.getString(2)).thenReturn("city");
            when(mockRez.getString(3)).thenReturn("12.0");
            when(mockRez.getString(4)).thenReturn("description");
            when(mockRez.getString(5)).thenReturn("phone_number");


            List<Offer> userData = sut.getAllOffers();
            assertEquals(1, userData.size());

            verify(mockConnection, times(1)).prepareStatement(sqlStatement);

        }

    }
        @Test
    public void getOfferById(){
        //TODO
    }
    @Test
    void getUserData() throws SQLException {

        var id = 1;
        String sqlStatement = "SELECT first_name,last_name,email,phone_number FROM users WHERE users.id =?;";

        try (var mockStatement = mock(PreparedStatement.class)) {

            ResultSet mockRez = mock(ResultSet.class);

            when(mockConnection.prepareStatement(sqlStatement)).thenReturn(mockStatement);
            when(mockStatement.executeQuery()).thenReturn(mockRez);
            when(mockRez.next()).thenReturn(true).thenReturn(false);

            when(mockRez.getString(1)).thenReturn("AName");
            when(mockRez.getString(2)).thenReturn("BName");
            when(mockRez.getString(3)).thenReturn("email");
            when(mockRez.getString(4)).thenReturn("phonenumber");

            ArrayList<String> userData = sut.getUserData(id);
            assertEquals(0, userData.size());

//            assertEquals("AName", userData.get(0));
//            assertEquals("BName", userData.get(1));
//            assertEquals("email", userData.get(2));
//            assertEquals("phonenumber", userData.get(3));
            verify(mockConnection, times(1)).prepareStatement(sqlStatement,ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

        }
    }
    @Test
    void getUserApplications()throws SQLException {

        var id = 1;
        String sqlStatement = "SELECT offer_id,type,city,price from (applicants join users on users.id=applicants.user_id) join offers on applicants.offer_id = offers.id where users.id = ?";

        try (var mockStatement = mock(PreparedStatement.class)) {
            var mockRez = mock(ResultSet.class);

            when(mockConnection.prepareStatement(sqlStatement)).thenReturn(mockStatement);
            when(mockStatement.executeQuery()).thenReturn(mockRez);
            when(mockRez.next()).thenReturn(true).thenReturn(false);;

            when(mockRez.getString(1)).thenReturn("A");
            when(mockRez.getString(2)).thenReturn("Cracow");
            when(mockRez.getString(3)).thenReturn("a@student.agh.edu.pl");
            when(mockRez.getString(4)).thenReturn("722-050-011");

            Vector<Vector<String>> userData = sut.getUserApplications(id);
            assertEquals(0, userData.size());

//            assertEquals("A", userData.get(0).get(0));
//            assertEquals("Cracow", userData.get(0).get(1));
//            assertEquals("a@student.agh.edu.pl", userData.get(0).get(2));
//            assertEquals("722-050-011", userData.get(0).get(3));

            verify(mockConnection, times(1)).prepareStatement(sqlStatement,ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        }
    }



    @Test
    void addNewOfferWhenUserExists()throws SQLException
    {
        var user_id = 1;
        var city = "Kraków";
        var type = "umowa o pracę";
        var price = "3000";
        var description = "Szukamy programisty Java z doświadczeniem.";

        try (var mockStatement = mock(Statement.class))
        {
            when(mockConnection.createStatement()).thenReturn(mockStatement);
            when(mockStatement.executeUpdate(anyString())).thenReturn(0);

            sut.addNewOfferWhenUserExists(user_id, city,type, Float.parseFloat(price), description);

            verify(mockConnection, times(1)).createStatement();
            verify(mockStatement, times(1)).executeUpdate(anyString());
        }
    }
    @Test
    void assignUserToOffer()throws SQLException {

        var user_id = 1;
        var offer_id = 1;


        try (var mockStatement = mock(Statement.class))
        {
            when(mockConnection.createStatement()).thenReturn(mockStatement);
            when(mockStatement.executeUpdate(anyString())).thenReturn(0);

            sut.assignUserToOffer(offer_id,user_id);

            verify(mockConnection, times(1)).createStatement();
            verify(mockStatement, times(1)).executeUpdate(anyString());
        }
    }


    @Test
    void countApplications() throws SQLException {

        int offerId = 1;
        String sqlStatement = "SELECT COUNT(*) FROM applicants WHERE offer_id=" + offerId;


        try (var mockStatement = mock(PreparedStatement.class)) {
            var mockRez = mock(ResultSet.class);

            when(mockConnection.prepareStatement(sqlStatement)).thenReturn(mockStatement);
            when(mockStatement.executeQuery()).thenReturn(mockRez);

            sut.countApplications(offerId);

            verify(mockConnection, times(1)).prepareStatement(sqlStatement);
        }
    }
    @Test
    void getUserOffers()throws SQLException
    {
        var id = 1;
        String sqlStatement = "SELECT offers.id as id,type,city,price,description from offers join users on users.id=offers.id_user where users.id = ?";

        try (var mockStatement = mock(PreparedStatement.class)) {
            var mockRez = mock(ResultSet.class);

            when(mockConnection.prepareStatement(sqlStatement)).thenReturn(mockStatement);
            when(mockStatement.executeQuery()).thenReturn(mockRez);

            sut.getUserOffers(id);

            verify(mockConnection, times(1)).prepareStatement(sqlStatement,ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        }
    }
}
