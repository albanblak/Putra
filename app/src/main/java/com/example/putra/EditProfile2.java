package com.example.putra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditProfile2 extends AppCompatActivity {


    EditText name, lastname, email, phone;
    Button submitBtn;
    SharedPreferences shp;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile2);
        shp = getSharedPreferences(LoginActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        long id = shp.getLong(LoginActivity.Id,-1);
        String documentId = shp.getString(LoginActivity.DocumentId,"test");
        System.out.println("Name nga editprofile2" + shp.getString(LoginActivity.Name,"testName"));
        System.out.println(documentId);
        bind();
        setData();



        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData(documentId,name.getText().toString(),lastname.getText().toString(),email.getText().toString(),phone.getText().toString(),id);
            }
        });
    }




   public void bind(){
       name = findViewById(R.id.etFirstName);
       lastname = findViewById(R.id.etLastname);
       email = findViewById(R.id.etEmail);
       phone = findViewById(R.id.etPhone);
       submitBtn = findViewById(R.id.submitBtnProfile);

   }


   public void setData(){
        name.setText(shp.getString(LoginActivity.Name,"test"));
        lastname.setText(shp.getString(LoginActivity.Lastame,"test"));
        email.setText(shp.getString(LoginActivity.Email,"test"));
   }









   public void updateData(String documentId, String name, String lastname, String email, String phone, long id){
        System.out.println("nga funksioni " + documentId);
        firestore.collection("users")
            .document(documentId)
            .update(                "name",name,
                    "lastname",lastname,
                                        "email",email,
                                        "phone",phone)

            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(EditProfile2.this,"Te dhenat u ndryshuan me sukses",Toast.LENGTH_SHORT).show();

                    finish();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditProfile2.this,"Nodhi nje gabim",Toast.LENGTH_SHORT).show();
                }
            });


       SQLiteDatabase db = new DatabaseHelper(EditProfile2.this).getWritableDatabase();
       ContentValues cv = new ContentValues();
       cv.put("name",name);
       cv.put("lastname",lastname);
       cv.put("email",email);
       db.update(DatabaseHelper.TABLE_NAME,cv,"id = ?", new String[]{String.valueOf(id)});
       SharedPreferences.Editor editor = shp.edit();
       editor.putString(LoginActivity.Name,name);
       editor.putString(LoginActivity.Lastame,lastname);
       editor.putString(LoginActivity.Email,email);
       editor.apply();



   }



}