package com.example.dailyrecords;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Display_Content extends AppCompatActivity {

    private SQLite_Handler handler;
    private SQLiteDatabase db;
    //private Cursor c;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_content);

        Intent intent = getIntent();
        int position_data = intent.getIntExtra(MainActivity.POSITION_DATA, 0);
        Integer i = Integer.valueOf(position_data);
        String id = i.toString();


        StringBuilder stb = new StringBuilder();
        stb.append("_id=");
        stb.append(id);
        String _id = stb.toString();
        System.out.println(_id);


        handler = new SQLite_Handler(getApplicationContext());
        db = handler.getReadableDatabase();
        Cursor c = db.query(
                handler.TABLE_NAME,
                new String[]{"location", "date", "title", "content"},
                //_id,
                null,
                null,
                null,
                null,
                null
        );
        c.moveToFirst();

        for(int d=0; d< position_data; d+=1){
            c.moveToNext();
        }


        TextView d_loc = findViewById(R.id.d_location);
        //d_loc.setText(c.getString(c.getColumnIndex("Location")));
        d_loc.setText(c.getString(0));

        TextView d_date = findViewById(R.id.d_date);
        d_date.setText(c.getString(1));

        TextView d_title = findViewById(R.id.d_title);
        d_title.setText(c.getString(2));

        TextView d_con = findViewById(R.id.d_content);
        d_con.setText(c.getString(3));

        c.close();

        backButton = findViewById(R.id.d_backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), MainActivity.class);
                startActivity(intent);
            }
        });

    }
}