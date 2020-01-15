package com.example.oemscandemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.oemscandemo.R;
import com.example.oemscandemo.model.MaintenanceDetail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TimeHistoryAdapter extends BaseAdapter {
    private List<MaintenanceDetail> detailList;
    private Context context;

    public TimeHistoryAdapter(List<MaintenanceDetail> detailList, Context context) {
        this.detailList = detailList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return detailList.size();
    }

    @Override
    public Object getItem(int position) {
        return detailList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        TextView date, title;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.history_item, parent, false);
        }
        MaintenanceDetail maintenanceDetail = (MaintenanceDetail) getItem(position);
        date = view.findViewById(R.id.history_date);
        title = view.findViewById(R.id.history_title);
        switch (maintenanceDetail.getStatus()) {
            case "STARTED":
                title.setText(R.string.start_date);
                break;
            case "PAUSED":
                title.setText(R.string.pause_date);
                break;
            case "RESUME":
                title.setText(R.string.resume_date);
                break;
            case "FINISHED":
                title.setText(R.string.finish_date);
                break;
        }
        String updatedDate = convertStringDateToAnotherStringDate(maintenanceDetail.getUpdatedDateAsString(), "yyyy-MM-dd'T'HH:mm:ss.SSSZ", "yyyy-MM-dd hh:mm:ss a");
        date.setText(updatedDate);
        return view;
    }

    private String convertStringDateToAnotherStringDate(String stringDate, String stringDateFormat, String returnDateFormat) {

        try {
            Date date = new SimpleDateFormat(stringDateFormat).parse(stringDate);
            return new SimpleDateFormat(returnDateFormat).format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
}
