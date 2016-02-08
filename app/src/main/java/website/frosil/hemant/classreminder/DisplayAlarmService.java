package website.frosil.hemant.classreminder;

/* # CSIT 6000B    #  HEMANT SINGHI        20294499          hssinghi@connect.ust.hk */

import android.app.AlertDialog;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.WindowManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DisplayAlarmService extends Service {

    private static String TAG = "MESSAGE";
    int h1=0, m1,hours=0,minutes=0;
    String s_ID="", course_day="", weekDay="",c_id;
    String course_time;
    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        s_ID="";
        course_day="";
            s_ID = intent.getStringExtra("s_ID");
            course_time= intent.getStringExtra("course_time");
            course_day = intent.getStringExtra("course_day");
            c_id = intent.getStringExtra("course_id");
            setalarm();
        return super.onStartCommand(intent, flags, startId);
    }

    public void setalarm()
    {

        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);



            Calendar calendar = Calendar.getInstance();
            weekDay = dayFormat.format(calendar.getTime());

            if (((course_day.equals(null)) && (course_day.isEmpty())) || ((course_time.equals(null)) && (course_time.isEmpty())) ) {
                Log.e("course_day", course_day);
                Log.e("hour", Integer.toString(h1));
                Log.e("minute", Integer.toString(m1));
                Log.e("course_time", course_time);

                AlertDialog alertDialog = new AlertDialog.Builder(this)
                        .setTitle(s_ID)
                        .setMessage("No alarm for today.")
                        .create();

                alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                alertDialog.show();
            }
            else  if (((!course_day.equals(null)) && (!course_day.isEmpty())) || ((!course_time.equals(null)) && (!course_time.isEmpty())) ){

                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
                Date convertedDate;
                try {
                    calendar = Calendar.getInstance();
                    calendar.set(Calendar.AM_PM, 1);
                    convertedDate = dateFormat.parse(course_time);
                    calendar.setTime(convertedDate);
                    h1 = calendar.get(Calendar.HOUR_OF_DAY);
                    m1 = calendar.get(Calendar.MINUTE);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Log.e("s_ID", s_ID);
                Log.e("course_day", course_day);
                Log.e("hour", Integer.toString(h1));
                Log.e("minute", Integer.toString(m1));
                Log.e("course_time", course_time);

                Log.d(TAG, "Alarm started");
                Calendar cal = Calendar.getInstance();
                hours = cal.get(Calendar.HOUR_OF_DAY);
                minutes = cal.get(Calendar.MINUTE);


                if ((h1 == hours) && (m1 == minutes) && (weekDay.equals(course_day) )) {
                    AlertDialog alertDialog = new AlertDialog.Builder(this)
                            .setTitle(s_ID)
                            .setMessage("You have a class of "+c_id)
                            .create();

                    alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                    alertDialog.show();
                }

            }

        }


    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        Log.d(TAG, "FirstService destroyed");
    }
}


