package com.example.yoonsung.ruinofkisa;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.LinearLayout;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

/**
 * Created by yoonsung on 2017. 11. 19..
 */
public class BarChartActivity extends BaseActivity {
    private GraphicalView mChartView;
    Intent intent;
    int maximum;
    String [] past_date = new String[7];
    int [] past_time = new int[14];

    String[] mMonth = new String[]{
            "Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul"
    };
    String pw,date;
    DBHelper dbManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart);
        intent = getIntent();
        pw = intent.getStringExtra("PW");
        date = intent.getStringExtra("DATE");
        dbManager = new DBHelper(getApplicationContext(), "User.db", null, 3);
        past_date = dbManager.pastDate(date);
        past_time = dbManager.pastTime(pw,past_date);
        for(int i=0;i<7;i++){
            mMonth[i] = past_date[6-i].substring(5);
        }
        drawChart();
    }
    public void drawChart() {
        int[] x = {0, 1, 2, 3, 4, 5, 6};
        int[] income = {2000, 2500, 2700, 3000, 2800, 3500, 3700};
        int[] expense = {2200, 2700, 2900, 2800, 2600, 3000, 3300};
        for(int i=0;i<7;i++){
            income[i] = past_time[(6-i)*2];
            expense[i] = past_time[(6-i)*2+1];
            if(maximum < income[i])
                maximum = income[i];
            if(maximum < expense[i])
                maximum = expense[i];
        }
        XYSeries incomeSeries = new XYSeries("정자세");
        XYSeries expenseSeries = new XYSeries("잘못된자세");
        for (int i = 0; i < x.length; i++) {
            incomeSeries.add(i, past_time[(6-i)*2+1]);
            expenseSeries.add(i, past_time[(6-i)*2]-past_time[(6-i)*2+1]);
        }
        // Creating a dataset to hold each series
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        // Adding Income Series to the dataset
        dataset.addSeries(incomeSeries);
        // Adding Expense Series to dataset
        dataset.addSeries(expenseSeries);
        // Creating XYSeriesRenderer to customize incomeSeries
        XYSeriesRenderer incomeRenderer = new XYSeriesRenderer();
        incomeRenderer.setColor(Color.rgb(252, 238, 88)); //color of the graph set to cyan
        incomeRenderer.setFillPoints(true);
        incomeRenderer.setLineWidth(20);
        incomeRenderer.setFillBelowLine(true);
        XYSeriesRenderer expenseRenderer = new XYSeriesRenderer();
        expenseRenderer.setColor(Color.rgb(252, 88, 88));
        expenseRenderer.setFillPoints(true);
        expenseRenderer.setLineWidth(20);
        expenseRenderer.setFillBelowLine(true);
        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
        multiRenderer.setOrientation(XYMultipleSeriesRenderer.Orientation.HORIZONTAL);
        multiRenderer.setXLabels(0);
        multiRenderer.setChartTitle("정자세 vs 잘못된자세 Chart");
        multiRenderer.setXTitle("Year 2017");
        multiRenderer.setYTitle("Time");
        multiRenderer.setChartTitleTextSize(40);
        multiRenderer.setAxisTitleTextSize(30);
        //setting text size of the graph lable
        multiRenderer.setLabelsTextSize(30);
        multiRenderer.setPanEnabled(false, false);
        multiRenderer.setZoomEnabled(false, false);
        multiRenderer.setShowGrid(false);
        multiRenderer.setAntialiasing(true);
        multiRenderer.setLegendHeight(60); // 밑에 파란거 크기 늘리는거 Year2017있는 부분
        //setting x axis label align
        multiRenderer.setXLabelsAlign(Paint.Align.CENTER);
        //setting y axis label to align
        multiRenderer.setYLabelsAlign(Paint.Align.LEFT);
        //setting text style
        multiRenderer.setTextTypeface("sans_serif", Typeface.NORMAL);

        //setting no of values to display in y axis

        multiRenderer.setYLabels(10); // y크기 표시해주는거

        // setting y axis max value, Since i'm using static values inside the graph so i'm setting y max value to 4000.

        // if you use dynamic values then get the max y value and set here

        multiRenderer.setYAxisMax(maximum); // 최대값 표시

        //setting used to move the graph on xaxiz to .5 to the right

        multiRenderer.setXAxisMin(-1.5); // 그래프 옆으로 미는거~

        //setting max values to be display in x axis

        multiRenderer.setXAxisMax(7);

        //setting bar size or space between two bars

        multiRenderer.setBarSpacing(0.5);

        //Setting background color of the graph to transparent

        multiRenderer.setBackgroundColor(Color.rgb(59,100,107));

        //Setting margin color of the graph to transparent

        multiRenderer.setMarginsColor(Color.rgb(63, 63, 63));

        multiRenderer.setApplyBackgroundColor(true);
        //setting the margin size for the graph in the order top, left, bottom, right

        multiRenderer.setMargins(new int[]{50, 40, 30, 30}); // 블루쪽 크기 늘리기

        for (int i = 0; i < x.length; i++) {

//            multiRenderer.addXTextLabel(i, mMonth[i]);
            multiRenderer.addTextLabel(i,mMonth[i]);
        }

        // Adding incomeRenderer and expenseRenderer to multipleRenderer

        // Note: The order of adding dataseries to dataset and renderers to multipleRenderer

        // should be same

        multiRenderer.addSeriesRenderer(incomeRenderer);

        multiRenderer.addSeriesRenderer(expenseRenderer);

        //this part is used to display graph on the xml

        LinearLayout layout = (LinearLayout) findViewById(R.id.chart_bar);

        //remove any views before u paint the chart

        layout.removeAllViews();

        //drawing bar chart

        mChartView = ChartFactory.getBarChartView(this, dataset, multiRenderer, BarChart.Type.DEFAULT);
        layout.addView(mChartView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,

                LinearLayout.LayoutParams.FILL_PARENT));

    }

}