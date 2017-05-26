package com.example.maleesha5.elektra11;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by maleesha5 on 5/19/2017.
 */

public class GetLocation extends Fragment {

    private Button getLocation;
    private String dName;
    private TextView location;

    public GetLocation() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.get_location, container, false);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            dName = bundle.getString("dName", "noName");
            Toast.makeText(getContext(), "" + dName,
                    Toast.LENGTH_SHORT).show();
        }

        getLocation = (Button) rootView.findViewById(R.id.btnLocation);
        location = (EditText) rootView.findViewById(R.id.txtLocation);

        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String deviceLoc = location.getText().toString().trim();


                if(!TextUtils.isEmpty(deviceLoc)){

                    SetIcon newDevice = new SetIcon();


                    Bundle bundle = new Bundle();
                    bundle.putString("dLocation", deviceLoc);
                    bundle.putString("dName", dName);
                    newDevice.setArguments(bundle);
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

                    fragmentTransaction.replace(R.id.content_main, newDevice, "Set Icon");
                    fragmentTransaction.commit();


                }else{

                    Toast.makeText(getContext(), "Please enter a device location!", Toast.LENGTH_LONG).show();
                }

            }
        });


        return rootView;
    }
}
