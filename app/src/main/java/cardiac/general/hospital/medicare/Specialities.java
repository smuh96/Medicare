package cardiac.general.hospital.medicare;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

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

    boolean haveConnectedWifi = false;
    boolean haveConnectedMobile = false;
    String network;

    String TAG = "Response";
    Object resultString;
    String json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specialities);

        TextView noInternet= (TextView) findViewById(R.id.no_internet);
        ListView listView = (ListView) findViewById(R.id.listView2);
        network= String.valueOf(haveNetworkConnection());
        if (network.equals("true")){
            noInternet.setVisibility(View.GONE);
            AsyncCallWS task = new AsyncCallWS();
            task.execute();
            SimpleAdapter simpleAdapter = new SimpleAdapter(this, specilitiesList, android.R.layout.simple_list_item_1, new String[] {"specilities"}, new int[] {android.R.id.text1});
            listView.setAdapter(simpleAdapter);
        }else{
            listView.setVisibility(View.GONE);
        }
        Log.i(TAG, "Internet"+network);
    }
    private class AsyncCallWS extends AsyncTask<Void, Void, Void> {

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
            Toast.makeText(Specialities.this, ""+json, Toast.LENGTH_LONG).show();
        }
    }

    List<Map<String,String>> specilitiesList = new ArrayList<Map<String,String>>();
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
            json=  "{\"specilities\":"+resultString.toString()+"}";
            Log.i(TAG, "JsonString: " + json);
            try{
                JSONObject jsonResponse = new JSONObject(json);
                JSONArray jsonMainNode = jsonResponse.optJSONArray("specilities");
                Log.i(TAG, "ObjectandArray: " + jsonResponse+" **** "+jsonMainNode);
                for(int i = 0; i<jsonMainNode.length();i++){
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    String name = jsonChildNode.optString("Id");
                    String number = jsonChildNode.optString("Name");
                    String outPut = name + ": " +number;
                    Log.i(TAG, "result: " + outPut);
                    specilitiesList.add(createEmployee("specilities", outPut));
                }
            }
            catch(JSONException e){
                Toast.makeText(getApplicationContext(), "Error"+e.toString(), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            Log.e(TAG, "Error: " + ex.getMessage());
        }
    }
    private HashMap<String, String> createEmployee(String name, String number){
        HashMap<String, String> employeeNameNo = new HashMap<String, String>();
        employeeNameNo.put(name, number);
        return employeeNameNo;
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