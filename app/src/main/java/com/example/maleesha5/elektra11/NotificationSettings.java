package com.example.maleesha5.elektra11;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maleesha5 on 5/26/2017.
 */

public class NotificationSettings extends Fragment {
    Button btnMail;
    Button btnMobile;
    AlertDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.notification, container, false);

        btnMail = (Button) rootView.findViewById(R.id.btnSetupMail);
        btnMobile = (Button) rootView.findViewById(R.id.btnMobNot);
        final DatabaseReference databaseReferenceNotification = FirebaseDatabase.getInstance().getReference("mailNotification");
        final DatabaseReference databaseReferenceMail = FirebaseDatabase.getInstance().getReference("email");
        final DatabaseReference databaseRefeNotificationInt = FirebaseDatabase.getInstance().getReference("notificationInterval");
        final DatabaseReference databaseReferenceMobile = FirebaseDatabase.getInstance().getReference("mobileNotification");


        btnMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                View mView = getActivity().getLayoutInflater().inflate(R.layout.setup_mail, null);
                final EditText mail = (EditText) mView.findViewById(R.id.txtMail);
                Button done = (Button) mView.findViewById(R.id.btnDone);
                final CheckBox chckMail = (CheckBox) mView.findViewById(R.id.chcMail);

                databaseReferenceMail.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String value = dataSnapshot.getValue(String.class);
                        mail.setText(value);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                databaseReferenceNotification.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        boolean checked = (Boolean) dataSnapshot.getValue();
                        chckMail.setChecked(checked);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (mail.getText().toString().isEmpty() && chckMail.isChecked()) {

                            Toast.makeText(getContext(), "Please enter a mail Address!", Toast.LENGTH_SHORT).show();


                        } else {

                            Toast.makeText(getContext(), "Status updated", Toast.LENGTH_SHORT).show();
                            String mailAddress = mail.getText().toString();
                            databaseReferenceMail.setValue(mailAddress);
                            databaseReferenceNotification.setValue(chckMail.isChecked());

                        }
                        dialog.dismiss();
                    }

                });

                mBuilder.setView(mView);
                dialog = mBuilder.create();
                dialog.show();
            }
        });

        btnMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                View mView = getActivity().getLayoutInflater().inflate(R.layout.mobile_notification, null);
                Button done = (Button) mView.findViewById(R.id.btnNotificationDone);
                final Spinner spinner = (Spinner) mView.findViewById(R.id.spinner);
                final CheckBox chkNotification = (CheckBox) mView.findViewById(R.id.checkBox);

                databaseReferenceMobile.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        boolean check = (Boolean) dataSnapshot.getValue();
                        chkNotification.setChecked(check);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                databaseRefeNotificationInt.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        long time = (Long) dataSnapshot.getValue();
                        spinner.setPrompt(Long.toString(time));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



                // Spinner click listener
              //  spinner.setOnItemSelectedListener(this);

                // Spinner Drop down elements
                List<Integer> categories = new ArrayList<Integer>();
                categories.add(2);
                categories.add(5);
                categories.add(15);
                categories.add(30);
                categories.add(45);
                categories.add(60);

                // Creating adapter for spinner
                ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<Integer>(getContext(), android.R.layout.simple_spinner_item, categories);

                // Drop down layout style - list view with radio button
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // attaching data adapter to spinner
                spinner.setAdapter(dataAdapter);

                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getContext(), "Status updated!", Toast.LENGTH_SHORT).show();
                        databaseRefeNotificationInt.setValue(spinner.getSelectedItem());
                        databaseReferenceMobile.setValue(chkNotification.isChecked());
                        dialog.dismiss();
                    }

                });
                mBuilder.setView(mView);
                dialog = mBuilder.create();
                dialog.show();
            }
        });

        return rootView;
    }
}
