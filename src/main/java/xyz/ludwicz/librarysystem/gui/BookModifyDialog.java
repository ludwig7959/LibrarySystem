package xyz.ludwicz.librarysystem.gui;

import org.hibernate.Session;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import xyz.ludwicz.librarysystem.LibrarySystem;
import xyz.ludwicz.librarysystem.data.Book;
import xyz.ludwicz.librarysystem.data.BookInfo;
import xyz.ludwicz.librarysystem.data.Category;
import xyz.ludwicz.librarysystem.data.Publisher;
import xyz.ludwicz.librarysystem.database.SQLTask;
import xyz.ludwicz.librarysystem.database.SQLTask.TaskType;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.sql.ResultSet;
import java.util.List;

public class BookModifyDialog extends JDialog {

    private final Book book;

    private final JPanel contentPanel = new JPanel();
    private JComboBox<String> titleComboBox;
    private JTextField authorTextField;

    private JComboBox<Publisher> publisherComboBox;
    private JComboBox<Category> categoryComboBox;

    public BookModifyDialog(Book book) {
        this.book = book;

        setBounds(100, 100, 280, 220);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        Session session = LibrarySystem.getInstance().getDatabaseManager().getSessionFactory().openSession();

        try {
            JLabel bookAddLabel = new JLabel("도서 추가");
            bookAddLabel.setFont(new Font("맑은 고딕", Font.BOLD, 12));
            bookAddLabel.setBounds(12, 10, 78, 15);
            contentPanel.add(bookAddLabel);

            JLabel titleLabel = new JLabel("제목");
            titleLabel.setBounds(12, 35, 31, 15);
            contentPanel.add(titleLabel);

            List<String> titles = session.createQuery("SELECT title FROM  BookInfo").getResultList();
            titleComboBox = new JComboBox<>(titles.toArray(new String[0]));
            titleComboBox.setEditable(true);
            titleComboBox.setBounds(65, 32, 185, 21);
            titleComboBox.setSelectedItem(book.getBookInfo().getTitle());
            contentPanel.add(titleComboBox);

            JLabel authorLabel = new JLabel("저자");
            authorLabel.setBounds(12, 60, 31, 15);
            contentPanel.add(authorLabel);

            authorTextField = new JTextField();
            authorTextField.setBounds(65, 57, 185, 21);
            authorTextField.setColumns(32);
            contentPanel.add(authorTextField);

            JLabel publisherLabel = new JLabel("출판사");
            publisherLabel.setBounds(12, 85, 42, 15);
            contentPanel.add(publisherLabel);

            publisherComboBox = new JComboBox<>(session.createQuery("FROM Publisher", Publisher.class).list().toArray(new Publisher[0]));
            publisherComboBox.setEditable(true);
            publisherComboBox.setBounds(65, 81, 185, 23);
            publisherComboBox.setSelectedItem(book.getBookInfo().getPublisher());
            contentPanel.add(publisherComboBox);

            JLabel categoryLabel = new JLabel("카테고리");
            categoryLabel.setBounds(12, 110, 48, 15);
            contentPanel.add(categoryLabel);

            List<Category> categories = session.createQuery("FROM Category", Category.class).list();
            categories.add(Category.ETC);
            categoryComboBox = new JComboBox<>(categories.toArray(new Category[0]));
            categoryComboBox.setBounds(65, 106, 185, 23);
            categoryComboBox.setSelectedItem(book.getBookInfo().getCategory());
            contentPanel.add(categoryComboBox);

            JButton modifyButton = new JButton("수정");
            modifyButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String title = (String) titleComboBox.getSelectedItem();
                    String author = authorTextField.getText();
                    Object publisherObject = publisherComboBox.getSelectedItem();
                    Object categoryObject = categoryComboBox.getSelectedItem();

                    if (title == null || title.isEmpty()) {
                        JOptionPane.showMessageDialog(BookModifyDialog.this, "책 제목을 입력하세요.", "경고", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    if (author == null || author.isEmpty()) {
                        JOptionPane.showMessageDialog(BookModifyDialog.this, "저자를 입력하세요.", "경고", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    if (publisherObject == null || !(publisherObject instanceof Publisher)) {
                        JOptionPane.showMessageDialog(BookModifyDialog.this, "존재하는 출판사를 입력하세요.", "경고", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    if (categoryObject == null || !(categoryObject instanceof Category)) {
                        JOptionPane.showMessageDialog(BookModifyDialog.this, "존재하는 카테고리를 입력하세요.", "경고", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    Publisher publisher = (Publisher) publisherObject;
                    Category category = (Category) categoryObject;

                    try {
                        int bookInfoId = 0;
                        SQLTask bookInfoTask = new SQLTask(TaskType.QUERY, "SELECT bookInfoId FROM BookInfo WHERE title = ? AND author = ? AND publisherId = ? AND categoryId = ?", title, author, publisher.getPublisherId(), category.getCategoryId() < 0 ? null : category.getCategoryId());
                        ResultSet bookInfoResult = (ResultSet) LibrarySystem.getInstance().getDatabaseManager().getTaskProcessor().submitTask(bookInfoTask).get();
                        if (bookInfoResult.next()) {
                            bookInfoId = bookInfoResult.getInt("bookInfoId");
                        } else {
                            int result = JOptionPane.showConfirmDialog(
                                    BookModifyDialog.this,
                                    "새로운 도서 정보를 추가하시겠습니까?",
                                    "도서 추가",
                                    JOptionPane.YES_NO_OPTION,
                                    JOptionPane.QUESTION_MESSAGE
                            );

                            if (result != JOptionPane.YES_OPTION) {
                                return;
                            }

                            SQLTask newBookInfoTask = new SQLTask(TaskType.UPDATE, "INSERT INTO BookInfo (title, author, publisherId, categoryId) VALUES (?, ?, ?, ?)", title, author, publisher.getPublisherId(), category.getCategoryId() < 0 ? null : category.getCategoryId());
                            if ((Boolean) LibrarySystem.getInstance().getDatabaseManager().getTaskProcessor().submitTask(newBookInfoTask).get()) {
                                SQLTask bookInfoTaskAgain = new SQLTask(TaskType.QUERY, "SELECT bookInfoId FROM BookInfo WHERE title = ? AND author = ? AND publisherId = ? AND categoryId = ?", title, author, publisher.getPublisherId(), category.getCategoryId() < 0 ? null : category.getCategoryId());
                                ResultSet bookInfoResultAgain = (ResultSet) LibrarySystem.getInstance().getDatabaseManager().getTaskProcessor().submitTask(bookInfoTaskAgain).get();
                                if (bookInfoResultAgain.next()) {
                                    bookInfoId = bookInfoResultAgain.getInt("bookInfoId");
                                }
                            }
                        }

                        SQLTask bookAddTask = new SQLTask(TaskType.UPDATE, "UPDATE Book SET bookInfoId = ? WHERE bookId = ?", bookInfoId, book.getBookId());
                        if ((Boolean) LibrarySystem.getInstance().getDatabaseManager().getTaskProcessor().submitTask(bookAddTask).get()) {
                            JOptionPane.showMessageDialog(BookModifyDialog.this, "도서 수정이 완료되었습니다.", "정보", JOptionPane.INFORMATION_MESSAGE);
                            dispose();
                        } else {
                            JOptionPane.showMessageDialog(BookModifyDialog.this, "도서 수정에 실패하였습니다.", "경고", JOptionPane.WARNING_MESSAGE);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
            modifyButton.setBounds(85, 148, 97, 23);
            contentPanel.add(modifyButton);

            setupAutoComplete();

            setLocationRelativeTo(null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    private void setupAutoComplete() {
        AutoCompleteDecorator.decorate(publisherComboBox);
        AutoCompleteDecorator.decorate(titleComboBox);

        titleComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String selectedTitle = (String) titleComboBox.getSelectedItem();
                if (selectedTitle != null && !selectedTitle.isEmpty()) {
                    Session session = LibrarySystem.getInstance().getDatabaseManager().getSessionFactory().openSession();
                    try {
                        SQLTask bookInfoTask = new SQLTask(TaskType.QUERY, "SELECT * FROM BookInfo WHERE title = ?", selectedTitle);
                        ResultSet bookInfoResult = (ResultSet) LibrarySystem.getInstance().getDatabaseManager().getTaskProcessor().submitTask(bookInfoTask).get();
                        if (bookInfoResult.next()) {
                            authorTextField.setText(bookInfoResult.getString("author"));
                            publisherComboBox.setSelectedItem(session.get(Publisher.class, bookInfoResult.getInt("publisherId")));
                            categoryComboBox.setEditable(true);
                            categoryComboBox.setSelectedItem(bookInfoResult.getInt("categoryId") == 0 ? Category.ETC : session.get(Category.class, bookInfoResult.getInt("categoryId")));
                            categoryComboBox.setEditable(false);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    } finally {
                        session.close();
                    }
                }
            }
        });

    }
}
