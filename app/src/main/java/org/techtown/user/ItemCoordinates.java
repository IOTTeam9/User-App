package org.techtown.user;

import android.graphics.Point;

import java.util.HashMap;
import java.util.Map;

public class ItemCoordinates {
    private Map<String, Point> itemCoordinates;

    public ItemCoordinates() {
        itemCoordinates = new HashMap<>();

        // 목적지 목록
        itemCoordinates.put("414호", new Point(12, 90));
        itemCoordinates.put("4층 엘레베이터1", new Point(18, 46));
        itemCoordinates.put("4층 중앙", new Point(20, 50));
        itemCoordinates.put("4층 아르테크네", new Point(5, 4));
        itemCoordinates.put("406호", new Point(50, 126));
        itemCoordinates.put("407호", new Point(55, 132));
        itemCoordinates.put("4층 엘레베이터2", new Point(21, 122));
        itemCoordinates.put("413호", new Point(12, 105));
        itemCoordinates.put("411호", new Point(13, 131));
        itemCoordinates.put("5층 엘레베이터1", new Point(15, 47));
        itemCoordinates.put("5층 중앙1", new Point(37, 81));
        itemCoordinates.put("5층 중앙2", new Point(12, 90));
        itemCoordinates.put("506호", new Point(50, 126));
        itemCoordinates.put("508호", new Point(40, 132));
        itemCoordinates.put("510호", new Point(19, 132));
        itemCoordinates.put("5층 엘레베이터2", new Point(21, 122));
        itemCoordinates.put("513호", new Point(12, 108));
        itemCoordinates.put("5층 엘레베이터3", new Point(16, 6));

        // 현재 위치 표시 위한 지점  ( 4층 )
        itemCoordinates.put("425호", new Point(12, 13));
        itemCoordinates.put("424호", new Point(12, 19));
        itemCoordinates.put("423호", new Point(12, 24));
        itemCoordinates.put("422호", new Point(12, 29));
        itemCoordinates.put("421호", new Point(12, 33));
        itemCoordinates.put("420호", new Point(12, 38));
        itemCoordinates.put("419호", new Point(12, 43));
        itemCoordinates.put("418호", new Point(12, 48));
        itemCoordinates.put("417호", new Point(12, 54));
        itemCoordinates.put("416호", new Point(12, 58));
        itemCoordinates.put("415호", new Point(12, 75));
        itemCoordinates.put("412호", new Point(12, 122));

        itemCoordinates.put("410호", new Point(19, 132));
        itemCoordinates.put("409호", new Point(30, 132));
        itemCoordinates.put("408호", new Point(40, 132));

        itemCoordinates.put("405호", new Point(47, 116));
        itemCoordinates.put("404호", new Point(43, 101));
        itemCoordinates.put("403호", new Point(39, 87));
        itemCoordinates.put("402호", new Point(35, 74));
        itemCoordinates.put("401호", new Point(33, 67));

        itemCoordinates.put("435호", new Point(30, 56));
        itemCoordinates.put("434호", new Point(29, 52));
        itemCoordinates.put("433호", new Point(27, 46));
        itemCoordinates.put("432호", new Point(26, 40));
        itemCoordinates.put("431호", new Point(25, 36));
        itemCoordinates.put("430호", new Point(24, 32));
        itemCoordinates.put("429호", new Point(22, 27));
        itemCoordinates.put("428호", new Point(21, 22));
        itemCoordinates.put("427호", new Point(19, 16));
        itemCoordinates.put("426호", new Point(18, 12));

        // 현재 위치 표시 위한 지점  ( 5층 )
        itemCoordinates.put("525호", new Point(12, 13));
        itemCoordinates.put("524호", new Point(12, 19));
        itemCoordinates.put("523호", new Point(12, 26));
        itemCoordinates.put("522호", new Point(12, 32));
        itemCoordinates.put("521호", new Point(12, 46));
        itemCoordinates.put("520호", new Point(12, 52));
        itemCoordinates.put("519호", new Point(12, 59));

        itemCoordinates.put("518호", new Point(12, 74));
        itemCoordinates.put("517호", new Point(12, 87));
        itemCoordinates.put("516호", new Point(12, 93));
        itemCoordinates.put("515호", new Point(12, 103));
        itemCoordinates.put("514호", new Point(12, 109));
        itemCoordinates.put("512호", new Point(12, 122));

        itemCoordinates.put("511호", new Point(13, 131));
        itemCoordinates.put("509호", new Point(30, 132));
        itemCoordinates.put("507호", new Point(55, 132));

        itemCoordinates.put("505호", new Point(47, 116));
        itemCoordinates.put("504호", new Point(43, 101));
        itemCoordinates.put("503호", new Point(39, 87));
        itemCoordinates.put("502호", new Point(35, 74));
        itemCoordinates.put("501호", new Point(33, 67));

        itemCoordinates.put("532호", new Point(29, 53));
        itemCoordinates.put("531호", new Point(28, 48));
        itemCoordinates.put("530호", new Point(26, 39));
        itemCoordinates.put("529호", new Point(22, 27));
        itemCoordinates.put("528호", new Point(21, 23));
        itemCoordinates.put("527호", new Point(20, 19));
        itemCoordinates.put("526호", new Point(18, 12));
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

    public Point getNowCoordinate(String item) {
        Point nowCoordinate = itemCoordinates.get(item);
        assert nowCoordinate != null;
        return new Point(nowCoordinate.x, nowCoordinate.y);
    }
}


