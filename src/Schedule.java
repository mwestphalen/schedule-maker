import java.time.*;
import java.util.ArrayList;
import java.util.Random;
public class Schedule {
	private ScheduledCourse[] scheduleList = new ScheduledCourse[4];
	private int totalCredits = 0;
	
	public void addMajorCourse(ArrayList<ScheduledCourse> introCourses, String[] majors) {
		// third iteration is for when both majors are full
		for (int i = 0; i < 3; i++) {
			String major = majors[i];
			if (major.equals("N") || i == 2) {
				boolean courseFound = false;
				while (!courseFound) {
					ScheduledCourse toAdd = getRandomCourse(introCourses);
					if (toAdd.getCourse().checkAvailability()) {
						if (checkConflict(toAdd)) {
							setMajorCourse(toAdd);
							toAdd.getCourse().addStudent();
							if (toAdd.getHasLab()) {
								toAdd.getCourseLab().addStudent();
							}
							return;
						}
					}
				}
			} else {
				for (int j = 0; j < introCourses.size(); j++) {
				// change this to make use of the find method
				ScheduledCourse toAdd = introCourses.get(j);
				if (major.equals(toAdd.getCourse().getCourseMajor())) {
					if (toAdd.getCourse().checkAvailability()) {
						if (checkConflict(toAdd)) {
							setMajorCourse(toAdd);
							toAdd.getCourse().addStudent();
							if (toAdd.getHasLab()) {
								toAdd.getCourseLab().addStudent();
							}
							return;
							}
						}
					}
				}
			}
		}
	}
	
	public void addElectiveCourse(ArrayList<ScheduledCourse> electiveCourses, ArrayList<ScheduledCourse> electivePref) {
		// check to see if preference is empty, if so add them to a random course
		if (electivePref.isEmpty()) {
			boolean courseFound = false;
			while (!courseFound) {
				ScheduledCourse toAdd = getRandomCourse(electiveCourses);
				if (toAdd.getCourse().checkAvailability()) {
					if (checkConflict(toAdd)) {
						setElectiveCourse(toAdd);
						toAdd.getCourse().addStudent();
						if (toAdd.getHasLab()) {
							toAdd.getCourseLab().addStudent();
						}
						courseFound = true;
					}
				}
			}
		} else {
			for (int i = 0; i < electivePref.size(); i++) {
			ScheduledCourse toAdd = electivePref.get(i);
			if (toAdd.getCourse().checkAvailability()) {
				if (checkConflict(toAdd)) {
					setElectiveCourse(toAdd);
					toAdd.getCourse().addStudent();
					if (toAdd.getHasLab()) {
						toAdd.getCourseLab().addStudent();
					}
					return;
				}
				}
			}
		}
		
	}
	
	public void addCompetencyCourse(ArrayList<ScheduledCourse> competencyCourses, String[] majors) {
		// do the same thing as add electice course but keeping in mine major
		// check the majors, see if the one selected has a competency course associated with major
		// if not or if they don't have a major add them to a general course.
	}
	
	public void addRCCCourse(ArrayList<ScheduledCourse> rccCourses, ArrayList<ScheduledCourse> rccPreferences) {
		
		if (rccPreferences.isEmpty()) {
			boolean courseFound = false;
			while (!courseFound) {
				ScheduledCourse toAdd = getRandomCourse(rccCourses);
				if (toAdd.getCourse().checkAvailability()) {
					if (checkConflict(toAdd)) {
						setRCCCourse(toAdd);
						toAdd.getCourse().addStudent();
						return;
					}
				}
			}
		} else {
			for (int i = 0; i < rccPreferences.size(); i++) {
				ScheduledCourse toAdd = rccPreferences.get(i);
				if (toAdd.getCourse().checkAvailability()) {
					if (checkConflict(toAdd)) {
						setRCCCourse(toAdd);
						toAdd.getCourse().addStudent();
						return;
				}
			}
		}
	}
	}
	
	public ScheduledCourse[] getScheduleList() {
		return scheduleList;
	}
	
	
	public ScheduledCourse getMajorCourse() {
		return scheduleList[0];
	}

	public void setMajorCourse(ScheduledCourse majorCourse) {
		scheduleList[0] = majorCourse;
	}
	
	public ScheduledCourse getCompetencyCourse() {
		return scheduleList[1];
	}

	public void setCompetencyCourse(ScheduledCourse competencyCourse) {
		scheduleList[1] = competencyCourse;
	}
	
	public ScheduledCourse getElectiveCourse() {
		return scheduleList[2];
	}
			
	public void setElectiveCourse(ScheduledCourse electiveCourse) {
		scheduleList[2] = electiveCourse;
	}

	public ScheduledCourse getRCCCourse() {
		return scheduleList[3];
	}
	
	public void setRCCCourse(ScheduledCourse rcc) {
		scheduleList[3] = rcc;
	}
	
	public int getTotalCredits() {
		return totalCredits;
	}
	
	public void setTotalCredits(int i) {
		totalCredits = i;
	}
	
	public void addCourseCredits() {
		// iterate through the schedule list adding up all the credits
		// this must be done after all courses have been added, if done before will crash
		for (int i = 0; i < 4; i++) {
			int creditsToAdd = scheduleList[i].getCourse().getCredits();
			if (scheduleList[i].getHasLab() == true) {
				creditsToAdd = creditsToAdd + scheduleList[i].getCourseLab().getCredits();
			}
			totalCredits = totalCredits + creditsToAdd;
		}
	}

	
	// break up this code to different methods to long
	public boolean checkConflict(ScheduledCourse c) {
		
		if (c.getHasLab()) {
			if (checkLabConflict(c.getLabTime()) == false) {
				return false;
			}
		}
		LocalTime toAddStart = c.getTime().getStartTime();
		LocalTime toAddEnd = c.getTime().getEndTime();
		String toAddDays = c.getTime().getMeetingDays();
		char toAddAmOrPm = c.getTime().getAmOrPm();
		for (int i = 0; i < 4; i++) {
			if (scheduleList[i] == null) {
				// checks if there is actually a class stored there
				continue;
			}
			LocalTime start = scheduleList[i].getTime().getStartTime();
			LocalTime end = scheduleList[i].getTime().getEndTime();
			String days = scheduleList[i].getTime().getMeetingDays();
			char amOrPm = scheduleList[i].getTime().getAmOrPm();
			// Make a check to see if they are on the same days, check if -1 length
			for (int j = 0; j < toAddDays.length(); j++) {
				for (int k = 0; k < days.length(); k++) {
					if (toAddDays.charAt(j) == days.charAt(k)) {
						// if days are the same then check if there are conflict
						if (toAddAmOrPm == amOrPm) {
							// check if in same time frame if not wont conflict?
							// if greater than parameter return positive, is equal returns 0, if less return negative
							int addStartVStart = toAddStart.compareTo(start);
							int addStartVEnd = toAddStart.compareTo(end);
							int addEndVStart = toAddEnd.compareTo(start);
							int addEndVEnd = toAddEnd.compareTo(end);
							if (addStartVStart > 0 && addStartVEnd <= 0) {
								// checks if start is in between course times ex: 1-2 to add vs 12-1:15
								return false;
							} else if (addEndVStart > 0 && addEndVEnd <= 0) {
								// checks if end time of course is between course times ex: 1-2:15 to add vs 2-3:15
								return false;
							} else if (addStartVStart == 0) {
								// checks if the course is at the same time ex: 1-2:15 to add vs 1-1:45
								return false;
							} else if (addEndVEnd == 0) {
								// checks if the course ends at the same time ex: 12-2:15 to add vs 1-2:15
								return false;
							} else if (addStartVStart < 0 && addEndVEnd > 0) {
								// checks if course to add goes over course already added ex: 12-3 to add vs 1-1:45
								return false;
							}
							
						}
						
					}
				}
			}
		}
		return true;
	}
	
	public boolean checkLabConflict(Time labTime) {
		for (int i = 0; i < 4; i++) {
			if (scheduleList[i] == null) {
				// checks if there is actually a class stored there
				continue;
			}
			LocalTime start = scheduleList[i].getTime().getStartTime();
			LocalTime end = scheduleList[i].getTime().getEndTime();
			String days = scheduleList[i].getTime().getMeetingDays();
			char amOrPm = scheduleList[i].getTime().getAmOrPm();
			// Make a check to see if they are on the same days, check if -1 length
			for (int j = 0; j < labTime.getMeetingDays().length(); j++) {
				for (int k = 0; k < days.length(); k++) {
					if (labTime.getMeetingDays().charAt(j) == days.charAt(k)) {
						// if days are the same then check if there are conflict
						if (labTime.getAmOrPm() == amOrPm) {
							// check if in same time frame if not wont conflict?
							// if greater than parameter return positive, is equal returns 0, if less return negative
							int addStartVStart = labTime.getStartTime().compareTo(start);
							int addStartVEnd = labTime.getStartTime().compareTo(end);
							int addEndVStart = labTime.getEndTime().compareTo(start);
							int addEndVEnd = labTime.getEndTime().compareTo(end);
							if (addStartVStart > 0 && addStartVEnd <= 0) {
								// checks if start is in between course times ex: 1-2 to add vs 12-1:15
								return false;
							} else if (addEndVStart > 0 && addEndVEnd <= 0) {
								// checks if end time of course is between course times ex: 1-2:15 to add vs 2-3:15
								return false;
							} else if (addStartVStart == 0) {
								// checks if the course is at the same time ex: 1-2:15 to add vs 1-1:45
								return false;
							} else if (addEndVEnd == 0) {
								// checks if the course ends at the same time ex: 12-2:15 to add vs 1-2:15
								return false;
							} else if (addStartVStart < 0 && addEndVEnd > 0) {
								// checks if course to add goes over course already added ex: 12-3 to add vs 1-1:45
								return false;
							}
							
						}
						
					}
				}
			}
		}
		
		return true;
	}
	
	// takes list of courses and selects a random course from it
	public ScheduledCourse getRandomCourse(ArrayList<ScheduledCourse> courses) {
		Random rand = new Random();
		int index = rand.nextInt(courses.size());
		ScheduledCourse selectedCourse = courses.get(index);
		return selectedCourse;
	}
}
