package com.example.home.firstproject;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.example.home.firstproject.adapter.CustomList_likes;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import android.support.v7.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Likes extends AppCompatActivity {
    public String id,type;
    ListView listView;
    ArrayList<String> arrayname = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likes);
        id = getIntent().getStringExtra("item_id");
        listView = (ListView) findViewById(R.id.listview);
        type = getIntent().getStringExtra("type");
        VKRequest vkRequest = new VKRequest("likes.getList", VKParameters.from("type", type,"item_id", id,"filter", "likes", VKApiConst.EXTENDED,1));
        vkRequest.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onError(VKError error) {
                super.onError(error);
                Toast.makeText(getApplicationContext(),"Помилка !",Toast.LENGTH_LONG).show();

            }

            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                try {
                    JSONObject jsonObject = (JSONObject) response.json.get("response");
                    JSONArray jsonArray = (JSONArray) jsonObject.get("items");

                    for (int i = 0; i<jsonArray.length();i++){
                        JSONObject name  = (JSONObject) jsonArray.get(i);
                        arrayname.add(name.getString("first_name")+" "+ name.getString("last_name"));
                    }
                    if (arrayname.size()==0){
                        setTitle("Ніхто не оцінив цей запис !");
                    }
                    else if (arrayname.size()==1){
                        setTitle("Сподобалось"+" "+arrayname.size()+"-му"+" "+"користувачу");
                    }
                    else {
                        setTitle("Сподобалось" + " " + arrayname.size() + "-ом" + " " + "користувачам");
                    }
                    CustomList_likes adapter = new CustomList_likes(getApplicationContext(), arrayname);
                    listView.setAdapter(adapter);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        });
    }
}
