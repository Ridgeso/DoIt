package Tests.Panels;

import main.Application;
import main.Database.Database;
import main.Database.Models.Offer;
import main.Panels.ContentPanel;
import main.Panels.LoginPanel;
import main.Panels.OfferPanel;
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

public class TestContentPanel {
    @Mock
    private Database mockDatabase;

    @Mock
    private Application mockApplication;

    private MockedStatic<Application> mockedStaticApplication;

    private ContentPanel sut;

    int id = 12;
    String offerName = "Oferta";
    String localisation = "Lokacja";
    String price = "120";

    @BeforeEach
    public void setUp() {
        try (var __ = mockConstruction(Database.class)) {
            MockitoAnnotations.openMocks(this);
        }
        mockedStaticApplication = mockStatic(Application.class);
        when(Application.getInstance()).thenReturn(mockApplication);
        when(Application.db()).thenReturn(mockDatabase);

        sut = new ContentPanel(id, offerName, localisation, price);
    }

    @Test
    void testViewButton() {
        var viewButton = (JButton)((JPanel)sut.getComponent(1)).getComponent(0);
        var newOffer = new Offer(12, "555555555", 2.5, "t", "opis", localisation, price);
        when(mockDatabase.getOfferById(id)).thenReturn(newOffer);

        OfferPanel mockOfferPanel = mock(OfferPanel.class);
        when(mockOfferPanel.getPanel()).thenReturn(new JPanel());
        try (var __ = mockConstruction(OfferPanel.class, (mock, context) -> {
            when(mock.getPanel()).thenReturn(new JPanel());
        })) {
            viewButton.doClick();
        }
    }
}
