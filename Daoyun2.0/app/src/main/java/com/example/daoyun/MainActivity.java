package com.example.daoyun;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.xuexiang.xui.widget.alpha.XUIAlphaTextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TabHost tabHost = findViewById(android.R.id.tabhost);
        tabHost.setup();
        LayoutInflater inflater = LayoutInflater.from(this);
        inflater.inflate(R.layout.verification_code, tabHost.getTabContentView());
        inflater.inflate(R.layout.password_code, tabHost.getTabContentView());
        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("验证码登录").setContent(R.id.yanzhengma));
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("密码登录").setContent(R.id.mima));

        TextView forget_bt = findViewById(R.id.forget_password);
        forget_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });
        TextView register_bt = findViewById(R.id.register);
        register_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,RigesterActivity.class);
                startActivity(intent);
            }
        });
        TextView register_bt2 = findViewById(R.id.register2);
        register_bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,RigesterActivity.class);
                startActivity(intent);
            }
        });

        Button login_bt_password = findViewById(R.id.btn_login_password);
        login_bt_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = ((EditText)findViewById(R.id.phone_number_login)).getText().toString();
                String password = ((EditText)findViewById(R.id.password_login)).getText().toString();
                if ("".equals(phoneNumber)){
                    Toast.makeText(MainActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                }
                else if ("".equals(password)){
                    Toast.makeText(MainActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent(MainActivity.this,HomeManagerActivity.class);
                    startActivity(intent);
                }
            }
        });
        Button login_bt_verification = findViewById(R.id.btn_login_verification);
        login_bt_verification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber2 = ((EditText)findViewById(R.id.phone_number_vercode)).getText().toString();
                String vercode = ((EditText)findViewById(R.id.vercode_login)).getText().toString();
                if ("".equals(phoneNumber2)){
                    Toast.makeText(MainActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                }
                else if ("".equals(vercode)){
                    Toast.makeText(MainActivity.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent(MainActivity.this,HomeManagerActivity.class);
                    startActivity(intent);
                }
            }
        });
        Button get_yanzhengma_bt = findViewById(R.id.get_yanzhengma);
        get_yanzhengma_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "验证码已发送(这是测试，其随意输入)", Toast.LENGTH_LONG).show();
            }
        });
    }
}