/**
 * Created by Dominik on 13.05.2016.
 */

import com.sun.org.apache.xml.internal.resolver.Catalog;
import com.tinkerforge.IPConnection;
import com.tinkerforge.BrickletDistanceUS;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class Main {


    private static final String HOST = "localhost";
    private static final int PORT = 4223;
    private static final String UID = "q9m"; // Change to your UID
    private static final String UID2= "jAY";


    public static int dist1 = 0;
    public static int dist2 = 0;
    public static long time1 = 0;
    public static long time2 = 0;
    public static double speed1 = 0;
    public static double speed2 = 0;
    public static long deltaZeit1 = 0;
    public static long deltaZeit2 = 0;

    private static double zero = 0;

    private static Map<Long, Integer> distMap = new HashMap<Long, Integer>();
    private static Map<Long, Integer> distMap2 = new HashMap<Long, Integer>();


    // Note: To make the example code cleaner we do not handle exceptions. Exceptions
    //       you might normally want to catch are described in the documentation
    public static void main(String args[]) throws Exception {
        IPConnection ipcon = new IPConnection(); // Create IP connection
        BrickletDistanceUS dus = new BrickletDistanceUS(UID, ipcon); // Create device object
        BrickletDistanceUS dus2 = new BrickletDistanceUS(UID2, ipcon);

        ipcon.connect(HOST, PORT); // Connect to brickd
        // Don't use device before ipcon is connected

        // Add distance value listener
        dus.addDistanceListener(new BrickletDistanceUS.DistanceListener() {
            public void distance(int distance) {
                distMap.put(System.currentTimeMillis(),distance);
            }
        });

        // Add distance value listener
        dus2.addDistanceListener(new BrickletDistanceUS.DistanceListener() {
            public void distance(int distance) {
                distMap2.put(System.currentTimeMillis(),distance);
            }
        });
        // Set period for distance value callback to 0.2s (200ms)
        // Note: The distance value callback is only called every 0.2 seconds
        //       if the distance value has changed since the last call!
        dus.setDistanceCallbackPeriod(10);
        dus2.setDistanceCallbackPeriod(10);


        System.out.println("Press key to exit");
        System.in.read();
        ipcon.disconnect();

    }
}