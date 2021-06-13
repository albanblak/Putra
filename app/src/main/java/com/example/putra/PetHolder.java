package com.example.putra;

import android.view.View;
import android.widget.TextView;

import agency.tango.android.avatarview.views.AvatarView;

public class PetHolder {

   TextView petTvName;
   AvatarView avatarView;


   PetHolder(View v){
       this.petTvName = v.findViewById(R.id.pettvName);
       this.avatarView = v.findViewById(R.id.petimageViwer);
   }

}
