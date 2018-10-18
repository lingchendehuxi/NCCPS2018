package com.android.incongress.cd.conference.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.android.incongress.cd.conference.beans.CountryCodeBean;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 本地国家码数据库
 * Created by Jacky on 2016/2/28.
 */
public class CountryDb {
    private static SQLiteDatabase database;
    public static final String DATABASE_FILENAME = "country_code.db";
    public static final String PACKAGE_NAME = "com.mobile.incongress.cd.conference.basic.csd";
    public static final String DATABASE_PATH = "/data" + Environment.getDataDirectory().getAbsolutePath() + "/" + PACKAGE_NAME;

    public static SQLiteDatabase openDatabase(Context context) {
        try {
            String databaseFilename = DATABASE_PATH + "/" + DATABASE_FILENAME;
            File file = new File(databaseFilename);

            if (!file.exists()) {
                file.createNewFile();
                InputStream is = context.getResources().openRawResource(R.raw.country_code);
                FileOutputStream fos = new FileOutputStream(file);
                byte[] buffer = new byte[8192];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }

                fos.close();
                is.close();
            }
            database = SQLiteDatabase.openOrCreateDatabase(file, null);
            return database;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取国家码所有数据
     * @param context
     * @return
     */
    public static List<CountryCodeBean> getAllCountryCode(Context context) {
        List<CountryCodeBean> countrys = new ArrayList<>();
        SQLiteDatabase db = CountryDb.openDatabase(context);

        Cursor cursor = db.query("countrycode", null, null, null, null, null, null);
        CountryCodeBean bean = null;
        while(cursor.moveToNext()) {
            bean = new CountryCodeBean();
            bean.setCountry(cursor.getString(cursor.getColumnIndex("country")));
            bean.setCode(cursor.getString(cursor.getColumnIndex("code")));
            countrys.add(bean);
        }
        return countrys;
    }
}
