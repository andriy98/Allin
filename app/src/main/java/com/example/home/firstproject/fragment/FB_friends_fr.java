package com.example.home.firstproject.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.home.firstproject.R;
import com.example.home.firstproject.View_fbfriend_wall;
import com.example.home.firstproject.View_friend_photo;
import com.example.home.firstproject.adapter.CustomList_fbfriends;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class FB_friends_fr extends Fragment {
    private static final int LAYOUT = R.layout.fb_friends_fr;
    private View view;
    ListView listView;
    ArrayList<String> arrayname = new ArrayList<>();
    ArrayList<String> arrayphoto = new ArrayList<>();
    ArrayList<String> arrayid = new ArrayList<>();
    public static FB_friends_fr getInstance(){
        Bundle args = new Bundle();
        FB_friends_fr fragment = new FB_friends_fr();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container,false);
        listView = (ListView) view.findViewById(R.id.listview);
        registerForContextMenu(listView);
        AccessToken token = AccessToken.getCurrentAccessToken();
        GraphRequest request = GraphRequest.newGraphPathRequest(
                token,
                "/me/taggable_friends",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        System.out.println("Response error: " + response.getError());
                        System.out.println("Raw: " + response.getRawResponse());
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = (JSONObject) new JSONObject(response.getRawResponse());
                            JSONArray jsonArray = (JSONArray) jsonObject.get("data");
                            for (int i = 0; i <jsonArray.length() ; i++) {
                                JSONObject name = (JSONObject) jsonArray.get(i);
                                JSONObject message = (JSONObject) name.get("picture");
                                JSONObject picture = (JSONObject) message.get("data");
                                arrayid.add(name.getString("id"));
                                arrayname.add(name.getString("name"));
                                arrayphoto.add(picture.getString("url"));


                            }
                            System.out.println("Arena"+Arrays.asList(jsonArray));
                            System.out.println("NAMEME"+Arrays.asList(arrayphoto));
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(),
                                     android.R.layout.simple_expandable_list_item_1, arrayname);
                            listView.setAdapter(arrayAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        CustomList_fbfriends adapter = new CustomList_fbfriends(getActivity(), arrayname, arrayphoto, arrayname, arrayphoto);

                        listView.setAdapter(adapter);

                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "picture.type(large),id,name");
        request.setParameters(parameters);
        request.executeAsync();


        return view;
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_menu_fbfriends, menu);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.view_wall_fb:
                Intent intent = new Intent(getContext(), View_fbfriend_wall.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                intent.putExtra("group_id", String.valueOf(arrayid.get(info.position)));
                startActivity(intent);
                return true;
            case R.id.view_photo_fb:
                Intent intent1 = new Intent(getContext(), View_friend_photo.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                intent1.putExtra("ava_url", String.valueOf(arrayphoto.get(info.position)));
                intent1.putExtra("name", String.valueOf(arrayname.get(info.position)));
                startActivity(intent1);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}