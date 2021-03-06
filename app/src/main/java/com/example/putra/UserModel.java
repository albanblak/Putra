package com.example.putra;

public class UserModel {

   long id;
   String name;
   String lastname;
   String email;
   String password;
   String documentId;
   String imageUrl;

   public String getImageUrl() {
      return imageUrl;
   }

   public void setImageUrl(String imageUrl) {
      this.imageUrl = imageUrl;
   }

   public String getDocumentId() {
      return documentId;
   }

   public void setDocumentId(String documentId) {
      this.documentId = documentId;
   }



   public UserModel(){

   }

   public UserModel(long id, String name, String lastname, String email, String password,String documentId) {
      this.id =  id;
      this.name = name;
      this.lastname = lastname;
      this.email = email;
      this.password = password;
      this.documentId = documentId;
   }

   public long getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getLastname() {
      return lastname;
   }

   public void setLastname(String lastname) {
      this.lastname = lastname;
   }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }



}
