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
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicBoolean;

public class GUI {
	private JFrame frame; 
	private CardLayout cardLayout;
	private JPanel cards;
	private Database database;

	public GUI() {

		// Create and set up
		frame = new JFrame("Rollins Schedule Maker");
		frame.setMinimumSize(new Dimension(770, 560));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		/*
		 *  Where the components controlled by the cardLayout are initialized
		 */
		JPanel mainMenuPanel = new JPanel()  {
			// Using anonymous inner class to override method that
			// displays the window to the screen
			@Override 
			public void setVisible(boolean visible) {
				// Call super class method
				super.setVisible(visible);
				
				// Generate courses and students' list every time we reach the main menu, aka, update and refresh
				if (visible) {
					// TODO: check return value and display warning sign if false
					database = Database.getInstance();
					if (database.initalizeDatabase() == false) {
						JOptionPane.showMessageDialog(cards, "Error initializing database. Please check the text files.",
								"Initializing Database", JOptionPane.ERROR_MESSAGE);
						System.exit(0);
						System.out.println("Error initializing database");
					}
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
		cards = new JPanel(cardLayout);
		cards.add(mainMenuPanel, "menu");

		ImageIcon icon = new ImageIcon("/Users/matheuswestphalen/Projects/ScheduleMaker/resources/rollins_logo.png");
		JLabel rollinsLogo = new JLabel();
		rollinsLogo
				.setIcon(icon);


		GridBagConstraints gbc_rollinsLogo = new GridBagConstraints();
		gbc_rollinsLogo.fill = GridBagConstraints.BOTH;
		gbc_rollinsLogo.insets = new Insets(0, 0, 5, 5);
		gbc_rollinsLogo.gridx = 6;
		gbc_rollinsLogo.gridy = 0;
		mainMenuPanel.add(rollinsLogo, gbc_rollinsLogo);

		// Where the GUI is assembled
		JButton addStudentButton = new JButton("Add Student");
		JButton remStudentButton = new JButton("Remove Student");
		JButton addCourseButton = new JButton("Add Course");
		JButton remCourseButton = new JButton("Remove Course");
		JButton makeScheduleButton = new JButton("Make Schedule");
		makeScheduleButton.setPreferredSize(new Dimension(150, 150));

		Box box = Box.createVerticalBox();
		box.add(addStudentButton);
		box.add(remStudentButton);
		box.add(addCourseButton);
		box.add(remCourseButton);
		box.add(makeScheduleButton);

		GridBagConstraints gbc_box = new GridBagConstraints();
		gbc_box.insets = new Insets(0, 0, 5, 5);
		gbc_box.gridx = 6;
		gbc_box.gridy = 3;
		mainMenuPanel.add(box, gbc_box);

		/*
		 * Action Listeners
		 */
		addStudentButton.addActionListener(event -> {
			cardLayout.show(cards, "addStudent");
			addStudent(addStudentPanel);
		});

		remStudentButton.addActionListener(event -> {
			cardLayout.show(cards, "remStudent");
			remStudent(remStudentPanel);
		});

		addCourseButton.addActionListener(event -> {
			cardLayout.show(cards, "addCourse");
			addCourse(addCoursePanel);
		});

		remCourseButton.addActionListener(event -> {
			cardLayout.show(cards, "remCourse");
			remCourse(remCoursePanel);
		});
		
		makeScheduleButton.addActionListener(event -> {
			
			// Check if there are enough courses for all students
			if (database.enoughCoursesForStudents(database.getIntroductoryCourses(), database.getStudentList().size(), 'i') == false) {
				// Not enough introductory courses
				JOptionPane.showMessageDialog(mainMenuPanel, "Error: There are more students than there are Introductory courses. Please add more",
						"Not enough courses", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (database.enoughCoursesForStudents(database.getCompetencyCourses(), database.getStudentList().size(), 'c') == false) {
				// Not enough competency courses
				JOptionPane.showMessageDialog(mainMenuPanel, "Error: There are more students than there are Competency courses. Please add more",
						"Not enough courses", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			if (database.enoughCoursesForStudents(database.getElectiveCourses(), database.getStudentList().size(), 'e') == false) {
				// Not enough elective courses
				JOptionPane.showMessageDialog(mainMenuPanel, "Error: There are more students than there are Elective courses. Please add more",
						"Not enough courses", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (database.enoughCoursesForStudents(database.getRCCCourses(), database.getStudentList().size(), 'r') == false) {
				// Not enough RCC courses
				JOptionPane.showMessageDialog(mainMenuPanel, "Error: There are more students than there are RCC courses. Please add more",
						"Not enough courses", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			
			Deque<Student> introDeque = new LinkedList<>();
			Deque<Student> compDeque = new LinkedList<>();
			Deque<Student> eleDeque = new LinkedList<>();
			Deque<Student> rccDeque = new LinkedList<>();
			
			Driver.createDeque(database.getStudentList(), introDeque);
			Driver.createDeque(database.getStudentList(), compDeque);
			Driver.createDeque(database.getStudentList(), eleDeque);
			Driver.createDeque(database.getStudentList(), rccDeque);
			
			// instead of checking if the courses are all full when adding can perhaps do a preemptive check comparing
			// size of all courses in category to number of students
			// might still run into issue if there is overlap
			
			Driver.addStudentsToIntroCourses(introDeque, database.getIntroductoryCourses());
			System.out.println("All students have a introductory course added.");
			Driver.addStudentsToCompetencyCourses(compDeque, database.getCompetencyCourses());
			System.out.println("All students have a competency course added.");
			Driver.addStudentsToElectiveCourses(eleDeque, database.getElectiveCourses());
			System.out.println("All students have a elective course added.");
			Driver.addStudentsToRCCCourses(rccDeque, database.getRCCCourses());
			System.out.println("All students have a RCC course added.");
			
			// Print to XLSX File (one workbook w/ worksheets for each student... good)
			for (int i = 0; i < database.getStudentList().size(); i++) {
				database.getStudentList().get(i).printStudentSchedule_Excel(i + 1, database.getStudentList().size());
			}
			
			JOptionPane.showMessageDialog(mainMenuPanel, "Students' schedules have been created. Quitting the system now.",
					"Schedules Creation", JOptionPane.INFORMATION_MESSAGE, icon);
			System.exit(0);
			
			return;
		});
		
		// Add other screens/panels to the layout manager (cardLayout)
		cards.add(addStudentPanel, "addStudent");
		cards.add(remStudentPanel, "remStudent");
		cards.add(addCoursePanel, "addCourse");
		cards.add(remCoursePanel, "remCourse");

		// First page to show when application opens
		cardLayout.show(cards, "menu");

		// Display the window
		frame.getContentPane().add(cards);
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
		ArrayList<JComboBox> rccComboBoxes;
		ArrayList<JComboBox> electivesComboBoxes;

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

		String[] statusOptions = { "N/A", "Early Decision", "Honors", "Accelerated Program" };
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
		gbc_electivesLabel.anchor = GridBagConstraints.NORTHWEST;
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
			//gbc_backButton.gridy++;
			//gbc_submitButton.gridy++;

			// Add new combobox and update addButton
			addStudentPanel.add(newComboBox, gbc_rccComboBoxes);
			addStudentPanel.add(addRCCButton, gbc_addRCCButton);
			addStudentPanel.add(electivesLabel, gbc_electivesLabel);
			addStudentPanel.add(electivesComboBoxes.get(0), gbc_electivesComboBoxes);
			addStudentPanel.add(addElectiveButton, gbc_addElectiveButton);
			addStudentPanel.add(backButton, gbc_backButton);
			addStudentPanel.add(submitButton, gbc_submitButton);

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
			gbc_backButton.gridy++;

			addStudentPanel.add(electivesLabel, gbc_electivesLabel);
			addStudentPanel.add(electivesComboBoxes.get(0), gbc_electivesComboBoxes);
			addStudentPanel.add(addElectiveButton, gbc_addElectiveButton);
			addStudentPanel.add(backButton, gbc_backButton);
			addStudentPanel.add(submitButton, gbc_submitButton);

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
							"Error: A student with the same R-Number is already in the system.", "Unable to add student",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			
			// Add student to .txt file
			try (FileWriter fw = new FileWriter("./students.txt", true)){
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

				// Initialize Student data
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
								: statusPick.getSelectedItem().toString().charAt(0)); // Ternary Operator: (condition) ? (value if true) : (value if false)
				
				String studentData2 = electivePref[0] + ", " + electivePref[1] + ", " + electivePref[2] + ", "
						+ electivePref[3] + ", " + electivePref[4] + ", " + electivePref[5] + ", " + electivePref[6];
				String studentData3 = rccPref[0] + ", " + rccPref[1] + ", " + rccPref[2] + ", " + rccPref[3] + ", "
						+ rccPref[4] + ", " + rccPref[5] + ", " + rccPref[6] + ", " + rccPref[7];

				//File file = new File("./students.txt");
				//if (!file.exists()) {
					//file.createNewFile();
				//}

				//FileWriter fileWriter = new FileWriter(file.getName(), true);
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(studentData1);
				bw.newLine();
				bw.write(studentData2);
				bw.newLine();
				bw.write(studentData3);
				bw.newLine();	
				bw.close();

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Error writing student data to file");
			}

			
			// Issue message that student was successfully added
			JOptionPane.showMessageDialog(addStudentPanel,
					"Student was successfully added.", "Student successfully added",
					JOptionPane.INFORMATION_MESSAGE);
			
			// Clean panel and go back to main menu
			cardLayout.show(cards, "menu");
			addStudentPanel.removeAll();
			return;
		});

		backButton.addActionListener(event -> {
			cardLayout.show(cards, "menu");
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
			cardLayout.show(cards, "menu");
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
							boolean lastInFile = false;
							// Read student data that is split into 3 lines
							String line1 = scanner.nextLine();
							String line2 = scanner.nextLine();
							String line3 = scanner.nextLine();

							if (line1.startsWith(student.getRNumber())) {
								if (scanner.hasNextLine()) {
									line1 = scanner.nextLine();
									line2 = scanner.nextLine();
									line3 = scanner.nextLine();
								} else {
									lastInFile = true;
								}
							}
							
							if (!lastInFile) {
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
					JOptionPane.showMessageDialog(remStudentPanel, "Student was successfully removed.", "Student Removal",
							JOptionPane.INFORMATION_MESSAGE, null);
					
					// Go back to main menu
					cardLayout.show(cards, "menu");
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

	private void addCourse(JPanel addCoursePanel) {
		// Set up Panel
		GridBagLayout layout = new GridBagLayout();
		addCoursePanel.setLayout(layout);

		// Set layout dimensions
		layout.columnWidths = new int[] { 34, 0, 0, 141, 130, 134, 45, 183, 0, 0, 0, 0 };
		layout.rowHeights = new int[] { 25, 0, 16, 26, 0, 16, 26, 16, 27, 16, 27, 16, 27, 16, 29, 16, 31, 29, 0 };
		layout.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		layout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				0.0, 0.0, 0.0, Double.MIN_VALUE };

		/*
		 * Common Components
		 */
		JLabel title = new JLabel("Add Course Form");
		JLabel courseCRNLabel = new JLabel("Course CRN");
		JTextField courseCRNBox = new JTextField();
		JLabel courseSubjectLabel = new JLabel("Course Subject"); // TODO add component to screen
		JTextField courseSubjectField = new JTextField();
		JLabel courseTitleLabel = new JLabel("Course Title");
		JLabel creditsLabel = new JLabel("Credit Hours");
		JTextField courseTitleField = new JTextField();
		JLabel coursePrefixLabel = new JLabel("Course Prefix");
		JLabel courseNumberLabel = new JLabel("Course Number");
		JTextField coursePrefixField = new JTextField();
		JTextField courseNumField = new JTextField();
		JLabel competencyLabel = new JLabel("Competency");
		JLabel capacityLabel = new JLabel("Capacity");
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
		JButton submitButton = new JButton("Submit");


		/*
		 * /* List possible credit hours
		 */
		 String[] creditHours = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };
		 JComboBox<String> creditsBox = new JComboBox<String>(creditHours);
		 creditsBox.setSelectedItem("N/A");
		 
		 /*
		  * List possible proficiency/competency
		  */
		 String[] competencies = { "N/A", "BCMP", "FCMP", "MCMP", "WCMP", "ECMP" };
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
			String[] startTimes = { "06:00 AM", "06:15 AM", "06:30 AM", "06:45 AM", "06:50 AM", "07:00 AM", "07:15 AM",
					"07:30 AM", "07:45 AM", "07:50 AM", "08:00 AM", "08:15 AM", "08:30 AM", "08:45 AM", "08:50 AM",
					"09:00 AM", "09:15 AM", "09:30 AM", "09:45 AM", "09:50 AM", "10:00 AM", "10:15 AM", "10:30 AM",
					"10:45 AM", "10:50 AM", "11:00 AM", "11:15 AM", "11:30 AM", "11:45 AM", "11:50 AM", "12:00 PM",
					"12:15 PM", "12:30 PM", "12:45 PM", "12:50 PM", "01:00 PM", "01:15 PM", "01:30 PM", "01:45 PM",
					"01:50 PM", "02:00 PM", "02:15 PM", "02:30 PM", "02:45 PM", "02:50 PM", "03:00 PM", "03:15 PM",
					"03:30 PM", "03:45 PM", "03:50 PM", "04:00 PM", "04:15 PM", "04:30 PM", "04:45 PM", "04:50 PM",
					"05:00 PM", "05:15 PM", "05:30 PM", "05:45 PM", "05:50 PM", "06:00 PM", "06:15 PM", "06:30 PM",
					"06:45 PM", "06:50 PM", "07:00 PM", "07:15 PM", "07:30 PM", "07:45 PM", "07:50 PM", "08:00 PM",
					"08:15 PM", "08:30 PM", "08:45 PM", "08:50 PM", "09:00 PM", "09:15 PM", "09:30 PM", "09:45 PM",
					"09:50 PM", };

			JComboBox<String> startTimeBox = new JComboBox<String>(startTimes);
			startTimeBox.setSelectedItem("N/A");
		 
		 /*
		  * List possible end times
		  */
			String[] endTimes = { "06:00 AM", "06:15 AM", "06:30 AM", "06:45 AM", "06:50 AM", "07:00 AM", "07:15 AM",
					"07:30 AM", "07:45 AM", "07:50 AM", "08:00 AM", "08:15 AM", "08:30 AM", "08:45 AM", "08:50 AM",
					"09:00 AM", "09:15 AM", "09:30 AM", "09:45 AM", "09:50 AM", "10:00 AM", "10:15 AM", "10:30 AM",
					"10:45 AM", "10:50 AM", "11:00 AM", "11:15 AM", "11:30 AM", "11:45 AM", "11:50 AM", "12:00 PM",
					"12:15 PM", "12:30 PM", "12:45 PM", "12:50 PM", "01:00 PM", "01:15 PM", "01:30 PM", "01:45 PM",
					"01:50 PM", "02:00 PM", "02:15 PM", "02:30 PM", "02:45 PM", "02:50 PM", "03:00 PM", "03:15 PM",
					"03:30 PM", "03:45 PM", "03:50 PM", "04:00 PM", "04:15 PM", "04:30 PM", "04:45 PM", "04:50 PM",
					"05:00 PM", "05:15 PM", "05:30 PM", "05:45 PM", "05:50 PM", "06:00 PM", "06:15 PM", "06:30 PM",
					"06:45 PM", "06:50 PM", "07:00 PM", "07:15 PM", "07:30 PM", "07:45 PM", "07:50 PM", "08:00 PM",
					"08:15 PM", "08:30 PM", "08:45 PM", "08:50 PM", "09:00 PM", "09:15 PM", "09:30 PM", "09:45 PM",
					"09:50 PM", };

			JComboBox<String> endTimeBox = new JComboBox<String>(endTimes);
			endTimeBox.setSelectedItem("N/A");

			/*
			 * Common Lab course Components
			 */
		JLabel labCRNLabel = new JLabel("Lab CRN");
		JTextField labCRNBox = new JTextField();
		JLabel labTitleLabel = new JLabel("Lab Title");
		JTextField labTitleBox = new JTextField();
		JLabel labCreditsLabel = new JLabel("Lab Credit Hours");
		JLabel labPrefixLabel = new JLabel("Lab Course Prefix");
		JLabel labNumberLabel = new JLabel("Lab Course Number");
		JTextField labPrefixField = new JTextField();
		JTextField labNumField = new JTextField();
		JLabel labCompetencyLabel = new JLabel("Lab Competency");
		JLabel labCapacityLabel = new JLabel("Lab Capacity");
		Box labHorizontalBox = Box.createHorizontalBox();
		Box labSundayGroup = Box.createVerticalBox();
		JLabel labSundayLabel = new JLabel("S");
		JRadioButton labSundayButton = new JRadioButton("");
		Box labMondayGroup = Box.createVerticalBox();
		JLabel labMondayLabel = new JLabel("M");
		JRadioButton labMondayButton = new JRadioButton("");
		Box labTuesdayGroup = Box.createVerticalBox();
		JLabel labTuesdayLabel = new JLabel("T");
		JRadioButton labTuesdayButton = new JRadioButton("");
		Box labWednesdayGroup = Box.createVerticalBox();
		JLabel labWednesdayLabel = new JLabel("W");
		JRadioButton labWednesdayButton = new JRadioButton("");
		Box labThursdayGroup = Box.createVerticalBox();
		JLabel labThursdayLabel = new JLabel("T");
		JRadioButton labThursdayButton = new JRadioButton("");
		Box labFridayGroup = Box.createVerticalBox();
		JLabel labFridayLabel = new JLabel("F");
		JRadioButton labFridayButton = new JRadioButton("");
		Box labSaturdayGroup = Box.createVerticalBox();
		JLabel labSaturdayLabel = new JLabel("S");
		JRadioButton labSaturdayButton = new JRadioButton("");
		JLabel labDaysLabel = new JLabel("Lab Meeting Days");
		JLabel labTimeLabel = new JLabel("Lab Start Time");
		JLabel labEndTimeLabel = new JLabel("Lab End Time");
		AtomicBoolean hasLab = new AtomicBoolean(false);
		
		/*
		 * List possible lab credit hours
		 */
		String[] labCreditHours = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };
		JComboBox<String> labCreditsBox = new JComboBox<String>(labCreditHours);
		labCreditsBox.setSelectedItem("N/A");
		 
		 /*
		  * List possible lab proficiency/competency
		  */
		String[] labCompetencies = { "N/A", "BCMP", "FCMP", "MCMP", "WCMP", "ECMP" };
		JComboBox<String> labCompetencyBox = new JComboBox<String>(labCompetencies);
		labCompetencyBox.setSelectedItem("N/A");
		 
		 /*
		  * List lab's capacity numbers
		  */
		SpinnerNumberModel labCapacityModel = new SpinnerNumberModel(0, 0, 99, 1);
		JSpinner labCapacitySpinner = new JSpinner(labCapacityModel);
		 
		 	/*
			 * List possible start times
			 */
			String[] labStartTimes = { "06:00 AM", "06:15 AM", "06:30 AM", "06:45 AM", "06:50 AM", "07:00 AM",
					"07:15 AM", "07:30 AM", "07:45 AM", "07:50 AM", "08:00 AM", "08:15 AM", "08:30 AM", "08:45 AM",
					"08:50 AM", "09:00 AM", "09:15 AM", "09:30 AM", "09:45 AM", "09:50 AM", "10:00 AM", "10:15 AM",
					"10:30 AM", "10:45 AM", "10:50 AM", "11:00 AM", "11:15 AM", "11:30 AM", "11:45 AM", "11:50 AM",
					"12:00 PM", "12:15 PM", "12:30 PM", "12:45 PM", "12:50 PM", "01:00 PM", "01:15 PM", "01:30 PM",
					"01:45 PM", "01:50 PM", "02:00 PM", "02:15 PM", "02:30 PM", "02:45 PM", "02:50 PM", "03:00 PM",
					"03:15 PM", "03:30 PM", "03:45 PM", "03:50 PM", "04:00 PM", "04:15 PM", "04:30 PM", "04:45 PM",
					"04:50 PM", "05:00 PM", "05:15 PM", "05:30 PM", "05:45 PM", "05:50 PM", "06:00 PM", "06:15 PM",
					"06:30 PM", "06:45 PM", "06:50 PM", "07:00 PM", "07:15 PM", "07:30 PM", "07:45 PM", "07:50 PM",
					"08:00 PM", "08:15 PM", "08:30 PM", "08:45 PM", "08:50 PM", "09:00 PM", "09:15 PM", "09:30 PM",
					"09:45 PM", "09:50 PM", };

			JComboBox<String> labStartTimeBox = new JComboBox<String>(labStartTimes);
			labStartTimeBox.setSelectedItem("N/A");

			/*
			 * List possible end times
			 */
			String[] labEndTimes = { "06:00 AM", "06:15 AM", "06:30 AM", "06:45 AM", "06:50 AM", "07:00 AM", "07:15 AM",
					"07:30 AM", "07:45 AM", "07:50 AM", "08:00 AM", "08:15 AM", "08:30 AM", "08:45 AM", "08:50 AM",
					"09:00 AM", "09:15 AM", "09:30 AM", "09:45 AM", "09:50 AM", "10:00 AM", "10:15 AM", "10:30 AM",
					"10:45 AM", "10:50 AM", "11:00 AM", "11:15 AM", "11:30 AM", "11:45 AM", "11:50 AM", "12:00 PM",
					"12:15 PM", "12:30 PM", "12:45 PM", "12:50 PM", "01:00 PM", "01:15 PM", "01:30 PM", "01:45 PM",
					"01:50 PM", "02:00 PM", "02:15 PM", "02:30 PM", "02:45 PM", "02:50 PM", "03:00 PM", "03:15 PM",
					"03:30 PM", "03:45 PM", "03:50 PM", "04:00 PM", "04:15 PM", "04:30 PM", "04:45 PM", "04:50 PM",
					"05:00 PM", "05:15 PM", "05:30 PM", "05:45 PM", "05:50 PM", "06:00 PM", "06:15 PM", "06:30 PM",
					"06:45 PM", "06:50 PM", "07:00 PM", "07:15 PM", "07:30 PM", "07:45 PM", "07:50 PM", "08:00 PM",
					"08:15 PM", "08:30 PM", "08:45 PM", "08:50 PM", "09:00 PM", "09:15 PM", "09:30 PM", "09:45 PM",
					"09:50 PM", };

			JComboBox<String> labEndTimeBox = new JComboBox<String>(labEndTimes);
			labEndTimeBox.setSelectedItem("N/A");

		/*
		 * Add Components to panel
		 */
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setFont(new Font("Lucida Grande", Font.BOLD, 20));

		GridBagConstraints gbc_title = new GridBagConstraints();
		gbc_title.anchor = GridBagConstraints.NORTHWEST;
		gbc_title.insets = new Insets(0, 0, 5, 5);
		gbc_title.gridwidth = 4;
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
		courseTitleField.setColumns(10);
		addCoursePanel.add(courseTitleField, gbc_courseTitleBox);
		
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
		coursePrefixField.setColumns(10);
		addCoursePanel.add(coursePrefixField, gbc_coursePrefixBox);
		
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
		addLabButton.addActionListener(event -> {
			hasLab.set(true);
			GridBagConstraints gbc_labTitleLabel = new GridBagConstraints();
			gbc_labTitleLabel.anchor = GridBagConstraints.WEST;
			gbc_labTitleLabel.insets = new Insets(0, 0, 5, 5);
			gbc_labTitleLabel.gridx = 5;
			gbc_labTitleLabel.gridy = 5;
			labTitleLabel.setFont(new Font("Lucida Grande", Font.BOLD, 13));
			addCoursePanel.add(labTitleLabel, gbc_labTitleLabel);
			
			GridBagConstraints gbc_labTitleBox = new GridBagConstraints();
			gbc_labTitleBox.insets = new Insets(0, 0, 5, 5);
			gbc_labTitleBox.fill = GridBagConstraints.HORIZONTAL;
			gbc_labTitleBox.gridx = 5;
			gbc_labTitleBox.gridy = 6;
			addCoursePanel.add(labTitleBox, gbc_labTitleBox);
			
			GridBagConstraints gbc_labCRNLabel = new GridBagConstraints();
			gbc_labCRNLabel.anchor = GridBagConstraints.WEST;
			gbc_labCRNLabel.insets = new Insets(0, 0, 5, 5);
			gbc_labCRNLabel.gridx = 5;
			gbc_labCRNLabel.gridy = 3;
			labCRNLabel.setFont(new Font("Lucida Grande", Font.BOLD, 13));
			addCoursePanel.add(labCRNLabel, gbc_labCRNLabel);
			
			GridBagConstraints gbc_labCreditsLabel = new GridBagConstraints();
			gbc_labCreditsLabel.anchor = GridBagConstraints.WEST;
			gbc_labCreditsLabel.insets = new Insets(0, 0, 5, 5);
			gbc_labCreditsLabel.gridx = 6;
			gbc_labCreditsLabel.gridy = 5;
			labCreditsLabel.setFont(new Font("Lucida Grande", Font.BOLD, 13));
			addCoursePanel.add(labCreditsLabel, gbc_labCreditsLabel);
			
			GridBagConstraints gbc_labCourseCRNBox = new GridBagConstraints();
			gbc_labCourseCRNBox.insets = new Insets(0, 0, 5, 5);
			gbc_labCourseCRNBox.fill = GridBagConstraints.HORIZONTAL;
			gbc_labCourseCRNBox.gridx = 5;
			gbc_labCourseCRNBox.gridy = 4;
			labCRNBox.setColumns(10);
			addCoursePanel.add(labCRNBox, gbc_labCourseCRNBox);
			
			GridBagConstraints gbc_labCreditsBox = new GridBagConstraints();
			gbc_labCreditsBox.insets = new Insets(0, 0, 5, 5);
			gbc_labCreditsBox.fill = GridBagConstraints.HORIZONTAL;
			gbc_labCreditsBox.gridx = 6;
			gbc_labCreditsBox.gridy = 6;
			addCoursePanel.add(labCreditsBox, gbc_labCreditsBox);
			
			GridBagConstraints gbc_labCoursePrefixLabel = new GridBagConstraints();
			gbc_labCoursePrefixLabel.anchor = GridBagConstraints.WEST;
			gbc_labCoursePrefixLabel.insets = new Insets(0, 0, 5, 5);
			gbc_labCoursePrefixLabel.gridx = 5;
			gbc_labCoursePrefixLabel.gridy = 7;
			labPrefixLabel.setFont(new Font("Lucida Grande", Font.BOLD, 13));
			addCoursePanel.add(labPrefixLabel, gbc_labCoursePrefixLabel);
			
			GridBagConstraints gbc_labCourseNumberLabel = new GridBagConstraints();
			gbc_labCourseNumberLabel.anchor = GridBagConstraints.WEST;
			gbc_labCourseNumberLabel.insets = new Insets(0, 0, 5, 5);
			gbc_labCourseNumberLabel.gridx = 6;
			gbc_labCourseNumberLabel.gridy = 7;
			labNumberLabel.setFont(new Font("Lucida Grande", Font.BOLD, 13));
			addCoursePanel.add(labNumberLabel, gbc_labCourseNumberLabel);
			
			GridBagConstraints gbc_labCoursePrefixBox = new GridBagConstraints();
			gbc_labCoursePrefixBox.insets = new Insets(0, 0, 5, 5);
			gbc_labCoursePrefixBox.fill = GridBagConstraints.HORIZONTAL;
			gbc_labCoursePrefixBox.gridx = 5;
			gbc_labCoursePrefixBox.gridy = 8;
			labPrefixField.setColumns(10);
			addCoursePanel.add(labPrefixField, gbc_labCoursePrefixBox);
			
			GridBagConstraints gbc_labCourseNumField = new GridBagConstraints();
			gbc_labCourseNumField.insets = new Insets(0, 0, 5, 5);
			gbc_labCourseNumField.fill = GridBagConstraints.HORIZONTAL;
			gbc_labCourseNumField.gridx = 6;
			gbc_labCourseNumField.gridy = 8;
			labNumField.setColumns(10);
			addCoursePanel.add(labNumField, gbc_labCourseNumField);
			
			GridBagConstraints gbc_labCompetencyLabel = new GridBagConstraints();
			gbc_labCompetencyLabel.anchor = GridBagConstraints.WEST;
			gbc_labCompetencyLabel.insets = new Insets(0, 0, 5, 5);
			gbc_labCompetencyLabel.gridx = 5;
			gbc_labCompetencyLabel.gridy = 9;
			labCompetencyLabel.setFont(new Font("Lucida Grande", Font.BOLD, 13));
			addCoursePanel.add(labCompetencyLabel, gbc_labCompetencyLabel);
			
			GridBagConstraints gbc_labCapacityLabel = new GridBagConstraints();
			gbc_labCapacityLabel.anchor = GridBagConstraints.WEST;
			gbc_labCapacityLabel.insets = new Insets(0, 0, 5, 5);
			gbc_labCapacityLabel.gridx = 6;
			gbc_labCapacityLabel.gridy = 9;
			labCapacityLabel.setFont(new Font("Lucida Grande", Font.BOLD, 13));
			addCoursePanel.add(labCapacityLabel, gbc_labCapacityLabel);
			
			GridBagConstraints gbc_labCompetencyBox = new GridBagConstraints();
			gbc_labCompetencyBox.insets = new Insets(0, 0, 5, 5);
			gbc_labCompetencyBox.fill = GridBagConstraints.HORIZONTAL;
			gbc_labCompetencyBox.gridx = 5;
			gbc_labCompetencyBox.gridy = 10;
			addCoursePanel.add(labCompetencyBox, gbc_labCompetencyBox);
			
			GridBagConstraints gbc_labCapacitySpinner = new GridBagConstraints();
			gbc_labCapacitySpinner.fill = GridBagConstraints.HORIZONTAL;
			gbc_labCapacitySpinner.insets = new Insets(0, 0, 5, 5);
			gbc_labCapacitySpinner.gridx = 6;
			gbc_labCapacitySpinner.gridy = 10;
			addCoursePanel.add(labCapacitySpinner, gbc_labCapacitySpinner);
			
			GridBagConstraints gbc_labMeetingTimeLabel = new GridBagConstraints();
			gbc_labMeetingTimeLabel.anchor = GridBagConstraints.WEST;
			gbc_labMeetingTimeLabel.insets = new Insets(0, 0, 5, 5);
			gbc_labMeetingTimeLabel.gridx = 5;
			gbc_labMeetingTimeLabel.gridy = 11;
			labTimeLabel.setHorizontalAlignment(SwingConstants.LEFT);
			labTimeLabel.setFont(new Font("Lucida Grande", Font.BOLD, 13));
			addCoursePanel.add(labTimeLabel, gbc_labMeetingTimeLabel);
			
			GridBagConstraints gbc_labEndTimeLabel = new GridBagConstraints();
			gbc_labEndTimeLabel.anchor = GridBagConstraints.WEST;
			gbc_labEndTimeLabel.insets = new Insets(0, 0, 5, 5);
			gbc_labEndTimeLabel.gridx = 6;
			gbc_labEndTimeLabel.gridy = 11;
			labEndTimeLabel.setHorizontalAlignment(SwingConstants.LEFT);
			labEndTimeLabel.setFont(new Font("Lucida Grande", Font.BOLD, 13));
			addCoursePanel.add(labEndTimeLabel, gbc_labEndTimeLabel);
			
			GridBagConstraints gbc_labStartTimeBox = new GridBagConstraints();
			gbc_labStartTimeBox.insets = new Insets(0, 0, 5, 5);
			gbc_labStartTimeBox.fill = GridBagConstraints.HORIZONTAL;
			gbc_labStartTimeBox.gridx = 5;
			gbc_labStartTimeBox.gridy = 12;
			addCoursePanel.add(labStartTimeBox, gbc_labStartTimeBox);
			
			GridBagConstraints gbc_labEndTimeBox = new GridBagConstraints();
			gbc_labEndTimeBox.insets = new Insets(0, 0, 5, 5);
			gbc_labEndTimeBox.fill = GridBagConstraints.HORIZONTAL;
			gbc_labEndTimeBox.gridx = 6;
			gbc_labEndTimeBox.gridy = 12;
			addCoursePanel.add(labEndTimeBox, gbc_labEndTimeBox);
			
			GridBagConstraints gbc_labDaysLabel = new GridBagConstraints();
			gbc_labDaysLabel.anchor = GridBagConstraints.WEST;
			gbc_labDaysLabel.insets = new Insets(0, 0, 5, 5);
			gbc_labDaysLabel.gridx = 5;
			gbc_labDaysLabel.gridy = 13;
			labDaysLabel.setHorizontalAlignment(SwingConstants.LEFT);
			labDaysLabel.setFont(new Font("Lucida Grande", Font.BOLD, 13));
			addCoursePanel.add(labDaysLabel, gbc_labDaysLabel);
			
			GridBagConstraints gbc_labHorizontalBox = new GridBagConstraints();
			gbc_labHorizontalBox.gridwidth = 2;
			gbc_labHorizontalBox.anchor = GridBagConstraints.WEST;
			gbc_labHorizontalBox.insets = new Insets(0, 0, 5, 5);
			gbc_labHorizontalBox.gridx = 5;
			gbc_labHorizontalBox.gridy = 14;
			addCoursePanel.add(labHorizontalBox, gbc_labHorizontalBox);
			
			labSundayGroup.setAlignmentX(Component.CENTER_ALIGNMENT);
			labHorizontalBox.add(labSundayGroup);
			
			labSundayLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			labSundayLabel.setHorizontalAlignment(SwingConstants.CENTER);
			
			labSundayGroup.add(labSundayLabel);
			labSundayButton.setAlignmentX(Component.CENTER_ALIGNMENT);
			
			labSundayButton.setHorizontalAlignment(SwingConstants.CENTER);
			labSundayGroup.add(labSundayButton);
			
			labHorizontalBox.add(labMondayGroup);
			labMondayLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			
			labMondayGroup.add(labMondayLabel);
			labMondayButton.setAlignmentX(Component.CENTER_ALIGNMENT);
			
			labMondayGroup.add(labMondayButton);
			
			labHorizontalBox.add(labTuesdayGroup);
			labTuesdayLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			
			labTuesdayGroup.add(labTuesdayLabel);
			labTuesdayButton.setAlignmentX(Component.CENTER_ALIGNMENT);
			
			labTuesdayGroup.add(labTuesdayButton);
			
			labHorizontalBox.add(labWednesdayGroup);
			labWednesdayLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			
			labWednesdayGroup.add(labWednesdayLabel);
			labWednesdayButton.setAlignmentX(Component.CENTER_ALIGNMENT);
			
			labWednesdayGroup.add(labWednesdayButton);
			
			labHorizontalBox.add(labThursdayGroup);
			labThursdayLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			
			labThursdayGroup.add(labThursdayLabel);
			labThursdayButton.setAlignmentX(Component.CENTER_ALIGNMENT);
			
			labThursdayGroup.add(labThursdayButton);
			
			labHorizontalBox.add(labFridayGroup);
			labFridayLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			
			labFridayGroup.add(labFridayLabel);
			labFridayButton.setAlignmentX(Component.CENTER_ALIGNMENT);
			
			labFridayGroup.add(labFridayButton);
			
			labHorizontalBox.add(labSaturdayGroup);
			labSaturdayLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			
			labSaturdayGroup.add(labSaturdayLabel);
			labSaturdayButton.setAlignmentX(Component.CENTER_ALIGNMENT);
			
			labSaturdayGroup.add(labSaturdayButton);
			// Remove Add Lab Button
			addCoursePanel.remove(addLabButton);

			// Add new components to the screen
			addCoursePanel.revalidate();
			addCoursePanel.repaint();
		});

		submitButton.addActionListener(event -> {
			

			// Check if CRN is 5 digits
			if (courseCRNBox.getText().length() != 5) {
				JOptionPane.showMessageDialog(addCoursePanel, "CRN must be 5 digits long (e.g. 98253)",
						"Fix CRN number", JOptionPane.ERROR_MESSAGE);
				return;
			}

			// Adding course entails checking to see it exists in all of the .txt files,
			// since some courses can be considered introductory courses for some but
			// elective for others and vice-versa.
			boolean courseFound = false;
			courseFound = addCourse_Helper(addCoursePanel, "./introductory.txt", courseCRNBox);
			// If course was found, that means we can't proceed with adding it to the .txt files
			if (courseFound) {
				JOptionPane.showMessageDialog(addCoursePanel,
						"Error: The entered course CRN matches one found in introductory.txt. Please enter a different CRN.",
						"Unable to add course", JOptionPane.ERROR_MESSAGE);
				return;
			}
			courseFound = addCourse_Helper(addCoursePanel, "./electives.txt", courseCRNBox);
			if (courseFound) {
				JOptionPane.showMessageDialog(addCoursePanel,
						"Error: The entered course CRN matches one found in electives.txt. Please enter a different CRN.",
						"Unable to add course", JOptionPane.ERROR_MESSAGE);
				return;
			}
			courseFound = addCourse_Helper(addCoursePanel, "./competency.txt", courseCRNBox);
			if (courseFound) {
				JOptionPane.showMessageDialog(addCoursePanel,
						"Error: The entered course CRN matches one found in competency.txt. Please enter a different CRN.",
						"Unable to add course", JOptionPane.ERROR_MESSAGE);
				return;
			}			
			courseFound = addCourse_Helper(addCoursePanel, "./rccCourses.txt", courseCRNBox);
			if (courseFound) {
				JOptionPane.showMessageDialog(addCoursePanel,
						"Error: The entered course CRN matches one found in rccCourses.txt. Please enter a different CRN.",
						"Unable to add course", JOptionPane.ERROR_MESSAGE);
				return;
			}

			
			
			// Get course meeting days
			try {
				String courseMeetingDays = ""; 
				if (mondayButton.isSelected()) {
					courseMeetingDays += "M";
				}

				if (tuesdayButton.isSelected()) {
					courseMeetingDays += "T";
				}

				if (wednesdayButton.isSelected()) {
					courseMeetingDays += "W";
				}

				if (thursdayButton.isSelected()) {
					courseMeetingDays += "R";
				}

				if (fridayButton.isSelected()) {
					courseMeetingDays += "F";
				}

				if (saturdayButton.isSelected()) {
					courseMeetingDays += "S";
				}

				if (sundayButton.isSelected()) {
					courseMeetingDays += "U";
				}

				// Initialize ScheduledCourse object and add course to .txt files
				String courseData = capacitySpinner.getValue().toString() + ", " + courseCRNBox.getText() + ", "
						+ coursePrefixField.getText() + " " + courseNumField.getText() + ", "
						+ courseTitleField.getText() + ", " + creditsBox.getSelectedItem().toString() + ", "
						+ startTimeBox.getSelectedItem().toString().substring(0, 5)
						+ "-" + endTimeBox.getSelectedItem().toString().substring(0, 5) + "-"
						+ endTimeBox.getSelectedItem().toString().charAt(6) + ", " + courseMeetingDays + ", " + "N" + ", " + 
						(competencyBox.getSelectedItem().toString().equals("N/A") ? "N" : competencyBox.getSelectedItem().toString()) +
						", " + (hasLab.get() ? "true" : "false");
				

				// If class has a lab
				String labData = "";
				if (hasLab.get() == true) {
					String labMeetingDays = "";
					if (labMondayButton.isSelected()) {
						labMeetingDays += "M";
					}

					if (labTuesdayButton.isSelected()) {
						labMeetingDays += "T";
					}

					if (labWednesdayButton.isSelected()) {
						labMeetingDays += "W";
					}

					if (labThursdayButton.isSelected()) {
						labMeetingDays += "R";
					}

					if (labFridayButton.isSelected()) {
						labMeetingDays += "F";
					}

					if (labSaturdayButton.isSelected()) {
						labMeetingDays += "S";
					}

					if (labSundayButton.isSelected()) {
						labMeetingDays += "U";
					}
					
					labData = labCapacitySpinner.getValue().toString() + ", " + labCRNBox.getText()
					+ ", " + labPrefixField.getText() + " "  + labNumField.getText() + ", " + labTitleBox.getText() 
					+ ", " + labCreditsBox.getSelectedItem().toString() + ", " + labStartTimeBox.getSelectedItem().toString().substring(0, 5)
					+ "-" + labEndTimeBox.getSelectedItem().toString().substring(0, 5) + "-" 
					+ labEndTimeBox.getSelectedItem().toString().charAt(6) + ", " + labMeetingDays + ", " + "N" + ", " + 
					(labCompetencyBox.getSelectedItem().toString().equals("N/A") ? "N" : labCompetencyBox.getSelectedItem().toString()) +
					", " + "false";
				}
				
				// Add course data to rccCourses.txt if it is one
				if (coursePrefixField.getText().equals("RCC")) {
					if (addCourseTo("./rccCourses.txt", courseData, labData) == false) {
						JOptionPane.showMessageDialog(addCoursePanel,
								"Error adding RCC course. Go back to main menu and try again.", "Error adding course",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
				} else {
					
					// Add course to introductory.txt and electives.txt, since any of these courses
					// can also be an elective
					if (addCourseTo("./introductory.txt", courseData, labData) == false) {
						JOptionPane.showMessageDialog(addCoursePanel,
								"Error adding course. Go back to main menu and try again.", "Error adding course",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					if (addCourseTo("./electives.txt", courseData, labData) == false) {
						JOptionPane.showMessageDialog(addCoursePanel,
								"Error adding course. Go back to main menu and try again.", "Error adding course",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					// Add course data to competency.txt if it has a competency
					if (!competencyBox.getSelectedItem().toString().equals("N/A")) {
						if (addCourseTo("./competency.txt", courseData, labData) == false) {
							JOptionPane.showMessageDialog(addCoursePanel,
									"Error adding competency course. Go back to main menu and try again.", "Error adding course",
									JOptionPane.ERROR_MESSAGE);
							return;
						}
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Error");
				JOptionPane.showMessageDialog(addCoursePanel,
						"Error adding course. Go back to main menu and try again.", "Error adding course",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			
			// Issue message that course was successfully added
			JOptionPane.showMessageDialog(addCoursePanel,
					"Course was successfully added.", "Course successfully added",
					JOptionPane.INFORMATION_MESSAGE);
			
			// Clean panel and go back to main menu
			cardLayout.show(cards, "menu");
			addCoursePanel.removeAll();
			return;
		});
		
		backButton.addActionListener(event -> {
			cardLayout.show(cards, "menu");
			addCoursePanel.removeAll();
		});
	}
	
	/*
	 * @return true if succeeded, false otherwise
	 */
	private boolean addCourseTo(String filePath, String courseData, String labData) {
		try {
			File file = new File(filePath);
			if (!file.exists() ) {
				file.createNewFile();
			}
			
			FileWriter fileWriter = new FileWriter(file.getName(), true);
			BufferedWriter bw = new BufferedWriter(fileWriter);
			bw.write(courseData);
			// If course has a lab
			if (labData.length() != 0) {
				bw.newLine();
				bw.write(labData);
			}
			bw.newLine();
			bw.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.print("Error writing course data to file " + filePath);
			return false;
		}
		return true;
	}
	
	// @param path - the .txt. to which add the course to
	// @return whether the course was found or not in the given path
	private boolean addCourse_Helper(JPanel addCoursePanel, String path, JTextField crnField) {
		// Check add course for introductory courses
		if (path.equals("./introductory.txt")) {
			// Make a copy of the list
			ArrayList<ScheduledCourse> introductoryCopy = new ArrayList<ScheduledCourse>(
					database.getIntroductoryCourses());
			for (ScheduledCourse course : introductoryCopy) {
				// If the entered CRN for the course matches one from the current list, reject request
				// to add course
				if (course.getCourse().getCRN() == Integer.parseInt(crnField.getText())) {
					return true;
				}
			}

		} else if (path.equals("./electives.txt")) {
			// Make a copy of the list
			ArrayList<ScheduledCourse> electivesCopy = new ArrayList<ScheduledCourse>(
					database.getCompetencyCourses());
			for (ScheduledCourse course : electivesCopy) {
				// If the entered CRN for the course matches one from the current list, reject
				// request to add course
				if (course.getCourse().getCRN() == Integer.parseInt(crnField.getText())) {
					return true;
				}
			}

		} else if (path.equals("./competency.txt")) {
			// Make a copy of the list
			ArrayList<ScheduledCourse> competencyCopy = new ArrayList<ScheduledCourse>(
					database.getCompetencyCourses());
			for (ScheduledCourse course : competencyCopy) {
				// If the entered CRN for the course matches one from the current list, reject
				// request to add course
				if (course.getCourse().getCRN() == Integer.parseInt(crnField.getText())) {
					return true;
				}
			}

		} else if (path.equals("./rccCourses.txt")) {
			// Make a copy of the list
			ArrayList<ScheduledCourse> rccCopy = new ArrayList<ScheduledCourse>(
					database.getRCCCourses());
			for (ScheduledCourse course : rccCopy) {
				// If the entered CRN for the course matches one from the current list, reject
				// request to add course
				if (course.getCourse().getCRN() == Integer.parseInt(crnField.getText())) {
					return true;
				}
			}
		}
		
		// If we reached here, the course to be added was not found in the other .txt files.
		// Proceed with course addition
		return false;
	}
	
	///////////////////////
	//// Remove Course ////
	///////////////////////
	
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
				JOptionPane.showMessageDialog(remCoursePanel, "CRN must be 5 digits long (e.g. 98512)",
						"Fix CRN Number field", JOptionPane.ERROR_MESSAGE);
				return;
			}
				
			// Remove course entails removing it from every .txt file it may be on,
			// since some courses can be considered introductory courses for some
			// but elective for others and vice-versa.
			boolean courseFound1 = false;
			boolean courseFound2 = false;
			boolean courseFound3 = false;
			boolean courseFound4 = false;
			courseFound1 = removeCourseFromFile(remCoursePanel, "./introductory.txt", crnField);
			courseFound2 = removeCourseFromFile(remCoursePanel, "./electives.txt", crnField);
			courseFound3 = removeCourseFromFile(remCoursePanel, "./competency.txt", crnField);
			courseFound4 = removeCourseFromFile(remCoursePanel, "./rccCourses.txt", crnField);

			if (courseFound1 || courseFound2 || courseFound3 || courseFound4) {
				// If reached here, course has been successfully removed from everywhere
				JOptionPane.showMessageDialog(remCoursePanel, "The course has been removed from the "
						+ "database", "Course Removal",
						JOptionPane.INFORMATION_MESSAGE);
				
				// Go back to main menu
				cardLayout.show(cards, "menu");
				remCoursePanel.removeAll();
				return;
				
			} else {
				
				// If reached here, course could not be found in any text file
				JOptionPane.showMessageDialog(remCoursePanel, "Error: The entered course could "
						+ "not be found in the database. Please enter a different CRN.", "Course Removal",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

		});
					
		backButton.addActionListener(event -> {
			cardLayout.show(cards, "menu");
			remCoursePanel.removeAll();
		});
	}

	// @param path - the .txt to which remove the course from
	private boolean removeCourseFromFile(JPanel remCoursePanel, String path, JTextField crnField) {
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
						// Go back to main menu
						cardLayout.show(cards, "menu");
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
							boolean lastInFile = false;
							String line = scanner.nextLine();
							String[] courseDetails = line.split(", ");

							if (courseDetails[1].equals(crnField.getText().toString())) {

								// Check if that course has a lab, if so, also remove
								// the next line containing lab details/info
								if (courseDetails[9].equals("true")) {
									scanner.nextLine();
								}
								
								if (scanner.hasNextLine()) {
									line = scanner.nextLine();
								} else {
									lastInFile = true;
								}
							}
							
							// If not in the last line of the file
							if (!lastInFile) {
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
						cardLayout.show(cards, "menu");
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
							boolean lastInFile = false;
							String line = scanner.nextLine();
							String[] courseDetails = line.split(", ");

							if (courseDetails[1].equals(crnField.getText().toString())) {

								// Check if that course has a lab, if so, also remove
								// the next line containing lab details/info
								if (courseDetails[9].equals("true")) {
									scanner.nextLine();
								}
								
								if (scanner.hasNextLine()) {
									line = scanner.nextLine();
								} else {
									lastInFile = true;
								}
							}
							
							// If not in the last line of the file
							if (!lastInFile) {
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
						cardLayout.show(cards, "menu");
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
							boolean lastInFile = false;
							// Read line by line
							String line = scanner.nextLine();
							String[] courseDetails = line.split(", ");

							if (courseDetails[1].equals(crnField.getText().toString())) {

								// Check if that course has a lab, if so, also remove
								// the next line containing lab details/info
								if (courseDetails[9].equals("true")) {
									scanner.nextLine();
								}
								
								if (scanner.hasNextLine()) {
									line = scanner.nextLine();
								} else {
									lastInFile = true;
								}
							}
							
							// If not in the last line of the file
							if (!lastInFile) {
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
						cardLayout.show(cards, "menu");
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
							//writer.close();
						} else {
							// Create a new file
							outputFile.createNewFile();
							writer = new BufferedWriter(new FileWriter(outputFile));
						}
						Scanner scanner = new Scanner(inputFile);
						while (scanner.hasNextLine()) {
							boolean lastInFile = false;
							// Read line by line
							String line = scanner.nextLine();
							String[] courseDetails = line.split(", ");

							if (courseDetails[1].equals(crnField.getText().toString())) {

								// Check if that course has a lab, if so, also remove
								// the next line containing lab details/info
								if (courseDetails[9].equals("true")) {
									scanner.nextLine();
								}
								
								if (scanner.hasNextLine()) {
									line = scanner.nextLine();
								} else {
									lastInFile = true;
								}
							}
							
							// If not in the last line of the file
							if (!lastInFile) {
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

		return false;
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
