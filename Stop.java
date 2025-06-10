import java.util.*;

public class Stop {
    private int id;
    private String name;
    private double lon;
    private double lat;
    private Set<Route> routes = new HashSet<>();
    private Node hubNode = new Node(this, null);

    Stop() {
        id = 0;
        name = null;
    };
    Stop(int id, String name, double lon, double lat) {
        this.id = id;
        this.name = name;
        this.lon = lon;
        this.lat = lat;
    }

    int getId() {return id;}
    String getName() {return name;}
    Set<Route> getRoutes() {return routes;}
    void addRoute(Route route) {
        routes.add(route);
    }
    Node getHubNode() {return hubNode;}
    @Override
    public String toString() {
        return String.format("%04d", id) + " " + name;
    }

    static double getDist(Stop s1, Stop s2) {
        double dx = Math.abs(s1.lon - s2.lon) * 111000; // 1 deg lon is ~111000 m
        double dy = Math.abs(s1.lat - s2.lat) * 85000; // 1 deg lat is ~85000 m at 42.7 lon
        return Math.sqrt(dx * dx + dy * dy);
    }
    static double getDist(Node n1, Node n2) {
        return getDist(n1.getStop(), n2.getStop());
    }
}