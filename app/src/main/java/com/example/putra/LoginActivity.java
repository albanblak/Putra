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

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    EditText etEmail, etPassword;
    Button btnLogin;


    String stringModel;
    UserModel userModel = new UserModel();
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
                    intent.putExtra("StringModel",stringModel);
                    startActivity(intent);
                    System.out.println("u klikua");
                }else if(status == -1){
                    Toast.makeText(LoginActivity.this,"User dont exists",Toast.LENGTH_SHORT).show();
                    System.out.println("nuk u klikua");
                }else if(status == 0) {
                    System.out.println("nuk u klikua 1");
                    Toast.makeText(LoginActivity.this, "Wrong  password", Toast.LENGTH_SHORT).show();
                }else {
                    System.out.println("nuk u klikua 2");
                    Toast.makeText(LoginActivity.this,"Spo bon hiq",Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    private int Loginuser(String email, String password){
        SQLiteDatabase objDb = new DatabaseHelper(LoginActivity.this).getReadableDatabase();
        Cursor cursor = objDb.query(false,DatabaseHelper.TABLE_NAME,new String[]{DatabaseHelper.ID, DatabaseHelper.EMAIL, DatabaseHelper.PASSWORD, DatabaseHelper.NAME, DatabaseHelper.LASTNAME},"Email"+ "=?",new String[]{email},"","","","");
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            String dbUserEmail = cursor.getString(0);
            String dbUserPassword = cursor.getString(2);
            System.out.println("POshte jane nga cursori");
            System.out.println(cursor.getInt(0));
            System.out.println(cursor.getString(1));
            System.out.println(cursor.getString(2));
            System.out.println(cursor.getString(3));
            System.out.println(cursor.getString(4));
            userModel.setId(cursor.getInt(0));
            userModel.setEmail(cursor.getString(1));
            userModel.setPassword(cursor.getString(2));
            userModel.setName(cursor.getString(3));
            userModel.setLastname(cursor.getString(4));

            Gson gsonParser = new Gson();
             stringModel = gsonParser.toJson(userModel);

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