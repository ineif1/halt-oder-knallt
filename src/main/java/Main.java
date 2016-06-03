/**
 * Created by Dominik on 13.05.2016.
 */

import com.sun.org.apache.xml.internal.resolver.Catalog;
import com.tinkerforge.IPConnection;
import com.tinkerforge.BrickletDistanceUS;


public class Main {


    private static final String HOST = "localhost";
    private static final int PORT = 4223;
    private static final String UID = "q9m"; // Change to your UID


    public static int dist1 = 0;
    public static int dist2 = 0;
    public static long time1 = 0;
    public static long time2 = 0;
    public static double speed1 = 0;
    public static double speed2 = 0;
    public static long deltaZeit1 = 0;
    public static long deltaZeit2 = 0;

    private static double zero = 0;


    // Note: To make the example code cleaner we do not handle exceptions. Exceptions
    //       you might normally want to catch are described in the documentation
    public static void main(String args[]) throws Exception {
        IPConnection ipcon = new IPConnection(); // Create IP connection
        BrickletDistanceUS dus = new BrickletDistanceUS(UID, ipcon); // Create device object


        ipcon.connect(HOST, PORT); // Connect to brickd
        // Don't use device before ipcon is connected

        // Add distance value listener
        dus.addDistanceListener(new BrickletDistanceUS.DistanceListener() {
            public void distance(int distance) {

                System.out.println("my run dist " + distance);
                //noch keine Messungen
                if (dist1 == 0){
                    System.out.println("erste Messung");
                    dist1 = distance;
                    time1 = System.currentTimeMillis()/1000;
                    //eine Messung vorhanden
                } else if (dist2 == 0){
                    System.out.println("zweite Messung");
                    //if ((dist1 - 25) > distance && (dist1 - 700) < distance) {
                        System.out.println("zweite gültige Messung");
                        dist2 = distance;
                        time2 = System.currentTimeMillis()/1000;

                        if (speed1 == 0) {
                            int weg = dist2 - dist1;
                            deltaZeit1 = time2 - time1;
                            speed1 = weg / deltaZeit1;
                            System.out.println("Erste Geschwindigkeitsmessung: " + speed1);
                        } else if (speed2 == 0) {
                            int weg = dist2 - dist1;
                            deltaZeit2 =  time2 - time1;
                            speed2 = weg / deltaZeit2;

                            System.out.println("Delta Geschwindigkeit: " + speed1 + " neu " + speed2);

                            double acceleration = (speed2 - speed1)/((time2 + deltaZeit2/2)-(time1 + deltaZeit1/2));
                            System.out.println("Beschleunigung: " + acceleration);

                            double timeZero = ((0-speed2)/acceleration);

                            double distToZero = speed2*timeZero + 0.5*acceleration*timeZero*timeZero;

                            System.out.println("Crash bei: " + ((dist1 + weg/2)-distToZero)/1000);
                            if(dist1+weg/2>=distToZero){
                                System.out.println("Wird Crashen");
                            }else{
                                System.out.println("Wird NICHT Crashen");
                            }

                            deltaZeit1 = deltaZeit2;
                            deltaZeit2 = 0;

                            speed1 = speed2;
                            speed2 = 0;

                            time1 = time2;
                            time2 = 0;
                        }
                    //}
                    dist1 = dist2;
                    dist2 = 0;
                }





        }
        });

        // Set period for distance value callback to 0.2s (200ms)
        // Note: The distance value callback is only called every 0.2 seconds
        //       if the distance value has changed since the last call!
        dus.setDistanceCallbackPeriod(1000);

        System.out.println("Press key to exit");
        System.in.read();
        ipcon.disconnect();

    }
}