package cardiac.general.hospital.medicare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class ViewReport extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String reportID = bundle.getString("Report_ID");
        //Toast.makeText(this, ""+reportID, Toast.LENGTH_SHORT).show();
    }
}
