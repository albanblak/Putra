package com.example.putra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    SharedPreferences shp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        shp = getSharedPreferences(LoginActivity.MyPREFERENCES, Context.MODE_PRIVATE);
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

      if(!shp.getBoolean(LoginActivity.IsLoggedIn, false)){
          System.out.println(shp.getBoolean(LoginActivity.IsLoggedIn,false));
         // Intent intent = new Intent(EditProfile.this, MainActivity.class);
          //startActivity(intent);
          finish();
      }


      aboutMeLayout.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent = new Intent(EditProfile.this, EditProfile2.class);
              startActivity(intent);
          }
      });

      becomeSitterLayout.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent = new Intent(EditProfile.this, BecomeSitterActivity.class);
              startActivity(intent);
          }
      });


      profilePicture.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent = new Intent(EditProfile.this, UploadImageActivity.class);
              startActivity(intent);

          }
      });

      setImage(new FirebaseCallBack() {
          @Override
          public void onCallBack(String string) {
              if(string == ""){

                  picassoLoader.loadImage(profilePicture,"https://st.depositphotos.com/2101611/3925/v/600/depositphotos_39258143-stock-illustration-businessman-avatar-profile-picture.jpg","test");
              }else{

                  picassoLoader.loadImage(profilePicture,string,"test");
              }
          }
      });

      logoutLayout.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              SharedPreferences.Editor editor = shp.edit();
              shp.edit().clear().apply();

              editor.putBoolean(LoginActivity.IsLoggedIn,false);
              editor.apply();
              System.out.println(shp.getBoolean(LoginActivity.IsLoggedIn,false));
              System.out.println(shp.getString(LoginActivity.Name,"defValue"));
              Intent intent = new Intent(EditProfile.this, MainActivity.class);
              startActivity(intent);
              finish();
          }
      });




    }


   public void setImage(FirebaseCallBack firebaseCallBack){

       firestore.collection("users")
               .document(shp.getString(LoginActivity.DocumentId,"defValue"))
               .get()
               .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                   @Override
                   public void onSuccess(DocumentSnapshot documentSnapshot) {
                        firebaseCallBack.onCallBack(documentSnapshot.getString("imageUrl"));
                   }
               })
               .addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {

                   }
               });
   }


    interface FirebaseCallBack{
        public void onCallBack(String string);
   }



}