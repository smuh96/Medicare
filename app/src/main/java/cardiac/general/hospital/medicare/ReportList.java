package cardiac.general.hospital.medicare;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportList extends AppCompatActivity {

    private static final String TAG = "Response";
    int TransNo,PinNo=0;
    SimpleAdapter simpleAdapter;
    ProgressDialog pd;
    ListView listView;
    boolean haveConnectedWifi = false;
    boolean haveConnectedMobile = false;
    String network;
    TextView noInternet;
    String jsonStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_list);
        Bundle bundle = getIntent().getExtras();
        TransNo = bundle.getInt("TransNumber");
        PinNo = bundle.getInt("PinNumber");

        pd = new ProgressDialog(ReportList.this);
        pd.setMessage("loading");
        pd.show();
        noInternet= (TextView) findViewById(R.id.no_internet);
        listView= (ListView) findViewById(R.id.reportsNameListView);
        network= String.valueOf(haveNetworkConnection());
        if (network.equals("true")) {
            noInternet.setVisibility(View.GONE);
            GetReportName task = new GetReportName();
            task.execute();
        }else{
            listView.setVisibility(View.GONE);
        }
    }
    private class GetReportName extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
        }
        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            GetJSON();
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");
            simpleAdapter = new SimpleAdapter(getBaseContext(), reportsNameList,R.layout.single_list_row, new String[] {"reportNamess"}, new int[] {android.R.id.text1});
            if (reportsNameList.isEmpty()){
                listView.setVisibility(View.GONE);
                noInternet.setVisibility(View.VISIBLE);
            }else{
                listView.setAdapter(simpleAdapter);
                listView.setEmptyView(findViewById(R.id.no_data));
            }
            pd.dismiss();
            ListViewClick();
        }
    }

    List<Map<String,String>> reportsNameList = new ArrayList<Map<String,String>>();
    public void GetJSON() {
        HttpHandler sh = new HttpHandler();
        // Making a request to url and getting response
        String url = "http://local.jmc.edu.pk:1133/ReportHandler.ashx?TransNo="+TransNo+"&PinNo="+PinNo;
        jsonStr = sh.makeServiceCall(url);
        if (jsonStr != null) {
            try {
                String jsonString = "{\"reportsName\":" + jsonStr+ "}";
                JSONObject jsonObj = new JSONObject(jsonString);
                // Getting JSON Array node
                JSONArray reportsName = jsonObj.getJSONArray("reportsName");
                // looping through All reportsName
                for (int i = 0; i < reportsName.length(); i++) {
                    JSONObject c = reportsName.getJSONObject(i);
                    int serialNo=i+1;
                    String reportName = c.getString("LTEST_DESC");
                    Log.d(TAG, reportName);
                    reportsNameList.add(reportNames("reportNamess", serialNo+"    "+reportName));
                }
            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Data not available " + e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
            }
        } else {
            Toast.makeText(this, "Couldn't get json from server.", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Couldn't get json from server.");            
        }
    }
    private HashMap<String, String> reportNames(String name, String number){
        HashMap<String, String> reportName = new HashMap<String, String>();
        reportName.put(name, number);
        return reportName;
    }
    public void ListViewClick() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String jsonString = "{\"reportsName\":" + jsonStr+ "}";
                    JSONObject jsonObj = new JSONObject(jsonString);
                    // Getting JSON Array node
                    JSONArray reportsName = jsonObj.getJSONArray("reportsName");
                    // looping through All reportsName
                        JSONObject c = reportsName.getJSONObject(position);
                        String reportName = c.getString("LTEST_DESC");
                        int reportID = c.getInt("LTEST_ID");
                    Intent intent = new Intent(ReportList.this, ViewReport.class);
                    intent.putExtra("Report_Name", reportName);
                    intent.putExtra("Report_ID", reportID);
                    intent.putExtra("Report_Trans", TransNo);
                    intent.putExtra("Report_Pn", PinNo);
                    startActivity(intent);
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"Data not available " + e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }
    private boolean haveNetworkConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
}