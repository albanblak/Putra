package com.example.putra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class BecomeSitterActivity extends AppCompatActivity {


   EditText service, description, price, title, city;
   Button submitBtn;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    SharedPreferences shp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_become_sitter);
        shp = getSharedPreferences(LoginActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        bind();

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               addData(getData());
            }
        });


    }

   private void bind(){
        service = findViewById(R.id.etService);
        description = findViewById(R.id.etDescription);
        price = findViewById(R.id.etPrice);
        submitBtn = findViewById(R.id.btnSubmit);
        title = findViewById(R.id.etSitterTitle);
        city = findViewById(R.id.etCity);
   }


   private void addData(Map<String, Object> sitter){
        firestore.collection("sitters")
        .add(sitter)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(BecomeSitterActivity.this,"u shtua me sukses",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("FireBase","Error adding document",e);
                    }
                });
   }


  private Map<String, Object> getData(){
        Map<String, Object> sitter = new HashMap<>();
        sitter.put("service",service.getText().toString());
        sitter.put("description",description.getText().toString());
        sitter.put("price",price.getText().toString());
        sitter.put("user",shp.getLong(LoginActivity.Id,-11));
        sitter.put("userName", shp.getString(LoginActivity.Name,"defValue"));
        sitter.put("title",title.getText().toString());
        sitter.put("id",PetId.generateUniqueId());
        sitter.put("city",city.getText().toString());
        sitter.put("imageUrl", shp.getString(LoginActivity.ImageUrl,"defValue"));

        return sitter;
  }


}