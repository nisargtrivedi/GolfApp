package com.app.discovergolf.Activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.app.discovergolf.Adapter.NewPagerAdapter;
import com.app.discovergolf.Adapter.WrappingViewPager;
import com.app.discovergolf.Fragment.LiveFragment;
import com.app.discovergolf.Fragment.ModFragment;
import com.app.discovergolf.R;
import com.app.discovergolf.Util.TTextView;

public class DateScoreActivity extends AppCompatActivity {

    TabLayout tabLayout;
    WrappingViewPager viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_score);
        tabLayout= (TabLayout)findViewById(R.id.tabLayout);
        TextView date= (TextView)findViewById(R.id.date);

        date.setText(getIntent().getStringExtra("date"));
       // Score.setText(getIntent().getStringExtra("score"));
        //TeamName.setText(getIntent().getStringExtra("date"));
        viewpager= (WrappingViewPager)findViewById(R.id.viewpager);
        tabLayout.setupWithViewPager(viewpager);
        viewpager.setAdapter(new NewPagerAdapter(getSupportFragmentManager()) {
            @Override
            public android.support.v4.app.Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return new LiveFragment();
                    case 1:
                        return new ModFragment();
                    default:
                        return new LiveFragment();

                }
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return "LIVE";
                    case 1:
                        return "M.O.D";
                    default:
                        return "LIVE";
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void backpress(View view) {
        onBackPressed();
    }

}
