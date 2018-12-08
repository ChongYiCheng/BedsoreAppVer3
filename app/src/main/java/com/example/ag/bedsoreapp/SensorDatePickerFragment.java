package com.example.ag.bedsoreapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class SensorDatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @NonNull
    @Override //To obtain date from DatePicker
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(),this,year,month,day);

    }


    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Singapore"));
        cal.set(year,month,day);
        Date date = cal.getTime(); //Date from DatePicker

        SimpleDateFormat mDateFormat = new SimpleDateFormat("d MMMM yyyy");
        mDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Singapore"));
        String displayDate = mDateFormat.format(date);


        SimpleDateFormat mDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
        mDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Singapore"));
        String queryDate = mDateFormat2.format(date);

        Intent intent = new Intent();
        intent.putExtra("queryDate", queryDate);
        intent.putExtra("displayDate", displayDate);
        getTargetFragment().onActivityResult(getTargetRequestCode(), 111, intent);




    }








}