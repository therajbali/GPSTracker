import javax.swing.*;
import org.gpsd.client.GpsdClient;
import org.gpsd.client.GpsdClientListener;
import org.gpsd.client.connector.GpsdConnection;
import org.gpsd.client.message.TPVObject;

public class GPSTracker {
    private static JLabel coordinatesLabel;

    public static void main(String[] args) {
        // Create the GUI frame and components
        JFrame frame = new JFrame("GPS Tracker");
        coordinatesLabel = new JLabel("Latitude: 0.000000, Longitude: 0.000000");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 100);
        frame.add(coordinatesLabel);
        frame.setVisible(true);

        // GPS client setup
        GpsdClient gpsdClient = new GpsdClient();

        gpsdClient.addListener(new GpsdClientListener() {
            @Override
            public void handleTPV(TPVObject tpv) {
                double latitude = tpv.getLatitude();
                double longitude = tpv.getLongitude();
                updateCoordinatesLabel(latitude, longitude);
            }
        });

        try {
            GpsdConnection gpsdConnection = new GpsdConnection("localhost", 2947);
            gpsdConnection.addListener(gpsdClient);
            gpsdConnection.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void updateCoordinatesLabel(double latitude, double longitude) {
        // Update the label with new GPS coordinates
        String coordinatesText = String.format("Latitude: %.6f, Longitude: %.6f", latitude, longitude);
        coordinatesLabel.setText(coordinatesText);
    }
}
