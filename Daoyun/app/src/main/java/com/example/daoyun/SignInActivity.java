package com.example.daoyun;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

import androidx.annotation.Nullable;

public class SignInActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qiandao);
        Chronometer ch = findViewById(R.id.timer);
        ch.setBase(SystemClock.elapsedRealtime()+60000);
        ch.setFormat("%s");
        ch.start();
        ch.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
//                ch.setText(removeCharAt(ch.getText().toString(),3));
                ch.setText(ch.getText().toString().substring(1));
                if(SystemClock.elapsedRealtime() - ch.getBase() >= 0){
                    ch.stop();
                    finish();
                }
            }
        });
        Button qiandao_bt = findViewById(R.id.qiandao_bt);
        qiandao_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ch.stop();
                finish();
            }
        });
        Button qiandao_back_bt = findViewById(R.id.qianda_back_bt);
        qiandao_back_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ch.stop();
                finish();
            }
        });
    }
    private static String removeCharAt(String str, int i) {
        return str.substring(0, i)+str.substring(i+1);
    }
}
