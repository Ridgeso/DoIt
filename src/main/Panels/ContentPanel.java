package main.Panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ContentPanel extends JPanel {
    private JLabel offerNameLabel;
    private JLabel localisationLabel;
    private JLabel priceLabel;
    private JButton downloadButton;
    private JButton viewButton;

    public ContentPanel(String offerName, String localisation, String price) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEtchedBorder());

        offerNameLabel = new JLabel("Offer Name: " + offerName);
        localisationLabel = new JLabel("Localisation: " + localisation);
        priceLabel = new JLabel("Price: " + price +"zł/h");

        downloadButton = new JButton("Download");
        downloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: implementacja działania
                System.out.println("Downloading offer: " + offerName);
            }
        });

        viewButton = new JButton("View Offer");
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: implementacja działania
                System.out.println("Viewing offer: " + offerName);
            }
        });

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        buttonPanel.setMaximumSize(new Dimension(1000, 1));
        buttonPanel.add(downloadButton);
        buttonPanel.add(viewButton);

        JPanel infoPanel = new JPanel(new GridLayout(3, 1));
        infoPanel.add(offerNameLabel);
        infoPanel.add(localisationLabel);
        infoPanel.add(priceLabel);

        add(infoPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Jobs");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JPanel main = new JPanel(new GridLayout(0, 1));
        JScrollPane scroll = new JScrollPane(main,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setPreferredSize(new Dimension(1000, 630));
        for(int i=0; i<10; i++) {
            ContentPanel empty = new ContentPanel("Offer " + i, "Location " + i, i+"00.0");
            main.add(empty);
        }
        frame.add(scroll);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }
}
