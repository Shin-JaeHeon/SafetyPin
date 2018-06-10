package kr.saintdev.safetypin.models.datas.subprofile;

/**
 * Copyright (c) 2015-2018 Saint software All rights reserved.
 *
 * @Date 2018-06-10
 */

public class TeacherObject {
    private int idx = 0;
    private String teacherClass = null;     // 교사의 교실
    private String teacherInfo = null;      // 교사의 상태 메세지
    private String teacherName = null;      // 교사의 이름
    private String teacherEmail = null;     // 교사의 이메일

    public TeacherObject(String teacherClass, String teacherInfo, String teacherName, String teacherEmail) {
        this.teacherClass = teacherClass;
        this.teacherInfo = teacherInfo;
        this.teacherName = teacherName;
        this.teacherEmail = teacherEmail;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public int getIdx() {
        return idx;
    }

    public String getTeacherClass() {
        return teacherClass;
    }

    public String getTeacherInfo() {
        return teacherInfo;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public String getTeacherEmail() {
        return teacherEmail;
    }
}
