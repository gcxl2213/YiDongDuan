package com.example.daoyun.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.daoyun.R;
import com.example.daoyun.fragment.MyCreateFragment;
import com.example.daoyun.fragment.MyJoinFragment;
import com.google.android.material.tabs.TabLayout;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private MyCreateFragment myCreateFragment;
    private MyJoinFragment myJoinFragment;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });



        myCreateFragment = new MyCreateFragment();
        myJoinFragment = new MyJoinFragment();
        TabLayout tabLayout = root.findViewById(R.id.tabLayout_home);

        //设置初始默认页
        tabLayout.getTabAt(1).select();
        getChildFragmentManager().beginTransaction().replace(R.id.linearLayout_home, myJoinFragment).commit();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().equals("我加入的")){//此处的“爱看”是指对应TabItem的text内容，通过该内容的比较来确定选中的是哪一个TabItem
                    getChildFragmentManager().beginTransaction().replace(R.id.linearLayout_home,myJoinFragment).commit();
                }
                if (tab.getText().equals("我创建的")){
                    getChildFragmentManager().beginTransaction().replace(R.id.linearLayout_home,myCreateFragment).commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return root;
    }
}