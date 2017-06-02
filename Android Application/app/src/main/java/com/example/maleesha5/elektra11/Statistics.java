package com.example.maleesha5.elektra11;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

import static com.google.android.gms.internal.zzs.TAG;


public class Statistics extends Fragment {

    BarGraphSeries<DataPoint> deviceStatus;
    DatabaseReference dataBaseDevcies;
    int status;
    String deviceID;
    int selectDate;
    int selectHr;
    Button set;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.stat_item, container, false);
        Button setButton = (Button) rootView.findViewById(R.id.btnSet);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            deviceID = bundle.getString("deviceID", "noName");
            Toast.makeText(getContext(), "" + deviceID,
                    Toast.LENGTH_SHORT).show();
        }

        final NumberPicker date = (NumberPicker) rootView.findViewById(R.id.pickerDate);
        NumberPicker time = (NumberPicker) rootView.findViewById(R.id.pickerTime);

        date.setMinValue(0);
        time.setMinValue(0);

        //Specify the maximum value/number of NumberPicker
        date.setMaxValue(31);
        time.setMaxValue(59);

        //Gets whether the selector wheel wraps when reaching the min/max value.
        date.setWrapSelectorWheel(true);

        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final GraphView graph = (GraphView)rootView.findViewById(R.id.graph);
                dataBaseDevcies = FirebaseDatabase.getInstance().getReference("report/" + MainActivity.deviceId + "/" +  deviceID + "/5/" + selectDate + "/" + selectHr);
                deviceStatus = new BarGraphSeries<>();

                final int a[] = new int[59];
                //Log.d(TAG, "ARRAY VAL " + a[5]);
                dataBaseDevcies.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot deviceSnapShot : dataSnapshot.getChildren()){
                            // if(dataSnapshot.getValue() == "true"){
                            status = 0;
                            a[Integer.parseInt(deviceSnapShot.getKey())] = 1;


                            // }


                            //Log.d("d");
                            Log.d(TAG, "KEY is" + deviceSnapShot.getKey());
                        }



                        // set manual X bounds
                        graph.getViewport().setYAxisBoundsManual(true);
                        graph.getViewport().setMinY(1);
                        graph.getViewport().setMaxY(0);

                        graph.getViewport().setXAxisBoundsManual(true);
                        graph.getViewport().setMinX(0);
                        graph.getViewport().setMaxX(60);

                        // enable scaling and scrolling
                        graph.getViewport().setScalable(true);
                        graph.getViewport().setScalableY(true);
                        graph.getViewport().setScrollable(true);

                        deviceStatus.setSpacing(50);
                        for(int x = 0; x < 59; x++){
                            deviceStatus.appendData(new DataPoint(x,a[x] * 10), true, 60);
                            Log.d(TAG, "KEY is ***********" + a[x] + " index " + x);

                        }
                        graph.addSeries(deviceStatus);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });



        date.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                selectDate = i1;

            }
        });

        time.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                selectHr = i1;
            }
        });
        //final HashMap<Double, Double> newmap = new HashMap();





    return rootView;
    }
}
