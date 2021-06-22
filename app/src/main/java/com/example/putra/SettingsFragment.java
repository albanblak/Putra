package com.example.putra;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;

public class SettingsFragment extends Fragment {
    Button btnAddPet;
    String documentId;
    ListView petListView;
    PetAdapter petAdapter;
    RelativeLayout meLayout;
    TextView tvName;
    TextView tvEditProfile;
    AvatarView imageView;
    IImageLoader imageLoader;

    FirebaseFirestore ff = FirebaseFirestore.getInstance();
    CollectionReference pets = ff.collection("pets");
    SharedPreferences sharedPreferences;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_settings, container, false);
        String  stringModel = getArguments().getString("stringModel");
        Gson gsonParser = new Gson();
       // UserModel userModel = gsonParser.fromJson(stringModel,UserModel.class);
        sharedPreferences = this.getActivity().getSharedPreferences(LoginActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        if (!sharedPreferences.getBoolean(LoginActivity.IsLoggedIn,false)){
            getActivity().finish();
        }

        UserModel userModel = new UserModel();
        userModel.setEmail(sharedPreferences.getString(LoginActivity.Email,null));
        userModel.setName(sharedPreferences.getString(LoginActivity.Name,null));
        userModel.setId((int) sharedPreferences.getLong(LoginActivity.Id,0));
        userModel.setLastname(sharedPreferences.getString(LoginActivity.Lastame,null));
        userModel.setPassword(sharedPreferences.getString(LoginActivity.Password,null));

        btnAddPet = view.findViewById(R.id.btnAddPet);
       petListView = view.findViewById(R.id.petListView);
       imageView = view.findViewById(R.id.imageViwer);
       tvName = view.findViewById(R.id.tvName);
       tvEditProfile = view.findViewById(R.id.tvEditProfile);
       meLayout = view.findViewById(R.id.melayout);
       petAdapter = new PetAdapter(getActivity());
        GetData(userModel.getId());
        petListView.setAdapter(petAdapter);
        imageLoader = new PicassoLoader();
        tvName.setText(userModel.getName());

        if(userModel.getImageUrl() == null){

            imageLoader.loadImage(imageView,"https://cdn.business2community.com/wp-content/uploads/2017/08/blank-profile-picture-973460_640.png","user");
        }else{
            imageLoader.loadImage(imageView,userModel.getImageUrl(),"test");
        }

       meLayout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(getActivity(),EditProfile.class);
               startActivity(intent);
           }
       });



      petListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               System.out.println("u kliku");
               PetModel petModel = petAdapter.dataSource.get(position);
               Gson gsonParser = new Gson();
               String  strPetModel = gsonParser.toJson(petModel);

               Intent intent = new Intent(view.getContext(), EditPetsActivity.class);
               intent.putExtra("strPetModel",strPetModel);
               intent.putExtra("documentId",documentId);
               startActivity(intent);

           }
       });











       btnAddPet.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               Intent intent = new Intent(getContext(),AddPetActivity.class);
               startActivity(intent);

           }
       });

        getImage(new EditProfile.FirebaseCallBack() {
            @Override
            public void onCallBack(String string) {
                if(string != null){

                    userModel.setImageUrl(string);
                    imageLoader.loadImage(imageView,userModel.getImageUrl(),"test");
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(LoginActivity.ImageUrl,string);
                    editor.apply();
                }
            }
        });

       return view;
    }



    public void GetData(long id){
        List<PetModel> pets = new ArrayList<>();

       ff.collection("pets")
               .whereEqualTo("user",id+"")
               .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                documentId = document.getId();
                                if(getActivity() != null){
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            petAdapter.dataSource.add(document.toObject(PetModel.class));

                                            petAdapter.notifyDataSetChanged();
                                        }
                                    });
                                }
                            }
                        }else{
                            Log.w("FIREBASE", "error getting documents", task.getException());
                        }
                    }
                });

    }


    public void getImage(EditProfile.FirebaseCallBack firebaseCallBack){
        ff.collection("users")
                .document(sharedPreferences.getString(LoginActivity.DocumentId,"defValue"))
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.getString("imageUrl") == ""){
                            firebaseCallBack.onCallBack("https://prairietherapy.ca/wp-content/uploads/2017/03/Blank-Profile-pic.png");
                        }else{
                            firebaseCallBack.onCallBack(documentSnapshot.getString("imageUrl"));
                        }
                    }
                });
    }


}
