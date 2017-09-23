package cardiac.general.hospital.medicare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Report extends AppCompatActivity {

    EditText getTransitionNo,getPinNo;
    Button getReportButton;
    int getTransNmbr,getPinNmbr=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        getTransitionNo= (EditText) findViewById(R.id.EditTxtTransition);
        getPinNo= (EditText) findViewById(R.id.EditTxtPinNo);

        getReportButton= (Button) findViewById(R.id.getReportBtn);
        getReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getTransitionNo.getText().toString().equals("")){
                    Toast.makeText(Report.this, "Please Enter Transaction No.", Toast.LENGTH_SHORT).show();
                }else{
                    getTransNmbr=Integer.parseInt(getTransitionNo.getText().toString());
                    if (getPinNo.getText().toString().equals("")){
                        getPinNmbr=0;
                    }else{
                        getPinNmbr=Integer.parseInt(getPinNo.getText().toString());
                    }
                    Intent intent = new Intent(getBaseContext(), ReportList.class);
                    intent.putExtra("TransNumber", getTransNmbr);
                    intent.putExtra("PinNumber", getPinNmbr);
                    startActivity(intent);
                }
            }
        });
    }
}
