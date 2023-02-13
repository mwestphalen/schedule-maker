public class Course {
	private final int capacity;
	private final int CRN;
	private final String course;
	private final String courseTitle;
	private final int credits;
	private final String courseMajor;
	private final String competency;
	private int numbStudents;
	
	public Course(int capacity1, int crn1, String course1, String courseTitle1, int credits1, 
			String courseMajor1, String competency1) {
		capacity = capacity1;
		CRN = crn1;
		course = course1;
		courseTitle = courseTitle1;
		credits = credits1;
		courseMajor = courseMajor1;
		competency = competency1;
	}
	
	public int getCRN() {
		return CRN;
	}
	
	public String getCourseTitle() {
		return courseTitle;
	}
	
	public String getCourseMajor() {
		return courseMajor;
	}
	
	public String getCompetency() {
		return competency;
	}
	
	public String getCourseName() {
		return course;
	}
	
	public int getCredits() {
		return credits;
	}
	
	public int getCapacity() {
		return capacity;
	}
	
	public int getNumbStudents() {
		return numbStudents;
	}
	
	public void addStudent() {
		numbStudents++;
	}
	
	public boolean checkAvailability() {
		if (capacity > numbStudents) {
			return true;
		} else {
			return false;
		}
	}
	
	public void printCourseInfo() {
		System.out.print(CRN + " " + course + " " + courseTitle + " " + credits);
	}
}
