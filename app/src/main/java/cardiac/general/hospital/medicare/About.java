package cardiac.general.hospital.medicare;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class About extends AppCompatActivity {
    ImageView arrow_down1,arrow_down2,arrow_down3;
    RelativeLayout history_layout,vision_layout,policy_layout;
    LinearLayout history_text,vision_text,policy_text;
    boolean isopen=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        //Arrow
        arrow_down1= (ImageView) findViewById(R.id.arrow_down1);
        arrow_down2= (ImageView) findViewById(R.id.arrow_down2);
        arrow_down3= (ImageView) findViewById(R.id.arrow_down3);
        //Relative Layout act as button
        history_layout= (RelativeLayout) findViewById(R.id.history_id);
        vision_layout= (RelativeLayout) findViewById(R.id.vision_id);
        policy_layout= (RelativeLayout) findViewById(R.id.policy_id);
        //Linear Layout act as text
        history_text= (LinearLayout) findViewById(R.id.history_text);
        vision_text= (LinearLayout) findViewById(R.id.vision_text);
        policy_text= (LinearLayout) findViewById(R.id.policy_text);

        history_text.setVisibility(View.GONE);
        vision_text.setVisibility(View.GONE);
        policy_text.setVisibility(View.GONE);

        history_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isopen || vision_text.getVisibility() == View.VISIBLE || policy_text.getVisibility() == View.VISIBLE){
                    arrow_down1.setImageResource(R.drawable.arrow_up);
                    arrow_down2.setImageResource(R.drawable.arrow_down);
                    arrow_down3.setImageResource(R.drawable.arrow_down);
                    isopen=true;
                    history_text.setVisibility(View.VISIBLE);
                    vision_text.setVisibility(View.GONE);
                    policy_text.setVisibility(View.GONE);
                }else {
                    arrow_down1.setImageResource(R.drawable.arrow_down);
                    arrow_down2.setImageResource(R.drawable.arrow_down);
                    arrow_down3.setImageResource(R.drawable.arrow_down);
                    isopen = false;

                    history_text.setVisibility(View.GONE);
                    vision_text.setVisibility(View.GONE);
                    policy_text.setVisibility(View.GONE);
                }
            }
        });
        vision_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isopen || history_text.getVisibility() == View.VISIBLE || policy_text.getVisibility() == View.VISIBLE){
                    arrow_down1.setImageResource(R.drawable.arrow_down);
                    arrow_down2.setImageResource(R.drawable.arrow_up);
                    arrow_down3.setImageResource(R.drawable.arrow_down);
                    isopen=true;
                    history_text.setVisibility(View.GONE);
                    vision_text.setVisibility(View.VISIBLE);
                    policy_text.setVisibility(View.GONE);
                }else {
                    arrow_down1.setImageResource(R.drawable.arrow_down);
                    arrow_down2.setImageResource(R.drawable.arrow_down);
                    arrow_down3.setImageResource(R.drawable.arrow_down);
                    isopen = false;

                    history_text.setVisibility(View.GONE);
                    vision_text.setVisibility(View.GONE);
                    policy_text.setVisibility(View.GONE);
                }
            }
        });
        policy_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isopen || history_text.getVisibility() == View.VISIBLE || vision_text.getVisibility() == View.VISIBLE){
                    arrow_down1.setImageResource(R.drawable.arrow_down);
                    arrow_down2.setImageResource(R.drawable.arrow_down);
                    arrow_down3.setImageResource(R.drawable.arrow_up);
                    isopen=true;
                    history_text.setVisibility(View.GONE);
                    vision_text.setVisibility(View.GONE);
                    policy_text.setVisibility(View.VISIBLE);
                }else {
                    arrow_down1.setImageResource(R.drawable.arrow_down);
                    arrow_down2.setImageResource(R.drawable.arrow_down);
                    arrow_down3.setImageResource(R.drawable.arrow_down);
                    isopen = false;

                    history_text.setVisibility(View.GONE);
                    vision_text.setVisibility(View.GONE);
                    policy_text.setVisibility(View.GONE);
                }
            }
        });
    }
}
