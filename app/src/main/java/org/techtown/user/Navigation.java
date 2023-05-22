package org.techtown.user;

import com.google.gson.annotations.SerializedName;

public class Navigation {
    @SerializedName("direction")
    private String direction;

    @SerializedName("distance")
    private int distance;

    public String getDirection() {
        return direction;
    }

    public int getDistance() {
        return distance;
    }
}
