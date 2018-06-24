package kr.saintdev.safetypin.views.activitys;

import android.content.Context;
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
import java.util.Objects;

import kr.saintdev.safetypin.R;
import kr.saintdev.safetypin.models.components.lib.TileManager;
import kr.saintdev.safetypin.models.datas.InternetHostConst;
import kr.saintdev.safetypin.models.datas.profile.MeProfileManager;
import kr.saintdev.safetypin.models.datas.profile.MeProfileObject;
import kr.saintdev.safetypin.models.datas.subprofile.SubProfileManager;
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
    private TextView myPinTitle = null;

    public TileManager menuTileManager = null;
    public static MeProfileManager profileManager = null;
    public static SubProfileManager subProfileManager = null;
    private DialogManager dm = null;
    public static Context context;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();

        // 객체 찾기
        context = this;
        TextView helloMessageView = findViewById(R.id.main_hello_parent);
        GridLayout gridLayout = findViewById(R.id.main_menu_tiles);
        this.myPinTitle = findViewById(R.id.main_pin_mypin);
        this.menuTileManager = new TileManager(this, gridLayout);

        profileManager = MeProfileManager.getInstance(this);
        subProfileManager = SubProfileManager.getInstance(this);
        this.dm = new DialogManager(this);

        MeProfileObject profileObject = profileManager.getProfileObject();
        helloMessageView.setText(String.format("안녕하세요.\n%s 학부모 님", profileObject.getName()));

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
                case 2:                 // 자녀 등록
                    intent = new Intent(getApplicationContext(), ChildActivity.class);
                    break;
                case 3:                 // 도움말
                    break;
            }

            if (intent != null) startActivity(intent);
        }
    }

    class OnBackgroundCallback implements OnBackgroundWorkListener, OnYesClickListener {
        @Override
        public void onSuccess(int requestCode, BackgroundWork worker) {
            if (requestCode == 0x0) {
                HttpResponseObject response = (HttpResponseObject) worker.getResult();

                if (response.isSuccess()) {
                    JSONObject message = response.getMessage();

                    try {
                        if (message.isNull("pin")) {
                            myPinTitle.setText(R.string.no_pin);
                        } else {
                            String pinCode = message.getString("pin");

                            myPinTitle.setText(pinCode);
                        }
                    } catch (JSONException jex) {
                        onFailed(0x0, jex);
                    }
                } else {
                    myPinTitle.setText(R.string.no_pin);
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
