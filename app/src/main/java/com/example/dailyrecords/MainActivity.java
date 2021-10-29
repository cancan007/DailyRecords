package com.example.dailyrecords;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //リストに表示するデータ
    private final static String[] listDatas = {
            "ONE","TWO","THREE","FOUR",
            "FIVE","SIX","SEVEN","EIGHT",
            "NINE","TEN","ELEVEN","TWELVE",
            "THIRTEEN","FOURTEEN","FIFTEEN","SIXTEEN",
            "SEVENTEEN","EIGHTEEN","NINETEEN","TWENTY"    };

    private SQLite_Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SQLite_Handler handler = new SQLite_Handler(getApplicationContext());
        //Enter_Activity EA = new Enter_Activity();

        final RecyclerView recyclerView = findViewById(R.id.recyclerview1);
        //LayoutManagerを設定
        //縦方向のリストはLinearLayoutManagerを使う
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL); //縦方向に設定
        recyclerView.setLayoutManager(layoutManager);
        //ItemDecorationを設定
        //DividerItemDecorationはリストに罫線を引くためのItemDecoration
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        recyclerView.addItemDecoration(itemDecoration);

        //DataBaseから表示するデータの配列を取ってくる
        try{
            String[] loc_array = readData();
        }catch(NullPointerException e){
            String[] loc_array = listDatas;
        }


        //アダプタのセット
        SampleRecyclerAdapter adapter = new SampleRecyclerAdapter(loc_array);
        recyclerView.setAdapter(adapter);
    }

    /* -------------------------------------------------------
     * RecyclerViewのアダプタ
     * --------------------------------------------------------*/
    class SampleRecyclerAdapter extends RecyclerView.Adapter<SampleRecyclerAdapter.SampleViewHolder> {
        //メンバ変数
        String[] itemDatas; //リストのデータを保持する変数

        //コンストラクタ
        SampleRecyclerAdapter(String[] itemDatas) {
            //データをコンストラクタで受け取りメンバ変数に格納
            this.itemDatas = itemDatas;
        }

        @NonNull
        @Override
        public SampleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {  // なぜ `Overrideなのに　関数名が違くてもいいのか
            //onCreateViewHolder()ではリスト一行分のレイアウトViewを生成する
            //作成したViewはViewHolderに格納してViewHolderをreturnで返す

            //レイアウトXMLからViewを生成
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.listitem_recyclerview1, viewGroup, false);
            //ViewHolderを生成してreturn (このSampleViewHolderはインナークラスのもの)
            SampleViewHolder holder = new SampleViewHolder(view);

            //クリックイベントを登録
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    Toast.makeText(v.getContext(), itemDatas[position], Toast.LENGTH_SHORT).show();
                }
            });

            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull SampleViewHolder sampleViewHolder, int position) {
            //onBindViewHolder()ではデータとレイアウトの紐づけを行なう
            sampleViewHolder.text01.setText(itemDatas[position]);
        }

        @Override
        public int getItemCount() {
            //データ個数を返す
            return itemDatas.length;
        }

        /* ViewHolder（インナークラス） */
        class SampleViewHolder extends RecyclerView.ViewHolder {
            TextView text01;

            SampleViewHolder(@NonNull View itemView) {
                super(itemView);
                text01 = itemView.findViewById(R.id.text01);
            }
        }
    }

    public String[] readData(){
        List<String> loc_list= new ArrayList<String>(); // 文字列を格納する空の配列を用意
        List<String> date_list = new ArrayList<String>();
        List<String> title_list = new ArrayList<String>();
        List<String> content_list = new ArrayList<String>();

        SQLiteDatabase db = handler.getReadableDatabase();
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
}