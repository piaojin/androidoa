package com.piaojin.ui.more;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.piaojin.helper.MySharedPreferences;
import com.piaojin.tools.ExitApplication;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import javax.inject.Inject;

import oa.piaojin.com.androidoa.MainActivity_;
import oa.piaojin.com.androidoa.R;

@EFragment
public class MoreFragment extends Fragment {

    @ViewById
    Button logout;
    @ViewById
    Button exit;
    @Inject
    MySharedPreferences mySharedPreferences;
    public static MoreFragment newInstance(String param1, String param2) {
        MoreFragment fragment = new MoreFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_more, container, false);
    }

    @AfterViews
    void init(){
        mySharedPreferences=new MySharedPreferences(getActivity());
    }
    @Click
    void logout(){
        mySharedPreferences.putString("pwd","");
        mySharedPreferences.putBoolean("isAutoLogin", false);
        MainActivity_.intent(getActivity()).start();
    }

    @Click
    void exit(){
        ExitApplication.getInstance().exit(getActivity());
    }

    void MyToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
}
