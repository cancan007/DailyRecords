package com.example.dailyrecords;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SQLite_Handler extends SQLiteOpenHelper {

    // データーベースのバージョン
    private static final int DATABASE_VERSION = 3;

    // データーベース情報を変数に格納
    private static final String DATABASE_NAME = "DailyBook.db";
    public static final String TABLE_NAME = "dailybookDB";
    private static final String _ID = "_id";
    private static final String COLUMN_LOCATION = "Location";
    private static final String COLUMN_DATE = "Date";
    private static final String COLUMN_TITLE = "Title";
    private static final String COLUMN_CONTENT = "Content";

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + '(' +
            _ID + " INTEGER PRIMARY KEY,"+   //ここで各々のデータを区別している。
            COLUMN_LOCATION + " TEXT," +
            COLUMN_DATE + " DATE," +
            COLUMN_TITLE + " TEXT," +
            COLUMN_CONTENT + " TEXT)";

    private static final String SQL_DELETE_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    SQLite_Handler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                SQL_CREATE_TABLE
        );
        saveData(db, "Tokyo", toDate("2017/03/02"), "Example", "First Test");
        saveData(db, "Germany", toDate("2022/02/12"), "Travel", "Great Travel");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(
                SQL_DELETE_TABLE
        );
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        onUpgrade(db, oldVersion, newVersion);
    }

    public void saveData(SQLiteDatabase db, String location, Date date, String title, String content){
        ContentValues values = new ContentValues();
        values.put("location", location);
        values.put("date", toString(date));
        values.put("title", title);
        values.put("content", content);

        db.insert(TABLE_NAME, null, values);
    }

    public String toString(Date date){
        String str = new SimpleDateFormat("yyyy-MM-dd").format(date);
        return str;
    }

    public Date toDate(String str){
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(str);
            return date;
        }catch(ParseException e){
            e.printStackTrace();
            return null;
        }
    }
}
