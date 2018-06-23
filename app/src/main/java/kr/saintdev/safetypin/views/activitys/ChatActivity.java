package kr.saintdev.safetypin.views.activitys;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

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
 * Copyright (c) 2015-2018 Saint software All rights reserved.
 *
 * @Date 2018-06-22
 */

public class ChatActivity extends AppCompatActivity {
    private MeProfileManager profileManager = MainActivity.profileManager;
    private SubProfileManager subProfileManager = MainActivity.subProfileManager;
    private MeProfileObject profileObject = profileManager.getProfileObject();
    private ChildObject childObject = subProfileManager.getChilds().get(0);
    private TeacherObject teacherObject = subProfileManager.getTeachers().get(0);
    private Button chat_send;
    private EditText text_input;
    private ListView chat_lv;
    private DialogManager dm = null;
    private ArrayAdapter<String> arrayAdapter;
    private String myChat = "";
    private HashMap<String, Object> args = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Objects.requireNonNull(getSupportActionBar()).hide();
        chat_send = findViewById(R.id.chat_send);
        text_input = findViewById(R.id.text_input);
        chat_lv = findViewById(R.id.chat_lv);
        chat_send.setOnClickListener(new OnChatSend());
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        chat_lv.setAdapter(arrayAdapter);
        chat_lv.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        args.put("session", profileObject.getSessionId());
        args.put("id", childObject.getChildCode());
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                HttpRequester myPinRequester = new HttpRequester(InternetHostConst.CHAT_LOAD, args, 0x0, new OnBackgroundCallback2(), MainActivity.context);
                myPinRequester.execute();
            }
        };
        timer.schedule(timerTask, 0, 5000);
    }

    class OnChatSend implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String msg = text_input.getText().toString();
            HashMap<String, Object> args = new HashMap<>();
            args.put("session", profileObject.getSessionId());
            args.put("teacher", teacherObject.getTeacherEmail());
            args.put("id", childObject.getChildCode());
            args.put("chat", msg);
            myChat = msg;
            HttpRequester myPinRequester = new HttpRequester(InternetHostConst.CHAT_SEND, args, 0x0, new OnBackgroundCallback(), MainActivity.context);
            myPinRequester.execute();

        }
    }

    class OnBackgroundCallback implements OnBackgroundWorkListener, OnYesClickListener {
        @Override
        public void onSuccess(int requestCode, BackgroundWork worker) {
            if (requestCode == 0x0) {
                HttpResponseObject response = (HttpResponseObject) worker.getResult();
                if (response.isSuccess()) {
                    arrayAdapter.add(profileObject.getName() + " : " + myChat);
                    myChat = "";
                } else {
                    Toast.makeText(MainActivity.context, "채팅 전송에 실패하였습니다.", Toast.LENGTH_LONG).show();
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

    class OnBackgroundCallback2 implements OnBackgroundWorkListener, OnYesClickListener {
        @Override
        public void onSuccess(int requestCode, BackgroundWork worker) {
            if (requestCode == 0x0) {
                HttpResponseObject response = (HttpResponseObject) worker.getResult();
                if (response.isSuccess()) {
                    JSONObject jsonObject = response.getMessage();
                    try {
                        arrayAdapter.clear();
                        JSONArray jsonArray = jsonObject.getJSONArray("chat");
                        int len = jsonArray.length();
                        for (int i = 0; i < len; i++) {
                            JSONObject chatObj = (JSONObject) jsonArray.get(i);
                            String msg = chatObj.getString("chat");
                            String name = chatObj.getInt("direction") == 0 ? profileObject.getName() : teacherObject.getTeacherName();
                            arrayAdapter.add(name + " : " + msg);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(MainActivity.context, "채팅 목록 불러오기가 실패하였습니다.", Toast.LENGTH_LONG).show();
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
