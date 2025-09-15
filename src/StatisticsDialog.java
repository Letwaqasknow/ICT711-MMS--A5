import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Stream;

/**
 * Dialog for displaying comprehensive system statistics.
 * 
 * Provides visual representation of:
 * - Member distribution by type
 * - Performance statistics
 * - Goal achievement rates
 * - Financial summaries
 * - Trend analysis
 * 
 * Uses charts, progress bars, and formatted displays for better visualization.
 */
public class StatisticsDialog extends JDialog {
    
    private EnhancedMemberManager manager;
    private DecimalFormat currencyFormat = new DecimalFormat("$#,##0.00");
    private DecimalFormat percentFormat = new DecimalFormat("#0.0%");
    
    /**
     * Constructs a new StatisticsDialog.
     * 
     * @param parent the parent frame
     * @param manager the member manager instance
     */
    public StatisticsDialog(JFrame parent, EnhancedMemberManager manager) {
        super(parent, "System Statistics", true);
        this.manager = manager;
        
        initializeComponents();
        setSize(700, 500);
        setLocationRelativeTo(parent);
        setResizable(false);
    }
    
    /**
     * Initializes all dialog components and layout.
     */
    private void initializeComponents() {
        setLayout(new BorderLayout(10, 10));
        
        // Create tabbed pane for different statistics categories
        JTabbedPane tabbedPane = new JTabbedPane();
        
        tabbedPane.addTab("Overview", createOverviewPanel());
        tabbedPane.addTab("Performance", createPerformancePanel());
        tabbedPane.addTab("Financial", createFinancialPanel());
        tabbedPane.addTab("Demographics", createDemographicsPanel());
        
        add(tabbedPane, BorderLayout.CENTER);
        
        // Close button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Creates the overview statistics panel.
     */
    private JPanel createOverviewPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.BOTH;
        
        List<Member> allMembers = manager.getAllMembers();
        
        // General statistics
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.5; gbc.weighty = 0.4;
        panel.add(createGeneralStatsPanel(allMembers), gbc);
        
        // Member type distribution
        gbc.gridx = 1; gbc.gridy = 0;
        panel.add(createMemberTypePanel(allMembers), gbc);
        
        // Quick facts
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2; gbc.weighty = 0.6;
        panel.add(createQuickFactsPanel(allMembers), gbc);
        
        return panel;
    }
    
    /**
     * Creates the general statistics panel.
     */
    private JPanel createGeneralStatsPanel(List<Member> members) {
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("General Statistics"));
        
        // Total members
        panel.add(createStatLabel("Total Members:", String.valueOf(members.size())));
        
        // Average performance
        double avgPerformance = members.stream()
                .mapToInt(Member::getPerformanceRating)
                .average()
                .orElse(0);
        panel.add(createStatLabel("Avg Performance:", String.format("%.1f/10", avgPerformance)));
        
        // Goal achievers
        long goalAchievers = members.stream().filter(Member::isGoalAchieved).count();
        panel.add(createStatLabel("Goal Achievers:", goalAchievers + " (" + 
                percentFormat.format((double) goalAchievers / members.size()) + ")"));
        
        // Total monthly revenue
        double totalRevenue = members.stream()
                .mapToDouble(Member::calculateMonthlyFee)
                .sum();
        panel.add(createStatLabel("Monthly Revenue:", currencyFormat.format(totalRevenue)));
        
        return panel;
    }
    
    /**
     * Creates the member type distribution panel.
     */
    private JPanel createMemberTypePanel(List<Member> members) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Member Types"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        long regularCount = members.stream().filter(m -> m instanceof RegularMember).count();
        long premiumCount = members.stream().filter(m -> m instanceof PremiumMember).count();
        long studentCount = members.stream().filter(m -> m instanceof StudentMember).count();
        
        int total = members.size();
        
        // Regular members
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Regular:"), gbc);
        gbc.gridx = 1;
        panel.add(createProgressBar((int) regularCount, total, Color.BLUE), gbc);
        gbc.gridx = 2;
        panel.add(new JLabel(regularCount + " (" + percentFormat.format((double) regularCount / total) + ")"), gbc);
        
        // Premium members
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Premium:"), gbc);
        gbc.gridx = 1;
        panel.add(createProgressBar((int) premiumCount, total, Color.GREEN), gbc);
        gbc.gridx = 2;
        panel.add(new JLabel(premiumCount + " (" + percentFormat.format((double) premiumCount / total) + ")"), gbc);
        
        // Student members
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Student:"), gbc);
        gbc.gridx = 1;
        panel.add(createProgressBar((int) studentCount, total, Color.ORANGE), gbc);
        gbc.gridx = 2;
        panel.add(new JLabel(studentCount + " (" + percentFormat.format((double) studentCount / total) + ")"), gbc);
        
        return panel;
    }
    
    /**
     * Creates the quick facts panel with interesting statistics.
     */
    private JPanel createQuickFactsPanel(List<Member> members) {
        JPanel panel = new JPanel(new GridLayout(2, 3, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Quick Facts"));
        
        if (!members.isEmpty()) {
            // Top performer
            Member topPerformer = members.stream()
                    .max((m1, m2) -> Integer.compare(m1.getPerformanceRating(), m2.getPerformanceRating()))
                    .orElse(null);
            
            if (topPerformer != null) {
                panel.add(createFactCard("Top Performer", 
                        topPerformer.getFullName() + " (" + topPerformer.getPerformanceRating() + "/10)",
                        new Color(144, 238, 144)));
            }
            
            // Highest payer
            Member highestPayer = members.stream()
                    .max((m1, m2) -> Double.compare(m1.calculateMonthlyFee(), m2.calculateMonthlyFee()))
                    .orElse(null);
            
            if (highestPayer != null) {
                panel.add(createFactCard("Highest Fee", 
                        highestPayer.getFullName() + " (" + currencyFormat.format(highestPayer.calculateMonthlyFee()) + ")",
                        new Color(255, 218, 185)));
            }
            
            // Performance distribution
            long highPerformers = members.stream().filter(m -> m.getPerformanceRating() >= 8).count();
            panel.add(createFactCard("High Performers", 
                    highPerformers + " members (8+ rating)",
                    new Color(173, 216, 230)));
            
            // Goal achievement rate
            long achievers = members.stream().filter(Member::isGoalAchieved).count();
            panel.add(createFactCard("Success Rate", 
                    percentFormat.format((double) achievers / members.size()) + " achieve goals",
                    new Color(255, 192, 203)));
            
            // Average fee by type
            double avgRegularFee = members.stream()
                    .filter(m -> m instanceof RegularMember)
                    .mapToDouble(Member::calculateMonthlyFee)
                    .average().orElse(0);
            panel.add(createFactCard("Avg Regular Fee", 
                    currencyFormat.format(avgRegularFee),
                    new Color(221, 160, 221)));
            
            // Premium trainers
            long uniqueTrainers = members.stream()
                    .filter(m -> m instanceof PremiumMember)
                    .map(m -> ((PremiumMember) m).getTrainerName())
                    .distinct().count();
            panel.add(createFactCard("Active Trainers", 
                    uniqueTrainers + " trainers",
                    new Color(255, 255, 224)));
        }
        
        return panel;
    }
    
    /**
     * Creates the performance analysis panel.
     */
    private JPanel createPerformancePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        List<Member> members = manager.getAllMembers();
        
        // Performance distribution chart
        panel.add(createPerformanceChart(members), BorderLayout.CENTER);
        
        // Performance summary
        panel.add(createPerformanceSummary(members), BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * Creates a performance distribution visualization.
     */
    private JPanel createPerformanceChart(List<Member> members) {
        JPanel panel = new JPanel(new GridLayout(2, 5, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Performance Distribution"));
        
        // Count members by performance rating
        int[] ratingCounts = new int[11]; // 0-10 ratings
        for (Member member : members) {
            ratingCounts[member.getPerformanceRating()]++;
        }
        
        // Create bars for each rating
        for (int i = 0; i <= 10; i++) {
            JPanel barPanel = new JPanel(new BorderLayout());
            barPanel.add(new JLabel(String.valueOf(i), JLabel.CENTER), BorderLayout.SOUTH);
            
            JProgressBar bar = new JProgressBar(JProgressBar.VERTICAL, 0, members.size());
            bar.setValue(ratingCounts[i]);
            bar.setStringPainted(true);
            bar.setString(String.valueOf(ratingCounts[i]));
            
            // Color coding
            if (i >= 8) bar.setForeground(Color.GREEN);
            else if (i >= 5) bar.setForeground(Color.ORANGE);
            else bar.setForeground(Color.RED);
            
            barPanel.add(bar, BorderLayout.CENTER);
            
            if (i < 6) {
                panel.add(barPanel);
            }
        }
        
        // Second row for ratings 6-10
        for (int i = 6; i <= 10; i++) {
            JPanel barPanel = new JPanel(new BorderLayout());
            barPanel.add(new JLabel(String.valueOf(i), JLabel.CENTER), BorderLayout.SOUTH);
            
            JProgressBar bar = new JProgressBar(JProgressBar.VERTICAL, 0, members.size());
            bar.setValue(ratingCounts[i]);
            bar.setStringPainted(true);
            bar.setString(String.valueOf(ratingCounts[i]));
            
            if (i >= 8) bar.setForeground(Color.GREEN);
            else if (i >= 5) bar.setForeground(Color.ORANGE);
            else bar.setForeground(Color.RED);
            
            barPanel.add(bar, BorderLayout.CENTER);
            panel.add(barPanel);
        }
        
        return panel;
    }
    
    /**
     * Creates performance summary statistics.
     */
    private JPanel createPerformanceSummary(List<Member> members) {
        JPanel panel = new JPanel(new GridLayout(1, 4, 10, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Performance Summary"));
        
        long excellent = members.stream().filter(m -> m.getPerformanceRating() >= 8).count();
        long good = members.stream().filter(m -> m.getPerformanceRating() >= 5 && m.getPerformanceRating() < 8).count();
        long needsImprovement = members.stream().filter(m -> m.getPerformanceRating() < 5).count();
        long achievers = members.stream().filter(Member::isGoalAchieved).count();
        
        panel.add(createSummaryCard("Excellent (8-10)", String.valueOf(excellent), Color.GREEN));
        panel.add(createSummaryCard("Good (5-7)", String.valueOf(good), Color.ORANGE));
        panel.add(createSummaryCard("Needs Work (<5)", String.valueOf(needsImprovement), Color.RED));
        panel.add(createSummaryCard("Goal Achievers", String.valueOf(achievers), Color.BLUE));
        
        return panel;
    }
    
    /**
     * Creates the financial analysis panel.
     */
    private JPanel createFinancialPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        List<Member> members = manager.getAllMembers();
        
        panel.add(createRevenueByTypePanel(members));
        panel.add(createFeeRangePanel(members));
        panel.add(createProjectionsPanel(members));
        panel.add(createTopPayersPanel(members));
        
        return panel;
    }
    
    /**
     * Creates revenue breakdown by member type.
     */
    private JPanel createRevenueByTypePanel(List<Member> members) {
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Revenue by Type"));
        
        double regularRevenue = members.stream()
                .filter(m -> m instanceof RegularMember)
                .mapToDouble(Member::calculateMonthlyFee)
                .sum();
        
        double premiumRevenue = members.stream()
                .filter(m -> m instanceof PremiumMember)
                .mapToDouble(Member::calculateMonthlyFee)
                .sum();
        
        double studentRevenue = members.stream()
                .filter(m -> m instanceof StudentMember)
                .mapToDouble(Member::calculateMonthlyFee)
                .sum();
        
        double totalRevenue = regularRevenue + premiumRevenue + studentRevenue;
        
        panel.add(new JLabel("Regular:"));
        panel.add(new JLabel(currencyFormat.format(regularRevenue)));
        
        panel.add(new JLabel("Premium:"));
        panel.add(new JLabel(currencyFormat.format(premiumRevenue)));
        
        panel.add(new JLabel("Student:"));
        panel.add(new JLabel(currencyFormat.format(studentRevenue)));
        
        panel.add(new JLabel("Total:"));
        JLabel totalLabel = new JLabel(currencyFormat.format(totalRevenue));
        totalLabel.setFont(totalLabel.getFont().deriveFont(Font.BOLD));
        panel.add(totalLabel);
        
        return panel;
    }
    
    /**
     * Creates fee range analysis panel.
     */
    private JPanel createFeeRangePanel(List<Member> members) {
        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Fee Analysis"));
        
        double[] fees = members.stream().mapToDouble(Member::calculateMonthlyFee).toArray();
        
        if (fees.length > 0) {
            java.util.Arrays.sort(fees);
            
            panel.add(new JLabel("Minimum:"));
            panel.add(new JLabel(currencyFormat.format(fees[0])));
            
            panel.add(new JLabel("Maximum:"));
            panel.add(new JLabel(currencyFormat.format(fees[fees.length - 1])));
            
            panel.add(new JLabel("Average:"));
            double average = java.util.Arrays.stream(fees).average().orElse(0);
            panel.add(new JLabel(currencyFormat.format(average)));
            
            panel.add(new JLabel("Median:"));
            double median = fees.length % 2 == 0 ? 
                    (fees[fees.length / 2 - 1] + fees[fees.length / 2]) / 2 :
                    fees[fees.length / 2];
            panel.add(new JLabel(currencyFormat.format(median)));
            
            panel.add(new JLabel("Range:"));
            panel.add(new JLabel(currencyFormat.format(fees[fees.length - 1] - fees[0])));
        }
        
        return panel;
    }
    
    /**
     * Creates revenue projections panel.
     */
    private JPanel createProjectionsPanel(List<Member> members) {
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Projections"));
        
        double monthlyRevenue = members.stream()
                .mapToDouble(Member::calculateMonthlyFee)
                .sum();
        
        panel.add(new JLabel("Monthly:"));
        panel.add(new JLabel(currencyFormat.format(monthlyRevenue)));
        
        panel.add(new JLabel("Quarterly:"));
        panel.add(new JLabel(currencyFormat.format(monthlyRevenue * 3)));
        
        panel.add(new JLabel("Annual:"));
        panel.add(new JLabel(currencyFormat.format(monthlyRevenue * 12)));
        
        panel.add(new JLabel("Avg per Member:"));
        double avgPerMember = members.isEmpty() ? 0 : monthlyRevenue / members.size();
        panel.add(new JLabel(currencyFormat.format(avgPerMember)));
        
        return panel;
    }
    
    /**
     * Creates top payers panel.
     */
    private JPanel createTopPayersPanel(List<Member> members) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Top Payers"));
        
        return panel;
    }
    
    /**
     * Creates the demographics panel.
     */
    private JPanel createDemographicsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel label = new JLabel("<html><center>Demographics information would include:<br><br>" +
                                 "• Member age distribution<br>" +
                                 "• Geographic analysis<br>" +
                                 "• Membership duration trends<br>" +
                                 "• Growth patterns<br><br>" +
                                 "This feature can be enhanced with additional<br>" +
                                 "member data fields in future versions.</center></html>", 
                                 JLabel.CENTER);
        label.setFont(label.getFont().deriveFont(Font.ITALIC));
        
        panel.add(label, BorderLayout.CENTER);
        
        return panel;
    }
    
    // Helper Methods
    
    private JPanel createStatLabel(String label, String value) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel(label), BorderLayout.WEST);
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(valueLabel.getFont().deriveFont(Font.BOLD));
        panel.add(valueLabel, BorderLayout.EAST);
        return panel;
    }
    
    private JProgressBar createProgressBar(int value, int max, Color color) {
        JProgressBar bar = new JProgressBar(0, max);
        bar.setValue(value);
        bar.setStringPainted(true);
        bar.setString(value + "/" + max);
        bar.setForeground(color);
        bar.setPreferredSize(new Dimension(100, 20));
        return bar;
    }
    
    private JPanel createFactCard(String title, String value, Color backgroundColor) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createRaisedBevelBorder(),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setBackground(backgroundColor);
        
        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 12f));
        
        JLabel valueLabel = new JLabel("<html><center>" + value + "</center></html>", JLabel.CENTER);
        valueLabel.setFont(valueLabel.getFont().deriveFont(10f));
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createSummaryCard(String title, String count, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLoweredBevelBorder());
        card.setBackground(color.brighter().brighter());
        
        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 11f));
        
        JLabel countLabel = new JLabel(count, JLabel.CENTER);
        countLabel.setFont(countLabel.getFont().deriveFont(Font.BOLD, 16f));
        countLabel.setForeground(color.darker());
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(countLabel, BorderLayout.CENTER);
        
        return card;
    }
}