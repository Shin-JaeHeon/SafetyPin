package kr.saintdev.safetypin.views.activitys;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

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
import kr.saintdev.safetypin.views.windows.dialog.DialogManager;
import kr.saintdev.safetypin.views.windows.dialog.clicklistener.OnYesClickListener;

/**
 * Copyright (c) 2018 Shin-JaeHeon All rights reserved.
 *
 * @Date 2018-06-21
 */

public class RequestActivity extends AppCompatActivity {
    private MeProfileManager profileManager =MainActivity.profileManager;
    private SubProfileManager subProfileManager = MainActivity.subProfileManager;
    private DialogManager dm = null;
    private Button requestButton = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reqeust);
        getSupportActionBar().hide();
        this.requestButton = this.findViewById(R.id.request_btn);
        this.requestButton.setOnClickListener(new OnRequestClickListener());
    }

    class OnRequestClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            MeProfileObject profileObject = profileManager.getProfileObject();
            ChildObject childObject = subProfileManager.getChilds().get(0);
            TeacherObject teacherObject = subProfileManager.getTeachers().get(0);
            HashMap<String, Object> args = new HashMap<>();
            args.put("session", profileObject.getSessionId());
            args.put("teacher", teacherObject.getTeacherEmail());
            args.put("id", childObject.getChildCode());
            HttpRequester myPinRequester = new HttpRequester(InternetHostConst.PIN_REQUEST, args, 0x0, new OnBackgroundCallback(), MainActivity.context);
            myPinRequester.execute();
        }
    }

    class OnBackgroundCallback implements OnBackgroundWorkListener, OnYesClickListener {
        @Override
        public void onSuccess(int requestCode, BackgroundWork worker) {
            if (requestCode == 0x0) {
                HttpResponseObject response = (HttpResponseObject) worker.getResult();

                if (response.isSuccess()) {
                    Toast.makeText(MainActivity.context, "선생님께 요청을 보냈습니다.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.context, "요청 보내기에 실패하였습니다.", Toast.LENGTH_LONG).show();
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
