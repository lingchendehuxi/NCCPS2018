package com.android.incongress.cd.conference.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mobile.incongress.cd.conference.basic.csccm.R;


public class BladeView extends View {
    private OnItemClickListener mOnItemClickListener;
    String[] b = {"↑", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
            "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X",
            "Y", "Z", "#"};
    int choose = -1;
    Paint paint = new Paint();
    private PopupWindow mPopupWindow;
    private TextView mPopupText;
    private Handler handler = new Handler();

    public BladeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public BladeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public BladeView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int height = getHeight();
        int width = getWidth();
        int singleHeight = height / b.length;
        for (int i = 0; i < b.length; i++) {
            paint.setColor(getResources().getColor(R.color.theme_color));
            paint.setTextSize(getResources().getDimensionPixelSize(R.dimen.bladeview_fontsize));//设置字体的大小
            paint.setFakeBoldText(true);
            paint.setAntiAlias(true);
            if (i == choose) {
                paint.setColor(Color.parseColor("#3399ff"));
            }
            float xPos = width / 2 - paint.measureText(b[i]) / 2;
            float yPos = singleHeight * i + singleHeight;
            canvas.drawText(b[i], xPos, yPos, paint);
            paint.reset();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();
        final int oldChoose = choose;
        final int c = (int) (y / getHeight() * b.length);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (oldChoose != c) {
                    if (c >= 0 && c < b.length) {
                        performItemClicked(c);
                        choose = c;
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (oldChoose != c) {
                    if (c >= 0 && c < b.length) {
                        performItemClicked(c);
                        choose = c;
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                choose = -1;
                dismissPopup();
                invalidate();
                break;
        }
        return true;
    }

    private void showPopup(int item) {
        if (mPopupWindow == null) {
            handler.removeCallbacks(dismissRunnable);
            mPopupText = new TextView(getContext());
            mPopupText.setBackgroundColor(Color.TRANSPARENT);
            mPopupText.setTextColor(getResources().getColor(R.color.theme_color));
            mPopupText.setTextSize(getResources().getDimensionPixelSize(R.dimen.bladeview_popup_fontsize));
            mPopupText.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);

            int height = getResources().getDimensionPixelSize(R.dimen.bladeview_popup_height);
            mPopupWindow = new PopupWindow(mPopupText, height, height);
        }

        mPopupText.setText(b[item]);
        //android 7.0 以上会出现问题，布局会跳到顶部
//        if (mPopupWindow.isShowing()) {
//            mPopupWindow.update();
//        } else {
//            mPopupWindow.showAtLocation( getRootView(),
//                    Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0,0);
//        }
        mPopupWindow.showAtLocation(getRootView(),
                Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0,0);
    }

    private void dismissPopup() {
        handler.postDelayed(dismissRunnable, 800);
    }

    Runnable dismissRunnable = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            if (mPopupWindow != null) {
                mPopupWindow.dismiss();
            }
        }
    };

    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    private void performItemClicked(int item) {
        if (mOnItemClickListener != null) {
            LogUtils.println("performItemClick:" + item);
            mOnItemClickListener.onItemClick(b[item]);
            showPopup(item);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String s);
    }

}
