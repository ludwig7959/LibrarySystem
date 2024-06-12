package xyz.ludwicz.librarysystem.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainUI extends JFrame {

    private JPanel contentPane;
    private JTextField bookSearchTextField;
    private JTable bookListTable;
    private JTable memberListTable;
    private JTextField memberSearchTextField;
    private JTextField publisherSearchTextField;
    private JTable publisherListTable;
    private JTextField categorySearchTextField;
    private JTable categoryListTable;

    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MainUI frame = new MainUI();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public MainUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 650, 400);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        contentPane.add(tabbedPane);

        JPanel bookPanel = new JPanel();
        tabbedPane.addTab("도서", null, bookPanel, null);
        bookPanel.setLayout(null);

        JButton bookSearchButton = new JButton("검색");
        bookSearchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });

        JButton modifyBookButton = new JButton("수정");
        modifyBookButton.setActionCommand("수정");
        modifyBookButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
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

        JButton bookAddButton = new JButton("추가");
        bookAddButton.setBounds(425, 6, 85, 23);
        bookPanel.add(bookAddButton);

        JButton removeBookButton = new JButton("제거");
        removeBookButton.setActionCommand("수정");
        removeBookButton.setBounds(522, 6, 85, 23);
        bookPanel.add(removeBookButton);

        JScrollPane bookListScrollPane = new JScrollPane();
        bookListScrollPane.setBounds(12, 35, 595, 245);
        bookPanel.add(bookListScrollPane);

        bookListTable = new JTable();
        bookListTable.setModel(new DefaultTableModel(
                new Object[][]{
                },
                new String[]{
                        "\uB3C4\uC11C \uBC88\uD638", "\uC81C\uBAA9", "\uC800\uC790", "\uCD9C\uD310\uC0AC", "\uC0C1\uD0DC"
                }
        ) {
            Class[] columnTypes = new Class[]{
                    Integer.class, String.class, String.class, String.class, String.class
            };

            public Class getColumnClass(int columnIndex) {
                return columnTypes[columnIndex];
            }
        });
        bookListTable.getColumnModel().getColumn(0).setResizable(false);
        bookListTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        bookListTable.getColumnModel().getColumn(1).setResizable(false);
        bookListTable.getColumnModel().getColumn(1).setPreferredWidth(250);
        bookListTable.getColumnModel().getColumn(2).setResizable(false);
        bookListTable.getColumnModel().getColumn(3).setResizable(false);
        bookListTable.getColumnModel().getColumn(4).setResizable(false);
        bookListTable.getColumnModel().getColumn(4).setPreferredWidth(50);
        bookListScrollPane.setViewportView(bookListTable);

        JComboBox bookSearchOptionComboBox = new JComboBox();
        bookSearchOptionComboBox.setBounds(12, 289, 70, 23);
        bookPanel.add(bookSearchOptionComboBox);

        bookSearchTextField = new JTextField();
        bookSearchTextField.setBounds(94, 290, 247, 21);
        bookPanel.add(bookSearchTextField);
        bookSearchTextField.setColumns(10);
        bookSearchButton.setBounds(353, 289, 70, 23);
        bookPanel.add(bookSearchButton);

        JLabel bookCategoryPickLabel = new JLabel("카테고리");
        bookCategoryPickLabel.setBounds(438, 293, 57, 15);
        bookPanel.add(bookCategoryPickLabel);

        JComboBox categoryPickComboBox = new JComboBox();
        categoryPickComboBox.setBounds(507, 289, 100, 23);
        bookPanel.add(categoryPickComboBox);

        JPanel memberPanel = new JPanel();
        tabbedPane.addTab("회원", null, memberPanel, null);
        memberPanel.setLayout(null);

        JButton removeMemberButton = new JButton("제거");
        removeMemberButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });

        JButton modifyMemberButton = new JButton("수정");
        modifyMemberButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
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
        memberPanel.add(addMemberButton);
        removeMemberButton.setBounds(522, 6, 85, 23);
        memberPanel.add(removeMemberButton);

        JScrollPane memberListScrollPane = new JScrollPane();
        memberListScrollPane.setBounds(12, 35, 595, 244);
        memberPanel.add(memberListScrollPane);

        memberListTable = new JTable();
        memberListTable.setModel(new DefaultTableModel(
                new Object[][]{
                },
                new String[]{
                        "\uD0C0\uC785", "\uD559\uBC88/\uC0AC\uBC88", "\uC774\uB984", "\uC804\uD654\uBC88\uD638"
                }
        ) {
            Class[] columnTypes = new Class[]{
                    String.class, String.class, String.class, String.class
            };

            public Class getColumnClass(int columnIndex) {
                return columnTypes[columnIndex];
            }
        });
        memberListTable.getColumnModel().getColumn(0).setResizable(false);
        memberListTable.getColumnModel().getColumn(0).setPreferredWidth(25);
        memberListTable.getColumnModel().getColumn(1).setResizable(false);
        memberListTable.getColumnModel().getColumn(1).setPreferredWidth(120);
        memberListTable.getColumnModel().getColumn(2).setResizable(false);
        memberListTable.getColumnModel().getColumn(2).setPreferredWidth(120);
        memberListTable.getColumnModel().getColumn(3).setResizable(false);
        memberListTable.getColumnModel().getColumn(3).setPreferredWidth(120);
        memberListScrollPane.setViewportView(memberListTable);

        JComboBox memberSearchOptionComboBox = new JComboBox();
        memberSearchOptionComboBox.setBounds(12, 289, 100, 23);
        memberPanel.add(memberSearchOptionComboBox);

        memberSearchTextField = new JTextField();
        memberSearchTextField.setColumns(10);
        memberSearchTextField.setBounds(124, 290, 371, 21);
        memberPanel.add(memberSearchTextField);

        JButton memberSearchButton = new JButton("검색");
        memberSearchButton.setBounds(507, 289, 100, 23);
        memberPanel.add(memberSearchButton);

        JPanel publisherPanel = new JPanel();
        tabbedPane.addTab("출판사", null, publisherPanel, null);
        publisherPanel.setLayout(null);

        JLabel publisherListLabel = new JLabel("출판사 목록");
        publisherListLabel.setBounds(12, 10, 85, 15);
        publisherPanel.add(publisherListLabel);

        JButton modifyPublisherButton = new JButton("수정");
        modifyPublisherButton.setBounds(328, 6, 85, 23);
        publisherPanel.add(modifyPublisherButton);

        JButton addPublisherButton = new JButton("추가");
        addPublisherButton.setBounds(425, 6, 85, 23);
        publisherPanel.add(addPublisherButton);

        JButton removePublisherButton = new JButton("제거");
        removePublisherButton.setBounds(522, 6, 85, 23);
        publisherPanel.add(removePublisherButton);

        JScrollPane publisherListScrollPane = new JScrollPane();
        publisherListScrollPane.setBounds(12, 35, 595, 244);
        publisherPanel.add(publisherListScrollPane);

        publisherListTable = new JTable();
        publisherListTable.setModel(new DefaultTableModel(
                new Object[][]{
                },
                new String[]{
                        "\uCD9C\uD310\uC0AC \uBC88\uD638", "\uC774\uB984", "\uC8FC\uC18C", "\uB3C4\uC11C \uC218"
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
        });
        publisherListTable.getColumnModel().getColumn(0).setResizable(false);
        publisherListTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        publisherListTable.getColumnModel().getColumn(1).setResizable(false);
        publisherListTable.getColumnModel().getColumn(1).setPreferredWidth(50);
        publisherListTable.getColumnModel().getColumn(2).setResizable(false);
        publisherListTable.getColumnModel().getColumn(2).setPreferredWidth(250);
        publisherListTable.getColumnModel().getColumn(3).setResizable(false);
        publisherListTable.getColumnModel().getColumn(3).setPreferredWidth(50);
        publisherListScrollPane.setViewportView(publisherListTable);

        JComboBox publisherSearchOptionComboBox = new JComboBox();
        publisherSearchOptionComboBox.setBounds(12, 289, 100, 23);
        publisherPanel.add(publisherSearchOptionComboBox);

        publisherSearchTextField = new JTextField();
        publisherSearchTextField.setColumns(10);
        publisherSearchTextField.setBounds(124, 290, 371, 21);
        publisherPanel.add(publisherSearchTextField);

        JButton publisherSearchButton = new JButton("검색");
        publisherSearchButton.setBounds(507, 289, 100, 23);
        publisherPanel.add(publisherSearchButton);

        JPanel categoryPanel = new JPanel();
        tabbedPane.addTab("카테고리", null, categoryPanel, null);
        categoryPanel.setLayout(null);

        JLabel categoryListLabel = new JLabel("카테고리 목록");
        categoryListLabel.setBounds(12, 10, 100, 15);
        categoryPanel.add(categoryListLabel);

        JButton modifyCategoryButton = new JButton("수정");
        modifyCategoryButton.setBounds(328, 6, 85, 23);
        categoryPanel.add(modifyCategoryButton);

        JButton addCategoryButton = new JButton("추가");
        addCategoryButton.setBounds(425, 6, 85, 23);
        categoryPanel.add(addCategoryButton);

        JButton removeCategoryButton = new JButton("제거");
        removeCategoryButton.setBounds(522, 6, 85, 23);
        categoryPanel.add(removeCategoryButton);

        JScrollPane categoryListScrollPane = new JScrollPane();
        categoryListScrollPane.setBounds(12, 35, 595, 244);
        categoryPanel.add(categoryListScrollPane);

        categoryListTable = new JTable();
        categoryListTable.setModel(new DefaultTableModel(
                new Object[][]{
                },
                new String[]{
                        "\uCE74\uD14C\uACE0\uB9AC \uBC88\uD638", "\uC774\uB984", "\uB3C4\uC11C \uC218"
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
        });
        categoryListTable.getColumnModel().getColumn(0).setResizable(false);
        categoryListTable.getColumnModel().getColumn(1).setResizable(false);
        categoryListTable.getColumnModel().getColumn(2).setResizable(false);
        categoryListScrollPane.setViewportView(categoryListTable);

        JComboBox categorySearchOptionComboBox = new JComboBox();
        categorySearchOptionComboBox.setBounds(12, 289, 100, 23);
        categoryPanel.add(categorySearchOptionComboBox);

        categorySearchTextField = new JTextField();
        categorySearchTextField.setColumns(10);
        categorySearchTextField.setBounds(124, 290, 371, 21);
        categoryPanel.add(categorySearchTextField);

        JButton categorySearchButton = new JButton("검색");
        categorySearchButton.setBounds(507, 289, 100, 23);
        categoryPanel.add(categorySearchButton);
    }
}
