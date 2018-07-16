package kr.saintdev.safetypin.views.controls.auth;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import kr.saintdev.safetypin.R;
import kr.saintdev.safetypin.models.components.lib.Functions;
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
import kr.saintdev.safetypin.views.fragments.auth.LoginFragment;
import kr.saintdev.safetypin.views.fragments.auth.ProcessAuthFragment;
import kr.saintdev.safetypin.views.windows.dialog.DialogManager;
import kr.saintdev.safetypin.views.windows.dialog.clicklistener.OnYesClickListener;
import kr.saintdev.safetypin.views.windows.progress.ProgressManager;

/**
 * Copyright (c) 2015-2018 Saint software All rights reserved.
 *
 * @Date 2018-06-10
 */

public class LoginControl {
    private LoginFragment fragmn = null;

    private EditText emailEditor = null;
    private EditText passwdEditor = null;

    private DialogManager dm = null;
    private ProgressManager pm = null;

    private OnBackgroundCallback backgroundCallback = null;

    private Context context = null;

    private static final int REQUEST_LOGIN = 0x0;

    public LoginControl(LoginFragment fragmn) {
        this.fragmn = fragmn;

        View v = fragmn.getCurrectView();

        this.emailEditor = v.findViewById(R.id.auth_login_email);
        this.passwdEditor = v.findViewById(R.id.auth_login_passwd);
        Button submitButton = v.findViewById(R.id.auth_select_login);

        // 객체 생성
        this.context = fragmn.getContext();
        this.dm = new DialogManager(fragmn.getCurrectActivity());
        this.pm = new ProgressManager(fragmn.getCurrectActivity());
        this.backgroundCallback = new OnBackgroundCallback();

        // 이벤트 핸들링
        this.dm.setOnYesButtonClickListener(new OnDialogOkClickListener(), "OK");
        submitButton.setOnClickListener(new OnSubmitClickListener());
    }

    /**
     * Submit 버튼을 클릭했습니다.
     */
    class OnSubmitClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String userEmail = emailEditor.getText().toString();
            String userPasswd = passwdEditor.getText().toString();


            if(Functions.checkEmpty(new String[]{ userEmail, userPasswd })) {
                // 빈 값이 없습니다.
                pm.setMessage("환영합니다.");
                pm.enable();

                HashMap<String, Object> args = new HashMap<>();
                args.put("email", userEmail);
                args.put("password", userPasswd);

                HttpRequester requester = new HttpRequester(InternetHostConst.ACCOUNT_LOGIN, args, REQUEST_LOGIN, backgroundCallback, context);
                requester.execute();
            } else {
                // 빈 값이 있습니다.
                showDialog("어딘가 빈 필드가 있습니다.");
            }
        }
    }

    /**
     * Dialog 가 Dismiss 되었습니다.
     */
    class OnDialogOkClickListener implements OnYesClickListener {
        @Override
        public void onClick(DialogInterface dialog) {
            dialog.dismiss();
        }
    }

    /**
     * Background Thread 가 Callback 하였습니다.
     */
    class OnBackgroundCallback implements OnBackgroundWorkListener {
        @Override
        public void onSuccess(int requestCode, BackgroundWork worker) {
            pm.disable();

            try {
                if (requestCode == REQUEST_LOGIN) {
                    HttpResponseObject response = (HttpResponseObject) worker.getResult();

                    if (response.isSuccess()) {
                        // 로그인 성공
                        JSONObject parent = response.getMessage();

                        // 이 장치의 프로필을 업데이트 합니다.
                        MeProfileManager profileManager = MeProfileManager.getInstance(context);
                        MeProfileObject meProfile = new MeProfileObject(
                                parent.getString("session"),
                                parent.getString("name")
                        );
                        profileManager.setProfileObject(meProfile);

                        // 이 부모에 대한 자녀-교사 연관 배열을 업데이트 합니다.
                        JSONArray childArray = parent.getJSONArray("child");
                        JSONArray teacherArray = parent.getJSONArray("teacher");

                        /*
                            자녀 JSONArray 를 ChildObject 컬랙션으로 변환 합니다.
                         */
                        ArrayList<ChildObject> childs = new ArrayList<>();
                        for(int i = 0; i < childArray.length(); i ++) {
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
                        for(int i = 0; i < teacherArray.length(); i ++) {
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
                        SubProfileManager subProfileManager = SubProfileManager.getInstance(context);
                        subProfileManager.resetChild(childs);
                        subProfileManager.resetTeacher(teachers);

                        // 저장 완료. 인증 프레그먼트로 이동합니다.
                        fragmn.getCurrectActivity().switchFragment(new ProcessAuthFragment());
                    } else {
                        // 로그인 실패!
                        showDialog("로그인 할 수 없습니다." + response.getErrorMessage());
                    }
                }
            } catch(JSONException jex) {
                onFailed(requestCode, jex);
            }
        }

        @Override
        public void onFailed(int requestCode, Exception ex) {
            pm.disable();
            showDialog("오류가 발생했습니다.\n" + ex.getMessage());
        }
    }

    private void showDialog(String content) {
        dm.setTitle("알림");
        dm.setDescription(content);
        dm.show();
    }
}
