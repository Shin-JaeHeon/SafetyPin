package kr.saintdev.safetypin.views.activitys;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import kr.saintdev.safetypin.R;
import kr.saintdev.safetypin.models.datas.InternetHostConst;
import kr.saintdev.safetypin.models.datas.profile.MeProfileManager;
import kr.saintdev.safetypin.models.datas.profile.MeProfileObject;
import kr.saintdev.safetypin.models.datas.subprofile.ChildObject;
import kr.saintdev.safetypin.models.datas.subprofile.SubProfileManager;
import kr.saintdev.safetypin.models.datas.subprofile.TeacherObject;
import kr.saintdev.safetypin.models.tasks.BackgroundWork;
import kr.saintdev.safetypin.models.tasks.OnBackgroundWorkListener;
import kr.saintdev.safetypin.models.tasks.http.HttpRequester;
import kr.saintdev.safetypin.models.tasks.http.HttpResponseObject;
import kr.saintdev.safetypin.views.windows.dialog.DialogManager;
import kr.saintdev.safetypin.views.windows.dialog.clicklistener.OnYesClickListener;

/**
 * Copyright (c) 2018 Shin-JaeHeon All rights reserved.
 *
 * @Date 2018-06-21
 */

public class ChildActivity extends AppCompatActivity {
    private MeProfileManager profileManager = MainActivity.profileManager;
    private EditText child;
    private DialogManager dm = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);
        Objects.requireNonNull(getSupportActionBar()).hide();
        Button requestButton = this.findViewById(R.id.child_add_btn);
        child = findViewById(R.id.child_code);
        requestButton.setOnClickListener(new OnRequestClickListener());
    }

    class OnRequestClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            MeProfileObject profileObject = profileManager.getProfileObject();
            HashMap<String, Object> args = new HashMap<>();
            args.put("session", profileObject.getSessionId());
            args.put("code", child.getText().toString());
            HttpRequester myPinRequester = new HttpRequester(InternetHostConst.CHILD_ADD, args, 0x0, new OnBackgroundCallback(), MainActivity.context);
            myPinRequester.execute();
        }
    }

    /**
     * Background Thread 가 Callback 하였습니다.
     */
    class OnBackgroundCallback2 implements OnBackgroundWorkListener {
        @Override
        public void onSuccess(int requestCode, BackgroundWork worker) {
            try {
                if (requestCode == 0x0) {
                    HttpResponseObject response = (HttpResponseObject) worker.getResult();
                    if (response.isSuccess()) {
                        JSONObject parent = response.getMessage();
                        Log.e("ss",response.getMessage().toString());
                        // 이 부모에 대한 자녀-교사 연관 배열을 업데이트 합니다.
                        JSONArray childArray = parent.getJSONArray("child");
                        JSONArray teacherArray = parent.getJSONArray("teacher");
                        /*
                            자녀 JSONArray 를 ChildObject 컬랙션으로 변환 합니다.
                         */
                        ArrayList<ChildObject> childs = new ArrayList<>();
                        for (int i = 0; i < childArray.length(); i++) {
                            JSONObject child = (JSONObject) childArray.get(i);  // 자녀 하나를 가져와서
                            ChildObject childObj = new ChildObject(     // ArrayList 으로 변경 합니다.
                                    child.getString("st_name"),
                                    child.getString("st_num"),
                                    child.getString("code")
                            );

                            childs.add(childObj);
                        }
                        /*
                            교사 JSONArray 를 TeacherObject 컬랙션으로 변환 합니다.
                         */
                        ArrayList<TeacherObject> teachers = new ArrayList<>();
                        for (int i = 0; i < teacherArray.length(); i++) {
                            JSONObject teacher = teacherArray.getJSONObject(i);

                            TeacherObject teacherObj = new TeacherObject(
                                    teacher.getString("class"),
                                    teacher.getString("info"),
                                    teacher.getString("name"),
                                    teacher.getString("email")
                            );
                            teachers.add(teacherObj);
                        }
                        // 자녀 - 교사 연관 배열을 저장합니다.
                        SubProfileManager subProfileManager = SubProfileManager.getInstance(MainActivity.context);
                        subProfileManager.resetChild(childs);
                        subProfileManager.resetTeacher(teachers);
                    }
                }
            } catch (JSONException jex) {
                onFailed(requestCode, jex);
            }
        }

        @Override
        public void onFailed(int requestCode, Exception ex) {
            ex.printStackTrace();
        }
    }

    class OnBackgroundCallback implements OnBackgroundWorkListener, OnYesClickListener {
        @Override
        public void onSuccess(int requestCode, BackgroundWork worker) {
            if (requestCode == 0x0) {
                HttpResponseObject response = (HttpResponseObject) worker.getResult();
                if (response.isSuccess()) {
                    Toast.makeText(MainActivity.context, "자녀를 등록하였습니다", Toast.LENGTH_LONG).show();
                    MeProfileObject profileObject = profileManager.getProfileObject();
                    HashMap<String, Object> args2 = new HashMap<>();
                    args2.put("session", profileObject.getSessionId());
                    HttpRequester requester = new HttpRequester(InternetHostConst.ACCOUNT_LOAD, args2, 0x0, new OnBackgroundCallback2(), MainActivity.context);
                    requester.execute();
                    finish();
                } else {
                    Toast.makeText(MainActivity.context, "자녀 등록에 실패하였습니다", Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        public void onFailed(int requestCode, Exception ex) {
            dm.setTitle("Exception!");
            dm.setDescription("An error occurred.\n" + ex.getMessage());
            dm.setOnYesButtonClickListener(this, "OK");
            dm.show();
        }

        @Override
        public void onClick(DialogInterface dialog) {
            dialog.dismiss();
        }
    }
}
