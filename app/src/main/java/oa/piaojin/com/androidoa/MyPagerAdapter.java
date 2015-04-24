package oa.piaojin.com.androidoa;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

public class MyPagerAdapter extends FragmentStatePagerAdapter  {

    private List<Fragment> fragmentList;
    public MyPagerAdapter(FragmentManager fm,List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList=fragmentList;
    }

    /**
     * 得到每个页面
     */
    @Override
    public Fragment getItem(int arg0) {
        // TODO Auto-generated method stub
        return (fragmentList == null || fragmentList.size() == 0) ? null : fragmentList.get(arg0);
    }

    /**
     * 页面的总个数
     */
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return fragmentList == null ? 0 : fragmentList.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //System.out.println( "position Destory" + position);
        //super.destroyItem(container, position, object);
    }
}