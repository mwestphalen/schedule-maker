import java.util.ArrayList;
public class Driver {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Database data = new Database();
		data.generateIntroductoryCourses();
		data.generateElectiveCourses();
		data.generateRCCCourses();
		
		data.generateStudentList();
		ArrayList<Student> studentList = data.getStudentList();
		ArrayList<ScheduledCourse> introCourses = data.getIntroductoryCourses();
		
		for (int i = 0; i < studentList.size(); i++) {
			Student a = studentList.get(i);
			a.getStudentSchedule().addMajorCourse(data.getIntroductoryCourses(), a.getMajors());
			a.getStudentSchedule().addElectiveCourse(data.getElectiveCourses(), studentList.get(i).getElectivePreferences());
			a.getStudentSchedule().addRCCCourse(data.getRCCCourses(), studentList.get(i).getRCCPref());
		}
	
		for (int i = 0; i < studentList.size(); i++) {
			Schedule s1 = studentList.get(i).getStudentSchedule();
			//s1.addCourseCredits(); this wont work until we have all add methods working as will try to access null
			ScheduledCourse[] schList = s1.getScheduleList();
			System.out.println(studentList.get(i).getFirstName() + " " + studentList.get(i).getLastName());
			System.out.println("Major course: " + studentList.get(i).getStudentSchedule().getMajorCourse().getCourse().getCourseTitle());
			if (studentList.get(i).getStudentSchedule().getMajorCourse().getHasLab()) {
				System.out.println("Major course lab: " + studentList.get(i).getStudentSchedule().getMajorCourse().getCourseLab().getCourseTitle());
			}
			System.out.println("Elective course: " + studentList.get(i).getStudentSchedule().getElectiveCourse().getCourse().getCourseTitle());
			if (studentList.get(i).getStudentSchedule().getElectiveCourse().getHasLab()) {
				System.out.println("Elective course lab: " + studentList.get(i).getStudentSchedule().getElectiveCourse().getCourseLab().getCourseTitle());
			}
			System.out.println("RCC course: " + studentList.get(i).getStudentSchedule().getRCCCourse().getCourse().getCourseTitle());
			if (studentList.get(i).getStudentSchedule().getRCCCourse().getHasLab()) {
				System.out.println("RCC course lab: " + studentList.get(i).getStudentSchedule().getRCCCourse().getCourseLab().getCourseTitle());
			}
			System.out.println("Total Credits: " + s1.getTotalCredits());
		}
		System.out.println();
		System.out.println("Introductory Courses:");
		for (int i = 0; i < data.getIntroductoryCourses().size(); i++) {
			System.out.print(data.getIntroductoryCourses().get(i).getCourse().getCourseName());
			System.out.print(" " + data.getIntroductoryCourses().get(i).getCourse().getNumbStudents());
			System.out.println();
		}
		System.out.println();
		System.out.println("Elective Courses:");
		for (int i = 0; i < data.getElectiveCourses().size(); i++) {
			System.out.print(data.getElectiveCourses().get(i).getCourse().getCourseName());
			System.out.print(" " + data.getElectiveCourses().get(i).getCourse().getNumbStudents());
			System.out.println();
		}
		System.out.println();
		System.out.println("RCC Courses:");
		for(int i = 0; i < data.getRCCCourses().size(); i++) {
			System.out.print(data.getRCCCourses().get(i).getCourse().getCourseName());
			System.out.print(" " + data.getRCCCourses().get(i).getCourse().getNumbStudents());
			System.out.println();
		}
		 // show that course capacity has changed
	}

}
