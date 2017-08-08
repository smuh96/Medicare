package cardiac.general.hospital.medicare;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public class SpecialityDetails extends AppCompatActivity {

    String TAG = "Response";
    Object resultString;
    ImageView cover;
    JSONObject jsonResponse;
    JSONArray jsonMainNode;
    String url;
    String id;
    TextView specialityName;
    String name;
    TextView Heading1,Heading2,Heading3,Heading4,Heading5;
    TextView Paragraph1,Paragraph2,Paragraph3,Paragraph4,Paragraph5;
    String heading1,heading2,heading3,heading4,heading5;
    String paragraph1,paragraph2,paragraph3,paragraph4,paragraph5;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speciality_details);
        pd = new ProgressDialog(SpecialityDetails.this);
        pd.setMessage("loading");
        pd.show();
        cover = (ImageView) findViewById(R.id.coverPic);
        specialityName= (TextView) findViewById(R.id.specialityname);
        Heading1= (TextView) findViewById(R.id.head1);
        Heading2= (TextView) findViewById(R.id.head2);
        Heading3= (TextView) findViewById(R.id.head3);
        Heading4= (TextView) findViewById(R.id.head4);
        Heading5= (TextView) findViewById(R.id.head5);
        Paragraph1= (TextView) findViewById(R.id.para1);
        Paragraph2= (TextView) findViewById(R.id.para2);
        Paragraph3= (TextView) findViewById(R.id.para3);
        Paragraph4= (TextView) findViewById(R.id.para4);
        Paragraph5= (TextView) findViewById(R.id.para5);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        id = bundle.getString("ID");
        AsyncCallWS task = new AsyncCallWS();
        ImageLoadTask task2 = new ImageLoadTask(url, cover);
        task.execute();
        task2.execute();
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
            //Toast.makeText(SpecialityDetails.this, "" + outPut, Toast.LENGTH_LONG).show();

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
            specialityName.setText(name);
            setHeadPara1();
            setHeadPara2();
            setHeadPara3();
            setHeadPara4();
            setHeadPara5();
            pd.dismiss();
        }
    }
    public void GetJSON() {
        String METHOD_NAME = "Get_SpecialityDetails";
        String NAMESPACE = "http://medicarehospital.pk//";
        String URL = "http://medicarehospital.pk/WebService.asmx";
        String SOAP_ACTION = NAMESPACE+METHOD_NAME;
        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("TreatmentId",id);
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
            JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
            String pic = jsonChildNode.optString("Picture");
            url = pic.replaceAll("~", "http://medicarehospital.pk/");
            Log.e(TAG, "URL: " + url);
            name = jsonChildNode.optString("Name");
            heading1 = jsonChildNode.optString("Head1");
            paragraph1 = jsonChildNode.optString("Headpara1");
            heading2 = jsonChildNode.optString("Head2");
            paragraph2 = jsonChildNode.optString("Headpara2");
            heading3 = jsonChildNode.optString("Head3");
            paragraph3 = jsonChildNode.optString("Headpara3");
            heading4 = jsonChildNode.optString("Head4");
            paragraph4 = jsonChildNode.optString("Headpara4");
            heading5 = jsonChildNode.optString("Head5");
            paragraph5 = jsonChildNode.optString("Headpara5");
        } catch (Exception ex) {
            Log.e(TAG, "Error: " + ex.getMessage());
        }
    }
    public void setHeadPara1(){
        if (!(heading1.equals(""))){
            Heading1.setText(heading1);
            Paragraph1.setText(paragraph1);
        }else {
            Heading1.setVisibility(View.GONE);
            Paragraph1.setVisibility(View.GONE);
        }
    }
    public void setHeadPara2(){
        if (!(heading2.equals(""))){
            Heading2.setText(heading2);
            Paragraph2.setText(paragraph2);
        }else {
            Heading2.setVisibility(View.GONE);
            Paragraph2.setVisibility(View.GONE);
        }
    }
    public void setHeadPara3(){
        if (!(heading3.equals(""))){
            Heading3.setText(heading3);
            Paragraph3.setText(paragraph3);
        }else {
            Heading3.setVisibility(View.GONE);
            Paragraph3.setVisibility(View.GONE);
        }
    }
    public void setHeadPara4(){
        if (!(heading4.equals(""))){
            Heading4.setText(heading4);
            Paragraph4.setText(paragraph4);
        }else {
            Heading4.setVisibility(View.GONE);
            Paragraph4.setVisibility(View.GONE);
        }
    }
    public void setHeadPara5(){
        if (!(heading5.equals(""))){
            Heading5.setText(heading5);
            Paragraph5.setText(paragraph5);
        }else {
            Heading5.setVisibility(View.GONE);
            Paragraph5.setVisibility(View.GONE);
        }
    }
}
