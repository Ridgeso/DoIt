package Tests;

import main.Application;
import main.Database.Database;
import main.Panels.RegistrationPanel;
import main.Panels.UserPanel;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;

import javax.swing.*;

import java.awt.*;
import java.sql.SQLException;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class TestRegistrationPanel {

    @Mock
    private Database mockDatabase;

    @Mock
    private Application mockApplication;

    private MockedStatic<Application> mockedStaticApplication;

    @InjectMocks
    private RegistrationPanel sut;

    @BeforeEach
    public void setUp() {
        try (var __ = mockConstruction(Database.class)) {
            MockitoAnnotations.openMocks(this);
        }
        mockedStaticApplication = mockStatic(Application.class);
        when(Application.getInstance()).thenReturn(mockApplication);
        when(Application.getDatabase()).thenReturn(mockDatabase);

        sut = new RegistrationPanel();
    }

    @AfterEach
    public void tearDown() {
        mockedStaticApplication.close();
    }

    @Test
    public void testInitializeComponents() {
        assertEquals(16, sut.getComponentCount());

        GridLayout scrollPane = (GridLayout)sut.getLayout();
        assertEquals(2, scrollPane.getColumns());
        assertEquals(8, scrollPane.getRows());
    }

    @Test
    public void testIsUserValid() {
        Answer<Boolean> answer = (InvocationOnMock /*unused*/) -> true;
        MockedStatic<JOptionPane> jOptionPane = mockStatic(JOptionPane.class);

        try {
            when(mockDatabase.checkUserLogin(anyString(), anyString())).thenReturn(0);
        } catch (SQLException er) {}
        doNothing().when(mockDatabase).addNewUser(anyString(), anyString(), anyString(), anyString(), anyString(), anyString());
        jOptionPane.when(
            () -> JOptionPane.showMessageDialog(any(), anyString(), anyString(), eq(JOptionPane.ERROR_MESSAGE)))
            .then(answer);

        JButton registerButton = (JButton)sut.getComponent(15);
        registerButton.doClick();

        clickOnRegisterButtonWithOneMoreField(1, "login");
        clickOnRegisterButtonWithOneMoreField(3, "email");
        clickOnRegisterButtonWithOneMoreField(5, "pass");
        clickOnRegisterButtonWithOneMoreField(7, "pass");
        clickOnRegisterButtonWithOneMoreField(9, "fname");
        clickOnRegisterButtonWithOneMoreField(11, "lname");
        try (var __ = mockConstruction(UserPanel.class)) {
            clickOnRegisterButtonWithOneMoreField(13, "555555555");
        }

        jOptionPane.verify(
            () -> JOptionPane.showMessageDialog(any(), anyString(), anyString(), eq(JOptionPane.ERROR_MESSAGE)),
            times(7));
    }

    void clickOnRegisterButtonWithOneMoreField(int field, String value)
    {
        JTextField phoneNumberField = (JTextField)sut.getComponent(field);
        phoneNumberField.setText(value);
        ((JButton)sut.getComponent(15)).doClick();
    }

}
