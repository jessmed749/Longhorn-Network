import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please provide the input file name as a command-line argument.");
            return;
        }
        String inputFile = args[0];
        try {
            // Parse student data
            List<UniversityStudent> students = DataParser.parseStudents(inputFile);
            System.out.println("Parsed " + students.size() + " students from " + inputFile + ":\n");
            for (UniversityStudent student : students) {
                System.out.println(student);
            }
            
            // Roommate matching
            //GaleShapley.assignRoommates(students);
            
            // Pod formation
            StudentGraph graph = new StudentGraph(students); //TODO: implement StudentGraph
           // PodFormation podFormation = new PodFormation(graph);
           // podFormation.formPods(4);
            
            // Display the student graph
            System.out.println("\nStudent Graph:");
            graph.displayGraph(); //TODO: implement displayGraph
            
            // Referral path finding
          //  ReferralPathFinder pathFinder = new ReferralPathFinder(graph);
            // TODO: Implement user interaction for specifying a target company
            
        } catch (NumberFormatException e) {
            System.err.println("Number format error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Parsing error: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
        }
    }
}

