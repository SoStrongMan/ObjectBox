package com.example.xx.objectbox;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.xx.objectbox.adapter.UserInfoListAdapter;
import com.example.xx.objectbox.application.ObApplication;
import com.example.xx.objectbox.base.BaseAppCompatActivity;
import com.example.xx.objectbox.bean.UserBean;
import com.example.xx.objectbox.bean.UserBean_;
import com.example.xx.objectbox.constant.Constant;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yanzhenjie.recyclerview.swipe.widget.DefaultItemDecoration;

import java.util.List;

import butterknife.BindView;
import io.objectbox.Box;

/**
 * 日期：2018/3/7
 * 描述：主界面
 *
 * @author XX
 */

public class MainActivity extends BaseAppCompatActivity {

    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.radio_group)
    RadioGroup radioGroup;
    @BindView(R.id.rb_man)
    RadioButton rbMan;
    @BindView(R.id.rb_woman)
    RadioButton rbWoman;
    @BindView(R.id.et_old)
    EditText etOld;
    @BindView(R.id.btn_write)
    Button btnWrite;
    @BindView(R.id.btn_read)
    Button btnRead;
    @BindView(R.id.btn_remove)
    Button btnRemove;
    @BindView(R.id.btn_query)
    Button btnQuery;
    @BindView(R.id.rv_user_info)
    SwipeMenuRecyclerView rvUserInfo;

    private Box<UserBean> userBeanBox;
    private UserInfoListAdapter mAdapter;

    @Override
    protected int setLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void destroy() {

    }

    @Override
    protected void getIntentData() {

    }

    @Override
    protected void initData() {
        userBeanBox = ObApplication.getBoxStore().boxFor(UserBean.class);

        rvUserInfo.setLayoutManager(new LinearLayoutManager(this));
        rvUserInfo.setHasFixedSize(true);
        rvUserInfo.addItemDecoration(new DefaultItemDecoration(Color.GRAY));
        rvUserInfo.setSwipeMenuCreator(mSwipeMenuCreator);
        rvUserInfo.setSwipeMenuItemClickListener(mSwipeMenuItemClickListener);
        mAdapter = new UserInfoListAdapter();
        rvUserInfo.setAdapter(mAdapter);
    }

    @Override
    protected void bindEvent() {
        btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addData();
            }
        }); //写入
        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readData();
            }
        }); //读取
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeAllData();
                mAdapter.setNewData(null);
            }
        }); //删除
        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString().trim();
                String old = etOld.getText().toString().trim();
                mAdapter.setNewData(
                        query(
                                name,
                                radioGroup.getCheckedRadioButtonId() == R.id.rb_man ? 0 : 1,
                                old));
            }
        }); //查询
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case Constant.REQUEST_REFRESH:
                UserBean bean = (UserBean) data.getSerializableExtra(Constant.RESULT_BEAN);
                //这里通过主键id来修改，当然也可以先根据条件查询出相应的bean后修改
                userBeanBox.put(bean);
                readData();
                break;
            default:
                break;
        }
    }

    /**
     * 查询
     *
     * @param name 姓名
     * @param sex  性别
     * @param old  年龄
     */
    private List<UserBean> query(String name, int sex, String old) {
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(old)) {
            return userBeanBox.query().equal(UserBean_.sex, sex).and()
                    .equal(UserBean_.name, name).and()
                    .equal(UserBean_.old, old)
                    .build().find();
        }
        if (!TextUtils.isEmpty(name) && TextUtils.isEmpty(old)) {
            return userBeanBox.query().equal(UserBean_.sex, sex).and()
                    .equal(UserBean_.name, name)
                    .build().find();
        }
        if (TextUtils.isEmpty(name) && !TextUtils.isEmpty(old)) {
            return userBeanBox.query().equal(UserBean_.sex, sex).and()
                    .equal(UserBean_.old, name)
                    .build().find();
        }
        if (TextUtils.isEmpty(name) && TextUtils.isEmpty(old)) {
            return userBeanBox.query().equal(UserBean_.sex, sex)
                    .build().find();
        }

        return null;
    }

    /**
     * 添加
     */
    private void addData() {
        String name = etName.getText().toString().trim();
        String old = etOld.getText().toString().trim();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(old)) {
            Toast.makeText(this, "请完善", Toast.LENGTH_SHORT).show();
            return;
        }

        UserBean userBean = new UserBean();
        userBean.setName(name);
        userBean.setSex(radioGroup.getCheckedRadioButtonId() == R.id.rb_man ? 0 : 1);
        userBean.setOld(old);
        userBeanBox.put(userBean);
    }

    /**
     * 读取全部
     */
    private void readData() {
        mAdapter.setNewData(userBeanBox.getAll());
    }

    /**
     * 删除所有数据
     */
    private void removeAllData() {
        userBeanBox.removeAll();
    }

    /**
     * 删除单个数据
     *
     * @param id 主键id
     */
    private void removeData(long id) {
        userBeanBox.remove(id);
    }

    /**
     * 设置菜单创建器
     */
    private SwipeMenuCreator mSwipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int i) {
            SwipeMenuItem updateItem = new SwipeMenuItem(MainActivity.this)
                    .setBackground(R.color.colorPrimary)
                    .setText("修改")
                    .setTextColor(Color.WHITE)
                    .setWidth((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics()))
                    .setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            //在item右侧添加一个修改菜单
            swipeRightMenu.addMenuItem(updateItem);

            SwipeMenuItem deleteItem = new SwipeMenuItem(MainActivity.this)
                    .setBackground(R.color.colorAccent)
                    .setText("删除")
                    .setTextColor(Color.WHITE)
                    .setWidth((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics()))
                    .setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            //在item右侧添加一个删除菜单
            swipeRightMenu.addMenuItem(deleteItem);
        }
    };

    /**
     * item菜单点击监听
     */
    private SwipeMenuItemClickListener mSwipeMenuItemClickListener = new SwipeMenuItemClickListener() {
        @Override
        public void onItemClick(SwipeMenuBridge swipeMenuBridge) {
            //任何操作必须先关闭菜单，否则可能出现item菜单打开状态错乱
            swipeMenuBridge.closeMenu();

            UserBean item = mAdapter.getItem(swipeMenuBridge.getAdapterPosition());
            if (item == null) {
                return;
            }

            switch (swipeMenuBridge.getPosition()) {
                case 0:
                    //修改
                    startActivityForResult(UpdateActivity.getUpdateIntent(MainActivity.this, item), Constant.REQUEST_REFRESH);
                    break;
                case 1:
                    //删除
                    removeData(item.getId());
                    mAdapter.remove(swipeMenuBridge.getAdapterPosition());
                    break;
                default:
                    break;
            }
        }
    };
}
