
import java.util.*;


public class Driver {
	
	public static void createDeque(ArrayList<Student> studentList, Deque<Student> courseDeque) {
		Student currentStudent;
		for (int i = 0; i < studentList.size(); i++) {
			currentStudent = studentList.get(i);
			if (!currentStudent.getStatus().equals("H") && !currentStudent.getStatus().equals("E")) {
				courseDeque.add(currentStudent);
			}
		}
		
		for (int i = 0; i < studentList.size(); i++) {
			currentStudent = studentList.get(i);
			if (currentStudent.getStatus().equals("H")) {
				courseDeque.addFirst(currentStudent);
			}
		}
		
		for (int i = 0; i < studentList.size(); i++) {
			currentStudent = studentList.get(i);
			if (currentStudent.getStatus().equals("E")) {
				courseDeque.addFirst(currentStudent);
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
			ArrayList<ScheduledCourse> rccPref = currentStudent.getRCCPreferences();
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
		//new GuiRemoveStudent();
		
		Database data = new Database();
		
		data.generateCourses('i');
		System.out.println("Introductory courses have been loaded.");
		data.generateCourses('c');
		System.out.println("Competency courses have been loaded.");
		data.generateCourses('e');
		System.out.println("Elective courses have been loaded.");
		data.generateCourses('r');
		System.out.println("RCC courses have been loaded.");
		
		data.generateStudentList();
		System.out.println("Students have been loaded.");
		
		data.enoughCoursesForStudents(data.getIntroductoryCourses(), data.getStudentList().size(), 'i');
		data.enoughCoursesForStudents(data.getCompetencyCourses(), data.getStudentList().size(), 'c');
		data.enoughCoursesForStudents(data.getElectiveCourses(), data.getStudentList().size(), 'e');
		data.enoughCoursesForStudents(data.getRCCCourses(), data.getStudentList().size(), 'r');
		
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
		System.out.println("All students have a introductory course added.");
		addStudentsToCompetencyCourses(compDeque, data.getCompetencyCourses());
		System.out.println("All students have a competency course added.");
		addStudentsToElectiveCourses(eleDeque, data.getElectiveCourses());
		System.out.println("All students have a elective course added.");
		addStudentsToRCCCourses(rccDeque, data.getRCCCourses());
		System.out.println("All students have a RCC course added.");
		
		// Use this to check if deque is working:
//		Iterator<Student> it = majorDeque.iterator();
//		 while (it.hasNext()) {
//			 System.out.println(it.next().getFirstName());
//		 }

		// Use this to check course information for system testing:
//		for (int i = 0; i < studentList.size(); i++) {
//      	Student s1 = studentList.get(i);
//           s1.getStudentSchedule().addCourseCredits();
//           System.out.println(s1.getFirstName() + " " + s1.getLastName());
//           System.out.println("Total Credits: " + s1.getStudentSchedule().getTotalCredits());
//           s1.printStudentSchedule();
//           System.out.println();
//		}
		
		// Print to TERMINAL
		for (int i = 0; i < studentList.size(); i++) { 
			Student s1 = studentList.get(i);
			s1.getStudentSchedule().addCourseCredits();
			System.out.println("#" + (i + 1) + " - " + s1.getFirstName() + " " + s1.getLastName());
			System.out.println("Total Credits: " + s1.getStudentSchedule().getTotalCredits());
			s1.printStudentSchedule_Terminal();
			System.out.println();
		}	
		
		// Print to XLSX File (one workbook w/ worksheets for each student... good)
		for (int i = 0; i < studentList.size(); i++) {
			studentList.get(i).printStudentSchedule_Excel(i + 1, studentList.size());
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
		System.out.println("Competency Courses:");
		for(int i = 0; i < data.getCompetencyCourses().size(); i++) {
			System.out.print(data.getCompetencyCourses().get(i).getCourse().getCourseCode());
			System.out.print(" " + data.getCompetencyCourses().get(i).getCourse().getNumbStudents());
			System.out.print(" / " + data.getCompetencyCourses().get(i).getCourse().getCapacity());
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
