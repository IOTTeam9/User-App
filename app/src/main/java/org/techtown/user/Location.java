package org.techtown.user;

import com.google.gson.annotations.SerializedName;

public class Location {

    @SerializedName("place")
    private String place;
    @SerializedName("ssid")
    private String ssid;
    @SerializedName("bssid")
    private String bssid;
    @SerializedName("rssi")
    private int rssi;

    public Location(String place, String ssid, String bssid, int rssi) {
        this.place = place;
        this.ssid = ssid;
        this.bssid = bssid;
        this.rssi = rssi;
    }

    public String getPlace() {
        return place;
    }

    public String getSsid() {
        return ssid;
    }

    public String getBssid() {
        return bssid;
    }

    public int getRssi() {
        return rssi;
    }
}
