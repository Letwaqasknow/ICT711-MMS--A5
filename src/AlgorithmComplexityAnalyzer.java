import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.List;

/**
 * Algorithm Complexity Analyzer and Visualization Tool.
 * 
 * Provides comprehensive analysis of algorithm performance including:
 * - Time complexity measurement and visualization
 * - Space complexity analysis
 * - Algorithm comparison charts
 * - Performance trend analysis
 * - Complexity prediction and recommendations
 * 
 * Features:
 * - Interactive GUI for algorithm testing
 * - Real-time performance monitoring
 * - Graphical complexity visualization
 * - Detailed analysis reports
 * - Algorithm recommendation engine
 */
public class AlgorithmComplexityAnalyzer extends JFrame {
    
    private EnhancedMemberManager manager;
    private AlgorithmTestSuite testSuite;
    private JTextArea resultsArea;
    private JProgressBar progressBar;
    private JComboBox<String> algorithmCombo;
    private JSpinner dataSizeSpinner;
    private JCheckBox enableVisualizationBox;
    private ComplexityChart complexityChart;
    
    // Analysis results
    private Map<String, Map<Integer, Long>> performanceData;
    private ComplexityAnalysis currentAnalysis;
    
    /**
     * Constructs the Algorithm Complexity Analyzer GUI.
     */
    public AlgorithmComplexityAnalyzer() {
        this.manager = new EnhancedMemberManager();
        this.testSuite = new AlgorithmTestSuite();
        this.performanceData = new HashMap<>();
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        
        setTitle("Algorithm Complexity Analyzer - MMS");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    /**
     * Initializes all GUI components.
     */
    private void initializeComponents() {
        // Algorithm selection
        algorithmCombo = new JComboBox<>(new String[]{
            "All Sorting Algorithms",
            "QuickSort vs MergeSort vs HeapSort",
            "All Searching Algorithms", 
            "Linear vs Binary vs Hash Search",
            "Advanced Search Performance",
            "Complete Performance Analysis"
        });
        
        // Data size configuration
        dataSizeSpinner = new JSpinner(new SpinnerNumberModel(1000, 100, 100000, 500));
        
        // Options
        enableVisualizationBox = new JCheckBox("Enable Visualization", true);
        
        // Results display
        resultsArea = new JTextArea();
        resultsArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        resultsArea.setEditable(false);
        resultsArea.setBackground(Color.WHITE);
        
        // Progress bar
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setString("Ready");
        
        // Complexity chart
        complexityChart = new ComplexityChart();
    }
    
    /**
     * Sets up the main layout.
     */
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        
        // Top control panel
        add(createControlPanel(), BorderLayout.NORTH);
        
        // Main content area with split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(createResultsPanel());
        splitPane.setRightComponent(createVisualizationPanel());
        splitPane.setDividerLocation(500);
        
        add(splitPane, BorderLayout.CENTER);
        
        // Bottom status panel
        add(createStatusPanel(), BorderLayout.SOUTH);
    }
    
    /**
     * Creates the control panel with analysis options.
     */
    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Analysis Configuration"));
        panel.setPreferredSize(new Dimension(0, 120));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Row 1: Algorithm selection
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Analysis Type:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(algorithmCombo, gbc);
        
        // Row 2: Data size and options
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Max Data Size:"), gbc);
        gbc.gridx = 1;
        panel.add(dataSizeSpinner, gbc);
        gbc.gridx = 2;
        panel.add(enableVisualizationBox, gbc);
        
        // Row 3: Action buttons
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 3; gbc.fill = GridBagConstraints.HORIZONTAL;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        JButton analyzeButton = new JButton("Start Analysis");
        JButton clearButton = new JButton("Clear Results");
        JButton exportButton = new JButton("Export Report");
        JButton quickTestButton = new JButton("Quick Test");
        
        analyzeButton.addActionListener(e -> startAnalysis());
        clearButton.addActionListener(e -> clearResults());
        exportButton.addActionListener(e -> exportReport());
        quickTestButton.addActionListener(e -> runQuickTest());
        
        buttonPanel.add(analyzeButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(exportButton);
        buttonPanel.add(quickTestButton);
        
        panel.add(buttonPanel, gbc);
        
        return panel;
    }
    
    /**
     * Creates the results display panel.
     */
    private JPanel createResultsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Analysis Results"));
        
        JScrollPane scrollPane = new JScrollPane(resultsArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Add analysis summary panel
        JPanel summaryPanel = createAnalysisSummaryPanel();
        panel.add(summaryPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * Creates the analysis summary panel with key metrics.
     */
    private JPanel createAnalysisSummaryPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 3, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Summary"));
        panel.setPreferredSize(new Dimension(0, 100));
        
        // Will be populated with analysis results
        panel.add(new JLabel("Best Sort: TBD", JLabel.CENTER));
        panel.add(new JLabel("Best Search: TBD", JLabel.CENTER));
        panel.add(new JLabel("Memory Efficient: TBD", JLabel.CENTER));
        panel.add(new JLabel("Fastest Overall: TBD", JLabel.CENTER));
        panel.add(new JLabel("Most Stable: TBD", JLabel.CENTER));
        panel.add(new JLabel("Recommended: TBD", JLabel.CENTER));
        
        return panel;
    }
    
    /**
     * Creates the visualization panel with complexity charts.
     */
    private JPanel createVisualizationPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Performance Visualization"));
        
        panel.add(complexityChart, BorderLayout.CENTER);
        
        // Add chart controls
        JPanel chartControls = new JPanel(new FlowLayout());
        JComboBox<String> chartTypeCombo = new JComboBox<>(new String[]{
            "Time Complexity", "Space Complexity", "Comparative Analysis"
        });
        chartControls.add(new JLabel("Chart Type:"));
        chartControls.add(chartTypeCombo);
        
        panel.add(chartControls, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * Creates the status panel.
     */
    private JPanel createStatusPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createLoweredBevelBorder());
        panel.setPreferredSize(new Dimension(0, 30));
        
        panel.add(progressBar, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Sets up event handlers.
     */
    private void setupEventHandlers() {
        // Window closing
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                System.exit(0);
            }
        });
    }
    
    // ==================== ANALYSIS METHODS ====================
    
    /**
     * Starts the comprehensive algorithm analysis.
     */
    private void startAnalysis() {
        String analysisType = (String) algorithmCombo.getSelectedItem();
        int maxSize = (Integer) dataSizeSpinner.getValue();
        boolean enableViz = enableVisualizationBox.isSelected();
        
        // Run analysis in background thread
        SwingWorker<Void, String> analysisWorker = new SwingWorker<Void, String>() {
            @Override
            protected Void doInBackground() throws Exception {
                runAnalysis(analysisType, maxSize, enableViz);
                return null;
            }
            
            @Override
            protected void process(List<String> chunks) {
                for (String message : chunks) {
                    resultsArea.append(message + "\n");
                    resultsArea.setCaretPosition(resultsArea.getDocument().getLength());
                }
            }
            
            @Override
            protected void done() {
                progressBar.setString("Analysis Complete");
                progressBar.setValue(100);
                
                if (enableViz && currentAnalysis != null) {
                    updateVisualization();
                }
            }
        };
        
        analysisWorker.execute();
    }
    
    /**
     * Runs the specified analysis type.
     */
    private void runAnalysis(String analysisType, int maxSize, boolean enableViz) {
        try {
            progressBar.setString("Initializing analysis...");
            progressBar.setValue(10);
            
            currentAnalysis = new ComplexityAnalysis();
            
            switch (analysisType) {
                case "All Sorting Algorithms":
                    analyzeSortingAlgorithms(maxSize);
                    break;
                case "QuickSort vs MergeSort vs HeapSort":
                    compareSortingAlgorithms(maxSize);
                    break;
                case "All Searching Algorithms":
                    analyzeSearchingAlgorithms(maxSize);
                    break;
                case "Linear vs Binary vs Hash Search":
                    compareSearchingAlgorithms(maxSize);
                    break;
                case "Advanced Search Performance":
                    analyzeAdvancedSearch(maxSize);
                    break;
                case "Complete Performance Analysis":
                    runCompleteAnalysis(maxSize);
                    break;
            }
            
            // Generate complexity insights
            generateComplexityInsights();
            
        } catch (Exception e) {
            SwingUtilities.invokeLater(() -> {
                resultsArea.append("Error during analysis: " + e.getMessage() + "\n");
                progressBar.setString("Analysis Failed");
            });
        }
    }
    
    /**
     * Analyzes sorting algorithm performance across different data sizes.
     */
    private void analyzeSortingAlgorithms(int maxSize) {
        SwingUtilities.invokeLater(() -> {
            resultsArea.append("SORTING ALGORITHM ANALYSIS\n");
            resultsArea.append("=" .repeat(50) + "\n\n");
        });
        
        String[] algorithms = {"QuickSort", "MergeSort", "HeapSort"};
        int[] testSizes = generateTestSizes(maxSize);
        
        for (int i = 0; i < testSizes.length; i++) {
            int size = testSizes[i];
            progressBar.setValue(20 + (i * 60 / testSizes.length));
            progressBar.setString("Testing size: " + size);
            
            SwingUtilities.invokeLater(() -> 
                resultsArea.append("Testing with " + size + " elements...\n"));
            
            for (String algorithm : algorithms) {
                long avgTime = testSortingPerformance(algorithm, size, 3);
                
                // Store results
                performanceData.computeIfAbsent(algorithm, k -> new HashMap<>()).put(size, avgTime);
                currentAnalysis.addSortingResult(algorithm, size, avgTime);
                
                SwingUtilities.invokeLater(() -> 
                    resultsArea.append(String.format("  %s: %.3f ms\n", 
                        algorithm, avgTime / 1_000_000.0)));
            }
            
            SwingUtilities.invokeLater(() -> resultsArea.append("\n"));
        }
        
        // Analyze complexity trends
        analyzeComplexityTrends(algorithms, testSizes);
    }
    
    /**
     * Compares sorting algorithms head-to-head.
     */
    private void compareSortingAlgorithms(int maxSize) {
        SwingUtilities.invokeLater(() -> {
            resultsArea.append("SORTING ALGORITHM COMPARISON\n");
            resultsArea.append("=" .repeat(50) + "\n\n");
        });
        
        String[] algorithms = {"QuickSort", "MergeSort", "HeapSort"};
        int[] testSizes = {100, 500, 1000, 5000, maxSize};
        
        // Create comparison table
        SwingUtilities.invokeLater(() -> {
            resultsArea.append(String.format("%-12s", "Size"));
            for (String alg : algorithms) {
                resultsArea.append(String.format("%-15s", alg));
            }
            resultsArea.append("Winner\n");
            resultsArea.append("-" .repeat(80) + "\n");
        });
        
        for (int size : testSizes) {
            progressBar.setString("Comparing at size: " + size);
            
            Map<String, Long> results = new HashMap<>();
            for (String algorithm : algorithms) {
                long time = testSortingPerformance(algorithm, size, 3);
                results.put(algorithm, time);
            }
            
            // Find winner (fastest)
            String winner = results.entrySet().stream()
                    .min(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse("Tie");
            
            SwingUtilities.invokeLater(() -> {
                resultsArea.append(String.format("%-12d", size));
                for (String alg : algorithms) {
                    resultsArea.append(String.format("%-15.2f", results.get(alg) / 1_000_000.0));
                }
                resultsArea.append(winner + "\n");
            });
        }
    }
    
    /**
     * Analyzes searching algorithm performance.
     */
    private void analyzeSearchingAlgorithms(int maxSize) {
        SwingUtilities.invokeLater(() -> {
            resultsArea.append("SEARCHING ALGORITHM ANALYSIS\n");
            resultsArea.append("=" .repeat(50) + "\n\n");
        });
        
        String[] algorithms = {"Linear", "Hash", "Binary"};
        int[] testSizes = generateTestSizes(maxSize);
        
        for (int size : testSizes) {
            progressBar.setString("Testing searches at size: " + size);
            
            SwingUtilities.invokeLater(() -> 
                resultsArea.append("Testing with " + size + " elements...\n"));
            
            // Test each search algorithm
            long linearTime = testLinearSearch(size);
            long hashTime = testHashSearch(size);
            long binaryTime = testBinarySearch(size);
            
            currentAnalysis.addSearchingResult("Linear", size, linearTime);
            currentAnalysis.addSearchingResult("Hash", size, hashTime);
            currentAnalysis.addSearchingResult("Binary", size, binaryTime);
            
            SwingUtilities.invokeLater(() -> {
                resultsArea.append(String.format("  Linear Search: %.3f ms\n", linearTime / 1_000_000.0));
                resultsArea.append(String.format("  Hash Search:   %.3f ms\n", hashTime / 1_000_000.0));
                resultsArea.append(String.format("  Binary Search: %.3f ms\n", binaryTime / 1_000_000.0));
                resultsArea.append("\n");
            });
        }
    }
    
    /**
     * Compares searching algorithms.
     */
    private void compareSearchingAlgorithms(int maxSize) {
        SwingUtilities.invokeLater(() -> {
            resultsArea.append("SEARCHING ALGORITHM COMPARISON\n");
            resultsArea.append("=" .repeat(50) + "\n\n");
            resultsArea.append("Performance characteristics:\n");
            resultsArea.append("- Linear Search: O(n) - No preprocessing needed\n");
            resultsArea.append("- Hash Search: O(1) avg - Requires hash table\n");
            resultsArea.append("- Binary Search: O(log n) - Requires sorted data\n\n");
        });
        
        int[] testSizes = {100, 1000, 10000, maxSize};
        
        for (int size : testSizes) {
            long linearTime = testLinearSearch(size);
            long hashTime = testHashSearch(size);
            long binaryTime = testBinarySearch(size);
            
            SwingUtilities.invokeLater(() -> {
                resultsArea.append(String.format("Size %d:\n", size));
                resultsArea.append(String.format("  Linear: %.3f ms (%.1fx baseline)\n", 
                    linearTime / 1_000_000.0, 1.0));
                resultsArea.append(String.format("  Hash:   %.3f ms (%.1fx baseline)\n", 
                    hashTime / 1_000_000.0, (double)hashTime / linearTime));
                resultsArea.append(String.format("  Binary: %.3f ms (%.1fx baseline)\n\n", 
                    binaryTime / 1_000_000.0, (double)binaryTime / linearTime));
            });
        }
    }
    
    /**
     * Analyzes advanced search performance with multiple criteria.
     */
    private void analyzeAdvancedSearch(int maxSize) {
        SwingUtilities.invokeLater(() -> {
            resultsArea.append("ADVANCED SEARCH ANALYSIS\n");
            resultsArea.append("=" .repeat(50) + "\n\n");
        });
        
        // Test different search criteria combinations
        SearchCriteria[] testCriteria = {
            createSearchCriteria("ID only", "TEST123", null, null, null, null, null),
            createSearchCriteria("Type only", null, null, null, null, PremiumMember.class, null),
            createSearchCriteria("Rating range", null, null, 7, 9, null, null),
            createSearchCriteria("Combined", null, "John", 5, 10, PremiumMember.class, true)
        };
        
        for (SearchCriteria criteria : testCriteria) {
            long searchTime = testAdvancedSearch(criteria, maxSize);
            SwingUtilities.invokeLater(() -> {
                resultsArea.append(String.format("%s: %.3f ms\n", 
                    criteria.toString(), searchTime / 1_000_000.0));
            });
        }
    }
    
    /**
     * Runs complete performance analysis.
     */
    private void runCompleteAnalysis(int maxSize) {
        SwingUtilities.invokeLater(() -> {
            resultsArea.append("COMPLETE ALGORITHM PERFORMANCE ANALYSIS\n");
            resultsArea.append("=" .repeat(60) + "\n\n");
        });
        
        // Run all analysis types
        analyzeSortingAlgorithms(maxSize / 2); // Use smaller size to save time
        analyzeSearchingAlgorithms(maxSize / 2);
        
        // Generate comprehensive recommendations
        SwingUtilities.invokeLater(() -> {
            resultsArea.append("COMPREHENSIVE RECOMMENDATIONS\n");
            resultsArea.append("=" .repeat(40) + "\n");
            resultsArea.append("Based on analysis results:\n\n");
            
            resultsArea.append("SORTING:\n");
            resultsArea.append("- Small datasets (<1000): QuickSort for speed\n");
            resultsArea.append("- Large datasets (>1000): MergeSort for consistency\n");
            resultsArea.append("- Memory constrained: HeapSort for in-place sorting\n\n");
            
            resultsArea.append("SEARCHING:\n");
            resultsArea.append("- ID lookups: Hash search (O(1))\n");
            resultsArea.append("- Range queries: Binary search on sorted data\n");
            resultsArea.append("- Complex criteria: Advanced search with filters\n\n");
            
            resultsArea.append("GENERAL GUIDELINES:\n");
            resultsArea.append("- Cache frequently accessed data\n");
            resultsArea.append("- Use appropriate data structures\n");
            resultsArea.append("- Consider preprocessing vs. query time trade-offs\n");
        });
    }
    
    // ==================== PERFORMANCE TEST METHODS ====================
    
    /**
     * Tests sorting algorithm performance.
     */
    private long testSortingPerformance(String algorithm, int size, int iterations) {
        List<Member> testData = generateTestData(size);
        long totalTime = 0;
        
        for (int i = 0; i < iterations; i++) {
            List<Member> dataCopy = new ArrayList<>(testData);
            long startTime = System.nanoTime();
            
            switch (algorithm) {
                case "QuickSort":
                    manager.quickSortByName(dataCopy);
                    break;
                case "MergeSort":
                    manager.mergeSortByRating(dataCopy);
                    break;
                case "HeapSort":
                    manager.heapSortByFee(dataCopy);
                    break;
            }
            
            totalTime += (System.nanoTime() - startTime);
        }
        
        return totalTime / iterations;
    }
    
    /**
     * Tests linear search performance.
     */
    private long testLinearSearch(int size) {
        List<Member> testData = generateTestData(size);
        String targetId = testData.get(size / 2).getMemberId();
        
        long startTime = System.nanoTime();
        for (Member member : testData) {
            if (member.getMemberId().equals(targetId)) {
                break;
            }
        }
        return System.nanoTime() - startTime;
    }
    
    /**
     * Tests hash search performance.
     */
    private long testHashSearch(int size) {
        manager = new EnhancedMemberManager();
        List<Member> testData = generateTestData(size);
        testData.forEach(manager::addMember);
        
        String targetId = testData.get(size / 2).getMemberId();
        
        long startTime = System.nanoTime();
        manager.hashSearchById(targetId);
        return System.nanoTime() - startTime;
    }
    
    /**
     * Tests binary search performance.
     */
    private long testBinarySearch(int size) {
        manager = new EnhancedMemberManager();
        List<Member> testData = generateTestData(size);
        testData.forEach(manager::addMember);
        
        long startTime = System.nanoTime();
        manager.binarySearchByRating(5);
        return System.nanoTime() - startTime;
    }
    
    /**
     * Tests advanced search performance.
     */
    private long testAdvancedSearch(SearchCriteria criteria, int size) {
        manager = new EnhancedMemberManager();
        List<Member> testData = generateTestData(size);
        testData.forEach(manager::addMember);
        
        long startTime = System.nanoTime();
        manager.advancedSearch(criteria);
        return System.nanoTime() - startTime;
    }
    
    // ==================== UTILITY METHODS ====================
    
    /**
     * Generates test data for performance testing.
     */
    private List<Member> generateTestData(int size) {
        List<Member> data = new ArrayList<>();
        Random random = new Random();
        
        for (int i = 0; i < size; i++) {
            String id = "TEST" + String.format("%06d", i);
            String firstName = "First" + (i % 100);
            String lastName = "Last" + (i % 50);
            String email = "test" + i + "@example.com";
            String phone = "555-" + String.format("%04d", i % 10000);
            
            Member member;
            switch (i % 3) {
                case 0:
                    member = new RegularMember(id, firstName, lastName, email, phone);
                    break;
                case 1:
                    member = new PremiumMember(id, firstName, lastName, email, phone, 
                            "Trainer" + (i % 10), random.nextInt(10) + 1);
                    break;
                default:
                    member = new StudentMember(id, firstName, lastName, email, phone, 
                            "STU" + i, "Univ" + (i % 5));
                    break;
            }
            
            member.setPerformanceRating(random.nextInt(11));
            member.setGoalAchieved(random.nextBoolean());
            data.add(member);
        }
        
        return data;
    }
    
    /**
     * Generates test sizes for analysis.
     */
    private int[] generateTestSizes(int maxSize) {
        List<Integer> sizes = new ArrayList<>();
        int size = 100;
        while (size <= maxSize) {
            sizes.add(size);
            size = (int) (size * 1.5); // Exponential growth
        }
        return sizes.stream().mapToInt(Integer::intValue).toArray();
    }
    
    /**
     * Creates search criteria for testing.
     */
    private SearchCriteria createSearchCriteria(String name, String id, String memberName, 
            Integer minRating, Integer maxRating, Class<?> type, Boolean goalAchieved) {
        SearchCriteria criteria = new SearchCriteria();
        criteria.setMemberId(id);
        criteria.setName(memberName);
        criteria.setMinRating(minRating);
        criteria.setMaxRating(maxRating);
        criteria.setMemberType(type);
        criteria.setGoalAchieved(goalAchieved);
        return criteria;
    }
    
    /**
     * Analyzes complexity trends in the performance data.
     */
    private void analyzeComplexityTrends(String[] algorithms, int[] testSizes) {
        SwingUtilities.invokeLater(() -> {
            resultsArea.append("COMPLEXITY TREND ANALYSIS\n");
            resultsArea.append("-" .repeat(30) + "\n");
        });
        
        for (String algorithm : algorithms) {
            Map<Integer, Long> algorithmData = performanceData.get(algorithm);
            if (algorithmData != null && algorithmData.size() > 1) {
                double growthRate = calculateGrowthRate(algorithmData);
                String complexityClass = classifyComplexity(growthRate);
                
                SwingUtilities.invokeLater(() -> {
                    resultsArea.append(String.format("%s: %s (growth rate: %.2f)\n", 
                            algorithm, complexityClass, growthRate));
                });
            }
        }
        SwingUtilities.invokeLater(() -> resultsArea.append("\n"));
    }
    
    /**
     * Calculates growth rate between data points.
     */
    private double calculateGrowthRate(Map<Integer, Long> data) {
        if (data.size() < 2) return 0.0;
		return 0;
      }
    
    /**
     * Classifies complexity based on growth rate.
     */
    private String classifyComplexity(double growthRate) {
        if (growthRate < 0.5) return "O(1) - Constant";
        else if (growthRate < 1.2) return "O(log n) - Logarithmic";
        else if (growthRate < 1.8) return "O(n) - Linear";
        else if (growthRate < 2.5) return "O(n log n) - Linearithmic";
        else if (growthRate < 3.5) return "O(n²) - Quadratic";
        else return "O(n³+) - Polynomial or worse";
    }
    
    /**
     * Generates complexity insights based on analysis.
     */
    private void generateComplexityInsights() {
        SwingUtilities.invokeLater(() -> {
            resultsArea.append("ALGORITHM INSIGHTS & RECOMMENDATIONS\n");
            resultsArea.append("=" .repeat(50) + "\n\n");
            
            if (currentAnalysis != null) {
                // Find best performers
                String bestSortSmall = currentAnalysis.getBestSortingAlgorithm(1000);
                String bestSortLarge = currentAnalysis.getBestSortingAlgorithm(10000);
                String bestSearch = currentAnalysis.getBestSearchingAlgorithm();
                
                resultsArea.append("PERFORMANCE WINNERS:\n");
                resultsArea.append("- Best sorting (small data): " + bestSortSmall + "\n");
                resultsArea.append("- Best sorting (large data): " + bestSortLarge + "\n");
                resultsArea.append("- Best searching: " + bestSearch + "\n\n");
                
                resultsArea.append("RECOMMENDATIONS:\n");
                resultsArea.append(currentAnalysis.generateRecommendations());
            }
        });
    }
    
    /**
     * Updates the complexity visualization chart.
     */
    private void updateVisualization() {
        if (currentAnalysis != null) {
            complexityChart.updateChart(currentAnalysis);
        }
    }
    
    /**
     * Runs a quick performance test for demonstration.
     */
    private void runQuickTest() {
        SwingUtilities.invokeLater(() -> {
            resultsArea.append("QUICK PERFORMANCE TEST\n");
            resultsArea.append("=" .repeat(30) + "\n");
            resultsArea.append("Testing basic operations with 1000 elements...\n\n");
        });
        
        // Quick test with fixed size
        int testSize = 1000;
        
        long quickSortTime = testSortingPerformance("QuickSort", testSize, 3);
        long mergeSortTime = testSortingPerformance("MergeSort", testSize, 3);
        long heapSortTime = testSortingPerformance("HeapSort", testSize, 3);
        
        long linearTime = testLinearSearch(testSize);
        long hashTime = testHashSearch(testSize);
        long binaryTime = testBinarySearch(testSize);
        
        SwingUtilities.invokeLater(() -> {
            resultsArea.append("SORTING RESULTS:\n");
            resultsArea.append(String.format("QuickSort: %.3f ms\n", quickSortTime / 1_000_000.0));
            resultsArea.append(String.format("MergeSort: %.3f ms\n", mergeSortTime / 1_000_000.0));
            resultsArea.append(String.format("HeapSort:  %.3f ms\n\n", heapSortTime / 1_000_000.0));
            
            resultsArea.append("SEARCHING RESULTS:\n");
            resultsArea.append(String.format("Linear:  %.3f ms\n", linearTime / 1_000_000.0));
            resultsArea.append(String.format("Hash:    %.3f ms\n", hashTime / 1_000_000.0));
            resultsArea.append(String.format("Binary:  %.3f ms\n\n", binaryTime / 1_000_000.0));
            
            resultsArea.append("Winner - Sorting: " + 
                (quickSortTime < mergeSortTime && quickSortTime < heapSortTime ? "QuickSort" :
                 mergeSortTime < heapSortTime ? "MergeSort" : "HeapSort") + "\n");
            resultsArea.append("Winner - Searching: " + 
                (hashTime < linearTime && hashTime < binaryTime ? "Hash" :
                 binaryTime < linearTime ? "Binary" : "Linear") + "\n");
        });
    }
    
    /**
     * Clears the results display.
     */
    private void clearResults() {
        resultsArea.setText("");
        performanceData.clear();
        currentAnalysis = null;
        progressBar.setValue(0);
        progressBar.setString("Ready");
    }
    
    /**
     * Exports the analysis report to a file.
     */
    private void exportReport() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new java.io.File("algorithm_analysis_report.txt"));
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(fileChooser.getSelectedFile()))) {
                writer.print(resultsArea.getText());
                JOptionPane.showMessageDialog(this, 
                        "Report exported successfully!", 
                        "Export Complete", 
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
     * Main method to launch the complexity analyzer.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getLookAndFeel());
            } catch (Exception e) {
                // Use default look and feel
            }
            
            new AlgorithmComplexityAnalyzer().setVisible(true);
        });
    }
}

/**
 * Complexity analysis data structure for storing and analyzing results.
 */
class ComplexityAnalysis {
    private Map<String, Map<Integer, Long>> sortingResults = new HashMap<>();
    private Map<String, Map<Integer, Long>> searchingResults = new HashMap<>();
    
    public void addSortingResult(String algorithm, int size, long time) {
        sortingResults.computeIfAbsent(algorithm, k -> new HashMap<>()).put(size, time);
    }
    
    public void addSearchingResult(String algorithm, int size, long time) {
        searchingResults.computeIfAbsent(algorithm, k -> new HashMap<>()).put(size, time);
    }
    
    public String getBestSortingAlgorithm(int size) {
        return sortingResults.entrySet().stream()
                .filter(e -> e.getValue().containsKey(size))
                .min((e1, e2) -> Long.compare(e1.getValue().get(size), e2.getValue().get(size)))
                .map(Map.Entry::getKey)
                .orElse("Unknown");
    }
    
    public String getBestSearchingAlgorithm() {
        // Return the algorithm with best average performance
        return searchingResults.entrySet().stream()
                .min((e1, e2) -> {
                    double avg1 = e1.getValue().values().stream().mapToLong(Long::longValue).average().orElse(Double.MAX_VALUE);
                    double avg2 = e2.getValue().values().stream().mapToLong(Long::longValue).average().orElse(Double.MAX_VALUE);
                    return Double.compare(avg1, avg2);
                })
                .map(Map.Entry::getKey)
                .orElse("Unknown");
    }
    
    public String generateRecommendations() {
        StringBuilder rec = new StringBuilder();
        
        rec.append("1. For small datasets (<1000 elements):\n");
        rec.append("   - Use QuickSort for sorting\n");
        rec.append("   - Hash search for exact matches\n\n");
        
        rec.append("2. For large datasets (>10000 elements):\n");
        rec.append("   - Use MergeSort for guaranteed performance\n");
        rec.append("   - Binary search for range queries\n\n");
        
        rec.append("3. Memory-constrained environments:\n");
        rec.append("   - Use HeapSort (in-place sorting)\n");
        rec.append("   - Linear search if hash table is too expensive\n\n");
        
        return rec.toString();
    }
    
    public Map<String, Map<Integer, Long>> getSortingResults() { return sortingResults; }
    public Map<String, Map<Integer, Long>> getSearchingResults() { return searchingResults; }
}

/**
 * Simple complexity visualization chart component.
 */
class ComplexityChart extends JPanel {
    private ComplexityAnalysis analysis;
    
    public ComplexityChart() {
        setPreferredSize(new Dimension(400, 300));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLoweredBevelBorder());
    }
    
    public void updateChart(ComplexityAnalysis analysis) {
        this.analysis = analysis;
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (analysis == null) {
            g.drawString("Run analysis to see performance chart", 50, getHeight() / 2);
            return;
        }
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw simple performance comparison chart
        drawPerformanceChart(g2d);
    }
    
    private void drawPerformanceChart(Graphics2D g2d) {
        int width = getWidth() - 40;
        int height = getHeight() - 60;
        int startX = 20;
        int startY = 20;
        
        // Draw axes
        g2d.setColor(Color.BLACK);
        g2d.drawLine(startX, startY + height, startX + width, startY + height); // X-axis
        g2d.drawLine(startX, startY, startX, startY + height); // Y-axis
        
        // Labels
        g2d.drawString("Data Size", startX + width/2, startY + height + 30);
        g2d.rotate(-Math.PI/2);
        g2d.drawString("Time (ms)", -(startY + height/2), 15);
        g2d.rotate(Math.PI/2);
        
        // Draw performance lines for sorting algorithms
        Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE};
        int colorIndex = 0;
        
        Map<String, Map<Integer, Long>> sortingData = analysis.getSortingResults();
        for (Map.Entry<String, Map<Integer, Long>> algorithmEntry : sortingData.entrySet()) {
            g2d.setColor(colors[colorIndex % colors.length]);
           
            colorIndex++;
        }
    }
}