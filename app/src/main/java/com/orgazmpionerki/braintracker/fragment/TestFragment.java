package com.orgazmpionerki.braintracker.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;

import com.braintracker.R;
import com.orgazmpionerki.braintracker.canvas.animator.BrainProgressAnimator;
import com.orgazmpionerki.braintracker.canvas.view.BrainProgressView;

/**
 * Created by Dmitriy on 29.06.2015.
 */
public class TestFragment extends BaseFragment {
    public static final String TAG = "com.braintracker.fargment.test_fragment";

    private boolean mFlag = false;

    public static TestFragment newInstance() {
        return new TestFragment();
    }

    public TestFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.test_fragment, null, true);
        layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));


        final TextSwitcher textSwitcher = (TextSwitcher) layout.findViewById(R.id.text_switcher);
        textSwitcher.setInAnimation(getActivity(), R.anim.text_slide_in_top);
        textSwitcher.setOutAnimation(getActivity(), R.anim.text_slide_out_bot);
        final String start = getResources().getString(R.string.start_service_button);
        final String stop = getResources().getString(R.string.stop_service_button);
        textSwitcher.setText(mFlag ? start : stop);

        final BrainProgressView progressView = (BrainProgressView) layout.findViewById(R.id.brain_progress);
        final BrainProgressAnimator brainProgressAnimator = new BrainProgressAnimator(progressView, 70, -10, 100);

        Button testButton = (Button) layout.findViewById(R.id.test_button);

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textSwitcher.setText(mFlag ? start : stop);
                brainProgressAnimator.start();
                mFlag = !mFlag;
            }
        });

        setRetainInstance(true);
        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void updateContent(Bundle args) {
    }

    @Override
    public String getFragmentTag() {
        return TAG;
    }
}