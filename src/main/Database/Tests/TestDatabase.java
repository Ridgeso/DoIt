package main.Database.Tests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import java.sql.*;
import main.Database.Database;

class TestDatabase
{
    Database sut;
    MockedStatic<DriverManager> mockDriverManager;
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
    void getUserData()
    {
    }

    @Test
    void getUserOffers()
    {
    }

    @Test
    void addNewOfferWhenUserDontExist()
    {
    }

    @Test
    void addNewOfferWhenUserExists()
    {
    }

    @Test
    void addNewAplicantWhenUserDontExist()
    {
    }

    @Test
    void addNewAplicantWhenUserExists()
    {
    }

}