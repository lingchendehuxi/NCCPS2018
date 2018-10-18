package com.android.incongress.cd.conference.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;

import com.android.incongress.cd.conference.AlarmActivity;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.beans.BusRemindBean;
import com.android.incongress.cd.conference.beans.SessionBean;
import com.android.incongress.cd.conference.model.Alert;
import com.android.incongress.cd.conference.model.Session;
import com.google.zxing.common.detector.MathUtils;

import org.litepal.crud.DataSupport;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by Admin on 2017/5/18.
 */

public class AlarmUtils {
    /**
     * type 1班车提醒
     * @param context
     * @param bean
     */
    public static void addAlarm(Context context, BusRemindBean bean) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Service.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmActivity.class);

        String from = "";
        String to = "";
        if(bean.getIsStartOrBack() == 0) {
            from = bean.getBusFrom();
            to = bean.getBusTo();
        }else if(bean.getIsStartOrBack() == 1) {
            from = bean.getBusTo();
            to = bean.getBusFrom();
        }

        intent.putExtra("from", from);
        intent.putExtra("to", to);
        intent.putExtra("busId", bean.getBusInfoId());
        intent.putExtra("type", 1);

        try {
            String s = bean.getBusDate() + " " + bean.getBusTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date date = sdf.parse(s.replace("\n"," "));
            long time30 = 30*60*1000;//30分钟
            long time15 = 15*60*1000;
            if (System.currentTimeMillis() < date.getTime()) {

                int isStartOrBack = 0;
                if(bean.getIsStartOrBack() == 1) {
                    isStartOrBack = 1;
                }else {
                    isStartOrBack = 2;
                }

                PendingIntent pi30 = PendingIntent.getActivity(context, bean.getBusInfoId() + 30 + isStartOrBack, intent, 0);
                PendingIntent pi15 = PendingIntent.getActivity(context, bean.getBusInfoId() + 15 + isStartOrBack, intent, 0);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    alarmManager.setExact(AlarmManager.RTC, date.getTime() - time30, pi30);
                    alarmManager.setExact(AlarmManager.RTC, date.getTime() - time15, pi15);
                } else {
                    alarmManager.set(AlarmManager.RTC,date.getTime() - time30, pi30);
                    alarmManager.set(AlarmManager.RTC,date.getTime() - time15, pi15);
                }
                bean.save();
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加会议闹钟 type 2 会议提醒
     * @param context
     * @param bean
     */
    public static void addMeetingAlarm(Context context, Alert bean) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Service.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmActivity.class);
        String[] tips = bean.getTitle().split("#@#");
        if(AppApplication.systemLanguage == 1)  {
            intent.putExtra("meetingName", tips[0]);
        }else {
            intent.putExtra("meetingName", tips[1]);
        }

        intent.putExtra("type", 2);

        Random random = new Random();
        int num = Math.abs(random.nextInt());
        PendingIntent pi = PendingIntent.getActivity(context,num, intent, 0);

        try {
            String s = bean.getDate() + " " + bean.getStart();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date date = sdf.parse(s.replace("\n"," "));

            long time5 = 5*60*1000;//5分钟

            if (System.currentTimeMillis() < date.getTime()) {
                alarmManager.set(AlarmManager.RTC,date.getTime() - time5, pi);
                bean.save();
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除闹钟提醒
     * @param context
     * @param bean
     */
    public static void deleteMeetingAlarm(Context context, SessionBean bean) {

    }

    public static void deleteMeetingAlarm(Context context, Alert bean) {

    }

    public static void deleteAlarm(Context context, BusRemindBean bean) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Service.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmActivity.class);
        intent.putExtra("from", bean.getBusFrom());
        intent.putExtra("to", bean.getBusTo());

        PendingIntent pi = PendingIntent.getActivity(context, bean.getBusInfoId(), intent, 0);
        alarmManager.cancel(pi);

        deleteBusResmindByBusInfoIdAndTime(bean.getBusInfoId(),bean.getIsStartOrBack());
    }


    public static List<BusRemindBean> getAllBusRemind() {
        List<BusRemindBean> reminds = DataSupport.findAll(BusRemindBean.class);
        if (reminds != null)
            return reminds;
        else
            return new ArrayList<BusRemindBean>();
    }


    /**
     * 查找是否有某个闹钟
     *
     * @param busInfoId
     * @param isStartOrBack
     * @return
     */
    public static boolean findBusRemindByBusInfoIdAndTime(int busInfoId, int isStartOrBack) {
        List<BusRemindBean> busRemindBeen = DataSupport.where("busInfoId = " + busInfoId + " and isStartOrBack = " + isStartOrBack).find(BusRemindBean.class);

        if (busRemindBeen != null && busRemindBeen.size() > 0)
            return true;
        else
            return false;
    }

    /**
     * 删除某个闹钟
     * @param busInfoId
     * @param isStartOrBack
     * @return
     */
    public static void deleteBusResmindByBusInfoIdAndTime(int busInfoId, int isStartOrBack) {
        List<BusRemindBean> busRemindBeenList = DataSupport.where("busInfoId = " + busInfoId + " and isStartOrBack = " + isStartOrBack).find(BusRemindBean.class);

        if(busRemindBeenList != null && busRemindBeenList.size() > 0) {
            for (int i = 0; i < busRemindBeenList.size(); i++) {
                busRemindBeenList.get(i).delete();
            }
        }
    }
}
