package com.bitdubai.fermat_android_api.layer.definition.wallet;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

import com.bitdubai.fermat_android_api.layer.definition.wallet.interfaces.SubAppsSession;
import com.bitdubai.fermat_android_api.layer.definition.wallet.interfaces.WizardConfiguration;
import com.bitdubai.fermat_android_api.ui.inflater.ViewInflater;
import com.bitdubai.fermat_android_api.ui.interfaces.FermatFragments;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.Activities;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.WizardTypes;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.interfaces.FermatScreenSwapper;
import com.bitdubai.fermat_api.layer.dmp_middleware.wallet_settings.interfaces.SubAppSettings;
import com.bitdubai.fermat_pip_api.layer.pip_network_service.subapp_resources.SubAppResourcesProviderManager;

import java.util.Objects;

/**
 * Common Android Fragment Base
 *
 * @author Matias Furszy
 * @author Francisco Vasquez
 * @version 1.1
 */
public abstract class FermatFragment extends Fragment implements FermatFragments {

    /**
     * FLAGS
     */
    protected boolean isAttached;

    /**
     * Platform
     */
    protected SubAppsSession subAppsSession;
    protected SubAppSettings subAppSettings;
    protected SubAppResourcesProviderManager subAppResourcesProviderManager;

    /**
     * ViewInflater
     */
    protected ViewInflater viewInflater;

    /**
     * REFERENCES
     */
    protected WizardConfiguration context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            context = (WizardConfiguration) getActivity();
        } catch (Exception ex) {
            throw new ClassCastException("cannot convert the current context to WizardConfiguration");
        }
    }

    /**
     * Start a configuration Wizard
     *
     * @param key  Enum Wizard registered type
     * @param args Object[] where you're be able to passing arguments like session, settings, resources, module, etc...
     */
    protected void startWizard(WizardTypes key, Object... args) {
        if (context != null && isAttached) {
            context.showWizard(key, args);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        isAttached = true;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        isAttached = false;
    }

    /**
     * Setting up SubApp Session
     *
     * @param subAppsSession SubAppSession object
     */
    public void setSubAppsSession(SubAppsSession subAppsSession) {
        this.subAppsSession = subAppsSession;
    }

    /**
     * Setting up SubAppSettings
     *
     * @param subAppSettings SubAppSettings object
     */
    public void setSubAppSettings(SubAppSettings subAppSettings) {
        this.subAppSettings = subAppSettings;
    }

    /**
     * Setting up SubAppResourcesProviderManager
     *
     * @param subAppResourcesProviderManager SubAppResourcesProviderManager object
     */
    public void setSubAppResourcesProviderManager(SubAppResourcesProviderManager subAppResourcesProviderManager) {
        this.subAppResourcesProviderManager = subAppResourcesProviderManager;
    }

    /**
     * Change activity
     */
    protected final void changeActivity(String activityCode,Object... objectses) {
        ((FermatScreenSwapper) getActivity()).changeActivity(activityCode,objectses);
    }

    /**
     * Change activity
     */
    protected final void changeFragment(String fragment) {
        ((FermatScreenSwapper) getActivity()).changeScreen(fragment, null);
    }


}

