import java.util.HashMap;
import java.util.ArrayList;
public class Student {
	private String rNumber;
	private String firstName;
	private String lastName;
	private Schedule schedule = new Schedule();
	private int totalCredits;
	private String[] majors = new String[2];
	// later change this to four for RCC course
	// dont have this set in final version as they can have 5 total with lab
	//May be to expensive find an alternative
	HashMap<Integer, Course> electivePreferences = new HashMap<Integer, Course>();
	
	// maybe have rNumber be an int, and not include the R in it.
	// schedule can only be made when these pieces of info are provided
	public Student() {
		
	}
	// determine with or not o have this, force these things to be included or not
	public Student(String fName, String lName, String numb, String major1, String major2) {
		firstName = fName;
		lastName = lName;
		rNumber = numb;
		totalCredits = 0;
		majors[0] = major1;
		majors[1] = major2;
	}
	
	public void addMajorCourse(ArrayList<Course> introCourses) {
		for (int i = 0; i < 2; i++) {
			String major = majors[i];
			if (major.equals("N")) {
				for (int j = 0; j < introCourses.size(); j++) {
					Course toAdd = introCourses.get(j);
					if (toAdd.checkAvailability()) {
						if (schedule.checkConflict(toAdd)) {
							if (toAdd.getHasLab()) {
								Course toAddLab = introCourses.get(j + 1);
								if (schedule.checkConflict(toAddLab)) {
									schedule.setLabCourse(toAddLab);
									toAddLab.addStudent();
									schedule.setMajorCourse(toAdd);
									toAdd.addStudent();
									return;
								}
							} else {
								schedule.setMajorCourse(toAdd);
								toAdd.addStudent();
								return;
							}
						}
					}
				}
			}
			for (int j = 0; j < introCourses.size(); j++) {
				Course toAdd = introCourses.get(j);
				if (major.equals(toAdd.getCourseMajor())) {
					if (toAdd.checkAvailability()) {
						if (schedule.checkConflict(toAdd)) {
							if (toAdd.getHasLab()) {
								Course toAddLab = introCourses.get(j + 1);
								if (schedule.checkConflict(toAddLab)) {
									schedule.setLabCourse(toAddLab);
									toAddLab.addStudent();
									schedule.setMajorCourse(toAdd);
									toAdd.addStudent();
									return;
								}
							} else {
								schedule.setMajorCourse(toAdd);
								toAdd.addStudent();
								return;
							}
						}
					}
				}
			}
		}
	}
						
					
		

//	
//	public String getElectiveCourse() {
//		return electiveCourse;
//	}
//	
//	public String getCompetencyCourse() {
//		return competencyCourse;
//	}
	
	public Schedule getStudentSchedule() {
		return schedule;
	}
	
	public String getRNumber() {
		return rNumber;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public String[] getMajors() {
		return majors;
	}
	
	public int getTotalCredits() {
		return totalCredits;
	}
	
	public void setRNumber(String num) {
		rNumber = num;
	}
	
	public void setFirstName(String name) {
		firstName = name;
	}
	
	public void setLastName(String name) {
		lastName = name;
	}
	// make something so that these three methods are only called once, or make them part of another larger method
	// called generateSchedule()
	
	
	public void addElectivePrefCourse(int position, Course elective) {
		electivePreferences.put(position, elective);
	}
	
	public void printStudentSchedule() {
		System.out.println(schedule.getMajorCourse());
		System.out.println(schedule.getCompetencyCourse());
		System.out.println(schedule.getRccCourse());
		System.out.println(schedule.getElectiveCourse());
	}
	
	
	 // maybe make a return method that returns an array of all information
	// do the thing so that you can only call the add course once maybe?
	// ask henning what info she wants regarding a course. Like its CRN, name, teacher?
	// if she wants more than just name than make seperate classes for each one with data that holds those things

}
