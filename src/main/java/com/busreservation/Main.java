package com.busreservation;

import java.awt.Color;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.busreservation.database.DBConnection;
import com.busreservation.ui.DashboardFrame;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            try {
                // Use consistent Java Swing rendering.
                UIManager.setLookAndFeel(
                        UIManager.getCrossPlatformLookAndFeelClassName()
                );

                // Global component defaults.
                UIManager.put("TextField.background", Color.WHITE);
                UIManager.put("TextField.foreground", new Color(31, 43, 61));
                UIManager.put("TextField.caretForeground", new Color(45, 105, 220));
                UIManager.put("TextField.selectionBackground", new Color(45, 105, 220));
                UIManager.put("TextField.selectionForeground", Color.WHITE);

                UIManager.put("ComboBox.background", Color.WHITE);
                UIManager.put("ComboBox.foreground", new Color(31, 43, 61));

                UIManager.put("Label.foreground", new Color(31, 43, 61));

                UIManager.put("Button.focus", new Color(45, 105, 220));

                // Verify database connection.
                DBConnection.getConnection().close();

                // Launch application.
                DashboardFrame dashboard = new DashboardFrame();
                dashboard.setVisible(true);

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        null,
                        "Could not initialize application.\n\n"
                                + ex.getMessage(),
                        "Application Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }
}