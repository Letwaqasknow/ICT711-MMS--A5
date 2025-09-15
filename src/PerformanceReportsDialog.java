import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Dialog for generating and displaying performance reports.
 * 
 * Provides functionality for:
 * - Generating appreciation letters for high performers
 * - Creating reminder letters for low performers
 * - Bulk report generation
 * - Export capabilities
 * - Performance trend analysis
 * 
 * Integrates with the member management system to provide actionable insights.
 */
public class PerformanceReportsDialog extends JDialog {
    
    private EnhancedMemberManager manager;
    private JTextArea reportArea;
    private JComboBox<String> reportTypeCombo;
    private JSpinner minRatingSpinner;
    private JCheckBox goalAchieversOnlyBox;
    private JButton generateButton;
    private JButton exportButton;
    private JButton printButton;
    
    /**
     * Constructs a new PerformanceReportsDialog.
     * 
     * @param parent the parent frame
     * @param manager the member manager instance
     */
    public PerformanceReportsDialog(JFrame parent, EnhancedMemberManager manager) {
        super(parent, "Performance Reports", true);
        this.manager = manager;
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        
        setSize(800, 600);
        setLocationRelativeTo(parent);
    }
    
    /**
     * Initializes all dialog components.
     */
    private void initializeComponents() {
        // Report type selection
        reportTypeCombo = new JComboBox<>(new String[]{
            "All Performance Reports",
            "Appreciation Letters (High Performers)",
            "Reminder Letters (Low Performers)",
            "Goal Achievement Analysis",
            "Member Type Performance Comparison",
            "Fee Impact Analysis"
        });
        
        // Filter controls
        minRatingSpinner = new JSpinner(new SpinnerNumberModel(5, 0, 10, 1));
        goalAchieversOnlyBox = new JCheckBox("Goal achievers only");
        
        // Action buttons
        generateButton = new JButton("Generate Report");
        exportButton = new JButton("Export to File");
        printButton = new JButton("Print Report");
        
        // Report display area
        reportArea = new JTextArea();
        reportArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        reportArea.setEditable(false);
        reportArea.setBackground(Color.WHITE);
        
        // Set tooltips
        reportTypeCombo.setToolTipText("Select the type of report to generate");
        minRatingSpinner.setToolTipText("Minimum performance rating to include");
        goalAchieversOnlyBox.setToolTipText("Include only members who achieved their goals");
        exportButton.setToolTipText("Save report to a text file");
        printButton.setToolTipText("Print the current report");
    }
    
    /**
     * Sets up the dialog layout.
     */
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        
        // Top panel with controls
        JPanel controlPanel = createControlPanel();
        add(controlPanel, BorderLayout.NORTH);
        
        // Center panel with report area
        JScrollPane scrollPane = new JScrollPane(reportArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Report Output"));
        scrollPane.setPreferredSize(new Dimension(750, 400));
        add(scrollPane, BorderLayout.CENTER);
        
        // Bottom panel with action buttons
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Creates the control panel with report options.
     */
    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Report Options"));
        panel.setPreferredSize(new Dimension(0, 100));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Report type
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Report Type:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        panel.add(reportTypeCombo, gbc);
        
        // Filters
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        panel.add(new JLabel("Min Rating:"), gbc);
        gbc.gridx = 1;
        panel.add(minRatingSpinner, gbc);
        gbc.gridx = 2;
        panel.add(goalAchieversOnlyBox, gbc);
        
        // Generate button
        gbc.gridx = 3; gbc.gridy = 0; gbc.gridheight = 2; gbc.fill = GridBagConstraints.VERTICAL;
        generateButton.setPreferredSize(new Dimension(120, 50));
        panel.add(generateButton, gbc);
        
        return panel;
    }
    
    /**
     * Creates the button panel for actions.
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));
        
        panel.add(exportButton);
        panel.add(printButton);
        panel.add(Box.createHorizontalStrut(20));
        
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        panel.add(closeButton);
        
        return panel;
    }
    
    /**
     * Sets up event handlers for all components.
     */
    private void setupEventHandlers() {
        generateButton.addActionListener(new GenerateReportAction());
        exportButton.addActionListener(new ExportReportAction());
        printButton.addActionListener(new PrintReportAction());
        
        // Auto-generate on type change
        reportTypeCombo.addActionListener(e -> generateReport());
        
        // Initial report generation
        SwingUtilities.invokeLater(this::generateReport);
    }
    
    /**
     * Generates the selected report type.
     */
    private void generateReport() {
        String selectedType = (String) reportTypeCombo.getSelectedItem();
        int minRating = (Integer) minRatingSpinner.getValue();
        boolean goalAchieversOnly = goalAchieversOnlyBox.isSelected();
        
        StringBuilder report = new StringBuilder();
        
        // Add header
        report.append("=".repeat(80)).append("\n");
        report.append("PERFORMANCE REPORT - ").append(selectedType.toUpperCase()).append("\n");
        report.append("Generated: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\n");
        report.append("=".repeat(80)).append("\n\n");
        
        List<Member> members = manager.getAllMembers();
        
        // Apply filters
        List<Member> filteredMembers = members.stream()
                .filter(m -> m.getPerformanceRating() >= minRating)
                .filter(m -> !goalAchieversOnly || m.isGoalAchieved())
                .collect(Collectors.toList());
        
        switch (selectedType) {
            case "All Performance Reports":
                generateAllPerformanceReports(report, filteredMembers);
                break;
            case "Appreciation Letters (High Performers)":
                generateAppreciationLetters(report, filteredMembers);
                break;
            case "Reminder Letters (Low Performers)":
                generateReminderLetters(report, members); // Use all members for low performers
                break;
            case "Goal Achievement Analysis":
                generateGoalAnalysis(report, members);
                break;
            case "Member Type Performance Comparison":
                generateTypeComparison(report, members);
                break;
            case "Fee Impact Analysis":
                generateFeeImpactAnalysis(report, members);
                break;
        }
        
        reportArea.setText(report.toString());
        reportArea.setCaretPosition(0); // Scroll to top
    }
    
    /**
     * Generates comprehensive performance reports for all members.
     */
    private void generateAllPerformanceReports(StringBuilder report, List<Member> members) {
        report.append("COMPREHENSIVE PERFORMANCE OVERVIEW\n");
        report.append("-".repeat(50)).append("\n\n");
        
        if (members.isEmpty()) {
            report.append("No members match the specified criteria.\n");
            return;
        }
        
        // Summary statistics
        double avgRating = members.stream().mapToInt(Member::getPerformanceRating).average().orElse(0);
        long goalAchievers = members.stream().filter(Member::isGoalAchieved).count();
        
        report.append("SUMMARY STATISTICS\n");
        report.append("Total Members: ").append(members.size()).append("\n");
        report.append("Average Performance: ").append(String.format("%.2f/10", avgRating)).append("\n");
        report.append("Goal Achievers: ").append(goalAchievers).append(" (").append(String.format("%.1f%%", (double) goalAchievers / members.size() * 100)).append(")\n\n");
        
        // Individual member reports
        report.append("INDIVIDUAL MEMBER REPORTS\n");
        report.append("-".repeat(50)).append("\n");
        
        for (Member member : members) {
            report.append("\n").append(member.generatePerformanceReport()).append("\n");
            
            // Add recommendations
            report.append("Recommendations: ");
            if (member.getPerformanceRating() >= 8) {
                report.append("Excellent performance! Consider leadership opportunities.");
            } else if (member.getPerformanceRating() >= 5) {
                report.append("Good performance. Focus on goal achievement for next month.");
            } else {
                report.append("Needs improvement. Consider additional training sessions.");
            }
            report.append("\n").append("-".repeat(40)).append("\n");
        }
    }
    
    /**
     * Generates appreciation letters for high-performing members.
     */
    private void generateAppreciationLetters(StringBuilder report, List<Member> members) {
        report.append("APPRECIATION LETTERS FOR HIGH PERFORMERS\n");
        report.append("-".repeat(50)).append("\n\n");
        
        List<Member> highPerformers = members.stream()
                .filter(m -> m.getPerformanceRating() >= Constants.HIGH_PERFORMANCE_THRESHOLD)
                .collect(Collectors.toList());
        
        if (highPerformers.isEmpty()) {
            report.append("No high-performing members found with the current criteria.\n");
            report.append("Consider lowering the minimum rating threshold.\n");
            return;
        }
        
        report.append("Found ").append(highPerformers.size()).append(" high-performing member(s):\n\n");
        
        for (Member member : highPerformers) {
            String letter = manager.generateAppreciationLetter(member);
            if (letter != null) {
                report.append(letter).append("\n");
                report.append("=".repeat(60)).append("\n\n");
            }
        }
    }
    
    /**
     * Generates reminder letters for low-performing members.
     */
    private void generateReminderLetters(StringBuilder report, List<Member> members) {
        report.append("REMINDER LETTERS FOR LOW PERFORMERS\n");
        report.append("-".repeat(50)).append("\n\n");
        
        List<Member> lowPerformers = members.stream()
                .filter(m -> m.getPerformanceRating() < Constants.LOW_PERFORMANCE_THRESHOLD)
                .collect(Collectors.toList());
        
        if (lowPerformers.isEmpty()) {
            report.append("Excellent news! No low-performing members found.\n");
            report.append("All members are maintaining satisfactory performance levels.\n");
            return;
        }
        
        report.append("Found ").append(lowPerformers.size()).append(" member(s) needing encouragement:\n\n");
        
        for (Member member : lowPerformers) {
            String letter = manager.generateReminderLetter(member);
            if (letter != null) {
                report.append(letter).append("\n");
                report.append("=".repeat(60)).append("\n\n");
            }
        }
    }
    
    /**
     * Generates goal achievement analysis.
     */
    private void generateGoalAnalysis(StringBuilder report, List<Member> members) {
        report.append("GOAL ACHIEVEMENT ANALYSIS\n");
        report.append("-".repeat(50)).append("\n\n");
        
        if (members.isEmpty()) {
            report.append("No members in the system.\n");
            return;
        }
        
        // Overall statistics
        long totalMembers = members.size();
        long achievers = members.stream().filter(Member::isGoalAchieved).count();
        long nonAchievers = totalMembers - achievers;
        
        report.append("OVERALL GOAL ACHIEVEMENT\n");
        report.append("Total Members: ").append(totalMembers).append("\n");
        report.append("Goal Achievers: ").append(achievers).append(" (").append(String.format("%.1f%%", (double) achievers / totalMembers * 100)).append(")\n");
        report.append("Non-Achievers: ").append(nonAchievers).append(" (").append(String.format("%.1f%%", (double) nonAchievers / totalMembers * 100)).append(")\n\n");
        
        // By member type
        report.append("GOAL ACHIEVEMENT BY MEMBER TYPE\n");
        report.append("-".repeat(40)).append("\n");
        
        analyzeGoalsByType(report, members, "Regular", RegularMember.class);
        analyzeGoalsByType(report, members, "Premium", PremiumMember.class);
        analyzeGoalsByType(report, members, "Student", StudentMember.class);
        
        // By performance level
        report.append("\nGOAL ACHIEVEMENT BY PERFORMANCE LEVEL\n");
        report.append("-".repeat(40)).append("\n");
        
        analyzeGoalsByPerformance(report, members, "High (8-10)", 8, 10);
        analyzeGoalsByPerformance(report, members, "Medium (5-7)", 5, 7);
        analyzeGoalsByPerformance(report, members, "Low (0-4)", 0, 4);
    }
    
    /**
     * Helper method to analyze goals by member type.
     */
    private void analyzeGoalsByType(StringBuilder report, List<Member> members, String typeName, Class<?> typeClass) {
        List<Member> typeMembers = members.stream()
                .filter(typeClass::isInstance)
                .collect(Collectors.toList());
        
        if (!typeMembers.isEmpty()) {
            long achievers = typeMembers.stream().filter(Member::isGoalAchieved).count();
            report.append(String.format("%-10s: %d/%d (%.1f%%)\n", 
                    typeName, achievers, typeMembers.size(), 
                    (double) achievers / typeMembers.size() * 100));
        }
    }
    
    /**
     * Helper method to analyze goals by performance level.
     */
    private void analyzeGoalsByPerformance(StringBuilder report, List<Member> members, String levelName, int minRating, int maxRating) {
        List<Member> levelMembers = members.stream()
                .filter(m -> m.getPerformanceRating() >= minRating && m.getPerformanceRating() <= maxRating)
                .collect(Collectors.toList());
        
        if (!levelMembers.isEmpty()) {
            long achievers = levelMembers.stream().filter(Member::isGoalAchieved).count();
            report.append(String.format("%-12s: %d/%d (%.1f%%)\n", 
                    levelName, achievers, levelMembers.size(), 
                    (double) achievers / levelMembers.size() * 100));
        }
    }
    
    /**
     * Generates member type performance comparison.
     */
    private void generateTypeComparison(StringBuilder report, List<Member> members) {
        report.append("MEMBER TYPE PERFORMANCE COMPARISON\n");
        report.append("-".repeat(50)).append("\n\n");
        
        if (members.isEmpty()) {
            report.append("No members in the system.\n");
            return;
        }
        
        // Separate by type
        List<Member> regularMembers = members.stream().filter(m -> m instanceof RegularMember).collect(Collectors.toList());
        List<Member> premiumMembers = members.stream().filter(m -> m instanceof PremiumMember).collect(Collectors.toList());
        List<Member> studentMembers = members.stream().filter(m -> m instanceof StudentMember).collect(Collectors.toList());
        
        // Generate comparison table
        report.append(String.format("%-15s %-8s %-12s %-12s %-12s\n", "Member Type", "Count", "Avg Rating", "Goal Rate", "Avg Fee"));
        report.append("-".repeat(70)).append("\n");
        
        if (!regularMembers.isEmpty()) {
            generateTypeStats(report, "Regular", regularMembers);
        }
        if (!premiumMembers.isEmpty()) {
            generateTypeStats(report, "Premium", premiumMembers);
        }
        if (!studentMembers.isEmpty()) {
            generateTypeStats(report, "Student", studentMembers);
        }
        
        // Overall summary
        report.append("-".repeat(70)).append("\n");
        generateTypeStats(report, "OVERALL", members);
    }
    
    /**
     * Helper method to generate statistics for a member type.
     */
    private void generateTypeStats(StringBuilder report, String typeName, List<Member> typeMembers) {
        double avgRating = typeMembers.stream().mapToInt(Member::getPerformanceRating).average().orElse(0);
        long achievers = typeMembers.stream().filter(Member::isGoalAchieved).count();
        double goalRate = (double) achievers / typeMembers.size() * 100;
        double avgFee = typeMembers.stream().mapToDouble(Member::calculateMonthlyFee).average().orElse(0);
        
        report.append(String.format("%-15s %-8d %-12.1f %-12.1f%% $%-11.2f\n", 
                typeName, typeMembers.size(), avgRating, goalRate, avgFee));
    }
    
    /**
     * Generates fee impact analysis.
     */
    private void generateFeeImpactAnalysis(StringBuilder report, List<Member> members) {
        report.append("FEE IMPACT ANALYSIS\n");
        report.append("-".repeat(50)).append("\n\n");
        
        if (members.isEmpty()) {
            report.append("No members in the system.\n");
            return;
        }
        
        // Performance vs Fee correlation
        report.append("PERFORMANCE VS FEE CORRELATION\n");
        report.append("-".repeat(40)).append("\n");
        
        List<Member> highPerformers = members.stream().filter(m -> m.getPerformanceRating() >= 8).collect(Collectors.toList());
        List<Member> lowPerformers = members.stream().filter(m -> m.getPerformanceRating() < 5).collect(Collectors.toList());
        
        if (!highPerformers.isEmpty()) {
            double avgHighFee = highPerformers.stream().mapToDouble(Member::calculateMonthlyFee).average().orElse(0);
            report.append(String.format("High Performers (8-10): Avg Fee = $%.2f\n", avgHighFee));
        }
        
        if (!lowPerformers.isEmpty()) {
            double avgLowFee = lowPerformers.stream().mapToDouble(Member::calculateMonthlyFee).average().orElse(0);
            report.append(String.format("Low Performers (0-4):  Avg Fee = $%.2f\n", avgLowFee));
        }
        
        // Goal achievement impact
        report.append("\nGOAL ACHIEVEMENT FEE IMPACT\n");
        report.append("-".repeat(40)).append("\n");
        
        List<Member> goalAchievers = members.stream().filter(Member::isGoalAchieved).collect(Collectors.toList());
        List<Member> nonAchievers = members.stream().filter(m -> !m.isGoalAchieved()).collect(Collectors.toList());
        
        if (!goalAchievers.isEmpty()) {
            double avgAchieverFee = goalAchievers.stream().mapToDouble(Member::calculateMonthlyFee).average().orElse(0);
            report.append(String.format("Goal Achievers:     Avg Fee = $%.2f\n", avgAchieverFee));
        }
        
        if (!nonAchievers.isEmpty()) {
            double avgNonAchieverFee = nonAchievers.stream().mapToDouble(Member::calculateMonthlyFee).average().orElse(0);
            report.append(String.format("Non-Achievers:      Avg Fee = $%.2f\n", avgNonAchieverFee));
        }
        
        // Revenue impact analysis
        report.append("\nREVENUE IMPACT ANALYSIS\n");
        report.append("-".repeat(40)).append("\n");
        
        double totalRevenue = members.stream().mapToDouble(Member::calculateMonthlyFee).sum();
        double potentialRevenue = members.stream().mapToDouble(Member::getBaseFee).sum();
        double discount = potentialRevenue - totalRevenue;
        
        report.append(String.format("Potential Revenue:  $%.2f\n", potentialRevenue));
        report.append(String.format("Actual Revenue:     $%.2f\n", totalRevenue));
        report.append(String.format("Total Discounts:    $%.2f\n", discount));
        report.append(String.format("Discount Rate:      %.1f%%\n", (discount / potentialRevenue) * 100));
    }
    
    // Action classes
    
    private class GenerateReportAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            generateReport();
        }
    }
    
    private class ExportReportAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            exportReport();
        }
    }
    
    private class PrintReportAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            printReport();
        }
    }
    
    /**
     * Exports the current report to a text file.
     */
    private void exportReport() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File("performance_report_" + 
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".txt"));
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                writer.print(reportArea.getText());
                JOptionPane.showMessageDialog(this, 
                        "Report exported successfully to:\n" + file.getAbsolutePath(),
                        "Export Successful", 
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, 
                        "Error exporting report: " + e.getMessage(),
                        "Export Error", 
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Prints the current report.
     */
    private void printReport() {
        try {
            boolean printed = reportArea.print();
            if (printed) {
                JOptionPane.showMessageDialog(this, 
                        "Report sent to printer successfully.",
                        "Print Successful", 
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                    "Error printing report: " + e.getMessage(),
                    "Print Error", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}