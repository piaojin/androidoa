package com.piaojin.ui.block.task;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import oa.piaojin.com.androidoa.R;

/**
 * Created by piaojin on 2015/4/26.
 */

@EFragment
public class TaskFragment extends Fragment {

    @ViewById
    com.piaojin.myview.ClearEditText taskEmploy;
    InputMethodManager inputMethodManager;


    private int type=0;//0添加任务,1查看已有任务

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        return inflater.inflate(R.layout.task_fragment, container, false);
    }

    //点击选择任务接收人
    @Click
    void taskEmploy(){
        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),0);
        SelectEmployDialog selectEmployDialog=new SelectEmployDialog();
        selectEmployDialog.show(getActivity().getFragmentManager(),"SelectEmployDialog");
    }
}
