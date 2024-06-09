import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

import org.junit.jupiter.api.*;
import org.mockito.*;

import main.Application;
import main.Database.Database;
import main.Database.Models.Offer;
import main.Panels.LoginPanel;
import main.Panels.MainPanel;
import main.Panels.UserPanel;

public class MainPanelTest {

    @Mock
    private Database mockDatabase;

    @Mock
    private Application mockApplication;

    private MainPanel mainPanel;
    private MockedStatic<Application> mockedStaticApplication;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockedStaticApplication = mockStatic(Application.class);
        when(Application.getInstance()).thenReturn(mockApplication);
        java.util.List<Offer> offers = Arrays.asList(
            new Offer(1, "123456789", 100, "Type1", "Description1", "City1", "100"),
            new Offer(2, "987654321", 200, "Type2", "Description2", "City2", "200")
        );
        when(mockDatabase.getAllOffers()).thenReturn(offers);
        mainPanel = new MainPanel(mockDatabase);
    }

    @AfterEach
    public void tearDown() {
        mockedStaticApplication.close();
    }

    @Test
    public void testInit() {
        mainPanel.init();

        int expectedComponentCount = 6; 
        assertEquals(expectedComponentCount, mainPanel.getComponentCount());

        assertTrue(mainPanel.getComponent(0) instanceof JButton);
        assertTrue(mainPanel.getComponent(1) instanceof JButton);

        JScrollPane scrollPane = (JScrollPane) mainPanel.getComponent(2);
        JTable table = (JTable) scrollPane.getViewport().getView();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        assertEquals(2, model.getRowCount());
        assertEquals(4, model.getColumnCount());
    }

    @Test
    public void testLogout() {
        JButton logoutButton = (JButton) mainPanel.getComponent(1);
        logoutButton.doClick();
        verify(mockApplication, times(1)).setPanel(any(LoginPanel.class));
    }
}
