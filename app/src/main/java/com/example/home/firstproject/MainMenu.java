package com.example.home.firstproject;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.home.firstproject.fragment.FB_auth;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.vk.sdk.VKSdk;

import io.fabric.sdk.android.Fabric;

public class MainMenu extends AppCompatActivity {
    final int DIALOG_EXIT = 1;
    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "b30GDIp4yzWkAwny4J8LWaZX2";
    private static final String TWITTER_SECRET = "oBUsWxtG8STpKUdoonMm95PiCw2lGXle0cu0UugjpJ3mFzRSdj";
    //public static VKList list;
    private ImageView img_exit, img_enter, img_vk_act, img_vk_unact,twuact;
    TwitterLoginButton twitterLoginButton;
    TwitterSession twitterSession;

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        ConnectivityManager myConnMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = myConnMgr.getActiveNetworkInfo();
        if (networkinfo != null && networkinfo.isConnected()) {
                TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY,TWITTER_SECRET);
                Fabric.with(this, new Twitter(authConfig));
                FB_auth fr = new FB_auth();
                android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.activity_main_menu, fr);
                fragmentTransaction.commit();
                setContentView(R.layout.activity_main_menu);
                twuact = (ImageView) findViewById(R.id.imageView2);
                twuact.setVisibility(View.INVISIBLE);
                twitterLoginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_btn);
                twitterSession = Twitter.getSessionManager().getActiveSession();

                if (twitterSession == null){
                    twitterLoginButton.setVisibility(View.VISIBLE);
                    twuact.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(),"Twitter",Toast.LENGTH_LONG);


                }
                else {
                    twitterLoginButton.setVisibility(View.INVISIBLE);
                    twuact.setVisibility(View.VISIBLE);
                }
                twuact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Twitter.logOut();
                        Toast.makeText(getApplicationContext(), "Успішно", Toast.LENGTH_LONG).show();
                        twitterLoginButton.setVisibility(View.VISIBLE);


                    }
                });

                twitterLoginButton.setCallback(new Callback<TwitterSession>() {
                    @Override
                    public void success(Result<TwitterSession> result) {
                        String username = result.data.getUserName();
                        Toast.makeText(getApplicationContext(), "Успішно", Toast.LENGTH_LONG).show();
                        twuact.setVisibility(View.VISIBLE);
                        twitterLoginButton.setVisibility(View.INVISIBLE);

                    }

                    @Override
                    public void failure(TwitterException exception) {
                        Toast.makeText(getApplicationContext(), "Помилка", Toast.LENGTH_LONG).show();
                    }
                });
                img_enter = (ImageView) findViewById((R.id.img_go));
                img_exit = (ImageView) findViewById(R.id.img_exit);
                img_enter.setClickable(false);
                img_vk_unact = (ImageView) findViewById(R.id.img_vk_unact);
                img_vk_unact.setVisibility(View.INVISIBLE);
                img_enter.setVisibility(View.INVISIBLE);


                img_vk_act = (ImageView) findViewById(R.id.img_vk_act);
                if (VKSdk.isLoggedIn()==true){
                    img_enter.setVisibility(View.VISIBLE);
                    img_vk_unact.setVisibility(View.VISIBLE);
                    img_vk_act.setVisibility(View.INVISIBLE);

                }
                else {
                    img_vk_act.setVisibility(View.VISIBLE);
                    img_vk_unact.setVisibility(View.INVISIBLE);
                }

                img_vk_act.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainMenu.this, vk_auth.class);
                        startActivity(intent);
                        finish();


                    }
                });
                img_vk_unact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(),"Ви вже авторизувались в ВК", Toast.LENGTH_SHORT).show();
                        VKSdk.logout();
                    }
                });




                if (VKSdk.isLoggedIn()==false){
                    img_enter.setClickable(true);

                }



                img_enter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainMenu.this, feed.class);
                        startActivity(intent);
                        finish();
                    }
                });
                img_exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialog(DIALOG_EXIT);
                    }
                });
            } else {
                Toast.makeText(this, "Немає підключення до мережі !", Toast.LENGTH_SHORT).show();
            }

    }
    //Кнопка вихід



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        twitterLoginButton.onActivityResult(requestCode,resultCode,data);
    }

    public Dialog onCreateDialog(int id) {
        if (id == DIALOG_EXIT) {
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setTitle("Вихід");
            adb.setMessage("Ви впевненні, що хочете вийти ?");
            adb.setIcon(android.R.drawable.ic_dialog_alert);
            adb.setNegativeButton("Вийти", mycl);
            adb.setPositiveButton("Відміна", mycl);
            return adb.create();
        }
        return super.onCreateDialog(id);
    }

    @Override
    public void onBackPressed() {
        showDialog(DIALOG_EXIT);

    }

    DialogInterface.OnClickListener mycl = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case Dialog.BUTTON_POSITIVE:
                    break;
                case Dialog.BUTTON_NEGATIVE:
                    System.exit(0);
                    break;
            }
        }

    };
}

