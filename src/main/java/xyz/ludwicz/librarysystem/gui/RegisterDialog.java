package xyz.ludwicz.librarysystem.gui;

import xyz.ludwicz.librarysystem.LibrarySystem;
import xyz.ludwicz.librarysystem.util.Utils;
import xyz.ludwicz.librarysystem.database.SQLTask;
import xyz.ludwicz.librarysystem.database.SQLTaskProcessor;
import xyz.ludwicz.librarysystem.gui.models.NoSpacesDocumentFilter;
import xyz.ludwicz.librarysystem.gui.models.PhoneNumberDocumentFilter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

public class RegisterDialog extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JTextField idTextField;
    private JTextField nameTextField;
    private JTextField phoneNumberTextField;
    private JPasswordField passwordTextField;

    public RegisterDialog() {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 240, 260);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        JLabel registerLabel = new JLabel("회원가입");
        registerLabel.setBounds(78, 10, 79, 22);
        registerLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        contentPanel.add(registerLabel);

        JLabel idLabel = new JLabel("아이디");
        idLabel.setBounds(12, 42, 50, 15);
        contentPanel.add(idLabel);

        idTextField = new JTextField();
        idTextField.setBounds(74, 39, 138, 21);
        contentPanel.add(idTextField);
        idTextField.setColumns(12);


        JLabel passwordLabel = new JLabel("비밀번호");
        passwordLabel.setBounds(12, 73, 50, 15);
        contentPanel.add(passwordLabel);

        passwordTextField = new JPasswordField();
        passwordTextField.setBounds(74, 70, 138, 21);
        ((AbstractDocument) passwordTextField.getDocument()).setDocumentFilter(new NoSpacesDocumentFilter());
        contentPanel.add(passwordTextField);

        JLabel nameLabel = new JLabel("이름");
        nameLabel.setBounds(12, 104, 50, 15);
        contentPanel.add(nameLabel);

        nameTextField = new JTextField();
        nameTextField.setColumns(37);
        nameTextField.setBounds(74, 101, 138, 21);
        contentPanel.add(nameTextField);

        JLabel phoneNumberLabel = new JLabel("전화번호");
        phoneNumberLabel.setBounds(12, 135, 50, 15);
        contentPanel.add(phoneNumberLabel);

        phoneNumberTextField = new JTextField();
        phoneNumberTextField.setColumns(32);
        phoneNumberTextField.setBounds(74, 132, 138, 21);
        ((AbstractDocument) phoneNumberTextField.getDocument()).setDocumentFilter(new PhoneNumberDocumentFilter());
        contentPanel.add(phoneNumberTextField);

        JButton registerButton = new JButton("회원가입");
        registerButton.setBounds(75, 188, 83, 23);
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idTextField.getText();
                char[] password = passwordTextField.getPassword();
                String name = nameTextField.getText();
                String phoneNumber = phoneNumberTextField.getText();

                if (id == null || id.isEmpty()) {
                    JOptionPane.showMessageDialog(RegisterDialog.this, "아이디를 입력하세요.", "경고", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (!id.matches("^[a-zA-Z]{1}[a-zA-Z0-9_]{4,11}$")) {
                    JOptionPane.showMessageDialog(RegisterDialog.this, "아이디는 5~12자의 영문, 숫자, 하이픈(_) 조합이어야 합니다.", "경고", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (password == null || password.length == 0) {
                    JOptionPane.showMessageDialog(RegisterDialog.this, "비밀번호를 입력하세요.", "경고", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (password.length < 6) {
                    JOptionPane.showMessageDialog(RegisterDialog.this, "비밀번호는 6자 이상이어야 합니다.", "경고", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (name == null || name.isEmpty()) {
                    JOptionPane.showMessageDialog(RegisterDialog.this, "이름을 입력하세요.", "경고", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (name.length() <= 1 || !name.matches("^[a-z|A-Z|ㄱ-ㅎ|가-힣| ]*$")) {
                    JOptionPane.showMessageDialog(RegisterDialog.this, "이름은 2글자 이상의 한글, 영문을 조합해야 합니다.", "경고", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (phoneNumber == null || phoneNumber.isEmpty()) {
                    JOptionPane.showMessageDialog(RegisterDialog.this, "전화번호를 입력하세요.", "경고", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (!phoneNumber.matches("^\\d{2,3}-\\d{3,4}-\\d{4}$")) {
                    JOptionPane.showMessageDialog(RegisterDialog.this, "올바르지 않은 전화번호입니다. (하이픈 포함)", "경고", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                registerUser(id, password, name, phoneNumber);
            }
        });
        contentPanel.add(registerButton);

        setLocationRelativeTo(null);
    }

    private void registerUser(String id, char[] password, String name, String phoneNumber) {
        SQLTaskProcessor taskProcessor = LibrarySystem.getInstance().getDatabaseManager().getTaskProcessor();

        SQLTask idCheckTask = new SQLTask(SQLTask.TaskType.QUERY, "SELECT COUNT(*) AS count FROM Librarian WHERE librarianId = ?", id);
        SQLTask phoneNumberCheckTask = new SQLTask(SQLTask.TaskType.QUERY, "SELECT COUNT(*) AS count FROM Librarian WHERE phone = ?", phoneNumber);
        try {
            ResultSet idCheckResult = (ResultSet) taskProcessor.submitTask(idCheckTask).get();
            ResultSet phoneNumberCheckResult = (ResultSet) taskProcessor.submitTask(phoneNumberCheckTask).get();

            if (idCheckResult.next() && idCheckResult.getInt("count") > 0) {
                JOptionPane.showMessageDialog(RegisterDialog.this, "중복된 아이디입니다.", "경고", JOptionPane.WARNING_MESSAGE);
            } else if (phoneNumberCheckResult.next() && phoneNumberCheckResult.getInt("count") > 0) {
                JOptionPane.showMessageDialog(RegisterDialog.this, "이미 사용중인 전화번호입니다.", "경고", JOptionPane.WARNING_MESSAGE);
            } else {
                String encryptedPassword = Utils.hashPassword(password);

                SQLTask registerTask = new SQLTask(SQLTask.TaskType.UPDATE, "INSERT INTO Librarian (librarianId, password, name, phone) VALUES (?, ?, ?, ?)",
                        id, encryptedPassword, name, phoneNumber);
                boolean registerResult = (Boolean) taskProcessor.submitTask(registerTask).get();
                if (registerResult) {
                    JOptionPane.showMessageDialog(RegisterDialog.this, "회원가입이 완료되었습니다.", "정보", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(RegisterDialog.this, "회원가입에 실패하였습니다.", "경고", JOptionPane.WARNING_MESSAGE);
                }
            }
        } catch (InterruptedException | ExecutionException | SQLException e) {
            e.printStackTrace();
        }
    }
}
