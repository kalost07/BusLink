public class Edge{
    private final Node target;
    private final double weight;
    Edge(Node target, double weight) {this.target = target; this.weight = weight;}
    Node getTarget() {return target;}
    double getWeight() {return weight;}
}