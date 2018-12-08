package com.example.ag.bedsoreapp;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
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


public class SensorFragment2 extends Fragment  {
    private DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();
    LineChart lineChart;
    private View rootView;
    private Date date;
    private static ArrayList<Float> listTimeStamp = new ArrayList<Float>();
    private static ArrayList<Float> listPressure = new ArrayList<Float>();
    String firebaseDate;
    String chosenDate;
    String displayDate;
    final List<Entry> lineEntries = new ArrayList<>();  //list of all data points for the chart
    TextView dateTextView;




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 111) {
            lineChart.clear();
            chosenDate = data.getStringExtra("queryDate");
            displayDate = data.getStringExtra("displayDate");
            dateTextView.setText(displayDate);
            //Log.i("DATEIS", displayDate);
            listPressure.clear();
            plotChart();

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_graph,container,false);


        Date date = Calendar.getInstance().getTime(); //Date from DatePicker

        SimpleDateFormat mDateFormat = new SimpleDateFormat("d MMMM yyyy");
        mDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Singapore"));
        displayDate = mDateFormat.format(date);
        dateTextView = (TextView) rootView.findViewById(R.id.dateTextView); //must use rootview
        dateTextView.setText(displayDate);



        lineChart = (LineChart) rootView.findViewById(R.id.linechart);

        Button datePicker = (Button) rootView.findViewById(R.id.datePicker); //must use rootview
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new SensorDatePickerFragment();
                newFragment.setTargetFragment(SensorFragment2.this, 111); // any number
                newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
            }
        });

        plotChart();

        return rootView;
    }



    public void plotChart() {
        DatabaseReference ref =mDatabaseReference.child("Sensor Data");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    // To obtain the chosen date to be queried
                    if (ds.getKey().equals(chosenDate)) {
                        firebaseDate = chosenDate;
                        Log.i("FirebaseDate", firebaseDate);
                        // Query the data from the chosen date
                        Query query = mDatabaseReference.child("Sensor Data").child(firebaseDate).orderByKey().limitToLast(1000);
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    //String pressure = ds.child("Pressure").getValue(String.class);  // Retrieve temperature from database
                                    //float pressureFloat = Float.parseFloat(pressure);
                                    float pressureFloat = ds.child("PValue2").getValue(Float.class);
                                    listPressure.add(pressureFloat);
                                    Log.i("PRESSUREVALUES", listPressure.toString());

                                    String timeStamp = ds.child("Timestamp").getValue(String.class);   // Retrieve timestamp from database
                                    float timeStampFloat = Float.parseFloat(timeStamp);   // Convert timestamp to float and pass the float value to the graph
                                    listTimeStamp.add(timeStampFloat);

                                    lineEntries.add(new Entry(timeStampFloat,pressureFloat)); // Append the data point to the Arraylist of data points.
                                    Log.i("LineEntries", lineEntries.toString());
                                }

                                displayLineChart(lineEntries);   // Plot the graph out using the list of data points.

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.e("THERE IS DATABASE ERROR",lineEntries.toString());
                            }
                        });
                    }
                    // remove existing displayed graph from ui if chosen date does not have any data.
                    else if (ds.getKey()!= chosenDate){
                        Log.i("NOT CHOSEN", "NOT CHOSEN");
                        lineChart.clear();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }




    public LineChart displayLineChart(List<Entry> lineentries) {
        LineDataSet dataSet = new LineDataSet(lineentries, "Pressure");
        LineData data = new LineData(dataSet);
        lineChart.setData(data);
        lineChart.invalidate();


        // To format the Y-axis
        //leftAxis.setValueFormatter(new MyYAxisValueFormatter());
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setLabelCount(6);
        leftAxis.setAxisMinimum(0);
        leftAxis.setAxisMaximum(50);
        lineChart.getAxisRight().setEnabled(false);


        // To format the X-axis with HH-mm time format
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new com.example.ag.bedsoreapp.HourAxisValueFormatter());
        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        xAxis.setLabelCount(5);
        xAxis.setAvoidFirstLastClipping(true);
        lineChart.setVisibleXRangeMaximum(10000f); //IMPORTANT 100000f
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        // Disable the legend
        Legend legend = lineChart.getLegend();
        legend.setEnabled(false);
        //legend.setCustom(ColorTemplate.rgb(102,187,106), new String[] { "Set1", "Set2", "Set3", "Set4", "Set5" });
        //legend.setTextSize(10f);

        // For customizing the aesthetics of the graph
        dataSet.setColor(Color.rgb(56, 142, 60));
        dataSet.setCircleColor(Color.rgb(255, 183, 77));
        dataSet.setDrawCircleHole(false);
        dataSet.setCircleRadius(4f);
        dataSet.setLineWidth(4f);
        dataSet.setValueTextSize(10f);
        dataSet.setValueTextColor(Color.BLUE);
        lineChart.getDescription().setEnabled(false);
        lineChart.setBackgroundColor(Color.rgb(178, 235, 242));
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setDrawValues(true);
        lineChart.setMaxVisibleValueCount(5);   //show text values only if zoomed-in to 5 data points
        dataSet.setDrawFilled(true);
        dataSet.setFillColor(Color.rgb(165, 214, 167));
        Log.i("LineEntries", lineentries.toString());
        //lineEntries.clear();

        // For pop-up MarkerView to display selected data point
        //IMarker marker = new com.example.mlsvslvedainag.plantfactory3.MyMarkerView(mContext,R.layout.custom_marker_view);
        //lineChart.setMarker(marker);
        return lineChart;
    }


}


