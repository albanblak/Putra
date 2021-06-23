package com.example.putra;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;

public class SitterDetailsActivity extends AppCompatActivity {

   AvatarView avatarView;
   ImageView imageViewBig, favoriteIcon, viewIcon, clockIcon;
   TextView tvName, tvCity, tvDescription, tvService, tvPrice;
   Button contactBtn;

   PicassoLoader picassoLoader = new PicassoLoader();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sitter_details);
        Gson gson = new Gson();
        Intent intent = getIntent();
        String sitterModelStr = intent.getStringExtra("sitterModelStr");
        SitterModel sitterModel = gson.fromJson(sitterModelStr,SitterModel.class);
        System.out.println(sitterModelStr);

       avatarView = findViewById(R.id.avatarView);
       imageViewBig = findViewById(R.id.imageView);
       favoriteIcon = findViewById(R.id.favoriteIcon);
       viewIcon = findViewById(R.id.viewIcon);
       tvName = findViewById(R.id.tvName);
       tvCity = findViewById(R.id.tvCity);
       tvDescription = findViewById(R.id.tvDescription);
       clockIcon = findViewById(R.id.clocIcon);
       tvService = findViewById(R.id.tvService);
       tvPrice = findViewById(R.id.tvPrice);
       contactBtn = findViewById(R.id.contactBtn);

       contactBtn.setText("Contact " +  sitterModel.getUserName());

       clockIcon.setImageResource(R.drawable.ic_baseline_access_time_24);
       tvName.setText(sitterModel.getUserName());
       favoriteIcon.setImageResource(R.drawable.ic_baseline_favorite_border_24);
       viewIcon.setImageResource(R.drawable.ic_baseline_remove_red_eye_24);
       tvCity.setText(sitterModel.getCity());
       tvDescription.setText(sitterModel.getDescription());
       tvService.setText(sitterModel.getService());
       tvPrice.setText(sitterModel.getPrice() + " €/day");

        picassoLoader.loadImage(avatarView,sitterModel.getImageUrl(),"test");
        Picasso.with(this).load(sitterModel.getImageUrl()).into(imageViewBig);




    }
}