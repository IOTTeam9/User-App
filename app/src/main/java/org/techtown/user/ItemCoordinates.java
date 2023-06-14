package org.techtown.user;

import android.graphics.Point;

import java.util.HashMap;
import java.util.Map;

public class ItemCoordinates {
    private Map<String, Point> itemCoordinates;

    public ItemCoordinates() {
        itemCoordinates = new HashMap<>();
        itemCoordinates.put("414호", new Point(12, 90));
        itemCoordinates.put("4층 엘레베이터1", new Point(18, 46));
        itemCoordinates.put("4층 중앙", new Point(20, 50));
        itemCoordinates.put("4층 아르테크네", new Point(5, 4));
        itemCoordinates.put("406호", new Point(50, 126));
        itemCoordinates.put("407호", new Point(55, 132));
        itemCoordinates.put("4층 엘레베이터2", new Point(21, 122));
        itemCoordinates.put("413호", new Point(12, 105));
        itemCoordinates.put("4층 계단", new Point(13, 131));
        itemCoordinates.put("5층 엘레베이터1", new Point(15, 47));
        itemCoordinates.put("5층 중앙1", new Point(37, 81));
        itemCoordinates.put("5층 중앙2", new Point(12, 90));
        itemCoordinates.put("506호", new Point(50, 126));
        itemCoordinates.put("508호", new Point(40, 132));
        itemCoordinates.put("510호", new Point(19, 132));
        itemCoordinates.put("5층 엘레베이터2", new Point(21, 122));
        itemCoordinates.put("513호", new Point(12, 108));
        itemCoordinates.put("5층 엘레베이터3", new Point(16, 6));
    }

    public Point getEndCoordinate(String item) {
        Point endCoordinate = itemCoordinates.get(item);
        return new Point(endCoordinate.x, endCoordinate.y);
    }
}


