package org.techtown.user;

import com.google.gson.annotations.SerializedName;

public class Location {

    @SerializedName("bssid")
    private String bssid;
    @SerializedName("rssi")
    private int rssi;

    public Location(String bssid, int rssi) {
        this.bssid = bssid;
        this.rssi = rssi;
    }


    public String getBssid() {
        return bssid;
    }

    public int getRssi() {
        return rssi;
    }
}
