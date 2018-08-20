package hr.ferit.iveselin.studentvozi.results;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hr.ferit.iveselin.studentvozi.R;
import hr.ferit.iveselin.studentvozi.base.BaseActivity;
import hr.ferit.iveselin.studentvozi.model.RideType;

public class ResultActivity extends BaseActivity {

    private static final String TAG = "ResultActivity";

    public static Intent getLaunchIntent(Context fromContext) {
        return new Intent(fromContext, ResultActivity.class);
    }


    @BindView(R.id.result_view_pager)
    ViewPager viewPager;
    @BindView(R.id.result_tabs)
    TabLayout tabLayout;

    private List<String> tabTitles = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        setUi();
    }

    private void setUi() {
        ButterKnife.bind(this);

        setViewPager();
    }

    private void setViewPager() {
        tabTitles.add("Offers");
        tabTitles.add("Requests");

        SimpleFragmentPageAdapter pageAdapter = new SimpleFragmentPageAdapter(getSupportFragmentManager());
        pageAdapter.setTabTitles(tabTitles);
        pageAdapter.addFragment(PageFragment.newInstance(RideType.OFFER));
        pageAdapter.addFragment(PageFragment.newInstance(RideType.REQUEST));

        viewPager.setAdapter(pageAdapter);

        tabLayout.setupWithViewPager(viewPager);
    }


}
