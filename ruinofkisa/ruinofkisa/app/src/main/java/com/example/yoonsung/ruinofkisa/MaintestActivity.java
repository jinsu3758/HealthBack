package com.example.yoonsung.ruinofkisa;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MaintestActivity extends BaseActivity {
    Intent intent;
//    출처: http://bitsoul.tistory.com/118 [Happy Programmer~]
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db);
        intent = getIntent();
        final String id = intent.getStringExtra("ID");
        final String pw = intent.getStringExtra("PW");
        final EditText etPrice = (EditText) findViewById(R.id.price);
        final TextView tvResult = (TextView) findViewById(R.id.result);
        final DBHelper dbManager = new DBHelper(getApplicationContext(), "User.db", null, 3);

        // DB에 데이터 삽입
        Button btn_insert = (Button) findViewById(R.id.insert);
        btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String item = etItem.getText().toString();
                long now = System.currentTimeMillis();
                Date d = new Date(now);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
                String date = simpleDateFormat.format(d);
                Log.d("날짜 : " ,date);
//                String date = etDate.getText().toString();
//                int price = Integer.parseInt(etPrice.getText().toString());
                dbManager.insert(id,pw,"2017.11.24",3020,2910,0,0,0,0,3300,0);
                dbManager.insert(id,pw,"2017.11.23",2901,2222,0,0,0,0,3300,0);
                dbManager.insert(id,pw,"2017.11.22",1029,783,0,0,0,0,0,0);
                dbManager.insert(id,pw,"2017.11.21",2019,502,0,0,0,0,0,0);
                dbManager.insert(id,pw,"2017.11.20",3300,1029,0,0,0,0,0,0);
                dbManager.insert(id,pw,"2017.11.25",938,710,0,0,0,0,0,0);
                dbManager.insert(id,pw,"2017.11.26",3900,2100,0,0,0,0,3300,0);

//                dbManager.insert("insert into USER_LIST values(null, '" + id + "','" + pw + "','" + date + "', " + price + ");");
//                dbHelper.update(item, price);
                dbManager.close();
                tvResult.setText(dbManager.PrintData());
            }
        });
        Button btnSelect = (Button) findViewById(R.id.select);
        btnSelect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                tvResult.setText( dbManager.PrintData() );
            }
        });
        Button btnUpdate = (Button) findViewById(R.id.update);
        btnUpdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // update 테이블명 where 조건 set 값;
//                String name = etName.getText().toString();
                String pri = etPrice.getText().toString();
                int price = Integer.parseInt(pri);
                dbManager.update("update USER_LIST set using_time = " + price + " where id = '" + id + "';");

                tvResult.setText( dbManager.PrintData() );
            }
        });
        // Delete
        Button btnDelete = (Button) findViewById(R.id.delete);
        btnDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // delete from 테이블명 where 조건;
//                String name = etName.getText().toString();
                dbManager.delete("delete from USER_LIST;");

                tvResult.setText( dbManager.PrintData() );
            }
        });


    }


}
