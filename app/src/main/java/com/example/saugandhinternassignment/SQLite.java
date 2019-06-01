package com.example.saugandhinternassignment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


public class SQLite extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION =1;
    public static final String DATABASE_NAME="TIMETABLE.db";
    public static final String TABLE_NAME1="Video";
    public static final String TABLE_NAME2="Audio";
    public static final String TABLE_NAME3="Image";
    public static final String TABLE_NAME4="Image2";

    public static final String CLASS_ID="ID";



    public SQLite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String Video="CREATE TABLE "+TABLE_NAME1 +"("
                +CLASS_ID+" INTEGER PRIMARY KEY," +
                "PATH TEXT )";
        String Audio="CREATE TABLE "+TABLE_NAME2 +"("
                +CLASS_ID+" INTEGER PRIMARY KEY," +
                "PATH TEXT )";
        String Image="CREATE TABLE "+TABLE_NAME3 +"("
                +CLASS_ID+" INTEGER PRIMARY KEY," +
                "PATH TEXT )";
        String Image2="CREATE TABLE "+TABLE_NAME4 +"("
                +CLASS_ID+" INTEGER PRIMARY KEY," +
                "PATH TEXT )";


        db.execSQL(Video);
        db.execSQL(Audio);
        db.execSQL(Image);
        db.execSQL(Image2);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME1);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME2);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME3);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME4);

        onCreate(db);

    }
    public boolean insertClass(String tablename,int ID, String path)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues =new ContentValues();
        contentValues.put(CLASS_ID, ID);
        contentValues.put("PATH", path);

        db.replace(tablename, null, contentValues);
        return true;
    }
    public String getpath(String tablename)
    {
        SQLiteDatabase db= this.getReadableDatabase();
        String query = "SELECT * FROM "+ tablename;
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        return cursor.getString(1);
    }

}
