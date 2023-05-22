package org.techtown.user;

import com.google.gson.annotations.SerializedName;

public class Location {
    @SerializedName("BSSID")
    private String BSSID;

    @SerializedName("RSSI")
    private int RSSI;

    public Location(String BSSID, int RSSI) {
        this.BSSID = BSSID;
        this.RSSI = RSSI;
    }

    public String getBSSID() {
        return BSSID;
    }

    public int getRSSI() {
        return RSSI;
    }
}
