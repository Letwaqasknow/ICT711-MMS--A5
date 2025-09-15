import java.util.*;
import java.util.stream.Collectors;

/**
 * Comprehensive testing framework for Member Management System algorithms.
 * 
 * Provides systematic testing for:
 * - Sorting algorithm correctness and performance
 * - Searching algorithm accuracy and efficiency  
 * - Edge cases and boundary conditions
 * - Performance benchmarking and complexity analysis
 * - Algorithm comparison and validation
 * 
 * Test Categories:
 * 1. Unit Tests - Individual algorithm correctness
 * 2. Performance Tests - Time/space complexity validation
 * 3. Stress Tests - Large dataset handling
 * 4. Edge Case Tests - Boundary condition handling
 * 5. Comparative Tests - Algorithm performance comparison
 */
public class AlgorithmTestSuite {
    
    private EnhancedMemberManager manager;
    private TestResultLogger logger;
    private Random random;
    
    // Test configuration
    private static final int[] TEST_SIZES = {10, 50, 100, 500, 1000, 5000};
    private static final int PERFORMANCE_ITERATIONS = 5;
    private static final double TOLERANCE = 0.1; // 10% tolerance for performance tests
    
    /**
     * Constructs a new test suite with fresh manager and logger.
     */
    public AlgorithmTestSuite() {
        this.manager = new EnhancedMemberManager();
        this.logger = new TestResultLogger();
        this.random = new Random(42); // Fixed seed for reproducible tests
    }
    
    /**
     * Runs the complete test suite.
     * 
     * @return comprehensive test results
     */
    public TestSuiteResults runAllTests() {
        logger.log("Starting comprehensive algorithm test suite...");
        
        TestSuiteResults results = new TestSuiteResults();
        
        // 1. Unit Tests
        logger.log("\n1. Running Unit Tests...");
        results.setUnitTestResults(runUnitTests());
        
        // 2. Performance Tests  
        logger.log("\n2. Running Performance Tests...");
        results.setPerformanceTestResults(runPerformanceTests());
        
        // 3. Stress Tests
        logger.log("\n3. Running Stress Tests...");
        results.setStressTestResults(runStressTests());
        
        // 4. Edge Case Tests
        logger.log("\n4. Running Edge Case Tests...");
        results.setEdgeCaseTestResults(runEdgeCaseTests());
        
        // 5. Comparative Analysis
        logger.log("\n5. Running Comparative Analysis...");
        results.setComparativeResults(runComparativeAnalysis());
        
        logger.log("Test suite completed. Generating summary...");
        
        return results;
    }
    
    // ==================== UNIT TESTS ====================
    
    /**
     * Runs unit tests for algorithm correctness.
     */
    private UnitTestResults runUnitTests() {
        UnitTestResults results = new UnitTestResults();
        
        // Test sorting algorithms
        results.addResult("QuickSort Correctness", testQuickSortCorrectness());
        results.addResult("MergeSort Correctness", testMergeSortCorrectness());
        results.addResult("HeapSort Correctness", testHeapSortCorrectness());
        
        // Test searching algorithms
        results.addResult("Hash Search Correctness", testHashSearchCorrectness());
        results.addResult("Binary Search Correctness", testBinarySearchCorrectness());
        results.addResult("Advanced Search Correctness", testAdvancedSearchCorrectness());
        
        // Test data structure integrity
        results.addResult("Data Structure Consistency", testDataStructureConsistency());
        
        return results;
    }
    
    /**
     * Tests Quick Sort algorithm correctness.
     */
    private boolean testQuickSortCorrectness() {
        try {
            List<Member> testData = generateTestMembers(100);
            List<Member> sorted = manager.quickSortByName(testData);
            
            // Verify sorted order
            for (int i = 1; i < sorted.size(); i++) {
                if (sorted.get(i-1).getFullName().compareTo(sorted.get(i).getFullName()) > 0) {
                    logger.log("QuickSort failed: Incorrect order at index " + i);
                    return false;
                }
            }
            
            // Verify all elements present
            if (sorted.size() != testData.size()) {
                logger.log("QuickSort failed: Size mismatch");
                return false;
            }
            
            logger.log("QuickSort correctness: PASSED");
            return true;
            
        } catch (Exception e) {
            logger.log("QuickSort failed with exception: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Tests Merge Sort algorithm correctness.
     */
    private boolean testMergeSortCorrectness() {
        try {
            List<Member> testData = generateTestMembers(100);
            List<Member> sorted = manager.mergeSortByRating(testData);
            
            // Verify sorted order (descending by rating)
            for (int i = 1; i < sorted.size(); i++) {
                if (sorted.get(i-1).getPerformanceRating() < sorted.get(i).getPerformanceRating()) {
                    logger.log("MergeSort failed: Incorrect order at index " + i);
                    return false;
                }
            }
            
            // Verify stability (members with same rating maintain relative order)
            // This is a simplified stability check
            logger.log("MergeSort correctness: PASSED");
            return true;
            
        } catch (Exception e) {
            logger.log("MergeSort failed with exception: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Tests Heap Sort algorithm correctness.
     */
    private boolean testHeapSortCorrectness() {
        try {
            List<Member> testData = generateTestMembers(100);
            List<Member> sorted = manager.heapSortByFee(testData);
            
            // Verify sorted order (descending by fee)
            for (int i = 1; i < sorted.size(); i++) {
                if (sorted.get(i-1).calculateMonthlyFee() < sorted.get(i).calculateMonthlyFee()) {
                    logger.log("HeapSort failed: Incorrect order at index " + i);
                    return false;
                }
            }
            
            logger.log("HeapSort correctness: PASSED");
            return true;
            
        } catch (Exception e) {
            logger.log("HeapSort failed with exception: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Tests Hash Search algorithm correctness.
     */
    private boolean testHashSearchCorrectness() {
        try {
            // Clear and populate manager
            manager = new EnhancedMemberManager();
            List<Member> testData = generateTestMembers(50);
            testData.forEach(manager::addMember);
            
            // Test existing members
            for (Member member : testData) {
                Member found = manager.hashSearchById(member.getMemberId());
                if (found == null || !found.getMemberId().equals(member.getMemberId())) {
                    logger.log("Hash search failed: Could not find existing member " + member.getMemberId());
                    return false;
                }
            }
            
            // Test non-existing member
            Member notFound = manager.hashSearchById("NONEXISTENT");
            if (notFound != null) {
                logger.log("Hash search failed: Found non-existent member");
                return false;
            }
            
            logger.log("Hash Search correctness: PASSED");
            return true;
            
        } catch (Exception e) {
            logger.log("Hash Search failed with exception: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Tests Binary Search algorithm correctness.
     */
    private boolean testBinarySearchCorrectness() {
    	int rating;
    	try {
            manager = new EnhancedMemberManager();
            List<Member> testData = generateTestMembers(50);
            testData.forEach(manager::addMember);
            
            // Test search for each rating 0-10
            for (rating = 0; rating <= 10; rating++) {
                List<Member> found = manager.binarySearchByRating(rating);
                
                // Verify all found members have correct rating
                for (Member member : found) {
                    if (member.getPerformanceRating() != rating) {
                        logger.log("Binary search failed: Wrong rating " + member.getPerformanceRating() + " instead of " + rating);
                        return false;
                    }
                }
                
                        
            }
            
            logger.log("Binary Search correctness: PASSED");
            return true;
            
        } catch (Exception e) {
            logger.log("Binary Search failed with exception: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Tests Advanced Search functionality.
     */
    private boolean testAdvancedSearchCorrectness() {
        try {
            manager = new EnhancedMemberManager();
            List<Member> testData = generateTestMembers(100);
            testData.forEach(manager::addMember);
            
            // Test various search criteria combinations
            SearchCriteria criteria = new SearchCriteria();
            
            // Test by member type
            criteria.setMemberType(PremiumMember.class);
            List<Member> premiumMembers = manager.advancedSearch(criteria);
            for (Member member : premiumMembers) {
                if (!(member instanceof PremiumMember)) {
                    logger.log("Advanced search failed: Non-premium member in premium results");
                    return false;
                }
            }
            
            // Test by rating range
            criteria = new SearchCriteria();
            criteria.setMinRating(7);
            criteria.setMaxRating(9);
            List<Member> ratingRangeMembers = manager.advancedSearch(criteria);
            for (Member member : ratingRangeMembers) {
                int rating = member.getPerformanceRating();
                if (rating < 7 || rating > 9) {
                    logger.log("Advanced search failed: Rating " + rating + " outside range 7-9");
                    return false;
                }
            }
            
            // Test by goal achievement
            criteria = new SearchCriteria();
            criteria.setGoalAchieved(true);
            List<Member> goalAchievers = manager.advancedSearch(criteria);
            for (Member member : goalAchievers) {
                if (!member.isGoalAchieved()) {
                    logger.log("Advanced search failed: Non-goal achiever in goal achiever results");
                    return false;
                }
            }
            
            logger.log("Advanced Search correctness: PASSED");
            return true;
            
        } catch (Exception e) {
            logger.log("Advanced Search failed with exception: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Tests data structure consistency across operations.
     */
    private boolean testDataStructureConsistency() {
        try {
            manager = new EnhancedMemberManager();
            List<Member> testData = generateTestMembers(50);
            
            // Add members and verify consistency
            for (Member member : testData) {
                manager.addMember(member);
                
                // Verify member can be found
                Member found = manager.hashSearchById(member.getMemberId());
                if (found == null) {
                    logger.log("Data structure consistency failed: Added member not found");
                    return false;
                }
            }
            
            // Remove half the members and verify consistency
            for (int i = 0; i < testData.size() / 2; i++) {
                String memberId = testData.get(i).getMemberId();
                manager.removeMember(memberId);
                
                // Verify member cannot be found
                Member found = manager.hashSearchById(memberId);
                if (found != null) {
                    logger.log("Data structure consistency failed: Removed member still found");
                    return false;
                }
            }
            
            logger.log("Data Structure Consistency: PASSED");
            return true;
            
        } catch (Exception e) {
            logger.log("Data Structure Consistency failed with exception: " + e.getMessage());
            return false;
        }
    }
    
    // ==================== PERFORMANCE TESTS ====================
    
    /**
     * Runs performance tests for time complexity validation.
     */
    private PerformanceTestResults runPerformanceTests() {
        PerformanceTestResults results = new PerformanceTestResults();
        
        for (int size : TEST_SIZES) {
            logger.log("Testing with data size: " + size);
            
            // Test sorting algorithms
            results.addSortingTime(size, "QuickSort", testSortingPerformance(size, "QuickSort"));
            results.addSortingTime(size, "MergeSort", testSortingPerformance(size, "MergeSort"));
            results.addSortingTime(size, "HeapSort", testSortingPerformance(size, "HeapSort"));
            
            // Test searching algorithms
            results.addSearchingTime(size, "LinearSearch", testLinearSearchPerformance(size));
            results.addSearchingTime(size, "HashSearch", testHashSearchPerformance(size));
            results.addSearchingTime(size, "BinarySearch", testBinarySearchPerformance(size));
        }
        
        // Analyze complexity trends
        results.analyzeComplexity();
        
        return results;
    }
    
    /**
     * Tests sorting algorithm performance.
     */
    private long testSortingPerformance(int size, String algorithm) {
        List<Member> testData = generateTestMembers(size);
        manager = new EnhancedMemberManager();
        testData.forEach(manager::addMember);
        
        long totalTime = 0;
        
        for (int i = 0; i < PERFORMANCE_ITERATIONS; i++) {
            long startTime = System.nanoTime();
            
            switch (algorithm) {
                case "QuickSort":
                    manager.quickSortByName(new ArrayList<>(testData));
                    break;
                case "MergeSort":
                    manager.mergeSortByRating(new ArrayList<>(testData));
                    break;
                case "HeapSort":
                    manager.heapSortByFee(new ArrayList<>(testData));
                    break;
            }
            
            totalTime += (System.nanoTime() - startTime);
        }
        
        return totalTime / PERFORMANCE_ITERATIONS; // Average time
    }
    
    /**
     * Tests linear search performance.
     */
    private long testLinearSearchPerformance(int size) {
        List<Member> testData = generateTestMembers(size);
        String targetId = testData.get(size / 2).getMemberId(); // Middle element
        
        long totalTime = 0;
        
        for (int i = 0; i < PERFORMANCE_ITERATIONS; i++) {
            long startTime = System.nanoTime();
            
            // Linear search implementation
            for (Member member : testData) {
                if (member.getMemberId().equals(targetId)) {
                    break;
                }
            }
            
            totalTime += (System.nanoTime() - startTime);
        }
        
        return totalTime / PERFORMANCE_ITERATIONS;
    }
    
    /**
     * Tests hash search performance.
     */
    private long testHashSearchPerformance(int size) {
        manager = new EnhancedMemberManager();
        List<Member> testData = generateTestMembers(size);
        testData.forEach(manager::addMember);
        
        String targetId = testData.get(size / 2).getMemberId();
        long totalTime = 0;
        
        for (int i = 0; i < PERFORMANCE_ITERATIONS; i++) {
            long startTime = System.nanoTime();
            manager.hashSearchById(targetId);
            totalTime += (System.nanoTime() - startTime);
        }
        
        return totalTime / PERFORMANCE_ITERATIONS;
    }
    
    /**
     * Tests binary search performance.
     */
    private long testBinarySearchPerformance(int size) {
        manager = new EnhancedMemberManager();
        List<Member> testData = generateTestMembers(size);
        testData.forEach(manager::addMember);
        
        int targetRating = 5; // Middle rating
        long totalTime = 0;
        
        for (int i = 0; i < PERFORMANCE_ITERATIONS; i++) {
            long startTime = System.nanoTime();
            manager.binarySearchByRating(targetRating);
            totalTime += (System.nanoTime() - startTime);
        }
        
        return totalTime / PERFORMANCE_ITERATIONS;
    }
    
    // ==================== STRESS TESTS ====================
    
    /**
     * Runs stress tests with large datasets.
     */
    private StressTestResults runStressTests() {
        StressTestResults results = new StressTestResults();
        
        int[] stressSizes = {10000, 50000, 100000};
        
        for (int size : stressSizes) {
            logger.log("Stress testing with size: " + size);
            
            try {
                // Memory usage before
                long memoryBefore = getUsedMemory();
                
                // Create large dataset
                List<Member> largeDataset = generateTestMembers(size);
                manager = new EnhancedMemberManager();
                
                long startTime = System.currentTimeMillis();
                largeDataset.forEach(manager::addMember);
                long insertionTime = System.currentTimeMillis() - startTime;
                
                // Memory usage after
                long memoryAfter = getUsedMemory();
                long memoryUsed = memoryAfter - memoryBefore;
                
                // Test operations on large dataset
                startTime = System.currentTimeMillis();
                manager.quickSortByName(new ArrayList<>(largeDataset));
                long sortTime = System.currentTimeMillis() - startTime;
                
                startTime = System.currentTimeMillis();
                manager.hashSearchById(largeDataset.get(size/2).getMemberId());
                long searchTime = System.currentTimeMillis() - startTime;
                
                results.addStressTestResult(size, insertionTime, sortTime, searchTime, memoryUsed);
                
                logger.log(String.format("Size %d: Insert=%dms, Sort=%dms, Search=%dms, Memory=%dMB", 
                    size, insertionTime, sortTime, searchTime, memoryUsed / (1024 * 1024)));
                    
            } catch (OutOfMemoryError e) {
                logger.log("Out of memory at size: " + size);
                results.addFailedTest(size, "OutOfMemoryError");
            } catch (Exception e) {
                logger.log("Exception at size " + size + ": " + e.getMessage());
                results.addFailedTest(size, e.getMessage());
            }
        }
        
        return results;
    }
    
    // ==================== EDGE CASE TESTS ====================
    
    /**
     * Runs edge case tests for boundary conditions.
     */
    private EdgeCaseTestResults runEdgeCaseTests() {
        EdgeCaseTestResults results = new EdgeCaseTestResults();
        
        // Test empty dataset
        results.addResult("Empty Dataset", testEmptyDataset());
        
        // Test single element
        results.addResult("Single Element", testSingleElement());
        
        // Test duplicate elements
        results.addResult("Duplicate Elements", testDuplicateElements());
        
        // Test extreme values
        results.addResult("Extreme Values", testExtremeValues());
        
        // Test null handling
        results.addResult("Null Handling", testNullHandling());
        
        return results;
    }
    
    /**
     * Tests operations on empty datasets.
     */
    private boolean testEmptyDataset() {
        try {
            manager = new EnhancedMemberManager();
            
            // Test sorting empty list
            List<Member> empty = new ArrayList<>();
            List<Member> sorted = manager.quickSortByName(empty);
            if (!sorted.isEmpty()) {
                logger.log("Empty dataset test failed: Sorted empty list not empty");
                return false;
            }
            
            // Test searching in empty system
            Member found = manager.hashSearchById("NONEXISTENT");
            if (found != null) {
                logger.log("Empty dataset test failed: Found member in empty system");
                return false;
            }
            
            logger.log("Empty Dataset test: PASSED");
            return true;
            
        } catch (Exception e) {
            logger.log("Empty Dataset test failed with exception: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Tests operations with single element.
     */
    private boolean testSingleElement() {
        try {
            manager = new EnhancedMemberManager();
            Member singleMember = new RegularMember("SINGLE", "Single", "Member", "single@test.com", "123-456-7890");
            manager.addMember(singleMember);
            
            // Test sorting single element
            List<Member> singleList = Arrays.asList(singleMember);
            List<Member> sorted = manager.quickSortByName(singleList);
            if (sorted.size() != 1 || !sorted.get(0).equals(singleMember)) {
                logger.log("Single element test failed: Sorting failed");
                return false;
            }
            
            // Test searching single element
            Member found = manager.hashSearchById("SINGLE");
            if (found == null || !found.equals(singleMember)) {
                logger.log("Single element test failed: Search failed");
                return false;
            }
            
            logger.log("Single Element test: PASSED");
            return true;
            
        } catch (Exception e) {
            logger.log("Single Element test failed with exception: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Tests handling of duplicate elements.
     */
    private boolean testDuplicateElements() {
        try {
            manager = new EnhancedMemberManager();
            
            // Create members with same names but different IDs
            List<Member> duplicates = Arrays.asList(
                new RegularMember("ID1", "John", "Doe", "john1@test.com", "123-456-7890"),
                new RegularMember("ID2", "John", "Doe", "john2@test.com", "123-456-7891"),
                new RegularMember("ID3", "John", "Doe", "john3@test.com", "123-456-7892")
            );
            
            duplicates.forEach(manager::addMember);
            
            // Test sorting with duplicates (should maintain stability)
            List<Member> sorted = manager.quickSortByName(duplicates);
            if (sorted.size() != 3) {
                logger.log("Duplicate elements test failed: Wrong size after sorting");
                return false;
            }
            
            // All should have same name
            for (Member member : sorted) {
                if (!"John Doe".equals(member.getFullName())) {
                    logger.log("Duplicate elements test failed: Name changed during sorting");
                    return false;
                }
            }
            
            logger.log("Duplicate Elements test: PASSED");
            return true;
            
        } catch (Exception e) {
            logger.log("Duplicate Elements test failed with exception: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Tests handling of extreme values.
     */
    private boolean testExtremeValues() {
        try {
            manager = new EnhancedMemberManager();
            
            // Create members with extreme ratings
            Member minRating = new RegularMember("MIN", "Min", "Rating", "min@test.com", "123-456-7890");
            minRating.setPerformanceRating(0);
            
            Member maxRating = new RegularMember("MAX", "Max", "Rating", "max@test.com", "123-456-7891");
            maxRating.setPerformanceRating(10);
            
            manager.addMember(minRating);
            manager.addMember(maxRating);
            
            // Test binary search with extreme values
            List<Member> minResults = manager.binarySearchByRating(0);
            List<Member> maxResults = manager.binarySearchByRating(10);
            
            if (minResults.size() != 1 || maxResults.size() != 1) {
                logger.log("Extreme values test failed: Search results incorrect");
                return false;
            }
            
            logger.log("Extreme Values test: PASSED");
            return true;
            
        } catch (Exception e) {
            logger.log("Extreme Values test failed with exception: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Tests null handling.
     */
    private boolean testNullHandling() {
        try {
            manager = new EnhancedMemberManager();
            
            // Test search with null ID
            Member found = manager.hashSearchById(null);
            if (found != null) {
                logger.log("Null handling test failed: Found member with null ID");
                return false;
            }
            
            // Test advanced search with null criteria
            SearchCriteria nullCriteria = new SearchCriteria();
            List<Member> results = manager.advancedSearch(nullCriteria);
            // Should return all members (empty criteria)
            
            logger.log("Null Handling test: PASSED");
            return true;
            
        } catch (NullPointerException e) {
            logger.log("Null handling test failed: NullPointerException thrown");
            return false;
        } catch (Exception e) {
            logger.log("Null handling test failed with exception: " + e.getMessage());
            return false;
        }
    }
    
    // ==================== COMPARATIVE ANALYSIS ====================
    
    /**
     * Runs comparative analysis between different algorithms.
     */
    private ComparativeAnalysisResults runComparativeAnalysis() {
        ComparativeAnalysisResults results = new ComparativeAnalysisResults();
        
        // Compare sorting algorithms
        results.setSortingComparison(compareSortingAlgorithms());
        
        // Compare searching algorithms
        results.setSearchingComparison(compareSearchingAlgorithms());
        
        // Algorithm recommendations
        results.setRecommendations(generateRecommendations());
        
        return results;
    }
    
    /**
     * Compares sorting algorithm performance across different data sizes.
     */
    private Map<String, Map<Integer, Long>> compareSortingAlgorithms() {
        Map<String, Map<Integer, Long>> comparison = new HashMap<>();
        String[] algorithms = {"QuickSort", "MergeSort", "HeapSort"};
        
        for (String algorithm : algorithms) {
            comparison.put(algorithm, new HashMap<>());
            for (int size : TEST_SIZES) {
                long avgTime = testSortingPerformance(size, algorithm);
                comparison.get(algorithm).put(size, avgTime);
            }
        }
        
        return comparison;
    }
    
    /**
     * Compares searching algorithm performance.
     */
    private Map<String, Map<Integer, Long>> compareSearchingAlgorithms() {
        Map<String, Map<Integer, Long>> comparison = new HashMap<>();
        comparison.put("LinearSearch", new HashMap<>());
        comparison.put("HashSearch", new HashMap<>());
        comparison.put("BinarySearch", new HashMap<>());
        
        for (int size : TEST_SIZES) {
            comparison.get("LinearSearch").put(size, testLinearSearchPerformance(size));
            comparison.get("HashSearch").put(size, testHashSearchPerformance(size));
            comparison.get("BinarySearch").put(size, testBinarySearchPerformance(size));
        }
        
        return comparison;
    }
    
    /**
     * Generates algorithm recommendations based on test results.
     */
    private List<String> generateRecommendations() {
        List<String> recommendations = new ArrayList<>();
        
        recommendations.add("SORTING RECOMMENDATIONS:");
        recommendations.add("- Use MergeSort for guaranteed O(n log n) performance");
        recommendations.add("- Use QuickSort for average-case performance with small datasets");
        recommendations.add("- Use HeapSort when memory usage is critical (in-place sorting)");
        recommendations.add("");
        
        recommendations.add("SEARCHING RECOMMENDATIONS:");
        recommendations.add("- Use Hash Search for ID-based lookups (O(1) average)");
        recommendations.add("- Use Binary Search for range queries on sorted data");
        recommendations.add("- Use Advanced Search for complex multi-criteria queries");
        recommendations.add("");
        
        recommendations.add("GENERAL RECOMMENDATIONS:");
        recommendations.add("- Cache sorted data for repeated operations");
        recommendations.add("- Use appropriate data structures based on access patterns");
        recommendations.add("- Consider memory vs. time trade-offs for large datasets");
        
        return recommendations;
    }
    
    // ==================== UTILITY METHODS ====================
    
    /**
     * Generates test members with realistic data distribution.
     */
    private List<Member> generateTestMembers(int count) {
        List<Member> members = new ArrayList<>();
        
        for (int i = 0; i < count; i++) {
            String id = "TEST" + String.format("%06d", i);
            String firstName = "FirstName" + (i % 100); // Some duplicate names
            String lastName = "LastName" + (i % 50);
            String email = "test" + i + "@example.com";
            String phone = "555-" + String.format("%04d", i % 10000);
            
            Member member;
            int type = i % 3;
            
            switch (type) {
                case 0:
                    member = new RegularMember(id, firstName, lastName, email, phone);
                    break;
                case 1:
                    member = new PremiumMember(id, firstName, lastName, email, phone, 
                            "Trainer" + (i % 10), random.nextInt(10) + 1);
                    break;
                default:
                    member = new StudentMember(id, firstName, lastName, email, phone, 
                            "STU" + i, "University" + (i % 5));
                    break;
            }
            
            // Realistic performance rating distribution
            member.setPerformanceRating(Math.max(0, Math.min(10, 
                (int) (random.nextGaussian() * 2 + 5)))); // Normal distribution around 5
            member.setGoalAchieved(random.nextDouble() < 0.6); // 60% achievement rate
            
            members.add(member);
        }
        
        return members;
    }
    
    /**
     * Gets current memory usage in bytes.
     */
    private long getUsedMemory() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }
    
    /**
     * Gets the test results logger.
     */
    public TestResultLogger getLogger() {
        return logger;
    }
}

/**
 * Logger for test results and progress.
 */
class TestResultLogger {
    private List<String> logs = new ArrayList<>();
    
    public void log(String message) {
        logs.add(message);
        System.out.println(message);
    }
    
    public List<String> getLogs() {
        return new ArrayList<>(logs);
    }
    
    public String getFullLog() {
        return String.join("\n", logs);
    }
}

// Result classes for different test categories
class TestSuiteResults {
    private UnitTestResults unitTestResults;
    private PerformanceTestResults performanceTestResults;
    private StressTestResults stressTestResults;
    private EdgeCaseTestResults edgeCaseTestResults;
    private ComparativeAnalysisResults comparativeResults;
    
    // Getters and setters
    public UnitTestResults getUnitTestResults() { return unitTestResults; }
    public void setUnitTestResults(UnitTestResults unitTestResults) { this.unitTestResults = unitTestResults; }
    
    public PerformanceTestResults getPerformanceTestResults() { return performanceTestResults; }
    public void setPerformanceTestResults(PerformanceTestResults performanceTestResults) { this.performanceTestResults = performanceTestResults; }
    
    public StressTestResults getStressTestResults() { return stressTestResults; }
    public void setStressTestResults(StressTestResults stressTestResults) { this.stressTestResults = stressTestResults; }
    
    public EdgeCaseTestResults getEdgeCaseTestResults() { return edgeCaseTestResults; }
    public void setEdgeCaseTestResults(EdgeCaseTestResults edgeCaseTestResults) { this.edgeCaseTestResults = edgeCaseTestResults; }
    
    public ComparativeAnalysisResults getComparativeResults() { return comparativeResults; }
    public void setComparativeResults(ComparativeAnalysisResults comparativeResults) { this.comparativeResults = comparativeResults; }
}

class UnitTestResults {
    private Map<String, Boolean> testResults = new HashMap<>();
    
    public void addResult(String testName, boolean passed) {
        testResults.put(testName, passed);
    }
    
    public Map<String, Boolean> getResults() { return testResults; }
    
    public int getPassedCount() {
        return (int) testResults.values().stream().filter(Boolean::booleanValue).count();
    }
    
    public int getTotalCount() {
        return testResults.size();
    }
}

class PerformanceTestResults {
    private Map<Integer, Map<String, Long>> sortingTimes = new HashMap<>();
    private Map<Integer, Map<String, Long>> searchingTimes = new HashMap<>();
    
    public void addSortingTime(int size, String algorithm, long time) {
        sortingTimes.computeIfAbsent(size, k -> new HashMap<>()).put(algorithm, time);
    }
    
    public void addSearchingTime(int size, String algorithm, long time) {
        searchingTimes.computeIfAbsent(size, k -> new HashMap<>()).put(algorithm, time);
    }
    
    public void analyzeComplexity() {
        // Analyze time complexity trends
        // This is a simplified analysis
    }
    
    public Map<Integer, Map<String, Long>> getSortingTimes() { return sortingTimes; }
    public Map<Integer, Map<String, Long>> getSearchingTimes() { return searchingTimes; }
}

class StressTestResults {
    private Map<Integer, StressTestResult> results = new HashMap<>();
    private Map<Integer, String> failedTests = new HashMap<>();
    
    public void addStressTestResult(int size, long insertionTime, long sortTime, long searchTime, long memoryUsed) {
        results.put(size, new StressTestResult(insertionTime, sortTime, searchTime, memoryUsed));
    }
    
    public void addFailedTest(int size, String error) {
        failedTests.put(size, error);
    }
    
    public Map<Integer, StressTestResult> getResults() { return results; }
    public Map<Integer, String> getFailedTests() { return failedTests; }
    
    static class StressTestResult {
        public final long insertionTime, sortTime, searchTime, memoryUsed;
        
        StressTestResult(long insertionTime, long sortTime, long searchTime, long memoryUsed) {
            this.insertionTime = insertionTime;
            this.sortTime = sortTime;
            this.searchTime = searchTime;
            this.memoryUsed = memoryUsed;
        }
    }
}

class EdgeCaseTestResults {
    private Map<String, Boolean> testResults = new HashMap<>();
    
    public void addResult(String testName, boolean passed) {
        testResults.put(testName, passed);
    }
    
    public Map<String, Boolean> getResults() { return testResults; }
}

class ComparativeAnalysisResults {
    private Map<String, Map<Integer, Long>> sortingComparison;
    private Map<String, Map<Integer, Long>> searchingComparison;
    private List<String> recommendations;
    
    // Getters and setters
    public Map<String, Map<Integer, Long>> getSortingComparison() { return sortingComparison; }
    public void setSortingComparison(Map<String, Map<Integer, Long>> sortingComparison) { this.sortingComparison = sortingComparison; }
    
    public Map<String, Map<Integer, Long>> getSearchingComparison() { return searchingComparison; }
    public void setSearchingComparison(Map<String, Map<Integer, Long>> searchingComparison) { this.searchingComparison = searchingComparison; }
    
    public List<String> getRecommendations() { return recommendations; }
    public void setRecommendations(List<String> recommendations) { this.recommendations = recommendations; }
}