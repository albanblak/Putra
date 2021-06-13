package com.example.putra;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

    ListView petListView;
    PetAdapter petAdapter;
    RelativeLayout meLayout;
    TextView tvName;
    TextView tvEditProfile;
    AvatarView imageView;
    IImageLoader imageLoader;
    FirebaseFirestore ff = FirebaseFirestore.getInstance();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view =  inflater.inflate(R.layout.fragment_settings, container, false);
       String stringModel = getArguments().getString("stringModel");
       Gson gsonParser = new Gson();
       UserModel userModel = gsonParser.fromJson(stringModel,UserModel.class);
       petListView = view.findViewById(R.id.petListView);
       imageView = view.findViewById(R.id.imageViwer);
       tvName = view.findViewById(R.id.tvName);
       tvEditProfile = view.findViewById(R.id.tvEditProfile);
       meLayout = view.findViewById(R.id.melayout);
       petAdapter = new PetAdapter(getActivity());
        GetData();
        petListView.setAdapter(petAdapter);
        imageLoader = new PicassoLoader();
        imageLoader.loadImage(imageView,"https://cdn.business2community.com/wp-content/uploads/2017/08/blank-profile-picture-973460_640.png","user");
        tvName.setText(userModel.getName());



       return view;
    }



    public void GetData(){

        List<PetModel> pets = new ArrayList<>();

        ff.collection("pets")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        petAdapter.dataSource.add(document.toObject(PetModel.class));

                                        petAdapter.notifyDataSetChanged();
                                    }
                                });

                            }


                        }else{
                            Log.w("FIREBASE", "error getting documents", task.getException());
                        }
                    }
                });



    }


}
