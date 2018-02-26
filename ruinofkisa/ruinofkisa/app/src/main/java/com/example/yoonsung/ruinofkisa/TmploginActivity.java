package com.example.yoonsung.ruinofkisa;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
public class TmploginActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener{
    Switch gpsSwitch;
    int gpsMode;
    private Vibrator vibe;
    private TextView output;
    int press1 = 0,press2=0,press3=0,press4=0;
    boolean my_default = true;
    boolean dialog = true;
    boolean working=false;
    boolean update = false;
    private Socket socket;
    private Handler mHandler;
    private String[] split;
    private BufferedReader socket_in;
    private PrintWriter socket_out;
    private String ip = "172.30.10.28";
    private int port = 4480;
    private String data;
    private Button button;
    private Button set;
    private Intent set_intent;
    private TextView sit_text;
    private Handler myHandler;
    private int press_list[]={0};
    String pw,date;
    int count = 0;
    Intent intent;
    int cnt,right_cnt;
    private ProgressDialog mProgressDialog;
    DBHelper dbManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tmplogin);
        dbManager = new DBHelper(getApplicationContext(), "User.db", null, 3);
        output = (TextView)findViewById(R.id.sensor_text);
        button = (Button)findViewById(R.id.sensor_button);
        sit_text =(TextView)findViewById(R.id.sit);
        myHandler = new Handler();
        intent = getIntent();
        pw = intent.getStringExtra("PW");
        date = intent.getStringExtra("DATE");
        cnt = dbManager.getPW(pw,date);
        right_cnt = dbManager.getRight(pw,date);
        press_list=dbManager.getDefault(pw,date);
        Log.d("아놔","using_time : "+cnt);
        Log.d("아놔","right_time : "+right_cnt);
        Log.d("아놔","p : "+press_list[0]+" "+press_list[1]+" "+press_list[2]+" "+press_list[3]);
        if(press_list[0]!=0 || press_list[1] !=0 || press_list[2] !=0 ||press_list[3]!=0) {
            dialog = false;
            count=5; // 이것만 바꾸면 임시방편으로 default값 다시 설정할 수있음
            my_default=false;
        }
        gpsSwitch = (Switch)findViewById(R.id.gpsSwitch);
        gpsSwitch.setOnCheckedChangeListener(this);
//        timer_text = (TextView)findViewById(R.id.timer);

        if(gpsMode == 0) {
            Log.d("으악","cnt : "+cnt);
            gpsSwitch.setChecked(false);
        }
        else
            gpsSwitch.setChecked(true);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("아놔","dialog : "+dialog);
                if(dialog == true)
                {
                    Log.d("mystyle", "디폴트지정중");
                    synchronized (this) {
                        dialog = false;
                    }
                    mProgressDialog = ProgressDialog.show(TmploginActivity.this,"", "10초동안 바른 자세로 앉아주세요.",true);
                    myHandler.postDelayed( new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            try
                            {
                                if (mProgressDialog!=null&&mProgressDialog.isShowing()){

                                    mProgressDialog.dismiss();
                                }
                            }
                            catch ( Exception e )
                            {
                                e.printStackTrace();
                            }
                        }
                    }, 10000);

                }
            }
        });
        mHandler = new Handler();
        press_list= new int[4];


        output.setText("시작");

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            socket.close();
            dbManager.update("update USER_LIST set using_time = " + cnt + " where pw = '" + pw + "' AND create_at = '" + date + "';");
            dbManager.update("update USER_LIST set right_time = " + right_cnt + " where pw = '" + pw + "' AND create_at = '" + date + "';");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void onStart() {
        super.onStart();
        myThread.start();
    }

    Thread myThread = new Thread(new Runnable() {
        @Override
        public void run() {
            String line;
            try {
                socket = new Socket();
                int port = 4480;
                socket.connect(new InetSocketAddress(ip, port), 10000);
                socket_in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                Log.d("tqtqtqtq","시간"+cnt);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                output.setText("인터넷 연결을 확인해 주세요");
                e.printStackTrace();
            }
            while (true) {
                try {
                    line = socket_in.readLine();
                    //socket_in.rea
                    data = line;
                    if (!data.equals("")) {
                        mHandler.post(Update);

                    }
                    Thread.sleep(1000);

                    Log.e("love", data);
                } catch (Exception e) {
                    ;
                }

            }
        }
    });
    private Runnable Update = new Runnable() {
        @Override
        public void run() {
            //txt.setText(data + "여기다!");
            Log.e("fuck to love", data + ".txt");
            if(working)
                cnt += 2;
            Log.d("허허","working : "+working);
            Log.d("허허","cnt :" + cnt);
            split = data.split(":");
            press1 = Integer.parseInt(split[0]);
            press2 = Integer.parseInt(split[1]);
            press3 = Integer.parseInt(split[2]);
            press4 = Integer.parseInt(split[3]);

            if(dialog == false && count < 5)
            {
                synchronized (this) {
                    press_list[0] += press1;
                    press_list[1] += press2;
                    press_list[2] += press3;
                    press_list[3] += press4;
                    if (count == 4) {
                        press_list[0] /= 5;
                        press_list[1] /= 5;
                        press_list[2] /= 5;
                        press_list[3] /= 5;
                        my_default = false;
                    }
                    count++;
                }
                dbManager.update("update USER_LIST set default1 = " + press_list[0] + " where pw = '" + pw + "' AND create_at = '" + date + "';");
                dbManager.update("update USER_LIST set default2 = " + press_list[1] + " where pw = '" + pw + "' AND create_at = '" + date + "';");
                dbManager.update("update USER_LIST set default3 = " + press_list[2] + " where pw = '" + pw + "' AND create_at = '" + date + "';");
                dbManager.update("update USER_LIST set default4 = " + press_list[3] + " where pw = '" + pw + "' AND create_at = '" + date + "';");

                Log.d("아놔","pjlkjlkj : "+press_list[0]+" "+press_list[1]+" "+press_list[2]+" "+press_list[3]);
            }
            else if(my_default == false)
            {
                Log.e("wow", String.valueOf(press_list[0]) + "/" + String.valueOf(press_list[1]) + "/" +
                        String.valueOf(press_list[2]) + "/" + String.valueOf(press_list[3]));

                synchronized (this) {
                    //위에가 적을 때(구부정한 자세)
                    if ((press1 < press_list[0] - 50 && press2 < press_list[1] - 50) &&
                            (press3 >= press_list[2] - 50  && press4 >= press_list[3] - 50
                            )) {
                        sit_text.setText("구부정한 자세");
//                        vibe.vibrate(1000);
                    }
                    //아래가 적을 때(기대고 있는 자세)
                    else if ((press1 > press_list[0] + 50 && press2 > press_list[1] + 50) &&
                            (press3 < press_list[2] - 50 && press4 < press_list[3] + 50 )) {
                        sit_text.setText("기대고 있는 자세");
//                        vibe.vibrate(1000);
                    }
                    //불균형 자세(양 옆이 다름)
                    else if ((press1 + 50 < press2 || press1 - 50 > press2) || (press2 + 50 < press1 || press2 - 50 > press1) ||
                            (press3 + 50 < press4 || press3 - 50 > press4) || (press4 + 50 < press3 || press4 - 50 > press3)) {
                        sit_text.setText("불균형 자세");
//                        vibe.vibrate(1000);
                    } else if ((press3 >= press_list[2] - 50 && press3 <= press_list[2] + 50 && press4 >= press_list[3] - 50 &&
                            press4 <= press_list[3] + 50) &&
                            (press1 >= press_list[0] - 50 && press1 <= press_list[0] + 50 && press2 >= press_list[1] - 50 &&
                                    press2 <= press_list[1] + 50)) {
                        sit_text.setText("바른 자세");
                        right_cnt+=2;
                    } else {
//                        vibe.vibrate(1000);
                        sit_text.setText("올바르지 못한 자세");
                    }
                }
            }
        }
    };

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked)
        {
            working=true;
            gpsMode = 1;
//            Intent intent = new Intent(this, IsNear.class);
            startService(intent);
        }
        else
        {
            working=false;
            gpsMode = 0;
//            dbManager.update("update USER_LIST set using_time = " + cnt + " where pw = '" + pw + "' AND create_at = '" + date + "';");
//            dbManager.update("update USER_LIST set right_time = " + right_cnt + " where pw = '" + pw + "' AND create_at = '" + date + "';");
//            dbManager.update("update USER_LIST set using_time = " + cnt + " where id = '" + id + "';");
//            Intent intent = new Intent(getApplicationContext(), IsNear.class);
            stopService(intent);
        }
    }
}