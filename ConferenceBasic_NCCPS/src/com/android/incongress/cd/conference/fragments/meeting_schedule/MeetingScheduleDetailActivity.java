package com.android.incongress.cd.conference.fragments.meeting_schedule;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.incongress.cd.conference.HomeActivity;
import com.android.incongress.cd.conference.adapters.MeetingScheduleAdapter;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseActivity;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.model.Class;
import com.android.incongress.cd.conference.model.ConferenceDbUtils;
import com.android.incongress.cd.conference.model.Session;
import com.android.incongress.cd.conference.widget.ScrollControlViewpager;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

public class MeetingScheduleDetailActivity extends BaseActivity {
    private MeetingScheduleAdapter adapter;
    private TabLayout mTabLayout;
    private ScrollControlViewpager mViewpager;
    private CharSequence Titles[];
    private int Numboftabs;
    private ImageView close;

    private List<String> mSessionDaysList = new ArrayList<>();
    private List<Class> mRoomList = new ArrayList<>();
    private ProgressDialog mDialog;
    private TextView mTvTips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.fragment_meeting_schedule);
        mTabLayout = (TabLayout) findViewById(R.id.table_layout);
        mViewpager = (ScrollControlViewpager) findViewById(R.id.pager);
        close = (ImageView) findViewById(R.id.close);
        mViewpager.setOffscreenPageLimit(3);
        mViewpager.setCanScroll(false);

        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mTvTips = (TextView) findViewById(R.id.tv_tips);
        mTvTips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlphaAnimation alphaAnimation = new AlphaAnimation(1f,0f);
                alphaAnimation.setDuration(300);
                alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mTvTips.setVisibility(View.GONE);
                        AppApplication.setSPBooleanValue(Constants.LOOK_SCHEDULE_TIPS, true);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                mTvTips.startAnimation(alphaAnimation);
            }
        });

        if(AppApplication.getSPBooleanValue(Constants.LOOK_SCHEDULE_TIPS)) {
            mTvTips.setVisibility(View.GONE);
        }

        new MyAsyncTask().execute();
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initViewsAction() {

    }

    @Override
    protected void setContentView() {

    }

    class MyAsyncTask extends AsyncTask<Integer, Integer, String> {
        /**
         * 这里的Integer参数对应AsyncTask中的第一个参数 这里的String返回值对应AsyncTask的第三个参数
         * 该方法并不运行在UI线程当中，主要用于异步操作，所有在该方法中不能对UI当中的空间进行设置和修改
         * 但是可以调用publishProgress方法触发onProgressUpdate对UI进行操作
         */
        @Override
        protected String doInBackground(Integer... params) {
            mSessionDaysList.clear();
            List<Session> allSession = ConferenceDbUtils.getAllSession();
            for (int i = 0; i < allSession.size(); i++) {
                Session session = allSession.get(i);
                if(mSessionDaysList.size()>0) {
                    if(!(mSessionDaysList.get(mSessionDaysList.size()-1).equals(session.getSessionDay()))) {
                        mSessionDaysList.add(session.getSessionDay());
                    }
                }else {
                    mSessionDaysList.add(session.getSessionDay());
                }
            }

            Titles = new CharSequence[mSessionDaysList.size()];
            for (int i = 0; i < mSessionDaysList.size(); i++) {
                Titles[i] = mSessionDaysList.get(i);
            }

            Numboftabs = mSessionDaysList.size();
            mRoomList.addAll(ConferenceDbUtils.getAllClasses());//获取会议室
            return "";
        }

        /**
         * 这里的String参数对应AsyncTask中的第三个参数（也就是接收doInBackground的返回值）
         * 在doInBackground方法执行结束之后在运行，并且运行在UI线程当中 可以对UI空间进行设置
         */
        @Override
        protected void onPostExecute(String result) {
            mDialog.dismiss();

            adapter = new MeetingScheduleAdapter(getSupportFragmentManager(), Titles, Numboftabs,mSessionDaysList);
            mViewpager.setAdapter(adapter);
            mTabLayout.setupWithViewPager(mViewpager);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = ProgressDialog.show(MeetingScheduleDetailActivity.this, null, "loading");
        }
    }

    public void setRightListener(View view) {
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                MeetingScheduleListActionFragment listFragment = new MeetingScheduleListActionFragment();
//                ImageView view = (ImageView) CommonUtils.initView(getActivity(), R.layout.title_right_image);
//                if(AppApplication.systemLanguage == 1) {
//                    view.setImageResource(R.drawable.schedule_switch_cn);
//                }else {
//                    view.setImageResource(R.drawable.schedule_switch);
//                }
//                listFragment.setRightListener(view);
//                action(listFragment, R.string.home_schedule, view, false, false, false);
//            }
//        });
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(Constants.FRAGMENT_MEETINGSCHEDULECALENDAR);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(Constants.FRAGMENT_MEETINGSCHEDULECALENDAR);
    }
}
