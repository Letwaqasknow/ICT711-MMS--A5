/**
 * Application-wide constants for the Member Management System.
 * 
 * Centralizes all hard-coded values to improve maintainability, consistency,
 * and configurability. Prevents magic numbers and duplicate string literals
 * throughout the codebase.
 * 

 */
public final class Constants {
    
    /**
     * Private constructor to prevent instantiation of utility class.
     * 
     * @throws AssertionError if instantiation is attempted
     */
    private Constants() {
        throw new AssertionError("Constants class should not be instantiated");
    }
    
    // ========== FILE AND DATA CONSTANTS ==========
    /** Default CSV file name for member data persistence */
    public static final String DEFAULT_FILE_NAME = "member_data.csv";
    
    /** CSV header row defining column structure */
    public static final String CSV_HEADER = "Type,ID,FirstName,LastName,Email,Phone,PerformanceRating,GoalAchieved,Extra1,Extra2";
    
    /** Minimum required fields for valid CSV member record */
    public static final int MIN_CSV_FIELDS_REQUIRED = 7;
    
    /** CSV field delimiter character */
    public static final String CSV_DELIMITER = ",";
    
    // ========== MEMBER TYPE CONSTANTS ==========
    /** Identifier for regular membership type in CSV and logic */
    public static final String MEMBER_TYPE_REGULAR = "Regular";
    
    /** Identifier for premium membership type in CSV and logic */
    public static final String MEMBER_TYPE_PREMIUM = "Premium";
    
    /** Identifier for student membership type in CSV and logic */
    public static final String MEMBER_TYPE_STUDENT = "Student";
    
    // ========== DEFAULT VALUES ==========
    /** Default trainer name when not specified for premium members */
    public static final String DEFAULT_TRAINER_NAME = "Default Trainer";
    
    /** Default university when not specified for student members */
    public static final String DEFAULT_UNIVERSITY = "Default University";
    
    /** Default student ID when not specified for student members */
    public static final String DEFAULT_STUDENT_ID = "STU001";
    
    /** Default number of training sessions per month for premium members */
    public static final int DEFAULT_SESSIONS_PER_MONTH = 4;
    
    /** Default performance rating for new members (0-10 scale) */
    public static final int DEFAULT_PERFORMANCE_RATING = 5;
    
    /** Default goal achievement status for new members */
    public static final boolean DEFAULT_GOAL_ACHIEVED = false;
    
    // ========== PERFORMANCE THRESHOLDS ==========
    /** Performance rating threshold for high performer benefits (≥8) */
    public static final int HIGH_PERFORMANCE_THRESHOLD = 8;
    
    /** Performance rating threshold for reminder letters (<5) */
    public static final int LOW_PERFORMANCE_THRESHOLD = 5;
    
    /** Performance rating threshold for penalties (<3) */
    public static final int PENALTY_PERFORMANCE_THRESHOLD = 3;
    
    /** Minimum allowed performance rating */
    public static final int MIN_PERFORMANCE_RATING = 0;
    
    /** Maximum allowed performance rating */
    public static final int MAX_PERFORMANCE_RATING = 10;
    
    // ========== FEES AND FINANCIAL CONSTANTS ==========
    /** Monthly base fee for regular membership ($) */
    public static final int REGULAR_BASE_FEE = 50;
    
    /** Monthly base fee for premium membership ($) */
    public static final int PREMIUM_BASE_FEE = 100;
    
    /** Monthly base fee for student membership ($) */
    public static final int STUDENT_BASE_FEE = 40;
    
    /** Cost per personal training session for premium members ($) */
    public static final int SESSION_COST = 25;
    
    /** Penalty amount for low performance regular members ($) */
    public static final int LOW_PERFORMANCE_PENALTY = 10;
    
    /** Bonus discount for high performance premium members ($) */
    public static final int HIGH_PERFORMANCE_BONUS = 20;
    
    /** Additional discount for goal-achieving students ($) */
    public static final int STUDENT_GOAL_ACHIEVEMENT_BONUS = 5;
    
    /** Minimum monthly fee for student members ($) */
    public static final int MINIMUM_STUDENT_FEE = 20;
    
    /** Goal achievement discount rate for regular members (10%) */
    public static final double REGULAR_GOAL_ACHIEVEMENT_DISCOUNT = 0.1;
    
    /** Goal achievement discount rate for premium members (15%) */
    public static final double PREMIUM_GOAL_ACHIEVEMENT_DISCOUNT = 0.15;
    
    /** Base discount rate for all student members (30%) */
    public static final double STUDENT_BASE_DISCOUNT = 0.3;
    
    // ========== UI MENU CONSTANTS ==========
    /** Main menu title display */
    public static final String MENU_TITLE = "===== Member Management System - ICT711 =====";
    
    /** Menu option for loading records from file */
    public static final String MENU_LOAD_RECORDS = "1. Load records from file";
    
    /** Menu option for adding new member */
    public static final String MENU_ADD_MEMBER = "2. Add new member";
    
    /** Menu option for updating member information */
    public static final String MENU_UPDATE_MEMBER = "3. Update member information";
    
    /** Menu option for deleting member */
    public static final String MENU_DELETE_MEMBER = "4. Delete member";
    
    /** Menu option for querying member details */
    public static final String MENU_QUERY_MEMBERS = "5. View/Query member details";
    
    /** Menu option for managing performance and fees */
    public static final String MENU_MANAGE_PERFORMANCE = "6. Manage performance and fees";
    
    /** Menu option for displaying statistics */
    public static final String MENU_DISPLAY_STATISTICS = "7. Display statistics";
    
    /** Menu option for exiting application */
    public static final String MENU_EXIT = "8. Exit";
    
    /** Main menu input prompt */
    public static final String MENU_PROMPT = "Please choose an option: ";
    
    // ========== CONFIRMATION PROMPTS ==========
    /** Standard 'yes' confirmation input */
    public static final String CONFIRMATION_YES = "y";
    
    /** Prompt for saving data to file */
    public static final String SAVE_TO_FILE_PROMPT = "Save to file? (y/n): ";
    
    /** Prompt for updating email address */
    public static final String UPDATE_EMAIL_PROMPT = "Update email? (y/n): ";
    
    /** Prompt for updating phone number */
    public static final String UPDATE_PHONE_PROMPT = "Update phone? (y/n): ";
    
    /** Prompt for updating performance rating */
    public static final String UPDATE_RATING_PROMPT = "Update performance rating? (y/n): ";
    
    /** Prompt for updating goal achievement status */
    public static final String UPDATE_GOAL_PROMPT = "Update goal achievement? (y/n): ";
    
    /** Prompt for confirming member deletion */
    public static final String CONFIRM_DELETION_PROMPT = "Confirm deletion? (y/n): ";
    
    // ========== UPDATE FIELD KEYS ==========
    /** Map key for email field updates */
    public static final String UPDATE_KEY_EMAIL = "email";
    
    /** Map key for phone field updates */
    public static final String UPDATE_KEY_PHONE = "phone";
    
    /** Map key for performance rating field updates */
    public static final String UPDATE_KEY_PERFORMANCE_RATING = "performanceRating";
    
    /** Map key for goal achievement field updates */
    public static final String UPDATE_KEY_GOAL_ACHIEVED = "goalAchieved";
    
    // ========== MESSAGES ==========
    /** Success message format for member addition */
    public static final String MSG_MEMBER_ADDED = "Member added successfully: %s with ID: %s";
    
    /** Success message format for member removal */
    public static final String MSG_MEMBER_REMOVED = "Member removed successfully: %s";
    
    /** Error message format when member not found */
    public static final String MSG_MEMBER_NOT_FOUND = "Member not found: %s";
    
    /** Success message format for member update */
    public static final String MSG_MEMBER_UPDATED = "Member updated successfully: %s with ID: %s";
    
    /** Info message format when searching for member */
    public static final String MSG_LOADING_MEMBERS = "Finding member by ID: %s";
    
    /** Info message format when searching by name */
    public static final String MSG_FINDING_BY_NAME = "Finding members by name: %s";
    
    /** Info message format when updating member */
    public static final String MSG_UPDATING_MEMBER = "Updating member with ID: %s";
    
    /** Warning message format when member not found for update */
    public static final String MSG_MEMBER_NOT_FOUND_UPDATE = "Member not found: %s for update, skipping...";
    
    /** Success message format for loading members from file */
    public static final String MSG_MEMBERS_LOADED = "Loaded %d members from %s";
    
    /** Success message format for saving members to file */
    public static final String MSG_MEMBERS_SAVED = "Saved %d members to %s";
    
    /** Application exit message with team information */
    public static final String MSG_EXIT_MESSAGE = "Thank you for using Member Management System!\n========================================\nThis program is developed for ICT711 group project.\nThe group members are: \n -Muhammad Eman Ejaz - 20034038 \n -Sajina Rana - 2005243 \n -Waqas Iqbal - 2005647 \n -Sravanth Rao - 2003358 \n========================================\nSee you next time!";
    
    /** Error message for invalid menu option */
    public static final String MSG_INVALID_OPTION = "Invalid option. Please try again.";
    
    /** Error message for invalid numeric input */
    public static final String MSG_INVALID_INPUT = "Invalid input. Please enter a number.";
    
    /** Generic error message format */
    public static final String MSG_ERROR_OCCURRED = "An error occurred: %s";
    
    // ========== STATISTICS LABELS ==========
    /** Statistics display title */
    public static final String STATS_TITLE = "\n===== Gym Statistics =====";
    
    /** Format for total members count display */
    public static final String STATS_TOTAL_MEMBERS = "Total Members: %d";
    
    /** Format for regular members count display */
    public static final String STATS_REGULAR_MEMBERS = "Regular Members: %d";
    
    /** Format for premium members count display */
    public static final String STATS_PREMIUM_MEMBERS = "Premium Members: %d";
    
    /** Format for student members count display */
    public static final String STATS_STUDENT_MEMBERS = "Student Members: %d";
    
    /** Format for average performance rating display */
    public static final String STATS_AVG_PERFORMANCE = "Average Performance Rating: %.2f";
    
    /** Format for goal achievers count display */
    public static final String STATS_GOAL_ACHIEVERS = "Members Who Achieved Goals: %d";
    
    /** Statistics display footer */
    public static final String STATS_FOOTER = "=========================\n";
    
    /** Message when no members exist in system */
    public static final String STATS_NO_MEMBERS = "No members in the system.";
    
    // ========== LETTER TEMPLATES ==========
    /** Template for appreciation letters to high performers */
    public static final String APPRECIATION_LETTER_TEMPLATE = 
        "Dear %s,\n\n" +
        "Congratulations on your outstanding performance this month!\n" +
        "Your dedication with a rating of %d/10 is truly commendable.\n" +
        "Keep up the excellent work!\n\n" +
        "Best regards,\nGym Management";
    
    /** Template for reminder letters to low performers */
    public static final String REMINDER_LETTER_TEMPLATE = 
        "Dear %s,\n\n" +
        "We noticed your performance rating is %d/10 this month.\n" +
        "We encourage you to participate more actively in gym activities.\n" +
        "Our trainers are here to help you achieve your fitness goals!\n\n" +
        "Best regards,\nGym Management";
        
    // ========== MEMBER TYPE DISPLAY NAMES ==========
    /** Display name for regular membership type */
    public static final String DISPLAY_REGULAR_MEMBERSHIP = "Regular Membership";
    
    /** Display name for premium membership type */
    public static final String DISPLAY_PREMIUM_MEMBERSHIP = "Premium Membership (Personal Trainer)";
    
    /** Display name for student membership type */
    public static final String DISPLAY_STUDENT_MEMBERSHIP = "Student Membership";
} 