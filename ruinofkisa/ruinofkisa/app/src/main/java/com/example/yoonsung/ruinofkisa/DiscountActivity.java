package com.example.yoonsung.ruinofkisa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by yoonsung on 2017. 11. 19..
 */
public class DiscountActivity extends BaseActivity {
    DBHelper dbManager;
    String pw,d;
    Intent intent;
    int check[] =new int[31];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount);
        intent=getIntent();
        pw = intent.getStringExtra("PW");
        d = intent.getStringExtra("DATE");
        dbManager = new DBHelper(getApplicationContext(), "User.db", null, 3);
        check=dbManager.getStamp(pw,d);
        ImageView v1 = (ImageView)findViewById(R.id.L1);
        ImageView v2 = (ImageView)findViewById(R.id.L2);
        ImageView v3 = (ImageView)findViewById(R.id.L3);
        ImageView v4 = (ImageView)findViewById(R.id.L4);
        ImageView v5 = (ImageView)findViewById(R.id.L5);
        ImageView v6 = (ImageView)findViewById(R.id.L6);
        ImageView v7 = (ImageView)findViewById(R.id.L7);
        ImageView v8 = (ImageView)findViewById(R.id.L8);
        ImageView v9 = (ImageView)findViewById(R.id.L9);
        ImageView v10 = (ImageView)findViewById(R.id.L10);
        ImageView v11 = (ImageView)findViewById(R.id.L11);
        ImageView v12 = (ImageView)findViewById(R.id.L12);
        ImageView v13 = (ImageView)findViewById(R.id.L13);
        ImageView v14 = (ImageView)findViewById(R.id.L14);
        ImageView v15 = (ImageView)findViewById(R.id.L15);
        ImageView v16 = (ImageView)findViewById(R.id.L16);
        ImageView v17 = (ImageView)findViewById(R.id.L17);
        ImageView v18 = (ImageView)findViewById(R.id.L18);
        ImageView v19 = (ImageView)findViewById(R.id.L19);
        ImageView v20 = (ImageView)findViewById(R.id.L20);
        ImageView v21 = (ImageView)findViewById(R.id.L21);
        ImageView v22 = (ImageView)findViewById(R.id.L22);
        ImageView v23 = (ImageView)findViewById(R.id.L23);
        ImageView v24 = (ImageView)findViewById(R.id.L24);
        ImageView v25 = (ImageView)findViewById(R.id.L25);
        ImageView v26 = (ImageView)findViewById(R.id.L26);
        ImageView v27 = (ImageView)findViewById(R.id.L27);
        ImageView v28 = (ImageView)findViewById(R.id.L28);
        ImageView v29 = (ImageView)findViewById(R.id.L29);
        ImageView v30 = (ImageView)findViewById(R.id.L30);

        if(check[1]!=1)
            v1.setVisibility(View.INVISIBLE);
        if(check[2]!=1)
            v2.setVisibility(View.INVISIBLE);
        if(check[3]!=1)
            v3.setVisibility(View.INVISIBLE);
        if(check[4]!=1)
            v4.setVisibility(View.INVISIBLE);
        if(check[5]!=1)
            v5.setVisibility(View.INVISIBLE);
        if(check[6]!=1)
            v6.setVisibility(View.INVISIBLE);
        if(check[7]!=1)
            v7.setVisibility(View.INVISIBLE);
        if(check[8]!=1)
            v8.setVisibility(View.INVISIBLE);
        if(check[9]!=1)
            v9.setVisibility(View.INVISIBLE);
        if(check[10]!=1)
            v10.setVisibility(View.INVISIBLE);
        if(check[11]!=1)
            v11.setVisibility(View.INVISIBLE);
        if(check[12]!=1)
            v12.setVisibility(View.INVISIBLE);
        if(check[13]!=1)
            v13.setVisibility(View.INVISIBLE);
        if(check[14]!=1)
            v14.setVisibility(View.INVISIBLE);
        if(check[15]!=1)
            v15.setVisibility(View.INVISIBLE);
        if(check[16]!=1)
            v16.setVisibility(View.INVISIBLE);
        if(check[17]!=1)
            v17.setVisibility(View.INVISIBLE);
        if(check[18]!=1)
            v18.setVisibility(View.INVISIBLE);
        if(check[19]!=1)
            v19.setVisibility(View.INVISIBLE);
        if(check[20]!=1)
            v20.setVisibility(View.INVISIBLE);
        if(check[21]!=1)
            v21.setVisibility(View.INVISIBLE);
        if(check[22]!=1)
            v22.setVisibility(View.INVISIBLE);
        if(check[23]!=1)
            v23.setVisibility(View.INVISIBLE);
        if(check[24]!=1)
            v24.setVisibility(View.INVISIBLE);
        if(check[25]!=1)
            v25.setVisibility(View.INVISIBLE);
        if(check[26]!=1)
            v26.setVisibility(View.INVISIBLE);
        if(check[27]!=1)
            v27.setVisibility(View.INVISIBLE);
        if(check[28]!=1)
            v28.setVisibility(View.INVISIBLE);
        if(check[29]!=1)
            v29.setVisibility(View.INVISIBLE);
        if(check[30]!=1)
            v30.setVisibility(View.INVISIBLE);














        //v1.setVisibility(View.INVISIBLE);
    }
}
