package com.piaojin.ui.block.workmates.chat;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.piaojin.domain.MyFile;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import oa.piaojin.com.androidoa.R;

/**
 * Created by piaojin on 2015/3/21.
 */
@EViewGroup(R.layout.chat_listview_item)
public class ChatItem extends RelativeLayout {
    Context context;
    @ViewById
    TextView tvleftmsg;
    @ViewById
    TextView tvrightmsg;
    @ViewById
    TextView time;
    @ViewById
    LinearLayout lleft;
    @ViewById
    LinearLayout lright;

    public ChatItem(Context context) {
        super(context);
        this.context = context;
    }

    public ChatItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public ChatItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @AfterViews
    void init() {

    }

    void MyToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
