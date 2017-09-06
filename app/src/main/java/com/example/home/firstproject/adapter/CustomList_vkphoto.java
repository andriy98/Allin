package com.example.home.firstproject.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.home.firstproject.R;
import com.example.home.firstproject.View_friend_photo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomList_vkphoto  extends ArrayAdapter {
    private Context context;
    private final ArrayList arrayphotourl;
    private final ArrayList arrayphotodate;


    public CustomList_vkphoto(Context context, ArrayList arrayphotourl, ArrayList arrayphotodate) {
        super(context, R.layout.mylist_photo, arrayphotourl);
        this.context = context;
        this.arrayphotodate = arrayphotodate;
        this.arrayphotourl = arrayphotourl;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView=inflater.inflate(R.layout.mylist_photo, null,true);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.item_photo);
        TextView dateTitle = (TextView) rowView.findViewById(R.id.item_date);
        dateTitle.setText((String) arrayphotodate.get(position));
        Picasso.with(context)
                .load(String.valueOf(arrayphotourl.get(position)))
                .into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getContext(), View_friend_photo.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                intent1.putExtra("ava_url", String.valueOf(arrayphotourl.get(position)));
                intent1.putExtra("name", String.valueOf(arrayphotodate.get(position)));
                getContext().startActivity(intent1);
            }
        });






        return rowView;

    };

}
