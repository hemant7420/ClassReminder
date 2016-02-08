package website.frosil.hemant.classreminder;

/* # CSIT 6000B    #  HEMANT SINGHI        20294499          hssinghi@connect.ust.hk */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements OnClickListener {
    private static final String LOGIN_URL = "http://frosil.website/API/activity_login.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    // JSON parser class
    JSONParser jsonParser = new JSONParser();
    private EditText user, pass;
    private Button bLogin;
    // Progress Dialog
    private ProgressDialog pDialog;

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (isNetworkAvailable()) {

            user = (EditText) findViewById(R.id.username);
            pass = (EditText) findViewById(R.id.password);
            bLogin = (Button) findViewById(R.id.login);

            bLogin.setOnClickListener(this);
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.login:
                new AttemptLogin().execute();
                // here we have used, switch case, because on activity_login activity you may
                // also want to show registration button, so if the user is new ! we can go the
                // registration activity , other than this we could also do this without switch //case.
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

        return super.onOptionsItemSelected(item);
    }


    class AttemptLogin extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread Show Progress Dialog *
         */

        String s_username = user.getText().toString();
        String s_pwd = pass.getText().toString();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Attempting for activity_login...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // here Check for success tag
            int success;

            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("s_username", s_username));
                params.add(new BasicNameValuePair("s_pwd", s_pwd));
                Log.d("request!", "starting");
                JSONObject json = jsonParser.makeHttpRequest(LOGIN_URL, "POST", params);
                // checking log for json response
                Log.e("Login attempt", json.toString());
                // success tag for json
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    String msg = json.getString("name");
                    String s_ID = json.getString("s_ID");
                    String s_name = json.getString("name");
                    String class_count = json.getString("class_count");
                    String reminder_count = json.getString("reminder_count");
                    Log.d("Successfully Login!", json.toString());
                    Intent ii = new Intent(LoginActivity.this, StartActivity.class);
                    ii.putExtra("name", s_name);
                    ii.putExtra("s_ID", s_ID);
                    ii.putExtra("msg", msg);
                    ii.putExtra("class_count", class_count);
                    ii.putExtra("reminder_count", reminder_count);
                    finish();
                    // this finish() method is used to tell android os that we are done with current
                    // activity now! Moving to other activity
                    startActivity(ii);

                    return json.getString(TAG_MESSAGE);
                } else {
                    return json.getString(TAG_MESSAGE);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * Once the background process is done we need to Dismiss the progress dialog asap *
         **/
        protected void onPostExecute(String message) {
            pDialog.dismiss();
            if (message != null) {
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
            }


        }

    }
}

