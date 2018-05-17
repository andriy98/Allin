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

import com.example.home.firstproject.Likes;
import com.example.home.firstproject.R;
import com.example.home.firstproject.adapter.CustomList_vkphoto;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class VK_photo_fragment extends Fragment {
    private static final int LAYOUT = R.layout.vk_photo_fr;
    private View view;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    ArrayList<String> arrayphotourl = new ArrayList<>();
    ArrayList<String> arrayphotodate = new ArrayList<>();
    ArrayList<String> arrayphotoid = new ArrayList<>();
    ListView listView;
    public static VK_photo_fragment getInstance(){
        Bundle args = new Bundle();
        VK_photo_fragment fragment = new VK_photo_fragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT,container,false);
        listView = (ListView) view.findViewById(R.id.listview);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        reqphotomain();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reqphotomain();
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_menu_vkwall, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.like:
                final String type="photo";
                VKRequest vkRequest = new VKRequest("likes.add",VKParameters.from("type", type,"item_id", String.valueOf(arrayphotoid.get(info.position))));
                vkRequest.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onError(VKError error) {
                        super.onError(error);

                    }

                    @Override
                    public void onComplete(VKResponse response) {
                        super.onComplete(response);
                        Toast.makeText(getContext(),"Лайк додано",Toast.LENGTH_LONG).show();

                    }
                });
                return true;
            case R.id.view_likes:
                Intent intent_likes = new Intent(getActivity(), Likes.class);
                intent_likes.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                intent_likes.putExtra("item_id", String.valueOf(arrayphotoid.get(info.position)));
                intent_likes.putExtra("type", "photo");
                startActivity(intent_likes);
                return true;
            case R.id.delete_post:
                VKRequest vkRequest1 = new VKRequest("photos.delete", VKParameters.from("photo_id",String.valueOf(arrayphotoid.get(info.position))));
                vkRequest1.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onError(VKError error) {
                        super.onError(error);
                        Toast.makeText(getContext(), "Помилка !", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete(VKResponse response) {
                        super.onComplete(response);
                        Toast.makeText(getContext(), "Фото успішно видалено !", Toast.LENGTH_LONG).show();
                    }
                });
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }



    public void reqphotomain(){
        arrayphotodate.clear();
        arrayphotoid.clear();
        arrayphotourl.clear();
        registerForContextMenu(listView);
        VKRequest vkRequest = new VKRequest("photos.getAll", VKParameters.from(VKApiConst.EXTENDED,1));
        vkRequest.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onError(VKError error) {
                super.onError(error);
            }

            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                try {
                    JSONObject jsonObject = (JSONObject) response.json.get("response");
                    JSONArray jsonArray = (JSONArray) jsonObject.get("items");
                    for (int i = 0; i<jsonArray.length();i++){
                        JSONObject name  = (JSONObject) jsonArray.get(i);
                        arrayphotourl.add(name.getString("photo_604"));
                        arrayphotoid.add(name.getString("id"));
                        long unixSeconds =  Long.parseLong((name.getString("date")));
                        Date date = new Date(unixSeconds*1000L); // *1000 is to convert seconds to milliseconds
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm"); // the format of your date
                        sdf.setTimeZone(TimeZone.getTimeZone("GMT+02")); // give a timezone reference for formating (see comment at the bottom
                        final String formattedDate = sdf.format(date);
                        arrayphotodate.add(formattedDate);





                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                CustomList_vkphoto adapter = new CustomList_vkphoto(getActivity(), arrayphotourl,arrayphotodate);
                listView.setAdapter(adapter);
            }
        });

    }
}
