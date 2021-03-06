package com.piaojin.ui.block.workmates.chat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.piaojin.common.LookResource;
import com.piaojin.event.LookEvent;
import com.piaojin.otto.BusProvider;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import oa.piaojin.com.androidoa.R;

@EFragment
public class LookFragment extends Fragment {

    final static int LOOKITEMNUM = 20;
    private int number = 0;
    @ViewById
    GridView GVlook;
    int[] look;//表情集合
    int[] showlook;
    private SimpleAdapter simpleAdapter;
    List<Map<String, Object>> list;

    public static LookFragment newInstance(String param1, String param2) {
        LookFragment fragment = new LookFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        list = new ArrayList<Map<String, Object>>();
        return inflater.inflate(R.layout.fragment_look, container, false);
    }

    @AfterViews
    void init() {
        initLook();
        GVlook.setOnItemClickListener(new MyItemClickListener());
        simpleAdapter = new SimpleAdapter(getActivity(), list, R.layout.gvlook_item, new String[]{"look_img"}, new int[]{R.id.look_img});
        GVlook.setAdapter(simpleAdapter);

        System.out.println("$$$表情总数:" + look.length + ",要显示的表情个数:" + showlook.length + "总页数:" + LookResource.pagecount + ",当前所处页数:" + LookResource.page
                + ",最后一页表情个数:" + LookResource.LastPageLookCount + ",statr:" + LookResource.start + ",number:" + number);
    }

    private void initLook(){
        //获取所有的表情集合
        if(!LookResource.isLoadLook){
            LookResource.LoadLookId(getResources(),R.array.look);
            //初始化表情总个数
            LookResource.initPageCount();
        }
        look = LookResource.looks;

        //设置表情的开始位置
        LookResource.doStartAndEnd();
        //设置最后一页表情的个数
        LookResource.doLastPageLookCount(LookResource.lookcount);

        //是否为最后一页
        if (LookResource.isLastPage() && !LookResource.isEQLOOKITEMNUM()) {
            number = LookResource.LastPageLookCount;
        } else {
            number = LookResource.LOOKITEMNUM;
        }

        System.out.println("$$$start:"+LookResource.start);
        //存放要显示的表情集合
        showlook = new int[number];
        for (int i = 0; i < number; i++) {
            showlook[i] = look[LookResource.start + i];
        }

        for (int i = 0; i < showlook.length; i++) {
            Map<String, Object> map = new Hashtable<String, Object>();
            map.put("look_img", showlook[i]);
            list.add(map);
        }

    }

    //表情点击事件
    private class MyItemClickListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            BusProvider.getInstance().post(new LookEvent(i+""));
        }
    }

    //更新表情
    public void notifyDataSetChanged(){
        list.clear();
        initLook();
        simpleAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    void MyToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
}
