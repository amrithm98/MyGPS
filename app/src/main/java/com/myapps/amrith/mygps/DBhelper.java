package com.myapps.amrith.mygps;
import android.app.ActionBar;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * Created by hp on 17-04-2016.
 */
public class DBhelper extends SQLiteOpenHelper {
    public static final String DBName="Location.db";
    public static final String TabName="prevlocs";
    public static final String c1="Time";
    public static final String c2="Address";
    public static final String c3="City";
    public static final String c4="State";
    public static final String c5="Country";
    public static final String c6="PostalCode";
    public static final String c7="SubLocality";

    public DBhelper(Context context) {
        super(context, DBName,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TabName + "(Time TEXT PRIMARY KEY,Address TEXT, City TEXT,State TEXT,Country TEXT,PostalCode TEXT,SubLocality TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TabName);
        onCreate(db);
    }
    public boolean insertData(String a,String b,String c,String d,String e,String f,String g){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(c1,a);
        contentValues.put(c2,b);
        contentValues.put(c3,c);
        contentValues.put(c4,d);
        contentValues.put(c5,e);
        contentValues.put(c6,f);
        contentValues.put(c7, g);
        long result=db.insert(TabName,null,contentValues);
        if (result==-1)
            return false;
        else
            return true;
        }
    public Cursor getdata(){

        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM "+TabName,null);
        return cursor;

    }
}






