package com.example.putra;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

public class EditPetsActivity extends AppCompatActivity {
    TextView tvBreed,tvType,tvGender, tvSize,tvBirth, tvNeutered, tvVaccinated;
    Button btnEdit;
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


    }
}