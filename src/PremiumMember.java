
/**
 * Premium gym member with personal trainer and enhanced benefits.
 * 
 * Extends Member base class to provide specific implementation for:
 * - Personal training sessions with associated costs
 * - Goal achievement discount (15%)
 * - High performance bonus for ratings 8 and above
 * 
 * Demonstrates inheritance and polymorphism from Member base class.
 *

 */
public class PremiumMember extends Member { // INHERITANCE: Extends Member base class
    // ENCAPSULATION: Additional private fields specific to premium members
    private String trainerName;      // Assigned personal trainer
    private int sessionsPerMonth;    // Number of training sessions per month
    
    /**
     * Constructs a new PremiumMember with specified details.
     * Automatically sets the base fee to the premium member rate.
     * 
     * @param memberId unique identifier for the member
     * @param firstName member's first name
     * @param lastName member's last name
     * @param email member's email address
     * @param phone member's phone number
     * @param trainerName assigned personal trainer's name
     * @param sessionsPerMonth number of training sessions per month
     */
    public PremiumMember(String memberId, String firstName, String lastName,
                        String email, String phone, String trainerName, int sessionsPerMonth) {
        super(memberId, firstName, lastName, email, phone, Constants.PREMIUM_BASE_FEE); // INHERITANCE: Call parent constructor
        this.trainerName = trainerName;
        this.sessionsPerMonth = sessionsPerMonth;
    }
    
    /**
     * Calculates monthly fee for premium members.
     * 
     * Fee calculation includes:
     * - Base fee for premium membership
     * - Cost for personal training sessions
     * - 15% discount if goal achieved
     * - $20 bonus discount for high performance (rating ≥ 8)
     * 
     * @return calculated monthly fee
     */
    @Override // POLYMORPHISM: Override abstract method from parent class
    public double calculateMonthlyFee() {
        double fee = getBaseFee(); // INHERITANCE: Use method inherited from parent class
        
        // Add cost for personal training sessions
        fee += (sessionsPerMonth * Constants.SESSION_COST);
        
        // Apply goal achievement discount
        if (isGoalAchieved()) { // INHERITANCE: Use method inherited from parent class
            fee = fee * (1 - Constants.PREMIUM_GOAL_ACHIEVEMENT_DISCOUNT);
        }
        
        // Apply high performance bonus
        if (getPerformanceRating() >= Constants.HIGH_PERFORMANCE_THRESHOLD) { // INHERITANCE: Use method inherited from parent class
            fee -= Constants.HIGH_PERFORMANCE_BONUS;
        }
        
        return fee;
    }
    
    /**
     * Returns the display name for premium membership.
     * 
     * @return "Premium Membership (Personal Trainer)"
     */
    @Override // POLYMORPHISM: Override abstract method from parent class
    public String getMemberType() {
        return Constants.DISPLAY_PREMIUM_MEMBERSHIP;
    }
    
    /** @return assigned trainer's name */
    public String getTrainerName() { return trainerName; } // ENCAPSULATION: Controlled access to private field
    
    /** @param trainerName new trainer name to assign */
    public void setTrainerName(String trainerName) { this.trainerName = trainerName; }
    
    /** @return number of training sessions per month */
    public int getSessionsPerMonth() { return sessionsPerMonth; }
    
    /** @param sessionsPerMonth new number of sessions to set */
    public void setSessionsPerMonth(int sessionsPerMonth) { this.sessionsPerMonth = sessionsPerMonth; }
}
