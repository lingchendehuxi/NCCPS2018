package com.android.incongress.cd.conference.fragments.search_schedule;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.incongress.cd.conference.adapters.SearchResultAdapter;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.fragments.meeting_schedule.SessionDetailViewPageFragment;
import com.android.incongress.cd.conference.model.Class;
import com.android.incongress.cd.conference.model.ConferenceDbUtils;
import com.android.incongress.cd.conference.model.Meeting;
import com.android.incongress.cd.conference.model.Session;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jacky on 2016/3/11.
 * 查询结果
 */
public class SearchResultActionFragment extends BaseFragment {
    private ListView mLvSearchResult;
    private SearchResultAdapter mAdapter;
    private List<Session> mSessionBeans;
    private List<Meeting> mMeetingBeans;
    private List<Class> mClasses;

    private TextView mTvNoDataTips;

    private List<Session> mSessionList = new ArrayList<>();

    private static String BUNDLE_SEARCH_DAY = "searchDay";
    private static String BUNDLE_SEARCH_ROOM = "searchRoom";
    private static String BUNDLE_SEARCH_START_TIME = "searchStartTime";
    private static String BUNDLE_SEARCH_END_TIME = "searchEndTime";

    private String mSearchDay = "";
    private String mSearchRoom = "";
    private String mSearchStartTime = "";
    private String mSearchEndTime = "";

    public static final SearchResultActionFragment getInstance(String searchDay, String searchRoom, String searchStartTime, String searchEndTime) {
        SearchResultActionFragment fragment = new SearchResultActionFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_SEARCH_DAY, searchDay);
        bundle.putString(BUNDLE_SEARCH_ROOM, searchRoom);
        bundle.putString(BUNDLE_SEARCH_START_TIME, searchStartTime);
        bundle.putString(BUNDLE_SEARCH_END_TIME, searchEndTime);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_result, null);

        Bundle bundle = getArguments();

        mSearchDay = bundle.getString(BUNDLE_SEARCH_DAY);
        mSearchRoom = bundle.getString(BUNDLE_SEARCH_ROOM);
        mSearchStartTime = bundle.getString(BUNDLE_SEARCH_START_TIME);
        mSearchEndTime = bundle.getString(BUNDLE_SEARCH_END_TIME);

        mSessionBeans = ConferenceDbUtils.getSessionByTimeAndRoom(mSearchDay,mSearchRoom,mSearchStartTime,mSearchEndTime);
        mMeetingBeans = ConferenceDbUtils.getMeetingBySessions(mSessionBeans);
        mClasses = ConferenceDbUtils.getAllClasses();

        mTvNoDataTips = (TextView) view.findViewById(R.id.tv_tips);
        mLvSearchResult = (ListView) view.findViewById(R.id.lv_search_result);

        mSessionList.addAll(ConferenceDbUtils.getAllSession());

        if(mSessionBeans.size() == 0) {
            mTvNoDataTips.setVisibility(View.VISIBLE);
            mLvSearchResult.setVisibility(View.GONE);
        }else {
            mAdapter = new SearchResultAdapter(getActivity(), mSessionBeans, mMeetingBeans, mClasses);
            mLvSearchResult.setAdapter(mAdapter);

            mLvSearchResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Session session = mSessionBeans.get(position);

                    SessionDetailViewPageFragment detail = new SessionDetailViewPageFragment();
                    detail.setArguments(getSessionPosition(session.getSessionGroupId()), mSessionList);
                    action(detail, R.string.meeting_schedule_detail_title, false, false, false);
                }
            });
        }

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

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(Constants.FRAGMENT_SEARCHRESULT);
    }


    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(Constants.FRAGMENT_SEARCHRESULT);
    }
}
