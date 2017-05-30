package com.example.maleesha5.elektra11;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by maleesha5 on 5/19/2017.
 */

public class GetLocation extends Fragment {

    private Button getLocation;
    private String dName;
    private TextView location;
    private ListView listLoc;
    private String deviceLocation;
    // Array of strings...
    String[] mobileArray = {"Colombo","Dehiwala-Mount Lavinia","Moratuwa",
            "Sri Jayawardenapura Kotte","Negombo","Kandy",
            "Kalmunai","Vavuniya","Galle",
            "Trincomalee","Batticaloa","Jaffna",
            "Katunayake","Dambulla","Kolonnawa",
            "Anuradhapura","Ratnapura","Badulla",
            "Matara","Puttalam","Chavakacheri",
            "Kattankudy","Matale","Kalutara",
            "Mannar","Panadura","Beruwala",
            "Ja-Ela","Point Pedro","Kelaniya",
            "Peliyagoda","Kurunegala","Wattala",
            "Gampola","Nuwara Eliya","Valvettithurai",
            "Chilaw","Eravur","Avissawella",
            "Weligama","Ambalangoda","Ampara",
            "Kegalle","Hatton","Nawalapitiya",
            "Balangoda","Hambantota","Tangalle",
            "Moneragala","Gampaha","Horana",
            "Wattegama","Minuwangoda","Bandarawela",
            "Kuliyapitiya","Haputale","Talawakele",
            "Harispattuwa","Kadugannawa","Embilipitiya"};

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
        ArrayAdapter adapter = new ArrayAdapter<String>(getContext(),
                R.layout.city_list, mobileArray);

        ListView listView = (ListView) rootView.findViewById(R.id.listCity);
        listView.setAdapter(adapter);


       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

               // When clicked, show a toast with the TextView text
               deviceLocation = ((TextView) view).getText().toString();
               Toast.makeText(getContext(),
                       ((TextView) view).getText(), Toast.LENGTH_SHORT).show();
           }
       });

        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                    SetIcon newDevice = new SetIcon();


                    Bundle bundle = new Bundle();
                    bundle.putString("dLocation", deviceLocation);
                    bundle.putString("dName", dName);
                    newDevice.setArguments(bundle);
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

                    fragmentTransaction.replace(R.id.content_main, newDevice, "Set Icon");
                    fragmentTransaction.commit();



            }
        });


        return rootView;
    }
}
