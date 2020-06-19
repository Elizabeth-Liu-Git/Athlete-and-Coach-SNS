package com.example.activitymonitor;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


/**
 * PageAdapter Class which is used for the tab layout and the handling of tab changes
 */
public class PageAdapter extends FragmentPagerAdapter {

    private int numTabs;

    /**
     * PageAdapter()
     * @param fm FragmentManager
     * @param numTabs number of tabs in the layout
     */
    public PageAdapter(@NonNull FragmentManager fm, int numTabs) {
        super(fm);
        this.numTabs = numTabs;
    }

    /**
     * getItem()
     * @param position position index that numerically identifies each tab
     * @return returns an instance of the tab dependent on index
     */
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new tab_0_upcoming();

            case 1:
                return new tab_1_history();

            default:
                return null;
        }
    }

    /**
     * getCount()
     * serves to find the amount of tabs in the layout
     * @return returns the amount of tabs in the layout
     */
    @Override
    public int getCount() {
        return 2;//Returns the # of tabs
    }

    /**
     * getItemPosition()
     * @param object (object) which position is requested
     * @return The position of the object
     */
    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
