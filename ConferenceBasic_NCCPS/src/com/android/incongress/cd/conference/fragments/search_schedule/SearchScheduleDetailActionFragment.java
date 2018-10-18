package com.android.incongress.cd.conference.fragments.search_schedule;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.incongress.cd.conference.adapters.SessionSearchAdapter;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.fragments.meeting_schedule.SessionDetailViewPageFragment;
import com.android.incongress.cd.conference.model.ConferenceDbUtils;
import com.android.incongress.cd.conference.model.Role;
import com.android.incongress.cd.conference.model.Session;
import com.android.incongress.cd.conference.utils.StringUtils;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jacky on 2016/4/16.
 * 查询页面
 */
public class SearchScheduleDetailActionFragment extends BaseFragment {
    private ListView mLvSearchResult;
    private TextView mTvNoData;
    private EditText mCetSearch;

    private SessionSearchAdapter mSessionAdapter;

    private List<Session> mSessionList = new ArrayList<>();
    private List<Role> mRoleList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_schedule_search,null);

        mLvSearchResult = (ListView) view.findViewById(R.id.lv_search_result);
        mTvNoData = (TextView) view.findViewById(R.id.tv_tips);


        mLvSearchResult.setEmptyView(mTvNoData);

        mSessionAdapter = new SessionSearchAdapter();
        mLvSearchResult.setAdapter(mSessionAdapter);

        mSessionList.addAll(ConferenceDbUtils.getAllSession());
        mRoleList.addAll(ConferenceDbUtils.getAllRoles());
        mCetSearch = (EditText) view.findViewById(R.id.search_search);

        mCetSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchText = s.toString();

                if(!StringUtils.isBlank(searchText)) {
                    mSessionAdapter.search(searchText);
                }
            }
        });

        mLvSearchResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Session session = mSessionAdapter.getDatasource().get(position);

                if(session != null) {
                    SessionDetailViewPageFragment detail = new SessionDetailViewPageFragment();
                    detail.setArguments(getSessionPosition(session.getSessionGroupId()), mSessionList);
                    action(detail, getString(R.string.meeting_schedule_detail_title), false, false, false);
                }
            }
        });

        return view;
    }


    /**
     * 获取Meeting在session中的位置
     * @param sessionGroupId
     * @return
     */
    private int getSessionPosition(int sessionGroupId) {
        for (int i = 0; i < mSessionList.size(); i++) {
            Session bean = mSessionList.get(i);
            if (bean.getSessionGroupId() == sessionGroupId) {
                return i;
            }
        }
        return 0;
    }
}
