package kr.saintdev.safetypin.views.fragments.auth;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import kr.saintdev.safetypin.R;
import kr.saintdev.safetypin.views.activitys.AuthmeActivity;
import kr.saintdev.safetypin.views.fragments.SuperFragment;

/**
 * Created by 5252b on 2018-05-07.
 */

public class SelectAuthFragment extends SuperFragment {
    AuthmeActivity control = null;

    Button gotoLoginPage = null;
    Button gotoSignupPage = null;

    public SelectAuthFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragmn_auth_select, container, false);
        this.control = (AuthmeActivity) getActivity();

        this.gotoLoginPage = v.findViewById(R.id.auth_select_login);
        this.gotoSignupPage = v.findViewById(R.id.auth_select_signup);

        OnButtonClickHandler handler = new OnButtonClickHandler();
        this.gotoSignupPage.setOnClickListener(handler);
        this.gotoLoginPage.setOnClickListener(handler);

        return v;
    }

    class OnButtonClickHandler implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.auth_select_login:
                    control.switchFragment(new LoginFragment());
                    break;
                case R.id.auth_select_signup:
                    control.switchFragment(new SignupFragment());
                    break;
            }
        }
    }
}
