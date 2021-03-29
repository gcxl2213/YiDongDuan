package com.example.daoyun;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TabHost;

import androidx.annotation.Nullable;

public class HomeControllerActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_controller);
        TabHost home_controller = findViewById(android.R.id.tabhost);
        home_controller.setup();
        LayoutInflater inflater = LayoutInflater.from(this);
        inflater.inflate(R.layout.home, home_controller .getTabContentView());
        inflater.inflate(R.layout.discover, home_controller .getTabContentView());
        inflater.inflate(R.layout.aboutme, home_controller .getTabContentView());
        home_controller.addTab(home_controller.newTabSpec("home").setIndicator("班课").setContent(R.id.home));
        home_controller.addTab(home_controller.newTabSpec("discover").setIndicator("发现").setContent(R.id.discover));
        home_controller.addTab(home_controller.newTabSpec("aboutme").setIndicator("我的").setContent(R.id.aboutme));
    }
}
