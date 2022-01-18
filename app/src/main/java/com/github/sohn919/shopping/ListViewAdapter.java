package com.github.sohn919.shopping;


import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {
    ArrayList<BasketItem> items = new ArrayList<BasketItem>();
    Context context;

    public void addItem(BasketItem item) {
        items.add(item);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        context = parent.getContext();
        BasketItem basketItem = items.get(position);

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_basket, parent, false);
        }

        TextView name = convertView.findViewById(R.id.name2);
        TextView brand = convertView.findViewById(R.id.brand);
        TextView price = convertView.findViewById(R.id.price);
        ImageView image = convertView.findViewById(R.id.image2);

        name.setText(basketItem.getName());
        brand.setText(basketItem.getBrand());
        price.setText(basketItem.getPrice());

        FirebaseStorage storage = FirebaseStorage.getInstance("gs://shoppingmall-2afe1.appspot.com");
        StorageReference storageRef = storage.getReference();
        storageRef.child("images/" + basketItem.getImage()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //이미지 로드 성공시
                Glide.with(context)
                        .load(uri)
                        .into(image);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                //이미지 로드 실패시
                Toast.makeText(context, "실패", Toast.LENGTH_SHORT).show();
            }
        });

        ViewGroup.LayoutParams layoutParams = convertView.getLayoutParams();
        layoutParams.height = 330;

        return convertView;
    }

}

