package main.Panels;

import main.Application;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ContentPanel extends JPanel {
    private JPanel buttonPanel, infoPanel;
    private JLabel offerNameLabel;
    private JLabel localisationLabel;
    private JLabel priceLabel;
    private JButton viewButton;
    private GridBagConstraints constraints = new GridBagConstraints();

    public ContentPanel(int id, String offerName, String localisation, String price) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEtchedBorder());

        offerNameLabel = new JLabel("Oferta: " + offerName);
        localisationLabel = new JLabel("Lokalizacja: " + localisation);
        priceLabel = new JLabel("Cena: " + price +"zł/h");


        viewButton = new JButton("Zobacz szczegóły");
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Viewing offer: " + offerName);
//                Implementacja z łączeniem do bazy danych, będzie działać gdy zacznie działac wyciąganie oferty po ID z bazy danych
//
                  viewOfferPanel(new OfferPanel(Application.db().getOfferById(id)).getPanel());
            }
        });

        constraints.gridheight = GridBagConstraints.REMAINDER;
        buttonPanel = new JPanel(new GridLayout(1, 0));
        buttonPanel.setPreferredSize(new Dimension(1, 20));
        buttonPanel.add(viewButton, constraints);

        infoPanel = new JPanel(new GridLayout(3, 1));
        infoPanel.add(offerNameLabel, constraints);
        infoPanel.add(localisationLabel, constraints);
        infoPanel.add(priceLabel, constraints);

        add(infoPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
    }

    public void viewOfferPanel(JPanel panel) {

        buttonPanel.setPreferredSize(new Dimension(1, 20));
        removeAll();
        add(panel);
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Jobs");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JPanel main = new JPanel(new GridLayout(10, 1));
        JScrollPane scroll = new JScrollPane(main,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setPreferredSize(new Dimension(1000, 630));
        for(int i = 0; i < 10; i++) {
            ContentPanel empty = new ContentPanel(i, "Offer " + i, "Location " + i, i+"00.0");
            main.add(empty);
        }
        frame.add(scroll);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }
}
