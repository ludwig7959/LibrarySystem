package xyz.ludwicz.librarysystem.gui;

import javax.swing.*;
import java.awt.*;

public class GUIManager {
    private JFrame mainFrame;

    public GUIManager() {

        mainFrame = new JFrame("Library System");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setSize(800, 600);

        CardLayout cardLayout = new CardLayout();
        mainFrame.setLayout(cardLayout);
    }

    public void show() {
        mainFrame.setVisible(true);
    }
}
