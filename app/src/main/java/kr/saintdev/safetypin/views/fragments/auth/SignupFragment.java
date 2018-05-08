package kr.saintdev.safetypin.views.fragments.auth;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kr.saintdev.safetypin.R;
import kr.saintdev.safetypin.views.activitys.AuthmeActivity;
import kr.saintdev.safetypin.views.fragments.SuperFragment;

/**
 * Copyright (c) 2015-2018 Saint software All rights reserved.
 *
 * @Date 2018-05-08
 */

public class SignupFragment extends SuperFragment {
    AuthmeActivity control = null;

    public SignupFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragmn_auth_signup, container, false);
        this.control = (AuthmeActivity) getActivity();


        return v;
    }
}
