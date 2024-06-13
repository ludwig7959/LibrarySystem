package xyz.ludwicz.librarysystem.gui;

import xyz.ludwicz.librarysystem.LibrarySystem;
import xyz.ludwicz.librarysystem.database.SQLTask;
import xyz.ludwicz.librarysystem.database.SQLTask.TaskType;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

public class PublisherAddDialog extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JTextField nameTextField;

    public PublisherAddDialog() {
        setBounds(100, 100, 200, 260);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        JLabel publisherAddLabel = new JLabel("출판사 추가");
        publisherAddLabel.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        publisherAddLabel.setBounds(12, 10, 103, 15);
        contentPanel.add(publisherAddLabel);

        JLabel nameLabel = new JLabel("이름");
        nameLabel.setBounds(12, 35, 35, 15);
        contentPanel.add(nameLabel);

        nameTextField = new JTextField();
        nameTextField.setBounds(56, 32, 116, 21);
        contentPanel.add(nameTextField);
        nameTextField.setColumns(32);

        JLabel addressLabel = new JLabel("주소");
        addressLabel.setBounds(12, 60, 57, 15);
        contentPanel.add(addressLabel);

        JTextArea addressTextArea = new JTextArea();
        addressTextArea.setLineWrap(true);
        addressTextArea.setBorder(new LineBorder(Color.black));
        addressTextArea.setBounds(12, 85, 160, 96);
        contentPanel.add(addressTextArea);

        JButton addButton = new JButton("추가");
        addButton.setBounds(42, 191, 97, 23);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String publisherName = nameTextField.getText();
                String address = addressTextArea.getText();

                if (publisherName == null || publisherName.isEmpty()) {
                    JOptionPane.showMessageDialog(PublisherAddDialog.this, "출판사 이름을 입력하세요.", "경고", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if(address == null || address.isEmpty()) {
                    JOptionPane.showMessageDialog(PublisherAddDialog.this, "주소를 입력하세요.", "경고", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    SQLTask checkTask = new SQLTask(TaskType.QUERY, "SELECT COUNT(*) AS count FROM Publisher WHERE name = ?", publisherName);
                    ResultSet checkResult = (ResultSet) LibrarySystem.getInstance().getDatabaseManager().getTaskProcessor().submitTask(checkTask).get();
                    if (checkResult.next() && checkResult.getInt("count") > 1) {
                        JOptionPane.showMessageDialog(PublisherAddDialog.this, "이미 존재하는 출판사 이름입니다.", "경고", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    SQLTask addTask = new SQLTask(TaskType.UPDATE, "INSERT INTO Publisher (name, address) VALUES (?, ?)", publisherName, address);
                    boolean addResult = (Boolean) LibrarySystem.getInstance().getDatabaseManager().getTaskProcessor().submitTask(addTask).get();
                    if (addResult) {
                        JOptionPane.showMessageDialog(PublisherAddDialog.this, "출판사 추가가 완료되었습니다.", "정보", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(PublisherAddDialog.this, "출판사 추가에 실패하였습니다.", "경고", JOptionPane.WARNING_MESSAGE);
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