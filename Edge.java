public class Edge{
    private Node target;
    private double weight;
    Edge(Node target, double weight) {this.target = target; this.weight = weight;}
    Node getTarget() {return target;}
    double getWeight() {return weight;}
}