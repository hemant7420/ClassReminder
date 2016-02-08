package website.frosil.hemant.classreminder;

/* # CSIT 6000B    #  HEMANT SINGHI        20294499          hssinghi@connect.ust.hk */

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class OtherActRemServ {

    private final String remote = "http://frosil.website/API/r_disp_day.php";
    String value = "", days = "";

    public OtherActRemServ(String day, String s_ID) {
        value = s_ID;
        days = day;
    }

    public String getLastItems() throws ClientProtocolException, IOException, JSONException {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(remote);
        List nameValuePairs = new ArrayList(2);
        nameValuePairs.add(new BasicNameValuePair("s_username", value));
        nameValuePairs.add(new BasicNameValuePair("weekDay", days));

        post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        HttpResponse response = client.execute(post);

        HttpEntity entity = response.getEntity();
        BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

}
