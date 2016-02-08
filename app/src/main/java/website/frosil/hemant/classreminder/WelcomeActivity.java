package website.frosil.hemant.classreminder;

/* # CSIT 6000B    #  HEMANT SINGHI        20294499          hssinghi@connect.ust.hk */

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class WelcomeActivity extends Activity {
    protected Vibrator vibr;
    SharedPreferences appPreferences;
    boolean isAppInstalled = false;
    private View.OnClickListener btnListener = new View.OnClickListener() {
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.l_btn:
                    // here we have used, switch case, because on activity_login activity you may
                    // also want to show registration button, so if the user is new ! we can go the
                    // registration activity , other than this we could also do this without switch //case.
                    Intent ii = new Intent(WelcomeActivity.this, LoginActivity.class);
                    startActivity(ii);
                    break;
                case R.id.s_btn:
                    // here we have used, switch case, because on activity_login activity you may
                    // also want to show registration button, so if the user is new ! we can go the
                    // registration activity , other than this we could also do this without switch //case.
                    Intent ix = new Intent(WelcomeActivity.this, Registration.class);
                    startActivity(ix);
                    break;
                default:
                    break;

            }
        }
    };
   /* Commented on 4th Nov, 2015 11:02 AM
   private void addShortcut() {

        //Adding shortcut for MainActivity
        //on Home screen
        Intent shortcutIntent = new Intent(getApplicationContext(),
                WelcomeActivity.class);

        shortcutIntent.setAction(Intent.ACTION_MAIN);

        Intent addIntent = new Intent();
        addIntent
                .putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "Class Reminder");

        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(getApplicationContext(),
                        R.mipmap.ic_icon));
        addIntent.putExtra("duplicate", true);


        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        getApplicationContext().sendBroadcast(addIntent);
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        vibr = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        if (vibr == null) {
            Log.v("message", "No vibration service exists.");
        } else {
            Log.v("message", "vibration service exists.");
            // Vibrate for 500 milliseconds
            vibr.vibrate(1000);
        }

        appPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        isAppInstalled = appPreferences.getBoolean("isAppInstalled", false);
        if (isAppInstalled == false) {
            /**
             * create short code
             */
            Intent shortcutIntent = new Intent(getApplicationContext(), WelcomeActivity.class);
            shortcutIntent.setAction(Intent.ACTION_MAIN);
            Intent intent = new Intent();
            intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
            intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "Class Reminder");
            intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.mipmap.ic_icon));
            intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
            getApplicationContext().sendBroadcast(intent);
            /**
             * Make preference true
             */
            SharedPreferences.Editor editor = appPreferences.edit();
            editor.putBoolean("isAppInstalled", true);
            editor.commit();
        }

        if (isNetworkAvailable()) {

            Button btn1 = (Button) findViewById(R.id.l_btn);
            btn1.setOnClickListener(btnListener);
            Button btn2 = (Button) findViewById(R.id.s_btn);
            btn2.setOnClickListener(btnListener);

            // Commented on 4th Nov 2015, 11:02 AM
            // addShortcut();

        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Network connection")
                    .setMessage("Do you want to open settings?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                        }
                    })
                    .setNegativeButton(android.R.string.no, null).show();
            // Error message here if network is unavailable.
            Toast.makeText(this, "Network is unavailable!", Toast.LENGTH_LONG).show();
        }


    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }
}
