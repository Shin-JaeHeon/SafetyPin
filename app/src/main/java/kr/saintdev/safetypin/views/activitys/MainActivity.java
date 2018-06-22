package kr.saintdev.safetypin.views.activitys;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import kr.saintdev.safetypin.R;
import kr.saintdev.safetypin.models.components.lib.TileManager;
import kr.saintdev.safetypin.models.datas.profile.MeProfileManager;
import kr.saintdev.safetypin.models.datas.profile.MeProfileObject;

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

        MeProfileObject profileObject = this.profileManager.getProfileObject();
        this.helloMessageView.setText("안녕하세요.\n" + profileObject.getName() + " 학부모 님");

        initMenuTiles();
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
            switch ((int) v.getTag()) {
                case 0:                 // 방문 요청 보내기
                    break;
                case 1:                 // 메세지 보내기
                    break;
                case 2:                 // 환경 설정
                    break;
                case 3:                 // 도움말
                    break;
            }
        }
    }
}
