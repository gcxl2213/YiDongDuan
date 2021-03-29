package com.example.daoyun;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TabHost tabHost = findViewById(android.R.id.tabhost);
        tabHost.setup();
        LayoutInflater inflater = LayoutInflater.from(this);
        inflater.inflate(R.layout.tab1, tabHost.getTabContentView());
        inflater.inflate(R.layout.tab2, tabHost.getTabContentView());
        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("验证码登录").setContent(R.id.yanzhengma));
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("密码登录").setContent(R.id.mima));

        Button forget_bt = findViewById(R.id.wang_mima);
        forget_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });
        Button register_bt = findViewById(R.id.register);
        register_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,RigesterActivity.class);
                startActivity(intent);
            }
        });
        Button register_bt2 = findViewById(R.id.register2);
        register_bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,RigesterActivity.class);
                startActivity(intent);
            }
        });
        Button login_bt = findViewById(R.id.bt_login);
        login_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });
        Button get_yanzhengma_bt = findViewById(R.id.get_yanzhengma);
        get_yanzhengma_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "验证码已发送", Toast.LENGTH_LONG).show();
            }
        });
    }
}