public class Course {
	private final int CRN;
	private final String courseSubject;
	//private final String courseNumber;
	private final String course;
	private final String courseTitle;
	private final int credits;
	private final int capacity;
	private int numbStudents;
	// Find way to store meetInfo other than string
	private final String meetingTime;
	private final String meetingDays;
	
	public Course(int crn, String subject, String name, int creds, String time, String days, int cap, 
			String title) {
		CRN = crn;
		courseSubject = subject;
		course = name;
		credits = creds;
		meetingTime = time;
		meetingDays = days;
		capacity = cap;
		numbStudents = 0;
		courseTitle = title;
	}
	
	public int getCRN() {
		return CRN;
	}
	
	public String getCourseSubject() {
		return courseSubject;
	}
	
//	public String getCourseNumber() {
//		return courseNumber;
//	}
	
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
	
	public String getMeetingTime() {
		return meetingTime;
	}
	
	public String getMeetingDays() {
		return meetingDays;
	}
	
	public void addStudent() {
		if (checkAvailability()) {
			numbStudents++;
		}
	}
	
	public boolean checkAvailability() {
		if (capacity > numbStudents) {
			return true;
		} else {
			return false;
		}
	}


}
