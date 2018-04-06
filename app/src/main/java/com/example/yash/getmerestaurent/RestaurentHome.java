package com.example.yash.getmerestaurent;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.yash.getmerestaurent.MainActivity;
import com.example.yash.getmerestaurent.R;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.location.LocationManager.*;

public class RestaurentHome extends AppCompatActivity {

    private Button mSignOut, mShowMenu, mShowDetails, mRegisterCurrentLocation;
    private FirebaseAuth mAuth;
    private DatabaseReference restaurentLocation;
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurent_home);

        mSignOut = findViewById(R.id.signOut);
        mShowMenu = findViewById(R.id.showMenu);
        mShowDetails = findViewById(R.id.showDetails);
        mRegisterCurrentLocation = findViewById(R.id.registerCurrentLocation);
        mAuth = FirebaseAuth.getInstance();
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .build();

        mSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                startActivity(new Intent(RestaurentHome.this, MainActivity.class));
                finish();
            }
        });

        mShowMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RestaurentHome.this, RestaurentShowMenu.class));
                finish();
            }
        });

        mShowDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RestaurentHome.this, RestaurentShowDetails.class));
                finish();
            }
        });

        mRegisterCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restaurentLocation = FirebaseDatabase.getInstance().getReference().child("Restaurent");
                String uid = mAuth.getCurrentUser().getUid();
                GeoFire geoFire = new GeoFire(restaurentLocation);
                if (ActivityCompat.checkSelfPermission(RestaurentHome.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(RestaurentHome.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    return;
                }
                Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                geoFire.setLocation(uid, new GeoLocation(lastLocation.getLatitude(),lastLocation.getLongitude()), new GeoFire.CompletionListener() {
                    @Override
                    public void onComplete(String key, DatabaseError error) {
                        Toast.makeText(getApplicationContext(),"Restaurent Location Registerd",Toast.LENGTH_SHORT).show();
                    }
                });
                startActivity(new Intent(RestaurentHome.this,RestaurentHome.class));
                finish();
            }
        });


    }
    protected void onStart() {
        super.onStart();
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

}
