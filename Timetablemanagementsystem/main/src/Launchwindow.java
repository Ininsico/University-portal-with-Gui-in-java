import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

// Class to store saved login data
class saveddata {
    private HashMap<String, String> Admininfo = new HashMap<>();
    private HashMap<String, String> Teacherinfo = new HashMap<>();
    private HashMap<String, String> Studentinfo = new HashMap<>();

    // Adding default users
    public void adder() {
        Admininfo.put("Director", "123");
        Admininfo.put("Registrar", "123");
        Admininfo.put("ExamManager", "123");

        Teacherinfo.put("Mohsin", "123");
        Teacherinfo.put("Umair", "123");
        Teacherinfo.put("Shamas", "123");
        Teacherinfo.put("Mustafa", "123");
        Teacherinfo.put("Javid", "123");

        Studentinfo.put("Arslan", "123");
        Studentinfo.put("Ayyan", "123");
        Studentinfo.put("Ahmedullah", "123");
        Studentinfo.put("Huzaifa", "123");
    }

    public HashMap<String, String> getAdmininfo() { return Admininfo; }
    public HashMap<String, String> getStudentinfo() { return Studentinfo; }
    public HashMap<String, String> getTeacherinfo() { return Teacherinfo; }
}

// Class to store user session data
class Savenamedata {
    private String name;
    private String role;

    public void setUser(String name, String role) {
        this.name = name;
        this.role = role;
    }

    public String getName() { return name; }
    public String getRole() { return role; }
}

// Class to validate login
// Inside the validatedata class
 class validatedata {
    saveddata data = new saveddata();
    Savenamedata sessionData = new Savenamedata();

    public validatedata() {
        data.adder();
    }

    public boolean validateUser(String name, String password, String role) {
        HashMap<String, String> userMap;
    
        switch (role) {
            case "Admin":
                userMap = data.getAdmininfo();
                break;
            case "Teacher":
                userMap = data.getTeacherinfo();
                break;
            case "Student":
                userMap = data.getStudentinfo();
                break;
            default:
                return false;
        }
    
        // Convert input username to lowercase for case-insensitive comparison
        name = name.toLowerCase();
    
        for (String storedName : userMap.keySet()) {
            if (storedName.equalsIgnoreCase(name) && userMap.get(storedName).equals(password)) {
                // Save session data after successful login
                sessionData.setUser(storedName, role); // store the name and role
                return true;
            }
        }
    
        return false;
    }

    public Savenamedata getSessionData() {
        return sessionData;
    }
}

// Main GUI class
// Main GUI class
public class Launchwindow implements ActionListener {
    private JFrame frame;
    private JTextField nameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleDropdown;
    private JButton loginButton;
    String savedname;

    public Launchwindow() {
        frame = new JFrame("Timetable Management System");
        frame.setSize(400, 300);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(new Color(30, 30, 30));

        JLabel title = new JLabel("Timetable Management System", SwingConstants.CENTER);
        title.setBounds(50, 10, 300, 30);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setForeground(Color.ORANGE);
        
        JLabel nameLabel = new JLabel("Username:");
        nameLabel.setBounds(50, 60, 100, 25);
        nameLabel.setForeground(Color.WHITE);

        nameField = new JTextField();
        nameField.setBounds(150, 60, 180, 25);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(50, 100, 100, 25);
        passLabel.setForeground(Color.WHITE);

        passwordField = new JPasswordField();
        passwordField.setBounds(150, 100, 180, 25);

        JLabel roleLabel = new JLabel("Select Role:");
        roleLabel.setBounds(50, 140, 100, 25);
        roleLabel.setForeground(Color.WHITE);

        String[] roles = { "Admin", "Teacher", "Student" };
        roleDropdown = new JComboBox<>(roles);
        roleDropdown.setBounds(150, 140, 180, 25);

        loginButton = new JButton("Login");
        loginButton.setBounds(140, 190, 120, 35);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(new Color(0, 102, 204));
        loginButton.setFocusable(false);
        loginButton.addActionListener(this);

        frame.add(title);
        frame.add(nameLabel);
        frame.add(nameField);
        frame.add(passLabel);
        frame.add(passwordField);
        frame.add(roleLabel);
        frame.add(roleDropdown);
        frame.add(loginButton);

        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String name = nameField.getText();
            String password = new String(passwordField.getPassword());
            String role = (String) roleDropdown.getSelectedItem();
    
            validatedata validator = new validatedata();
            if (validator.validateUser(name, password, role)) {
                showCustomPopup("Login Successful!", "Welcome, " + name + "!\nRole: " + role, JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();  // Close the login window
    
                // Store the logged-in user's name and role in session
                Savenamedata userSession = new Savenamedata();
                userSession.setUser(name, role);
                savedname = name;

                // Open respective window based on role
                switch (role) {
                    case "Admin":
                        new AdminWindow();
                        break;
                    case "Teacher":
                        new TeacherWindow();
                        break;
                    case "Student":
                        new StudentWindow(savedname);
                        break;
                }
            } else {
                showCustomPopup("Login Failed", "Invalid credentials, please try again!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showCustomPopup(String title, String message, int messageType) {
        UIManager.put("OptionPane.messageFont", new Font("Arial", Font.BOLD, 14));
        UIManager.put("OptionPane.messageForeground", Color.WHITE);
        UIManager.put("Panel.background", new Color(45, 45, 45));
        UIManager.put("OptionPane.background", new Color(45, 45, 45));
        JOptionPane.showMessageDialog(frame, message, title, messageType);
    }
}
