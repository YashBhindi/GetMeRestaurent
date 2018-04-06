package com.example.yash.getmerestaurent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yash.getmerestaurent.R;
import com.example.yash.getmerestaurent.RestaurentMenuAddItemBean;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RestaurentAddMenuIteam extends AppCompatActivity {

    private Button mAdd;
    private EditText mCategory,mName,mPrice;
    private TextView mC,mN,mP;
    private FirebaseAuth mAuth;
    private DatabaseReference MenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurent_add_menu_iteam);

        mAdd=findViewById(R.id.add);
        mC=findViewById(R.id.c);
        mN=findViewById(R.id.n);
        mP=findViewById(R.id.p);
        mName=findViewById(R.id.name);
        mCategory=findViewById(R.id.category);
        mPrice=findViewById(R.id.price);
        String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        MenuItem= FirebaseDatabase.getInstance().getReference("Users").child("Restaurents").child(uid).child("Menu");
        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=mName.getText().toString().trim();
                String category=mCategory.getText().toString().trim();
                double price=Double.parseDouble(mPrice.getText().toString());

                RestaurentMenuAddItemBean item = new RestaurentMenuAddItemBean(category,name,price);
                MenuItem.push().setValue(item);
                Toast.makeText(getApplicationContext(),"Item added",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RestaurentAddMenuIteam.this,RestaurentHome.class));

            }
        });

    }
}
