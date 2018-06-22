package kr.saintdev.safetypin.views.activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import kr.saintdev.safetypin.R;
import kr.saintdev.safetypin.models.components.lib.TileManager;
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
 * Copyright (c) 2015-2018 Saint software All rights reserved.
 *
 * @Date 2018-06-21
 */

public class MainActivity extends AppCompatActivity {
    private TextView helloMessageView = null;
    private GridLayout gridLayout = null;
    private TextView myPinTitle = null;
    private TextView myPinSubTitle = null;

    private TileManager menuTileManager = null;
    private MeProfileManager profileManager = null;
    private DialogManager dm = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        // 객체 찾기
        this.helloMessageView = findViewById(R.id.main_hello_parent);
        this.gridLayout = findViewById(R.id.main_menu_tiles);
        this.myPinTitle = findViewById(R.id.main_pin_mypin);
        this.myPinSubTitle = findViewById(R.id.main_pin_mypin_subtitle);
        this.menuTileManager = new TileManager(this, this.gridLayout);

        this.profileManager = MeProfileManager.getInstance(this);
        this.dm = new DialogManager(this);

        MeProfileObject profileObject = this.profileManager.getProfileObject();
        this.helloMessageView.setText("안녕하세요.\n" + profileObject.getName() + " 학부모 님");

        initMenuTiles();

        // MyPIN 을 불러옵니다.
        HashMap<String, Object> args = new HashMap<>();
        args.put("session", profileObject.getSessionId());
        HttpRequester myPinRequester = new HttpRequester(InternetHostConst.MY_PIN_REQUEST, args, 0x0, new OnBackgroundCallback(), this);
        myPinRequester.execute();
    }

    private void initMenuTiles() {
        OnTileClickListener listener = new OnTileClickListener();
        this.menuTileManager.addTile(R.drawable.ic_fly_blue, R.string.main_menu_send_visit_request, listener, 0);
        this.menuTileManager.addTile(R.drawable.ic_comment_blue, R.string.main_menu_send_message, listener, 1);
        this.menuTileManager.addTile(R.drawable.ic_settings_blue, R.string.main_menu_settings, listener, 2);
        this.menuTileManager.addTile(R.drawable.ic_help_blue, R.string.main_menu_help, listener, 3);
    }

    class OnTileClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = null;

            switch ((int) v.getTag()) {
                case 0:                 // 방문 요청 보내기
                    intent = new Intent(getApplicationContext(), RequestActivity.class);
                    break;
                case 1:                 // 메세지 보내기
                    intent = new Intent(getApplicationContext(), ChatActivity.class);
                    break;
                case 2:                 // 환경 설정
                    break;
                case 3:                 // 도움말
                    break;
            }

            if(intent != null) startActivity(intent);
        }
    }

    class OnBackgroundCallback implements OnBackgroundWorkListener, OnYesClickListener {
        @Override
        public void onSuccess(int requestCode, BackgroundWork worker) {
            if(requestCode == 0x0) {
                HttpResponseObject response = (HttpResponseObject) worker.getResult();

                if(response.isSuccess()) {
                    JSONObject message = response.getMessage();

                    try {
                        if(message.isNull("pin")) {
                            myPinTitle.setText("생성된 PIN 코드가 없습니다.");
                        } else {
                            String pinCode = message.getString("pin");

                            myPinTitle.setText(pinCode);
                        }
                    } catch(JSONException jex) {
                        onFailed(0x0, jex);
                    }
                } else {
                    myPinTitle.setText("앗! 오류가 발생했습니다.");
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
