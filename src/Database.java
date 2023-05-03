import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.time.*;
public class Database {
	private static ArrayList<ScheduledCourse> introductoryCourses;
	private static ArrayList<ScheduledCourse> competencyCourses;
	private static ArrayList<ScheduledCourse> electiveCourses;
	private static ArrayList<ScheduledCourse> rccCourses;
	private static ArrayList<Student> studentList;
	private static Database instance = null;
	
	/**
	 * Constructor for Database.
	 * @return true if successfully generated all lists, false otherwise.
	 */
	private Database() {
	}	
	
	/* Database is a singleton with only one instance shared throughout the program.
	 * @return instance of the database
	 */
	public static Database getInstance() {
		if (instance == null) {
			instance = new Database();
		}
		
		return instance;
	}
	
	/*
	 * Where all of the data needed for the database is initialized. 
	 * @return true if successfully generated all lists, false otherwise.
	 */
	public boolean initalizeDatabase() {
		// Initialize courses	
		 introductoryCourses = new ArrayList<ScheduledCourse>();
		if (generateCourses('i') == false) {
			// Error generating introductory courses
			System.out.println("Error generating introductory courses. Check introductory.txt.");
			return false;
		}
		
		competencyCourses = new ArrayList<ScheduledCourse>();
		if (generateCourses('c') == false) {
			// Error generating competency courses
			System.out.println("Error generating competency courses. Check competency.txt.");
			return false;
		}
		
		rccCourses = new ArrayList<ScheduledCourse>();
		if (generateCourses('r') == false) {
			// Error generating RCC courses
			System.out.println("Error generating RCC courses. Check rccCourses.txt.");
			return false;
		}
		
		electiveCourses = new ArrayList<ScheduledCourse>();
		if (generateCourses('e') == false) {
			// Error generating elective courses
			System.out.println("Error generating elective courses. Check electives.txt.");
			return false;
		}
		
		// Initialize students' list
		studentList = new ArrayList<Student>();
		if (generateStudentList() == false) {
			// Error generating student's list
			System.out.println("Error generating student list. Check students.txt.");
			return false;
		}
		
		// Successfully generated all lists
		return true;
	}
	
	/**
	 * Populates one of the array-lists for courses based on passed in character.
	 * Passed in character decides which file is to be read from and where the ScheduledCourses will be stored.
	 * 
	 * @param typeOfCourses a character that indicates which course type to generate courses for
	 * @return true if it successfully generated all courses, false otherwise.
	 */ 
	public boolean generateCourses(char typeOfCourses) {
		try {
			File fileObj;
			if (typeOfCourses == 'i') {
				fileObj = new File("introductory.txt");
			} else if (typeOfCourses == 'c') {
				fileObj = new File("competency.txt");
			} else if (typeOfCourses == 'e') {
				fileObj = new File("electives.txt");
			} else {
				fileObj = new File("rccCourses.txt");
			}
			Scanner fileInput = new Scanner(fileObj);
			int lineNumber = 1;
			while (fileInput.hasNextLine()) {
				String line = fileInput.nextLine();
				String[] values = line.split(", ");
				if (checkNumberOfVariables(lineNumber, fileObj, values, 10) == false) {
					return false;
				}
				if (checkCourseVariables(lineNumber, fileObj, values) == false) {
					return false;
				}
				int courseCapacity = Integer.parseInt(values[0]);
				int CRN = Integer.parseInt(values[1]);
				String course = values[2];
				String courseTitle = values[3];
				int credits = Integer.parseInt(values[4]);
				String meetingTime = values[5];
				String meetingDays = values[6];
				String courseMajor = values[7];
				String proficiency = values[8];
				Boolean hasLab = Boolean.parseBoolean(values[9]);
				Course courseToAdd = new Course(courseCapacity, CRN, course, courseTitle, credits, courseMajor, proficiency);
				Time courseTime = new Time(meetingTime, meetingDays);
				if (hasLab == true) {
					String line2 = fileInput.nextLine();
					String[] values2 = line2.split(", ");
					if (checkNumberOfVariables(lineNumber, fileObj, values, 10) == false) {
						return false;
					}
					if (checkCourseVariables(lineNumber, fileObj, values) == false) {
						return false;
					}
					int courseCapacity2 = Integer.parseInt(values2[0]);
					int CRN2 = Integer.parseInt(values2[1]);
					String course2 = values2[2];
					String courseTitle2 = values2[3];
					int credits2 = Integer.parseInt(values2[4]);
					String meetingTime2 = values2[5];
					String meetingDays2 = values2[6];
					String courseMajor2 = values2[7];
					String proficiency2 = values2[8];
					Course courseLabToAdd = new Course(courseCapacity2, CRN2, course2, courseTitle2, credits2, courseMajor2, proficiency2);
					Time courseLabTime = new Time(meetingTime2, meetingDays2);
					
					if (typeOfCourses == 'i') {
						introductoryCourses.add(new ScheduledCourse(courseToAdd, courseTime, courseLabToAdd, courseLabTime));
					} else if (typeOfCourses == 'c') {
						competencyCourses.add(new ScheduledCourse(courseToAdd, courseTime, courseLabToAdd, courseLabTime));
					} else if (typeOfCourses == 'e') {
						electiveCourses.add(new ScheduledCourse(courseToAdd, courseTime, courseLabToAdd, courseLabTime));
					} else {
						rccCourses.add(new ScheduledCourse(courseToAdd, courseTime, courseLabToAdd, courseLabTime));
					}
					lineNumber++;
				} else {
					if (typeOfCourses == 'i') {
						introductoryCourses.add(new ScheduledCourse(courseToAdd, courseTime));
					} else if (typeOfCourses == 'c') {
						competencyCourses.add(new ScheduledCourse(courseToAdd, courseTime));
					} else if (typeOfCourses == 'e') {
						electiveCourses.add(new ScheduledCourse(courseToAdd, courseTime));
					} else {
						rccCourses.add(new ScheduledCourse(courseToAdd, courseTime));
					}
				}
				lineNumber++;
			}
			// Close file
			fileInput.close();
			
			// Print to terminal which type of courses have been initalized
			if (typeOfCourses == 'i') {
				System.out.println("Introductory courses have been loaded.");
			} else if (typeOfCourses == 'c') {
				System.out.println("Competency courses have been loaded.");
			} else if (typeOfCourses == 'e') {
				System.out.println("Elective courses have been loaded.");
			} else {
				System.out.println("RCC courses have been loaded.");
			}
			
			return true;
		} catch (FileNotFoundException exc) {
			 if (typeOfCourses == 'i') {
				 System.out.println("The course file: introductory.txt is missing.");
			 } else if (typeOfCourses == 'c') {
				 System.out.println("The course file: competency.txt is missing.");
			 } else if (typeOfCourses == 'e') {
				 System.out.println("The course file: electives.txt is missing.");
			 } else {
				 System.out.println("The course file: rccCourses.txt is missing.");
			 }
			
			 System.out.println("Please make sure all course files are present to run the system.");
		     System.out.println("System shutting down.");
		     return false;
		}
	}
	
	/**
	 * Generates student objects from the student text file, adding them to the student list.
	 * @return true if it successfully generated student list, false otherwise.
	 */
	public boolean generateStudentList() { //
		try {
			File fileObj = new File("students.txt");
			Scanner fileInput = new Scanner(fileObj);
			int lineNumber = 0;
			while (fileInput.hasNextLine()) {
				String line = fileInput.nextLine();
				lineNumber++;
				String[] values = line.split(", ");
				 if (checkNumberOfVariables(lineNumber, fileObj, values, 7) == false) {
					 return false;
				 }
				String rNumber = values[0];
				String firstName = values[1];
				String lastName = values[2];
				String firstMajor = values[3];
				String secondMajor = values[4];
				String lang = values[5];
				String status = values[6];
				String line2 = fileInput.nextLine();
				lineNumber++;
				String[] values2 = line2.split(", ");
				ArrayList<ScheduledCourse> electivePref = new ArrayList<ScheduledCourse>();
				for(int i = 0; i < 7; i++) {
					if (!values2[i].equals("N")) {
						// tests to see if they have an elective preference or not, if not don't add
						ScheduledCourse c = findCourse(values2[i], 'e');
						if (c == null) {
							return false;
						}
						electivePref.add(c);
					}
				}
				
				String line3 = fileInput.nextLine();
				lineNumber++;
				String[] values3 = line3.split(", ");
				ArrayList<ScheduledCourse> rccPref = new ArrayList<ScheduledCourse>();
				for(int i = 0; i < 8; i++) {
					if (!values3[i].equals("N")) {
						ScheduledCourse c = findCourse(values3[i], 'r');
						if (c == null) {
							return false;
						}
						rccPref.add(c);
					}
				}
				studentList.add(new Student(firstName, lastName, rNumber, firstMajor, secondMajor, lang, status, electivePref, rccPref));
			}
			fileInput.close();
		} catch (Exception exc) { 
			System.out.println("An error occurred generating students' list.");
			exc.printStackTrace();
			return false;
		}
		
		// Successfully generated students' list
		System.out.println("Students have been loaded.");
		return true;
	}
	
	/**
	 * Checks to see if the number of variables on the line in the text file matches the expected amount.
	 * 
	 * @param lineNumber the line in the text file that the scanner is currently reading in
	 * @param txtFile the text file which the scanner is reading from
	 * @param values the data stored on the line in the text file
	 * @param expectedLength how many points of data are expected on the text file line
	 * @return false if an error was found at a certain line/file, true if no errors were found
	 */ 
	public boolean checkNumberOfVariables(int lineNumber, File txtFile, String[] values, int expectedLength) { 
		if (values.length < expectedLength) {
			System.out.println("Error on line " + lineNumber + " in file " + txtFile);
			System.out.println("Missing expected variable");
			System.out.println();
			return false;
		} else if (values.length > expectedLength) {
			System.out.println("There are to many variables on line " + lineNumber + " in file " + txtFile);
			System.out.println();
			return false;
		}
		return true;
	}	

	/**
	 * Checks to see if the type of data in the text file matches the expected types.
	 * 
	 * @param lineNumber the line in the text file that the scanner is currently reading in
	 * @param txtFile the text file which the scanner is reading from
	 * @param values the data stored on the line in the text file
	 * @return false if an error was found at a certain line, true if no errors were found
	 */ 
	public boolean checkCourseVariables(int lineNumber, File txtFile, String[] values) {
		try {
			Integer.parseInt(values[0]);
			Integer.parseInt(values[1]);
			Integer.parseInt(values[4]);
			Boolean.parseBoolean(values[9]);
			String[] meetingTimes = values[5].split("-");
			String[] firstTimes = meetingTimes[0].split(":");
			String[] secondTimes = meetingTimes[1].split(":");
			LocalTime.of(Integer.parseInt(firstTimes[0]), Integer.parseInt(firstTimes[1]));
			LocalTime.of(Integer.parseInt(secondTimes[0]), Integer.parseInt(secondTimes[1]));
			meetingTimes[2].charAt(0);
			
		} catch (Exception e) {
			System.out.println("Error on line " + lineNumber + " in file " + txtFile);
			System.out.println("Invalid type for course information.");
			System.out.println();
			return false;
		}
		
		return true;
	}

	/**
	 * Checks to see if there is enough courses of a certain type to house all students. 
	 * If there is not enough courses, type is used to indicate to the user which type of course does not have enough courses in it.
	 * 
	 * @param courseCategory all of the courses for a certain type of course
	 * @param numbOfStudents int of the number of students
	 * @param type character to represent type of courses
	 * @return true if there are enough courses for students, false otherwise
	 */ 
	public boolean enoughCoursesForStudents(ArrayList<ScheduledCourse> courseCategory, int numbOfStudents, char type) {
		int totalCourseSeats = 0;
		String courseTypeName;
		if (type == 'i') {
			courseTypeName = "introductory";
		} else if (type == 'c') {
			courseTypeName = "competency";
		} else if (type == 'e') {
			courseTypeName = "elective";
		} else {
			courseTypeName = "rcc";
		}
		for (int i = 0; i < courseCategory.size(); i++) {
			totalCourseSeats = totalCourseSeats + courseCategory.get(i).getCourse().getCapacity();
		}
		if (totalCourseSeats < numbOfStudents) {
			System.out.println("Error: There are more students than there are courses for " + courseTypeName + " courses.");
			return false;	
		}
		
		return true;
	}
	
	public ArrayList<ScheduledCourse> getIntroductoryCourses() {
		return introductoryCourses;
	}
	
	public ArrayList<ScheduledCourse> getCompetencyCourses() {
		return competencyCourses;
	}
	
	public ArrayList<ScheduledCourse> getElectiveCourses() {
		return electiveCourses;
	}
	
	public ArrayList<ScheduledCourse> getRCCCourses() {
		return rccCourses;
	}
	
	public ArrayList<Student> getStudentList() {
		return studentList;
	}
	
	/**
	 * Returns the searched for ScheduledCourse based off its name and what type of course it is. 
	 * 
	 * @param name the name of the searched for course
	 * @param type the type of the searched for course
	 * @return
	 */ // TODO: change printing statements to reflect errors on GUI
	public ScheduledCourse findCourse(String name, char type) {
		ScheduledCourse course = null;
		if (type == 'i') {
			for (int i = 0; i < electiveCourses.size(); i++) {
				if (name.equals(electiveCourses.get(i).getCourse().getCourseCode())) {
					return electiveCourses.get(i);
				}
			}

			System.out.println("Could not find Elective course " + name);
			System.out.println("Please make sure preference sheet information is entered correctly.");
			System.out.println("View the read.me for more information.");
		} else if (type == 'e') {
			for (int i = 0; i < electiveCourses.size(); i++) {
				if (name.equals(electiveCourses.get(i).getCourse().getCourseCode())) {
					return electiveCourses.get(i);
				}
			}

			System.out.println("Could not find Elective course " + name);
			System.out.println("Please make sure preference sheet information is entered correctly.");
			System.out.println("View the read.me for more information.");
		} else {
			for (int i = 0; i < rccCourses.size(); i++) {
				if (name.equals(rccCourses.get(i).getCourse().getCourseCode())) {
					return rccCourses.get(i);
				}
			}

			System.out.println("Could not find RCC course " + name);
			System.out.println("Please make sure preference sheet information is entered correctly.");
			System.out.println("View the read.me for more information.");
		}
		
		// If course wasn't found, this should be null
		return course;
	}
}