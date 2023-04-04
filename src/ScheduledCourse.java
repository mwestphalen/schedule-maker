
public class ScheduledCourse {
	
	private Course course;
	private Course courseLab;
	private Time time;
	private Time labTime;
	private final boolean hasLab;	
	
	public ScheduledCourse(Course c, Time t) {
		course = c;
		time = t;
		hasLab = false;
	}
	
	public ScheduledCourse(Course c, Time t, Course cl, Time lt) {
		course = c;
		courseLab = cl;
		time = t;
		labTime = lt;
		hasLab = true;	
	}

	public Course getCourse() {
		return course;
	}
	
	public Course getCourseLab() {
		return courseLab;
	}

	public Time getTime() {
		return time;
	}
	
	public Time getLabTime() {
		return labTime;
	}

	public void setTime(Time time) {
		this.time = time;
	}
	
	public void setLabTime(Time time) {
		labTime = time;
	}
	public void setCourse(Course course) {
		this.course = course;
	}
	
	public void setCourseLab(Course course) {
		courseLab = course;
	}
	
	public boolean getHasLab() {
		return hasLab;
	}
}