package com.example.home.firstproject.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.home.firstproject.R;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

import retrofit2.Call;

public class TW_wall_fragment extends ListFragment {
    private static final int LAYOUT = R.layout.tw_wall_fr;
    private View view;
    EditText edt1;
    ImageView btnpost;
    SwipeRefreshLayout swipeRefreshLayout;

    public static TW_wall_fragment getInstance(){
        Bundle args = new Bundle();
        TW_wall_fragment fragment = new TW_wall_fragment();
        fragment.setArguments(args);
        return fragment;
    }







    public void getwalltw(){
        final UserTimeline userTimeline = new UserTimeline.Builder()
                .build();
        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(getActivity())
                .setTimeline(userTimeline)
                .build();
        setListAdapter(adapter);
    }
    public void setUpUi(){
        btnpost = (ImageView) view.findViewById(R.id.button);
        edt1 = (EditText) view.findViewById(R.id.editText);
        btnpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt1.getText().length()>0) {
                    String a = edt1.getText().toString();
                    postTweet(a);
                    edt1.setText("");


                }
            }
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRowVerticalPosition = (getListView() == null || getListView().getChildCount() == 0) ? 0 : getListView().getChildAt(0).getTop();
                swipeRefreshLayout.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container,false);
        setUpUi();
        getwalltw();
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getwalltw();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        return view;
    }






    public void postTweet(String text){
        StatusesService statusesService = Twitter.getApiClient().getStatusesService();
        Call<Tweet> call = statusesService.update(text, null, false, null, null, null, false, false, null);
        call.enqueue(new Callback<Tweet>() {
            @Override
            public void success(Result<Tweet> result) {
                Toast.makeText(getContext(),"Запис успішно додано !",Toast.LENGTH_LONG).show();

                //Do something with result
            }

            public void failure(TwitterException exception) {
                Toast.makeText(getContext(),"Помилка !",Toast.LENGTH_LONG).show();
                //Do something on failure
            }
        });
    }


}










