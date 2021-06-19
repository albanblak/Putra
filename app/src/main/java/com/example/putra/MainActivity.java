package com.example.putra;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button logBtnDirect, regBtnDirect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logBtnDirect = findViewById(R.id.logBtnDirect);
        regBtnDirect = findViewById(R.id.regBtnDirect);

       SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.MyPREFERENCES,MODE_PRIVATE);
        boolean isLogedIn = sharedPreferences.getBoolean(LoginActivity.IsLoggedIn,false);
        if(isLogedIn){
           Intent intent = new Intent(MainActivity.this,DashboardActivity.class);
           //finish();
      }


        regBtnDirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
                finish();


            }
        });

        logBtnDirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.MyPREFERENCES,MODE_PRIVATE);
                boolean isLoggedIn = sharedPreferences.getBoolean(LoginActivity.IsLoggedIn,false);
                if(isLoggedIn == true){
                    System.out.println("is finished");
                    System.out.println(getSharedPreferences(LoginActivity.MyPREFERENCES,MODE_PRIVATE).getString(LoginActivity.Name, "emri"));
                    finish();
                }

                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

    }
}