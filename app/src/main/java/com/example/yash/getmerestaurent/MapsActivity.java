package com.example.yash.getmerestaurent;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;
    GoogleApiClient googleApiClient;
    Location mLocation,pick;
    LocationRequest locationRequest;
    Button showRestaurent,mLogout;
    Marker restaurentMarker[];
    RestaurentBean rBean=new RestaurentBean();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        showRestaurent=findViewById(R.id.showRestaurent);
        mLogout=findViewById(R.id.logout);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        showRestaurent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                LatLng latLng =new LatLng(mLocation.getLatitude(),mLocation.getLongitude());
                pick = new Location(mLocation);
                pick=mLocation;
                String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Customer");

                GeoFire geoFire2 = new GeoFire(ref);
                geoFire2.setLocation(uid, new GeoLocation(mLocation.getLatitude(), mLocation.getLongitude()), new GeoFire.CompletionListener() {
                    @Override
                    public void onComplete(String key, DatabaseError error) {

                    }
                });
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(5));
                mMap.addMarker(new MarkerOptions().position(latLng));

                showRestaurent.setText("Showing near by restaurents");

                getRestaurents();
                mLogout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
                        DatabaseReference customer = FirebaseDatabase.getInstance().getReference().child("Customer");

                        GeoFire geoFire = new GeoFire(customer);
                        geoFire.removeLocation(uid, new GeoFire.CompletionListener() {
                            @Override
                            public void onComplete(String key, DatabaseError error) {

                            }
                        });
                    }
                });
            }
        });




    }
    private int radius=30,size,count=0;
    private boolean restaurentFound=false;
    private List<String> restaurentFoundId=new ArrayList<>();
    private void getRestaurents(){
        DatabaseReference restaurentLocation = FirebaseDatabase.getInstance().getReference().child("Restaurent");

        GeoFire geoFire = new GeoFire(restaurentLocation);
        GeoQuery geoQuery=geoFire.queryAtLocation(new GeoLocation(pick.getLatitude(),pick.getLongitude()),radius);
        geoQuery.removeAllListeners();
        restaurentFoundId.clear();
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {

                restaurentFoundId.add(key);
            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {

                restaurentFound = true;
                size= restaurentFoundId.size();

                getRestaurentsMarker();

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });



    }
    private  void getRestaurentsMarker(){
        final Marker[] restaurentMarker = new Marker[restaurentFoundId.size()];
        for (String id : restaurentFoundId) {
            Toast.makeText(getApplicationContext(),id,Toast.LENGTH_SHORT).show();
            /*DatabaseReference r=FirebaseDatabase.getInstance().getReference().child("Users").child("Restaurents").child(id).child("General");
            r.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //    studentList.clear();
                    for (DataSnapshot student_snapshot : dataSnapshot.getChildren()) {
                        rBean = student_snapshot.getValue(RestaurentBean.class);
                        break;
                    }

                }@Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });*/
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Restaurent").child(id).child("l");


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
                    LatLng loc=new LatLng(lat,lon);

                    restaurentMarker[count] =mMap.addMarker(new MarkerOptions().position(loc).snippet(String.valueOf(count)).title("Resraurent").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                    count++;
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        // TODO: Before enabling the My Location layer, you must request
        // location permission from the user. This sample does not include
        // a request for location permission.
        buildGoogleApiClient();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(getApplicationContext(),marker.getTitle(),Toast.LENGTH_SHORT).show();
                marker.showInfoWindow();
                return true;
            }
        });
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent1 = new Intent(MapsActivity.this, CustomerHome.class);
                String title = marker.getTitle();
                String rid=restaurentFoundId.get(Integer.parseInt(marker.getSnippet()));
                intent1.putExtra("title", title);
                intent1.putExtra("restaurentId", rid);


                startActivity(intent1);

            }
        });
    }

    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {



        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        mLocation=location;
        LatLng latLng =new LatLng(location.getLatitude(),location.getLongitude());

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        mMap.addMarker(new MarkerOptions().position(latLng));

        String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference customer = FirebaseDatabase.getInstance().getReference().child("Customer");

        final GeoFire geoFire1 = new GeoFire(customer);
        geoFire1.setLocation(uid, new GeoLocation(location.getLatitude(),location.getLongitude()), new GeoFire.CompletionListener() {
            @Override
            public void onComplete(String key, DatabaseError error) {
            }
        });



    }

  /*  @Overr
    protected void onStop() {
        super.onStop();
        String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference customer = FirebaseDatabase.getInstance().getReference().child("Customer");

        GeoFire geoFire = new GeoFire(customer);
        geoFire.removeLocation(uid, new GeoFire.CompletionListener() {
            @Override
            public void onComplete(String key, DatabaseError error) {

            }
        });
    }*/
}
