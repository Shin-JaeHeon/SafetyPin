package kr.saintdev.safetypin.views.controls.auth;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;

import kr.saintdev.safetypin.R;
import kr.saintdev.safetypin.models.components.lib.Functions;
import kr.saintdev.safetypin.models.datas.InternetHostConst;
import kr.saintdev.safetypin.models.tasks.BackgroundWork;
import kr.saintdev.safetypin.models.tasks.OnBackgroundWorkListener;
import kr.saintdev.safetypin.models.tasks.http.HttpRequester;
import kr.saintdev.safetypin.models.tasks.http.HttpResponseObject;
import kr.saintdev.safetypin.views.fragments.auth.LoginFragment;
import kr.saintdev.safetypin.views.fragments.auth.SignupFragment;
import kr.saintdev.safetypin.views.windows.dialog.DialogManager;
import kr.saintdev.safetypin.views.windows.dialog.clicklistener.OnYesClickListener;
import kr.saintdev.safetypin.views.windows.progress.ProgressManager;

/**
 * Copyright (c) 2015-2018 Saint software All rights reserved.
 *
 * @Date 2018-06-10
 */

public class SignupControl {
    private SignupFragment view = null;
    private Context context = null;

    private EditText nameEditor = null;     // 이름 에디터
    private EditText emailEditor = null;    // 이메일 에디터
    private EditText passwordEditor = null; // 비밀번호 입력 에디터
    private Button submitButton = null;     // 확인 버튼

    private DialogManager dm = null;
    private ProgressManager pm  = null;

    private OnBackgroundCallback backgroundCallback = null;


    private static final int REQUEST_REGISTER = 0x0;


    public SignupControl(SignupFragment view) {
        this.view = view;
        View v = this.view.getView();

        this.nameEditor = v.findViewById(R.id.auth_signup_name);
        this.emailEditor = v.findViewById(R.id.auth_signup_email);
        this.passwordEditor = v.findViewById(R.id.auth_signup_passwd);
        this.submitButton = v.findViewById(R.id.auth_signup_commit);

        this.dm = new DialogManager(view.getControl());
        this.pm = new ProgressManager(view.getControl());
        this.context = this.view.getContext();

        this.backgroundCallback = new OnBackgroundCallback();

        this.submitButton.setOnClickListener(new OnSubmitClickListener());
        this.dm.setOnYesButtonClickListener(new OnDialogOkClickListener(), "OK");
    }


    /**
     * Submit 버튼을 클릭했습니다.
     */
    class OnSubmitClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String userName = nameEditor.getText().toString();
            String userEmail = emailEditor.getText().toString();
            String userPasswd = passwordEditor.getText().toString();


            if(Functions.checkEmpty(new String[]{ userName, userEmail, userPasswd })) {
                // 빈 값이 없습니다.
                pm.setMessage("가입하고 있습니다.");
                pm.enable();

                HashMap<String, Object> args = new HashMap<>();
                args.put("email", userEmail);
                args.put("name", userName);
                args.put("password", userPasswd);

                HttpRequester requester = new HttpRequester(InternetHostConst.ACCOUNT_REGISTER, args, REQUEST_REGISTER, backgroundCallback, context);
                requester.execute();
            } else {
                // 빈 값이 있습니다.
                showDialog("앗...", "어딘가 빈 필드가 있습니다.");
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

            if(requestCode == REQUEST_REGISTER) {
                HttpResponseObject response = (HttpResponseObject) worker.getResult();

                if(response.isSuccess()) {
                    view.getControl().switchFragment(new LoginFragment());
                    Toast.makeText(context, "가입 되었습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    showDialog("알림", "가입에 실패하였습니다.\n" + response.getErrorMessage());
                }
            }
        }

        @Override
        public void onFailed(int requestCode, Exception ex) {
            pm.disable();
            showDialog("알림", "오류가 발생했습니다.\n" + ex.getMessage());
        }
    }

    private void showDialog(String title, String content) {
        dm.setTitle(title);
        dm.setDescription(content);
        dm.show();
    }
}
