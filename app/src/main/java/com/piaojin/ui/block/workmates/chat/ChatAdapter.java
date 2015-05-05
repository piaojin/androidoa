package com.piaojin.ui.block.workmates.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.piaojin.common.UserInfo;
import com.piaojin.domain.Message;

import java.util.List;

import oa.piaojin.com.androidoa.R;

/**
 * Created by piaojin on 2015/5/4.
 */
public class ChatAdapter extends BaseAdapter {

    private UserInfo userInfo;
    private List<Message> list = null;
    private LayoutInflater layoutInflater;
    private Context context;

    public ChatAdapter(List<Message> list, Context context) {
        layoutInflater = LayoutInflater.from(context);
        userInfo = new UserInfo(context);
        userInfo.init();
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ChatItem chatItem = ChatItem_.build(context);
        Message message = list.get(i);
        if (message.getSenderid() == UserInfo.employ.getUid()) {
            IMsgViewType.MSGTYPE = IMsgViewType.IMVT_TO_MSG;
        } else {
            IMsgViewType.MSGTYPE = IMsgViewType.IMVT_COM_MSG;
        }
        //发送消息
        if (IMsgViewType.MSGTYPE == IMsgViewType.IMVT_TO_MSG) {
            chatItem.lright.setVisibility(View.VISIBLE);
            chatItem.lleft.setVisibility(View.GONE);
        } else {
            //接收消息
            chatItem.lleft.setVisibility(View.VISIBLE);
            chatItem.lright.setVisibility(View.GONE);
        }
        //发送消息
        if (IMsgViewType.MSGTYPE == IMsgViewType.IMVT_TO_MSG) {

            chatItem.tvrightmsg.setText(message.getMsg());
        } else {

            chatItem.tvleftmsg.setText(message.getMsg());
        }
        return chatItem;
    }

    public void updateView(List<Message> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    //用于判断是收到消息还是发生消息
    public static class IMsgViewType {
        static int MSGTYPE = 0;
        static final int IMVT_COM_MSG = 0;
        static final int IMVT_TO_MSG = 1;
    }

    static class ViewHolder {
        public LinearLayout lleft;
        public LinearLayout lright;
        public TextView tvleftmsg;
        public TextView tvrightmsg;
        public TextView time;
    }
}
