package com.example.putra;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import agency.tango.android.avatarview.views.AvatarView;

public class SitterHolder {

    TextView name, title,  price;
    AvatarView avatarView;
    Button deletePetsBtn;


    SitterHolder(View v){
        this.name =   v.findViewById(R.id.tvsitterName);
        this.title = v.findViewById(R.id.tvTitle);
        this.price = v.findViewById(R.id.tvPrice);
        this.avatarView = v.findViewById(R.id.image);

    }

}
