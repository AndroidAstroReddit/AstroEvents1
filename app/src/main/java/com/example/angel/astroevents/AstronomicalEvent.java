package com.example.angel.astroevents;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by boydjohnson on 10/26/15.
 */
public class AstronomicalEvent {
    private static final String JSON_YEAR = "EVENT YEAR";
    private static final String JSON_DAY_OF_WEEK = "EVENT DAYOFWEEK";
    private static final String JSON_MONTH = "EVENT MONTH";
    private static final String JSON_DAY = "EVENT DAY";
    private static final String JSON_EVENT = "EVENT EVENT";
    private static final String JSON_TIME = "EVENT TIME";


    private String year;
    private String time_zone;
    private String day_of_week;
    private String month;
    private Integer day;
    private String event_name;
    private String time;

    public String getYear() {
        return year;
    }

    public AstronomicalEvent(JSONObject json) throws JSONException{
        day = json.getInt(JSON_DAY);
        year = json.getString(JSON_YEAR);
        day_of_week = json.getString(JSON_DAY_OF_WEEK);
        month = json.getString(JSON_MONTH);
        event_name = json.getString(JSON_EVENT);
        time = json.getString(JSON_TIME);


    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getTime_zone() {
        return time_zone;
    }

    public void setTime_zone(String time_zone) {
        this.time_zone = time_zone;
    }

    public String getDay_of_week() {
        return day_of_week;
    }

    public void setDay_of_week(String day_of_week) {
        this.day_of_week = day_of_week;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

@Override
    public String toString(){
        return event_name;
    }
}
