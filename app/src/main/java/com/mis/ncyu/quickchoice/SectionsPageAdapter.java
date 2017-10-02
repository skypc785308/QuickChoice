package com.mis.ncyu.quickchoice;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import com.mis.ncyu.quickchoice.recommend.compute_recommend;

import java.util.ArrayList;
import java.util.List;

import static com.mis.ncyu.quickchoice.recommend.compute_recommend.timess;

/**
 * Created by User on 2/28/2017.
 */

public class SectionsPageAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    private static Fragment tmp = new Fragment();

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    public SectionsPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
//        Log.e("times",String.valueOf(timess[0]));
//        Log.e("times",String.valueOf(timess[1]));
//        Log.e("times",String.valueOf(timess[2]));
//        Log.e("times",String.valueOf(timess[3]));
//        Log.e("times",String.valueOf(timess[4]));
        Fragment now  = (Fragment)object;
        if (tmp != now){
            Log.e("切換後的",String.valueOf(position));
            tmp = now;
            if(position !=0 && timess != null){
//                timess[position-2] +=1;
//                Log.e("值：",String.valueOf(timess[position-2]));
                compute_recommend.fragment_pos=position;
            }

        }

    }
}

