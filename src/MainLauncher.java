import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Main launcher class that allows users to choose between GUI and Text-based interface.
 * 
 * Provides a simple dialog for interface selection and launches the appropriate
 * version of the Member Management System based on user preference.
 * 
 * Features:
 * - Interface selection dialog
 * - Professional splash screen
 * - System information display
 * - Graceful error handling
 */
public class MainLauncher {
    
    /**
     * Application entry point.
     * Shows interface selection dialog and launches chosen interface.
     * 
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        // Set system look and feel for better appearance
        try {
            UIManager.setLookAndFeel(UIManager.getLookAndFeel());
        } catch (Exception e) {
            // Continue with default look and feel if system L&F fails
        }
        
        SwingUtilities.invokeLater(() -> {
            showSplashScreen();
            showInterfaceSelectionDialog();
        });
    }
    
    /**
     * Shows a professional splash screen with application information.
     */
    private static void showSplashScreen() {
        JWindow splash = new JWindow();
        splash.setSize(500, 300);
        splash.setLocationRelativeTo(null);
        
        // Create splash content
        JPanel content = new JPanel(new BorderLayout());
        content.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createRaisedBevelBorder(),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        content.setBackground(Color.WHITE);
        
        // Title
        JLabel titleLabel = new JLabel("Member Management System", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 102, 204));
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("ICT711 Assessment 4 - Enhanced Version", JLabel.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(Color.DARK_GRAY);
        
        // Team info
        String teamInfo = "<html><center>" +
            "Developed by:<br>" +
            "Muhammad Eman Ejaz - 20034038<br>" +
            "Sajina Rana - 2005243<br>" +
            "Waqas Iqbal - 2005647<br>" +
            "Sravanth Rao - 2003358" +
            "</center></html>";
        JLabel teamLabel = new JLabel(teamInfo, JLabel.CENTER);
        teamLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        
        // Progress bar
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setString("Loading...");
        progressBar.setStringPainted(true);
        
        // Layout
        JPanel topPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        topPanel.setOpaque(false);
        topPanel.add(titleLabel);
        topPanel.add(subtitleLabel);
        
        content.add(topPanel, BorderLayout.NORTH);
        content.add(teamLabel, BorderLayout.CENTER);
        content.add(progressBar, BorderLayout.SOUTH);
        
        splash.setContentPane(content);
        splash.setVisible(true);
        
        // Show splash for 3 seconds
        Timer timer = new Timer(3000, e -> splash.dispose());
        timer.setRepeats(false);
        timer.start();
        
        // Wait for splash to finish
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Shows the interface selection dialog allowing user to choose between GUI and TBI.
     */
    private static void showInterfaceSelectionDialog() {
        JDialog dialog = new JDialog((Frame) null, "Choose Interface", true);
        //dialog.setDefaultCloseOperation(JDialog.EXIT_ON_CLOSE);
        dialog.setSize(450, 300);
        dialog.setLocationRelativeTo(null);
        dialog.setResizable(false);
        
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(245, 245, 245));
        
        // Header
        JLabel headerLabel = new JLabel("Select Your Preferred Interface", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerLabel.setForeground(new Color(0, 102, 204));
        
        // Options panel
        JPanel optionsPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        optionsPanel.setOpaque(false);
        
        // GUI Option
        JPanel guiPanel = createOptionPanel(
            "Graphical User Interface (GUI)",
            "Modern, intuitive interface with tables, forms, and visual elements",
            "🖥️",
            () -> {
                dialog.dispose();
                launchGUI();
            }
        );
        
        // Text-based Option
        JPanel textPanel = createOptionPanel(
            "Text-Based Interface (TBI)",
            "Traditional console-based interface with menu-driven navigation",
            "📟",
            () -> {
                dialog.dispose();
                launchTextInterface();
            }
        );
        
        optionsPanel.add(guiPanel);
        optionsPanel.add(textPanel);
        
        // Footer
        JLabel footerLabel = new JLabel("Choose the interface that best suits your preference", JLabel.CENTER);
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        footerLabel.setForeground(Color.GRAY);
        
        // Layout
        mainPanel.add(headerLabel, BorderLayout.NORTH);
        mainPanel.add(optionsPanel, BorderLayout.CENTER);
        mainPanel.add(footerLabel, BorderLayout.SOUTH);
        
        dialog.setContentPane(mainPanel);
        dialog.setVisible(true);
    }
    
    /**
     * Creates a styled option panel for interface selection.
     * 
     * @param title option title
     * @param description option description
     * @param icon option icon
     * @param action action to perform when selected
     * @return formatted option panel
     */
    private static JPanel createOptionPanel(String title, String description, String icon, Runnable action) {
        JPanel panel = new JPanel(new BorderLayout(10, 5));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createRaisedBevelBorder(),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        panel.setBackground(Color.WHITE);
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panel.setBackground(new Color(240, 248, 255));
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                panel.setBackground(Color.WHITE);
            }
            
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                action.run();
            }
        });
        
        // Icon and title
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        headerPanel.setOpaque(false);
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Dialog", Font.PLAIN, 24));
        
        JLabel titleLabel = new JLabel(" " + title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(new Color(0, 102, 204));
        
        headerPanel.add(iconLabel);
        headerPanel.add(titleLabel);
        
        // Description
        JLabel descLabel = new JLabel("<html>" + description + "</html>");
        descLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        descLabel.setForeground(Color.DARK_GRAY);
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(descLabel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Launches the GUI version of the Member Management System.
     */
    private static void launchGUI() {
        SwingUtilities.invokeLater(() -> {
            try {
                new MemberManagementGUI();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                    "Error launching GUI interface: " + e.getMessage(),
                    "Launch Error",
                    JOptionPane.ERROR_MESSAGE);
                // Fallback to text interface
                launchTextInterface();
            }
        });
    }
    
    /**
     * Launches the text-based version of the Member Management System.
     */
    private static void launchTextInterface() {
        // Run in separate thread to avoid blocking EDT
        new Thread(() -> {
            System.out.println("\n" + Constants.MENU_TITLE);
            System.out.println("Starting Text-Based Interface...\n");
            MemberManagementSystem.main(new String[]{});
        }).start();
    }
}