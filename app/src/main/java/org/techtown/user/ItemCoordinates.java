package org.techtown.user;

import android.content.Intent;
import android.graphics.Point;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class ItemCoordinates {
    private final Map<String, Point> itemCoordinates;

    public ItemCoordinates() {
        itemCoordinates = new HashMap<>();

        // 목적지 목록
        itemCoordinates.put("414", new Point(12, 90));
        itemCoordinates.put("400_eli1", new Point(18, 46));
        itemCoordinates.put("4_terrace", new Point(20, 50));
        itemCoordinates.put("400_art", new Point(5, 4));
        itemCoordinates.put("406", new Point(50, 126));
        itemCoordinates.put("407", new Point(55, 132));
        itemCoordinates.put("400_eli3", new Point(21, 122));
        itemCoordinates.put("413", new Point(12, 105));
        itemCoordinates.put("411", new Point(13, 131));
        itemCoordinates.put("500_eli2", new Point(15, 47));
        itemCoordinates.put("cube_n", new Point(37, 81));
        itemCoordinates.put("cube_s", new Point(12, 90));
        itemCoordinates.put("506", new Point(50, 126));
        itemCoordinates.put("508", new Point(40, 132));
        itemCoordinates.put("510", new Point(19, 132));
        itemCoordinates.put("500_eli3", new Point(21, 122));
        itemCoordinates.put("513", new Point(12, 108));
        itemCoordinates.put("500_eli1", new Point(16, 6));

        // 현재 위치 표시 위한 지점  ( 4층 )
        itemCoordinates.put("400_eli2", new Point(15, 47));
        itemCoordinates.put("425", new Point(12, 13));
        itemCoordinates.put("424", new Point(12, 19));
        itemCoordinates.put("423", new Point(12, 24));
        itemCoordinates.put("422", new Point(12, 29));
        itemCoordinates.put("421", new Point(12, 33));
        itemCoordinates.put("420", new Point(12, 38));
        itemCoordinates.put("419", new Point(12, 43));
        itemCoordinates.put("418", new Point(12, 48));
        itemCoordinates.put("417", new Point(12, 54));
        itemCoordinates.put("416", new Point(12, 58));
        itemCoordinates.put("415", new Point(12, 75));
        itemCoordinates.put("412", new Point(12, 122));

        itemCoordinates.put("410", new Point(19, 132));
        itemCoordinates.put("409", new Point(30, 132));
        itemCoordinates.put("408", new Point(40, 132));

        itemCoordinates.put("405", new Point(47, 116));
        itemCoordinates.put("404", new Point(43, 101));
        itemCoordinates.put("403", new Point(39, 87));
        itemCoordinates.put("402", new Point(35, 74));
        itemCoordinates.put("401", new Point(33, 67));

        itemCoordinates.put("435", new Point(30, 56));
        itemCoordinates.put("434", new Point(29, 52));
        itemCoordinates.put("433", new Point(27, 46));
        itemCoordinates.put("432", new Point(26, 40));
        itemCoordinates.put("431", new Point(25, 36));
        itemCoordinates.put("430", new Point(24, 32));
        itemCoordinates.put("429", new Point(22, 27));
        itemCoordinates.put("428", new Point(21, 22));
        itemCoordinates.put("427", new Point(19, 16));
        itemCoordinates.put("426", new Point(18, 12));

        // 현재 위치 표시 위한 지점  ( 5층 )
        itemCoordinates.put("525", new Point(12, 13));
        itemCoordinates.put("524", new Point(12, 19));
        itemCoordinates.put("523", new Point(12, 26));
        itemCoordinates.put("522", new Point(12, 32));
        itemCoordinates.put("521", new Point(12, 46));
        itemCoordinates.put("520", new Point(12, 52));
        itemCoordinates.put("519", new Point(12, 59));

        itemCoordinates.put("518", new Point(12, 74));
        itemCoordinates.put("517", new Point(12, 87));
        itemCoordinates.put("516", new Point(12, 93));
        itemCoordinates.put("515", new Point(12, 103));
        itemCoordinates.put("514", new Point(12, 109));
        itemCoordinates.put("512", new Point(12, 122));

        itemCoordinates.put("511", new Point(13, 131));
        itemCoordinates.put("509", new Point(30, 132));
        itemCoordinates.put("507", new Point(55, 132));

        itemCoordinates.put("505", new Point(47, 116));
        itemCoordinates.put("504", new Point(43, 101));
        itemCoordinates.put("503", new Point(39, 87));
        itemCoordinates.put("502", new Point(35, 74));
        itemCoordinates.put("501", new Point(33, 67));

        itemCoordinates.put("532", new Point(29, 53));
        itemCoordinates.put("531", new Point(28, 48));
        itemCoordinates.put("530", new Point(26, 39));
        itemCoordinates.put("529", new Point(22, 27));
        itemCoordinates.put("528", new Point(21, 23));
        itemCoordinates.put("527", new Point(20, 19));
        itemCoordinates.put("526", new Point(18, 12));
    }

    public Point getEndCoordinate(String item) {
        Point endCoordinate = itemCoordinates.get(item);
        assert endCoordinate != null;
        return new Point(endCoordinate.x, endCoordinate.y);
    }

    public Point getStartCoordinate(String item) {
        Point startCoordinate = itemCoordinates.get(item);
        if (startCoordinate == null) return new Point(12, 90);
        return new Point(startCoordinate.x, startCoordinate.y);
    }

    public Point getNowCoordinate(String item, Intent intent) {
        Point nowCoordinate = itemCoordinates.get(item);
//        assert nowCoordinate != null;

        if(nowCoordinate == null) {
            int X = intent.getIntExtra("startX", 0);
            int Y = intent.getIntExtra("startY", 0);
            Log.d("POINT_X", String.valueOf(X));
            Log.d("POINT_Y", String.valueOf(Y));
            return new Point(X, Y);
        }
        Log.d("NORMALPOINT", "");
        return new Point(nowCoordinate.x, nowCoordinate.y);
    }
}


