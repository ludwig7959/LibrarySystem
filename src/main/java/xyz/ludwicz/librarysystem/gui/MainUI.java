package xyz.ludwicz.librarysystem.gui;

import org.hibernate.Session;
import xyz.ludwicz.librarysystem.LibrarySystem;
import xyz.ludwicz.librarysystem.data.Book;
import xyz.ludwicz.librarysystem.data.BookInfo;
import xyz.ludwicz.librarysystem.data.Category;
import xyz.ludwicz.librarysystem.data.Member;
import xyz.ludwicz.librarysystem.data.Publisher;
import xyz.ludwicz.librarysystem.database.SQLTask;
import xyz.ludwicz.librarysystem.database.SQLTask.TaskType;
import xyz.ludwicz.librarysystem.gui.models.MapTableModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class MainUI extends JFrame {

    private JPanel contentPane;
    private JComboBox<BookSearchOption> bookSearchOptionComboBox;
    private JTextField bookSearchTextField;
    private JTable bookListTable;
    private MapTableModel<Integer> bookListModel;
    private JComboBox<Category> categoryPickComboBox;
    private JTable memberListTable;
    private MapTableModel<String> memberListModel;
    private JComboBox<MemberSearchOption> memberSearchOptionComboBox;
    private JTextField memberSearchTextField;
    private JComboBox<PublisherSearchOption> publisherSearchOptionComboBox;
    private JTextField publisherSearchTextField;
    private JTable publisherListTable;
    private MapTableModel<Integer> publisherListModel;
    private JTextField categorySearchTextField;
    private JTable categoryListTable;
    private MapTableModel<Integer> categoryListModel;

    private BookAddDialog bookAddDialog = null;
    private JDialog childDialog = null;


    public MainUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setBounds(100, 100, 650, 400);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                if (childDialog == null || !childDialog.isDisplayable())
                    return;

                childDialog.requestFocus();
            }
        });

        setContentPane(contentPane);
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        contentPane.add(tabbedPane);

        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);

        JPanel bookPanel = new JPanel();
        tabbedPane.addTab("도서", null, bookPanel, null);
        bookPanel.setLayout(null);

        JButton bookSearchButton = new JButton("검색");
        bookSearchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchBook();
            }
        });

        JButton modifyBookButton = new JButton("수정");
        modifyBookButton.setActionCommand("수정");
        modifyBookButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (childDialog != null && childDialog.isDisplayable()) {
                    return;
                }

                int row = bookListTable.getSelectedRow();
                if (row < 0)
                    return;

                Integer bookId = bookListModel.getKeyAtRow(row);

                Session session = LibrarySystem.getInstance().getDatabaseManager().getSessionFactory().openSession();
                Book book = session.get(Book.class, bookId);
                session.close();

                childDialog = new BookModifyDialog(book);
                childDialog.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        refreshBookTab();
                    }
                });

                childDialog.setVisible(true);
            }
        });

        JLabel bookListLabel = new JLabel("도서 목록");
        bookListLabel.setBounds(12, 10, 57, 15);
        bookPanel.add(bookListLabel);

        JButton bookCheckoutInfoButton = new JButton("대출정보");
        bookCheckoutInfoButton.setBounds(231, 6, 85, 23);
        bookPanel.add(bookCheckoutInfoButton);
        modifyBookButton.setBounds(328, 6, 85, 23);
        bookPanel.add(modifyBookButton);

        JButton addBookButton = new JButton("추가");
        addBookButton.setBounds(425, 6, 85, 23);
        addBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (childDialog != null && childDialog.isDisplayable()) {
                    return;
                }
                childDialog = new BookAddDialog();
                childDialog.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        refreshBookTab();
                    }
                });
                childDialog.setVisible(true);
            }
        });
        bookPanel.add(addBookButton);

        JButton removeBookButton = new JButton("제거");
        removeBookButton.setBounds(522, 6, 85, 23);
        removeBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = bookListTable.getSelectedRow();
                if (row < 0) {
                    return;
                }

                int id = bookListModel.getKeyAtRow(row);
                SQLTask deleteTask = new SQLTask(TaskType.UPDATE, "DELETE FROM Book WHERE bookId = ?", id);
                try {
                    boolean deleteResult = (Boolean) LibrarySystem.getInstance().getDatabaseManager().getTaskProcessor().submitTask(deleteTask).get();
                    if (deleteResult) {
                        refreshBookTab();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        bookPanel.add(removeBookButton);

        JScrollPane bookListScrollPane = new JScrollPane();
        bookListScrollPane.setBounds(12, 35, 595, 245);
        bookPanel.add(bookListScrollPane);

        bookListTable = new JTable();
        bookListModel = new MapTableModel<>(
                new String[]{
                        "도서 번호", "제목", "저자", "출판사", "상태"
                }
        ) {
            Class[] columnTypes = new Class[]{
                    Integer.class, String.class, String.class, String.class, String.class
            };

            public Class getColumnClass(int columnIndex) {
                return columnTypes[columnIndex];
            }

            boolean[] columnEditables = new boolean[]{
                    false, false, false, false, false
            };

            public boolean isCellEditable(int row, int column) {
                return columnEditables[column];
            }
        };
        bookListTable.setModel(bookListModel);
        for (int i = 0; i < bookListTable.getColumnCount(); i++) {
            bookListTable.getColumnModel().getColumn(i).setResizable(false);
            bookListTable.getColumnModel().getColumn(i).setCellRenderer(leftRenderer);
        }
        bookListTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        bookListTable.getColumnModel().getColumn(1).setPreferredWidth(250);
        bookListTable.getColumnModel().getColumn(4).setPreferredWidth(50);
        bookListScrollPane.setViewportView(bookListTable);

        bookSearchOptionComboBox = new JComboBox<>(BookSearchOption.values());
        bookSearchOptionComboBox.setBounds(12, 289, 80, 23);
        bookPanel.add(bookSearchOptionComboBox);

        bookSearchTextField = new JTextField();
        bookSearchTextField.setBounds(104, 290, 237, 21);
        bookPanel.add(bookSearchTextField);
        bookSearchTextField.setColumns(10);
        bookSearchButton.setBounds(353, 289, 70, 23);
        bookPanel.add(bookSearchButton);

        JLabel bookCategoryPickLabel = new JLabel("카테고리");
        bookCategoryPickLabel.setBounds(438, 293, 57, 15);
        bookPanel.add(bookCategoryPickLabel);

        Session session = LibrarySystem.getInstance().getDatabaseManager().getSessionFactory().openSession();
        List<Category> categories = session.createQuery("FROM Category", Category.class).list();
        session.close();
        categories.add(0, Category.ALL);
        categories.add(Category.ETC);

        categoryPickComboBox = new JComboBox<>(categories.toArray(new Category[0]));
        categoryPickComboBox.setBounds(507, 289, 100, 23);
        categoryPickComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    refreshBookTab();
                }
            }
        });
        bookPanel.add(categoryPickComboBox);

        JPanel memberPanel = new JPanel();
        tabbedPane.addTab("회원", null, memberPanel, null);
        memberPanel.setLayout(null);

        JButton removeMemberButton = new JButton("제거");
        removeMemberButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int row = memberListTable.getSelectedRow();
                if (row < 0) {
                    return;
                }

                String id = memberListModel.getKeyAtRow(row);
                SQLTask deleteTask = new SQLTask(TaskType.UPDATE, "DELETE FROM Member WHERE memberId = ?", id);
                try {
                    boolean deleteResult = (Boolean) LibrarySystem.getInstance().getDatabaseManager().getTaskProcessor().submitTask(deleteTask).get();
                    if (deleteResult) {
                        refreshMemberTab();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        JButton modifyMemberButton = new JButton("수정");
        modifyMemberButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (childDialog != null && childDialog.isDisplayable()) {
                    return;
                }

                int row = memberListTable.getSelectedRow();
                if (row < 0)
                    return;

                String memberId = memberListModel.getKeyAtRow(row);

                Session session = LibrarySystem.getInstance().getDatabaseManager().getSessionFactory().openSession();
                Member member = session.get(Member.class, memberId);
                session.close();

                childDialog = new MemberModifyDialog(member);
                childDialog.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        refreshMemberTab();
                    }
                });
                childDialog.setVisible(true);
            }
        });

        JLabel memberListLabel = new JLabel("회원 목록");
        memberListLabel.setBounds(12, 10, 57, 15);
        memberPanel.add(memberListLabel);

        JButton memberCheckoutInfoButton = new JButton("대출목록");
        memberCheckoutInfoButton.setBounds(231, 6, 85, 23);
        memberPanel.add(memberCheckoutInfoButton);
        modifyMemberButton.setBounds(328, 6, 85, 23);
        memberPanel.add(modifyMemberButton);

        JButton addMemberButton = new JButton("추가");
        addMemberButton.setBounds(425, 6, 85, 23);
        addMemberButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (childDialog != null && childDialog.isDisplayable()) {
                    return;
                }
                childDialog = new MemberAddDialog();
                childDialog.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        refreshMemberTab();
                    }
                });
                childDialog.setVisible(true);
            }
        });
        memberPanel.add(addMemberButton);

        removeMemberButton.setBounds(522, 6, 85, 23);
        memberPanel.add(removeMemberButton);

        JScrollPane memberListScrollPane = new JScrollPane();
        memberListScrollPane.setBounds(12, 35, 595, 244);
        memberPanel.add(memberListScrollPane);

        memberListTable = new JTable();
        memberListModel = new MapTableModel<>(
                new String[]{
                        "타입", "학번/사번", "이름", "전화번호"
                }
        ) {
            Class[] columnTypes = new Class[]{
                    String.class, String.class, String.class, String.class
            };

            public Class getColumnClass(int columnIndex) {
                return columnTypes[columnIndex];
            }

            boolean[] columnEditables = new boolean[]{
                    false, false, false, false
            };

            public boolean isCellEditable(int row, int column) {
                return columnEditables[column];
            }
        };
        for (int i = 0; i < memberListTable.getColumnCount(); i++) {
            memberListTable.getColumnModel().getColumn(i).setResizable(false);
            memberListTable.getColumnModel().getColumn(i).setCellRenderer(leftRenderer);
        }
        memberListTable.setModel(memberListModel);
        memberListTable.getColumnModel().getColumn(0).setPreferredWidth(25);
        memberListTable.getColumnModel().getColumn(1).setPreferredWidth(120);
        memberListTable.getColumnModel().getColumn(2).setPreferredWidth(120);
        memberListTable.getColumnModel().getColumn(3).setPreferredWidth(120);
        memberListTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        memberListScrollPane.setViewportView(memberListTable);

        memberSearchOptionComboBox = new JComboBox<>(MemberSearchOption.values());
        memberSearchOptionComboBox.setBounds(12, 289, 100, 23);
        memberPanel.add(memberSearchOptionComboBox);

        memberSearchTextField = new JTextField();
        memberSearchTextField.setColumns(10);
        memberSearchTextField.setBounds(124, 290, 371, 21);
        memberPanel.add(memberSearchTextField);

        JButton memberSearchButton = new JButton("검색");
        memberSearchButton.setBounds(507, 289, 100, 23);
        memberSearchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchMember();
            }
        });
        memberPanel.add(memberSearchButton);

        JPanel publisherPanel = new JPanel();
        tabbedPane.addTab("출판사", null, publisherPanel, null);
        publisherPanel.setLayout(null);

        JLabel publisherListLabel = new JLabel("출판사 목록");
        publisherListLabel.setBounds(12, 10, 85, 15);
        publisherPanel.add(publisherListLabel);

        JButton modifyPublisherButton = new JButton("수정");
        modifyPublisherButton.setBounds(328, 6, 85, 23);
        modifyPublisherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (childDialog != null && childDialog.isDisplayable()) {
                    return;
                }

                int row = publisherListTable.getSelectedRow();
                if (row < 0)
                    return;

                int publisherId = publisherListModel.getKeyAtRow(row);

                Session session = LibrarySystem.getInstance().getDatabaseManager().getSessionFactory().openSession();
                Publisher publisher = session.get(Publisher.class, publisherId);
                session.close();

                childDialog = new PublisherModifyDialog(publisher);
                childDialog.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        refreshPublisherTab();
                    }
                });
                childDialog.setVisible(true);
            }
        });
        publisherPanel.add(modifyPublisherButton);

        JButton addPublisherButton = new JButton("추가");
        addPublisherButton.setBounds(425, 6, 85, 23);
        addPublisherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (childDialog != null && childDialog.isDisplayable()) {
                    return;
                }
                childDialog = new PublisherAddDialog();
                childDialog.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        refreshPublisherTab();
                    }
                });
                childDialog.setVisible(true);
            }
        });
        publisherPanel.add(addPublisherButton);

        JButton removePublisherButton = new JButton("제거");
        removePublisherButton.setBounds(522, 6, 85, 23);
        removePublisherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = publisherListTable.getSelectedRow();
                if (row < 0) {
                    return;
                }

                int id = publisherListModel.getKeyAtRow(row);
                SQLTask deleteTask = new SQLTask(TaskType.UPDATE, "DELETE FROM Publisher WHERE publisherId = ?", id);
                try {
                    boolean deleteResult = (Boolean) LibrarySystem.getInstance().getDatabaseManager().getTaskProcessor().submitTask(deleteTask).get();
                    if (deleteResult) {
                        refreshPublisherTab();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        publisherPanel.add(removePublisherButton);

        JScrollPane publisherListScrollPane = new JScrollPane();
        publisherListScrollPane.setBounds(12, 35, 595, 244);
        publisherPanel.add(publisherListScrollPane);

        publisherListTable = new JTable();
        publisherListModel = new MapTableModel<>(
                new String[]{
                        "출판사 번호", "이름", "주소", "도서 수"
                }
        ) {
            Class[] columnTypes = new Class[]{
                    Integer.class, String.class, String.class, Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return columnTypes[columnIndex];
            }

            boolean[] columnEditables = new boolean[]{
                    false, false, false, false
            };

            public boolean isCellEditable(int row, int column) {
                return columnEditables[column];
            }
        };
        publisherListTable.setModel(publisherListModel);
        for (int i = 0; i < publisherListTable.getColumnCount(); i++) {
            publisherListTable.getColumnModel().getColumn(i).setResizable(false);
            publisherListTable.getColumnModel().getColumn(i).setCellRenderer(leftRenderer);
        }
        publisherListTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        publisherListTable.getColumnModel().getColumn(1).setPreferredWidth(50);
        publisherListTable.getColumnModel().getColumn(2).setPreferredWidth(250);
        publisherListTable.getColumnModel().getColumn(3).setPreferredWidth(50);
        publisherListTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        publisherListScrollPane.setViewportView(publisherListTable);

        publisherSearchOptionComboBox = new JComboBox<>(PublisherSearchOption.values());
        publisherSearchOptionComboBox.setBounds(12, 289, 100, 23);
        publisherPanel.add(publisherSearchOptionComboBox);

        publisherSearchTextField = new JTextField();
        publisherSearchTextField.setColumns(10);
        publisherSearchTextField.setBounds(124, 290, 371, 21);
        publisherPanel.add(publisherSearchTextField);

        JButton publisherSearchButton = new JButton("검색");
        publisherSearchButton.setBounds(507, 289, 100, 23);
        publisherSearchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchPublisher();
            }
        });
        publisherPanel.add(publisherSearchButton);

        JPanel categoryPanel = new JPanel();
        tabbedPane.addTab("카테고리", null, categoryPanel, null);
        categoryPanel.setLayout(null);

        JLabel categoryListLabel = new JLabel("카테고리 목록");
        categoryListLabel.setBounds(12, 10, 100, 15);
        categoryPanel.add(categoryListLabel);

        JButton modifyCategoryButton = new JButton("수정");
        modifyCategoryButton.setBounds(328, 6, 85, 23);
        modifyCategoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (childDialog != null && childDialog.isDisplayable()) {
                    return;
                }

                int row = categoryListTable.getSelectedRow();
                if (row < 0)
                    return;

                int categoryId = categoryListModel.getKeyAtRow(row);

                Session session = LibrarySystem.getInstance().getDatabaseManager().getSessionFactory().openSession();
                Category category = session.get(Category.class, categoryId);
                session.close();

                childDialog = new CategoryModifyDialog(category);
                childDialog.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        refreshCategoryTab();
                    }
                });

                childDialog.setVisible(true);
            }
        });
        categoryPanel.add(modifyCategoryButton);

        JButton addCategoryButton = new JButton("추가");
        addCategoryButton.setBounds(425, 6, 85, 23);
        addCategoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (childDialog != null && childDialog.isDisplayable()) {
                    return;
                }
                childDialog = new CategoryAddDialog();
                childDialog.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        refreshCategoryTab();
                    }
                });
                childDialog.setVisible(true);
            }
        });
        categoryPanel.add(addCategoryButton);

        JButton removeCategoryButton = new JButton("제거");
        removeCategoryButton.setBounds(522, 6, 85, 23);
        removeCategoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = categoryListTable.getSelectedRow();
                if (row < 0) {
                    return;
                }

                int id = categoryListModel.getKeyAtRow(row);
                SQLTask deleteTask = new SQLTask(TaskType.UPDATE, "DELETE FROM Category WHERE categoryId = ?", id);
                try {
                    boolean deleteResult = (Boolean) LibrarySystem.getInstance().getDatabaseManager().getTaskProcessor().submitTask(deleteTask).get();
                    if (deleteResult) {
                        refreshCategoryTab();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        categoryPanel.add(removeCategoryButton);

        JScrollPane categoryListScrollPane = new JScrollPane();
        categoryListScrollPane.setBounds(12, 35, 595, 277);
        categoryPanel.add(categoryListScrollPane);

        categoryListTable = new JTable();
        categoryListModel = new MapTableModel<>(
                new String[]{
                        "카테고리 번호", "이름", "도서 수"
                }
        ) {
            Class[] columnTypes = new Class[]{
                    Integer.class, String.class, Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return columnTypes[columnIndex];
            }

            boolean[] columnEditables = new boolean[]{
                    false, false, false
            };

            public boolean isCellEditable(int row, int column) {
                return columnEditables[column];
            }
        };
        categoryListTable.setModel(categoryListModel);
        for (int i = 0; i < categoryListTable.getColumnCount(); i++) {
            categoryListTable.getColumnModel().getColumn(i).setResizable(false);
            categoryListTable.getColumnModel().getColumn(i).setCellRenderer(leftRenderer);
        }
        categoryListTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        categoryListScrollPane.setViewportView(categoryListTable);

        refreshBookTab();
        refreshMemberTab();
        refreshPublisherTab();
        refreshCategoryTab();

        setLocationRelativeTo(null);
    }

    private void refreshBookTab() {
        Session session = LibrarySystem.getInstance().getDatabaseManager().getSessionFactory().openSession();
        Category category = (Category) categoryPickComboBox.getSelectedItem();
        String query = "FROM Book ORDER BY bookId";
        // "(SELECT COUNT(*) FROM Book b JOIN BookInfo bi ON b.bookInfoId = bi.bookInfoId WHERE bi.categoryId = categoryId)"
        if (category.getCategoryId() != 0) {
            if (category.getCategoryId() < 0) {
                query = "SELECT b FROM Book b JOIN b.bookInfo bi ON b.bookInfoId = bi.bookInfoId WHERE bi.category IS NULL";
            } else {
                query = "SELECT b FROM Book b JOIN b.bookInfo bi WHERE bi.category = " + category.getCategoryId();
            }
        }

        Map<Integer, Book> books = new TreeMap<>();
        for (Book book : session.createQuery(query, Book.class).list()) {
            books.put(book.getBookId(), book);
        }

        List<Integer> keysToRemove = new ArrayList<>();
        Iterator<Integer> keyIterator = bookListModel.keySet().iterator();
        while (keyIterator.hasNext()) {
            int key = keyIterator.next();
            if (!books.containsKey(key)) {
                keysToRemove.add(key);
            }
        }

        for (Integer key : keysToRemove) {
            bookListModel.removeRow(key);
        }

        for (Entry<Integer, Book> entry : books.entrySet()) {
            BookInfo bookInfo = entry.getValue().getBookInfo();
            String status = "보관 중";
            if (entry.getValue().isCheckedOut()) {
                if (entry.getValue().isOverdue()) {
                    status = "연체";
                } else {
                    status = "대출 중";
                }
            }
            bookListModel.setRow(entry.getKey(), new Object[]{entry.getKey(), bookInfo.getTitle(), bookInfo.getAuthor(), bookInfo.getPublisher().getName(), status});
        }

        bookListModel.fireTableDataChanged();
        session.close();
    }

    private void searchBook() {
        Session session = LibrarySystem.getInstance().getDatabaseManager().getSessionFactory().openSession();

        String searchKeyword = bookSearchTextField.getText();
        if (searchKeyword == null || searchKeyword.isEmpty()) {
            refreshBookTab();
            return;
        }

        Map<Integer, Book> books = new TreeMap<>();
        boolean isInteger = false;
        int intSearchKeyword = 0;
        String query = "";
        switch ((BookSearchOption) bookSearchOptionComboBox.getSelectedItem()) {
            case ID:
                try {
                    intSearchKeyword = Integer.parseInt(searchKeyword);
                } catch (NumberFormatException e) {
                    return;
                }
                query = "SELECT b FROM Book b WHERE b.bookId = :searchKeyword";
                isInteger = true;
                break;
            case TITLE:
                query = "SELECT b FROM Book b JOIN b.bookInfo bi WHERE bi.title LIKE :searchKeyword";
                break;
            case AUTHOR:
                query = "SELECT b FROM Book b JOIN b.bookInfo bi WHERE bi.author LIKE :searchKeyword";
                break;
            case PUBLISHER:
                query = "SELECT b FROM Book b JOIN b.bookInfo bi WHERE bi.publisher.name LIKE :searchKeyword";
                break;
            default:
                throw new IllegalArgumentException("Invalid search option");
        }

        Category category = (Category) categoryPickComboBox.getSelectedItem();
        if (category.getCategoryId() != 0) {
            if (category.getCategoryId() < 0) {
                query += " AND bi.category IS NULL";
            } else {
                query += " AND bi.category.categoryId = :categoryId";
            }
        }

        for (Book book : session.createQuery(query, Book.class).setParameter("searchKeyword", isInteger ? intSearchKeyword : "%" + searchKeyword + "%").list()) {
            books.put(book.getBookId(), book);
        }

        List<Integer> keysToRemove = new ArrayList<>();
        Iterator<Integer> keyIterator = bookListModel.keySet().iterator();
        while (keyIterator.hasNext()) {
            Integer key = keyIterator.next();
            if (!books.containsKey(key)) {
                keysToRemove.add(key);
            }
        }

        for (int key : keysToRemove) {
            bookListModel.removeRow(key);
        }

        for (Entry<Integer, Book> entry : books.entrySet()) {
            BookInfo bookInfo = entry.getValue().getBookInfo();
            String status = "보관 중";
            if (entry.getValue().isCheckedOut()) {
                if (entry.getValue().isOverdue()) {
                    status = "연체";
                } else {
                    status = "대출 중";
                }
            }
            bookListModel.setRow(entry.getKey(), new Object[]{entry.getKey(), bookInfo.getTitle(), bookInfo.getAuthor(), bookInfo.getPublisher().getName(), status});
        }

        bookListModel.fireTableDataChanged();
        session.close();
    }

    private void refreshMemberTab() {
        Session session = LibrarySystem.getInstance().getDatabaseManager().getSessionFactory().openSession();

        Map<String, Member> members = new TreeMap<>();
        for (Member member : session.createQuery("FROM Member ORDER BY name", Member.class).list()) {
            members.put(member.getMemberId(), member);
        }

        List<String> keysToRemove = new ArrayList<>();
        Iterator<String> keyIterator = memberListModel.keySet().iterator();
        while (keyIterator.hasNext()) {
            String key = keyIterator.next();
            if (!members.containsKey(key)) {
                keysToRemove.add(key);
            }
        }

        for (String key : keysToRemove) {
            memberListModel.removeRow(key);
        }

        for (Entry<String, Member> entry : members.entrySet()) {
            memberListModel.setRow(entry.getKey(), new Object[]{entry.getValue().getType().equals("STUDENT") ? "학생" : "교수", entry.getKey(), entry.getValue().getName(), entry.getValue().getPhone()});
        }

        memberListModel.fireTableDataChanged();
        session.close();
    }

    private void searchMember() {
        Session session = LibrarySystem.getInstance().getDatabaseManager().getSessionFactory().openSession();

        String searchKeyword = memberSearchTextField.getText();
        if (searchKeyword == null || searchKeyword.isEmpty()) {
            refreshMemberTab();
            return;
        }

        Map<String, Member> members = new TreeMap<>();
        String searchAttribute = "";
        switch ((MemberSearchOption) memberSearchOptionComboBox.getSelectedItem()) {
            case ID:
                searchAttribute = "memberId";
                break;
            case NAME:
                searchAttribute = "name";
                break;
            case PHONE:
                searchAttribute = "phone";
                break;
            default:
                break;
        }

        for (Member member : session.createQuery("FROM Member WHERE " + searchAttribute + " LIKE :searchKeyword ORDER BY name", Member.class).setParameter("searchKeyword", "%" + searchKeyword + "%").list()) {
            members.put(member.getMemberId(), member);
        }

        List<String> keysToRemove = new ArrayList<>();
        Iterator<String> keyIterator = memberListModel.keySet().iterator();
        while (keyIterator.hasNext()) {
            String key = keyIterator.next();
            if (!members.containsKey(key)) {
                keysToRemove.add(key);
            }
        }

        for (String key : keysToRemove) {
            memberListModel.removeRow(key);
        }

        for (Entry<String, Member> entry : members.entrySet()) {
            memberListModel.setRow(entry.getKey(), new Object[]{entry.getValue().getType().equals("STUDENT") ? "학생" : "교수", entry.getKey(), entry.getValue().getName(), entry.getValue().getPhone()});
        }

        memberListModel.fireTableDataChanged();
        session.close();
    }

    private void refreshPublisherTab() {
        Session session = LibrarySystem.getInstance().getDatabaseManager().getSessionFactory().openSession();

        Map<Integer, Publisher> publishers = new TreeMap<>();
        for (Publisher publisher : session.createQuery("FROM Publisher ORDER BY name", Publisher.class).list()) {
            publishers.put(publisher.getPublisherId(), publisher);
        }

        List<Integer> keysToRemove = new ArrayList<>();
        Iterator<Integer> keyIterator = publisherListModel.keySet().iterator();
        while (keyIterator.hasNext()) {
            int key = keyIterator.next();
            if (!publishers.containsKey(key)) {
                keysToRemove.add(key);
            }
        }

        for (int key : keysToRemove) {
            publisherListModel.removeRow(key);
        }

        for (Entry<Integer, Publisher> entry : publishers.entrySet()) {
            publisherListModel.setRow(entry.getKey(), new Object[]{entry.getKey(), entry.getValue().getName(), entry.getValue().getAddress(), entry.getValue().getBookCount()});
        }

        publisherListModel.fireTableDataChanged();
        session.close();
    }

    private void searchPublisher() {
        Session session = LibrarySystem.getInstance().getDatabaseManager().getSessionFactory().openSession();

        String searchKeyword = publisherSearchTextField.getText();
        if (searchKeyword == null || searchKeyword.isEmpty()) {
            refreshPublisherTab();
            return;
        }

        Map<Integer, Publisher> publishers = new TreeMap<>();
        String searchAttribute = "";
        String query = "";
        boolean isInteger;
        switch ((PublisherSearchOption) publisherSearchOptionComboBox.getSelectedItem()) {
            case ID:
                searchAttribute = "publisherId";
                isInteger = true;
                break;
            case NAME:
                searchAttribute = "name";
                isInteger = false;
                break;
            case ADDRESS:
                searchAttribute = "address";
                isInteger = false;
                break;
            default:
                isInteger = false;
                break;
        }
        int parsedKeyword = 0;
        if (isInteger) {
            try {
                parsedKeyword = Integer.parseInt(searchKeyword);
            } catch (NumberFormatException e) {
                return;
            }
            query = "FROM Publisher WHERE " + searchAttribute + " = :searchKeyword ORDER BY name";
        } else {
            query = "FROM Publisher WHERE " + searchAttribute + " LIKE :searchKeyword ORDER BY name";
        }

        for (Publisher publisher : session.createQuery(query, Publisher.class).setParameter("searchKeyword", isInteger ? parsedKeyword : "%" + searchKeyword + "%").list()) {
            publishers.put(publisher.getPublisherId(), publisher);
        }

        List<Integer> keysToRemove = new ArrayList<>();
        Iterator<Integer> keyIterator = publisherListModel.keySet().iterator();
        while (keyIterator.hasNext()) {
            int key = keyIterator.next();
            if (!publishers.containsKey(key)) {
                keysToRemove.add(key);
            }
        }

        for (int key : keysToRemove) {
            publisherListModel.removeRow(key);
        }

        for (Entry<Integer, Publisher> entry : publishers.entrySet()) {
            publisherListModel.setRow(entry.getKey(), new Object[]{entry.getKey(), entry.getValue().getName(), entry.getValue().getAddress(), entry.getValue().getBookCount()});
        }

        publisherListModel.fireTableDataChanged();
        session.close();
    }

    private void refreshCategoryTab() {
        Session session = LibrarySystem.getInstance().getDatabaseManager().getSessionFactory().openSession();

        Map<Integer, Category> categories = new TreeMap<>();
        for (Category category : session.createQuery("FROM Category ORDER BY categoryId", Category.class).list()) {
            categories.put(category.getCategoryId(), category);
        }

        List<Integer> keysToRemove = new ArrayList<>();
        Iterator<Integer> keyIterator = categoryListModel.keySet().iterator();
        while (keyIterator.hasNext()) {
            int key = keyIterator.next();
            if (!categories.containsKey(key)) {
                keysToRemove.add(key);
            }
        }

        for (int key : keysToRemove) {
            categoryListModel.removeRow(key);
        }

        for (Entry<Integer, Category> entry : categories.entrySet()) {
            categoryListModel.setRow(entry.getKey(), new Object[]{entry.getKey(), entry.getValue().getName(), entry.getValue().getBookCount()});
        }

        categoryListModel.fireTableDataChanged();
        session.close();
    }

    enum MemberSearchOption {
        ID("학번/사번"),
        NAME("이름"),
        PHONE("전화번호");

        private String displayName;

        MemberSearchOption(String displayName) {
            this.displayName = displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

    enum BookSearchOption {
        TITLE("제목"),
        ID("도서번호"),
        AUTHOR("저자"),
        PUBLISHER("출판사");

        private String displayName;

        BookSearchOption(String displayName) {
            this.displayName = displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

    enum PublisherSearchOption {
        NAME("이름"),
        ID("번호"),
        ADDRESS("주소");

        private String displayName;

        PublisherSearchOption(String displayName) {
            this.displayName = displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }
}
