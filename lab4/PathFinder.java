
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
        /********************
         * TODO: Task 1 
         ********************/

        HashMap<V, Double> distanceTo = new HashMap<>();
        HashMap<V, V> previous = new HashMap<>();
        List<V> visitedNodesList = new ArrayList<>();
        List<V> path = new LinkedList<>();
        PriorityQueue<DirectedEdge<V>> pq = new PriorityQueue<>(Comparator.comparing(x -> distanceTo.get(x.to())));

        for (DirectedEdge edge: graph.outgoingEdges(start)) {
            if (edge.weight() < 0) {
                throw new IllegalArgumentException("Edge has a negative weight");
            }
        }

        distanceTo.put(start, 0.0);

        pq.add(new DirectedEdge<>(start, start));

        while (!pq.isEmpty()) {
            V currentNode = pq.poll().to();
            if(!visitedNodesList.contains(currentNode)) {
                visitedNodesList.add(currentNode);
            }

            if(currentNode.equals(goal)) {
                while (currentNode != start) {
                    path.add(currentNode);
                    currentNode = previous.get(currentNode);
                }

                path.add(start);

                List<V> reversePath = new LinkedList<>();
                for (int i = path.size() - 1; i >= 0; i--) {
                    reversePath.add(path.get(i));
                }

                return new Result<>(true, start, goal, distanceTo.get(goal), reversePath, visitedNodesList.size());
            }

            for(DirectedEdge<V> edge: graph.outgoingEdges(currentNode)) {

                V edgeTo = edge.to();

                if(distanceTo.get(edgeTo) == null) {
                    distanceTo.put(edgeTo, distanceTo.get(currentNode) + edge.weight());
                    previous.put(edgeTo, currentNode);
                    pq.add(edge);
                } else if (distanceTo.get(edgeTo) > distanceTo.get(currentNode) + edge.weight()) {
                    distanceTo.put(edgeTo, distanceTo.get(currentNode) + edge.weight());
                    previous.put(edgeTo, currentNode);
                    pq.add(edge);
                }
            }

        }

        return new Result<>(false, start, null, -1, null, visitedNodesList.size());
    }
    

    public Result<V> searchAstar(V start, V goal) {
        /********************
         * TODO: Task 3
         ********************/

        HashMap<V, V> previous = new HashMap<>();
        HashMap<V, Double> gScore = new HashMap<>();
        HashMap<V, Double> fScore = new HashMap<>();
        List<V> visitedNodesList = new ArrayList<>();
        List<V> path = new LinkedList<>();
        PriorityQueue<DirectedEdge<V>> pq = new PriorityQueue<>(Comparator.comparing(x -> fScore.get(x.to())));

        for (DirectedEdge edge: graph.outgoingEdges(start)) {
            if (edge.weight() < 0) {
                throw new IllegalArgumentException("Edge has a negative weight");
            }
        }

        gScore.put(start, 0.0);
        fScore.put(start, graph.guessCost(start, goal));

        pq.add(new DirectedEdge<>(start, start));

        while (!pq.isEmpty()) {
            V currentNode = pq.poll().to();
            if(!visitedNodesList.contains(currentNode)) {
                visitedNodesList.add(currentNode);
            }

            if(currentNode.equals(goal)) {
                while (currentNode != start) {
                    path.add(currentNode);
                    currentNode = previous.get(currentNode);
                }

                path.add(start);

                List<V> reversePath = new LinkedList<>();
                for (int i = path.size() - 1; i >= 0; i--) {
                    reversePath.add(path.get(i));
                }

                return new Result<>(true, start, goal, gScore.get(goal), reversePath, visitedNodesList.size());
            }

            for(DirectedEdge<V> edge: graph.outgoingEdges(currentNode)) {

                V edgeTo = edge.to();

                if(fScore.get(edgeTo) == null) {
                    gScore.put(edgeTo, gScore.get(currentNode) + edge.weight());
                    previous.put(edgeTo, currentNode);
                    fScore.put(edgeTo, gScore.get(currentNode) + edge.weight() + graph.guessCost(edgeTo, goal));
                    pq.add(edge);
                } else if (gScore.get(edgeTo) > gScore.get(currentNode) + edge.weight()) {
                    gScore.put(edgeTo, gScore.get(currentNode) + edge.weight());
                    previous.put(edgeTo, currentNode);
                    fScore.put(edgeTo, gScore.get(currentNode) + edge.weight() + graph.guessCost(edgeTo, goal));
                    pq.add(edge);
                }
            }

        }

        return new Result<>(false, start, null, -1, null, visitedNodesList.size());
    }

}
