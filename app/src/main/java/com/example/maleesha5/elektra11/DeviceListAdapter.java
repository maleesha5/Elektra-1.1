package com.example.maleesha5.elektra11;

/**
 * Created by maleesha5 on 5/15/2017.
 */


import android.app.Activity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class DeviceListAdapter extends ArrayAdapter<DeviceInfo> {

    private final Activity context;
    private final ArrayList<DeviceInfo> itemname;
    Switch powerSwitch;
    Button remove;
    FirebaseDatabase firebase;


    public DeviceListAdapter(Activity context, ArrayList<DeviceInfo> device) {
        super(context, R.layout.custom_icon, device);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemname=device;
        //this.imgid=device;
    }

   public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.custom_icon, null,true);

       firebase = FirebaseDatabase.getInstance();
       final DatabaseReference myRef = firebase.getReference("devices");

        TextView txtTitle = (TextView) rowView.findViewById(R.id.Itemname);
        TextView txtWatt = (TextView) rowView.findViewById(R.id.txtWatt);

       ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        powerSwitch = (Switch) rowView.findViewById(R.id.switch1);
        remove = (Button) rowView.findViewById(R.id.btnRemove);

        powerSwitch.setChecked(itemname.get(position).isStatus());
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef.child(itemname.get(position).getDeviceId()).removeValue();
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
        //extratxt.setText("Description "+itemname[position]);
        return rowView;

    };




}
