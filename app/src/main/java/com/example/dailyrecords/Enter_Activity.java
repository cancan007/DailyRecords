package com.example.dailyrecords;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Enter_Activity extends AppCompatActivity {

    public SQLite_Handler handler;  // 他のアクティビティでも使えるように、publicにする

    private TextView loc_v, date_v, title_v, content_v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);

        SQLite_Handler handler = new SQLite_Handler(getApplicationContext());

        loc_v = findViewById(R.id.location);
        date_v = findViewById(R.id.date);
        title_v = findViewById(R.id.title_d);
        content_v = findViewById(R.id.content);

    }

    public String[] readData(View view){
        List<String> loc_list= new ArrayList<String>(); // 文字列を格納する空の配列を用意
        List<String> date_list = new ArrayList<String>();
        List<String> title_list = new ArrayList<String>();
        List<String> content_list = new ArrayList<String>();

        SQLiteDatabase db = handler.getReadableDatabase();
        //MainActivity Main = new MainActivity();
        //SQLiteDatabase db = Main.db;
        Cursor cursor = db.query(
                handler.TABLE_NAME,
                new String[]{"location", "date", "title", "content"},
                null,  //WHERE句の列名
                null,  //WHERE句の値
                null,  //GROUP BY句の値
                null,  //HAVING句の値
                null  //ORDER BY句の値
        );
        cursor.moveToFirst();  // databaseの最初の行へ移動

        for(int i=0; i<cursor.getCount(); i+=1){
            loc_list.add(cursor.getString(cursor.getColumnIndex("location"))); // cursor.getColumnincex(カラム名)でそのカラムの列数を渡す
            date_list.add(cursor.getString(cursor.getColumnIndex("date")));
            title_list.add(cursor.getString(cursor.getColumnIndex("title")));
            content_list.add(cursor.getString(cursor.getColumnIndex("content")));
            cursor.moveToNext();
        }

        cursor.close();
        String[] loc_array = loc_list.toArray(new String[0]);

        return loc_array;
    }

    public void saveData(View view){
        SQLiteDatabase db = handler.getWritableDatabase();
        ContentValues values = new ContentValues();

        String location = loc_v.getText().toString();
        String date = date_v.getText().toString();
        String title = title_v.getText().toString();
        String content = content_v.getText().toString();

        values.put("location", location);
        values.put("date", date);
        values.put("title", title);
        values.put("content", content);

        db.insert(handler.TABLE_NAME, null, values);
    }
}