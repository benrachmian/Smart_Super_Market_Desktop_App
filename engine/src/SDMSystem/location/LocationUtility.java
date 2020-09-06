package SDMSystem.location;

import java.awt.*;

public class LocationUtility {

    private LocationUtility(){}
    public static float calcDistance(Point p1, Point p2){
        int a = Math.abs(p1.x - p2.x);
        int b = Math.abs(p1.y - p2.y);
        double aPower2 = Math.pow(a, 2);
        double bPower2 = Math.pow(b, 2);
        return (float) Math.sqrt(aPower2 + bPower2);
    }

    public static String locationToString(Point location){
        return "X = " + location.x + " Y = " + location.y;
    }
}
