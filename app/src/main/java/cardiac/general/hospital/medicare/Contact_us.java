package cardiac.general.hospital.medicare;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Contact_us extends AppCompatActivity implements OnMapReadyCallback {

    public GoogleMap mMap;
    LinearLayout phone1,phone2,email,mapContact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        phone1= (LinearLayout) findViewById(R.id.phone1_contact);
        phone2= (LinearLayout) findViewById(R.id.phone2_contact);
        email= (LinearLayout) findViewById(R.id.email_contact);
        mapContact= (LinearLayout) findViewById(R.id.map_contact);

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mEmail = new Intent(Intent.ACTION_SEND);
                mEmail.putExtra(Intent.EXTRA_EMAIL, new String[]{"info@medicarehospital.pk"});
                mEmail.putExtra(Intent.EXTRA_SUBJECT, "Contact us");
                mEmail.putExtra(Intent.EXTRA_TEXT, "Enter your message here");
                // prompts to choose email client
                mEmail.setType("message/rfc822");
                startActivity(Intent.createChooser(mEmail, "Choose an email client to contact us"));
            }
        });

        phone1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = "+9221 3493 1886";
                Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(phoneIntent);
            }
        });
        phone2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = "+9221 3879 8941";
                Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(phoneIntent);
            }
        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng address = new LatLng(24.881146, 67.063410);
        mMap.addMarker(new MarkerOptions().position(address).title("Medicare Cardiac & General Hospital"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(address));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(19.0f));
    }
}
