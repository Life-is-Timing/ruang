package com.ruang0124.mytraveldiary;

import java.io.Serializable;

public class DiaryModel implements Serializable
{
    int id;                 // 게시글 번호
    int emotion;            // 오늘의 기분
    String title;           // 제목
    String content;         // 내용
    String userDate;        //
    String writeDate;       // 작성 날짜


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEmotion() {
        return emotion;
    }

    public void setEmotion(int emotion) {
        this.emotion = emotion;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserDate() {
        return userDate;
    }

    public void setUserDate(String userDate) {
        this.userDate = userDate;
    }

    public String getWriteDate() {
        return writeDate;
    }

    public void setWriteDate(String writeDate) {
        this.writeDate = writeDate;
    }
}
