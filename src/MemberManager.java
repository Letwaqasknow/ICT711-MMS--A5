
// ========== MemberManager.java (Manager Class) ==========
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Central manager class for all member-related operations.
 * 
 * Provides comprehensive functionality for:
 * - Member CRUD operations (Create, Read, Update, Delete)
 * - File I/O operations for data persistence
 * - Performance management and reporting
 * - Statistical analysis and reporting
 * 
 * Demonstrates:
 * - Java Collections (ArrayList, Streams)
 * - File I/O with CSV format
 * - Exception handling
 * - Business logic implementation
 * 
 */
public class MemberManager {
    /** Dynamic storage for all gym members */
    private ArrayList<Member> members;
    
    /** Currently loaded/default file name for persistence */
    private String currentFileName;
    
    /**
     * Constructs a new MemberManager with empty member list.
     * Sets default file name for data persistence.
     */
    public MemberManager() {
        this.members = new ArrayList<>();
        this.currentFileName = Constants.DEFAULT_FILE_NAME;
    }
    
    /**
     * Adds a new member to the management system.
     * 
     * @param member the member object to add (cannot be null)
     */
    public void addMember(Member member) {
        members.add(member);
        System.out.printf(Constants.MSG_MEMBER_ADDED, member.getFullName(), member.getMemberId());
        System.out.println();
    }
    
    /**
     * Removes a member from the system by ID.
     * 
     * @param memberId unique identifier of member to remove
     * @return true if member was found and removed, false otherwise
     */
    public boolean removeMember(String memberId) {
        boolean removed = members.removeIf(m -> m.getMemberId().equals(memberId));
        if (removed) {
            System.out.printf(Constants.MSG_MEMBER_REMOVED, memberId);
            System.out.println();
        } else {
            System.out.printf(Constants.MSG_MEMBER_NOT_FOUND, memberId);
            System.out.println();
        }
        return removed;
    }
    
    /**
     * Finds a member by their unique ID.
     * 
     * @param memberId unique identifier to search for
     * @return Member object if found, null otherwise
     */
    public Member findMemberById(String memberId) {
        System.out.printf(Constants.MSG_LOADING_MEMBERS, memberId);
        System.out.println();
        return members.stream()
                .filter(m -> m.getMemberId().equals(memberId))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Finds members by partial name match (case-insensitive).
     * 
     * @param name partial name to search for
     * @return list of members matching the search criteria
     */
    public List<Member> findMembersByName(String name) {
        System.out.printf(Constants.MSG_FINDING_BY_NAME, name);
        System.out.println();
        String searchName = name.toLowerCase();
        return members.stream()
                .filter(m -> m.getFullName().toLowerCase().contains(searchName))
                .collect(Collectors.toList());
    }
    
    /**
     * Finds members with performance rating above specified threshold.
     * Results are sorted by performance rating (highest first).
     * 
     * @param minRating minimum performance rating to include
     * @return list of members meeting performance criteria, sorted by rating
     */
    public List<Member> findMembersByPerformance(int minRating) {
        return members.stream()
                .filter(m -> m.getPerformanceRating() >= minRating)
                .sorted((m1, m2) -> Integer.compare(m2.getPerformanceRating(), m1.getPerformanceRating()))
                .collect(Collectors.toList());
    }
    
    /**
     * Updates member information based on provided field map.
     * 
     * @param memberId unique identifier of member to update
     * @param updates map containing field names and new values
     * @return true if member was found and updated, false otherwise
     */
    public boolean updateMember(String memberId, Map<String, Object> updates) {
        System.out.printf(Constants.MSG_UPDATING_MEMBER, memberId);
        System.out.println();
        Member member = findMemberById(memberId);
        if (member == null) {
            System.out.printf(Constants.MSG_MEMBER_NOT_FOUND_UPDATE, memberId);
            System.out.println();
            return false;
        }
        
        // Update fields based on provided map
        if (updates.containsKey(Constants.UPDATE_KEY_EMAIL)) {
            member.setEmail((String) updates.get(Constants.UPDATE_KEY_EMAIL));
        }
        if (updates.containsKey(Constants.UPDATE_KEY_PHONE)) {
            member.setPhone((String) updates.get(Constants.UPDATE_KEY_PHONE));
        }
        if (updates.containsKey(Constants.UPDATE_KEY_PERFORMANCE_RATING)) {
            member.setPerformanceRating((Integer) updates.get(Constants.UPDATE_KEY_PERFORMANCE_RATING));
        }
        if (updates.containsKey(Constants.UPDATE_KEY_GOAL_ACHIEVED)) {
            member.setGoalAchieved((Boolean) updates.get(Constants.UPDATE_KEY_GOAL_ACHIEVED));
        }

        System.out.printf(Constants.MSG_MEMBER_UPDATED, member.getFullName(), member.getMemberId());
        System.out.println();
        
        return true;
    }
    
    /**
     * Loads member data from CSV file into the system.
     * Clears existing members and replaces with file content.
     * 
     * Expected CSV format: Type,ID,FirstName,LastName,Email,Phone,PerformanceRating,GoalAchieved,Extra1,Extra2
     * 
     * @param fileName path to CSV file to load
     * @throws IOException if file cannot be read or has invalid format
     */
    public void loadFromFile(String fileName) throws IOException {
        // clear the members list
        members.clear();
        currentFileName = fileName;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            reader.readLine(); // skip header
            
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(Constants.CSV_DELIMITER);
                // each line has 7 required fields (type, id, first name, last name, email, phone, performance rating, goal achieved)
                // extra fields are for premium members (trainer name and sessions per month) and student members (student ID and university)
                if (parts.length >= Constants.MIN_CSV_FIELDS_REQUIRED) {
                    String type = parts[0];
                    String id = parts[1];
                    String firstName = parts[2];
                    String lastName = parts[3];
                    String email = parts[4];
                    String phone = parts[5];
                    int performanceRating = Integer.parseInt(parts[6]);
                    boolean goalAchieved = Boolean.parseBoolean(parts[7]);
                    
                    Member member = null;
                    
                    // POLYMORPHISM: Creating different subclass objects but treating them as Member type
                    switch (type) {
                        case Constants.MEMBER_TYPE_REGULAR:
                            member = new RegularMember(id, firstName, lastName, email, phone);
                            break;
                        case Constants.MEMBER_TYPE_PREMIUM:
                            String trainer = parts.length > 8 ? parts[8] : Constants.DEFAULT_TRAINER_NAME;
                            int sessions = parts.length > 9 ? Integer.parseInt(parts[9]) : Constants.DEFAULT_SESSIONS_PER_MONTH;
                            member = new PremiumMember(id, firstName, lastName, email, phone, trainer, sessions);
                            break;
                        case Constants.MEMBER_TYPE_STUDENT:
                            String studentId = parts.length > 8 ? parts[8] : Constants.DEFAULT_STUDENT_ID;
                            String university = parts.length > 9 ? parts[9] : Constants.DEFAULT_UNIVERSITY;
                            member = new StudentMember(id, firstName, lastName, email, phone, studentId, university);
                            break;
                    }
                    
                    if (member != null) {
                        member.setPerformanceRating(performanceRating);
                        member.setGoalAchieved(goalAchieved);
                        members.add(member);
                    }
                }
            }
            System.out.printf(Constants.MSG_MEMBERS_LOADED, members.size(), fileName);
            System.out.println();
        } catch (IOException e) {
            System.out.printf(Constants.MSG_ERROR_OCCURRED, e.getMessage());
            System.out.println();
            e.printStackTrace();
        }
    }
    
    /**
     * Saves all current members to CSV file.
     * 
     * @param fileName path where to save the CSV file
     * @throws IOException if file cannot be written
     */
    public void saveToFile(String fileName) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println(Constants.CSV_HEADER);
            
            for (Member member : members) { // POLYMORPHISM: Iterate through different member types using common interface
                writer.print(member.getMemberType().split(" ")[0] + Constants.CSV_DELIMITER); // POLYMORPHISM: Calls subclass implementation
                writer.print(member.getMemberId() + Constants.CSV_DELIMITER);
                writer.print(member.getFirstName() + Constants.CSV_DELIMITER);
                writer.print(member.getLastName() + Constants.CSV_DELIMITER);
                writer.print(member.getEmail() + Constants.CSV_DELIMITER);
                writer.print(member.getPhone() + Constants.CSV_DELIMITER);
                writer.print(member.getPerformanceRating() + Constants.CSV_DELIMITER);
                writer.print(member.isGoalAchieved());
                
                // POLYMORPHISM: Runtime type checking and casting to access subclass-specific features
                if (member instanceof PremiumMember) {
                    PremiumMember pm = (PremiumMember) member;
                    writer.print(Constants.CSV_DELIMITER + pm.getTrainerName() + Constants.CSV_DELIMITER + pm.getSessionsPerMonth());
                } else if (member instanceof StudentMember) {
                    StudentMember sm = (StudentMember) member;
                    writer.print(Constants.CSV_DELIMITER + sm.getStudentId() + Constants.CSV_DELIMITER + sm.getUniversity());
                }
                writer.println();
            }
            System.out.printf(Constants.MSG_MEMBERS_SAVED, members.size(), fileName);
            System.out.println();
        }
    }
    
    /**
     * Generates appreciation letter for high-performing members.
     * Only generates letters for members with rating ≥ 8.
     * 
     * @param member the member to generate letter for
     * @return formatted appreciation letter or null if not eligible
     */
    public String generateAppreciationLetter(Member member) {
        if (member.getPerformanceRating() >= Constants.HIGH_PERFORMANCE_THRESHOLD) {
            return String.format(Constants.APPRECIATION_LETTER_TEMPLATE,
                member.getFullName(), member.getPerformanceRating());
        }
        return null;
    }
    
    /**
     * Generates reminder letter for low-performing members.
     * Only generates letters for members with rating < 5.
     * 
     * @param member the member to generate letter for
     * @return formatted reminder letter or null if not applicable
     */
    public String generateReminderLetter(Member member) {
        if (member.getPerformanceRating() < Constants.LOW_PERFORMANCE_THRESHOLD) {
            return String.format(Constants.REMINDER_LETTER_TEMPLATE,
                member.getFullName(), member.getPerformanceRating());
        }
        return null;
    }
    
    /**
     * Returns a copy of all members in the system.
     * 
     * @return new ArrayList containing all current members
     */
    public List<Member> getAllMembers() {
        return new ArrayList<>(members);
    }
    
    /**
     * Displays comprehensive statistics about all members.
     * Includes counts by type, average performance, and goal achievement rates.
     */
    public void displayStatistics() {
        if (members.isEmpty()) {
            System.out.println(Constants.STATS_NO_MEMBERS);
            return;
        }
        
        // Calculate statistics using streams
        // POLYMORPHISM: Runtime type checking to categorize different member types
        long regularCount = members.stream().filter(m -> m instanceof RegularMember).count();
        long premiumCount = members.stream().filter(m -> m instanceof PremiumMember).count();
        long studentCount = members.stream().filter(m -> m instanceof StudentMember).count();
        
        double avgPerformance = members.stream()
                .mapToInt(Member::getPerformanceRating)
                .average()
                .orElse(0);
        
        long goalAchievers = members.stream().filter(Member::isGoalAchieved).count();
        
        // Display formatted statistics
        System.out.println(Constants.STATS_TITLE);
        System.out.printf(Constants.STATS_TOTAL_MEMBERS, members.size());
        System.out.println();
        System.out.printf(Constants.STATS_REGULAR_MEMBERS, regularCount);
        System.out.println();
        System.out.printf(Constants.STATS_PREMIUM_MEMBERS, premiumCount);
        System.out.println();
        System.out.printf(Constants.STATS_STUDENT_MEMBERS, studentCount);
        System.out.println();
        System.out.printf(Constants.STATS_AVG_PERFORMANCE, avgPerformance);
        System.out.println();
        System.out.printf(Constants.STATS_GOAL_ACHIEVERS, goalAchievers);
        System.out.println();
        System.out.println(Constants.STATS_FOOTER);
    }
}
