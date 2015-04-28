package com.piaojin.ui.block.personalfile;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import com.piaojin.dao.FileDAO;
import com.piaojin.dao.MySqliteHelper;
import com.piaojin.domain.MyFile;
import com.piaojin.event.StartUploadEvent;
import com.piaojin.event.UploadFinishEvent;
import com.piaojin.module.AppModule;
import com.piaojin.otto.BusProvider;
import com.piaojin.tools.ActionBarTools;
import com.piaojin.tools.ExitApplication;
import com.piaojin.tools.FileUtil;
import com.piaojin.ui.block.upload.UploadDialog;
import com.squareup.otto.Subscribe;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import dagger.ObjectGraph;
import oa.piaojin.com.androidoa.HomeActivity_;
import oa.piaojin.com.androidoa.R;

@EActivity(R.layout.activity_personal_file)
public class PersonalFileActivity extends Activity {

    @ViewById
    ListView myFileListView;
    @ViewById
    ImageView empty;
    private List<MyFile> myFileList;
    private MySqliteHelper mySqliteHelper;
    private FileDAO fileDAO;
    private List<Map<String, Object>> list;
    private ObjectGraph objectGraph;
    private SimpleAdapter simpleAdapter;
    private UploadDialog uploadDialog;
    private MyFileSelectDialog myFileSelectDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化dagger
        objectGraph = ObjectGraph.create(new AppModule(this));
        objectGraph.inject(this);
        //把当前Activity放入集合，方便最后完全退出程序
        ExitApplication.getInstance().addActivity(this);
    }

    private void initMyFileList() {
        if (list != null && list.size() > 0) {
            simpleAdapter = new SimpleAdapter(this, list, R.layout.explore_item,
                    new String[]{"explorefileicon", "explorefilename", "explorefiletime", "explorefilesize", "explorefilefid"}, new int[]{R.id.explorefileicon, R.id.explorefilename
                    , R.id.explorefiletime, R.id.explorefilesize, R.id.explorefilefid});
                myFileListView.setAdapter(simpleAdapter);
        }else{
            empty.setVisibility(View.VISIBLE);
        }
    }

    private void initList() {
        if (myFileList != null && myFileList.size() > 0) {
            list = new ArrayList<Map<String, Object>>();
            for (int i = 0; i < myFileList.size(); i++) {
                MyFile tempfile = myFileList.get(i);
                Map map = new HashMap();
                //设置文件的图标
                int filetype = FileUtil.getFileType(new File(tempfile.getAbsoluteurl()));
                if (filetype != -1) {
                    map.put("explorefileicon", filetype);
                } else {
                    map.put("explorefileicon", R.drawable.weizhi);
                }
                //初始化文件名
                map.put("explorefilename", tempfile.getName());
                //初始化文件的大小
                map.put("explorefilesize", FileUtil.FormetFileSize(tempfile.getFilesize().longValue()));
                //初始化文件的最后编辑时间
                //获取文件最后编辑的时间
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(new File(tempfile.getAbsoluteurl()).lastModified());
                map.put("explorefiletime", sdf.format(cal.getTime()));
                //初始化文件的fid
                map.put("explorefilefid", tempfile.getFid());
                list.add(map);
            }
        }
    }

    @AfterViews
    void init() {
        mySqliteHelper = new MySqliteHelper(this);
        fileDAO = new FileDAO(mySqliteHelper.getReadableDatabase());
        myFileList = fileDAO.getAllDownFile();
        initActionBar();
        initList();
        initMyFileList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initActionBar();
        BusProvider.getInstance().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
    }

    private void initActionBar() {
        ActionBarTools.setActionBarLayout(R.layout.workmates_actionbar, this);
        ActionBarTools.setTitleText("我的文件");
        ActionBarTools.setButtonText("上传");
        ActionBarTools.HideBtnaddSchedule(true);
        ActionBarTools.HideBtnBack(true);
        ActionBarTools.HideBtnBack2(false);
    }


    //返回按钮点击事件
    public void back(View view) {
        HomeActivity_.intent(this).start();
        overridePendingTransition(R.anim.scale_in, R.anim.down_out);
    }

    //上传文件按钮事件
    public void addSchedule(View view) {
        myFileSelectDialog = new MyFileSelectDialog();
        Fragment fragment=getFragmentManager().findFragmentByTag("MyFileSelectDialog");
        if(fragment!=null&&!fragment.isRemoving()){
            getFragmentManager().beginTransaction().remove(myFileSelectDialog).commitAllowingStateLoss();
        }
        myFileSelectDialog.show(getFragmentManager(), "MyFileSelectDialog");
        myFileSelectDialog.setCancelable(true);
    }

    //otto
    //开始上传文件
    @Subscribe
    public void onStartUploadEvent(StartUploadEvent startUploadEvent) {
        uploadDialog = new UploadDialog(startUploadEvent.getFile());
        Fragment fragment=getFragmentManager().findFragmentByTag("UploadDialog");
        if(fragment!=null&&!fragment.isRemoving()) {
            getFragmentManager().beginTransaction().remove(uploadDialog).commitAllowingStateLoss();
        }
        uploadDialog.show(getFragmentManager(), "UploadDialog");
    }

    //文件上传结束
    @Subscribe
    public void onUploadFinishEvent(UploadFinishEvent uploadFinishEvent) {
        if(list!=null){
            list.clear();
        }
        if(uploadDialog!=null){
            getFragmentManager().beginTransaction().remove(uploadDialog).commitAllowingStateLoss();
        }
        init();
        if(list==null||list.size()<=0){
            empty.setVisibility(View.VISIBLE);
        }else{
            simpleAdapter.notifyDataSetChanged();
            empty.setVisibility(View.GONE);
        }
    }

    void MyToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
