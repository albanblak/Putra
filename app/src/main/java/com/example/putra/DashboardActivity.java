package com.example.putra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

public class DashboardActivity extends AppCompatActivity {

   FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    UserModel userModel = new UserModel();

    BottomNavigationView bottomNav;
    String stringModel;


    @Override
    public void onBackPressed() {
       moveTaskToBack(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dasboard);
        bottomNav = findViewById(R.id.bottom_navigator);


        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        userModel.setId((int) sharedPreferences.getLong(LoginActivity.Id,01));
        userModel.setName(sharedPreferences.getString(LoginActivity.Name,"test"));
        userModel.setLastname(sharedPreferences.getString(LoginActivity.Lastame,"test"));
        userModel.setEmail(sharedPreferences.getString(LoginActivity.Email,"test"));
        userModel.setPassword(sharedPreferences.getString(LoginActivity.Password,"test"));


/*
        Intent intent = getIntent();
        Gson gson = new Gson();
        stringModel = intent.getStringExtra("StringModel");
        System.out.println(stringModel);

        String userId = intent.getStringExtra("userId");
        if(stringModel == null || stringModel.isEmpty()){
            System.out.println("hi ne if");
            System.out.println(userId);
           firestore.collection("users")
                   .whereEqualTo("id",userId)
                   .get()
                   .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                       @Override
                       public void onComplete(@NonNull Task<QuerySnapshot> task) {
                           if(task.isSuccessful()){
                               for(QueryDocumentSnapshot document: task.getResult()) {
                                   runOnUiThread(new Runnable() {
                                       @Override
                                       public void run() {
                                            userModel = document.toObject(UserModel.class);
                                           System.out.println(document.getId());
                                           stringModel = gson.toJson(userModel);
                                       }
                                   });
                               }
                           }else{
                               runOnUiThread(new Runnable() {
                                   @Override
                                   public void run() {

                                       System.out.println("nuk eshte succesfull");
                                   }
                               });
                           }
                       }
                   })
                   .addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception e) {
                           System.out.println("deshtoi");
                       }
                   });

        }else{
            System.out.println("nuk hini ne if");
            userModel = gson.fromJson(stringModel, UserModel.class);
        }
//        System.out.println( " getEmail: " + userModel.getEmail());
 //       System.out.println("getId: " + userModel.getId());
  //      System.out.println("getName: " +userModel.getName());

 */
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FindFragment()).commit() ;
    }


   private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
       @Override
       public boolean onNavigationItemSelected(@NonNull MenuItem item) {
           Bundle bundle = new Bundle();
           bundle.putString("stringModel",stringModel);
           System.out.println("On  :" + stringModel);

           Fragment selectedFragment = null;

           switch (item.getItemId()){
               case R.id.nav_setting:
                   selectedFragment = new SettingsFragment();

                   break;
               case R.id.nav_find :
                   selectedFragment = new FindFragment();
                   break;
               case R.id.nav_messages:
                   selectedFragment = new MessageFragment();
                   break;
           }
           selectedFragment.setArguments(bundle);
           getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
           return  true;
       }
   };


}