package xyz.ludwicz.librarysystem.gui;

import javax.swing.*;
import java.awt.*;

public class GUIManager {
    private JFrame mainFrame;
    private JPanel mainPanel;
    private CardLayout cardLayout;

    public GUIManager() {
    }

    public void show() {
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);

        cardLayout.show(mainPanel, "Login");
    }
}
