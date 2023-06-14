package org.techtown.user;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NavigationActivity extends AppCompatActivity implements SensorEventListener {
    private List<AStarAlgorithm.Node> path = new ArrayList<>(); // 경로 정보
    private int numRows; // 미로 행 개수
    private int numCols; // 미로 열 개수
    private int[][] maze; // 미로 정보를 저장하는 2차원 배열

    // -----------------------------------------------------------------------

    ArrayList<String[]> arrayList = new ArrayList<>();
    List<Location> locationList = new ArrayList<>();

    WifiManager wifiManager;
    List<ScanResult> scanResultList;

    boolean isPermitted = false;
    boolean isWifiScan = false;
    boolean doneWifiScan = true;
    final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    String currentPosition;
    String currentDestination;
    Timer timer;


    //화살표 빙글빙글용
    private ImageView compassImage;
    private float currentDegree = 0f;
    private SensorManager sensorManager;


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

        if (!doneWifiScan) {

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

            for (int i = 0; i < scanResultList.size(); i++) {
                ScanResult result = scanResultList.get(i);
                String[] dataset = new String[2];

                // dataset에 BSSID, RSSI, Place를 순서대로 저장
                dataset[0] = String.valueOf(result.BSSID);
                dataset[1] = String.valueOf(result.level);

                Log.d("WIFI_NAVI MAC!! : ", dataset[0]);
                Log.d("WIFI_NAVI LEVEL!! : ", dataset[1]);

                arrayList.add(i, dataset);
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        //화살표 이미지, 센서
        compassImage = findViewById(R.id.compass_image);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // 툴바 생성
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);//기본 제목을 없애줍니다.
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김

        Intent intent = getIntent();
        String selectedDest = intent.getStringExtra("selectedDest");
        int endX = intent.getIntExtra("endX", 0);
        int endY = intent.getIntExtra("endY", 0);
        currentDestination = intent.getStringExtra("destination");
        currentPosition = intent.getStringExtra("currentPosition");

        TextView position = findViewById(R.id.main_currentPosition_tv);
        TextView destination = findViewById(R.id.destination);
        TextView pathLengthTextView = findViewById(R.id.pathLengthTextView);

        destination.setText(selectedDest);
        position.setText(currentPosition);

        maze = readMazeFromFile();
        AStarAlgorithm algorithm = new AStarAlgorithm(maze);
        path = algorithm.findShortestPath(106, 12, endY, endX); //임의로 지정
        if (path != null && !path.isEmpty()) {
            //algorithm.showPath(this, path); // path toast로 미리 출력
            drawPathOnCanvas(path, numRows, numCols);
            double pathLength = path.size() * 1.32; // 출발지-도착지 거리 구하기
            String formattedLength = String.format("%.2f", pathLength);  // 소수점 둘째 자리까지 표시
            pathLengthTextView.setText(formattedLength + "m");
        }

        // -----------------------------------------------------------------------


        // 세팅 검사
        requestRuntimePermission();

        // wifiManger 설정
        wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        if (wifiManager.isWifiEnabled() == false) {
            wifiManager.setWifiEnabled(true);
        }

        // 정해진 시간마다 실행
        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                doneWifiScan = false;

                if (isPermitted) {
                    Log.d("LISTSIZE", String.valueOf(arrayList.size()));
                    wifiManager.startScan();
                    isWifiScan = true;
                }
            }
        };
        timer.schedule(timerTask, 0, 3000);


    }

    @Override
    protected void onResume() {
        super.onResume();

        //센서
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION) == null) {
            // 기기가 방향 센서를 지원하지 않는 경우 처리할 로직 추가
        } else {
            sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                    SensorManager.SENSOR_DELAY_GAME);
        }


        // wifi scan 결과 수신을 위한 BroadcastReceiver 등록
        IntentFilter filter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //센서
        sensorManager.unregisterListener(this);
        // wifi scan 결과 수신용 BroadcastReceiver 등록 해제
        unregisterReceiver(mReceiver);
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
                if (response.isSuccessful()) {
                    Log.d("API_CALL", "API INSIDE2");

                    ReceiveResponse resp = response.body();
                    currentPosition = resp.getLocation();

                    // 도착했을 경우 도착 토스트 띄우고 MainActivity 화면으로 나가기.
                    if (currentPosition == currentDestination) {
                        Toast.makeText(getApplicationContext(), "Arrived at " + currentDestination, Toast.LENGTH_LONG).show();
                        timer.cancel();
                        finish();
                    }

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

    private void requestRuntimePermission() {
        //*******************************************************************
        // Runtime permission check
        //*******************************************************************
        if (ContextCompat.checkSelfPermission(NavigationActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(NavigationActivity.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(NavigationActivity.this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        } else {
            // ACCESS_FINE_LOCATION 권한이 있는 것
            isPermitted = true;
        }
        //*********************************************************************
    }

    public List<Location> transformDataList(ArrayList<String[]> arrayList) {

        locationList.clear();

        for (int i = 0; i < arrayList.size(); i++) {
            locationList.add(new Location(arrayList.get(i)[0], Integer.parseInt(arrayList.get(i)[1])));
            Log.d("LOCATION_NAVI", locationList.get(i).getBssid());
            Log.d("LOCATION_NAVI", String.valueOf(locationList.get(i).getRssi()));
        }

        return locationList;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: { //toolbar의 back키 눌렀을 때 동작
                // 액티비티 이동
                Intent listIntent = new Intent(NavigationActivity.this, MainActivity.class);
                startActivity(listIntent);
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void drawPathOnCanvas(List<AStarAlgorithm.Node> path, int numRows, int numCols) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(50);
        // 배경 이미지 로드
        Bitmap background = BitmapFactory.decodeResource(getResources(), R.drawable.map);

        // 캔버스 크기 설정
        int canvasWidth = background.getWidth();
        int canvasHeight = background.getHeight();

        // 빈 비트맵 생성
        Bitmap bitmap = Bitmap.createBitmap(canvasWidth, canvasHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        // 배경 이미지 그리기
        canvas.drawBitmap(background, 0, 0, null);
        // 경로 그리기
        int startX, startY, endX, endY;
        AStarAlgorithm.Node prevNode = null;
        for (int i = 1; i < path.size(); i++) {
            AStarAlgorithm.Node currentNode = path.get(i);
            AStarAlgorithm.Node previousNode = path.get(i - 1);

            startX = calculateXCoordinate(previousNode.col, canvasWidth, numCols);
            startY = calculateYCoordinate(previousNode.row, canvasHeight, numRows);
            endX = calculateXCoordinate(currentNode.col, canvasWidth, numCols);
            endY = calculateYCoordinate(currentNode.row, canvasHeight, numRows);
            canvas.drawLine(startX, startY, endX, endY, paint);
        }

        ImageView mapViewImageView = findViewById(R.id.mapView);
        mapViewImageView.setImageBitmap(bitmap);
    }

    // maze와 map의 크기를 맞춰서 좌표 설정해주는 부분
    //maze : 73 150
    //map : 1864 3780
    //96.6m -> maze의 1픽셀마다 1.32m
    private int calculateXCoordinate(int col, int canvasWidth, int numCols) {
        return (int) (col * 25.5);
    }

    private int calculateYCoordinate(int row, int canvasHeight, int numRows) {
        return (int) (row * 25.2);
    }


    private int[][] readMazeFromFile() {
        List<int[]> rows = new ArrayList<>();

        try {
            InputStream inputStream = getResources().openRawResource(R.raw.maze);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(" ");
                int[] row = new int[tokens.length];
                for (int i = 0; i < tokens.length; i++) {
                    row[i] = Integer.parseInt(tokens[i]);
                }
                rows.add(row);
            }
            reader.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int numRows = rows.size();
        int numCols = numRows > 0 ? rows.get(0).length : 0;
        int[][] maze = new int[numRows][numCols];
        for (int i = 0; i < numRows; i++) {
            maze[i] = rows.get(i);
        }

        return maze;
    }

    ItemCoordinates itemCoordinates = new ItemCoordinates();

    //센서값 표출 및 빙글빙글
    @Override
    public void onSensorChanged(SensorEvent event) {
        float degree = Math.round(event.values[0]);


        Point endPoint = itemCoordinates.getEndCoordinate(currentDestination);
        Point startPoint = itemCoordinates.getStartCoordinate("414호");//TODO: currentPosition 정상적으로 받아와지면 (currentPosition);

        double weight;

        if (endPoint.x == startPoint.x) {
            if (endPoint.y > startPoint.y) {
                weight = Math.PI / 2.0;  // 90도
            } else {
                weight = -Math.PI / 2.0;  // -90도
            }
        } else {
            weight = Math.atan((double) (endPoint.y - startPoint.y) / (double) (endPoint.x - startPoint.x));
        }

        weight = Math.toDegrees(weight) + 32;  // 라디안 단위를 각도로 변환, 위쪽을 북쪽으로 맞춰주는 32º 추가

        Log.i("weight", String.valueOf(weight));
        degree -= weight;

        RotateAnimation rotateAnimation = new RotateAnimation(
                currentDegree,
                -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        rotateAnimation.setDuration(200);
        rotateAnimation.setFillAfter(true);

        compassImage.startAnimation(rotateAnimation);
        currentDegree = -degree;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // 정확도 변경 시 처리할 로직 추가
    }
}





