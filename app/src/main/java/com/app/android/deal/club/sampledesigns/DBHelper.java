package com.app.android.deal.club.sampledesigns;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME="healthpredict";
    public static final String TABLE_NAME="healthpredict";

    public static final String ID = "_id";
    public static final String TITLE = "title";
    public static final String TIME_DATE = "time_date";


    public static final String Doc_MailId= "dmail";
    public static final String Patient_MailId="pmail";
    public static final String PATIENT_TABLE="patient_tble";


    SQLiteDatabase SqlDb;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null,DATABASE_VERSION );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE="create table "+TABLE_NAME+"("+ID+" integer primary key AUTOINCREMENT, "
                +TITLE+" text, "
                +TIME_DATE+" text );";

        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

    }


    public void sampleData()
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(TITLE, "Ratchasan: ஹாலிவுட்டில் ரீமேக் ஆக இருக்கும் ராட்சசன்?");
        values.put(TIME_DATE, "2018-October-16 02:07:41 am");
        db.insert(TABLE_NAME, null, values);

        values.put(TITLE, "Tamil Flash News: இன்றைய முக்கிய செய்திகள் 15-10-2018");
        values.put(TIME_DATE, "2018-October-16 02:07:41 am");
        db.insert(TABLE_NAME, null, values);

        values.put(TITLE, "நீள  மழைச் சாரல் " +
                "தென்றல் நெசவு நடத்தும் இடம். " +
                "நீள மழைச் சாரல்");
        values.put(TIME_DATE, "2018-October-16 02:07:41 am");
        db.insert(TABLE_NAME, null, values);


        values.put(TITLE, "பூங்காற்றே நீ வீசாதே ஒஹோஓஒ " +
                "பூங்காற்றே நீ வீசாதே நான் தான் இங்கே விசிறி");
        values.put(TIME_DATE, "2018-October-16 02:07:41 am");
        db.insert(TABLE_NAME, null, values);

        values.put(TITLE, "ஸ்டெர்லைட் ஆய்வுக்கு அவகாசம் நீட்டிப்பு: பசுமைத் தீர்ப்பாயம்");
        values.put(TIME_DATE, "2018-October-16 02:07:41 am");
        db.insert(TABLE_NAME, null, values);

        values.put(TITLE, "Ratchasan: ஹாலிவுட்டில் ரீமேக் ஆக இருக்கும் ராட்சசன்?");
        values.put(TIME_DATE, "2018-October-16 02:07:41 am");
        db.insert(TABLE_NAME, null, values);
    }

    public int setNewsDetails(String title, String time_date)
    {
        int result = 1;
        try {
            SqlDb = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(TITLE, title);
            cv.put(TIME_DATE, time_date);
            SqlDb.insertOrThrow(TABLE_NAME, null, cv);
            SqlDb.close();
        }catch (Exception e)
        {
            result=0;
            Log.e("Error ","SqlException :::"+ String.valueOf(e.getMessage()));
            e.printStackTrace();
        }
        return result;
    }




    public Cursor getNewsDetails()
    {
        SqlDb=this.getReadableDatabase();
        List<DataModel> data_filter=new ArrayList<DataModel>();
        String query="select * from "+TABLE_NAME+";";
        Cursor cur=SqlDb.rawQuery(query,null);
        if(cur!=null)
        {
            cur.moveToFirst();
        }
        return cur;
    }

}
