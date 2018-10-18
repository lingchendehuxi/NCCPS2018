package com.android.incongress.cd.conference.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.model.ConferenceDbUtils;
import com.android.incongress.cd.conference.model.Session;
import com.android.incongress.cd.conference.utils.CommonUtils;
import com.android.incongress.cd.conference.utils.DateUtil;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SessionSearchAdapter extends BaseAdapter {

    private List<Session> datasource = new ArrayList<>();

    public SessionSearchAdapter() {
    }

    public List<Session> getDatasource() {
        return datasource;
    }

    @Override
    public int getCount() {
        return datasource.size();
    }

    @Override
    public Object getItem(int position) {
        return datasource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            convertView = CommonUtils.initView(AppApplication.getContext(), R.layout.search_child);
            holder = new Holder();
            TextView titleView = (TextView) convertView.findViewById(R.id.search_child_title);
            TextView timeView = (TextView) convertView.findViewById(R.id.search_child_time);
            holder.titleView = titleView;
            holder.timeView = timeView;
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        Session bean = datasource.get(position);
        Date date = DateUtil.getDate(bean.getSessionDay(), DateUtil.DEFAULT);
        if (AppApplication.systemLanguage == 1) {
            holder.titleView.setText(bean.getSessionName());
            holder.timeView.setText(DateUtil.getDateString(date, DateUtil.DEFAULT_CHINA_TWO) + " " + bean.getStartTime() + "-" + bean.getEndTime());
        } else {
            holder.titleView.setText(bean.getSessionNameEN());
            holder.timeView.setText(DateUtil.getDateShort(date) + " " + bean.getStartTime() + "-" + bean.getEndTime());
        }

        return convertView;
    }


    private class Holder {
        TextView titleView;
        TextView timeView;
    }

    public void search(String sessionname) {
        if (AppApplication.systemLanguage == 1) {
            datasource = ConferenceDbUtils.getSessionByName(sessionname, false);
        } else {
            datasource = ConferenceDbUtils.getSessionByName(sessionname, true);
        }
        notifyDataSetChanged();
    }
}
