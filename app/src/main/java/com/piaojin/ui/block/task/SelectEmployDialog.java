package com.piaojin.ui.block.task;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.piaojin.dao.DepartmentDAO;
import com.piaojin.dao.EmployDAO;
import com.piaojin.dao.MySqliteHelper;
import com.piaojin.domain.Department;
import com.piaojin.domain.Employ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oa.piaojin.com.androidoa.R;

/**
 * Created by piaojin on 2015/4/26.
 */
public class SelectEmployDialog extends DialogFragment {

    public static Employ employ;
    private View view;
    private Context context;
    private ListView departmentList;
    private ListView employList;
    private List<Map<String, Object>> dlist;
    private List<Map<String, Object>> elist;
    private MySqliteHelper mySqliteHelper;
    private DepartmentDAO departmentDAO;
    private EmployDAO employDAO;
    private SimpleAdapter dadapter;
    private SimpleAdapter eadapter;
    private List<Department> d;
    private List<Employ> e;
    private EditText taskEmploy;

    public SelectEmployDialog(EditText employtext) {
        this.taskEmploy = employtext;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        view = inflater.inflate(R.layout.select_employ, null);
        init(view);
        return view;
    }

    private void init(View view) {
        departmentList = (ListView) view.findViewById(R.id.departmentList);
        employList = (ListView) view.findViewById(R.id.employList);
        d = new ArrayList<Department>();
        e = new ArrayList<Employ>();
        context = getActivity();
        mySqliteHelper = new MySqliteHelper(context);
        dlist = new ArrayList<Map<String, Object>>();
        elist = new ArrayList<Map<String, Object>>();
        initD();
        initE();
        initDList();
        initEList();
        departmentList.setOnItemClickListener(new DepartmentItemClick());
        employList.setOnItemClickListener(new EmployItemClick());
    }

    private void initD() {
        if (departmentDAO == null) {
            departmentDAO = new DepartmentDAO(mySqliteHelper.getReadableDatabase());
        }
        if (d != null && d.size() > 0) {
            d.clear();
        }
        d = departmentDAO.getAllDepartment();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        close();
    }

    private void initE() {
        if (employDAO == null) {
            employDAO = new EmployDAO(mySqliteHelper.getReadableDatabase());
        }
        if (e != null && e.size() > 0) {
            e.clear();
        }
        e = employDAO.getAllEmploy();
    }

    private void initDList() {

        if (d != null && d.size() > 0) {
            for (Department department : d) {
                Map map = new HashMap<String, Object>();
                map.put("name", department.getDname());
                map.put("dpid", department.getDpid());
                map.put("kid", department.getKid());
                dlist.add(map);
            }
            if (dadapter == null) {
                dadapter = new SimpleAdapter(context, dlist, R.layout.select_employ_item,
                        new String[]{"name", "dpid", "kid"}, new int[]{R.id.name, R.id.dpid, R.id.kid});
                departmentList.setAdapter(dadapter);
            }
        }
    }

    private void initEList() {
        if(elist!=null){
            elist.clear();
        }
        if (e != null && e.size() > 0) {
            for (Employ employ : e) {
                Map map = new HashMap<String, Object>();
                map.put("name", employ.getName());
                map.put("dpid", employ.getUid());
                map.put("kid", employ.getKid());
                elist.add(map);
            }
            if (eadapter == null) {
                eadapter = new SimpleAdapter(context, elist, R.layout.select_employ_item,
                        new String[]{"name", "dpid", "kid"}, new int[]{R.id.name, R.id.dpid, R.id.kid});
                employList.setAdapter(eadapter);
            }
        }
    }


    private class DepartmentItemClick implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Map<String, Object> map = new HashMap<String, Object>();
            map = (HashMap<String, Object>) adapterView.getItemAtPosition(i);
            String kid = map.get("kid").toString();
            e = employDAO.getEmployByDepartment(Integer.parseInt(kid));
            if(e!=null&&e.size()>0){
                initEList();
                eadapter.notifyDataSetChanged();
            }
        }
    }

    private class EmployItemClick implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Map<String, Object> map = new HashMap<String, Object>();
            map = (HashMap<String, Object>) adapterView.getItemAtPosition(i);
            String kid = map.get("kid").toString();
            String name = map.get("name").toString();
            taskEmploy.setText(name);
            taskEmploy.setTag(kid);
            employ=employDAO.getById(Integer.parseInt(kid));
            SelectEmployDialog.this.dismiss();
        }
    }

    private void close() {
        if (departmentDAO != null) {
            departmentDAO.close();
        }
        if (employDAO != null) {
            employDAO.close();
        }
    }

    void MyToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
