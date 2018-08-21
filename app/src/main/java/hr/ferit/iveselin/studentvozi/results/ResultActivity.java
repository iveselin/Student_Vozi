package hr.ferit.iveselin.studentvozi.results;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hr.ferit.iveselin.studentvozi.LoginActivity;
import hr.ferit.iveselin.studentvozi.R;
import hr.ferit.iveselin.studentvozi.base.BaseActivity;
import hr.ferit.iveselin.studentvozi.model.RideType;
import hr.ferit.iveselin.studentvozi.utils.LoginUtils;

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

    @Override
    protected void onStart() {
        super.onStart();

        if (!LoginUtils.checkLogin()) {
            Toast.makeText(getApplicationContext(), "You have to login", Toast.LENGTH_SHORT).show();
            startActivity(LoginActivity.getLaunchIntent(this));
            finish();
        }
    }

    private void setViewPager() {
        tabTitles.add(RideType.OFFER.getDisplayName());
        tabTitles.add(RideType.REQUEST.getDisplayName());

        SimpleFragmentPageAdapter pageAdapter = new SimpleFragmentPageAdapter(getSupportFragmentManager());
        pageAdapter.setTabTitles(tabTitles);
        pageAdapter.addFragment(PageFragment.newInstance(RideType.OFFER));
        pageAdapter.addFragment(PageFragment.newInstance(RideType.REQUEST));

        viewPager.setAdapter(pageAdapter);

        tabLayout.setupWithViewPager(viewPager);
    }


}
