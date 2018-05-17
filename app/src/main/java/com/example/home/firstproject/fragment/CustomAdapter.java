package com.example.home.firstproject.fragment;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.home.firstproject.R;
import com.example.home.firstproject.SendMessage;
import com.example.home.firstproject.adapter.CustomList_groups;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiDialog;
import com.vk.sdk.api.model.VKApiMessage;
import com.vk.sdk.api.model.VKList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import static com.example.home.firstproject.R.layout.activity_vk_auth;
import static com.example.home.firstproject.R.layout.vk_msg_fr;
import static com.vk.sdk.VKUIHelper.getApplicationContext;


public class CustomAdapter extends BaseAdapter {

    private ArrayList<String> useres, messages;
    private Context context;
    private VKList<VKApiDialog> list;


    public CustomAdapter(Context context, ArrayList<String> useres, ArrayList<String> messages, VKList<VKApiDialog> list) {
        this.useres = useres;
        this.messages = messages;
        this.context = context;
        this.list = list;
    }



    public CustomAdapter(Context context, ArrayList<String> useres, ArrayList<String> messages ) {
        this.useres = useres;
        this.messages = messages;
        this.context = context;
    }

    @Override
    public int getCount() {
        return useres.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        SetData setData = new SetData();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View view =inflater.inflate(R.layout.style_list_view,null);
        setData.usee_name = (TextView) view.findViewById(R.id.user_name);
        setData.msg = (TextView) view.findViewById(R.id.msg);
        setData.usee_name.setText(useres.get(position));
        setData.msg.setText(messages.get(position));

        if (list!=null){
            view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final ArrayList<String> inList = new ArrayList<>();
                final ArrayList<String> outList = new ArrayList<>();
                final ArrayList<String> vhod = new ArrayList<String>();
                final ArrayList<String> izhod = new ArrayList<String>();
                final int id = list.get(position).message.user_id;

                VKRequest vkRequest = new VKRequest("messages.getHistory", VKParameters.from(VKApiConst.USER_ID, id));
                vkRequest.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        super.onComplete(response);
                        try {

                            JSONObject jsonObject = (JSONObject) response.json.get("response");
                            JSONArray jsonArray = (JSONArray) jsonObject.get("items");
                            for (int i = 0; i<jsonArray.length();i++){
                                JSONObject name  = (JSONObject) jsonArray.get(i);

                                if (Integer.parseInt(String.valueOf(name.getString("user_id")))==id &&  (Integer.parseInt(String.valueOf(name.getString("from_id")))==id)){
                                    vhod.add(name.getString("body"));

                                }
                                else {
                                    izhod.add(name.getString("body"));
                                }






                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });




                VKRequest request = new VKRequest("messages.getHistory", VKParameters.from(VKApiConst.USER_ID, id));
                request.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        super.onComplete(response);
                        try {
                            JSONArray array = response.json.getJSONObject("response").getJSONArray("items");
                            VKApiMessage [] msg = new VKApiMessage[array.length()];
                           /*






                            }
                            */

                            for (int i = 0; i < array.length();i++) {
                                VKApiMessage mes = new VKApiMessage(array.getJSONObject(i));
                                msg[i] = mes;
                            }

                            for (VKApiMessage mess : msg ){
                                if (mess.out){
                                    outList.add(mess.body);

                                }else {
                                    inList.add(mess.body);
                                }
                            }


                            Intent intent = new Intent(getApplicationContext(),SendMessage.class );
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                            //intent.putExtra("id", id).putExtra("inList", inList).putExtra("out",outList);
                            intent.putExtra("id", id).putExtra("vhod", vhod).putExtra("izhod",izhod);
                            getApplicationContext().startActivity(intent);
                             //context.startActivity(, SendMessage.class).putExtra("id", id).putExtra("inList", inList).putExtra("out",outList));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });


            }
        });}else {
            Toast.makeText(getApplicationContext(),"list!=0",Toast.LENGTH_LONG);
        }



        return view;
    }

    public class SetData {
    TextView usee_name, msg;

    }

}
