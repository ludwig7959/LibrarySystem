package xyz.ludwicz.librarysystem.gui;

import javax.swing.*;
import java.awt.*;

public class GUIManager {
    private JFrame mainFrame;
    private JPanel mainPanel;
    private CardLayout cardLayout;

    public GUIManager() {
        mainFrame = new JFrame("Library System");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setResizable(false);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(new LoginPage(), "Login");

        mainFrame.add(mainPanel);
        mainPanel.setSize(900, 300);
        mainFrame.pack();
    }

    public void show() {
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);

        cardLayout.show(mainPanel, "Login");
    }
}
