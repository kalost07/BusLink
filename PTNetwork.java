import java.util.*;
import java.io.*;

public class PTNetwork {
    private final Map<Integer, Stop> stopMap = new HashMap<>();
    private final Map<Integer, Route> routeMap = new HashMap<>();

    private final Map<Node, Set<Edge>> edges = new HashMap<>();
    static final int TRANSFER_PENALTY = 5;
    static final double DISTANCE_MULTIPLIER = 0.06;
    static final int MAX_WALKABLE_DISTANCE = 500;

    Map<Integer, Stop> getStopMap(){return stopMap;}
    Map<Integer, Route> getRouteMap(){return routeMap;}

    PTNetwork (String stopFile, String routeFile) {
        // Enter stops in map
        try(Scanner in = new Scanner(new BufferedReader(new FileReader(stopFile)))) {
            int id;
            String name;
            double lon, lat;
            while (in.hasNext()) {
                id = in.nextInt();
                name = in.nextLine().strip();
                lon = in.nextDouble();
                lat = in.nextDouble();
                stopMap.put(id, new Stop(id, name, lon, lat));
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        // Enter lines in map
        // Puts two lines for both directions
        int id = 0;
        try(Scanner in = new Scanner(new BufferedReader(new FileReader(routeFile)))) {
            String name;
            String type;
            while (in.hasNext()) {
                type = in.next();
                name = in.nextLine().strip();
                // System.out.println(type + " " + name);
                ArrayList<Stop> stops1 = new ArrayList<>();
                parseLine(in.nextLine()).forEach(stopId -> {
                    Stop stop = stopMap.get(stopId);
                    if(stop == null) return;
                    stops1.add(stop);
                });
                ArrayList<Stop> stops2 = new ArrayList<>();
                parseLine(in.nextLine()).forEach(stopId -> {
                    Stop stop = stopMap.get(stopId);
                    if(stop == null) return;
                    stops2.add(stop);
                });
                routeMap.put(++id, new Route(id, name + "-1", type, stops1));
                routeMap.put(++id, new Route(id, name + "-2", type, stops2));
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        //add route information to stops
        for (Route route : routeMap.values()) {
            ArrayList<Stop> stops = route.getStops();
            for (Stop stop : stops) {
                stop.addRoute(route);
            }
        }
        //build graph nodes & transfer edges
        for (Stop stop : stopMap.values()) {
            Set<Edge> hubEdgeSet = new HashSet<>();
            edges.put(stop.getHubNode(), hubEdgeSet);
            for (Route route: stop.getRoutes()) {
                Node node = new Node(stop, route);
                Set<Edge> edgeSet = new HashSet<>();
                edges.put(node, edgeSet);

                edgeSet.add(new Edge(stop.getHubNode(), TRANSFER_PENALTY)); // penalty to enter hub
                hubEdgeSet.add(new Edge(node, 0)); // free edge to leave hub
            }
        }
        //add walkable edges
        for (Stop stop1 : stopMap.values()) {
            for (Stop stop2 : stopMap.values()) {
                double dist = Stop.getDist(stop1, stop2);
                if (dist < MAX_WALKABLE_DISTANCE) {
                    edges.get(stop1.getHubNode()).add(new Edge(stop2.getHubNode(),dist * DISTANCE_MULTIPLIER));
                }
            }
        }
        //add edges of graph
        for (Route route : routeMap.values()) {
            ArrayList<Stop> stops = route.getStops();
            for (int i = 0; i < stops.size() - 1; i++) {
                edges.get(new Node(stops.get(i), route)).add(new Edge(new Node(stops.get(i+1), route), 1));
            }
        }
    }

    private static ArrayList<Integer> parseLine(String line) {
        ArrayList<Integer> result = new ArrayList<>();
        Scanner lineScanner = new Scanner(line);
        while (lineScanner.hasNextInt()) {
            result.add(lineScanner.nextInt());
        }
        lineScanner.close();
        return result;
    }

    ArrayList<Node> simpleSearch(Stop start, Stop end) {
        // Uses Dijkstra's algorithm

        Map<Node, Double> visited = new HashMap<>(); // holds the least distance to each node
        Map<Node, Node> parents = new HashMap<>(); // holds preceding node for each node
        Queue<Node> queue = new LinkedList<>(); // holds nodes to be processed

        // Add hub of start stop
        queue.add(start.getHubNode());
        visited.put(queue.peek(), 0.);
        parents.put(queue.peek(), null);

        // Dijkstra
        while (!queue.isEmpty()) {
            Node curr = queue.remove();
            double dist = visited.get(curr);
            for(Edge edge : edges.get(curr)) {
                Node target = edge.getTarget();
                double newDist = visited.get(curr) + edge.getWeight();
                if(!visited.containsKey(target) || visited.get(target) > newDist) {
                    visited.put(target, newDist);
                    queue.add(target);
                    parents.put(target, curr);
                }
            }
        }
        // Reconstruction of the path
        Node endNode = end.getHubNode();
        if (visited.get(endNode) == null) {
            return new ArrayList<>();
        }

        LinkedList<Node> path = new LinkedList<>();
        while (endNode != null) {
            path.addFirst(endNode);
            endNode = parents.get(endNode);
        }
        return new ArrayList<>(path);
    }
    ArrayList<Node> simpleSearch(int start, int end) {
        return simpleSearch(stopMap.get(start), stopMap.get(end));
    }

}