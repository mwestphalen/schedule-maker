
import java.util.*;


public class Driver {
	
	public static void createDeque(ArrayList<Student> studentList, Deque<Student> courseDeque) {
		Student currentStudent;
		for (int i = 0; i < studentList.size(); i++) {
			currentStudent = studentList.get(i);
			if (currentStudent.getStatus().equals("H") || currentStudent.getStatus().equals("E")) {
				courseDeque.addFirst(currentStudent);
			} else {
				courseDeque.add(currentStudent);
			}
		}
		return;
	}
	
	public static void addStudentsToIntroCourses(Deque<Student> courseDeque,  ArrayList<ScheduledCourse> courses) {
		Student currentStudent;
		int studentsNoPref = 0;
		while (!courseDeque.isEmpty()) {
			currentStudent = courseDeque.element();
			String[] majors = currentStudent.getMajors();
			if (majors[0].equals("N") && majors[1].equals("N")) {
				// this is used to put those without a preference for major at the end
				if (studentsNoPref >= courseDeque.size()) {
					currentStudent.getStudentSchedule().addIntroCourse(courses, currentStudent.getMajors());
					courseDeque.remove();
				} else {
					courseDeque.addLast(currentStudent);
					courseDeque.remove();
					studentsNoPref++;
				}
			} else {
				currentStudent.getStudentSchedule().addIntroCourse(courses, currentStudent.getMajors());
				courseDeque.remove();
			}	
		}
	}
	
	public static void addStudentsToCompetencyCourses(Deque<Student> courseDeque, ArrayList<ScheduledCourse> courses) {
		Student currentStudent;
		int studentsNoPref = 0;
		while (!courseDeque.isEmpty()) {
			currentStudent = courseDeque.element();
			String[] majors = currentStudent.getMajors();
			String lang = currentStudent.getLanguage();
			if (majors[0].equals("N") && majors[1].equals("N")) {
				// this is used to put those without a preference for major at the end
				if (studentsNoPref >= courseDeque.size()) {
					currentStudent.getStudentSchedule().addCompetencyCourse(courses, majors, lang);
					courseDeque.remove();
				} else {
					courseDeque.addLast(currentStudent);
					courseDeque.remove();
					studentsNoPref++;
				}
			} else {
				currentStudent.getStudentSchedule().addCompetencyCourse(courses, majors, lang);
				courseDeque.remove();
			}
		}
	}
	
	public static void addStudentsToElectiveCourses(Deque<Student> courseDeque, ArrayList<ScheduledCourse> courses) {
		Student currentStudent;
		int studentsNoPref = 0;
		while (!courseDeque.isEmpty()) {
			currentStudent = courseDeque.element();
			ArrayList<ScheduledCourse> electivePref = currentStudent.getElectivePreferences();
			if (electivePref.isEmpty()) {
				if (studentsNoPref >= courseDeque.size()) {
					currentStudent.getStudentSchedule().addElectiveCourse(courses, electivePref);
					courseDeque.remove();
				} else {
					courseDeque.addLast(currentStudent);
					courseDeque.remove();
					studentsNoPref++;
				}
			} else {
				currentStudent.getStudentSchedule().addElectiveCourse(courses, electivePref);
				courseDeque.remove();
			}
		}
	}
	
	public static void addStudentsToRCCCourses(Deque<Student> courseDeque, ArrayList<ScheduledCourse> courses) {
		Student currentStudent;
		int studentsNoPref = 0;
		while(!courseDeque.isEmpty()) {
			currentStudent = courseDeque.element();
			ArrayList<ScheduledCourse> rccPref = currentStudent.getRCCPref();
			if (rccPref.isEmpty()) {
				if (studentsNoPref >= courseDeque.size()) {
					currentStudent.getStudentSchedule().addRCCCourse(courses, rccPref);
					courseDeque.remove();
				} else {
					courseDeque.addLast(currentStudent);
					courseDeque.remove();
					studentsNoPref++;
				}
			} else {
				currentStudent.getStudentSchedule().addRCCCourse(courses, rccPref);
				courseDeque.remove();
			}
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Database data = new Database();
		// could use strings instead of letters to make it more clear
		data.generateCourses('i');
		data.generateCourses('c');
		data.generateCourses('e');
		data.generateCourses('r');
		
		data.generateStudentList();
		ArrayList<Student> studentList = data.getStudentList();
		
		Deque<Student> introDeque = new LinkedList<>();
		Deque<Student> compDeque = new LinkedList<>();
		Deque<Student> eleDeque = new LinkedList<>();
		Deque<Student> rccDeque = new LinkedList<>();
		
		createDeque(studentList, introDeque);
		createDeque(studentList, compDeque);
		createDeque(studentList, eleDeque);
		createDeque(studentList, rccDeque);
		
		addStudentsToIntroCourses(introDeque, data.getIntroductoryCourses());
		addStudentsToCompetencyCourses(compDeque, data.getCompetencyCourse());
		addStudentsToElectiveCourses(eleDeque, data.getElectiveCourses());
		addStudentsToRCCCourses(rccDeque, data.getRCCCourses());
		
		// here to make sure that the deque is in order and working
//		Iterator<Student> it = majorDeque.iterator();
//		 while (it.hasNext()) {
//			 System.out.println(it.next().getFirstName());
//		 }
		
//		for (int i = 0; i < studentList.size(); i++) {
//			Student a = studentList.get(i);
//			a.getStudentSchedule().addMajorCourse(data.getIntroductoryCourses(), a.getMajors());
//			a.getStudentSchedule().addElectiveCourse(data.getElectiveCourses(), studentList.get(i).getElectivePreferences());
//			a.getStudentSchedule().addRCCCourse(data.getRCCCourses(), studentList.get(i).getRCCPref());
//			a.getStudentSchedule().addCompetencyCourse(data.getCompetencyCourse(), a.getMajors(), a.getLanguage());
//		}
	
		
		// Print to TERMINAL
		for (int i = 0; i < studentList.size(); i++) { 
			Student s1 = studentList.get(i);
			s1.getStudentSchedule().addCourseCredits();
			System.out.println(s1.getFirstName() + " " + s1.getLastName());
			System.out.println("Total Credits: " + s1.getStudentSchedule().getTotalCredits());
			s1.printStudentSchedule_Terminal();
			System.out.println();
		}	
		
		// Print to XLSX File (one workbook w/ worksheets for each student... good)
		for (int i = 0; i < studentList.size(); i++) {
			studentList.get(i).printStudentSchedule_Excel(i);
		}
		
		System.out.println();
		System.out.println("Introductory Courses:");
		for (int i = 0; i < data.getIntroductoryCourses().size(); i++) {
			System.out.print(data.getIntroductoryCourses().get(i).getCourse().getCourseCode());
			System.out.print(" " + data.getIntroductoryCourses().get(i).getCourse().getNumbStudents());
			System.out.print(" / " + data.getIntroductoryCourses().get(i).getCourse().getCapacity());
			System.out.println();
		}
		System.out.println();
		System.out.println("Elective Courses:");
		for (int i = 0; i < data.getElectiveCourses().size(); i++) {
			System.out.print(data.getElectiveCourses().get(i).getCourse().getCourseCode());
			System.out.print(" " + data.getElectiveCourses().get(i).getCourse().getNumbStudents());
			System.out.print(" / " + data.getElectiveCourses().get(i).getCourse().getCapacity());
			System.out.println();
		}
		System.out.println();
		System.out.println("RCC Courses:");
		for(int i = 0; i < data.getRCCCourses().size(); i++) {
			System.out.print(data.getRCCCourses().get(i).getCourse().getCourseCode());
			System.out.print(" " + data.getRCCCourses().get(i).getCourse().getNumbStudents());
			System.out.print(" / " + data.getRCCCourses().get(i).getCourse().getCapacity());
			System.out.println();
		}
	}

}
