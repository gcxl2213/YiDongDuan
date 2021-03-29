package com.example.daoyun;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TextView;


public class HomeActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        TabHost home = findViewById(android.R.id.home);
        home.setup();
        LayoutInflater inflater = LayoutInflater.from(this);
        inflater.inflate(R.layout.my_create, home.getTabContentView());
        inflater.inflate(R.layout.my_join, home.getTabContentView());
        home.addTab(home.newTabSpec("my_create").setIndicator("我创建的").setContent(R.id.create));
        home.addTab(home.newTabSpec("my_join").setIndicator("我加入的").setContent(R.id.join));

        ImageButton detail_bt1 = findViewById(R.id.detail_bt1);
        detail_bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,ProjectDetailActivity.class);
                startActivity(intent);
            }
        });
        TextView detail_bt2 = findViewById(R.id.project_name1);
        detail_bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,ProjectDetailActivity.class);
                startActivity(intent);
            }
        });
        Button detail_bt3 = findViewById(R.id.detail_bt2);
        detail_bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,ProjectDetailActivity.class);
                startActivity(intent);
            }
        });

        Button qiandao_go_bt = findViewById(R.id.qiandao_go_bt);
        qiandao_go_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,SignInActivity.class);
                startActivity(intent);
            }
        });

        TextView create_bt = findViewById(R.id.create_project);
        create_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,CreateProjectActivity.class);
                startActivity(intent);
            }
        });
        TextView aboutme_bt = findViewById(R.id.aboutme_bt);
        aboutme_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,AboutMeActivity.class);
                startActivity(intent);
            }
        });
        TextView aboutme_bt2 = findViewById(R.id.aboutme_bt2);
        aboutme_bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,AboutMeActivity.class);
                startActivity(intent);
            }
        });
    }
}
