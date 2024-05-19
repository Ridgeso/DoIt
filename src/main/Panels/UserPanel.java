package main.Panels;

import main.Application;
import main.Database.Database;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

public class UserPanel extends JPanel {

    static Database db;
    private ArrayList<String> data;
    private JPanel offerAddPanel;
    private Vector<Vector<String>> offers;
    private Vector<Vector<String>> applications;
    private ImageIcon image;
    private JLabel label;
    private Vector<String> header;
    private GridBagConstraints constraints;
    private JButton buttonAdd;
    private JButton buttonAddOffer;
    private JButton showMainPanelButton;

    public UserPanel() {
        db = new Database();
        init();
    }

    public void init() {
        final var iconPath = Path.of(Application.assetsDir.toString(), "user.jpg");

        image = new ImageIcon(iconPath.toString());
        label = new JLabel(image);
        constraints = new GridBagConstraints();
        header = new Vector<>();

        data = db.getUserData(Application.getInstance().getUser());
        applications = db.getUserApplications(Application.getInstance().getUser());
        offers = db.getUserOffers(Application.getInstance().getUser());
        label.setBorder(BorderFactory.createBevelBorder(1));

        setLayout(new GridBagLayout());
        String[] titles = {"imie: ", "nazwisko: ", "email: ", "numer telefonu: "};
        ArrayList<JLabel> dataLabels = new ArrayList<>();

        for (int i = 0; i < titles.length; i++) {
            dataLabels.add(new JLabel(titles[i] + data.get(i)));//+data.get(i)
        }

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 1;
        add(label, constraints);
        constraints.gridx = 0;

        constraints.gridy = 1;
        for (int i = 0; i < titles.length; i++) {
            ++constraints.gridy;
            add(dataLabels.get(i), constraints);
        }
        header.add("typ");
        header.add("miasto");
        header.add("cena");
        header.add("opis");

        JTable table = new JTable(offers, header);
        table.getColumnModel().getColumn(3).setPreferredWidth(200);
        JScrollPane pane = new JScrollPane(table);
        pane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                "Zamieszczone ogłoszenia",
                TitledBorder.CENTER,
                TitledBorder.TOP));
        pane.setPreferredSize(new Dimension(600, 200));
        ++constraints.gridy;
        constraints.insets = new Insets(5, 5, 5, 5);
        add(pane, constraints);
        ++constraints.gridy;

        JLabel title = new JLabel("Oferty na które aplikowałeś:", SwingConstants.CENTER);
        if (!applications.isEmpty())
            add(title, constraints);

        for (int j = 0; j < applications.size(); j++) {
            ++constraints.gridy;
            ContentPanel empty = new ContentPanel(j, "Offer " + applications.get(j).get(0),
                    "Location " + applications.get(j).get(1), applications.get(j).get(2));
            add(empty, constraints);
        }

        buttonAdd = new JButton("Dodaj nowe ogłoszenie");
        ++constraints.gridy;
        buttonAdd.addActionListener(e -> showOfferForm());
        add(buttonAdd, constraints);

        showMainPanelButton = new JButton("Pokaż panel główny");
        showMainPanelButton.addActionListener(e -> showMainPanel());
        ++constraints.gridy;
        add(showMainPanelButton, constraints);

        setBackground(new Color(255, 240, 206, 255));
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                "Panel użytkownika",
                TitledBorder.CENTER,
                TitledBorder.TOP));
        setVisible(true);
    }

    private void AddOffer(String city, String type, float price, String description) {
        remove(offerAddPanel);
        remove(buttonAddOffer);
        revalidate();
        repaint();

        JLabel infoLabel = new JLabel("Dodano ofertę", SwingConstants.CENTER);
        infoLabel.setText("Dodano ofertę");
        try {
            db.addNewOfferWhenUserExists(Application.getInstance().getUser(), city, type, price, description);
        } catch (SQLException exception) {
            infoLabel.setText("Błąd");
        }
        ++constraints.gridy;
        add(infoLabel, constraints);

        Timer timer = new Timer(4000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                infoLabel.setText("");
                add(buttonAdd, constraints);
                ((Timer) e.getSource()).stop();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void showOfferForm() {
        remove(buttonAdd);
        revalidate();

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

        add(offerAddPanel, constraints);
        offerAddPanel.setVisible(true);
        ++constraints.gridy;
        add(buttonAddOffer, constraints);
    }

    private void showMainPanel() {
        Application.getInstance().setPanel(new MainPanel());
    }
}
