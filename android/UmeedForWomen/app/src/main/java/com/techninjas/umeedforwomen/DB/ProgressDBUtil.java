package com.techninjas.umeedforwomen.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.techninjas.umeedforwomen.Models.Progress;
import com.techninjas.umeedforwomen.Models.Task;

import java.util.ArrayList;
import java.util.List;

public class ProgressDBUtil extends SQLiteOpenHelper {
    public static final String DB_NAME = "UMEED_APP_DB";
    public static final String TABLE_NAME = "PROGRESS";
    public static final String PROGRESS_ID = "ID";
    public static final String TASK_ID = "TASK_ID";
    public static final String PROGRESS_QTY = "QTY";
    public static final String PROGRESS_TIMESTAMP = "NAME";
    public static final String PROGRESS_FILEPATH = "FILEPATH";

    public ProgressDBUtil(Context context){
        super(context, DB_NAME, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + " (" + PROGRESS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +  TASK_ID + " TEXT," + PROGRESS_QTY + " INTEGER, " + PROGRESS_TIMESTAMP + " TEXT, " +
                PROGRESS_FILEPATH + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean insertData(Progress progress){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TASK_ID, progress.getTaskId());
        contentValues.put(PROGRESS_QTY, progress.getQty());
        contentValues.put(PROGRESS_TIMESTAMP, progress.getTimestamp());
        contentValues.put(PROGRESS_FILEPATH, progress.getImageLocation());
        long res = db.insert(TABLE_NAME, null, contentValues);
        if(res == -1) return false;
        return true;
    }

    public List<Progress> readData(){
        SQLiteDatabase db = this.getReadableDatabase();
        List<Progress> progresses = new ArrayList<Progress>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            //Log.d("APP_LOGS", cursor.getInt(0) + cursor.getString(1) + cursor.getInt(2));
            Progress progress = new Progress(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getString(3), cursor.getString(4));
            progresses.add(progress);
            cursor.moveToNext();
        }
        cursor.close();
        return progresses;
    }

    public Integer deleteData(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?", new String[] {String.valueOf(id)});
    }

    public void deleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE from " + TABLE_NAME + ";");
    }
}
