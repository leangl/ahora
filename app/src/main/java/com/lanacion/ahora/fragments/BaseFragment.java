package com.lanacion.ahora.fragments;

import android.support.v4.app.Fragment;

import com.lanacion.ahora.activities.BaseActivity;

import roboguice.fragment.RoboFragment;

/**
 * Created by Ignacio Saslavsky on 10/04/15.
 * correonano@gmail.com
 */
public class BaseFragment extends RoboFragment {
    public void start(Fragment fragment) {
        ((BaseActivity) getActivity()).start(fragment, true);
    }

    protected void stopLoading() {
        if (getBaseActivity() != null) getBaseActivity().stopLoading();
    }

    protected void showLoading() {
        showLoading(true);
    }

    protected void showLoading(boolean cancelable) {
        if (getBaseActivity() != null) getBaseActivity().showLoading(cancelable);
    }

    protected BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }
}
