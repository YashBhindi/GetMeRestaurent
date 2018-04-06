package com.example.yash.getmerestaurent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yash.getmerestaurent.R;
import com.example.yash.getmerestaurent.RestaurentBean;
import com.example.yash.getmerestaurent.RestaurentHome;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RestaurentShowDetails extends AppCompatActivity {


    private FirebaseAuth mAuth= FirebaseAuth.getInstance();
    private String uid=mAuth.getCurrentUser().getUid();
    private DatabaseReference restaurent  = FirebaseDatabase.getInstance().getReference("Users").child("Restaurents");
    private FirebaseUser user;
    private RestaurentBean restaurentBean;

    private EditText mRestaurentName,mManagerName,mRestaurentAddress,mManagerContact,mOpenDay,mCloseDay;
    private EditText mOpenTime,mCloseTime;
    private Button mupdate;

    private TextView rn,mn,mc,od,cd,ot,ct;

    private int hour1,hour2,minute1,minute2;
    private int Fhour1,Fhour2,Fminute1,Fminute2;
    // EditText mLongitude,mLatitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurent_show_details);


        mRestaurentName = (EditText)findViewById(R.id.restaurentName);
        mManagerName = (EditText)findViewById(R.id.managerName);
        mManagerContact = (EditText)findViewById(R.id.managerContact);
        mRestaurentAddress = (EditText)findViewById(R.id.restaurentAddress);
        mOpenDay= (EditText)findViewById(R.id.openDay);
        mCloseDay = (EditText)findViewById(R.id.closeDay);
        mupdate = (Button)findViewById(R.id.updateDetails);
        mOpenTime=(EditText)findViewById(R.id.openTime);
        mCloseTime=(EditText)findViewById(R.id.closeTime);

        rn=findViewById(R.id.rn);
        mn=findViewById(R.id.mn);
        mc=findViewById(R.id.mc);
        od=findViewById(R.id.od);
        cd=findViewById(R.id.cd);
        ct=findViewById(R.id.ct);
        ot=findViewById(R.id.ot);


        mupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRestaurent();
            }
        });
    }

    protected void onStart() {
        super.onStart();
        uid = mAuth.getCurrentUser().getUid();
        restaurent=FirebaseDatabase.getInstance().getReference("Users").child("Restaurents").child(uid).child("General");
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


    }
    public void addRestaurent(){
        String RestaurentName = mRestaurentName.getText().toString();
        String ManagerName = mManagerName.getText().toString();
        String ManagerContact= mManagerContact.getText().toString();
        String RestaurentAddress =mRestaurentAddress.getText().toString();
        String OpenDay = mOpenDay.getText().toString();
        String CloseDay= mCloseDay.getText().toString();

        String OpenTime= mOpenTime.getText().toString();
        String CloseTime= mCloseTime.getText().toString();

        mAuth=FirebaseAuth.getInstance();
        if(!TextUtils.isEmpty(RestaurentName) && !TextUtils.isEmpty(RestaurentAddress) && !TextUtils.isEmpty(ManagerName) && !TextUtils.isEmpty(ManagerContact) && !TextUtils.isEmpty(OpenDay) && !TextUtils.isEmpty(CloseDay) && !TextUtils.isEmpty(OpenTime) && !TextUtils.isEmpty(CloseTime)){
            uid = mAuth.getCurrentUser().getUid();
            restaurentBean=new RestaurentBean(uid,RestaurentName,ManagerName,ManagerContact,RestaurentAddress,OpenDay,CloseDay,OpenTime,CloseTime);
            uid = mAuth.getCurrentUser().getUid();
            restaurent=FirebaseDatabase.getInstance().getReference("Users").child("Restaurents").child(uid).child("General");

            restaurent.child("details").setValue(restaurentBean);
            Toast.makeText(getApplicationContext(),"Restaurent's General Details Set",Toast.LENGTH_SHORT).show();

            startActivity(new Intent(RestaurentShowDetails.this,RestaurentHome.class));
        }
        else {
            Toast.makeText(getApplicationContext(),"Please fill all the details",Toast.LENGTH_SHORT).show();

        }
    }

}
