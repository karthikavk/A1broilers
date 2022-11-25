package in.kassapos.a1broilers.db;

import android.content.Context;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;

import in.kassapos.beltshop.db.PalaniDataBase;

/**
 * Created by KASSAPOS8 on 9/18/2015.
 */
public class MyDbHelper extends PalaniDataBase {
    public MyDbHelper(Context context) throws PackageManager.NameNotFoundException {
        super(context);
    }
    public MyDbHelper(Context context,String dbname) throws PackageManager.NameNotFoundException {
        super(context,dbname);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table SSENotificationData (id integer primary key autoincrement,key text,data text)");
        db.execSQL("create table ReceivedData (id integer primary key autoincrement,data text,created_date datetime)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

}
