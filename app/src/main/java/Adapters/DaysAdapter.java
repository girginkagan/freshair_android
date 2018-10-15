package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.weather.futureworks.weather.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import Interfaces.ClassM;
import Interfaces.DayItem;

public class DaysAdapter extends BaseAdapter{
    private Context context;
    private  ArrayList<DayItem> contentItems = new ArrayList<>();
    LayoutInflater inflater;
    public DaysAdapter(Context context, JSONObject xy) {
        this.context = context;
        try {
                JSONArray xt = xy.getJSONArray("list");
                for(int i1 = 1; i1< xt.length(); i1++) {
                    JSONObject xtt = xt.getJSONObject(i1);
                    JSONObject xts = xtt.getJSONObject("main");
                    String day, status, degree;
                    int img;
                    String exp;
                    if(ClassM.units.equals("imperial"))
                        exp = " °F";
                    else
                        exp = " °C";
                    degree = xts.getString("temp").substring(0,2) + exp;
                    JSONArray xt1 = xtt.getJSONArray("weather");
                    for (int i2 = 0; i2 < xt1.length(); i2++) {
                        JSONObject xxx = xt1.getJSONObject(i2);
                        day = xtt.getString("dt_txt");
                        status = xxx.getString("main");
                        String id = xxx.getString("id");
                        if(id.equals("800"))
                            img = R.drawable.clears;
                        else if(Integer.parseInt(id)>=200 && Integer.parseInt(id)<300)
                            img = R.drawable.thunderstorms;
                        else if(Integer.parseInt(id)>=300 && Integer.parseInt(id)<400)
                            img = R.drawable.drizzles;
                        else if(Integer.parseInt(id)>=500 && Integer.parseInt(id)<600)
                            img = R.drawable.rainys;
                        else if(Integer.parseInt(id)>=600 && Integer.parseInt(id)<700)
                            img = R.drawable.snowys;
                        else
                            img = R.drawable.cloudys;
                        if(day.substring(day.length() - 8, day.length()).equals("12:00:00")) {
                            String dayf = day.substring(0, 10);
                            Calendar calendar = Calendar.getInstance();
                            Date date = new Date();
                            SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
                            try {
                                date = date_format.parse(day.substring(0, 10));
                            } catch (ParseException e) {
                            }

                            calendar.setTime(date);
                            int result = calendar.get(Calendar.DAY_OF_WEEK);
                            switch (result) {
                                case Calendar.SUNDAY:
                                    day = "Sunday";
                                    break;
                                case Calendar.MONDAY:
                                    day = "Monday";
                                    break;
                                case Calendar.TUESDAY:
                                    day = "Tuesday";
                                    break;
                                case Calendar.WEDNESDAY:
                                    day = "Wednesday";
                                    break;
                                case Calendar.THURSDAY:
                                    day = "Thursday";
                                    break;
                                case Calendar.FRIDAY:
                                    day = "Friday";
                                    break;
                                case Calendar.SATURDAY:
                                    day = "Saturday";
                                    break;
                            }
                            DayItem xyy = new DayItem(day, dayf, status, degree, img);
                            contentItems.add(xyy);
                        }
                    }
                }

        }
        catch (Exception e){

        }
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void updateAdapter(JSONObject xy){
        ArrayList<DayItem> contentItemstemp = new ArrayList<>();
        try {
            JSONArray xt = xy.getJSONArray("list");
            for(int i1 = 1; i1< xt.length(); i1++) {
                JSONObject xtt = xt.getJSONObject(i1);
                JSONObject xts = xtt.getJSONObject("main");
                String day, status, degree;
                int img;
                String exp;
                if(ClassM.units.equals("imperial"))
                    exp = " °F";
                else
                    exp = " °C";
                degree = xts.getString("temp").substring(0,2) + exp;
                JSONArray xt1 = xtt.getJSONArray("weather");
                for (int i2 = 0; i2 < xt1.length(); i2++) {
                    JSONObject xxx = xt1.getJSONObject(i2);
                    day = xtt.getString("dt_txt");
                    status = xxx.getString("main");
                    String id = xxx.getString("id");
                    if(id.equals("800"))
                        img = R.drawable.clears;
                    else if(Integer.parseInt(id)>=200 && Integer.parseInt(id)<300)
                        img = R.drawable.thunderstorms;
                    else if(Integer.parseInt(id)>=300 && Integer.parseInt(id)<400)
                        img = R.drawable.drizzles;
                    else if(Integer.parseInt(id)>=500 && Integer.parseInt(id)<600)
                        img = R.drawable.rainys;
                    else if(Integer.parseInt(id)>=600 && Integer.parseInt(id)<700)
                        img = R.drawable.snowys;
                    else
                        img = R.drawable.cloudys;
                    if(day.substring(day.length() - 8, day.length()).equals("12:00:00")) {
                        String dayf = day.substring(0, 10);
                        Calendar calendar = Calendar.getInstance();
                        Date date = new Date();
                        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            date = date_format.parse(day.substring(0, 10));
                        } catch (ParseException e) {
                        }

                        calendar.setTime(date);
                        int result = calendar.get(Calendar.DAY_OF_WEEK);
                        switch (result) {
                            case Calendar.SUNDAY:
                                day = "Sunday";
                                break;
                            case Calendar.MONDAY:
                                day = "Monday";
                                break;
                            case Calendar.TUESDAY:
                                day = "Tuesday";
                                break;
                            case Calendar.WEDNESDAY:
                                day = "Wednesday";
                                break;
                            case Calendar.THURSDAY:
                                day = "Thursday";
                                break;
                            case Calendar.FRIDAY:
                                day = "Friday";
                                break;
                            case Calendar.SATURDAY:
                                day = "Saturday";
                                break;
                        }
                        DayItem xyy = new DayItem(day, dayf, status, degree, img);
                        contentItemstemp.add(xyy);
                    }
                }
            }

        }
        catch (Exception e){

        }
        contentItems = contentItemstemp;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View myView;
        if (null == convertView) {
            myView = inflater.inflate(R.layout.day, parent, false);

            TextView txt = (TextView) myView.findViewById(R.id.forecast);
            txt.setText(contentItems.get(position).status);
            TextView txt1 = (TextView) myView.findViewById(R.id.dayw);
            txt1.setText(contentItems.get(position).day);
            TextView txt2 = (TextView) myView.findViewById(R.id.degree);
            txt2.setText(contentItems.get(position).degree);
            ImageView img = (ImageView)myView.findViewById(R.id.img);
            img.setImageResource(contentItems.get(position).resImg);
        }
        else{
            myView = convertView;
        }
        return myView;
    }

    @Override
    public int getCount() {
        return contentItems.size();
    }

    @Override
    public Object getItem(int position) {
        return contentItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}