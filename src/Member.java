import java.time.LocalDate;

/**
 * Abstract base class representing a gym member.
 * 
 * Implements Serializable for potential future object persistence (not currently used).
 * The serialVersionUID ensures compatibility when deserializing objects across
 * different versions of this class.
 * 
 * Demonstrates key OOP principles:
 * - Abstraction: Abstract methods for subclass-specific behavior
 * - Encapsulation: Private fields with controlled access via getters/setters
 * - Inheritance: Base class for RegularMember, PremiumMember, StudentMember
 * 

 */
public abstract class Member { // ABSTRACTION: Abstract class cannot be instantiated directly
    /** Serial version UID for object serialization compatibility */
    
    // ENCAPSULATION: Private fields hide internal data from external access
    private String memberId;        // Unique identifier for the member
    private String firstName;       // Member's first name
    private String lastName;        // Member's last name
    private String email;          // Contact email address
    private String phone;          // Contact phone number
    private LocalDate joinDate;    // Date when member joined the gym
    private double baseFee;        // Base monthly fee for membership type
    private int performanceRating; // Performance rating on 0-10 scale
    private boolean goalAchieved;  // Whether member achieved monthly goal
    
    /**
     * Constructs a new Member with specified details.
     * 
     * @param memberId unique identifier for the member
     * @param firstName member's first name
     * @param lastName member's last name
     * @param email member's email address
     * @param phone member's phone number
     * @param baseFee base monthly fee for this membership type
     */
    public Member(String memberId, String firstName, String lastName, 
                  String email, String phone, double baseFee) {
        this.memberId = memberId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.joinDate = LocalDate.now();
        this.baseFee = baseFee;
        this.performanceRating = Constants.DEFAULT_PERFORMANCE_RATING;
        this.goalAchieved = Constants.DEFAULT_GOAL_ACHIEVED;
    }
    
    /**
     * Calculates the monthly fee for this member.
     * Must be implemented by subclasses to provide type-specific fee calculation.
     * 
     * @return calculated monthly fee amount
     */
    public abstract double calculateMonthlyFee(); // ABSTRACTION: Abstract method forces subclasses to provide implementation
    
    /**
     * Returns the display name for this member's type.
     * Must be implemented by subclasses to provide type-specific names.
     * 
     * @return string representation of member type
     */
    public abstract String getMemberType(); // ABSTRACTION: Abstract method forces subclasses to provide implementation
    
    /**
     * Generates a comprehensive performance report for this member.
     * 
     * @return formatted string containing member performance details
     */
    public String generatePerformanceReport() {
        StringBuilder report = new StringBuilder();
        report.append("Performance Report for ").append(getFullName()).append("\n");
        report.append("Member Type: ").append(getMemberType()).append("\n"); // POLYMORPHISM: Calls subclass implementation
        report.append("Performance Rating: ").append(performanceRating).append("/10\n");
        report.append("Goal Achievement: ").append(goalAchieved ? "Yes" : "No").append("\n");
        report.append("Monthly Fee: $").append(String.format("%.2f", calculateMonthlyFee())).append("\n"); // POLYMORPHISM: Calls subclass implementation
        return report.toString();
    }
    
    // Getter and setter methods with validation where appropriate
    
    /** @return member's unique identifier */
    public String getMemberId() { return memberId; } // ENCAPSULATION: Controlled access to private field
    
    /** @param memberId new member ID to set */
    public void setMemberId(String memberId) { this.memberId = memberId; }
    
    /** @return member's first name */
    public String getFirstName() { return firstName; }
    
    /** @param firstName new first name to set */
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    /** @return member's last name */
    public String getLastName() { return lastName; }
    
    /** @param lastName new last name to set */
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    /** @return member's full name (first + last) */
    public String getFullName() { return firstName + " " + lastName; }
    
    /** @return member's email address */
    public String getEmail() { return email; }
    
    /** @param email new email address to set */
    public void setEmail(String email) { this.email = email; }
    
    /** @return member's phone number */
    public String getPhone() { return phone; }
    
    /** @param phone new phone number to set */
    public void setPhone(String phone) { this.phone = phone; }
    
    /** @return date when member joined */
    public LocalDate getJoinDate() { return joinDate; }
    
    /** @param joinDate new join date to set */
    public void setJoinDate(LocalDate joinDate) { this.joinDate = joinDate; }
    
    /** @return base monthly fee for this membership type */
    public double getBaseFee() { return baseFee; }
    
    /** @param baseFee new base fee to set */
    public void setBaseFee(double baseFee) { this.baseFee = baseFee; }
    
    /** @return current performance rating (0-10) */
    public int getPerformanceRating() { return performanceRating; }
    
    /**
     * Sets performance rating with validation.
     * Only accepts values within the valid range (0-10).
     * 
     * @param performanceRating new rating to set (0-10)
     */
    public void setPerformanceRating(int performanceRating) { // ENCAPSULATION: Controlled modification with validation
        if (performanceRating >= Constants.MIN_PERFORMANCE_RATING && 
            performanceRating <= Constants.MAX_PERFORMANCE_RATING) {
            this.performanceRating = performanceRating;
        }
    }
    
    /** @return whether member achieved their monthly goal */
    public boolean isGoalAchieved() { return goalAchieved; }
    
    /** @param goalAchieved new goal achievement status */
    public void setGoalAchieved(boolean goalAchieved) { this.goalAchieved = goalAchieved; }
    
    /**
     * Returns a string representation of the member.
     * 
     * @return formatted string with key member information
     */
    @Override
    public String toString() {
        return String.format("Member ID: %s | Name: %s | Type: %s | Rating: %d/10 | Fee: $%.2f",
                memberId, getFullName(), getMemberType(), performanceRating, calculateMonthlyFee()); // POLYMORPHISM: getMemberType() and calculateMonthlyFee() call subclass implementations
    }
}
