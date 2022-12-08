package com.dndev.signupfirbase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.dndev.signupfirbase.Models.NewsApiResponse;
import com.dndev.signupfirbase.Models.NewsHeadlines;
import com.dndev.signupfirbase.adapters.RecommendationAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SelectListener, View.OnClickListener {
    RecyclerView recyclerView;
    CustomAdapter adapter;
    ProgressDialog dialog;
    Button b0, b1, b2, b3, b4, b5, b6, b7;
    SearchView searchView;
    RecommendationAdapter recommendationAdapter;
    RecyclerView recommendationRecycler;

    private List<String> recommendationKeyList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchView = findViewById(R.id.search_view);
        recommendationRecycler = findViewById(R.id.recommendationRecyclerView);

        recommendationKeyList = new ArrayList<>();

        recommendationKeyList = readDb();

        if (recommendationKeyList.size() > 0) {

            RequestManager manager = new RequestManager(MainActivity.this);
            manager.getRecommendations(listener, "general", recommendationKeyList.get(recommendationKeyList.size()-1));
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                dialog.setTitle("Fetching news articles of " + query);
                writeDB(query);
                dialog.show();
                RequestManager manager = new RequestManager(MainActivity.this);
                manager.getNewsHeadlines(listener, "general", query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        dialog = new ProgressDialog(this);
        dialog.setTitle("Fetching news articles..");
        dialog.show();

        b0 = findViewById(R.id.btn_0);
        b0.setOnClickListener(this);
        b1 = findViewById(R.id.btn_1);
        b1.setOnClickListener(this);
        b2 = findViewById(R.id.btn_2);
        b2.setOnClickListener(this);
        b3 = findViewById(R.id.btn_3);
        b3.setOnClickListener(this);
        b4 = findViewById(R.id.btn_4);
        b4.setOnClickListener(this);
        b5 = findViewById(R.id.btn_5);
        b5.setOnClickListener(this);
        b6 = findViewById(R.id.btn_6);
        b6.setOnClickListener(this);
        b7 = findViewById(R.id.btn_7);
        b7.setOnClickListener(this);

        RequestManager manager = new RequestManager(this);
        manager.getNewsHeadlines(listener, "general", null);
    }

    private final OnFetchDataListener<NewsApiResponse> listener = new OnFetchDataListener<NewsApiResponse>() {
        @Override
        public void onFetchData(List<NewsHeadlines> list, String message) {

            if (list.isEmpty()){
                Toast.makeText(MainActivity.this, "No data found!!!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
            else {
                showNews(list);
                dialog.dismiss();
            }
        }

        @Override
        public void onFetchRecommendationsData(List<NewsHeadlines> list, String message) {
            showRecommendations(list);
        }

        @Override
        public void onError(String message) {

            Toast.makeText(MainActivity.this, "An Error Occured!!!", Toast.LENGTH_LONG).show();

        }


    };

    private void showNews(List<NewsHeadlines> list) {
        recyclerView = findViewById(R.id.recycler_main);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        adapter = new CustomAdapter(this, list, this);
        recyclerView.setAdapter(adapter);

    }
    public void showRecommendations(List<NewsHeadlines> list) {
        recommendationAdapter = new RecommendationAdapter(this, list, item -> {

            item.addView();
            startActivity(new Intent(MainActivity.this, DetailsActivity.class)
                    .putExtra("data", item));
        });
        recommendationRecycler.setVisibility(View.VISIBLE);
        recommendationRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recommendationRecycler.setAdapter(recommendationAdapter);
    }

    @Override
    public void OnNewsClicked(NewsHeadlines headlines) {
        headlines.addView();
        startActivity(new Intent(MainActivity.this, DetailsActivity.class)
        .putExtra("data", headlines));
    }

    @Override
    public void onClick(View v) {
        Button button = (Button) v;
        String category = button.getText().toString();

        dialog.setTitle("Fetching news articles of " + category);
        dialog.show();

        RequestManager manager = new RequestManager(this);
        manager.getNewsHeadlines(listener, category, null);
    }

    public void writeDB(String key) {
        SQLiteDatabase db = this.openOrCreateDatabase("News", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS news (key_column VARCHAR)");
        db.execSQL("INSERT INTO news (key_column) VALUES ('"+key+"')");
    }

    public List<String> readDb() {
        SQLiteDatabase db = this.openOrCreateDatabase("News", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS news (key_column VARCHAR(100));");
        Cursor cursor = db.rawQuery("SELECT * FROM news", null);
        List<String> keyList = new ArrayList<>();
        if(cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            int key_column_number = cursor.getColumnIndex("key_column");

            do{
                keyList.add(cursor.getString(key_column_number));
            } while (cursor.moveToNext());
        }


        return keyList;
    }
}
