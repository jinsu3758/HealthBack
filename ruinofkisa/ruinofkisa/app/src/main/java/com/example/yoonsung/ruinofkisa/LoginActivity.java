package com.example.yoonsung.ruinofkisa;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

/*
자신의 보험목록을 출력해주어 허리관련 보험이 있는지 확인해야됨
로그인 창에서 입력한 아이디와 패스워드는 intent로 넘어옴
*/
public class LoginActivity extends BaseActivity {
    Intent intent;
    Intent g_intent;
    Intent a_intent;
    Intent t_intent,db_intent;
    String id,date,pw;
    private final long FINISH_INTERVAL_TIME = 2000;
    private long   backPressedTime = 0;
    private MainActivity Activity = (MainActivity)MainActivity.MActivity;
    private LinearLayout back_layout;
    private TextView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        long now = System.currentTimeMillis();
        Date d = new Date(now);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
        date = simpleDateFormat.format(d);
        intent = getIntent();
        id = intent.getStringExtra("ID");
        pw = intent.getStringExtra("PW");
        Log.d("윤성",id+","+pw);
        g_intent = new Intent(this,GraphActivity.class);
        a_intent = new Intent(this,AttitudeActivity.class);
        t_intent = new Intent(this,TmploginActivity.class);
        db_intent = new Intent(this,MaintestActivity.class);
        back = (TextView)findViewById(R.id.back_plz);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void button1Clicked(View v){ // 정자세로 있었던 시간 & 그래프로 할인율 보기 & 자신의 보험료 확인
        //인텐트로 시간,보험료 넘겨주기
        a_intent.putExtra("name", id);
        a_intent.putExtra("id_number", pw);
        a_intent.putExtra("DATE",date);
        startActivity(a_intent);
    }
    public void button2Clicked(View v){ // 정자세로 있었던 시간 & 그래프로 할인율 보기 & 자신의 보험료 확인
        //인텐트로 시간,보험료 넘겨주기
        g_intent.putExtra("ID",id);
        g_intent.putExtra("PW",pw);
        g_intent.putExtra("DATE",date);
        startActivity(g_intent);
    }
    public void button3Clicked(View v){
        t_intent.putExtra("PW",pw);
        t_intent.putExtra("DATE",date);
        startActivity(t_intent);
    }
    public void button4Clicked(View v){
        db_intent.putExtra("ID",id);
        db_intent.putExtra("PW",pw);
        startActivity(db_intent);
    }
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime)
        {
            Activity.finish();
            super.onBackPressed();
        }
        else
        {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }

    }

    public void button5Clicked(){
        Log.v("help", "helpme");
    }

    public void onDestroy() {

        super.onDestroy();
    }

}