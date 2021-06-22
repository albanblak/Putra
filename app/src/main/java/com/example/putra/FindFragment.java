package com.example.putra;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;

public class FindFragment extends Fragment {


   ListView sittersListView;
   SitterAdapter sitterAdapter;
   FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view =  inflater.inflate(R.layout.fragment_find,container,false);
        List<SitterModel> sitterModels;
        sittersListView = view.findViewById(R.id.sitterListView);
        sitterAdapter = new SitterAdapter(getContext()) ;
        sittersListView.setAdapter(sitterAdapter);
      getData(new FirestoreCall() {
          @Override
          public void onCallBack1(List<SitterModel> sitterModelList) {

          }
      });

       return view;
    }


   public void getData(FirestoreCall firestoreCall){

        List<SitterModel> sitter = new ArrayList<>();

        firestore.collection("sitters")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            List<SitterModel> sitterModels = new ArrayList<>();
                            for(QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                               if(getActivity() != null){
                                   getActivity().runOnUiThread(new Runnable() {
                                       @Override
                                       public void run() {
                                           sitterAdapter.dataSource.add(documentSnapshot.toObject(SitterModel.class));
                                           sitterAdapter.notifyDataSetChanged();
                                       }
                                   });
                               }
                           }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("sitter nuk po bon");
                    }
                });
   }



  public void   getuser(long id, FirestoreCall firestoreCall){
        UserModel userModel = new UserModel();
      firestore.collection("users")
      .whereEqualTo("id",id+"")
              .get()
              .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                  @Override
                  public void onComplete(@NonNull Task<QuerySnapshot> task) {
                      if(task.isSuccessful()){
                         for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                           //  firestoreCall.onCallBack(documentSnapshot.toObject(UserModel.class));
                             System.out.println("user u nxorr me sukses");
                         }
                      }
                  }
              })
              .addOnFailureListener(new OnFailureListener() {
                  @Override
                  public void onFailure(@NonNull Exception e) {
                        System.out.println("deshtoi fetch i userit");
                  }
              });



   }


   interface FirestoreCall{

        void onCallBack1(List<SitterModel> sitterModelList);
   }

   interface Tets{
        void onCallBack(SitterModel sitterModel);
   }


}
