package com.example.putra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

public class DashboardActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;
    String stringModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dasboard);

        bottomNav = findViewById(R.id.bottom_navigator);

        bottomNav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FindFragment()).commit();

        Intent intent = getIntent();
        Gson gson = new Gson();
        stringModel = intent.getStringExtra("StringModel");
        System.out.println("ky eshte modeli" );
        System.out.println(stringModel);
        UserModel userModel = gson.fromJson(stringModel, UserModel.class);
        System.out.println( " getEmail: " + userModel.getEmail());
        System.out.println("getId: " + userModel.getId());
        System.out.println("getName: " +userModel.getName());


    }


   private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
       @Override
       public boolean onNavigationItemSelected(@NonNull MenuItem item) {
           Bundle bundle = new Bundle();
           bundle.putString("stringModel",stringModel);

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