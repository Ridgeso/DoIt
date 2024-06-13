package main.Panels;

import javax.swing.*;

import main.Application;
import main.Database.Database;

import javax.swing.border.EmptyBorder;
import javax.xml.crypto.Data;

import main.Database.Models.Offer;
import java.awt.*;

import static main.Application.getInstance;

public class OfferPanel extends JPanel {
    private JLabel phoneNumberLabel, titleLabel, locationLabel, rateLabel, typeLabel, descriptionLabel, applicationCounter;
    private JTextField phoneNumberField/* , titleField, locationField,*/, rateField, typeField;
    private JTextArea descriptionArea;
    private JButton backButton, applyButton;
    JPanel fieldsPanel;
    private static Database db = null;
    public OfferPanel(int id, String phoneNumber, /*String title, String location,*/ double rate, String type, String description) {
        db = Application.getDatabase();
        applicationCounter = new JLabel("Liczba aplikacji: "+ db.countApplications(id)); //TODO dodać do tego countApplications(_database.offer_id)
        phoneNumberLabel = new JLabel("Numer telefonu:");
        titleLabel = new JLabel("Tytuł oferty:");
        locationLabel = new JLabel("Lokalizacja:");
        rateLabel = new JLabel("Stawka oferty:");
        typeLabel = new JLabel("Typ oferty:");
        descriptionLabel = new JLabel("Opis oferty:");
        backButton = new JButton("Powrót");
        applyButton = new JButton("Aplikuj");
        phoneNumberField = new JTextField(phoneNumber);
        // titleField = new JTextField(title);
        // locationField = new JTextField(location);
        rateField = new JTextField(String.valueOf(rate));
        typeField = new JTextField(type);
        descriptionArea = new JTextArea(description);
        descriptionArea.setEditable(false);

        // Ustawienie marginesów dla pól tekstowych i obszaru tekstowego
        int textFieldMargin = 10;
        phoneNumberField.setBorder(BorderFactory.createCompoundBorder(
                phoneNumberField.getBorder(),
                BorderFactory.createEmptyBorder(textFieldMargin, textFieldMargin, textFieldMargin, textFieldMargin)
        ));
        // titleField.setBorder(BorderFactory.createCompoundBorder(
        //         titleField.getBorder(),
        //         BorderFactory.createEmptyBorder(textFieldMargin, textFieldMargin, textFieldMargin, textFieldMargin)
        // ));
        // locationField.setBorder(BorderFactory.createCompoundBorder(
        //         locationField.getBorder(),
        //         BorderFactory.createEmptyBorder(textFieldMargin, textFieldMargin, textFieldMargin, textFieldMargin)
        // ));
        rateField.setBorder(BorderFactory.createCompoundBorder(
                rateField.getBorder(),
                BorderFactory.createEmptyBorder(textFieldMargin, textFieldMargin, textFieldMargin, textFieldMargin)
        ));
        typeField.setBorder(BorderFactory.createCompoundBorder(
                typeField.getBorder(),
                BorderFactory.createEmptyBorder(textFieldMargin, textFieldMargin, textFieldMargin, textFieldMargin)
        ));
        descriptionArea.setBorder(BorderFactory.createCompoundBorder(
                descriptionArea.getBorder(),
                BorderFactory.createEmptyBorder(textFieldMargin, textFieldMargin, textFieldMargin, textFieldMargin)
        ));

        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10)); // Marginesy dla panelu

        fieldsPanel = new JPanel(new GridLayout(6, 2));
        fieldsPanel.add(phoneNumberLabel);
        fieldsPanel.add(phoneNumberField);
        fieldsPanel.add(titleLabel);
        // fieldsPanel.add(titleField);
        fieldsPanel.add(locationLabel);
        // fieldsPanel.add(locationField);
        fieldsPanel.add(rateLabel);
        fieldsPanel.add(rateField);
        fieldsPanel.add(typeLabel);
        fieldsPanel.add(typeField);
        fieldsPanel.add(descriptionLabel);
        fieldsPanel.add(new JScrollPane(descriptionArea));
        fieldsPanel.add(applicationCounter);

        add(fieldsPanel, BorderLayout.CENTER);

        backButton.addActionListener(e -> {
            changePanel(new MainPanel());
        });

        applyButton.addActionListener(e -> {
             db.assignUserToOffer(id,getInstance().getUserId());
        });
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(backButton, BorderLayout.WEST);
        add(topPanel, BorderLayout.NORTH);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBorder(new EmptyBorder(20, 10, 30, 10)); // Add some padding
        bottomPanel.add(applyButton);
        add(bottomPanel, BorderLayout.SOUTH);

        setBounds(0, 0, 1000, 630);
        setVisible(true);
    }

    public JPanel getPanel()
    {
        return fieldsPanel;
    }
    public OfferPanel(Offer offer, Database db) {
        this(offer.id(),offer.phoneNumber(), offer.rate(),
                offer.type(), offer.description());
    }



    public void changePanel(JPanel panel) {
        removeAll();
        add(panel);
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        var frame = new JFrame();
        String phoneNumber = "123-456-789";
        String title = "Oferta pracy";
        String location = "Warszawa";
        double rate = 50.0;
        String type = "Pełny etat";
        String description = "Szukamy programisty Java z doświadczeniem.";
        int id = 1;
        frame.setTitle("DoIt");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setResizable(false);
        frame.setSize(1000, 630);
        frame.setVisible(true);
        frame.setBackground(new Color(255, 240, 206, 255));

        frame.add(new OfferPanel(id,phoneNumber, rate, type, description));
        frame.revalidate();
        frame.repaint();
    }
}
