package cardiac.general.hospital.medicare;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        int SPLASH_DISPLAY_LENGTH = 1000;
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(Splash.this,MainMenu.class);
                Splash.this.startActivity(mainIntent);
                Splash.this.finish();
                // transition from splash to main menu
                overridePendingTransition(R.anim.activityfadein,R.anim.splashfadeout);
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}