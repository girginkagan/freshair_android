package views;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.weather.futureworks.weather.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import Interfaces.ClassM;

public class SplashActivity extends AppCompatActivity {
    Button rtbt;
    ProgressBar pb;
    LocationManager lm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SharedPreferences sp11 = getSharedPreferences("Settings", 0);
        String city = sp11.getString("city", null);
        String temp = sp11.getString("temp", null);
        String response = sp11.getString("response", null);
        String date = sp11.getString("date", null);

        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = sdf.format(today);
        rtbt = (Button) findViewById(R.id.retrybt);
        pb = (ProgressBar) findViewById(R.id.pb);
        rtbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });
        try {
            if (!city.equals("")) {
                ClassM.city = city;
            }
            if (!temp.equals("")) {
                ClassM.units = temp;
            }
            if(temp.equals("") || city.equals(""))
                start();
        }
        catch (NullPointerException e){
            ClassM.units = "metric";
            start();
        }
        try {
            if (!response.equals("") && date.equals(currentDate)) {
                try {
                    ClassM.dresults = new JSONObject(response);
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } catch (JSONException e) {

                }
            } else {
                JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, "http://api.openweathermap.org/data/2.5/forecast?q=" + ClassM.city + "&units=" + ClassM.units + "&APPID=7143dce5516e5adc2e1b97920dbd08cb",
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                ClassM.dresults = response;
                                SharedPreferences sp = getSharedPreferences("Settings", 0);
                                SharedPreferences.Editor Ed = sp.edit();
                                Ed.putString("response", response.toString());
                                Calendar calendar = Calendar.getInstance();
                                Date today = calendar.getTime();
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                String currentDate = sdf.format(today);
                                Ed.putString("date", currentDate);
                                Ed.commit();
                                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(SplashActivity.this, "An error occurred. Please try again", Toast.LENGTH_LONG).show();
                            }
                        }
                );
                jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                        3600000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                //Creating a request queue
                RequestQueue requestQueue = Volley.newRequestQueue(SplashActivity.this);
                //Adding our request to the queue
                requestQueue.add(jsonArrayRequest);
            }
        }
        catch (NullPointerException e){
            if(!ClassM.city.equals("")) {
                JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, "http://api.openweathermap.org/data/2.5/forecast?q=" + ClassM.city + "&units=" + ClassM.units + "&APPID=7143dce5516e5adc2e1b97920dbd08cb",
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                ClassM.dresults = response;
                                SharedPreferences sp = getSharedPreferences("Settings", 0);
                                SharedPreferences.Editor Ed = sp.edit();
                                Ed.putString("response", response.toString());
                                Calendar calendar = Calendar.getInstance();
                                Date today = calendar.getTime();
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                String currentDate = sdf.format(today);
                                Ed.putString("date", currentDate);
                                Ed.commit();
                                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(SplashActivity.this, "An error occurred. Please try again", Toast.LENGTH_LONG).show();
                            }
                        }
                );
                jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                        3600000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                //Creating a request queue
                RequestQueue requestQueue = Volley.newRequestQueue(SplashActivity.this);
                //Adding our request to the queue
                requestQueue.add(jsonArrayRequest);
            }
        }
    }


    void start(){
        rtbt.setVisibility(View.VISIBLE);
        pb.setVisibility(View.GONE);
        SharedPreferences sp = getSharedPreferences("Settings", 0);
        SharedPreferences.Editor Ed = sp.edit();
        Ed.putString("city", "");
        Ed.putString("temp", "metric");
        Ed.commit();
        final AlertDialog alertDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
        builder.setTitle("Select City");

        final EditText input = new EditText(SplashActivity.this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("City name");
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                rtbt.setVisibility(View.GONE);
                pb.setVisibility(View.VISIBLE);
                if(input.getText().length() >= 2) {
                    ClassM.city = input.getText().toString();
                    JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, "http://api.openweathermap.org/data/2.5/forecast?q=" + ClassM.city + "&units=" + ClassM.units + "&APPID=7143dce5516e5adc2e1b97920dbd08cb",
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    ClassM.dresults = response;
                                    ClassM.city = input.getText().toString();
                                    SharedPreferences sp = getSharedPreferences("Settings", 0);
                                    SharedPreferences.Editor Ed = sp.edit();
                                    Ed.putString("response", response.toString());
                                    Calendar calendar = Calendar.getInstance();
                                    Date today = calendar.getTime();
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                    String currentDate = sdf.format(today);
                                    Ed.putString("date", currentDate);
                                    Ed.putString("city", ClassM.city);
                                    Ed.commit();
                                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(SplashActivity.this, "An error occurred. Please try again", Toast.LENGTH_LONG).show();
                                    rtbt.setVisibility(View.VISIBLE);
                                    pb.setVisibility(View.GONE);
                                }
                            }
                    );
                    jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                            3600000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    //Creating a request queue
                    RequestQueue requestQueue = Volley.newRequestQueue(SplashActivity.this);
                    //Adding our request to the queue
                    requestQueue.add(jsonArrayRequest);
                }
                else{
                    Toast.makeText(SplashActivity.this, "City name should be more than a character.", Toast.LENGTH_LONG).show();
                    rtbt.setVisibility(View.VISIBLE);
                    pb.setVisibility(View.GONE);
                }
            }
        });
        alertDialog = builder.create();
        alertDialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
                input.getBackground().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_IN);
            }
        });
        alertDialog.show();
    }
}