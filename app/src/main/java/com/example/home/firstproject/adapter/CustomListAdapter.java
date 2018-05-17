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

public class CustomListAdapter extends ArrayAdapter {
    private Context context;
    private final ArrayList arrayList;
    private final ArrayList arrayList_photo;
    private final ArrayList arrayonline;


    public CustomListAdapter(Context context, ArrayList arrayList, ArrayList arrayList_photo, ArrayList arrayonline) {
        super(context, R.layout.mylist, arrayList);
        this.context =  context;
        this.arrayList = arrayList;
        this.arrayList_photo = arrayList_photo;
        this.arrayonline = arrayonline;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView=inflater.inflate(R.layout.mylist, null,true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView extratxt = (TextView) rowView.findViewById(R.id.textView1);


        Picasso.with(context)
                .load(String.valueOf(arrayList_photo.get(position)))
                .transform(new CircularTransformation(0))
                .into(imageView);







        txtTitle.setText((String) arrayList.get(position));
        if (Integer.parseInt(String.valueOf(arrayonline.get(position)))==1){
            extratxt.setText("Online");

        }
        else {
            extratxt.setText("");
        }


        return rowView;

    };

}
