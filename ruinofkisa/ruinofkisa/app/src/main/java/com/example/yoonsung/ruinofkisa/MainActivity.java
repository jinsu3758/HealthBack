package com.example.yoonsung.ruinofkisa;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by 소꺼 on 2017-11-24.
 */

public class MainActivity extends BaseActivity {
    private EditText idt;
    private EditText pwt;
    private Button button;
    private String temp;
    private Intent intent;
    private Intent main_intent;
    private String ids;
    private String pws;
    private TextView home_text;
    DBHelper dbManager;
    public static MainActivity MActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MActivity = MainActivity.this;
        dbManager = new DBHelper(getApplicationContext(), "User.db", null, 3);
        idt = (EditText) findViewById(R.id.id_text);
        pwt = (EditText) findViewById(R.id.pw_text);
        button = (Button) findViewById(R.id.login_button);
//        home_text = (TextView) findViewById(R.id.first_text);
        intent = (Intent) new Intent(this, LoginActivity.class);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ids = idt.getText().toString();
                pws = pwt.getText().toString();
                temp = pws;
                if (ids.equals("") || pws.equals("")) {
                    Toast.makeText(getApplicationContext(), "주민등록번호와 이름을 입력해 주세요", Toast.LENGTH_SHORT).show();
                } else {
                    final Handler mHandler = new Handler();
                    new Thread(new Runnable() {
                        @Override
                        public void run() { // TODO Auto-generated method stub
                            temp = apiResult("https://openapi.kyobo.co.kr:1443/v1.0/INFO/user/husermapinfo", "1", "PRTY_REG_NO", temp, "p95AT1LAeQZ6tqCE5aTV7Q7bmSdzzCkR");

                            com.google.gson.JsonParser json_parser = new com.google.gson.JsonParser();
                            JsonObject json_object = (JsonObject) json_parser.parse(temp);
                            JsonObject data_object = (JsonObject) json_object.get("dataHeader");
                            temp = data_object.get("successCode").toString();
                            if(data_object.get("successCode").toString().equals("\"0\"")) {
                                long now = System.currentTimeMillis();
                                Date d = new Date(now);
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
                                String date = simpleDateFormat.format(d);
                                if(dbManager.getPW(pws,date)==-1) { // 새로만들어
                                    dbManager.insert(ids, pws, date, 0, 3, 0, 0, 0, 0, 0, 0);
                                }
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        intent.putExtra("ID", ids);
                                        intent.putExtra("PW", pws);
                                        Log.v("test", ids + " " + pws);
                                        startActivity(intent);
                                    }
                                });
                            }
                            else{
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "주민등록 번호를 확인하세요", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }).start();



                }
            }
        });
    }


    public String apiResult(String apiUrl, String cnt, String apiReuestId, String apiReuestValue, String kyoboApiKey) {
        String result = null;
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());


            JSONObject req = new JSONObject();
            for (int i = 0; i < Integer.valueOf(cnt); i++) {
                if (apiReuestValue.split("/")[i] != null) {
                    req.put(apiReuestId.split("/")[i], apiReuestValue.split("/")[i]);
                }
            }
            String reqS = "";

            /*
            if("/app/rest/getConsumer".equals(apiUrl)){
                //로그인API 일 경우
                reqS = req.toString();
                apiUrl = "http://10.33.171.174:8080"+apiUrl;
            }else{
                reqS = "{\"dataBody\":"+req.toString()+"}";
                apiUrl = "https://10.33.1.70:8443"+apiUrl;
            }
            */

            reqS = "{\"dataBody\":" + req.toString() + "}";

            System.out.println("request json data===> " + reqS);
            URL url = new URL(apiUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.addRequestProperty("Content-Type", "application/json;charset=UTF-8");
            con.addRequestProperty("kyoboApiKey", kyoboApiKey);
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(false);
            con.setDefaultUseCaches(false);
            con.connect();

            OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
            out.write(reqS);
            out.flush();
            out.close();
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            } else {
                br = new BufferedReader(new InputStreamReader(con.getErrorStream(), "UTF-8"));
            }
            String inputLine;
            StringBuffer sb = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine);
            }
            br.close();
            result = sb.toString();
            ObjectMapper mapper = new ObjectMapper();
            Object json = mapper.readValue(result, Object.class);
            result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
            System.out.println("response json data===> " + result);
        } catch (Exception e) {
            result = e.toString();
            Log.v("error", e.toString());
        }
        return result;
    }
}