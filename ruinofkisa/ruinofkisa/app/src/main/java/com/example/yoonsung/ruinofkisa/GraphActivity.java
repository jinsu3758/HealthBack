package com.example.yoonsung.ruinofkisa;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/*
자신의 보험목록을 출력해주어 허리관련 보험이 있는지 확인해야됨
로그인 창에서 입력한 아이디와 패스워드는 intent로 넘어옴
*/
public class GraphActivity extends BaseActivity{
    TextView btnPieChart,btnBarChart,btnDiscountChart;
    Button btnLineChart;
    Intent l_intent,p_intent,b_intent,d_intent,intent;
//    static Switch gpsSwitch;
    static int timer_sec,cnt,gpsMode;
    Thread t;
    private TextView timer_text;
    DBHelper dbManager;
//    boolean run;
    long now;
    String id,pw;
    String date;
    @Override
    protected void onCreate(Bundle savedInstanceState) { // count변수를 디비에서 꺼내와야됨
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        intent=getIntent();
        id=intent.getStringExtra("ID");
        pw = intent.getStringExtra("PW");
        date = intent.getStringExtra("DATE");
        Log.d("으악",id);
        dbManager = new DBHelper(getApplicationContext(), "User.db", null, 3);
        btnLineChart =  (Button)findViewById(R.id.btnLineChart);
        btnPieChart = (TextView)findViewById(R.id.btnPieChart);
        btnBarChart = (TextView)findViewById(R.id.btnBarChart);
        btnDiscountChart = (TextView)findViewById(R.id.btnDiscountChart);
//        IsNear clickAdapter = new IsNear(this);
//        btnLineChart.setOnClickListener(this);
        l_intent = new Intent(this,LineChartActivity.class);
        p_intent = new Intent(this,PieChartActivity.class);
        b_intent = new Intent(this,BarChartActivity.class);
        d_intent = new Intent(this,DiscountActivity.class);
        btnLineChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(l_intent);
            }
        });
        btnPieChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p_intent.putExtra("PW",pw);
                p_intent.putExtra("DATE",date);
                startActivity(p_intent);
            }
        });
        btnBarChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b_intent.putExtra("PW",pw);
                b_intent.putExtra("DATE",date);
                startActivity(b_intent);
            }
        });
        btnDiscountChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d_intent.putExtra("PW",pw);
                d_intent.putExtra("DATE",date);
                startActivity(d_intent);
            }
        });
//        gpsSwitch = (Switch)findViewById(R.id.gpsSwitch);
//        gpsSwitch.setOnCheckedChangeListener(this);
        timer_text = (TextView)findViewById(R.id.timer);
        /*if(gpsMode == 0) {
            Log.d("으악","cnt : "+cnt);
            gpsSwitch.setChecked(false);
        }
        else
            gpsSwitch.setChecked(true);*/
    }
    /*
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked)
        {
            Log.d("날짜",date);
            cnt = dbManager.getPW(pw,date);
            Log.d("날짜",""+cnt);
//            Log.d("날짜",date);
            gpsMode = 1;
//            count=0;
            Log.d("들어가","시작");
            t = new Thread() {
                @Override
                public void run() {
                    try {
                        while (!isInterrupted()) {
                            Thread.sleep(1000);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                                    updateYOURthing();
                                    cnt++;
                                    timer_text.setText("시간 : "+cnt);
                                }
                            });
                        }
                    } catch (InterruptedException e) {
                    }
                }
            };
            t.start();

        }
        else
        {
            Log.d("들어가","끝");
            gpsMode = 0;
            t.interrupt();
            dbManager.update("update USER_LIST set using_time = " + cnt + " where pw = '" + pw + "' AND create_at = '" + date + "';");
//            dbManager.update("update USER_LIST set using_time = " + cnt + " where id = '" + id + "';");
        }
    }
    */
}
