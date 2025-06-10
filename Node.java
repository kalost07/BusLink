import java.util.Objects;

public class Node{
    private Stop stop;
    private Route route;
    Node() {stop = null;route = null;}
    Node(Stop stop, Route route) {this.stop = stop; this.route = route;}
    @Override
    public int hashCode() {
        return Objects.hash(stop, route);
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj.getClass() != Node.class) return false;
        Node other = (Node) obj;
        return stop == other.stop && route == other.route;
    }
    Stop getStop() {return stop;}
    Route getRoute() {return route;}

    @Override
    public String toString() {
        return "Node [stop id = " + stop.getId() + ", route = " + route + "]";
    }
}