package com.example.daoyun;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.daoyun.fragment.CourseFragment;
import com.example.daoyun.fragment.MyCreateFragment;

public class CreateProjectActivity extends FragmentActivity {

    private int createNumber;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_project);
        Button create_project_bt = findViewById(R.id.create_project_back);
        SharedPreferences sp = getSharedPreferences("daoyun", MODE_MULTI_PROCESS);
        create_project_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button create_project_cancel = findViewById(R.id.create_project_cancel);
        create_project_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button create_project = findViewById(R.id.create_project_go);
        create_project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateProjectActivity.this,HomeManagerActivity.class);
                createNumber = sp.getInt("createnumber", 0);
                createNumber++;
                Toast.makeText(CreateProjectActivity.this,""+createNumber, Toast.LENGTH_SHORT);
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("createnumber", createNumber);
                editor.commit();
                intent.putExtra("preClassName","CreateProjectActivity");
                startActivity(intent);
            }
        });
    }
}
