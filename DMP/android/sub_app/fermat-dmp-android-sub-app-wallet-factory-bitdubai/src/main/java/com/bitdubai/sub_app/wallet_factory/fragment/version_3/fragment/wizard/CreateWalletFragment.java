package com.bitdubai.sub_app.wallet_factory.fragment.version_3.fragment.wizard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bitdubai.sub_app.wallet_factory.R;

/**
 * Created by francisco on 05/08/15.
 */
public class CreateWalletFragment extends Fragment {

    /**
     * UI
     */
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.wizard_create_wallet_fragment, container, false);

        return rootView;
    }
}
