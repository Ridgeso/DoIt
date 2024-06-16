package main.Panels;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import main.Application;
import main.Database.Database;
import main.Database.Models.Offer;

import java.awt.*;
import java.util.List;
import java.util.Vector;

import javax.swing.table.TableCellRenderer;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainPanel extends JPanel {

    private GridBagConstraints constraints;
    private JTable table;
    private JFrame offerDetailsFrame;

    public MainPanel() {
        init();
    }

    public void init() {
        setLayout(new GridBagLayout());
        constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);

        JButton profileButton = new JButton("Profil");
        profileButton.addActionListener(e -> showUserProfile());
        constraints.gridx = 0;
        constraints.gridy = 0;
        add(profileButton, constraints);

        JButton logoutButton = new JButton("Wyloguj");
        logoutButton.addActionListener(e -> logout());
        constraints.gridx = 1;
        add(logoutButton, constraints);

        List<Offer> offers = Application.db().getAllOffers();
        Vector<Vector<Object>> data = new Vector<>();
        for (Offer offer : offers) {
            Vector<Object> rowData = new Vector<>();
            rowData.add(offer.type());
            rowData.add(offer.city());
            rowData.add(offer.price());
            rowData.add(offer);
            data.add(rowData);
        }

        Vector<String> header = new Vector<>();
        header.add("Typ oferty");
        header.add("Miasto");
        header.add("Cena");
        header.add("Akcja");

        DefaultTableModel model = new DefaultTableModel(data, header);
        table = new JTable(model) {
            @Override
            public Class<?> getColumnClass(int column) {
                return column == 3 ? Offer.class : Object.class;
            }
        };
        table.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer());
        table.addMouseListener(new ButtonMouseListener(table));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(600, 400));
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        add(scrollPane, constraints);

        setBackground(new Color(255, 240, 206, 255));
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                "Ekran główny",
                TitledBorder.CENTER,
                TitledBorder.TOP));
    }

    private void showUserProfile() {
        Application.getInstance().setPanel(new UserPanel());
    }

    private void logout() {
        Application.getInstance().setPanel(new LoginPanel());
    }

    public class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            setText("Rozwiń");
            return this;
        }
    }

    private class ButtonMouseListener extends MouseAdapter {
        private final JTable table;

        public ButtonMouseListener(JTable table) {
            this.table = table;
        }

        public void mouseClicked(MouseEvent e) {
            int column = table.getColumnModel().getColumnIndexAtX(e.getX());
            int row = e.getY() / table.getRowHeight();
            if (row < table.getRowCount() && row >= 0 && column == 3) {
                Offer offer = (Offer) table.getValueAt(row, column);
                if (offer != null) {
                    showOfferDetails(offer);
                }
            }
        }
    }

    public void showOfferDetails(Offer offer) {
        if (offerDetailsFrame != null) {
            offerDetailsFrame.dispose();
        }

        OfferPanel offerPanel = new OfferPanel(offer);
        offerDetailsFrame = new JFrame("Offer Details");
        offerDetailsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        offerDetailsFrame.getContentPane().add(offerPanel);
        offerDetailsFrame.pack();
        offerDetailsFrame.setLocationRelativeTo(null);
        offerDetailsFrame.setVisible(true);
    }
}
