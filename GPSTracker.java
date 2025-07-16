import javax.swing.*;
import org.gpsd.client.GpsdClient;
import org.gpsd.client.GpsdClientListener;
import org.gpsd.client.connector.GpsdConnection;
import org.gpsd.client.message.TPVObject;
import java.util.ArrayList;
import java.util.List;
   
public class EnhancedGPSTracker {
    private static JLabel coordinatesLabel;
    private static JLabel altitudeLabel;
    private static JLabel speedLabel;
    private static JLabel timeLabel; 
    private static JLabel addressLabel;
    private static List<TPVObject> gpsDataPoints = new ArrayList<>();

    public static void main(String[] args) {
        JFrame frame = new JFrame("Enhanced GPS Tracker");
        coordinatesLabel = new JLabel("Latitude: 0.000000, Longitude: 0.000000");
        altitudeLabel = new JLabel("Altitude: 0.0 meters");
        speedLabel = new JLabel("Speed: 0.0 km/h");
        timeLabel = new JLabel("Time: --:--:--");
        addressLabel = new JLabel("Address: Unknown");
        JCheckBox trackRecordingCheckbox = new JCheckBox("Record Track");
        JButton reverseGeolocationButton = new JButton("Get Address");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.add(coordinatesLabel);
        frame.add(altitudeLabel);
        frame.add(speedLabel);
        frame.add(timeLabel);
        frame.add(addressLabel);
        frame.add(trackRecordingCheckbox);
        frame.add(reverseGeolocationButton);
        frame.setSize(400, 300);
        frame.setVisible(true);

        GpsdClient gpsdClient = new GpsdClient();

        gpsdClient.addListener(new GpsdClientListener() {
            @Override
            public void handleTPV(TPVObject tpv) {
                double latitude = tpv.getLatitude();
                double longitude = tpv.getLongitude();
                double altitude = tpv.getAltitude();
                double speed = tpv.getSpeed() * 3.6;
                String time = tpv.getTime();

                updateCoordinatesLabel(latitude, longitude);
                updateAltitudeLabel(altitude);
                updateSpeedLabel(speed);
                updateTimeLabel(time);

                if (trackRecordingCheckbox.isSelected()) {
                    gpsDataPoints.add(tpv);
                }
            }
        });

        reverseGeolocationButton.addActionListener(e -> {
            if (!gpsDataPoints.isEmpty()) {
                TPVObject lastDataPoint = gpsDataPoints.get(gpsDataPoints.size() - 1);
                String address = performReverseGeolocation(lastDataPoint.getLatitude(), lastDataPoint.getLongitude());
                addressLabel.setText("Address: " + address);
            }
        });

        try {
            GpsdConnection gpsdConnection = new GpsdConnection("localhost", 2947);
            gpsdConnection.addListener(gpsdClient);
            gpsdConnection.start();
        } catch (Exception e) {
            showError("Error connecting to GPSD: " + e.getMessage());
        }
    }

    private static void updateCoordinatesLabel(double latitude, double longitude) {
        String coordinatesText = String.format("Latitude: %.6f, Longitude: %.6f", latitude, longitude);
        coordinatesLabel.setText(coordinatesText);
    }

    private static void updateAltitudeLabel(double altitude) {
        String altitudeText = String.format("Altitude: %.2f meters", altitude);
        altitudeLabel.setText(altitudeText);
    }

    private static void updateSpeedLabel(double speed) {
        String speedText = String.format("Speed: %.2f km/h", speed);
        speedLabel.setText(speedText);
    }

    private static void updateTimeLabel(String time) {
        timeLabel.setText("Time: " + time);
    }

    private static String performReverseGeolocation(double latitude, double longitude) {
        // Implement reverse geolocation here
        return "Sample Address"; // Replace with actual fetched address
    }

    private static void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
