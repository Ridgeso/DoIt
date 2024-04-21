package main;

import main.Panels.UserPanel;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;

public class Application extends JFrame
{

    public static void appMain()
    {
        if (instance == null)
        {
            new Application();
        }
    }

    public static final Application getInstance() { return instance; }
    public static final Path assetsDir = Path.of("assets");

    private Application()
    {
        instance = this;
        init();
        setPanel(new UserPanel());
    }

    private void setPanel(JPanel panel)
    {
        getContentPane().add(panel);
        instance.revalidate();
        instance.repaint();
    }

    public int getUser()
    {
        return user;
    }

    public void setUser(int newUser)
    {
        this.user = newUser;
    }
    
    private void init()
    {
        setTitle("DoIt");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);
        setSize(1000, 630);
        setVisible(true);

        setBackground(new Color(255, 240, 206, 255));

        final var iconPath = Path.of(assetsDir.toString(), "icon.jpg");
        final var iconImage = new ImageIcon(iconPath.toString());
        setIconImage(iconImage.getImage());
    }

    private static Application instance = null;
    private int user;
}
