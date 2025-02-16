import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.regex.*;
import java.util.List;

public class StudentWindow {
    private JFrame frame;
    private JPanel infoPanel;
    private static List<String> enrolledCourses;
    private int totalCredits;
    private JPanel menuPanel;
    private JPanel menuPanel2;
    private JPanel contentPanel;
    private JPanel feebuttonmenu;
    private JPanel input;
    private JPanel courseManagementPanel;
    private JPanel Studentmanagement;
    private JPanel feeButtonMenu;
    private String studentName;
    private String studentEmail;
    private String studentPhoneNumber;

    private static class CourseTree {
        private class Node {
            String course;
            Node left, right;

            Node(String course) {
                this.course = course;
                left = right = null;
            }
        }

        private Node root;
        private List<String> courseList = new ArrayList<>();
        private Map<String, Color> courseColors = new HashMap<>(); // Stores course colors dynamically

        public CourseTree() {
            // Default courses
            for (int i = 0; i < enrolledCourses.size(); i++) {
                addCourse(enrolledCourses.get(i));
            }
        }

        public void addCourse(String course) {
            root = insert(root, course);
            courseList.add(course);
            assignRandomColor(course); // Assign color dynamically
        }

        public void removeCourse(String course) {
            root = delete(root, course);
            courseList.remove(course);
            courseColors.remove(course); // Remove color when course is deleted
        }

        public String[][] getTimetableData() {
            String[][] timetableData = new String[5][6]; // 5 days, 6 periods per day
            Random random = new Random();

            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 6; j++) {
                    timetableData[i][j] = courseList.isEmpty() ? "Free Period"
                            : courseList.get(random.nextInt(courseList.size()));
                }
            }
            return timetableData;
        }

        // BST Insertion
        private Node insert(Node root, String course) {
            if (root == null)
                return new Node(course);
            if (course.compareTo(root.course) < 0)
                root.left = insert(root.left, course);
            else
                root.right = insert(root.right, course);
            return root;
        }

        // BST Deletion
        private Node delete(Node root, String course) {
            if (root == null)
                return null;
            if (course.compareTo(root.course) < 0)
                root.left = delete(root.left, course);
            else if (course.compareTo(root.course) > 0)
                root.right = delete(root.right, course);
            else {
                if (root.left == null)
                    return root.right;
                else if (root.right == null)
                    return root.left;

                root.course = minValue(root.right);
                root.right = delete(root.right, root.course);
            }
            return root;
        }

        private String minValue(Node root) {
            while (root.left != null)
                root = root.left;
            return root.course;
        }

        private void assignRandomColor(String course) {
            Random rand = new Random();
            Color randomColor = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256)); // Random color
            courseColors.put(course, randomColor);
        }

        public Color getCourseColor(String course) {
            return courseColors.getOrDefault(course, Color.WHITE); // Return white if not found
        }
    }

    // Course data placeholder (including AI and DSA courses)
    private static class CourseData {

        static final Map<String, Integer> DEFAULT_COURSES = new HashMap<String, Integer>() {
            {
                put("Database", 3);
                put("OOP", 3);
                put("Calculus", 4);
                put("Professional Practices", 3);
                put("Computer Networking", 3);
            }
        };

        static final Map<String, Integer> EXTRA_COURSES = new HashMap<String, Integer>() {
            {
                put("AI", 4);
                put("DSA", 4);
            }
        };
    }

    public List<String> getenrolledcourses() {
        return enrolledCourses;
    }

    public StudentWindow(String studentName) {
        this.studentName = studentName;
        enrolledCourses = new ArrayList<>(CourseData.DEFAULT_COURSES.keySet());
        totalCredits = CourseData.DEFAULT_COURSES.values().stream().mapToInt(Integer::intValue).sum();
        frame = new JFrame("Student Portal - SIS CUI ATD");
        frame.setSize(800, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(240, 240, 240));

        // Sidebar Menu Panel
        menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(new Color(41, 128, 185));
        menuPanel.setPreferredSize(new Dimension(200, 500));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Student Management Panel
        Studentmanagement = new JPanel();
        Studentmanagement.setLayout(new BoxLayout(Studentmanagement, BoxLayout.Y_AXIS));
        Studentmanagement.setBackground(new Color(41, 128, 185));

        // Fee Menu Panel
        feeButtonMenu = new JPanel();
        feeButtonMenu.setLayout(new BoxLayout(feeButtonMenu, BoxLayout.Y_AXIS));
        feeButtonMenu.setBackground(new Color(41, 128, 185));
        feeButtonMenu.setPreferredSize(new Dimension(200, 500));
        feeButtonMenu.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Fee Status Button
        JButton viewFeeStatusButton = new JButton("Fee Status");
        styleButton(viewFeeStatusButton);
        viewFeeStatusButton.addActionListener(e -> showFeeStatus());
        feeButtonMenu.add(viewFeeStatusButton);

        // Pay Fees Button
        JButton payFeeButton = new JButton("Submit Remaining Dues");
        styleButton(payFeeButton);
        payFeeButton.addActionListener(e -> payFees(payFeeButton));
        feeButtonMenu.add(payFeeButton);

        // Input Panel
        input = new JPanel();
        input.setLayout(new BoxLayout(input, BoxLayout.Y_AXIS));
        input.setBackground(new Color(41, 128, 185));

        JButton emailAddressButton = new JButton("Update Email");
        styleButton(emailAddressButton);
        emailAddressButton.addActionListener(e -> promptForValidEmail());
        input.add(emailAddressButton);

        JButton phoneNumberButton = new JButton("Update Phone Number");
        styleButton(phoneNumberButton);
        phoneNumberButton.addActionListener(e -> promptForValidPhoneNumber());
        input.add(phoneNumberButton);

        // Course Management Panel
        courseManagementPanel = new JPanel();
        courseManagementPanel.setLayout(new BoxLayout(courseManagementPanel, BoxLayout.Y_AXIS));
        courseManagementPanel.setBackground(new Color(41, 128, 185));

        JButton viewCoursesButton = new JButton("View Course List");
        styleButton(viewCoursesButton);
        viewCoursesButton.addActionListener(e -> showCourseList());
        courseManagementPanel.add(viewCoursesButton);

        JButton addCourseButton = new JButton("Add Course");
        styleButton(addCourseButton);
        addCourseButton.addActionListener(e -> addCourse());
        courseManagementPanel.add(addCourseButton);

        JButton removeCourseButton = new JButton("Remove Course");
        styleButton(removeCourseButton);
        removeCourseButton.addActionListener(e -> removeCourse());
        courseManagementPanel.add(removeCourseButton);

        JButton freezeSemesterButton = new JButton("Freeze Semester");
        styleButton(freezeSemesterButton);
        freezeSemesterButton.addActionListener(e -> freezeSemester());
        courseManagementPanel.add(freezeSemesterButton);

        JButton timetableButton = new JButton("Timetable");
        styleButton(timetableButton);
        timetableButton.addActionListener(e -> timetable());
        courseManagementPanel.add(timetableButton);

        // Toggle Button for Course & Student Management
        JButton toggleButton = new JButton("Course Management");
        toggleButton.setFont(new Font("Arial", Font.BOLD, 14));
        toggleButton.setBackground(new Color(41, 128, 185));
        toggleButton.setForeground(Color.WHITE);
        toggleButton.setPreferredSize(new Dimension(180, 50));
        toggleButton.setBorderPainted(false);
        toggleButton.setHorizontalAlignment(SwingConstants.CENTER);
        toggleButton.setVerticalAlignment(SwingConstants.CENTER);
        toggleButton.addActionListener(e -> {
            boolean isCourseVisible = !courseManagementPanel.isVisible();
            courseManagementPanel.setVisible(isCourseVisible);
            Studentmanagement.setVisible(true); // Ensure student management is always visible

            contentPanel.revalidate();
            contentPanel.repaint();
        });

        // Add components to menu panel
        menuPanel.add(toggleButton);
        menuPanel.add(courseManagementPanel);
        menuPanel.add(Studentmanagement);
        courseManagementPanel.setVisible(false);
        Studentmanagement.setVisible(false);

        // Main Content Panel
        contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setLayout(new BorderLayout());

        // Title
        JLabel title = new JLabel("Welcome to SIS Portal", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setPreferredSize(new Dimension(0, 60));
        contentPanel.add(title, BorderLayout.NORTH);

        // Info Panel for displaying course info
        infoPanel = new JPanel();
        infoPanel.setLayout(null);
        infoPanel.setBackground(Color.WHITE);
        contentPanel.add(infoPanel, BorderLayout.CENTER);

        // Display student profile
        displayStudentProfile();

        // Add Panels to Frame
        frame.add(menuPanel, BorderLayout.WEST);
        frame.add(contentPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setBackground(new Color(52, 152, 219));
        button.setForeground(Color.WHITE);
    }

    private void displayStudentProfile() {
        JLabel studentNameLabel = new JLabel("Student: " + studentName);
        studentNameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        studentNameLabel.setBounds(30, 60, 300, 30);
        infoPanel.add(studentNameLabel);

        JLabel studentEmailLabel = new JLabel("Email: " + studentEmail);
        studentEmailLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        studentEmailLabel.setBounds(30, 90, 300, 30);
        infoPanel.add(studentEmailLabel);

        JLabel studentPhoneLabel = new JLabel("Phone: " + studentPhoneNumber);
        studentPhoneLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        studentPhoneLabel.setBounds(30, 120, 300, 30);
        infoPanel.add(studentPhoneLabel);
    }

    private String promptForValidEmail() {
        String email;
        Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@gmail\\.com$");

        while (true) {
            email = JOptionPane.showInputDialog(frame, "Enter your email address (e.g., example@gmail.com):");
            if (email != null && emailPattern.matcher(email).matches()) {
                break;
            } else {
                JOptionPane.showMessageDialog(frame, "Please enter a valid Gmail address (e.g., example@gmail.com).");
            }
        }

        return email;
    }

    private String promptForValidPhoneNumber() {
        String phoneNumber;
        Pattern phonePattern = Pattern.compile("^\\d{4}\\d{7}$");

        while (true) {
            phoneNumber = JOptionPane.showInputDialog(frame, "Enter your phone number (e.g., 12345678901):");
            if (phoneNumber != null && phonePattern.matcher(phoneNumber).matches()) {
                break;
            } else {
                JOptionPane.showMessageDialog(frame, "Please enter a valid phone number (e.g., 12345678901).");
            }
        }

        return phoneNumber;
    }

    // Global variable to track fee payment status
    private boolean isFeePaid = false;

    private void showFeeStatus() {
        if (isFeePaid) {
            JOptionPane.showMessageDialog(frame, "Your fees have been PAID.", "Fee Status",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(frame, "Your current outstanding fees: PKR 50,000", "Fee Status",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void payFees(JButton payFeeButton) {
        if (isFeePaid) {
            JOptionPane.showMessageDialog(frame, "Your fees are already paid!", "Info",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String amountStr = JOptionPane.showInputDialog(frame, "Enter the amount to pay:", "Pay Fees",
                JOptionPane.QUESTION_MESSAGE);

        if (amountStr != null && !amountStr.trim().isEmpty()) {
            try {
                int amount = Integer.parseInt(amountStr.trim());
                if (amount >= 50000) { // Assuming 50,000 is the total fee
                    isFeePaid = true;
                    JOptionPane.showMessageDialog(frame, "Payment of PKR " + amount + " successfully submitted!",
                            "Payment Successful", JOptionPane.INFORMATION_MESSAGE);
                    payFeeButton.setText("Fees Paid");
                    payFeeButton.setEnabled(false); // Disable button after payment
                } else {
                    JOptionPane.showMessageDialog(frame, "Insufficient amount. Please pay the full fee!",
                            "Invalid Payment", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Invalid input. Please enter a numeric amount.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateCourseDisplay() {
        infoPanel.removeAll();
        displayStudentProfile(); // Display the updated profile details

        int yOffset = 160; // Adjust offset to avoid overlap with student profile
        for (String course : enrolledCourses) {
            JLabel courseLabel = new JLabel(course + " ("
                    + CourseData.DEFAULT_COURSES.getOrDefault(course, CourseData.EXTRA_COURSES.get(course))
                    + " credits)");
            courseLabel.setBounds(30, yOffset, 600, 25);
            courseLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            infoPanel.add(courseLabel);
            yOffset += 30;
        }

        // Display total credits
        JLabel totalCreditsLabel = new JLabel("Total Credits: " + totalCredits);
        totalCreditsLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        totalCreditsLabel.setBounds(30, yOffset, 200, 30);
        infoPanel.add(totalCreditsLabel);

        infoPanel.repaint();
        infoPanel.revalidate();
    }

    private void showCourseList() {
        contentPanel.removeAll();
        JLabel title = new JLabel("Course List", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setPreferredSize(new Dimension(0, 60));
        contentPanel.add(title, BorderLayout.NORTH);

        infoPanel.setLayout(null);
        updateCourseDisplay();

        contentPanel.add(infoPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void addCourse() {
        if (totalCredits >= 22) {
            JOptionPane.showMessageDialog(frame, "You have reached the maximum credit hour limit!");
            return;
        }

        // Combine both default and extra courses
        Set<String> availableCourses = new HashSet<>(CourseData.DEFAULT_COURSES.keySet());
        availableCourses.addAll(CourseData.EXTRA_COURSES.keySet());

        // Remove already enrolled courses from the available list
        availableCourses.removeAll(enrolledCourses);

        if (availableCourses.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No more courses available to add.");
            return;
        }

        String[] availableCoursesArray = availableCourses.toArray(new String[0]);
        String course = (String) JOptionPane.showInputDialog(frame, "Select a course:", "Add Course",
                JOptionPane.QUESTION_MESSAGE, null, availableCoursesArray, availableCoursesArray[0]);

        if (course != null) {
            // Check if adding the course will exceed the credit limit
            int courseCredits = CourseData.DEFAULT_COURSES.getOrDefault(course, CourseData.EXTRA_COURSES.get(course));
            if (totalCredits + courseCredits > 22) {
                JOptionPane.showMessageDialog(frame, "Adding this course will exceed the maximum credit limit!");
                return;
            }

            enrolledCourses.add(course);
            totalCredits += courseCredits;
            showCourseList(); // Update the course list view
        }
    }

    private void removeCourse() {
        if (totalCredits <= 10) {
            JOptionPane.showMessageDialog(frame, "You cannot drop below 10 credit hours!");
            return;
        }

        String[] coursesArray = enrolledCourses.toArray(new String[0]);
        String course = (String) JOptionPane.showInputDialog(frame, "Select a course to remove:", "Remove Course",
                JOptionPane.QUESTION_MESSAGE, null, coursesArray, coursesArray[0]);
        if (course != null) {
            // Check if removing the course will drop below the minimum credit limit
            int courseCredits = CourseData.DEFAULT_COURSES.getOrDefault(course, CourseData.EXTRA_COURSES.get(course));
            if (totalCredits - courseCredits < 10) {
                JOptionPane.showMessageDialog(frame,
                        "Removing this course will drop your credit hours below the minimum limit!");
                return;
            }

            enrolledCourses.remove(course);
            totalCredits -= courseCredits;

            // Add the course back to the extra courses list if it was a removed course
            if (!CourseData.DEFAULT_COURSES.containsKey(course)) {
                CourseData.EXTRA_COURSES.put(course, courseCredits);
            }

            showCourseList(); // Update the course list view
        }
    }

    private void freezeSemester() {
        JOptionPane.showMessageDialog(frame, "The semester is now frozen. No more changes can be made.");
    }

    private JFrame fframe;
    private JButton[][] timetableButtons;
    private String[][] subjects;
    private Point firstClick = null;
    private CourseTree courseTree; // Dynamic Tree

    public void timetable() {
        courseTree = new CourseTree(); // Initialize Tree
        subjects = courseTree.getTimetableData(); // Load initial timetable
        timetabler();
    }

    private void timetabler() {
        fframe = new JFrame("Interactive Timetable");
        fframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fframe.setSize(800, 400);
        fframe.setLayout(new BorderLayout());

        JPanel timetablePanel = new JPanel(new GridLayout(6, 6, 5, 5)); // Organized with gaps
        timetableButtons = new JButton[5][6];

        // Header row with periods
        String[] periodNames = { "Period 1", "Period 2", "Period 3", "Period 4", "Period 5", "Period 6" };
        for (String period : periodNames) {
            JLabel label = new JLabel(period, SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 14));
            timetablePanel.add(label);
        }

        // Subject slots
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 6; j++) {
                JButton slot = new JButton(subjects[i][j]);
                slot.setBackground(courseTree.getCourseColor(subjects[i][j])); // Dynamic Color
                slot.setFont(new Font("Arial", Font.PLAIN, 14));
                slot.setOpaque(true);
                slot.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

                int row = i, col = j; // Final variables for action
                slot.addActionListener(e -> handleClick(row, col));

                timetableButtons[i][j] = slot;
                timetablePanel.add(slot);
            }
        }

        frame.add(timetablePanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void handleClick(int row, int col) {
        if (firstClick == null) {
            firstClick = new Point(row, col); // Store first selection
        } else {
            // Swap subjects
            int r1 = firstClick.x, c1 = firstClick.y;
            String temp = subjects[r1][c1];
            subjects[r1][c1] = subjects[row][col];
            subjects[row][col] = temp;

            // Update buttons
            timetableButtons[r1][c1].setText(subjects[r1][c1]);
            timetableButtons[r1][c1].setBackground(courseTree.getCourseColor(subjects[r1][c1]));

            timetableButtons[row][col].setText(subjects[row][col]);
            timetableButtons[row][col].setBackground(courseTree.getCourseColor(subjects[row][col]));

            firstClick = null; // Reset selection
        }
    }
}
