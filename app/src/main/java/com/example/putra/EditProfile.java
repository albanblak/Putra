package com.example.putra;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;

public class EditProfile extends AppCompatActivity {

   LinearLayout aboutMeLayout;
   LinearLayout becomeSitterLayout;
   LinearLayout notificationLayout;
   LinearLayout logoutLayout;
   AvatarView profilePicture;
   ImageView aboutMeIcon;
   ImageView becomeSitterIcon;
   ImageView iconSettings;
   ImageView iconLogout;


   PicassoLoader picassoLoader = new PicassoLoader();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

       aboutMeLayout = findViewById(R.id.aboutMeLayout);
       becomeSitterLayout = findViewById(R.id.becomeSitterLayout);
       profilePicture = findViewById(R.id.imageViwerProfile);
       aboutMeIcon = findViewById(R.id.iconAboutMe);
       becomeSitterIcon = findViewById(R.id.iconBecomeSitter);
       notificationLayout = findViewById(R.id.notificationLayout);
       logoutLayout = findViewById(R.id.logoutLayout);
       iconLogout = findViewById(R.id.iconLogout);
       iconSettings = findViewById(R.id.iconSettings);
        picassoLoader.loadImage( profilePicture,"https://www.bengtolcollege.ac.in/wp-content/uploads/2020/07/thesomeday123170900021.jpg","test");
      aboutMeIcon.setImageResource(R.drawable.ic_baseline_person_outline_24);
      becomeSitterIcon.setImageResource(R.drawable.ic_baseline_payment_24);
      iconSettings.setImageResource(R.drawable.ic_baseline_settings_24);
      iconLogout.setImageResource(R.drawable.ic_baseline_logout_24);



      aboutMeLayout.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent = new Intent(EditProfile.this, EditProfile2.class);
              startActivity(intent);
          }
      });




    }
}