package main;

import main.Database.Database;
import main.Panels.LoginPanel;
import main.Panels.MainPanel;

import javax.swing.*;
import javax.xml.crypto.Data;
import java.awt.*;
import java.nio.file.Path;

public class Application extends JFrame {
    private static Database _db = new Database();
    public static void appMain() {
        if (instance == null) {
            new Application();
        }
    }

    public static final Application getInstance() {
        return instance;
    }

    public static final Path assetsDir = Path.of("assets");

    public static final Database getDatabase() { return  _db;}

    private Application() {
        instance = this;
        init();
        setPanel(new LoginPanel());
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
        setPanel(new LoginPanel());
    }
    public void setUserId(int newUserId) {
        this.userId = newUserId;
    }

    private static void addClosingEvenet(JFrame frame){
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                int result = JOptionPane.showConfirmDialog(frame,
                        "Are you sure you want to close this window?", "Close Window?",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if (result == JOptionPane.YES_OPTION){
                    _db.close();
                    System.exit(0);
                }
                else {
                    frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                }
            }
        });
    }

    private void init() {
        setTitle("DoIt");


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addClosingEvenet(this);

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
