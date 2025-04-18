import java.io.*;
import java.util.*;

public class DataParser {
    public static List<UniversityStudent> parseStudents(String filename) throws IOException {

        List<UniversityStudent> students = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        
        // Student variables
        String student_name = "";
        String student_gender = "";
        String student_major = "";
        int student_age = 0;
        int student_year = 0;
        double student_gpa = 0.0;
        
        List<String> student_roommatePref = new ArrayList<>();
        List<String> student_prevIntern = new ArrayList<>();
        
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue; // Skip empty lines
            
            if (line.startsWith("Student:")) {
                if (!student_name.isEmpty()) {
                    UniversityStudent student = new UniversityStudent(student_name, 
                        student_age, student_gender, student_year, student_major, student_gpa, student_roommatePref, student_prevIntern);
                    students.add(student);
                    
                    // Reset variables for the next student
                    student_name = "";
                    student_gender = "";
                    student_major = "";
                    student_age = 0;
                    student_year = 0;
                    student_gpa = 0.0;
                    student_roommatePref = new ArrayList<>();
                    student_prevIntern = new ArrayList<>();
                }
                continue; // Move to next line
            }
            
            // Process the line in "Key: Value" format
            int colonIndex = line.indexOf(":");
            if (colonIndex == -1) {
                // If no colon found, skip the line
                continue;
            }
            String key = line.substring(0, colonIndex).trim();
            String value = line.substring(colonIndex + 1).trim();
            
            switch (key) {
                case "Name":
                    student_name = value;
                    break;
                case "Age":
                    try {
                        student_age = Integer.parseInt(value);
                    } catch (NumberFormatException e) {
                        student_age = 0; // default if fail
                    }
                    break;
                case "Gender":
                    student_gender = value;
                    break;
                case "Year":
                    try {
                        student_year = Integer.parseInt(value);
                    } catch (NumberFormatException e) {
                        student_year = 0; // default if fail
                    }
                    break;
                case "Major":
                    student_major = value;
                    break;
                case "GPA":
                    try {
                        student_gpa = Double.parseDouble(value);
                    } catch (NumberFormatException e) {
                        student_gpa = 0.0; // default if fail
                    }
                    break;
                case "RoommatePreferences":
                    if (!value.isEmpty()) {
                        String[] prefs = value.split(",");
                        for (String pref : prefs) {
                            String trimmedPref = pref.trim();
                            if (!trimmedPref.isEmpty()) {
                                student_roommatePref.add(trimmedPref);
                            }
                        }
                    }
                    break;
                case "PreviousInternships":
                    if (!value.isEmpty()) {
                        String[] internships = value.split(",");
                        for (String internship : internships) {
                            String trimmedInternship = internship.trim();
                            if (!trimmedInternship.isEmpty()) {
                                student_prevIntern.add(trimmedInternship);
                            }
                        }
                    }
                    break;
                default:
                    // Unrecognized key, ignore
                    break;
            }
        }
        
        // Add the last student if any data was collected
        if (!student_name.isEmpty()) {
            UniversityStudent student = new UniversityStudent(student_name, 
                student_age, student_gender, student_year, student_major, student_gpa, student_roommatePref, student_prevIntern);
            students.add(student);
        }
        reader.close();
        return students;
    }
}