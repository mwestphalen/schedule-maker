import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Scanner;
import java.util.TreeSet;

public class GUI {
	CardLayout cardLayout;
	JPanel constPanel;
	JButton addStudentButton, remStudentButton, addCourseButton, remCourseButton;
	ArrayList<JComboBox> rccComboBoxes;
	ArrayList<JComboBox> electivesComboBoxes;
	GroupLayout.ParallelGroup picksParallel;
	GroupLayout.SequentialGroup picksSequential;
	Database database;

	public GUI() {

		// Create and set up
		JFrame frame = new JFrame("Freshmen Schedule Automation");
		frame.setMinimumSize(new Dimension(770, 560));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		/*
		 *  Where the components controlled by the cardLayout are initialized
		 */
		JPanel mainMenuPanel = new JPanel()  {
			// Using anonymous inner class to override method the that
			// displays the window to the screen
			@Override 
			public void setVisible(boolean visible) {
				// Call super class method
				super.setVisible(visible);
				
				// Generate courses and students' list every time we reach the main menu, aka, update and refresh
				if (visible) {
					database = new Database();
					System.out.println("Database initiated");
				}
			}
		};
		JPanel addStudentPanel = new JPanel();
		JPanel remStudentPanel = new JPanel();
		JPanel addCoursePanel = new JPanel();
		JPanel remCoursePanel = new JPanel();
		
		GridBagLayout gbl_mainMenuPanel = new GridBagLayout();
		gbl_mainMenuPanel.columnWidths = new int[] { 145, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_mainMenuPanel.rowHeights = new int[] { 116, 0, 0, 0, 0, 0, 0, 0 };
		gbl_mainMenuPanel.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				0.0, Double.MIN_VALUE };
		gbl_mainMenuPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		mainMenuPanel.setLayout(gbl_mainMenuPanel);
		mainMenuPanel.setVisible(true);

		// Create the panel that contains the "cards"
		cardLayout = new CardLayout();
		constPanel = new JPanel(cardLayout);
		constPanel.add(mainMenuPanel, "menu");

		JLabel rollinsLogo = new JLabel();
		rollinsLogo
				.setIcon(new ImageIcon("/Users/matheuswestphalen/Projects/ScheduleMaker/resources/rollins_logo.png"));

		GridBagConstraints gbc_rollinsLogo = new GridBagConstraints();
		gbc_rollinsLogo.fill = GridBagConstraints.BOTH;
		gbc_rollinsLogo.insets = new Insets(0, 0, 5, 5);
		gbc_rollinsLogo.gridx = 6;
		gbc_rollinsLogo.gridy = 0;
		mainMenuPanel.add(rollinsLogo, gbc_rollinsLogo);

		// Where the GUI is assembled
		addStudentButton = new JButton("Add Student");
		remStudentButton = new JButton("Remove Student");
		addCourseButton = new JButton("Add Course");
		remCourseButton = new JButton("Remove Course");

		Box box = Box.createVerticalBox();
		box.add(addStudentButton);
		box.add(remStudentButton);
		box.add(addCourseButton);
		box.add(remCourseButton);

		GridBagConstraints gbc_box = new GridBagConstraints();
		gbc_box.insets = new Insets(0, 0, 5, 5);
		gbc_box.gridx = 6;
		gbc_box.gridy = 3;
		mainMenuPanel.add(box, gbc_box);

		/*
		 * Action Listeners
		 */
		addStudentButton.addActionListener(event -> {
			cardLayout.show(constPanel, "addStudent");
			addStudent(addStudentPanel);
		});

		remStudentButton.addActionListener(event -> {
			cardLayout.show(constPanel, "remStudent");
			remStudent(remStudentPanel);
		});

		addCourseButton.addActionListener(event -> {
			cardLayout.show(constPanel, "addCourse");
			addCourse(addCoursePanel);
		});

		remCourseButton.addActionListener(event -> {
			cardLayout.show(constPanel, "remCourse");
			remCourse(remCoursePanel);
		});
		
		// Add other screens/panels to the layout manager (cardLayout)
		constPanel.add(addStudentPanel, "addStudent");
		constPanel.add(remStudentPanel, "remStudent");
		constPanel.add(addCoursePanel, "addCourse");
		constPanel.add(remCoursePanel, "remCourse");

		// First page to show when application opens
		cardLayout.show(constPanel, "menu");

		// Display the window
		frame.getContentPane().add(constPanel);
		frame.setSize(1000, 800);
		frame.setLocationRelativeTo(null);
		frame.pack();
		frame.setVisible(true);
	}


	////////////////////
	// Add Student //
	////////////////////

	private void addStudent(JPanel addStudentPanel) {
		// Set up Panel
		GridBagLayout layout = new GridBagLayout();
		addStudentPanel.setLayout(layout);

		// Set layout dimensions
		layout.columnWidths = new int[] { 34, 136, 130, 134, 45, 183, 0, 0, 0, 0 };
		layout.rowHeights = new int[] { 25, 0, 16, 26, 16, 26, 16, 27, 16, 27, 16, 27, 16, 29, 16, 31, 29, 0 };
		layout.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		layout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				0.0, 0.0, Double.MIN_VALUE };

		/*
		 * Common Components
		 */
		JLabel title = new JLabel("Add Student Form");
		JLabel name = new JLabel("Student Name:");
		JTextField firstNameField = new JTextField(10);
		JTextField lastNameField = new JTextField(10);
		JLabel rNumber = new JLabel("Student's R-Number:");
		JTextField rNumberField = new JTextField(10);
		JLabel major1 = new JLabel("1st Major (if any):");
		JLabel major2 = new JLabel("2nd Major (if any):");
		JButton addMajorOption = new JButton("+");
		JLabel langPreference = new JLabel("Language preference: ");
		JLabel status = new JLabel("Pick student's status (if any):");
		JLabel rccLabel = new JLabel("Pick student's RCCs:");
		JButton addRCCButton = new JButton("+");
		JLabel electivesLabel = new JLabel("Pick student's Electives:");
		JButton addElectiveButton = new JButton("+");
		JButton backButton = new JButton("Back");
		JButton submitButton = new JButton("Submit");

		/*
		 * Get introductory course list
		 */

		// Add majors based on currently available introductory classes in
		// introductory.txt
		int majorsSize = database.getIntroductoryCourses().size();
		TreeSet<String> uniqueMajors = new TreeSet<String>(); // Avoid duplicates + allows for ascending order
		uniqueMajors.add("N/A");
		for (int i = 0; i < majorsSize; i++) {
			if (!database.getIntroductoryCourses().get(i).getCourse().getCourseMajor().equals("N")) {
				uniqueMajors.add(database.getIntroductoryCourses().get(i).getCourse().getCourseMajor());
			} else {
				uniqueMajors.add("N/A");
			}
		}

		// Create string array to hold all majors (needed for JComboBox constructor)
		String majors[] = new String[uniqueMajors.size()];
		uniqueMajors.toArray(majors);

		JComboBox<String> major1Pick = new JComboBox<String>(majors);
		major1Pick.setSelectedItem("N/A");

		// TODO * Eliminate first choice from second JComboBox
		JComboBox<String> major2Pick = new JComboBox<String>(majors);
		major2Pick.setSelectedItem("N/A");

		/*
		 * Get language course list
		 */

		// Add language courses based on currently available FCMP courses in
		// competency.txt
		int langSize = database.getCompetencyCourses().size();
		TreeSet<String> uniqueLang = new TreeSet<String>(); // Avoid duplicates + allows for ascending order
		uniqueLang.add("N/A");
		for (int i = 0; i < langSize; i++) {
			if (database.getCompetencyCourses().get(i).getCourse().getProficiency().equals("FCMP")) {
				uniqueLang.add(database.getCompetencyCourses().get(i).getCourse().getCourseCode().split(" ")[0]);
			} else if (database.getCompetencyCourses().get(i).getCourse().getProficiency().equals("N")) {
				uniqueLang.add("N/A");
			}
		}

		// Create string array to hold all languages (needed for JComboBox constructor)
		String[] languages = new String[uniqueLang.size()];
		uniqueLang.toArray(languages);

		JComboBox<String> langPreferencePick = new JComboBox<String>(languages);
		langPreferencePick.setSelectedItem("N/A");

		/*
		 * List possible student's status
		 */

		String[] statusOptions = { "N/A", "E", "H", "ARP" };
		JComboBox<String> statusPick = new JComboBox<String>(statusOptions);
		statusPick.setSelectedItem("N/A");

		/*
		 * Get RCC course list
		 */

		int rccSize = database.getRCCCourses().size();
		TreeSet<String> uniqueRCCs = new TreeSet<String>(); // Avoid duplicates + allows for ascending order
		uniqueRCCs.add("N/A");
		for (int i = 0; i < rccSize; i++) {
			uniqueRCCs.add(database.getRCCCourses().get(i).getCourse().getCourseTitle());
		}

		// Create string array to hold all courses (needed for JComboBox constructor)
		String[] rccList = new String[uniqueRCCs.size()];

		// Convert TreeSet to String[] (as JComboBox only accepts arrays in constructor)
		uniqueRCCs.toArray(rccList);

		// Add first JComboBox with all RCC courses
		JComboBox<String> rccPick = new JComboBox<String>(rccList);
		rccPick.setSelectedItem("N/A");

		// Initialize list to hold all possible RCC JComboBoxes that may be added
		rccComboBoxes = new ArrayList<JComboBox>();

		// Add first JComboBox with the first set of courses
		rccComboBoxes.add(rccPick);

		/*
		 * Get electives course list
		 */

		// Add electives based on currently available elective courses on electives.txt
		int electivesSize = database.getElectiveCourses().size();
		TreeSet<String> uniqueElectives = new TreeSet<String>(); // Avoid duplicates + allows for ascending order
		uniqueElectives.add("N/A");
		for (int i = 0; i < electivesSize; i++) {
			uniqueElectives.add(database.getElectiveCourses().get(i).getCourse().getCourseTitle());
		}

		// Create string array to hold all courses (needed for JComboBox constructor)
		String[] electivesList = new String[uniqueElectives.size()];

		// Convert TreeSet to String[] (as JComboBox only accepts arrays in constructor)
		uniqueElectives.toArray(electivesList);

		// Add first JComboBox with all RCC courses
		JComboBox<String> electivesPick = new JComboBox<String>(electivesList);
		electivesPick.setSelectedItem("N/A");

		// Initialize list to hold all possible RCC JComboBoxes that may be added
		electivesComboBoxes = new ArrayList<JComboBox>();

		// Add first JComboBox with the first set of courses
		electivesComboBoxes.add(electivesPick);

		/*
		 * Add components to panel
		 */
		// TODO Put this all in a method for better readability
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setFont(new Font("Lucida Grande", Font.BOLD, 20));
		GridBagConstraints gbc_title = new GridBagConstraints();
		gbc_title.anchor = GridBagConstraints.NORTHWEST;
		gbc_title.insets = new Insets(0, 0, 5, 5);
		gbc_title.gridwidth = 2;
		gbc_title.gridx = 3;
		gbc_title.gridy = 1;
		addStudentPanel.add(title, gbc_title);
		
		name.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		GridBagConstraints gbc_name = new GridBagConstraints();
		gbc_name.anchor = GridBagConstraints.NORTHWEST;
		gbc_name.insets = new Insets(0, 0, 5, 5);
		gbc_name.gridx = 1;
		gbc_name.gridy = 2;
		addStudentPanel.add(name, gbc_name);
		firstNameField.setColumns(10);

		GridBagConstraints gbc_firstNameField = new GridBagConstraints();
		gbc_firstNameField.fill = GridBagConstraints.HORIZONTAL;
		gbc_firstNameField.anchor = GridBagConstraints.NORTH;
		gbc_firstNameField.insets = new Insets(0, 0, 5, 5);
		gbc_firstNameField.gridx = 1;
		gbc_firstNameField.gridy = 3;
		addStudentPanel.add(firstNameField, gbc_firstNameField);
		lastNameField.setColumns(10);

		GridBagConstraints gbc_lastNameField = new GridBagConstraints();
		gbc_lastNameField.fill = GridBagConstraints.HORIZONTAL;
		gbc_lastNameField.anchor = GridBagConstraints.NORTH;
		gbc_lastNameField.insets = new Insets(0, 0, 5, 5);
		gbc_lastNameField.gridx = 2;
		gbc_lastNameField.gridy = 3;
		addStudentPanel.add(lastNameField, gbc_lastNameField);
		rNumber.setFont(new Font("Lucida Grande", Font.BOLD, 13));

		GridBagConstraints gbc_rNumber = new GridBagConstraints();
		gbc_rNumber.anchor = GridBagConstraints.NORTHWEST;
		gbc_rNumber.insets = new Insets(0, 0, 5, 5);
		gbc_rNumber.gridx = 1;
		gbc_rNumber.gridy = 4;
		addStudentPanel.add(rNumber, gbc_rNumber);
		rNumberField.setColumns(10);

		GridBagConstraints gbc_rNumberField = new GridBagConstraints();
		gbc_rNumberField.fill = GridBagConstraints.HORIZONTAL;
		gbc_rNumberField.anchor = GridBagConstraints.NORTH;
		gbc_rNumberField.insets = new Insets(0, 0, 5, 5);
		gbc_rNumberField.gridx = 1;
		gbc_rNumberField.gridy = 5;
		addStudentPanel.add(rNumberField, gbc_rNumberField);
		major1.setFont(new Font("Lucida Grande", Font.BOLD, 13));

		GridBagConstraints gbc_major1 = new GridBagConstraints();
		gbc_major1.anchor = GridBagConstraints.NORTHWEST;
		gbc_major1.insets = new Insets(0, 0, 5, 5);
		gbc_major1.gridx = 1;
		gbc_major1.gridy = 6;
		addStudentPanel.add(major1, gbc_major1);

		major2.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		GridBagConstraints gbc_major2 = new GridBagConstraints();
		gbc_major2.anchor = GridBagConstraints.NORTHWEST;
		gbc_major2.insets = new Insets(0, 0, 5, 5);
		gbc_major2.gridx = 2;
		gbc_major2.gridy = 6;
		addStudentPanel.add(major2, gbc_major2);
		GridBagConstraints gbc_major1Pick = new GridBagConstraints();
		gbc_major1Pick.anchor = GridBagConstraints.NORTH;
		gbc_major1Pick.fill = GridBagConstraints.HORIZONTAL;
		gbc_major1Pick.insets = new Insets(0, 0, 5, 5);
		gbc_major1Pick.gridx = 1;
		gbc_major1Pick.gridy = 7;
		addStudentPanel.add(major1Pick, gbc_major1Pick);

		GridBagConstraints gbc_major2Pick = new GridBagConstraints();
		gbc_major2Pick.anchor = GridBagConstraints.NORTH;
		gbc_major2Pick.fill = GridBagConstraints.HORIZONTAL;
		gbc_major2Pick.insets = new Insets(0, 0, 5, 5);
		gbc_major2Pick.gridx = 2;
		gbc_major2Pick.gridy = 7;
		addStudentPanel.add(major2Pick, gbc_major2Pick);

		langPreference.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		GridBagConstraints gbc_langPreference = new GridBagConstraints();
		gbc_langPreference.anchor = GridBagConstraints.NORTHWEST;
		gbc_langPreference.insets = new Insets(0, 0, 5, 5);
		gbc_langPreference.gridwidth = 2;
		gbc_langPreference.gridx = 1;
		gbc_langPreference.gridy = 8;
		addStudentPanel.add(langPreference, gbc_langPreference);

		GridBagConstraints gbc_langPreferencePick = new GridBagConstraints();
		gbc_langPreferencePick.anchor = GridBagConstraints.NORTH;
		gbc_langPreferencePick.fill = GridBagConstraints.HORIZONTAL;
		gbc_langPreferencePick.insets = new Insets(0, 0, 5, 5);
		gbc_langPreferencePick.gridx = 1;
		gbc_langPreferencePick.gridy = 9;
		addStudentPanel.add(langPreferencePick, gbc_langPreferencePick);

		status.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		GridBagConstraints gbc_status = new GridBagConstraints();
		gbc_status.anchor = GridBagConstraints.NORTHWEST;
		gbc_status.insets = new Insets(0, 0, 5, 5);
		gbc_status.gridx = 1;
		gbc_status.gridy = 10;
		addStudentPanel.add(status, gbc_status);

		GridBagConstraints gbc_statusPick = new GridBagConstraints();
		gbc_statusPick.anchor = GridBagConstraints.NORTH;
		gbc_statusPick.fill = GridBagConstraints.HORIZONTAL;
		gbc_statusPick.insets = new Insets(0, 0, 5, 5);
		gbc_statusPick.gridx = 1;
		gbc_statusPick.gridy = 11;
		addStudentPanel.add(statusPick, gbc_statusPick);

		rccLabel.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		GridBagConstraints gbc_rccLabel = new GridBagConstraints();
		gbc_rccLabel.anchor = GridBagConstraints.NORTHWEST;
		gbc_rccLabel.insets = new Insets(0, 0, 5, 5);
		gbc_rccLabel.gridx = 1;
		gbc_rccLabel.gridy = 12;
		addStudentPanel.add(rccLabel, gbc_rccLabel);

		// Add first RCC ComboBox to the screen
		GridBagConstraints gbc_rccComboBoxes = new GridBagConstraints();
		gbc_rccComboBoxes.fill = GridBagConstraints.HORIZONTAL;
		gbc_rccComboBoxes.insets = new Insets(0, 0, 5, 5);
		gbc_rccComboBoxes.gridwidth = 3;
		gbc_rccComboBoxes.gridx = 1;
		gbc_rccComboBoxes.gridy = 13;
		addStudentPanel.add(rccComboBoxes.get(0), gbc_rccComboBoxes);

		GridBagConstraints gbc_addRCCButton = new GridBagConstraints();
		gbc_addRCCButton.insets = new Insets(0, 0, 5, 5);
		gbc_addRCCButton.gridx = 4;
		gbc_addRCCButton.gridy = 13;
		addStudentPanel.add(addRCCButton, gbc_addRCCButton);

		electivesLabel.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		GridBagConstraints gbc_electivesLabel = new GridBagConstraints();
		gbc_electivesLabel.anchor = GridBagConstraints.WEST;
		gbc_electivesLabel.insets = new Insets(0, 0, 5, 5);
		gbc_electivesLabel.gridx = 1;
		gbc_electivesLabel.gridy = 14;
		addStudentPanel.add(electivesLabel, gbc_electivesLabel);

		// Add first Elective ComboBox to the screen
		GridBagConstraints gbc_electivesComboBoxes = new GridBagConstraints();
		gbc_electivesComboBoxes.fill = GridBagConstraints.HORIZONTAL;
		gbc_electivesComboBoxes.insets = new Insets(0, 0, 5, 5);
		gbc_electivesComboBoxes.gridwidth = 3;
		gbc_electivesComboBoxes.gridx = 1;
		gbc_electivesComboBoxes.gridy = 15;
		addStudentPanel.add(electivesPick, gbc_electivesComboBoxes);

		GridBagConstraints gbc_addElectiveButton = new GridBagConstraints();
		gbc_addElectiveButton.insets = new Insets(0, 0, 5, 5);
		gbc_addElectiveButton.gridx = 4;
		gbc_addElectiveButton.gridy = 15;
		addStudentPanel.add(addElectiveButton, gbc_addElectiveButton);

		GridBagConstraints gbc_backButton = new GridBagConstraints();
		gbc_backButton.insets = new Insets(0, 0, 0, 5);
		gbc_backButton.gridx = 7;
		gbc_backButton.gridy = 16;
		addStudentPanel.add(backButton, gbc_backButton);

		GridBagConstraints gbc_submitButton = new GridBagConstraints();
		gbc_submitButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_submitButton.insets = new Insets(0, 0, 0, 5);
		gbc_submitButton.gridx = 8;
		gbc_submitButton.gridy = 16;
		addStudentPanel.add(submitButton, gbc_submitButton);
		
		/*
		 * Action Listeners
		 */

		// Add more RCC Course ComboBoxes
		addRCCButton.addActionListener(event -> {
			// Update RCC options
			int selectedItem = rccComboBoxes.size() - 1;
			// .remove(rccComboBoxes.get(selectedItem).getSelectedItem().toString());
			// sortedRCCList.toArray(rccList);
			uniqueRCCs.remove(rccComboBoxes.get(selectedItem).getSelectedItem().toString());
			uniqueRCCs.toArray(rccList);

			// Create new JComboBox to be added
			JComboBox<String> newComboBox = new JComboBox<String>(rccList);

			// Add new RCC ComboBox to list of RCC Comboboxes
			rccComboBoxes.add(newComboBox);

			// Set grid bag constraints of new RCC ComboBox to the appropriate positions
			gbc_rccComboBoxes.gridy++;
			gbc_addRCCButton.gridy++;

			// Move Elective portion down accordingly
			gbc_electivesLabel.gridy++;
			gbc_addElectiveButton.gridy++;
			gbc_electivesComboBoxes.gridy++;

			// Move back/submit button down
			gbc_backButton.gridy++;
			gbc_submitButton.gridy++;

			// Add new combobox and update addButton
			addStudentPanel.add(newComboBox, gbc_rccComboBoxes);
			addStudentPanel.add(addRCCButton, gbc_addRCCButton);
			addStudentPanel.add(backButton, gbc_backButton);
			addStudentPanel.add(submitButton, gbc_submitButton);
			addStudentPanel.add(electivesLabel, gbc_electivesLabel);
			addStudentPanel.add(electivesComboBoxes.get(0), gbc_electivesComboBoxes);
			addStudentPanel.add(addElectiveButton, gbc_addElectiveButton);

			addStudentPanel.revalidate();
			addStudentPanel.repaint();

			// Limit the # of rcc courses to 8
			if (rccComboBoxes.size() == 8) {
				addStudentPanel.remove(addRCCButton);
			}
		});

		// Add another elective button listener
		addElectiveButton.addActionListener(event -> {
			// Update Electives options
			int selectedItem = electivesComboBoxes.size() - 1;
			uniqueElectives.remove(electivesComboBoxes.get(selectedItem).getSelectedItem().toString());
			uniqueElectives.toArray(electivesList);

			// Create new JComboBox to be added
			JComboBox<String> newComboBox = new JComboBox<String>(electivesList);

			// Add new RCC ComboBox to list of RCC Comboboxes
			electivesComboBoxes.add(newComboBox);

			// Set grid bag constraints of new electives ComboBox to the appropriate
			// positions
			gbc_electivesComboBoxes.gridy++;
			gbc_addElectiveButton.gridy++;

			// Add new combobox and update addButton
			addStudentPanel.add(newComboBox, gbc_electivesComboBoxes);
			addStudentPanel.add(addElectiveButton, gbc_addElectiveButton);

			// Move submit button down
			gbc_submitButton.gridy++;

			addStudentPanel.add(submitButton, gbc_submitButton);
			addStudentPanel.add(electivesLabel, gbc_electivesLabel);
			addStudentPanel.add(electivesComboBoxes.get(0), gbc_electivesComboBoxes);
			addStudentPanel.add(addElectiveButton, gbc_addElectiveButton);

			addStudentPanel.revalidate();
			addStudentPanel.repaint();

			// Limit the # of electives courses to 7
			if (electivesComboBoxes.size() == 7) {
				addStudentPanel.remove(addElectiveButton);
			}

		});

		// Submit button listener
		submitButton.addActionListener(event -> {

			// Check if R-Number is 9 digits
			if (rNumberField.getText().length() != 9) {
				JOptionPane.showMessageDialog(addStudentPanel, "R-Number must be 9 digits long (e.g. R12345678)",
						"Fix R-Number field", JOptionPane.ERROR_MESSAGE);
				return;
			}

			ArrayList<Student> studentsCopy = database.getStudentList();
			for (Student student : studentsCopy) {
				if (rNumberField.getText().equals(student.getRNumber())) {
					// Found a student with the same R-Number, issue warning
					JOptionPane.showMessageDialog(addStudentPanel,
							"Error: A student with the same R-Number is already in the system.", "Unable to remove student",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			
			// Add student to .txt file
			try {
				// Convert the selected preferences for RCC into arrays using Course Code
				// instead of Course Title
				String[] rccPref = { "N", "N", "N", "N", "N", "N", "N", "N" };
				for (int i = 0; i < rccComboBoxes.size(); i++) {
					for (ScheduledCourse rccCourse : database.getRCCCourses()) {
						if (rccCourse.getCourse().getCourseTitle()
								.equals(rccComboBoxes.get(i).getSelectedItem().toString())) {
							rccPref[i] = rccCourse.getCourse().getCourseCode();
						}
					}
				}

				String[] electivePref = { "N", "N", "N", "N", "N", "N", "N", "N" };
				for (int i = 0; i < electivesComboBoxes.size(); i++) {
					for (ScheduledCourse electiveCourse : database.getCompetencyCourses()) {
						if (electiveCourse.getCourse().getCourseTitle()
								.equals(electivesComboBoxes.get(i).getSelectedItem().toString())) {
							electivePref[i] = electiveCourse.getCourse().getCourseCode();
						}
					}
				}

				// Initialize Student Object
				String studentData1 = rNumberField.getText() + ", " + firstNameField.getText() + ", "
						+ lastNameField.getText() + ", " + (major1Pick.getSelectedItem().toString().equals("N/A") ? "N"
								: major1Pick.getSelectedItem().toString())
						+ // Ternary Operator: (condition) ? (value if true) : (value if false)
						", " + (major2Pick.getSelectedItem().toString().equals("N/A") ? "N"
								: major2Pick.getSelectedItem().toString())
						+ // Ternary Operator: (condition) ? (value if true) : (value if false)
						", " + (langPreferencePick.getSelectedItem().toString().equals("N/A") ? "N"
								: langPreferencePick.getSelectedItem().toString())
						+ // Ternary Operator: (condition) ? (value if true) : (value if false)
						", " + (statusPick.getSelectedItem().toString().equals("N/A") ? "N"
								: statusPick.getSelectedItem().toString()); // Ternary Operator: (condition) ? (value if true) : (value if false)
				
				String studentData2 = electivePref[0] + ", " + electivePref[1] + ", " + electivePref[2] + ", "
						+ electivePref[3] + ", " + electivePref[4] + ", " + electivePref[5] + ", " + electivePref[6];
				String studentData3 = rccPref[0] + ", " + rccPref[1] + ", " + rccPref[2] + ", " + rccPref[3] + ", "
						+ rccPref[4] + ", " + rccPref[5] + ", " + rccPref[6] + ", " + rccPref[7];

				File file = new File("./students.txt");
				if (!file.exists()) {
					file.createNewFile();
				}

				FileWriter fileWriter = new FileWriter(file.getName(), true);
				BufferedWriter bw = new BufferedWriter(fileWriter);
				bw.write(studentData1);
				bw.newLine();
				bw.write(studentData2);
				bw.newLine();
				bw.write(studentData3);
				bw.newLine();
				bw.close();

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Error");
			}

			// Clean panel and go back to main menu
			cardLayout.show(constPanel, "menu");
			addStudentPanel.removeAll();
			return;
		});

		backButton.addActionListener(event -> {
			cardLayout.show(constPanel, "menu");
			addStudentPanel.removeAll();
			return;
		});
	}

	////////////////////
	// Remove Student //
	////////////////////

	private void remStudent(JPanel remStudentPanel) {
		// Set up Panel
		GridBagLayout layout = new GridBagLayout();
		remStudentPanel.setLayout(layout);

		// Set layout dimensions
		layout.columnWidths = new int[] { 34, 136, 130, 0, 0, 134, 45, 183, 0, 0 };
		layout.rowHeights = new int[] { 25, 0, 16, 26, 16, 26, 16, 27, 16, 27, 16, 27, 16, 29, 16, 31, 29, 0 };
		layout.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		layout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				0.0, 0.0, Double.MIN_VALUE };

		/*
		 * Common Components
		 */
		JLabel title = new JLabel("Remove Student Form");
		JLabel rNumber = new JLabel("R-Number");
		JTextField rNumberField = new JTextField();
		JButton removeButton = new JButton("Remove");
		JButton backButton = new JButton("Back");

		/*
		 * Add Components to panel
		 */
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setFont(new Font("Lucida Grande", Font.BOLD, 20));
		GridBagConstraints gbc_title = new GridBagConstraints();
		gbc_title.anchor = GridBagConstraints.WEST;
		gbc_title.insets = new Insets(0, 0, 5, 5);
		gbc_title.gridwidth = 3;
		gbc_title.gridx = 3;
		gbc_title.gridy = 1;
		remStudentPanel.add(title, gbc_title);
		rNumber.setFont(new Font("Lucida Grande", Font.BOLD, 13));

		GridBagConstraints gbc_rNumber = new GridBagConstraints();
		gbc_rNumber.anchor = GridBagConstraints.NORTH;
		gbc_rNumber.insets = new Insets(0, 0, 5, 5);
		gbc_rNumber.gridx = 4;
		gbc_rNumber.gridy = 4;
		remStudentPanel.add(rNumber, gbc_rNumber);

		rNumberField.setColumns(10);
		GridBagConstraints gbc_rNumberField = new GridBagConstraints();
		gbc_rNumberField.fill = GridBagConstraints.HORIZONTAL;
		gbc_rNumberField.anchor = GridBagConstraints.NORTH;
		gbc_rNumberField.insets = new Insets(0, 0, 5, 5);
		gbc_rNumberField.gridx = 4;
		gbc_rNumberField.gridy = 5;
		remStudentPanel.add(rNumberField, gbc_rNumberField);

		GridBagConstraints gbc_removeButton = new GridBagConstraints();
		gbc_removeButton.insets = new Insets(0, 0, 5, 5);
		gbc_removeButton.gridx = 4;
		gbc_removeButton.gridy = 6;
		remStudentPanel.add(removeButton, gbc_removeButton);

		GridBagConstraints gbc_backButton = new GridBagConstraints();
		gbc_backButton.insets = new Insets(0, 0, 0, 5);
		gbc_backButton.gridx = 7;
		gbc_backButton.gridy = 16;
		remStudentPanel.add(backButton, gbc_backButton);

		/*
		 * Action Listeners
		 */

		backButton.addActionListener(event -> {
			cardLayout.show(constPanel, "menu");
			remStudentPanel.removeAll();
			return;
		});

		removeButton.addActionListener(event -> {

			if (rNumberField.getText().length() != 9) {
				JOptionPane.showMessageDialog(remStudentPanel, "R-Number must be 9 digits long (e.g. R12345678)",
						"Fix R-Number field", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			// Make a copy of the list
			ArrayList<Student> studentsCopy = new ArrayList<Student>(database.getStudentList());
			for (Student student : studentsCopy) {
				if (student.getRNumber().equals(rNumberField.getText())) {

					// Try to open the file
					File inputFile = new File("./students.txt");
					if (!inputFile.exists()) {
						JOptionPane.showMessageDialog(remStudentPanel,
								"Error: There are no records of any student in the system.", "Unable to remove student",
								JOptionPane.ERROR_MESSAGE);
						return;
					}

					// Try to remove student from students.txt
					try {
						File outputFile = new File("./remStudent_output.txt");
						Scanner scanner = new Scanner(inputFile);
						BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
						while (scanner.hasNextLine()) {
							// Read student data that is split into 3 lines
							String line1 = scanner.nextLine();
							String line2 = scanner.nextLine();
							String line3 = scanner.nextLine();

							// Check if R-Number of student to be removed matches the first string
							// in the first line, which happens to be the R-Number as well. If so,
							// we are not writing it back to the file
							if (!line1.startsWith(student.getRNumber())) {
								writer.write(line1);
								writer.newLine();
								writer.write(line2);
								writer.newLine();
								writer.write(line3);
								writer.newLine();
							}
						}

						// Close input and output stream
						scanner.close();
						writer.close();

						// Replace input file with the output file
						inputFile.delete();
						outputFile.renameTo(inputFile);

					} catch (IOException e) {
						e.printStackTrace();
					}
					
					// If reached here, student has been successfully removed from everywhere
					JOptionPane.showMessageDialog(remStudentPanel, "Student successfully removed.", "Student Removal",
							JOptionPane.OK_OPTION);
					
					// Go back to main menu
					cardLayout.show(constPanel, "menu");
					remStudentPanel.removeAll();
					
					return;
				}
			}

			// Didn't find student
			JOptionPane.showMessageDialog(remStudentPanel, "Unable to locate student. Please enter R Number again",
					"Unable to locate student", JOptionPane.ERROR_MESSAGE);
			return;
		});
	}

	////////////////////
	//// Add Course ////
	////////////////////
	////////////////////
	private void addCourse(JPanel addCoursePanel) {
		// Set up Panel
		GridBagLayout layout = new GridBagLayout();
		addCoursePanel.setLayout(layout);

		// Set layout dimensions
		layout.columnWidths = new int[] { 34, 0, 0, 141, 130, 134, 45, 183, 0, 0, 0, 0 };
		layout.rowHeights = new int[] { 25, 0, 16, 26, 0, 16, 26, 16, 27, 16, 27, 16, 27, 16, 29, 16, 31, 29, 0 };
		layout.columnWeights = new double[] { 0.0, 0.0, 1.0, 0.0, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		layout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				0.0, 0.0, Double.MIN_VALUE };

		/*
		 * Common Components
		 */
		 JLabel title = new JLabel("Add Course Form");
		 JLabel courseCRNLabel = new JLabel("Course CRN");
		 JTextField courseCRNBox = new JTextField();
		 JLabel courseTitleLabel = new JLabel("Course Title");
		 JLabel creditsLabel = new JLabel("Credit Hours");
		 JTextField courseTitleBox = new JTextField();
		 JLabel coursePrefixLabel = new JLabel("Course Prefix");
		 JLabel courseNumberLabel = new JLabel("Course Number");
		 JTextField coursePrefixBox = new JTextField();
		 JTextField courseNumField = new JTextField();
		 JLabel competencyLabel = new JLabel("Competency");
		 JLabel capacityLabel = new JLabel("Capacity");
		 JButton submitButton = new JButton("Submit");
		 Box horizontalBox = Box.createHorizontalBox();
		 Box sundayGroup = Box.createVerticalBox();
		 JLabel sundayLabel = new JLabel("S");
		 JRadioButton sundayButton = new JRadioButton("");
		 Box mondayGroup = Box.createVerticalBox();
		 JLabel mondayLabel = new JLabel("M");
		 JRadioButton mondayButton = new JRadioButton("");
		 Box tuesdayGroup = Box.createVerticalBox();
		 JLabel tuesdayLabel = new JLabel("T");
		 JRadioButton tuesdayButton = new JRadioButton("");
		 Box wednesdayGroup = Box.createVerticalBox();
		 JLabel wednesdayLabel = new JLabel("W");
		 JRadioButton wednesdayButton = new JRadioButton("");
		 Box thursdayGroup = Box.createVerticalBox();
		 JLabel thursdayLabel = new JLabel("T");
		 JRadioButton thursdayButton = new JRadioButton("");
		 Box fridayGroup = Box.createVerticalBox();
		 JLabel fridayLabel = new JLabel("F");
		 JRadioButton fridayButton = new JRadioButton("");
		 Box saturdayGroup = Box.createVerticalBox();
		 JLabel saturdayLabel = new JLabel("S");
		 JRadioButton saturdayButton = new JRadioButton("");
		 JLabel daysLabel = new JLabel("Meeting Days");
		 JLabel meetingTimeLabel = new JLabel("Start Time");
		 JLabel endTimeLabel = new JLabel("End Time");
		 JButton addLabButton = new JButton("Add Lab");
		 JButton backButton = new JButton("Back");
		
		/*
		 * List possible credit hours 
		 */
		 String[] creditHours = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };
		 JComboBox<String> creditsBox = new JComboBox<String>(creditHours);
		 creditsBox.setSelectedItem("N/A");
		 
		 /*
		  * List possible proficiency/competency
		  */
		 String[] competencies = { "BCMP", "FCMP", "MCMP", "WCMP", "ECMP" };
		 JComboBox<String> competencyBox = new JComboBox<String>(competencies);
		 competencyBox.setSelectedItem("N/A");
		 
		 /*
		  * List class' capacity numbers
		  */
		 SpinnerNumberModel capacityModel = new SpinnerNumberModel(0, 0, 99, 1);
		 JSpinner capacitySpinner = new JSpinner(capacityModel);
		 
		 /*
		  * List possible start times
		  */
		 String[] startTimes = { };
		 JComboBox<String> startTimeBox = new JComboBox<String>(startTimes);
		 startTimeBox.setSelectedItem("N/A");
		 
		 /*
		  * List possible end times
		  */
		 String[] endTimes = { };
		 JComboBox<String> endTimeBox = new JComboBox<String>(endTimes);
		 startTimeBox.setSelectedItem("N/A");
		 
		 
		/*
		 * Add Components to panel
		 */
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setFont(new Font("Lucida Grande", Font.BOLD, 20));

		GridBagConstraints gbc_title = new GridBagConstraints();
		gbc_title.anchor = GridBagConstraints.NORTHWEST;
		gbc_title.insets = new Insets(0, 0, 5, 5);
		gbc_title.gridwidth = 2;
		gbc_title.gridx = 4;
		gbc_title.gridy = 1;
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setFont(new Font("Lucida Grande", Font.BOLD, 20));
		addCoursePanel.add(title, gbc_title);
		
		GridBagConstraints gbc_courseTitleLabel = new GridBagConstraints();
		gbc_courseTitleLabel.anchor = GridBagConstraints.WEST;
		gbc_courseTitleLabel.insets = new Insets(0, 0, 5, 5);
		gbc_courseTitleLabel.gridx = 2;
		gbc_courseTitleLabel.gridy = 5;
		courseTitleLabel.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		addCoursePanel.add(courseTitleLabel, gbc_courseTitleLabel);
		
		GridBagConstraints gbc_courseTitleBox = new GridBagConstraints();
		gbc_courseTitleBox.insets = new Insets(0, 0, 5, 5);
		gbc_courseTitleBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_courseTitleBox.gridx = 2;
		gbc_courseTitleBox.gridy = 6;
		courseTitleBox.setColumns(10);
		addCoursePanel.add(courseTitleBox, gbc_courseTitleBox);
		
		GridBagConstraints gbc_courseCRNLabel = new GridBagConstraints();
		gbc_courseCRNLabel.anchor = GridBagConstraints.WEST;
		gbc_courseCRNLabel.insets = new Insets(0, 0, 5, 5);
		gbc_courseCRNLabel.gridx = 2;
		gbc_courseCRNLabel.gridy = 3;
		courseCRNLabel.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		addCoursePanel.add(courseCRNLabel, gbc_courseCRNLabel);
		
		GridBagConstraints gbc_creditsLabel = new GridBagConstraints();
		gbc_creditsLabel.anchor = GridBagConstraints.WEST;
		gbc_creditsLabel.insets = new Insets(0, 0, 5, 5);
		gbc_creditsLabel.gridx = 3;
		gbc_creditsLabel.gridy = 5;
		creditsLabel.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		addCoursePanel.add(creditsLabel, gbc_creditsLabel);
		
		GridBagConstraints gbc_courseCRNBox = new GridBagConstraints();
		gbc_courseCRNBox.insets = new Insets(0, 0, 5, 5);
		gbc_courseCRNBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_courseCRNBox.gridx = 2;
		gbc_courseCRNBox.gridy = 4;
		courseCRNBox.setColumns(10);
		addCoursePanel.add(courseCRNBox, gbc_courseCRNBox);
		
		GridBagConstraints gbc_creditsBox = new GridBagConstraints();
		gbc_creditsBox.insets = new Insets(0, 0, 5, 5);
		gbc_creditsBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_creditsBox.gridx = 3;
		gbc_creditsBox.gridy = 6;
		addCoursePanel.add(creditsBox, gbc_creditsBox);
		
		GridBagConstraints gbc_coursePrefixLabel = new GridBagConstraints();
		gbc_coursePrefixLabel.anchor = GridBagConstraints.WEST;
		gbc_coursePrefixLabel.insets = new Insets(0, 0, 5, 5);
		gbc_coursePrefixLabel.gridx = 2;
		gbc_coursePrefixLabel.gridy = 7;
		coursePrefixLabel.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		addCoursePanel.add(coursePrefixLabel, gbc_coursePrefixLabel);
		
		GridBagConstraints gbc_courseNumberLabel = new GridBagConstraints();
		gbc_courseNumberLabel.anchor = GridBagConstraints.WEST;
		gbc_courseNumberLabel.insets = new Insets(0, 0, 5, 5);
		gbc_courseNumberLabel.gridx = 3;
		gbc_courseNumberLabel.gridy = 7;
		courseNumberLabel.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		addCoursePanel.add(courseNumberLabel, gbc_courseNumberLabel);
		
		GridBagConstraints gbc_coursePrefixBox = new GridBagConstraints();
		gbc_coursePrefixBox.insets = new Insets(0, 0, 5, 5);
		gbc_coursePrefixBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_coursePrefixBox.gridx = 2;
		gbc_coursePrefixBox.gridy = 8;
		coursePrefixBox.setColumns(10);
		addCoursePanel.add(coursePrefixBox, gbc_coursePrefixBox);
		
		GridBagConstraints gbc_courseNumField = new GridBagConstraints();
		gbc_courseNumField.insets = new Insets(0, 0, 5, 5);
		gbc_courseNumField.fill = GridBagConstraints.HORIZONTAL;
		gbc_courseNumField.gridx = 3;
		gbc_courseNumField.gridy = 8;
		courseNumField.setColumns(10);
		addCoursePanel.add(courseNumField, gbc_courseNumField);
		
		GridBagConstraints gbc_competencyLabel = new GridBagConstraints();
		gbc_competencyLabel.anchor = GridBagConstraints.WEST;
		gbc_competencyLabel.insets = new Insets(0, 0, 5, 5);
		gbc_competencyLabel.gridx = 2;
		gbc_competencyLabel.gridy = 9;
		competencyLabel.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		addCoursePanel.add(competencyLabel, gbc_competencyLabel);
		
		GridBagConstraints gbc_capacityLabel = new GridBagConstraints();
		gbc_capacityLabel.anchor = GridBagConstraints.WEST;
		gbc_capacityLabel.insets = new Insets(0, 0, 5, 5);
		gbc_capacityLabel.gridx = 3;
		gbc_capacityLabel.gridy = 9;
		capacityLabel.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		addCoursePanel.add(capacityLabel, gbc_capacityLabel);
		
		GridBagConstraints gbc_competencyBox = new GridBagConstraints();
		gbc_competencyBox.insets = new Insets(0, 0, 5, 5);
		gbc_competencyBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_competencyBox.gridx = 2;
		gbc_competencyBox.gridy = 10;
		addCoursePanel.add(competencyBox, gbc_competencyBox);
		
		GridBagConstraints gbc_capacitySpinner = new GridBagConstraints();
		gbc_capacitySpinner.fill = GridBagConstraints.HORIZONTAL;
		gbc_capacitySpinner.insets = new Insets(0, 0, 5, 5);
		gbc_capacitySpinner.gridx = 3;
		gbc_capacitySpinner.gridy = 10;
		addCoursePanel.add(capacitySpinner, gbc_capacitySpinner);
		
		GridBagConstraints gbc_lblMeetingTime = new GridBagConstraints();
		gbc_lblMeetingTime.anchor = GridBagConstraints.WEST;
		gbc_lblMeetingTime.insets = new Insets(0, 0, 5, 5);
		gbc_lblMeetingTime.gridx = 2;
		gbc_lblMeetingTime.gridy = 11;
		meetingTimeLabel.setHorizontalAlignment(SwingConstants.LEFT);
		meetingTimeLabel.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		addCoursePanel.add(meetingTimeLabel, gbc_lblMeetingTime);
		
		GridBagConstraints gbc_lblEndTime = new GridBagConstraints();
		gbc_lblEndTime.anchor = GridBagConstraints.WEST;
		gbc_lblEndTime.insets = new Insets(0, 0, 5, 5);
		gbc_lblEndTime.gridx = 3;
		gbc_lblEndTime.gridy = 11;
		endTimeLabel.setHorizontalAlignment(SwingConstants.LEFT);
		endTimeLabel.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		addCoursePanel.add(endTimeLabel, gbc_lblEndTime);
		
		GridBagConstraints gbc_startTimeBox = new GridBagConstraints();
		gbc_startTimeBox.insets = new Insets(0, 0, 5, 5);
		gbc_startTimeBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_startTimeBox.gridx = 2;
		gbc_startTimeBox.gridy = 12;
		addCoursePanel.add(startTimeBox, gbc_startTimeBox);
		
		GridBagConstraints gbc_endTimeBox = new GridBagConstraints();
		gbc_endTimeBox.insets = new Insets(0, 0, 5, 5);
		gbc_endTimeBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_endTimeBox.gridx = 3;
		gbc_endTimeBox.gridy = 12;
		addCoursePanel.add(endTimeBox, gbc_endTimeBox);
		
		GridBagConstraints gbc_daysLabel = new GridBagConstraints();
		gbc_daysLabel.anchor = GridBagConstraints.WEST;
		gbc_daysLabel.insets = new Insets(0, 0, 5, 5);
		gbc_daysLabel.gridx = 2;
		gbc_daysLabel.gridy = 13;
		daysLabel.setHorizontalAlignment(SwingConstants.LEFT);
		daysLabel.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		addCoursePanel.add(daysLabel, gbc_daysLabel);
		
		GridBagConstraints gbc_horizontalBox = new GridBagConstraints();
		gbc_horizontalBox.gridwidth = 2;
		gbc_horizontalBox.anchor = GridBagConstraints.WEST;
		gbc_horizontalBox.insets = new Insets(0, 0, 5, 5);
		gbc_horizontalBox.gridx = 2;
		gbc_horizontalBox.gridy = 14;
		addCoursePanel.add(horizontalBox, gbc_horizontalBox);
		sundayGroup.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		horizontalBox.add(sundayGroup);
		sundayLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		sundayLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		sundayGroup.add(sundayLabel);
		sundayButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		sundayButton.setHorizontalAlignment(SwingConstants.CENTER);
		
		sundayGroup.add(sundayButton);
		
		horizontalBox.add(mondayGroup);
		mondayLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		mondayGroup.add(mondayLabel);
		mondayButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		mondayGroup.add(mondayButton);
		
		horizontalBox.add(tuesdayGroup);
		tuesdayLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		tuesdayGroup.add(tuesdayLabel);
		tuesdayButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		tuesdayGroup.add(tuesdayButton);
		
		horizontalBox.add(wednesdayGroup);
		wednesdayLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		wednesdayGroup.add(wednesdayLabel);
		wednesdayButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		wednesdayGroup.add(wednesdayButton);
		
		horizontalBox.add(thursdayGroup);
		thursdayLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		thursdayGroup.add(thursdayLabel);
		thursdayButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		thursdayGroup.add(thursdayButton);
		
		horizontalBox.add(fridayGroup);
		fridayLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		fridayGroup.add(fridayLabel);
		fridayButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		fridayGroup.add(fridayButton);
		
		horizontalBox.add(saturdayGroup);
		saturdayLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		saturdayGroup.add(saturdayLabel);
		saturdayButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		saturdayGroup.add(saturdayButton);
		
		GridBagConstraints gbc_addLabButton = new GridBagConstraints();
		gbc_addLabButton.insets = new Insets(0, 0, 5, 5);
		gbc_addLabButton.gridx = 2;
		gbc_addLabButton.gridy = 15;
		addCoursePanel.add(addLabButton, gbc_addLabButton);
		
		GridBagConstraints gbc_backButton = new GridBagConstraints();
		gbc_backButton.anchor = GridBagConstraints.WEST;
		gbc_backButton.insets = new Insets(0, 0, 5, 5);
		gbc_backButton.gridx = 2;
		gbc_backButton.gridy = 18;
		addCoursePanel.add(backButton, gbc_backButton);
		
		GridBagConstraints gbc_submitButton = new GridBagConstraints();
		gbc_submitButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_submitButton.insets = new Insets(0, 0, 5, 5);
		gbc_submitButton.gridx = 9;
		gbc_submitButton.gridy = 18;
		addCoursePanel.add(submitButton, gbc_submitButton);
		
		/*
		 * Action Listeners
		 */
		
		backButton.addActionListener(event -> {
			cardLayout.show(constPanel, "menu");
			addCoursePanel.removeAll();
		});
	}
	

	////////////////////
	/// Remove Course //
	////////////////////
	private void remCourse(JPanel remCoursePanel) {
		// Set up Panel
		GridBagLayout layout = new GridBagLayout();
		remCoursePanel.setLayout(layout);

		// Set layout dimensions
		layout.columnWidths = new int[] { 34, 136, 130, 0, 0, 134, 45, 183, 0, 0 };
		layout.rowHeights = new int[] { 25, 0, 16, 26, 16, 26, 16, 27, 16, 27, 16, 27, 16, 29, 16, 31, 29, 0 };
		layout.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		layout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				0.0, 0.0, Double.MIN_VALUE };

		/*
		 * Common Components
		 */
		JLabel title = new JLabel("Remove Course Form");
		JLabel crn_string = new JLabel("Course CRN");
		JTextField crnField = new JTextField();
		JButton removeButton = new JButton("Remove");
		JButton backButton = new JButton("Back");

		/*
		 * Add Components to panel
		 */
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setFont(new Font("Lucida Grande", Font.BOLD, 20));
		GridBagConstraints gbc_title = new GridBagConstraints();
		gbc_title.anchor = GridBagConstraints.WEST;
		gbc_title.insets = new Insets(0, 0, 5, 5);
		gbc_title.gridwidth = 3;
		gbc_title.gridx = 3;
		gbc_title.gridy = 1;
		remCoursePanel.add(title, gbc_title);
		
		crn_string.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		GridBagConstraints gbc_CRN = new GridBagConstraints();
		gbc_CRN.anchor = GridBagConstraints.NORTH;
		gbc_CRN.insets = new Insets(0, 0, 5, 5);
		gbc_CRN.gridx = 4;
		gbc_CRN.gridy = 4;
		remCoursePanel.add(crn_string, gbc_CRN);

		crnField.setColumns(10);
		GridBagConstraints gbc_crnField = new GridBagConstraints();
		gbc_crnField.fill = GridBagConstraints.HORIZONTAL;
		gbc_crnField.anchor = GridBagConstraints.NORTH;
		gbc_crnField.insets = new Insets(0, 0, 5, 5);
		gbc_crnField.gridx = 4;
		gbc_crnField.gridy = 5;
		remCoursePanel.add(crnField, gbc_crnField);

		GridBagConstraints gbc_removeButton = new GridBagConstraints();
		gbc_removeButton.insets = new Insets(0, 0, 5, 5);
		gbc_removeButton.gridx = 4;
		gbc_removeButton.gridy = 6;
		remCoursePanel.add(removeButton, gbc_removeButton);
		
		GridBagConstraints gbc_backButton = new GridBagConstraints();
		gbc_backButton.insets = new Insets(0, 0, 0, 5);
		gbc_backButton.gridx = 7;
		gbc_backButton.gridy = 16;
		remCoursePanel.add(backButton, gbc_backButton);
		
		/*
		 * Action Listeners
		 */
		removeButton.addActionListener(event -> {
			
			// Check if CRN is 5 digits long
			if (crnField.getText().length() != 5) {
				JOptionPane.showMessageDialog(remCoursePanel, "R-Number must be 9 digits long (e.g. R12345678)",
						"Fix R-Number field", JOptionPane.ERROR_MESSAGE);
				return;
			}
				
			// Remove course entails removing it from every .txt file it may be on,
			// since some courses can be considered introductory courses for some
			// but elective for others and vice-versa.
			boolean courseFound = false;
			courseFound = remCourse_Helper("./introductory.txt", crnField, remCoursePanel);
			courseFound = remCourse_Helper("./electives.txt", crnField, remCoursePanel);
			courseFound = remCourse_Helper("./competency.txt", crnField, remCoursePanel);
			courseFound = remCourse_Helper("./rccCourses.txt", crnField, remCoursePanel);

			if (courseFound) {
				// If reached here, course has been successfully removed from everywhere
				JOptionPane.showMessageDialog(remCoursePanel, "Course successfully removed.", "Course Removal",
						JOptionPane.OK_OPTION);
				// Go back to main menu

				cardLayout.show(constPanel, "menu");
				remCoursePanel.removeAll();
			}

		});
		
		backButton.addActionListener(event -> {
			cardLayout.show(constPanel, "menu");
			remCoursePanel.removeAll();
		});
	}

	// @param path - the .txt to which remove the course from
	private boolean remCourse_Helper(String path, JTextField crnField, JPanel remCoursePanel) {
		// Check removal for introductory courses
		if (path.equals("./introductory.txt")) {
			// Make a copy of the list
			ArrayList<ScheduledCourse> introductoryCopy = new ArrayList<ScheduledCourse>(
					database.getIntroductoryCourses());
			for (ScheduledCourse course : introductoryCopy) {
				if (course.getCourse().getCRN() == Integer.parseInt(crnField.getText().toString())) {
					// Try to open introductory files
					File inputFile = new File(path);
					if (!inputFile.exists()) {
						JOptionPane.showMessageDialog(remCoursePanel,
								"Error: There are no records of any introducory courses in the system.",
								"Unable to remove course", JOptionPane.ERROR_MESSAGE);
						// TODO: Perhaps go back to main menu instead
						// Go back to main menu
						cardLayout.show(constPanel, "menu");
						remCoursePanel.removeAll();
						return false;
					}

					// Try to remove course from introductory
					try {
						File outputFile = new File("./remCourse_output1.txt");
						BufferedWriter writer;

						if (outputFile.exists()) {
							// Overwrite the file by creating a new BufferedWriter with append mode set to
							// false
							writer = new BufferedWriter(new FileWriter(outputFile, false));
							writer.write("");
						} else {
							// Create a new file
							outputFile.createNewFile();
							writer = new BufferedWriter(new FileWriter(outputFile));
						}

						Scanner scanner = new Scanner(inputFile);
						while (scanner.hasNextLine()) {
							// Read line by line
							String line = scanner.nextLine();
							String[] courseDetails = line.split(", ");

							// CRN will be the second string/element on the file/array
							// If the CRN is found, it won't be written to the file
							if (courseDetails[1].equals(crnField.getText().toString())) {

								// Check if that course has a lab, if so, also remove
								// the next line containing lab details/info
								if (courseDetails[9].equals("True")) {
									scanner.nextLine();
								}

								scanner.nextLine();
							} else {
								writer.write(line);
								writer.newLine();
							}
						}

						// Close input and output stream
						scanner.close();
						writer.close();

						// Replace input file with the output file
						inputFile.delete();
						outputFile.renameTo(inputFile);

						// Remove course from ArrayList<ScheduledCourse> introductoryCourses in database
						database.getIntroductoryCourses().remove(course);

						// Return true means course was found and deleted
						return true;

					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} else if (path.equals("./electives.txt")) {
			// Make a copy of the list
			ArrayList<ScheduledCourse> electivesCopy = new ArrayList<ScheduledCourse>(database.getElectiveCourses());
			for (ScheduledCourse course : electivesCopy) {
				if (course.getCourse().getCRN() == Integer.parseInt(crnField.getText().toString())) {

					// Try to open introductory files
					File inputFile = new File(path);
					if (!inputFile.exists()) {
						JOptionPane.showMessageDialog(remCoursePanel,
								"Error: There are no records of any introducory courses in the system.",
								"Unable to remove course", JOptionPane.ERROR_MESSAGE);
						// Go back to main menu
						cardLayout.show(constPanel, "menu");
						remCoursePanel.removeAll();
						return false;
					}

					// Try to remove course from electives
					try {
						File outputFile = new File("./remCourse_output2.txt");
						BufferedWriter writer;

						if (outputFile.exists()) {
							// Overwrite the file by creating a new BufferedWriter with append mode set to
							// false
							writer = new BufferedWriter(new FileWriter(outputFile, false));
							writer.write("");
							writer.close();
						} else {
							// Create a new file
							outputFile.createNewFile();
							writer = new BufferedWriter(new FileWriter(outputFile));
						}

						Scanner scanner = new Scanner(inputFile);
						while (scanner.hasNextLine()) {
							// Read line by line
							String line = scanner.nextLine();
							String[] courseDetails = line.split(", ");

							// CRN will be the second string/element on the file/array
							// If the CRN is found, it won't be written to the file
							if (courseDetails[1].equals(crnField.getText().toString())) {
								// Check if that course has a lab, if so, also remove
								// the next line containing lab details/info
								if (courseDetails[9].equals("True")) {
									scanner.nextLine();
								}

								scanner.nextLine();
							} else {
								writer.write(line);
								writer.newLine();
							}
						}

						// Close input and output stream
						scanner.close();
						writer.close();

						// Replace input file with the output file
						inputFile.delete();
						outputFile.renameTo(inputFile);

						// Remove course from ArrayList<ScheduledCourse> electiveCourses in database
						database.getElectiveCourses().remove(course);

						// Return true means course was found and deleted
						return true;

					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} else if (path.equals("./competency.txt")) {
			// Make a copy of the list
			ArrayList<ScheduledCourse> competencyCopy = new ArrayList<ScheduledCourse>(database.getCompetencyCourses());
			for (ScheduledCourse course : competencyCopy) {
				if (course.getCourse().getCRN() == Integer.parseInt(crnField.getText().toString())) {

					// Try to open introductory files
					File inputFile = new File(path);
					if (!inputFile.exists()) {
						JOptionPane.showMessageDialog(remCoursePanel,
								"Error: There are no records of any introducory courses in the system.",
								"Unable to remove course", JOptionPane.ERROR_MESSAGE);

						// Go back to main menu
						cardLayout.show(constPanel, "menu");
						remCoursePanel.removeAll();
						return false;
					}

					// Try to remove course from competency courses
					try {
						File outputFile = new File("./remCourse_output3.txt");
						BufferedWriter writer;

						if (outputFile.exists()) {
							// Overwrite the file by creating a new BufferedWriter with append mode set to
							// false
							writer = new BufferedWriter(new FileWriter(outputFile, false));
							writer.write("");
							writer.close();
						} else {
							// Create a new file
							outputFile.createNewFile();
							writer = new BufferedWriter(new FileWriter(outputFile));
						}

						Scanner scanner = new Scanner(inputFile);
						while (scanner.hasNextLine()) {
							// Read line by line
							String line = scanner.nextLine();
							String[] courseDetails = line.split(", ");

							// CRN will be the second string/element on the file/array
							// If the CRN is found, it won't be written to the file
							if (courseDetails[1].equals(crnField.getText().toString())) {
								// Check if that course has a lab, if so, also remove
								// the next line containing lab details/info
								if (courseDetails[9].equals("True")) {
									scanner.nextLine();
								}

								scanner.nextLine();
							} else {
								writer.write(line);
								writer.newLine();
							}
						}

						// Close input and output stream
						scanner.close();
						writer.close();

						// Replace input file with the output file
						inputFile.delete();
						outputFile.renameTo(inputFile);

						// Remove course from ArrayList<ScheduledCourse> competencyCourses in database
						database.getCompetencyCourses().remove(course);

						// Return true means course was found and deleted
						return true;

					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} else if (path.equals("./rccCourses.txt")) {
			// Make a copy of the list
			ArrayList<ScheduledCourse> rccCopy = new ArrayList<ScheduledCourse>(database.getRCCCourses());
			for (ScheduledCourse course : rccCopy) {
				if (course.getCourse().getCRN() == Integer.parseInt(crnField.getText().toString())) {

					// Try to open introductory files
					File inputFile = new File(path);
					if (!inputFile.exists()) {
						JOptionPane.showMessageDialog(remCoursePanel,
								"Error: There are no records of any introducory courses in the system.",
								"Unable to remove course", JOptionPane.ERROR_MESSAGE);

						// Go back to main menu
						cardLayout.show(constPanel, "menu");
						remCoursePanel.removeAll();
						return false;
					}

					// Try to remove course from rcc courses
					try {
						File outputFile = new File("./remCourse_output4.txt");
						BufferedWriter writer;

						if (outputFile.exists()) {
							// Overwrite the file by creating a new BufferedWriter with append mode set to
							// false
							writer = new BufferedWriter(new FileWriter(outputFile, false));
							writer.write("");
							writer.close();
						} else {
							// Create a new file
							outputFile.createNewFile();
							writer = new BufferedWriter(new FileWriter(outputFile));
						}
						Scanner scanner = new Scanner(inputFile);
						while (scanner.hasNextLine()) {
							// Read line by line
							String line = scanner.nextLine();
							String[] courseDetails = line.split(", ");

							// CRN will be the second string/element on the file/array
							// If the CRN is found, it won't be written to the file
							if (courseDetails[1].equals(crnField.getText().toString())) {
								// Check if that course has a lab, if so, also remove
								// the next line containing lab details/info
								if (courseDetails[9].equals("True")) {
									scanner.nextLine();
								}

								scanner.nextLine();
							} else {
								writer.write(line);
								writer.newLine();
							}
						}

						// Close input and output stream
						scanner.close();
						writer.close();

						// Replace input file with the output file
						inputFile.delete();
						outputFile.renameTo(inputFile);

						// Remove course from ArrayList<ScheduledCourse> rccCourses in database
						database.getRCCCourses().remove(course);

						// Return true means course was found and deleted
						return true;

					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

		return true;
	}

	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new GUI();
			}
		});
	}
}
