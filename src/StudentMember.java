
/**
 * Student gym member with special discounted rates.
 * 
 * Extends Member base class to provide specific implementation for:
 * - 30% base discount for all students
 * - Additional $5 discount for goal achievement
 * - Minimum fee protection ($20 minimum)
 * - No performance penalties
 * 
 * Demonstrates inheritance and polymorphism from Member base class.
 *

 */
public class StudentMember extends Member { // INHERITANCE: Extends Member base class
    // ENCAPSULATION: Additional private fields specific to student members
    private String studentId;    // Student's academic ID
    private String university;   // Student's educational institution
    
    /**
     * Constructs a new StudentMember with specified details.
     * Automatically sets the base fee to the student member rate.
     * 
     * @param memberId unique identifier for the member
     * @param firstName member's first name
     * @param lastName member's last name
     * @param email member's email address
     * @param phone member's phone number
     * @param studentId student's academic ID
     * @param university student's educational institution
     */
    public StudentMember(String memberId, String firstName, String lastName,
                        String email, String phone, String studentId, String university) {
        super(memberId, firstName, lastName, email, phone, Constants.STUDENT_BASE_FEE); // INHERITANCE: Call parent constructor
        this.studentId = studentId;
        this.university = university;
    }
    
    /**
     * Calculates monthly fee for student members.
     * 
     * Fee calculation includes:
     * - Base fee with 30% student discount
     * - Additional $5 discount if goal achieved
     * - Minimum fee of $20 guaranteed
     * - No performance penalties (student-friendly policy)
     * 
     * @return calculated monthly fee (minimum $20)
     */
    @Override // POLYMORPHISM: Override abstract method from parent class
    public double calculateMonthlyFee() {
        double fee = getBaseFee() * (1 - Constants.STUDENT_BASE_DISCOUNT); // INHERITANCE: Use method inherited from parent class
        
        // Additional discount for goal achievement
        if (isGoalAchieved()) { // INHERITANCE: Use method inherited from parent class
            fee -= Constants.STUDENT_GOAL_ACHIEVEMENT_BONUS;
        }
        
        // Ensure minimum fee is maintained
        return Math.max(fee, Constants.MINIMUM_STUDENT_FEE);
    }
    
    /**
     * Returns the display name for student membership.
     * 
     * @return "Student Membership"
     */
    @Override // POLYMORPHISM: Override abstract method from parent class
    public String getMemberType() {
        return Constants.DISPLAY_STUDENT_MEMBERSHIP;
    }
    
    /** @return student's academic ID */
    public String getStudentId() { return studentId; } // ENCAPSULATION: Controlled access to private field
    
    /** @param studentId new student ID to set */
    public void setStudentId(String studentId) { this.studentId = studentId; }
    
    /** @return student's university/institution */
    public String getUniversity() { return university; }
    
    /** @param university new university to set */
    public void setUniversity(String university) { this.university = university; }
}
