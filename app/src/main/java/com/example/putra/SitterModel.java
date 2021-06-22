package com.example.putra;

public class SitterModel {

   public long id;
   public String service;
   public String description;
   public String price;
   public int userId;
   public String title;
   public String userName;
   public String imageUrl;




    public SitterModel(){

    }

    public SitterModel(long id, String service, String description, String price, long userId, String imageUrl) {
        this.id = id;
        this.service = service;
        this.description = description;
        this.price = price;
        this.userId = (int) userId;
        this.imageUrl = imageUrl;


    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }





   public long getId() {
      return id;
   }

   public void setId(long id) {
      this.id = id;
   }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }



   public String getService() {
      return service;
   }

   public void setService(String service) {
      this.service = service;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public String getPrice() {
      return price;
   }

   public void setPrice(String price) {
      this.price = price;
   }

}
