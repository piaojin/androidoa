package com.piaojin.ui.message;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.piaojin.common.ActionResource;
import com.piaojin.dao.ChatDAO;
import com.piaojin.dao.EmployDAO;
import com.piaojin.dao.MySqliteHelper;
import com.piaojin.domain.Chat;
import com.piaojin.domain.Employ;
import com.piaojin.ui.block.personalfile.ActionDialog;
import com.piaojin.ui.block.workmates.chat.ChatActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oa.piaojin.com.androidoa.HomeActivity;
import oa.piaojin.com.androidoa.R;

@EFragment
public class MessageFragment extends Fragment {

    private Employ employ;
    private EmployDAO employDAO;
    private ChatDAO chatDAO;
    private MySqliteHelper mySqliteHelper;
    private List<Chat> chatList;
    @ViewById
    public ListView MessaeList;
    private SimpleAdapter msgAdapter;
    private Context context;
    private List<Map<String,Object>> list;
    private ActionDialog actionDialog;

    @AfterViews
    void init(){
        context=getActivity();
        mySqliteHelper=new MySqliteHelper(context);
        chatDAO=new ChatDAO(mySqliteHelper.getReadableDatabase());
        chatList=new ArrayList<Chat>();
        list=new ArrayList<Map<String, Object>>();
        initList();
        initData();
        msgAdapter =new SimpleAdapter(context,list,R.layout.messagelist_item,new String[]{
                "tvKid","head_icon","name","msg","last_time"
        },new int[]{R.id.tvKid,R.id.head_icon,R.id.name,R.id.msg,R.id.last_time});
        MessaeList.setAdapter(msgAdapter);
        MessaeList.setOnItemClickListener(new MyOnItemClickListener());
        MessaeList.setOnItemLongClickListener(new MyOnItemLongClickListener());
    }

    private void initList(){

        chatList=chatDAO.getAllChat();
    }

    private void initData(){
        list.clear();
        for(Chat chat:chatList){
            Map<String,Object> map=new HashMap<String,Object>();
            map.put("head_icon",R.drawable.avatar0);
            map.put("tvKid",chat.getKid());
            map.put("name",chat.getName());
            map.put("msg",chat.getMsg());
            map.put("last_time",chat.getTime());
            list.add(map);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_message, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private class MyOnItemClickListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if(employDAO==null){
                employDAO=new EmployDAO(mySqliteHelper.getReadableDatabase());
            }
            Map<String, Object> map = new HashMap<String, Object>();
            map = (HashMap<String, Object>) adapterView.getItemAtPosition(i);
            int kid=Integer.valueOf(map.get("tvKid").toString());
            employ=employDAO.getById(kid);
            Intent intent = new Intent(getActivity(), ChatActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("chat_employ", employ);
            intent.putExtra("chat_employ_bundle", bundle);
            getActivity().startActivity(intent);
        }
    }
    private class MyOnItemLongClickListener implements AdapterView.OnItemLongClickListener{

        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

            Map<String, Object> map = new HashMap<String, Object>();
            map = (HashMap<String, Object>) adapterView.getItemAtPosition(i);
            int kid=Integer.valueOf(map.get("tvKid").toString());
            actionDialog=new ActionDialog(ActionResource.ACTION_CHAT,getActivity(),"聊天列表",kid,MessageFragment.this);
            actionDialog.show(getActivity().getFragmentManager(),"ActionDialog2");
            return true;
        }
    }

    public void updateView(){
        initList();
        initData();
        msgAdapter.notifyDataSetChanged();
    }

    void MyToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
