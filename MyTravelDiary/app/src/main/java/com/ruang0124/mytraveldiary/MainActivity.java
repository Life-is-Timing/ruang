package com.ruang0124.mytraveldiary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{

    RecyclerView diaryMainView;                 // 리스트 뷰
    ImageButton writeButton;                    // 작성 버튼
    DiaryListAdapter dlAdapter;                 // 리사이클러뷰와 연동할 어댑터
    ArrayList<DiaryModel> DiaryListArray;       // 리스트에 보여질 데이터들 ( 배열이다 )
    DBHelper dbHelper;                          // 데이터베이스

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);
        DiaryListArray = new ArrayList<>();

        diaryMainView = findViewById(R.id.diaryMainView);
        dlAdapter = new DiaryListAdapter();                 // 리사이클러 뷰 어댑터 인스턴스 생성

        diaryMainView.setAdapter(dlAdapter);                // 연동하기

        ImageButton writeButton = findViewById(R.id.writeButton);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.OVAL);
        drawable.setColor(Color.WHITE);
        writeButton.setBackground(drawable);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)      // 이미지 버튼 원형으로
        {
            writeButton.setClipToOutline(true);
        }

        writeButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // 작성하기 화면으로 이동
                Intent intent = new Intent(MainActivity.this, DiaryDetailActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        // 엑티비티 다시 실행해서 정보 업데이트
        setLoadRecentList();
    }

    private void setLoadRecentList()
    {
        // 최신정보로 갱신
        if (!DiaryListArray.isEmpty())
        {
            // 데이터가 존재한다면 초기화
            DiaryListArray.clear();
        }

        DiaryListArray = dbHelper.getDiaryList();
        dlAdapter.initList(DiaryListArray);
    }

}

















