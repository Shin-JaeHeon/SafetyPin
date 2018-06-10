package kr.saintdev.safetypin.views.fragments.auth;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kr.saintdev.safetypin.R;
import kr.saintdev.safetypin.views.activitys.AuthmeActivity;
import kr.saintdev.safetypin.views.controls.auth.LoginControl;
import kr.saintdev.safetypin.views.fragments.SuperFragment;

/**
 * Copyright (c) 2015-2018 Saint software All rights reserved.
 *
 * @Date 2018-05-07
 */

public class LoginFragment extends SuperFragment {
    AuthmeActivity activity = null;
    LoginControl control = null;
    View view = null;

    public LoginFragment() {
    }


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragmn_auth_login, container, false);
        this.activity = (AuthmeActivity) getActivity();
        this.control = new LoginControl(this);
        this.view = v;

        return v;
    }

    public AuthmeActivity getCurrectActivity() {
        return activity;
    }

    public LoginControl getControl() {
        return control;
    }

    public View getCurrectView() {
        return view;
    }
}
