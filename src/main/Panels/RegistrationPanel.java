package main.Panels;

import main.Application;
import main.Database.Database;
import main.Database.Models.User;
import main.Panels.FieldLimiters.JTextFieldLimit;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class RegistrationPanel extends JPanel {
    private JTextField loginField, emailField, firstNameField, lastNameField, phoneNumberField;
    private JPasswordField passwordField, confirmPasswordField;
    private JCheckBox agreeCheckBox;
    private JButton registerButton;
    private Database _database;

    public RegistrationPanel() {
        initializeComponents();
        addComponentsToPanel();
        registerButton.addActionListener(e -> {
            User newUser = new User(
                firstNameField.getText(),
                lastNameField.getText(),
                emailField.getText(),
                loginField.getText(),
                new String(passwordField.getPassword()),
                phoneNumberField.getText());

            if (!isUserValida(newUser)) {
                JOptionPane.showMessageDialog(RegistrationPanel.this, "Proszę uzupełnić poprawnie wszystkie pola", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }

            registerUser(newUser);
            try {
                Application.getInstance().setUserId(_database.checkIfUserExist(newUser.firstName(), newUser.lastName()));
            } catch (SQLException er) {}

            Application.getInstance().setPanel(new UserPanel());
        });
    }

    private void initializeComponents() {
        loginField = new JTextField(20);
        emailField = new JTextField(20);
        firstNameField = new JTextField(20);
        lastNameField = new JTextField(20);
        phoneNumberField = new JTextField(9);
        passwordField = new JPasswordField(20);
        confirmPasswordField = new JPasswordField(20);
        agreeCheckBox = new JCheckBox("Zgoda na RODO");
        registerButton = new JButton("Zarejestruj");
        _database = Application.getDatabase();
        agreeCheckBox.setOpaque(false);
        loginField.setDocument(new JTextFieldLimit(42));
        emailField.setDocument(new JTextFieldLimit(42));
        firstNameField.setDocument(new JTextFieldLimit(42));
        lastNameField.setDocument(new JTextFieldLimit(42));
        phoneNumberField.setDocument(new JTextFieldLimit(9));
        passwordField.setDocument(new JTextFieldLimit(32));
        confirmPasswordField.setDocument(new JTextFieldLimit(32));
    }

    private void addComponentsToPanel() {
        setLayout(new GridLayout(8, 2));
        setOpaque( false );
        add(new JLabel("Login:"));          add(loginField);
        add(new JLabel("Email:"));          add(emailField);
        add(new JLabel("Hasło:"));          add(passwordField);
        add(new JLabel("Powtórz Hasło:"));  add(confirmPasswordField);
        add(new JLabel("Imię:"));           add(firstNameField);
        add(new JLabel("Nazwisko:"));       add(lastNameField);
        add(new JLabel("Numer telefonu:")); add(phoneNumberField);
        add(agreeCheckBox);                      add(registerButton);
    }

    private boolean isUserValida(User user) {
        return !user.firstName().isEmpty() && !user.lastName().isEmpty() && !user.email().isEmpty()
                && !user.login().isEmpty() && !user.password().isEmpty() && !user.phoneNumber().isEmpty();
    }

    private void registerUser(User newUser)
    {
        _database.addNewUser(newUser.firstName(), newUser.lastName(), newUser.email(), newUser.login(), newUser.password(), newUser.phoneNumber());
    }
    public static void main(String[] args)
    {
        var frame = new JFrame();
        frame.setTitle("DoIt");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(1000, 630);
        frame.setVisible(true);
        frame.setBackground(new Color(255, 240, 206, 255));

        Database db= Application.getDatabase();
        frame.add(new RegistrationPanel());
        frame.revalidate();
        frame.repaint();
    }
}


