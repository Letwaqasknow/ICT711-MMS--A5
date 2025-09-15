import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * Dialog for adding new members or editing existing members.
 * 
 * Provides a comprehensive form interface with:
 * - Member type selection
 * - Common fields for all member types
 * - Type-specific fields that show/hide dynamically
 * - Input validation
 * - Performance and goal management
 * 
 * Supports all member types: Regular, Premium, and Student
 */
public class MemberDialog extends JDialog {
    
    private boolean successful = false;
    private Member member;
    private EnhancedMemberManager manager;
    
    // Common UI components
    private JTextField memberIdField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JSlider performanceSlider;
    private JCheckBox goalAchievedBox;
    private JComboBox<String> memberTypeCombo;
    
    // Premium member specific
    private JTextField trainerNameField;
    private JSpinner sessionsSpinner;
    private JPanel premiumPanel;
    
    // Student member specific
    private JTextField studentIdField;
    private JTextField universityField;
    private JPanel studentPanel;
    
    // Control buttons
    private JButton saveButton;
    private JButton cancelButton;
    
    /**
     * Constructs a new MemberDialog.
     * 
     * @param parent the parent window
     * @param title dialog title
     * @param member existing member to edit (null for new member)
     * @param manager the member manager instance
     */
    public MemberDialog(JFrame parent, String title, Member member, EnhancedMemberManager manager) {
        super(parent, title, true);
        this.member = member;
        this.manager = manager;
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        populateFields();
        
        setSize(500, 600);
        setLocationRelativeTo(parent);
        setResizable(false);
    }
    
    /**
     * Initializes all dialog components.
     */
    private void initializeComponents() {
        // Common fields
        memberIdField = new JTextField(15);
        firstNameField = new JTextField(15);
        lastNameField = new JTextField(15);
        emailField = new JTextField(20);
        phoneField = new JTextField(15);
        
        // Performance components
        performanceSlider = new JSlider(0, 10, 5);
        performanceSlider.setMajorTickSpacing(2);
        performanceSlider.setMinorTickSpacing(1);
        performanceSlider.setPaintTicks(true);
        performanceSlider.setPaintLabels(true);
        
        goalAchievedBox = new JCheckBox("Goal achieved this month");
        
        // Member type selection
        memberTypeCombo = new JComboBox<>(new String[]{"Regular", "Premium", "Student"});
        
        // Premium member fields
        trainerNameField = new JTextField(15);
        sessionsSpinner = new JSpinner(new SpinnerNumberModel(4, 1, 20, 1));
        
        // Student member fields
        studentIdField = new JTextField(15);
        universityField = new JTextField(20);
        
        // Control buttons
        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");
        
        // Set tooltips
        memberIdField.setToolTipText("Unique identifier for the member");
        emailField.setToolTipText("Member's email address");
        phoneField.setToolTipText("Member's contact phone number");
        performanceSlider.setToolTipText("Performance rating from 0 (poor) to 10 (excellent)");
        trainerNameField.setToolTipText("Name of assigned personal trainer");
        sessionsSpinner.setToolTipText("Number of training sessions per month");
        studentIdField.setToolTipText("Student's academic ID number");
        universityField.setToolTipText("Student's educational institution");
    }
    
    /**
     * Sets up the dialog layout with appropriate panels and borders.
     */
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        
        // Main content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));
        
        // Member type panel
        contentPanel.add(createMemberTypePanel());
        contentPanel.add(Box.createVerticalStrut(10));
        
        // Basic info panel
        contentPanel.add(createBasicInfoPanel());
        contentPanel.add(Box.createVerticalStrut(10));
        
        // Performance panel
        contentPanel.add(createPerformancePanel());
        contentPanel.add(Box.createVerticalStrut(10));
        
        // Type-specific panels
        premiumPanel = createPremiumPanel();
        studentPanel = createStudentPanel();
        
        contentPanel.add(premiumPanel);
        contentPanel.add(studentPanel);
        
        // Button panel
        JPanel buttonPanel = createButtonPanel();
        
        add(contentPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Initial visibility setup
        updatePanelVisibility();
    }
    
    /**
     * Creates the member type selection panel.
     */
    private JPanel createMemberTypePanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("Member Type"));
        
        panel.add(new JLabel("Type:"));
        panel.add(memberTypeCombo);
        
        return panel;
    }
    
    /**
     * Creates the basic member information panel.
     */
    private JPanel createBasicInfoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Basic Information"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Row 0: Member ID
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Member ID:"), gbc);
        gbc.gridx = 1;
        panel.add(memberIdField, gbc);
        
        // Row 1: First Name
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("First Name:"), gbc);
        gbc.gridx = 1;
        panel.add(firstNameField, gbc);
        
        // Row 2: Last Name
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Last Name:"), gbc);
        gbc.gridx = 1;
        panel.add(lastNameField, gbc);
        
        // Row 3: Email
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(emailField, gbc);
        gbc.gridwidth = 1;
        
        // Row 4: Phone
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        panel.add(phoneField, gbc);
        
        return panel;
    }
    
    /**
     * Creates the performance tracking panel.
     */
    private JPanel createPerformancePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Performance"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Performance rating
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Performance Rating:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(performanceSlider, gbc);
        
        // Goal achievement
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 3;
        panel.add(goalAchievedBox, gbc);
        
        return panel;
    }
    
    /**
     * Creates the premium member specific panel.
     */
    private JPanel createPremiumPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Premium Member Details"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Trainer name
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Trainer Name:"), gbc);
        gbc.gridx = 1;
        panel.add(trainerNameField, gbc);
        
        // Sessions per month
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Sessions/Month:"), gbc);
        gbc.gridx = 1;
        panel.add(sessionsSpinner, gbc);
        
        return panel;
    }
    
    /**
     * Creates the student member specific panel.
     */
    private JPanel createStudentPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Student Member Details"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Student ID
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Student ID:"), gbc);
        gbc.gridx = 1;
        panel.add(studentIdField, gbc);
        
        // University
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("University:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(universityField, gbc);
        
        return panel;
    }
    
    /**
     * Creates the button panel with Save and Cancel buttons.
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));
        
        saveButton.setPreferredSize(new Dimension(80, 30));
        cancelButton.setPreferredSize(new Dimension(80, 30));
        
        panel.add(saveButton);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(cancelButton);
        
        return panel;
    }
    
    /**
     * Sets up event handlers for all components.
     */
    private void setupEventHandlers() {
        // Member type change handler
        memberTypeCombo.addActionListener(e -> updatePanelVisibility());
        
        // Save button handler
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateAndSave()) {
                    successful = true;
                    dispose();
                }
            }
        });
        
        // Cancel button handler
        cancelButton.addActionListener(e -> dispose());
        
        // Enter key handling
        getRootPane().setDefaultButton(saveButton);
        
        // Escape key handling
        KeyStroke escapeStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeStroke, "ESCAPE");
        getRootPane().getActionMap().put("ESCAPE", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
    
    /**
     * Updates the visibility of type-specific panels based on selected member type.
     */
    private void updatePanelVisibility() {
        String selectedType = (String) memberTypeCombo.getSelectedItem();
        
        premiumPanel.setVisible("Premium".equals(selectedType));
        studentPanel.setVisible("Student".equals(selectedType));
        
        pack();
        repaint();
    }
    
    /**
     * Populates form fields with existing member data (for editing).
     */
    private void populateFields() {
        if (member != null) {
            // Basic fields
            memberIdField.setText(member.getMemberId());
            memberIdField.setEnabled(false); // Don't allow ID changes
            firstNameField.setText(member.getFirstName());
            lastNameField.setText(member.getLastName());
            emailField.setText(member.getEmail());
            phoneField.setText(member.getPhone());
            performanceSlider.setValue(member.getPerformanceRating());
            goalAchievedBox.setSelected(member.isGoalAchieved());
            
            // Set member type
            if (member instanceof PremiumMember) {
                memberTypeCombo.setSelectedItem("Premium");
                PremiumMember pm = (PremiumMember) member;
                trainerNameField.setText(pm.getTrainerName());
                sessionsSpinner.setValue(pm.getSessionsPerMonth());
            } else if (member instanceof StudentMember) {
                memberTypeCombo.setSelectedItem("Student");
                StudentMember sm = (StudentMember) member;
                studentIdField.setText(sm.getStudentId());
                universityField.setText(sm.getUniversity());
            } else {
                memberTypeCombo.setSelectedItem("Regular");
            }
            
            memberTypeCombo.setEnabled(false); // Don't allow type changes
        } else {
            // Set defaults for new member
            performanceSlider.setValue(5);
            goalAchievedBox.setSelected(false);
        }
    }
    
    /**
     * Validates form input and saves the member.
     * 
     * @return true if validation passed and member was saved
     */
    private boolean validateAndSave() {
        // Validate required fields
        String memberId = memberIdField.getText().trim();
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        
        if (memberId.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || 
            email.isEmpty() || phone.isEmpty()) {
            showError("Please fill in all required fields.");
            return false;
        }
        
        // Validate email format (basic check)
        if (!email.contains("@") || !email.contains(".")) {
            showError("Please enter a valid email address.");
            return false;
        }
        
        // Check for duplicate member ID (only for new members)
        if (member == null && manager.findMemberById(memberId) != null) {
            showError("Member ID already exists. Please choose a different ID.");
            return false;
        }
        
        try {
            String selectedType = (String) memberTypeCombo.getSelectedItem();
            
            if (member == null) {
                // Create new member
                switch (selectedType) {
                    case "Regular":
                        member = new RegularMember(memberId, firstName, lastName, email, phone);
                        break;
                    case "Premium":
                        String trainer = trainerNameField.getText().trim();
                        if (trainer.isEmpty()) trainer = "Default Trainer";
                        int sessions = (Integer) sessionsSpinner.getValue();
                        member = new PremiumMember(memberId, firstName, lastName, email, phone, trainer, sessions);
                        break;
                    case "Student":
                        String studentId = studentIdField.getText().trim();
                        String university = universityField.getText().trim();
                        if (studentId.isEmpty()) studentId = "STU" + System.currentTimeMillis();
                        if (university.isEmpty()) university = "Default University";
                        member = new StudentMember(memberId, firstName, lastName, email, phone, studentId, university);
                        break;
                }
                
                // Set performance data
                member.setPerformanceRating(performanceSlider.getValue());
                member.setGoalAchieved(goalAchievedBox.isSelected());
                
                // Add to manager
                manager.addMember(member);
                
            } else {
                // Update existing member
                member.setFirstName(firstName);
                member.setLastName(lastName);
                member.setEmail(email);
                member.setPhone(phone);
                member.setPerformanceRating(performanceSlider.getValue());
                member.setGoalAchieved(goalAchievedBox.isSelected());
                
                // Update type-specific fields
                if (member instanceof PremiumMember) {
                    PremiumMember pm = (PremiumMember) member;
                    pm.setTrainerName(trainerNameField.getText().trim());
                    pm.setSessionsPerMonth((Integer) sessionsSpinner.getValue());
                } else if (member instanceof StudentMember) {
                    StudentMember sm = (StudentMember) member;
                    sm.setStudentId(studentIdField.getText().trim());
                    sm.setUniversity(universityField.getText().trim());
                }
            }
            
            return true;
            
        } catch (Exception e) {
            showError("Error saving member: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Shows an error message to the user.
     */
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Validation Error", JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Returns whether the dialog operation was successful.
     * 
     * @return true if member was saved successfully
     */
    public boolean wasSuccessful() {
        return successful;
    }
}