package com.bitdubai.fermat_dap_android_wallet_redeem_point_bitdubai.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bitdubai.fermat_android_api.layer.definition.wallet.FermatFragment;

/**
 * Created by francisco on 15/09/15.
 */
public class MainFragment extends FermatFragment {
    
    /**
     * UI
     */
    private View rootView;


    public static MainFragment newInstance() {
        return new MainFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(0, container, false);

        return rootView;
    }

}
