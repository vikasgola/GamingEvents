package io.github.vikasgola.gamingevent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                Bundle bundle =new Bundle();
                bundle.putString("key","live");
                Current current = new Current();
                current.setArguments(bundle);
                return current;
            case 1:
                Bundle bundle1 =new Bundle();
                bundle1.putString("key","upcoming");
                Current current1 = new Current();
                current1.setArguments(bundle1);
                return current1;
            case 2:
                Bundle bundle2 =new Bundle();
                bundle2.putString("key","finished");
                Current current2 = new Current();
                current2.setArguments(bundle2);
                return current2;
            default:
                break;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Live";
            case 1:
                return "Upcoming";
            case 2:
                return "Finished";
            default:
                break;
        }
        return "";
    }


}
