package com.techninjas.umeedforwomen.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.techninjas.umeedforwomen.Models.Progress;
import com.techninjas.umeedforwomen.Models.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskDBUtil extends SQLiteOpenHelper {
    public static final String DB_NAME = "UMEED_WM_DB";
    public static final String TABLE_NAME = "TASKS";
    public static final String TASK_ID = "ID";
    public static final String TASK_NAME = "NAME";
    public static final String TASK_QTY = "QTY";
    public static final String TASK_DONE = "DONE";

    public TaskDBUtil(Context context){
        super(context, DB_NAME, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + " (" + TASK_ID + " TEXT PRIMARY KEY, " + TASK_NAME + " TEXT," + TASK_QTY + " INTEGER, " + TASK_DONE + " INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean insertData(List<Task> tasks){
        SQLiteDatabase db = this.getWritableDatabase();
        for(Task task: tasks){
            ContentValues contentValues = new ContentValues();
            contentValues.put(TASK_ID, task.getId());
            contentValues.put(TASK_NAME, task.getTask_name());
            contentValues.put(TASK_QTY, task.getQty());
            contentValues.put(TASK_DONE, 0);
            long res = db.insert(TABLE_NAME, null, contentValues);
            if(res == -1) return false;
        }
        return true;
    }

    public List<Task> readData(){
        SQLiteDatabase db = this.getReadableDatabase();
        List<Task> tasks = new ArrayList<Task>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            Task task = new Task(cursor.getString(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3));
            tasks.add(task);
            cursor.moveToNext();
        }
        cursor.close();
        return tasks;
    }

    public void update(String id, int done){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TASK_DONE, done);
        db.update(TABLE_NAME, contentValues, "ID = ?", new String[] {id});
    }

    public void deleteData(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "ID = ?", new String[] {id});
    }

    public void deleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE from " + TABLE_NAME + ";");
    }
}
