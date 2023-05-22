package org.techtown.user;

import com.google.gson.annotations.SerializedName;

public class NavigationResponse {

    @SerializedName("code")
    private int code;

    @SerializedName("data")
    private Navigation data;

    @SerializedName("isSuccess")
    private boolean isSuccess;

    @SerializedName("message")
    private String message;

    public int getCode() {
        return code;
    }

    public Navigation getData() {
        return data;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public String getMessage() {
        return message;
    }
}
