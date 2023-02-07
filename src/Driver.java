import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;
public class Driver {
	
	public Queue<Student> generateStudentQueue(ArrayList<Student> studentList) {
		Queue<Student> studentQueue = new LinkedList<>();
		for (int i = 0; i < studentList.size(); i++) {
			studentQueue.add(studentList.get(i));
		}
		return studentQueue;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Database data = new Database();
		data.generateIntroductoryCourses();
		data.generateElectiveCourses();
		data.generateRCCCourses();
		data.generateCompetencyCourses();
		
		data.generateStudentList();
		ArrayList<Student> studentList = data.getStudentList();
		
		//Queue<Student> majorQueue = generateStudentQueue(data.getStudentList());
		
		//while(!majorQueue.isEmpty()) {
			//Student currentStudent = majorQueue.element();
			//String[] majors = currentStudent.getMajors();
			//if (majors[0].equals("N") && majors[1].equals("N")) {
				// then remove from front and put to back
			//}
		//}
		
		for (int i = 0; i < studentList.size(); i++) {
			Student a = studentList.get(i);
			a.getStudentSchedule().addMajorCourse(data.getIntroductoryCourses(), a.getMajors());
			a.getStudentSchedule().addElectiveCourse(data.getElectiveCourses(), studentList.get(i).getElectivePreferences());
			a.getStudentSchedule().addRCCCourse(data.getRCCCourses(), studentList.get(i).getRCCPref());
		}
	
		for (int i = 0; i < studentList.size(); i++) {
			Student s1 = studentList.get(i);
			//s1.addCourseCredits(); this wont work until we have all add methods working as will try to access null
			System.out.println(s1.getFirstName() + " " + s1.getLastName());
			s1.printStudentSchedule();
			System.out.println();
			
		}
		System.out.println();
		System.out.println("Introductory Courses:");
		for (int i = 0; i < data.getIntroductoryCourses().size(); i++) {
			System.out.print(data.getIntroductoryCourses().get(i).getCourse().getCourseName());
			System.out.print(" " + data.getIntroductoryCourses().get(i).getCourse().getNumbStudents());
			System.out.print(" / " + data.getIntroductoryCourses().get(i).getCourse().getCapacity());
			System.out.println();
		}
		System.out.println();
		System.out.println("Elective Courses:");
		for (int i = 0; i < data.getElectiveCourses().size(); i++) {
			System.out.print(data.getElectiveCourses().get(i).getCourse().getCourseName());
			System.out.print(" " + data.getElectiveCourses().get(i).getCourse().getNumbStudents());
			System.out.print(" / " + data.getElectiveCourses().get(i).getCourse().getCapacity());
			System.out.println();
		}
		System.out.println();
		System.out.println("RCC Courses:");
		for(int i = 0; i < data.getRCCCourses().size(); i++) {
			System.out.print(data.getRCCCourses().get(i).getCourse().getCourseName());
			System.out.print(" " + data.getRCCCourses().get(i).getCourse().getNumbStudents());
			System.out.print(" / " + data.getRCCCourses().get(i).getCourse().getCapacity());
			System.out.println();
		}
	}

}
