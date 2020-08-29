package SDMSystem.location;

import java.awt.*;

public interface Locationable {
    Point getLocation();
    float getDistanceFrom(Point target);
}
