package com.piaojin.ui.block.personalfile;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.piaojin.dao.MySqliteHelper;
import oa.piaojin.com.androidoa.R;

/**
 * Created by piaojin on 2015/4/22.
 */
public class ActionDialog extends DialogFragment {

    private View view;
    private TextView actionname;
    private TextView action;
    private int actionType = -1;
    private Context context;
    private String title;
    private int id;
    private MySqliteHelper mySqliteHelper;

    public ActionDialog(int actionType, Context context, String title, int id) {
        this.actionType = actionType;
        this.context = context;
        this.title = title;
        this.id = id;
    }

    private void deleteMessage() {

    }

    private void deleteTask() {

    }

    private void deletefile() {

    }

    private void deleteNotif() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.action, null);
        return view;
    }

    private void init(View view) {
        actionname = (TextView) view.findViewById(R.id.actionname);
        action = (TextView) view.findViewById(R.id.action);
    }
}
