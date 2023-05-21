package org.techtown.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

public class NavigationActivity extends AppCompatActivity {
    private final int fragment_straight = 1;
    private final int fragment_left = 2;
    private final int fragment_right = 3;
    private final int fragment_uturn = 4;
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

        // 리셋 버튼 클릭 시 메인 화면으로 이동
        Button btnReset = findViewById(R.id.resetbtn);
        btnReset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(NavigationActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.straightbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentView(fragment_straight);
            }
        });

        findViewById(R.id.leftbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentView(fragment_left);
            }
        });

        findViewById(R.id.rightbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentView(fragment_right);
            }
        });

        findViewById(R.id.uturnbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentView(fragment_uturn);
            }
        });
        FragmentView(fragment_straight);
}
    private void FragmentView(int fragment){

        //FragmentTransactiom를 이용해 프래그먼트를 사용합니다.
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (fragment){
            case 1:
                // 첫번 째 프래그먼트 호출
                FragmentStraight fragment_straight = new FragmentStraight();
                transaction.replace(R.id.fragment_container, fragment_straight);
                transaction.commit();
                break;

            case 2:
                // 두번 째 프래그먼트 호출
                FragmentLeft fragment_left = new FragmentLeft();
                transaction.replace(R.id.fragment_container, fragment_left);
                transaction.commit();
                break;

            case 3:
                // 세번 째 프래그먼트 호출
                FragmentRight fragment_right = new FragmentRight();
                transaction.replace(R.id.fragment_container, fragment_right);
                transaction.commit();
                break;

            case 4:
                // 네번 째 프래그먼트 호출
                FragmentUturn fragment_uturn = new FragmentUturn();
                transaction.replace(R.id.fragment_container, fragment_uturn);
                transaction.commit();
                break;
        }

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
