package main.Panels;

import main.Application;
import main.Database.Database;
import main.Panels.FieldLimiters.JTextFieldLimit;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class LoginPanel extends JPanel {
    private JLabel loginLabel, passwordLabel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private Database _database;
    private JButton loginButton, goToRegistration;

    public LoginPanel() {
        initializeComponents();
        addComponentsToPanel();
        setBackground(new Color(255, 240, 206, 255));
        loginButton.addActionListener(e -> {
            if(!usernameField.getText().isEmpty() && passwordField.getPassword().length > 0) {
                try {
                    int id = _database.checkUserLogin(usernameField.getText(), new String(passwordField.getPassword()));
                    if(id >= 0) {
                        Application.getInstance().setUserId(id);
                        JOptionPane.showMessageDialog(LoginPanel.this, "Użytkownik poprawnie zalogowany", "Informacja", JOptionPane.INFORMATION_MESSAGE);
                        changePanel(new UserPanel());
                    }
                    else {
                        JOptionPane.showMessageDialog(LoginPanel.this, "Nie istnieje taki użytkownik", "Błąd", JOptionPane.ERROR_MESSAGE);
                    }
                }
                catch (SQLException exception){
                    System.out.println(exception.getMessage());
                    JOptionPane.showMessageDialog(LoginPanel.this, "Błąd podczas logowania", "Błąd", JOptionPane.ERROR_MESSAGE);
                }

                //JOptionPane.showMessageDialog(LoginPanel.this, "Błąd podczas logowania", "Błąd", JOptionPane.ERROR_MESSAGE);
                passwordField.setText("");
            }
            else {
                JOptionPane.showMessageDialog(LoginPanel.this, "Wpisz wszystkie dane", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        });
        goToRegistration.addActionListener(e -> {
            changePanel(new RegistrationPanel());
        });
    }


    private void initializeComponents() {
        loginLabel = new JLabel("Login:");
        passwordLabel = new JLabel("Hasło");

        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Zaloguj");
        goToRegistration = new JButton("Rejestracja");
        usernameField.setDocument(new JTextFieldLimit(42));
        passwordField.setDocument(new JTextFieldLimit(32));
        _database = new Database();
    }

    private void addComponentsToPanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        add(loginLabel, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        add(usernameField, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 2;
        add(passwordLabel, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 3;
        add(passwordField, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 4;
        add(loginButton, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 5;
        add(goToRegistration, c);

        setBounds(0,0,1000,630);
        setVisible(true);
    }

    public void changePanel(JPanel panel) {
        removeAll();
        add(panel);
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        var frame = new JFrame();

        frame.setTitle("DoIt");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setResizable(false);
        frame.setSize(1000, 630);
        frame.setVisible(true);
        frame.setBackground(new Color(255, 240, 206, 255));

        frame.add(new LoginPanel());
        frame.revalidate();
        frame.repaint();

    }

}
