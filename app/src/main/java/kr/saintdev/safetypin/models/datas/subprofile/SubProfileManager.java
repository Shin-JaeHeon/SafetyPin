package kr.saintdev.safetypin.models.datas.subprofile;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;

import kr.saintdev.safetypin.models.datas.dbm.DBHelper;

/**
 * Copyright (c) 2015-2018 Saint software All rights reserved.
 *
 * @Date 2018-06-10
 */

public class SubProfileManager {
    private static SubProfileManager instance = null;
    DBHelper dbHelper = null;

    public static SubProfileManager getInstance(Context context) {
        if(SubProfileManager.instance == null) {
            SubProfileManager.instance = new SubProfileManager(context);
        }

        return SubProfileManager.instance;
    }

    private SubProfileManager(Context context) {
        this.dbHelper = new DBHelper(context);
        this.dbHelper.open();
    }

    public ArrayList<TeacherObject> getTeachers() {
        Cursor cs = this.dbHelper.sendReadableQuery("SELECT * FROM tb_child_teacher");
        ArrayList<TeacherObject> teacherArray = new ArrayList<>();

        while(cs.moveToNext()) {
            TeacherObject teacher = new TeacherObject(
                    cs.getString(1),
                    cs.getString(2),
                    cs.getString(3),
                    cs.getString(4)
            );

            teacherArray.add(teacher);
        }

        return teacherArray;
    }

    public ArrayList<ChildObject> getChilds() {
        Cursor cs = this.dbHelper.sendReadableQuery("SELECT * FROM tb_parent_childs");
        ArrayList<ChildObject> childArray = new ArrayList<>();

        while(cs.moveToNext()) {
            ChildObject child = new ChildObject(
                    cs.getString(1),
                    cs.getString(2)
            );

            childArray.add(child);
        }

        return childArray;
    }

    public void resetTeacher(ArrayList<TeacherObject> teachers) {
        this.dbHelper.sendWriteableQuery("DELETE FROM tb_child_teacher");   // 모든 행 삭제
        SQLiteDatabase db = dbHelper.getWriteDB();

        for(int i = 0; i < teachers.size(); i ++) {
            TeacherObject teacher = teachers.get(i);

            SQLiteStatement pst = db.compileStatement("INSERT INTO tb_child_teacher (teacher_class, teacher_info, teacher_name, teacher_email) VALUES(?,?,?,?)");
            pst.bindString(1, teacher.getTeacherClass());
            pst.bindString(2, teacher.getTeacherInfo());
            pst.bindString(3, teacher.getTeacherName());
            pst.bindString(4, teacher.getTeacherEmail());
            pst.execute();
        }
    }

    public void resetChild(ArrayList<ChildObject> childs) {
        this.dbHelper.sendWriteableQuery("DELETE FROM tb_parent_childs");   // 모든 행 삭제
        SQLiteDatabase db = dbHelper.getWriteDB();

        for(int i = 0; i < childs.size(); i ++) {
            ChildObject child = childs.get(i);

            SQLiteStatement pst = db.compileStatement("INSERT INTO tb_parent_childs (child_name, child_num) VALUES(?,?)");
            pst.bindString(1, child.getChildName());
            pst.bindString(2, child.getChildNum());
            pst.execute();
        }
    }
}
