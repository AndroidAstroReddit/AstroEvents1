package com.example.angel.astroevents;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by boydjohnson on 10/26/15.
 */
public class AstronomicalEvent {
    private static final String JSON_YEAR = "year";
    private static final String JSON_DAY_OF_WEEK = "day_of_week";
    private static final String JSON_MONTH = "month";
    private static final String JSON_DAY = "day";
    private static final String JSON_EVENT = "event";
    private static final String JSON_TIME = "time";


    private String year;
    private String time_zone;
    private String day_of_week;
    private String month;
    private String day;
    private String event_name;
    private String time;

    public String getYear() {
        return year;
    }

    public AstronomicalEvent(String eventName, String eventWeekday, String eventMonth, String eventDay, String eventYear, String eventTime){
        event_name = eventName;
        day_of_week = eventWeekday;
        month = eventMonth;
        day = eventDay;

        time = eventTime;
        year = eventYear;
    }


    public AstronomicalEvent(JSONObject json) throws JSONException{
        event_name = json.getString(JSON_EVENT);
        month = json.getString(JSON_MONTH);

        day = json.getString(JSON_DAY);
        day_of_week = json.getString(JSON_DAY_OF_WEEK);
        time = json.getString(JSON_TIME);
        year = json.getString(JSON_YEAR);






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

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
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
