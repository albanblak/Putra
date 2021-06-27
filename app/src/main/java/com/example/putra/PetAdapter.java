package com.example.putra;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;

public class PetAdapter  extends BaseAdapter {

   List<PetModel> dataSource = new ArrayList<>() ;
   IImageLoader imageLoader = new PicassoLoader();


    Context c;

   public PetAdapter(Context c){
      this.c = c;
   }



    @Override
    public int getCount() {
        return dataSource.size();
    }

    @Override
    public Object getItem(int position) {
       return  dataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
       return  dataSource.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PetHolder petHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(c).inflate(R.layout.pets_layout, parent, false);
            petHolder = new PetHolder(convertView);
            convertView.setTag(petHolder);
        }else{
            petHolder = (PetHolder) convertView.getTag();
        }

        petHolder.petTvName.setText(dataSource.get(position).getName());
        imageLoader.loadImage(petHolder.avatarView, dataSource.get(position).getImageUrl(),"test");
        RelativeLayout petLinear = convertView.findViewById(R.id.linearPet);
        Animation animation = AnimationUtils.loadAnimation(c, R.anim.slide_up);
        convertView.startAnimation(animation);


        return convertView;
    }
}
