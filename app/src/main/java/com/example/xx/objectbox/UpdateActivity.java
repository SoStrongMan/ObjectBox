package com.example.xx.objectbox;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.xx.objectbox.base.BaseAppCompatActivity;
import com.example.xx.objectbox.bean.UserBean;
import com.example.xx.objectbox.constant.Constant;

import butterknife.BindView;

/**
 * 日期：2018/3/7
 * 描述：修改（更新）
 *
 * @author XX
 */

public class UpdateActivity extends BaseAppCompatActivity {

    /**
     * 跳转
     *
     * @param context 上下文
     * @param bean    UserBean
     * @return 意图
     */
    public static Intent getUpdateIntent(Context context, UserBean bean) {
        Intent i = new Intent(context, UpdateActivity.class);
        i.putExtra(Constant.INTENT_BEAN, bean);
        return i;
    }

    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.rb_man)
    RadioButton rbMan;
    @BindView(R.id.rb_woman)
    RadioButton rbWoman;
    @BindView(R.id.radio_group)
    RadioGroup radioGroup;
    @BindView(R.id.et_old)
    EditText etOld;
    @BindView(R.id.btn_save)
    Button btnSave;

    private UserBean mUserBean;

    @Override
    protected int setLayout() {
        return R.layout.activity_update;
    }

    @Override
    protected void destroy() {

    }

    @Override
    protected void getIntentData() {
        if (getIntent() != null) {
            mUserBean = (UserBean) getIntent().getSerializableExtra(Constant.INTENT_BEAN);
        }
    }

    @Override
    protected void initData() {
        etName.setText(mUserBean.getName());
        if (mUserBean.getSex() == 0) {
            rbMan.setChecked(true);
        } else {
            rbWoman.setChecked(true);
        }
        etOld.setText(mUserBean.getOld());
    }

    @Override
    protected void bindEvent() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString().trim();
                String old = etOld.getText().toString().trim();
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(old)) {
                    Toast.makeText(UpdateActivity.this, "请完善", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent i = new Intent();
                UserBean userBean = new UserBean();
                userBean.setId(mUserBean.getId());
                userBean.setName(name);
                userBean.setSex(radioGroup.getCheckedRadioButtonId() == R.id.rb_man ? 0 : 1);
                userBean.setOld(old);
                i.putExtra(Constant.RESULT_BEAN, userBean);
                setResult(RESULT_OK, i);
                finish();
            }
        }); //保存
    }
}
