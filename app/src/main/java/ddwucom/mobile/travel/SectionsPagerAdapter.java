package ddwucom.mobile.travel;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    ArrayList<String> imageList;

    public SectionsPagerAdapter(@NonNull FragmentManager fm, ArrayList<String> imageList) {
        super(fm);
        this.imageList = imageList;
    }

    @Override
    public Fragment getItem(int position) {
        return PlaceholderFragment.newInstance(position, imageList.get(position));
    }

    @Override
    public int getCount() {
        return imageList.size();
    }
}
