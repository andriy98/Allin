package com.example.home.firstproject.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.home.firstproject.R;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VK_newsfeed_fragment extends Fragment {
    private View view;
    private static final int LAYOUT = R.layout.vk_nfeed_fr;
    public ArrayList<String> arraytext = new ArrayList<>();
    public ArrayList<String> arrayaudio = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);
        reqfeedvk();
        return view;
    }
    public static VK_newsfeed_fragment getInstance(){
        Bundle args = new Bundle();
        VK_newsfeed_fragment fragment = new VK_newsfeed_fragment();
        fragment.setArguments(args);
        return fragment;
    }
















    public void reqfeedvk(){
        arraytext.clear();
        VKParameters vkParameters = new VKParameters();
        vkParameters.put("count",10);
        VKRequest vkRequest = new VKRequest("newsfeed.get",vkParameters);
        vkRequest.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                try {
                    JSONObject jsonObject = (JSONObject) response.json.get("response");
                    JSONArray jsonArray = (JSONArray) jsonObject.get("items");
                    for (int i = 0; i<jsonArray.length();i++){
                        JSONObject name  = (JSONObject) jsonArray.get(i);
                        arraytext.add(name.getString("text"));


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        });

    }
}
