package com.example.oemscandemo.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.oemscandemo.R;
import com.example.oemscandemo.db.DBHelper;
import com.example.oemscandemo.ui.SelectLocationActivity;
import com.example.oemscandemo.ui.SelectUploadLocationActivity;
import com.example.oemscandemo.ui.UnitTakingActivity;
import com.example.oemscandemo.utils.HomeListener;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment implements View.OnClickListener {
    private DBHelper helper;
    HomeListener mListener;
    CardView cardMaintenance, cardUnitTaking, cardDownload, cardUpload;

    public HomeFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HomeListener) {
            mListener = (HomeListener) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        String prefName = "user";
        SharedPreferences prefs = getActivity().getSharedPreferences(prefName, MODE_PRIVATE);
        int unitTakeStatus = prefs.getInt("unitTakeStatus", 0);
        if (unitTakeStatus != 1){
            view = inflater.inflate(R.layout.fragment_hom, container, false);
        }else {
            view = inflater.inflate(R.layout.fragment_home, container, false);
        }

        helper = new DBHelper(getContext());

        cardMaintenance = view.findViewById(R.id.maintenance_card);
        cardUnitTaking = view.findViewById(R.id.unit_take_card);
        cardDownload = view.findViewById(R.id.download_card);
        cardUpload = view.findViewById(R.id.upload_card);

        cardMaintenance.setOnClickListener(this);
        cardUnitTaking.setOnClickListener(this);
        cardDownload.setOnClickListener(this);
        cardUpload.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.maintenance_card:
                Intent intent = new Intent(getActivity(), SelectLocationActivity.class);
                startActivity(intent);
                break;
            case R.id.unit_take_card:
                startActivity(new Intent(getActivity(), UnitTakingActivity.class));
                break;
            case R.id.download_card:
                if (helper.getLocationCount() != 0) {
                    if (mListener != null) mListener.onDownload();
                } else {
                    Toast.makeText(getContext(), "Locations are not exists!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.upload_card:
                if (helper.getUploadMaintenanceCount() > 0 || helper.getUploadConditionMaintenanceCount() > 0 ||
                        helper.getUploadAssetConditionCount() > 0) {
                    Intent intentUpload = new Intent(getActivity(), SelectUploadLocationActivity.class);
                    startActivity(intentUpload);
                } else {
                    Toast.makeText(getContext(), "There is no maintenance to upload!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
