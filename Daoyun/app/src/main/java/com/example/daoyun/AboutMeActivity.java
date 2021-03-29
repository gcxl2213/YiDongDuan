package com.example.daoyun;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class AboutMeActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aboutme);
        TextView detail_go_bt = findViewById(R.id.detail_go_bt);
        detail_go_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutMeActivity.this,AboutMeDetailActivity.class);
                startActivity(intent);
            }
        });
        TextView banke_bt = findViewById(R.id.banke);
        banke_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutMeActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}
