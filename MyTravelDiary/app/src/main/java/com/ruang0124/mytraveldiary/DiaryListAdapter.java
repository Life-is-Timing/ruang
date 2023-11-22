package com.ruang0124.mytraveldiary;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DiaryListAdapter extends RecyclerView.Adapter<DiaryListAdapter.ViewHolder>
{
    ArrayList<DiaryModel> DiaryListArray;           // 다이어리 데이터들을 들고 있는 자료형 배열
    Context DiaryListContext;
    DBHelper dbHelper;                              // db연결해서 수정, 삭제하기 위함

    @NonNull
    @Override
    public DiaryListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        // 생성시 호출
        DiaryListContext = parent.getContext();
        dbHelper = new DBHelper(DiaryListContext);                  // context내용 사용가능 하도록
        View holder = LayoutInflater.from(DiaryListContext).inflate(R.layout.diary_main_list_item, parent, false);
        return new ViewHolder(holder);
    }

    @Override
    public void onBindViewHolder(@NonNull DiaryListAdapter.ViewHolder holder, int position)
    {
        // 생성후 연동


        // 이미지 번호  ( 0:happy, 1:laugh, 2:bored, 3:sad, 4:angry )
        int emotion = DiaryListArray.get(position).getEmotion();
        switch (emotion)
        {
            case 0:
                holder.dmli_image.setImageResource(R.drawable.happy);
                break;
            case 1:
                holder.dmli_image.setImageResource(R.drawable.laugh);
                break;
            case 2:
                holder.dmli_image.setImageResource(R.drawable.bored);
                break;
            case 3:
                holder.dmli_image.setImageResource(R.drawable.sad);
                break;
            case 4:
                holder.dmli_image.setImageResource(R.drawable.angry);
                break;
        }

        // 제목, 날짜
        String title = DiaryListArray.get(position).getTitle();
        String userDate = DiaryListArray.get(position).getUserDate();

        holder.dmli_title.setText(title);
        holder.dmli_user_date.setText(userDate);
    }

    @Override
    public int getItemCount()
    {
        // 총 개수
        return DiaryListArray.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView dmli_image;
        TextView dmli_title, dmli_user_date;

        // 1개의 아이템 뷰
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            dmli_image = itemView.findViewById(R.id.dmli_image);            // 기분 이모티콘
            dmli_title = itemView.findViewById(R.id.dmli_title);            // 일기 제목
            dmli_user_date = itemView.findViewById(R.id.dmli_user_date);    // 사용자 지정 날짜

            // 클릭 ( 상세보기 )
            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view) {
                    int currentState = getAdapterPosition();                    // 클릭한 게시글 번호 가져옴

                    DiaryModel diaryModel = DiaryListArray.get(currentState);   // 클릭한 아이템 정보를 가지는 변수

                    Intent diaryDetailIntent = new Intent(DiaryListContext, DiaryDetailActivity.class);     // 정보가지고 화면이동
                    diaryDetailIntent.putExtra("diaryModel", diaryModel);                             // implements 해줘야 에러 없어짐
                    diaryDetailIntent.putExtra("mode", "detail");                               // 읽기
                    DiaryListContext.startActivity(diaryDetailIntent);
                }
            });

            // 길게 클릭 ( 수정, 삭제 )
            itemView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View view)
                {
                    int currentState = getAdapterPosition();            // 선택된 게시글 정보

                    DiaryModel diaryModel = DiaryListArray.get(currentState);   // 클릭한 아이템 정보를 가지는 변수

                    String[] strChoiceArray = {"수정", "삭제"};             // 수정할지 삭제할지 선택하도록

                    new AlertDialog.Builder(DiaryListContext).setTitle("수정 또는 삭제").setItems(strChoiceArray, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int position) {
                            if (position == 0)
                            {
                                // 수정하기
                                Intent diaryDetailIntent = new Intent(DiaryListContext, DiaryDetailActivity.class);     // 정보가지고 화면이동
                                diaryDetailIntent.putExtra("diaryModel", diaryModel);
                                diaryDetailIntent.putExtra("mode", "modify"); // 수정
                                DiaryListContext.startActivity(diaryDetailIntent);
                            }
                            else
                            {
                                // 삭제하기
                                dbHelper.deleteDiaryList(diaryModel.getWriteDate());        // 작성 날짜 일치하면 삭제

                                DiaryListArray.remove(currentState);                        // 화면에서 안보이도록 설정
                                notifyItemRemoved(currentState);
                            }
                        }
                    }).show();

                    return false;
                }
            });
        }
    }


    public void initList(ArrayList<DiaryModel> lstDiary)
    {
        // 리스트 업데이트
        DiaryListArray = lstDiary;
        notifyDataSetChanged();
    }


}
