package com.example.yoonsung.ruinofkisa;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

public class PieChartActivity extends BaseActivity {
    Intent intent;
    DBHelper dbManager;
    int[] pieChartValues = {10, 10};  //각 계열(Series)의 값
//    public static final String TYPE = "type";
    //각 계열(Series)의 색상

    private static int[] COLORS = new int[]{Color.rgb(252, 238, 88), Color.rgb(252, 88, 88)};
    //각 계열의 타이틀
    String[] mSeriesTitle = new String[] {"RIGHT_PIE", "WRONG_PIE"};
    private CategorySeries mSeries = new CategorySeries("계열");
    private DefaultRenderer mRenderer = new DefaultRenderer();
    private GraphicalView mChartView;
    String date,pw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);
        Log.d("색깔",""+COLORS[0]);
        Log.d("색깔",""+COLORS[1]);
        intent=getIntent();
        dbManager = new DBHelper(getApplicationContext(), "User.db", null, 3);
        pw = intent.getStringExtra("PW");
        date = intent.getStringExtra("DATE");
//        pieChartValues[0] = ;
        pieChartValues = dbManager.todayTime(pw,date);
        int tmp = pieChartValues[0];
        pieChartValues[0] = pieChartValues[1]; // right
        pieChartValues[1] = tmp - pieChartValues[0]; // wrong
        mRenderer.setApplyBackgroundColor(true);

        mRenderer.setBackgroundColor(Color.rgb(63, 63, 63));
//        mRenderer.setBackgroundColor(Color.pack());

//        mRenderer.setChartTitleTextSize(20);
        mRenderer.setLabelsTextSize(50);
        mRenderer.setLegendTextSize(50);

        mRenderer.setMargins(new int[]{20, 30, 15, 0});

//        mRenderer.setZoomButtonsVisible(true);

//        mRenderer.setStartAngle(90);


        if (mChartView == null) {

            LinearLayout layout = (LinearLayout) findViewById(R.id.chart_pie);

            mChartView = ChartFactory.getPieChartView(this, mSeries, mRenderer);

//            mRenderer.setClickEnabled(true);

//            mRenderer.setSelectableBuffer(10);

            layout.addView(mChartView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,

                    LinearLayout.LayoutParams.FILL_PARENT));

        } else {

            mChartView.repaint();

        }
        fillPieChart();

    }


    public void fillPieChart() {

        for (int i = 0; i < pieChartValues.length; i++) {

            mSeries.add(mSeriesTitle[i] + "_" + (String.valueOf(pieChartValues[i])), pieChartValues[i]);
//            mSeries.
            //Chart에서 사용할 값, 색깔, 텍스트등을 DefaultRenderer객체에 설정
            SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
            renderer.setColor(COLORS[(mSeries.getItemCount() - 1) % COLORS.length]);
//            renderer.setColor();
            mRenderer.addSeriesRenderer(renderer);
            if (mChartView != null)
                mChartView.repaint();
            mChartView.setBackgroundColor(Color.WHITE);
        }
    }
}