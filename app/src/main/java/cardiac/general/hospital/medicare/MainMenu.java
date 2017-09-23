package cardiac.general.hospital.medicare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }
    public void Specialities_Click(View view){
        Intent i = new Intent(MainMenu.this,Specialities.class);
        startActivity(i);
    }
    public void Find_Consultant_Click(View view){
        Intent i = new Intent(MainMenu.this,FindConsultant.class);
        startActivity(i);
    }
    public void Appointment_Click(View view){
        Intent i = new Intent(MainMenu.this,MakeAppointment.class);
        startActivity(i);
    }
    public void Report_Click(View view){
        Intent i = new Intent(MainMenu.this,Report.class);
        startActivity(i);
    }
    public void About_Click(View view){
        Intent i = new Intent(MainMenu.this,About.class);
        startActivity(i);
    }
    public void Contact_Click(View view){
        Intent i = new Intent(MainMenu.this,Contact_us.class);
        startActivity(i);
    }
}
