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

public class CustomList_fbfriends extends ArrayAdapter {
    private Context context;
    private final ArrayList arrayname;
    private final ArrayList arrayphoto;



    public CustomList_fbfriends(Context context, ArrayList arrayList_groups, ArrayList arrayList_groups_photo, ArrayList arrayname, ArrayList arrayphoto) {
        super(context, R.layout.mylist_fbfriends,arrayList_groups);
        this.context =  context;
        this.arrayname = arrayname;
        this.arrayphoto = arrayphoto;


    }

    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView=inflater.inflate(R.layout.mylist_fbfriends, null,true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon_group);
        Picasso.with(context)
                .load(String.valueOf(arrayphoto.get(position)))
                .transform(new CircularTransformation(0))
                .into(imageView);








        txtTitle.setText((String) arrayname.get(position));



        return rowView;

    };

}
