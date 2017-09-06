package com.example.home.firstproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

public class View_fbfriend_wall extends AppCompatActivity {
    public String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_fbfriend_wall);
        id = getIntent().getStringExtra("group_id");
        //getfriendwall();

    }

public void getfriendwall(){
    AccessToken token = AccessToken.getCurrentAccessToken();
    GraphRequest request = GraphRequest.newGraphPathRequest(
            token,
            id+"/feed",
            new GraphRequest.Callback() {
                @Override
                public void onCompleted(GraphResponse response) {

                }
            });

    Bundle parameters = new Bundle();
    parameters.putString("fields", "from,full_picture,created_time,message");
    request.setParameters(parameters);
    request.executeAsync();

}




}


