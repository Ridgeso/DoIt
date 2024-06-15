package Tests;

import main.Application;
import main.Database.Database;
import main.Panels.LoginPanel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;

import javax.swing.*;

import static org.mockito.Mockito.*;

public class LoginPanelTests {

    @Mock
    private Database mockDatabase;

    @Mock
    private Application mockApplication;

    private MockedStatic<Application> mockedStaticApplication;

    @InjectMocks
    private LoginPanel sut;

    @BeforeEach
    public void setUp() {
        try (var __ = mockConstruction(Database.class)) {
            MockitoAnnotations.openMocks(this);
        }
        mockedStaticApplication = mockStatic(Application.class);
        when(Application.getInstance()).thenReturn(mockApplication);
        when(Application.getDatabase()).thenReturn(mockDatabase);

        sut = new LoginPanel();
    }

    @AfterEach
    public void tearDown() {
        mockedStaticApplication.close();
    }

    @Test
    void testEmptyUsernameAndPasswordShowsError() throws NoSuchFieldException, IllegalAccessException{
        // Simulate empty fields
        Answer<Boolean> answer = (InvocationOnMock /*unused*/) -> true;
        MockedStatic<JOptionPane> jOptionPane = mockStatic(JOptionPane.class);

        JTextField usernameField = (JTextField)sut.getComponent(1);
        usernameField.setText("");
        JTextField passwordField = (JTextField)sut.getComponent(3);
        passwordField.setText("");

        // Trigger the login button action
        JButton loginButton = (JButton)sut.getComponent(4);
        loginButton.doClick();

        // Verify the error message dialog
        jOptionPane.verify(
                () -> JOptionPane.showMessageDialog(any(), eq("Wpisz wszystkie dane"), anyString(), eq(JOptionPane.ERROR_MESSAGE)),
                times(1));
    }

}