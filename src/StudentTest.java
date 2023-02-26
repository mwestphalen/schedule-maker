import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

class StudentTest {

	@Test
	void testStudentNoArgsConstructor() {
		// Setting up for testing
		Student s = new Student();
		
		// Check that the call worked as it should
		assertNotNull(s);
	}
	
	@Test
	void testStudentStringConstructor() {
		// Setting up for testing
		ArrayList<ScheduledCourse> electivePref = new ArrayList<ScheduledCourse>();
		ArrayList<ScheduledCourse> rccPref = new ArrayList<ScheduledCourse>();
		
		/* TODO fill up arrays */
		
		Student s = new Student("Rochelle", "Elva", "R12345678", "CMS", "MAT", "SPN", "H", electivePref, rccPref);
				
		// Check that the call worked as it should
		assertNotNull(s);
	}
	
	@Test
	void testGetStudentSchedule() {
		/* TODO */
		
		
	}
	
	@Test
	void testGetRNumber() {
		// Setting up for testing
		ArrayList<ScheduledCourse> electivePref = new ArrayList<ScheduledCourse>();
		ArrayList<ScheduledCourse> rccPref = new ArrayList<ScheduledCourse>();
		Student s = new Student("Michael", "West", "R87654321", "PHY", "N", "N", "N", electivePref, rccPref);
		
		// Call to the method being tested
		String actual = s.getRNumber();
		
		// Check the call worked as it should
		assertEquals("R87654321", actual);
		
	}
}
	
	
