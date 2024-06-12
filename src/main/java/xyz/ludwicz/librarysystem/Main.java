package xyz.ludwicz.librarysystem;

import xyz.ludwicz.librarysystem.database.DatabaseManager;
import xyz.ludwicz.librarysystem.gui.LoginUI;

import javax.swing.*;

public class Main {


    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            DatabaseManager.getInstance();
            LoginUI loginUI = new LoginUI();
            loginUI.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
