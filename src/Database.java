import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
public class Database {
	// Make the database a singleton
	private static ArrayList<Course> electiveCourses = new ArrayList<Course>();
	private static ArrayList<Course> introductoryCourses = new ArrayList<Course>();
	private static ArrayList<Course> proficiencyCourses = new ArrayList<Course>();
	private static ArrayList<Student> studentList = new ArrayList<Student>();
	
	
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
				electiveCourses.add(new Course(courseCapacity, CRN, course, courseTitle, credits, meetingTime, meetingDays, courseMajor, proficiency, hasLab));
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
				introductoryCourses.add(new Course(courseCapacity, CRN, course, courseTitle, credits, meetingTime, meetingDays, courseMajor, proficiency, hasLab));
			}
			fileInput.close();
		} catch (FileNotFoundException exc) {
			System.out.println("An error occurred.");
			exc.printStackTrace();
		}
	}
	
	
	public void generateProficiencyCourses() {
		
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
				studentList.add(new Student(firstName, lastName, rNumber, firstMajor, secondMajor));
				//Figure out how preference sheets will be read in
			}
			fileInput.close();
		} catch (FileNotFoundException exc) {
			System.out.println("An error occurred.");
			exc.printStackTrace();
		}
	}
	
	public ArrayList<Course> getElectiveCourses() {
		return electiveCourses;
	}
	
	public ArrayList<Course> getIntroductoryCourses() {
		return introductoryCourses;
	}
	
	public ArrayList<Course> getProficiencyCourse() {
		return proficiencyCourses;
	}
	
	public ArrayList<Student> getStudentList() {
		return studentList;
	}
	

}
