import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
public class Database {
	private ArrayList<Course> electiveCourses = new ArrayList<Course>();
	private ArrayList<Course> introductoryCourses = new ArrayList<Course>();
	private ArrayList<Course> proficiencyCourses = new ArrayList<Course>();
	private ArrayList<Student> studentList = new ArrayList<Student>();
	//maybe an arraylist of students and their information, then in main go through the arraylist
	//calling on generate schedule for all of them
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
				String name = values[2];
				String title = values[3];
				int hours = Integer.parseInt(values[4]);
				String time = values[5];
				String days = values[6];
				String subject = values[7];
				electiveCourses.add(new Course(CRN, subject, name, hours, time, days, courseCapacity, title));
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
				String name = values[2];
				String title = values[3];
				int hours = Integer.parseInt(values[4]);
				String time = values[5];
				String days = values[6];
				String subject = values[7];
				introductoryCourses.add(new Course(CRN, subject, name, hours, time, days, courseCapacity, title));
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
	
	public ArrayList<Course> getIntroductoryCourses() {
		return introductoryCourses;
	}
	
//	public Course findMajorCourse(Student s) {
//		
//	}
	
//	public Course findElectiveCourse(Student s) {
//		
//	}
//	
//	public Course findProficiencyCourse(Student s) {
//		
//	}
}
