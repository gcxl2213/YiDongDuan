package com.example.daoyun;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import com.example.daoyun.fragment.DiscoverFragment;
import com.example.daoyun.fragment.MeFragment;
import com.example.daoyun.fragment.MyCreateFragment;
import com.example.daoyun.ui.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class HomeManagerActivity extends AppCompatActivity {

    private Fragment[] fragments = new Fragment[3];
    private BottomNavigationView nav_view;
    private int preFragmentIndex = 0;

    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_manager);
        Intent intent = getIntent();
        String preClassName = intent.getStringExtra("preClassName");
        if("CreateProjectActivity".equals(preClassName)){
            Fragment fragment = new MyCreateFragment();
            Bundle bundle = new Bundle();
            bundle.putString("preClassName","CreateProjectActivity");
            fragment.setArguments(bundle);

            fragmentManager = getSupportFragmentManager();
            Fragment homeFragment = new HomeFragment();
            FragmentTransaction fragmentTransaction =fragmentManager.beginTransaction();
            //fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
            fragmentTransaction.remove(homeFragment);
            fragmentTransaction.add(R.id.nav_host_fragment, homeFragment);
            fragmentTransaction.commit();
//            Toast.makeText(this, "finish", Toast.LENGTH_LONG).show();
        }
        initView();
        initFragment();
        selectFragment();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

    }

    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.id.title_home_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void initView(){
        nav_view = findViewById(R.id.nav_view);
    }

    private void initFragment(){
        fragments[0] = new HomeFragment();
        fragments[1] = new DiscoverFragment();
        fragments[2] = new MeFragment();
        initLoadFragment(R.id.nav_host_fragment, 0, fragments);
    }

    private void initLoadFragment(int containerId, int showFragment, Fragment[] fragments){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        for(int i = 0; i < fragments.length; i++){
            //首先将Fragment添加到事务中
            transaction.add(containerId, fragments[i], fragments[i].getClass().getName());
            //默认展示 fragments[showFragment]
            //这里做首次Fragment的展示，如果不是指定的Fragment就先隐藏，需要的时候再显示出来
            if (i != showFragment)
                transaction.hide(fragments[i]);
        }
        //提交事物
        transaction.commitAllowingStateLoss();
    }

    private void selectFragment() {
        nav_view.setItemIconTintList(null);
        nav_view.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.home:
                    showAndHideFragment(fragments[0], fragments[preFragmentIndex]);
                    preFragmentIndex = 0;
                    break;
                case R.id.discover:
                    showAndHideFragment(fragments[1], fragments[preFragmentIndex]);
                    preFragmentIndex = 1;
                    break;
                case R.id.me:
                    showAndHideFragment(fragments[2], fragments[preFragmentIndex]);
                    preFragmentIndex = 2;
                    break;
            }
            return true;
        });
    }

    private void showAndHideFragment(Fragment show, Fragment hide){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (show != hide)
            transaction.show(show).hide(hide).commitAllowingStateLoss();
    }

}