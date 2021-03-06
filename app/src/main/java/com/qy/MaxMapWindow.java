package com.qy;

import android.app.Application;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.lang.reflect.Field;
import java.util.List;

public class MaxMapWindow extends AndroidViewModel implements View.OnTouchListener {

    public Context mContext;
    private WindowManager.LayoutParams mWindowParams;
    private WindowManager mWindowManager;
    public View maxMapView;
    private static LinearLayout heroTx;
//    private ProgressBar hero1Hp, hero2Hp, hero3Hp, hero4Hp, hero5Hp;
    private static ImageView hero1View, hero2View, hero3View, hero4View, hero5View;
    private List<Hero> hl;
    private static int X = 0, Y = 0;
    public static LinearLayout[] llArr = new LinearLayout[5];
    public static ProgressBar[] hpArr = new ProgressBar[5];

    private MutableLiveData<String> data;

    public MaxMapWindow(@NonNull Application application) {
        super(application);
        initFloatWindow();
    }
    public MaxMapWindow(@NonNull Application application,Context context) {
        super(application);
        this.mContext = context;
        initFloatWindow();
    }

    //使用字符串创建LiveData
    private MutableLiveData<String> mCurrentName;
    public MutableLiveData<String> getCurrentName() {
        if (mCurrentName == null) {
            mCurrentName = new MutableLiveData<String>();
        }
        return mCurrentName;
    }


    private void initFloatWindow() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (inflater == null)
            return;
        maxMapView = (View) inflater.inflate(R.layout.max_map, null);
        maxMapView.setOnTouchListener(this);

        mWindowParams = new WindowManager.LayoutParams();
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        if (Build.VERSION.SDK_INT >= 26) {//8.0新特性
            mWindowParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            mWindowParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        mWindowParams.format = PixelFormat.RGBA_8888;
        mWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        mWindowParams.gravity = Gravity.START | Gravity.TOP;
        mWindowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        heroTx = maxMapView.findViewById(R.id.hero_tx);
        llArr[0] = maxMapView.findViewById(R.id.linear_layout_hero1);
        llArr[1] = maxMapView.findViewById(R.id.linear_layout_hero2);
        llArr[2] = maxMapView.findViewById(R.id.linear_layout_hero3);
        llArr[3] = maxMapView.findViewById(R.id.linear_layout_hero4);
        llArr[4] = maxMapView.findViewById(R.id.linear_layout_hero5);
        hero1View = maxMapView.findViewById(R.id.hero1);
        hero2View = maxMapView.findViewById(R.id.hero2);
        hero3View = maxMapView.findViewById(R.id.hero3);
        hero4View = maxMapView.findViewById(R.id.hero4);
        hero5View = maxMapView.findViewById(R.id.hero5);
//        hpArr[0] = maxMapView.findViewById(R.id.hero_hp1);
//        hpArr[1] = maxMapView.findViewById(R.id.hero_hp2);
//        hpArr[2] = maxMapView.findViewById(R.id.hero_hp3);
//        hpArr[3] = maxMapView.findViewById(R.id.hero_hp4);
//        hpArr[4] = maxMapView.findViewById(R.id.hero_hp5);
//        for (int i = 0; i < hpArr.length; i++) {
//            hpArr[i].setVisibility(View.GONE);
//        }
    }

    public static void updateHeroView(boolean isShow) {
        if (isShow) {
            heroTx.setVisibility(View.VISIBLE);
        } else {
            heroTx.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {

        return false;
    }


    public void showFloatWindow() {
        if (null != maxMapView && maxMapView.getParent() == null) {
            DisplayMetrics metrics = new DisplayMetrics();
            mWindowManager.getDefaultDisplay().getMetrics(metrics);
            mWindowParams.width = metrics.widthPixels;
            mWindowParams.height = metrics.heightPixels;
            mWindowManager.addView(maxMapView, mWindowParams);
        }
    }

    public void setMap(ThisData td) {
        try {
            if (null != td && null != td.getHl()) {
                hl = td.getHl();
                for (int i = 0; i < hl.size(); i++) {
                    setMap(hpArr[i], llArr[i], hl.get(i));
                }
            }
        } catch (Exception e) {
            System.out.println("MaxMap117:程序异常");
        }
    }

    public static void setMap(ProgressBar hp, LinearLayout linear_layout_hero_max, Hero hero) {
//        hp.setSecondaryProgress((int) Math.floor(hero.getHpPercentage()));
        if (0 < hero.getHpPercentage()) {
            linear_layout_hero_max.setVisibility(View.VISIBLE);
            String[] xyArr = hero.getEntityXY().split(",");
            setLayoutXY(linear_layout_hero_max, Double.valueOf(xyArr[1]).intValue(), Double.valueOf(xyArr[0]).intValue());
        } else {
            linear_layout_hero_max.setVisibility(View.GONE);
        }
    }

    public static void setLayoutXY(LinearLayout view, int x, int y) {
        view.setX(y - 50 + X);
        view.setY(x - 210 + Y);
    }

    public static void setLayoutSrc(ImageView view, int heroId) {
        try {
            Field field = (Field) R.mipmap.class.getDeclaredField("_" + heroId);
            view.setImageResource(field.getInt(R.mipmap.class));
        } catch (Exception ex) {

        }
    }

    public View getMaxMapView() {
        return maxMapView;
    }

    public void setX(int x) {
        X = x;
    }

    public void setY(int y) {
        Y = y;
    }

    public MutableLiveData<String> getData() {
        return data;
    }
}