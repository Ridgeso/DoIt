package main;

import main.Database.Database;
import main.Panels.LoginPanel;
import main.Panels.MainPanel;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;

public class Application extends JFrame {
    private static Database _db = null;
    public static void appMain(Database db) {
        if (instance == null) {
            _db = db;
            new Application(_db);
        }
    }

    public static final Application getInstance() {
        return instance;
    }

    public static final Path assetsDir = Path.of("assets");

    private Application(Database db) {
        instance = this;
        init();
        setPanel(new LoginPanel(db));
    }

    public void setPanel(JPanel panel) {
        getContentPane().removeAll(); 
        getContentPane().add(panel);
        revalidate();
        repaint();
    }

    public int getUserId() {
        return userId;
    }
    public void logout(){
        setPanel(new LoginPanel(_db));
    }
    public void setUserId(int newUserId) {
        this.userId = newUserId;
    }

    private void init() {

        setTitle("DoIt");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(1000, 630);
        setVisible(true);

        setBackground(new Color(255, 240, 206, 255));

        final var iconPath = Path.of(assetsDir.toString(), "icon.jpg");
        final var iconImage = new ImageIcon(iconPath.toString());
        setIconImage(iconImage.getImage());
    }

    private static Application instance = null;
    public static final int INVALID_USER_ID = -1;
    private int userId = INVALID_USER_ID;
}
