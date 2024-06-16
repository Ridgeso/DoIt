package Tests.Panels;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.awt.event.ContainerEvent;
import java.awt.event.MouseEvent;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.tree.ExpandVetoException;

import main.Database.Models.User;
import main.Panels.ContentPanel;
import main.Panels.MainPanel;
import main.Panels.UserPanel;
import org.junit.jupiter.api.*;
import org.mockito.*;
import main.Application;
import main.Database.Database;
import main.Database.Models.Offer;
import main.Panels.LoginPanel;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

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
        when(Application.db()).thenReturn(mockDatabase);
        doAnswer(
                new Answer() {
                    @Override
                    public Object answer(InvocationOnMock invocation) throws Throwable {
                        LoginPanel loginPanel = new LoginPanel();
                        return null;
                    }
                }
        ).when(mockApplication).logout();
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

        when(mockDatabase.getAnnoucerData(11)).thenReturn(new User("Adam","Kowalski","adama.kowal@.gmail.com","","","123456789"));

        Vector<Offer> offers = new Vector<>();
        offers.add(new Offer(1, "", 30.0, "Sprzataczka", "Praca dorywcza", "Krakow", "30.0"));
        when(mockDatabase.getUserOffers(1)).thenReturn(offers);

        Vector<User> users = new Vector<>();
        users.add(new User("Adam","Kowalski","adama.kowal@.gmail.com","","","123456789"));
        when(mockDatabase.getApplicantsData(1)).thenReturn(users);

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                when(mockDatabase.getUserOffers(anyInt())).thenReturn(new Vector<Offer>());
                return null;
            }
        }).when(mockDatabase).deleteOffer(1);

        userPanel = new UserPanel();

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
        assertEquals(7, model.getColumnCount());
    }

    @Test
    public void testLogout() {

        JButton ShowMainPanelButton = (JButton) userPanel.getInnerPanel().getComponent(5);
        try(MockedConstruction<LoginPanel> mocked  = mockConstruction(LoginPanel.class)) {

            ShowMainPanelButton.doClick();
            assertEquals(1, mocked.constructed().size());

        }
    }
    @Test
    public void testApplications() {
        JButton application_button = (JButton) userPanel.getInnerPanel().getComponent(9);
        application_button.doClick();
        int expectedComponentCount = 12;
        assertTrue(userPanel.getInnerPanel().getComponent(10) instanceof ContentPanel);
        assertEquals(expectedComponentCount, userPanel.getInnerPanel().getComponentCount());
    }

    @Test
    public void testAddOffer() {
        JButton offer_button = (JButton) userPanel.getInnerPanel().getComponent(7);
        offer_button.doClick();
        int expectedComponentCount = 11;
        assertEquals(expectedComponentCount, userPanel.getInnerPanel().getComponentCount());
    }

    @Test
    public void testShowMainPanel() {

        JButton ShowMainPanelButton = (JButton) userPanel.getInnerPanel().getComponent(8);

        try(MockedConstruction<MainPanel> mocked  = mockConstruction(MainPanel.class)) {
            ShowMainPanelButton.doClick();
            assertEquals(1, mocked.constructed().size());
        }
    }

    @Test
    public void testDeleteOffer() {
        JScrollPane scrollPane = (JScrollPane) userPanel.getInnerPanel().getComponent(6);
        JTable table = (JTable) scrollPane.getViewport().getView();
        assertEquals(1, table.getRowCount());
        int x = table.getWidth()/6*5 + table.getX() +500;

        UserPanel.ButtonMouseListener listener = userPanel.new ButtonMouseListener(table);
        MouseEvent clickEvent = new MouseEvent(table, MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(),
                0, x, 0, 1, false);

        listener.mouseClicked(clickEvent);
        verify(mockDatabase).deleteOffer(anyInt());
        userPanel = new UserPanel();

        scrollPane = (JScrollPane) userPanel.getInnerPanel().getComponent(6);
        table = (JTable) scrollPane.getViewport().getView();

        assertEquals(0, table.getRowCount());
    }

    @Test
    public void  testshowApplicantsDetails() {
        JScrollPane scrollPane = (JScrollPane) userPanel.getInnerPanel().getComponent(6);
        JTable table = (JTable) scrollPane.getViewport().getView();

        int x = table.getWidth()/6*5 + table.getX() +300;
        System.out.println(table.getColumnModel().getColumnIndexAtX(x));
        assertEquals(1, table.getRowCount());
        UserPanel.ButtonMouseListener listener = userPanel.new ButtonMouseListener(table);
        MouseEvent clickEvent = new MouseEvent(table, MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(),
                0, x, 0, 1, false);

        try (MockedConstruction<UserPanel.UsersinfoPanel> mocked = mockConstruction(UserPanel.UsersinfoPanel.class)) {
            try {
                listener.mouseClicked(clickEvent);
            }catch (Exception e)
            {
                System.out.println(e.getMessage());
            }
            verify(mockDatabase).getApplicantsData(anyInt());
            assertEquals(1, mocked.constructed().size());
        }

    }

    @Test
    public void  testshowAnnoucerDetails() {

        JButton application_button = (JButton) userPanel.getInnerPanel().getComponent(9);

        try {
            application_button.doClick();

            JButton annoucerbutton = (JButton) userPanel.getInnerPanel().getComponent(11);
            try (MockedConstruction<UserPanel.UsersinfoPanel> mocked = mockConstruction(UserPanel.UsersinfoPanel.class)) {
                annoucerbutton.doClick();
                verify(mockDatabase).getAnnoucerData(anyInt());
                assertEquals(1, mocked.constructed().size());
            }
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

    }
}
