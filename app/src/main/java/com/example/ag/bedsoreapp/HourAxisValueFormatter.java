package com.example.ag.bedsoreapp;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class HourAxisValueFormatter implements IAxisValueFormatter  // Convert timestamps to HH:mm format for the x-axis
{

    private DateFormat mDataFormat;
    private Date mDate;

    public HourAxisValueFormatter() {
        this.mDataFormat = new SimpleDateFormat("HH:mm");
        this.mDate = new Date();
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        long convertedTimestamp = (long) value;


        // Convert timestamp to hour:minute
        return getHour(convertedTimestamp);
    }


    private String getHour(long timestamp){  // Convert timestamps to HH:mm format for the x-axis
        try{
            mDate.setTime(timestamp*1000); //times 1000 because Java expects milliseconds
            mDataFormat.setTimeZone(TimeZone.getTimeZone("Asia/Singapore"));
            return mDataFormat.format(mDate);
        }
        catch(Exception ex){
            return "xx";
        }
    }
}