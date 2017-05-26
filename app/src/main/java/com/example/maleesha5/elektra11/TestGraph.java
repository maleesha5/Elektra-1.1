package com.example.maleesha5.elektra11;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.example.maleesha5.elektra11.R.id.parent;
import static com.example.maleesha5.elektra11.R.id.spinnerDate;
import static com.google.android.gms.internal.zzs.TAG;

/**
 * Created by maleesha5 on 5/20/2017.
 */

public class TestGraph extends Fragment implements AdapterView.OnItemSelectedListener {

    List<String> devices;
    List<String> year;
    List<String> month;
    List<String> day;
    List<String> hour;
    ArrayList<DeviceInfo> deviceList;
    String device;
    String sYear;
    String smonth;
    String sdate;
    String shr;


    public TestGraph() {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "Value Query: " + "Cretate");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.stat_item, container, false);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("devices");

        deviceList = new ArrayList<>();
        devices = new ArrayList<String>();
        year = new ArrayList<String>();
        day = new ArrayList<String>();
        month = new ArrayList<String>();
        hour = new ArrayList<String>();

        devices.add("Devices");
        year.add("Year");
        month.add("Month");
        day.add("Date");
        hour.add("Hour");


        for (int i = 2015; i < 2020; i++) {

            year.add(Integer.toString(i));

        }
        for (int i = 1; i < 13; i++) {

            month.add(Integer.toString(i));

        }
        for (int i = 1; i < 32; i++) {

            day.add(Integer.toString(i));

        }
        for (int i = 1; i < 25; i++) {

            hour.add(Integer.toString(i));

        }


        myRef.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot deviceSnapShot : dataSnapshot.getChildren()) {

                    DeviceInfo newDevice = deviceSnapShot.getValue(DeviceInfo.class);
                    deviceList.add(newDevice);
                    Log.d(TAG, "Value Query: " + newDevice);

                    devices.add(newDevice.getDeviceName());


                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // Spinner element
        Spinner spinnerDate = (Spinner) rootView.findViewById(R.id.spinnerDate);
        Spinner spinnerYear = (Spinner) rootView.findViewById(R.id.spinnerYear);
        Spinner spinnerMonth = (Spinner) rootView.findViewById(R.id.spinnerMonth);
        final Spinner spinnerHr = (Spinner) rootView.findViewById(R.id.spinnerHour);
        Spinner spinnerDevice = (Spinner) rootView.findViewById(R.id.spinnerDevice);

        // Spinner click listener
        spinnerDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                 sdate = adapterView.getItemAtPosition(i).toString();

                // Showing selected spinner item
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerDevice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                device = adapterView.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerHr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                shr = adapterView.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                smonth = adapterView.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sYear = adapterView.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        // Spinner Drop down elements

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, devices);
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, year);
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, month);
        ArrayAdapter<String> dateAdpater = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, day);
        ArrayAdapter<String> hourAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, hour);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerDevice.setAdapter(dataAdapter);
        spinnerYear.setAdapter(yearAdapter);
        spinnerMonth.setAdapter(monthAdapter);
        spinnerDate.setAdapter(dateAdpater);
        spinnerHr.setAdapter(hourAdapter);

        HashMap<String, Double> queryResult = new HashMap<>();


        for(DeviceInfo dev:deviceList){
            Log.d(TAG, "Value Query: " + "aa");

            if(dev.getDeviceName().equals(device)){

                queryResult = dev.getSeperateDate(dev.getFormattedTVol(),sYear, smonth, sdate, shr);

            }

        }
        /*queryResult.put(55.5,34.5);
        queryResult.put("ssddsd",05.5);
        queryResult.put("wew",33.5);*/

        DataPoint[] graphData = new DataPoint[100];
        for (int i = 0;i < 100; i++)
        {
             graphData[i] = new DataPoint(i,5);

        }

        GraphView graph = (GraphView) rootView.findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(graphData);

        graph.addSeries(series);

        return rootView;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
