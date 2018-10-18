package com.android.incongress.cd.conference.fragments.me;


import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.fragments.HandlerFragement;
import com.android.incongress.cd.conference.widget.IncongressEditText;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SettingsHelper extends HandlerFragement {
	private View mView;
	private IncongressEditText phone;
	private IncongressEditText mail;
	private IncongressEditText content;
	private int mConnectTime = 0;// 因为hession有Bug 所有采用的是如果连接5次还没有连接上就提示无法连接请重试
	public final static int MSG_SUBMIT_ERROR = 0x2002;//出现错误
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.settings_help, null);
//		MobclickAgent.onEvent(getActivity(),UMengPage.Page_Setting_Helper); //统计页面
		phone=(IncongressEditText)view.findViewById(R.id.settings_help_phone);
		mail=(IncongressEditText)view.findViewById(R.id.settings_help_mail);
		content=(IncongressEditText)view.findViewById(R.id.settings_help_content);
		return view;
	}
	public void setView(View view){
		mView=view;
		mView.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(content.getText().toString()!=null
				&&!content.getText().toString().equals("")){
					sendfeedback();
					hideShurufa();
				}else{
		            Toast.makeText(getActivity(),R.string.settings_help_content_submit_hit, 
		            		Toast.LENGTH_SHORT).show();
				}
				
			}
			
		});
	}
	private void sendfeedback(){
		boolean submitphoneflag=true;
		boolean submitmailflag=true;

		String contentvalue=content.getText().toString().trim();
		String phonenumber=phone.getText().toString();
		String emailvalue=mail.getText().toString();
		Pattern pattern=Pattern.compile("^[1][3-8]+\\d{9}");
		Matcher matcher=pattern.matcher(phonenumber);
		if(phonenumber!=null&&!"".equals(phonenumber)){
			if (!matcher.matches()) {
				Toast.makeText(getActivity(), getString(R.string.nophonenumber), Toast.LENGTH_SHORT).show();
				submitphoneflag=false;
			}
		}else{
			submitphoneflag=false;
		}
        if(emailvalue!=null&&!"".equals(emailvalue)){
    		pattern= Pattern.compile("^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)$");
    		matcher= pattern.matcher(emailvalue);
    		if (!matcher.matches()) {
    			Toast.makeText(getActivity(), getString(R.string.nomailaddress), Toast.LENGTH_SHORT).show();
    			submitmailflag=false;
    		}
        }else{
        	submitmailflag=false;
        }
		if(submitphoneflag==false&&submitmailflag==false){
			Toast.makeText(getActivity(), getString(R.string.settings_help_phone_mail), Toast.LENGTH_SHORT).show();
			return;
		}
		mView.setClickable(false);
		int conId = AppApplication.conId;

		CHYHttpClientUsage.getInstanse().doSendFeedbackV2(conId,phonenumber, emailvalue,contentvalue,new JsonHttpResponseHandler(Constants.ENCODING_GBK){
			@Override
			public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				String str = response.toString();

				int state= parseState(str);
				if(state==1){
					mhandler.sendEmptyMessage(MSG_SUCCESS);
				}else{
					mhandler.sendEmptyMessage(MSG_SUBMIT_ERROR);
				}
			}
		});
	}
	@Override
	protected void onReceiveMsg(Message msg) {
		// TODO Auto-generated method stub
		// dismissDialog();
		switch (msg.what) {
		case MSG_SUCCESS:
			System.out.println("----success success----");
			mConnectTime = 0;
            Toast.makeText(getActivity(),R.string.incongress_send_success, 
            		Toast.LENGTH_SHORT).show();
            performback();
			break;
		case MSG_ERROR:
			System.out.println("-----error error error error---::");
			mConnectTime++;
			if (mConnectTime == 5) {
				Toast.makeText(getActivity(), R.string.incongress_connect_fail, 
						Toast.LENGTH_SHORT).show();
				mConnectTime=0;
				mView.setClickable(true);
			}else{
				sendfeedback();
			}
			break;
		case MSG_SUBMIT_ERROR:
			mConnectTime = 0;
            Toast.makeText(getActivity(),R.string.incongress_send_fail, 
            		Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
//		MobclickAgent.onPageEnd(UMengPage.Page_Setting_Helper); //统计页面
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		MobclickAgent.onPageStart(UMengPage.Page_Setting_Helper); //统计页面
	}


	//解析状态
	public static int parseState(String str) {
		int state = 0;
		try {
			JSONObject stateobj = new JSONObject(str);
			state = stateobj.getInt("state");
		} catch (Exception e) {

		}
		return state;
	}
}
