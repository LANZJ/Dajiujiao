package com.jopool.crow.imkit.activity.group;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.jopool.crow.R;
import com.jopool.crow.imlib.utils.CWToastUtil;
import com.jopool.crow.imlib.utils.CWValidator;

/**
 * 编辑群聊名称
 * Created by wuhk on 2016/11/3.
 */
public class CWGroupModifyNameFragment extends Fragment {
    private EditText nameEt;

    public static final String PARAM_GROUP_NAME = "param_group_name";
    public static final String PARAM_GROUP_ID = "param_group_id";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cw_group_fragment_modify_name, container, false);
        nameEt = (EditText) view.findViewById(R.id.nameEt);
        initWidgets();
        return view;
    }

    private void initWidgets() {
        String name = getActivity().getIntent().getStringExtra(PARAM_GROUP_NAME);
        nameEt.setText(name);
    }

    /**
     * 保存群聊名称
     */
    public void saveGroupName() {
        String groupName = nameEt.getText().toString();
        String groupId = getActivity().getIntent().getStringExtra(PARAM_GROUP_ID);
        if (CWValidator.isEmpty(groupName)) {
            CWToastUtil.displayTextShort("请输入群聊名称");
        } else {
//            CWGroupModifyGroupNameTask.modifyGroupName(getActivity(), groupId, groupName, new AsyncTaskSuccessCallback<String>() {
//                @Override
//                public void successCallback(Result<String> result) {
//                    CWGroupDetailFragment.reload = true;
//                    getActivity().finish();
//                }
//            });

        }
    }
}
