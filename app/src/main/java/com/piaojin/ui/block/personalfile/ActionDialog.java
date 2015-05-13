package com.piaojin.ui.block.personalfile;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.piaojin.common.ActionResource;
import com.piaojin.dao.ChatDAO;
import com.piaojin.dao.FileDAO;
import com.piaojin.dao.MessageDAO;
import com.piaojin.dao.MySqliteHelper;
import com.piaojin.domain.Message;
import com.piaojin.domain.MyFile;
import com.piaojin.helper.FileHelper;
import com.piaojin.tools.FileUtil;
import com.piaojin.ui.message.MessageFragment;

import java.util.List;

import oa.piaojin.com.androidoa.R;

/**
 * Created by piaojin on 2015/4/22.
 */
public class ActionDialog extends DialogFragment {

    private View view;
    private PersonalFileActivity personalFileActivity;
    private TextView actionname;
    private Button open;
    private Button delete;
    private int actionType = -1;//操作类型(0个人文件,1聊天)
    private MySqliteHelper mySqliteHelper;
    private Context context;
    private String title;
    private int id;//文件或聊天id
    private FileDAO fileDAO;
    private MessageDAO messageDAO;
    private MyFile myFile;
    private MessageFragment messageFragment;
    private ChatDAO chatDAO;

    public ActionDialog(int actionType, Context context, String title, int id) {
        mySqliteHelper=new MySqliteHelper(context);
        this.actionType = actionType;
        this.context = context;
        this.title = title;
        this.id = id;
    }
    public ActionDialog(int actionType, Context context, String title, int id,MessageFragment messageFragment) {
        mySqliteHelper=new MySqliteHelper(context);
        this.actionType = actionType;
        this.context = context;
        this.title = title;
        this.id = id;
        this.messageFragment=messageFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        view = inflater.inflate(R.layout.action, null);
        init(view);
        if(actionType==ActionResource.ACTION_FILE){
            personalFileActivity=(PersonalFileActivity)context;
            open.setVisibility(View.VISIBLE);
        }
        return view;
    }

    private void init(View view) {
        actionname = (TextView) view.findViewById(R.id.actionname);
        open = (Button) view.findViewById(R.id.open);
        delete = (Button) view.findViewById(R.id.delete);
        open.setOnClickListener(new OpenListener());
        delete.setOnClickListener(new DeleteListener());
    }

    private class OpenListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {

            switch(actionType){
                case ActionResource.ACTION_FILE:

                    initData();
                    if(myFile!=null){
                        FileHelper.openFile(context,myFile.getAbsoluteurl());
                    }else{
                        MyToast("该文件不存在!");
                    }
                    fileDAO.close();
                    break;
            }
        }
    }

    private class DeleteListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {

            switch(actionType){
                case ActionResource.ACTION_FILE:

                    initData();
                    if(fileDAO.deleteById(id)){
                        personalFileActivity.updateView();
                        FileUtil.delete(myFile.getAbsoluteurl());
                        MyToast("删除成功!");
                    }else{
                        MyToast("删除失败!");
                    }
                    fileDAO.close();
                    break;
                case ActionResource.ACTION_CHAT:

                    chatDAO=new ChatDAO(mySqliteHelper.getWritableDatabase());
                    messageDAO=new MessageDAO(mySqliteHelper.getWritableDatabase());
                    chatDAO.delete(id);
                    List<Message> list=messageDAO.getVideoMessage(id);
                    for(Message m:list){

                        FileUtil.delete(m.getVideourl());
                    }
                    messageDAO.delete(id);
                    messageFragment.updateView();
                    break;
            }
            dismiss();
        }
    }

    private void initData(){
        fileDAO=new FileDAO(mySqliteHelper.getWritableDatabase());
        myFile=fileDAO.getById(id);
    }

    void MyToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
