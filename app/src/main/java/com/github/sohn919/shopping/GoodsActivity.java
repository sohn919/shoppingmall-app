package com.github.sohn919.shopping;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class GoodsActivity extends AppCompatActivity {

    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = mDatabase.getReference();
    private FirebaseAuth firebaseAuth;

    protected void onCreate(Bundle savedInstanceStare) {
        super.onCreate(savedInstanceStare);
        setContentView(R.layout.activity_goods);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        GridListAdapter adapter = new GridListAdapter();


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Users");

        Intent intent = getIntent();
        String brand = intent.getStringExtra("brand");
        String code = intent.getStringExtra("code");
        String description = intent.getStringExtra("description");
        String image = intent.getStringExtra("image");
        String name = intent.getStringExtra("name");
        String price = intent.getStringExtra("price");

        Log.d("현재값????????????????????????????", user.getUid());
        Log.d("현재값????????????????????????????", user.getUid());
        Log.d("현재값????????????????????????????", user.getUid());

        reference.child(user.getUid()).child("basket").child(code).child("brand").setValue(brand);
        reference.child(user.getUid()).child("basket").child(code).child("code").setValue(code);;
        reference.child(user.getUid()).child("basket").child(code).child("description").setValue(description);;
        reference.child(user.getUid()).child("basket").child(code).child("image").setValue(image);;
        reference.child(user.getUid()).child("basket").child(code).child("name").setValue(name);;
        reference.child(user.getUid()).child("basket").child(code).child("price").setValue(price);;
        reference.child(user.getUid()).child("basket").child(code).child("check").setValue(0);;

    }

}

