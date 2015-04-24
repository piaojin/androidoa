package com.piaojin.ui.sms;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oa.piaojin.com.androidoa.R;

@EFragment
public class SmSFragment extends Fragment {
    @ViewById
    ListView smslist;
    private SimpleAdapter smsAdapter;
    private Context context;
    private List<Map<String,Object>> list;
    public static SmSFragment newInstance(String param1, String param2) {
        SmSFragment fragment = new SmSFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sms, container, false);
    }

    @AfterViews
    void init(){
        context=getActivity();
        list=new ArrayList<Map<String, Object>>();
        for(int i=0;i<6;i++){
            Map<String,Object> map=new HashMap<String,Object>();
            map.put("sms_title","请看公告！");
            map.put("sms_sender","飘金");
            map.put("sms_send_time","2015-3-26");
            list.add(map);
        }
        smsAdapter=new SimpleAdapter(context,list,R.layout.smslist_item,new String[]{
                "sms_title","sms_sender","sms_send_time"
        },new int[]{R.id.sms_title,R.id.sms_sender,R.id.sms_send_time});
        smslist.setAdapter(smsAdapter);
    }
}
