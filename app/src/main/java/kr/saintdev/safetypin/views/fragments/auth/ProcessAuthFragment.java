package kr.saintdev.safetypin.views.fragments.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kr.saintdev.safetypin.R;
import kr.saintdev.safetypin.models.datas.profile.MeProfileManager;
import kr.saintdev.safetypin.models.datas.profile.MeProfileObject;
import kr.saintdev.safetypin.views.activitys.AuthmeActivity;
import kr.saintdev.safetypin.views.activitys.MainActivity;
import kr.saintdev.safetypin.views.fragments.SuperFragment;

/**
 * Created by 5252b on 2018-05-07.
 */

public class ProcessAuthFragment extends SuperFragment {
    AuthmeActivity control = null;

    public ProcessAuthFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragmn_auth_process, container, false);
        this.control = (AuthmeActivity) getActivity();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 여기서 가입된 계정인지 아닌지
        // 확인하고 자동 로그인 등의 처리를 진행합니다.
        MeProfileManager profileManager = MeProfileManager.getInstance(getContext());
        MeProfileObject profileObject = profileManager.getProfileObject();

        if(profileObject == null) {
            // 로그인 화면으로 이동한다.
            this.control.switchFragment(new SelectAuthFragment());
        } else {
            // 메인 화면으로 이동합니다.
            startActivity(new Intent(getContext(), MainActivity.class));
            this.control.finish();
        }
    }
}
