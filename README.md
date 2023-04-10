# Automation of Freshman Schedule Creation
Our school scheduling automation system is designed specifically for incoming freshmen at Rollins College, with the goal of making schedule creation faster and more efficient. With our system, the registrar office no longer have to spend hours poring over course catalogs and trying to coordinate class times with other commitments. Our intuitive user interface and intelligent algorithm make it easy to create schedules for hundreds of students in just a few clicks. Plus, our system has been proven to significantly reduce the amount of time spent by the registrar's office in creating schedules, freeing up staff to focus on other important tasks. With our scheduling automation system.

# Instructions
## General 
For the system to run, the ____.exe file must be in the same folder as the students.txt, competency.txt, electives.txt, introductory.txt, and rccCourses.txt. If any of these files are missing, the Schedule Maker will print out an error and refuse to run. If these files are missing, simply adding .txt files with the appropriate names, then adding the data will resolve the issues. <br>

The system runs by first creating a list of students organized in the order of early enrollments first, followed by 3/2 Accelerated Management Program (AMP) students, honors students, then all other students. Students are first added to an introductory course, competency course, elective course, and then finally, the RCC course. When adding students to these courses, the system goes through the list of students in order so that the early enrollments get priority, followed by honors students, and so on. If a student has no preferences for certain courses (for example, a student does not have a filled-out Course Preference Form), they are put at the end of the list so that students with preferences can get the classes they want. <br>

When adding courses, the Schedule Maker checks to see if the course’s times interfere with any course already added. If it does, the Schedule Maker will find another course within the student’s preferences and check if that one works. 
The Schedule Maker ends when all students have an introductory, competency, elective, and RCC course added to their schedules.

<u>All data for courses are entered in the following way:<u>
Each part of the data must be separated with a `,` for the system to read the data in correctly. If `,` is missing or there is more than needed, the system will throw an error for too little or too much data. <br>

In total, there should be ten variables for a course. The information should be in the following order:
1. Course capacity
2. Course number (CRN)
3. Course name
4. Course title
5. Credits
6. Time
7. Days course meets
8. Major the course belongs to
9. Any competency the course covers
10. Variable to indicate if the course has a lab or not
<br>

Time for the course must be stored in the form of 01:00-2:15-P, with ‘-’ being used to separate the start time (the first number), the end time (the second number), and whether the course is in the morning or afternoon (the letter). This letter must be either a capital P for the afternoon (think pm) or a capital A for the morning (think am). <br>

The final variable indicates to the system whether or not the course has a lab. If the course has a lab, enter ‘true’ and enter information for the lab in the same format on the following line. (Keep in mind that the lab should not have ‘true’ for having a lab as it is the lab.) If the course does not have a lab, then enter ‘false.’ Remember that capitalization MATTERS, so ‘true’ and ‘false’ must all be lowercase. <br>

If students do not have a complete list of preferences or do not have one at all. For each course, they are missing the ‘N.’ For example, if a student only has six courses, you would put one N for the last course to fill out their preference sheet. 
	There should be no empty lines between data in the text files. If there is, it may result in an error.

<u> All data for students is entered in the following way: <u>
	All student data should be stored in the student.txt file. There are three parts for student information; the student’s general information (R-Number, last name, etc.), their elective preferences, and their rcc preferences.The general information of the student should come first and be stored in the following order:
1. R-Number
2. First name
3. Last name
4. First major of interest
5. Second major of interest
6. Language of interest
7. Status

For status only a capital letter should be entered. For students with honors put in an ‘H’, for students who were early enrolled put in ‘E’, for students who are part of the AMP ‘A’, and all other students should have the letter ‘N’ for no special status. <br>

In the case that a student does not have a major course, put ‘N’. For example if a student has a major of ENG but no second major, then the first major should read ‘Eng’ and the second should read ‘N’.<br>

All of the following information should be on one line separated by the delimiter “, “ similar to the format of courses. In addition to the student information listed above, each student should also have a list of preferences for both elective and RCC courses. These should be listed in the lines following the general student information, with the students’ elective preference being on the line immediately after the general information, and the RCC preferences being on the line immediately after elective preferences. Meaning a single student with all their information should take up three lines in the student text file. <br>

Both of these lists should list the name of the course and be separated by the delimiter “, “. There should be a total of seven courses for electives and eight courses for RCC. If students do not have a complete list of preferences or do not have one at all. For each course, they are missing put a letter ‘N.’ For example, if a student only has six courses for electives, you would put one N for the last course to fill out their preference sheet. <br>
Example: 
* missing
* missing

## UI

Once the system is launched, that is, the .exe file is opened, the user will be prompted with a window-based application with a rather familiar screen. Rollins’ Office of the Registrar logo will appear, followed by five buttons vertically laid out, namely “Add Student,” “Remove Student,” “Add Course,” “Remove Course,” and “Make Schedules.” Each button holds its own functionality to make the system ultimately work.
* **Add Student:** Pressing this button should bring up a screen that allows the user to enter the basic information required for a student, such as their First and Last Name, along with their 9-digit R-Number. In addition, other fields will be present to represent the information acquired through the * Course Preference Form; these are:
  * the student’s Major(s) of choice
  * their Status on whether they are an Honors student, applied as early enrollment/decision, or are a part of the 3/2 Accelerated Management Program
  * their Language Preference
  * their RCC and Elective Preferences 

The user may press the `Submit` button once all the information has been picked/filled out, which will check to see if that student has already been entered based on their R-Number. If so, a pop-up message is shown stating that a student with the same identifier is already on the database; if not, it adds the student to the system and the students.txt file. <br>

* **Remove Student:** Upon clicking this button, the user is prompted with a new screen with a single field to be filled out, the R-Number field. Since the R-Number works as a unique identifier for each student at Rollins, that is all that is needed to remove them from the system. Upon entering the number and pressing Remove, the system runs through the students.txt, checking if it finds a match; if so, the student is successfully removed; otherwise, it prompts the user with a message stating that the operation was unsuccessful as a student with that identifier was not found.

* **Add Course:** Once the user presses this button, it will take them to the respective screen where they will be prompted with various fields to be filled to add a course to the system successfully. These fields are:
  * the unique Course CRN
  * the Course Subject
  * the name of the course under Course Title
  * how many Credit Hours the course fulfills
  * the Course Prefix as a 3-lettered acronym
  * the Course Number as commonly preceded by the prefix
  * the Competency that the course fulfills (if any)
  * the Capacity that the course holds once it has been assigned a room
  * the Start and the End Time for the class
  * the days that the class meets under Meeting Days, which can be selected by pressing on the corresponding radio buttons underneath the labels for each day.

In addition, an Add Lab button is provided in the case that a specific class does have a lab requirement, which will prompt the same exact fields to be filled out on the other side of the screen once pressed. The user may choose to Submit the information at the end, which will first check if that course has already been added to the system based on the CRN provided. If a course with the same CRN is found, a pop-up window will show an error message to the user stating that a course with the same identifier is already in the system; otherwise, it will successfully add that course to their respective database and .txt files. <br>

* **Remove Course:** Analogous to removing a student, once the user clicks this button, it will prompt them with a single field to be filled out, the unique identifier Course CRN, and two buttons, Back and Remove. Upon entering the CRN of the course to be removed and continuing with the removal process, the system will first check every course .txt file for a course with that same CRN. If none have been found, an error message is displayed stating that no course could be found with the given CRN; otherwise, the course will be removed from every .txt file it may have been in.

* **Make Schedule:** Once all the desired courses and students have been added to the database, the user may decide to run the system by clicking this button. This will generate the schedules for each student, along with other important data, and export it into a well-presented Excel (.XLSX) file, ensuring that all constraints (e.g., student priority, class conflicts) are taken into consideration in their creation.

# Problem & Solutions
Make sure that when entering classes in students’ preferences, that the courses on the list are in the same format as those in the document. If that is not the case, the system will say that such a course doesn’t exist when in fact, it does; the input is just different. Ex: CMS 120 would work for CMS 120. However, CmS 120, cms 120, CMS  120, and CMS 120 will not. <br>

If the error for too many or too few variables keeps occurring, make sure you put in the correct amount of information. Check the formatting. If you don’t have ‘, ‘ (keep in mind the space), the system won’t recognize the following information. 

