package main.Panels;

import main.Database.Database;
import main.Database.Models.User;
import main.Panels.FieldLimiters.JTextFieldLimit;

import javax.swing.*;
import java.awt.*;

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
            User newUser = new User(firstNameField.getText(),
                    lastNameField.getText(),
                    emailField.getText(),
                    loginField.getText(),
                    new String(passwordField.getPassword()),
                    phoneNumberField.getText());
            if (validateData(newUser)) {
                registerUser(newUser);
                    JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
                    frame.getContentPane().removeAll();
                    frame.getContentPane().add(new UserPanel()); //do zmiany na mainPanel
                    frame.revalidate();
                    frame.repaint();
                    JOptionPane.showMessageDialog(RegistrationPanel.this, "Błąd podczas rejestracji użytkownika", "Błąd", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(RegistrationPanel.this, "Proszę uzupełnić poprawnie wszystkie pola", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
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
        _database = new Database();

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
        add(new JLabel("Login:"));
        add(loginField);
        add(new JLabel("Email:"));
        add(emailField);
        add(new JLabel("Hasło:"));
        add(passwordField);
        add(new JLabel("Powtórz Hasło:"));
        add(confirmPasswordField);
        add(new JLabel("Imię:"));
        add(firstNameField);
        add(new JLabel("Nazwisko:"));
        add(lastNameField);
        add(new JLabel("Numer telefonu:"));
        add(phoneNumberField);
        add(agreeCheckBox);
        add(registerButton);
    }

    private boolean validateData(User user) {
        return !user.firstName().isEmpty() && !user.lastName().isEmpty() && !user.email().isEmpty()
                && !user.login().isEmpty() && !user.password().isEmpty() && !user.phoneNumber().isEmpty();
    }

    private void registerUser(User newUser)
    {
        _database.addNewUser(newUser.firstName(), newUser.lastName(), newUser.email(), newUser.login(), newUser.password(), newUser.phoneNumber());
    }
}


