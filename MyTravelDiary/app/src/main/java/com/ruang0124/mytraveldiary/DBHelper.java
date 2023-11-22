package com.ruang0124.mytraveldiary;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper
{
    // 안드로이드 내부 저장소
    private static final String DB_NAME = "MyFirstTravelDiary.db";
    private static final int DB_VERSION = 1;

    // 생성자
    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        // database create
        db.execSQL("CREATE TABLE IF NOT EXISTS DiaryMain (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT NOT NULL, " +
                "content TEXT NOT NULL, " +
                "imageNumber INTEGER NOT NULL, " +
                "userDate TEXT NOT NULL, " +
                "writeDate TEXT NOT NULL)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        onCreate(db);
    }


    // 순서대로 CRUD 라고 한다
    // INSERT ( 데이터 저장 )
    public void insertDiary(String _title, String _content, int _imageNumber, String _userDate, String  _writeDate)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO DiaryMain (title, content, imageNumber, userDate, writeDate) " +
                "VALUES ('" + _title + "','" +  _content + "','" +  _imageNumber + "','" +  _userDate + "','" +  _writeDate + "')");
    }


    // READ ( 데이터 조회 )
    public ArrayList<DiaryModel> getDiaryList()
    {
        ArrayList<DiaryModel> readDiary = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM DiaryMain ORDER BY userDate DESC", null);
        if (cursor.getColumnCount() != 0)
        {
            // 정보를 1개 이상 가져온다면
            while (cursor.moveToNext())
            {
                // 다음 정보가 있을때 까지 계속 반복해서 가져옴
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String content = cursor.getString(cursor.getColumnIndexOrThrow("content"));
                int imageNumber = cursor.getInt(cursor.getColumnIndexOrThrow("imageNumber"));
                String userDate = cursor.getString(cursor.getColumnIndexOrThrow("userDate"));
                String writeDate = cursor.getString(cursor.getColumnIndexOrThrow("writeDate"));

                // 클래스 생성
                DiaryModel diaryModel = new DiaryModel();
                diaryModel.setId(id);
                diaryModel.setTitle(title);
                diaryModel.setContent(content);
                diaryModel.setEmotion(imageNumber);
                diaryModel.setUserDate(userDate);
                diaryModel.setWriteDate(writeDate);

                readDiary.add(diaryModel);
            }
        }
        cursor.close();

        return readDiary;
    }


    // UPDATE ( 데이터 수정 )
    public void updateDiaryList(String _title, String _content, int _imageNumber, String _userDate, String _writeDate, String _createDate)
    {
        // beforeDate를 키값으로 이용해서 이전에 작성된 데이터를 찾을 수 있음
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE DiaryMain " +
                "SET title = '" + _title + "', " +
                "content = '" + _content + "', " +
                "imageNumber = '" + _imageNumber + "', " +
                "userDate = '" + _userDate + "', " +
                "writeDate = '" + _writeDate + "' " +
                "WHERE writeDate = '" + _createDate + "' ");
    }


    // DELETE ( 데이터 삭제 )
    public void deleteDiaryList(String _writeDate)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM DiaryMain WHERE writeDate = '" + _writeDate + "' ");
    }


}










