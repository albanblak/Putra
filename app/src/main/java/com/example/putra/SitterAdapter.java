package com.example.putra;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

import agency.tango.android.avatarview.loader.PicassoLoader;

public class SitterAdapter extends BaseAdapter {

   List<SitterModel> dataSource = new ArrayList<>();
   PicassoLoader picassoLoader = new PicassoLoader();
   Context c;

   public SitterAdapter(Context c){
       this.c = c;
   }

    @Override
    public int getCount() {
        return dataSource.size();
    }

    @Override
    public Object getItem(int position) {
        return dataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return dataSource.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       SitterHolder sitterHolder;
       if(convertView == null){
           convertView = LayoutInflater.from(c).inflate(R.layout.sitter_layout,parent,false);
           sitterHolder = new SitterHolder(convertView);
           convertView.setTag(sitterHolder);
       }else{
          sitterHolder = (SitterHolder) convertView.getTag();
       }


        sitterHolder.title.setText(dataSource.get(position).getTitle());
        sitterHolder.price.setText(dataSource.get(position).getPrice());
        sitterHolder.name.setText(dataSource.get(position).getUserName());
        sitterHolder.title.setText(dataSource.get(position).getTitle());
        String imageUrl = dataSource.get(position).getImageUrl();
        picassoLoader.loadImage(sitterHolder.avatarView,imageUrl,"test");

        // sitterHolder.name.setText(dataSource.get(position).getUserId());
        Animation animation = AnimationUtils.loadAnimation(c,R.anim.fade_in);
        convertView.startAnimation(animation);

      return convertView;
    }






}
