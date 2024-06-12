package Tests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

import main.Panels.MainPanel;
import main.Panels.UserPanel;
import org.junit.jupiter.api.*;
import org.mockito.*;
import main.Application;
import main.Database.Database;
import main.Database.Models.Offer;
import main.Panels.LoginPanel;

public class UserPanelTest {

    @Mock
    private Database mockDatabase;

    @Mock
    private Application mockApplication;

    private UserPanel userPanel;
    private MockedStatic<Application> mockedStaticApplication;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockedStaticApplication = mockStatic(Application.class);
        when(Application.getInstance()).thenReturn(mockApplication);
        when(Application.getInstance().getUserId()).thenReturn(1);

        ArrayList<String> data = new ArrayList<>();
        data.add("Jan");
        data.add("Kowalski");
        data.add("jan@kowal.com");
        data.add("123-456-789");
        when(mockDatabase.getUserData(1)).thenReturn(data);

        Vector<Vector<String>> applications = new Vector<>();
        Vector<String> application = new Vector<>();
        application.add("11");
        application.add("Sprzataczka");
        application.add("Krakow");
        application.add("30.0");
        applications.add(application);
        when(mockDatabase.getUserApplications(1)).thenReturn(applications);

        Vector<Offer> offers = new Vector<>();
        offers.add(new Offer(1, "", 30.0, "Sprzataczka", "Praca dorywcza", "Krakow", "30.0"));
        when(mockDatabase.getUserOffers(1)).thenReturn(offers);
        userPanel.db = mockDatabase;
        userPanel = new UserPanel(mockDatabase);

    }

    @AfterEach
    public void tearDown() {
        mockedStaticApplication.close();
    }

    @Test
    public void testInit() {

        int expectedComponentCount = 10;
        assertEquals(expectedComponentCount, userPanel.getInnerPanel().getComponentCount());

        assertTrue(userPanel.getInnerPanel().getComponent(0) instanceof JLabel);
        assertTrue(userPanel.getInnerPanel().getComponent(1) instanceof JLabel);
        assertTrue(userPanel.getInnerPanel().getComponent(2) instanceof JLabel);
        assertTrue(userPanel.getInnerPanel().getComponent(3) instanceof JLabel);
        assertTrue(userPanel.getInnerPanel().getComponent(4) instanceof JLabel);

        JScrollPane scrollPane = (JScrollPane) userPanel.getInnerPanel().getComponent(6);
        JTable table = (JTable) scrollPane.getViewport().getView();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        assertEquals(1, model.getRowCount());
        assertEquals(5, model.getColumnCount());
    }

    @Test
    public void testLogout() {
        JButton logoutButton = (JButton) userPanel.getInnerPanel().getComponent(5);
        logoutButton.doClick();
        verify(mockApplication, times(1)).logout();
    }

    @Test
    public void test_applications() {
        JButton application_button = (JButton) userPanel.getInnerPanel().getComponent(9);
        application_button.doClick();
        int expectedComponentCount = 12;
        assertEquals(expectedComponentCount, userPanel.getInnerPanel().getComponentCount());
    }

    @Test
    public void test_addof() {
        JButton offer_button = (JButton) userPanel.getInnerPanel().getComponent(7);
        offer_button.doClick();
        int expectedComponentCount = 11;
        assertEquals(expectedComponentCount, userPanel.getInnerPanel().getComponentCount());
    }

    @Test
    public void test_showMainPanel() {
        JButton application_see = (JButton) userPanel.getInnerPanel().getComponent(8);
        application_see.doClick();
        verify(mockApplication, times(1)).setPanel(any(MainPanel.class));
    }
}
