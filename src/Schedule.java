
public class Schedule {
	private Course majorCourse;
	private Course electiveCourse;
	private Course competencyCourse;
	private Course rccCourse;
	
	
	
	
	public Course getMajorCourse() {
		return majorCourse;
	}

	public void setMajorCourse(Course majorCourse) {
		this.majorCourse = majorCourse;
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

	public void addMajorCourse() {
		
	}
	
	public void addElectiveCourse() {
		
	}
	
	public void addCompetencyCourse() {
		
	}
	
	public boolean checkConflict(Course c) {
		return false;
	}
}
