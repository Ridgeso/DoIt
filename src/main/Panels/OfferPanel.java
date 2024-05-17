package main.Panels;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import main.Database.Models.Offer;

import java.awt.*;

public class OfferPanel extends JPanel {
    private JLabel phoneNumberLabel, titleLabel, locationLabel, rateLabel, typeLabel, descriptionLabel;
    private JTextField phoneNumberField/* , titleField, locationField,*/, rateField, typeField;
    private JTextArea descriptionArea;

    public OfferPanel(String phoneNumber, /*String title, String location,*/ double rate, String type, String description) {


        phoneNumberLabel = new JLabel("Numer telefonu:");
        titleLabel = new JLabel("Tytuł oferty:");
        locationLabel = new JLabel("Lokalizacja:");
        rateLabel = new JLabel("Stawka oferty:");
        typeLabel = new JLabel("Typ oferty:");
        descriptionLabel = new JLabel("Opis oferty:");

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

        setLayout(new GridLayout(6, 2));
        setBorder(new EmptyBorder(10, 10, 10, 10)); // Marginesy dla panelu
        add(phoneNumberLabel);
        add(phoneNumberField);
        add(titleLabel);
        // add(titleField);
        add(locationLabel);
        // add(locationField);
        add(rateLabel);
        add(rateField);
        add(typeLabel);
        add(typeField);
        add(descriptionLabel);
        add(new JScrollPane(descriptionArea));

        setBounds(0,0,1000,630);
        setVisible(true);
    }

    public OfferPanel(Offer offer) {
        this(offer.phoneNumber(), offer.rate(),
             offer.type(), offer.description());
    }

    public static void main(String[] args) {
        var frame = new JFrame();
        String phoneNumber = "123-456-789";
        String title = "Oferta pracy";
        String location = "Warszawa";
        double rate = 50.0;
        String type = "Pełny etat";
        String description = "Szukamy programisty Java z doświadczeniem.";

        frame.setTitle("DoIt");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setResizable(false);
        frame.setSize(1000, 630);
        frame.setVisible(true);
        frame.setBackground(new Color(255, 240, 206, 255));

        frame.add(new OfferPanel(phoneNumber, rate, type, description));
        frame.revalidate();
        frame.repaint();

    }
}
