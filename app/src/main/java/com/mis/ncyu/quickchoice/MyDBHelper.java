package com.mis.ncyu.quickchoice;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by UserMe on 2017/8/13.
 */

public class MyDBHelper extends SQLiteOpenHelper {
    private static MyDBHelper instance = null;

    public static MyDBHelper getInstance(Context ctx){
        if (instance==null){
            instance = new MyDBHelper(ctx, "bank_offer.db", null, 1);
        }
        return instance;
    }

    private MyDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE  TABLE total_change " +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "bank VARCHAR(20), " +
                "discont_limit VARCHAR(20)," +
                "point_limit VARCHAR(20)," +
                "change_percent VARCHAR(20))");
        db.execSQL("CREATE  TABLE login " +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id VARCHAR(20), " +
                "login_date DATETIME, "+
                "cash INTEGER,"+
                "oil INTEGER,"+
                "red INTEGER,"+
                "plane INTEGER," +
                "movie INTEGER)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        final String DROP_TABLE = "DROP TABLE IF EXISTS total_change";
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }
}
