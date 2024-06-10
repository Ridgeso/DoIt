package main.Panels;
import main.Application;
import main.Database.Database;
import main.Database.Models.Offer;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Vector;

public class UserPanel extends JPanel {

    public static Database db;
    private ArrayList<String> data;
    private JPanel offerAddPanel;
    private Vector<Offer> offers;
    private Vector<Vector<String>> applications;
    private ImageIcon image;
    private JLabel label;
    private Vector<String> header;
    private GridBagConstraints constraints;
    private JButton buttonAdd;
    private JButton buttonAddOffer;
    private JButton showMainPanelButton;
    private JPanel innerPanel;
    private JButton see_applications;
    private Boolean see_application;
    private LinkedList<ContentPanel> contentPanels;
    public UserPanel() {
        if(db==null)
            db = new Database();
        init();
    }

    public void initComponents()
    {
        final var iconPath = Path.of(Application.assetsDir.toString(), "user.jpg");
        image = new ImageIcon(iconPath.toString());
        label = new JLabel(image);
        constraints = new GridBagConstraints();
        header = new Vector<>();
        contentPanels = new LinkedList<>();

        data = db.getUserData(Application.getInstance().getUserId());
        applications = db.getUserApplications(Application.getInstance().getUserId());
        offers = db.getUserOffers(Application.getInstance().getUserId());
        label.setBorder(BorderFactory.createBevelBorder(1));
        see_application=Boolean.FALSE;

        for (int j = 0; j < applications.size(); j++) {
            ++constraints.gridy;
            ContentPanel empty = new ContentPanel(Integer.parseInt(applications.get(j).get(0)),  applications.get(j).get(1),
                    applications.get(j).get(2), applications.get(j).get(3));
            contentPanels.add(empty);
        }

    }
    public JPanel getInnerPanel() {
        return innerPanel;
    }
    public void init() {

        initComponents();
        innerPanel = new JPanel(new GridBagLayout());
        setLayout(new GridBagLayout());
        String[] titles = {"imie: ", "nazwisko: ", "email: ", "numer telefonu: "};
        ArrayList<JLabel> dataLabels = new ArrayList<>();

        for (int i = 0; i < titles.length; i++) {
            dataLabels.add(new JLabel(titles[i] + data.get(i)));
        }

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 1;
        innerPanel.add(label, constraints);
        constraints.gridx = 0;
        constraints.gridy = 1;

        for (int i = 0; i < titles.length; i++) {
            ++constraints.gridy;
            innerPanel.add(dataLabels.get(i), constraints);
        }

        header.add("typ");
        header.add("miasto");
        header.add("cena");
        header.add("opis");

        Vector<Vector<Object>> data = new Vector<>();
        for (Offer offer : offers) {
            Vector<Object> rowData = new Vector<>();
            rowData.add(offer.type());
            rowData.add(offer.city());
            rowData.add(offer.price());
            rowData.add(offer.description());
            data.add(rowData);
        }

        JTable table = new JTable(data, header);
        table.getColumnModel().getColumn(3).setPreferredWidth(200);
        JScrollPane pane = new JScrollPane(table);
        pane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                "Zamieszczone ogłoszenia",
                TitledBorder.CENTER,
                TitledBorder.TOP));
        pane.setPreferredSize(new Dimension(600, 200));

        ++constraints.gridy;
        constraints.insets = new Insets(5, 5, 5, 5);
        innerPanel.add(pane, constraints);
        ++constraints.gridy;

        buttonAdd = new JButton("Dodaj nowe ogłoszenie");
        buttonAdd.setName("offer");
        ++constraints.gridy;
        buttonAdd.addActionListener(e -> showOfferForm());
        innerPanel.add(buttonAdd, constraints);

        showMainPanelButton = new JButton("Pokaż panel główny");
        showMainPanelButton.addActionListener(e -> showMainPanel());
        ++constraints.gridy;
        innerPanel.add(showMainPanelButton, constraints);
        see_applications = new JButton("Zobacz Twoje aplikacje");
        see_applications.addActionListener(e -> see_applications());
        ++constraints.gridy;

        innerPanel.add(see_applications, constraints);
        innerPanel.setBackground(new Color(255, 240, 206, 255));
        innerPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                "Panel użytkownika",
                TitledBorder.CENTER,
                TitledBorder.TOP));

        JButton logoutButton = new JButton("Wyloguj");
        logoutButton.addActionListener(e -> logout());
        constraints.gridx = 1;
        constraints.anchor = GridBagConstraints.EAST;
        innerPanel.add(logoutButton, constraints);
        constraints.gridx = 0;

        JScrollPane scrollPane = new JScrollPane(innerPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        setVisible(true);
    }
    private void logout() {
        Application.getInstance().logout();
    }

    private void see_applications()
    {
        see_application = Boolean.TRUE;


        for (int j = 0; j < applications.size(); j++) {
            ++constraints.gridy;
            innerPanel.add(contentPanels.get(j), constraints);
        }

        innerPanel.revalidate();
        innerPanel.repaint();
    }

    private void AddOffer(String city, String type, float price, String description) {

        innerPanel.remove(offerAddPanel);
        innerPanel.remove(buttonAddOffer);
        innerPanel.revalidate();
        innerPanel.repaint();

        JLabel infoLabel = new JLabel("Dodano ofertę", SwingConstants.CENTER);
        infoLabel.setText("Dodano ofertę");

        try {
            db.addNewOfferWhenUserExists(Application.getInstance().getUserId(), city, type, price, description);
        } catch (SQLException exception) {
            infoLabel.setText("Błąd");
        }
        ++constraints.gridy;
        innerPanel.add(infoLabel, constraints);

        Timer timer = new Timer(4000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                infoLabel.setText("");
                innerPanel.add(buttonAdd, constraints);
                ((Timer) e.getSource()).stop();
            }
        });
        timer.setRepeats(false);
        timer.start();

        ++constraints.gridy;
        innerPanel.add(showMainPanelButton,constraints);

    }

    private void showOfferForm() {

        innerPanel.remove(buttonAdd);
        innerPanel.remove(showMainPanelButton);
        innerPanel.remove(see_applications);
        innerPanel.revalidate();
        offerAddPanel = new JPanel();
        offerAddPanel.setVisible(false);
        offerAddPanel.setLayout(new GridLayout(4, 2));
        offerAddPanel.setPreferredSize(new Dimension(600, 150));

        JLabel labelType = new JLabel("Typ zgłoszenia: ");
        JLabel labelCity = new JLabel("miasto: ");
        JLabel labelPrize = new JLabel("cena: ");
        JLabel labelDescription = new JLabel("opis: ");

        JTextField textType = new JTextField(32);
        JTextField textCity = new JTextField(28);
        JTextField textPrize = new JTextField(8);
        JTextField textDescription = new JTextField(300);

        buttonAddOffer = new JButton("Dodaj");
        buttonAddOffer.addActionListener(e ->
        {
            try {
                AddOffer(textType.getText(),
                        textCity.getText(), Float.parseFloat(textPrize.getText()), textDescription.getText());
            } catch (NumberFormatException exception) {
                System.out.println("Złe parametry");
            }

        });

        offerAddPanel.add(labelType);
        offerAddPanel.add(textType);
        offerAddPanel.add(labelCity);
        offerAddPanel.add(textCity);
        offerAddPanel.add(labelPrize);
        offerAddPanel.add(textPrize);
        offerAddPanel.add(labelDescription);
        offerAddPanel.add(textDescription);

        offerAddPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Formularz zgłoszenia"));
        offerAddPanel.updateUI();

        if(see_application)
        {
            for(var a:contentPanels)
            {
                innerPanel.remove(a);
            }
        }

        innerPanel.add(offerAddPanel, constraints);
        offerAddPanel.setVisible(true);
        ++constraints.gridy;
        innerPanel.add(buttonAddOffer, constraints);
        ++constraints.gridy;
        innerPanel.add(showMainPanelButton, constraints);
        ++constraints.gridy;
        innerPanel.add(see_applications, constraints);

    }

    private void showMainPanel() {
        Application.getInstance().setPanel(new MainPanel(db));
    }
}
