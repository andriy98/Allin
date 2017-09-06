package com.example.home.firstproject.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.home.firstproject.R;
import com.example.home.firstproject.adapter.CustomList_fbwall;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class FB_wall_fragment  extends Fragment{
    private View view;
    private static final int LAYOUT = R.layout.fb_wall_fr;
    EditText edt1;
    ImageView btnpost;
    ArrayList<String> arraymessage = new ArrayList<>();
    ArrayList<String> arrayfrom = new ArrayList<>();
    ArrayList<String> arraydate = new ArrayList<>();
    SwipeRefreshLayout swipeRefreshLayout;

    ListView listView;
    public static FB_wall_fragment getInstance(){
        Bundle args = new Bundle();
        FB_wall_fragment fb_wall_fragment = new FB_wall_fragment();
        fb_wall_fragment.setArguments(args);
        return fb_wall_fragment;
    }
    public void makepost() throws JSONException {
        AccessToken token = AccessToken.getCurrentAccessToken();
        GraphRequest request = GraphRequest.newPostRequest(
                token,
                "/feed",
                new JSONObject(),
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                    edt1.setText("");
                        Toast.makeText(getContext(),"Запис успішно додано !", Toast.LENGTH_LONG).show();
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("message", edt1.getText().toString() );
        request.setParameters(parameters);
        request.executeAsync();
    }










    public void reqmain_fb_wall(){
        arraymessage.clear();
        arraydate.clear();
        arrayfrom.clear();
        AccessToken token = AccessToken.getCurrentAccessToken();
        GraphRequest request = GraphRequest.newGraphPathRequest(
                token,
                "/me/feed",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = (JSONObject) new JSONObject(response.getRawResponse());
                            JSONArray jsonArray = (JSONArray) jsonObject.get("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject data = (JSONObject) jsonArray.get(i);
                                arraydate.add(data.getString("created_time"));
                                arraymessage.add(data.getString("message"));
                                JSONObject name = (JSONObject) data.get("from");
                                arrayfrom.add(name.getString("name"));
                                //
                                //arraymessage.add(data.getString("message"));

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        CustomList_fbwall adapter = new CustomList_fbwall(getActivity(), arrayfrom, arraydate, arraymessage);
                        listView.setAdapter(adapter);
                        System.out.println("WALLFB" + Arrays.asList(arrayfrom));
                        System.out.println("WALLFB" + Arrays.asList(arraydate));
                        System.out.println("WALLFB" + Arrays.asList(arraymessage));

                    }
                });



        Bundle parameters = new Bundle();
        parameters.putString("fields", "from,full_picture,created_time,message");
        request.setParameters(parameters);
        request.executeAsync();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);
        listView = (ListView) view.findViewById(R.id.listview);
        edt1 = (EditText) view.findViewById(R.id.editText);
        btnpost = (ImageView) view.findViewById(R.id.button);
        btnpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    makepost();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        reqmain_fb_wall();
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reqmain_fb_wall();
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
        return view;
    }
}
