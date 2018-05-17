package com.example.home.firstproject.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.example.home.firstproject.R;
import com.example.home.firstproject.adapter.CustomList_vkphoto;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FB_photo_fragment extends Fragment {
    private static final int LAYOUT = R.layout.fb_photo_fr;
    private View view;
    ListView listView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    ArrayList<String> arrayphotourl = new ArrayList<>();
    ArrayList<String> arrayphotodate = new ArrayList<>();
    public static FB_photo_fragment getInstance(){
        Bundle args = new Bundle();
        FB_photo_fragment fragment = new FB_photo_fragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT,container,false);
        listView = (ListView) view.findViewById(R.id.listview);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        reqfbphoto();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reqfbphoto();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRowVerticalPosition = (listView == null || listView.getChildCount() == 0) ? 0 : listView.getChildAt(0).getTop();
                mSwipeRefreshLayout.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
            }
        });


        return view;
    }

    public void reqfbphoto(){
        arrayphotodate.clear();
        arrayphotourl.clear();
        AccessToken token = AccessToken.getCurrentAccessToken();
        GraphRequest request = GraphRequest.newGraphPathRequest(
                token,
                "/me/photos/uploaded",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        // Insert your code here
                        JSONObject jsonObject;
                        try {
                            jsonObject = (JSONObject) new JSONObject(response.getRawResponse());
                            JSONArray jsonArray = (JSONArray) jsonObject.get("data");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject name = (JSONObject) jsonArray.get(i);
                                String date1 = (String) name.getString("created_time");
                                String date = date1.replace("T"," ");
                                arrayphotodate.add(date.substring(0,16));
                                JSONArray jsonArray1 = name.getJSONArray("images");
                                JSONObject source = (JSONObject) jsonArray1.get(0);
                                arrayphotourl.add(source.getString("source"));

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        CustomList_vkphoto adapter = new CustomList_vkphoto(getActivity(), arrayphotourl,arrayphotodate);
                        listView.setAdapter(adapter);
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "images,created_time");
        request.setParameters(parameters);
        request.executeAsync();

    }


}
