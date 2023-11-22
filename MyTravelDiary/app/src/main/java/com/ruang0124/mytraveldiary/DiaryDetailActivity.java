package com.ruang0124.mytraveldiary;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.radiobutton.MaterialRadioButton;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DiaryDetailActivity extends AppCompatActivity implements View.OnClickListener
{
    // 상세보기화면이거나 수정하기화면으로 mode에 따라 달라지도록 이곳에서 동시에 처리

    private RadioGroup radioGroup;
    private VideoView videoView;                // 비디오 보이는 부분
    private ImageView imageView;                // 초기에 이미지 보이도록

    private TextView userDate;                  // 사용자가 달력을 이용해서 설정한 날짜 , 보이는 부분
    private EditText editTitle, editContent;    // 제목, 내용
    private String createDate = "";             // 게시글 작성날짜
    private String selectUserDate = "";         // 사용자가 선택한 날짜
    private String selectMode = "";             // 수정, 삭제 어떤거 할지
    private int selectEmotion = -1;             // 선택한 이모지 번호

    private DBHelper dbHelper;                  // 데이터베이스


    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_detail);

        // db 객체 생성
        dbHelper = new DBHelper(this);

        // 동영상 관련
        radioGroup = findViewById(R.id.radio_emotions);
        videoView = findViewById(R.id.videoView);
        imageView = findViewById(R.id.imageView);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                int selectedVideoId = getSelectedVideoId(checkedId);
                if (selectedVideoId != 0)
                {
                    playVideo(selectedVideoId);
                }
            }
        });

        // 동영상 제외 나머지
        userDate = findViewById(R.id.dmli_show_date);           // 작성 날짜 ( 선택한거 )
        editTitle = findViewById(R.id.write_title);             // 제목
        editContent = findViewById(R.id.dmli_content);          // 내용
        radioGroup = findViewById(R.id.radio_emotions);         // 라디오 그룹

        ImageView leftArrow = findViewById(R.id.leftArrow);     // 뒤로가기
        ImageView check = findViewById(R.id.check);             // 작성완료

        leftArrow.setOnClickListener(this);                     // 클릭 기능 활성화
        check.setOnClickListener(this);                         // 클릭 기능 활성화
        userDate.setOnClickListener(this);                      // 클릭 기능 활성화

        // 안드로이드 시스템 시간 가져오기
        selectUserDate = new SimpleDateFormat("yyyy/MM/dd E요일", Locale.KOREAN).format(new Date());
        userDate.setText(selectUserDate);

        // 수정일경우 값 가져오기
        Intent intent = getIntent();
        if (intent.getExtras() != null)             // 만약 데이터가 있을경우 실행
        {
            DiaryModel diaryModel = (DiaryModel) intent.getSerializableExtra("diaryModel");
            selectMode = intent.getStringExtra("mode");
            createDate = diaryModel.getWriteDate();             // 게시글 수정하기 위해서 필요한부분

            // 수정하려면 작성된 내용이 화면에 보여야 하므로 제목, 내용 등등을 작성된 내용에 맞게 설정해준다
            editTitle.setText(diaryModel.getTitle());
            editContent.setText(diaryModel.getContent());
            int selectEmotion = diaryModel.getEmotion();
            ((MaterialRadioButton) radioGroup.getChildAt(selectEmotion)).setChecked(true);
            selectUserDate = diaryModel.getUserDate();
            userDate.setText(diaryModel.getUserDate());

            if (selectMode.equals("modify"))
            {
                TextView header_title = findViewById(R.id.header_title);
                header_title.setText("일기 수정하기");
            }
            else if (selectMode.equals("detail"))
            {
                TextView header_title = findViewById(R.id.header_title);
                header_title.setText("나의 일기장");

                // 읽기만 가능하도록 수정기능을 off
                check.setVisibility(View.INVISIBLE);
                editTitle.setEnabled(false);
                editContent.setEnabled(false);
                userDate.setEnabled(false);
                for (int i=0; i<radioGroup.getChildCount(); i++)
                {
                    radioGroup.getChildAt(i).setEnabled(false);
                }
            }
        }
    }

    // 선택된 이모지에 따라 동영상 재생
    private int getSelectedVideoId(int checkedId)
    {
        if (checkedId == R.id.radio_happy)
        {
            return R.raw.happy;
        }
        else if (checkedId == R.id.radio_laugh)
        {
            return R.raw.laugh;
        }
        else if (checkedId == R.id.radio_bored)
        {
            return R.raw.bored;
        }
        else if (checkedId == R.id.radio_sad)
        {
            return R.raw.sad;
        }
        else if (checkedId == R.id.radio_angry)
        {
            return R.raw.angry;
        }
        else if (checkedId == R.id.imageView)
        {
            return R.id.imageView;
        }
        else
        {
            return 0;
        }
    }


    // 초기 이미지에서 동영상으로
    private void playVideo(int videoId)
    {
        // 이미지뷰 숨기기
        imageView.setVisibility(View.GONE);
        // 비디오뷰 보이기
        videoView.setVisibility(View.VISIBLE);

        String videoPath = "android.resource://" + getPackageName() + "/" + videoId;
        videoView.setVideoPath(videoPath);
        videoView.start();
    }


    // 클릭하면 수행할것들
    @Override
    public void onClick(View v) {
        int viewId = v.getId();

        if (viewId == R.id.leftArrow)
        {
            // leftArrow를 클릭한 경우
            finish();                           // 현재 액티비티 종료
        }
        else if (viewId == R.id.check)
        {
            // check를 클릭한 경우
            selectEmotion = radioGroup.indexOfChild(findViewById(radioGroup.getCheckedRadioButtonId()));

            // 제목, 내용 비어있는지 확인
            if (editTitle.getText().length() == 0 || editContent.getText().length() == 0)
            {
                Toast.makeText(this, "제목과 내용 모두 입력해야 합니다!!", Toast.LENGTH_SHORT).show();
                return;         // 아래 과정 실행하지 않고 여기서 종료
            }

            // 이모지 선택란 확인
            if (selectEmotion == -1)
            {
                Toast.makeText(this, "오늘의 상태는 어떤가요?", Toast.LENGTH_SHORT).show();
                return;         // 아래 과정 실행하지 않고 여기서 종료
            }

            // 무사히 통과하면 입력된 값 저장하기
            String title = editTitle.getText().toString();
            String content = editContent.getText().toString();
            String UserDate = selectUserDate;
            if (UserDate.equals(""))
            {
                UserDate = userDate.getText().toString();
            }

            // DB에 저장해야해서 시간 더 자세하게
            String writeDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.KOREAN).format(new Date());

            // DB에 저장하기
            if (selectMode.equals("modify"))
            {
                dbHelper.updateDiaryList(title, content, selectEmotion, UserDate, writeDate, createDate);
                Toast.makeText(this, "수정이 완료되었습니다", Toast.LENGTH_SHORT).show();
            }
            else
            {
                dbHelper.insertDiary(title, content, selectEmotion, UserDate, writeDate);
                Toast.makeText(this, "저장완료!!", Toast.LENGTH_SHORT).show();
            }
            finish();
        }
        else if (viewId == R.id.dmli_show_date)
        {
            // dmli_user_date를 클릭한 경우
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener()
            {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day)
                {
                    // 달력에 선택 된 년, 월, 일 을 가지고 와서 다시 캘린더 함수에 넣어줘서 사용자가 선택한 요일을 알아낸다
                    Calendar innerCal = Calendar.getInstance();
                    innerCal.set(Calendar.YEAR, year);
                    innerCal.set(Calendar.MONTH, month);
                    innerCal.set(Calendar.DAY_OF_MONTH, day);

                    selectUserDate = new SimpleDateFormat("yyyy/MM/dd E요일", Locale.KOREAN).format(innerCal.getTime());
                    userDate.setText(selectUserDate);
                }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));

            dialog.show();          // 화면에 보이도록

        }
    }

}
