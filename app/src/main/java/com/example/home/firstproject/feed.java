package com.example.home.firstproject;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.home.firstproject.fragment.Friends_fragment;
import com.example.home.firstproject.fragment.Photo_fragment;
import com.example.home.firstproject.fragment.TW_feed_fragment;
import com.example.home.firstproject.fragment.VK_feed_fragment;
import com.example.home.firstproject.fragment.VK_friends_fragment;
import com.example.home.firstproject.fragment.VK_groups_fragment;
import com.example.home.firstproject.fragment.VK_msg_fragment;
import com.example.home.firstproject.fragment.Wall_fragment;

public class feed extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener  {
    final int DIALOG_EXIT = 1;
    VK_friends_fragment frfragment;
        VK_feed_fragment fefragment;
        VK_msg_fragment msg_fragment;
        VK_groups_fragment groups_fragment;
        Wall_fragment wall_fragment;
        Friends_fragment friends_fragment;
        Photo_fragment photo_fragment;
        TW_feed_fragment tw_feed_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        frfragment = new VK_friends_fragment();
        fefragment = new VK_feed_fragment();
        msg_fragment = new VK_msg_fragment();
        groups_fragment = new VK_groups_fragment();
        wall_fragment = new Wall_fragment();
        friends_fragment = new Friends_fragment();
        photo_fragment = new Photo_fragment();
        tw_feed_fragment = new TW_feed_fragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
        fragmentTransaction2.replace(R.id.content_feed, tw_feed_fragment).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            showDialog(DIALOG_EXIT);
        }
    }







    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (id == R.id.wall) {
            fragmentTransaction.replace(R.id.content_feed, wall_fragment);

        } else if (id == R.id.nav_camera) {
        fragmentTransaction.replace(R.id.content_feed, friends_fragment);


            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            fragmentTransaction.replace(R.id.content_feed, msg_fragment);
        }
        else if (id == R.id.nav_photo) {
        fragmentTransaction.replace(R.id.content_feed, photo_fragment);

        } else if (id == R.id.nav_slideshow) {
            fragmentTransaction.replace(R.id.content_feed, tw_feed_fragment);

        } else if (id == R.id.nav_manage) {
            fragmentTransaction.replace(R.id.content_feed, groups_fragment);
        } else if (id == R.id.nav_send) {
            showDialog(DIALOG_EXIT);
        }   fragmentTransaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
