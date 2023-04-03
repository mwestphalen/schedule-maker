// For EXCEL output
import java.io.IOException;
import java.io.FileOutputStream;
import org.apache.poi.hssf.usermodel.HSSFHeader;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.xssf.usermodel.extensions.XSSFHeaderFooter;

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
	
	// Excel Output variables
	private static XSSFWorkbook workbook;
	private static CellStyle infoLabelCellStyle;
	private static CellStyle infoDefaultCellStyle;
	private static CellStyle labelCellStyle;
	private static CellStyle defaultCellStyle;
	private static CellStyle courseTitleStyle;
	private static XSSFFont subheaderFont;
	private static XSSFDrawing patriarch;
	private static XSSFTextBox textbox;
	static {
		// Create blank workbook
		workbook = new XSSFWorkbook();
		
		// Set up fonts for output
		XSSFFont boldFont = workbook.createFont();
		boldFont.setBold(true);
		
		subheaderFont = workbook.createFont();
		subheaderFont.setBold(true);
		subheaderFont.setUnderline(FontUnderline.SINGLE);
		subheaderFont.setFontHeightInPoints((short) 28);
		
		// Set cell style for output
		infoLabelCellStyle = workbook.createCellStyle();
		infoLabelCellStyle.setAlignment(HorizontalAlignment.RIGHT);
		infoLabelCellStyle.setFillForegroundColor(IndexedColors.ROYAL_BLUE.getIndex());
		infoLabelCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		infoLabelCellStyle.setBorderTop(BorderStyle.THIN);
		infoLabelCellStyle.setBorderBottom(BorderStyle.THIN);
		infoLabelCellStyle.setBorderLeft(BorderStyle.THIN);
		infoLabelCellStyle.setBorderRight(BorderStyle.THIN);
		infoLabelCellStyle.setFont(boldFont);
		
		infoDefaultCellStyle = workbook.createCellStyle();
		infoDefaultCellStyle.setAlignment(HorizontalAlignment.RIGHT);
		infoDefaultCellStyle.setBorderTop(BorderStyle.THIN);
		infoDefaultCellStyle.setBorderBottom(BorderStyle.THIN);
		infoDefaultCellStyle.setBorderLeft(BorderStyle.THIN);
		infoDefaultCellStyle.setBorderRight(BorderStyle.THIN);
		
		labelCellStyle = workbook.createCellStyle();
		labelCellStyle.setAlignment(HorizontalAlignment.CENTER);
		labelCellStyle.setFillForegroundColor(IndexedColors.ROYAL_BLUE.getIndex());
		labelCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		labelCellStyle.setBorderTop(BorderStyle.THIN);
		labelCellStyle.setBorderBottom(BorderStyle.THIN);
		labelCellStyle.setBorderLeft(BorderStyle.THIN);
		labelCellStyle.setBorderRight(BorderStyle.THIN);
		labelCellStyle.setFont(boldFont);
		
		defaultCellStyle = workbook.createCellStyle();
		defaultCellStyle.setAlignment(HorizontalAlignment.CENTER);
		defaultCellStyle.setBorderTop(BorderStyle.THIN);
		defaultCellStyle.setBorderBottom(BorderStyle.THIN);
		defaultCellStyle.setBorderLeft(BorderStyle.THIN);
		defaultCellStyle.setBorderRight(BorderStyle.THIN);
		
		courseTitleStyle = workbook.createCellStyle();
		courseTitleStyle.setAlignment(HorizontalAlignment.LEFT);
		courseTitleStyle.setBorderTop(BorderStyle.THIN);
		courseTitleStyle.setBorderBottom(BorderStyle.THIN);
		courseTitleStyle.setBorderLeft(BorderStyle.THIN);
		courseTitleStyle.setBorderRight(BorderStyle.THIN);
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
	
	public ArrayList<ScheduledCourse> getRCCPreferences() {
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
	
	public String[] getStudentInfo() {
		String[] studentInfo = new String[3];
		studentInfo[0] = firstName + " " + lastName;
		studentInfo[1] = rNumber;
		studentInfo[2] = majors[0] + ", " + majors[1];
		
		return studentInfo;

	}

	
	public void printStudentSchedule_Terminal() {
		System.out.println("Intro: ");
		try {
			schedule.getMajorCourse().getCourse().printCourseInfo();
			System.out.print(" ");
			schedule.getMajorCourse().getTime().printTime();
			
			if (schedule.getMajorCourse().getHasLab()) {
				System.out.println();
				schedule.getMajorCourse().getCourseLab().printCourseInfo();
				System.out.print(" ");
				schedule.getMajorCourse().getLabTime().printTime();
			}
		} catch (Exception e){
			
		}
		System.out.println();
		System.out.println("Competency: ");
		try {
			schedule.getCompetencyCourse().getCourse().printCourseInfo();
			System.out.print(" ");
			schedule.getCompetencyCourse().getTime().printTime();
			
			if (schedule.getCompetencyCourse().getHasLab()) {
				System.out.println();
				schedule.getCompetencyCourse().getCourseLab().printCourseInfo();
				System.out.print(" ");
				schedule.getCompetencyCourse().getLabTime().printTime();
			}
		} catch (Exception e) {
			
		}
		System.out.println();
		System.out.println("Elective: ");
		try {
			schedule.getElectiveCourse().getCourse().printCourseInfo();
			System.out.print(" ");
			schedule.getElectiveCourse().getTime().printTime();
			
			if (schedule.getElectiveCourse().getHasLab()) {
				System.out.println();
				schedule.getElectiveCourse().getCourseLab().printCourseInfo();
				System.out.print(" ");
				schedule.getElectiveCourse().getLabTime().printTime();
			}
		} catch (Exception e) {
			
		}
		System.out.println();
		System.out.println("RCC: ");
		try {
			schedule.getRCCCourse().getCourse().printCourseInfo();
			System.out.print(" ");
			schedule.getRCCCourse().getTime().printTime();
			
			if (schedule.getRCCCourse().getHasLab()) {
				System.out.println();
				schedule.getRCCCourse().getCourseLab().printCourseInfo();
				System.out.print(" ");
				schedule.getRCCCourse().getLabTime().printTime();
			}
		} catch (Exception e) {
			
		}
		
		System.out.println();
	}
	 	
	public void printStudentSchedule_Excel(int studentNum, int totalNumOfStudents) {
		/* TODO
		 * Check how many sheets it creates in different machines...
		 * 1. For some reason, they are all stopping at 129 ... studentList.size() in main = 129
		 * 2. Might have to break 100 each per workbook b/c of limiting memory.
		 */
		
		
		// Create blank sheet for each student under one workbook
		XSSFSheet sheet = workbook.createSheet(firstName + " " + lastName + " (" + studentNum + ")");
		sheet.setFitToPage(true);
		sheet.getPrintSetup().setFitWidth((short) 1);
		sheet.getPrintSetup().setFitHeight((short) 0);
		
		// Set Sheet header
		XSSFHeaderFooter header = (XSSFHeaderFooter) sheet.getHeader();
		header.setCenter(HSSFHeader.font("Calibri", "Bold") + HSSFHeader.fontSize((short) 36) + "Tentative Academic Schedule");
		
		// Prompt
		XSSFDrawing patriarch = sheet.createDrawingPatriarch();
		XSSFTextBox textbox = patriarch.createTextbox(new XSSFClientAnchor(0,0,0,0, 0, 1, 7, 1));
		textbox.setText("This course schedule was constructed according to the preferences, required foundation "
				+ "competencies, academic background, and anticipated major(s) as filled out by the student in the "
				+ "Course Preference Form.");
		
		// Student's Info Sub Header
		textbox = patriarch.createTextbox(new XSSFClientAnchor(0,0,0,0,0,3,2,3));
		XSSFRichTextString prompt = new XSSFRichTextString("STUDENT'S INFO");
		prompt.applyFont(subheaderFont);
		textbox.setText(prompt);
		
		// Set student's name, R-Number, and Major of Interest
		printPersonalInfo_Excel(sheet);
		
		// Student's Schedule Sub Header
		textbox = patriarch.createTextbox(new XSSFClientAnchor(0, 0, 0, 0, 0, 10, 2, 10));
		prompt = new XSSFRichTextString("SCHEDULE");
		prompt.applyFont(subheaderFont);
		textbox.setText(prompt);
		
		// Print schedule labels to Excel
		printScheduleLabel_Excel(sheet);
		
		
		// Set course schedule
		int classIndex = 0;
		boolean hasLab = false;
		// Class data is written from row [9-14]
		for (int row = 14; row < 19; row++) {
			XSSFRow rowCreated = sheet.createRow(row);
			XSSFCell cell;
			// Class data is written from column A-G [0,6]
			for (int col = 0; col < 7; col++) {
					
				if (row > 17 && !hasLab) {
					break;
				}
				// Print class' main details
				if (col < 4) {
					// Create cell and set style
					cell = rowCreated.createCell(col);

					if (col == 2) {
						cell.setCellStyle(courseTitleStyle); // Left Aligned
					} else {
						cell.setCellStyle(defaultCellStyle); // Center Aligned
					}
					
					if (col == 3) {	
						// Print credits as integers
						cell.setCellValue(
								Integer.parseInt(getStudentSchedule().getScheduleList()[classIndex].getCourse().getCourseInfo_XLSX()[3]));
						
					} else {
						// Print everything else as text
						cell.setCellValue(
								getStudentSchedule().getScheduleList()[classIndex].getCourse().getCourseInfo_XLSX()[col]);
					}

				} else if (col == 4) {
					// Print class' date details
					cell = rowCreated.createCell(col);
					cell.setCellStyle(defaultCellStyle);
					cell.setCellValue(
							getStudentSchedule().getScheduleList()[classIndex].getTime().getTimeInfo_XLSX()[0]);
				} else if (col == 5) {
					// Print class' time details
					cell = rowCreated.createCell(col);
					cell.setCellStyle(defaultCellStyle);
					cell.setCellValue(
							getStudentSchedule().getScheduleList()[classIndex].getTime().getTimeInfo_XLSX()[1]);
				} else {
					// Print course's fulfillment type (Major of Interest, RCC, Elective,
					// Competency)
					cell = rowCreated.createCell(col);
					cell.setCellStyle(defaultCellStyle);
					switch (classIndex) {
					case 0:
						cell.setCellValue("Major");
						break;
					case 1:
						cell.setCellValue("Competency");
						break;
					case 2:
						cell.setCellValue("Elective");
						break;
					case 3:
						cell.setCellValue("RCC");
						break;
					default:
						cell.setCellValue("error");
						break;
					}
						
					
				}
			}

			// Print lab details if course has a lab
			if (row < 18 && getStudentSchedule().getScheduleList()[classIndex].getHasLab()) {

				hasLab = true;
				row++;
				XSSFRow labRow = sheet.createRow(row);
				for (int labCol = 0; labCol < 7; labCol++) {

					cell = labRow.createCell(labCol);
					
					// Print lab's main details
					switch (labCol) {
					case 0:
						// Print lab's course CRN
						cell.setCellStyle(defaultCellStyle);
						cell.setCellValue(getStudentSchedule().getScheduleList()[classIndex].getCourseLab()
								.getCourseInfo_XLSX()[labCol]);
						break;
					case 1:
						// Print lab's course number
						cell.setCellStyle(defaultCellStyle);
						cell.setCellValue(getStudentSchedule().getScheduleList()[classIndex].getCourseLab()
								.getCourseInfo_XLSX()[labCol]);
						break;
					case 2:
						// Print lab's course title
						cell.setCellStyle(courseTitleStyle);
						cell.setCellValue(getStudentSchedule().getScheduleList()[classIndex].getCourseLab()
								.getCourseInfo_XLSX()[labCol]);

						break;
					case 3:
						// Print lab's credits as integers
						cell.setCellStyle(defaultCellStyle);
						cell.setCellValue(Integer.parseInt(getStudentSchedule().getScheduleList()[classIndex]
								.getCourseLab().getCourseInfo_XLSX()[3]));
						break;
					case 4:
						// Print lab meeting dates
						cell.setCellStyle(defaultCellStyle);
						cell.setCellValue(
								getStudentSchedule().getScheduleList()[classIndex].getLabTime().getTimeInfo_XLSX()[0]);
						break;
					case 5:
						// Print lab meeting time
						cell.setCellStyle(defaultCellStyle);
						cell.setCellValue(
								getStudentSchedule().getScheduleList()[classIndex].getLabTime().getTimeInfo_XLSX()[1]);
						break;
					case 6:
						// Print course's fulfillment type (Major of Interest, RCC, Elective, Competency)
						cell.setCellStyle(defaultCellStyle);
						switch (classIndex) {
						case 0:
							cell.setCellValue("Major");
							break;
						case 1:
							cell.setCellValue("Competency");
							break;
						case 2:
							cell.setCellValue("Elective");
							break;
						case 3:
							cell.setCellValue("RCC");
							break;
						default:
								cell.setCellValue("error");
								break;
						}

						break;
					default:
						cell.setCellStyle(defaultCellStyle);
						cell.setCellValue("ERROR");
						break;
					}
				}
			}

			// Go to the next class on the list
			classIndex++;
		}
		
		// Print total credits
		if (!hasLab) {
			XSSFCell cell = sheet.createRow(18).createCell(2);
			cell.setCellStyle(labelCellStyle);
			cell.setCellValue("Total Credits");
			
			cell = sheet.getRow(18).createCell(3);
			cell.setCellStyle(defaultCellStyle);
			cell.setCellFormula("SUM(D15:D18)");
		} else {
			XSSFCell cell = sheet.createRow(19).createCell(2);
			cell.setCellStyle(labelCellStyle);
			cell.setCellValue("Total Credits");
			
			cell = sheet.getRow(19).createCell(3);
			cell.setCellStyle(defaultCellStyle);
			cell.setCellFormula("SUM(D15:D19)");
		}
				
		// Print Student's RCC Preferences
		printStudentPreferences_Excel(sheet, hasLab);
		
		// Writing to excel file
		try (FileOutputStream outputStream = new FileOutputStream("StudentSchedules.xlsx")) {
			workbook.write(outputStream);
			
			// Remove template sheet and close output stream
			if (studentNum == totalNumOfStudents) {
				//workbook.setActiveSheet(0);
				workbook.removeSheetAt(0);
			}
			outputStream.close();
			
		} catch (IOException e) { 
			e.printStackTrace();
		}  
		
		
	}

	public void printPersonalInfo_Excel(XSSFSheet sheet) {
		// Set overall cell style
		sheet.setColumnWidth(0, 14*255);
		sheet.setColumnWidth(1, 27*255);
	
		for (int rowIndex = 6; rowIndex < 9; rowIndex++) {
			XSSFRow row = sheet.createRow(rowIndex);
			XSSFCell cell;
			
			switch (rowIndex) {
			case 6:
				// Set Name label (bold, centered)
				cell = row.createCell(0);	
				cell.setCellStyle(infoLabelCellStyle);
				cell.setCellValue("Name");
			
				cell = row.createCell(1);
				cell.setCellStyle(infoDefaultCellStyle);
				cell.setCellValue(firstName + " " + lastName);
				break;
			case 7:
				cell = row.createCell(0);
				cell.setCellStyle(infoLabelCellStyle);
				cell.setCellValue("R-Number");
			
				cell = row.createCell(1);
				cell.setCellStyle(infoDefaultCellStyle);
				cell.setCellValue(rNumber);
				break;
			case 8:
				cell = row.createCell(0);
				cell.setCellStyle(infoLabelCellStyle);
				cell.setCellValue("Major of Interest");
			
				cell = row.createCell(1);
				cell.setCellStyle(infoDefaultCellStyle);	
			
				// Set student's Major of Interest (if only one, or any at all)
				if (!majors[0].equals("N") && !majors[1].equals("N")) {
					cell.setCellValue(majors[0] + ", " + majors[1]);
				} else if (!majors[0].equals("N") && majors[1].equals("N")) {
					cell.setCellValue(majors[0]);
				} else {
					cell.setCellValue("Exploring");
				}
				
				
				break;
			default:
				cell = row.createCell(0);
				cell.setCellStyle(infoDefaultCellStyle);
				cell.setCellValue("ERROR");
				break;
			}
		}
		
		// Print if student is an Honors student
		if (getStatus().equals("H")) {
			
			XSSFCell cell = sheet.getRow(6).createCell(2);
			cell.setCellStyle(defaultCellStyle);
			cell.setCellValue("*Honors Student");
		}
	}

	public void printScheduleLabel_Excel(XSSFSheet sheet) {
		// Set columns' width
		sheet.setColumnWidth(2, 31 * 255);
		sheet.setColumnWidth(5, 12 * 255);
		sheet.setColumnWidth(6, 12 * 255);

		XSSFRow labelRow = sheet.createRow(13);
		XSSFCell cell = null;

		cell = labelRow.createCell(0);
		cell.setCellStyle(labelCellStyle);
		cell.setCellValue("CRN");
		cell = labelRow.createCell(1);
		cell.setCellStyle(labelCellStyle);
		cell.setCellValue("Course Number");
		cell = labelRow.createCell(2);
		cell.setCellStyle(labelCellStyle);
		cell.setCellValue("Course Title");
		cell = labelRow.createCell(3);
		cell.setCellStyle(labelCellStyle);
		cell.setCellValue("Credits");
		cell = labelRow.createCell(4);
		cell.setCellStyle(labelCellStyle);
		cell.setCellValue("Days");
		cell = labelRow.createCell(5);
		cell.setCellStyle(labelCellStyle);
		cell.setCellValue("Time");
		cell = labelRow.createCell(6);
		cell.setCellStyle(labelCellStyle);
		cell.setCellValue("Fulfilment");
	}

	public void printStudentPreferences_Excel(XSSFSheet sheet, boolean hasLab) {
		
		/*
		Print Student's RCC Preference
		 */
		// Track what row to start printing
		int rowToStart = 23;
		if (hasLab) {
			rowToStart++;
		}
		
		patriarch = sheet.createDrawingPatriarch();
		
		// RCC Preferences Sub Header
		if (hasLab) {
			textbox = patriarch.createTextbox(new XSSFClientAnchor(0, 0, 0, 0, 0, 21, 2, 21));
		} else {
			textbox = patriarch.createTextbox(new XSSFClientAnchor(0, 0, 0, 0, 0, 20, 2, 20));
		}

		XSSFRichTextString prompt = new XSSFRichTextString("RCC PREFERENCES");
		prompt.applyFont(subheaderFont);
		textbox.setText(prompt);
		
		// Print RCC Preferences labels
		XSSFRow labelRow = sheet.createRow(rowToStart);
		XSSFCell cell = null;

		cell = labelRow.createCell(0);
		cell.setCellStyle(labelCellStyle);
		cell.setCellValue("Preference Order");
		cell = labelRow.createCell(1);
		cell.setCellStyle(labelCellStyle);
		cell.setCellValue("RCC's Course Number");
		cell = labelRow.createCell(2);
		cell.setCellStyle(labelCellStyle);
		cell.setCellValue("RCC's Course Title");
		cell = labelRow.createCell(3);
		cell.setCellStyle(labelCellStyle);
		cell.setCellValue("Credits");
		cell = labelRow.createCell(4);
		cell.setCellStyle(labelCellStyle);
		cell.setCellValue("Days");
		cell = labelRow.createCell(5);
		cell.setCellStyle(labelCellStyle);
		cell.setCellValue("Time");		
		
		// Print RCC Preference class details
		int numRCCPref = getRCCPreferences().size();
		rowToStart++;
		// No RCC were picked, therefore N/A is printed and for loop is skipped
		if (numRCCPref == 0) {
			XSSFRow rowCreated = sheet.createRow(rowToStart);
			for (int col = 0; col < 6; col++) {
				cell = rowCreated.createCell(col);
				cell.setCellStyle(defaultCellStyle);
				cell.setCellValue("N/A");
				
			}
		}
		
		int rccIndex = 0;
		// RCC Class data is written from row [16,24] ***
		for (int row = rowToStart; row < (numRCCPref + rowToStart); row++) {
			XSSFRow rowCreated = sheet.createRow(row);
			
			// stop on 
			// RCC Class data is written from column A-F [0,5]
			for (int col = 0; col < 6; col++) {
				
				// Print class'main details
				switch (col) {
				case 0:
					// Print the order classes were chosen
					cell = rowCreated.createCell(col);
					cell.setCellStyle(defaultCellStyle);
					cell.setCellValue(rccIndex + 1);
					break;
				case 1:
					// Print course number
					cell = rowCreated.createCell(col);
					cell.setCellStyle(defaultCellStyle);
					cell.setCellValue(
							getRCCPreferences().get(rccIndex).getCourse().getCourseInfo_XLSX()[col]);
					break;
				case 2:
					// Print course title
					cell = rowCreated.createCell(col);
					cell.setCellStyle(courseTitleStyle);
					cell.setCellValue(
							getRCCPreferences().get(rccIndex).getCourse().getCourseInfo_XLSX()[col]);
					break;
				case 3:
					// Print credits as integers
					cell = rowCreated.createCell(col);
					cell.setCellStyle(defaultCellStyle);
					cell.setCellValue(
							Integer.parseInt(getRCCPreferences().get(rccIndex).getCourse().getCourseInfo_XLSX()[3]));
					break;
				case 4: 
					// Print course days
					cell = rowCreated.createCell(col);
					cell.setCellStyle(defaultCellStyle);
					cell.setCellValue(
							getRCCPreferences().get(rccIndex).getTime().getTimeInfo_XLSX()[0]);
					break;
				case 5:
					// Print course meeting times
					cell = rowCreated.createCell(col);
					cell.setCellStyle(defaultCellStyle);
					cell.setCellValue(
							getRCCPreferences().get(rccIndex).getTime().getTimeInfo_XLSX()[1]);
					break;
				default:
					cell = rowCreated.createCell(col);
					cell.setCellStyle(defaultCellStyle);
					cell.setCellValue("Error");
					break;	
				}
			}
			rccIndex++;
		}
		
		
		/*
		Print Student's Electives Preference
		 */
		// Track at what row to start printing
		if (numRCCPref == 0) {numRCCPref++;}
		rowToStart += (numRCCPref + 1);
		
		// Elective Preferences Sub Header
		textbox = patriarch.createTextbox(new XSSFClientAnchor(0, 0, 0, 0, 0, rowToStart, 3, rowToStart));
		prompt = new XSSFRichTextString("ELECTIVE PREFERENCES");
		prompt.applyFont(subheaderFont);
		textbox.setText(prompt);
		rowToStart+=3;
		
		// Print Elective Preferences labels
		
		labelRow = sheet.createRow(rowToStart);
		
		cell = labelRow.createCell(0);
		cell.setCellStyle(labelCellStyle);
		cell.setCellValue("Preference Order");
		cell = labelRow.createCell(1);
		cell.setCellStyle(labelCellStyle);
		cell.setCellValue("Elective's Course Number");
		cell = labelRow.createCell(2);
		cell.setCellStyle(labelCellStyle);
		cell.setCellValue("Elective's Course Title");
		cell = labelRow.createCell(3);
		cell.setCellStyle(labelCellStyle);
		cell.setCellValue("Credits");
		cell = labelRow.createCell(4);
		cell.setCellStyle(labelCellStyle);
		cell.setCellValue("Days");
		cell = labelRow.createCell(5);
		cell.setCellStyle(labelCellStyle);
		cell.setCellValue("Time");

		// Print RCC Preference class details
		int numElectivePref = getElectivePreferences().size();
		rowToStart++;
		// No Electives were picked, therefore N/A is printed
		if (numElectivePref == 0) {
			XSSFRow rowCreated = sheet.createRow(rowToStart);
			for (int col = 0; col < 6; col++) {
				cell = rowCreated.createCell(col);
				cell.setCellStyle(defaultCellStyle);
				cell.setCellValue("N/A");
			}
		}

		int electiveIndex = 0;
		// Elective Class data is written from row [16,24] ***
		for (int row = rowToStart; row < numElectivePref + rowToStart; row++) {
			XSSFRow rowCreated = sheet.createRow(row);

			// Elective Class data is written from column A-F [0,5]
			for (int col = 0; col < 6; col++) {

				// Print class'main details
				switch (col) {
				case 0:
					// Print the order classes were chosen
					cell = rowCreated.createCell(col);
					cell.setCellStyle(defaultCellStyle);
					cell.setCellValue(electiveIndex + 1);
					break;
				case 1:
					// Print course number
					cell = rowCreated.createCell(col);
					cell.setCellStyle(defaultCellStyle);
					cell.setCellValue(
							getElectivePreferences().get(electiveIndex).getCourse().getCourseInfo_XLSX()[col]);
					break;
				case 2:
					// Print course title
					cell = rowCreated.createCell(col);
					cell.setCellStyle(courseTitleStyle);
					cell.setCellValue(
							getElectivePreferences().get(electiveIndex).getCourse().getCourseInfo_XLSX()[col]);
					break;
				case 3:
					// Print credits as integers
					cell = rowCreated.createCell(col);
					cell.setCellStyle(defaultCellStyle);
					cell.setCellValue(
							Integer.parseInt(getElectivePreferences().get(electiveIndex).getCourse().getCourseInfo_XLSX()[3]));
					break;
				case 4: 
					// Print course days
					cell = rowCreated.createCell(col);
					cell.setCellStyle(defaultCellStyle);
					cell.setCellValue(
							getElectivePreferences().get(electiveIndex).getTime().getTimeInfo_XLSX()[0]);
					break;
				case 5:
					// Print course meeting times
					cell = rowCreated.createCell(col);
					cell.setCellStyle(defaultCellStyle);
					cell.setCellValue(
							getElectivePreferences().get(electiveIndex).getTime().getTimeInfo_XLSX()[1]);
					break;
				default:
					cell = rowCreated.createCell(col);
					cell.setCellStyle(defaultCellStyle);
					cell.setCellValue("Error");
					break;	
				}
			}
			electiveIndex++;
		}
		
		/*
		 * Print Student's Language Preference
		 */
		// Track at what row to start printing
		if (numElectivePref == 0) {
			numElectivePref++;
		}
		rowToStart += (numElectivePref + 1);

		// Language Preferences Sub Header
		textbox = patriarch.createTextbox(new XSSFClientAnchor(0, 0, 0, 0, 0, rowToStart, 3, rowToStart));
		prompt = new XSSFRichTextString("LANGUAGE PREFERENCES");
		prompt.applyFont(subheaderFont);
		textbox.setText(prompt);
		rowToStart += 3;

		// Print Language Preferences labels
		labelRow = sheet.createRow(rowToStart);
		cell = labelRow.createCell(0);
		cell.setCellStyle(labelCellStyle);
		cell.setCellValue("Spoken at Home");

		labelRow = sheet.createRow(rowToStart + 1);
		cell = labelRow.createCell(0);
		cell.setCellStyle(labelCellStyle);
		cell.setCellValue("Interest");

		// Print language spoken at home
		rowToStart++;
		// Currently, the system does not track student's language
		// spoken at home

		// Print language of interest, if any were picked
		cell = sheet.getRow(rowToStart).createCell(1);
		cell.setCellStyle(defaultCellStyle);
		if (getLanguage().equals("N")) {
			cell.setCellValue("N/A");
		} else {
			cell.setCellValue(getLanguage());
		}

		// Comments Sub Header
		rowToStart += 2;
		textbox = patriarch.createTextbox(new XSSFClientAnchor(0, 0, 0, 0, 0, rowToStart, 2, rowToStart));
		prompt = new XSSFRichTextString("COMMENTS");
		prompt.applyFont(subheaderFont);
		textbox.setText(prompt);
		
	}
}
