import java.time.*;
import java.util.ArrayList;
import java.util.Random;
public class Schedule {
	private ScheduledCourse[] scheduleList = new ScheduledCourse[4];
	private int totalCredits = 0;
	
	public void addIntroCourse(ArrayList<ScheduledCourse> introCourses, String[] majors) {
		// third iteration is for when both majors are full
		String major = "N";
		try {
			for (int i = 0; i < 3; i++) {
				if (i != 2) {
					major = majors[i];
				}
				if (major.equals("N") || i == 2) {
					boolean courseFound = false;
					while (!courseFound) {
						ScheduledCourse toAdd = getRandomCourse(introCourses);
						if (addCourse(toAdd, "M")) {
							return;
						}
					}
				} else {
					for (int j = 0; j < introCourses.size(); j++) {
						// change this to make use of the find method
						ScheduledCourse toAdd = introCourses.get(j);
						if (major.equals(toAdd.getCourse().getCourseMajor())) {
							if (addCourse(toAdd, "M")) {
								return;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Error: When retrieving introductory course to add to schedule.");
			System.out.println("Please make sure that there are enough courses for students");
			System.exit(0);
		}
	}
	
	public void addElectiveCourse(ArrayList<ScheduledCourse> electiveCourses, ArrayList<ScheduledCourse> electivePref) {
		
		if (!electivePref.isEmpty()) {
			for (int i = 0; i < electivePref.size(); i++) {
				ScheduledCourse toAdd = electivePref.get(i);
				if (addCourse(toAdd, "E")) {
					return;
				}
			}
		}
		boolean courseFound = false;
		while (!courseFound) {
			ScheduledCourse toAdd = getRandomCourse(electiveCourses);
			if (addCourse(toAdd, "E")) {
				return;
			}
		}
	}
	
	//COMPETENCY TAKES WAAAAAAAAAAAAAAY TO LONG, FIND A WAY TO SPEED IT UP AND REDUCE LOAD
	public void addCompetencyCourse(ArrayList<ScheduledCourse> competencyCourses, String[] majors, String lang) {
		
		ScheduledCourse toAdd;
		// if student has a language preference priority is put on getting them into that course
		// could cause problems with not recognizing language preference. VALIDATE
		if (!lang.equals("N")) {
			ArrayList<ScheduledCourse> languageCourses = new ArrayList<ScheduledCourse>();
			for (int i = 0; i < competencyCourses.size(); i++) {
				if (lang.equals(competencyCourses.get(i).getCourse().getCourseMajor())) {
					languageCourses.add(competencyCourses.get(i));
				}
			}
			for (int i = 0; i < languageCourses.size(); i++) {
				toAdd = languageCourses.get(i);
				if (addCourse(toAdd, "C")) {
					return;
				}
			}
		}
		// if student does not have a lang preference or it is full see what competency if good for
		// first major
		for (int i = 0; i < 2; i++) {
			String major = majors[i];
			if (!major.equals("N")) {
				if (major.equals("BUS") || major.equals("SE")) {
					for (int j = 0; j < competencyCourses.size(); j++) {
						toAdd = competencyCourses.get(i);
						String competency = toAdd.getCourse().getProficiency();
						if (!competency.equals("ECMP") && !competency.equals("MCMP")) {
							// this below should be made into its on method
							if (addCourse(toAdd, "C")) {
								return;
							}
						}
					}
				} else if (major.equals("PHI")) {
					for (int j = 0; j < competencyCourses.size(); j++) {
						toAdd = competencyCourses.get(i);
						String competency = toAdd.getCourse().getProficiency();
						if (!competency.equals("ECMP")) {
							// this below should be made into its on method
							if (addCourse(toAdd, "C")) {
								return;
							}
						}
					}
				} else if (major.equals("ENG")) {
					for (int j = 0; j < competencyCourses.size(); j++) {
						toAdd = competencyCourses.get(i);
						String competency = toAdd.getCourse().getProficiency();
						if (!competency.equals("WCMP")) {
							// this below should be made into its on method
							if (addCourse(toAdd, "C")) {
								return;
							}
						}
					}
				} else if (major.equals("INB")) {
					for (int j = 0; j < competencyCourses.size(); j++) {
						toAdd = competencyCourses.get(i);
						String competency = toAdd.getCourse().getProficiency();
						if (!competency.equals("ECMP") && !competency.equals("MCMP") && !competency.equals("FCMP")) {
							// this below should be made into its on method
							if (addCourse(toAdd, "C")) {
								return;
							}
						}
					}
				} else if (major.equals("MAT") || major.equals("CHM") || major.equals("CMS") || major.equals("ECO")
					|| major.equals("PHY") || major.equals("POL") || major.equals("PSY") || major.equals("PPE")
					|| major.equals("SOC")) {
					for (int j = 0; j < competencyCourses.size(); j++) {
						toAdd = competencyCourses.get(i);
						String competency = toAdd.getCourse().getProficiency();
						if (!competency.equals("MCMP")) {
							// this below should be made into its on method
							if (addCourse(toAdd, "C")) {
								return;
							}
						}
					}
				}
			}
		}
		
		// makes list of all English 140 courses
		ArrayList<ScheduledCourse> english140Courses = new ArrayList<ScheduledCourse>();
		for (int i = 0; i < competencyCourses.size(); i++) {
			if(competencyCourses.get(i).getCourse().getCourseCode().contains("ENGW 140")) {
				english140Courses.add(competencyCourses.get(i));
			}
		}
		
		// checks to see if student can be added to an English 140 course
		for (int i = 0; i < english140Courses.size(); i++) {
			toAdd = english140Courses.get(i);
			if (addCourse(toAdd, "C")) {
				return;
			}
		}
		
		// if all fails, add student to a random competency course
		boolean courseFound = false;
		while (!courseFound) {
			toAdd = getRandomCourse(competencyCourses);
			if (addCourse(toAdd, "C")) {
				return;
			}
		}
	}

	
	public void addRCCCourse(ArrayList<ScheduledCourse> rccCourses, ArrayList<ScheduledCourse> rccPreferences) {
		
		try {
			if (!rccPreferences.isEmpty()) {
				for (int i = 0; i < rccPreferences.size(); i++) {
					ScheduledCourse toAdd = rccPreferences.get(i);
					if (addCourse(toAdd, "R")) {
						return;
					}
					}
				}
			// this adds a random method, if there is an empty list or none of the courses listed are available
			boolean courseFound = false;
			while (!courseFound) {
				ScheduledCourse toAdd = getRandomCourse(rccCourses);
			if (addCourse(toAdd, "R")) {
				return;
			}
			}
		} catch (Exception e) {
			System.out.println("Error: When retrieving RCC course course to add to schedule.");
			System.out.println("Please make sure that there are enough courses for students");
			System.exit(0);
		}
//		if (!rccPreferences.isEmpty()) {
//			for (int i = 0; i < rccPreferences.size(); i++) {
//				ScheduledCourse toAdd = rccPreferences.get(i);
//				if (addCourse(toAdd, "R")) {
//					return;
//				}
//				}
//			}
//		// this adds a random method, if there is an empty list or none of the courses listed are available
//		boolean courseFound = false;
//		while (!courseFound) {
//			ScheduledCourse toAdd = getRandomCourse(rccCourses);
//			if (addCourse(toAdd, "R")) {
//				return;
//			}
//		}
	}
	
	public ScheduledCourse[] getScheduleList() {
		return scheduleList;
	}
	
	
	public ScheduledCourse getMajorCourse() {
		return scheduleList[0];
	}

	public void setIntroCourse(ScheduledCourse introCourse) {
		scheduleList[0] = introCourse;
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
		int creditsToAdd;
		for (int i = 0; i < 4; i++) {
			creditsToAdd = 0;
			try {
				scheduleList[i].getCourse();
				creditsToAdd = scheduleList[i].getCourse().getCredits();
				if (scheduleList[i].getHasLab() == true) {
					creditsToAdd = creditsToAdd + scheduleList[i].getCourseLab().getCredits();
				}
			} catch (Exception e) {
				
			}
			totalCredits = totalCredits + creditsToAdd;
		}
	}
	
	public boolean addCourse(ScheduledCourse toAdd, String courseType) {
		// M is for major, C is for competency, E is for elective, and R for RCC
		if (toAdd.getCourse().checkAvailability()) {
			if (checkConflict(toAdd)) {
				if (courseType.equals("M")) {
					setIntroCourse(toAdd);
				} else if (courseType.equals("E")) {
					setElectiveCourse(toAdd);
				} else if (courseType.equals("C")) {
					setCompetencyCourse(toAdd);
				} else {
					setRCCCourse(toAdd);
				}
				toAdd.getCourse().addStudent();
				if (toAdd.getHasLab()) {
					toAdd.getCourseLab().addStudent();
				}
				return true;
				}
			}
		return false;
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
	public static ScheduledCourse getRandomCourse(ArrayList<ScheduledCourse> courses) {
		Random rand = new Random();
		int index = rand.nextInt(courses.size());
		ScheduledCourse selectedCourse = courses.get(index);
		return selectedCourse;
	}
}
