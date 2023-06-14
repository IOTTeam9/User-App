package org.techtown.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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
    List<Location> locationList = new ArrayList<>();

    WifiManager wifiManager;
    List<ScanResult> scanResultList;

    boolean isPermitted = false;
    boolean isWifiScan = false;
    boolean doneWifiScan = true;
    final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    TextView currentPosition;

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

        arrayList.clear();

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
                String[] dataset = new String[2];

                // dataset에 BSSID, RSSI, Place를 순서대로 저장
                dataset[0] = String.valueOf(result.BSSID);
                dataset[1] = String.valueOf(result.level);

                Log.d("WIFI MAC!! : ", dataset[0]);
                Log.d("WIFI RSSI!! : ", dataset[1]);

                arrayList.add(i, dataset);
            }
        }
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String[] mid = {"414호", "4층 엘레베이터1", "4층 중앙", "4층 아르테크네", "406호",
                "407호", "4층 엘레베이터2", "413호", "411호", "5층 엘레베이터1", "5층 중앙1", "5층 중앙2", "506호"
                , "508호", "510호", "5층 엘레베이터2", "513호", "5층 엘레베이터3"};

        Button findBtn = (Button) findViewById(R.id.main_findBtn_btn);
        ListView list = (ListView) findViewById(R.id.listView1);
        currentPosition = (TextView) findViewById(R.id.main_currentPosition_tv);

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
                ItemCoordinates itemCoordinates = new ItemCoordinates();
                // 선택된 아이템의 값을 가져옴
                selectedDest = mid[arg2];
                Point endPoint = itemCoordinates.getEndCoordinate(selectedDest); // 목적지 좌표 찾아오기
                //Point startPoint = itemCoordinates.getStartCoordinate(selectedDest); 출발지 좌표 찾아오기

                // NavigationActivity로 이동하는 코드
                Intent intent = new Intent(MainActivity.this, NavigationActivity.class);
                intent.putExtra("selectedDest", selectedDest);
                intent.putExtra("endX", endPoint.x);
                intent.putExtra("endY", endPoint.y); // 목적지 이름 및 좌표 전송
                intent.putExtra("destination", arg0.getAdapter().getItem(arg2).toString());  // 선택된 목적지 전송
                Log.d("DEST", arg0.getAdapter().getItem(arg2).toString());
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

        locationList.clear();

        for(int i = 0; i < arrayList.size(); i++) {
            locationList.add(new Location(arrayList.get(i)[0], Integer.parseInt(arrayList.get(i)[1])));
            Log.d("LOCATION", locationList.get(i).getBssid());
            Log.d("LOCATION", String.valueOf(locationList.get(i).getRssi()));
        }
        Log.d("LOCATIONLIST", locationList.toString());
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
        retrofitAPI.sendLocation(locationList).enqueue(new Callback<ReceiveResponse>() {
            @Override
            public void onResponse(Call<ReceiveResponse> call, Response<ReceiveResponse> response) {
                if(response.isSuccessful()) {
                    Log.d("API_CALL", "API INSIDE2");

                    ReceiveResponse resp = response.body();
                    currentPosition.setText(resp.getLocation());

                    Log.d("CURRENT_POSITION", resp.getLocation());
                }
            }

            @Override
            public void onFailure(Call<ReceiveResponse> call, Throwable t) {
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