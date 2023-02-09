import java.util.*;
public class Driver {
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Database data = new Database();
		data.generateIntroductoryCourses();
		data.generateElectiveCourses();
		data.generateRCCCourses();
		data.generateCompetencyCourses();
		
		data.generateStudentList();
		ArrayList<Student> studentList = data.getStudentList();
		
		Deque<Student> majorDeque = new LinkedList<>();
		Deque<Student> compDeque = new LinkedList<>();
		Deque<Student> eleDeque = new LinkedList<>();
		Deque<Student> rccDeque = new LinkedList<>();
		Student currentStudent;
		
		for (int i = 0; i < studentList.size(); i++) {
			currentStudent = studentList.get(i);
			if (currentStudent.getStatus().equals("H") || currentStudent.getStatus().equals("E")) {
				majorDeque.addFirst(studentList.get(i));
				compDeque.addFirst(studentList.get(i));
				eleDeque.addFirst(studentList.get(i));
				rccDeque.addFirst(studentList.get(i));
			} else {
				majorDeque.add(studentList.get(i));
				compDeque.add(studentList.get(i));
				eleDeque.add(studentList.get(i));
				rccDeque.add(studentList.get(i));
			}
		}
		
		int studentsNoPref = 0;
		
		while (!majorDeque.isEmpty()) {
			currentStudent = majorDeque.element();
			String[] majors = currentStudent.getMajors();
			if (majors[0].equals("N") && majors[1].equals("N")) {
				// this is used to put those without a preference for major at the end
				if (studentsNoPref >= majorDeque.size()) {
					currentStudent.getStudentSchedule().addMajorCourse(data.getIntroductoryCourses(), currentStudent.getMajors());
					majorDeque.remove();
				} else {
					majorDeque.addLast(currentStudent);
					majorDeque.remove();
					studentsNoPref++;
				}
			} else {
				currentStudent.getStudentSchedule().addMajorCourse(data.getIntroductoryCourses(), currentStudent.getMajors());
				majorDeque.remove();
			}
			
		}
		
		studentsNoPref = 0;
		while (!compDeque.isEmpty()) {
			currentStudent = compDeque.element();
			String[] majors = currentStudent.getMajors();
			String lang = currentStudent.getLanguage();
			if (majors[0].equals("N") && majors[1].equals("N")) {
				// this is used to put those without a preference for major at the end, ask
				// if what she thinks about language are they higher priority to place as in the
				// students who want to take it
				if (studentsNoPref >= compDeque.size()) {
					currentStudent.getStudentSchedule().addCompetencyCourse(data.getCompetencyCourse(), majors, lang);
					compDeque.remove();
				} else {
					compDeque.addLast(currentStudent);
					compDeque.remove();
					studentsNoPref++;
				}
			} else {
				currentStudent.getStudentSchedule().addCompetencyCourse(data.getCompetencyCourse(), majors, lang);
				compDeque.remove();
			}
			
		}
		
		studentsNoPref = 0;
		while (!eleDeque.isEmpty()) {
			currentStudent = eleDeque.element();
			ArrayList<ScheduledCourse> electivePref = currentStudent.getElectivePreferences();
			if (electivePref.isEmpty()) {
				if (studentsNoPref >= eleDeque.size()) {
					currentStudent.getStudentSchedule().addElectiveCourse(data.getElectiveCourses(), electivePref);
					eleDeque.remove();
				} else {
					eleDeque.addLast(currentStudent);
					eleDeque.remove();
					studentsNoPref++;
				}
			} else {
				currentStudent.getStudentSchedule().addElectiveCourse(data.getElectiveCourses(), electivePref);
				eleDeque.remove();
			}
		}
		
		studentsNoPref = 0;
		while(!rccDeque.isEmpty()) {
			currentStudent = rccDeque.element();
			ArrayList<ScheduledCourse> rccPref = currentStudent.getRCCPref();
			if (rccPref.isEmpty()) {
				if (studentsNoPref >= rccDeque.size()) {
					currentStudent.getStudentSchedule().addRCCCourse(data.getRCCCourses(), rccPref);
					rccDeque.remove();
				} else {
					rccDeque.addLast(currentStudent);
					rccDeque.remove();
					studentsNoPref++;
				}
			} else {
				currentStudent.getStudentSchedule().addRCCCourse(data.getRCCCourses(), rccPref);
				rccDeque.remove();
			}
		}
		
		
//		for (int i = 0; i < studentList.size(); i++) {
//			Student a = studentList.get(i);
//			a.getStudentSchedule().addMajorCourse(data.getIntroductoryCourses(), a.getMajors());
//			a.getStudentSchedule().addElectiveCourse(data.getElectiveCourses(), studentList.get(i).getElectivePreferences());
//			a.getStudentSchedule().addRCCCourse(data.getRCCCourses(), studentList.get(i).getRCCPref());
//			a.getStudentSchedule().addCompetencyCourse(data.getCompetencyCourse(), a.getMajors(), a.getLanguage());
//		}
	
		for (int i = 0; i < studentList.size(); i++) {
			Student s1 = studentList.get(i);
			s1.getStudentSchedule().addCourseCredits();
			System.out.println(s1.getFirstName() + " " + s1.getLastName());
			System.out.println("Total Credits: " + s1.getStudentSchedule().getTotalCredits());
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
