import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Scanner;
import java.util.TreeSet;


public class GUI  {
	CardLayout cardLayout;
	JPanel constPanel;
	JButton addStudentButton, remStudentButton, addCourseButton, remCourseButton;
	ArrayList<JComboBox> rccComboBoxes;
	ArrayList<JComboBox> electivesComboBoxes;
	GroupLayout.ParallelGroup picksParallel;
	GroupLayout.SequentialGroup picksSequential;
	Database database;
	
	public GUI() {
		
		// Initialize database/courses
		database = new Database();
		database.generateCourses('i');
		database.generateCourses('c');
		database.generateCourses('r');
		database.generateCourses('e');
		
		// Create and set up
		JFrame frame = new JFrame("Freshmen Schedule Automation");
		frame.setMinimumSize(new Dimension(770, 560));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Where the components controlled by the cardLayout are initialized
		JPanel mainMenuPanel = new JPanel(new FlowLayout());
		JPanel addStudentPanel = new JPanel();
		JPanel remStudentPanel = new JPanel();
		JPanel addCoursePanel = new JPanel();
		JPanel remCoursePanel = new JPanel();
	
		
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
		
		
		mainMenuPanel.add(box);
		mainMenuPanel.setVisible(true);
		
		// Create the panel that contains the "cards"
		cardLayout = new CardLayout();
		constPanel = new JPanel(cardLayout);
		constPanel.add(mainMenuPanel, "menu");
		constPanel.add(addStudentPanel, "addStudent");
		constPanel.add(remStudentPanel, "remStudent");
		constPanel.add(addCoursePanel, "addCourse");
		constPanel.add(remCoursePanel, "remCourse");

		// First page to show when application opens
		cardLayout.show(constPanel, "menu");
		
		// Interactions with the buttons
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
		});
		
		remCourseButton.addActionListener(event -> {
			cardLayout.show(constPanel, "remCourse");
		});
		


		// Display the window
		frame.add(constPanel);
		frame.setSize(1000, 800);
		frame.setLocationRelativeTo(null);
		frame.pack();
		frame.setVisible(true);

	}
	
	private void addStudent(JPanel addStudentPanel) {
		// Set up Panel
		GridBagLayout layout = new GridBagLayout();
		addStudentPanel.setLayout(layout);
		
		// Set layout dimensions
		layout.columnWidths = new int[]{34, 136, 130, 134, 45, 183, 0, 0, 0, 0};
		layout.rowHeights = new int[]{25, 0, 16, 26, 16, 26, 16, 27, 16, 27, 16, 27, 16, 29, 16, 31, 29, 0};
		layout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		layout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		
		/*
		 * Common Components
		 */
		JLabel title = new JLabel("Add Student Form");
		JLabel name = new JLabel("Student Name:");
		JTextField firstNameField = new JTextField(10);
		JTextField lastNameField = new JTextField(10);
		JLabel rNumber = new JLabel ("Student's R-Number:");
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
		 *  Get introductory course list
		 */
		
		// Add majors based on currently available introductory classes in introductory.txt
		int majorsSize = database.getIntroductoryCourses().size();
		TreeSet<String> uniqueMajors = new TreeSet<String>();		// Avoid duplicates + allows for ascending order
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
		
		 // Add language courses based on currently available FCMP courses in competency.txt
		 int langSize = database.getCompetencyCourses().size();
		 TreeSet<String> uniqueLang = new TreeSet<String>();		// Avoid duplicates + allows for ascending order
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
		
		String[] statusOptions = {"N/A", "E", "H", "ARP" };
		JComboBox<String> statusPick = new JComboBox<String>(statusOptions);
		statusPick.setSelectedItem("N/A");
		
		
		/*
		 *  Get RCC course list
		 */
		

		int rccSize = database.getRCCCourses().size();
		TreeSet<String> uniqueRCCs = new TreeSet<String>();		// Avoid duplicates + allows for ascending order
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
		 *  Get electives course list
		 */

		// Add electives based on currently available elective courses on electives.txt
		int electivesSize = database.getElectiveCourses().size();
		TreeSet<String> uniqueElectives = new TreeSet<String>();	// Avoid duplicates + allows for ascending order
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
		
		//JScrollPane scrollPane = new JScrollPane(addStudentPanel);
		
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
		//scrollPane.setPreferredSize(new Dimension(770,560));
		//frame.getContentPane().add(scrollPane);
		
		//frame.pack();
		//frame.setVisible(true);
		
		
		/*
		 * Action Listeners
		 */
		
		// Add more RCC Course ComboBoxes
		addRCCButton.addActionListener(event -> {
				// Update RCC options
				int selectedItem = rccComboBoxes.size() - 1;
				//.remove(rccComboBoxes.get(selectedItem).getSelectedItem().toString());
				//sortedRCCList.toArray(rccList);
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

			// Set grid bag constraints of new electives ComboBox to the appropriate positions
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
			
			// Add student to .txt file
			try {
				// Convert the selected preferences for RCC into arrays using Course Code instead of Course Title
				String[] rccPref = { "N", "N", "N", "N", "N", "N", "N", "N" };
				for (int i = 0; i < rccComboBoxes.size(); i++) {
					for (ScheduledCourse rccCourse : database.getRCCCourses()) {
						if (rccCourse.getCourse().getCourseTitle().equals(rccComboBoxes.get(i).getSelectedItem().toString())) {
							rccPref[i] = rccCourse.getCourse().getCourseCode();
						}
					}
				}

				
				String[] electivePref = { "N", "N", "N", "N", "N", "N", "N", "N" };
				for (int i = 0; i < electivesComboBoxes.size(); i++) {
					for (ScheduledCourse electiveCourse : database.getCompetencyCourses()) {
						if (electiveCourse.getCourse().getCourseTitle().equals(electivesComboBoxes.get(i).getSelectedItem().toString())) {
							electivePref[i] = electiveCourse.getCourse().getCourseCode();
						}
					}
				}
				
				// Initialize Student Object
				String studentData1 = rNumberField.getText() + ", " + firstNameField.getText() + ", " + lastNameField.getText() + 
						", " + (major1Pick.getSelectedItem().toString().equals("N/A") ? "N" : major1Pick.getSelectedItem().toString()) + 				 // Ternary Operator: (condition) ? (value if true) : (value if false)
						", " + (major2Pick.getSelectedItem().toString().equals("N/A") ? "N" : major2Pick.getSelectedItem().toString()) + 				 // Ternary Operator: (condition) ? (value if true) : (value if false)
						", " + (langPreferencePick.getSelectedItem().toString().equals("N/A") ? "N" : langPreferencePick.getSelectedItem().toString()) + // Ternary Operator: (condition) ? (value if true) : (value if false)
						", " + (statusPick.getSelectedItem().toString().equals("N/A") ? "N" : statusPick.getSelectedItem().toString()); 				 // Ternary Operator: (condition) ? (value if true) : (value if false)
				String studentData2 = electivePref[0] + ", " + electivePref[1] + ", " + electivePref[2] + ", " + electivePref[3] +
						", " + electivePref[4] + ", " + electivePref[5] + ", " + electivePref[6];
				String studentData3 = rccPref [0] + ", " + rccPref[1] + ", " + rccPref[2] + ", " + rccPref[3] + ", " + rccPref[4] +
						", " + rccPref[5] + ", " + rccPref[6] + ", " + rccPref[7];
				
				File file = new File ("./students.txt");
				if (!file.exists()) {
					file.createNewFile() ;
				}
				
				// Test
				System.out.println(studentData1);
				System.out.println(studentData2);
				System.out.println(studentData3);
				
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
		});
		
		backButton.addActionListener(event -> {
			cardLayout.show(constPanel, "menu");
			addStudentPanel.removeAll();
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
		layout.columnWidths = new int[]{34, 136, 130, 0, 0, 134, 45, 183, 0, 0};
		layout.rowHeights = new int[]{25, 0, 16, 26, 16, 26, 16, 27, 16, 27, 16, 27, 16, 29, 16, 31, 29, 0};
		layout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		layout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		
		/*
		 *  Common Components
		 */
		JLabel title = new JLabel("Remove Student Form");
		JLabel rNumber = new JLabel("R-Number");
		JTextField rNumberField = new JTextField();
		JButton removeButton = new JButton("Remove");
		
		
		// Initialize database/courses
		// TODO: Maybe initialize this every time we're in main menu instead
		database = new Database();
		database.generateCourses('i');
		database.generateCourses('c');
		database.generateCourses('r');
		database.generateCourses('e');
		database.generateStudentList();
	
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

		removeButton.addActionListener(event -> {
			for (Student student : database.getStudentList()) {
				if (student.getRNumber().equals(rNumberField.getText())) {				
					
					// Try to open the file
					File inputFile = new File("./students.txt");
					if (!inputFile.exists()) {
						JOptionPane.showMessageDialog(remStudentPanel, "Error: There are no records of any student in the system.", 
								"Unable to remove student", JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					// Try to remove student from students.txt
					try {
						File outputFile = new File("remStudent_output.txt");
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
						
						// Remove Student from ArrayList<Student> StudentList in database
						database.getStudentList().remove(student);
						
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					// If reached here, student has been successfully removed from everywhere
					JOptionPane.showMessageDialog(remStudentPanel, "Student successfully removed.",
							"Student Removal", JOptionPane.OK_OPTION);
					
					// Go back to main menu
					cardLayout.show(constPanel, "menu");
					remStudentPanel.removeAll();
					
				}
			}
			
			// Didn't find student
			JOptionPane.showMessageDialog(remStudentPanel, "Unable to locate student. Please enter R Number again", 
					"Unable to locate student", JOptionPane.ERROR_MESSAGE);
			return;
		});
		
		remStudentPanel.add(removeButton, gbc_removeButton);
		//remStudentPanel.add(scrollPane);


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
