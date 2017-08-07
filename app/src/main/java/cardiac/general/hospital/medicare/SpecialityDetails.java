package cardiac.general.hospital.medicare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SpecialityDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speciality_details);
        TextView SpecialityId= (TextView) findViewById(R.id.Speciality_id);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String id = bundle.getString("ID");
        SpecialityId.setText(id);
    }
}
