package com.example.yash.getmerestaurent;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.yash.getmerestaurent.R;
import com.example.yash.getmerestaurent.RestaurentBean;
import com.example.yash.getmerestaurent.RestaurentHome;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;

public class RestaurentSignup extends AppCompatActivity {

    private FirebaseAuth mAuth= FirebaseAuth.getInstance();
    private DatabaseReference restaurent;
    private FirebaseUser user;
    private RestaurentBean restaurentBean;
    private String uid;
    private EditText mRestaurentName,mManagerName,mRestaurentAddress,mManagerContact,mOpenDay,mCloseDay;
    private EditText mOpenTime,mCloseTime;
    private  Button mNext;

    private int hour1,hour2,minute1,minute2;
    private int Fhour1,Fhour2,Fminute1,Fminute2;
    // EditText mLongitude,mLatitude;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth=FirebaseAuth.getInstance();
        restaurent=FirebaseDatabase.getInstance().getReference("Users").child("Restaurents");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurent_signup);

        mRestaurentName = (EditText)findViewById(R.id.restaurentName);
        mManagerName = (EditText)findViewById(R.id.managerName);
        mManagerContact = (EditText)findViewById(R.id.managerContact);
        mRestaurentAddress = (EditText)findViewById(R.id.restaurentAddress);
        mOpenDay= (EditText)findViewById(R.id.openDay);
        mCloseDay = (EditText)findViewById(R.id.closeDay);
        mNext = (Button)findViewById(R.id.next);
        mOpenTime=(EditText)findViewById(R.id.openTime);
        mCloseTime=(EditText)findViewById(R.id.closeTime);


        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRestaurent();
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

            restaurent.child(uid).child("General").child("details").setValue(restaurentBean);
            Toast.makeText(getApplicationContext(),"Restaurent's General Details Set",Toast.LENGTH_SHORT).show();

            startActivity(new Intent(RestaurentSignup.this,RestaurentHome.class));
        }
        else {
            Toast.makeText(getApplicationContext(),"Please fill all the details",Toast.LENGTH_SHORT).show();

        }
    }



}
