package kr.saintdev.safetypin.views.fragments.auth;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kr.saintdev.safetypin.R;
import kr.saintdev.safetypin.views.fragments.SuperFragment;

/**
 * Created by 5252b on 2018-05-07.
 */

public class SelectAuthFragment extends SuperFragment {
    public SelectAuthFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragmn_auth_select, container, false);
        return v;
    }
}
