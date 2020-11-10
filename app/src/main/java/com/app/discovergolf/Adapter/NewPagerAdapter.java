package com.app.discovergolf.Adapter;



import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;


/**
 * Created by DELL on 15-01-2018.
 */

 public abstract class NewPagerAdapter extends FragmentPagerAdapter {
    private int mCurrentPosition = -1;

    public NewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * @param container View container (instance of {@link WrappingViewPager}))
     * @param position  Item position
     * @param object    {@link Fragment}
     */
    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);

        if (!(container instanceof WrappingViewPager)) {
            throw new UnsupportedOperationException("ViewPager is not a WrappingViewPager");
        }

        if (position != mCurrentPosition) {
            Fragment fragment = (Fragment) object;
            WrappingViewPager pager = (WrappingViewPager) container;
            if (fragment != null && fragment.getView() != null) {
                mCurrentPosition = position;
                pager.onPageChanged(fragment.getView());
            }
        }
    }
}