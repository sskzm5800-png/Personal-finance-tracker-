import db.DatabaseConnection;
import ui.LoginFrame;

import javax.swing.*;

/**
 * Main entry point for Personal Finance Tracker application
 * Java 25 LTS - Swing UI - MySQL Backend
 */
public class Main {
    public static void main(String[] args) {
        // Print application header
        printHeader();

        // Test database connection
        DatabaseConnection dbConnection = DatabaseConnection.getInstance();
        if (!dbConnection.testConnection()) {
            System.err.println("\n⚠️  WARNING: Database connection failed!");
            System.err.println("You can still use the application in Guest Mode.");
            System.err.println("However, data will not be persisted.\n");
        }

        // Set Nimbus Look and Feel for modern dark UI
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    customizeNimbusTheme();
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("Could not set Nimbus Look and Feel: " + e.getMessage());
            System.err.println("Using default Look and Feel instead.");
        }

        // Launch the login frame on EDT (Event Dispatch Thread)
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }

    /**
     * Print application header to console
     */
    private static void printHeader() {
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║                                                           ║");
        System.out.println("║       PERSONAL FINANCE TRACKER v1.0                       ║");
        System.out.println("║       Java 25 LTS | Swing UI | MySQL 8.0                  ║");
        System.out.println("║                                                           ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        System.out.println();
    }

    /**
     * Customize Nimbus theme for dark appearance
     */
    private static void customizeNimbusTheme() {
        UIManager.put("control", new java.awt.Color(45, 45, 45));
        UIManager.put("nimbusBase", new java.awt.Color(30, 30, 30));
        UIManager.put("nimbusBlueGrey", new java.awt.Color(50, 50, 50));
        UIManager.put("nimbusFocus", new java.awt.Color(70, 130, 180));
        UIManager.put("text", java.awt.Color.WHITE);
        UIManager.put("textForeground", java.awt.Color.WHITE);
        UIManager.put("textBackground", new java.awt.Color(60, 60, 60));
        UIManager.put("background", new java.awt.Color(45, 45, 45));
        
        // ComboBox customization
        UIManager.put("ComboBox.background", new java.awt.Color(60, 60, 60));
        UIManager.put("ComboBox.foreground", java.awt.Color.WHITE);
        UIManager.put("ComboBox.selectionBackground", new java.awt.Color(70, 130, 180));
        UIManager.put("ComboBox.selectionForeground", java.awt.Color.WHITE);
        
        // Table customization
        UIManager.put("Table.background", new java.awt.Color(50, 50, 50));
        UIManager.put("Table.foreground", java.awt.Color.WHITE);
        UIManager.put("Table.gridColor", new java.awt.Color(70, 70, 70));
        
        // ScrollPane customization
        UIManager.put("ScrollPane.background", new java.awt.Color(45, 45, 45));
    }
}
