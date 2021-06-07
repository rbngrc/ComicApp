package com.example.comicapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper (Context context) {
        super(context, "login.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase myDB) {
        myDB.execSQL("create Table comic(num text primary key, url text, title text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase myDB, int oldVersion, int newVersion) {
        myDB.execSQL("drop Table if exists comic");
    }

    public Cursor leerBaseDatos(int id) {
        SQLiteDatabase myDB = this.getWritableDatabase();
        Cursor cursor = myDB.rawQuery("select * from comic where num = " + id, null);
        return cursor;
    }

    public boolean insertarBaseDatos(int num, String url, String title) {
        SQLiteDatabase myDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("url", url);
        contentValues.put("num", num);
        long result = myDB.insert("comic", null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean comprobarComic(int num){
        SQLiteDatabase myDB = this.getWritableDatabase();

        Cursor cursor = myDB.rawQuery("select * from comic where num = ?", new String[] {String.valueOf(num)});

        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }

    }
}
