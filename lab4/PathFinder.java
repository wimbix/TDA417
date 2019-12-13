
import java.util.*;

import java.util.stream.Collectors;


public class PathFinder<V> {

    private DirectedGraph<V> graph;
    private long startTimeMillis;


    public PathFinder(DirectedGraph<V> graph) {
        this.graph = graph;
    }


    public class Result<V> {
        public final boolean success;
        public final V start;
        public final V goal;
        public final double cost;
        public final List<V> path;
        public final int visitedNodes;
        public final double elapsedTime;

        public Result(boolean success, V start, V goal, double cost, List<V> path, int visitedNodes) {
            this.success = success;
            this.start = start;
            this.goal = goal;
            this.cost = cost;
            this.path = path;
            this.visitedNodes = visitedNodes;
            this.elapsedTime = (System.currentTimeMillis() - startTimeMillis) / 1000.0;
        }

        public String toString() {
            String s = "";
            s += String.format("Visited nodes: %d\n", visitedNodes);
            s += String.format("Elapsed time: %.1f seconds\n", elapsedTime);
            if (success) {
                s += String.format("Total cost from %s -> %s: %s\n", start, goal, cost);
                s += "Path: " + path.stream().map(Object::toString).collect(Collectors.joining(" -> "));
            } else {
                s += String.format("No path found from %s", start);
            }
            return s;
        }
    }


    public Result<V> search(String algorithm, V start, V goal) {
        startTimeMillis = System.currentTimeMillis();
        switch (algorithm) {
        case "random":   return searchRandom(start, goal);
        case "dijkstra": return searchDijkstra(start, goal);
        case "astar":    return searchAstar(start, goal);
        }
        throw new IllegalArgumentException("Unknown search algorithm: " + algorithm);
    }


    public Result<V> searchRandom(V start, V goal) {
        int visitedNodes = 0;
        LinkedList<V> path = new LinkedList<>();
        double cost = 0.0;
        Random random = new Random();

        V current = start;
        path.add(current);
        while (current != null) {
            visitedNodes++;
            if (current.equals(goal)) {
                return new Result<>(true, start, current, cost, path, visitedNodes);
            }

            List<DirectedEdge<V>> neighbours = graph.outgoingEdges(start);
            if (neighbours == null || neighbours.size() == 0) {
                break;
            } else {
                DirectedEdge<V> edge = neighbours.get(random.nextInt(neighbours.size()));
                cost += edge.weight();
                current = edge.to();
                path.add(current);
            }
        }
        return new Result<>(false, start, null, -1, null, visitedNodes);
    }


    public Result<V> searchDijkstra(V start, V goal) {
        int visitedNodes = 0;
        /********************
         * TODO: Task 1 
         ********************/

        HashMap<V, Double> distance = new HashMap<>();
        HashMap<V, V> previous = new HashMap<>();
        List<V> visitedNodesList = new ArrayList<>();
        PriorityQueue<DirectedEdge<V>> pq = new PriorityQueue<>(Comparator.comparing(x -> x.weight()));

        for (DirectedEdge edge: graph.outgoingEdges(start)) {
            if (edge.weight() < 0) {
                throw new IllegalArgumentException("Edge has a negative weight");
            }
        }


        previous.put(start, null);
        distance.put(start, 0.0);

        pq.add(new DirectedEdge<>(start, start));

        while (!pq.isEmpty()) {
            V current = pq.poll().to();
            if(!visitedNodesList.contains(current)) {
                visitedNodesList.add(current);
            }

            if(current.equals(goal)) {
                List<V> path = new ArrayList<>();
                while (current != start) {
                    path.add(current);
                    current = previous.get(current);
                }
                visitedNodes = visitedNodesList.size();

                return new Result<>(true, start, goal, distance.get(goal), path, visitedNodes);
            }

            for(DirectedEdge<V> edge: graph.outgoingEdges(current)) {

                V edgeTo = edge.to();

                if(distance.get(edgeTo) == null) {
                    distance.put(edgeTo, distance.get(current) + edge.weight());
                    previous.put(edgeTo, current);
                    pq.add(edge);
                } else if (distance.get(edgeTo) > distance.get(current) + edge.weight()) {
                    distance.put(edgeTo, distance.get(current) + edge.weight());
                    previous.put(edgeTo, current);
                    pq.add(edge);
                }
            }

        }

        return new Result<>(false, start, null, -1, null, visitedNodes);
    }
    

    public Result<V> searchAstar(V start, V goal) {
        int visitedNodes = 0;
        /********************
         * TODO: Task 3
         ********************/
        return new Result<>(false, start, null, -1, null, visitedNodes);
    }

}
