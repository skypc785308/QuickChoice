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

    public MyDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE  TABLE bank.card " +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "bank_name VARCHAR(20), " +
                "card_name VARCHAR(20))");
        db.execSQL("CREATE  TABLE card.offer " +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "card_name VARCHAR(20),"+
                "offer_type VARCHAR(20),"+
                "key_value VARCHAR(20), " +
                "coper_store VARCHAR(20), " +
                "offer_context VARCHAR(100))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
