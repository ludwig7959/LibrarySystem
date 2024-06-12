package xyz.ludwicz.librarysystem.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginUI extends JFrame {

    private JPanel contentPane;
    private JTextField idInputTextField;
    private JPasswordField passwordInputField;

    public LoginUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 350, 200);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel title = new JLabel("도서관 관리 시스템");
        title.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        title.setBounds(75, 10, 179, 30);
        contentPane.add(title);

        JLabel idLabel = new JLabel("아이디");
        idLabel.setBounds(32, 53, 57, 15);
        contentPane.add(idLabel);

        idInputTextField = new JTextField();
        idInputTextField.setBounds(96, 50, 116, 21);
        contentPane.add(idInputTextField);
        idInputTextField.setColumns(10);

        JLabel passwordLabel = new JLabel("비밀번호");
        passwordLabel.setBounds(32, 84, 48, 15);
        contentPane.add(passwordLabel);

        passwordInputField = new JPasswordField();
        passwordInputField.setBounds(96, 81, 116, 21);
        contentPane.add(passwordInputField);

        JButton loginButton = new JButton("로그인");
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        loginButton.setBounds(225, 50, 97, 49);
        contentPane.add(loginButton);

        JButton registerButton = new JButton("회원가입");
        registerButton.setBounds(106, 112, 97, 23);
        contentPane.add(registerButton);
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RegisterDialog().setVisible(true);
            }
        });

        setLocationRelativeTo(null);
    }
}