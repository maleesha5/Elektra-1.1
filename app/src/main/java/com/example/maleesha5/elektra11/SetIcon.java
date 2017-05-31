package com.example.maleesha5.elektra11;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SetIcon extends Fragment {

    private String dID;
    private String dName;
    private String dLocation;
    DatabaseReference dataBaseDevcies;

    public SetIcon(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.set_img_pg, container, false);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            dName = bundle.getString("dName", "noName");
            dLocation = bundle.getString("dLocation", "noLocation");

        }
        dataBaseDevcies = FirebaseDatabase.getInstance().getReference(MainActivity.deviceId + "/devices");

        GridView gridview = (GridView) rootView.findViewById(R.id.gridview);

        final ImageAdapter imgAdapter = new ImageAdapter(getContext());
        gridview.setAdapter(imgAdapter);
        final HashMap<String, Double> newHash = new HashMap<>();
        newHash.put("00",0.0);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {



                dID = dataBaseDevcies.push().getKey();

                DeviceInfo newDevice = new DeviceInfo(dName,imgAdapter.getmThumbIds()[position], false, dID, dLocation,newHash, 0.0, 0.0, 0.0);

                dataBaseDevcies.child(dID).setValue(newDevice);
                Devices device = new Devices();


                Bundle bundle = new Bundle();
                bundle.putString("dName", dName);

                device.setArguments(bundle);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

                fragmentTransaction.replace(R.id.content_main, device, "Connect");
                fragmentTransaction.commit();

            }
        });



        return  rootView;
    }


}
