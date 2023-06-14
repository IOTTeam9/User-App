package org.techtown.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.w3c.dom.Node;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class NavigationActivity extends AppCompatActivity {
    private List<AStarAlgorithm.Node> path = new ArrayList<>(); // 경로 정보
    private int numRows; // 미로 행 개수
    private int numCols; // 미로 열 개수
    private int[][] maze; // 미로 정보를 저장하는 2차원 배열

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

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
        TextView textView = findViewById(R.id.destination);
        textView.setText(selectedDest);

        maze = readMazeFromFile();
        AStarAlgorithm algorithm = new AStarAlgorithm(maze);
        path = algorithm.findShortestPath(106, 12, endY, endX); //임의로 지정

        if (path != null && !path.isEmpty()) {
            algorithm.showPath(this, path); // path toast로 미리 출력
            drawPathOnCanvas(path, numRows, numCols);
        }
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
        paint.setStrokeWidth(20);
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
}





