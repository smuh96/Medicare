package cardiac.general.hospital.medicare;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DoctorDetail extends AppCompatActivity {
    String TAG = "Response";
    Object resultString;
    ImageView cover;
    JSONObject jsonResponse;
    JSONArray jsonMainNode;
    String url;
    String id;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_detail);
        pd = new ProgressDialog(DoctorDetail.this);
        pd.setMessage("loading");
        pd.show();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        id = bundle.getString("DocId");
        AsyncCallDoctorDetail task = new AsyncCallDoctorDetail();
        task.execute();
    }

    private class AsyncCallDoctorDetail extends AsyncTask<Void, Void, Void> {
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
            pd.dismiss();
            Toast.makeText(DoctorDetail.this, "" + jsonMainNode, Toast.LENGTH_LONG).show();

        }
    }
    private class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {
        ImageLoadTask(String src, ImageView imageView) {
            url = src;
            cover = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            cover.setImageBitmap(result);
        }
    }
    public void GetJSON() {
        String METHOD_NAME = "Get_DoctorDetail";
        String NAMESPACE = "http://medicarehospital.pk//";
        String URL = "http://medicarehospital.pk/WebService.asmx";
        String SOAP_ACTION = NAMESPACE+METHOD_NAME;
        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("DocId",id);
            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);
            HttpTransportSE transport = new HttpTransportSE(URL);
            transport.call(SOAP_ACTION, soapEnvelope);
            resultString= soapEnvelope.getResponse();

            String jsonString="{\"specialities\":"+resultString.toString()+"}";
            jsonResponse = new JSONObject(jsonString);
            jsonMainNode = jsonResponse.optJSONArray("specialities");
            Log.i(TAG, "ABCD: " + jsonMainNode);/*
            JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
            String pic = jsonChildNode.optString("Picture");
            url = pic.replaceAll("~", "http://medicarehospital.pk/");
            Log.d(TAG, "URL: " + url);
            name = jsonChildNode.optString("Name");*/
        } catch (Exception ex) {
            Log.e(TAG, "Error: " + ex.getMessage());
        }
    }
}
