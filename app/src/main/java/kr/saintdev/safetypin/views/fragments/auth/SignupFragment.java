package kr.saintdev.safetypin.views.fragments.auth;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import kr.saintdev.safetypin.R;
import kr.saintdev.safetypin.models.components.lib.Functions;
import kr.saintdev.safetypin.models.tasks.BackgroundWork;
import kr.saintdev.safetypin.models.tasks.OnBackgroundWorkListener;
import kr.saintdev.safetypin.views.activitys.AuthmeActivity;
import kr.saintdev.safetypin.views.controls.auth.SignupControl;
import kr.saintdev.safetypin.views.fragments.SuperFragment;
import kr.saintdev.safetypin.views.windows.dialog.DialogManager;
import kr.saintdev.safetypin.views.windows.dialog.clicklistener.OnYesClickListener;
import kr.saintdev.safetypin.views.windows.progress.ProgressManager;

/**
 * Copyright (c) 2015-2018 Saint software All rights reserved.
 *
 * @Date 2018-05-08
 */

public class SignupFragment extends SuperFragment {
    AuthmeActivity activity = null;
    SignupControl control = null;
    View v = null;


    public SignupFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragmn_auth_signup, container, false);
        this.activity = (AuthmeActivity) getActivity();
        this.v = v;

        this.control = new SignupControl(this);

        return this.v;
    }

    public View getView() {
        return this.v;
    }

    public AuthmeActivity getControl() {
        return activity;
    }
}

