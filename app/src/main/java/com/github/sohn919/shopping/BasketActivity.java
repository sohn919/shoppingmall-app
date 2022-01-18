package com.github.sohn919.shopping;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;


public class BasketActivity extends AppCompatActivity {

    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = mDatabase.getReference();
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

        firebaseAuth = FirebaseAuth.getInstance();


        //유저가 로그인 하지 않은 상태라면 null 상태이고 이 액티비티를 종료하고 로그인 액티비티를 연다.
        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        //유저가 있다면, null이 아니면 계속 진행
        FirebaseUser user = firebaseAuth.getCurrentUser();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu); //뒤로가기 버튼 이미지 지정


        ListView listView = findViewById(R.id.listview);
        ListViewAdapter adapter = new ListViewAdapter();



        myRef.child("Users").child(user.getUid()).child("basket").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String code = "", name = "", image = "", brand = "", description = "", price = "", check = "";
                    for (DataSnapshot snapshot2 : snapshot.getChildren()) {
                        if ("code".equals(snapshot2.getKey().toString())) {
                            code = snapshot2.getValue().toString();
                        }
                        if ("name".equals(snapshot2.getKey().toString())) {
                            name = snapshot2.getValue().toString();
                        }
                        if ("image".equals(snapshot2.getKey().toString())) {
                            image = snapshot2.getValue().toString();
                        }
                        if ("brand".equals(snapshot2.getKey().toString())) {
                            brand = snapshot2.getValue().toString();
                        }
                        if ("description".equals(snapshot2.getKey().toString())) {
                            description = snapshot2.getValue().toString();
                        }
                        if ("price".equals(snapshot2.getKey().toString())) {
                            price = snapshot2.getValue().toString();
                        }
                        if ("check".equals(snapshot2.getKey().toString())) {
                            check = snapshot2.getValue().toString();
                        }
                    }

                    adapter.addItem(new BasketItem(code, name, image, brand, description, price, check));
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final ListItem item = (ListItem) adapter.getItem(position);
                Intent intent = new Intent(getApplicationContext(), GoodsActivity.class);
                intent.putExtra("code", item.getCode());
                startActivity(intent);
            }
        });


    }
}
