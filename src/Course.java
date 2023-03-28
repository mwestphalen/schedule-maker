public class Course {
	private final int capacity;
	private final int CRN;
	private final String courseCode;
	private final String courseTitle;
	private final int credits;
	private final String courseMajor;
	private final String proficiency;
	private int numbStudents;
	
	public Course(int capacity1, int crn1, String courseCode1, String courseTitle1, int credits1, 
			String courseMajor1, String proficiency1) {
		capacity = capacity1;
		CRN = crn1;
		courseCode = courseCode1;
		courseTitle = courseTitle1;
		credits = credits1;
		courseMajor = courseMajor1;
		proficiency = proficiency1;
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
	
	public String getProficiency() {
		return proficiency;
	}
	
	public String getCourseCode() {
		return courseCode;
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
		System.out.print(CRN + " " + courseCode + " " + courseTitle + " " + credits);
	}
	
	public String[] getCourseInfo_XLSX() {
		return new String[] {String.valueOf(CRN), courseCode, courseTitle, String.valueOf(credits)};
		
	}
}
