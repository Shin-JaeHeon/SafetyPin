package kr.saintdev.safetypin.views.activitys;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Objects;

import kr.saintdev.safetypin.R;
import kr.saintdev.safetypin.models.datas.InternetHostConst;
import kr.saintdev.safetypin.models.datas.profile.MeProfileManager;
import kr.saintdev.safetypin.models.datas.profile.MeProfileObject;
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
        setContentView(R.layout.activity_reqeust);
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
            args.put("id", child.getText().toString());
            HttpRequester myPinRequester = new HttpRequester(InternetHostConst.CHILD_ADD, args, 0x0, new OnBackgroundCallback(), MainActivity.context);
            myPinRequester.execute();
        }
    }

    class OnBackgroundCallback implements OnBackgroundWorkListener, OnYesClickListener {
        @Override
        public void onSuccess(int requestCode, BackgroundWork worker) {
            if (requestCode == 0x0) {
                HttpResponseObject response = (HttpResponseObject) worker.getResult();
                if (response.isSuccess()) {
                    Toast.makeText(MainActivity.context, "자녀를 등록하였습니다", Toast.LENGTH_LONG).show();
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
