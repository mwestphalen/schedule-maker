import java.time.*;
public class Time {

	private String meetingDays;
	private LocalTime startTime;
	private LocalTime endTime;
	private char amOrPm;
	
	public Time() {
		
	}
	
	public Time(String meetingTime1, String meetingDays1) {
		setMeetingTimes(meetingTime1);
		setMeetingDays(meetingDays1);
	}
	
	public String getMeetingDays() {
		return meetingDays;
	}
	
	public LocalTime getStartTime() {
		return startTime;
	}
	
	public LocalTime getEndTime() {
		return endTime;
	}
	
	public char getAmOrPm() {
		return amOrPm;
	}
	
	public String[] getTimeInfo_XLSX() {
		return new String[] {meetingDays, startTime.toString() + "-" + endTime.toString() + String.valueOf(amOrPm)};
	}
	
	public void setMeetingDays(String meetingDays1) {
		meetingDays = meetingDays1;
	}
	
	public void setMeetingTimes(String meetingInformation) {
		String[] values = meetingInformation.split("-");
		String[] firstTimes= values[0].split(":");
		String[] secondTimes = values[1].split(":");
		int firstHour = Integer.parseInt(firstTimes[0]);
		int firstMinutes = Integer.parseInt(firstTimes[1]);
		int secondHour = Integer.parseInt(secondTimes[0]);
		int secondMinutes = Integer.parseInt(secondTimes[1]);
		startTime = LocalTime.of(firstHour, firstMinutes);
		endTime = LocalTime.of(secondHour, secondMinutes);
		amOrPm = values[2].charAt(0);
	}
	
	public void printTime() {
		System.out.print(startTime + "-" + endTime + "-" + amOrPm + " " + meetingDays);
	}
}
