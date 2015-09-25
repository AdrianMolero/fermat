package com.bitdubai.fermat_dap_android_sub_app_asset_factory_bitdubai.sessions;

import com.bitdubai.fermat_android_api.layer.definition.wallet.interfaces.SubAppsSession;
import com.bitdubai.fermat_api.layer.ccp_engine.sub_app_runtime.enums.SubApps;
import com.bitdubai.fermat_dap_api.layer.dap_module.asset_factory.interfaces.AssetFactoryModuleManager;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.error_manager.ErrorManager;

import java.util.HashMap;
import java.util.Map;

/**
 * AssetIssuer SubApp Session
 *
 * @author Francisco Vasquez
 * @version 1.0
 */
public class AssetFactorySession implements SubAppsSession {

    /**
     * Issuer Manager
     */
    private AssetFactoryModuleManager manager;
    /**
     * SubApps type
     */
    private SubApps subApps;

    /**
     * Active objects in wallet session
     */
    private Map<String, Object> data;

    /**
     * Error manager
     */
    private ErrorManager errorManager;

    /**
     * Constructor
     *
     * @param subApps      SubApp Type
     * @param errorManager Error Manager
     * @param manager      AssetIssuerWallet Manager
     */
    public AssetFactorySession(SubApps subApps, ErrorManager errorManager, AssetFactoryModuleManager manager) {
        this.subApps = subApps;
        data = new HashMap<String, Object>();
        this.errorManager = errorManager;
        this.manager = manager;
    }


    @Override
    public SubApps getSubAppSessionType() {
        return subApps;
    }

    @Override
    public void setData(String key, Object object) {
        data.put(key, object);
    }

    @Override
    public Object getData(String key) {
        return data.get(key);
    }

    @Override
    public ErrorManager getErrorManager() {
        return errorManager;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AssetFactorySession that = (AssetFactorySession) o;

        return subApps == that.subApps;

    }

    @Override
    public int hashCode() {
        return subApps.hashCode();
    }

    /**
     * Get Asset Issuer Wallet Manager instance
     *
     * @return AssetIssuerWalletManager object
     */
    public AssetFactoryModuleManager getManager() {
        return manager;
    }
}
