package com.flyco.tablayoutsamples.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.flyco.tablayoutsamples.adapter.SimpleHomeAdapter;

public class SimpleHomeActivity extends AppCompatActivity {
    private Context context = this;
    private final String[] items = {"SlidingTabLayout", "CommonTabLayout"};
    private final Class<?>[] classes = {SlidingTabActivity.class, CommonTabActivity.class};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ListView lv = new ListView(context);
        lv.setCacheColorHint(Color.TRANSPARENT);
        lv.setFadingEdgeLength(0);
        lv.setAdapter(new SimpleHomeAdapter(context, items));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, classes[position]);
                startActivity(intent);
            }
        });

        setContentView(lv);
    }
}
