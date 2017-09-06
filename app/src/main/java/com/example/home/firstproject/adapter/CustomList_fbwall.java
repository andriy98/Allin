package com.example.home.firstproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.home.firstproject.R;

import java.util.ArrayList;

public class CustomList_fbwall extends ArrayAdapter {
    private Context context;
    private final ArrayList arrayfrom;
    private final ArrayList arraydate;
    private final ArrayList arraymessage;


    public CustomList_fbwall(Context context, ArrayList arrayfrom, ArrayList arraydate, ArrayList arraymessage) {
        super(context, R.layout.mylist_fbwall,arrayfrom);
        this.context = context;
        this.arrayfrom = arrayfrom;
        this.arraydate = arraydate;
        this.arraymessage = arraymessage;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.mylist_fbwall, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.item_post);
        TextView dateTitle = (TextView) rowView.findViewById(R.id.textView);
        TextView Title = (TextView) rowView.findViewById(R.id.item_date);
        txtTitle.setText((String) arraymessage.get(position));
        dateTitle.setText((String) arrayfrom.get(position));
        String date1 = (String) arraydate.get(position);
        String date = date1.replace("T"," ");
        Title.setText(date.substring(0,16));









        return rowView;
    }
}
