package com.example.maleesha5.elektra11;

import android.support.v4.app.Fragment;;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by maleesha5 on 5/15/2017.
 */

public class Devices extends Fragment {

    ArrayList<DeviceInfo> arrayListDevices = new ArrayList<>();
   // ArrayList<Long> arrayListImg = new ArrayList<>();

    ListView list;


    public Devices(){
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_devices, container, false);
        list=(ListView) rootView.findViewById(R.id.list);


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("devices");



        final DeviceListAdapter adapter=new DeviceListAdapter(getActivity(), arrayListDevices);
        list.setAdapter(adapter);
        final HashMap<String, Double> newmap = new HashMap();

        myRef.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayListDevices.clear();

                for(DataSnapshot deviceSnapShot : dataSnapshot.getChildren()){

                    DeviceInfo newDevice = deviceSnapShot.getValue(DeviceInfo.class);
                    HashMap<String, Double> epoch = newDevice.getVoltage();
                    for (Map.Entry<String, Double> entry : epoch.entrySet())
                    {
                        //System.out.println(entry.getKey() + "/" + entry.getValue());
                        long dateTime = Long.parseLong(entry.getKey());
                        String formattedDatetime = new java.text.SimpleDateFormat("MM dd yyyy HH mm ss").
                                format(new java.util.Date(dateTime*1000));

                        newmap.put(formattedDatetime, entry.getValue());

                    }

                    newDevice.setFormattedTVol(newmap);
                   // Log.d(TAG, "Value Date: " + newDevice.getFormattedTVol());

                    arrayListDevices.add(newDevice);

                }


                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        return rootView;
    }




}
