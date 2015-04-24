package com.piaojin.ui.home;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.piaojin.common.CommonResource;
import com.piaojin.ui.block.personalfile.PersonalFileActivity_;
import com.piaojin.ui.block.schedule.ScheduleActivity_;
import com.piaojin.ui.block.sharedfile.SharedFileActivity_;
import com.piaojin.ui.block.workmates.WorkMatesActivity_;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import oa.piaojin.com.androidoa.R;

@EFragment
public class HomeFragment extends Fragment {

    @ViewById
    GridView GVhome;
    private GVhomeAdapter GVadapter;
    private Context context;
    private int num[];//存放数字角标

    @AfterViews
    void init(){
        context=getActivity();
        num=new int[]{1,2,3,4,5,6,7,8,9,0,11};
        GVhome.setSelector(new ColorDrawable(Color.TRANSPARENT));
        GVadapter=new GVhomeAdapter(getActivity(),num);
        GVhome.setAdapter(GVadapter);
        GVhome.setOnItemClickListener(new GVOnItemClickListener());
    }
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    private class GVOnItemClickListener implements OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            //Toast.makeText(context,"位置:"+i,Toast.LENGTH_SHORT).show();
            num[i]=num[i]+1;
            GVadapter.notifyDataSetChanged();
            switch (i){
                case CommonResource.EMPLOY: //同事列表
                    WorkMatesActivity_.intent(getActivity()).start();
                    getActivity().overridePendingTransition(R.anim.down_in, R.anim.scale_out);
                    break;
                case CommonResource.SCHEDULE: //日程安排
                    ScheduleActivity_.intent(getActivity()).start();
                    getActivity().overridePendingTransition(R.anim.down_in, R.anim.scale_out);
                    break;
                case CommonResource.MYFILE: //个人文件
                    PersonalFileActivity_.intent(getActivity()).start();
                    getActivity().overridePendingTransition(R.anim.down_in, R.anim.scale_out);
                    break;
                case CommonResource.SHAREDFILE://共享文件
                    SharedFileActivity_.intent(getActivity()).start();
                    break;
            }
        }
    }
}
