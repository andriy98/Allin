package com.example.home.firstproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKList;

public class vk_auth extends AppCompatActivity {
    public static VKList list;
    private String[] scope  = new String[]{VKScope.MESSAGES, VKScope.FRIENDS, VKScope.WALL, VKScope.GROUPS, VKScope.PHOTOS};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vk_auth);
        VKSdk.login(this,scope);


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
// Пользователь успешно авторизовался
                Toast.makeText(getApplicationContext(),"Успішно",Toast.LENGTH_LONG).show();
                Intent i = new Intent(vk_auth.this,MainMenu.class);
                startActivity(i);
                finish();
                VKRequest vkRequest = VKApi.friends().get(VKParameters.from(VKApiConst.FIELDS, "first_name, photo_50","order","hints"));
                VKRequest vkRequest1 = VKApi.users().get();
                vkRequest1.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        super.onComplete(response);
                    }
                });






            }
            @Override
            public void onError(VKError error) {
// Произошла ошибка авторизации (например, пользователь запретил авторизацию)
                Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();

            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
