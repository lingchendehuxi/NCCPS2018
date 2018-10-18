package com.android.incongress.cd.conference.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.beans.ActivityBean;
import com.android.incongress.cd.conference.beans.AlertBean;
import com.android.incongress.cd.conference.model.Alert;
import com.android.incongress.cd.conference.model.ConferenceDbUtils;
import com.android.incongress.cd.conference.utils.AlarmUtils;
import com.android.incongress.cd.conference.utils.AlermClock;
import com.android.incongress.cd.conference.utils.DateUtil;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.yqritc.recyclerviewflexibledivider.FlexibleDividerDecoration;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.Date;
import java.util.List;

/**
 * Created by Jacky on 2016/1/21.
 */
public class MyTaskFragmentAdapter extends RecyclerView.Adapter<MyTaskFragmentAdapter.ViewHolder>  implements
        FlexibleDividerDecoration.PaintProvider,
        FlexibleDividerDecoration.VisibilityProvider,
        HorizontalDividerItemDecoration.MarginProvider{
    private List<ActivityBean> mActivitys;
    private Context mContext;

    @Override
    public int dividerLeftMargin(int position, RecyclerView parent) {
        return 0;
    }

    @Override
    public int dividerRightMargin(int position, RecyclerView parent) {
        return 0;
    }

    @Override
    public Paint dividerPaint(int position, RecyclerView parent) {
        Paint paint = new Paint();
        paint.setColor(mContext.getResources().getColor(R.color.alpha_gray));
        paint.setStrokeWidth(18);
        return paint;
    }

    @Override
    public boolean shouldHideDivider(int position, RecyclerView parent) {
        return false;
    }

    public MyTaskFragmentAdapter(List<ActivityBean> mActivitys, Context mContext) {
        this.mActivitys = mActivitys;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_secretay, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ActivityBean bean = mActivitys.get(position);
        Date date = DateUtil.getDate(bean.getDate(), DateUtil.DEFAULT);
        if(AppApplication.systemLanguage == 1) {
            holder.tvTime.setText(DateUtil.getDateString(date, DateUtil.DEFAULT_CHINA_TWO)  + " " + bean.getStart_time() + "-" + bean.getEnd_time());
            holder.tvRole.setText(bean.getTypeName());
            holder.tvActivityName.setText(bean.getActivityName());
            holder.tvLocation.setText(bean.getLocation());
        }else {
            holder.tvTime.setText(DateUtil.getDateString(date, DateUtil.DEFAULT_ENGLISH) + " " + bean.getStart_time() +"-" + bean.getEnd_time());
            holder.tvRole.setText(bean.getTypeEnName());
            holder.tvActivityName.setText(bean.getActivityNameEN());
            holder.tvLocation.setText(bean.getLocationEn());
        }
        Alert alertbean = new Alert();
        alertbean.setDate(bean.getDate());
        alertbean.setEnable(1);
        alertbean.setEnd(bean.getEnd_time());
        alertbean.setRelativeid(String.valueOf(bean.getIsSessionOrMeeting()));
        alertbean.setRepeatdistance("5");
        alertbean.setRepeattimes("0");
        alertbean.setRoom(bean.getLocation());
        alertbean.setStart(bean.getStart_time());
        alertbean.setTitle(bean.getActivityName());
        alertbean.setType(AlertBean.TYPE_SESSTION);
        if(!AppApplication.getSPBooleanValue(String.valueOf(bean.getIsSessionOrMeeting()))){
            AlarmUtils.addMeetingAlarm(mContext, alertbean);
        }
        AppApplication.setSPBooleanValue(String.valueOf(bean.getIsSessionOrMeeting()),true);
    }

    @Override
    public int getItemCount() {
        return mActivitys.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvRole;
        TextView tvActivityName;
        TextView tvTime;
        TextView tvLocation;

        public ViewHolder(View itemView) {
            super(itemView);

            tvRole = (TextView) itemView.findViewById(R.id.tv_role_name);
            tvActivityName = (TextView) itemView.findViewById(R.id.tv_activity_name);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvLocation = (TextView) itemView.findViewById(R.id.tv_location);
        }
    }
}
