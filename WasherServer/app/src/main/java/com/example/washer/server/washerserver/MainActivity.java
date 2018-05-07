package com.example.washer.server.washerserver;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.OnChartGestureListener;
import com.github.mikephil.charting.interfaces.OnChartValueSelectedListener;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends ActionBarActivity {
    frag_One fo = new frag_One();
    frag_Two ft = new frag_Two();

    private static final int WASHER_ON = 0;
    private static final int WASHER_OFF = 1;

    static String token;
    static String serial;

    static boolean runFlag = true;
    static int runCount = 0;
    static int inputCount = 0;
    static float[] vibrateStack = new float[10];
    static float vibrateSum = 0f;

    static Context context;
    static Activity activity;
    static String respond;
    Intent intent;
    @Override
    public Context getApplicationContext() {
        context = super.getApplicationContext();
        return super.getApplicationContext();
    }

    private static LineChart mChart;
    private static TextView tvX, tvY;

    private static SensorManager sensorManager;
    private static Sensor accelerormeterSensor;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    public void changeStatus(){
        runFlag = false;
        runCount = 0;
        String url = "http://218.54.46.89:3000/washer/putstatus";
        HttpClient http = new DefaultHttpClient();
        try {

            ArrayList<NameValuePair> nameValuePairs =
                    new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("token", token));
            nameValuePairs.add(new BasicNameValuePair("washeridx", serial));
            nameValuePairs.add(new BasicNameValuePair("status", "0"));

            HttpParams params = http.getParams();
            HttpConnectionParams.setConnectionTimeout(params, 5000);
            HttpConnectionParams.setSoTimeout(params, 5000);

            HttpPost httpPost = new HttpPost(url);
            UrlEncodedFormEntity entityRequest =
                    new UrlEncodedFormEntity(nameValuePairs, "EUC-KR");

            httpPost.setEntity(entityRequest);

            HttpResponse responsePost = http.execute(httpPost);
            HttpEntity resEntity = responsePost.getEntity();
            respond = EntityUtils.toString(resEntity);
            JSONObject obj = new JSONObject(respond);
            Toast.makeText(context, obj.getString("msg"), Toast.LENGTH_LONG).show();
        }catch(Exception e){e.printStackTrace();}
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getApplicationContext();
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        activity = this;

        changeStatus();

        intent = getIntent();
        token = intent.getExtras().getString("token");
        serial = intent.getExtras().getString("idx");

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return fo;
                case 1:
                    return ft;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
            }
            return null;
        }
    }

    public static class frag_One extends Fragment implements SensorEventListener {

        private long lastTime;
        private float speed = 0f;
        private float lastX = 0f;
        private float lastY = 0f;
        private float lastZ = 0f;
        private float x, y, z;

        private static final int SHAKE_THRESHOLD = 1000;
        private static final int DATA_X = SensorManager.DATA_X;
        private static final int DATA_Y = SensorManager.DATA_Y;
        private static final int DATA_Z = SensorManager.DATA_Z;

        int count = 0;
        LineDataSet set1;
        ArrayList<LineDataSet> dataSets;
        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<Entry> yVals = new ArrayList<Entry>();
        LineData data;

        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main_1, container, false);
            sensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
            accelerormeterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            speed = 0f;
            lastX = 0f;
            lastY = 0f;
            lastZ = 0f;
            x = y = z = 0f;

            mChart = (LineChart) rootView.findViewById(R.id.chart1);
            mChart.setOnChartGestureListener(new OnChartGestureListener() {
                @Override
                public void onChartLongPressed(MotionEvent motionEvent) {

                }

                @Override
                public void onChartDoubleTapped(MotionEvent motionEvent) {

                }

                @Override
                public void onChartSingleTapped(MotionEvent motionEvent) {

                }

                @Override
                public void onChartFling(MotionEvent motionEvent, MotionEvent motionEvent2, float v, float v2) {

                }
            });
            mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry entry, int i) {

                }

                @Override
                public void onNothingSelected() {

                }
            });

            mChart.setStartAtZero(false);
            mChart.setDrawYValues(false);
            mChart.setDrawBorder(true);
            mChart.setBorderPositions(new BarLineChartBase.BorderPosition[]{
                    BarLineChartBase.BorderPosition.BOTTOM
            });
            mChart.setDescription("");
            mChart.setNoDataTextDescription("You need to provide data for the chart.");

            mChart.setHighlightEnabled(true);
            mChart.setTouchEnabled(true);
            mChart.setDragEnabled(true);
            mChart.setScaleEnabled(true);
            mChart.setPinchZoom(true);
            mChart.setHighlightEnabled(false);

            for (int i = 0; i < 60; i++) {
                xVals.add((i) + "");
            }

            set1 = new LineDataSet(yVals, "DataSet 1");
            set1.enableDashedLine(15f, 15f, 0f);
            set1.setColor(Color.BLACK);
            set1.setCircleColor(Color.BLACK);
            set1.setLineWidth(1f);
            set1.setCircleSize(4f);
            set1.setFillAlpha(65);
            set1.setFillColor(Color.BLACK);

            dataSets = new ArrayList<LineDataSet>();
            dataSets.add(set1); // add the datasets

            // create a data object with the datasets
            data = new LineData(xVals, dataSets);
            // set data
            mChart.setData(data);
            return rootView;
        }

        public void onStart() {
            super.onStart();
            if (accelerormeterSensor != null)
                sensorManager.registerListener(this, accelerormeterSensor,
                        SensorManager.SENSOR_DELAY_GAME);
        }

        public void onStop() {
            super.onStop();
        }

        public void onDestroy(){
            super.onDestroy();
            if (sensorManager != null)
                sensorManager.unregisterListener(this);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                long currentTime = System.currentTimeMillis();
                long gabOfTime = (currentTime - lastTime);
                vibrateSum = 0;
                if (gabOfTime > 1000) {
                    lastTime = currentTime;
                    vibrateSum = 0f;
                    x = event.values[SensorManager.DATA_X];
                    y = event.values[SensorManager.DATA_Y];
                    z = event.values[SensorManager.DATA_Z];

                    speed = Math.abs(x + y + z - lastX - lastY - lastZ) / gabOfTime * 100000;
                    if(speed < 1000) {
                        if (inputCount < 9) {
                            vibrateStack[inputCount++] = speed;
                        } else {
                            for (int i = 0; i < 9; i++) {
                                vibrateStack[i] = vibrateStack[i + 1];
                                vibrateStack[inputCount] = speed;
                            }
                            for (int i = 0; i < 10; i++) {
                                vibrateSum += vibrateStack[i];
                            }
                        }
                    }
                    if((vibrateSum / 10.0f > 90 && !runFlag) || (vibrateSum / 10.0f < 90 && runFlag)){
                        chageState();
                    }

                    if(count >= 60){
                        xVals.add(count + "");
                    }
                    if(speed < 1000) {
                        yVals.add(new Entry(speed, count++));
                    } else {
                        yVals.add(new Entry(0, count++));
                    }
                    set1 = new LineDataSet(yVals, "진동 변화율");
                    set1.enableDashedLine(15f, 15f, 0f);
                    set1.setColor(Color.BLACK);
                    set1.setCircleColor(Color.BLACK);
                    set1.setLineWidth(1f);
                    set1.setCircleSize(4f);
                    set1.setFillAlpha(65);
                    set1.setFillColor(Color.BLACK);

                    dataSets.remove(0);
                    dataSets.add(set1); // add the datasets

                    // create a data object with the datasets
                    data = new LineData(xVals, dataSets);
                    // set data
                    mChart.setData(data);
                    mChart.invalidate();
                    lastX = event.values[DATA_X];
                    lastY = event.values[DATA_Y];
                    lastZ = event.values[DATA_Z];
                }
            }
        }
        public void chageState(){
            if(runFlag = true){
                runFlag = false;
                runCount = 0;
                String url = "http://218.54.46.89:3000/washer/putstatus";
                HttpClient http = new DefaultHttpClient();
                try {

                    ArrayList<NameValuePair> nameValuePairs =
                            new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("token", token));
                    nameValuePairs.add(new BasicNameValuePair("washeridx", serial));
                    nameValuePairs.add(new BasicNameValuePair("status", "0"));

                    HttpParams params = http.getParams();
                    HttpConnectionParams.setConnectionTimeout(params, 5000);
                    HttpConnectionParams.setSoTimeout(params, 5000);

                    HttpPost httpPost = new HttpPost(url);
                    UrlEncodedFormEntity entityRequest =
                            new UrlEncodedFormEntity(nameValuePairs, "EUC-KR");

                    httpPost.setEntity(entityRequest);

                    HttpResponse responsePost = http.execute(httpPost);
                    HttpEntity resEntity = responsePost.getEntity();
                    respond = EntityUtils.toString(resEntity);
                    JSONObject obj = new JSONObject(respond);
                    Toast.makeText(context, obj.getString("msg"), Toast.LENGTH_LONG).show();
                }catch(Exception e){e.printStackTrace();}
            } else {
                runFlag = true;
                runCount = 0;
                String url = "http://218.54.46.89:3000/washer/putstatus";
                HttpClient http = new DefaultHttpClient();
                try {

                    ArrayList<NameValuePair> nameValuePairs =
                            new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("token", token));
                    nameValuePairs.add(new BasicNameValuePair("washeridx", serial));
                    nameValuePairs.add(new BasicNameValuePair("status", "1"));

                    HttpParams params = http.getParams();
                    HttpConnectionParams.setConnectionTimeout(params, 5000);
                    HttpConnectionParams.setSoTimeout(params, 5000);

                    HttpPost httpPost = new HttpPost(url);
                    UrlEncodedFormEntity entityRequest =
                            new UrlEncodedFormEntity(nameValuePairs, "EUC-KR");

                    httpPost.setEntity(entityRequest);

                    HttpResponse responsePost = http.execute(httpPost);
                    HttpEntity resEntity = responsePost.getEntity();
                    respond = EntityUtils.toString(resEntity);
                    JSONObject obj = new JSONObject(respond);
                    Toast.makeText(context, obj.getString("msg"), Toast.LENGTH_LONG).show();
                }catch(Exception e){e.printStackTrace();}
            }
        }
    }

    public static class frag_Two extends Fragment {
        Button serialBtn;
        TextView serialText;
        LinearLayout layout;
        String str;
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main_2, container, false);
            layout = (LinearLayout)rootView.findViewById(R.id.layout);
            serialText = (TextView)rootView.findViewById(R.id.serialText);
            serialText.setText(serial);
            return rootView;
        }
    }
}
