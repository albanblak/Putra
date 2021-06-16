package com.example.putra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.gson.Gson;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;

public class EditPetsAcitivity2 extends AppCompatActivity {

   EditText etPetName, etPetType, etPetBreed, etPetGender, etPetSize, etPetBirth;
    AvatarView imageView;
    IImageLoader imageLoader = new PicassoLoader();
    Switch vaccinatedSwitch, neuteredSwitch;
    Button submitBtn;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pets_acitivity2);
        imageView = findViewById(R.id.selectImageViewPet);
        imageLoader.loadImage( imageView,"https://media.istockphoto.com/vectors/dog-icon-with-add-sign-labrador-retriever-icon-and-new-plus-positive-vector-id1145021752","test");
        etPetName = findViewById(R.id.etEditPetName);
        etPetType = findViewById(R.id.etEditPetType);
        etPetBreed = findViewById(R.id.etEditPetBreed);
        etPetGender = findViewById(R.id.etEditPetGender);
        etPetSize = findViewById(R.id.etEditPetSize);
        etPetBirth = findViewById(R.id.etEditPetBirth);
        vaccinatedSwitch = findViewById(R.id.vaccinatedSwitch);
        neuteredSwitch = findViewById(R.id.neuteredSwitch);
        submitBtn = findViewById(R.id.submitBtn);
        Intent intent = getIntent();
        String documentId = intent.getStringExtra("documentId");
        String strPetModel = intent.getStringExtra("strModel");

        Gson gson = new Gson();
        PetModel petModel = gson.fromJson(strPetModel, PetModel.class);

        String vaccinated = vaccinatedSwitch.isChecked() ? "true" : "false";
        String neutered = neuteredSwitch.isChecked() ? "true" : "false";

        etPetName.setText(petModel.getName());
        etPetType.setText(petModel.getType());
        etPetBreed.setText(petModel.getBreed());
        etPetGender.setText(petModel.getGender());
        etPetSize.setText(petModel.getSize());
        etPetBirth.setText(petModel.getYearOfBirth());

        if(petModel.getVaccinated().equals("true")){
            vaccinatedSwitch.setChecked(true);
        }else{
            vaccinatedSwitch.setChecked(false);
        }

       if(petModel.getNeutered().equals("true")) {
           neuteredSwitch.setChecked(true);
       }else{
           neuteredSwitch.setChecked(false);
       }


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               System.out.println("eshte klikuar imageView");
            }
        });


       submitBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               firestore.collection("pets")
               .document(documentId)

                       .update(
                               "name", etPetName.getText().toString(),
                               "breed",etPetBreed.getText().toString(),
                               "type",etPetType.getText().toString(),
                               "gender",etPetGender.getText().toString(),
                               "size", etPetSize.getText().toString(),
                               "yearOfBirth", etPetBirth.getText().toString(),
                               "vaccinated", vaccinatedSwitch.isChecked()? "true" : "false",
                               "neutered", neuteredSwitch.isChecked() ? "true" : "false"

                       ).addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {

                       firestore.collection("pets")
                               .document(documentId)
                               .get()
                               .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                   @Override
                                   public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                       if(task.isSuccessful()){
                                        PetModel    petModel = task.getResult().toObject(PetModel.class);
                                        String strPetModel = gson.toJson(petModel);
                                        Intent intent1 = new Intent(EditPetsAcitivity2.this, EditPetsActivity.class);
                                        intent1.putExtra("strPetModel", strPetModel);
                                        intent1.putExtra("documentId",task.getResult().getId());
                                        startActivity(intent1);
                                       }
                                   }
                               })
                               .addOnFailureListener(new OnFailureListener() {
                                   @Override
                                   public void onFailure(@NonNull Exception e) {
                                       Toast.makeText(EditPetsAcitivity2.this,"Nodhi diqka gabim", Toast.LENGTH_SHORT).show();

                                   }
                               });

                   }
               }).addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
                       Toast.makeText(EditPetsAcitivity2.this,"diqka ndodhi gabim",Toast.LENGTH_SHORT).show();
                   }
               });

           }
       });





    }








}