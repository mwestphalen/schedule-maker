import java.util.ArrayList;
public class Student {
	private String rNumber;
	private String firstName;
	private String lastName;
	private Schedule schedule = new Schedule();
	private String[] majors = new String[2];
	private ArrayList<ScheduledCourse> electivePreferences = new ArrayList<ScheduledCourse>();
	private ArrayList<ScheduledCourse> rccPreferences = new ArrayList<ScheduledCourse>();
	
	public Student() {
		
	}

	public Student(String numb, String fName, String lName, String major1, String major2, ArrayList<ScheduledCourse> electivePref
			, ArrayList<ScheduledCourse> rccPref) {
		rNumber = numb;
		firstName = fName;
		lastName = lName;
		majors[0] = major1;
		majors[1] = major2;
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
	
	
	public String[] getMajors() {
		return majors;
	}
	
	public ArrayList<ScheduledCourse> getElectivePreferences() {
		return electivePreferences;
	}
	
	public ArrayList<ScheduledCourse> getRCCPref() {
		return rccPreferences;
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

	public void printStudentSchedule() {
		System.out.println(schedule.getMajorCourse());
		System.out.println(schedule.getCompetencyCourse());
		System.out.println(schedule.getRCCCourse());
		System.out.println(schedule.getElectiveCourse());
	}

}
