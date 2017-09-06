package com.example.home.firstproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.home.firstproject.adapter.CustomList_friendwall;
import com.squareup.picasso.Picasso;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class View_friend_wall extends AppCompatActivity {
    private String id;
    private String ava;
    private String name;
    EditText Text;
    ImageView addpost;
    ImageView ava_photo;
    ListView listView;
    ArrayList<String> arraypost = new ArrayList<>();
    ArrayList<String> arraydate = new ArrayList<>();
    ArrayList<String> arrayid = new ArrayList<>();
    ArrayList<String> arrayphoto = new ArrayList<>();
    SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_friend_wall);
        id = getIntent().getStringExtra("id");
        ava = getIntent().getStringExtra("ava");
        name = getIntent().getStringExtra("name");
        setTitle(name);
        ava_photo = (ImageView) findViewById(R.id.ava);
        ava_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), View_friend_photo.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                intent1.putExtra("ava_url", ava);
                intent1.putExtra("name", name);
                startActivity(intent1);
            }
        });
        Text = (EditText) findViewById(R.id.editText);
        addpost = (ImageView) findViewById(R.id.button);
        addpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reqpost();
            }
        });
        listView =(ListView) findViewById(R.id.listview);
        Picasso.with(getApplicationContext())
                .load(ava)
                .into(ava_photo);
        reqmain();
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reqmain();
                swipeRefreshLayout.setRefreshing(false);

            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRowVerticalPosition = (listView == null || listView.getChildCount() == 0) ? 0 : listView.getChildAt(0).getTop();
                swipeRefreshLayout.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
            }
        });


    }

    public void reqmain(){
        arraydate.clear();
        arraypost.clear();
        VKRequest vkRequest1 = VKApi.wall().get(VKParameters.from(VKApiConst.OWNER_ID, id,VKApiConst.COUNT,100));
        vkRequest1.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                System.out.println(response.responseString);
                try {
                    JSONObject jsonObject = (JSONObject) response.json.get("response");
                    JSONArray jsonArray = (JSONArray) jsonObject.get("items");
                    System.out.println("Ident"+Arrays.asList(jsonArray));
                    for (int i = 0; i<jsonArray.length();i++){
                        JSONObject name  = (JSONObject) jsonArray.get(i);
                        arraypost.add(name.getString("text"));
                        arraydate.add(name.getString("date"));
                        arrayid.add(name.getString("from_id"));
                        if (((JSONObject) jsonArray.get(i)).has("attachments")) {
                            JSONArray jsonArray1 = name.getJSONArray("attachments");
                            System.out.println("Karpaty"+jsonArray1);
                            for (int j=0;j<jsonArray1.length();j++) {
                                JSONObject object = (JSONObject) jsonArray1.getJSONObject(j).get("photo");
                                if (jsonArray1.getJSONObject(j).getString("type").equals("video") ||
                                        jsonArray1.getJSONObject(j).getString("type").equals("doc") ||
                                        jsonArray1.getJSONObject(j).getString("type").equals("link")||
                                        jsonArray1.getJSONObject(j).getString("type").equals("audio")
                                        ){
                                    arrayphoto.add("");
                                }
                                else{
                                    arrayphoto.add(object.getString("photo_604"));
                                }
                            }
                        }
                        else
                        {
                            arrayphoto.add("");
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println("IDENT"+ Arrays.asList(arraypost));
                System.out.println("IDENT2"+Arrays.asList(arrayid));
                System.out.println("weraf"+Arrays.asList(arrayphoto));
                CustomList_friendwall adapter = new CustomList_friendwall(getApplicationContext(), arraypost, arraydate,arrayid,arrayphoto);
                listView.setAdapter(adapter);

            }
        });
    }
    public void reqpost(){
        VKRequest vkRequest1 = VKApi.wall().post(VKParameters.from(VKApiConst.OWNER_ID, id, VKApiConst.MESSAGE, Text.getText()));
        vkRequest1.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onError(VKError error) {
                super.onError(error);
                System.out.println("STAS"+error.errorMessage);
                Toast.makeText(getApplicationContext(), "Помилка !",Toast.LENGTH_LONG).show();

            }

            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                Toast.makeText(getApplicationContext(), "Запис успішно додано !",Toast.LENGTH_LONG).show();
                Text.setText("");

            }


        });
    }

}
