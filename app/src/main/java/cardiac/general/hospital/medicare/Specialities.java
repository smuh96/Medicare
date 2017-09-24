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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Specialities extends AppCompatActivity {

    String TAG = "Response";
    String jsonStr;
    boolean haveConnectedWifi = false;
    boolean haveConnectedMobile = false;
    String network;
    TextView noInternet;
    ListView listView;
    SimpleAdapter simpleAdapter;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specialities);

        pd = new ProgressDialog(Specialities.this);
        pd.setMessage("loading");
        pd.show();
        noInternet= (TextView) findViewById(R.id.no_internet);
        listView= (ListView) findViewById(R.id.listView1);
        network= String.valueOf(haveNetworkConnection());
        if (network.equals("true")) {
            noInternet.setVisibility(View.GONE);
            AsyncCallSpecialities task = new AsyncCallSpecialities();
            task.execute();
        }else{
            listView.setVisibility(View.GONE);
        }
    }
    private class AsyncCallSpecialities extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() { Log.i(TAG, "onPreExecute"); }

        @Override
        protected Void doInBackground(Void... params) { Log.i(TAG, "doInBackground");
            GetJSON();
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");
            //Toast.makeText(MainActivity.this, "" + outPut, Toast.LENGTH_LONG).show();
            simpleAdapter = new SimpleAdapter(getBaseContext(), specialitiesList,R.layout.single_list_row, new String[] {"specialitiess"}, new int[] {android.R.id.text1});
            listView.setAdapter(simpleAdapter);
            pd.dismiss();
            listView.setEmptyView(findViewById(R.id.no_data));
            ListViewClick();
        }
    }
    List<Map<String,String>> specialitiesList = new ArrayList<Map<String,String>>();
    public void GetJSON() {
        HttpHandler sh = new HttpHandler();
        String URL = "http://medicarehospital.pk/SpecialityHandler.ashx";
        jsonStr = sh.makeServiceCall(URL);
        Log.d(TAG,URL+jsonStr);
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            // Getting JSON Array node
            JSONArray jsonMainNode = jsonObj.getJSONArray("Specialitydata");
            for(int i = 0; i<jsonMainNode.length();i++){
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                //String number = jsonChildNode.optString("Id");
                String name = jsonChildNode.optString("Name");
                specialitiesList.add(createspecialities("specialitiess", name));
            }
        } catch (Exception ex) {
            Log.e(TAG, "Error: " + ex.getMessage());
        }
    }
    private HashMap<String, String> createspecialities(String name, String number){
        HashMap<String, String> specialitiesNameNo = new HashMap<String, String>();
        specialitiesNameNo.put(name, number);
        return specialitiesNameNo;
    }
    public void ListViewClick() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    // Getting JSON Array node
                    JSONArray jsonMainNode = jsonObj.getJSONArray("Specialitydata");
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(position);
                    String number = jsonChildNode.optString("Id");
                    //Toast.makeText(Specialities.this, "ID : "+number, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Specialities.this, SpecialityDetails.class);
                    intent.putExtra("ID", number);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
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