package com.example.yash.getmerestaurent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.yash.getmerestaurent.R;
import com.example.yash.getmerestaurent.RestaurentAddMenuIteam;
import com.example.yash.getmerestaurent.RestaurentMenuAddItemBean;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RestaurentShowMenu extends AppCompatActivity {

    Button mAddItem;
    private FirebaseAuth mAuth= FirebaseAuth.getInstance();
    private String uid=mAuth.getCurrentUser().getUid();
    private DatabaseReference restaurent  = FirebaseDatabase.getInstance().getReference("Users").child("Restaurents");
    private FirebaseUser user;
    private RestaurentMenuAddItemBean restaurentMenuAddItemBean;
    private Button mBack;
    RecyclerView recyclerView;
    RestaurentMenuAdapter restaurentMenuAdapter;
    List<RestaurentMenuAddItemBean> menuAddItemBeanList;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth= FirebaseAuth.getInstance();
        uid=mAuth.getCurrentUser().getUid();
        restaurent  = FirebaseDatabase.getInstance().getReference("Users").child("Restaurents").child(uid).child("Menu");

        restaurent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                menuAddItemBeanList.clear();

                for(DataSnapshot item: dataSnapshot.getChildren()){
                    restaurentMenuAddItemBean=item.getValue(RestaurentMenuAddItemBean.class);
                    menuAddItemBeanList.add(restaurentMenuAddItemBean);
                }
                restaurentMenuAdapter=new RestaurentMenuAdapter(RestaurentShowMenu.this,menuAddItemBeanList);
                recyclerView.setAdapter(restaurentMenuAdapter);
                recyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurent_show_menu);

        mAddItem=findViewById(R.id.addItem);
        recyclerView=findViewById(R.id.recycleView);
        mBack=findViewById(R.id.mback);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        menuAddItemBeanList=new ArrayList<>();


        mAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RestaurentShowMenu.this,RestaurentAddMenuIteam.class));
                finish();
                return;

            }
        });
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RestaurentShowMenu.this,RestaurentHome.class));
                finish();
            }
        });
    }
}
