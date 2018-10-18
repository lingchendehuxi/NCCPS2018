package com.android.incongress.cd.conference;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;

import com.android.incongress.cd.conference.beans.BusRemindBean;
import com.android.incongress.cd.conference.utils.AlarmUtils;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.List;

public class AlarmActivity extends Activity{
	//MediaPlayer alarmMusic;
	private Vibrator vibrator;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm);
		//加载指定音乐，并为之创建MediaPlayer对象
		//alarmMusic = MediaPlayer.create(this, R.raw.beep);
		//alarmMusic.setLooping(true);
		vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
		long [] pattern = {100,400,100,400};   // 停止 开启 停止 开启
		vibrator.vibrate(pattern,1);           //重复两次上面的pattern 如果只想震动一次，index设为-1
		//播放闹钟
		//alarmMusic.start();

		int type = getIntent().getIntExtra("type",1);

		if(type == 1) {
			//创建一个对话框
			String from = getIntent().getStringExtra("from");
			String to = getIntent().getStringExtra("to");

			new AlertDialog.Builder(AlarmActivity.this).setCancelable(false).setTitle("班车提醒")
					.setMessage(getString(R.string.reminder_bus_tips,from,to))
					.setPositiveButton("确定", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

							//停止音乐
							//alarmMusic.stop();
							vibrator.cancel();
							AlarmActivity.this.finish();

							Intent intent = new Intent();
							intent.setClass(AlarmActivity.this, HomeActivity.class);
							startActivity(intent);
						}
					}).show();
		}

		if(type == 2) {
			String title = getIntent().getStringExtra("meetingName");

			new AlertDialog.Builder(AlarmActivity.this).setCancelable(false).setTitle("会议提醒")
					.setMessage(title)
					.setPositiveButton("确定", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							//停止音乐
							//alarmMusic.stop();
							vibrator.cancel();
							AlarmActivity.this.finish();

							Intent intent = new Intent();
							intent.setClass(AlarmActivity.this, HomeActivity.class);
							startActivity(intent);
						}
					}).show();
		}

	}
}