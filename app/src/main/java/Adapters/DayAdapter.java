package Adapters;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.weather.futureworks.weather.R;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import Interfaces.ClassM;
import Interfaces.CustomItemClickListener;
import Interfaces.DayItemF;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.ItemViewHolder> {

    private CustomItemClickListener listener;

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView degree,time;
        ImageView img;
        ItemViewHolder(View myView) {
            super(myView);
            degree = (TextView) myView.findViewById(R.id.vdegree);
            time = (TextView) myView.findViewById(R.id.vtime);
            img = (ImageView) myView.findViewById(R.id.vdayicon);
        }
    }

    private ArrayList<DayItemF> contentItems = new ArrayList<>();

    public DayAdapter(JSONObject xy, CustomItemClickListener listener){
        try {
            JSONArray xt = xy.getJSONArray("list");
            for(int i1 = 0; i1< xt.length(); i1++) {
                JSONObject xtt = xt.getJSONObject(i1);
                JSONObject xts = xtt.getJSONObject("main");
                String time, degree;
                int img;
                String exp;
                if(ClassM.units.equals("imperial"))
                    exp = " °F";
                else
                    exp = " °C";
                degree = xts.getString("temp").substring(0, 2) + exp;
                JSONArray xt1 = xtt.getJSONArray("weather");
                JSONObject xxx = xt1.getJSONObject(0);
                time = xtt.getString("dt_txt");
                Calendar calendar = Calendar.getInstance();
                Date today = calendar.getTime();

                calendar.add(Calendar.DAY_OF_YEAR, 1);
                Date tomorrow = calendar.getTime();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String currentDate = sdf.format(today);
                String tomorrowDate = sdf.format(tomorrow);
                String id = xxx.getString("id");
                if (id.equals("800"))
                    img = R.drawable.clears;
                else if (Integer.parseInt(id) >= 200 && Integer.parseInt(id) < 300)
                    img = R.drawable.thunderstorms;
                else if (Integer.parseInt(id) >= 300 && Integer.parseInt(id) < 400)
                    img = R.drawable.drizzles;
                else if (Integer.parseInt(id) >= 500 && Integer.parseInt(id) < 600)
                    img = R.drawable.rainys;
                else if (Integer.parseInt(id) >= 600 && Integer.parseInt(id) < 700)
                    img = R.drawable.snowys;
                else
                    img = R.drawable.cloudys;

                time = time.substring(time.length() - 8, time.length() - 3);
                DayItemF xyy = new DayItemF(time, degree, img);
                if(xtt.getString("dt_txt").substring(0,10).equals(currentDate) || xtt.getString("dt_txt").substring(0,10).equals(tomorrowDate)) {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date now = new Date();
                    int difference = now.compareTo(formatter.parse(xtt.getString("dt_txt")));
                    if(difference < 0)
                        contentItems.add(xyy);
                }
            }

        }
        catch (Exception e){

        }
        this.listener = listener;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.times, viewGroup, false);
        final ItemViewHolder pvh = new ItemViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, pvh.getPosition());
            }
        });
        return pvh;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder itemViewHolder, int i) {
        itemViewHolder.degree.setText(contentItems.get(i).degree);
        itemViewHolder.time.setText(contentItems.get(i).time);
        itemViewHolder.img.setImageResource(contentItems.get(i).resImg);
    }

    @Override
    public int getItemCount() {
        return contentItems.size();
    }
}