package com.example.putra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    EditText etEmail, etPassword;
    Button btnLogin;
    String stringModel;
    UserModel userModel = new UserModel();
    String test;
    SharedPreferences sharedPreferences;

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Id = "idKey";
    public static final String Name = "nameKey";
    public static final String Lastame = "lastnameKey";
    public static final String Email = "emailKey";
    public static final String Password = "passwordKey";
    public static final String IsLoggedIn  = "loggKey";
    public static final String DocumentId = "documentIdKey";
    public static final String ImageUrl = "imageUrlKey";

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.loginBtn);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int status = Loginuser(etEmail.getText().toString(),etPassword.getText().toString());
                if(status == 1){
                    readData(new FirestoreCallback() {
                        @Override
                        public void onCallback(String string) {
                            System.out.println("nga callbacki : " + string );
                            userModel.setDocumentId(string);


                            System.out.println("1" + test);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putLong(Id, userModel.getId());
                            editor.putString(Name,userModel.getName());
                            editor.putString(Lastame, userModel.getLastname());
                            editor.putString(Email, userModel.getEmail());
                            editor.putString(Password, userModel.getPassword());
                            editor.putBoolean(IsLoggedIn,true);
                            editor.putString(DocumentId,userModel.getDocumentId());
                            editor.apply();



                            // Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                            //intent.putExtra("StringModel",stringModel);
                            //startActivity(intent);
                            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                            startActivity(intent);
                            System.out.println("u klikua");
                            finish();


                        }
                    });







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

            userModel.setId(cursor.getInt(0));
            userModel.setEmail(cursor.getString(1));
            userModel.setPassword(cursor.getString(2));
            userModel.setName(cursor.getString(3));
            userModel.setLastname(cursor.getString(4));

            //getFromFirestoreProfile(userModel.getId());


            System.out.println("ky eshte testi " + test);


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






  private void readData(FirestoreCallback firestoreCallback) {
      firestore.collection("users")
              .whereEqualTo("id",userModel.getId())
              .get()
              .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                  @Override
                  public void onComplete(@NonNull Task<QuerySnapshot> task) {
                      if (task.isSuccessful()){

                          for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                             test = documentSnapshot.getId();
                          }

                          firestoreCallback.onCallback(test);
                      }
                  }
              })
              .addOnFailureListener(new OnFailureListener() {
                  @Override
                  public void onFailure(@NonNull Exception e) {
                      Toast.makeText(LoginActivity.this,"ndodhi nje gabim",Toast.LENGTH_SHORT).show();
                  }
              });



  }



    private interface FirestoreCallback{
        void onCallback(String string);
    }


 }