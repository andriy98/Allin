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

import java.util.ArrayList;

/**
 * Created by Andrii on 04.01.2017.
 */

public class CustomList_mes   extends ArrayAdapter {
    private Context context;
    private  ArrayList<String> izhod = new ArrayList<>();
    private  ArrayList<String> vhod = new ArrayList<>();




    public CustomList_mes(Context context, ArrayList vhod, ArrayList izhod) {
        super(context, R.layout.mylist_mes,vhod);
        this.context =  context;
        this.vhod = vhod;
        this.izhod = izhod;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView=inflater.inflate(R.layout.mylist_mes, null,true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.text1);
        TextView txtTitle2 = (TextView) rowView.findViewById(R.id.text2);









        txtTitle2.setText(String.valueOf(vhod.get(position)));
        txtTitle.setText(String.valueOf(izhod.get(position)));



        return rowView;

    };

}