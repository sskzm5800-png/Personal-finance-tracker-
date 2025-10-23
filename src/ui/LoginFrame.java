package ui;

import dao.UserDAO;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Login frame for user authentication
 */
public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JButton guestButton;
    private UserDAO userDAO;

    public LoginFrame() {
        userDAO = new UserDAO();
        initializeComponents();
    }

    private void initializeComponents() {
        setTitle("Personal Finance Tracker - Login");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel with dark background
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(new Color(45, 45, 45));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title Label
        JLabel titleLabel = new JLabel("Personal Finance Tracker");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        // Subtitle
        JLabel subtitleLabel = new JLabel("Manage Your Finances Smartly");
        subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        subtitleLabel.setForeground(new Color(180, 180, 180));
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 1;
        mainPanel.add(subtitleLabel, gbc);

        // Username Label
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(usernameLabel, gbc);

        // Username Field
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField.setBackground(new Color(60, 60, 60));
        usernameField.setForeground(Color.WHITE);
        usernameField.setCaretColor(Color.WHITE);
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 100)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        gbc.gridx = 1;
        mainPanel.add(usernameField, gbc);

        // Password Label
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(passwordLabel, gbc);

        // Password Field
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBackground(new Color(60, 60, 60));
        passwordField.setForeground(Color.WHITE);
        passwordField.setCaretColor(Color.WHITE);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 100)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        gbc.gridx = 1;
        mainPanel.add(passwordField, gbc);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(new Color(45, 45, 45));

        // Login Button
        loginButton = createStyledButton("Login", new Color(70, 130, 180));
        loginButton.addActionListener(e -> handleLogin());
        buttonPanel.add(loginButton);

        // Register Button
        registerButton = createStyledButton("Register", new Color(60, 179, 113));
        registerButton.addActionListener(e -> handleRegister());
        buttonPanel.add(registerButton);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        mainPanel.add(buttonPanel, gbc);

        // Guest Button
        guestButton = createStyledButton("Continue as Guest", new Color(128, 128, 128));
        guestButton.addActionListener(e -> handleGuest());
        gbc.gridy = 5;
        mainPanel.add(guestButton, gbc);

        // Add Enter key listener for login
        KeyAdapter enterKeyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleLogin();
                }
            }
        };
        usernameField.addKeyListener(enterKeyListener);
        passwordField.addKeyListener(enterKeyListener);

        add(mainPanel);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(150, 35));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter both username and password");
            return;
        }

        User user = userDAO.loginUser(username, password);
        
        if (user != null) {
            JOptionPane.showMessageDialog(this, 
                "Welcome back, " + user.getUsername() + "!", 
                "Login Successful", 
                JOptionPane.INFORMATION_MESSAGE);
            openDashboard(user);
        } else {
            showError("Invalid username or password");
        }
    }

    private void handleRegister() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter both username and password");
            return;
        }

        if (username.length() < 3) {
            showError("Username must be at least 3 characters long");
            return;
        }

        if (password.length() < 6) {
            showError("Password must be at least 6 characters long");
            return;
        }

        if (userDAO.usernameExists(username)) {
            showError("Username already exists. Please choose another.");
            return;
        }

        if (userDAO.registerUser(username, password)) {
            JOptionPane.showMessageDialog(this, 
                "Registration successful! You can now login.", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
            usernameField.setText("");
            passwordField.setText("");
        } else {
            showError("Registration failed. Please try again.");
        }
    }

    private void handleGuest() {
        int choice = JOptionPane.showConfirmDialog(this,
            "Guest mode data will not be saved to database.\nContinue?",
            "Guest Mode",
            JOptionPane.YES_NO_OPTION);
        
        if (choice == JOptionPane.YES_OPTION) {
            User guestUser = new User(-1, "Guest", "");
            openDashboard(guestUser);
        }
    }

    private void openDashboard(User user) {
        SwingUtilities.invokeLater(() -> {
            DashboardFrame dashboard = new DashboardFrame(user);
            dashboard.setVisible(true);
            dispose();
        });
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
