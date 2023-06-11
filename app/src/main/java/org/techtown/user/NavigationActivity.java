package org.techtown.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

public class NavigationActivity extends AppCompatActivity {
    private TextView selectedItemTextView;
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
        TextView textView = findViewById(R.id.destination);
        textView.setText(selectedDest);

        // 리셋 버튼 클릭 시 메인 화면으로 이동
        Button btnReset = findViewById(R.id.resetbtn);
        btnReset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(NavigationActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()){
                case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                    // 액티비티 이동
                    Intent listIntent = new Intent(NavigationActivity.this, MainActivity.class);
                    startActivity(listIntent);
                    finish();
                    return true;
                }
            }
            return super.onOptionsItemSelected(item);
        }
}
