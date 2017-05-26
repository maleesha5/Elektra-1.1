package com.example.maleesha5.elektra11;

import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by maleesha5 on 5/15/2017.
 */

public class AddDevice extends Fragment {

    private String dName;
    Button btnSetName;
    EditText deviceName;



    public AddDevice() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.add_device, container, false);

        btnSetName =(Button) rootView.findViewById(R.id.btnNext);
        deviceName = (EditText) rootView.findViewById(R.id.txtDeviceName);



        btnSetName.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                addDevice();



                GetLocation newDevice = new GetLocation();


                Bundle bundle = new Bundle();
                bundle.putString("dName", dName);

                newDevice.setArguments(bundle);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

                fragmentTransaction.replace(R.id.content_main, newDevice, "Get Location");
                fragmentTransaction.commit();

            }
        });



        return rootView;
    }

    private void addDevice(){

        dName = deviceName.getText().toString().trim();

        if(!TextUtils.isEmpty(dName)){



        }else{

            Toast.makeText(getContext(), "Please enter a device Name", Toast.LENGTH_LONG).show();
        }


    }
}


