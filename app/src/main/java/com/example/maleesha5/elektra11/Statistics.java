package com.example.maleesha5.elektra11;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


public class Statistics extends Fragment {

    LineGraphSeries<DataPoint> deviceStatus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.stat_item, container, false);

        GraphView graph = (GraphView)rootView.findViewById(R.id.graph);
        deviceStatus = new LineGraphSeries<DataPoint>();

        deviceStatus.appendData(new DataPoint(0, 1), true, 500);

        graph.addSeries(deviceStatus);


    return rootView;
    }
}
