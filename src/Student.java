import java.util.ArrayList;
public class Student {
	private String rNumber;
	private String firstName;
	private String lastName;
	private String language;
	private String status;
	private Schedule schedule = new Schedule();
	private String[] majors = new String[2];
	private ArrayList<ScheduledCourse> electivePreferences = new ArrayList<ScheduledCourse>();
	private ArrayList<ScheduledCourse> rccPreferences = new ArrayList<ScheduledCourse>();
	
	public Student() {
		
	}

	public Student(String fName, String lName, String numb, String major1, String major2, String lang,
			String stat, ArrayList<ScheduledCourse> electivePref, ArrayList<ScheduledCourse> rccPref) {
		firstName = fName;
		lastName = lName;
		rNumber = numb;
		majors[0] = major1;
		majors[1] = major2;
		language = lang;
		status = stat;
		for(int i = 0; i < electivePref.size(); i++) {
			electivePreferences.add(electivePref.get(i));
		}
		for(int i = 0; i < rccPref.size(); i++) {
			rccPreferences.add(rccPref.get(i));
		}
	}
	
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
	
	public String getStatus() {
		return status;
	}
	
	public String[] getMajors() {
		return majors;
	}
	
	public ArrayList<ScheduledCourse> getElectivePreferences() {
		return electivePreferences;
	}
	
	public ArrayList<ScheduledCourse> getRCCPref() {
		return rccPreferences;
	}
	
	public String getLanguage() {
		return language;
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
	
	public void setLanguage(String lang) {
		language = lang;
	}
	
	public void setStatus(String stat) {
		status = stat;
	}

	public void printStudentSchedule() {
		System.out.println("Intro: ");
		try {
			schedule.getMajorCourse().getCourse().printCourseInfo();
			System.out.print(" ");
			schedule.getMajorCourse().getTime().printTime();
		} catch (Exception e){
			
		}
		System.out.println();
		System.out.println("Elective: ");
		try {
			schedule.getElectiveCourse().getCourse().printCourseInfo();
			System.out.print(" ");
			schedule.getElectiveCourse().getTime().printTime();
		} catch (Exception e) {
			
		}
		System.out.println();
		System.out.println("RCC: ");
		try {
			schedule.getRCCCourse().getCourse().printCourseInfo();
			System.out.print(" ");
			schedule.getRCCCourse().getTime().printTime();
		} catch (Exception e) {
			
		}
		System.out.println();
		System.out.println("Competency: ");
		try {
			schedule.getCompetencyCourse().getCourse().printCourseInfo();
			System.out.print(" ");
			schedule.getCompetencyCourse().getTime().printTime();
		} catch (Exception e) {
			
		}
		System.out.println();
	}

}
