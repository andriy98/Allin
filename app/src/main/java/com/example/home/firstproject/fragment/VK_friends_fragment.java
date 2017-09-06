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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.home.firstproject.R;
import com.example.home.firstproject.SendMessage;
import com.example.home.firstproject.View_friend_photo;
import com.example.home.firstproject.View_friend_wall;
import com.example.home.firstproject.adapter.CustomListAdapter;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.vk.sdk.VKUIHelper.getApplicationContext;

public class VK_friends_fragment extends Fragment  {
    private static final int LAYOUT = R.layout.vk_friends_fr;
    private View view;
    ImageView imageView;
    private ListView listView;
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayList<String> arrayList_photo = new ArrayList<>();
    ArrayList<String> arrayonline = new ArrayList<>();
    ArrayList<String> arrayid = new ArrayList<>();
    ArrayList<String> arrayava = new ArrayList<>();
    private SwipeRefreshLayout mSwipeRefreshLayout;


    public static VK_friends_fragment getInstance(){
        Bundle args = new Bundle();
        VK_friends_fragment fragment = new VK_friends_fragment();
        fragment.setArguments(args);
        return fragment;
    }
    public void friendsreq(){
        arrayList.clear();
        arrayList_photo.clear();
        arrayonline.clear();
        registerForContextMenu(listView);
        VKRequest vkRequest = VKApi.friends().get(VKParameters.from(VKApiConst.FIELDS, "first_name, photo_50, photo_100, photo_200_orig","order","hints"));
        vkRequest.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                VKList vkList = (VKList) response.parsedModel;

                try {
                    JSONObject jsonObject = (JSONObject) response.json.get("response");
                    JSONArray jsonArray = (JSONArray) jsonObject.get("items");

                    for (int i = 0; i<jsonArray.length();i++){
                        JSONObject name  = (JSONObject) jsonArray.get(i);
                        arrayList.add(name.getString("first_name")+" "+name.getString("last_name"));
                        arrayid.add(name.getString("id"));
                        arrayList_photo.add(name.getString("photo_100"));
                        arrayava.add(name.getString("photo_200_orig"));
                        arrayonline.add(name.getString("online"));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                CustomListAdapter adapter = new CustomListAdapter(getActivity(), arrayList, arrayList_photo, arrayonline);


                // ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(),
                // android.R.layout.simple_expandable_list_item_1, vkList);
                listView.setAdapter(adapter);

            }
        });

    }

    @Nullable

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container,false);
        listView = (ListView) view.findViewById(R.id.listview);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                friendsreq();
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
        imageView = (ImageView) view.findViewById(R.id.imageView);
        friendsreq();

        return view;

    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_menu_friends, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.leave:
                Intent intent = new Intent(getApplicationContext(),SendMessage.class );
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                intent.putExtra("id", Integer.parseInt(arrayid.get(info.position)));
                getApplicationContext().startActivity(intent);
                return true;
            case R.id.delete:
                VKRequest vkRequest1 = VKApi.friends().delete(VKParameters.from(VKApiConst.USER_ID, String.valueOf(arrayid.get(info.position))));
                vkRequest1.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onError(VKError error) {
                        super.onError(error);
                        Toast.makeText(getContext(), "Не вдалось видалити друга !" ,Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete(VKResponse response) {
                        super.onComplete(response);
                        Toast.makeText(getContext(), "Успішно !" ,Toast.LENGTH_SHORT).show();


                    }
                });
                return true;
            case R.id.view_photo:
                Intent intent1 = new Intent(getActivity(), View_friend_photo.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                intent1.putExtra("ava_url", String.valueOf(arrayava.get(info.position)));
                intent1.putExtra("name", String.valueOf(arrayList.get(info.position)));
                startActivity(intent1);
                return true;
            case R.id.view_wall:
                Intent intent_wall = new Intent(getActivity(), View_friend_wall.class);
                intent_wall.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                intent_wall.putExtra("id", String.valueOf(arrayid.get(info.position)));
                intent_wall.putExtra("ava", String.valueOf(arrayava.get(info.position)));
                intent_wall.putExtra("name", String.valueOf(arrayList.get(info.position)));
                startActivity(intent_wall);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

}






