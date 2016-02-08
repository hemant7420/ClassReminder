package website.frosil.hemant.classreminder;

/* # CSIT 6000B    #  HEMANT SINGHI        20294499          hssinghi@connect.ust.hk */

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import java.util.ArrayList;
import java.util.HashMap;

public class ClassFragment extends Fragment {
    ListView lv = null;
    String strtext = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_class, container, false);
        RelativeLayout myLayout = (RelativeLayout) view.findViewById(R.id.rlc);
        strtext = getArguments().getString("s_ID");
        lv = (ListView) myLayout.findViewById(R.id.list);
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
                ClassService service = new ClassService(strtext);
                JSONArray jsonArray = new JSONArray(service.getLastItems());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jo = jsonArray.getJSONObject(i);
                    HashMap map = new HashMap();
                    map.put("course_ID", jo.getString("course_ID"));
                    map.put("course_name", jo.getString("course_name"));
                    map.put("course_desc", jo.getString("course_desc"));
                    map.put("course_room", jo.getString("course_room"));
                    map.put("course_prof", jo.getString("course_prof"));
                    map.put("course_time", jo.getString("course_time"));
                    map.put("course_day", jo.getString("course_day"));
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
            ListAdapter adapter = new SimpleAdapter(getActivity(), list1, R.layout.classliststyle, new String[]{"course_ID", "course_name", "course_desc", "course_room", "course_prof", "course_time", "course_day"},
                    new int[]{R.id.txt_course_id, R.id.txt_course_name, R.id.txt_course_desc, R.id.txt_course_room, R.id.txt_course_prof, R.id.txt_course_time, R.id.txt_course_day});
            lv.setAdapter(adapter);
        }

    }


}



