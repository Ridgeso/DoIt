package Tests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.awt.Color;
import java.nio.file.Path;

import javax.swing.*;

import org.junit.jupiter.api.*;
import org.mockito.*;

import main.Application;
import main.Database.Database;
import main.Panels.LoginPanel;
import main.Panels.MainPanel;

public class ApplicationTest {

    @Mock
    private Database mockDatabase;

    private MockedStatic<Application> mockedStaticApplication;
    private Application application;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockedStaticApplication = mockStatic(Application.class);
        when(Application.getDatabase()).thenReturn(mockDatabase);

        application = new Application();
        Application.instance = application;
    }

    @AfterEach
    public void tearDown() {
        mockedStaticApplication.close();
    }



    @Test
    public void testSetPanel() {
        JPanel testPanel = new JPanel();
        application.setPanel(testPanel);

        assertEquals(1, application.getContentPane().getComponentCount());
        assertEquals(testPanel, application.getContentPane().getComponent(0));
    }

    @Test
    public void testGetDatabase() {
        assertNotNull(Application.getDatabase());
    }

    @Test
    public void testGetUserId() {
        assertEquals(Application.INVALID_USER_ID, application.getUserId());

        application.setUserId(10);
        assertEquals(10, application.getUserId());
    }

    @Test
    public void testLogout() {
        application.logout();

        assertEquals(1, application.getContentPane().getComponentCount());
        assertTrue(application.getContentPane().getComponent(0) instanceof LoginPanel);
    }

    @Test
    public void testInit() {
        application.init();

        assertEquals("DoIt", application.getTitle());
        assertEquals(JFrame.EXIT_ON_CLOSE, application.getDefaultCloseOperation());
        assertFalse(application.isResizable());
        assertEquals(1000, application.getWidth());
        assertEquals(630, application.getHeight());
        assertEquals(new Color(255, 240, 206, 255), application.getBackground());

        // Verify that the icon image is set correctly
        final var iconPath = Path.of(Application.assetsDir.toString(), "icon.jpg");
        final var iconImage = new ImageIcon(iconPath.toString());
        assertEquals(iconImage.getImage(), application.getIconImage());
    }
}
