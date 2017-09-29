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
import java.text.SimpleDateFormat;
import java.util.Date;

public class ViewReport extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "Response";
    Button btn_generate;
    TextView TransitionNo,MRNo,PatientName,PatientGender,PatientAge,CollectionDate,Panel,PatientConsaltant,PatientDepartment, PatientLaboratoryNo;
    TextView ReportName,TestDescription,SystemDate;
    TextView [] variable =new TextView[13];
    TextView [] variableResult =new TextView[13];
    TextView [] variableNormal =new TextView[13];
    LinearLayout ll_pdflayout;
    public static int REQUEST_PERMISSIONS = 1;
    boolean boolean_permission;
    boolean boolean_save;
    Bitmap bitmap;
    ProgressDialog progressDialog,pd;
    String jsonStr;
    String TestName,Report_Name;
    int Test_TransNo,Test_PinNo,Report_ID;
    JSONObject jsonObj,jsonChildNode;
    JSONArray jsonMainNode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report);

        pd = new ProgressDialog(ViewReport.this);
        pd.setMessage("loading");
        pd.show();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        TestName= bundle.getString("Report_Name");
        Test_TransNo= bundle.getInt("Report_Trans");
        Report_Name=TestName.replaceAll(" ","%20");
        Report_ID=bundle.getInt("Report_ID");
        Test_PinNo= bundle.getInt("Report_Pn");

        initVariables();
        AsyncCallReportPDF task = new AsyncCallReportPDF();
        task.execute();
        init();
        fn_permission();
        listener();
        //Toast.makeText(this, ""+TestName+Test_TransNo+Test_PinNo, Toast.LENGTH_SHORT).show();
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
        SystemDate= (TextView) findViewById(R.id.systemDate);

        //Variable Names
        variable[0]= (TextView) findViewById(R.id.variable1);
        variable[1]= (TextView) findViewById(R.id.variable2);
        variable[2]= (TextView) findViewById(R.id.variable3);
        variable[3]= (TextView) findViewById(R.id.variable4);
        variable[4]= (TextView) findViewById(R.id.variable5);
        variable[5]= (TextView) findViewById(R.id.variable6);
        variable[6]= (TextView) findViewById(R.id.variable7);
        variable[7]= (TextView) findViewById(R.id.variable8);
        variable[8]= (TextView) findViewById(R.id.variable9);
        variable[9]= (TextView) findViewById(R.id.variable10);
        variable[10]= (TextView) findViewById(R.id.variable11);
        variable[11]= (TextView) findViewById(R.id.variable12);
        variable[12]= (TextView) findViewById(R.id.variable13);

        //variable Result
        variableResult[0]= (TextView) findViewById(R.id.variable1Result);
        variableResult[1]= (TextView) findViewById(R.id.variable2Result);
        variableResult[2]= (TextView) findViewById(R.id.variable3Result);
        variableResult[3]= (TextView) findViewById(R.id.variable4Result);
        variableResult[4]= (TextView) findViewById(R.id.variable5Result);
        variableResult[5]= (TextView) findViewById(R.id.variable6Result);
        variableResult[6]= (TextView) findViewById(R.id.variable7Result);
        variableResult[7]= (TextView) findViewById(R.id.variable8Result);
        variableResult[8]= (TextView) findViewById(R.id.variable9Result);
        variableResult[9]= (TextView) findViewById(R.id.variable10Result);
        variableResult[10]= (TextView) findViewById(R.id.variable11Result);
        variableResult[11]= (TextView) findViewById(R.id.variable12Result);
        variableResult[12]= (TextView) findViewById(R.id.variable13Result);

        //variable Normal
        variableNormal[0]= (TextView) findViewById(R.id.variable1Normal);
        variableNormal[1]= (TextView) findViewById(R.id.variable2Normal);
        variableNormal[2]= (TextView) findViewById(R.id.variable3Normal);
        variableNormal[3]= (TextView) findViewById(R.id.variable4Normal);
        variableNormal[4]= (TextView) findViewById(R.id.variable5Normal);
        variableNormal[5]= (TextView) findViewById(R.id.variable6Normal);
        variableNormal[6]= (TextView) findViewById(R.id.variable7Normal);
        variableNormal[7]= (TextView) findViewById(R.id.variable8Normal);
        variableNormal[8]= (TextView) findViewById(R.id.variable9Normal);
        variableNormal[9]= (TextView) findViewById(R.id.variable10Normal);
        variableNormal[10]= (TextView) findViewById(R.id.variable11Normal);
        variableNormal[11]= (TextView) findViewById(R.id.variable12Normal);
        variableNormal[12]= (TextView) findViewById(R.id.variable13Normal);
    }
    private class AsyncCallReportPDF extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() { Log.i("Test", "onPreExecute"); }
        @Override
        protected Void doInBackground(Void... params) { Log.i("Test", "doInBackground");
            GetJSON();
            pd.dismiss();
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            Log.i("Test", "onPostExecute");
        }
    }
    public void GetJSON() {
        HttpHandler sh = new HttpHandler();
        String url = "http://local.jmc.edu.pk:1133/reporthandler.ashx?TransNo="+Test_TransNo+"&PinNo="+Test_PinNo+"&TestName="+Report_Name+"&TestId=0"+Report_ID;
        jsonStr = sh.makeServiceCall(url);
        Log.d(TAG, url + jsonStr);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    String jsonString="{\"ReportPDF\":"+jsonStr+"}";
                    jsonObj = new JSONObject(jsonString);
                    // Getting JSON Array node
                    jsonMainNode = jsonObj.getJSONArray("ReportPDF");
                    jsonChildNode = jsonMainNode.getJSONObject(0);

                    TransitionNo.setText(jsonChildNode.optString("LBIORESM_LTESTM_ID"));
                    if (jsonChildNode.optString("MRNo").equals("")){
                        MRNo.setText("0");
                    }else{MRNo.setText(jsonChildNode.optString("MRNo"));  }
                    PatientName.setText(jsonChildNode.optString("OPAT_NAME"));
                    PatientGender.setText(jsonChildNode.optString("LTESTM_SEX"));
                    PatientAge.setText(jsonChildNode.optString("YEAR"));
                    CollectionDate.setText(jsonChildNode.optString("LTESTM_SER_DATE"));
                    Panel.setText(jsonChildNode.optString("PANEL_DESC"));
                    PatientConsaltant.setText(jsonChildNode.optString("CONSL_DESC"));
                    PatientDepartment.setText(jsonChildNode.optString("MDEPT_DESC"));
                    PatientLaboratoryNo.setText(jsonChildNode.optString("LABNO"));

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
                    String format = simpleDateFormat.format(new Date());
                    SystemDate.setText("Webserver "+format);
                    ReportName.setText(TestName);
                    TestDescription.setText(jsonChildNode.optString("TESTHEADING"));

                    for (int i = 0; i < jsonMainNode.length(); i++) {
                        JSONObject c = jsonMainNode.getJSONObject(i);
                        String testNames = c.optString("LTEST_DESC");
                        String testResults = c.optString("LBIORESD_LRESULT");
                        String testNormals = c.optString("LTEST_RANGE1");
                        Log.d("Testing",testNames+"&"+testResults+"&"+testNormals);

                        variable[i].setVisibility(View.VISIBLE);
                        variable[i].setText(testNames);
                        variableResult[i].setVisibility(View.VISIBLE);
                        variableResult[i].setText(testResults);
                        variableNormal[i].setVisibility(View.VISIBLE);
                        variableNormal[i].setText(testNormals);
                    }
        } catch (Exception ex) {
            Log.e(TAG, "Error: " + ex.getMessage());
        }
            }
        });
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
