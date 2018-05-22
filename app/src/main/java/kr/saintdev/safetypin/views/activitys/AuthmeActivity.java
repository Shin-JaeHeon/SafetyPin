package kr.saintdev.safetypin.views.activitys;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import kr.saintdev.safetypin.R;
import kr.saintdev.safetypin.views.fragments.SuperFragment;

/**
 * Created by 5252b on 2018-05-07.
 */

public class AuthmeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authme);
        controlActionBar(null);     // ActionBar 을 숨깁니다.
    }

    public void switchFragment(SuperFragment view) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.anime_visible, R.anim.anime_invisible);
        ft.replace(R.id.authme_container, view);
        ft.commit();

        ft.addToBackStack(null);
    }

    public void controlActionBar(@Nullable String title) {
        ActionBar bar = getSupportActionBar();

        if(bar != null) {
            if (title == null) {
                bar.hide();
            } else {
                bar.show();
                bar.setTitle(title);
            }
        }
    }
}
