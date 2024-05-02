package main.Panels;

import main.Application;
import main.Database.Database;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.border.TitledBorder;

public class UserPanel  extends JPanel {

    public UserPanel()
    {
        init();
    }

    public void init()
    {
        int i;
        final var iconPath = Path.of(Application.assetsDir.toString(), "user.jpg");
        ImageIcon image = new ImageIcon(iconPath.toString());
        JLabel label = new JLabel(image);
        GridBagConstraints c = new GridBagConstraints();
        Vector<String> header = new Vector<>();
        label.setBorder(BorderFactory.createBevelBorder(1));
        setLayout(new GridBagLayout());

        data = db.getUserData(Application.getInstance().getUserId());
        String[] titles = {"imie:","nazwisko:","email:","numer telefonu:"};

        ArrayList<JLabel> dataLabels = new ArrayList<>();
        for (i = 0; i < data.size(); i++) {

            dataLabels.add(new JLabel(titles[i]+data.get(i)));

        }
        offers = db.getUserOffers(Application.getInstance().getUserId());
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;
        add(label,c);

        c.gridx = 0;
        for(i=0;i<data.size();i++)
        {
            c.gridy = 1+i;
            add(dataLabels.get(i), c);
        }
        c.weighty = 1;
        c.gridy = ++i;

        header.add("typ");
        header.add("miasto");
        header.add("cena");
        header.add("opis");

        JTable table = new JTable(offers, header);
        c.gridy = i;
        c.gridx = 0;
        JScrollPane pane = new JScrollPane(table);
        pane.setBorder (BorderFactory.createTitledBorder (BorderFactory.createEtchedBorder (),
                "Zamieszczone ogłoszenia",
                TitledBorder.CENTER,
                TitledBorder.TOP));
        pane.setPreferredSize(new Dimension(500,200));
        add(pane,c);

        setBackground(new Color(255, 240, 206, 255));
        setBorder(BorderFactory.createTitledBorder (BorderFactory.createEtchedBorder (),
                "Panel użytkownika",
                TitledBorder.CENTER,
                TitledBorder.TOP));
        setVisible(true);
        setBounds(0, 0, 1000, 630);
    }

    private ArrayList<String> data;
    private Vector<Vector<String>> offers;
    static Database db = new Database();

}
