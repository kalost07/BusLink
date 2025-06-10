import java.util.ArrayList;

class Route {
    private int id;
    private String name;
    private String type;
    private ArrayList<Stop> stops = new ArrayList<>();

    Route() {
    }

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