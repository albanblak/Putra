package com.example.putra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText etName, etLastname, etEmail, etPassword;
    Button registerBtn;
    int idLong;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    UserModel userModel = new UserModel();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = findViewById(R.id.etName);
        etLastname = findViewById(R.id.etLastname);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        registerBtn = findViewById(R.id.registerButton);






        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase objDb = new DatabaseHelper(RegisterActivity.this).getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                //contentValues.put(DatabaseHelper.ID, IdHelper.getID());
                contentValues.put(DatabaseHelper.NAME,etName.getText().toString());
                contentValues.put(DatabaseHelper.LASTNAME,etLastname.getText().toString());
                contentValues.put(DatabaseHelper.EMAIL, etEmail.getText().toString());
                contentValues.put(DatabaseHelper.PASSWORD, etPassword.getText().toString());





                try{
                    long id = objDb.insert(DatabaseHelper.TABLE_NAME,null,contentValues);
                    if(id > 0){
                       // Loginuser(etEmail.getText().toString(),etPassword.getText().toString());
                        //addToFireStore((int) id,etName.getText().toString(),etLastname.getText().toString(),etEmail.getText().toString(),etPassword.getText().toString());

                        Loginuser(DatabaseHelper.EMAIL,DatabaseHelper.PASSWORD);
                        userModel.setId(idLong);
                        userModel.setName(etName.getText().toString());
                        userModel.setLastname(etLastname.getText().toString());
                        userModel.setEmail(etEmail.getText().toString());
                        userModel.setPassword(etPassword.getText().toString());
                        Gson gsonParser = new Gson();
                        String stringModel = gsonParser.toJson(userModel);

                        addToFireStore((int)id,etName.getText().toString(),etLastname.getText().toString(),etEmail.getText().toString(),etPassword.getText().toString());
                        Intent intent = new Intent(RegisterActivity.this,DashboardActivity.class);
                        intent.putExtra("StringModel",stringModel);
                        startActivity(intent);

                    }else{
                        Toast.makeText(RegisterActivity.this,"Nodhi diqka gabim",Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
                finally {
                    objDb.close();
                }



            }
        });

    }
    private int Loginuser(String email, String password){
        SQLiteDatabase objDb = new DatabaseHelper(RegisterActivity.this).getReadableDatabase();
        Cursor cursor = objDb.query(false,DatabaseHelper.TABLE_NAME,new String[]{DatabaseHelper.ID,DatabaseHelper.EMAIL, DatabaseHelper.PASSWORD},"Email"+ "=?",new String[]{email},"","","","");
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            idLong = cursor.getInt(0);
            System.out.println("kjo eshte id: " + idLong );
            String dbUserEmail = cursor.getString(0);
            String dbUserPassword = cursor.getString(1);
            System.out.println(dbUserEmail);
            System.out.println(dbUserPassword);


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


    private void addToFireStore(int id, String name, String lastname, String email, String password){

     Map<String, Object> user = new HashMap<>();
        user.put("id",id);
        user.put("name",name);
        user.put("lastname",lastname);
        user.put("email",email);
        user.put("password",password);
        user.put("imageUrl","https://st.depositphotos.com/2101611/3925/v/600/depositphotos_39258143-stock-illustration-businessman-avatar-profile-picture.jpg");
        user.put("phone","");


      db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("FIREBASE","DocumentSnapshot added with ID: "  + documentReference.getId());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                System.out.println("eshte kryer me sukses");
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                System.out.println("ka deshtuar");
                                Log.w("FIREBASE", "Error adding document", e);
                            }
                        });
                    }
                });

   /*     db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document: task.getResult()){
                                Log.d("Fire", document.getId() + "==> " + document.getData());
                            }
                        }
                    }
                });

*/

    }



}