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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.home.firstproject.Likes;
import com.example.home.firstproject.R;
import com.example.home.firstproject.adapter.CustomList_wall;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKWallPostResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class VK_wall_fragment extends Fragment {
    private static final int LAYOUT = R.layout.vk_wall_fragment;
    private View view;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    ArrayList<String> arraypost = new ArrayList<>();
    ArrayList<String> arraydate = new ArrayList<>();
    ArrayList<String> arrayphoto = new ArrayList<>();
    ArrayList<String> arraycheck = new ArrayList<>();
    ArrayList<String> arrayfrom = new ArrayList<>();
    ArrayList<String> arrayname = new ArrayList<>();
    ArrayList<String> arrayidpost = new ArrayList<>();
    ListView listView;
    EditText editText;
    ImageView img_button;
    VKAccessToken vkAccessToken = VKAccessToken.currentToken();
    public void requ(){
    arrayname.clear();

        for (int i=0;i<arrayfrom.size();i++){
            VKRequest vkRequest = VKApi.users().get(VKParameters.from(VKApiConst.USER_IDS, arrayfrom.get(i)));
            /*VKParameters vkParameters = new VKParameters();
            vkParameters.put("user_ids", arrayfrom.get(i));
            VKRequest vkRequesttit = new VKRequest("users.get", vkParameters);

*/


            vkRequest.executeWithListener(new VKRequest.VKRequestListener() {
                @Override
                public void onComplete(VKResponse response1) {
                    super.onComplete(response1);
                    try {
                        JSONArray jsonArray1 = (JSONArray) response1.json.get("response");
///                        for (int j=0;j<arrayfrom.size();j++) {
                            JSONObject name = (JSONObject) jsonArray1.get(0);
                        arrayname.add(name.getString("first_name")+" "+name.getString("last_name"));
                        if (arrayname.size() == arrayfrom.size()){

                            CustomList_wall adapter = new CustomList_wall(getActivity(), arraypost, arraydate, arraycheck, arrayphoto, arrayname);
                            listView.setAdapter(adapter);
                            //tut adapter mosh
                        }
                        //}
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }




                }
            });
        }


    }

    public void reqmain(){
        arraydate.clear();
        arraypost.clear();
        arraycheck.clear();
        arrayfrom.clear();
        arrayidpost.clear();
        arrayphoto.clear();
        registerForContextMenu(listView);
                        final VKRequest vkRequest1 = VKApi.wall().get(VKParameters.from(VKApiConst.OWNER_ID, vkAccessToken.userId));
                        vkRequest1.executeWithListener(new VKRequest.VKRequestListener() {
                            @Override
                            public void onComplete(VKResponse response) {
                                super.onComplete(response);
                                try {
                                    JSONObject jsonObject = (JSONObject) response.json.get("response");
                                    JSONArray jsonArray = (JSONArray) jsonObject.get("items");
                                    for (int i = 0; i<jsonArray.length();i++){
                                        JSONObject name  = (JSONObject) jsonArray.get(i);
                                        arraypost.add(name.getString("text"));
                                        arraydate.add(name.getString("date"));
                                        arrayfrom.add(name.getString("from_id"));
                                        arrayidpost.add(name.getString("id"));
                                        if (((JSONObject) jsonArray.get(i)).has("attachments")) {
                                            JSONArray jsonArray1 = name.getJSONArray("attachments");
                                            for (int j=0;j<jsonArray1.length();j++) {
                                                JSONObject object = (JSONObject) jsonArray1.getJSONObject(j).get("photo");
                                                arrayphoto.add(object.getString("photo_604"));
                                            }
                                            arraycheck.add("Enabled");
                                        }
                                        else
                                        {
                                            arraycheck.add("Disenabled");
                                            arrayphoto.add("");
                                        }
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                requ();




                            }
                        });
                }


    public static VK_wall_fragment getInstance(){
        Bundle args = new Bundle();
        VK_wall_fragment fragment = new VK_wall_fragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void makePost() {
        VKParameters parameters = new VKParameters();
        parameters.put(VKApiConst.MESSAGE, editText.getText());
        VKRequest post = VKApi.wall().post(parameters);
        post.setModelClass(VKWallPostResult.class);
        post.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                Toast.makeText(getContext(),"Запис успішно додано !", Toast.LENGTH_LONG).show();
                editText.setText("");
                // post was added
            }
            @Override
            public void onError(VKError error) {
                Toast.makeText(getContext(),"Помилка", Toast.LENGTH_LONG).show();
                // error
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        arraypost.clear();
        img_button = (ImageView) view.findViewById(R.id.button);
        editText = (EditText) view.findViewById(R.id.editText);
        img_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.length()>0){
                    makePost();
                }
                else{
                    Toast.makeText(getContext(),"Введіть текст запису",Toast.LENGTH_LONG).show();
                }
            }
        });
        listView = (ListView) view.findViewById(R.id.listview);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        reqmain();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reqmain();
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

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT,container,false);
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
                final String type="post";
                VKRequest vkRequest = new VKRequest("likes.add",VKParameters.from("type", type,"item_id", String.valueOf(arrayidpost.get(info.position))));
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
                intent_likes.putExtra("item_id", String.valueOf(arrayidpost.get(info.position)));
                intent_likes.putExtra("type", "post");
                startActivity(intent_likes);
                return true;
            case R.id.delete_post:
                VKRequest vkRequest1 = VKApi.wall().delete(VKParameters.from("post_id",arrayidpost.get(info.position)));
                vkRequest1.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onError(VKError error) {
                        super.onError(error);
                        Toast.makeText(getContext(), "Не вдалось видалити запис !",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete(VKResponse response) {
                        super.onComplete(response);
                        Toast.makeText(getContext(), "Запис успішно видалено !",Toast.LENGTH_LONG).show();
                    }
                });
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
