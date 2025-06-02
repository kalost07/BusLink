import java.util.*;
import java.io.*;

public class PTNetwork {
    Map<Integer, Station> stationMap = new HashMap<>();
    Map<String, Line> lineMap = new HashMap<>();

    PTNetwork (String stationFile, String lineFile) {
        // Enter stations in map
        try(Scanner in = new Scanner(new BufferedReader(new FileReader(stationFile)))) {
            int id;
            String name;
            while (in.hasNext()) {
                id = in.nextInt();
                name = in.nextLine();
                stationMap.put(id, new Station(id, name));
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        // Enter lines in map
        // Puts two lines for both directions
        try(Scanner in = new Scanner(new BufferedReader(new FileReader(lineFile)))) {
            String name;
            while (in.hasNext()) {
                name = in.nextLine();
                ArrayList<Integer> stations1 = parseLine(in.nextLine());
                ArrayList<Integer> stations2 = parseLine(in.nextLine());
                lineMap.put(name, new Line(name, stations1));
                lineMap.put("-" + name, new Line("-" + name, stations2));
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
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
}

class Station {
    int id;
    String name;

    Station() {
        id = 0;
        name = null;
    };
    Station(int id, String name) {
        this.id = id;
        this.name = name;
    }

    int getId() {return id;}
}

class Line {
    String name;
    ArrayList<Integer> stations = new ArrayList<>();

    Line() {
    }

    ;

    Line(String name, ArrayList<Integer> arr) {
        this.name = name;
        stations = arr;
    }

    String getName() {
        return name;
    }
}
