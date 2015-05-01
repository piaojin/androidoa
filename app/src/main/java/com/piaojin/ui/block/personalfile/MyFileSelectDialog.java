package com.piaojin.ui.block.personalfile;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.piaojin.common.FileResource;
import com.piaojin.common.UploadfileResource;
import com.piaojin.domain.MyFile;
import com.piaojin.helper.NetWorkHelper;
import com.piaojin.tools.FileUtil;
import com.piaojin.ui.block.upload.UploadService;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oa.piaojin.com.androidoa.R;

/**
 * Created by piaojin on 2015/4/12.
 */

public class MyFileSelectDialog extends DialogFragment {

    private View view;
    private ImageView nothing;
    private EditText path;
    private Button upload;
    private ImageButton select_type;
    private Button back;
    private Button cancel;
    private ListView fileList;
    private LinearLayout llphone;
    private LinearLayout llsd;
    private TextView type;
    private File parentfile;
    private File[] files;
    private FileListAdapter fileListAdapter;
    public static String ROOTPATH;
    public static String SDPATH;
    private List<Map<String, Object>> list;
    private SimpleAdapter simpleAdapter;
    private StringBuffer pathstr;//当前文件或目录绝对路径
    private boolean isfile = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        view = inflater.inflate(R.layout.file_select, null);
        init(view);
        return view;
    }

    private void init(View view) {
        upload = (Button) view.findViewById(R.id.upload);
        nothing = (ImageView) view.findViewById(R.id.nothing);
        path = (EditText) view.findViewById(R.id.path);
        llsd = (LinearLayout) view.findViewById(R.id.llsd);
        llphone = (LinearLayout) view.findViewById(R.id.llphone);
        select_type = (ImageButton) view.findViewById(R.id.select_type);
        type = (TextView) view.findViewById(R.id.type);
        back = (Button) view.findViewById(R.id.back);
        cancel = (Button) view.findViewById(R.id.cancel);
        fileList = (ListView) view.findViewById(R.id.fileList);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyFileSelectDialog.this.dismiss();
            }
        });
        upload.setOnClickListener(new UpLoadListener());
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HidellPhone(false);
                Hidellsd(false);
                if (list == null || FileResource.ROOT_PATH.equals(pathstr.toString())) {
                    return;
                }
                list.clear();
                back();
            }
        });

        select_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HidellPhone(true);
                Hidellsd(true);
            }
        });
        llphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HidellPhone(false);
                Hidellsd(false);
                setTypeText("手机存储");
            }
        });
        llsd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HidellPhone(false);
                Hidellsd(false);
                setTypeText("SD 卡");
            }
        });

        FileResource.init();
        list = new ArrayList<Map<String, Object>>();
        SDPATH = FileResource.SDPath;
        pathstr = new StringBuffer();
        pathstr.append(SDPATH);
        initList(SDPATH);
        fileListAdapter = new FileListAdapter(getActivity(), parentfile);
        simpleAdapter = new SimpleAdapter(getActivity(), list, R.layout.explore_item
                , new String[]{"explorefileicon", "explorefilename", "explorefiletime", "explorefilesize"}, new int[]{R.id.explorefileicon, R.id.explorefilename
                , R.id.explorefiletime, R.id.explorefilesize});
        fileList.setAdapter(simpleAdapter);
        fileList.setOnItemClickListener(new MyOnItemClickListener());
    }

    private class MyOnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Map<String, Object> map = new HashMap<String, Object>();
            map = (HashMap<String, Object>) adapterView.getItemAtPosition(i);
            String temppath = map.get("explorefilename").toString();
            File file = new File(pathstr.toString());
            if (!file.isDirectory()) {
                if (isfile) {
                    pathstr = pathstr.replace(0, pathstr.length(), pathstr.substring(0,
                            pathstr.lastIndexOf("/")));
                }
            }
            pathstr.append("/" + temppath);
            file = new File(pathstr.toString());
            if (file.isDirectory()) {
                list.clear();
                path.setText("");
                isfile = false;
                initList(pathstr.toString());
                simpleAdapter.notifyDataSetChanged();
            } else {
                isfile = true;
                path.setText(pathstr);
            }
        }
    }

    //初始化list
    private void initList(String path) {
        parentfile = new File(path);
        files = parentfile.listFiles();
        if (files != null && files.length > 0) {
            nothing.setVisibility(View.GONE);
            for (int i = 0; i < files.length; i++) {
                Map<String, Object> map = new HashMap<String, Object>();
                File file = files[i];
                boolean isfoler = file.isDirectory();//是否是文件夹
                if (isfoler) {
                    map.put("explorefileicon", R.drawable.dir);
                } else {
                    int filetype = getFileType(file);
                    if (filetype != -1) {
                        map.put("explorefileicon", filetype);
                    } else {
                        map.put("explorefileicon", R.drawable.weizhi);
                    }
                    //文件夹没有大小，文件才有大小
                    map.put("explorefilesize", FileUtil.FormetFileSize(file.length()));
                }
                map.put("explorefilename", file.getName());
                //获取文件最后编辑的时间
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(file.lastModified());
                map.put("explorefiletime", sdf.format(cal.getTime()));
                list.add(map);
            }
        } else {
            //目录为空
            nothing.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    //获取文件类型
    private int getFileType(File file) {
        int type = -1;
        for (int i = 0; i < FileResource.filetypename.length; i++) {
            if (file.getName().endsWith(FileResource.filetypename[i])) {
                type = FileResource.filetypeid[i];
            }
        }
        return type;
    }

    private void HidellPhone(boolean hide) {
        if (hide) {
            llphone.setVisibility(View.VISIBLE);
        } else {
            llphone.setVisibility(View.GONE);
        }
    }

    private void Hidellsd(boolean hide) {
        if (hide) {
            llsd.setVisibility(View.VISIBLE);
        } else {
            llsd.setVisibility(View.GONE);
        }
    }

    private void setTypeText(String type) {
        this.type.setText(type);
    }

    private class UpLoadListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (path.getText().toString() == null || "".equals(path.getText().toString())) {
                MyToast("请选择上传文件");
                return;
            }

            if(!NetWorkHelper.isNetWorkAvailable(getActivity())){
                return;
            }
            File file = new File(path.getText().toString());
            if (file.isFile() && file.length() > 0 && !file.isDirectory()) {
                MyFile myfile = new MyFile();
                myfile.setType(FileResource.TYPE_MY);
                myfile.setAbsoluteurl(file.getAbsolutePath());
                myfile.setUid(1);//setUid(1)
                myfile.setUrl(UploadfileResource.UPLOADURL);
                myfile.setHttpurl(UploadfileResource.SAVEURL);
                myfile.setUname("飘金");
                myfile.setName(file.getName());
                myfile.setFilesize(Double.parseDouble((file.length() + "").trim()));
                myfile.setStatus(FileResource.STATUS_DOWN);
                myfile.setCompletedsize(0.00);
                myfile.setIscomplete(0);
                Intent intent = new Intent(getActivity(), UploadService.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("myfile", myfile);
                intent.putExtra("myfile_bundle", bundle);
                getActivity().startService(intent);
            }
        }
    }

    //返回上一级按钮点击事件
    public void back() {
        if (pathstr == null) {
            return;
        }
        if (!new File(pathstr.toString()).isDirectory()) {
            pathstr = pathstr.replace(0, pathstr.length(), pathstr.substring(0,
                    pathstr.lastIndexOf("/")));
        }
        File file = new File(pathstr.toString());
        if (file.getParentFile() != null) {
            file = file.getParentFile();
            pathstr.replace(0, pathstr.length(), file.getAbsolutePath());
            initList(file.getAbsolutePath());
            simpleAdapter.notifyDataSetChanged();
        }
    }

    void MyToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
}
