package com.example.home.firstproject.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.home.firstproject.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class CustomList_wall extends ArrayAdapter {
    private Context context;
    private final ArrayList arraypost;
    private final ArrayList arraydate;
    private final ArrayList arraycheck;
    private final ArrayList arrayphoto;
    private final ArrayList arrayname;



    public CustomList_wall(Context context, ArrayList arraypost, ArrayList arraydate, ArrayList arraycheck, ArrayList arrayphoto,ArrayList arrayname) {
        super(context, R.layout.mylist_wall,arraypost);
        this.context =  context;
        this.arraypost = arraypost;
        this.arraydate = arraydate;
        this.arraycheck = arraycheck;
        this.arrayphoto = arrayphoto;
        this.arrayname = arrayname;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView=inflater.inflate(R.layout.mylist_wall, null,true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.item_post);
        TextView dateTitle = (TextView) rowView.findViewById(R.id.item_date);
        TextView nameTitle = (TextView) rowView.findViewById(R.id.item_name);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        txtTitle.setText((String) arraypost.get(position));
        if (String.valueOf(arrayphoto.get(position)).equals("")){

        }
        else {
            Picasso.with(context)
                    .load(String.valueOf(arrayphoto.get(position)))
                    .into(imageView);
        }


        long unixSeconds =  Long.parseLong((String) arraydate.get(position));
        Date date = new Date(unixSeconds*1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm"); // the format of your date
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+02")); // give a timezone reference for formating (see comment at the bottom
        String formattedDate = sdf.format(date);
        dateTitle.setText(formattedDate);
        try {
            nameTitle.setText((String) arrayname.get(position));
        }catch (IndexOutOfBoundsException e){

        }




        return rowView;

    };





}
