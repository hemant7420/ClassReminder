package website.frosil.hemant.classreminder;

/* # CSIT 6000B    #  HEMANT SINGHI        20294499          hssinghi@connect.ust.hk */

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class StartActivity extends AppCompatActivity {
    String[] titles = {"Home", "Classes", "Reminders", "Logout"};
    Date convertedDate;
    String class_count = "";
    String reminder_count = "";
    String s_ID = "";
    String substr2 = "";
    String substr3 = "";
    String welcome_tv = "";
    Bundle extras;
    String weekDay = "";
    PendingIntent pintent;
    /* Added on 21st Oct, 2015, 2:47PM*/
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private CharSequence mTitle;
    private CharSequence mDrawerTitle;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar topToolBar;
    private ListView listView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start);
        listView1 = (ListView) findViewById(R.id.list2);
        new AsyncConnection().execute();
        mTitle = mDrawerTitle = getTitle();

        topToolBar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(topToolBar);
        topToolBar.setLogo(R.mipmap.ic_acbar);
        topToolBar.setLogoDescription(getResources().getString(R.string.logo_desc));

        extras = getIntent().getExtras();
        if (extras != null) {
            welcome_tv = extras.getString("name");
        }

        final TextView welcome_login = (TextView) findViewById(R.id.textView_login);
        final TextView rem_txt = (TextView) findViewById(R.id.textView_list);
        welcome_login.setText("Welcome  " + welcome_tv);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        extras = getIntent().getExtras();
        if (extras != null) {
            class_count = extras.getString("class_count");
            substr2 = class_count.replaceAll("[\\W]", "");
            Log.e("class", substr2);
            reminder_count = extras.getString("reminder_count");
            substr3 = class_count.replaceAll("[\\W]", "");
            Log.e("reminder", substr3);
        }

        List<ItemObject> listViewItems = new ArrayList<ItemObject>();
        listViewItems.add(new ItemObject("Home", R.mipmap.ic_home1));
        listViewItems.add(new ItemObject("Classes" + "       " + substr2, R.mipmap.ic_class));
        listViewItems.add(new ItemObject("Reminder" + "    " + substr3, R.mipmap.ic_reminder));
        listViewItems.add(new ItemObject("Logout", R.mipmap.ic_lock));

        mDrawerList.setAdapter(new StartActivityAdapter(this, listViewItems));

        mDrawerToggle = new ActionBarDrawerToggle(StartActivity.this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);

                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.setDrawerIndicatorEnabled(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                welcome_login.setVisibility(View.GONE);
                rem_txt.setVisibility(View.GONE);
                listView1.setVisibility(View.GONE);
                selectItemFragment(position);
            }
        });

        extras = getIntent().getExtras();
        if (extras != null) {
            welcome_tv = extras.getString("s_ID");

        }


    }


    private void selectItemFragment(int position) {

        Fragment fragment = null;
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        switch (position) {
            default:
            case 0:

                extras = getIntent().getExtras();
                if (extras != null) {
                    welcome_tv = extras.getString("name");
                    s_ID = extras.getString("s_ID");

                }
                Bundle bundle = new Bundle();
                bundle.putString("s_ID", s_ID);
                bundle.putString("name", welcome_tv);
                // set Fragmentclass Arguments
                fragment = new HomeFragment();
                fragment.setArguments(bundle);
                break;
            case 1:
                extras = getIntent().getExtras();
                if (extras != null) {
                    s_ID = extras.getString("s_ID");

                }
                bundle = new Bundle();
                bundle.putString("s_ID", s_ID);
                // set Fragmentclass Arguments
                fragment = new ClassFragment();
                fragment.setArguments(bundle);
                break;
            case 2:
                extras = getIntent().getExtras();
                if (extras != null) {
                    s_ID = extras.getString("s_ID");

                }
                bundle = new Bundle();
                bundle.putString("s_ID", s_ID);
                // set Fragmentclass Arguments
                fragment = new ReminderFragment();
                fragment.setArguments(bundle);
                break;
            case 3:
                fragment = new LogoutFragment();
                break;
        }
        fragmentManager.beginTransaction().replace(R.id.main_fragment_container, fragment).commit();

        mDrawerList.setItemChecked(position, true);
        setTitle(titles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }


    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class AsyncConnection extends AsyncTask {

        String course_day = "";
        String course_time = "";
        String s_ID1;
        String c_id ="";
        int i1;
        String cv = "";
        private ArrayList list1 = new ArrayList();

        @Override
        protected Object doInBackground(Object... params) {
            try {
                extras = getIntent().getExtras();
                if (extras != null) {
                     s_ID1= extras.getString("s_ID");
                    Log.e("s_ID", s_ID1);
                }

                SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);

                Calendar calendar = Calendar.getInstance();
                weekDay = dayFormat.format(calendar.getTime());

                OtherActRemServ service = new OtherActRemServ(weekDay, s_ID1);
                Log.e("weekDay", weekDay);
                JSONArray jsonArray = new JSONArray(service.getLastItems());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jo = jsonArray.getJSONObject(i);
                    HashMap map = new HashMap();
                    map.put("class_id1", jo.getString("course_ID"));
                    map.put("class_day1", jo.getString("course_day"));
                    // map.put("class_time1", jo.getString("course_time"));
                    String time = jo.getString("course_time");

                    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
                    convertedDate = new Date();
                    try {
                        convertedDate = dateFormat.parse(time);
                        i1 = convertedDate.getHours() - 1;
                        convertedDate.setHours(i1);
                        cv = dateFormat.format(convertedDate);
                        Log.e("CD", cv);

                        map.put("class_time1", cv);


                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


                    list1.add(map);
                    c_id =jo.getString("course_ID");
                    course_day = jo.getString("course_day");
                    course_time = jo.getString("course_time");
                }


            } catch (ClientProtocolException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            Calendar cal = Calendar.getInstance();
            Intent serviceIntent = new Intent(getApplicationContext(), DisplayAlarmService.class);

            serviceIntent.putExtra("s_ID",s_ID1);
            serviceIntent.putExtra("course_time",cv);
            serviceIntent.putExtra("course_day",course_day);
            serviceIntent.putExtra("course_id",c_id);
            pintent = PendingIntent.getService(getApplicationContext(),0, serviceIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),30*1000, pintent);


            // getApplicationContext().startService(serviceIntent);
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            ListAdapter adapter = new SimpleAdapter(StartActivity.this, list1, R.layout.oa_reminder, new String[]{"class_id1", "class_day1", "class_time1"},
                    new int[]{R.id.txt_classid1, R.id.txt_class_day1, R.id.txt_class_time1});
            listView1.setAdapter(adapter);
            Log.e("Called", "Yes");

        }
    }
}