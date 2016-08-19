package com.mbientlab.metawear.app;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by nilif on 2016/5/17.
 */
public class PersonDao {

    private static final String TAG = "the database";
    private SqliteHelper sqliteHelper;
    private static float data1 ;
    private static float data2 ;
    private static float data3 ;
    private static float data4 ;
    private static int stepNum ;
    private static int gesture ;
    private static String date ;


    public PersonDao(Context context){
        sqliteHelper = new SqliteHelper(context);
    }
    public SqliteHelper getSqliteHelper(){
        return sqliteHelper;
    }

    public void addStep(int data){
        SQLiteDatabase db = sqliteHelper.getWritableDatabase();
        db.execSQL("insert into user (stepnum) values (?)",new Object[]{data});
        Log.d(TAG, "addStep is success");
        db.close();
    }
    // 向数据库中插入多条数据
    public void addInfor(float data1,float data2,float data3,float data4,int step,int gesture,String date){
        SQLiteDatabase db = sqliteHelper.getWritableDatabase();
        db.execSQL("insert into person (data1,data2,data3,data4,stepnum,gestrue,time) values(?,?,?,?,?,?,?)",new Object[]{data1,data2,data3,data4,step,gesture,date});
        Log.w(TAG, "insert is success");

        db.close();
    }
    public void deleteTable(){
        SQLiteDatabase db = sqliteHelper.getReadableDatabase();
        db.execSQL("drop table person");
        Log.w(TAG, "deleteTable: ");
        db.close();
    }

    public void deleteDatabase(){
        SQLiteDatabase db = sqliteHelper.getWritableDatabase();
        db.execSQL("drop ");
    }

    // 从数据库获取信息，可以写在上传服务器那里的代码。
    public Cursor getInfor() {
        SQLiteDatabase db = sqliteHelper.getReadableDatabase();
        Cursor result = db.rawQuery("select* from person", null);
        Log.w(TAG, "getInfor: " + result.getCount());
       // result.moveToFirst();
        for (result.moveToFirst();!result.isAfterLast();result.moveToNext()){
            data1 = result.getFloat(result.getColumnIndex("data1"));
            data2 = result.getFloat(result.getColumnIndex("data2"));
            data3 = result.getFloat(result.getColumnIndex("data3"));
            data4 = result.getFloat(result.getColumnIndex("data4"));
            stepNum = result.getInt(result.getColumnIndex("stepnum"));
            gesture = result.getInt(result.getColumnIndex("gestrue"));
            date = result.getString(result.getColumnIndex("time"));
//            return (Cursor) new Person(data1,data2,data3,data4,stepNum,gesture,date);
            Log.w(TAG, "data1: " + data1 + "data2: " + data2 + "data3: " + data3 + "data4: " + data4 + "stepNum: " + stepNum + "gesture: " + gesture + "date: " + date);
        }
       // Log.w(TAG, "data1: "+data1+"data2: "+data2+"data3: "+data3+"data4: "+data4+"stepNum: "+stepNum+"gesture: "+gesture+"date: "+date);
        return result;
    }

    public void creatTable(){
        SQLiteDatabase db = sqliteHelper.getReadableDatabase();
        db.execSQL("create table person(data1 float,data2 float,data3 float,data4 float,stepnum int,gestrue int,time text)");
        Log.e(TAG, "creatTable:" );
    }

    // 获取方法

}
