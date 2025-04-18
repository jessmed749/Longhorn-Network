import java.util.*;

public class StudentGraph {

    public static class Edge{
        public UniversityStudent neighbor;
        public int weight;

        public Edge(UniversityStudent neighbor, int weight) {
            this.neighbor = neighbor;
            this.weight = weight;
        }

        @Override
        public String toString(){
            return "(" + neighbor.name + ", " + weight + ")";
        }
    }

    private Map<UniversityStudent, List<Edge>> adjacencyList;

    public StudentGraph(List<UniversityStudent> students) {
        adjacencyList = new HashMap<>();
        
        // Add all students as nodes
        for (UniversityStudent s : students) {
            adjacencyList.put(s, new ArrayList<>());
        }
        
        // For each unique pair of students, calculate connection strength and add edges if > 0
        for (int i = 0; i < students.size(); i++) {
            for (int j = i + 1; j < students.size(); j++) {
                UniversityStudent s1 = students.get(i);
                UniversityStudent s2 = students.get(j);
                int weight = s1.calculateConnectionStrength(s2);
                if (weight > 0) {
                    addEdge(s1, s2, weight);
                }
            }
        }
    }

    private void addEdge(UniversityStudent s1, UniversityStudent s2, int weight) {
        adjacencyList.get(s1).add(new Edge(s2, weight));
        adjacencyList.get(s2).add(new Edge(s1, weight)); // Assuming undirected graph
    }

    // Returns a list of edges (neighbors) for a given student
    public List<Edge> getNeighbors(UniversityStudent s) {
        return adjacencyList.get(s);
    }

    public Set<UniversityStudent> getAllNodes() {
        return adjacencyList.keySet();
    }

    public void displayGraph() {
        System.out.println("\nStudent Graph:");
        for (UniversityStudent s : adjacencyList.keySet()){
            System.out.println(s.name + " -> " + adjacencyList.get(s));
        }
    }
    
}
