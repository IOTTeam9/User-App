package org.techtown.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private String selectedDest;
    ArrayList<String[]> arrayList = new ArrayList<>();
    ;
    List<Location> locationList = new ArrayList<>();

    WifiManager wifiManager;
    List<ScanResult> scanResultList;

    boolean isPermitted = false;
    boolean isWifiScan = false;
    boolean doneWifiScan = true;
    final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                getWifiInfo();
                transformDataList(arrayList);
                sendLocation(locationList);
            }
        }
    };

    private void getWifiInfo() {

        if(!doneWifiScan) {

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            scanResultList = wifiManager.getScanResults();

            for(int i = 0; i < scanResultList.size(); i++){
                ScanResult result = scanResultList.get(i);
                String[] dataset = new String[3];

                // dataset에 SSID, BSSID, RSSI, Place를 순서대로 저장
                dataset[0] = String.valueOf(result.SSID);
                dataset[1] = String.valueOf(result.BSSID);
                dataset[2] = String.valueOf(result.level);

                Log.d("WIFI NAME !! : ", dataset[0]);
                Log.d("WIFI MAC!! : ", dataset[1]);
                Log.d("RSSI LEVEL!! : ", dataset[2]);

                arrayList.add(i, dataset);
            }
        }
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String[] mid = {"AI관-407", "AI관-410", "AI관-414", "AI관-416", "AI관-428",
                "AI관-507", "AI관-510", "AI관-514", "AI관-516", "AI관-517", "AI관-518", "AI관-519", "AI관-528"};

        Button findBtn = (Button) findViewById(R.id.main_findBtn_btn);
        ListView list = (ListView) findViewById(R.id.listView1);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mid);
        list.setAdapter(adapter);

        // 세팅 검사
        requestRuntimePermission();

        // wifiManger 설정
        wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        if (wifiManager.isWifiEnabled() == false) {
            wifiManager.setWifiEnabled(true);
        }

        // List 클릭 시
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // 선택된 아이템의 값을 가져옴
                selectedDest = mid[arg2];

                // NavigationActivity로 이동하는 코드
                Intent intent = new Intent(MainActivity.this, NavigationActivity.class);
                intent.putExtra("selectedDest", selectedDest); // 목적지 보내기
                startActivity(intent);
            }
        });

        // Find 버튼 클릭 시
        findBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                doneWifiScan = false;

                if (isPermitted) {
                    wifiManager.startScan();
                    isWifiScan = true;

                }


            }
        });



    }

    public List<Location> transformDataList(ArrayList<String[]> arrayList) {

        for(int i = 0; i < arrayList.size(); i++) {
            locationList.add(new Location(arrayList.get(i)[0], arrayList.get(i)[1], Integer.parseInt(arrayList.get(i)[2])));
            Log.d("LOCATION", locationList.get(i).getBssid());
        }

        return locationList;
    }


    private void requestRuntimePermission() {
        //*******************************************************************
        // Runtime permission check
        //*******************************************************************
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        } else {
            // ACCESS_FINE_LOCATION 권한이 있는 것
            isPermitted = true;
        }
        //*********************************************************************
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // read_external_storage-related task you need to do.

                    // ACCESS_FINE_LOCATION 권한을 얻음
                    isPermitted = true;

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                    // 권한을 얻지 못 하였으므로 location 요청 작업을 수행할 수 없다
                    // 적절히 대처한다
                    isPermitted = false;

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }



    // user가 현재 위치에서 WIFI 정보를 보내는 함수 (place, ssid, bssid, rssi)
    private void sendLocation(List<Location> locationList) {

        // retrofit 설정
        String BASE_URL = "https://cha-conne.kro.kr";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LocationRetrofitInterface retrofitAPI = retrofit.create(LocationRetrofitInterface.class);
        Log.d("API_CALL", "API INSIDE");
        // sendLocation API 호출
        retrofitAPI.sendLocation(locationList).enqueue(new Callback<NavigationResponse>() {
            @Override
            public void onResponse(Call<NavigationResponse> call, Response<NavigationResponse> response) {
                if(response.isSuccessful()) {
                    Log.d("API_CALL", "API INSIDE2");
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

    @Override
    protected void onResume() {
        super.onResume();

        // wifi scan 결과 수신을 위한 BroadcastReceiver 등록
        IntentFilter filter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // wifi scan 결과 수신용 BroadcastReceiver 등록 해제
        unregisterReceiver(mReceiver);
    }
}