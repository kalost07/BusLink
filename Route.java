import java.util.ArrayList;

class Route {
    private final int id;
    private final String name;
    private final String type;
    private final ArrayList<Stop> stops;

    Route(int id, String name, String type, ArrayList<Stop> arr) {
        this.name = name;
        this.id = id;
        this.type = type;
        stops = arr;
    }

    public ArrayList<Stop> getStops() {return stops;}
    public String getName() {return name;}
    public int getId() {return id;}
    public String getType() {return type;}

    @Override
    public String toString() {
        return type + " " + name;
    }
}