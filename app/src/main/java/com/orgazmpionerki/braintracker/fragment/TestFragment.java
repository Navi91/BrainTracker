package com.orgazmpionerki.braintracker.fragment;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;

import com.braintracker.R;
import com.crashlytics.android.core.CrashlyticsCore;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;
import com.orgazmpionerki.braintracker.canvas.animator.BrainProgressAnimator;
import com.orgazmpionerki.braintracker.canvas.view.BrainProgressView;
import com.orgazmpionerki.braintracker.database.BrainTrackerDatabase;
import com.orgazmpionerki.braintracker.dataprovider.datacontent.IDataElement;
import com.orgazmpionerki.braintracker.util.Tracer;
import com.orgazmpionerki.braintracker.wear.WearController;

import java.util.List;

/**
 * Created by Dmitriy on 29.06.2015.
 */
public class TestFragment extends BaseFragment {
    public static final String TAG = "com.braintracker.fargment.test_fragment";

    private boolean mFlag = false;
    private WearController mWearController;

    public static TestFragment newInstance() {
        return new TestFragment();
    }

    public TestFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mWearController = new WearController(getActivity());
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

        Button testButton = (Button) layout.findViewById(R.id.test_button);

        testButton.setOnClickListener(view -> {
            mFlag = !mFlag;
//            testWearConnection();
            getAccounts();
        });


        setRetainInstance(true);
        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        mWearController.connect();
    }

    @Override
    public void onPause() {
        super.onPause();
        mWearController.disconnect();
    }

    @Override
    public void updateContent(Bundle args) {
    }

    private void testWearConnection() {
        BrainTrackerDatabase database = new BrainTrackerDatabase(getActivity());
        database.open();

        mWearController.notifyPointsChanged(database.getBrainPoints(1));

        database.close();
    }

    private final String mGoogleAccountType = "com.google";

    private void getAccounts() {
        AccountManager accountManager = AccountManager.get(getActivity());
        Account[] accounts = accountManager.getAccountsByType(mGoogleAccountType);

        Account googleAccount = null;
        for (Account account : accounts) {
            Tracer.debug("account_debug", account.name);
            googleAccount = account;
        }

        if (googleAccount != null) {

        }
    }

    @Override
    public String getFragmentTag() {
        return TAG;
    }
}