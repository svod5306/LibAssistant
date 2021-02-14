package com.example.libassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;


import java.util.Hashtable;
import com.example.libassistant.zxing.encode.CodeCreator;

public class ExchangeBookActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            setContentView(R.layout.activity_exchange_book);
        }catch (Exception e){
            e.printStackTrace();
        }
        //setContentView(R.layout.activity_exchange_book);

        String barData=getIntent().getStringExtra("barData");
        //Bitmap barCode=createQRCodeBitmap(barData,200,200,"UTF-8","L","0", Color.BLACK,Color.WHITE);
        try{
            Bitmap barCode=CodeCreator.createQRCode(barData);
            ImageView imageView=findViewById(R.id.barCode);
            imageView.setImageBitmap(barCode);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
