import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.Timer;

public class Main {
    public static void main(String[] args) {
        PTNetwork ptNetwork = new PTNetwork("stops.txt", "routes.txt");

        JFrame frame = new JFrame("BusLink");
        frame.setLayout(new BorderLayout());
        frame.setSize(800, 600);

        JPanel northPanel = new JPanel();
        northPanel.setLayout(new GridLayout(2,1));

        JPanel northSubPanel = new JPanel(new GridLayout(1,2));

        // Make a timer that calls showPopup() and filters the active dropDown menu when typing
        Timer debounceTimer = new Timer(300, null);
        Stop[] stops = ptNetwork.getStopMap().values().toArray(new Stop[0]);

        final JComboBox<Stop>[] currentActiveCombo = new JComboBox[]{null};

        JComboBox<Stop> dropDown1 = new JComboBox<>(stops);
        JComboBox<Stop> dropDown2 = new JComboBox<>(stops);
        dropDown1.setEditable(true);
        dropDown2.setEditable(true);
        dropDown1.getEditor().getEditorComponent().addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                currentActiveCombo[0] = dropDown1;
            }
        });
        dropDown2.getEditor().getEditorComponent().addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                currentActiveCombo[0] = dropDown2;
            }
        });
        debounceTimer.addActionListener(e -> {
            if (currentActiveCombo[0] != null && currentActiveCombo[0].getEditor().getEditorComponent().hasFocus()) {
                JTextField editor = (JTextField) currentActiveCombo[0].getEditor().getEditorComponent();
                String input = editor.getText().toUpperCase();

                List<Stop> filtered = Arrays.stream(stops)
                        .filter(item -> item.toString().toUpperCase().contains(input))
                        .toList();

                DefaultComboBoxModel<Stop> model = new DefaultComboBoxModel<>();
                for (Stop item : filtered) model.addElement(item);
                currentActiveCombo[0].setModel(model);
                currentActiveCombo[0].setSelectedItem(input);
                for (Stop stop : filtered) {
                    if (stop.toString().equalsIgnoreCase(input)) {
                        currentActiveCombo[0].setSelectedItem(stop);
                        break;
                    }
                }
                currentActiveCombo[0].showPopup();
            }
        });
        debounceTimer.start();

        northSubPanel.add(dropDown1);
        northSubPanel.add(dropDown2);
        northPanel.add(northSubPanel);

        // Output
        JLabel resultLabel = new JLabel("Result");
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JScrollPane resultScrollPane = new JScrollPane(resultLabel);
        frame.add(resultScrollPane, BorderLayout.CENTER);

        JLabel summaryLabel = new JLabel("Summary");
        summaryLabel.setHorizontalAlignment(SwingConstants.CENTER);
        frame.add(summaryLabel, BorderLayout.SOUTH);

        JButton findRouteButton = new JButton("Find Route");
        findRouteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Stop stop1 = (Stop)dropDown1.getSelectedItem();
                Stop stop2 = (Stop)dropDown2.getSelectedItem();
                if (stop1 == null || stop2 == null) {return;}
                ArrayList<Node> path = ptNetwork.simpleSearch(stop1, stop2);
                // Debug purposes
                for (Node node : path) {
                    System.out.println(node);
                }
                updateLabels(resultLabel, summaryLabel, path);

            }
        });
        northPanel.add(findRouteButton);

        frame.add(northPanel, BorderLayout.NORTH);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                debounceTimer.stop();
            }
        });
        frame.setVisible(true);
        System.out.println("comboBox1 isDisplayable: " + dropDown1.isDisplayable());
        System.out.println("comboBox2 isDisplayable: " + dropDown2.isDisplayable());
    }

    static void updateLabels(JLabel resultLabel, JLabel sumLabel, ArrayList<Node> path) {
        if(path.isEmpty()) {
            resultLabel.setText("No Routes Found");
            sumLabel.setText("No Routes Found");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        Node curr = path.getFirst();
        Node prev = curr;
        Node prev2 = prev;
        int walkDist = 0;
        int pathDist = 0;
        int numStops = 0;
        sb.append(String.format("Start at %s<br>", colorCode(curr.getStop())));
        for (int i = 1; i < path.size(); i++) {
            curr = path.get(i);
            if (curr.getRoute() == null && prev.getRoute() == null) { // walking edge
                int dist = (int)(Stop.getDist(prev, curr));
                walkDist += dist;
                pathDist--;
                sb.append(String.format("Walk to %s (%d meters)<br>", colorCode(curr.getStop()), dist));
            }
            if (curr.getRoute() != null && prev.getRoute() == null) { // start new ride
                pathDist += PTNetwork.TRANSFER_PENALTY - 1;
                numStops = 0;
                if (prev2.getRoute() != null) // Transfer edge(s)
                    sb.append(String.format("Transfer to %s<br>", colorCode(curr.getRoute())));
            }
            if (curr.getRoute() == null && prev.getRoute() != null) { // Route ended
                sb.append(String.format("Ride to %s on %s (%d stop%s)<br>"
                        , colorCode(curr.getStop()), colorCode(prev.getRoute())
                        , numStops - 1, numStops - 1 == 1? "":"s"));
                pathDist--;
            }
            pathDist ++;
            numStops++;
            prev2 = prev;
            prev = curr;
        }
        pathDist += (int) (PTNetwork.DISTANCE_MULTIPLIER * walkDist);
        sb.append("</html>");
        resultLabel.setText(sb.toString());

        sb = new StringBuilder();
        sb.append(String.format("""
                <html>
                Total walking distance: %d meters<br>
                Total path distance: %d units</html>"""
                , walkDist, pathDist));
        sumLabel.setText(sb.toString());
    }
    // Control the coloring of those elements in text
    static String colorCode(Stop stop) {
        return "<font color='orange'>" + String.format("%04d", stop.getId()) + "</font>" +
                "<font color='red'> " + stop.getName() + "</font>";
    }
    static String colorCode(Route route) {
        return "<font color='blue'>" + route.toString() + "</font>";
    }
}