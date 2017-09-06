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
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiDialog;
import com.vk.sdk.api.model.VKApiGetDialogResponse;
import com.vk.sdk.api.model.VKList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class VK_msg_fragment extends Fragment {
    private View view;
    private static final int LAYOUT = R.layout.vk_msg_fr;
    private ListView listView;
    public static VKList list1;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public void reqmsg(){
        VKRequest vkRequest = VKApi.friends().get(VKParameters.from(VKApiConst.FIELDS, "first_name, photo_50", "order", "hints"));
        vkRequest.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                System.out.println("responseE" + response.responseString);
                list1 = (VKList) response.parsedModel;
            }
        });


        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        // Прізвища друзів !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!




        VKRequest vkRequest12 = VKApi.messages().getDialogs();
        vkRequest12.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                System.out.println("Pasha"+response.responseString);
                final ArrayList<String> arrayList = new ArrayList<>();
                try {

                    JSONObject jsonObject = (JSONObject) response.json.get("response");


                    JSONArray jsonArray = (JSONArray) jsonObject.get("items");



                    for (int i = 0; i<jsonArray.length();i++) {

                        JSONObject message = (JSONObject) ((JSONObject) jsonArray.get(i)).get("message");
                        if (message.getString("title").equals(" ... ")) {
                            VKParameters vkParameters = new VKParameters();
                            vkParameters.put("user_ids", message.getString("user_id"));
                            VKRequest vkRequesttit = new VKRequest("users.get", vkParameters);
                            vkRequesttit.executeWithListener(new VKRequest.VKRequestListener() {
                                @Override
                                public void onComplete(VKResponse response1) {
                                    super.onComplete(response1);
                                    try {
                                        JSONArray jsonArray1 = (JSONArray) response1.json.get("response");
                                        System.out.println("PRINTER2" + Arrays.asList(jsonArray1));

                                        for (int i1 = 0; i1 < jsonArray1.length(); i1++) {


                                            JSONObject name = (JSONObject) jsonArray1.get(i1);
                                            arrayList.add(name.getString("first_name") + " " + name.getString("last_name"));
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    // try {
                                    //JSONObject jsonObject1 = (JSONObject) response.json.get("response");


                                    // JSONArray jsonArrayf = (JSONArray) jsonObject1.get("first_name");
                                        /*for (int i1 = 0; i1<jsonArrayf.length();i1++){
                                            JSONObject name = (JSONObject) jsonArrayf.get(i1);
                                            arrayList.add(name.getString("first_name")+" "+name.getString("last_name"));


                                        }*/


                                    //  }
                                    // catch (JSONException e) {
                                    //     e.printStackTrace();
                                    // }

                                }
                            });




                        /*else {
                            arrayList.add(message.getString("title"));


                        }*/

                            //if (name2.getString())


                        }
                        else {
                            arrayList.add(message.getString("title"));
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
//////////////////////////////////////////////////////////////////////////////////////


        VKRequest vkRequest1 = VKApi.messages().getDialogs(VKParameters.from(VKApiConst.COUNT, 200));
        vkRequest1.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                VKApiGetDialogResponse getMessagesResponse = (VKApiGetDialogResponse) response.parsedModel;


                VKList<VKApiDialog> list = getMessagesResponse.items;

                ArrayList<String> messages = new ArrayList<>();

                ArrayList<String> users = new ArrayList<>();


                for (VKApiDialog msg : list) {
                    users.add(String.valueOf(list1.getById(msg.message.user_id)));
                    messages.add(msg.message.body);
                }


                listView.setAdapter(new CustomAdapter(getContext(), users, messages, list));
            }
        });

    }
    @Override
    public void onStart() {
        super.onStart();
        getActivity().setTitle("Повідомлення");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);
        listView = (ListView) view.findViewById(R.id.listview);
        reqmsg();
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reqmsg();
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
