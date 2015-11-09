package com.orgazmpionerki.braintracker.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.braintracker.R;
import com.orgazmpionerki.braintracker.activity.WelcomeActivity;
import com.orgazmpionerki.braintracker.auth.AuthActivity;

/**
 * Created by Dmitriy on 08.11.2015.
 */
public class WelcomeFragment extends BaseFragment {
    private static final String TAG = "com.orgazmpionerki.braintracker.fragment.welcome_fragment";

    public static WelcomeFragment newInstance() {
        return new WelcomeFragment();
    }

    public WelcomeFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.f_welcome, container, false);

        Button button = (Button) layout.findViewById(R.id.btn_go);
        button.setOnClickListener(v -> {
            getActivity().startActivityForResult(new Intent(getActivity(), AuthActivity.class), WelcomeActivity.AUTH_REQUEST);
        });

        setRetainInstance(false);
        return layout;
    }

    @Override
    public String getFragmentTag() {
        return TAG;
    }
}
