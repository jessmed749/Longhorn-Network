import java.util.*;

public class ReferralPathFinder {
    private StudentGraph graph; // Graph representing the student network
    
    public ReferralPathFinder(StudentGraph graph) {
        // Constructor
        this.graph = graph;
    }

    public List<UniversityStudent> findReferralPath(UniversityStudent start, String targetCompany) {
        // Maps to store the best know distance and previous node for path recon
        Map<UniversityStudent, Double> dist = new HashMap<>();
        Map<UniversityStudent, UniversityStudent> prev = new HashMap<>();
        Set<UniversityStudent> visited = new HashSet<>();

        //Initialize distances to infinity
        for (UniversityStudent s : graph.getAllNodes()){
            dist.put(s, Double.MAX_VALUE);
            prev.put(s, null);
        }
        dist.put(start, 0.0); // Distance to start is 0

        // Priority queue for Dijkstra's algorithm
        PriorityQueue<UniversityStudent> pq = new PriorityQueue<>(Comparator.comparingDouble(dist::get));
        pq.add(start);

        while(!pq.isEmpty()){
            UniversityStudent u = pq.poll();
            if(visited.contains(u)) {
                continue; // Skip if already visited
            }
            visited.add(u);

            for (String internship : u.previousInternships) {
                if (internship.equalsIgnoreCase(targetCompany)) {
                    // Reconstruct the path from start to u
                    List<UniversityStudent> path = new ArrayList<>();
                    UniversityStudent cur = u;
                    while(cur != null) {
                        path.add(cur);
                        cur = prev.get(cur);
                    }
                    Collections.reverse(path); // Reverse the path to get the correct order
                    return path;
                }
            }

            // Relaxation for neighbors
            for (StudentGraph.Edge edge : graph.getNeighbors(u)){
                UniversityStudent v = edge.neighbor;
                if (visited.contains(v)) {
                    continue; // Skip if already visited
                }

                double newDist = dist.get(u) + (1.0 / edge.weight); // Assuming weight is the connection strength
                if (newDist < dist.get(v)) {
                    dist.put(v, newDist);
                    prev.put(v, u);
                    pq.add(v); // Add to priority queue for further exploration
                }
            }
        }
        

        return new ArrayList<>();
    }
}
