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
    Object resultString;
    boolean haveConnectedWifi = false;
    boolean haveConnectedMobile = false;
    String network;
    TextView noInternet;
    ListView listView;
    JSONObject jsonResponse;
    JSONArray jsonMainNode;
    SimpleAdapter simpleAdapter;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specialities);

        pd = new ProgressDialog(Specialities.this);
        pd.setMessage("loading");
        noInternet= (TextView) findViewById(R.id.no_internet);
        listView= (ListView) findViewById(R.id.listView1);
        network= String.valueOf(haveNetworkConnection());
        if (network.equals("true")) {
            noInternet.setVisibility(View.GONE);
            AsyncCallWS task = new AsyncCallWS();
            task.execute();
        }else{
            listView.setVisibility(View.GONE);
        }
    }
    private class AsyncCallWS extends AsyncTask<Void, Void, Void> {
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
            simpleAdapter = new SimpleAdapter(getBaseContext(), specialitiesList, android.R.layout.simple_list_item_1, new String[] {"specialitiess"}, new int[] {android.R.id.text1});
            listView.setAdapter(simpleAdapter);
            pd.dismiss();
            ListViewClick();
        }
    }
    List<Map<String,String>> specialitiesList = new ArrayList<Map<String,String>>();
    public void GetJSON() {
        String METHOD_NAME = "Get_Speciality";
        String NAMESPACE = "http://medicarehospital.pk//";
        String URL = "http://medicarehospital.pk/WebService.asmx";
        String SOAP_ACTION = NAMESPACE+METHOD_NAME;
        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            HttpTransportSE transport = new HttpTransportSE(URL);

            transport.call(SOAP_ACTION, soapEnvelope);
            resultString= soapEnvelope.getResponse();
            String jsonString="{\"specialities\":"+resultString.toString()+"}";
            jsonResponse = new JSONObject(jsonString);
            jsonMainNode = jsonResponse.optJSONArray("specialities");
            Log.i(TAG, "Result: " + jsonMainNode);
            for(int i = 0; i<jsonMainNode.length();i++){
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                //String number = jsonChildNode.optString("Id");
                String name = jsonChildNode.optString("Name");
                String outPut = i+1 + ": " +name;
                specialitiesList.add(createspecialities("specialitiess", outPut));
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