package com.example.putra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

public class EditPetsActivity extends AppCompatActivity {
    TextView tvBreed,tvType,tvGender, tvSize,tvBirth, tvNeutered, tvVaccinated;
    Button btnEdit, deletePetBtn;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pets);


        tvBirth = findViewById(R.id.tvBirth);
        tvBreed = findViewById(R.id.tvBreed);
        tvType = findViewById(R.id.tvType);
        tvGender = findViewById(R.id.tvGender);
        tvSize = findViewById(R.id.tvSize);
        tvNeutered = findViewById(R.id.tvNeutered);
        tvVaccinated = findViewById(R.id.tvVaccinated);
        deletePetBtn = findViewById(R.id.deletePetBtn);

        Intent intent = getIntent();
        String petModelString = intent.getStringExtra("strPetModel");
        String documentId = intent.getStringExtra("documentId");
        Gson gsonParser = new Gson();
        PetModel petModel = gsonParser.fromJson(petModelString, PetModel.class);
        tvBreed.setText(petModel.getBreed());
        tvType.setText(petModel.getType());
        tvGender.setText(petModel.getGender());
        tvSize.setText(petModel.getSize());
        tvNeutered.setText(petModel.getNeutered());
        tvVaccinated.setText(petModel.getVaccinated());
        tvBirth.setText(petModel.getYearOfBirth());
        btnEdit = findViewById(R.id.btnEdit);



        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(EditPetsActivity.this, EditPetsAcitivity2.class);
                intent1.putExtra("strModel",intent.getStringExtra("strPetModel"));
                intent1.putExtra("documentId",documentId);
                startActivity(intent1);
            }
        });

       deletePetBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               firestore.collection("pets")
                       .document(documentId)
                       .delete()
                       .addOnSuccessListener(new OnSuccessListener<Void>() {
                           @Override
                           public void onSuccess(Void aVoid) {
                               Toast.makeText(EditPetsActivity.this, "U fshi me sukses", Toast.LENGTH_SHORT).show();
                               finish();
                           }
                       })
                       .addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception e) {
                               Toast.makeText(EditPetsActivity.this, "task failed + " + e.getMessage(), Toast.LENGTH_SHORT).show();
                           }
                       });
           }
       });



    }
}