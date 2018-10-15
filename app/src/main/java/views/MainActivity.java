package views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v17.leanback.widget.HorizontalGridView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.weather.futureworks.weather.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import Adapters.DayAdapter;
import Adapters.DaysAdapter;
import Adapters.SelectedDayAdapter;
import Components.ExpandableGridView;
import Components.SystemBarTintManager;
import Interfaces.ClassM;
import Interfaces.CustomItemClickListener;
import Interfaces.DayItem;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class MainActivity extends AppCompatActivity {

    DaysAdapter adapter;
    ExpandableGridView gridView;
    HorizontalGridView verticalDay;
    CustomItemClickListener x;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SystemBarTintManager tintManager;
        setTranslucentStatus(true);
        tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.sunnyday);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int dpValue = 50;
        float d = MainActivity.this.getResources().getDisplayMetrics().density;
        int margin = (int)(dpValue * d);

        int dpValue1 = 300;
        float d1 = MainActivity.this.getResources().getDisplayMetrics().density;
        int height1 = (int)(dpValue1 * d1);
        if((height1 * 1.5) > height)
            height1 = (int)(150 * d1);

        LinearLayout lay = (LinearLayout) findViewById(R.id.layout);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, height - height1, 0, 0);
        lay.setLayoutParams(layoutParams);
        lay.setPadding(0,0,0,height - height1);

        RelativeLayout ly = (RelativeLayout) findViewById(R.id.top);
        RelativeLayout.LayoutParams rlayoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                height - height1);
        rlayoutParams.setMargins(0, margin, 0, 0);
        ly.setLayoutParams(rlayoutParams);

        JSONArray xt = null;
        try {
            xt = ClassM.dresults.getJSONArray("list");
            for (int i1 = 1; i1 < xt.length(); i1++) {
                JSONObject xtt = xt.getJSONObject(i1);
                JSONObject xts = xtt.getJSONObject("main");
                String status, degree;
                int img;
                String exp;
                if(ClassM.units.equals("imperial"))
                    exp = " °F";
                else
                    exp = " °C";
                degree = xts.getString("temp").substring(0, 2) + exp;
                JSONArray xt1 = xtt.getJSONArray("weather");
                JSONObject xxx = xt1.getJSONObject(0);
                status = xxx.getString("main");
                String id = xxx.getString("id");
                if (id.equals("800"))
                    img = R.drawable.clear;
                else if (Integer.parseInt(id) >= 200 && Integer.parseInt(id) < 300)
                    img = R.drawable.thunderstorm;
                else if (Integer.parseInt(id) >= 300 && Integer.parseInt(id) < 400)
                    img = R.drawable.drizzle;
                else if (Integer.parseInt(id) >= 500 && Integer.parseInt(id) < 600)
                    img = R.drawable.rainy;
                else if (Integer.parseInt(id) >= 600 && Integer.parseInt(id) < 700)
                    img = R.drawable.snowy;
                else
                    img = R.drawable.cloudy;

                TextView txt = (TextView)findViewById(R.id.degreef);
                txt.setText(degree);
                TextView txt1 = (TextView)findViewById(R.id.statusf);
                txt1.setText(status);
                ImageView imgf = (ImageView)findViewById(R.id.imgf);
                imgf.setImageResource(img);
            }
            JSONObject cityInfo = ClassM.dresults.getJSONObject("city");
            TextView txt2 = (TextView)findViewById(R.id.city);
            txt2.setText(cityInfo.getString("name"));
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df1 = new SimpleDateFormat("HH:mm");
            String formattedTime = df1.format(c.getTime());
            TextView reftime = (TextView)findViewById(R.id.reftime);
            reftime.setText(formattedTime);
        } catch (JSONException e) {
            Toast.makeText(MainActivity.this, "An error occurred. Please try again", Toast.LENGTH_LONG).show();
        }
        x = new CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

            }
        };
        verticalDay = (HorizontalGridView) findViewById(R.id.daygrid);
        verticalDay.setAdapter(new DayAdapter(ClassM.dresults, x));
        gridView = (ExpandableGridView) findViewById(R.id.days);
        adapter = new DaysAdapter(MainActivity.this, ClassM.dresults);
        gridView.setAdapter(adapter);
        gridView.setExpanded(true);
        ScrollView scrollView = (ScrollView)findViewById(R.id.sc);
        scrollView.setFocusableInTouchMode(true);
        scrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);

        Button refreshbt = (Button)findViewById(R.id.refbt);
        refreshbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressBar pb = (ProgressBar)findViewById(R.id.pb);
                pb.setVisibility(View.VISIBLE);
                pb.setIndeterminate(true);

                JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, "http://api.openweathermap.org/data/2.5/forecast?q="+ ClassM.city +"&units="+ ClassM.units +"&APPID=7143dce5516e5adc2e1b97920dbd08cb",
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                ClassM.dresults = response;
                                JSONArray xt = null;
                                try {
                                    xt = ClassM.dresults.getJSONArray("list");
                                    for (int i1 = 1; i1 < xt.length(); i1++) {
                                        JSONObject xtt = xt.getJSONObject(i1);
                                        JSONObject xts = xtt.getJSONObject("main");
                                        String status, degree;
                                        int img;
                                        String exp;
                                        if(ClassM.units.equals("imperial"))
                                            exp = " °F";
                                        else
                                            exp = " °C";
                                        degree = xts.getString("temp").substring(0, 2) + exp;
                                        JSONArray xt1 = xtt.getJSONArray("weather");
                                        JSONObject xxx = xt1.getJSONObject(0);
                                        status = xxx.getString("main");
                                        String id = xxx.getString("id");
                                        if (id.equals("800"))
                                            img = R.drawable.clear;
                                        else if (Integer.parseInt(id) >= 200 && Integer.parseInt(id) < 300)
                                            img = R.drawable.thunderstorm;
                                        else if (Integer.parseInt(id) >= 300 && Integer.parseInt(id) < 400)
                                            img = R.drawable.drizzle;
                                        else if (Integer.parseInt(id) >= 500 && Integer.parseInt(id) < 600)
                                            img = R.drawable.rainy;
                                        else if (Integer.parseInt(id) >= 600 && Integer.parseInt(id) < 700)
                                            img = R.drawable.snowy;
                                        else
                                            img = R.drawable.cloudy;

                                        TextView txt = (TextView)findViewById(R.id.degreef);
                                        txt.setText(degree);
                                        TextView txt1 = (TextView)findViewById(R.id.statusf);
                                        txt1.setText(status);
                                        ImageView imgf = (ImageView)findViewById(R.id.imgf);
                                        imgf.setImageResource(img);
                                    }
                                    JSONObject cityInfo = ClassM.dresults.getJSONObject("city");
                                    TextView txt2 = (TextView)findViewById(R.id.city);
                                    txt2.setText(cityInfo.getString("name"));
                                    Calendar c = Calendar.getInstance();
                                    SimpleDateFormat df1 = new SimpleDateFormat("HH:mm");
                                    String formattedTime = df1.format(c.getTime());
                                    TextView reftime = (TextView)findViewById(R.id.reftime);
                                    reftime.setText(formattedTime);
                                    SharedPreferences sp = getSharedPreferences("Settings", 0);
                                    SharedPreferences.Editor Ed = sp.edit();
                                    Ed.putString("response", response.toString());
                                    Calendar calendar = Calendar.getInstance();
                                    Date today = calendar.getTime();
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                    String currentDate = sdf.format(today);
                                    Ed.putString("date", currentDate);
                                    Ed.commit();
                                } catch (JSONException e) {
                                    Toast.makeText(MainActivity.this, "An error occurred. Please try again", Toast.LENGTH_LONG).show();
                                }
                                pb.setVisibility(View.GONE);
                                pb.setIndeterminate(false);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this, "An error occurred. Please try again", Toast.LENGTH_LONG).show();
                                pb.setVisibility(View.GONE);
                                pb.setIndeterminate(false);
                            }
                        }
                );
                jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                        3600000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                //Creating a request queue
                RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                //Adding our request to the queue
                requestQueue.add(jsonArrayRequest);
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog alertDialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                HorizontalGridView gridViewd = new HorizontalGridView(builder.getContext());
                DayItem x = (DayItem) gridView.getItemAtPosition(position);
                gridViewd.setAdapter(new SelectedDayAdapter(ClassM.dresults, x.dayf));
                gridViewd.setBackgroundColor(getResources().getColor(R.color.colorTextwh));
                builder.setView(gridViewd);
                builder.setTitle(x.day);
                alertDialog = builder.show();
                int dpValue = 200;
                float d = MainActivity.this.getResources().getDisplayMetrics().density;
                int margin = (int)(dpValue * d);
                alertDialog.getWindow().setLayout(MATCH_PARENT, margin);
            }
        });

        Button setting = (Button) findViewById(R.id.settings);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, SettingsActivity.class), 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        SharedPreferences sp11 = getSharedPreferences("Settings", 0);
        String city = sp11.getString("city", null);
        String temp = sp11.getString("temp", null);
        if(requestCode == 1) {
            if (!city.equals(ClassM.city) || !temp.equals(ClassM.units)) {
                final ProgressBar pb = (ProgressBar) findViewById(R.id.pb);
                pb.setVisibility(View.VISIBLE);
                pb.setIndeterminate(true);

                JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, "http://api.openweathermap.org/data/2.5/forecast?q=" + ClassM.city + "&units=" + ClassM.units + "&APPID=7143dce5516e5adc2e1b97920dbd08cb",
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                ClassM.dresults = response;
                                JSONArray xt = null;
                                try {
                                    xt = ClassM.dresults.getJSONArray("list");
                                    if (xt.length() > 0) {
                                        for (int i1 = 1; i1 < xt.length(); i1++) {
                                            JSONObject xtt = xt.getJSONObject(i1);
                                            JSONObject xts = xtt.getJSONObject("main");
                                            String status, degree;
                                            int img;
                                            String exp;
                                            if (ClassM.units.equals("imperial"))
                                                exp = " °F";
                                            else
                                                exp = " °C";
                                            degree = xts.getString("temp").substring(0, 2) + exp;
                                            JSONArray xt1 = xtt.getJSONArray("weather");
                                            JSONObject xxx = xt1.getJSONObject(0);
                                            status = xxx.getString("main");
                                            String id = xxx.getString("id");
                                            if (id.equals("800"))
                                                img = R.drawable.clear;
                                            else if (Integer.parseInt(id) >= 200 && Integer.parseInt(id) < 300)
                                                img = R.drawable.thunderstorm;
                                            else if (Integer.parseInt(id) >= 300 && Integer.parseInt(id) < 400)
                                                img = R.drawable.drizzle;
                                            else if (Integer.parseInt(id) >= 500 && Integer.parseInt(id) < 600)
                                                img = R.drawable.rainy;
                                            else if (Integer.parseInt(id) >= 600 && Integer.parseInt(id) < 700)
                                                img = R.drawable.snowy;
                                            else
                                                img = R.drawable.cloudy;

                                            TextView txt = (TextView) findViewById(R.id.degreef);
                                            txt.setText(degree);
                                            TextView txt1 = (TextView) findViewById(R.id.statusf);
                                            txt1.setText(status);
                                            ImageView imgf = (ImageView) findViewById(R.id.imgf);
                                            imgf.setImageResource(img);
                                        }
                                        JSONObject cityInfo = ClassM.dresults.getJSONObject("city");
                                        TextView txt2 = (TextView) findViewById(R.id.city);
                                        txt2.setText(cityInfo.getString("name"));
                                        Calendar c = Calendar.getInstance();
                                        SimpleDateFormat df1 = new SimpleDateFormat("HH:mm");
                                        String formattedTime = df1.format(c.getTime());
                                        TextView reftime = (TextView) findViewById(R.id.reftime);
                                        reftime.setText(formattedTime);
                                        SharedPreferences sp = getSharedPreferences("Settings", 0);
                                        SharedPreferences.Editor Ed = sp.edit();
                                        Ed.putString("city", ClassM.city);
                                        Ed.putString("temp", ClassM.units);
                                        Ed.putString("response", response.toString());
                                        Calendar calendar = Calendar.getInstance();
                                        Date today = calendar.getTime();
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                        String currentDate = sdf.format(today);
                                        Ed.putString("date", currentDate);
                                        Ed.commit();
                                        adapter.updateAdapter(ClassM.dresults);
                                        gridView.deferNotifyDataSetChanged();
                                        gridView.invalidateViews();
                                        gridView.setAdapter(adapter);
                                        verticalDay.setAdapter(new DayAdapter(ClassM.dresults, x));
                                    } else {
                                        Toast.makeText(MainActivity.this, "We couldn't find the city.", Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(MainActivity.this, "An error occurred. Please try again", Toast.LENGTH_LONG).show();
                                }
                                pb.setVisibility(View.GONE);
                                pb.setIndeterminate(false);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (error.toString().substring(0, 10).equals("Unexpected"))
                                    Toast.makeText(MainActivity.this, "We couldn't find the city.", Toast.LENGTH_LONG).show();
                                else
                                    Toast.makeText(MainActivity.this, "An error occurred. Please try again", Toast.LENGTH_LONG).show();
                                pb.setVisibility(View.GONE);
                                pb.setIndeterminate(false);
                            }
                        }
                );
                jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                        3600000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                //Creating a request queue
                RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                //Adding our request to the queue
                requestQueue.add(jsonArrayRequest);
            }
        }
    }

    private void setTranslucentStatus(boolean on) {
        // TODO Auto-generated method stub

        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);

    }
}
