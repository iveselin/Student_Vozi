package hr.ferit.iveselin.studentvozi.results;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class SimpleFragmentPageAdapter extends FragmentPagerAdapter {
    private List<String> tabTitles = new ArrayList<>();
    private List<Fragment> fragments = new ArrayList<>();

    public SimpleFragmentPageAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setTabTitles(List<String> stringList) {
        tabTitles = stringList;
    }

    public void addFragment(Fragment fragment) {
        fragments.add(fragment);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public String getPageTitle(int position) {
        return tabTitles.get(position);
    }
}
