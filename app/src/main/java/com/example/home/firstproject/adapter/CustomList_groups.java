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



public class CustomList_groups extends ArrayAdapter {
    private Context context;
    private final ArrayList arrayList_groups;
    private final ArrayList arrayList_groups_photo;



    public CustomList_groups(Context context, ArrayList arrayList_groups, ArrayList arrayList_groups_photo) {
        super(context, R.layout.mylist_groups,arrayList_groups);
        this.context =  context;
        this.arrayList_groups = arrayList_groups;
        this.arrayList_groups_photo = arrayList_groups_photo;

    }

    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView=inflater.inflate(R.layout.mylist_groups, null,true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon_group);
        Picasso.with(context)
                .load(String.valueOf(arrayList_groups_photo.get(position)))
                .transform(new CircularTransformation(0))
                .into(imageView);








        txtTitle.setText((String) arrayList_groups.get(position));



        return rowView;

    };

}
