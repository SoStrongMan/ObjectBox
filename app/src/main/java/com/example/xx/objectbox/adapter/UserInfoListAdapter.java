package com.example.xx.objectbox.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.xx.objectbox.R;
import com.example.xx.objectbox.bean.UserBean;

/**
 * 日期：2018/3/8
 * 描述：
 *
 * @author XX
 */

public class UserInfoListAdapter extends BaseQuickAdapter<UserBean, BaseViewHolder> {

    public UserInfoListAdapter() {
        super(R.layout.recycle_item_user_info);
    }

    @Override
    protected void convert(BaseViewHolder helper, UserBean item) {
        helper.setText(R.id.tv_content,
                String.format("ID：%s\n姓名：%s\n性别：%s\n年龄：%s",
                        item.getId(),
                        item.getName(),
                        item.getSex() == 0 ? "男" : "女",
                        item.getOld()));
    }
}
