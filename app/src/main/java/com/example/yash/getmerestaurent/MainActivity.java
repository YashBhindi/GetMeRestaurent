package com.example.yash.getmerestaurent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button mCustomer,mRestaurent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCustomer=(Button)findViewById(R.id.cutomer);
        mRestaurent=(Button)findViewById(R.id.restaurent);

        mCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,CustomerLogin.class));
                finish();
                return;
            }
        });


        mRestaurent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,RestaurentLogin.class));
                finish();
                return;
            }
        });

    }
}
