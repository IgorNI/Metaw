package com.mbientlab.metawear.app;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by nilif on 2016/5/12.
 */
public class SqliteHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String SWORD="SWORD";
    public static final String DB_NAME = "test.db";

    public SqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public SqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public SqliteHelper(Context context,String name) {
        this(context,name,VERSION);
    }
    public SqliteHelper(Context context){
        super(context,DB_NAME,null,VERSION);
    }

    public SqliteHelper(Context context, String name, int version) {
        this(context,name,null,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        db.openOrCreateDatabase(DB_NAME,null);
        Log.d(SWORD,"创建一个sqlit数据库");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //创建成功，日志输出
        Log.d(SWORD, "onUpgrade a database");
    }

}
