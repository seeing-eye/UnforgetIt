package org.jasey.unforgetit;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import org.jasey.unforgetit.adapter.TaskPagerAdapter;

public class UnforgetItActivity extends FragmentActivity {
    private final static int PAGE_COUNT = 2;

    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unforget_it_activity);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new TaskPagerAdapter(getSupportFragmentManager(), getApplicationContext(), PAGE_COUNT);
        mPager.setAdapter(mPagerAdapter);

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mPager.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

}
