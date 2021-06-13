package com.example.putra;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static String TABLE_NAME = "users";
    public static String ID = "id";
    public static String NAME = "name";
    public static String LASTNAME = "lastname";
    public static String EMAIL = "email";
    public static String PASSWORD = "password";

    public DatabaseHelper(@Nullable Context context) {
        super(context, "PutraDb", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table users(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, lastname TEXT, email TEXT, password TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
