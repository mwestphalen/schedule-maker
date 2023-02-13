import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
public class Database {
	// Make the database a singleton
	private static ArrayList<ScheduledCourse> electiveCourses = new ArrayList<ScheduledCourse>();
	private static ArrayList<ScheduledCourse> introductoryCourses = new ArrayList<ScheduledCourse>();
	private static ArrayList<ScheduledCourse> competencyCourses = new ArrayList<ScheduledCourse>();
	private static ArrayList<ScheduledCourse> rccCourses = new ArrayList<ScheduledCourse>();
	private static ArrayList<Student> studentList = new ArrayList<Student>();
	// use : as delimiter as hour and minutes - for end time etc
	
	public Database() {
		 // Maybe make calls to all of the generate functions?
	}
	
	public void generateElectiveCourses() {
		try {
			File fileObj = new File("electives.txt");
			Scanner fileInput = new Scanner(fileObj);
			while (fileInput.hasNextLine()) {
				String line = fileInput.nextLine();
				String[] values = line.split(", ");
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
					electiveCourses.add(new ScheduledCourse(courseToAdd, courseTime, courseLabToAdd, courseLabTime));
				} else {
					electiveCourses.add(new ScheduledCourse(courseToAdd, courseTime));
				}
			}
			fileInput.close();
		} catch (FileNotFoundException exc) {
			System.out.println("An error occurred.");
			exc.printStackTrace();
		}
	}
	
	public void generateIntroductoryCourses() {
		try {
			File fileObj = new File("courses.txt");
			Scanner fileInput = new Scanner(fileObj);
			while (fileInput.hasNextLine()) {
				String line = fileInput.nextLine();
				String[] values = line.split(", ");
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
					introductoryCourses.add(new ScheduledCourse(courseToAdd, courseTime, courseLabToAdd, courseLabTime));
				} else {
					introductoryCourses.add(new ScheduledCourse(courseToAdd, courseTime));
				}
			}
			fileInput.close();
		} catch (FileNotFoundException exc) {
			System.out.println("An error occurred.");
			exc.printStackTrace();
		}
	}
	
	
	public void generateCompetencyCourses() {
		try {
			File fileObj = new File("competency.txt");
			Scanner fileInput = new Scanner(fileObj);
			while (fileInput.hasNextLine()) {
				String line = fileInput.nextLine();
				String[] values = line.split(", ");
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
					competencyCourses.add(new ScheduledCourse(courseToAdd, courseTime, courseLabToAdd, courseLabTime));
				} else {
					competencyCourses.add(new ScheduledCourse(courseToAdd, courseTime));
				}
			}
			fileInput.close();
		} catch (FileNotFoundException exc) {
			System.out.println("An error occurred.");
			exc.printStackTrace();
		}
	}
	
	public void generateRCCCourses() {
		try {
			File fileObj = new File("rccCourses.txt");
			Scanner fileInput = new Scanner(fileObj);
			while (fileInput.hasNextLine()) {
				String line = fileInput.nextLine();
				String[] values = line.split(", ");
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
					rccCourses.add(new ScheduledCourse(courseToAdd, courseTime, courseLabToAdd, courseLabTime));
				} else {
					rccCourses.add(new ScheduledCourse(courseToAdd, courseTime));
				}
			}
		} catch (FileNotFoundException exc) {
			System.out.println("An error occurred.");
			exc.printStackTrace();
		}
	}
	
	public void generateStudentList() {
		try {
			File fileObj = new File("students.txt");
			Scanner fileInput = new Scanner(fileObj);
			while (fileInput.hasNextLine()) {
				String line = fileInput.nextLine();
				String[] values = line.split(", ");
				String rNumber = values[0];
				String firstName = values[1];
				String lastName = values[2];
				String firstMajor = values[3];
				String secondMajor = values[4];
				String lang = values[5];
				String status = values[6];
				String line2 = fileInput.nextLine();
				String[] values2 = line2.split(", ");
				ArrayList<ScheduledCourse> electivePref = new ArrayList<ScheduledCourse>();
				for(int i = 0; i < 7; i++) {
					if (!values2[i].equals("N")) {
						// tests to see if they have an elective preference or not, if not don't add like if they have 3 preferences instead of 7
						ScheduledCourse c = findElectiveCourse(values2[i]);
						electivePref.add(c);
					}
				}
				
				String line3 = fileInput.nextLine();
				String[] values3 = line3.split(", ");
				ArrayList<ScheduledCourse> rccPref = new ArrayList<ScheduledCourse>();
				for(int i = 0; i < 7; i++) {
					if (!values3[i].equals("N")) {
						ScheduledCourse c = findRCCCourse(values3[i]);
						rccPref.add(c);
					}
				}
				studentList.add(new Student(firstName, lastName, rNumber, firstMajor, secondMajor, lang, status, electivePref, rccPref));
			}
			fileInput.close();
		} catch (FileNotFoundException exc) {
			System.out.println("An error occurred.");
			exc.printStackTrace();
		}
	}
	
	public ArrayList<ScheduledCourse> getElectiveCourses() {
		return electiveCourses;
	}
	
	public ArrayList<ScheduledCourse> getIntroductoryCourses() {
		return introductoryCourses;
	}
	
	public ArrayList<ScheduledCourse> getRCCCourses() {
		return rccCourses;
	}
	
	public ArrayList<ScheduledCourse> getCompetencyCourse() {
		return competencyCourses;
	}
	
	public ArrayList<Student> getStudentList() {
		return studentList;
	}
	
	public ScheduledCourse findElectiveCourse(String name) {
		for(int i = 0; i < electiveCourses.size(); i++) {
			if (name.equals(electiveCourses.get(i).getCourse().getCourseName())) {
				return electiveCourses.get(i);
			}
		}
		// This should never happen there should always be a course, if this does happen make it so it prints an error message and potentially quit the method or add random course?
			// -> This could happen if user enters or mistypes the elective course tiel (i.e. Ethivs instead of Ethics).
			// In that case, you're right... we have to print a message. But what if we check for the course CRN instead of 
			// its title? Sounds more unique and less error prone.
		return electiveCourses.get(1);
	}
	
	public ScheduledCourse findRCCCourse(String name) {
		// used to find the course in the student's course preferences for rcc.
		for(int i = 0; i < rccCourses.size(); i++) {
			if (name.equals(rccCourses.get(i).getCourse().getCourseName())) {
				return rccCourses.get(i);
			}
		}
		// This should never happen
		return rccCourses.get(1);
	}
	
	public ScheduledCourse findIntroductoryCourse(String name) {
		for(int i = 0; i < introductoryCourses.size(); i++) {
			if (name.equals(introductoryCourses.get(i).getCourse().getCourseName())) {
				return introductoryCourses.get(i);
			}
		}
		// This should never happen there should always be a course, if this does happen make it so it prints an error message and potentially quit the method or add random course?
		return introductoryCourses.get(1);
	}
	

}
