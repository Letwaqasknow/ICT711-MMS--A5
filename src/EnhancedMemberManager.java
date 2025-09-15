import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Enhanced Member Manager with advanced sorting and searching algorithms.
 * 
 * Extends the original MemberManager functionality with:
 * - Multiple sorting algorithms (Quick Sort, Merge Sort, Heap Sort)
 * - Advanced searching algorithms (Binary Search, Hash-based search)
 * - Performance monitoring and complexity analysis
 * - Algorithm benchmarking capabilities
 * - Optimized data structures for different operations
 * 
 * Demonstrates:
 * - Algorithm implementation and comparison
 * - Time complexity analysis
 * - Space complexity optimization
 * - Performance profiling
 * 
 * Time Complexities:
 * - Quick Sort: O(n log n) average, O(n²) worst case
 * - Merge Sort: O(n log n) guaranteed
 * - Heap Sort: O(n log n) guaranteed
 * - Binary Search: O(log n) for sorted data
 * - Hash Search: O(1) average case
 */
public class EnhancedMemberManager extends MemberManager {
    
    // Performance monitoring
    private long lastOperationTime;
    private String lastAlgorithmUsed;
    private Map<String, Long> algorithmPerformanceStats;
    
    // Optimized data structures for different operations
    private Map<String, Member> memberHashMap; // O(1) ID-based lookups
    private TreeMap<String, Member> sortedByName; // O(log n) name-based operations
    private List<Member> cachedSortedByRating; // Cache for rating-based operations
    private boolean cacheDirty = true;
    
    // Algorithm selection constants
    public enum SortAlgorithm {
        QUICK_SORT, MERGE_SORT, HEAP_SORT, JAVA_BUILT_IN
    }
    
    public enum SearchAlgorithm {
        LINEAR_SEARCH, BINARY_SEARCH, HASH_SEARCH
    }
    
    /**
     * Constructs an enhanced member manager with optimized data structures.
     */
    public EnhancedMemberManager() {
        super();
        this.algorithmPerformanceStats = new HashMap<>();
        this.memberHashMap = new HashMap<>();
        this.sortedByName = new TreeMap<>();
        this.cachedSortedByRating = new ArrayList<>();
        initializePerformanceStats();
    }
    
    /**
     * Initializes performance statistics tracking.
     */
    private void initializePerformanceStats() {
        algorithmPerformanceStats.put("QuickSort", 0L);
        algorithmPerformanceStats.put("MergeSort", 0L);
        algorithmPerformanceStats.put("HeapSort", 0L);
        algorithmPerformanceStats.put("LinearSearch", 0L);
        algorithmPerformanceStats.put("BinarySearch", 0L);
        algorithmPerformanceStats.put("HashSearch", 0L);
    }
    
    /**
     * Adds a member with optimized data structure updates.
     * Time Complexity: O(log n) due to TreeMap insertion
     * Space Complexity: O(1) additional space
     */
    @Override
    public void addMember(Member member) {
        super.addMember(member);
        
        // Update optimized data structures
        memberHashMap.put(member.getMemberId(), member);
        sortedByName.put(member.getFullName().toLowerCase(), member);
        cacheDirty = true;
        
        System.out.println("Member added with O(log n) complexity");
    }
    
    /**
     * Removes a member with optimized cleanup.
     * Time Complexity: O(log n) due to TreeMap removal
     * Space Complexity: O(1)
     */
    @Override
    public boolean removeMember(String memberId) {
        Member member = memberHashMap.get(memberId);
        if (member != null) {
            boolean result = super.removeMember(memberId);
            if (result) {
                memberHashMap.remove(memberId);
                sortedByName.remove(member.getFullName().toLowerCase());
                cacheDirty = true;
            }
            return result;
        }
        return false;
    }
    
    // ==================== SORTING ALGORITHMS ====================
    
    /**
     * Sorts members by name using Quick Sort algorithm.
     * 
     * Time Complexity: O(n log n) average case, O(n²) worst case
     * Space Complexity: O(log n) due to recursion stack
     * 
     * @param members list to sort
     * @return sorted list by name
     */
    public List<Member> quickSortByName(List<Member> members) {
        long startTime = System.nanoTime();
        List<Member> sortedList = new ArrayList<>(members);
        quickSortByNameRecursive(sortedList, 0, sortedList.size() - 1);
        recordPerformance("QuickSort", startTime);
        return sortedList;
    }
    
    /**
     * Recursive helper for Quick Sort by name.
     */
    private void quickSortByNameRecursive(List<Member> list, int low, int high) {
        if (low < high) {
            int pivotIndex = partitionByName(list, low, high);
            quickSortByNameRecursive(list, low, pivotIndex - 1);
            quickSortByNameRecursive(list, pivotIndex + 1, high);
        }
    }
    
    /**
     * Partition helper for Quick Sort by name.
     */
    private int partitionByName(List<Member> list, int low, int high) {
        String pivot = list.get(high).getFullName().toLowerCase();
        int i = low - 1;
        
        for (int j = low; j < high; j++) {
            if (list.get(j).getFullName().toLowerCase().compareTo(pivot) <= 0) {
                i++;
                Collections.swap(list, i, j);
            }
        }
        Collections.swap(list, i + 1, high);
        return i + 1;
    }
    
    /**
     * Sorts members by performance rating using Merge Sort algorithm.
     * 
     * Time Complexity: O(n log n) guaranteed
     * Space Complexity: O(n) for auxiliary arrays
     * 
     * @param members list to sort
     * @return sorted list by performance rating (descending)
     */
    public List<Member> mergeSortByRating(List<Member> members) {
        long startTime = System.nanoTime();
        List<Member> sortedList = new ArrayList<>(members);
        mergeSortByRatingRecursive(sortedList, 0, sortedList.size() - 1);
        recordPerformance("MergeSort", startTime);
        return sortedList;
    }
    
    /**
     * Recursive helper for Merge Sort by rating.
     */
    private void mergeSortByRatingRecursive(List<Member> list, int left, int right) {
        if (left < right) {
            int middle = left + (right - left) / 2;
            mergeSortByRatingRecursive(list, left, middle);
            mergeSortByRatingRecursive(list, middle + 1, right);
            mergeByRating(list, left, middle, right);
        }
    }
    
    /**
     * Merge helper for Merge Sort by rating.
     */
    private void mergeByRating(List<Member> list, int left, int middle, int right) {
        List<Member> leftArray = new ArrayList<>(list.subList(left, middle + 1));
        List<Member> rightArray = new ArrayList<>(list.subList(middle + 1, right + 1));
        
        int i = 0, j = 0, k = left;
        
        while (i < leftArray.size() && j < rightArray.size()) {
            // Sort in descending order of performance rating
            if (leftArray.get(i).getPerformanceRating() >= rightArray.get(j).getPerformanceRating()) {
                list.set(k++, leftArray.get(i++));
            } else {
                list.set(k++, rightArray.get(j++));
            }
        }
        
        while (i < leftArray.size()) {
            list.set(k++, leftArray.get(i++));
        }
        
        while (j < rightArray.size()) {
            list.set(k++, rightArray.get(j++));
        }
    }
    
    /**
     * Sorts members by monthly fee using Heap Sort algorithm.
     * 
     * Time Complexity: O(n log n) guaranteed
     * Space Complexity: O(1) in-place sorting
     * 
     * @param members list to sort
     * @return sorted list by monthly fee (descending)
     */
    public List<Member> heapSortByFee(List<Member> members) {
        long startTime = System.nanoTime();
        List<Member> sortedList = new ArrayList<>(members);
        heapSortByFeeImpl(sortedList);
        recordPerformance("HeapSort", startTime);
        return sortedList;
    }
    
    /**
     * Implementation of Heap Sort by fee.
     */
    private void heapSortByFeeImpl(List<Member> list) {
        int n = list.size();
        
        // Build max heap
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapifyByFee(list, n, i);
        }
        
        // Extract elements from heap one by one
        for (int i = n - 1; i > 0; i--) {
            Collections.swap(list, 0, i);
            heapifyByFee(list, i, 0);
        }
    }
    
    /**
     * Heapify helper for Heap Sort by fee.
     */
    private void heapifyByFee(List<Member> list, int n, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;
        
        if (left < n && list.get(left).calculateMonthlyFee() > list.get(largest).calculateMonthlyFee()) {
            largest = left;
        }
        
        if (right < n && list.get(right).calculateMonthlyFee() > list.get(largest).calculateMonthlyFee()) {
            largest = right;
        }
        
        if (largest != i) {
            Collections.swap(list, i, largest);
            heapifyByFee(list, n, largest);
        }
    }
    
    // ==================== SEARCHING ALGORITHMS ====================
    
    /**
     * Finds member by ID using Hash-based search.
     * 
     * Time Complexity: O(1) average case, O(n) worst case
     * Space Complexity: O(n) for hash map
     * 
     * @param memberId ID to search for
     * @return member if found, null otherwise
     */
    public Member hashSearchById(String memberId) {
        long startTime = System.nanoTime();
        Member result = memberHashMap.get(memberId);
        recordPerformance("HashSearch", startTime);
        return result;
    }
    
    /**
     * Finds members by name using optimized TreeMap search.
     * 
     * Time Complexity: O(log n) for exact match, O(k) for prefix search
     * Space Complexity: O(n) for TreeMap
     * 
     * @param namePrefix prefix to search for
     * @return list of members with matching names
     */
    public List<Member> treeSearchByName(String namePrefix) {
        long startTime = System.nanoTime();
        String lowerPrefix = namePrefix.toLowerCase();
        
        List<Member> results = sortedByName.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(lowerPrefix))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
        
        recordPerformance("TreeSearch", startTime);
        return results;
    }
    
    /**
     * Finds members by performance rating using Binary Search.
     * Requires pre-sorted list for O(log n) performance.
     * 
     * Time Complexity: O(log n) for search, O(n log n) for initial sort if needed
     * Space Complexity: O(1) for search operation
     * 
     * @param targetRating exact rating to find
     * @return list of members with exact rating match
     */
    public List<Member> binarySearchByRating(int targetRating) {
        long startTime = System.nanoTime();
        
        // Ensure cached sorted list is up to date
        if (cacheDirty) {
            cachedSortedByRating = getAllMembers().stream()
                    .sorted((m1, m2) -> Integer.compare(m1.getPerformanceRating(), m2.getPerformanceRating()))
                    .collect(Collectors.toList());
            cacheDirty = false;
        }
        
        List<Member> results = new ArrayList<>();
        
        // Find first occurrence
        int firstIndex = binarySearchFirstOccurrence(cachedSortedByRating, targetRating);
        
        if (firstIndex != -1) {
            // Collect all members with the same rating
            for (int i = firstIndex; i < cachedSortedByRating.size(); i++) {
                if (cachedSortedByRating.get(i).getPerformanceRating() == targetRating) {
                    results.add(cachedSortedByRating.get(i));
                } else {
                    break;
                }
            }
        }
        
        recordPerformance("BinarySearch", startTime);
        return results;
    }
    
    /**
     * Binary search helper to find first occurrence of target rating.
     */
    private int binarySearchFirstOccurrence(List<Member> sortedList, int target) {
        int left = 0, right = sortedList.size() - 1;
        int result = -1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            int midRating = sortedList.get(mid).getPerformanceRating();
            
            if (midRating == target) {
                result = mid;
                right = mid - 1; // Continue searching left for first occurrence
            } else if (midRating < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return result;
    }
    
    /**
     * Advanced search with multiple criteria using optimized algorithms.
     * 
     * @param criteria search criteria object
     * @return list of matching members
     */
    public List<Member> advancedSearch(SearchCriteria criteria) {
        long startTime = System.nanoTime();
        List<Member> candidates = getAllMembers();
        
        // Apply filters in order of selectivity (most selective first)
        
        // 1. Member ID filter (most selective, use hash search if exact match)
        if (criteria.getMemberId() != null && !criteria.getMemberId().isEmpty()) {
            if (criteria.isExactMatch()) {
                Member exactMember = hashSearchById(criteria.getMemberId());
                candidates = exactMember != null ? Arrays.asList(exactMember) : new ArrayList<>();
            } else {
                candidates = candidates.stream()
                        .filter(m -> m.getMemberId().toLowerCase().contains(criteria.getMemberId().toLowerCase()))
                        .collect(Collectors.toList());
            }
        }
        
        // 2. Performance rating filter
        if (criteria.getMinRating() != null || criteria.getMaxRating() != null) {
            candidates = candidates.stream()
                    .filter(m -> (criteria.getMinRating() == null || m.getPerformanceRating() >= criteria.getMinRating()) &&
                                (criteria.getMaxRating() == null || m.getPerformanceRating() <= criteria.getMaxRating()))
                    .collect(Collectors.toList());
        }
        
        // 3. Member type filter
        if (criteria.getMemberType() != null) {
            candidates = candidates.stream()
                    .filter(m -> criteria.getMemberType().isInstance(m))
                    .collect(Collectors.toList());
        }
        
        // 4. Goal achievement filter
        if (criteria.getGoalAchieved() != null) {
            candidates = candidates.stream()
                    .filter(m -> m.isGoalAchieved() == criteria.getGoalAchieved())
                    .collect(Collectors.toList());
        }
        
        // 5. Name filter (least selective, applied last)
        if (criteria.getName() != null && !criteria.getName().isEmpty()) {
            String searchName = criteria.getName().toLowerCase();
            candidates = candidates.stream()
                    .filter(m -> m.getFullName().toLowerCase().contains(searchName))
                    .collect(Collectors.toList());
        }
        
        recordPerformance("AdvancedSearch", startTime);
        return candidates;
    }
    
    // ==================== PERFORMANCE MONITORING ====================
    
    /**
     * Records performance statistics for algorithm operations.
     */
    private void recordPerformance(String algorithmName, long startTime) {
        lastOperationTime = System.nanoTime() - startTime;
        lastAlgorithmUsed = algorithmName;
        
        // Update cumulative statistics
        long currentTotal = algorithmPerformanceStats.getOrDefault(algorithmName, 0L);
        algorithmPerformanceStats.put(algorithmName, currentTotal + lastOperationTime);
    }
    
    /**
     * Performs algorithm benchmarking with different data sizes.
     * 
     * @param maxSize maximum size to test
     * @return benchmark results
     */
    public BenchmarkResults performBenchmark(int maxSize) {
        BenchmarkResults results = new BenchmarkResults();
        
        // Generate test data of varying sizes
        for (int size = 100; size <= maxSize; size *= 2) {
            List<Member> testData = generateTestData(size);
            
            // Benchmark sorting algorithms
            results.addSortingResult(size, "QuickSort", benchmarkSorting(testData, SortAlgorithm.QUICK_SORT));
            results.addSortingResult(size, "MergeSort", benchmarkSorting(testData, SortAlgorithm.MERGE_SORT));
            results.addSortingResult(size, "HeapSort", benchmarkSorting(testData, SortAlgorithm.HEAP_SORT));
            results.addSortingResult(size, "JavaBuiltIn", benchmarkSorting(testData, SortAlgorithm.JAVA_BUILT_IN));
            
            // Benchmark searching algorithms
            String randomId = testData.get(new Random().nextInt(testData.size())).getMemberId();
            results.addSearchingResult(size, "LinearSearch", benchmarkLinearSearch(testData, randomId));
            results.addSearchingResult(size, "HashSearch", benchmarkHashSearch(testData, randomId));
        }
        
        return results;
    }
    
    /**
     * Benchmarks sorting algorithms.
     */
    private long benchmarkSorting(List<Member> data, SortAlgorithm algorithm) {
        long startTime = System.nanoTime();
        
        switch (algorithm) {
            case QUICK_SORT:
                quickSortByName(data);
                break;
            case MERGE_SORT:
                mergeSortByRating(data);
                break;
            case HEAP_SORT:
                heapSortByFee(data);
                break;
            case JAVA_BUILT_IN:
                data.sort(Comparator.comparing(Member::getFullName));
                break;
        }
        
        return System.nanoTime() - startTime;
    }
    
    /**
     * Benchmarks linear search.
     */
    private long benchmarkLinearSearch(List<Member> data, String targetId) {
        long startTime = System.nanoTime();
        
        for (Member member : data) {
            if (member.getMemberId().equals(targetId)) {
                break;
            }
        }
        
        return System.nanoTime() - startTime;
    }
    
    /**
     * Benchmarks hash-based search.
     */
    private long benchmarkHashSearch(List<Member> data, String targetId) {
        // Pre-populate hash map
        Map<String, Member> hashMap = new HashMap<>();
        for (Member member : data) {
            hashMap.put(member.getMemberId(), member);
        }
        
        long startTime = System.nanoTime();
        hashMap.get(targetId);
        return System.nanoTime() - startTime;
    }
    
    /**
     * Generates test data for benchmarking.
     */
    private List<Member> generateTestData(int size) {
        List<Member> testData = new ArrayList<>();
        Random random = new Random();
        
        for (int i = 0; i < size; i++) {
            String id = "TEST" + String.format("%06d", i);
            String firstName = "FirstName" + i;
            String lastName = "LastName" + i;
            String email = "test" + i + "@example.com";
            String phone = "555-" + String.format("%04d", i);
            
            Member member;
            int type = random.nextInt(3);
            
            switch (type) {
                case 0:
                    member = new RegularMember(id, firstName, lastName, email, phone);
                    break;
                case 1:
                    member = new PremiumMember(id, firstName, lastName, email, phone, 
                            "Trainer" + i, random.nextInt(10) + 1);
                    break;
                default:
                    member = new StudentMember(id, firstName, lastName, email, phone, 
                            "STU" + i, "University" + i);
                    break;
            }
            
            member.setPerformanceRating(random.nextInt(11));
            member.setGoalAchieved(random.nextBoolean());
            testData.add(member);
        }
        
        return testData;
    }
    
    // ==================== GETTERS FOR PERFORMANCE DATA ====================
    
    public long getLastOperationTime() {
        return lastOperationTime;
    }
    
    public String getLastAlgorithmUsed() {
        return lastAlgorithmUsed;
    }
    
    public Map<String, Long> getAlgorithmPerformanceStats() {
        return new HashMap<>(algorithmPerformanceStats);
    }
    
    /**
     * Gets performance statistics in human-readable format.
     */
    public String getPerformanceReport() {
        StringBuilder report = new StringBuilder();
        report.append("Algorithm Performance Statistics\n");
        report.append("================================\n");
        
        if (lastAlgorithmUsed != null) {
            report.append(String.format("Last Operation: %s (%.3f ms)\n", 
                    lastAlgorithmUsed, lastOperationTime / 1_000_000.0));
        }
        
        report.append("\nCumulative Performance (total time in ms):\n");
        algorithmPerformanceStats.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(entry -> report.append(String.format("%-15s: %.3f ms\n", 
                        entry.getKey(), entry.getValue() / 1_000_000.0)));
        
        return report.toString();
    }
}

/**
 * Search criteria class for advanced searching functionality.
 */
class SearchCriteria {
    private String memberId;
    private String name;
    private Integer minRating;
    private Integer maxRating;
    private Class<?> memberType;
    private Boolean goalAchieved;
    private boolean exactMatch = false;
    
    // Constructors
    public SearchCriteria() {}
    
    public SearchCriteria(String memberId, String name, Integer minRating, Integer maxRating, 
                         Class<?> memberType, Boolean goalAchieved) {
        this.memberId = memberId;
        this.name = name;
        this.minRating = minRating;
        this.maxRating = maxRating;
        this.memberType = memberType;
        this.goalAchieved = goalAchieved;
    }
    
    // Getters and setters
    public String getMemberId() { return memberId; }
    public void setMemberId(String memberId) { this.memberId = memberId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public Integer getMinRating() { return minRating; }
    public void setMinRating(Integer minRating) { this.minRating = minRating; }
    
    public Integer getMaxRating() { return maxRating; }
    public void setMaxRating(Integer maxRating) { this.maxRating = maxRating; }
    
    public Class<?> getMemberType() { return memberType; }
    public void setMemberType(Class<?> memberType) { this.memberType = memberType; }
    
    public Boolean getGoalAchieved() { return goalAchieved; }
    public void setGoalAchieved(Boolean goalAchieved) { this.goalAchieved = goalAchieved; }
    
    public boolean isExactMatch() { return exactMatch; }
    public void setExactMatch(boolean exactMatch) { this.exactMatch = exactMatch; }
}

/**
 * Benchmark results class for algorithm performance analysis.
 */
class BenchmarkResults {
    private Map<String, Map<Integer, Long>> sortingResults = new HashMap<>();
    private Map<String, Map<Integer, Long>> searchingResults = new HashMap<>();
    
    public void addSortingResult(int dataSize, String algorithm, long timeNanos) {
        sortingResults.computeIfAbsent(algorithm, k -> new HashMap<>()).put(dataSize, timeNanos);
    }
    
    public void addSearchingResult(int dataSize, String algorithm, long timeNanos) {
        searchingResults.computeIfAbsent(algorithm, k -> new HashMap<>()).put(dataSize, timeNanos);
    }
    
    public Map<String, Map<Integer, Long>> getSortingResults() { return sortingResults; }
    public Map<String, Map<Integer, Long>> getSearchingResults() { return searchingResults; }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Benchmark Results\n");
        sb.append("================\n\n");
        
        sb.append("Sorting Algorithm Performance:\n");
        sortingResults.forEach((algorithm, results) -> {
            sb.append(String.format("%-12s: ", algorithm));
            results.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(entry -> sb.append(String.format("n=%d:%.2fms ", 
                            entry.getKey(), entry.getValue() / 1_000_000.0)));
            sb.append("\n");
        });
        
        sb.append("\nSearching Algorithm Performance:\n");
        searchingResults.forEach((algorithm, results) -> {
            sb.append(String.format("%-12s: ", algorithm));
            results.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(entry -> sb.append(String.format("n=%d:%.2fms ", 
                            entry.getKey(), entry.getValue() / 1_000_000.0)));
            sb.append("\n");
        });
        
        return sb.toString();
    }
}