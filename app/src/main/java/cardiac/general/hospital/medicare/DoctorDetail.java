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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
    ImageView DoctorPicture;
    JSONObject jsonResponse;
    JSONArray jsonMainNode;
    String url,id;
    String Name,Degree,Speciality,Clinicdays1,Timing1,Clinicdays2,Timing2,AppointmentContact,Introduction,PictureAddress,TreatId,TreatName;
    TextView DoctorName,DoctorSpeciality,DoctorDegree,DoctorDays1,DoctorTime1,DoctorDays2,DoctorTime2,DoctorContact,DoctorIntro;
    Button AppointmentBtn;
    ProgressDialog pd;
    int SpeciPos,ConsPos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_detail);
        DoctorPicture= (ImageView) findViewById(R.id.doctorPic);
        DoctorName= (TextView) findViewById(R.id.doctorname);
        DoctorSpeciality= (TextView) findViewById(R.id.doctorpeciality);
        DoctorDegree= (TextView) findViewById(R.id.doctordegree);
        DoctorDays1= (TextView) findViewById(R.id.doctordays1);
        DoctorTime1= (TextView) findViewById(R.id.doctortime1);
        DoctorDays2= (TextView) findViewById(R.id.doctordays2);
        DoctorTime2= (TextView) findViewById(R.id.doctortime2);
        DoctorContact= (TextView) findViewById(R.id.doctorcontact);
        DoctorIntro= (TextView) findViewById(R.id.doctorintro);
        AppointmentBtn= (Button) findViewById(R.id.doctorAppointmentBtn);
        pd = new ProgressDialog(DoctorDetail.this);
        pd.setMessage("loading");
        pd.show();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        id = bundle.getString("DocId");
        SpeciPos = bundle.getInt("SpeciPos");
        ConsPos = bundle.getInt("ConsPos");
        //Toast.makeText(this, ""+SpeciPos+"&"+ConsPos, Toast.LENGTH_SHORT).show();
        AsyncCallDoctorDetail task = new AsyncCallDoctorDetail();
        ImageLoadTask task2 = new ImageLoadTask(url, DoctorPicture);
        task.execute();
        task2.execute();
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
            DoctorName.setText(Name);
            DoctorSpeciality.setText(Speciality);
            DoctorDegree.setText(Degree);
            DoctorDays1.setText(Clinicdays1);
            DoctorTime1.setText(Timing1);
            DoctorDays2.setText(Clinicdays2);
            DoctorTime2.setText(Timing2);
            DoctorContact.setText("Appointments: "+AppointmentContact);
            DoctorIntro.setText(Introduction);
            AppointmentBtn.setVisibility(View.VISIBLE);
            //Toast.makeText(DoctorDetail.this, "" + jsonMainNode, Toast.LENGTH_LONG).show();
            Log.d(TAG, "OutPut : " +jsonMainNode);
            AppointmentBtnClick();
        }
    }
    private class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {
        ImageLoadTask(String src, ImageView imageView) {
            url = src;
            DoctorPicture = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection.openConnection();
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
            DoctorPicture.setImageBitmap(result);
            pd.dismiss();
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
            JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
            Name = jsonChildNode.optString("Name");
            Degree = jsonChildNode.optString("Degree");
            Speciality = jsonChildNode.optString("Speciality");
            Clinicdays1 = jsonChildNode.optString("Clinicdays1");
            Timing1 = jsonChildNode.optString("Timing1");
            Clinicdays2 = jsonChildNode.optString("Clinicdays2");
            Timing2 = jsonChildNode.optString("Timing2");
            AppointmentContact = jsonChildNode.optString("AppointmentContact");
            Introduction = jsonChildNode.optString("Introduction");
            PictureAddress = jsonChildNode.optString("PictureAddress");
            url = PictureAddress.replaceAll("~", "http://medicarehospital.pk/");
            url = url.replaceAll("%", "");
            url = url.replaceAll(" ", "");
            TreatId = jsonChildNode.optString("TreatId");
            TreatName = jsonChildNode.optString("TreatName");

            Log.d(TAG, "URL: " + url);
        } catch (Exception ex) {
            Log.e(TAG, "Error: " + ex.getMessage());
        }
    }
    public void AppointmentBtnClick() {
        AppointmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoctorDetail.this, MakeAppointment.class);
                intent.putExtra("ConsPos", ConsPos);
                intent.putExtra("SpeciePos", SpeciPos);
                startActivity(intent);
            }
        });
    }
}
