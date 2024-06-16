//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package main.Panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import main.Application;
import main.Database.Database;
import main.Database.Models.Offer;

public class OfferPanel extends JPanel {
    private JLabel phoneNumberLabel;
    private JLabel titleLabel;
    private JLabel locationLabel;
    private JLabel rateLabel;
    private JLabel typeLabel;
    private JLabel descriptionLabel;
    private JLabel applicationCounter;
    private JLabel infoLabel;
    private JTextField phoneNumberField;
    private JTextField rateField;
    private JTextField typeField;
    private JTextArea descriptionArea;

    private JButton applyButton;
    JPanel fieldsPanel;

    public OfferPanel(int id, String phoneNumber, double rate, String type, String description) {
        this.applicationCounter = new JLabel("Liczba aplikacji: " + Application.db().countApplications(id));
        this.phoneNumberLabel = new JLabel("Numer telefonu:");
        this.titleLabel = new JLabel("Tytuł oferty:");
        this.locationLabel = new JLabel("Lokalizacja:");
        this.rateLabel = new JLabel("Stawka oferty:");
        this.typeLabel = new JLabel("Typ oferty:");
        this.infoLabel = new JLabel(" Zaaplikowano");
        this.descriptionLabel = new JLabel("Opis oferty:");
        this.applyButton = new JButton("Aplikuj");
        this.phoneNumberField = new JTextField(phoneNumber);
        this.rateField = new JTextField(String.valueOf(rate));
        this.typeField = new JTextField(type);
        this.descriptionArea = new JTextArea(description);
        this.descriptionArea.setEditable(false);
        int textFieldMargin = 10;
        this.phoneNumberField.setBorder(BorderFactory.createCompoundBorder(this.phoneNumberField.getBorder(), BorderFactory.createEmptyBorder(textFieldMargin, textFieldMargin, textFieldMargin, textFieldMargin)));
        this.rateField.setBorder(BorderFactory.createCompoundBorder(this.rateField.getBorder(), BorderFactory.createEmptyBorder(textFieldMargin, textFieldMargin, textFieldMargin, textFieldMargin)));
        this.typeField.setBorder(BorderFactory.createCompoundBorder(this.typeField.getBorder(), BorderFactory.createEmptyBorder(textFieldMargin, textFieldMargin, textFieldMargin, textFieldMargin)));
        this.descriptionArea.setBorder(BorderFactory.createCompoundBorder(this.descriptionArea.getBorder(), BorderFactory.createEmptyBorder(textFieldMargin, textFieldMargin, textFieldMargin, textFieldMargin)));
        this.setLayout(new BorderLayout());
        this.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.fieldsPanel = new JPanel(new GridLayout(7, 2));
        this.fieldsPanel.add(this.phoneNumberLabel);
        this.fieldsPanel.add(this.phoneNumberField);
        this.fieldsPanel.add(this.titleLabel);
        this.fieldsPanel.add(this.locationLabel);
        this.fieldsPanel.add(this.rateLabel);
        this.fieldsPanel.add(this.rateField);
        this.fieldsPanel.add(this.typeLabel);
        this.fieldsPanel.add(this.typeField);
        this.fieldsPanel.add(this.descriptionLabel);
        this.fieldsPanel.add(new JScrollPane(this.descriptionArea));
        this.fieldsPanel.add(this.applicationCounter);
        this.infoLabel.setVisible(false);
        this.fieldsPanel.add(this.infoLabel);
        this.add(this.fieldsPanel, "Center");

        if (Application.db().checkIfUserIsAssignedToOffer(String.valueOf(id), String.valueOf(Application.getInstance().getUserId()))) {
            this.applyButton.setVisible(false);
            this.infoLabel.setVisible(true);
            this.fieldsPanel.repaint();
        }

        this.fieldsPanel.repaint();
        this.applyButton.addActionListener((e) -> {
            if (!Application.db().checkIfUserIsAssignedToOffer(String.valueOf(id), String.valueOf(Application.getInstance().getUserId()))) {
                Application.db().assignUserToOffer(id, Application.getInstance().getUserId());
                JOptionPane.showMessageDialog(this, "Pomyślnie zaaplikowano", "Informacja", 1);
                this.applyButton.setVisible(false);
                this.infoLabel.setVisible(true);
                this.applicationCounter.setText("Liczba aplikacji: " + Application.db().countApplications(id));
                this.fieldsPanel.repaint();
            } else {
                JOptionPane.showMessageDialog(this, "Już zaaplikowałeś na tą ofertę", "Informacja", 1);
            }

        });
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBorder(new EmptyBorder(20, 10, 30, 10));
        bottomPanel.add(this.applyButton);
        this.add(bottomPanel, "South");
        this.setBounds(0, 0, 1000, 630);
        this.setVisible(true);
    }

    public JPanel getPanel() {
        return this.fieldsPanel;
    }

    public OfferPanel(Offer offer) {
        this(offer.id(), offer.phoneNumber(), offer.rate(), offer.type(), offer.description());
    }



    public static void main(String[] args) {
        JFrame frame = new JFrame();
        String phoneNumber = "123-456-789";
        String title = "Oferta pracy";
        String location = "Warszawa";
        double rate = 50.0;
        String type = "Pełny etat";
        String description = "Szukamy programisty Java z doświadczeniem.";
        int id = 1;
        frame.setTitle("DoIt");
        frame.setDefaultCloseOperation(3);
        frame.setLayout((LayoutManager)null);
        frame.setResizable(false);
        frame.setSize(1000, 630);
        frame.setVisible(true);
        frame.setBackground(new Color(255, 240, 206, 255));
        frame.add(new OfferPanel(id, phoneNumber, rate, type, description));
        frame.revalidate();
        frame.repaint();
    }
}
