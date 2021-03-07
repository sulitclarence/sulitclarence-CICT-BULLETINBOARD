package com.example.group2_sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    //Database version
    private static final int DATABASE_VERSION = 1;
    //Database Name
    private static final String DATABASE_NAME = "student_database";
    //Database Table name
    private static final String TABLE_NAME = "STUDENT";
    //Table columns
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    private SQLiteDatabase sqLiteDatabase;


    //creating table query
    private static final String CREATE_TABLE = "create table " + TABLE_NAME +"("+ID+
            " INTEGER PRIMARY KEY AUTOINCREMENT," + NAME + " TEXT NOT NULL,"+EMAIL+" TEXT NOT NULL);";
    //Constructor
    public DataBaseHelper (Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //Add Student Data
    public void addStudent(StudentClass studentClass){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBaseHelper.NAME, studentClass.getName());
        contentValues.put(DataBaseHelper.EMAIL, studentClass.getEmail());
        sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.insert(DataBaseHelper.TABLE_NAME, null,contentValues);
    }

    public List<StudentClass> getStudentList(){
        String sql = "select * from " + TABLE_NAME;
        sqLiteDatabase = this.getReadableDatabase();
        List<StudentClass> storeStudent = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery(sql,null);
        if (cursor.moveToFirst()){
            do {
                int id = Integer.parseInt(cursor.getString(0));
                String name = cursor.getString(1);
                String email = cursor.getString(2);
                storeStudent.add(new StudentClass(id,name,email));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return storeStudent;
    }

    public void updateStudent(StudentClass studentClass){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBaseHelper.NAME,studentClass.getName());
        contentValues.put(DataBaseHelper.EMAIL,studentClass.getEmail());
        sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.update(TABLE_NAME,contentValues,ID + " = ?" , new String[]
                {String.valueOf(studentClass.getId())});
    }

    public void deleteStudent(int id){
        sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_NAME, ID + " = ? ", new String[]
                {String.valueOf(id)});
    }

}


