package com.stuffer.stuffers.activity.loan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stuffer.stuffers.R;

import java.util.ArrayList;
import java.util.List;

public class L_IntroActivity extends AppCompatActivity {

    private LinearLayout dotsLayout;
    private TextView[] dots;

    private ViewPager mViewPager;
    private List<View> mList;
    private ViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lintro);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        mViewPager = findViewById(R.id.mViewPager);

        mList = new ArrayList<>();
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view1 = inflater.inflate(R.layout.layout_loan_intro1, null);
        View view2 = inflater.inflate(R.layout.layout_loan_intro2, null);
        View view3 = inflater.inflate(R.layout.layout_loan_intro3, null);
        View view4 = inflater.inflate(R.layout.layout_loan_intro4, null);
        view4.findViewById(R.id.btnContinue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent intentHome = new Intent(L_IntroActivity.this, L_HomeActivity.class);
                startActivity(intentHome);*/
            }
        });

        mList.add(view1);
        mList.add(view2);
        mList.add(view3);
        mList.add(view4);

        mAdapter = new ViewAdapter(mList);
        mViewPager.setAdapter(mAdapter);
        addBottomDots(0);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                addBottomDots(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[4];

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.dot_inactive));
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(getResources().getColor(R.color.dot_active));


    }

    public class ViewAdapter extends PagerAdapter {
        private List<View> viewList;

        public ViewAdapter(List<View> viewList) {
            this.viewList = viewList;
        }


        public int getCount() {

            return viewList.size();
        }

        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(viewList.get(position));
        }

        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(viewList.get(position));//千万别忘记添加到container
            return viewList.get(position);
        }

    }
}