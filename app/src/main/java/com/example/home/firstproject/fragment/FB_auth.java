package com.example.home.firstproject.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.home.firstproject.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class FB_auth extends Fragment {
    private Button button;
    private Button butreq;
    private TextView mDetails;
    private static final int LAYOUT = R.layout.fb_auth;
    private View view;
    private CallbackManager mCallbackManager;
    public FacebookCallback<LoginResult> mCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
            Toast.makeText(getContext(), "Успішно !", Toast.LENGTH_LONG).show();
            if (profile !=null){
                mDetails.setText("Welcome " + profile.getName());
            }
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException error) {
        Toast.makeText(getContext(), "Помилка", Toast.LENGTH_LONG).show();
        }
    };



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();

    }
    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container,false);
        mDetails = (TextView) view.findViewById(R.id.text_details);
        button = (Button) view.findViewById(R.id.button3);
        butreq = (Button) view.findViewById(R.id.button4);







        butreq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccessToken token = AccessToken.getCurrentAccessToken();
                GraphRequest request = GraphRequest.newGraphPathRequest(
                        token,
                        "/me/taggable_friends",
                        new GraphRequest.Callback() {
                            @Override
                            public void onCompleted(GraphResponse response) {
                                // Insert your code here
                            }
                        });

                request.executeAsync();


            }
        });







        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoggedIn()==true){
                    Toast.makeText(getContext(),"YES",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getContext(),"NO",Toast.LENGTH_LONG).show();
                }

            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LoginButton loginButton = (LoginButton) view.findViewById(R.id.login_button);
        loginButton.setReadPermissions("email","user_posts","read_page_mailboxes","user_photos");
        //loginButton.setPublishPermissions("publish_actions");
        loginButton.setFragment(this);
        loginButton.registerCallback(mCallbackManager,mCallback);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode,data);
    }

}


