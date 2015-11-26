package com.afollestad.sectionedrecyclerviewsample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * @author Aidan Follestad (afollestad)
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView list = (RecyclerView) findViewById(R.id.list);
        MainAdapter adapter = new MainAdapter();
        GridLayoutManager manager = new GridLayoutManager(this,
                getResources().getInteger(R.integer.grid_span));
        list.setLayoutManager(manager);
        adapter.setLayoutManager(manager);
        list.setAdapter(adapter);
    }
}