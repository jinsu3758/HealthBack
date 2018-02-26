package com.example.yoonsung.ruinofkisa;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by ccu03 on 2017-11-25.
 */

public class BaseActivity extends AppCompatActivity{
    private static Typeface myTypeface = null;

    @Override
    public void setContentView(int layoutResID){
        super.setContentView(layoutResID);
        if(myTypeface == null){
            myTypeface = Typeface.createFromAsset(this.getAssets(),"yapyap.ttf");
        }
        setGlobalFont(getWindow().getDecorView());
    }
    private void setGlobalFont(View view){
        if (view !=null){
            if(view instanceof ViewGroup){
                ViewGroup vg = (ViewGroup) view;
                int vgCnt = vg.getChildCount();
                for(int i =0;i<vgCnt;i++){
                    View v = vg.getChildAt(i);
                    if (v instanceof TextView) {
                        ((TextView) v).setTypeface(myTypeface);
                    }
                    setGlobalFont(v);
                }
            }
        }
    }
}