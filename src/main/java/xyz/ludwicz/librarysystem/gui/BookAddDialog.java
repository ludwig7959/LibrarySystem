package xyz.ludwicz.librarysystem.gui;

import xyz.ludwicz.librarysystem.LibrarySystem;
import xyz.ludwicz.librarysystem.database.SQLTask;
import xyz.ludwicz.librarysystem.database.SQLTask.TaskType;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class BookAddDialog extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JTextField titleTextField;
    private JTextField authorTextField;

    public BookAddDialog() {
        setBounds(100, 100, 280, 220);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);
        JLabel bookAddLabel = new JLabel("도서 추가");
        bookAddLabel.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        bookAddLabel.setBounds(12, 10, 78, 15);
        contentPanel.add(bookAddLabel);

        JLabel titleLabel = new JLabel("제목");
        titleLabel.setBounds(12, 35, 31, 15);
        contentPanel.add(titleLabel);

        titleTextField = new JTextField();
        titleTextField.setColumns(255);
        titleTextField.setBounds(65, 32, 185, 21);
        contentPanel.add(titleTextField);

        JLabel authorLabel = new JLabel("저자");
        authorLabel.setBounds(12, 60, 31, 15);
        contentPanel.add(authorLabel);

        authorTextField = new JTextField();
        authorTextField.setBounds(65, 57, 185, 21);
        contentPanel.add(authorTextField);
        authorTextField.setColumns(10);

        JLabel publisherLabel = new JLabel("출판사");
        publisherLabel.setBounds(12, 85, 42, 15);
        contentPanel.add(publisherLabel);

        JComboBox publisherComboBox = new JComboBox();
        publisherComboBox.setBounds(65, 81, 185, 23);
        contentPanel.add(publisherComboBox);

        JLabel categoryLabel = new JLabel("카테고리");
        categoryLabel.setBounds(12, 110, 48, 15);
        contentPanel.add(categoryLabel);

        JComboBox categoryComboBox = new JComboBox();
        categoryComboBox.setBounds(65, 106, 185, 23);
        contentPanel.add(categoryComboBox);

        JButton addButton = new JButton("추가");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        addButton.setBounds(85, 148, 97, 23);
        contentPanel.add(addButton);

        setLocationRelativeTo(null);
    }

    private List<String> getBookInfoSuggestions(String query) {
        List<String> suggestions = new ArrayList<>();
        try {
            SQLTask task = new SQLTask(TaskType.QUERY, "SELECT title FROM BookInfo WHERE title LIKE ?", query + "%");
            ResultSet result = (ResultSet) LibrarySystem.getInstance().getDatabaseManager().getTaskProcessor().submitTask(task).get();
            while (result.next()) {
                suggestions.add(result.getString("title"));
            }
        } catch (SQLException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return suggestions;
    }
    private List<String> getCategorySuggestions(String query) {
        List<String> suggestions = new ArrayList<>();
        try {
            SQLTask task = new SQLTask(TaskType.QUERY, "SELECT title FROM BookInfo WHERE title LIKE ?", query + "%");
            ResultSet result = (ResultSet) LibrarySystem.getInstance().getDatabaseManager().getTaskProcessor().submitTask(task).get();
            while (result.next()) {
                suggestions.add(result.getString("title"));
            }
        } catch (SQLException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return suggestions;
    }
}
