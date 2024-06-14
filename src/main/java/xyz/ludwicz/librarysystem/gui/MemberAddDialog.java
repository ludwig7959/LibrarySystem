package xyz.ludwicz.librarysystem.gui;

import xyz.ludwicz.librarysystem.LibrarySystem;
import xyz.ludwicz.librarysystem.database.SQLTask;
import xyz.ludwicz.librarysystem.database.SQLTask.TaskType;
import xyz.ludwicz.librarysystem.gui.models.PhoneNumberDocumentFilter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

public class MemberAddDialog extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JTextField idTextField;
    private JTextField nameTextField;
    private JTextField phoneNumberTextField;

    public MemberAddDialog() {
        setBounds(100, 100, 220, 210);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        JLabel addMemberLabel = new JLabel("회원 추가");
        addMemberLabel.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        addMemberLabel.setBounds(12, 10, 63, 15);
        contentPanel.add(addMemberLabel);

        JLabel typeLabel = new JLabel("유형");
        typeLabel.setBounds(12, 35, 47, 15);
        contentPanel.add(typeLabel);

        JComboBox<String> typeComboBox = new JComboBox<>(new String[] {"학생", "교수"});
        typeComboBox.setBounds(76, 31, 116, 23);
        contentPanel.add(typeComboBox);

        JLabel idLabel = new JLabel("번호");
        idLabel.setBounds(12, 60, 33, 15);
        contentPanel.add(idLabel);

        idTextField = new JTextField();
        idTextField.setBounds(76, 57, 116, 21);
        contentPanel.add(idTextField);
        idTextField.setColumns(16);

        JLabel nameLabel = new JLabel("이름");
        nameLabel.setBounds(12, 85, 33, 15);
        contentPanel.add(nameLabel);

        nameTextField = new JTextField();
        nameTextField.setBounds(76, 82, 116, 21);
        contentPanel.add(nameTextField);
        nameTextField.setColumns(32);

        JLabel phoneNumberLabel = new JLabel("전화번호");
        phoneNumberLabel.setBounds(12, 110, 57, 15);
        contentPanel.add(phoneNumberLabel);

        phoneNumberTextField = new JTextField();
        phoneNumberTextField.setBounds(76, 107, 116, 21);
        ((AbstractDocument) phoneNumberTextField.getDocument()).setDocumentFilter(new PhoneNumberDocumentFilter());
        contentPanel.add(phoneNumberTextField);
        phoneNumberTextField.setColumns(16);

        JButton addButton = new JButton("추가");
        addButton.setBounds(52, 135, 97, 23);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String type = typeComboBox.getSelectedItem().equals("학생") ? "STUDENT" : "PROFESSOR";
                String id = idTextField.getText();
                String name = nameTextField.getText();
                String phone = phoneNumberTextField.getText();

                if (id == null || id.isEmpty()) {
                    JOptionPane.showMessageDialog(MemberAddDialog.this, "학번/사번을 입력하세요.", "경고", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (name == null || name.isEmpty()) {
                    JOptionPane.showMessageDialog(MemberAddDialog.this, "이름을 입력하세요.", "경고", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (name.length() <= 1 || !name.matches("^[a-z|A-Z|ㄱ-ㅎ|가-힣| ]*$")) {
                    JOptionPane.showMessageDialog(MemberAddDialog.this, "이름은 2글자 이상의 한글, 영문을 조합해야 합니다.", "경고", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (phone == null || phone.isEmpty()) {
                    JOptionPane.showMessageDialog(MemberAddDialog.this, "전화번호를 입력하세요.", "경고", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (!phone.matches("^\\d{2,3}-\\d{3,4}-\\d{4}$")) {
                    JOptionPane.showMessageDialog(MemberAddDialog.this, "올바르지 않은 전화번호입니다. (하이픈 포함)", "경고", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    SQLTask idCheckTask = new SQLTask(TaskType.QUERY, "SELECT COUNT(*) AS count FROM Member WHERE memberId = ?", id);
                    ResultSet idCheckResult = (ResultSet) LibrarySystem.getInstance().getDatabaseManager().getTaskProcessor().submitTask(idCheckTask).get();
                    if (idCheckResult.next() && idCheckResult.getInt("count") > 1) {
                        JOptionPane.showMessageDialog(MemberAddDialog.this, "이미 존재하는 학번/사번입니다.", "경고", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    SQLTask phoneCheckTask = new SQLTask(TaskType.QUERY, "SELECT COUNT(*) AS count FROM Member WHERE phone = ?", phone);
                    ResultSet phoneCheckResult = (ResultSet) LibrarySystem.getInstance().getDatabaseManager().getTaskProcessor().submitTask(phoneCheckTask).get();
                    if (phoneCheckResult.next() && phoneCheckResult.getInt("count") > 1) {
                        JOptionPane.showMessageDialog(MemberAddDialog.this, "이미 사용중인 전화번호입니다.", "경고", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    SQLTask addTask = new SQLTask(TaskType.UPDATE, "INSERT INTO Member (memberId, name, phone, type) VALUES (?, ?, ?, ?)", id, name, phone, type);
                    boolean addResult = (Boolean) LibrarySystem.getInstance().getDatabaseManager().getTaskProcessor().submitTask(addTask).get();
                    if (addResult) {
                        JOptionPane.showMessageDialog(MemberAddDialog.this, "회원 추가가 완료되었습니다.", "정보", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(MemberAddDialog.this, "회원 추가에 실패하였습니다.", "경고", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    dispose();
                }
            }
        });
        contentPanel.add(addButton);

        setLocationRelativeTo(null);
    }
}
