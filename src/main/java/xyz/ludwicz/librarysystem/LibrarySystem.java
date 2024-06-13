package xyz.ludwicz.librarysystem;

import xyz.ludwicz.librarysystem.database.DatabaseManager;
import xyz.ludwicz.librarysystem.gui.LoginUI;
import xyz.ludwicz.librarysystem.gui.MainUI;

import javax.swing.*;

public class LibrarySystem {

    private static LibrarySystem instance;

    synchronized public static LibrarySystem getInstance() {
        if(instance == null)
            instance = new LibrarySystem();

        return instance;
    }

    private LibrarySystem() {

    }

    private LoginSession session = null;

    private DatabaseManager databaseManager;

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    private LoginUI loginUI;
    private MainUI mainUI;

    public void start() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            databaseManager = new DatabaseManager();
            loginUI = new LoginUI();
            loginUI.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void login(String id, String name, String phone) {
        session = new LoginSession(id, name, phone);

        loginUI.dispose();

        mainUI = new MainUI();
        mainUI.setVisible(true);
    }

    public static void main(String[] args) {
        LibrarySystem.getInstance().start();
    }
}
