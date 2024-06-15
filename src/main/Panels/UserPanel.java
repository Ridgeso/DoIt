package main.Panels;
import main.Application;
import main.Database.Database;
import main.Database.Models.Offer;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Vector;
import main.Database.Models.*;
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
    private JButton buttonAdd, buttonAddOffer, showMainPanelButton, show_applications;
    private JPanel innerPanel;
    private Boolean show_application;
    private LinkedList<ContentPanel> contentPanels;
    private JTable table;
    private ArrayList<JButton> buttons;
    private JFrame userinfo;
    private int offeradd_position, offers_position, application_position;
    private JScrollPane pane;

    public UserPanel() {
        db = Application.getDatabase();
        init();
    }

    public void initComponents() {
        final var iconPath = Path.of(Application.assetsDir.toString(), "user.jpg");
        image = new ImageIcon(iconPath.toString());
        label = new JLabel(image);
        constraints = new GridBagConstraints();
        header = new Vector<>();
        contentPanels = new LinkedList<>();
        buttonAdd = new JButton("Dodaj nowe ogłoszenie");
        show_applications = new JButton("Zobacz Twoje aplikacje");
        buttonAddOffer = new JButton("Dodaj");
        buttons = new ArrayList<>();

        data = db.getUserData(Application.getInstance().getUserId());
        applications = db.getUserApplications(Application.getInstance().getUserId());
        label.setBorder(BorderFactory.createBevelBorder(1));
        show_application = Boolean.FALSE;


        for (int j = 0; j < applications.size(); j++) {
            ++constraints.gridy;
            ContentPanel empty = new ContentPanel(Integer.parseInt(applications.get(j).get(0)), applications.get(j).get(1),
                    applications.get(j).get(2), applications.get(j).get(3));
            contentPanels.add(empty);
            final int id = j;
            JButton annoucerDataButton = new JButton("Dane ogłaszającego");
            annoucerDataButton.addActionListener(e -> showAnnoucerDetails(Integer.parseInt(applications.get(id).get(0))));
            buttons.add(annoucerDataButton);
        }

    }

    public JPanel getInnerPanel() {
        return innerPanel;
    }

    private void show_offers() {
        offers = db.getUserOffers(Application.getInstance().getUserId());
        constraints.gridx = 0;
        constraints.gridy = offers_position;

        if (pane != null)
            innerPanel.remove(pane);

        Vector<Vector<Object>> data = new Vector<>();
        for (Offer offer : offers) {
            Vector<Object> rowData = new Vector<>();
            rowData.add(offer.type());
            rowData.add(offer.city());
            rowData.add(offer.price());
            rowData.add(offer.description());
            rowData.add(offer);
            rowData.add(offer);
            rowData.add(offer);
            data.add(rowData);
        }
        DefaultTableModel model = new DefaultTableModel(data, header);

        table = new JTable(model) {
            @Override
            public Class<?> getColumnClass(int column) {
                return (column == 4 | column == 5 || column == 6) ? Offer.class : Object.class;
            }
        };

        table.getColumnModel().getColumn(3).setPreferredWidth(200);
        table.getColumnModel().getColumn(4).setCellRenderer(new UserPanel.ButtonRenderer("Dane"));
        table.getColumnModel().getColumn(5).setCellRenderer(new UserPanel.ButtonRenderer("Opłać"));
        table.getColumnModel().getColumn(6).setCellRenderer(new UserPanel.ButtonRenderer("Usuń"));
        table.addMouseListener(new UserPanel.ButtonMouseListener(table));

        pane = new JScrollPane(table);
        pane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                "Zamieszczone ogłoszenia",
                TitledBorder.CENTER,
                TitledBorder.TOP));
        pane.setPreferredSize(new Dimension(600, 200));

        ++constraints.gridy;
        constraints.insets = new Insets(5, 5, 5, 5);
        innerPanel.add(pane, constraints);
        innerPanel.revalidate();
        innerPanel.repaint();

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
        constraints.gridy = 0;
        constraints.gridx = 1;
        innerPanel.add(label, constraints);
        constraints.gridx = 0;
        constraints.gridy = 1;

        for (int i = 0; i < titles.length; i++) {

            innerPanel.add(dataLabels.get(i), constraints);
            ++constraints.gridy;
        }
        --constraints.gridy;
        JButton logoutButton = new JButton("Wyloguj");
        logoutButton.addActionListener(e -> logout());
        constraints.gridx = 1;
        constraints.anchor = GridBagConstraints.EAST;
        innerPanel.add(logoutButton, constraints);
        constraints.gridx = 0;

        header.add("typ");
        header.add("miasto");
        header.add("cena");
        header.add("opis");
        header.add("Aplikujący");
        header.add("Platnosc");
        header.add("Usuń");

        offers_position = constraints.gridy;
        show_offers();
        ++constraints.gridy;
        offeradd_position = constraints.gridy;

        ++constraints.gridy;
        buttonAdd.addActionListener(e -> showOfferForm());
        innerPanel.add(buttonAdd, constraints);

        showMainPanelButton = new JButton("Pokaż panel główny");
        showMainPanelButton.addActionListener(e -> showMainPanel());
        ++constraints.gridy;
        innerPanel.add(showMainPanelButton, constraints);
        show_applications.addActionListener(e -> show_applications());
        ++constraints.gridy;

        innerPanel.add(show_applications, constraints);
        application_position = constraints.gridy + 1;
        innerPanel.setBackground(new Color(255, 240, 206, 255));
        innerPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                "Panel użytkownika",
                TitledBorder.CENTER,
                TitledBorder.TOP));

        JScrollPane scrollPane = new JScrollPane(innerPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        setVisible(true);
    }

    public class UsersinfoPanel extends JPanel{

        private ArrayList<String> data;
        private  JPanel innerPanel;
        private GridBagConstraints constraints;

        public UsersinfoPanel(Vector<User> users) {

            constraints = new GridBagConstraints();
            innerPanel = new JPanel();
            innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));

            for(int j=0;j< users.size();j++) {

                JPanel fieldsPanel = new JPanel();
                fieldsPanel.setLayout(new GridBagLayout());
                data = new ArrayList<>();
                data.add(users.get(j).firstName());
                data.add(users.get(j).lastName());
                data.add(users.get(j).email());
                data.add(users.get(j).phoneNumber());

                String[] titles = {"imie: ", "nazwisko: ", "email: ", "numer telefonu: "};
                ArrayList<JLabel> dataLabels = new ArrayList<>();

                for (int i = 0; i < titles.length; i++) {
                    dataLabels.add(new JLabel(titles[i] + data.get(i)));
                }

                constraints.fill = GridBagConstraints.HORIZONTAL;
                constraints.gridx = 0;
                constraints.gridy = 1;

                for (int i = 0; i < data.size(); i++) {
                    ++constraints.gridy;
                    fieldsPanel.add(dataLabels.get(i), constraints);
                }
                fieldsPanel.setBorder(BorderFactory.createTitledBorder(
                        BorderFactory.createEtchedBorder(), "Dane"));
                innerPanel.add(fieldsPanel);
            }

            if (users.isEmpty())
                innerPanel.add(new JLabel("Brak aplikujących"));

            setBorder(new EmptyBorder(10, 10, 10, 10));
            setBounds(0, 0, 1000, 630);
            JScrollPane scrollPane = new JScrollPane(innerPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            setLayout(new BorderLayout());
            add(scrollPane, BorderLayout.CENTER);
            setVisible(true);
        }
    }

    public class ButtonRenderer extends JButton implements TableCellRenderer {
        private String text;

        public ButtonRenderer(String text) {
            setOpaque(true);
            this.text = text;
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            setText(text);
            return this;
        }
    }

    public class ButtonMouseListener extends MouseAdapter {
        private final JTable table;

        public ButtonMouseListener(JTable table) {
            this.table = table;
        }

        public void mouseClicked(MouseEvent e) {
            int column = table.getColumnModel().getColumnIndexAtX(e.getX());
            int row = e.getY() / table.getRowHeight();
            if (row < 0 || table.getRowCount() <= row) {
                return;
            }
            if (column == 4) {
                Offer offer = (Offer) table.getValueAt(row, column);
                if (offer != null) {
                    showApplicantDetails(offer.id());
                }
            } else if (column == 5) {
                JOptionPane.showMessageDialog(
                        UserPanel.this,
                        "Przekierowywanie do systemu płatności", "Płatność", JOptionPane.INFORMATION_MESSAGE);
            } else if (column == 6) {
                Offer offer = (Offer) table.getValueAt(row, column);
                if (offer != null) {
                    db.deleteOffer(offer.id());
                    offers.clear();
                    show_offers();

                }
            }
        }
    }

        private void showApplicantDetails(int id_offer) {
            Vector<User> applicants = db.getApplicantsData(id_offer);
            if (userinfo != null) {
                userinfo.dispose();
            }

                UsersinfoPanel userinfoPanel = new UsersinfoPanel(applicants);
                userinfo = new JFrame("Dane");
                userinfo.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                userinfo.getContentPane().add(userinfoPanel);
                userinfo.pack();
                userinfo.setLocationRelativeTo(null);
                userinfo.setVisible(true);


        }

        private void showAnnoucerDetails(int id_offer) {
            User annoucer = db.getAnnoucerData(id_offer);
            if (userinfo != null) {
                userinfo.dispose();
            }
            Vector<User> annoucers = new Vector<>();
            annoucers.add(annoucer);
            UsersinfoPanel userinfoPanel = new UsersinfoPanel(annoucers);
            userinfo = new JFrame("Dane ");
            userinfo.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            userinfo.getContentPane().add(userinfoPanel);
            userinfo.pack();
            userinfo.setLocationRelativeTo(null);
            userinfo.setVisible(true);
        }


        private void logout() {
            Application.getInstance().logout();
        }

    private void show_applications()
    {
        if(show_application)
            return;
        show_application = Boolean.TRUE;
        constraints.gridy =  application_position;

        for (int j = 0; j < applications.size(); j++) {

            ++constraints.gridy;
            innerPanel.add(contentPanels.get(j), constraints);
            ++constraints.gridy;
            innerPanel.add(buttons.get(j), constraints);

        }
        innerPanel.revalidate();
    }



    private void AddOffer(String city, String type, float price, String description) {

        innerPanel.remove(offerAddPanel);
        innerPanel.remove(buttonAddOffer);
        boolean success = true;

        constraints.gridy = offeradd_position;
        try {
            db.addNewOfferWhenUserExists(Application.getInstance().getUserId(), city, type, price, description);
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
            JOptionPane.showMessageDialog(
                    UserPanel.this,
                    "Błąd przy dodawaniu oferty",
                    "Błąd",
                    JOptionPane.ERROR_MESSAGE);
            success = false;
        }
        if (success) {
            JOptionPane.showMessageDialog(
                    UserPanel.this,
                    "Dodano ofertę",
                    "Informacja",
                    JOptionPane.INFORMATION_MESSAGE);
        }

        ++constraints.gridy;
        innerPanel.revalidate();
        innerPanel.add(buttonAdd, constraints);
        ++constraints.gridy;
        innerPanel.add(showMainPanelButton,constraints);
        ++constraints.gridy;
        innerPanel.add(show_applications,constraints);
        offers.clear();
        show_offers();

    }

    private void showOfferForm() {

        innerPanel.remove(buttonAdd);
        innerPanel.remove(showMainPanelButton);
        innerPanel.remove(show_applications);

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

        buttonAddOffer.addActionListener(e ->
        {
            try {
                AddOffer(textType.getText(),
                        textCity.getText(), Float.parseFloat(textPrize.getText()), textDescription.getText());
            } catch (NumberFormatException exception) {
                System.out.println(exception.getMessage());
                JOptionPane.showMessageDialog(
                        UserPanel.this,
                        "Złe parametry ogłoszenia",
                        "Błąd",
                        JOptionPane.ERROR_MESSAGE);
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

        if(show_application)
        {
            for(int i=0;i<contentPanels.size();i++)
            {
                innerPanel.remove(contentPanels.get(i));
                innerPanel.remove(buttons.get(i));

            }
        }
        show_application = false;

        int tmp = constraints.gridy;
        constraints.gridy = offeradd_position;
        innerPanel.add(offerAddPanel, constraints);
        offerAddPanel.setVisible(true);
        ++constraints.gridy;
        innerPanel.add(buttonAddOffer, constraints);
        ++constraints.gridy;
        innerPanel.add(showMainPanelButton, constraints);
        ++constraints.gridy;
        innerPanel.add(show_applications, constraints);
        constraints.gridy = tmp;
        innerPanel.revalidate();
    }

        private void showMainPanel() {
            Application.getInstance().setPanel(new MainPanel());
        }
    }

