package com.piaojin.ui.message;

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
public class MessageFragment extends Fragment {

    @ViewById
    public ListView MessaeList;
    private SimpleAdapter msgAdapter;
    private Context context;
    private List<Map<String,Object>> list;
    public static MessageFragment newInstance(String param1, String param2) {
        MessageFragment fragment = new MessageFragment();
        return fragment;
    }

    @AfterViews
    void init(){
        context=getActivity();
        list=new ArrayList<Map<String, Object>>();
        for(int i=0;i<6;i++){
            Map<String,Object> map=new HashMap<String,Object>();
            map.put("head_icon",R.drawable.avatar0);
            map.put("sender","桂煌");
            map.put("msg","后退，我要开始装逼了！");
            map.put("last_time","2015-3-26");
            list.add(map);
        }
        msgAdapter =new SimpleAdapter(context,list,R.layout.messagelist_item,new String[]{
                "head_icon","sender","msg","last_time"
        },new int[]{R.id.head_icon,R.id.sender,R.id.msg,R.id.last_time});
        MessaeList.setAdapter(msgAdapter);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_message, container, false);
    }

}
