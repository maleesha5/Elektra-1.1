package com.example.maleesha5.elektra11;

/**
 * Created by maleesha5 on 5/15/2017.
 */


import android.app.Activity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class DeviceListAdapter extends ArrayAdapter<DeviceInfo> {

    private final Activity context;
    private final ArrayList<DeviceInfo> itemname;
    Switch powerSwitch;
    Button remove;
    Button btnSettings;
    FirebaseDatabase firebase;
    AlertDialog dialog;


    public DeviceListAdapter(Activity context, ArrayList<DeviceInfo> device) {
        super(context, R.layout.custom_icon, device);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemname=device;
        //this.imgid=device;
    }

   public View getView(final int position, View view, ViewGroup parent) {
        final LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.custom_icon, null,true);

       firebase = FirebaseDatabase.getInstance();
       final DatabaseReference myRef = firebase.getReference("devices");

        TextView txtTitle = (TextView) rowView.findViewById(R.id.Itemname);
        TextView txtWatt = (TextView) rowView.findViewById(R.id.txtWatt);
        TextView txtVoltage = (TextView) rowView.findViewById(R.id.txtVol);
        TextView txtAmpere = (TextView) rowView.findViewById(R.id.txtAmps);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        powerSwitch = (Switch) rowView.findViewById(R.id.switch1);
        remove = (Button) rowView.findViewById(R.id.btnRemove);
        btnSettings = (Button) rowView.findViewById(R.id.btnSettings);


        powerSwitch.setChecked(itemname.get(position).isStatus());

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef.child(itemname.get(position).getDeviceId()).removeValue();
            }
        });



        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                View mView = inflater.inflate(R.layout.device_settings, null);

                final EditText deviceName = (EditText) mView.findViewById(R.id.txtName);
                final EditText deviceLocation = (EditText) mView.findViewById(R.id.txtLocation);

                Button done = (Button) mView.findViewById(R.id.btnDone);
                GridView gridview = (GridView) mView.findViewById(R.id.grdSettings);

                deviceName.setText(itemname.get(position).getDeviceName());
                deviceLocation.setText(itemname.get(position).getLocation());


                final ImageAdapter imgAdapter = new ImageAdapter(getContext());
                gridview.setAdapter(imgAdapter);

                gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        itemname.get(position).setImgId(imgAdapter.getmThumbIds()[i]);
                    }
                });

                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = deviceName.getText().toString();
                        String location = deviceLocation.getText().toString();

                        itemname.get(position).setDeviceName(name);
                        itemname.get(position).setLocation(location);

                        itemname.get(position).updateFirebase();
                        dialog.dismiss();
                    }
                });

                mBuilder.setView(mView);
                dialog = mBuilder.create();
                dialog.show();
            }

        });

        powerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               //Toast.makeText(getContext(), itemname.get(position).getDeviceName(), Toast.LENGTH_SHORT).show();
               if(isChecked){
                   Toast.makeText(getContext(),"ON", Toast.LENGTH_SHORT).show();
                    itemname.get(position).setStatus(true);
               }else{
                   Toast.makeText(getContext(), "OFF", Toast.LENGTH_SHORT).show();
                   itemname.get(position).setStatus(false);

               }
               itemname.get(position).updateFirebase();

               // Log.d(TAG, "Quer Result; " + itemname.get(position).getSeperateDate(itemname.get(position).getFormattedTVol(),"2017","05","20","17"));


           }
         });
       //TextView extratxt = (TextView) rowView.findViewById(R.id.textView1);

        txtTitle.setText(itemname.get(position).getDeviceName());
        imageView.setImageResource((int) itemname.get(position).getImgId());
        txtWatt.setText(Double.toString(itemname.get(position).getLatestWatt()) + "W");
        txtAmpere.setText(Double.toString(itemname.get(position).getLatestAmpere()) + "A");
        txtVoltage.setText(Double.toString(itemname.get(position).getLatestVoltage()) + "V");

       //extratxt.setText("Description "+itemname[position]);
        return rowView;

    };




}
