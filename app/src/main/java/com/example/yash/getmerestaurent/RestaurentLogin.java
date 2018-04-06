package com.example.yash.getmerestaurent;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yash.getmerestaurent.R;
import com.example.yash.getmerestaurent.RestaurentHome;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RestaurentLogin extends AppCompatActivity {

    private Button mSignin,mSignup;
    private EditText mEmail,mPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth=FirebaseAuth.getInstance();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
       /*if(user!=null){
            startActivity(new Intent(RestaurentLogin.this,RestaurentHome.class));
            finish();
            return;
        }*/
        setContentView(R.layout.activity_restaurent_login);

        mEmail=(EditText)findViewById(R.id.email);
        mPassword=(EditText)findViewById(R.id.password);
        mSignin=(Button)findViewById(R.id.signin);
        mSignup=(Button)findViewById(R.id.signup);
        mAuth=FirebaseAuth.getInstance();


        mSignin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String Semail=mEmail.getText().toString().trim();
                String Spassword=mPassword.getText().toString().trim();
                if(TextUtils.isEmpty(Semail) ){
                    Toast.makeText(getApplicationContext(),"Please enter email",Toast.LENGTH_SHORT).show();
                }
                if(TextUtils.isEmpty(Spassword) ){
                    Toast.makeText(getApplicationContext(),"Please enter password",Toast.LENGTH_SHORT).show();
                }
                mAuth.signInWithEmailAndPassword(Semail,Spassword).addOnCompleteListener(RestaurentLogin.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Sign in failed..",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            startActivity(new Intent(RestaurentLogin.this,RestaurentHome.class));
                            finish();
                            return;
                        }
                    }
                });

            }
        });


        mSignup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String email=mEmail.getText().toString().trim();
                String password=mPassword.getText().toString().trim();
                if(TextUtils.isEmpty(email) ){
                    Toast.makeText(getApplicationContext(),"Please enter email",Toast.LENGTH_SHORT).show();
                }
                if(TextUtils.isEmpty(password) ){
                    Toast.makeText(getApplicationContext(),"Please enter password",Toast.LENGTH_SHORT).show();
                }
                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(RestaurentLogin.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Sign Up failed..",Toast.LENGTH_SHORT).show();
                        }else {
                            String user_id = mAuth.getCurrentUser().getUid();
                            DatabaseReference current_user_uid = FirebaseDatabase.getInstance().getReference().child("Users").child("Restaurents").child(user_id);
                            current_user_uid.setValue(true);
                            startActivity(new Intent(RestaurentLogin.this,RestaurentSignup.class));
                            finish();
                            return;
                        }
                    }
                });

            }
        });

    }


}
