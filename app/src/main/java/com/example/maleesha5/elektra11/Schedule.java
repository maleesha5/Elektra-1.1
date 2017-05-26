package com.example.maleesha5.elektra11;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;


public class Schedule extends Fragment implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {
    Button b_pick,btn_cancel;
    TextView result;
    // TextView result2;
    TextView result3;
    TextView result4;
    CountDownTimer countDownTimer;
    View rootView;
    int day,month,year,hour,minute,second;                      //current date variables
    int dayFinal,monthFinal,yearFinal,hourFinal,minuteFinal;    //user selected date variables
    int dayPre,monthPre,yearPre,hourPre,minutePre,secondPre;     // current date in miliseconds variables
    int hourPas,minutePas,monthPas,dayPas;                      //user selected date in miliseconds
    int totalPre,totalPas,difference;
    DatabaseReference myRef;//date difference and total variables
    boolean isCancel=false;

   // private final String API_KEY = "cada20a3fbf7e42890cb2fa273db6f6bad7eb0b3";
    //private final String VARIABLE_ID = "585e7f1176254273e64e6cd8";

    public Schedule() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         rootView = inflater.inflate(R.layout.fragment_schedule, container, false);


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("devices/-KkdzvO6FNc2Ye6na2VP/status");
        b_pick=(Button)rootView.findViewById(R.id.b_pick);
        btn_cancel=(Button)rootView.findViewById(R.id.btn_cancel);

        btn_cancel.setEnabled(false);
       // b_pick.setOnClickListener(btnOnClickListener);
        //cancel=(Button) findViewById(R.id.cancel);
        //cancel.setOnClickListener(btnOnClickListener);
        result=(TextView) rootView.findViewById(R.id.result);
        // result2=(TextView) findViewById(R.id.result2);
        result3=(TextView) rootView.findViewById(R.id.result3);
        result4=(TextView) rootView.findViewById(R.id.result4);

        b_pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_cancel.setEnabled(true);
                isCancel=false;
                start();
                b_pick.setEnabled(false);

            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCancel=true;
                btn_cancel.setEnabled(false);
                b_pick.setEnabled(true);
                result3.setText("Canceled");
            }
        });

        return rootView;

    }
    private void start(){   // setting date in date picker to current date

        Calendar c= Calendar.getInstance();
        year=c.get(Calendar.YEAR);
        month=c.get(Calendar.MONTH)+1;
        day=c.get(Calendar.DAY_OF_MONTH);
        hour=c.get(Calendar.HOUR_OF_DAY);
        minute=c.get(Calendar.MINUTE);
        second=c.get(Calendar.SECOND);




        // result2.setText(year+" "+month+" "+day+ " "+hour+ " "+minute+ " "
        //         +second);

        DatePickerDialog datePickerDialog=new DatePickerDialog(getActivity(), Schedule.this, year, month, day);
        datePickerDialog.show();

    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {  //user selected month day

        // yearFinal=year;
        monthFinal=monthOfYear;
        dayFinal=dayOfMonth;

        //Calendar c=Calendar.getInstance();

        TimePickerDialog timePickerDialog=new TimePickerDialog(getActivity(), Schedule.this,hour, minute,
                DateFormat.is24HourFormat(getContext()));
        timePickerDialog.show();


    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {  // user selected hour and minute
        hourFinal=hourOfDay;
        minuteFinal=minute;








        result.setText("Month :"+monthFinal+"\nDay :"+dayFinal+"\nHour :"+hourFinal+"\nMinute :"+minuteFinal);
        //result2.setText(yearFinal);

        current();
    }

    private void current() {

        Calendar c= Calendar.getInstance();
        year=c.get(Calendar.YEAR);
        month=c.get(Calendar.MONTH)+1;
        day=c.get(Calendar.DAY_OF_MONTH);
        hour=c.get(Calendar.HOUR_OF_DAY);
        minute=c.get(Calendar.MINUTE);
        second=c.get(Calendar.SECOND);

        //default values to convert month,day,hour,minute and seconds into miliseconds

        final int month_def=262800288*10;
        final int day_def=86400000;
        final int hour_def=3600000;
        final int minute_def=60000;
        final int sec_def=1000;




        result4.setText("Month :"+month+"\nDay :"+day+"\nHour :"+hour+"\nMinute :"+minute);

        monthPre=month*month_def;       //current month in miliseconds
        dayPre=day*day_def;             //current day in miliseconds
        hourPre=hour*hour_def;          //current hour in miliseconds
        minutePre=minute*minute_def;    //current minute in miliseconds
        secondPre=second*sec_def;       //current second in miliseconds

        totalPre=hourPre+minutePre+secondPre+monthPre+dayPre;   //current date in miliseconds

        monthPas=monthFinal*month_def;              //user entered month in miliseconds
        dayPas=dayFinal*day_def;                    //user entered day in miliseconds
        hourPas=hourFinal*hour_def;                 //user entered hour in miliseconds
        minutePas=minuteFinal*minute_def;           //user entered minute in miliseconds

        totalPas=hourPas+minutePas+monthPas+dayPas;  // user entered date in miliseconds

        difference=(totalPas-totalPre);         //date difference in miliseconds
        result3.setText("the dif ="+difference);


        if(difference>0) {
            count();
        }else{
            result3.setText("Incorrect Input,,");
        }


    }

    private void count() {
        countDownTimer : new CountDownTimer(difference,1000) {      // setting countdown time as date difference in miliseconds
            @Override
            public void onTick(long millisUntilFinished) {

                if(isCancel){
                    cancel();
                }else {

                    result3.setText((millisUntilFinished / 1000) + " seconds");
                }

            }

            @Override
            public void onFinish() {
                result3.setText("finished");
                b_pick.setEnabled(true);

                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        myRef.setValue(false);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                // on finish set text to finished
                //  Vibrator v=(Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                // v.vibrate(500);
                // countDownTimer.cancel();

            }
        }.start();  // starting the countdown timer
    }


    // cancel countdown
    public void cancel(){
        //result2.setText("canceled");
        countDownTimer.cancel();
/*if(countDownTimer!=null) {
    countDownTimer.cancel();
    result3.setText("canceled");
    countDownTimer = null;
}*/





    }

}
