package com.example.yash.getmerestaurent;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CustomerPathToRestaurent extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener, DirectionCallback {
    private Button btnRequestDirection;
    private GoogleMap googleMap;
    private String serverKey = "AIzaSyDTRZKkTdHap0_EJsuDRIhmn0o4RkOKDAg";
    private LatLng origin ;
    private LatLng destination ;
    private String Rid,Cuid;
    private Button btnBack,btnGetPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_path_restaurent);

        btnBack = findViewById(R.id.back);
        btnGetPath=findViewById(R.id.getPath);
        btnGetPath.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
        Intent in = getIntent();
        Rid = in.getStringExtra("restaurentId");
        Cuid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        getCustomerLocation(Cuid);
        getRestaurentLocation(Rid);




    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;




    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.getPath) {
            requestDirection();
        }
        if(id==R.id.back){
            startActivity(new Intent(CustomerPathToRestaurent.this,MapsActivity.class));
        }
    }

    public void requestDirection() {
        Snackbar.make(btnGetPath, "Direction Requesting...", Snackbar.LENGTH_SHORT).show();
        GoogleDirection.withServerKey(serverKey)
                .from(origin)
                .to(destination)
                .transportMode(TransportMode.DRIVING)
                .execute(this);
    }

    @Override
    public void onDirectionSuccess(Direction direction, String rawBody) {
        Snackbar.make(btnGetPath, "Success with status : " + direction.getStatus(), Snackbar.LENGTH_SHORT).show();
        if (direction.isOK()) {
            Route route = direction.getRouteList().get(0);
            googleMap.addMarker(new MarkerOptions().position(origin).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            googleMap.addMarker(new MarkerOptions().position(destination).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

            ArrayList<LatLng> directionPositionList = route.getLegList().get(0).getDirectionPoint();
            googleMap.addPolyline(DirectionConverter.createPolyline(this, directionPositionList, 5, Color.RED));
            setCameraWithCoordinationBounds(route);

            btnGetPath.setVisibility(View.GONE);
        } else {
            Snackbar.make(btnGetPath, direction.getStatus(), Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDirectionFailure(Throwable t) {
        Snackbar.make(btnGetPath, t.getMessage(), Snackbar.LENGTH_SHORT).show();
    }

    private void setCameraWithCoordinationBounds(Route route) {
        LatLng southwest = route.getBound().getSouthwestCoordination().getCoordination();
        LatLng northeast = route.getBound().getNortheastCoordination().getCoordination();
        LatLngBounds bounds = new LatLngBounds(southwest, northeast);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
    }
    public void getRestaurentLocation(String id){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Restaurent").child(id).child("l");
        LatLng loc;
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                List<Object> map= (List<Object>) dataSnapshot.getValue();
                double lat=0;
                double lon=0;
                if(map.get(0)!=null){
                    lat=Double.parseDouble(map.get(0).toString());
                }
                if(map.get(1)!=null){
                    lon=Double.parseDouble(map.get(1).toString());
                }
                destination=new LatLng(lat,lon);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getCustomerLocation(String id){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Customer").child(id).child("l");
        LatLng loc;
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                List<Object> map= (List<Object>) dataSnapshot.getValue();
                double lat=0;
                double lon=0;
                if(map.get(0)!=null){
                    lat=Double.parseDouble(map.get(0).toString());
                }
                if(map.get(1)!=null){
                    lon=Double.parseDouble(map.get(1).toString());
                }
                origin=new LatLng(lat,lon);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


}
