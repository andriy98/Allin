package com.example.home.firstproject.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.home.firstproject.R;
import com.example.home.firstproject.View_friend_wall;
import com.example.home.firstproject.adapter.CustomList_groups;
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

public class VK_groups_fragment  extends Fragment{
    private static final int LAYOUT = R.layout.vk_group_fr;
    private View view;
    private ListView listView;
    ArrayList<String> arraylist_groups = new ArrayList<>();
    ArrayList<String> arraylist_groups_id = new ArrayList<>();
    ArrayList<String> arraylist_groups_photo = new ArrayList<>();
    ArrayList<String> arraynew = new ArrayList<>();
    ArrayList<String> arrayorigphoto = new ArrayList<>();
    private SwipeRefreshLayout mSwipeRefreshLayout;

        @Override
    public void onStart() {
        super.onStart();
        getActivity().setTitle("Спільноти");
    }

    public void reqgroups(){
        arraylist_groups_photo.clear();
        arraylist_groups.clear();
        registerForContextMenu(listView);
        VKRequest vkRequest = VKApi.groups().get(VKParameters.from(VKApiConst.EXTENDED, 1, "name"));
        vkRequest.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                System.out.println(response.responseString);



                try {

                    JSONObject jsonObject = (JSONObject) response.json.get("response");
                    JSONArray jsonArray = (JSONArray) jsonObject.get("items");
                    System.out.println("ssshsh"+jsonArray);
                    for (int i = 0; i<jsonArray.length();i++){
                        JSONObject name  = (JSONObject) jsonArray.get(i);
                        arraylist_groups.add(name.getString("name"));
                        arraylist_groups_photo.add(name.getString("photo_100"));
                        arrayorigphoto.add(name.getString("photo_200"));
                        arraylist_groups_id.add(name.getString("id"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                CustomList_groups adapter = new CustomList_groups(getActivity(), arraylist_groups, arraylist_groups_photo);
                listView.setAdapter(adapter);
            }
        });

    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.view_wall:
                VKRequest vkRequest = VKApi.groups().getById(VKParameters.from(VKApiConst.GROUP_ID, String.valueOf(arraylist_groups_id.get(info.position))));
                vkRequest.executeWithListener(new VKRequest.VKRequestListener() {

                    @Override
                    public void onComplete(VKResponse response) {
                        super.onComplete(response);
                        System.out.println("SASAR" +response.responseString);
                        try {
                            JSONArray jsonArray = (JSONArray) response.json.get("response");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject name = (JSONObject) jsonArray.get(i);
                                arraynew.add(name.getString("name"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        System.out.println("SASAR1"+ Arrays.asList(arraynew));
                    }



                });
                Intent intent_wall = new Intent(getActivity(), View_friend_wall.class);
                intent_wall.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                intent_wall.putExtra("id", "-"+String.valueOf(arraylist_groups_id.get(info.position)));
                intent_wall.putExtra("name", String.valueOf(arraylist_groups.get(info.position)));
                intent_wall.putExtra("ava", String.valueOf(arrayorigphoto.get(info.position)));
                startActivity(intent_wall);
                return true;
            case R.id.leave:
                VKRequest vkRequest1 = VKApi.groups().leave(VKParameters.from(VKApiConst.GROUP_ID,String.valueOf(arraylist_groups_id.get(info.position))));
                vkRequest1.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onError(VKError error) {
                        super.onError(error);
                        Toast.makeText(getContext(), "Не вдалось вийти зі спільноти !" ,Toast.LENGTH_SHORT).show();
                        }

                    @Override
                    public void onComplete(VKResponse response) {
                        super.onComplete(response);
                        System.out.println("HERE"+response.responseString);
                        Toast.makeText(getContext(), "Успішно !" ,Toast.LENGTH_SHORT).show();
                    }
                });
                return true;
          default:
                return super.onContextItemSelected(item);
        }
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container,false);
        listView = (ListView) view.findViewById(R.id.listview);
        reqgroups();
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reqgroups();
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
}
