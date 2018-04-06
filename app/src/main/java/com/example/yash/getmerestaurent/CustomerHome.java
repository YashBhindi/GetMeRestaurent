package com.example.yash.getmerestaurent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CustomerHome extends AppCompatActivity {

    private FirebaseAuth mAuth= FirebaseAuth.getInstance();
    private String uid=mAuth.getCurrentUser().getUid();
    private DatabaseReference restaurent  = FirebaseDatabase.getInstance().getReference("Users").child("Restaurents");
    private FirebaseUser user;
    private RestaurentBean restaurentBean;

    private TextView mRestaurentName,mManagerName,mRestaurentAddress,mManagerContact,mOpenDay,mCloseDay;
    private TextView mOpenTime,mCloseTime;
    private Button mupdate;
    private Button mBack,mGoToRestaurent;
    private TextView rn,mn,mc,od,cd,ot,ct;

    private int hour1,hour2,minute1,minute2;
    private int Fhour1,Fhour2,Fminute1,Fminute2;

    RecyclerView rv;
    RestaurentMenuAdapter restaurentMenuAdapter;
    List<RestaurentMenuAddItemBean> menuAddItemBeanList;
    private RestaurentMenuAddItemBean restaurentMenuAddItemBean;



    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_customer_home);
        Intent intent = getIntent();
        String id = intent.getStringExtra("title");
        name = intent.getStringExtra("restaurentId");
        Toast.makeText(getApplicationContext(),name,Toast.LENGTH_SHORT).show();
        mRestaurentName = findViewById(R.id.restaurentName);
        mManagerName = findViewById(R.id.managerName);
        mManagerContact = findViewById(R.id.managerContact);
        mRestaurentAddress = findViewById(R.id.restaurentAddress);
        mOpenDay= findViewById(R.id.openDay);
        mCloseDay = findViewById(R.id.closeDay);
        mupdate = (Button)findViewById(R.id.updateDetails);
        mOpenTime=findViewById(R.id.openTime);
        mCloseTime=findViewById(R.id.closeTime);
        mBack=findViewById(R.id.back);
        mGoToRestaurent=findViewById(R.id.goToRestaurent);
        rn=findViewById(R.id.rn);
        mn=findViewById(R.id.mn);
        mc=findViewById(R.id.mc);
        od=findViewById(R.id.od);
        cd=findViewById(R.id.cd);
        ct=findViewById(R.id.ct);
        ot=findViewById(R.id.ot);

        rv=findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        menuAddItemBeanList=new ArrayList<>();

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CustomerHome.this,MapsActivity.class));
                finish();
            }
        });

        mGoToRestaurent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // startActivity(new Intent(CustomerHome.this,CustomerGetDirection.class));
            }
        });

    }

    protected void onStart() {
        super.onStart();

        restaurent=FirebaseDatabase.getInstance().getReference("Users").child("Restaurents").child(name).child("General");
        restaurent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //    studentList.clear();
                for (DataSnapshot student_snapshot : dataSnapshot.getChildren()) {
                    restaurentBean = student_snapshot.getValue(RestaurentBean.class);
                    break;
                }
                mRestaurentName.setText(restaurentBean.getRestaurentName());
                mManagerName.setText(restaurentBean.getManagerNamem());
                mManagerContact.setText(restaurentBean.getManagerContact());
                mRestaurentAddress.setText(restaurentBean.getRestaurentAddress());
                mOpenDay.setText(restaurentBean.getOpenDay());
                mCloseDay.setText(restaurentBean.getCloseDay());
                mOpenTime.setText(restaurentBean.getOpenTime());

                mCloseTime.setText(restaurentBean.getCloseTime());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
        DatabaseReference cMenu;
        cMenu  = FirebaseDatabase.getInstance().getReference("Users").child("Restaurents").child(name).child("Menu");

        cMenu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                menuAddItemBeanList.clear();

                for(DataSnapshot item: dataSnapshot.getChildren()){
                    restaurentMenuAddItemBean=item.getValue(RestaurentMenuAddItemBean.class);
                    menuAddItemBeanList.add(restaurentMenuAddItemBean);
                }
                restaurentMenuAdapter=new RestaurentMenuAdapter(CustomerHome.this,menuAddItemBeanList);
                rv.setAdapter(restaurentMenuAdapter);
                rv.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

}
