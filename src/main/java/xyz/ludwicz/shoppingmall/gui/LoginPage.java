package xyz.ludwicz.shoppingmall.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPage extends JPanel {
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginPage() {
        setSize(400, 300);

        setLayout(new GridLayout(3, 2));

        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Login button clicked!");
            }
        });

        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(new JLabel());
        add(loginButton);
    }
}