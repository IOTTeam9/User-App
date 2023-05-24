package org.techtown.user;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity  {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String[] mid = { "AI관-407", "AI관-410","AI관-414", "AI관-416", "AI관-428",
                "AI관-507", "AI관-510","AI관-514", "AI관-516", "AI관-517","AI관-518","AI관-519", "AI관-528"};

        ListView list = (ListView) findViewById(R.id.listView1);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mid);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Intent intent = new Intent(MainActivity.this, NavigationActivity.class);
                startActivity(intent);
            }
        });


        // retrofit 설정
        String BASE_URL = "https://cha-conne.kro.kr";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LocationRetrofitInterface retrofitAPI = retrofit.create(LocationRetrofitInterface.class);

        // WiFi manager를 통해 현재 위치의 fingerprint 값을 보내는 코드 (지금은 임시로 더미 데이터를 사용합니다.)
        // 이 부분에 WiFi manager를 통해 현재 위치의 fingerprint 값을 구하여 Location 타입 변수에 저장하는 코드가 필요합니다.
        Location myLocation = new Location("414호", "SSID","mac_address", -60);

        // sendLocation API 호출
        retrofitAPI.sendLocation(myLocation).enqueue(new Callback<NavigationResponse>() {
            @Override
            public void onResponse(Call<NavigationResponse> call, Response<NavigationResponse> response) {
                if(response.isSuccessful()) {
                    NavigationResponse resp = response.body();
                    // resp에서 data 변수를 추출하여 네비게이션 기능에 사용하면 됩니다.
                    // resp의 data 변수에는 Navigation 타입의 변수인 direction과 distance가 존재합니다.

                    // 밑에 있는 direction과 distance를 네비게이션 기능에 이용하면 됩니다.
//                    Navigation respData = resp.getData();
//                    String direction = respData.getDirection();
//                    int distance = respData.getDistance();
//                    Log.d("SUCCESS", "direction : " + direction + " distance : " + distance);

                    Log.d("SUCCESS", resp.getMessage());
                }
            }

            @Override
            public void onFailure(Call<NavigationResponse> call, Throwable t) {
                Log.d("FAILURE", t.getMessage());
            }
        });


    }
}
