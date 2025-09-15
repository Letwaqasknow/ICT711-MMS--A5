import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Comprehensive GUI implementation for the Member Management System.
 * 
 * Provides a modern, intuitive interface with:
 * - Menu bar and toolbar for quick access
 * - Table view with sorting and filtering
 * - Add/Edit member forms
 * - Search functionality
 * - Statistics dashboard
 * - File operations
 * - Performance management
 * 
 * Demonstrates advanced Swing concepts and MVC architecture.
 */
public class MemberManagementGUI extends JFrame {
    
    // Core components
    private EnhancedMemberManager manager;
    private JTable memberTable;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    
    // UI Components
    private JTextField searchField;
    private JComboBox<String> searchCriteriaCombo;
    private JComboBox<String> memberTypeFilter;
    private JLabel statusLabel;
    private JLabel memberCountLabel;
    private JProgressBar operationProgress;
    
    // Table columns
    private static final String[] COLUMN_NAMES = {
        "ID", "Name", "Type", "Email", "Phone", "Performance", "Goal", "Monthly Fee"
    };
    
    /**
     * Constructs the main GUI window and initializes all components.
     */
    public MemberManagementGUI() {
        manager = new EnhancedMemberManager();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadInitialData();
        setVisible(true);
    }
    
    /**
     * Initializes all GUI components and their properties.
     */
    private void initializeComponents() {
        // Main window setup
        setTitle("Member Management System - GUI Interface");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setIconImage(createApplicationIcon());
        
        // Initialize manager
        manager = new EnhancedMemberManager();
        
        // Create table model and table
        tableModel = new DefaultTableModel(COLUMN_NAMES, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        
        memberTable = new JTable(tableModel);
        setupTable();
        
        // Create sorter
        sorter = new TableRowSorter<>(tableModel);
        memberTable.setRowSorter(sorter);
        
        // Search components
        searchField = new JTextField(20);
        searchField.setToolTipText("Enter search terms...");
        
        searchCriteriaCombo = new JComboBox<>(new String[]{
            "All Fields", "Name", "Email", "Phone", "Member ID"
        });
        
        memberTypeFilter = new JComboBox<>(new String[]{
            "All Types", "Regular", "Premium", "Student"
        });
        
        // Status components
        statusLabel = new JLabel("Ready");
        memberCountLabel = new JLabel("Members: 0");
        operationProgress = new JProgressBar();
        operationProgress.setVisible(false);
    }
    
    /**
     * Sets up the table appearance and behavior.
     */
    private void setupTable() {
        memberTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        memberTable.setRowHeight(25);
        memberTable.getTableHeader().setReorderingAllowed(false);
        
        // Set column widths
        memberTable.getColumnModel().getColumn(0).setPreferredWidth(80);  // ID
        memberTable.getColumnModel().getColumn(1).setPreferredWidth(150); // Name
        memberTable.getColumnModel().getColumn(2).setPreferredWidth(120); // Type
        memberTable.getColumnModel().getColumn(3).setPreferredWidth(180); // Email
        memberTable.getColumnModel().getColumn(4).setPreferredWidth(120); // Phone
        memberTable.getColumnModel().getColumn(5).setPreferredWidth(90);  // Performance
        memberTable.getColumnModel().getColumn(6).setPreferredWidth(60);  // Goal
        memberTable.getColumnModel().getColumn(7).setPreferredWidth(100); // Fee
        
        // Custom cell renderer for performance rating
        memberTable.getColumnModel().getColumn(5).setCellRenderer(new PerformanceRenderer());
        memberTable.getColumnModel().getColumn(6).setCellRenderer(new GoalRenderer());
        memberTable.getColumnModel().getColumn(7).setCellRenderer(new CurrencyRenderer());
        
        // Add double-click handler for editing
        memberTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    editSelectedMember();
                }
            }
        });
    }
    
    /**
     * Creates the main layout using BorderLayout with multiple panels.
     */
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Menu bar
        setJMenuBar(createMenuBar());
        
        // Toolbar
        add(createToolBar(), BorderLayout.NORTH);
        
        // Main content area
        add(createMainPanel(), BorderLayout.CENTER);
        
        // Status bar
        add(createStatusBar(), BorderLayout.SOUTH);
    }
    
    /**
     * Creates the menu bar with File, Edit, View, and Help menus.
     */
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // File menu
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        
        addMenuItem(fileMenu, "New Database", KeyEvent.VK_N, "ctrl N", e -> newDatabase());
        addMenuItem(fileMenu, "Load from File", KeyEvent.VK_L, "ctrl L", e -> loadFromFile());
        addMenuItem(fileMenu, "Save to File", KeyEvent.VK_S, "ctrl S", e -> saveToFile());
        fileMenu.addSeparator();
        addMenuItem(fileMenu, "Import CSV", KeyEvent.VK_I, "ctrl I", e -> importCSV());
        addMenuItem(fileMenu, "Export CSV", KeyEvent.VK_E, "ctrl E", e -> exportCSV());
        fileMenu.addSeparator();
        addMenuItem(fileMenu, "Exit", KeyEvent.VK_X, "alt F4", e -> exitApplication());
        
        // Edit menu
        JMenu editMenu = new JMenu("Edit");
        editMenu.setMnemonic(KeyEvent.VK_E);
        
        addMenuItem(editMenu, "Add Member", KeyEvent.VK_A, "ctrl PLUS", e -> addNewMember());
        addMenuItem(editMenu, "Edit Member", KeyEvent.VK_E, "F2", e -> editSelectedMember());
        addMenuItem(editMenu, "Delete Member", KeyEvent.VK_D, "DELETE", e -> deleteSelectedMember());
        editMenu.addSeparator();
        addMenuItem(editMenu, "Find Member", KeyEvent.VK_F, "ctrl F", e -> focusSearchField());
        
        // View menu
        JMenu viewMenu = new JMenu("View");
        viewMenu.setMnemonic(KeyEvent.VK_V);
        
        addMenuItem(viewMenu, "Refresh", KeyEvent.VK_R, "F5", e -> refreshTable());
        addMenuItem(viewMenu, "Statistics", KeyEvent.VK_S, "ctrl T", e -> showStatistics());
        addMenuItem(viewMenu, "Performance Reports", KeyEvent.VK_P, "ctrl P", e -> showPerformanceReports());
        
        // Help menu
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic(KeyEvent.VK_H);
        
        addMenuItem(helpMenu, "User Guide", KeyEvent.VK_U, "F1", e -> showUserGuide());
        addMenuItem(helpMenu, "About", KeyEvent.VK_A, null, e -> showAbout());
        
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(viewMenu);
        menuBar.add(helpMenu);
        
        return menuBar;
    }
    
    /**
     * Helper method to add menu items with consistent formatting.
     */
    private void addMenuItem(JMenu menu, String text, int mnemonic, String accelerator, ActionListener action) {
        JMenuItem item = new JMenuItem(text);
        item.setMnemonic(mnemonic);
        if (accelerator != null) {
            item.setAccelerator(KeyStroke.getKeyStroke(accelerator));
        }
        item.addActionListener(action);
        menu.add(item);
    }
    
    /**
     * Creates the toolbar with common action buttons.
     */
    private JToolBar createToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        
        addToolBarButton(toolBar, "New", "Create new database", e -> newDatabase());
        addToolBarButton(toolBar, "Load", "Load from file", e -> loadFromFile());
        addToolBarButton(toolBar, "Save", "Save to file", e -> saveToFile());
        toolBar.addSeparator();
        addToolBarButton(toolBar, "Add", "Add new member", e -> addNewMember());
        addToolBarButton(toolBar, "Edit", "Edit selected member", e -> editSelectedMember());
        addToolBarButton(toolBar, "Delete", "Delete selected member", e -> deleteSelectedMember());
        toolBar.addSeparator();
        addToolBarButton(toolBar, "Refresh", "Refresh table", e -> refreshTable());
        addToolBarButton(toolBar, "Stats", "Show statistics", e -> showStatistics());
        
        // Add search components to toolbar
        toolBar.addSeparator();
        toolBar.add(new JLabel("Search: "));
        toolBar.add(searchCriteriaCombo);
        toolBar.add(searchField);
        toolBar.add(new JLabel(" Type: "));
        toolBar.add(memberTypeFilter);
        
        return toolBar;
    }
    
    /**
     * Helper method to add toolbar buttons with consistent formatting.
     */
    private void addToolBarButton(JToolBar toolBar, String text, String tooltip, ActionListener action) {
        JButton button = new JButton(text);
        button.setToolTipText(tooltip);
        button.addActionListener(action);
        button.setFocusPainted(false);
        toolBar.add(button);
    }
    
    /**
     * Creates the main content panel with table and search controls.
     */
    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Table panel
        JScrollPane scrollPane = new JScrollPane(memberTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Members"));
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        return mainPanel;
    }
    
    /**
     * Creates the status bar at the bottom of the window.
     */
    private JPanel createStatusBar() {
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBorder(BorderFactory.createLoweredBevelBorder());
        statusBar.setPreferredSize(new Dimension(0, 25));
        
        statusBar.add(statusLabel, BorderLayout.WEST);
        statusBar.add(memberCountLabel, BorderLayout.CENTER);
        statusBar.add(operationProgress, BorderLayout.EAST);
        
        return statusBar;
    }
    
    /**
     * Sets up event handlers for various components.
     */
    private void setupEventHandlers() {
        // Search field handler
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                performSearch();
            }
        });
        
        // Search criteria combo handler
        searchCriteriaCombo.addActionListener(e -> performSearch());
        
        // Member type filter handler
        memberTypeFilter.addActionListener(e -> performSearch());
        
        // Table selection handler
        memberTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateStatusForSelection();
            }
        });
        
        // Window closing handler
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitApplication();
            }
        });
    }
    
    /**
     * Loads initial data and populates the table.
     */
    private void loadInitialData() {
        SwingUtilities.invokeLater(() -> {
            try {
                // Try to load default file if it exists
                File defaultFile = new File(Constants.DEFAULT_FILE_NAME);
                if (defaultFile.exists()) {
                    manager.loadFromFile(Constants.DEFAULT_FILE_NAME);
                    refreshTable();
                    setStatus("Loaded default data file");
                } else {
                    // Create sample data
                    createSampleData();
                    setStatus("Created sample data");
                }
            } catch (Exception e) {
                showError("Error loading initial data: " + e.getMessage());
                createSampleData();
            }
        });
    }
    
    /**
     * Creates sample data for demonstration purposes.
     */
    private void createSampleData() {
        // Add some sample members
        manager.addMember(new RegularMember("M001", "John", "Doe", "john.doe@email.com", "555-0101"));
        manager.addMember(new PremiumMember("M002", "Jane", "Smith", "jane.smith@email.com", "555-0102", "Alex Trainer", 8));
        manager.addMember(new StudentMember("M003", "Bob", "Johnson", "bob.johnson@email.com", "555-0103", "STU2024001", "State University"));
        
        // Set some performance data
        List<Member> members = manager.getAllMembers();
        if (!members.isEmpty()) {
            members.get(0).setPerformanceRating(8);
            members.get(0).setGoalAchieved(true);
            members.get(1).setPerformanceRating(9);
            members.get(1).setGoalAchieved(true);
            members.get(2).setPerformanceRating(6);
            members.get(2).setGoalAchieved(false);
        }
        
        refreshTable();
    }
    
    /**
     * Refreshes the table with current data.
     */
    private void refreshTable() {
        SwingUtilities.invokeLater(() -> {
            tableModel.setRowCount(0);
            List<Member> members = manager.getAllMembers();
            
            for (Member member : members) {
                Object[] row = {
                    member.getMemberId(),
                    member.getFullName(),
                    member.getMemberType(),
                    member.getEmail(),
                    member.getPhone(),
                    member.getPerformanceRating(),
                    member.isGoalAchieved() ? "Yes" : "No",
                    String.format("$%.2f", member.calculateMonthlyFee())
                };
                tableModel.addRow(row);
            }
            
            updateMemberCount();
            setStatus("Table refreshed - " + members.size() + " members");
        });
    }
    
    /**
     * Performs search based on current criteria and filters.
     */
    private void performSearch() {
        String searchText = searchField.getText().toLowerCase().trim();
        String criteria = (String) searchCriteriaCombo.getSelectedItem();
        String typeFilter = (String) memberTypeFilter.getSelectedItem();
        
        if (sorter != null) {
            sorter.setRowFilter(new CombinedRowFilter(searchText, criteria, typeFilter));
            updateMemberCount();
        }
    }
    
    /**
     * Custom RowFilter that combines text search with type filtering.
     */
    private class CombinedRowFilter extends RowFilter<DefaultTableModel, Object> {
        private final String searchText;
        private final String criteria;
        private final String typeFilter;
        
        public CombinedRowFilter(String searchText, String criteria, String typeFilter) {
            this.searchText = searchText;
            this.criteria = criteria;
            this.typeFilter = typeFilter;
        }
        
        @Override
        public boolean include(Entry<? extends DefaultTableModel, ? extends Object> entry) {
            // Type filter
            if (!"All Types".equals(typeFilter)) {
                String memberType = entry.getStringValue(2);
                if (!memberType.toLowerCase().contains(typeFilter.toLowerCase())) {
                    return false;
                }
            }
            
            // Text search
            if (searchText.isEmpty()) {
                return true;
            }
            
            switch (criteria) {
                case "Name":
                    return entry.getStringValue(1).toLowerCase().contains(searchText);
                case "Email":
                    return entry.getStringValue(3).toLowerCase().contains(searchText);
                case "Phone":
                    return entry.getStringValue(4).toLowerCase().contains(searchText);
                case "Member ID":
                    return entry.getStringValue(0).toLowerCase().contains(searchText);
                default: // All Fields
                    for (int i = 0; i < entry.getValueCount(); i++) {
                        if (entry.getStringValue(i).toLowerCase().contains(searchText)) {
                            return true;
                        }
                    }
                    return false;
            }
        }
    }
    
    // Action Methods
    
    private void newDatabase() {
        int result = JOptionPane.showConfirmDialog(this,
            "This will clear all current data. Continue?",
            "New Database",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
            
        if (result == JOptionPane.YES_OPTION) {
            manager = new EnhancedMemberManager();
            refreshTable();
            setStatus("New database created");
        }
    }
    
    private void loadFromFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));
        fileChooser.setCurrentDirectory(new File("."));
        
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                showProgress("Loading file...");
                manager.loadFromFile(file.getAbsolutePath());
                refreshTable();
                setStatus("Loaded: " + file.getName());
                showSuccess("File loaded successfully!");
            } catch (IOException e) {
                showError("Error loading file: " + e.getMessage());
            } finally {
                hideProgress();
            }
        }
    }
    
    private void saveToFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));
        fileChooser.setCurrentDirectory(new File("."));
        fileChooser.setSelectedFile(new File("member_data.csv"));
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                showProgress("Saving file...");
                manager.saveToFile(file.getAbsolutePath());
                setStatus("Saved: " + file.getName());
                showSuccess("File saved successfully!");
            } catch (IOException e) {
                showError("Error saving file: " + e.getMessage());
            } finally {
                hideProgress();
            }
        }
    }
    
    private void importCSV() {
        loadFromFile(); // Same as load from file
    }
    
    private void exportCSV() {
        saveToFile(); // Same as save to file
    }
    
    private void exitApplication() {
        int result = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to exit?",
            "Exit Application",
            JOptionPane.YES_NO_OPTION);
            
        if (result == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
    
    private void addNewMember() {
        MemberDialog dialog = new MemberDialog(this, "Add New Member", null, manager);
        dialog.setVisible(true);
        
        if (dialog.wasSuccessful()) {
            refreshTable();
            setStatus("Member added successfully");
        }
    }
    
    private void editSelectedMember() {
        int selectedRow = memberTable.getSelectedRow();
        if (selectedRow == -1) {
            showWarning("Please select a member to edit.");
            return;
        }
        
        // Convert view index to model index
        int modelRow = memberTable.convertRowIndexToModel(selectedRow);
        String memberId = (String) tableModel.getValueAt(modelRow, 0);
        Member member = manager.findMemberById(memberId);
        
        if (member != null) {
            MemberDialog dialog = new MemberDialog(this, "Edit Member", member, manager);
            dialog.setVisible(true);
            
            if (dialog.wasSuccessful()) {
                refreshTable();
                setStatus("Member updated successfully");
            }
        }
    }
    
    private void deleteSelectedMember() {
        int selectedRow = memberTable.getSelectedRow();
        if (selectedRow == -1) {
            showWarning("Please select a member to delete.");
            return;
        }
        
        int modelRow = memberTable.convertRowIndexToModel(selectedRow);
        String memberId = (String) tableModel.getValueAt(modelRow, 0);
        String memberName = (String) tableModel.getValueAt(modelRow, 1);
        
        int result = JOptionPane.showConfirmDialog(this,
            "Delete member: " + memberName + " (ID: " + memberId + ")?",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
            
        if (result == JOptionPane.YES_OPTION) {
            if (manager.removeMember(memberId)) {
                refreshTable();
                setStatus("Member deleted successfully");
            } else {
                showError("Failed to delete member");
            }
        }
    }
    
    private void focusSearchField() {
        searchField.requestFocus();
        searchField.selectAll();
    }
    
    private void showStatistics() {
        StatisticsDialog dialog = new StatisticsDialog(this, manager);
        dialog.setVisible(true);
    }
    
    private void showPerformanceReports() {
        PerformanceReportsDialog dialog = new PerformanceReportsDialog(this, manager);
        dialog.setVisible(true);
    }
    
    private void showUserGuide() {
        String guide = "Member Management System - User Guide\n\n" +
                      "File Operations:\n" +
                      "- Use File menu to load/save member data\n" +
                      "- CSV format is supported for import/export\n\n" +
                      "Member Management:\n" +
                      "- Add: Click Add button or use Ctrl+Plus\n" +
                      "- Edit: Double-click member or press F2\n" +
                      "- Delete: Select member and press Delete\n\n" +
                      "Search and Filter:\n" +
                      "- Use search field to find members\n" +
                      "- Select search criteria from dropdown\n" +
                      "- Filter by member type\n" +
                      "- Click column headers to sort\n\n" +
                      "Performance Management:\n" +
                      "- View statistics from View menu\n" +
                      "- Generate performance reports\n" +
                      "- Track goals and ratings\n\n" +
                      "Keyboard Shortcuts:\n" +
                      "- Ctrl+N: New database\n" +
                      "- Ctrl+L: Load file\n" +
                      "- Ctrl+S: Save file\n" +
                      "- Ctrl+F: Find member\n" +
                      "- F5: Refresh\n" +
                      "- F1: This help";
                      
        JTextArea textArea = new JTextArea(guide);
        textArea.setEditable(false);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));
        
        JOptionPane.showMessageDialog(this, scrollPane, "User Guide", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showAbout() {
        String about = "Member Management System\n" +
                      "Version 2.0 - GUI Enhanced\n\n" +
                      "ICT711 Assessment 4\n" +
                      "Enhanced with GUI Interface\n\n" +
                      "Developed by:\n" +
                      "Muhammad Eman Ejaz - 20034038\n" +
                      "Sajina Rana - 2005243\n" +
                      "Waqas Iqbal - 2005647\n" +
                      "Sravanth Rao - 2003358\n\n" +
                      "Features:\n" +
                      "• Modern GUI interface\n" +
                      "• Advanced search and filtering\n" +
                      "• Multiple member types\n" +
                      "• Performance tracking\n" +
                      "• Statistical reporting\n" +
                      "• CSV import/export\n\n" +
                      "© 2024 ICT711 Project Team";
                      
        JOptionPane.showMessageDialog(this, about, "About MMS", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Utility Methods
    
    private void updateMemberCount() {
        int totalMembers = manager.getAllMembers().size();
        int visibleMembers = memberTable.getRowCount();
        
        if (totalMembers == visibleMembers) {
            memberCountLabel.setText("Members: " + totalMembers);
        } else {
            memberCountLabel.setText("Showing: " + visibleMembers + " of " + totalMembers);
        }
    }
    
    private void updateStatusForSelection() {
        int selectedRow = memberTable.getSelectedRow();
        if (selectedRow != -1) {
            int modelRow = memberTable.convertRowIndexToModel(selectedRow);
            String memberName = (String) tableModel.getValueAt(modelRow, 1);
            setStatus("Selected: " + memberName);
        } else {
            setStatus("Ready");
        }
    }
    
    private void setStatus(String status) {
        statusLabel.setText(status);
    }
    
    private void showProgress(String message) {
        operationProgress.setVisible(true);
        operationProgress.setIndeterminate(true);
        setStatus(message);
    }
    
    private void hideProgress() {
        operationProgress.setVisible(false);
        operationProgress.setIndeterminate(false);
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Warning", JOptionPane.WARNING_MESSAGE);
    }
    
    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private Image createApplicationIcon() {
        // Create a simple icon programmatically
        int size = 32;
        Image icon = new java.awt.image.BufferedImage(size, size, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) icon.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(new Color(0, 102, 204));
        g.fillOval(2, 2, size-4, size-4);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        FontMetrics fm = g.getFontMetrics();
        String text = "M";
        int x = (size - fm.stringWidth(text)) / 2;
        int y = (size - fm.getHeight()) / 2 + fm.getAscent();
        g.drawString(text, x, y);
        g.dispose();
        return icon;
    }
    
    // Custom cell renderers
    
    /**
     * Custom renderer for performance rating column with color coding.
     */
    private class PerformanceRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            if (value instanceof Integer) {
                int rating = (Integer) value;
                setText(rating + "/10");
                
                if (!isSelected) {
                    if (rating >= 8) {
                        setBackground(new Color(144, 238, 144)); // Light green
                    } else if (rating >= 5) {
                        setBackground(new Color(255, 255, 224)); // Light yellow
                    } else {
                        setBackground(new Color(255, 182, 193)); // Light red
                    }
                } else {
                    setBackground(table.getSelectionBackground());
                }
            }
            
            setHorizontalAlignment(SwingConstants.CENTER);
            return this;
        }
    }
    
    /**
     * Custom renderer for goal achievement column.
     */
    private class GoalRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            setHorizontalAlignment(SwingConstants.CENTER);
            
            if ("Yes".equals(value)) {
                setText("✓ Yes");
                if (!isSelected) {
                    setForeground(new Color(0, 128, 0)); // Dark green
                }
            } else {
                setText("✗ No");
                if (!isSelected) {
                    setForeground(Color.RED);
                }
            }
            
            if (isSelected) {
                setForeground(table.getSelectionForeground());
            }
            
            return this;
        }
    }
    
    /**
     * Custom renderer for currency formatting.
     */
    private class CurrencyRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setHorizontalAlignment(SwingConstants.RIGHT);
            return this;
        }
    }
}