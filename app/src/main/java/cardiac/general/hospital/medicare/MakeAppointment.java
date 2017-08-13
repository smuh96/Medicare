package cardiac.general.hospital.medicare;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import android.app.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MakeAppointment extends AppCompatActivity {
    Button SubmitBtn;
    EditText name,mr_no,age,contact,brief_history,choice1,choice2;
    RadioGroup RadioChoice1,RadioChoice2;
    RadioButton RadioChoice11,RadioChoice12,RadioChoice13,RadioChoice21,RadioChoice22,RadioChoice23;
    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;
    String TAG = "Response";
    Object resultSpec,resultCons;
    JSONObject jsonResponseSpec,jsonResponseCons;
    JSONObject jsonChildNodeSpec;
    JSONArray jsonMainNodeSpec,jsonMainNodeCons;
    SimpleAdapter simpleAdapterSpec,simpleAdapterCons;
    Spinner SpecialitySpinner,ConsultantsSpinner;
    List<Map<String,String>> consultantsList = new ArrayList<Map<String,String>>();
    int TreatPos,ConsPos=0;
    String SpecialityString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_appointment);

        SubmitBtn= (Button) findViewById(R.id.AppointmentSubmitBtn);

        name= (EditText) findViewById(R.id.pt_name);
        mr_no= (EditText) findViewById(R.id.pt_mrno);
        age= (EditText) findViewById(R.id.pt_age);
        contact= (EditText) findViewById(R.id.pt_cell);
        brief_history= (EditText) findViewById(R.id.pt_brief);

        RadioChoice1= (RadioGroup) findViewById(R.id.radiochoice1);
        RadioChoice2= (RadioGroup) findViewById(R.id.radiochoice2);

        RadioChoice11= (RadioButton) findViewById(R.id.radiochoice1_1);
        RadioChoice12= (RadioButton) findViewById(R.id.radiochoice1_2);
        RadioChoice13= (RadioButton) findViewById(R.id.radiochoice1_3);
        RadioChoice21= (RadioButton) findViewById(R.id.radiochoice2_1);
        RadioChoice22= (RadioButton) findViewById(R.id.radiochoice2_2);
        RadioChoice23= (RadioButton) findViewById(R.id.radiochoice2_3);

        choice1= (EditText) findViewById(R.id.datechoice1);
        choice2= (EditText) findViewById(R.id.datechoice2);

        SpecialitySpinner= (Spinner) findViewById(R.id.speciality_spinner);
        ConsultantsSpinner= (Spinner) findViewById(R.id.consultants_spinner);

        AsyncCallSpecialities task = new AsyncCallSpecialities();
        task.execute();
        SpecialitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    jsonChildNodeSpec = jsonMainNodeSpec.getJSONObject(position-1);
                    String number = jsonChildNodeSpec.optString("Id");
                    TreatPos= Integer.parseInt(number);
                    //Toast.makeText(MakeAppointment.this, "ID : "+number +"&"+ TreatmentId, Toast.LENGTH_SHORT).show();
                    AsyncCallconsultants task2 = new AsyncCallconsultants();
                    task2.execute();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        choice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateLabel1();
                    }

                };
                new DatePickerDialog(MakeAppointment.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        choice2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateLabel2();
                    }
                };
                new DatePickerDialog(MakeAppointment.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        SubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SpecialitySpinner == null || SpecialitySpinner.getSelectedItem() ==null || SpecialitySpinner.getAdapter()==null ){
                    SpecialityString = (String)SpecialitySpinner.getSelectedItem();
                    Toast.makeText(MakeAppointment.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }else{
                    if(name.getText().toString().equals("")) {
                        Toast.makeText(MakeAppointment.this, "Please Enter Your Full Name", Toast.LENGTH_SHORT).show();
                    }else if(age.getText().toString().equals("")) {
                        Toast.makeText(MakeAppointment.this, "Please Enter Your age", Toast.LENGTH_SHORT).show();
                    }else if(contact.getText().toString().equals("")) {
                        Toast.makeText(MakeAppointment.this, "Please Enter Your Contact #", Toast.LENGTH_SHORT).show();
                    }else if(SpecialitySpinner.getSelectedItem().toString().trim().equals("{specialitiess=Select any Speciality}")) {
                        Toast.makeText(MakeAppointment.this, "Please Select any Speciality", Toast.LENGTH_SHORT).show();
                    }else if(ConsultantsSpinner.getSelectedItem().toString().trim().equals("{consultantss=Select any Consultant}")){
                        Toast.makeText(MakeAppointment.this, "Please Select any Consultant", Toast.LENGTH_SHORT).show();
                    }else if(choice1.getText().toString().equals("")) {
                        Toast.makeText(MakeAppointment.this, "Please Enter 1st choice Date", Toast.LENGTH_SHORT).show();
                    }else if(getRadioChoice1()==null) {
                        Toast.makeText(MakeAppointment.this, "Please Choose 1st choice Timing", Toast.LENGTH_SHORT).show();
                    }/*else if(getRadioChoice2()==null) {
                Toast.makeText(MakeAppointment.this, "Please Choose 2nd choice Timings", Toast.LENGTH_SHORT).show();
            }*/else{
                        Intent mEmail = new Intent(Intent.ACTION_SEND);
                        mEmail.putExtra(Intent.EXTRA_EMAIL, new String[]{"info@medicarehospital.pk"});
                        mEmail.putExtra(Intent.EXTRA_SUBJECT, "subject");
                        mEmail.putExtra(Intent.EXTRA_TEXT, "message"+choice2.getText());
                        // prompts to choose email client
                        mEmail.setType("message/rfc822");
                        startActivity(Intent.createChooser(mEmail, "Choose an email client to send your"));
                    }
                }
            }
        });
    }
    private class AsyncCallSpecialities extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() { Log.i(TAG, "onPreExecute"); }

        @Override
        protected Void doInBackground(Void... params) { Log.i(TAG, "doInBackground");
            GetSpecJSON();
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");
            simpleAdapterSpec = new SimpleAdapter(getBaseContext(), specialitiesList, android.R.layout.simple_list_item_1, new String[] {"specialitiess"}, new int[] {android.R.id.text1});
            simpleAdapterSpec.notifyDataSetChanged();
            SpecialitySpinner.setAdapter(simpleAdapterSpec);
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            if (bundle!= null) {// to avoid the NullPointerException
                TreatPos = bundle.getInt("SpeciePos");
                SpecialitySpinner.setSelection(TreatPos+1);
                SpecialitySpinner.setClickable(false);
                SpecialitySpinner.setEnabled(false);

            }else{
                SpecialitySpinner.setSelection(TreatPos);
            }
        }
    }
    private class AsyncCallconsultants extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() { Log.i(TAG, "onPreExecute"); }

        @Override
        protected Void doInBackground(Void... params) { Log.i(TAG, "doInBackground");
            consultantsList.clear();
            GetconsultantJSON();
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");
            simpleAdapterCons = new SimpleAdapter(getBaseContext(), consultantsList, android.R.layout.simple_list_item_1, new String[] {"consultantss"}, new int[] {android.R.id.text1});
            simpleAdapterCons.notifyDataSetChanged();
            ConsultantsSpinner.setAdapter(simpleAdapterCons);
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            if (bundle!= null) {// to avoid the NullPointerException
                ConsPos = bundle.getInt("ConsPos");
                ConsultantsSpinner.setSelection(ConsPos+1);
                ConsultantsSpinner.setClickable(false);
                ConsultantsSpinner.setEnabled(false);

            }else{
                ConsultantsSpinner.setSelection(ConsPos);
            }
        }
    }
    private void updateLabel1() {
        String myFormat = "dd MMM, yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        choice1.setText(sdf.format(myCalendar.getTime()));
    }
    private void updateLabel2() {
        String myFormat = "dd MMM, yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        choice2.setText(sdf.format(myCalendar.getTime()));
    }

    List<Map<String,String>> specialitiesList = new ArrayList<Map<String,String>>();
    public void GetSpecJSON() {
        String METHOD_NAME = "Get_FindAConsultant";
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
            resultSpec= soapEnvelope.getResponse();
            //String result= "[{\"Id\":\"3\",\"Name\":\"Cardiology And Cardiothoracic Surgery\"},{\"Id\":\"32\",\"Name\":\"Clinical Nutrition And Dietetics\"},{\"Id\":\"9\",\"Name\":\"Dental Surgery\"},{\"Id\":\"19\",\"Name\":\"Dermatology And Cosmetology\"},{\"Id\":\"23\",\"Name\":\"Dialysis\"},{\"Id\":\"6\",\"Name\":\"E.N.T\"},{\"Id\":\"10\",\"Name\":\"Endocrinology\"},{\"Id\":\"20\",\"Name\":\"Eye\"},{\"Id\":\"18\",\"Name\":\"Gastroenterology\"},{\"Id\":\"11\",\"Name\":\"General Surgery\"},{\"Id\":\"39\",\"Name\":\"Maxillo Facial\"},{\"Id\":\"12\",\"Name\":\"Medicine\"},{\"Id\":\"13\",\"Name\":\"Nephrology\"},{\"Id\":\"26\",\"Name\":\"Neurology\"},{\"Id\":\"14\",\"Name\":\"Neurosurgery\"},{\"Id\":\"15\",\"Name\":\"Obstetrics and Gynaecology\"},{\"Id\":\"29\",\"Name\":\"Oncology\"},{\"Id\":\"7\",\"Name\":\"Orthopedics\"},{\"Id\":\"16\",\"Name\":\"Pediatrics And Neonatology\"},{\"Id\":\"37\",\"Name\":\"Plastic And Reconstructive Surgery\"},{\"Id\":\"17\",\"Name\":\"Psychiatry\"},{\"Id\":\"25\",\"Name\":\"Pulmonology\"},{\"Id\":\"24\",\"Name\":\"Rheumatology\"},{\"Id\":\"5\",\"Name\":\"Urology\"}]";
            String jsonString="{\"specialities\":"+resultSpec.toString()+"}";
            jsonResponseSpec = new JSONObject(jsonString);
            jsonMainNodeSpec = jsonResponseSpec.optJSONArray("specialities");
            Log.i(TAG, "Result: " + jsonMainNodeSpec);
            for(int i = 0; i<jsonMainNodeSpec.length();i++){
                jsonChildNodeSpec = jsonMainNodeSpec.getJSONObject(i);
                //String number = jsonChildNode.optString("Id");
                String name = jsonChildNodeSpec.optString("Name");
                specialitiesList.add(createspecialities("specialitiess", name));
                Log.d(TAG, "OutPut: " + name);
            }
            specialitiesList.add(0, (createspecialities("specialitiess", "Select any Speciality")));
        } catch (Exception ex) {
            Log.e(TAG, "Error: " + ex.getMessage());
        }
    }
    private HashMap<String, String> createspecialities(String name, String number){
        HashMap<String, String> specialitiesNameNo = new HashMap<String, String>();
        specialitiesNameNo.put(name, number);
        return specialitiesNameNo;
    }
    public void GetconsultantJSON() {
        String METHOD_NAME = "Get_ConultantDetails";
        String NAMESPACE = "http://medicarehospital.pk//";
        String URL = "http://medicarehospital.pk/WebService.asmx";
        String SOAP_ACTION = NAMESPACE+METHOD_NAME;
        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("TreatmentId", TreatPos);

            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            HttpTransportSE transport = new HttpTransportSE(URL);

            transport.call(SOAP_ACTION, soapEnvelope);
            resultCons= soapEnvelope.getResponse();
            String jsonString="{\"consultants\":"+resultCons.toString()+"}";
            jsonResponseCons = new JSONObject(jsonString);
            jsonMainNodeCons = jsonResponseCons.optJSONArray("consultants");
            Log.i(TAG, "Result: " + jsonMainNodeCons);
            for(int i = 0; i<jsonMainNodeCons.length();i++){
                JSONObject jsonChildNode = jsonMainNodeCons.getJSONObject(i);
                //String number = jsonChildNode.optString("Id");
                String name = jsonChildNode.optString("Name");
                consultantsList.add(createconsultants("consultantss", name));
                Log.d(TAG, "OutPut: " + name);
            }
            consultantsList.add(0,createconsultants("consultantss", "Select any Consultant"));
        } catch (Exception ex) {
            Log.e(TAG, "Error: " + ex.getMessage());
        }
    }
    private HashMap<String, String> createconsultants(String name, String number){
        HashMap<String, String> consultantsNameNo = new HashMap<String, String>();
        consultantsNameNo.put(name, number);
        return consultantsNameNo;
    }
    private String getRadioChoice1(){
        if (RadioChoice11.isChecked()){return RadioChoice11.getText().toString();
        }if (RadioChoice12.isChecked()){return RadioChoice12.getText().toString();
        }if (RadioChoice13.isChecked()){return RadioChoice13.getText().toString();
        }return null;
    }
    private String getRadioChoice2(){
        if (RadioChoice21.isChecked()){return RadioChoice21.getText().toString();
        }if (RadioChoice22.isChecked()){return RadioChoice22.getText().toString();
        }if (RadioChoice23.isChecked()){return RadioChoice23.getText().toString();
        }return null;
    }
}
