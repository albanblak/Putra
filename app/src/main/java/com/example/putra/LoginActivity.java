package com.example.putra;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    EditText etEmail, etPassword;
    Button btnLogin;

    List<String> userData = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.loginBtn);



        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int status = Loginuser(etEmail.getText().toString(),etPassword.getText().toString());
                if(status == 1){
                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                    intent.putExtra("email",userData.get(0));
                    intent.putExtra("password",userData.get(1));
                    startActivity(intent);
                }else if(status == -1){
                    Toast.makeText(LoginActivity.this,"User dont exists",Toast.LENGTH_SHORT).show();
                }else if(status == 0) {
                    Toast.makeText(LoginActivity.this, "Wrong  password", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(LoginActivity.this,"Spo bon hiq",Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    private int Loginuser(String email, String password){
        SQLiteDatabase objDb = new DatabaseHelper(LoginActivity.this).getReadableDatabase();
        Cursor cursor = objDb.query(false,DatabaseHelper.TABLE_NAME,new String[]{DatabaseHelper.EMAIL, DatabaseHelper.PASSWORD},"Email"+ "=?",new String[]{email},"","","","");
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            String dbUserEmail = cursor.getString(0);
            String dbUserPassword = cursor.getString(1);
            userData.add(dbUserEmail);
            userData.add(dbUserPassword);
            cursor.close();
            objDb.close();
            if(password.equals(dbUserPassword)){
                return 1;
            }else{
                return 0;
            }
        }
        return  -1;
    }
 }