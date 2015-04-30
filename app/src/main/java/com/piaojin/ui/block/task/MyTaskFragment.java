package com.piaojin.ui.block.task;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.piaojin.common.CommonResource;
import com.piaojin.common.TaskResource;
import com.piaojin.dao.EmployDAO;
import com.piaojin.dao.MySqliteHelper;
import com.piaojin.dao.TaskDAO;
import com.piaojin.domain.Employ;
import com.piaojin.domain.Task;
import com.piaojin.helper.HttpHepler;
import com.piaojin.helper.SmSHelper;
import com.piaojin.tools.DateTimePickDialogUtil;
import com.piaojin.tools.DateUtil;
import com.piaojin.tools.MyAnimationUtils;
import com.piaojin.ui.block.upload.UploadService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import oa.piaojin.com.androidoa.R;

/**
 * Created by piaojin on 2015/4/26.
 */

public class MyTaskFragment extends Fragment {

    private Context context;
    private View view;
    private EmployDAO employDAO;
    private TaskDAO taskDAO;
    private MySqliteHelper mySqliteHelper;
    private HttpHepler httpHepler;
    private ListView mytaskList;
    private List<Map<String, Object>> list;
    private List<Task> taskList;
    private int type = 0;//0添加任务,1查看我的任务,2查看我发布的任务
    private SimpleAdapter simpleAdapter;
    private String typetext="发布人:";

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public MyTaskFragment(int type) {
        this.type = type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.task, container, false);
        init(view);
        return view;
    }

    void init(View view) {
        context = getActivity();
        mytaskList = (ListView) view.findViewById(R.id.mytaskList);
        httpHepler = new HttpHepler();
        mySqliteHelper = new MySqliteHelper(context);
        employDAO = new EmployDAO(mySqliteHelper.getReadableDatabase());
        taskDAO = new TaskDAO(mySqliteHelper.getReadableDatabase());
        taskList = new ArrayList<Task>();
        list = new ArrayList<Map<String, Object>>();
        mytaskList.setOnItemClickListener(new MyOnItemClickListener());
        initList();
    }

    public void initList() {
        if (taskList != null && taskList.size() > 0) {
            taskList.clear();
        }
        switch (type) {
            case TaskResource.TYPE_MYTASK:
                typetext="发布人:";
                taskList = taskDAO.getMyTask(1);//1这边应填写登入用户的uid
                break;
            case TaskResource.TYPE_TASK:
                typetext="接收人:";
                taskList = taskDAO.getTask(1);//1这边应填写登入用户的uid
                break;
        }
        initListMap();
        initAdapter();
    }

    private void initListMap() {
        if (taskList != null && taskList.size() > 0) {
            for (Task t : taskList) {
                Map map = new HashMap();
                //用图标表示任务的状态
                switch(t.getStatus()){
                    case TaskResource.STATUSSEND:
                        map.put("statusicon", R.drawable.smiley_50);
                        break;
                    case TaskResource.STATUSACCEPT:
                        map.put("statusicon", R.drawable.smiley_30);
                        break;
                    case TaskResource.STATUSFINISH:
                        map.put("statusicon", R.drawable.smiley_16);
                        break;
                    default:
                        map.put("statusicon", R.drawable.smiley_50);
                        break;
                }
                map.put("kid", t.getKid());
                map.put("title", t.getTitle());
                map.put("time", t.getEndtime());
                Employ e=null;
                if(type==TaskResource.TYPE_MYTASK){
                    e = employDAO.getById(t.getEid());
                }else{
                    e = employDAO.getById(t.getUid());
                }
                map.put("name", typetext + e.getName());
                list.add(map);
            }
        }
    }

    private void initAdapter() {
        if (list.size() > 0) {
            if (simpleAdapter == null) {
                simpleAdapter = new SimpleAdapter(context, list, R.layout.task_item,
                        new String[]{"kid", "title","statusicon", "time", "name"}, new int[]{R.id.kid, R.id.title,R.id.statusicon, R.id.time, R.id.name});
                mytaskList.setAdapter(simpleAdapter);
            }
        }
    }

    private class MyOnItemClickListener implements AdapterView.OnItemClickListener {

        public MyOnItemClickListener() {
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Map<String, Object> map = new HashMap<String, Object>();
            map = (HashMap<String, Object>) adapterView.getItemAtPosition(i);
            String kid = map.get("kid").toString();
            String name = map.get("name").toString();
            Task task=taskDAO.getTaskByKid(Integer.parseInt(kid));
            if(task!=null){
                Intent intent = new Intent(getActivity(), TaskDetailActivity_.class);
                intent.putExtra("type",type);
                Bundle bundle = new Bundle();
                bundle.putSerializable("task", task);
                intent.putExtra("task_bundle", bundle);
                getActivity().startActivity(intent);
            }else{
                MyToast("出现意外啦,亲!");
            }
        }
    }

    void MyToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
