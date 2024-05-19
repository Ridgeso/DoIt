package main.Database.Tests;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import main.Database.Database;
import org.mockito.Mockito;

class TestDatabase
{
    Database sut;
    static MockedStatic<DriverManager> mockDriverManager;
    Connection mockConnection;

    void expectGetConnection()
        throws SQLException
    {
        mockDriverManager = mockStatic(DriverManager.class);
        mockConnection = mock(Connection.class);
        mockDriverManager.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString())).thenReturn(mockConnection);
    }

    void verifyGetConnection()
    {
        mockDriverManager.verify(() -> DriverManager.getConnection(anyString(), anyString(), anyString()));
    }

    @BeforeEach
    void setUp()
        throws SQLException
    {
        sut = new Database();
        expectGetConnection();
    }

    @AfterEach
    void finish()
    {
        verifyGetConnection();
        mockDriverManager.close();
    }

    @Test
    void testConnect()
    {
        sut.connect();
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
    void getUserData() throws SQLException {
        var id = 1;
        String sqlStatement = "SELECT first_name,last_name,email,phone_number FROM Users WHERE Users.id = ?";

        try (var mockStatement = mock(PreparedStatement.class)) {
            var mockRez = mock(ResultSet.class);

            when(mockConnection.prepareStatement(sqlStatement)).thenReturn(mockStatement);
            when(mockStatement.executeQuery()).thenReturn(mockRez);

            sut.getUserData(id);

            verify(mockConnection, times(1)).prepareStatement(sqlStatement,ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        }
    }

    @Test
    void getUserOffers()throws SQLException
    {
        var id = 1;
        String sqlStatement = "SELECT type,city,price,description from Offers join Users on Users.id=Offers.id where Users.id = ?";

        try (var mockStatement = mock(PreparedStatement.class)) {
            var mockRez = mock(ResultSet.class);

            when(mockConnection.prepareStatement(sqlStatement)).thenReturn(mockStatement);
            when(mockStatement.executeQuery()).thenReturn(mockRez);

            sut.getUserOffers(id);

            verify(mockConnection, times(1)).prepareStatement(sqlStatement,ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        }
    }
    @Test
    void getUserApplications()throws SQLException {
        var id = 1;
        String sqlStatement = "SELECT type,city,price from (Applications join Users on Users.id=Application.id_user) join Offers on Application.id_offer = Offers.id where Users.id = ?";

        try (var mockStatement = mock(PreparedStatement.class)) {
            var mockRez = mock(ResultSet.class);

            when(mockConnection.prepareStatement(sqlStatement)).thenReturn(mockStatement);
            when(mockStatement.executeQuery()).thenReturn(mockRez);

            sut.getUserApplications(id);

            verify(mockConnection, times(1)).prepareStatement(sqlStatement, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        }
    }

//    @Test
//    void addNewOfferWhenUserDontExist()
//    {
//    }

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
//
//    @Test
//    void addNewAplicantWhenUserDontExist()
//    {
//
//    }
//
//    @Test
//    void addNewAplicantWhenUserExists()
//    {
//
//    }

//    @Test
//    void assignUserToOffer()throws SQLException {
//        var user_id = 1;
//        var offer_id = 1;
//
//
//        try (var mockStatement = mock(Statement.class))
//        {
//            when(mockConnection.createStatement()).thenReturn(mockStatement);
//            when(mockStatement.executeUpdate(anyString())).thenReturn(0);
//
//            sut.AssignUserToOffer(offer_id,user_id);
//
//            verify(mockConnection, times(1)).createStatement();
//            verify(mockStatement, times(1)).executeUpdate(anyString());
//        }
//    }


//    @Test
//    void countApplications() throws SQLException{
//        int offer_id;
//        //TODO
//    }
}