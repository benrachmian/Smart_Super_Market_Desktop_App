package SDMSystem.validation;

import java.awt.*;
import java.util.Collection;

import SDMSystem.exceptions.*;

public class LocationValidation {
    public static void checkLocationValidation2D(Point locationToCheck, int minRow, int maxRow, int minCol, int maxCol){
        if((locationToCheck.x <minRow || locationToCheck.x > maxRow) ||
                (locationToCheck.y < minCol || locationToCheck.y > maxCol)){
            throw new LocationNotInRangeException(minRow,maxRow,minCol,maxCol);
        }
    }

    public static boolean checkIfUniqueLocation(Point targetLocation, Collection<Point> othersLocation) {
        boolean res = true;
        for(Point location : othersLocation){
            if (location.equals(targetLocation)){
                res = false;
                break;
            }
        }

        return res;
    }
}
