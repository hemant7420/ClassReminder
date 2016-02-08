package website.frosil.hemant.classreminder;

/* # CSIT 6000B    #  HEMANT SINGHI        20294499          hssinghi@connect.ust.hk */

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


public class ReminderFragment extends Fragment {
    ListView lv = null;
    String strtext = "";
    Date convertedDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reminders, container, false);
        RelativeLayout myLayout = (RelativeLayout) view.findViewById(R.id.rlr);

        strtext = getArguments().getString("s_ID");
        lv = (ListView) myLayout.findViewById(R.id.list1);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        new AsyncConnection().execute("");
    }

    private class AsyncConnection extends AsyncTask {

        private ArrayList list1 = new ArrayList();

        @Override
        protected Object doInBackground(Object... params) {
            try {
                ReminderService service = new ReminderService(strtext);
                JSONArray jsonArray = new JSONArray(service.getLastItems());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jo = jsonArray.getJSONObject(i);
                    HashMap map = new HashMap();
                    map.put("class_id", jo.getString("course_ID"));
                    map.put("class_day", jo.getString("course_day"));
                    // map.put("class_time", jo.getString("course_time"));
                    String time = jo.getString("course_time");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
                    convertedDate = new Date();
                    try {
                        convertedDate = dateFormat.parse(time);
                        int i1 = convertedDate.getHours() - 1;
                        convertedDate.setHours(i1);
                        String cv = dateFormat.format(convertedDate);
                        Log.e("CD", cv);

                        map.put("class_time", cv);
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    list1.add(map);
                }
            } catch (ClientProtocolException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            ListAdapter adapter = new SimpleAdapter(getActivity(), list1, R.layout.reminderliststyle, new String[]{"class_id", "class_day", "class_time"},
                    new int[]{R.id.txt_classid, R.id.txt_class_day, R.id.txt_class_time});
            lv.setAdapter(adapter);
        }

    }


}



