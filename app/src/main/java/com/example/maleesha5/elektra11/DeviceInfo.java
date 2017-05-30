package com.example.maleesha5.elektra11;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * Created by maleesha5 on 5/16/2017.
 */

public class DeviceInfo {


    public DeviceInfo(){

    }

    public DeviceInfo(String deviceName, long imgId, boolean status, String deviceId, String location, HashMap<String, Double> voltage, Double latestWatt, Double latestVoltage, Double latestAmpere) {
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.imgId = imgId;
        this.status = status;
        this.location = location;
        this.voltage = voltage;
        this.latestWatt = latestWatt;
        this.latestVoltage = latestVoltage;
        this.latestAmpere = latestAmpere;
    }

    private String deviceName;
    private long imgId;
    private boolean status;
    private String deviceId;
    DatabaseReference dataBaseDevcies;
    private String location;
    private HashMap<String,Double> voltage;
    private HashMap<String,Double> formattedTVol;
    private double latestWatt;
    private double latestVoltage;
    private double latestAmpere;

    public double getLatestVoltage() {
        return latestVoltage;
    }

    public double getLatestAmpere() {
        return latestAmpere;
    }




    public double getLatestWatt() {
        return latestWatt;
    }


    public HashMap<String, Double> getFormattedTVol() {
        return formattedTVol;
    }

    public void setFormattedTVol(HashMap<String, Double> formattedTVol) {
        this.formattedTVol = formattedTVol;
    }



    public HashMap<String, Double> getVoltage() {
        return voltage;
    }

    public void setVoltage(HashMap<String, Double> voltage) {
        this.voltage = voltage;
    }




    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }



    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public long getImgId() {
        return imgId;
    }

    public void setImgId(long imgId) {
        this.imgId = imgId;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }


    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void updateFirebase(){

        dataBaseDevcies = FirebaseDatabase.getInstance().getReference("devices");
        DeviceInfo newDevice = new DeviceInfo(this.deviceName, this.imgId, this.status, this.deviceId, this.location, this.voltage, this.latestWatt, this.latestVoltage, this.latestAmpere);

        dataBaseDevcies.child(this.deviceId).setValue(newDevice);

    }



    public HashMap<String, Double> getSeperateDate(HashMap<String, Double> formattedDateTime, String year, String month, String date){

        String[] words;
        HashMap<String, Double> queryResult = new HashMap<>();
        for (Map.Entry<String, Double> entry : formattedDateTime.entrySet())
        {

            //System.out.println(entry.getKey() + "/" + entry.getValue());
            String dateTime = entry.getKey();
            // String s1="java string split method by javatpoint";
            words=dateTime.split("\\s");//splits the string based on whitespace
            //using java foreach loop to print elements of string array


            if(words[2] .equals(year) && words[0].equals(month) && words[1].equals(date)){

                queryResult.put(dateTime, entry.getValue());
            }



        }

        return queryResult;
    }

    public HashMap<String, Double> getSeperateDate(HashMap<String, Double> formattedDateTime, String year){

        String[] words;
        HashMap<String, Double> queryResult = new HashMap<>();
        for (Map.Entry<String, Double> entry : formattedDateTime.entrySet())
        {
            //System.out.println(entry.getKey() + "/" + entry.getValue());
            String dateTime = entry.getKey();
            // String s1="java string split method by javatpoint";
            words=dateTime.split("\\s");//splits the string based on whitespace
            //using java foreach loop to print elements of string array


            if(words[2] .equals(year)){

                queryResult.put(dateTime, entry.getValue());
            }




        }

        return queryResult;
    }
    public HashMap<String, Double> getSeperateDate(HashMap<String, Double> formattedDateTime, String year, String month){

        String[] words;
        HashMap<String, Double> queryResult = new HashMap<>();
        for (Map.Entry<String, Double> entry : formattedDateTime.entrySet())
        {
            //System.out.println(entry.getKey() + "/" + entry.getValue());
            String dateTime = entry.getKey();
            // String s1="java string split method by javatpoint";
            words=dateTime.split("\\s");//splits the string based on whitespace
            //using java foreach loop to print elements of string array



            if(words[2] .equals(year) && words[0].equals(month)){

                queryResult.put(dateTime, entry.getValue());
            }



        }

        return queryResult;
    }

    public HashMap<String, Double> getSeperateDate(HashMap<String, Double> formattedDateTime, String year, String month, String date, String hour){

        String[] words;
        HashMap<String, Double> queryResult = new HashMap<>();
        for (Map.Entry<String, Double> entry : formattedDateTime.entrySet())
        {

            //System.out.println(entry.getKey() + "/" + entry.getValue());
            String dateTime = entry.getKey();
            // String s1="java string split method by javatpoint";
            words=dateTime.split("\\s");//splits the string based on whitespace
            //using java foreach loop to print elements of string array



            if(words[2] .equals(year) && words[0].equals(month) && words[1].equals(date) && words[3].equals(hour)){

                queryResult.put(dateTime, entry.getValue());
            }



        }

        return queryResult;
    }
}
