package com.example.putra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import agency.tango.android.avatarview.views.AvatarView;

public class AddPetActivity extends AppCompatActivity {

   AvatarView avatarView;
   EditText name, type, breed, gender, size, birth;
   Button submitBtn;
   Switch vaccinatedSwitch, neuteredSwitch;

   FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    SharedPreferences shp;
    PetAdapter petAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pet);
        shp = getSharedPreferences(LoginActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        bind();

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addPet(getData());
            }
        });


    }




   public void bind(){
        name = findViewById(R.id.etEditPetName);
        type = findViewById(R.id.etEditPetType);
        breed = findViewById(R.id.etEditPetBreed);
        gender = findViewById(R.id.etEditPetGender);
        size = findViewById(R.id.etEditPetSize);
        birth = findViewById(R.id.etEditPetBirth);
        avatarView = findViewById(R.id.selectImageViewPet);
        submitBtn = findViewById(R.id.submitBtn);
        vaccinatedSwitch = findViewById(R.id.vaccinatedSwitch);
        neuteredSwitch = findViewById(R.id.neuteredSwitch);
   }




   public void addPet(Map<String, Object> pet){
    /*  Map<String, Object> pet = new HashMap<>();

      pet.put("name",name);
      pet.put("type",type);
      pet.put("gender",gender);
      pet.put("breed",breed);
      pet.put("size",size);
      pet.put("id",id);
      pet.put("user",userId);
     */
      firestore.collection("pets")
              .add(pet)
              .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                  @Override
                  public void onSuccess(DocumentReference documentReference) {
                      Toast.makeText(AddPetActivity.this,"Eshte shtuar me sukses",Toast.LENGTH_SHORT).show();
                        finish();
                  }
              })
              .addOnFailureListener(new OnFailureListener() {
                  @Override
                  public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddPetActivity.this, "Diqka ndodhi gabim",Toast.LENGTH_SHORT).show();
                  }
              });

   }

   public Map<String, Object> getData(){
        Map<String, Object> pet = new HashMap<>();

       pet.put("name",name.getText().toString());
       pet.put("type",type.getText().toString());
       pet.put("gender",gender.getText().toString());
       pet.put("breed",breed.getText().toString());
       pet.put("size",size.getText().toString());
       pet.put("yearOfBirth",birth.getText().toString());
       pet.put("id",PetId.generateUniqueId()+"");
       pet.put("user",shp.getLong(LoginActivity.Id,-1)+"");

       if(vaccinatedSwitch.isChecked()){
           pet.put("vaccinated","true");
       }else{
           pet.put("vaccinated","false");
       }

       if(neuteredSwitch.isChecked()){
           pet.put("neutered","true");
       }else{
           pet.put("neutered","false");
       }


       return pet;

   }


}