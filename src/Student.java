// For EXCEL output
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.groupdocs.conversion.*;


import java.util.ArrayList;

public class Student {
	private String rNumber;
	private String firstName;
	private String lastName;
	private String language;
	private String status;
	private Schedule schedule = new Schedule();
	private String[] majors = new String[2];
	private ArrayList<ScheduledCourse> electivePreferences = new ArrayList<ScheduledCourse>();
	private ArrayList<ScheduledCourse> rccPreferences = new ArrayList<ScheduledCourse>();
	
	// Create blank workbook
	private static XSSFWorkbook workbook;
	private static FileInputStream excelTemplate;
	static {
		
		  // Load Excel Template for use with Apache POI 
		try { excelTemplate = new FileInputStream("schedule_template.xlsx"); 
		
		} catch (FileNotFoundException e) { 
			e.printStackTrace(); 
		
		} 
		try { workbook = new XSSFWorkbook(excelTemplate); 
		
		} catch (IOException e) {
			e.printStackTrace();
		}
		 

		
	}
	public Student() {
		
	}

	public Student(String fName, String lName, String numb, String major1, String major2, String lang,
			String stat, ArrayList<ScheduledCourse> electivePref, ArrayList<ScheduledCourse> rccPref) {
		firstName = fName;
		lastName = lName;
		rNumber = numb;
		majors[0] = major1;
		majors[1] = major2;
		language = lang;
		status = stat;
		for(int i = 0; i < electivePref.size(); i++) {
			electivePreferences.add(electivePref.get(i));
		}
		for(int i = 0; i < rccPref.size(); i++) {
			rccPreferences.add(rccPref.get(i));
		}
	}
	
	public Schedule getStudentSchedule() {
		return schedule;
	}
	
	public String getRNumber() {
		return rNumber;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public String getStatus() {
		return status;
	}
	
	public String[] getMajors() {
		return majors;
	}
	
	public ArrayList<ScheduledCourse> getElectivePreferences() {
		return electivePreferences;
	}
	
	public ArrayList<ScheduledCourse> getRCCPref() {
		return rccPreferences;
	}
	
	public String getLanguage() {
		return language;
	}
	
	public void setRNumber(String num) {
		rNumber = num;
	}
	
	public void setFirstName(String name) {
		firstName = name;
	}
	
	public void setLastName(String name) {
		lastName = name;
	}
	
	public void setLanguage(String lang) {
		language = lang;
	}
	
	public void setStatus(String stat) {
		status = stat;
	}

	public void printStudentSchedule() {
		System.out.println("Intro: ");
		schedule.getMajorCourse().getCourse().printCourseInfo();
		System.out.print(" ");
		schedule.getMajorCourse().getTime().printTime();
		System.out.println();
		System.out.println("Elective: ");
		schedule.getElectiveCourse().getCourse().printCourseInfo();
		System.out.print(" ");
		schedule.getElectiveCourse().getTime().printTime();
		System.out.println();
		System.out.println("RCC: ");
		schedule.getRCCCourse().getCourse().printCourseInfo();
		System.out.print(" ");
		schedule.getRCCCourse().getTime().printTime();
		System.out.println();
		System.out.println("Competency: ");
		schedule.getCompetencyCourse().getCourse().printCourseInfo();
		System.out.print(" ");
		schedule.getCompetencyCourse().getTime().printTime();
		System.out.println();
	}

	
	public String[] getStudentInfo() {
		String[] studentInfo = new String[3];
		studentInfo[0] = firstName + " " + lastName;
		studentInfo[1] = rNumber;
		studentInfo[2] = majors[0] + ", " + majors[1];
		
		return studentInfo;

	}

	
	 public void printStudentSchedule_Terminal() {
		 System.out.println("Major: ");
		 schedule.getMajorCourse().getCourse().printCourseInfo();
		 System.out.print(" "); 
		 schedule.getMajorCourse().getTime().printTime();
		 System.out.println(); 
		 System.out.println("Elective: ");
		 schedule.getElectiveCourse().getCourse().printCourseInfo();
		 System.out.print(" "); 
		 schedule.getElectiveCourse().getTime().printTime();
		 System.out.println();
		 System.out.println("RCC: ");
		 schedule.getRCCCourse().getCourse().printCourseInfo();
		 System.out.print(" ");
		 schedule.getRCCCourse().getTime().printTime();
		 System.out.println();
		 System.out.println("Competency: ");
		 schedule.getCompetencyCourse().getCourse().printCourseInfo();
		 System.out.print(" ");
		 schedule.getCompetencyCourse().getTime().printTime();
		 System.out.println(); 
	}
	 	
	
	public void printStudentSchedule_Excel(int sheetNum) {
		// Create blank sheet from template
		XSSFSheet sheet = workbook.cloneSheet(0, "Student's Schedule " + sheetNum);
		sheet.setFitToPage(true);
		sheet.getPrintSetup().setFitWidth((short) 1);
		sheet.getPrintSetup().setFitHeight((short) 0);

		// Set student's name
		sheet.getRow(4).getCell(1).setCellValue(firstName + " " + lastName);
		
		// Set student's R-Number
		sheet.getRow(5).getCell(1).setCellValue(rNumber);
		
		// Set student's Major of Interest (if only one, or any at all)
		if (!majors[0].equals("N") && !majors[1].equals("N")) {
			sheet.getRow(6).getCell(1).setCellValue(majors[0] + ", " + majors[1]);
		} else if (!majors[0].equals("N") && majors[1].equals("N")) {
			sheet.getRow(6).getCell(1).setCellValue(majors[0]);
		}
		
		// Set course schedule
		for (int row = 9; row < 13; row++) {
			for (int col = 0; col < 6; col++) {
				// Print class' main details
				if (col < 4) {
					sheet.getRow(row).getCell(col).setCellValue(getStudentSchedule().getScheduleList()[row - 9].getCourse().getCourseInfo_XLSX()[col]);
				} else {
					// Print class' time details
					sheet.getRow(row).getCell(col).setCellValue(getStudentSchedule().getScheduleList()[row - 9].getTime().getTimeInfo_XLSX()[0]);
					sheet.getRow(row).getCell(col).setCellValue(getStudentSchedule().getScheduleList()[row - 9].getTime().getTimeInfo_XLSX()[1]);
				}
			}
		}
		
		// Deleting template sheet
		workbook.removeSheetAt(0);
		
		// Writing to excel file
		try (FileOutputStream outputStream = new FileOutputStream("StudentSchedules.xlsx")) {
			workbook.write(outputStream);
		} catch (IOException e) { 
			e.printStackTrace();
		}  
	}
	
	public void convertExcelToPDF(int sheetNum) {
		Converter converter = new Converter("StudentSchedules.xlsx");
		
	}
}
