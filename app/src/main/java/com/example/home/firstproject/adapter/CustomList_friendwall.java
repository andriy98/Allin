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
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class CustomList_friendwall extends ArrayAdapter {
    private Context context;
    private final ArrayList arraypost;
    private final ArrayList arraydate;
    private final ArrayList arrayid;
    private final ArrayList arrayphoto;

    public CustomList_friendwall(Context context, ArrayList arraypost, ArrayList arraydate,ArrayList arrayid,ArrayList arrayphoto) {
        super(context, R.layout.mylist_friendwall,arraypost);
        this.context =  context;
        this.arraypost = arraypost;
        this.arraydate = arraydate;
        this.arrayid = arrayid;
        this.arrayphoto = arrayphoto;
        }
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView=inflater.inflate(R.layout.mylist_friendwall, null,true);
        final ArrayList arraynew = new ArrayList();
        TextView txtTitle = (TextView) rowView.findViewById(R.id.item_post);
        TextView dateTitle = (TextView) rowView.findViewById(R.id.item_date);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        final TextView nameTitle = (TextView) rowView.findViewById(R.id.item_name);
        txtTitle.setText((String) arraypost.get(position));
        long unixSeconds =  Long.parseLong((String) arraydate.get(position));
        Date date = new Date(unixSeconds*1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm"); // the format of your date
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+02")); // give a timezone reference for formating (see comment at the bottom
        String formattedDate = sdf.format(date);
        dateTitle.setText(formattedDate);
        String id = (String)arrayid.get(position);
        if (id.contains("-")) {
            VKRequest vkRequest = VKApi.groups().getById(VKParameters.from(VKApiConst.GROUP_ID, id.substring(1)));
            vkRequest.executeWithListener(new VKRequest.VKRequestListener() {

                @Override
                public void onComplete(VKResponse response) {
                    super.onComplete(response);
                    try {
                        JSONArray jsonArray = (JSONArray) response.json.get("response");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject name = (JSONObject) jsonArray.get(i);
                            nameTitle.setText(name.getString("name"));

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }



            });

        }
        if (arrayphoto.size()>0) {
            try {
                if (String.valueOf(arrayphoto.get(position)).equals("")) {

                } else {
                    Picasso.with(context)
                            .load(String.valueOf(arrayphoto.get(position)))
                            .into(imageView);
                }
            }catch (IndexOutOfBoundsException e){

            }
        }
        return rowView;

    };


}
