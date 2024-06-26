package xyz.ludwicz.librarysystem.gui;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import xyz.ludwicz.librarysystem.LibrarySystem;
import xyz.ludwicz.librarysystem.data.Category;
import xyz.ludwicz.librarysystem.database.SQLTask;
import xyz.ludwicz.librarysystem.database.SQLTask.TaskType;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

public class CategoryAddDialog extends JDialog {


    private final JPanel contentPanel = new JPanel();
    private JTextField categoryTextField;

    public CategoryAddDialog() {
        setBounds(100, 100, 154, 142);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        JLabel categoryAddLabel = new JLabel("카테고리 추가");
        categoryAddLabel.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        categoryAddLabel.setBounds(12, 10, 91, 15);
        contentPanel.add(categoryAddLabel);

        categoryTextField = new JTextField();
        categoryTextField.setBounds(12, 35, 114, 21);
        contentPanel.add(categoryTextField);
        categoryTextField.setColumns(16);

        JButton addButton = new JButton("추가");
        addButton.setBounds(22, 70, 97, 23);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String categoryName = categoryTextField.getText();
                if (categoryName == null || categoryName.isEmpty()) {
                    JOptionPane.showMessageDialog(CategoryAddDialog.this, "카테고리 이름을 입력하세요.", "경고", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                /*
                Session session = LibrarySystem.getInstance().getDatabaseManager().getSessionFactory().openSession();
                Transaction transaction = session.beginTransaction();
                */

                try {
                    /*
                    Query<Long> query = session.createQuery("SELECT COUNT(c) FROM Category c WHERE c.name = :name", Long.class);
                    query.setParameter("name", categoryName);
                    Long count = query.uniqueResult();
                    if (count != null && count > 0) {
                        JOptionPane.showMessageDialog(CategoryAddDialog.this, "이미 존재하는 카테고리 이름입니다.", "경고", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    Category category = new Category();
                    category.setName(categoryName);
                    session.save(category);
                    transaction.commit();

                    JOptionPane.showMessageDialog(CategoryAddDialog.this, "카테고리 추가가 완료되었습니다.", "정보", JOptionPane.INFORMATION_MESSAGE);
                    */

                    SQLTask checkTask = new SQLTask(TaskType.QUERY, "SELECT COUNT(*) AS count FROM Category WHERE name = ?", categoryName);
                    ResultSet checkResult = (ResultSet) LibrarySystem.getInstance().getDatabaseManager().getTaskProcessor().submitTask(checkTask).get();
                    if (checkResult.next() && checkResult.getInt("count") > 1) {
                        JOptionPane.showMessageDialog(CategoryAddDialog.this, "이미 존재하는 카테고리 이름입니다.", "경고", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    SQLTask addTask = new SQLTask(TaskType.UPDATE, "INSERT INTO Category (name) VALUES (?)", categoryName);
                    boolean addResult = (Boolean) LibrarySystem.getInstance().getDatabaseManager().getTaskProcessor().submitTask(addTask).get();
                    if (addResult) {
                        JOptionPane.showMessageDialog(CategoryAddDialog.this, "카테고리 추가가 완료되었습니다.", "정보", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(CategoryAddDialog.this, "카테고리 추가에 실패하였습니다.", "경고", JOptionPane.WARNING_MESSAGE);
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
