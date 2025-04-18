import java.util.*;

public class GaleShapley {
    public static void assignRoommates(List<UniversityStudent> students) {
        // Track which proposal each student is up to
        Map<UniversityStudent, Integer> nextProposalIndex = new HashMap<>();
        // Map to quickly lookup a student by name
        Map<String, UniversityStudent> nameToStudent = new HashMap<>();

        for (UniversityStudent s : students) {
            nameToStudent.put(s.getName(), s);
            nextProposalIndex.put(s, 0); // Initialize proposal index for each student
        }

        // Queue for students who are free to propose
        Queue<UniversityStudent> freeStudents = new LinkedList<>();
        for (UniversityStudent s : students) {
            if(!s.roommatePreferences.isEmpty()) {
                freeStudents.offer(s); // Add students with preferences to the queue
            }
        }

        while(!freeStudents.isEmpty()) {
            UniversityStudent s = freeStudents.poll();
            //Skiped if already matched
            if(s.getRoommate() != null) {
                continue;
            }

            int index = nextProposalIndex.get(s);
            if(index >= s.roommatePreferences.size()) {
                continue; // No more preferences to propose to
            }

            String preferredRoommateName = s.roommatePreferences.get(index);
            nextProposalIndex.put(s, index + 1); // Move to the next preference
            UniversityStudent t = nameToStudent.get(preferredRoommateName);
            if (t == null) {
                //Preferred roommate not found, skip this proposal
                if(nextProposalIndex.get(s) < s.roommatePreferences.size()) {
                    freeStudents.offer(s); // Re-add to the queue for next proposal
                }
                continue;
            }

            if(!t.roommatePreferences.contains(s.name)){
                if(nextProposalIndex.get(s) < s.roommatePreferences.size()) {
                    freeStudents.offer(s); // Re-add to the queue for next proposal
                }
                continue; // t is not interested in s, skip this proposal
            }

            // Check if t is free or prefers s over current roommate
            if(t.getRoommate() == null) {
                // t is free, match s and t
                roommatePairs.put(s, t);
                roommatePairs.put(t, s);
                s.setRoommate(t);
                t.setRoommate(s);

            } else {
                UniversityStudent currentPartner = t.getRoommate();
                int currentIndex = t.roommatePreferences.indexOf(currentPartner.name);
                int newIndex = t.roommatePreferences.indexOf(s.getName());
                if (newIndex < currentIndex) {
                    // t prefers s over current roommate, switch roommates
                    roommatePairs.put(t, s);
                    roommatePairs.put(s, t);
                    roommatePairs.remove(currentPartner); // Remove current partner from the pairs
                    freeStudents.offer(currentPartner); // Add current partner back to the queue
                    currentPartner.setRoommate(null); // Set current partner's roommate to null
                    s.setRoommate(t); // Set s's roommate to t
                    t.setRoommate(s); // Set t's roommate to s
                } else {
                    // t jeject s
                    if(nextProposalIndex.get(s) < s.roommatePreferences.size()) {
                        freeStudents.offer(s); // Re-add to the queue for next proposal
                    }
                }
            }
            
        }

        // Print the final roommate pairs
        System.out.println("\nRoommate Pairings (Gale-Shapley):");
        Set<UniversityStudent> printed = new HashSet<>();
        for (UniversityStudent s : roommatePairs.keySet()) {
            UniversityStudent partner = roommatePairs.get(s);
            if (!printed.contains(s) && !printed.contains(partner)){
                System.out.println(s.name + " paired with " + partner.name);
                printed.add(s);
                printed.add(partner);
            }
        }
    }
}
