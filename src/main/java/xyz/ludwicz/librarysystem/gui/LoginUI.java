package xyz.ludwicz.librarysystem.gui;

import xyz.ludwicz.librarysystem.LibrarySystem;
import xyz.ludwicz.librarysystem.util.Utils;
import xyz.ludwicz.librarysystem.database.SQLTask;
import xyz.ludwicz.librarysystem.gui.models.NoSpacesDocumentFilter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

public class LoginUI extends JFrame {

    private JPanel contentPane;
    private JTextField idTextField;
    private JPasswordField passwordTextField;

    private RegisterDialog registerDialog;

    public LoginUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
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

        idTextField = new JTextField();
        idTextField.setBounds(96, 50, 116, 21);
        contentPane.add(idTextField);
        idTextField.setColumns(12);

        JLabel passwordLabel = new JLabel("비밀번호");
        passwordLabel.setBounds(32, 84, 48, 15);
        contentPane.add(passwordLabel);

        passwordTextField = new JPasswordField();
        passwordTextField.setBounds(96, 81, 116, 21);
        ((AbstractDocument) passwordTextField.getDocument()).setDocumentFilter(new NoSpacesDocumentFilter());
        contentPane.add(passwordTextField);

        JButton loginButton = new JButton("로그인");
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String id = idTextField.getText();
                char[] password = passwordTextField.getPassword();

                if (id == null || id.isEmpty()) {
                    JOptionPane.showMessageDialog(LoginUI.this, "아이디를 입력하세요.", "경고", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (!id.matches("^[a-zA-Z]{1}[a-zA-Z0-9_]{4,11}$")) {
                    JOptionPane.showMessageDialog(LoginUI.this, "아이디는 5~12자의 영문, 숫자, 하이픈(_) 조합이어야 합니다.", "경고", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (password == null || password.length == 0) {
                    JOptionPane.showMessageDialog(LoginUI.this, "비밀번호를 입력하세요.", "경고", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (password.length < 6) {
                    JOptionPane.showMessageDialog(LoginUI.this, "비밀번호는 6자 이상이어야 합니다.", "경고", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                SQLTask loginTask = new SQLTask(SQLTask.TaskType.QUERY, "SELECT * FROM Librarian WHERE librarianId = ?", id);
                try {
                    ResultSet loginResult = (ResultSet) LibrarySystem.getInstance().getDatabaseManager().getTaskProcessor().submitTask(loginTask).get();
                    if(!loginResult.next()) {
                        JOptionPane.showMessageDialog(LoginUI.this, "존재하지 않는 아이디입니다.", "경고", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    
                    String encryptedPassword = Utils.hashPassword(password);
                    if(encryptedPassword.equals(loginResult.getString("password"))) {
                        LibrarySystem.getInstance().login(loginResult.getString("librarianId"), loginResult.getString("name"), loginResult.getString("phone"));
                    } else {
                        JOptionPane.showMessageDialog(LoginUI.this, "올바르지 않은 비밀번호입니다.", "경고", JOptionPane.WARNING_MESSAGE);
                    }
                } catch(InterruptedException | ExecutionException | SQLException ex) {
                    ex.printStackTrace();
                }
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
                if(registerDialog != null && registerDialog.isDisplayable()) {
                    return;
                }
                registerDialog = new RegisterDialog();
                registerDialog.setVisible(true);
            }
        });

        setLocationRelativeTo(null);
    }
}