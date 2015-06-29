package com.orgazmpionerki.braintracker.dialog;

import android.app.DialogFragment;

/**
 * Created by Dmitriy on 29.06.2015.
 */
public abstract class BaseDialog extends DialogFragment {

    public BaseDialog() {
        super();
    }

    public abstract String getDialogTag();
}
