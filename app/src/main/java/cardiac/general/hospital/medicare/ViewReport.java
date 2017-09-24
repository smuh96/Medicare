package cardiac.general.hospital.medicare;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ViewReport extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "Response";
    Button btn_generate;
    TextView TransitionNo,MRNo,PatientName,PatientGender,PatientAge,CollectionDate,Panel,PatientConsaltant,PatientDepartment, PatientLaboratoryNo;
    TextView ReportName,TestDescription;
    TextView Variable1,Variable2,Variable3,Variable4,Variable5,Variable6,Variable7,Variable8,Variable9,Variable10,Variable11,Variable12;
    TextView Variable1Result,Variable2Result,Variable3Result,Variable4Result,Variable5Result,Variable6Result,Variable7Result,Variable8Result,Variable9Result,Variable10Result,Variable11Result,Variable12Result;
    TextView Variable1Normal,Variable2Normal,Variable3Normal,Variable4Normal,Variable5Normal,Variable6Normal,Variable7Normal,Variable8Normal,Variable9Normal,Variable10Normal,Variable11Normal,Variable12Normal;
    LinearLayout ll_pdflayout;
    public static int REQUEST_PERMISSIONS = 1;
    boolean boolean_permission;
    boolean boolean_save;
    Bitmap bitmap;
    ProgressDialog progressDialog;
    String jsonStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report);
        initVariables();
        AsyncCallReportPDF task = new AsyncCallReportPDF();
        task.execute();
        init();
        fn_permission();
        listener();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String reportID = bundle.getString("Report_ID");
        //Toast.makeText(this, ""+reportID, Toast.LENGTH_SHORT).show();
    }
    private void initVariables(){
        //Personal Information
        TransitionNo= (TextView) findViewById(R.id.transtionNo);
        MRNo= (TextView) findViewById(R.id.mrNo);
        PatientName= (TextView) findViewById(R.id.patientName);
        PatientGender= (TextView) findViewById(R.id.gender);
        PatientAge= (TextView) findViewById(R.id.patientAge);
        CollectionDate= (TextView) findViewById(R.id.collectionDate);
        Panel= (TextView) findViewById(R.id.panel);
        PatientConsaltant= (TextView) findViewById(R.id.consaltant);
        PatientDepartment= (TextView) findViewById(R.id.department);
        PatientLaboratoryNo = (TextView) findViewById(R.id.laboratoryNo);

        //Report Information
        ReportName= (TextView) findViewById(R.id.reportName);
        TestDescription= (TextView) findViewById(R.id.testDescription);

        //Variable Names
        Variable1= (TextView) findViewById(R.id.variable1);
        Variable2= (TextView) findViewById(R.id.variable2);
        Variable3= (TextView) findViewById(R.id.variable3);
        Variable4= (TextView) findViewById(R.id.variable4);
        Variable5= (TextView) findViewById(R.id.variable5);
        Variable6= (TextView) findViewById(R.id.variable6);
        Variable7= (TextView) findViewById(R.id.variable7);
        Variable8= (TextView) findViewById(R.id.variable8);
        Variable9= (TextView) findViewById(R.id.variable9);
        Variable10= (TextView) findViewById(R.id.variable10);
        Variable11= (TextView) findViewById(R.id.variable11);

        //variable Result
        Variable1Result= (TextView) findViewById(R.id.variable1Result);
        Variable2Result= (TextView) findViewById(R.id.variable2Result);
        Variable3Result= (TextView) findViewById(R.id.variable3Result);
        Variable4Result= (TextView) findViewById(R.id.variable4Result);
        Variable5Result= (TextView) findViewById(R.id.variable5Result);
        Variable6Result= (TextView) findViewById(R.id.variable6Result);
        Variable7Result= (TextView) findViewById(R.id.variable7Result);
        Variable8Result= (TextView) findViewById(R.id.variable8Result);
        Variable9Result= (TextView) findViewById(R.id.variable9Result);
        Variable10Result= (TextView) findViewById(R.id.variable10Result);
        Variable11Result= (TextView) findViewById(R.id.variable11Result);

        //variable Normal
        Variable1Normal= (TextView) findViewById(R.id.variable1Normal);
        Variable2Normal= (TextView) findViewById(R.id.variable2Normal);
        Variable3Normal= (TextView) findViewById(R.id.variable3Normal);
        Variable4Normal= (TextView) findViewById(R.id.variable4Normal);
        Variable5Normal= (TextView) findViewById(R.id.variable5Normal);
        Variable6Normal= (TextView) findViewById(R.id.variable6Normal);
        Variable7Normal= (TextView) findViewById(R.id.variable7Normal);
        Variable8Normal= (TextView) findViewById(R.id.variable8Normal);
        Variable9Normal= (TextView) findViewById(R.id.variable9Normal);
        Variable10Normal= (TextView) findViewById(R.id.variable10Normal);
        Variable11Normal= (TextView) findViewById(R.id.variable11Normal);
    }
    private class AsyncCallReportPDF extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() { Log.i("Test", "onPreExecute"); }
        @Override
        protected Void doInBackground(Void... params) { Log.i("Test", "doInBackground");
            GetJSON();
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            Log.i("Test", "onPostExecute");
        }
    }
    public void GetJSON() {
        HttpHandler sh = new HttpHandler();
        String url = "http://medicarehospital.pk/Jsonfiles/OnlineReportJson.txt";
        //jsonStr = sh.makeServiceCall(url);
        String resultString="[{\"TransactionNo\":\"11\",\"MRNo\":\"0\",\"PatientName\":\"MRS. HAMEEDA\",\"Sex\":\"FEMALE\",\"Age\":\"50 Years\",\"CollectionDate\":\"20-APR-13\",\"Panel\":\"CASH PATIENT (MC)\",\"Consultant\":\"DENTAL OPD\",\"Department\":\"DENTAL DEPARTMENT\",\"LaboratoryNo\":\"Null\",\"ReportName\":\"HAEMATOLOGY\",\"TestDescription\":\"COMPLETE BLOOD PICTURE (CP)\",\"Variable1\":\"HAEMOGLOBIN\",\"Variable1Result\":\"13.0\",\"Variable1Normal\":\"Female : 11.1 - 14.5\",\"Variable2\":\"R.B.C. COUNT\",\"Variable2Result\":\"4.51\",\"Variable2Normal\":\"Female : 3.9 - 5.5\",\"Variable3\":\"HAEMATOCRIT(P.C.V.)\",\"Variable3Result\":\"40\",\"Variable3Normal\":\"Female : 35.4 - 42.0\",\"Variable4\":\"M.C.V.\",\"Variable4Result\":\"89\",\"Variable4Normal\":\"76.0 - 96.0\",\"Variable5\":\"M.C.H.\",\"Variable5Result\":\"29\",\"Variable5Normal\":\"26 - 32\",\"Variable6\":\"M.C.H.C.\",\"Variable6Result\":\"32\",\"Variable6Normal\":\"32 - 36\",\t\t\"Variable7\":\"WBC COUNT\",\"Variable7Result\":\"7,800\",\"Variable7Normal\":\"4.0 - 11.0\",\"Variable8\":\"NEUTROPHILS\",\"Variable8Result\":\"57\",\"Variable8Normal\":\"40 - 75\",\"Variable9\":\"LYMPHOCYTES\",\"Variable9Result\":\"38\",\"Variable9Normal\":\"20 - 45\",\"Variable10\":\"EOSINOPHILS\",\"Variable10Result\":\"03\",\"Variable10Normal\":\"1-6\",\t\"Variable11\":\"MONOCYTES\",\"Variable11Result\":\"02\",\"Variable11Normal\":\"2-8\",\"Variable12\":\"PLATELET COUNT\",\"Variable8Result\":\"158,000\",\"Variable8Normal\":\"150 - 450\"}]";
        String jsonString="{\"ReportPDF\":"+resultString+"}";
        Log.d(TAG, url + jsonStr);
        try {
            JSONObject jsonObj = new JSONObject(jsonString);
            // Getting JSON Array node
            JSONArray jsonMainNode = jsonObj.getJSONArray("ReportPDF");
            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                TransitionNo.setText(jsonChildNode.optString("TransactionNo"));
                MRNo.setText(jsonChildNode.optString("MRNo"));
                PatientName.setText(jsonChildNode.optString("PatientName"));
                PatientGender.setText(jsonChildNode.optString("Sex"));
                PatientAge.setText(jsonChildNode.optString("Age"));
                CollectionDate.setText(jsonChildNode.optString("CollectionDate"));
                Panel.setText(jsonChildNode.optString("Panel"));
                PatientConsaltant.setText(jsonChildNode.optString("Consultant"));
                PatientDepartment.setText(jsonChildNode.optString("Department"));
                PatientLaboratoryNo.setText(jsonChildNode.optString("LaboratoryNo"));

                ReportName.setText(jsonChildNode.optString("ReportName"));
                TestDescription.setText(jsonChildNode.optString("TestDescription"));

                Variable1.setText(jsonChildNode.optString("Variable1"));
                Variable2.setText(jsonChildNode.optString("Variable2"));
                Variable3.setText(jsonChildNode.optString("Variable3"));
                Variable4.setText(jsonChildNode.optString("Variable4"));
                Variable5.setText(jsonChildNode.optString("Variable5"));
                Variable6.setText(jsonChildNode.optString("Variable6"));
                Variable7.setText(jsonChildNode.optString("Variable7"));
                Variable8.setText(jsonChildNode.optString("Variable8"));
                Variable9.setText(jsonChildNode.optString("Variable9"));
                Variable10.setText(jsonChildNode.optString("Variable10"));
                Variable11.setText(jsonChildNode.optString("Variable11"));

                Variable1Result.setText(jsonChildNode.optString("Variable1Result"));
                Variable2Result.setText(jsonChildNode.optString("Variable2Result"));
                Variable3Result.setText(jsonChildNode.optString("Variable3Result"));
                Variable4Result.setText(jsonChildNode.optString("Variable4Result"));
                Variable5Result.setText(jsonChildNode.optString("Variable5Result"));
                Variable6Result.setText(jsonChildNode.optString("Variable6Result"));
                Variable7Result.setText(jsonChildNode.optString("Variable7Result"));
                Variable8Result.setText(jsonChildNode.optString("Variable8Result"));
                Variable9Result.setText(jsonChildNode.optString("Variable9Result"));
                Variable10Result.setText(jsonChildNode.optString("Variable10Result"));
                Variable11Result.setText(jsonChildNode.optString("Variable11Result"));

                Variable1Normal.setText(jsonChildNode.optString("Variable1Normal"));
                Variable2Normal.setText(jsonChildNode.optString("Variable2Normal"));
                Variable3Normal.setText(jsonChildNode.optString("Variable3Normal"));
                Variable4Normal.setText(jsonChildNode.optString("Variable4Normal"));
                Variable5Normal.setText(jsonChildNode.optString("Variable5Normal"));
                Variable6Normal.setText(jsonChildNode.optString("Variable6Normal"));
                Variable7Normal.setText(jsonChildNode.optString("Variable7Normal"));
                Variable8Normal.setText(jsonChildNode.optString("Variable8Normal"));
                Variable9Normal.setText(jsonChildNode.optString("Variable9Normal"));
                Variable10Normal.setText(jsonChildNode.optString("Variable10Normal"));
                Variable11Normal.setText(jsonChildNode.optString("Variable11Normal"));
            }
        } catch (Exception ex) {
            Log.e(TAG, "Error: " + ex.getMessage());
        }
    }
    private void init(){
        btn_generate = (Button)findViewById(R.id.btn_generate);
        ll_pdflayout = (LinearLayout) findViewById(R.id.ll_pdflayout);
    }

    private void listener(){
        btn_generate.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_generate:

                if (boolean_save) {
                    Intent intent = new Intent(getApplicationContext(), PDFViewActivity.class);
                    startActivity(intent);

                } else {
                    if (boolean_permission) {
                        progressDialog = new ProgressDialog(ViewReport.this);
                        progressDialog.setMessage("Please wait");
                        bitmap = loadBitmapFromView(ll_pdflayout, ll_pdflayout.getWidth(), ll_pdflayout.getHeight());
                        createPdf();
//                        saveBitmap(bitmap);
                    } else {

                    }

                    createPdf();
                    break;
                }
        }
    }

    private void createPdf(){
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float hight = displaymetrics.heightPixels ;
        float width = displaymetrics.widthPixels ;

        int convertHighet = (int) hight, convertWidth = (int) width;

//        Resources mResources = getResources();
//        Bitmap bitmap = BitmapFactory.decodeResource(mResources, R.drawable.screenshot);

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(convertWidth, convertHighet, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();


        Paint paint = new Paint();
        canvas.drawPaint(paint);


        bitmap = Bitmap.createScaledBitmap(bitmap, convertWidth, convertHighet, true);

        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0 , null);
        document.finishPage(page);


        // write the document content
        String targetPdf = "/sdcard/test.pdf";
        File filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
            btn_generate.setText("Check PDF");
            boolean_save=true;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }

        // close the document
        document.close();
    }



    public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);

        return b;
    }

    private void fn_permission() {
        if ((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)||
                (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {

            if ((ActivityCompat.shouldShowRequestPermissionRationale(ViewReport.this, android.Manifest.permission.READ_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions(ViewReport.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);

            }

            if ((ActivityCompat.shouldShowRequestPermissionRationale(ViewReport.this, Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions(ViewReport.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);

            }
        } else {
            boolean_permission = true;


        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                boolean_permission = true;


            } else {
                Toast.makeText(getApplicationContext(), "Please allow the permission", Toast.LENGTH_LONG).show();

            }
        }
    }

}
