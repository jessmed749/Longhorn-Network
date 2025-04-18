import java.util.*;

public class UniversityStudent extends Student {
    // TODO: Constructor and additional methods to be implemented
    private UniversityStudent roommate;
    private Set<UniversityStudent> friends;
    private Map<UniversityStudent, List<String>> chatHistory;


    public UniversityStudent(String name, int age, String gender, int year, String major, double gpa,
                             List<String> roommatePreferences, List<String> previousInternships) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.year = year;
        this.major = major;
        this.gpa = gpa;
        // Create defensive copies of the lists
        this.roommatePreferences = new ArrayList<>(roommatePreferences);
        this.previousInternships = new ArrayList<>(previousInternships);
        this.friends = new HashSet<>();
        this.chatHistory = new HashMap<>();
    }

    public String getName() {
        return this.name;
    }

    public synchronized void setRoommate(UniversityStudent roommate) {
        this.roommate = roommate;
    }
    
    public synchronized UniversityStudent getRoommate() {
        return this.roommate;
    }
    
    public synchronized void addFriend(UniversityStudent friend) {
        this.friends.add(friend);
    }
    
    public synchronized boolean isFriend(UniversityStudent student) {
        return this.friends.contains(student);
    }
    
    public synchronized void addMessage(UniversityStudent other, String message) {
        if (!chatHistory.containsKey(other)) {
            chatHistory.put(other, new ArrayList<>());
        }
        chatHistory.get(other).add(message);
    }
    
    public synchronized List<String> getChatHistory(UniversityStudent other) {
        return chatHistory.getOrDefault(other, new ArrayList<>());
    }
    
    @Override
    public int calculateConnectionStrength(Student other) {
        if (!(other instanceof UniversityStudent)) {
            return 0;
        }
        
        UniversityStudent otherStudent = (UniversityStudent) other;
        int strength = 0;
        
        // Roommate: Add 4 if they are roommates
        if (this.roommate != null && this.roommate.equals(otherStudent)) {
            strength += 4;
        }
        
        // Shared Internships: Add 3 for each shared internship
        for (String internship : this.previousInternships) {
            if (otherStudent.previousInternships.contains(internship)) {
                strength += 3;
            }
        }
        
        // Same Major: Add 2 if they share the same major
        if (this.major.equals(otherStudent.major)) {
            strength += 2;
        }
        
        // Same Age: Add 1 if they are the same age
        if (this.age == otherStudent.age) {
            strength += 1;
        }
        
        return strength;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        UniversityStudent that = (UniversityStudent) obj;
        return Objects.equals(name, that.name);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }





    public String toString() {
        return "UniversityStudent{name='" + name + "', age=" + age + ", gender='" + gender + 
               "', year=" + year + ", major='" + major + "', gpa=" + gpa + 
               ", roommatePreferences=" + roommatePreferences + 
               ", previousInternships=" + previousInternships + "}";
    }


}
