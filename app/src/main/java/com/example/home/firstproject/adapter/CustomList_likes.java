package com.example.home.firstproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.home.firstproject.R;

import java.util.ArrayList;

public class CustomList_likes extends ArrayAdapter {
    private Context context;
    private final ArrayList arrayname;



    public CustomList_likes(Context context, ArrayList arrayname) {
        super(context, R.layout.mylist_likes, arrayname);
        this.context = context;
        this.arrayname = arrayname;
    }
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.mylist_likes, null, true);
        TextView name = (TextView) rowView.findViewById(R.id.item_name);
        name.setText((String) arrayname.get(position));
        return rowView;
    }
}
