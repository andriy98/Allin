package com.example.home.firstproject;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.home.firstproject.adapter.CustomList_mes;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import java.util.ArrayList;
import java.util.Arrays;

public class SendMessage extends Activity{
    ArrayList<String> inList = new ArrayList<>();
    ArrayList<String> outList = new ArrayList<>();
    ArrayList<String> vhod = new ArrayList<>();
    ArrayList<String> izhod = new ArrayList<>();


    int id = 0;

    EditText text;
    ImageView send;
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialogs);

        //inList = getIntent().getStringArrayListExtra("in");
       // outList = getIntent().getStringArrayListExtra("out");
        //vhod = getIntent().getStringArrayListExtra("vhod");
        //izhod = getIntent().getStringArrayListExtra("izhod");
        id = getIntent().getIntExtra("id",0);

        text = (EditText) findViewById(R.id.edittext);
        text.setText("");
       // Arrays.sort(inList.toArray(), Collections.reverseOrder());
       // Arrays.sort(outList.toArray(), Collections.reverseOrder());



        listView = (ListView) findViewById(R.id.listMsg);
        CustomList_mes adapter = new CustomList_mes(getApplicationContext(), vhod, izhod);

       // listView.setAdapter(new CustomAdapter(getApplicationContext(),vhod,izhod));
         //listView.setAdapter(adapter);
          send = (ImageView) findViewById(R.id.button);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VKRequest request = new VKRequest("messages.send", VKParameters.from(VKApiConst.USER_ID,id, VKApiConst.MESSAGE, text.getText().toString()));

                request.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onError(VKError error) {
                        super.onError(error);
                    }

                    @Override
                    public void onComplete(VKResponse response) {
                        super.onComplete(response);
                        Toast.makeText(getApplicationContext(),"Повідомлення відправлено !",Toast.LENGTH_LONG).show();
                        text.setText("");
                    }
                });

            }
        });

    }
}
