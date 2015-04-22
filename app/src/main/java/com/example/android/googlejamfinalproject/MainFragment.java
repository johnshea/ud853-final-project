package com.example.android.googlejamfinalproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MainFragment extends Fragment {

    Button btnListCurrentAlerts;
    Button btnCreateNewAlert;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btnListCurrentAlerts = (Button) getView().findViewById(R.id.button_mainfragment_listcurrentalerts);
        btnCreateNewAlert = (Button) getView().findViewById(R.id.button_mainfragment_createnewalert);

        btnListCurrentAlerts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), ListActivity.class);
                startActivity(intent);
            }
        });

        btnCreateNewAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new ReportEventFragment())
                        .addToBackStack(null)
                        .commit();

            }
        });

    }
}
