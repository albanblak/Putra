package com.example.putra;

public class Upload {

    private String mName;
    private String mImageUrl;

    public Upload(){
//
    }

    public Upload(String name, String mImageUrl){

        if(name.trim().equals("")){
            name="No Name";
        }
        this.mName = name;
        this.mImageUrl = mImageUrl;
    }


    public String getmName(){
        return this.mName;
    }

    public void setmName(String name){
        this.mName = name;
    }


    public String getmImageUrl(){
        return this.mImageUrl;
    }

    public void setmImageUrl(String imageUrl){
        this.mImageUrl = imageUrl;
    }
}
