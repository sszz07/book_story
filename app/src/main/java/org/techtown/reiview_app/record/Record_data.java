package org.techtown.reiview_app.record;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Record_data {
    @Expose
    @SerializedName("read_time")
    private int read_time;

    @Expose
    @SerializedName("record_date")
    private String record_date;

    @Expose
    @SerializedName("email")
    private String email;

    public int getRead_time() {
        return read_time;
    }

    public void setRead_time(int read_time) {
        this.read_time = read_time;
    }

    public String getRecord_date() {
        return record_date;
    }

    public void setRecord_date(String record_date) {
        this.record_date = record_date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
