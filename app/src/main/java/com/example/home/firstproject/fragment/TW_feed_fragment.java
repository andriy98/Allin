package com.example.home.firstproject.fragment;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.example.home.firstproject.R;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;
import com.twitter.sdk.android.tweetui.FixedTweetTimeline;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import java.util.List;
import retrofit2.Call;

public class TW_feed_fragment extends ListFragment {
    private static final int LAYOUT = R.layout.tw_feed_fr;
    private View view;
    SwipeRefreshLayout swipeRefreshLayout;


    public static TW_feed_fragment getInstance(){
        Bundle args = new Bundle();
        TW_feed_fragment fragment = new TW_feed_fragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Новини");
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container,false);
        gethometimeline();
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                gethometimeline();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        return view;

    }



    public void gethometimeline() {
        StatusesService statusesService = Twitter.getApiClient().getStatusesService();

        Call<List<Tweet>> call = statusesService.homeTimeline(200, null, null, null, null, null, null);
        call.enqueue(
                new Callback<List<Tweet>>() {
                    @Override
                    public void success(Result<List<Tweet>> result) {
                        final FixedTweetTimeline timeline = new FixedTweetTimeline.Builder()
                                .setTweets(result.data)
                                .build();
                        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(getActivity())
                                .setTimeline(timeline)//this method cannot receive a List<Tweet> object like result.data
                                .build();
                        setListAdapter(adapter);
                    }
                    @Override
                    public void failure(TwitterException exception) {
                    }
                });
    }
}
