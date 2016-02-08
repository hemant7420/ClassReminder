package website.frosil.hemant.classreminder;

/* # CSIT 6000B    #  HEMANT SINGHI        20294499          hssinghi@connect.ust.hk */

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class Registration extends AppCompatActivity {
    private static final String REGISTRATION_URL = "http://frosil.website/API/registration.php";
    ArrayList<String> listItems = new ArrayList<>();
    ArrayList<String> listItems1 = new ArrayList<>();
    ArrayList<String> listItems2 = new ArrayList<>();
    ArrayList<String> listItems3 = new ArrayList<>();
    ArrayList<String> listItems4 = new ArrayList<>();
    ArrayList<String> listItems5 = new ArrayList<>();
    String line;
    ArrayAdapter<String> adapter, adapter1, adapter2, adapter3, adapter4, adapter5;
    Spinner sp1, sp2, sp3, sp4, sp5, sp6;
    private EditText user, pass, email, studentID;
    // Progress Dialog

    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        if (isNetworkAvailable()) {
            studentID = (EditText) findViewById(R.id.studentid);
            user = (EditText) findViewById(R.id.username);
            pass = (EditText) findViewById(R.id.password);
            email = (EditText) findViewById(R.id.studentemail);


            sp1 = (Spinner) findViewById(R.id.spinner1);
            adapter = new ArrayAdapter<>(this, R.layout.reg_spinner_layout, R.id.txt, listItems);
            sp1.setAdapter(adapter);

            sp2 = (Spinner) findViewById(R.id.spinner2);
            adapter1 = new ArrayAdapter<>(this, R.layout.reg_spinner_layout, R.id.txt, listItems1);
            sp2.setAdapter(adapter1);

            sp3 = (Spinner) findViewById(R.id.spinner3);
            adapter2 = new ArrayAdapter<>(this, R.layout.reg_spinner_layout, R.id.txt, listItems2);
            sp3.setAdapter(adapter2);

            sp4 = (Spinner) findViewById(R.id.spinner4);
            adapter3 = new ArrayAdapter<>(this, R.layout.reg_spinner_layout, R.id.txt, listItems3);
            sp4.setAdapter(adapter3);

            sp5 = (Spinner) findViewById(R.id.spinner5);
            adapter4 = new ArrayAdapter<>(this, R.layout.reg_spinner_layout, R.id.txt, listItems4);
            sp5.setAdapter(adapter4);

            sp6 = (Spinner) findViewById(R.id.spinner6);
            adapter5 = new ArrayAdapter<>(this, R.layout.reg_spinner_layout, R.id.txt, listItems5);
            sp6.setAdapter(adapter5);
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

    public void onStart() {
        super.onStart();
        BackTask bt = new BackTask();
        bt.execute();
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

    // Email Validation

    public void insert(View view) {

        String c_id = sp1.getSelectedItem().toString();
        c_id = c_id.substring(0, 10);
        Log.e("c_id", c_id);
        String c_id1 = sp2.getSelectedItem().toString();
        c_id1 = c_id1.substring(0, 10);
        Log.e("c_id1", c_id1);
        String c_id2 = sp3.getSelectedItem().toString();
        c_id2 = c_id2.substring(0, 10);
        Log.e("c_id2", c_id2);
        String c_id3 = sp4.getSelectedItem().toString();
        c_id3 = c_id3.substring(0, 10);
        Log.e("c_id3", c_id3);
        String c_id4 = sp5.getSelectedItem().toString();
        c_id4 = c_id4.substring(0, 10);
        Log.e("c_id4", c_id4);
        String c_id5 = sp6.getSelectedItem().toString();
        c_id5 = c_id5.substring(0, 10);
        Log.e("c_id5", c_id5);
        String s_ID = studentID.getText().toString();
        Log.e("s_ID", s_ID);

        String email_id = email.getText().toString();
        Log.e("email_id", email_id);
        String s_username = user.getText().toString();
        Log.e("s_username", s_username);
        String s_pwd = pass.getText().toString();
        Log.e("s_pwd", s_pwd);

        if (isValidEmail(email_id) && (studentID.length() < 11)) {
            insertToDatabase(c_id, c_id1, c_id2, c_id3, c_id4, c_id5, s_ID, email_id, s_username, s_pwd);
        } else {
            if (!isValidEmail(email_id)) {
                new AlertDialog.Builder(this)
                        .setTitle("Email id")
                        .setMessage("Email ID is invalid")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                email.requestFocus();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
            if (studentID.length() >= 11) {
                new AlertDialog.Builder(this)
                        .setTitle("Student ID")
                        .setMessage("ID must be within length 11")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                email.requestFocus();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
        }
    }

    private void insertToDatabase(String c_id, String c_id1, String c_id2, String c_id3, String c_id4, String c_id5, String s_ID, String email_id, String s_username, String s_pwd) {
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String c_id = params[0];
                String c_id1 = params[1];
                String c_id2 = params[2];
                String c_id3 = params[3];
                String c_id4 = params[4];
                String c_id5 = params[5];
                String s_ID = params[6];
                String email_id = params[7];
                String s_username = params[8];
                String s_pwd = params[9];
                String body = "";

                List<NameValuePair> paramsadd = new ArrayList<NameValuePair>();
                paramsadd.add(new BasicNameValuePair("s_name", s_username));
                paramsadd.add(new BasicNameValuePair("s_pwd", s_pwd));
                paramsadd.add(new BasicNameValuePair("s_ID", s_ID));
                paramsadd.add(new BasicNameValuePair("s_email", email_id));
                paramsadd.add(new BasicNameValuePair("c_ID5", c_id5));
                paramsadd.add(new BasicNameValuePair("c_ID4", c_id4));
                paramsadd.add(new BasicNameValuePair("c_ID3", c_id3));
                paramsadd.add(new BasicNameValuePair("c_ID2", c_id2));
                paramsadd.add(new BasicNameValuePair("c_ID1", c_id1));
                paramsadd.add(new BasicNameValuePair("c_ID", c_id));

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(REGISTRATION_URL);
                    httpPost.setEntity(new UrlEncodedFormEntity(paramsadd));

                    HttpResponse response = httpClient.execute(httpPost);

                    HttpEntity entity = response.getEntity();

                    BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                    while ((body = rd.readLine()) != null) {
                        Log.e("HttpResponse", body);
                    }

                } catch (ClientProtocolException e) {

                } catch (IOException e) {

                }

                return body;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if (result == "") {
                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                } else {
                    Intent ii = new Intent(Registration.this, WelcomeActivity.class);


                    finish();
                    // this finish() method is used to tell android os that we are done with current
                    // activity now! Moving to other activity
                    startActivity(ii);
                }

            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(c_id, c_id1, c_id2, c_id3, c_id4, c_id5, s_ID, email_id, s_username, s_pwd);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


//
//    class AttemptRegister extends AsyncTask<String, String, String>
//    { /** * Before starting background thread Show Progress Dialog * */
//    boolean failure = false;
//        @Override protected void onPreExecute()
//        {
//            super.onPreExecute();
//            pDialog = new ProgressDialog(Registration.this);
//            pDialog.setMessage("Attempting for registration...");
//            pDialog.setIndeterminate(false);
//            pDialog.setCancelable(true);
//            pDialog.show();
//        }
//
//
//
//        @Override protected String doInBackground(String... args)
//        {
//            // here Check for success tag
//            int success;
//
//            try
//            {
//
//                List<NameValuePair> params = new ArrayList<NameValuePair>();
//
//
//                Log.d("request!", "starting");
//                JSONObject json = jsonParser.makeHttpRequest(REGISTRATION_URL, "POST", params);
//                // checking log for json response
//                Log.d("Login attempt", json.toString());
//                // success tag for json
//                success = json.getInt(TAG_SUCCESS);
//                if (success == 1)
//                {
//                    Log.d("Registration done!", json.toString());
//                    Intent ii = new Intent(Registration.this, activity_login.class);
//                    finish();
//                    // this finish() method is used to tell android os that we are done with current
//                    // activity now! Moving to other activity
//                    startActivity(ii);
//
//                    return json.getString(TAG_MESSAGE);
//                } else
//                {
//                    return json.getString(TAG_MESSAGE);
//                }
//
//            }
//            catch (JSONException e)
//            {
//                e.printStackTrace();
//            }
//            return null;
//        }
//        /** * Once the background process is done we need to Dismiss the progress dialog asap * **/
//        protected void onPostExecute(String message)
//        {
//            pDialog.dismiss();
//            if (message != null)
//            {
//                Toast.makeText(Registration.this, message, Toast.LENGTH_LONG).show();
//            }
//
//
//        }
//
//    }

    private class BackTask extends AsyncTask<Void, Void, Void> {
        ArrayList<String> list1, list2, list3, list4, list5, list6;

        protected void onPreExecute() {
            super.onPreExecute();
            list1 = new ArrayList<>();
            list2 = new ArrayList<>();
            list3 = new ArrayList<>();
            list4 = new ArrayList<>();
            list5 = new ArrayList<>();
            list6 = new ArrayList<>();


        }

        protected Void doInBackground(Void... params) {
            InputStream is = null;
            String result = "";
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://frosil.website/API/course_disp.php");
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                // Get our response as a String.
                is = entity.getContent();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //convert response to string
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
                line = null;
                while ((line = reader.readLine()) != null) {
                    result += line;
                }
                is.close();
                //result=sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // parse json data
            try {
                JSONArray jArray = new JSONArray(result);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject jsonObject = jArray.getJSONObject(i);
                    // add interviewee name to arraylist
                    list1.add(jsonObject.getString("co_details"));
                    list2.add(jsonObject.getString("co_details"));
                    list3.add(jsonObject.getString("co_details"));
                    list4.add(jsonObject.getString("co_details"));
                    list5.add(jsonObject.getString("co_details"));
                    list6.add(jsonObject.getString("co_details"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            listItems.addAll(list1);
            listItems1.addAll(list2);
            listItems2.addAll(list3);
            listItems3.addAll(list4);
            listItems4.addAll(list5);
            listItems5.addAll(list6);
            adapter.notifyDataSetChanged();
            adapter1.notifyDataSetChanged();
            adapter2.notifyDataSetChanged();
            adapter3.notifyDataSetChanged();
            adapter4.notifyDataSetChanged();
            adapter5.notifyDataSetChanged();
        }
    }


}
