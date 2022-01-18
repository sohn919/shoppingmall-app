package com.github.sohn919.shopping;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.common.GoogleSignatureVerifier;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.text.SimpleDateFormat;
import java.util.Date;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.storage.UploadTask;
import com.john.waveview.WaveView;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;

    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = mDatabase.getReference();
    private FirebaseAuth firebaseAuth;


    private TextView textViewUserEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);


        GridView gridView = findViewById(R.id.gridView);
        GridListAdapter adapter = new GridListAdapter();


            myRef.child("Goods").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String code = "", name = "", image = "", brand = "", description = "", price = "";
                        for(DataSnapshot snapshot2 : snapshot.getChildren()) {
                            if("code".equals(snapshot2.getKey().toString())) {
                                code = snapshot2.getValue().toString();
                            }
                            if("name".equals(snapshot2.getKey().toString())) {
                                name = snapshot2.getValue().toString();
                            }
                            if("image".equals(snapshot2.getKey().toString())) {
                                image = snapshot2.getValue().toString();
                            }
                            if("brand".equals(snapshot2.getKey().toString())) {
                                brand = snapshot2.getValue().toString();
                            }
                            if("description".equals(snapshot2.getKey().toString())) {
                                description = snapshot2.getValue().toString();
                            }
                            if("price".equals(snapshot2.getKey().toString())) {
                                price = snapshot2.getValue().toString();
                            }
                        }
                        adapter.addItem(new ListItem(code, name, image, brand, description, price));
                        gridView.setAdapter(adapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {
                }
            });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final ListItem item = (ListItem) adapter.getItem(position);
                Intent intent = new Intent(getApplicationContext(), GoodsActivity.class);
                intent.putExtra("brand", item.getBrand());
                intent.putExtra("code", item.getCode());
                intent.putExtra("description", item.getDescription());
                intent.putExtra("image", item.getImage());
                intent.putExtra("name", item.getName());
                intent.putExtra("price", item.getPrice());
                startActivity(intent);
//                Toast.makeText(MainActivity.this, item.getName() + "클릭 이벤트 발생", Toast.LENGTH_SHORT).show();
            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers();

                int id = menuItem.getItemId();
                String title = menuItem.getTitle().toString();

                if(id == R.id.basket){
                    Intent intent = new Intent(MainActivity.this, BasketActivity.class);
                    startActivity(intent);
                }
                else if(id == R.id.enroll){
                    finish();
                    Intent intent = new Intent(MainActivity.this, EnrollActivity.class);
                    startActivity(intent);
                }
//                else if(id == R.id.admin){
//                    myRef.child("Users").child(user.getUid()).child("admin").addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                           Object admin = snapshot.getValue();
//                           Log.e("가져온거",""+admin);
//                           adminCheck = admin.toString();
//
//                            if(adminCheck.equals("0")){
//                                Toast.makeText(context, "관리자 계정이 아닙니다.", Toast.LENGTH_SHORT).show();
//                            }
//                            else{
//                                Intent intent = new Intent(MainActivity.this, ManageActivity.class);
//                                startActivity(intent);
//                                Toast.makeText(context, "관리자 메뉴", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//                        }
//                    });
//
//
//                }
                return true;
            }
        });


        //로그인 표시
        View header = navigationView.getHeaderView(0);
        textViewUserEmail = (TextView) header.findViewById(R.id.textViewUserEmail);
        //textViewUserEmail의 내용을 변경해 준다.
        textViewUserEmail.setText(user.getEmail() + "으로 로그인 하였습니다.");
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ // 왼쪽 상단 버튼 눌렀을 때
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }


}
