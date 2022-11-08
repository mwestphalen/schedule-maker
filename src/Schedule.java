
public class Schedule {
	private Course majorCourse;
	private Course electiveCourse;
	private Course competencyCourse;
	private Course rccCourse;
	private Course labCourse;
	private Course[] scheduleList = new Course[5];
	
	
	public Course getMajorCourse() {
		return majorCourse;
	}

	public void setMajorCourse(Course majorCourse) {
		this.majorCourse = majorCourse;
	}
	
	public void setLabCourse(Course c) {
		labCourse = c;
	}
	
	public Course getLabCourse() {
		return labCourse;
	}
	
	public Course[] getScheduleList() {
		return scheduleList;
	}

	public Course getElectiveCourse() {
		return electiveCourse;
	}

	public void setElectiveCourse(Course electiveCourse) {
		this.electiveCourse = electiveCourse;
	}

	public Course getCompetencyCourse() {
		return competencyCourse;
	}

	public void setCompetencyCourse(Course competencyCourse) {
		this.competencyCourse = competencyCourse;
	}

	public Course getRccCourse() {
		return rccCourse;
	}

	public void setRccCourse(Course rccCourse) {
		this.rccCourse = rccCourse;
	}

	
	public void addElectiveCourse() {
		
	}
	
	public void addCompetencyCourse() {
		
	}
	
	public boolean checkConflict(Course c) {
//		for (int i = 0; i < 4; i++) {
//			String meetDays1 = scheduleList[i].getMeetingDays();
//			String meetTime1 = scheduleList[i].getMeetingTime();
//			if (meetDays1.equals(c.getMeetingDays())) {
//				//find way to convert string of time so that it can be compared. If they do intercept return false.
//				return false;
//			}
//		}
		return true;
	}
}
