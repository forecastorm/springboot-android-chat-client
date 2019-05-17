package com.nil.client;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nil.client.adapter.ViewPagerAdapter;
import com.nil.client.fragment.ChatFragment;
import com.nil.client.fragment.UserFragment;
import com.nil.client.model.User;

public class TabActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        //find views
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.view_pager);
        //find views ends

        //get extras
        User currentUser = (User)getIntent().getSerializableExtra("CurrentUserKey");
        //get extras ends

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        //adding new tabs

        ChatFragment chatFragment = new ChatFragment();
        UserFragment userFragment = new UserFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("CurrentUserKey",currentUser);
        userFragment.setArguments(bundle);


        viewPagerAdapter.addFragment(chatFragment,"chat");
        viewPagerAdapter.addFragment(userFragment,"user");

        //adding new tabs ends
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
